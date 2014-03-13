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
		System.out.println("Fbl Record progress " + fblProgress);
		
		try{
			BufferedReader br = new BufferedReader(new FileReader( LOG_PATH ));
			String sLine;
			boolean newflag = false;
		 
		    while ((sLine = br.readLine()) != null) 
		    {		    
	           EmailFbl fbl = new EmailFbl();
	           String[] recordArr = sLine.split("::");
	           fbl.timestamp = Timestamp.valueOf(recordArr[0].trim());
	           fbl.emailAddress = recordArr[1].trim();
	           fbl.templateId = recordArr[2].trim();
	           fbl.uuid = recordArr[3].trim();
	           fbl.domain = recordArr[4].trim();
	           fbl.recordline = sLine;
	           fblList.add(fbl);
	           if (fblProgress.indexOf(sLine) >= 0)
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
		
	
