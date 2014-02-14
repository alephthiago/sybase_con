package com.bcf.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.bcf.properties.PropertySingleton;

public class DBManager {
	
	private DBManager() {}
	
	public static DBManager instance = null;
	private static Connection sybaseConnection = null;
	private static Connection oracleConnection = null;
	
	static final String oracle_server = PropertySingleton.getInstance().getProperty("oracle_server");
	static final String oracle_server_user = PropertySingleton.getInstance().getProperty("oracle_server_user");
	static final String oracle_server_pw = PropertySingleton.getInstance().getProperty("oracle_server_pw");
	
	
	static final String oracle_teste = PropertySingleton.getInstance().getProperty("oracle_teste");
	static final String oracle_teste_user = PropertySingleton.getInstance().getProperty("oracle_teste_user");
	static final String oracle_teste_pw = PropertySingleton.getInstance().getProperty("oracle_teste_pw");
	
	public static DBManager getInstance() {
		if(instance == null) {
			instance = new DBManager();
		}
		return instance;
	}
	
	  
    
    public static Connection getConnectionSybase() throws SQLException{    
        try {    
           /* Class.forName("com.sybase.jdbc3.jdbc.SybDriver");    
            if (sybaseConnection == null || sybaseConnection.isClosed()) {  
            	sybaseConnection = DriverManager.getConnection(  
                        "jdbc:sybase:Tds:10.58.57.77:4100",  
                        "SIOP",  
                        "siop"  
                );                  
            } */
            
            Class.forName("com.sybase.jdbc3.jdbc.SybDriver");    
            if (sybaseConnection == null || sybaseConnection.isClosed()) {  
            	sybaseConnection = DriverManager.getConnection("jdbc:sybase:Tds:10.58.63.182:4100","SIOP","siop");                  
            } 
            
           
        } catch(SQLException e) {    
            System.out.println(e);  
        }  catch(ClassNotFoundException d) { 
        	System.out.println(d);  
        }
        if (sybaseConnection == null) {
        	System.out.println("Conexao inexistente");
        }else{
        	System.out.println("Conexao ativa");
        }
        return sybaseConnection;  
    }  
    public static Connection getConnectionOracle() {
		
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//LOCAL
			if(oracleConnection == null || oracleConnection.isClosed()){
			oracleConnection = DriverManager.getConnection(oracle_server,oracle_server_user,oracle_server_pw);
			}
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return oracleConnection;
	}
    
public static Connection getConnectionLocalOracle() {
		
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//LOCAL
			if(oracleConnection == null || oracleConnection.isClosed()){
			oracleConnection = DriverManager.getConnection("jdbc:oracle:thin:@localhos:1521:XE","SYSTEM","68246824");
			}
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return oracleConnection;
	}
    
    

}
