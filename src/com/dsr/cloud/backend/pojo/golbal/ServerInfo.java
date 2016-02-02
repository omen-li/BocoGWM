package com.dsr.cloud.backend.pojo.golbal;

import com.dsr.cloud.backend.utils.PropertiesUtil;

public class ServerInfo {
	
	static{
		mainServerUrl=PropertiesUtil.getValue("boco_cloud_default_url");
		commandServerUrl=PropertiesUtil.getValue("boco_command_default_url");
		
	}
	public ServerInfo(){
	}
	
	public ServerInfo(String mainServerUrl, String commandServerUrl){
		this.mainServerUrl= mainServerUrl;
		
		this.commandServerUrl=commandServerUrl;
	}

	private static String mainServerUrl;
	
	private static String commandServerUrl;
	
	public static String getMainServerUrl() {
			return mainServerUrl;
	}
	
	public static void setMainServerUrl(String mainServerUrl) {
		ServerInfo.mainServerUrl = mainServerUrl;
	}
	
	public static String getCommandServerUrl() {
			return commandServerUrl;
		
	}
	
	public static void setCommandServerUrl(String commandServerUrl) {
		ServerInfo.commandServerUrl = commandServerUrl;
	}
	
	
}
