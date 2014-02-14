package com.bfc.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import com.bcf.conn.DBManager;
import com.bcf.util.Format;

public class TestDAO {
	
	private Connection conn;	

	PreparedStatement ps = null;

	private int index;

	private ResultSet rs;
	
	private void getConnectionLocalOracle() {
		this.conn = DBManager.getConnectionLocalOracle();
	}
	
	private void closeConnection() throws Exception {
		this.conn.close();
	}
	
	private void readSybaseWriteTestDate(){
		
		try{
			getConnectionLocalOracle();
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	public List<String> sybaseDateReader(){
		List<String> dates = new ArrayList<String>(); 
		
		try {
			conn = DBManager.getConnectionSybase();			
			Statement stmt = conn.createStatement();
			String sql = "SELECT FirstOccurrence FROM alerts.status";
			
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				dates.add(Format.formatTimeStampToString(rs.getString("FirstOccurrence")));
			}
			stmt.close();
			rs.close();
			closeConnection();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return dates;		
	}
	
	public List<String> oracleDateReader(){
		List<String> dates = new ArrayList<String>(); 
		
		try {
			conn = DBManager.getConnectionSybase();			
			Statement stmt = conn.createStatement();
			String sql = "SELECT DWDTISTI FROM DWATOCA WHERE DWCXUSR2 = 'SMI'";
			
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				dates.add(Format.formatTimeStampToString(rs.getString("DWDTISTI")));
			}
			stmt.close();
			rs.close();
			closeConnection();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return dates;		
	}
	
	

}
