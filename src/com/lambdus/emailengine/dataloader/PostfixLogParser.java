package com.lambdus.emailengine.dataloader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.joda.time.DateTime;



public class PostfixLogParser {
		
	private static final String POSTFIX_LOG_PATH = "/var/log/mail.log";
	
	private static final String POSTFIX_SUCCESS_MARKER = "status=sent (250";  
	
	private static final String POSTFIX_BOUNCE_MARKER = "status=bounced";
	
	private static final String POSTFIX_CLEANUP_MARKER = "/cleanup";
	
	private static final String POSTFIX_LOCAL_RELAY_MARKER = "postfix/smtp";

		
	public static ArrayList<EmailSuccess> processSuccess(){
	
		ArrayList<EmailSuccess> successList = new ArrayList<EmailSuccess>();
		HashMap<String,String> templateMtaHash = new HashMap<String,String>();
		String successProgress = ProgressRegister.readLastUpdateSuccess();
		System.out.println("Success Record progress " + successProgress);
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(POSTFIX_LOG_PATH));
			String sLine;
		 
		    while ((sLine = br.readLine()) != null) 
		    {
		    	
		        if(sLine.indexOf(POSTFIX_SUCCESS_MARKER) != -1 && sLine.indexOf(POSTFIX_LOCAL_RELAY_MARKER) == -1){
		             EmailSuccess success = new EmailSuccess(); 		
		             success.toAddress = getToAddress(sLine);
		             success.timestamp = getTimestamp(sLine);
		             String[] remoteData = getRemoteData(sLine);
		             success.remoteHost = remoteData[0];
		             success.remoteIP = remoteData[1];
		             String[] outboundData = getOutboundData(sLine);
		             success.outboundHost = outboundData[0] != null ? outboundData[0] : "None";
		             success.outboundIP = outboundData[1] != null ? outboundData[1] : "None";
		             success.mailingId = getMailingId(sLine);		             
		             successList.add(success);
		             if (checkProgressMailingId(sLine, successProgress))
		               {
		            	 successList.clear();
		               }		             
		            
		    	     }
		        
		           if(sLine.indexOf(POSTFIX_CLEANUP_MARKER) != -1){
		        	   String mtaId = getMtaId(sLine);
		        	   String msgId = getMessageId(sLine);
		        	   try{	   
		        	   String embedTemplateId = msgId.split("\\.")[1];
		        	   if(embedTemplateId.matches("-?\\d+")){
		        	        templateMtaHash.put(mtaId, embedTemplateId);
		        	       }
		        	   }catch(Exception e){}
		           }

		    	
		    }
		    
		    br.close();
		    
		    if (successList.size() > 0 && templateMtaHash.size() > 0){
		    	
		    	for (int i = 0; i < successList.size(); i++){
		    		
		    		if(templateMtaHash.containsKey(successList.get(i).mailingId))
		    		{
		    		   successList.get(i).templateId = templateMtaHash.get(successList.get(i).mailingId);
		    		}
		    		else{
		    		   successList.get(i).templateId = "0";
		    		}
		    		
		    	}
		    }
		    
		   
	    } 
		
		catch (IOException e) {
			System.out.println("Success Process IOException: " + e.getMessage());
	     }
		catch (Exception ex) {
			System.out.println("Success Process Exception: " + ex.getMessage());
	     } 		
		
		System.out.println("Success Records Processed: " + successList.size());
		
		return successList;
	
	}
	
	
	public static  ArrayList<EmailBounce> processBounce(){

		ArrayList<EmailBounce> bounceList = new ArrayList<EmailBounce>();
		HashMap<String,String> templateMtaHash = new HashMap<String,String>();
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
		             bounce.remoteHost = remoteData[0] != null ? remoteData[0] : "None";
		             bounce.remoteIP = remoteData[1] != null ? remoteData[1] : "None";
		             String[] outboundData = getOutboundData(sLine);
		             bounce.outboundHost = outboundData[0] != null ? outboundData[0] : "None";
		             bounce.outboundIP = outboundData[1] != null ? outboundData[1] : "None";
		             bounce.ispResponse = getIspResponse(sLine);
		             bounce.mailingId = getMailingId(sLine);
		             bounceList.add(bounce);
		             if (checkProgressMailingId(sLine, bounceProgress)){
		            	 bounceList.clear();
		             }
		    	}
		    	
		        if(sLine.indexOf(POSTFIX_CLEANUP_MARKER) != -1){
		        	   String mtaId = getMtaId(sLine);
		        	   String msgId = getMessageId(sLine);
		        	   try{
		        	   String embedTemplateId = msgId.split("\\.")[1];
		        	   if(embedTemplateId.matches("-?\\d+")){
		        	       templateMtaHash.put(mtaId, embedTemplateId);
		        	       }
		        	   }catch(Exception e){}
		           }
		    	
		    	
		    }
            br.close();
		    
		    if (bounceList.size() > 0 && templateMtaHash.size() > 0){
		    	
		    	for (int i = 0; i < bounceList.size(); i++){
		    	    
		    		if(templateMtaHash.containsKey(bounceList.get(i).mailingId))
		    		{
		    		  bounceList.get(i).templateId = templateMtaHash.get(bounceList.get(i).mailingId);
		    		}
		    		else{
		    		  bounceList.get(i).templateId = "0";
		    		}
		    	}
		     }
		    
	    } 
		
		catch (IOException e) {
			System.out.println("Bounce Process IOException: " + e.getMessage());
	     }
		catch (Exception ex) {
			System.out.println("Bounce Process Exception: " + ex.getMessage());
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
		
		try{
		Date date = new Date();
		String logdate = line.substring(0, 15);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM d HH:mm:ss");
		date = sdf.parse(Calendar.getInstance().get(Calendar.YEAR) + " " + logdate);
		  Calendar tzCal = Calendar.getInstance(TimeZone.getTimeZone("EST"));
		  tzCal.setTime(date);
		  tzCal.add(Calendar.HOUR_OF_DAY, -5);
		  //return new Timestamp(date.getTime());
		  return new Timestamp(tzCal.getTimeInMillis());
		}catch(Exception e){return new Timestamp(0);}
	}	
	

	static private String[] getRemoteData(String line){
		
		String[] remote = new String[2];
		try{
		 String[] remoteArr = line.split("relay=")[1].split("\\]")[0].split("\\[");
		 remote[0] = remoteArr[0]; 
		 remote[1] = remoteArr[1];
		}catch(Exception e) {return remote;}
		return remote;
	}
	
	static private String[] getOutboundData(String line){
		
		String[] outbound = new String[2];
		try{
		 String[] outboundArr = line.split("outhost-")[1].split("/")[0].split("--outip-");
		 outbound[0] = outboundArr[0]; 
		 outbound[1] = outboundArr[1];
		}catch(Exception e) {return outbound;}
		return outbound;
	}	
	
	static private String getIspResponse(String line){
		
		String response = "";
		try{
		  response = line.split("said: ")[1];
		  }catch(Exception e){return "";}
		return response;
	}
	
	static private String getMailingId(String line){
		try{
		return line.split(": to")[0].split("\\]: ")[1];
		}catch(Exception e){return "";}
	}
	
	static private String getMtaId(String line){
		try{
		return line.split(": message-id")[0].split("\\]: ")[1];
		}catch(Exception e){return "";}
	}	
	
	static private String getMessageId(String line){
		try{
		return line.split("message-id=<")[1].split(">")[0];
		}catch(Exception e){return "";}
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
