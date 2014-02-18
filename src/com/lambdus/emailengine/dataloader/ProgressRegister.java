package com.lambdus.emailengine.dataloader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ProgressRegister {
	

		
	public static String readLastUpdateSuccess(){
		
		StringBuilder sb = new StringBuilder();
		
		try{
			File file = new File("/var/log/dataloader-progress-success.log");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileReader fr = new FileReader(file.getAbsoluteFile());
			BufferedReader br = new BufferedReader(fr);
			String sLine;
			while ((sLine = br.readLine()) != null) {
				sb.append(sLine);
			}
			br.close();
		}
		catch(Exception e){
		  	
		}
		System.out.println("Last Update Success Read: " + sb.toString());
		return sb.toString();
		
	}
	
	public static boolean writeLastUpdateSuccess(String progressInfo){
		
		try{
			File file = new File("/var/log/dataloader-progress-success.log");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), false);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(progressInfo);
			bw.close();
			System.out.println("Last Update Success Write: " + progressInfo);
			
			return true;
		}
		catch(Exception e){
			return false;
		}
		
	}
	
	
	
	public static String readLastUpdateBounce(){
		
		StringBuilder sb = new StringBuilder();
		
		try{
			File file = new File("/var/log/dataloader-progress-bounce.log");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileReader fr = new FileReader(file.getAbsoluteFile());
			BufferedReader br = new BufferedReader(fr);
			String sLine;
			while ((sLine = br.readLine()) != null) {
				sb.append(sLine);
			}
			br.close();
		}
		catch(Exception e){
		  	
		}
		System.out.println("Last Update Bounce Read: " + sb.toString());
		return sb.toString();
		
	}
	
	public static boolean writeLastUpdateBounce(String progressInfo){
		
		try{
			File file = new File("/var/log/dataloader-progress-bounce.log");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), false);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(progressInfo);
			bw.close();
			System.out.println("Last Update Bounce Write: " + progressInfo);
			
			return true;
		}
		catch(Exception e){
			return false;
		}
		
	}	
	

}
