package com.dsr.cloud.backend.pojo.rep;

import java.util.HashMap;

public class CloudResponse {

	private Integer requestStatus;
	
	private Integer errcode;
	
	private String errdes;
	
	private HashMap<String,String> data;

	public Integer getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(Integer requestStatus) {
		this.requestStatus = requestStatus;
	}

	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}

	public String getErrdes() {
		return errdes;
	}

	public void setErrdes(String errdes) {
		this.errdes = errdes;
	}

	public HashMap<String, String> getData() {
		return data;
	}
	
	public void setData(HashMap<String, String> data) {
		this.data = data;
	}
	
}
