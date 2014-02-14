package com.bcf.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.bcf.dao.SybaseDAO;

public class WriteOracleTest implements Job{
	
	public void execute(JobExecutionContext context)
			throws JobExecutionException {					
		
		SybaseDAO sybaseDAO = new SybaseDAO();		
		
		
		sybaseDAO.executeWritterTest();			
	}

}
