package com.dsr.cloud.backend.pojo.req;

public class GatewayErrorReportReq {
	
	private String deviceId;
	private String gatewayAccount;
	private String keyword;
	private String mainServerUrl;
	private String commandServerUrl;
	private String currVersion;
	private String sessionId;
	private String systemCode;
	private Integer errorCode;
	
	
	public GatewayErrorReportReq(String deviceId,String gatewayAccount,String keyword){
		this.deviceId=deviceId;
		
		this.gatewayAccount=gatewayAccount;
		this.keyword=keyword;
	}
	
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getGatewayAccount() {
		return gatewayAccount;
	}
	public void setGatewayAccount(String gatewayAccount) {
		this.gatewayAccount = gatewayAccount;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getMainServerUrl() {
		return mainServerUrl;
	}
	public void setMainServerUrl(String mainServerUrl) {
		this.mainServerUrl = mainServerUrl;
	}
	public String getCommandServerUrl() {
		return commandServerUrl;
	}
	public void setCommandServerUrl(String commandServerUrl) {
		this.commandServerUrl = commandServerUrl;
	}
	public String getCurrVersion() {
		return currVersion;
	}
	public void setCurrVersion(String currVersion) {
		this.currVersion = currVersion;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getSystemCode() {
		return systemCode;
	}
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	

}
