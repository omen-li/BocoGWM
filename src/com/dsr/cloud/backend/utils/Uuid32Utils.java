package com.dsr.cloud.backend.utils;

public class Uuid32Utils {
	
	private static final String SPLIT ="-";
	
	public static String getId2Gwm(String deviceId){
		return deviceId.replace("-", "");
		
	}

	public static String getId2Boco(String deviceId){
		
		StringBuffer sb = new StringBuffer();
		sb.append(deviceId.substring(0, 8))
		.append(SPLIT)
		.append(deviceId.substring(8,12))
		.append(SPLIT)
		.append(deviceId.substring(12,16))
		.append(SPLIT)
		.append(deviceId.substring(16,20))
		.append(SPLIT)
		.append(deviceId.substring(20,32));
		return sb.toString();
		
	}
	
	
	public static void main(String[] args) {
		String a ="80a8130d-65e3-49b9-8e18-25aa486d8e76";
		String b="80a8130d65e349b98e1825aa486d8e76";
				
		System.out.println(getId2Gwm(a));
		System.out.println(getId2Boco(b));
		
		
	}
	
}
