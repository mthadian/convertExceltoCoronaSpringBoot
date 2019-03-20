package com.KCB.Pesalink.Controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.validator.internal.util.privilegedactions.NewInstance;

public class PesalinkCSNFController
{
	public static void executeCSNF() throws IOException
	{
		String currentWorkingDir = System.getProperty("user.dir");
		String inputFolder=currentWorkingDir.concat("\\input");		
		String outputFolder=currentWorkingDir.concat("\\output");
		String errorFolder=currentWorkingDir.concat("\\error");
		String backUpFolder=currentWorkingDir.concat("\\backup");
		
		//InputStream ExcelFile_Reader = new FileInputStream(inputFolder.concat("\\CSNF20190225_BANK01_1.xlsx"));
		
		File folderInput= new File(inputFolder);
		File[] files=folderInput.listFiles();
		String currentFile="";
		for(File file:files)
		{
			currentFile=file.getName();
			InputStream ExcelFile_Reader = new FileInputStream(inputFolder.concat("\\"+currentFile));
			if(currentFile.contains("CSNF") && currentFile.contains(".xlsx"))
			{
				try 
				{
					XSSFWorkbook wb = new XSSFWorkbook(ExcelFile_Reader);
					String file_name=wb.getSheetName(0);
					PrintWriter writer_vooma = new PrintWriter(outputFolder.concat("\\"+file_name+" VOOMA"+".CUT"), "UTF-8");
					PrintWriter writer_T24 = new PrintWriter(outputFolder.concat("\\"+file_name+" T24"+".CUT"), "UTF-8");
				
					String fullDate=currentFile.substring(4, 12);
					String date_yy=currentFile.substring(6, 8);
					String date_y=currentFile.substring(7, 12);
			
					
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
			        int lineNumber_VOOMA=2;
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
			        	//int tranAmount= (int) cell_TranAmount;
			        	
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
			                	
			                	
			                	int length_lineNumber = String.valueOf(lineNumber_VOOMA).length();
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
				        		
				        		String Our_ref1="";
				        		String SenderAn =formatter.formatCellValue(row.getCell(43,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
				        		
				        		BigDecimal bd_SenderAn;
				        		if(SenderAn.contains("+"))
				        		{
				        			double d_senderAn=row.getCell(43).getNumericCellValue();
				        			bd_SenderAn=new BigDecimal(d_senderAn);
				        			SenderAn=String.valueOf(bd_SenderAn);
				        		}
				        		
				        		
				        		String ReceiverAn =formatter.formatCellValue(row.getCell(102,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));		        		
				        		BigDecimal bd_ReceiverAn;
				        		if(ReceiverAn.contains("+"))
				        		{
				        			double d_ReceiverAn=row.getCell(102).getNumericCellValue();
				        			bd_ReceiverAn=new BigDecimal(d_ReceiverAn);
				        			ReceiverAn=String.valueOf(bd_ReceiverAn);
				        		}
				        		
				        		if(D_or_C.contains("D"))
				        		{
				        			Our_ref1=SenderAn;
				        		}
				        		
				        		if(D_or_C.contains("C"))
				        		{
				        			Our_ref1=ReceiverAn;
				        		}
				        		
				        		int length_Our_ref1=Our_ref1.length();
					        	int rem0Our_ref1=16-length_Our_ref1;
					        	 char[] arr0Our_ref1 = new char[rem0Our_ref1];
						        	if(length_Our_ref1<16)
						        	{
						        		for(int x=0;x<rem0Our_ref1;x++)
						        		{
						        			arr0Our_ref1[x]=' ';
						        			//arr0senderAcc[x]='0';
						        		}	
						        		
						        	}
						        	
					        	String space_Our_ref1 = new String(arr0Our_ref1);
					        	
					        	String Our_ref2=Our_ref1;
					        	String space_Our_ref2=space_Our_ref1;
					        	
					        	
					        	double D_RRN=row.getCell(41).getNumericCellValue();			        	
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
							        
							        double D_TransactionId=row.getCell(62).getNumericCellValue();			        	
					        		BigDecimal transactionId= new BigDecimal(D_TransactionId);	
					        		
					        		int length_transactionId=String.valueOf(transactionId).length();
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
							        	
						        	String receiverInstCode=formatter.formatCellValue(row.getCell(104,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
						        	String senderInstCode=formatter.formatCellValue(row.getCell(107,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
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
							        	
							        	String ApprCode=formatter.formatCellValue(row.getCell(3,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
							        	String stan=formatter.formatCellValue(row.getCell(52,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
							        	
							        	
							        	String comment=String.valueOf(acqId)+String.valueOf(transType)+ApprCode+stan;
							        	
							        	int length_comment=comment.length();
							        	int remSpacecomment=250-length_comment;
							        	 char[] arrSpacecomment = new char[remSpacecomment];
								        	if(length_comment<250)
								        	{
								        		for(int x=0;x<remSpacecomment;x++)
								        		{
								        			arrSpacecomment[x]=' ';
								        		}	
								        		
								        	}
								        	
								        	String space_comment = new String(arrSpacecomment);
								        	
							        	int space_AfterComment=132;
							 	        char[] spaceAfterComment = new char[space_AfterComment];
							 	        for(int a=0;a<space_AfterComment;a++)
							     		{
							 	        	spaceAfterComment[a]=' ';
							     		}
							 	        String CommentSpace = new String(spaceAfterComment);
							        	
					        		
					        		
					        		
					        		
					        		
					        	
					        	writer_vooma.println("CO2TKCBLKENX"+NXKCBSpace+"KCBLKENX"+NXKCBSpace+"KES1400530450001VOOMA"+VOOMAKESSpace+"KES"+fullDate+date_y
					        			+"00100"+zeros_LineNumber+lineNumber_VOOMA+"2"+zeros_Amount+amount+D_or_C+" "+tranDate+SettlDate+Our_ref1+space_Our_ref1
					        			+TranDateTime+"  "+Our_ref2+space_Our_ref2+RRN+spaces_RRN+TType_UTType_FCode_DepIdSpace+transactionId+space_transactionId
					        			+SenderReceiverInstCode+space_senderReceiver+comment+space_comment+CommentSpace);              	
			                	              	
			                
			                	
			                	vooma_totalTransAmount=vooma_totalTransAmount+cell_TranAmount;
			                	lineNumber_VOOMA=lineNumber_VOOMA+1;
			        			
			        		}
			        		//END VOOMA STATEMENT LINE
			        		
			        		//START T24 STATEMENT LINE
			        		else ///T24
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
				        		
				        		String Our_ref1="";
				        		String SenderAn =formatter.formatCellValue(row.getCell(43,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
				        		
				        		BigDecimal bd_SenderAn;
				        		if(SenderAn.contains("+"))
				        		{
				        			double d_senderAn=row.getCell(43).getNumericCellValue();
				        			bd_SenderAn=new BigDecimal(d_senderAn);
				        			SenderAn=String.valueOf(bd_SenderAn);
				        		}
				        		
				        		
				        		String ReceiverAn =formatter.formatCellValue(row.getCell(102,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));		        		
				        		BigDecimal bd_ReceiverAn;
				        		if(ReceiverAn.contains("+"))
				        		{
				        			double d_ReceiverAn=row.getCell(102).getNumericCellValue();
				        			bd_ReceiverAn=new BigDecimal(d_ReceiverAn);
				        			ReceiverAn=String.valueOf(bd_ReceiverAn);
				        		}
				        		
				        		if(D_or_C.contains("D"))
				        		{
				        			Our_ref1=SenderAn;
				        		}
				        		
				        		if(D_or_C.contains("C"))
				        		{
				        			Our_ref1=ReceiverAn;
				        		}
				        		
				        		int length_Our_ref1=Our_ref1.length();
					        	int rem0Our_ref1=16-length_Our_ref1;
					        	 char[] arr0Our_ref1 = new char[rem0Our_ref1];
						        	if(length_Our_ref1<16)
						        	{
						        		for(int x=0;x<rem0Our_ref1;x++)
						        		{
						        			arr0Our_ref1[x]=' ';
						        			//arr0senderAcc[x]='0';
						        		}	
						        		
						        	}
						        	
					        	String space_Our_ref1 = new String(arr0Our_ref1);
					        	
					        	String Our_ref2=Our_ref1;
					        	String space_Our_ref2=space_Our_ref1;
					        	
					        	
					        	double D_RRN=row.getCell(41).getNumericCellValue();			        	
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
							        
							        double D_TransactionId=row.getCell(62).getNumericCellValue();			        	
					        		BigDecimal transactionId= new BigDecimal(D_TransactionId);	
					        		
					        		int length_transactionId=String.valueOf(transactionId).length();
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
							        	
						        	String receiverInstCode=formatter.formatCellValue(row.getCell(104,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
						        	String senderInstCode=formatter.formatCellValue(row.getCell(107,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
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
							        	
							        	String ApprCode=formatter.formatCellValue(row.getCell(3,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
							        	String stan=formatter.formatCellValue(row.getCell(52,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
							        	
							        	
							        	String comment=String.valueOf(acqId)+String.valueOf(transType)+ApprCode+stan;
							        	
							        	int length_comment=comment.length();
							        	int remSpacecomment=250-length_comment;
							        	 char[] arrSpacecomment = new char[remSpacecomment];
								        	if(length_comment<250)
								        	{
								        		for(int x=0;x<remSpacecomment;x++)
								        		{
								        			arrSpacecomment[x]=' ';
								        		}	
								        		
								        	}
								        	
								        	String space_comment = new String(arrSpacecomment);
								        	
							        	int space_AfterComment=132;
							 	        char[] spaceAfterComment = new char[space_AfterComment];
							 	        for(int a=0;a<space_AfterComment;a++)
							     		{
							 	        	spaceAfterComment[a]=' ';
							     		}
							 	        String CommentSpace = new String(spaceAfterComment);
							        	
					        		
					        		
					        		
					        		
					        		
					        	
					        	writer_T24.println("CO2TKCBLKENX"+NXKCBSpace+"KCBLKENX"+NXKCBSpace+"KES1400530450001TWO"+TWOKESSpace+"KES"+fullDate+date_y
					        			+"00100"+zeros_LineNumber+lineNumber_T24+"2"+zeros_Amount+amount+D_or_C+" "+tranDate+SettlDate+Our_ref1+space_Our_ref1
					        			+TranDateTime+"  "+Our_ref2+space_Our_ref2+RRN+spaces_RRN+TType_UTType_FCode_DepIdSpace+transactionId+space_transactionId
					        			+SenderReceiverInstCode+space_senderReceiver+comment+space_comment+CommentSpace);              	
			                	              	
			                
			                	T24_totalTransAmount=T24_totalTransAmount+cell_TranAmount;
			                	lineNumber_T24=lineNumber_T24+1;
			        		
							}
			        		
			        		//END T24 STATEMENT LINE
			        		
			        		
			        	}
			        	//transtype!=50 end
			        	
			        	
			        	
			        }
			        
			      //<--OUTPUT SUM OF ENTRIES
			       vooma_totalTransAmount=Math.round(vooma_totalTransAmount*100)/100.00;
			       BigDecimal b_vooma_totalTransAmount=new BigDecimal(vooma_totalTransAmount);
			        
			        
			        BigDecimal absolute_vooma_totalTransAmount=b_vooma_totalTransAmount.abs();        
			        BigDecimal post_vooma_totalTransAmount= absolute_vooma_totalTransAmount.multiply(new BigDecimal(10000));
			        
			        
			        
			        
			        String vooma_Total_DorC="";
			        if(b_vooma_totalTransAmount.signum() < 0)
			        {
			        	vooma_Total_DorC="D";
			        }
			        else 
			        {
			        	vooma_Total_DorC="C";
			        	
					}
			        
			        int last_lineNumber_VOOMA=lineNumber_VOOMA;
			        
			        int length_lineNumberVOOMA = String.valueOf(last_lineNumber_VOOMA).length();
			        
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
			    	
			        int length_voomaAmount = String.valueOf(post_vooma_totalTransAmount).length();
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
			       	
			       	String vooma_dummyString="SETTLEMENT      ";
			       	int space_AfterDummyVooma=885;
				        char[] spaceAfterDummyVooma = new char[space_AfterDummyVooma];
				        for(int a=0;a<space_AfterDummyVooma;a++)
			 		{
				        	spaceAfterDummyVooma[a]=' ';
			 		}
				        String vooma_spaceDummy = new String(spaceAfterDummyVooma);

			    	
			    	
			        
			        writer_vooma.println("CO2TKCBLKENX"+NXKCBSpace+"KCBLKENX"+NXKCBSpace+"KES1400530450001VOOMA"+VOOMAKESSpace+"KES"+fullDate+date_y
			    			+"00100"+zeros_LineNumberVOOMA+lineNumber_VOOMA+"2"+zeros_voomaAmount+post_vooma_totalTransAmount+vooma_Total_DorC+" "
			        		+fullDate+fullDate+vooma_dummyString+vooma_spaceDummy);
			        
			        /*----------------------------- */
			        T24_totalTransAmount=Math.round(T24_totalTransAmount*100)/100.00;
			        BigDecimal b_T24_totalTransAmount=new BigDecimal(T24_totalTransAmount);
			         
			         
			         BigDecimal absolute_T24_totalTransAmount=b_T24_totalTransAmount.abs();        
			         BigDecimal post_T24_totalTransAmount= absolute_T24_totalTransAmount.multiply(new BigDecimal(10000));
			         
			         
			         
			         
			         String T24_Total_DorC="";
			         if(b_T24_totalTransAmount.signum() < 0)
			         {
			        	 T24_Total_DorC="D";
			         }
			         else 
			         {
			        	 T24_Total_DorC="C";
			         	
			 		}
			         
			         int last_lineNumber_T24=lineNumber_T24;
			         
			         
			         int length_lineNumberT24 = String.valueOf(last_lineNumber_T24).length();
			         
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
			     	
			         int length_T24Amount = String.valueOf(post_T24_totalTransAmount).length();
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
			        	
			        	String T24_dummyString="SETTLEMENT      ";
			        	int space_AfterDummyT24=885;
			 	        char[] spaceAfterDummyT24 = new char[space_AfterDummyT24];
			 	        for(int a=0;a<space_AfterDummyT24;a++)
			  		{
			 	        	spaceAfterDummyT24[a]=' ';
			  		}
			 	        String T24_spaceDummy = new String(spaceAfterDummyT24);

			     	
			     	
			         
			         writer_T24.println("CO2TKCBLKENX"+NXKCBSpace+"KCBLKENX"+NXKCBSpace+"KES1400530450001TWO"+TWOKESSpace+"KES"+fullDate+date_y
			     			+"00100"+zeros_LineNumberT24+lineNumber_T24+"2"+zeros_T24Amount+post_T24_totalTransAmount+T24_Total_DorC+" "
			         		+fullDate+fullDate+T24_dummyString+T24_spaceDummy);
			        
			      //OUTPUT SUM OF ENTRIES-->
			        
			      //<--OUTPUT CLOSING BALANCE
			        int closingLineNumberVOOMA=last_lineNumber_VOOMA+1;
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
			       +fullDate+date_y+"00100"+zeros_closingLineNumberVOOMA+closingLineNumberVOOMA+"3"
			       +fullDate+"000000000000000000C"+AfterOpenBalSpace);
			        /*---------------------------*/
			        
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
			       +fullDate+date_y+"00100"+zeros_closingLineNumberT24+closingLineNumberT24+"3"
			       +fullDate+"000000000000000000C"+AfterOpenBalSpace);
			        
			        
			        
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
				}
				
				
			}
		}
		//for end
		
		

     
		
		
		
	}

}
