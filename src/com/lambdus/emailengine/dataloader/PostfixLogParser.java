package com.lambdus.emailengine.dataloader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



public class PostfixLogParser {
		
	private static final String POSTFIX_LOG_PATH = "/var/log/mail.log";
	
	private static final String POSTFIX_SUCCESS_MARKER = "status=sent (250";  
	
	private static final String POSTFIX_BOUNCE_MARKER = "status=bounced";
	

		
	public static ArrayList<EmailSuccess> processSuccess(){
	
		ArrayList<EmailSuccess> successList = new ArrayList<EmailSuccess>();
		String successProgress = ProgressRegister.readLastUpdateSuccess();
		System.out.println("Success Record progress " + successProgress);
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(POSTFIX_LOG_PATH));
			String sLine;
		 
		    while ((sLine = br.readLine()) != null) 
		    {
		    	
		        if(sLine.indexOf(POSTFIX_SUCCESS_MARKER) != -1){
		             EmailSuccess success = new EmailSuccess(); 		
		             success.toAddress = getToAddress(sLine);
		             success.timestamp = getTimestamp(sLine);
		             String[] remoteData = getRemoteData(sLine);
		             success.remoteHost = remoteData[0];
		             success.remoteIP = remoteData[1];
		             String[] outboundData = getOutboundData(sLine);
		             success.outboundHost = outboundData[0];
		             success.outboundIP = outboundData[1];
		             success.mailingId = getMailingId(sLine);
		             successList.add(success);
		             if (checkProgressMailingId(sLine, successProgress))
		               {
		            	 successList.clear();
		               }		             
		            
		    	     }
		        
		    	
		    }
		    br.close();
	    } 
		
		catch (IOException e) {
			System.out.println("Success Process Exception: " + e.getMessage());
	     } 
		
		System.out.println("Success Records Processed: " + successList.size());
		
		return successList;
	
	}
	
	
	public static  ArrayList<EmailBounce> processBounce(){

		ArrayList<EmailBounce> bounceList = new ArrayList<EmailBounce>();
		String bounceProgress = ProgressRegister.readLastUpdateBounce();
		System.out.println("Bounce Record progress " + bounceProgress);
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(POSTFIX_LOG_PATH));
			String sLine;
		 
		    while ((sLine = br.readLine()) != null) 
		    {
		    	if(sLine.indexOf(POSTFIX_BOUNCE_MARKER) != -1){
		             EmailBounce bounce = new EmailBounce(); 		
		             bounce.toAddress = getToAddress(sLine);
		             bounce.timestamp = getTimestamp(sLine);
		             String[] remoteData = getRemoteData(sLine);
		             bounce.remoteHost = remoteData[0];
		             bounce.remoteIP = remoteData[1];
		             String[] outboundData = getOutboundData(sLine);
		             bounce.outboundHost = outboundData[0];
		             bounce.outboundIP = outboundData[1];
		             bounce.ispResponse = getIspResponse(sLine);
		             bounce.mailingId = getMailingId(sLine);
		             bounceList.add(bounce);
		             if (checkProgressMailingId(sLine, bounceProgress)){
		            	 bounceList.clear();
		             }
		    	}
		    }
		    br.close();
	    } 
		
		catch (IOException e) {
			System.out.println("Bounce Process Exception: " + e.getMessage());
	     } 
		
		System.out.println("Bounce Records Processed: " + bounceList.size());
		
		return bounceList;
	
	}	
	
	

	static private String getToAddress(String line){
		try{
		return line.split("to=<")[1].split(">")[0];
		}catch(Exception e) {return "";}
	}
	
	static private Timestamp getTimestamp(String line){
		Date date = new Date();
		String logdate = line.substring(0, 15);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM d HH:mm:ss");
		try{
		 date = sdf.parse(Calendar.getInstance().get(Calendar.YEAR) + " " + logdate);
		}catch(ParseException pe){System.out.println(pe.getMessage());}
		return new Timestamp(date.getTime());
	}	
	

	static private String[] getRemoteData(String line){
		String[] remote = new String[2];
		try{
		 String[] remoteArr = line.split("relay=")[1].split("\\]")[0].split("\\[");
		 remote[0] = remoteArr[0]; 
		 remote[1] = remoteArr[1];
		}catch(Exception e) {System.out.println(e.getMessage());}
		return remote;
	}
	
	static private String[] getOutboundData(String line){
		String[] outbound = new String[2];
		try{
		 String[] outboundArr = line.split("outhost-")[1].split("/")[0].split("--outip-");
		 outbound[0] = outboundArr[0]; 
		 outbound[1] = outboundArr[1];
		}catch(Exception e) {System.out.println(e.getMessage());}
		return outbound;
	}	
	
	static private String getIspResponse(String line){
		String response = "";
		try{
		  response = line.split("said: ")[1];
		  }catch(Exception e){System.out.println(e.getMessage());}
		return response;
	}
	
	static private String getMailingId(String line){
		return line.split(": to")[0].split("]: ")[1];
	}
	
	private static boolean checkProgressMailingId(String line, String progress){
		int found = line.indexOf(progress);
		if (found > 0){
			return true;
		}
		else{
		    return false;
		}
	} 
	
	
}
