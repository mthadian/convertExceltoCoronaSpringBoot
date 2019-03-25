package com.KCB.Pesalink.Controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.opencsv.CSVReader;

/**
 * @author PMMuthama
 *
 */

public class ConvertCSVtoXLSX 
{
	public static void CSVtoXLSX()
	{
		String currentWorkingDir = System.getProperty("user.dir");
		String inputFolder=currentWorkingDir.concat("\\input");	
		try {
	        String csvFileAddress = inputFolder.concat("\\KBA Session transactions report.csv"); //csv file address
	        String xlsxFileAddress = inputFolder.concat("\\KBA Session transactions report.xlsx");; //xlsx file address
	        XSSFWorkbook workBook = new XSSFWorkbook();
	        XSSFSheet sheet = workBook.createSheet("sheet1");
	        String currentLine=null;
	        int RowNum=0;
	        BufferedReader br = new BufferedReader(new FileReader(csvFileAddress));
	        while ((currentLine = br.readLine()) != null) {
	            String str[] = currentLine.split(",");
	            RowNum++;
	            XSSFRow currentRow=sheet.createRow(RowNum);
	            for(int i=0;i<str.length;i++){
	                currentRow.createCell(i).setCellValue(str[i]);
	            }
	        }

	        FileOutputStream fileOutputStream =  new FileOutputStream(xlsxFileAddress);
	        workBook.write(fileOutputStream);
	        fileOutputStream.close();
	        System.out.println("Done");
	    } catch (Exception ex) {
	        System.out.println(ex.getMessage()+"Exception in try");
	    }
	}


}
