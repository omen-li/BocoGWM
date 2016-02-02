package com.dsr.cloud.backend.pojo.req;

public class SensorEntity {
	
	
	public SensorEntity(){
		
	}
	
	public SensorEntity(String serialno,
			Integer battery,
			String deviceName,
			String currVersion,
			Integer sensorType,
			Integer status){
		
		this.serialno=serialno;
		this.battery=battery;
		this.deviceName=deviceName;
		this.currVersion=currVersion;
		this.sensorType=sensorType;
		this.status=status;
	}
	
	private String serialno;
	
	private Integer battery;
	
	private String deviceName;
	
	private String currVersion;
	
	private Integer sensorType;
	
	private Integer status;

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	public Integer getBattery() {
		return battery;
	}

	public void setBattery(Integer battery) {
		this.battery = battery;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getCurrVersion() {
		return currVersion;
	}

	public void setCurrVersion(String currVersion) {
		this.currVersion = currVersion;
	}

	public Integer getSensorType() {
		return sensorType;
	}

	public void setSensorType(Integer sensorType) {
		this.sensorType = sensorType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	

}
