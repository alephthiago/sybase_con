package com.bcf.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bcf.beans.Alarm;
import com.bcf.conn.DBManager;
import com.bcf.properties.PropertySingleton;
import com.bcf.util.Format;

public class SybaseDAO {

	private static Logger log = LoggerFactory.getLogger(SybaseDAO.class);

	private Connection conn;

	private List<Alarm> lista;

	PreparedStatement ps = null;

	private int index;

	private ResultSet rs;

	String table1 = PropertySingleton.getInstance().getProperty("table1");

	private void getConnectionSyBase() throws SQLException {
		this.conn = DBManager.getConnectionSybase();
	}

	private void getConnectionOracle() {
		this.conn = DBManager.getConnectionOracle();
	}

	private void closeConnection() throws Exception {
		this.conn.close();
	}

	public BigDecimal searchLat(String DWCXUSR1) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BigDecimal lat = null;

		try {
			String sql = "Select DWCDCOGX from DWATPDI where DWCODES1 = ? and DWTPPOI = 500";
			conn = DBManager.getConnectionOracle();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, DWCXUSR1);
			rs = pstmt.executeQuery();
				
			if (rs.next()) {
				lat = rs.getBigDecimal(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return lat;
	}

	public BigDecimal searchLong(String DWCXUSR1) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BigDecimal lon = null;

		try {
			String sql = "Select DWCDCOGY from DWATPDI where DWCODES1 = ? and DWTPPOI = 500";
			conn = DBManager.getConnectionOracle();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, DWCXUSR1);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				lon = rs.getBigDecimal(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if (pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return lon;
	}

	public List<Alarm> executeReader() {
		try {
			getConnectionSyBase();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Alarm alarm = new Alarm();
		lista = new ArrayList<Alarm>();

		try {

			conn = DBManager.getConnectionSybase();

			Statement stmt = conn.createStatement();
			String sql = "SELECT Summary, Node, NodeAlias, Severity , Identifier, Manager, FirstOccurrence, StateChange, LastOccurrence, Class"
					+ "  FROM alerts.status where Class = 10501 AND Severity IN (4 , 5) AND Node IN ('7210','0706','6878','7414')";
			// 706 (ex 0706), 6878, 7414, 7210
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				alarm = new Alarm();
				alarm.setDWDSINFO(rs.getString("Summary"));// Summary
				alarm.setDWCXUSR1(rs.getString("Node"));// Node
				alarm.setDWDSINDI(rs.getString("NodeAlias"));// NodeAlias
				alarm.setDWCDUSR0(rs.getInt("Severity"));// Severity
				alarm.setDWDSUSR3(rs.getString("Identifier"));// Identifier
				alarm.setDWDSUSR4(rs.getString("Manager"));// Manager
				alarm.setDWDSUSR5(rs.getString("Summary"));// Summary
				alarm.setDWDTUSR0(rs.getString("FirstOccurrence"));// FirstOccurrence
				alarm.setDWDTUSR1(rs.getString("StateChange"));// StateChange
				alarm.setDWDTUSR2(rs.getString("LastOccurrence"));// LastOccurrence
				alarm.setDWCXCLA0(rs.getString("FirstOccurrence"));// FirstOccurrence
				alarm.setDWCXCLA1(rs.getString("StateChange"));// StateChange
				alarm.setDWCXCLA2(rs.getString("LastOccurrence"));// LastOccurrence
				alarm.setDWCXCLA6(rs.getString("Class"));// Class
				alarm.setDWCXUSR2("SMI");
				alarm.setDWFLUSR0("S");
				alarm.setDWFLUSR1("1");
				alarm.setDWTPPOI("115");

				System.out.println("Alarme novo :" + alarm.getDWCXUSR1());
				lista.add(alarm);
			}
			stmt.close();
			rs.close();
			closeConnection();
		} catch (SQLException sq) {
			sq.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Numero de registros trazidos do sybase : "
				+ lista.size());
		return lista;
	}

	public void executeWritter(List<Alarm> lista) {
		getConnectionOracle();
		index = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String cleaner = "DELETE FROM " + table1
					+ " where DWCXUSR2 = 'SMI'";
			pstmt = conn.prepareStatement(cleaner);
			pstmt.execute(cleaner);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		try {
			System.out.println("excute Writer ....... ");

			String alterSession = "alter session set nls_date_format = 'DD-MM-YYYY HH24:MI:SS'";
			pstmt = conn.prepareStatement(alterSession);
			pstmt.execute();
			pstmt.close();

			for (int i = 0; i < lista.size(); i++) {

				try {
					System.out.println("excute Writer ....... " + i);

					String sql = "INSERT INTO "
							+ table1
							+ " (DWCDCLIE,DWPRINDI,DWCODPRO,DWCODES1,DWCDCOGX,DWCDCOGY,DWTPPOI,DWTPINDI,DWDSRAGS,DWDSPRES,DWDSINDI,DWCXCIVN,DWDSESPN,DWSGPROV,DWDSCOMU,DWDSLOCA,DWCXCAP,DWCXSTAT,DWCXTEL0,DWCXTEL1,"
							+ "DWCXTEL2,DWCXFAX,DWCXURL,DWCXEMAI,DWDSINFO,DWCXICON,DWCXMIZO,DWCXCOMU,DWCXPROV,DWCXREGI,DWCXNIEL,DWCXCAPZ,DWCXPAD1,DWCXPAD2,DWCXPAD3,DWCXSTSG,DWCXFISC,DWCDPIVA,DWCXFORG,DWCXCCIA,DWSPCCIA,DWCDSTSO,DWCXUSR0,"
							+ "DWCXUSR1,DWCXUSR2,DWCXUSR3,DWCXUSR4,DWCXUSR5,DWCDUSR0,DWCDUSR1,DWCDUSR2,DWCDUSR3,DWCDUSR4,DWCDUSR5,DWDSUSR0,DWDSUSR1,DWDSUSR2,DWDSUSR3,DWDSUSR4,DWDSUSR5,DWVLUSR0,DWVLUSR1,DWVLUSR2,DWVLUSR3,DWVLUSR4,DWVLUSR5,"
							+ "DWDTUSR0,DWDTUSR1,DWDTUSR2,DWDTUSR3,DWDTUSR4,DWDTUSR5,DWFLUSR0,DWFLUSR1,DWFLUSR2,DWFLUSR3,DWFLUSR4,DWFLUSR5,DWCXCLA0,DWCXCLA1,DWCXCLA2,DWCXCLA3,DWCXCLA4,DWCXCLA5,DWCXCLA6,DWDSCLA0,DWDSCLA1,DWDSCLA2,DWDSCLA3,"
							+ "DWDSCLA4,DWDSCLA5,DWDSCLA6,DWCDPROP,DWCDLIV1,DWCDLIV2,DWDTISTI,DWDTAGGT,DWDTVALD,DWDTVALA,DWDTANNU,DWDTCANC,DWFLATTI,DWCDUTEN,DWPRUKEY)"
							+ " values"
							+ "(POISEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
							+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,?,?,"
							+ "?,?,?,?)";

					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, 0);
					pstmt.setInt(2, 0);
					pstmt.setString(3, " ");
					pstmt.setBigDecimal(4, lista.get(i).getDWCDCOGX());
					pstmt.setBigDecimal(5, lista.get(i).getDWCDCOGY());
					pstmt.setString(6, lista.get(i).getDWTPPOI());
					pstmt.setInt(7, 0);
					pstmt.setString(8, " ");
					pstmt.setString(9, " ");
					// 10
					pstmt.setString(10, lista.get(i).getDWDSINDI());
					pstmt.setString(11, " ");
					pstmt.setString(12, " ");
					pstmt.setString(13, " ");
					pstmt.setString(14, " ");
					pstmt.setString(15, " ");
					pstmt.setString(16, " ");
					pstmt.setString(17, " ");
					pstmt.setString(18, " ");
					pstmt.setString(19, " ");
					// 20
					pstmt.setString(20, " ");
					pstmt.setString(21, " ");
					pstmt.setString(22, " ");
					pstmt.setString(23, " ");
					System.out.println("DWDSINFO" + lista.get(i).getDWDSINFO());
					pstmt.setString(24, lista.get(i).getDWDSINFO());
					pstmt.setString(25, " ");
					pstmt.setString(26, " ");
					pstmt.setString(27, " ");
					pstmt.setString(28, " ");
					pstmt.setString(29, " ");
					// 30
					pstmt.setString(30, " ");
					pstmt.setString(31, " ");
					pstmt.setString(32, " ");
					pstmt.setString(33, " ");
					pstmt.setString(34, " ");
					pstmt.setString(35, " ");
					pstmt.setString(36, " ");
					pstmt.setInt(37, 0);
					pstmt.setString(38, " ");
					pstmt.setString(39, " ");
					// 40
					pstmt.setString(40, " ");
					pstmt.setInt(41, 0);
					pstmt.setString(42, " ");
					pstmt.setString(43, lista.get(i).getDWCXUSR1());
					pstmt.setString(44, lista.get(i).getDWCXUSR2());
					pstmt.setString(45, " ");
					pstmt.setString(46, " ");
					pstmt.setString(47, " ");
					pstmt.setInt(48, lista.get(i).getDWCDUSR0()); // DWCDUSR0
					pstmt.setInt(49, 0);
					// 50
					pstmt.setInt(50, 0);
					pstmt.setInt(51, 0);
					pstmt.setInt(52, 0);
					pstmt.setInt(53, 0);
					pstmt.setString(54, " ");
					pstmt.setString(55, " ");
					pstmt.setString(56, " ");
					pstmt.setString(57, lista.get(i).getDWDSUSR3());
					pstmt.setString(58, lista.get(i).getDWDSUSR4());
					pstmt.setString(59, lista.get(i).getDWDSUSR5());
					// 60
					pstmt.setInt(60, 0);
					pstmt.setInt(61, 0);
					pstmt.setInt(62, 0);
					pstmt.setInt(63, 0);
					pstmt.setInt(64, 0);
					pstmt.setInt(65, 0);
					pstmt.setString(66, Format.formatTimeStampToString(lista
							.get(i).getDWDTUSR0()));
					pstmt.setString(67, Format.formatTimeStampToString(lista
							.get(i).getDWDTUSR1()));
					pstmt.setString(68, Format.formatTimeStampToString(lista
							.get(i).getDWDTUSR2()));
					pstmt.setString(69, "01/01/0101 00:00:00");// DD/MM/YYYY
																// HH:MI:SS
					// 70
					pstmt.setString(70, "01/01/0101 00:00:00");
					pstmt.setString(71, "01/01/0101 00:00:00");
					pstmt.setString(72, lista.get(i).getDWFLUSR0());
					pstmt.setString(73, lista.get(i).getDWFLUSR1());
					pstmt.setString(74, " ");
					pstmt.setString(75, " ");
					pstmt.setString(76, " ");
					pstmt.setString(77, " ");

					pstmt.setString(78, Format.formatTimeStampToString(lista
							.get(i).getDWCXCLA0()));
					pstmt.setString(79, Format.formatTimeStampToString(lista
							.get(i).getDWCXCLA1()));
					// 80
					pstmt.setString(80, Format.formatTimeStampToString(lista
							.get(i).getDWCXCLA2()));
					pstmt.setString(81, " ");
					pstmt.setString(82, " ");
					pstmt.setString(83, " ");
					pstmt.setString(84, "10052");
					pstmt.setString(85, " ");
					pstmt.setString(86, " ");
					pstmt.setString(87, " ");
					pstmt.setString(88, " ");
					pstmt.setString(89, " ");
					// 90
					pstmt.setString(90, " ");
					pstmt.setString(91, " ");
					pstmt.setInt(92, 0);
					pstmt.setInt(93, 0);
					pstmt.setInt(94, 0);
					pstmt.setString(95, "31/12/3999 00:00:00");
					pstmt.setString(96, "01/01/0101 00:00:00");
					pstmt.setString(97, "01/01/0101 00:00:00");
					pstmt.setInt(98, 0);
					pstmt.setString(99, "ES");
					pstmt.setInt(100, 0);
					System.out.println("...before exec");
					pstmt.execute();
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					pstmt.close();
				}
				System.out.println("index: " + i + " inserted sucessfully");

			}
			conn.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void executeWritterTest() {
		System.out.println("executeWritterTest called");
		getConnectionOracle();
		index = 0;
		PreparedStatement pstmt = null;

		try {

			String cleaner = "DELETE FROM " + table1
					+ " where DWCXUSR2 = 'SMI'";
			pstmt = conn.prepareStatement(cleaner);
			pstmt.execute(cleaner);
			pstmt.close(); 
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		try {
			String alterSession = "alter session set nls_date_format = 'DD-MM-YYYY HH24:MI:SS'";
			pstmt = conn.prepareStatement(alterSession);
			pstmt.execute();

			String[] array = {
					"Insert into GALILEOITAU.DWATOCA (DWCDCLIE,DWPRINDI,DWCODPRO,DWCODES1,DWCDCOGX,DWCDCOGY,DWTPPOI,DWTPINDI,DWDSRAGS,DWDSPRES,DWDSINDI,DWCXCIVN,DWDSESPN,DWSGPROV,DWDSCOMU,DWDSLOCA,DWCXCAP,DWCXSTAT,DWCXTEL0,DWCXTEL1,DWCXTEL2,DWCXFAX,DWCXURL,DWCXEMAI,DWDSINFO,DWCXICON,DWCXMIZO,DWCXCOMU,DWCXPROV,DWCXREGI,DWCXNIEL,DWCXCAPZ,DWCXPAD1,DWCXPAD2,DWCXPAD3,DWCXSTSG,DWCXFISC,DWCDPIVA,DWCXFORG,DWCXCCIA,DWSPCCIA,DWCDSTSO,DWCXUSR0,DWCXUSR1,DWCXUSR2,DWCXUSR3,DWCXUSR4,DWCXUSR5,DWCDUSR0,DWCDUSR1,DWCDUSR2,DWCDUSR3,DWCDUSR4,DWCDUSR5,DWDSUSR0,DWDSUSR1,DWDSUSR2,DWDSUSR3,DWDSUSR4,DWDSUSR5,DWVLUSR0,DWVLUSR1,DWVLUSR2,DWVLUSR3,DWVLUSR4,DWVLUSR5,DWDTUSR0,DWDTUSR1,DWDTUSR2,DWDTUSR3,DWDTUSR4,DWDTUSR5,DWFLUSR0,DWFLUSR1,DWFLUSR2,DWFLUSR3,DWFLUSR4,DWFLUSR5,DWCXCLA0,DWCXCLA1,DWCXCLA2,DWCXCLA3,DWCXCLA4,DWCXCLA5,DWCXCLA6,DWDSCLA0,DWDSCLA1,DWDSCLA2,DWDSCLA3,DWDSCLA4,DWDSCLA5,DWDSCLA6,DWCDPROP,DWCDLIV1,DWCDLIV2,DWDTISTI,DWDTAGGT,DWDTVALD,DWDTVALA,DWDTANNU,DWDTCANC,DWFLATTI,DWCDUTEN,DWPRUKEY) values ((SELECT MAX(DWCDCLIE)+1 FROM DWATOCA),'0','0',' ','-43,177494','-22,932534','115','0',' ',' ','FalhaEnriquecimento',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','PANICO SILENCIOSO',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','0',' ',' ',' ','0',' ','706','SMI',' ',' ',' ','5','0','0','0','0','0','  ',' ',' ','3.3767.98291:PANIC:P1.C04.USER:','ProbeESB@X0.Q.X0.OF6.NETCOOL.EVENT','PANICO SILENCIOSO','0','0','0','0','0','0',CURRENT_DATE,CURRENT_DATE,CURRENT_DATE,to_date('01/01/01','DD/MM/RR'),to_date('01/01/01','DD/MM/RR'),to_date('01/01/01','DD/MM/RR'),'S','1',' ',' ',' ',' ',to_char(CURRENT_TIMESTAMP,'DD-MM-YYYY HH24:MI:SS'),to_char(CURRENT_TIMESTAMP,'DD-MM-YYYY HH24:MI:SS'),to_char(CURRENT_TIMESTAMP,'DD-MM-YYYY HH24:MI:SS'),' ',' ',' ','10052',' ',' ',' ',' ',' ',' ',' ','0','0','0',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,to_date('31/12/99','DD/MM/RR'),to_date('01/01/01','DD/MM/RR'),to_date('01/01/01','DD/MM/RR'),'0','ES','0')",
					"Insert into GALILEOITAU.DWATOCA (DWCDCLIE,DWPRINDI,DWCODPRO,DWCODES1,DWCDCOGX,DWCDCOGY,DWTPPOI,DWTPINDI,DWDSRAGS,DWDSPRES,DWDSINDI,DWCXCIVN,DWDSESPN,DWSGPROV,DWDSCOMU,DWDSLOCA,DWCXCAP,DWCXSTAT,DWCXTEL0,DWCXTEL1,DWCXTEL2,DWCXFAX,DWCXURL,DWCXEMAI,DWDSINFO,DWCXICON,DWCXMIZO,DWCXCOMU,DWCXPROV,DWCXREGI,DWCXNIEL,DWCXCAPZ,DWCXPAD1,DWCXPAD2,DWCXPAD3,DWCXSTSG,DWCXFISC,DWCDPIVA,DWCXFORG,DWCXCCIA,DWSPCCIA,DWCDSTSO,DWCXUSR0,DWCXUSR1,DWCXUSR2,DWCXUSR3,DWCXUSR4,DWCXUSR5,DWCDUSR0,DWCDUSR1,DWCDUSR2,DWCDUSR3,DWCDUSR4,DWCDUSR5,DWDSUSR0,DWDSUSR1,DWDSUSR2,DWDSUSR3,DWDSUSR4,DWDSUSR5,DWVLUSR0,DWVLUSR1,DWVLUSR2,DWVLUSR3,DWVLUSR4,DWVLUSR5,DWDTUSR0,DWDTUSR1,DWDTUSR2,DWDTUSR3,DWDTUSR4,DWDTUSR5,DWFLUSR0,DWFLUSR1,DWFLUSR2,DWFLUSR3,DWFLUSR4,DWFLUSR5,DWCXCLA0,DWCXCLA1,DWCXCLA2,DWCXCLA3,DWCXCLA4,DWCXCLA5,DWCXCLA6,DWDSCLA0,DWDSCLA1,DWDSCLA2,DWDSCLA3,DWDSCLA4,DWDSCLA5,DWDSCLA6,DWCDPROP,DWCDLIV1,DWCDLIV2,DWDTISTI,DWDTAGGT,DWDTVALD,DWDTVALA,DWDTANNU,DWDTCANC,DWFLATTI,DWCDUTEN,DWPRUKEY) values ((SELECT MAX(DWCDCLIE)+1 FROM DWATOCA),'0','0',' ','-34,907189','-8,056889','115','0',' ',' ','FalhaEnriquecimento',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','PANICO SILENCIOSO',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','0',' ',' ',' ','0',' ','6878','SMI',' ',' ',' ','5','0','0','0','0','0',' ',' ',' ','3.3767.98291:PANIC:P1.C04.USER:','ProbeESB@X0.Q.X0.OF6.NETCOOL.EVENT','PANICO SILENCIOSO','0','0','0','0','0','0',CURRENT_DATE,CURRENT_DATE,CURRENT_DATE,to_date('01/01/01','DD/MM/RR'),to_date('01/01/01','DD/MM/RR'),to_date('01/01/01','DD/MM/RR'),'S','1',' ',' ',' ',' ',to_char(CURRENT_TIMESTAMP,'DD-MM-YYYY HH24:MI:SS'),to_char(CURRENT_TIMESTAMP,'DD-MM-YYYY HH24:MI:SS'),to_char(CURRENT_TIMESTAMP,'DD-MM-YYYY HH24:MI:SS'),' ',' ',' ','10052',' ',' ',' ',' ',' ',' ',' ','0','0','0',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,to_date('31/12/99','DD/MM/RR'),to_date('01/01/01','DD/MM/RR'),to_date('01/01/01','DD/MM/RR'),'0','ES','0')",
					"Insert into GALILEOITAU.DWATOCA (DWCDCLIE,DWPRINDI,DWCODPRO,DWCODES1,DWCDCOGX,DWCDCOGY,DWTPPOI,DWTPINDI,DWDSRAGS,DWDSPRES,DWDSINDI,DWCXCIVN,DWDSESPN,DWSGPROV,DWDSCOMU,DWDSLOCA,DWCXCAP,DWCXSTAT,DWCXTEL0,DWCXTEL1,DWCXTEL2,DWCXFAX,DWCXURL,DWCXEMAI,DWDSINFO,DWCXICON,DWCXMIZO,DWCXCOMU,DWCXPROV,DWCXREGI,DWCXNIEL,DWCXCAPZ,DWCXPAD1,DWCXPAD2,DWCXPAD3,DWCXSTSG,DWCXFISC,DWCDPIVA,DWCXFORG,DWCXCCIA,DWSPCCIA,DWCDSTSO,DWCXUSR0,DWCXUSR1,DWCXUSR2,DWCXUSR3,DWCXUSR4,DWCXUSR5,DWCDUSR0,DWCDUSR1,DWCDUSR2,DWCDUSR3,DWCDUSR4,DWCDUSR5,DWDSUSR0,DWDSUSR1,DWDSUSR2,DWDSUSR3,DWDSUSR4,DWDSUSR5,DWVLUSR0,DWVLUSR1,DWVLUSR2,DWVLUSR3,DWVLUSR4,DWVLUSR5,DWDTUSR0,DWDTUSR1,DWDTUSR2,DWDTUSR3,DWDTUSR4,DWDTUSR5,DWFLUSR0,DWFLUSR1,DWFLUSR2,DWFLUSR3,DWFLUSR4,DWFLUSR5,DWCXCLA0,DWCXCLA1,DWCXCLA2,DWCXCLA3,DWCXCLA4,DWCXCLA5,DWCXCLA6,DWDSCLA0,DWDSCLA1,DWDSCLA2,DWDSCLA3,DWDSCLA4,DWDSCLA5,DWDSCLA6,DWCDPROP,DWCDLIV1,DWCDLIV2,DWDTISTI,DWDTAGGT,DWDTVALD,DWDTVALA,DWDTANNU,DWDTCANC,DWFLATTI,DWCDUTEN,DWPRUKEY) values ((SELECT MAX(DWCDCLIE)+1 FROM DWATOCA),'0','0',' ','-43,399657','-19,823673','115','0',' ',' ','FalhaEnriquecimento',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','PANICO SILENCIOSO',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','0',' ',' ',' ','0',' ','7414','SMI',' ',' ',' ','5','0','0','0','0','0','  ',' ',' ','3.3767.98291:PANIC:P1.C04.USER:','ProbeESB@X0.Q.X0.OF6.NETCOOL.EVENT','PANICO SILENCIOSO','0','0','0','0','0','0',CURRENT_DATE,CURRENT_DATE,CURRENT_DATE,to_date('01/01/01','DD/MM/RR'),to_date('01/01/01','DD/MM/RR'),to_date('01/01/01','DD/MM/RR'),'S','1',' ',' ',' ',' ',to_char(CURRENT_TIMESTAMP,'DD-MM-YYYY HH24:MI:SS'),to_char(CURRENT_TIMESTAMP,'DD-MM-YYYY HH24:MI:SS'),to_char(CURRENT_TIMESTAMP,'DD-MM-YYYY HH24:MI:SS'),' ',' ',' ','10052',' ',' ',' ',' ',' ',' ',' ','0','0','0',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,to_date('31/12/99','DD/MM/RR'),to_date('01/01/01','DD/MM/RR'),to_date('01/01/01','DD/MM/RR'),'0','ES','0')",
					"Insert into GALILEOITAU.DWATOCA (DWCDCLIE,DWPRINDI,DWCODPRO,DWCODES1,DWCDCOGX,DWCDCOGY,DWTPPOI,DWTPINDI,DWDSRAGS,DWDSPRES,DWDSINDI,DWCXCIVN,DWDSESPN,DWSGPROV,DWDSCOMU,DWDSLOCA,DWCXCAP,DWCXSTAT,DWCXTEL0,DWCXTEL1,DWCXTEL2,DWCXFAX,DWCXURL,DWCXEMAI,DWDSINFO,DWCXICON,DWCXMIZO,DWCXCOMU,DWCXPROV,DWCXREGI,DWCXNIEL,DWCXCAPZ,DWCXPAD1,DWCXPAD2,DWCXPAD3,DWCXSTSG,DWCXFISC,DWCDPIVA,DWCXFORG,DWCXCCIA,DWSPCCIA,DWCDSTSO,DWCXUSR0,DWCXUSR1,DWCXUSR2,DWCXUSR3,DWCXUSR4,DWCXUSR5,DWCDUSR0,DWCDUSR1,DWCDUSR2,DWCDUSR3,DWCDUSR4,DWCDUSR5,DWDSUSR0,DWDSUSR1,DWDSUSR2,DWDSUSR3,DWDSUSR4,DWDSUSR5,DWVLUSR0,DWVLUSR1,DWVLUSR2,DWVLUSR3,DWVLUSR4,DWVLUSR5,DWDTUSR0,DWDTUSR1,DWDTUSR2,DWDTUSR3,DWDTUSR4,DWDTUSR5,DWFLUSR0,DWFLUSR1,DWFLUSR2,DWFLUSR3,DWFLUSR4,DWFLUSR5,DWCXCLA0,DWCXCLA1,DWCXCLA2,DWCXCLA3,DWCXCLA4,DWCXCLA5,DWCXCLA6,DWDSCLA0,DWDSCLA1,DWDSCLA2,DWDSCLA3,DWDSCLA4,DWDSCLA5,DWDSCLA6,DWCDPROP,DWCDLIV1,DWCDLIV2,DWDTISTI,DWDTAGGT,DWDTVALD,DWDTVALA,DWDTANNU,DWDTCANC,DWFLATTI,DWCDUTEN,DWPRUKEY) values ((SELECT MAX(DWCDCLIE)+1 FROM DWATOCA),'0','0',' ','-46,785908','-23,593894','115','0',' ',' ','FalhaEnriquecimento',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','PANICO SILENCIOSO',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','0',' ',' ',' ','0',' ','7210','SMI',' ',' ',' ','5','0','0','0','0','0',' ',' ',' ','3.3767.98291:PANIC:P1.C04.USER:','ProbeESB@X0.Q.X0.OF6.NETCOOL.EVENT','PANICO SILENCIOSO','0','0','0','0','0','0',CURRENT_DATE,CURRENT_DATE,CURRENT_DATE,to_date('01/01/01','DD/MM/RR'),to_date('01/01/01','DD/MM/RR'),to_date('01/01/01','DD/MM/RR'),'S','1',' ',' ',' ',' ',to_char(CURRENT_TIMESTAMP,'DD-MM-YYYY HH24:MI:SS'),to_char(CURRENT_TIMESTAMP,'DD-MM-YYYY HH24:MI:SS'),to_char(CURRENT_TIMESTAMP,'DD-MM-YYYY HH24:MI:SS'),' ',' ',' ','10052',' ',' ',' ',' ',' ',' ',' ','0','0','0',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,to_date('31/12/99','DD/MM/RR'),to_date('01/01/01','DD/MM/RR'),to_date('01/01/01','DD/MM/RR'),'0','ES','0')",
					"Insert into GALILEOITAU.DWATOCA (DWCDCLIE,DWPRINDI,DWCODPRO,DWCODES1,DWCDCOGX,DWCDCOGY,DWTPPOI,DWTPINDI,DWDSRAGS,DWDSPRES,DWDSINDI,DWCXCIVN,DWDSESPN,DWSGPROV,DWDSCOMU,DWDSLOCA,DWCXCAP,DWCXSTAT,DWCXTEL0,DWCXTEL1,DWCXTEL2,DWCXFAX,DWCXURL,DWCXEMAI,DWDSINFO,DWCXICON,DWCXMIZO,DWCXCOMU,DWCXPROV,DWCXREGI,DWCXNIEL,DWCXCAPZ,DWCXPAD1,DWCXPAD2,DWCXPAD3,DWCXSTSG,DWCXFISC,DWCDPIVA,DWCXFORG,DWCXCCIA,DWSPCCIA,DWCDSTSO,DWCXUSR0,DWCXUSR1,DWCXUSR2,DWCXUSR3,DWCXUSR4,DWCXUSR5,DWCDUSR0,DWCDUSR1,DWCDUSR2,DWCDUSR3,DWCDUSR4,DWCDUSR5,DWDSUSR0,DWDSUSR1,DWDSUSR2,DWDSUSR3,DWDSUSR4,DWDSUSR5,DWVLUSR0,DWVLUSR1,DWVLUSR2,DWVLUSR3,DWVLUSR4,DWVLUSR5,DWDTUSR0,DWDTUSR1,DWDTUSR2,DWDTUSR3,DWDTUSR4,DWDTUSR5,DWFLUSR0,DWFLUSR1,DWFLUSR2,DWFLUSR3,DWFLUSR4,DWFLUSR5,DWCXCLA0,DWCXCLA1,DWCXCLA2,DWCXCLA3,DWCXCLA4,DWCXCLA5,DWCXCLA6,DWDSCLA0,DWDSCLA1,DWDSCLA2,DWDSCLA3,DWDSCLA4,DWDSCLA5,DWDSCLA6,DWCDPROP,DWCDLIV1,DWCDLIV2,DWDTISTI,DWDTAGGT,DWDTVALD,DWDTVALA,DWDTANNU,DWDTCANC,DWFLATTI,DWCDUTEN,DWPRUKEY) values ((SELECT MAX(DWCDCLIE)+1 FROM DWATOCA),'0','0',' ','-46,785908','-23,593894','115','0',' ',' ','FalhaEnriquecimento',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','PANICO SILENCIOSO',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','0',' ',' ',' ','0',' ','7210','SMI',' ',' ',' ','5','0','0','0','0','0',' ',' ',' ','3.3767.98291:PANIC:P1.C04.USER:','ProbeESB@X0.Q.X0.OF6.NETCOOL.EVENT','PANICO SILENCIOSO','0','0','0','0','0','0',CURRENT_DATE,CURRENT_DATE,CURRENT_DATE,to_date('01/01/01','DD/MM/RR'),to_date('01/01/01','DD/MM/RR'),to_date('01/01/01','DD/MM/RR'),'S','1',' ',' ',' ',' ',to_char(CURRENT_TIMESTAMP,'DD-MM-YYYY HH24:MI:SS'),to_char(CURRENT_TIMESTAMP,'DD-MM-YYYY HH24:MI:SS'),to_char(CURRENT_TIMESTAMP,'DD-MM-YYYY HH24:MI:SS'),' ',' ',' ','10052',' ',' ',' ',' ',' ',' ',' ','0','0','0',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,to_date('31/12/99','DD/MM/RR'),to_date('01/01/01','DD/MM/RR'),to_date('01/01/01','DD/MM/RR'),'0','ES','0')" };

			for (int i = 0; i < array.length; i++) {
				String sql = array[i];

				pstmt = conn.prepareStatement(sql);
				pstmt.execute();

			}
			System.out.println("5 registers inserted sucessfully");
			pstmt.close();
			conn.commit();
			closeConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
