package com.dsr.cloud.backend.task;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dsr.cloud.backend.cache.CommandCache;
import com.dsr.cloud.backend.cache.SourceIdCache;
import com.dsr.cloud.backend.message.BocoData;
import com.dsr.cloud.backend.utils.MyLogger;
import com.dsr.cloud.backend.utils.StringUtil;
import com.dsr.cloud.backend.utils.Uuid32Utils;
import com.dsr.gateway.manager.device.CommandFactory;
import com.dsr.gateway.manager.dto.api.gateway.CommandInfo;
import com.greenlive.home.GatewayCommand;

public class TestGenerateCommand {
	private static final Integer DEFAULT_COMMAND_ID=2;
	private Integer i ;
	public TestGenerateCommand(Integer i){
		this.i=i;
	}
	
	public void generateCommand(){
		MyLogger.info("start to generate command by server self...");
		String retJson = getRetJson();
		GatewayCommand data=JSON.parseObject(retJson,GatewayCommand.class);
		setCache(data);
		MyLogger.info(" generate command [" + data.getCommand() + "] over...");
	}
	
	private String getRetJson(){
		
		String sensorReport= "{\"command\":\"SensorReport\",\"commandId\":\"10\",\"gatewayieee\":\"00155f002803ccc9\",\"deviceId\":\"80a8130d-65e3-49b9-8e18-25aa486d8e76\"}";
		String findSensor="{\"command\":\"FindSensor\",\"commandId\":\"1\",\"gatewayieee\":\"00155f002803ccc9\",\"deviceId\":\"80a8130d-65e3-49b9-8e18-25aa486d8e76\"}";
		String dataReport="{\"sensorieee\":\"00155F00F84541CC\",\"command\":\"DataReport\",\"commandId\":\"1\",\"gatewayieee\":\"00155f002803ccc9\",\"deviceId\":\"80a8130d-65e3-49b9-8e18-25aa486d8e76\"}";
		String initGateway="{\"command\":\"InitGateway\",\"commandId\":\"1\",\"gatewayieee\":\"00155f002803ccc9\",\"deviceId\":\"80a8130d-65e3-49b9-8e18-25aa486d8e76\"}";
		String removeSensor="{\"command\":\"RemoveSensor\",\"commandId\":\"1\",\"gatewayieee\":\"00155f002803ccc9\",\"deviceId\":\"80a8130d-65e3-49b9-8e18-25aa486d8e76\",\"sensorieees\":[\"00155f00f8454265\"]}";
		String addSensor="{\"command\":\"AddSensor\",\"commandId\":\"1\",\"gatewayieee\":\"00155f002803ccc9\",\"deviceId\":\"80a8130d-65e3-49b9-8e18-25aa486d8e76\",\"sensorieees\":[\"00155f00f8454265\"]}";

		
//		int i;
//		i=new java.util.Random().nextInt(5);
		
		//sensor report
		if(i==0){
			return sensorReport;
		}
		//FindSensor
		else if(i==1){
			return findSensor;
		}
		else if(i==2){
			return dataReport;
		}
		else if(i==3){
			return initGateway;
		}
		else if(i==4){
			return removeSensor;
		}
		else if(i==5){
			return addSensor;
		}
		else 
			return null;
	}
	
	
	
	public static void setCache(GatewayCommand data){
		CommandInfo command = new CommandInfo();
		String deviceId = data.getDeviceId();
		if(data.getCommand().equals(BocoData.COMMAND_TYPE_SENSOR_REPORT)){
			command = CommandFactory.createSensorReportCommand(DEFAULT_COMMAND_ID);
		}
		else if(data.getCommand().equals(BocoData.COMMAND_TYPE_FIND_SENSOR)){
			command = CommandFactory.createFindSensorCommand(DEFAULT_COMMAND_ID);
		}
		else if(data.getCommand().equals(BocoData.COMMAND_TYPE_DATA_REPORT)){
			String sensorieee = data.getSensorieee();
			String sourceId = SourceIdCache.getSourceId(sensorieee);
			if(!StringUtil.isEmpty(sourceId))
				command = CommandFactory.createDataReportCommand(DEFAULT_COMMAND_ID,sensorieee);
			else
				MyLogger.error("can not find sourceId in the sourceId cache, pls to check the excel!");
		}
		else if(data.getCommand().equals(BocoData.COMMAND_TYPE_INIT_GATEWAY)){
			command = CommandFactory.createInitGatewayCommand(DEFAULT_COMMAND_ID);
		}
		else if(data.getCommand().equals(BocoData.COMMAND_TYPE_REMOVE_SENSOR)){
			List<String> sensorList = data.getSensorieees();
			sensorList = getSourceId(sensorList);
			command = CommandFactory.createRemoveSensorCommand(DEFAULT_COMMAND_ID, sensorList);
		}
		else if(data.getCommand().equals(BocoData.COMMAND_TYPE_ADD_SENSOR)){
			List<String> sensorList = data.getSensorieees();
			sensorList = getSourceId(sensorList);
			command = CommandFactory.createAddSensorCommand(DEFAULT_COMMAND_ID, sensorList);
		}
		command.setDeviceId(Uuid32Utils.getId2Gwm(deviceId));
		CommandCache.add(deviceId, command);
		
		
	}
	
	private static List<String> getSourceId(List<String> sensorList){
		List<String> result = new ArrayList<String>();
		
		for(String sensorIeee:sensorList){
			String sourceId = SourceIdCache.getSourceId(sensorIeee);
			if(!StringUtil.isEmpty(sourceId))
				result.add(sourceId);
			
		}
		
		return result;
		
		
	}
	
	public Integer getI() {
		return i;
	}
	
	public void setI(Integer i) {
		this.i = i;
	}
	
	
	public static void main(String[] args) {
		String addSensor="{\"command\":\"AddSensor\",\"commandId\":\"1\",\"gatewayieee\":\"00155f002803ccc9\",\"deviceId\":\"80a8130d-65e3-49b9-8e18-25aa486d8e76\",\"sensorieees\":[\"00155f00f8454265\"]}";
		GatewayCommand data=JSON.parseObject(addSensor,GatewayCommand.class);
		System.out.println(data.getSensorieees().size());
		setCache(data);
	}

}
