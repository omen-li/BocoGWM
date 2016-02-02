package com.dsr.cloud.backend.utils;

public class SensorIEEEUtils {
	
	private static final String SENSOR_IEEE_HEAD=PropertiesUtil.getValue("sensor_ieee_head");
	
	public String getInfo2Boco(String sensorIEEE){
		return SENSOR_IEEE_HEAD+sensorIEEE;
	}
	
	public String getInfo2GW(String sensorIEEE){
		return sensorIEEE.replace(SENSOR_IEEE_HEAD, "");
	}
	

}
