package com.dsr.cloud.backend.utils;

import java.io.ByteArrayInputStream;   
import java.io.ByteArrayOutputStream;   
import java.io.IOException;   
import java.util.zip.GZIPInputStream;   
import java.util.zip.GZIPOutputStream;   

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class GzipUtil {
	 
	  
	 // 压缩   
	 public static byte[] compress(String str) throws IOException {   
	    if (str == null || str.length() == 0) {   
	     return null;   
	   }   
	    ByteArrayOutputStream out = new ByteArrayOutputStream();   
	   GZIPOutputStream gzip = new GZIPOutputStream(out);   
	    gzip.write(str.getBytes("UTF-8"));   
	    gzip.close();   
	   return out.toByteArray();   
	  }   
	  
	  // 解压缩   
	 public static String uncompress(byte[] input) throws IOException {   
	    if (input == null || input.length == 0) {   
	      return null;   
	  }   
	   ByteArrayOutputStream out = new ByteArrayOutputStream();   
	   ByteArrayInputStream in = new ByteArrayInputStream(input);   
	    GZIPInputStream gunzip = new GZIPInputStream(in);   
	    byte[] buffer = new byte[256];   
	    int n;   
	   while ((n = gunzip.read(buffer))>= 0) {   
	    out.write(buffer, 0, n);   
	    }   
	    // toString()使用平台默认编码，也可以显式的指定如toString(&quot;GBK&quot;)   
	    return out.toString();   
	  }   
	  
	  // 测试方法   
	  public static void main(String[] args) throws IOException {   
	         
	      //测试字符串   
	     String str="H4sIAAAAAAAAAIuuVkosSk30L0gt8kxRsjIxMtFRSgYKlKSGZOamKlkZmhhYWBobmphZGhgY6Cjl5Ccn5qSCVFrCeX6JIIVKz+aufbp93tMdTUowiZDKAqAEUF1ufkpmWiVWA/OBFieW5BdBDXna2/ds6gagCUWpufllqUB7DGpjASymQImkAAAA";   
	     BASE64Encoder base64 = new BASE64Encoder();
	     BASE64Decoder base = new BASE64Decoder();
	     
	     System.out.println(uncompress(base.decodeBuffer(str)));
//	     
	     String str2="11234567890123456789:[{\"中文_EN";
//	     String base2=  base64.encode(str.getBytes());
//	     System.out.println("basetest=" + base2);
//	     System.out.println("basetest lentg=" + base2.length());
//	
	   String value =   base64.encode(GzipUtil.compress(str2));
	   System.out.println(value);
//	     System.out.println("原长度："+str.length());     
//	        System.out.println("压缩后："+GzipUtil.compress(str).length); 
//	      System.out.println("base64后："+value.length());     
////	         
//	    System.out.println("解压缩："+GzipUtil.uncompress());   
	  }   
	 

}
