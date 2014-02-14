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
	      Date date = new Date();
		  System.out.println("JOB STARTED AT :" + date.toString());
		  ArrayList<EmailSuccess> successRecords = PostfixLogParser.processSuccess();
		  ArrayList<EmailBounce> bounceRecords = PostfixLogParser.processBounce();
		
		  LogDataLoader.loadSuccessData(successRecords);
		  LogDataLoader.loadBounceData(bounceRecords); 

		}
		catch(Exception e){
			log.info(e.getMessage());
		}
	        
       }
		

}


