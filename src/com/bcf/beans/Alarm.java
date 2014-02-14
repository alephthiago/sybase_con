package com.bcf.beans;

import java.math.BigDecimal;
import com.bcf.dao.SybaseDAO;

public class Alarm {
	
	public Alarm (){}
	
	private int DWCDCLIE;//1
	private BigDecimal DWCDCOGX;//5
	private BigDecimal DWCDCOGY;//6
	private String DWTPPOI;//7
	private String DWDSINDI;//8
	private String DWDSINFO;//25
	private String DWCXUSR1;//44
	private String DWCXUSR2;//45
	private Integer DWCDUSR0;//49
	private String DWDSUSR0;//55
	private String DWDSUSR3;//58
	private String DWDSUSR4;//59
	private String DWDSUSR5;//60
	private String DWDTUSR0;//67 date 
	private String DWDTUSR1;//68 date
	private String DWDTUSR2;//69 date
	private String DWFLUSR0;//73
	private String DWFLUSR1;//74
	private String DWCXCLA0;//79
	private String DWCXCLA1;//80
	private String DWCXCLA2;//81
	private String DWCXCLA6;//77
	
	
	
	
	public String getDWDSUSR5() {
		return DWDSUSR5;
	}
	public void setDWDSUSR5(String dWDSUSR5) {
		DWDSUSR5 = dWDSUSR5;
	}
	public Integer getDWCDCLIE() {
		return DWCDCLIE;
	}
	public void setDWCDCLIE(Integer dWCDCLIE) {
		DWCDCLIE = dWCDCLIE;
	}
	public BigDecimal getDWCDCOGX() {
		return DWCDCOGX;
	}	
	public BigDecimal getDWCDCOGY() {
		return DWCDCOGY;
	}	
	public String getDWTPPOI() {
		return DWTPPOI;
	}
	public void setDWTPPOI(String dWTPPOI) {
		if(dWTPPOI == null){
			dWTPPOI = " ";
		}else{
			DWTPPOI = dWTPPOI;
		}		
	}
	public String getDWDSINDI() {
		return DWDSINDI;
	}
	public void setDWDSINDI(String dWDSINDI) {
		if(dWDSINDI == null){
			dWDSINDI = " ";
		}else{
			DWDSINDI = dWDSINDI;
		}			
	}
	public String getDWDSINFO() {
		return DWDSINFO;
	}
	public void setDWDSINFO(String dWDSINFO) {
		if(dWDSINFO == null){
			dWDSINFO = " ";
		}else{
			DWDSINFO = dWDSINFO;
		}
	}
	public String getDWCXUSR1() {
		return DWCXUSR1;
	}
	public void setDWCXUSR1(String dWCXUSR1) {
		SybaseDAO sybase = new SybaseDAO();
		
		if(dWCXUSR1 == null){
			dWCXUSR1 = " ";
		
			
			this.DWCDCOGX = new BigDecimal("0");
			this.DWCDCOGY = new BigDecimal("0");
		
		}
		
		try {

			
			DWCXUSR1 = dWCXUSR1;
			DWCXUSR1 = DWCXUSR1.replaceAll("[^0-9]", "");
			System.out.println("CLena della stringa "+DWCXUSR1);
			int value = Integer.parseInt(DWCXUSR1); 
			Integer wvalue = new Integer(value); 
			DWCXUSR1 = wvalue.toString(); 
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		if (DWCXUSR1.length()>1){
			
			
			BigDecimal x = sybase.searchLat(DWCXUSR1);
			BigDecimal y = sybase.searchLong(DWCXUSR1);
			
			
			if(x == null||y == null){
				this.DWCDCOGX = new BigDecimal(0.0);
				this.DWCDCOGY = new BigDecimal(0.0);
			}else{
				this.DWCDCOGX = x;
				this.DWCDCOGY = y;
			}					
		}		
	}
	public String getDWCXUSR2() {
		return DWCXUSR2;
	}
	public void setDWCXUSR2(String dWCXUSR2) {
		if(dWCXUSR2 == null){
			dWCXUSR2 = " ";
		}else{
			DWCXUSR2 = dWCXUSR2;
		}		
	}
	public Integer getDWCDUSR0() {
		return DWCDUSR0;
	}
	public void setDWCDUSR0(Integer dWCDUSR0) {
		
		DWCDUSR0 = dWCDUSR0;
	}
	public String getDWDSUSR0() {
		return DWDSUSR0;
	}
	public void setDWDSUSR0(String dWDSUSR0) {
		if(dWDSUSR0 == null){
			dWDSUSR0 = " ";
		}else{
			DWDSUSR0 = dWDSUSR0;
		}		
	}
	public String getDWDSUSR3() {
		return DWDSUSR3;
	}
	public void setDWDSUSR3(String dWDSUSR3) {
		if(dWDSUSR3 == null){
			dWDSUSR3 = " ";
		}else{
			DWDSUSR3 = dWDSUSR3;
		}
	}
	public String getDWDSUSR4() {
		return DWDSUSR4;
	}
	public void setDWDSUSR4(String dWDSUSR4) {
		if(dWDSUSR4 == null){
			dWDSUSR4 = " ";
		}else{
			DWDSUSR4 = dWDSUSR4;
		}
	}		
	public String getDWFLUSR0() {
		return DWFLUSR0;
	}
	public void setDWFLUSR0(String dWFLUSR0) {
		if(dWFLUSR0 == null){
			dWFLUSR0 = " ";
		}else{
			DWFLUSR0 = dWFLUSR0;
		}
	}
	public String getDWFLUSR1() {
		return DWFLUSR1;
	}
	public void setDWFLUSR1(String dWFLUSR1) {
		if(dWFLUSR1 == null){
			dWFLUSR1 = " ";
		}else{
			DWFLUSR1 = dWFLUSR1;
		}		
	}	
	public String getDWCXCLA6() {
		return DWCXCLA6;
	}
	public void setDWCXCLA6(String dWCXCLA6) {
		if(dWCXCLA6 == null){
			dWCXCLA6 = " ";
		}else{
			DWCXCLA6 = dWCXCLA6;
		}
	}
	public String getDWDTUSR0() {
		return DWDTUSR0;
	}
	public void setDWDTUSR0(String dWDTUSR0) {
		if(dWDTUSR0 == null){
			dWDTUSR0 = " ";
		}else{
			DWDTUSR0 = dWDTUSR0;
		}
	}
	public String getDWDTUSR1() {
		return DWDTUSR1;
	}
	public void setDWDTUSR1(String dWDTUSR1) {
		if(dWDTUSR1 == null){
			dWDTUSR1 = " ";
		}else{
			DWDTUSR1 = dWDTUSR1;
		}
	}
	public String getDWDTUSR2() {
		return DWDTUSR2;
	}
	public void setDWDTUSR2(String dWDTUSR2) {
		if(dWDTUSR2 == null){
			dWDTUSR2 = " ";
		}else{
			DWDTUSR2 = dWDTUSR2;
		}
	}
	public String getDWCXCLA0() {
		return DWCXCLA0;
	}
	public void setDWCXCLA0(String dWCXCLA0) {
		if(dWCXCLA0 == null){
			dWCXCLA0 = " ";
		}else{
			DWCXCLA0 = dWCXCLA0;
		}
	}
	public String getDWCXCLA1() {
		return DWCXCLA1;
	}
	public void setDWCXCLA1(String dWCXCLA1) {
		if(dWCXCLA1 == null){
			dWCXCLA1 = " ";
		}else{
			DWCXCLA1 = dWCXCLA1;
		}
	}
	public String getDWCXCLA2() {
		return DWCXCLA2;
	}
	public void setDWCXCLA2(String dWCXCLA2) {
		if(dWCXCLA2 == null){
			dWCXCLA2 = " ";
		}else{
			DWCXCLA2 = dWCXCLA2;
		}
	}
	
	
	
	

	
}
