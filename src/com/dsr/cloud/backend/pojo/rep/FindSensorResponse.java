package com.dsr.cloud.backend.pojo.rep;

import java.util.HashMap;
import java.util.List;

import com.dsr.gateway.manager.dto.boco.FindSensorResponseDto;

public class FindSensorResponse {

	private Integer requestStatus;
	
	private Integer errcode;
	
	private String errdes;
	
	private List<FindSensorResponseDto.FindSensorInfo> data;

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

	public List<FindSensorResponseDto.FindSensorInfo> getData() {
		return data;
	}
	
	public void setData(List<FindSensorResponseDto.FindSensorInfo> data) {
		this.data = data;
	}
	
	
}
