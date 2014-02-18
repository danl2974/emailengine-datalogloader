package com.lambdus.emailengine.dataloader;

import java.util.ArrayList;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;


public class DataLoaderScheduledJob implements Job {

	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		try{
		  System.out.println("\n\nJOB STARTED AT: " + new Date().toString());
		  
		  ArrayList<EmailSuccess> successRecords = PostfixLogParser.processSuccess();
		  ArrayList<EmailBounce> bounceRecords = PostfixLogParser.processBounce();
		
		  LogDataLoader.loadSuccessData(successRecords);
		  LogDataLoader.loadBounceData(bounceRecords);
		  
		  System.out.println("\n\nJOB ENDED AT: " + new Date().toString());
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	        
       }
		

}


