package com.KCB.Pesalink;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.KCB.Pesalink.Controllers.PesalinkKBAController;
import com.KCB.Pesalink.Controllers.ConvertCSVtoXLSX;
import com.KCB.Pesalink.Controllers.DeleteFiles;
import com.KCB.Pesalink.Controllers.PesalinkCSNFController;
import com.KCB.Pesalink.Controllers.PesalinkFolderController;



/**
 * @author PMMuthama
 *
 */
@SpringBootApplication
public class PesalinkApplication {

	public static void main(String[] args) throws IOException 
	{
		SpringApplication.run(PesalinkApplication.class, args);
		
		String slash="";
		
		if (SystemUtils.IS_OS_WINDOWS)
		{
			slash="\\";
		}
		else 
		{
			slash="/";
		}
		PesalinkFolderController.createInputFolder();
		PesalinkFolderController.createOutputFolder();
		PesalinkFolderController.createErrorFolder();
		PesalinkFolderController.createBackupFolder();
		
		String currentWorkingDir = System.getProperty("user.dir");
		String inputFolder=currentWorkingDir.concat(slash+"input");
		
		File file = new File(inputFolder);
	
		
		while(true)
		{
					
				if(file.list().length>0)
				{
					PesalinkKBAController.executeKBA();
					PesalinkCSNFController.executeCSNF();
					DeleteFiles.deleteCorona();
					DeleteFiles.deleteOldBackups();
					//ConvertCSVtoXLSX.CSVtoXLSX();
					
					
						
				}
		
			
		}
		
			
	
		
	}

}
