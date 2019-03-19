package com.KCB.Pesalink;

import java.awt.Checkbox;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.KCB.Pesalink.Controllers.PesalinkFileController;
import com.KCB.Pesalink.Controllers.PesalinkFolderController;

@SpringBootApplication
public class PesalinkApplication {

	public static void main(String[] args) throws IOException 
	{
		SpringApplication.run(PesalinkApplication.class, args);
		PesalinkFolderController.createInputFolder();
		PesalinkFolderController.createOutputFolder();
		PesalinkFolderController.createErrorFolder();
		PesalinkFolderController.createBackupFolder();
		
		String currentWorkingDir = System.getProperty("user.dir");
		String inputFolder=currentWorkingDir.concat("\\input");
		
		File file = new File(inputFolder);
		String nameString=file.getName();
		System.out.println("INPUT FOLDER "+inputFolder);
		
		
		
		while(true)
		{
			if(file.isDirectory())
			{
					
				if(file.list().length>0)
				{
					PesalinkFileController.readExcel();
					
					
						
				}
				
					
			}
			
		}
		
			
	
		
	}

}
