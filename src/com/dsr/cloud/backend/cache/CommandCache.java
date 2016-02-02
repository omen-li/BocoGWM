package com.dsr.cloud.backend.cache;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.dsr.gateway.manager.dto.api.gateway.CommandInfo;


public class CommandCache {
	
	private static Hashtable<String ,List<CommandInfo>>commandMap = new Hashtable<String, List<CommandInfo>>();
	
	public static boolean exists(String gatewayId) {
		
		if(commandMap.containsKey(gatewayId)){
			List<CommandInfo> commandList = commandMap.get(gatewayId);
			if(commandList !=null && commandList.size()>0)
				return true;
			else
				return false;
		}
		else
			return false;
	}
	
	public static List<CommandInfo> get(String gatewayId){
		return commandMap.get(gatewayId);
	}
	
	public static void add(String gatewayId,CommandInfo command) {
		if(exists(gatewayId)){
			List<CommandInfo> commandList = commandMap.get(gatewayId);
			commandList.add(command);
			commandMap.put(gatewayId, commandList);
		}else{
			List<CommandInfo> commandList = new ArrayList<CommandInfo>();
			commandList.add(command);
			commandMap.put(gatewayId, commandList);
		}
	}
	
	public static void remove(String gatewayId) {
		if(exists(gatewayId))
			commandMap.remove(gatewayId);
	}
	
	public static CommandInfo custCommand(String gatewayId){
		CommandInfo command = new CommandInfo();
		if(exists(gatewayId)){
			List<CommandInfo> commandList = commandMap.get(gatewayId);
			command = commandList.get(0);
			commandList.remove(0);
			return command;
		}else
			return null;
	}
	
	public static void put(String gatewayId,List<CommandInfo> commandList){
		commandMap.put(gatewayId, commandList);
	}
	
	public static void clear() {
		commandMap.clear();
	}

}
