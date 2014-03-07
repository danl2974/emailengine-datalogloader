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
		  ArrayList<EmailFbl> fblRecords = FblLogParser.processFblData();

		  //Drop Data to Lambdus system
		  LogDataLoader.loadSuccessData(successRecords);
		  LogDataLoader.loadBounceData(bounceRecords);
		  LogDataLoader.loadFblData(fblRecords);
		  //Drop Data to Local
		  LogDataLoader.loadLocalSuccessData(successRecords);
		  LogDataLoader.loadLocalBounceData(bounceRecords);
		  LogDataLoader.loadLocalFblData(fblRecords);
		  
		  System.out.println("\n\nJOB ENDED AT: " + new Date().toString());
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	        
       }
		

}


