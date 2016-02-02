package com.dsr.cloud.backend.pojo.req;

public class RegisterReq {
	
	public RegisterReq(){
		
	}
	
	public RegisterReq(String serialno,String macno){
		this.serialno = serialno;
		this.macno= macno;
	}
	
	public RegisterReq(String serialno,String macno, String factory){
		this.serialno = serialno;
		this.macno= macno;
		this.factory= factory;
	}
	
	
	private String serialno;
	
	private String macno;
	
	private String factory;

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	public String getMacno() {
		return macno;
	}

	public void setMacno(String macno) {
		this.macno = macno;
	}
	
	public String getFactory() {
		return factory;
	}
	
	public void setFactory(String factory) {
		this.factory = factory;
	}
	

}
