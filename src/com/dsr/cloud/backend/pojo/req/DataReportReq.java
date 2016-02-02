package com.dsr.cloud.backend.pojo.req;

public class DataReportReq {
	
	public DataReportReq(){
		
	}
	
	public DataReportReq(String serialno,
	 String attributename,
     Integer status,
     String value){
		this.serialno=serialno;
		this.attributename=attributename;
		this.status=status;
		this.value=value;
		
	}
	
	private String serialno;
	
	private String attributename;

	private Integer status;
	
	private String value;
	

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	public String getAttributename() {
		return attributename;
	}

	public void setAttributename(String attributename) {
		this.attributename = attributename;
	}

	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	

}
