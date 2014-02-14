package com.bcf.task;


import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bcf.beans.Alarm;
import com.bcf.dao.SybaseDAO;
import com.bcf.main.Execute;

public class ReadSybaseAndWriteOracle implements Job {

	public static final Logger log = LoggerFactory.getLogger(Execute.class);
	
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
		// Here is the job that you want to be executed
		log.info(" Sybase connector /n Iniciando Aplicação ...");				
		
		SybaseDAO sybaseDAO = new SybaseDAO();
		
		List<Alarm> list = new ArrayList<Alarm>();
		
		list = sybaseDAO.executeReader();
		
		sybaseDAO.executeWritter(list);			
		
	/*File archive = new File("log.text");	
	try {
		FileWriter fw = new FileWriter( archive );		
		BufferedWriter bw = new BufferedWriter(fw);		
		for (String string : list) {
			bw.write( string );
			bw.newLine();	
		}
		bw.close();
		fw.close();
			} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/		
	}

}
