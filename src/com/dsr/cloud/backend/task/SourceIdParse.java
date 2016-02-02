package com.dsr.cloud.backend.task;

import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dsr.cloud.backend.cache.SourceIdCache;
import com.dsr.cloud.backend.utils.MyLogger;


/**
 * sourceId用来GP sensor配对
 * Sensor IEEE 用来BOCO cloud标识sensor
 * 定时解析exccel并刷新sourceIdCache
 * @author admin
 *
 */
public class SourceIdParse {
	
	private static final String fileName="sourceId.xlsx";
	
	private static final String filePath =  SourceIdParse.class.getClassLoader().getResource("").getPath().replace("WEB-INF/classes/", "");
	
	
	public void parseExcel()throws Exception{
		MyLogger.info("Task Start to parse sourceId exccel...");
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(filePath +fileName));
		XSSFSheet sheet =workbook.getSheetAt(0);
		
		//获得总行数
		int rowNum = sheet.getLastRowNum();
		int j =0;
		for(int i=1;i<=rowNum;i++){
			
			try {
				XSSFRow row = sheet.getRow(i);
				String sourceId = row.getCell(0).getRichStringCellValue().getString();
				String ieee = row.getCell(1).getRichStringCellValue().getString();
				
				SourceIdCache.addPair(ieee, sourceId);
				j++;
			} catch (Exception e) {
				MyLogger.error("parse excel error at line " + (i+1),e);
				continue;
			}
			
		}
		
		MyLogger.info("Task finished...[" + j + "] sourceId records have been updated...");
		
		
		
	}
	
	public static void main(String[] args) throws Exception {
		SourceIdParse parse = new SourceIdParse();
		parse.parseExcel();
	}
	

}
