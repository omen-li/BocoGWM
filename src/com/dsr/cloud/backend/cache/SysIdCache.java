package com.dsr.cloud.backend.cache;

import java.util.Hashtable;

import com.dsr.cloud.backend.utils.StringUtil;

public class SysIdCache {
	
	private static Hashtable<String ,String>gwMap = new Hashtable<String, String>();
	

	public static void putSysID(String deviceId,String sysId){
		gwMap.put(deviceId, sysId);
	}
	
	public static String getSysId(String deviceId){
		return gwMap.get(deviceId);
	}
	

	
	public static boolean exists(String deviceId){
		String sysId =gwMap.get(deviceId);
		if(StringUtil.isEmpty(sysId))
			return false;
		else
			return true;
	}
	
}
