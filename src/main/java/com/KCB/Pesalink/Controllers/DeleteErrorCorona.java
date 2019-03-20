package com.KCB.Pesalink.Controllers;

import java.io.File;

public class DeleteErrorCorona 
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
	
	
}
