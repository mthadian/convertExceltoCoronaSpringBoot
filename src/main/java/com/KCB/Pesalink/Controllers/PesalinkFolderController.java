package com.KCB.Pesalink.Controllers;

import java.io.File;

public class PesalinkFolderController
{
		
		public static void createInputFolder()
		{
			String currentWorkingDir = System.getProperty("user.dir");
			
	        
	        File inputFolder = new File("input");
	        // if the directory does not exist, create it
	        if (!inputFolder.exists()) 
	        {
	            System.out.println("creating directory: " + inputFolder.getName());
	            boolean result = false;

	            try{
	            	inputFolder.mkdir();
	                result = true;
	            } 
	            catch(SecurityException se){
	                //handle it
	            }        
	            if(result) {    
	                System.out.println("inputFolder created");  
	            }
	        }
	        
			
		}
		
		
		
		public static void createOutputFolder()
		{
		
	        File outputFolder = new File("output");
	        // if the directory does not exist, create it
	        if (!outputFolder.exists()) 
	        {
	            System.out.println("creating directory: " + outputFolder.getName());
	            boolean result = false;

	            try{
	            	outputFolder.mkdir();
	                result = true;
	            } 
	            catch(SecurityException se){
	                //handle it
	            }        
	            if(result) {    
	                System.out.println("outputFolder created");  
	            }
	        }
	        
			
		}
		
		public static void createErrorFolder()
		{
		
	        File errorFolder = new File("error");
	        // if the directory does not exist, create it
	        if (!errorFolder.exists()) 
	        {
	            System.out.println("creating directory: " + errorFolder.getName());
	            boolean result = false;

	            try{
	            	errorFolder.mkdir();
	                result = true;
	            } 
	            catch(SecurityException se){
	                //handle it
	            }        
	            if(result) {    
	                System.out.println("errorFolder created");  
	            }
	        }
	        
			
		}
		
		public static void createBackupFolder()
		{
		
	        File backupFolder = new File("backup");
	        // if the directory does not exist, create it
	        if (!backupFolder.exists()) 
	        {
	            System.out.println("creating directory: " + backupFolder.getName());
	            boolean result = false;

	            try{
	            	backupFolder.mkdir();
	                result = true;
	            } 
	            catch(SecurityException se){
	                //handle it
	            }        
	            if(result) {    
	                System.out.println("backupFolder created");  
	            }
	        }
	        
			
		}
		


}
