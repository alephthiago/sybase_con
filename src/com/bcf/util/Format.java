package com.bcf.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Format {
	
	
	public static Date formatStringToDate(String data){ 
		if (data == null || data.equals(""))
			return null;

		Date date = null;
		try {
			//SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH24:mm:SS");
			//date = (java.util.Date)sdf.parse(data);
			date = new Date(data);
		} catch (Exception e) {            
			e.printStackTrace();
		}
		System.out.println("Finalizou formatacao");
		return date;
	}
	
	public static String formatTimeStampToString(String data){ 
		if (data == null || data.equals(""))
			return null;
		System.out.println("o que veio do banco "+data);
		
		
		Date date = new Date(Long.parseLong(data)*1000);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT-0200"));
        String formatted = format.format(date);
        System.out.println(formatted);        
        formatted = format.format(date);
		
		System.out.println("Finalizou formatacao");
		return formatted;
	}


	public static String formatDateToString(Date data) throws Exception { 
		if (data == null)
			return null;

		String date = null;
		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");		
		date = sdf.format(data.getTime());
		
		System.out.println("Result of this conversion "+ date);
		
		return date;	
	}

	

	
	public static String formatText(String texto, int tamanho) {
		
		// tamanho=10, texto="Fulano", completa=4
		// tamanho=10, texto="Fulano Ciclano", completa=-4
		int completa = tamanho - texto.length();
		
		if (completa > 0) {
			// COMPLENTAR COM ESPACOS
			// tamanho=10, texto="Fulano", texto="Fulano    "
			texto = String.format("%s%"+completa+"s", texto, "");
			
		} else {
			// TRUNCAR
			// tamanho=10, texto="Fulano Ciclano", texto="Fulano Cic"
			texto = texto.substring(0, tamanho);
			
		}
		
		return texto;
		
	}
	
	public static String formatNumber(int numero, int tamanho) {
		
		// numero=1,      tamanho=5, texto=00001
		// numero=111,    tamanho=5, texto=00111
		// numero=111111, tamanho=5, texto=111111
		String texto = String.format("%0"+tamanho+"d", numero);
		
		if (texto.length() > tamanho) {
			texto = texto.substring(0, tamanho);
		}
		
		return texto;
		
	}
}
