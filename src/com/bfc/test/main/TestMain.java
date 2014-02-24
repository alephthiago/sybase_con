package com.bfc.test.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bfc.test.dao.TestDAO;

public class TestMain {
	public static void main(String[] args) {
		TestDAO tdao = new TestDAO(); 
		//comentario
		
		List<String> dates = new ArrayList<String>();
		
		dates = tdao.oracleDateReader();
		
		for (String date : dates) {
			System.out.println(date);
		}
	}

}
