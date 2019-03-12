package com.KCB.Pesalink;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.KCB.Pesalink.Controllers.PesalinkFileController;
import com.KCB.Pesalink.Controllers.PesalinkFolderController;

@SpringBootApplication
public class PesalinkApplication {

	public static void main(String[] args) throws IOException 
	{
		SpringApplication.run(PesalinkApplication.class, args);
		System.out.println("Main application started");
		PesalinkFolderController.createInputFolder();
		PesalinkFolderController.createOutputFolder();
		PesalinkFolderController.createErrorFolder();
		PesalinkFileController.readExcel();
	}

}
