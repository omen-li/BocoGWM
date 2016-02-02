package com.dsr.cloud.backend.cache;

import java.util.Hashtable;

import com.dsr.cloud.backend.utils.MyLogger;
import com.dsr.cloud.backend.utils.StringUtil;

public class SessionCache {
	
	private static Hashtable<String ,String>gwMap = new Hashtable<String, String>();
	
	private static Hashtable<String ,String>sensorMap = new Hashtable<String, String>();

	public static void loginSession(String deviceId,String sessionId){
		gwMap.put(deviceId, sessionId);
	}

	
	public static boolean addSession2Sensor(String serialno,String deviceId){
		String sessionId = gwMap.get(deviceId);
		if(!StringUtil.isEmpty(sessionId)){
			sensorMap.put(serialno, sessionId);
			return true;
		}else
			return false;
	}
	
	public static String getSessionIdBySn(String serialno){
		String sessionId = sensorMap.get(serialno);
		if(StringUtil.isEmpty(sessionId))
			MyLogger.info("sessionId does not exsit! serialno=" + serialno);
		else
			MyLogger.info("sessionId =" + sessionId);
		return sessionId;
	}
	
	
	public static String getSessionIdByDeviceId(String deviceId){
		String sessionId = gwMap.get(deviceId);
		if(StringUtil.isEmpty(sessionId))
			MyLogger.info("sessionId does not exsit! deviceId=" + deviceId);
		else
			MyLogger.info("sessionId =" + sessionId);
		return sessionId;
	}
	
	public static boolean exists(String deviceId){
		String sessionId =gwMap.get(deviceId);
		if(StringUtil.isEmpty(sessionId))
			return false;
		else
			return true;
	}
	
}
