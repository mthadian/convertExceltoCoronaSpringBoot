package com.KCB.Pesalink.Controllers;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * @author PMMuthama
 *
 */
public class DeleteFiles 
{
	public static void deleteCorona()
	{
		String currentWorkingDir = System.getProperty("user.dir");
		String outputFolder=currentWorkingDir.concat("\\output");
		
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
		String currentWorkingDir = System.getProperty("user.dir");
		String backupFolder=currentWorkingDir.concat("\\backup");
		
		File folderBackup=new File(backupFolder);
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
