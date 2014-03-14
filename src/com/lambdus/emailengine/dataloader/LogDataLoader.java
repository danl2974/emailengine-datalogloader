package com.lambdus.emailengine.dataloader;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class LogDataLoader {
	
	//UPDATE for Lambdus Data System
	private static String azureConnection = "jdbc:sqlserver://k22cep04af.database.windows.net:1433;database=email_engine;user=emailengine@k22cep04af;password=!Welco2200;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
	
	private static String localjdbcHandle = "jdbc:mysql://54.80.250.241:3306/email_engine";
	private static String localdbusername = "dan";
	private static String localdbpassword = "lambdus2200";
	
	
	static public void loadSuccessData(ArrayList<EmailSuccess> successList){
		
		if (successList.size() == 0){
			System.out.println("No successes to load");
			return;
			}
		
		CallableStatement callableStatement = null;
		
		try{
		  String sproc = "{call test.LoadEmailSuccess(?,?,?,?,?,?,?,?)}";
		  Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		  Connection con = DriverManager.getConnection(azureConnection);
		  callableStatement = con.prepareCall(sproc);
		  
		  for (EmailSuccess es : successList){
		      callableStatement.setTimestamp(1, es.timestamp);
		      callableStatement.setString(2, es.toAddress);
		      callableStatement.setString(3, es.outboundIP);
		      callableStatement.setString(4, es.outboundHost);
		      callableStatement.setString(5, es.remoteIP);
		      callableStatement.setString(6, es.remoteHost);
		      callableStatement.setInt(7, Integer.valueOf(es.templateId));
		      callableStatement.setString(8, es.uuid);
		      callableStatement.addBatch();
		  }
	      try{
		       callableStatement.executeBatch();
		      }catch(Exception e){System.out.println("EXCEPTION: loadSuccessData " + e.getMessage());}
	      
	      callableStatement.close();		 
		  con.close();
		  ProgressRegister.writeLastUpdateSuccess(successList.get(successList.size() - 1).mailingId);
		}
		catch(SQLException sqle){
			System.out.println("SQL EXCEPTION: loadSuccessData " + sqle.getMessage());
		}
		catch(Exception e){
			System.out.println("EXCEPTION: loadSuccessData " + e.getMessage());
		}
	}
	
	
	
	static public void loadBounceData(ArrayList<EmailBounce> bounceList){
		
		if (bounceList.size() == 0){
			System.out.println("No bounces to load");
			return;
			}
		
		CallableStatement callableStatement = null;
		
		try{
		  String sproc = "{call test.LoadEmailBounce(?,?,?,?,?,?,?,?)}";
		  Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		  Connection con = DriverManager.getConnection(azureConnection);
		  callableStatement = con.prepareCall(sproc);
		  
		  for (EmailBounce eb : bounceList){
		      callableStatement.setTimestamp(1, eb.timestamp);
		      callableStatement.setString(2, eb.toAddress);
		      callableStatement.setString(3, eb.outboundIP);
		      callableStatement.setString(4, eb.outboundHost);
		      callableStatement.setString(5, eb.remoteIP);
		      callableStatement.setString(6, eb.remoteHost);
		      callableStatement.setString(7, eb.ispResponse);
		      callableStatement.setInt(8, Integer.valueOf(eb.templateId));
		      callableStatement.addBatch();
		  }
	      try{
		       callableStatement.executeBatch();
		      }catch(Exception e){System.out.println(e.getMessage());}
		 
	      callableStatement.close();
		  con.close();
		  ProgressRegister.writeLastUpdateBounce(bounceList.get(bounceList.size() - 1).mailingId);
		  
		}
		catch(SQLException sqle){
			System.out.println("SQL EXCEPTION: loadBounceData " + sqle.getMessage());
		}
		catch(Exception e){
			System.out.println("EXCEPTION: loadBounceData " + e.getMessage());
		}
	}
	
	
	
	
	static public void loadLocalSuccessData(ArrayList<EmailSuccess> successList){
		
		if (successList.size() == 0){
			return;
			}
		
		Connection con = null;
		CallableStatement callableStatement = null;
	    try {
	    	 Class.forName("com.mysql.jdbc.Driver");
		     con = DriverManager.getConnection(localjdbcHandle, localdbusername, localdbpassword);
		     
		     String sproc = "{call addSendSuccess(?,?,?,?,?,?,?,?)}";
		     callableStatement = con.prepareCall(sproc);
		     
		     for (EmailSuccess es : successList){
		       callableStatement.setTimestamp(1, es.timestamp);
		       callableStatement.setString(2, es.toAddress);
		       callableStatement.setString(3, es.outboundIP);
		       callableStatement.setString(4, es.outboundHost);
		       callableStatement.setString(5, es.remoteIP);
		       callableStatement.setString(6, es.remoteHost);
		       callableStatement.setInt(7, Integer.valueOf(es.templateId));
		       callableStatement.setString(8, es.uuid);
		       callableStatement.addBatch();
		     }
		     callableStatement.executeBatch();
		     callableStatement.close();
		     con.close();
	    }
	    catch(Exception e){System.out.println("EXCEPTION: loadLocalSuccessData " + e.getMessage());}
	    System.out.println("Local DB Success Write Done");
	}	
	
	
	
	static public void loadLocalBounceData(ArrayList<EmailBounce> bounceList){
		
		if (bounceList.size() == 0){
			return;
			}
		
		Connection con = null;
		CallableStatement callableStatement = null;
	    try {
	    	 Class.forName("com.mysql.jdbc.Driver");
		     con = DriverManager.getConnection(localjdbcHandle, localdbusername, localdbpassword);
		     
		     String sproc = "{call addSendBounce(?,?,?,?,?,?,?,?)}";
		     callableStatement = con.prepareCall(sproc);
		     
		     for (EmailBounce eb : bounceList){
			  callableStatement.setTimestamp(1, eb.timestamp);
			  callableStatement.setString(2, eb.toAddress);
			  callableStatement.setString(3, eb.outboundIP);
			  callableStatement.setString(4, eb.outboundHost);
			  callableStatement.setString(5, eb.remoteIP);
			  callableStatement.setString(6, eb.remoteHost);
			  callableStatement.setString(7, eb.ispResponse);
			  callableStatement.setInt(8, Integer.valueOf(eb.templateId));
		      callableStatement.addBatch();
		     }
		     callableStatement.executeBatch();
		     callableStatement.close();
		     con.close();
	    }
	    catch(Exception e){System.out.println("EXCEPTION: loadLocalBounceData " + e.getMessage());}
	    System.out.println("Local DB Bounce Write Done");
	}		
	

	

	static public void loadFblData(ArrayList<EmailFbl> fblList){
		
		if (fblList.size() == 0){
			System.out.println("No fbl complaints to load");
			return;
			}
		
		CallableStatement callableStatement = null;
		
		try{
		  String sproc = "{call test.LoadEmailFbl(?,?,?,?,?)}";
		  Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		  Connection con = DriverManager.getConnection(azureConnection);
		  callableStatement = con.prepareCall(sproc);
		  
		  for (EmailFbl ef : fblList){
		      callableStatement.setTimestamp(1, ef.timestamp);
		      callableStatement.setString(2, ef.emailAddress);
		      callableStatement.setInt(3, Integer.valueOf(ef.templateId));
		      callableStatement.setString(4, ef.domain);
		      callableStatement.setString(5, ef.uuid);
		      callableStatement.addBatch();
		  }
	      try{
		       callableStatement.executeBatch();
		      }catch(Exception e){System.out.println("EXCEPTION: loadFblData " + e.getMessage());}
	      
	      callableStatement.close();		 
		  con.close();
		  ProgressRegister.writeLastUpdateComplaint(fblList.get(fblList.size() - 1).recordline);
		}
		catch(SQLException sqle){
			System.out.println("SQL EXCEPTION: loadFblData " + sqle.getMessage());
		}
		catch(Exception e){
			System.out.println("EXCEPTION: loadFblData " + e.getMessage());
		}
	}	

	
	
	static public void loadLocalFblData(ArrayList<EmailFbl> fblList){
		
		if (fblList.size() == 0){
			return;
			}
		
		Connection con = null;
		CallableStatement callableStatement = null;
	    try {
	    	 Class.forName("com.mysql.jdbc.Driver");
		     con = DriverManager.getConnection(localjdbcHandle, localdbusername, localdbpassword);
		     
		     String sproc = "{call addFblComplaint(?,?,?,?,?)}";
		     callableStatement = con.prepareCall(sproc);
		     
		     for (EmailFbl ef : fblList){
		       callableStatement.setTimestamp(1, ef.timestamp);
		       callableStatement.setString(2, ef.emailAddress);
		       callableStatement.setInt(3, Integer.valueOf(ef.templateId));
		       callableStatement.setString(4, ef.domain);
		       callableStatement.setString(5, ef.uuid);
		       callableStatement.addBatch();
		     }
		     callableStatement.executeBatch();
		     callableStatement.close();
		     con.close();
	    }
	    catch(Exception e){System.out.println("EXCEPTION: loadLocalFblData " + e.getMessage());}
	    System.out.println("Local DB FBL Write Done");
	}
	
	
}
