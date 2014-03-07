package com.lambdus.emailengine.dataloader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.Format;
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


public class FblLogParser {
	
	private static final String LOG_PATH = "/usr/local/share/datalogloader/fbl/fbl.log";

	public static ArrayList<EmailFbl> processFblData(){
		
		ArrayList<EmailFbl> fblList = new ArrayList<EmailFbl>();
		String fblProgress = ProgressRegister.readLastUpdateComplaint();
		System.out.println("Success Record progress " + fblProgress);
		
		try{
			BufferedReader br = new BufferedReader(new FileReader( LOG_PATH ));
			String sLine;
			boolean newflag = false;
		 
		    while ((sLine = br.readLine()) != null) 
		    {		    
	           EmailFbl fbl = new EmailFbl();
	           String[] recordArr = sLine.split("\\|");
	           fbl.timestamp = Timestamp.valueOf(recordArr[0]);
	           fbl.emailAddress = recordArr[1];
	           fbl.templateId = recordArr[2];
	           fbl.uuid = recordArr[3];
	           fbl.domain = recordArr[4];
	           fbl.recordline = sLine;
	           fblList.add(fbl);
	           if (sLine == fblProgress)
	           {
	              fblList.clear();
	           }
		    }	
		    
		    br.close();	
	     }
		 catch(Exception e){System.out.println(e.getMessage());}
	  
	     return fblList;   
      }
	
	
}	
		
	
