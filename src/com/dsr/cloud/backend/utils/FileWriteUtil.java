/**
 * Project Name:eBooking
 * File Name:FileUtil.java
 * Package Name:com.eBooking.util
 * Date:2014年3月4日上午9:28:34
 * Copyright (c) 2014, liyidong@nbse.net.cn All Rights Reserved.
 *
*/

package com.dsr.cloud.backend.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * ClassName:FileWriteUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2014年3月4日 上午9:28:34 <br/>
 * @author   Liyidong
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class FileWriteUtil {
	
	public static void fileWrite(String filePath,String fileName,String content)throws Exception{
	    if(filePath.endsWith("/")){
	    	filePath=filePath.substring(0, filePath.length()-1);
	    }
		File dest = new File(filePath);
		if(!dest.exists()){
			dest.mkdirs(); 
		}
	       try {  
	    	   File file =  new File(filePath+"/"+fileName);
	           BufferedWriter writer  = new BufferedWriter(new FileWriter(file));  
	           if(content!=null){  
	               writer.write(content);  
	           }  
	           writer.flush();  
	           writer.close();  
	       } catch (FileNotFoundException e) {  
	           throw new Exception(e); 
	       } catch (IOException e) {  
	    	   throw new Exception(e); 
	       } 
	}
	
	
	public static FileOutputStream createFile(String filePath,String fileName)throws Exception{
		  if(filePath.endsWith("/")){
		    	filePath=filePath.substring(0, filePath.length()-1);
		    }
		    
			File dest = new File(filePath);
			if(!dest.exists()){
				dest.mkdirs(); 
			}
		
			File file =  new File(filePath+"/"+fileName);
			if(!file.exists()){
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	   
			return null;
		
	}
	
	
	
	public static void main(String[] args) throws Exception {
		createFile("D:\\test\\2018","test2222.txt");
		System.out.println("succ!");
		String path = "D:\\test\\2018\\test3333333.xls";
		 FileOutputStream fos=new FileOutputStream(path); 
		 fos.close();
	}
	
	

}
