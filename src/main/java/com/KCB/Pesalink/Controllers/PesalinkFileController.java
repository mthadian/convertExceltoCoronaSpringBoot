package com.KCB.Pesalink.Controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.FileUtils;

public class PesalinkFileController 
{
	public static void readExcel() throws IOException
	{
		String currentWorkingDir = System.getProperty("user.dir");
		String inputFolder=currentWorkingDir.concat("\\input");
		System.out.println("inputFolder folder path--- "+inputFolder);
		
		String outputFolder=currentWorkingDir.concat("\\output");	 //cd to outputfolder	
		InputStream ExcelFileToRead = new FileInputStream(inputFolder.concat("\\KBA SESSION TRANSACTION REPORT2.xlsx"));
		
		XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
		String file_name=wb.getSheetName(0);
		PrintWriter writer = new PrintWriter(outputFolder.concat("\\"+file_name+".CUT"), "UTF-8");
	
		  
		  XSSFSheet sheet = wb.getSheetAt(0);
		  
	        XSSFRow row; 
	        XSSFCell cell;
	        double cell_TransactionId;
	        double cell_TransactionAmt;
	        double cell_TranCcy;
	        double cell_openBalDate;
	        XSSFCell cell_TranType;
	        XSSFCell cell_TranName;
	        XSSFCell cell_TotalFee;
	        XSSFCell cell_TotalFeeSign;
	        XSSFCell cell_ServiceFee;
	        XSSFCell cell_ServiceFeeSign;
	        
	        
	        
	        int lastRow = sheet.getLastRowNum();
	        
	        //<--Add Header to CORONA FILE
	        
	        int space_HeaderSort=60;
	        char[] spaceHS = new char[space_HeaderSort];
	        for(int x=0;x<space_HeaderSort;x++)
    		{
	        	spaceHS[x]=' ';
    		}
	        String HeaderSortSpace = new String(spaceHS);
	        
	        int space_CORONACS=4;
	        char[] spaceCS = new char[space_CORONACS];
	        for(int y=0;y<space_CORONACS;y++)
    		{
	        	spaceCS[y]=' ';
    		}
	        String CORONACSSpace = new String(spaceCS);
	        
	        int space_CS07=8;
	        char[] space07 = new char[space_CS07];
	        for(int z=0;z<space_CS07;z++)
    		{
	        	space07[z]=' ';
    		}
	        String CS07Space = new String(space07);
	        
	        //<--Get date as string
	        SimpleDateFormat DtFormat = new SimpleDateFormat("dd/MM/yyyy");
	        XSSFCell sheetDate=wb.getSheetAt(0).getRow(2).getCell(1);	//get date from this cell 
	        Date date=sheetDate.getDateCellValue();
	        String dateString=DtFormat.format(date);
	        String raw_date= new String(dateString);
	        String year=raw_date.substring(6,10);
	        String month=raw_date.substring(3, 5);
	        String day=raw_date.substring(0,2);
	        String year_suffix=raw_date.substring(8,10);
	        //get date as string -->
	     
	        int space_AfterHeader=896;
	        char[] spaceAfterHeader = new char[space_AfterHeader];
	        for(int a=0;a<space_AfterHeader;a++)
    		{
	        	spaceAfterHeader[a]=' ';
    		}
	        String AfterHeaderSpace = new String(spaceAfterHeader);
	        
	    
	        writer.println("CO0O"+HeaderSortSpace+"00000000000000000000000CORONA"+CORONACSSpace+"CS"+CS07Space+"07070"+year+month+day+year_suffix+"000000"+AfterHeaderSpace);	
	        
	      //Add Header to CORONA FILE -->
	        
	       //<--Add opening Balance
	        XSSFRow row_openBal=sheet.getRow(9);
	        
	        
	        cell_openBalDate = row_openBal.getCell(33).getNumericCellValue();
	        int transOpenDate = (int) cell_openBalDate;
	        String strDate = String.valueOf(transOpenDate);
	        String openBal_year=strDate.substring(0,4);
	        String openBal_year_y=strDate.substring(3,4);
	        String openBal_month=strDate.substring(4,6);
	        String openBal_day=strDate.substring(6,8);
	        
	        
	        System.out.println("Opening balance Date year "+openBal_year);
	        System.out.println("Opening balance Date month "+openBal_month);
	        System.out.println("Opening balance Date day "+openBal_day);
	        
	        //Generate space between NX&KCB
	        int space_NXKCB=3;
	        char[] spaceNXKCB = new char[space_NXKCB];
	        for(int b=0;b<space_NXKCB;b++)
    		{
	        	spaceNXKCB[b]=' ';
    		}
	        String NXKCBSpace = new String(spaceNXKCB);
	        
	      //Generate space between NX&KES
	        //space between NX&KCB and NX&KES is equal so reuse var->NXKCBSpace
	        
	        int space_VOOMAKES=14;
	        char[] spaceVOOMAKES = new char[space_VOOMAKES];
	        for(int b=0;b<space_VOOMAKES;b++)
    		{
	        	spaceVOOMAKES[b]=' ';
    		}
	        String VOOMAKESSpace = new String(spaceVOOMAKES);
	        
	        //910
	        
	        int space_AfterOpenBal=910;
	        char[] spaceAfterOpenBal = new char[space_AfterOpenBal];
	        for(int a=0;a<space_AfterOpenBal;a++)
    		{
	        	spaceAfterOpenBal[a]=' ';
    		}
	        String AfterOpenBalSpace = new String(spaceAfterOpenBal);
	        
	        
	        
	        writer.println("CO1OKCBLKENX"+NXKCBSpace+"KCBLKENX"+NXKCBSpace+"KES1400530450001VOOMA"+VOOMAKESSpace+"KES"+openBal_year+openBal_month
	        		+openBal_day+openBal_year_y+openBal_month+openBal_day+"0010000011"
	        		+openBal_year+openBal_month+openBal_day+"000000000000000000C"+AfterOpenBalSpace);
	        
	        
	        
	       
	        
	        
	        //Add Opening balance -->
	        
	        //<--body
	        int lineNumber=2;
	        for(int i=9;i<=30;i++)
	        {
	        	row=sheet.getRow(i);
	        	
	        	cell_TransactionId = row.getCell(0).getNumericCellValue();
	        	cell_TransactionAmt=row.getCell(1).getNumericCellValue();
	        	//cell_TranCcy=row.getCell(2).getNumericCellValue();
	        	
	        	
	        	
	        	int length_lineNumber = String.valueOf(lineNumber).length();
	        	
	        	
	        	
	        	
	        	int rem0=4-length_lineNumber;
	        	char[] arr0 = new char[rem0];
	        	if(length_lineNumber<12)
	        	{
	        		for(int x=0;x<rem0;x++)
	        		{
	        			arr0[x]='0';
	        		}	
	        		
	        	}
	        	
	        	String zeros_LineNumber = new String(arr0);
	        	//System.out.println("Line number counter "+lineNumber+" linu number length "+length_lineNumber+" Trailing zeros"+trailingZero+" String"+length_lnString);
	       
	        	
	        	int transamount = (int) cell_TransactionAmt;
	        	int transamount_absoluteVal=Math.abs(transamount);
	        	String D_or_C="";
	        	if(transamount<0)
	        	{
	        		D_or_C="C";
	        		
	        	}
	        	else
	        	{
	        		D_or_C="D";
	        	}
	        
	        	System.out.println("transamount balance iterate "+transamount+" "+D_or_C+" Absolute value "+transamount_absoluteVal);
	        	
	        	writer.println("CO2OKCBLKENX"+NXKCBSpace+"KCBLKENX"+NXKCBSpace+"KES1400530450001VOOMA"+VOOMAKESSpace+"KES"+openBal_year+openBal_month
		        		+openBal_day+openBal_year_y+openBal_month+openBal_day+"00100"+zeros_LineNumber+lineNumber+"2");
	        	
	        	lineNumber=lineNumber+1;
	        	
	        }
	        //body-->
	        writer.close();
	        
	        System.out.println("LAST ROOOOOOOW "+lastRow);
	 
	}

}
