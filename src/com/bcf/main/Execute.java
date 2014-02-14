package com.bcf.main;

import java.util.Scanner;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.bcf.task.TaskScheduler;
import com.bcf.task.TaskSchedulerTest;



public class Execute {
	
	public static final Logger log = LoggerFactory.getLogger(Execute.class);
	
	public static void main(String[] args){	
		int cd_operacao;
		
		Scanner entry = new Scanner(System.in);
		
		System.out.println( "Please, insert 0 to read sybase data and insert it in oracle \n OR insert 1 to run a test");
		
		cd_operacao = entry.nextInt();	
			
		if(cd_operacao == 0){
			try {
				TaskScheduler.init();
			} catch (Exception e) {			
				e.printStackTrace();
			}
		}else if(cd_operacao == 1){
			try {
				TaskSchedulerTest.init();
			} catch (Exception e) {				
				e.printStackTrace();
			}
		}else{
			System.out.println("You didnt chose any of the options, shutting down...");
		}
		
		entry.close();
	
		
				
	}
}
