package com.dsr.cloud.backend.pojo.req;

public class LoginReq {

	public LoginReq(){
		
	}
	
	
	public LoginReq(String account,String keyword){
		
		this.account=account;
		
		this.keyword=keyword;
		
	}
	
	private String account;
	
	private String keyword;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	
	
}
