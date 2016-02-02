package com.dsr.cloud.backend.cache;

import java.util.Hashtable;

public class SourceIdCache {
	
	/**
	 * key=sensor Ieee
	 * value = sensor sourceId
	 */
	private static Hashtable<String ,String>IeeeMap = new Hashtable<String, String>();
	
	/**
	 * key = sensor sourceId
	 * value = sensor Ieee
	 */
	private static Hashtable<String ,String>sourceIdMap = new Hashtable<String, String>();

	public static boolean existsIeee(String ieee){
		if(IeeeMap.contains(ieee))
			return true;
		else
			return false;
	}
	
	public static boolean existsSourceId(String sourceId){
		if(sourceIdMap.contains(sourceId))
			return true;
		else
			return false;
	}
	
	
	public static void addPair(String ieee,String sourceId){
		IeeeMap.put(ieee, sourceId);
		sourceIdMap.put(sourceId,ieee);
	}
	
	
	public static String getIeee(String sourceId){
		return sourceIdMap.get(sourceId);
	}
	
	
	public static String getSourceId(String ieee){
		return IeeeMap.get(ieee);
	}
	
	
	
	
	
}
