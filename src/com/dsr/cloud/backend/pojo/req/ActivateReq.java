package com.dsr.cloud.backend.pojo.req;

public class ActivateReq {
	
	public ActivateReq(){
		
	}
	
	public ActivateReq( String deviceId,String version){
		this.deviceId=deviceId;
		this.version=version;
	}
	
	
	private String deviceId;
	
	private String version;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
	

}
