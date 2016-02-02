package com.dsr.cloud.backend.pojo.command;

import java.util.List;

public class Command {
	
	private String command;
	
	private String commandId;
	
	private String commandType;

	private String sensorieee;
	
	private List<String> sensorieees;
	
	private String md5;
	
	private String size;
	
	private String value;

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
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

	public List<String> getSensorieees() {
		return sensorieees;
	}

	public void setSensorieees(List<String> sensorieees) {
		this.sensorieees = sensorieees;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
	
	
	
}
