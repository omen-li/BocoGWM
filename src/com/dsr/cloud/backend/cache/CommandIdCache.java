package com.dsr.cloud.backend.cache;

import java.util.Hashtable;
import java.util.List;

import com.dsr.gateway.manager.dto.api.gateway.CommandInfo;

public class CommandIdCache {
	/**
	 * key=gatewayId  value = commandId
	 */
	private static Hashtable<String ,String>bocoMap = new Hashtable<String, String>();
	
	/**
	 * key=gwmCommandId value = gatewayId
	 */
	private static Hashtable<String ,String>gwMap = new Hashtable<String, String>();
	
	public String  getCommandIdByGatewayId(String gatewayId){
		return bocoMap.get(gatewayId);
	}
	
	public String getGatewayIdByGwmCommandId(String gwmCommandId){
		return gwMap.get(gwmCommandId);
	}
	
	public String addCommandId(String gatewayId,String commandId){
		
		
		return null;
		
	}
	
	
	
	private String generateGwmCommandId(String commandId){
		return null;
		
		
		
		
	}
	
	
	
	
	
	

}
