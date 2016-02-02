package com.greenlive.home;



public class Command {
	
	String command;
	String commandId;	
	String gatewayieee;
	
	
	String md5;
	String size;
	String value;
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

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

	public String getGatewayieee() {
		return gatewayieee;
	}

	public void setGatewayieee(String gatewayieee) {
		this.gatewayieee = gatewayieee;
	}
	
}
