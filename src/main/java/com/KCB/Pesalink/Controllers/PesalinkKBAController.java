package com.KCB.Pesalink.Controllers;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.compress.compressors.FileNameUtil;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PesalinkKBAController 
{
	
	public static void executeKBA() throws IOException
	{
		String currentWorkingDir = System.getProperty("user.dir");
		String inputFolder=currentWorkingDir.concat("\\input");		
		String outputFolder=currentWorkingDir.concat("\\output");
		String errorFolder=currentWorkingDir.concat("\\error");
		String backUpFolder=currentWorkingDir.concat("\\backup");
	
		//InputStream ExcelFileToRead = new FileInputStream(inputFolder.concat("\\KBA SESSION TRANSACTION REPORT2.xlsx"));
		
		File folderInput= new File(inputFolder);
		File[] files=folderInput.listFiles();
		for(File file:files)
		{
			String currentFile=file.getName();
			InputStream ExcelFile_Reader = new FileInputStream(inputFolder.concat("\\"+currentFile));
			if(currentFile.contains("KBA") && currentFile.contains(".xlsx"))
			{
				try
				{
					System.out.println("CURRENT FILE PROCESSING IS "+currentFile);
				
					XSSFWorkbook wb = new XSSFWorkbook(ExcelFile_Reader);
					String file_name=wb.getSheetName(0);
					PrintWriter writer_vooma = new PrintWriter(outputFolder.concat("\\"+file_name+" VOOMA"+".CUT"), "UTF-8");
					PrintWriter writer_T24 = new PrintWriter(outputFolder.concat("\\"+file_name+" T24"+".CUT"), "UTF-8");
					
					DataFormatter formatter = new DataFormatter();
					
					
				
					  
					  XSSFSheet sheet = wb.getSheetAt(0);
					  
				        XSSFRow row; 
				     
				        String cell_TransactionId;
				        double cell_TransactionAmt;
				       
				        double cell_openBalDate;
				        String cell_TranDate;
				        double cell_TranType;
				        double cell_Mti;
				     
				        
				        
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
				        String year_y=raw_date.substring(9,10);
				        //get date as string -->
				     
				        int space_AfterHeader=896;
				        char[] spaceAfterHeader = new char[space_AfterHeader];
				        for(int a=0;a<space_AfterHeader;a++)
			    		{
				        	spaceAfterHeader[a]=' ';
			    		}
				        String AfterHeaderSpace = new String(spaceAfterHeader);
				        
				    
				        writer_vooma.println("CO0O"+HeaderSortSpace+"00000000000000000000000CORONA"+CORONACSSpace+"CS"+CS07Space+"07080"+year+month+day+year_suffix+"000000"+AfterHeaderSpace);	
				        writer_T24.println("CO0O"+HeaderSortSpace+"00000000000000000000000CORONA"+CORONACSSpace+"CS"+CS07Space+"07080"+year+month+day+year_suffix+"000000"+AfterHeaderSpace);	

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
				        +year+month+day+year_y+month+day+"0010000011"
				        +year+month+day+"000000000000000000C"+AfterOpenBalSpace);
				        
				        
				        writer_T24.println("CO1TKCBLKENX"+NXKCBSpace+"KCBLKENX"+NXKCBSpace+"KES1400530450001TWO"+TWOKESSpace+"KES"
				        +year+month+day+year_y+month+day+"0010000011"
				        +year+month+day+"000000000000000000C"+AfterOpenBalSpace);
				        
				        
				       
				        
				        
				        //Add Opening balance -->
				        
				        //<--body
				        
				        double vooma_totalTransAmount=0;
				        double T24_totalTransAmount=0;
				        int lineNumber_vooma=2;
				        int lineNumber_T24=2;
				        for(int i=9;i<=lastRow;i++)
				        {
				        	row=sheet.getRow(i);
				        	
				        	//cell_TransactionId = row.getCell(0).getNumericCellValue();
				        	cell_TransactionId=formatter.formatCellValue(row.getCell(0,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
				        	
				        	cell_TransactionAmt=row.getCell(1).getNumericCellValue();
				        	
				        	cell_TranType=row.getCell(4).getNumericCellValue();
				        	int transType = (int) cell_TranType;
				        	cell_Mti=row.getCell(3).getNumericCellValue();
				        	int Mti = (int) cell_Mti;
				        	
				        	String stan=formatter.formatCellValue(row.getCell(34,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
				        	String tranName=formatter.formatCellValue(row.getCell(5,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
				        	String senderName=formatter.formatCellValue(row.getCell(40,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
				        	
				        	if(transType!=50)
				        	{
				        		//START VOOMA STATEMENTLINE
				        		if(tranName.contains("VO") || tranName.contains("EC"))
				        		{
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
						        	
						        	String D_or_C="";
						        	if((transType==10 && Mti==1200) ||(transType==26 && Mti==1420) )
						        	{
						        		D_or_C="D";
						        	}
						        	else if((transType==10 && Mti==1420) ||(transType==26 && Mti==1200))
						        	{
						        		D_or_C="C";
						        		
						        	}
						        	
						        	double transamount_absoluteVal=Math.abs(cell_TransactionAmt);
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
							        	
							        	String senderAcc=formatter.formatCellValue(row.getCell(41,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
							        	
							        	BigDecimal int_senderAcc;
							        	
							        	if(senderAcc.contains("+"))
							        	{
							        		double d_senderAcc=row.getCell(41).getNumericCellValue();
							        		// int_senderAcc = (int) d_senderAcc;
							        		  int_senderAcc= new BigDecimal(d_senderAcc);
							        		 senderAcc=String.valueOf(int_senderAcc);
							        		
							        	}
							        	
							        	int length_senderAcc=senderAcc.length();
							        	int rem0senderAcc=16-length_senderAcc;
							        	 char[] arr0senderAcc = new char[rem0senderAcc];
								        	if(length_senderAcc<16)
								        	{
								        		for(int x=0;x<rem0senderAcc;x++)
								        		{
								        			arr0senderAcc[x]=' ';
								        			//arr0senderAcc[x]='0';
								        		}	
								        		
								        	}
								        	
								        	String space_senderAcc = new String(arr0senderAcc);
								        	
								        	//36
								        	
								        	cell_TranDate = formatter.formatCellValue(row.getCell(36));
									        String str_tranDate = String.valueOf(cell_TranDate);
									        String tranDate_FullDate=cell_TranDate.substring(0,8);
									        String tranDate_Time=cell_TranDate.substring(9,15);
									        
									        //30
									        double D_RRN=row.getCell(29).getNumericCellValue();
							        		//int  int_RRN = (int) D_RRN;
							        		BigDecimal RRN= new BigDecimal(D_RRN);
							        		
							        		int length_RRN = String.valueOf(RRN).length();
							        		int rem0RRN=16-length_RRN;
								        	 char[] arr0RRN = new char[rem0RRN];
									        	if(length_RRN<16)
									        	{
									        		for(int x=0;x<rem0RRN;x++)
									        		{
									        			arr0RRN[x]=' ';
									        		}	
									        		
									        	}
									        	
									        	String spaces_RRN = new String(arr0RRN);
									        	
									        	int space_TType_UTType_FCode_DepId=14;
										        char[] spaceTType_UTType_FCode_DepId = new char[space_TType_UTType_FCode_DepId];
										        for(int x=0;x<space_TType_UTType_FCode_DepId;x++)
									    		{
										        	spaceTType_UTType_FCode_DepId[x]=' ';
									    		}
										        String TType_UTType_FCode_DepIdSpace = new String(spaceTType_UTType_FCode_DepId);
										        
										       // int transactionId = (int) cell_TransactionId;
										        int length_transactionId=String.valueOf(cell_TransactionId).length();
										        int rem0transactionId=41-length_transactionId;
									        	 char[] arr0transactionId = new char[rem0transactionId];
										        	if(length_transactionId<41)
										        	{
										        		for(int x=0;x<rem0transactionId;x++)
										        		{
										        			arr0transactionId[x]=' ';
										        		}	
										        		
										        	}
										        	
										        	String space_transactionId = new String(arr0transactionId);
										        	
										        	//checks if receriverinstcode is null or not
										        	String receiverInstCode=formatter.formatCellValue(row.getCell(38,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
										        	String senderInstCode=formatter.formatCellValue(row.getCell(39,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
										        	
										        	
										        	String SenderReceiverInstCode="R"+String.valueOf(receiverInstCode)+"  "+"S"+String.valueOf(senderInstCode);
										        	int length_senderReceiver=SenderReceiverInstCode.length();
										        	int remSpacesenderReceiver=400-length_senderReceiver;
										        	 char[] arrSpacesenderReceiver = new char[remSpacesenderReceiver];
											        	if(length_senderReceiver<400)
											        	{
											        		for(int x=0;x<remSpacesenderReceiver;x++)
											        		{
											        			arrSpacesenderReceiver[x]=' ';
											        		}	
											        		
											        	}
											        	
											        	String space_senderReceiver = new String(arrSpacesenderReceiver);
											        	
											        	String stanTransSenderName= stan+tranName+senderName;
											        	int length_stanTransSenderName=stanTransSenderName.length();
											        	int remSpacestanTransSenderName=250-length_stanTransSenderName;
											        	 char[] arrSpacestanTransSenderName = new char[remSpacestanTransSenderName];
												        	if(length_stanTransSenderName<250)
												        	{
												        		for(int x=0;x<remSpacestanTransSenderName;x++)
												        		{
												        			arrSpacestanTransSenderName[x]=' ';
												        		}	
												        		
												        	}
												        	
												        	String space_stanTransSenderName = new String(arrSpacestanTransSenderName);
												        	
												        	int space_AfterSenderName=132;
												 	        char[] spaceAfterSenderName = new char[space_AfterSenderName];
												 	        for(int a=0;a<space_AfterSenderName;a++)
												     		{
												 	        	spaceAfterSenderName[a]=' ';
												     		}
												 	        String SenderNameSpace = new String(spaceAfterSenderName);
											        	
											        	
							        		
									        
									       
						        		
						        	
						        
						        	
						        	
							        	writer_vooma.println("CO2TKCBLKENX"+NXKCBSpace+"KCBLKENX"+NXKCBSpace+"KES1400530450001VOOMA"+VOOMAKESSpace+"KES"+year+month+day+year_y+month+day
							        			+"00100"+zeros_LineNumber+lineNumber_vooma+"2"+zeros_Amount+amount+D_or_C+" "
							        		+openBal_year+openBal_month+openBal_day+openBal_year+openBal_month+openBal_day+senderAcc+space_senderAcc+tranDate_FullDate
							        		+tranDate_Time+"  "+senderAcc+space_senderAcc+RRN+spaces_RRN+TType_UTType_FCode_DepIdSpace+cell_TransactionId+space_transactionId+SenderReceiverInstCode
							        		+space_senderReceiver+stanTransSenderName+space_stanTransSenderName+SenderNameSpace);
							        	
							        	vooma_totalTransAmount=vooma_totalTransAmount+cell_TransactionAmt;
						        	
							        	lineNumber_vooma=lineNumber_vooma+1;
				        			
				        			
				        			
				        		}
				        		//END VOOMA STATEMENTLINE
				        		
				        		//START T24 STATEMENTLINE
				        		if(tranName.contains("P2P"))
				        		{
				        			int length_lineNumber = String.valueOf(lineNumber_T24).length();
						        	
						        	
						        	
						        	
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
						        	
						        	String D_or_C="";
						        	if((transType==10 && Mti==1200) ||(transType==26 && Mti==1420) )
						        	{
						        		D_or_C="D";
						        	}
						        	else if((transType==10 && Mti==1420) ||(transType==26 && Mti==1200))
						        	{
						        		D_or_C="C";
						        		
						        	}
						        	
						        	double transamount_absoluteVal=Math.abs(cell_TransactionAmt);
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
							        	
							        	String senderAcc=formatter.formatCellValue(row.getCell(41,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
							        	
							        	BigDecimal int_senderAcc;
							        	
							        	if(senderAcc.contains("+"))
							        	{
							        		
							        		double d_senderAcc=row.getCell(41).getNumericCellValue();
							        		// int_senderAcc = (int) d_senderAcc;
							        		  int_senderAcc= new BigDecimal(d_senderAcc);
							        		 senderAcc=String.valueOf(int_senderAcc);
							        		
							        	}
							        	
							        	int length_senderAcc=senderAcc.length();
							        	int rem0senderAcc=16-length_senderAcc;
							        	 char[] arr0senderAcc = new char[rem0senderAcc];
								        	if(length_senderAcc<16)
								        	{
								        		for(int x=0;x<rem0senderAcc;x++)
								        		{
								        			arr0senderAcc[x]=' ';
								        			//arr0senderAcc[x]='0';
								        		}	
								        		
								        	}
								        	
								        	String space_senderAcc = new String(arr0senderAcc);
								        	
								        	//36
								        	
								        	cell_TranDate = formatter.formatCellValue(row.getCell(36));
									        String str_tranDate = String.valueOf(cell_TranDate);
									        String tranDate_FullDate=cell_TranDate.substring(0,8);
									        String tranDate_Time=cell_TranDate.substring(9,15);
									        
									        //30
									        double D_RRN=row.getCell(29).getNumericCellValue();
							        		//int  int_RRN = (int) D_RRN;
							        		BigDecimal RRN= new BigDecimal(D_RRN);
							        		
							        		int length_RRN = String.valueOf(RRN).length();
							        		int rem0RRN=16-length_RRN;
								        	 char[] arr0RRN = new char[rem0RRN];
									        	if(length_RRN<16)
									        	{
									        		for(int x=0;x<rem0RRN;x++)
									        		{
									        			arr0RRN[x]=' ';
									        		}	
									        		
									        	}
									        	
									        	String spaces_RRN = new String(arr0RRN);
									        	
									        	int space_TType_UTType_FCode_DepId=14;
										        char[] spaceTType_UTType_FCode_DepId = new char[space_TType_UTType_FCode_DepId];
										        for(int x=0;x<space_TType_UTType_FCode_DepId;x++)
									    		{
										        	spaceTType_UTType_FCode_DepId[x]=' ';
									    		}
										        String TType_UTType_FCode_DepIdSpace = new String(spaceTType_UTType_FCode_DepId);
										        
										       // int transactionId = (int) cell_TransactionId;
										        int length_transactionId=String.valueOf(cell_TransactionId).length();
										        int rem0transactionId=41-length_transactionId;
									        	 char[] arr0transactionId = new char[rem0transactionId];
										        	if(length_transactionId<41)
										        	{
										        		for(int x=0;x<rem0transactionId;x++)
										        		{
										        			arr0transactionId[x]=' ';
										        		}	
										        		
										        	}
										        	
										        	String space_transactionId = new String(arr0transactionId);
										        	
										        	//checks if receriverinstcode is null or not
										        	String receiverInstCode=formatter.formatCellValue(row.getCell(38,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
										        	String senderInstCode=formatter.formatCellValue(row.getCell(39,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
										        	
										        	
										        	String SenderReceiverInstCode="R"+String.valueOf(receiverInstCode)+"  "+"S"+String.valueOf(senderInstCode);
										        	int length_senderReceiver=SenderReceiverInstCode.length();
										        	int remSpacesenderReceiver=400-length_senderReceiver;
										        	 char[] arrSpacesenderReceiver = new char[remSpacesenderReceiver];
											        	if(length_senderReceiver<400)
											        	{
											        		for(int x=0;x<remSpacesenderReceiver;x++)
											        		{
											        			arrSpacesenderReceiver[x]=' ';
											        		}	
											        		
											        	}
											        	
											        	String space_senderReceiver = new String(arrSpacesenderReceiver);
											        	
											        	String stanTransSenderName= stan+tranName+senderName;
											        	int length_stanTransSenderName=stanTransSenderName.length();
											        	int remSpacestanTransSenderName=250-length_stanTransSenderName;
											        	 char[] arrSpacestanTransSenderName = new char[remSpacestanTransSenderName];
												        	if(length_stanTransSenderName<250)
												        	{
												        		for(int x=0;x<remSpacestanTransSenderName;x++)
												        		{
												        			arrSpacestanTransSenderName[x]=' ';
												        		}	
												        		
												        	}
												        	
												        	String space_stanTransSenderName = new String(arrSpacestanTransSenderName);
												        	
												        	int space_AfterSenderName=132;
												 	        char[] spaceAfterSenderName = new char[space_AfterSenderName];
												 	        for(int a=0;a<space_AfterSenderName;a++)
												     		{
												 	        	spaceAfterSenderName[a]=' ';
												     		}
												 	        String SenderNameSpace = new String(spaceAfterSenderName);
											
						        	
							        	writer_T24.println("CO2TKCBLKENX"+NXKCBSpace+"KCBLKENX"+NXKCBSpace+"KES1400530450001TWO"+TWOKESSpace+"KES"+year+month+day+year_y+month+day
							        			+"00100"+zeros_LineNumber+lineNumber_T24+"2"+zeros_Amount+amount+D_or_C+" "
							        		+openBal_year+openBal_month+openBal_day+openBal_year+openBal_month+openBal_day+senderAcc+space_senderAcc+tranDate_FullDate
							        		+tranDate_Time+"  "+senderAcc+space_senderAcc+RRN+spaces_RRN+TType_UTType_FCode_DepIdSpace+cell_TransactionId+space_transactionId+SenderReceiverInstCode
							        		+space_senderReceiver+stanTransSenderName+space_stanTransSenderName+SenderNameSpace);
							        	
							        	T24_totalTransAmount=T24_totalTransAmount+cell_TransactionAmt;
						        	
							        	lineNumber_T24=lineNumber_T24+1;
				        			
									
								}
				        		//END T24 STATEMENTLINE
				        		
				        		
				        		
				        		
				        	}
				        	
				        	
				        	
				        	
				        	
				        	
				        	
				        	
				        }
				        //body-->
				        
				        
				        vooma_totalTransAmount=Math.round(vooma_totalTransAmount*100)/100.00;
				        T24_totalTransAmount=Math.round(T24_totalTransAmount*100)/100.00;
				        
				        String vooma_Total_DorC="";
				        if(vooma_totalTransAmount<0)
				        {
				        	vooma_Total_DorC="D";
				        }
				        else 
				        {
				        	vooma_Total_DorC="C";
				        	
						}
				        
				        
				        String T24_Total_DorC="";
				        if(T24_totalTransAmount<0)
				        {
				        	T24_Total_DorC="D";
				        }
				        else 
				        {
				        	T24_Total_DorC="C";
				        	
						}
				        
				        double vooma_transamount_absoluteVal=Math.abs(vooma_totalTransAmount);
			        	BigDecimal voomaAmountBigDecimal= new BigDecimal(vooma_transamount_absoluteVal*10000);
			        	
			        	double T24_transamount_absoluteVal=Math.abs(T24_totalTransAmount);
			        	BigDecimal T24AmountBigDecimal= new BigDecimal(T24_transamount_absoluteVal*10000);
			        	
			        	
			        	int length_voomaAmount = String.valueOf(voomaAmountBigDecimal).length();
			        	
			        	int length_T24Amount = String.valueOf(T24AmountBigDecimal).length();
			        	
			        	 int rem0voomaAmount=18-length_voomaAmount;
			        	 char[] arr0voomaAmount = new char[rem0voomaAmount];
				        	if(length_voomaAmount<18)
				        	{
				        		for(int x=0;x<rem0voomaAmount;x++)
				        		{
				        			arr0voomaAmount[x]='0';
				        		}	
				        		
				        	}
				        	
				        	String zeros_voomaAmount = new String(arr0voomaAmount);
				        	
				        	int rem0T24Amount=18-length_T24Amount;
				        	 char[] arr0T24Amount = new char[rem0T24Amount];
					        	if(length_T24Amount<18)
					        	{
					        		for(int x=0;x<rem0T24Amount;x++)
					        		{
					        			arr0T24Amount[x]='0';
					        		}	
					        		
					        	}
					        	
					        	String zeros_T24Amount = new String(arr0T24Amount);
				        
				        
				        
				        
				        
				        
				        int last_lineNumber_vooma=lineNumber_vooma;
				        int last_lineNumber_T24=lineNumber_T24;
				        
				        int length_lineNumberVOOMA = String.valueOf(last_lineNumber_vooma).length();
				        int length_lineNumberT24 = String.valueOf(last_lineNumber_T24).length();
			        	
			        	
			        	
			        	
			        	int rem0lineNumberVOOMA=4-length_lineNumberVOOMA;
			        	char[] arr0 = new char[rem0lineNumberVOOMA];
			        	if(length_lineNumberVOOMA<4)
			        	{
			        		for(int x=0;x<rem0lineNumberVOOMA;x++)
			        		{
			        			arr0[x]='0';
			        		}	
			        		
			        	}
			        	
			        	String zeros_LineNumberVOOMA = new String(arr0);
			        	
			        	int rem0lineNumberT24=4-length_lineNumberT24;
			        	char[] arr0T24 = new char[rem0lineNumberT24];
			        	if(length_lineNumberT24<4)
			        	{
			        		for(int x=0;x<rem0lineNumberT24;x++)
			        		{
			        			arr0T24[x]='0';
			        		}	
			        		
			        	}
			        	
			        	String zeros_LineNumberT24 = new String(arr0T24);
			        	
			        	String vooma_dummyString="DUMMY           ";
			        	
			        	int space_AfterDummyVooma=885;
			 	        char[] spaceAfterDummyVooma = new char[space_AfterDummyVooma];
			 	        for(int a=0;a<space_AfterDummyVooma;a++)
			     		{
			 	        	spaceAfterDummyVooma[a]=' ';
			     		}
			 	        String vooma_spaceDummy = new String(spaceAfterDummyVooma);
			 	        
			 	       String T24_dummyString="DUMMY           ";
			       	
			 	       	int space_AfterDummyT24=885;
				        char[] spaceAfterDummyT24 = new char[space_AfterDummyT24];
				        for(int a=0;a<space_AfterDummyT24;a++)
			    		{
				        	spaceAfterDummyT24[a]=' ';
			    		}
				        String T24_spaceDummy = new String(spaceAfterDummyT24);
			 	        
			 	        
				        
				        //<--OUTPUT SUM OF ENTRIES
				        writer_vooma.println("CO2TKCBLKENX"+NXKCBSpace+"KCBLKENX"+NXKCBSpace+"KES1400530450001VOOMA"+VOOMAKESSpace+"KES"+year+month+day+year_y+month+day
				        		+"00100"+zeros_LineNumberVOOMA+last_lineNumber_vooma+"2"+zeros_voomaAmount+voomaAmountBigDecimal+vooma_Total_DorC+" "
				        		+year+month+day+year+month+day+vooma_dummyString+vooma_spaceDummy);
				        
				        writer_T24.println("CO2TKCBLKENX"+NXKCBSpace+"KCBLKENX"+NXKCBSpace+"KES1400530450001TWO"+TWOKESSpace+"KES"+year+month+day+year_y+month+day
				        		+"00100"+zeros_LineNumberT24+last_lineNumber_T24+"2"+zeros_T24Amount+T24AmountBigDecimal+T24_Total_DorC+" "
				        		+year+month+day+year+month+day+T24_dummyString+T24_spaceDummy);
				        
				        
				        //OUTPUT SUM OF ENTRIES-->
				        
				       //<--OUTPUT CLOSING BALANCE
				        
				        int closingLineNumberVOOMA=last_lineNumber_vooma+1;
				        int length_closingLineNumberVOOMA = String.valueOf(closingLineNumberVOOMA).length();
			        
			        	int rem0closingLineNumberVOOMA=4-length_closingLineNumberVOOMA;
			        	char[] arr0closingLineNumberVOOMA = new char[rem0closingLineNumberVOOMA];
			        	if(length_closingLineNumberVOOMA<4)
			        	{
			        		for(int x=0;x<rem0closingLineNumberVOOMA;x++)
			        		{
			        			arr0closingLineNumberVOOMA[x]='0';
			        		}	
			        		
			        	}
			        	
			        	String zeros_closingLineNumberVOOMA = new String(arr0closingLineNumberVOOMA);
				   
				        
				        writer_vooma.println("CO3TKCBLKENX"+NXKCBSpace+"KCBLKENX"+NXKCBSpace+"KES1400530450001VOOMA"+VOOMAKESSpace+"KES"
				       +year+month+day+year_y+month+day+"00100"+zeros_closingLineNumberVOOMA+closingLineNumberVOOMA+"3"
				       +year+month+day+"000000000000000000C"+AfterOpenBalSpace);
				        
				        
				        int closingLineNumberT24=last_lineNumber_T24+1;
				        int length_closingLineNumberT24 = String.valueOf(closingLineNumberT24).length();
			        
			        	int rem0closingLineNumberT24=4-length_closingLineNumberT24;
			        	char[] arr0closingLineNumberT24 = new char[rem0closingLineNumberT24];
			        	if(length_closingLineNumberT24<4)
			        	{
			        		for(int x=0;x<rem0closingLineNumberT24;x++)
			        		{
			        			arr0closingLineNumberT24[x]='0';
			        		}	
			        		
			        	}
			        	
			        	String zeros_closingLineNumberT24 = new String(arr0closingLineNumberT24);
				        
				        writer_T24.println("CO3TKCBLKENX"+NXKCBSpace+"KCBLKENX"+NXKCBSpace+"KES1400530450001TWO"+TWOKESSpace+"KES"
				        +year+month+day+year_y+month+day+"00100"+zeros_closingLineNumberT24+closingLineNumberT24+"3"
				        +year+month+day+"000000000000000000C"+AfterOpenBalSpace);
				        
				        
				        
				        //OUTPUT CLOSING BALANCE-->
				        
				        
				        
				        
				        writer_vooma.close();
				        writer_T24.close();
				        
				        System.out.println("<-------WAITING FOR NEW FILE INPUT -------->");
				     
					
					
					//Close the reader
					ExcelFile_Reader.close();
					//move the read excel file to backup
					file.renameTo(new File(backUpFolder+"\\"+currentFile));
					
				} catch (Exception e) 
				{
					// TODO: handle exception
					System.out.println(e.getMessage());
					ExcelFile_Reader.close();
					
					file.renameTo(new File(errorFolder+"\\"+currentFile));
					
					//System.out.println(file.renameTo(new File(errorFolder+"\\"+currentFile)));
					//readExcel();
					
				}
				ExcelFile_Reader.close();
				
				
				
			}
			
			
			
			
		}
		
		
		
		
		
	 
	}

}
