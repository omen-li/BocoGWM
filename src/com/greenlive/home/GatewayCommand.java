package com.greenlive.home;

import java.util.ArrayList;
import java.util.List;


public class GatewayCommand extends Command{

	String operation;
	String commandType;
	
	String deviceId;
	String sensorieee;
	
	List<String> sensorieees;
	String value;
	
	
	public GatewayCommand(){
		
	}
	
	


	public String getCommandType() {
		return commandType;
	}



	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}



	


	public String getSensorieee() {
		return sensorieee;
	}



	public void setSensorieee(String sensorieee) {
		this.sensorieee = sensorieee;
	}



	public String getValue() {
		return value;
	}



	public void setValue(String value) {
		this.value = value;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}


	public List<String> getSensorieees() {
		return sensorieees;
	}


	public void setSensorieees(List<String> sensorieees) {
		this.sensorieees = sensorieees;
	}


	public String getDeviceId() {
		return deviceId;
	}
	
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	
	public static void main(String[] args) {
		List<String> sensorieees = new ArrayList<String>();
		sensorieees.add("1");
		sensorieees.add("2");
		System.out.println(sensorieees);
	}
}
