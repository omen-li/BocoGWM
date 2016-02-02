package com.dsr.cloud.backend.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	
	public static String get32Lowercase(byte[] plain){
		try { 
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(plain); 
			byte b[] = md.digest(); 
			
			int i; 
			
			StringBuffer buf = new StringBuffer(""); 
			for (int offset = 0; offset < b.length; offset++) { 
				i = b[offset]; 
				if(i<0) i+= 256; 
				if(i<16) 
					buf.append("0"); 
				buf.append(Integer.toHexString(i)); 
			} 
			
			return buf.toString();
			
			
		} catch (NoSuchAlgorithmException e) { 
			e.printStackTrace(); 
			return null;
		} 
	}
	
	public static String get16Lowercase(byte[] plain){
		try { 
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(plain); 
			byte b[] = md.digest(); 
			
			int i; 
			
			StringBuffer buf = new StringBuffer(""); 
			for (int offset = 0; offset < b.length; offset++) { 
				i = b[offset]; 
				if(i<0) i+= 256; 
				if(i<16) 
					buf.append("0"); 
				buf.append(Integer.toHexString(i)); 
			} 
			
			return buf.toString().substring(8,24);
		} catch (NoSuchAlgorithmException e) { 
			e.printStackTrace(); 
			return null;
		} 
	}
	
	
	public static void main(String[] args) {
		
		System.out.println(get32Lowercase("123".getBytes()));
		
		
	}
	
}
