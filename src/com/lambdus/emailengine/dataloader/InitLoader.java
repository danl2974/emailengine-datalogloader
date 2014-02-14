package com.lambdus.emailengine.dataloader;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import java.util.logging.Logger;

public class InitLoader {
	
	private static final Logger log = Logger.getLogger(InitLoader.class.getName());
	
	public static void main(String args[]){
		 
		 try{
		    SchedulerFactory sf = new StdSchedulerFactory();
		    Scheduler scheduler = sf.getScheduler();
		    scheduler.start();
		 
		    JobDetail job = JobBuilder.newJob(DataLoaderScheduledJob.class)
				    .withIdentity("loader", "datalog")
				    .build();

		    Trigger trigger = TriggerBuilder.newTrigger()
		    	    .withIdentity("loadertrigger", "datalog")
		    	    .startNow()
		    	    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
		    	            .withIntervalInMinutes(5)
		    	            .repeatForever())
		    	    .build();

	        scheduler.scheduleJob(job, trigger);
		 }
		 catch(Exception e){
			 log.info(e.getMessage());
		 }
		
	}

}
