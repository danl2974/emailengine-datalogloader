package com.lambdus.emailengine.dataloader;

import java.util.ArrayList;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import java.util.logging.Logger;

public class DataLoaderScheduledJob implements Job {
	
	private static final Logger log = Logger.getLogger(DataLoaderScheduledJob.class.getName());
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		try{
		  PostfixLogParser postfixParser = new PostfixLogParser();
		  
		  ArrayList<EmailSuccess> successRecords = postfixParser.processSuccess();
		  ArrayList<EmailBounce> bounceRecords = postfixParser.processBounce();
		
		  LogDataLoader.loadSuccessData(successRecords); 
		  LogDataLoader.loadBounceData(bounceRecords);      

		}
		catch(Exception e){
			log.info(e.getMessage());
		}
	        
       }
		

}


