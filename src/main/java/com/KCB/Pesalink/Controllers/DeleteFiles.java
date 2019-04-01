package com.KCB.Pesalink.Controllers;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.SystemUtils;


/**
 * @author PMMuthama
 *
 */
public class DeleteFiles 
{
	public static void deleteCorona()
	{
		String slash="";
		
		if (SystemUtils.IS_OS_WINDOWS)
		{
			slash="\\";
		}
		else 
		{
			slash="/";
		}
		
		String currentWorkingDir = System.getProperty("user.dir");
		String inputFolder=currentWorkingDir.concat(slash+"input");		
		String outputFolder=currentWorkingDir.concat(slash+"output");
		String errorFolder=currentWorkingDir.concat(slash+"error");
		String backUpFolder=currentWorkingDir.concat(slash+"backup");
		
		File folderOutput= new File(outputFolder);
		File[] files=folderOutput.listFiles();
		for(File file:files)
		{
			//System.out.println("Name-->"+file.getName()+" SPACE --> "+file.length());
			
			if(file.length()==0)
			{
				file.delete();
			}
		}
	}
	
	public static void deleteOldBackups()
	{
		String slash="";
		
		if (SystemUtils.IS_OS_WINDOWS)
		{
			slash="\\";
		}
		else 
		{
			slash="/";
		}
		
		String currentWorkingDir = System.getProperty("user.dir");
		String inputFolder=currentWorkingDir.concat(slash+"input");		
		String outputFolder=currentWorkingDir.concat(slash+"output");
		String errorFolder=currentWorkingDir.concat(slash+"error");
		String backUpFolder=currentWorkingDir.concat(slash+"backup");
		File folderBackup=new File(backUpFolder);
		File[] files=folderBackup.listFiles();
		for(File file:files)
		{
			long lastModifiedDate=file.lastModified();
			long dateTimenow = new java.util.Date().getTime();
			
			long difftime=dateTimenow-lastModifiedDate;
			
			int diff = (int)difftime/(1000 * 60 * 60 * 24);
			if(diff>15)
			{
				file.delete();
			}
			
	
	
			
			
		}
		
		
		
	}
	
	
}
