package com.dsr.cloud.backend.cache;

import java.util.Hashtable;

public class KeyMapTmp {
	
	private static Hashtable<String ,String[]>keyMap = new Hashtable<String, String[]>();
	
	public static String[] getAccount(String account){
		return keyMap.get(account);
	}
	
	public static void setAccount(String account,String pwd){

		String[] a =new String[2];
		a[0]=account;
		a[1]=pwd;
		
		keyMap.put(account.substring(0, 8),a);
		
		
		
	}
	
	
	

}
