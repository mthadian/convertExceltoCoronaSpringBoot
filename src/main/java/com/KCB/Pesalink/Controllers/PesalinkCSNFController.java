package com.KCB.Pesalink.Controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PesalinkCSNFController
{
	public static void executeCSNF() throws IOException
	{
		String currentWorkingDir = System.getProperty("user.dir");
		String inputFolder=currentWorkingDir.concat("\\input");		
		String outputFolder=currentWorkingDir.concat("\\output");
		String errorFolder=currentWorkingDir.concat("\\error");
		String backUpFolder=currentWorkingDir.concat("\\backup");
		
		InputStream ExcelFile_Reader = new FileInputStream(inputFolder.concat("\\CSNF20190225_BANK01_1.xlsx"));
		
		File folderInput= new File(inputFolder);
		File[] files=folderInput.listFiles();
		String currentFile="";
		for(File file:files)
		{
			currentFile=file.getName();
			//InputStream ExcelFile_Reader = new FileInputStream(inputFolder.concat("\\"+currentFile));
			if(currentFile.contains("CSNF") && currentFile.contains(".xlsx"))
			{
				System.out.println("CSNF EXISTS");
			}
		}
		//for end
		
		XSSFWorkbook wb = new XSSFWorkbook(ExcelFile_Reader);
		String file_name=wb.getSheetName(0);
		PrintWriter writer_vooma = new PrintWriter(outputFolder.concat("\\"+file_name+" VOOMA"+".CUT"), "UTF-8");
		PrintWriter writer_T24 = new PrintWriter(outputFolder.concat("\\"+file_name+" T24"+".CUT"), "UTF-8");
	
		String fullDate=currentFile.substring(4, 12);
		String date_yy=currentFile.substring(6, 8);
		String date_y=currentFile.substring(7, 12);
		
		System.out.println("date--yy "+date_yy );
		System.out.println("date--y "+date_y );
		
		DataFormatter formatter = new DataFormatter();		  
		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow row;
		
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
        
        int space_AfterHeader=896;
        char[] spaceAfterHeader = new char[space_AfterHeader];
        for(int a=0;a<space_AfterHeader;a++)
		{
        	spaceAfterHeader[a]=' ';
		}
        String AfterHeaderSpace = new String(spaceAfterHeader);
        writer_vooma.println("CO0T"+HeaderSortSpace+"00000000000000000000000CORONA"+CORONACSSpace+"CS"+CS07Space+"07080"+fullDate+date_yy+"000000"+AfterHeaderSpace);	
        writer_T24.println("CO0T"+HeaderSortSpace+"00000000000000000000000CORONA"+CORONACSSpace+"CS"+CS07Space+"07080"+fullDate+date_yy+"000000"+AfterHeaderSpace); 
        
        //Add Header to CORONA FILE -->
        
      //<--Add opening Balance
        
        
        
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
        
        
        int space_TWOKES=16;
        char[] spaceTWOKES = new char[space_TWOKES];
        for(int b=0;b<space_TWOKES;b++)
		{
        	spaceTWOKES[b]=' ';
		}
        String TWOKESSpace = new String(spaceTWOKES);
        
        //910
        
        int space_AfterOpenBal=910;
        char[] spaceAfterOpenBal = new char[space_AfterOpenBal];
        for(int a=0;a<space_AfterOpenBal;a++)
		{
        	spaceAfterOpenBal[a]=' ';
		}
        String AfterOpenBalSpace = new String(spaceAfterOpenBal);
        
        
        
        writer_vooma.println("CO1TKCBLKENX"+NXKCBSpace+"KCBLKENX"+NXKCBSpace+"KES1400530450001VOOMA"+VOOMAKESSpace+"KES"
        +fullDate+date_y+"0010000011"
        +fullDate+"000000000000000000C"+AfterOpenBalSpace);
        
        
        writer_T24.println("CO1TKCBLKENX"+NXKCBSpace+"KCBLKENX"+NXKCBSpace+"KES1400530450001TWO"+TWOKESSpace+"KES"
        +fullDate+date_y+"0010000011"
        +fullDate+"000000000000000000C"+AfterOpenBalSpace);
        
        //Add Opening balance -->
        
        //<-- statement running line
        double vooma_totalTransAmount=0;
        double T24_totalTransAmount=0;
        int lineNumber_vooma=2;
        int lineNumber_T24=2;
        
        double cell_TranType;
        for(int i=1;i<lastRow;i++)
        {
        	row=sheet.getRow(i);
        	cell_TranType=row.getCell(61).getNumericCellValue();
        	
        	double cell_Mti=row.getCell(36).getNumericCellValue();
        	double cell_AcqId=row.getCell(4).getNumericCellValue();
        	double cell_TranAmount=row.getCell(56).getNumericCellValue();
        	
        	
        	int transType = (int) cell_TranType;
        	int mti= (int) cell_Mti;
        	int acqId= (int) cell_AcqId;
        	int tranAmount= (int) cell_TranAmount;
        	
        	if(transType!=50)      		
        		
        	{
        		//START VOOMA STATEMENT LINE
        		if(acqId==1 || transType==26 )///vooma
        		{
        			String D_or_C="";
                	if((transType==26 && mti==1200) || (transType==10 && mti==1420))
                	{
                		D_or_C="C";
                	}
                
                	if((transType==26 && mti==1420) || (transType==10 && mti==1200))
                	{
                		D_or_C="D";
                	}
                	
                	
                	int length_lineNumber = String.valueOf(lineNumber_vooma).length();
		        	int rem0lineNumber=4-length_lineNumber;
		        	char[] arr0 = new char[rem0lineNumber];
		        	if(length_lineNumber<12)
		        	{
		        		for(int x=0;x<rem0lineNumber;x++)
		        		{
		        			arr0[x]='0';
		        		}	
		        		
		        	}
		        	
		        	String zeros_LineNumber = new String(arr0);
		        	
		        	double transamount_absoluteVal=Math.abs(cell_TranAmount);
		        	//int amount = (int) (transamount_absoluteVal*10000);
		        	BigDecimal amount= new BigDecimal(transamount_absoluteVal*10000);
		        	
		        	int length_Amount = String.valueOf(amount).length();
		        	
		        	 int rem0Amount=18-length_Amount;
		        	 char[] arr0Amount = new char[rem0Amount];
			        	if(length_Amount<18)
			        	{
			        		for(int x=0;x<rem0Amount;x++)
			        		{
			        			arr0Amount[x]='0';
			        		}	
			        		
			        	}
			        	String zeros_Amount = new String(arr0Amount);	
			        	
			        	
			        	double cell_TranDateTime=row.getCell(58).getNumericCellValue();		        		
		        		BigDecimal TranDateTime= new BigDecimal(cell_TranDateTime);		        		
		        		String str_TranDateTime = String.valueOf(TranDateTime);
		        		String tranDate=str_TranDateTime.substring(0, 8);
		        		
		        		double cell_SettlDate =row.getCell(51).getNumericCellValue();		        		
		        		BigDecimal SettlDate = new BigDecimal(cell_SettlDate);		        		
		        		
		        		
		        	
		        	writer_vooma.println("CO2TKCBLKENX"+NXKCBSpace+"KCBLKENX"+NXKCBSpace+"KES1400530450001VOOMA"+VOOMAKESSpace+"KES"+fullDate+date_y
		        			+"00100"+zeros_LineNumber+lineNumber_vooma+"2"+zeros_Amount+amount+D_or_C+" "+tranDate+SettlDate);
                	
                	
                	
                	
                	
                	
                	System.out.println("Transtype "+transType+" Mti--> "+mti+" D_or_C "+D_or_C+" acqId "+acqId+" tranAmount "+tranAmount+" tranDate "+tranDate+" SettlDate "+SettlDate);
                	lineNumber_vooma=lineNumber_vooma+1;
        			
        		}
        		//END VOOMA STATEMENT LINE
        		
        		//START T24 STATEMENT LINE
        		else ///T24
        		{
        			System.out.println("T24");
				}
        		
        		//END T24 STATEMENT LINE
        		
        		
        	}
        	//transtype!=50 end
        	
        	
        	
        }
        
        
        // statement running line
        
        
        
        writer_vooma.close();
        writer_T24.close();

     
		
		
		
	}

}
