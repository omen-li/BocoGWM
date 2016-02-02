package com.dsr.cloud.backend.pojo.req;

import java.util.List;

public class Sensors {

	public Sensors(){
	}
	
	public Sensors(List<SensorEntity> sensors){
		
		this.sensors=sensors;
		
	}

	private List<SensorEntity> sensors;
	
	public List<SensorEntity> getSensors() {
		return sensors;
	}
	
	
	public void setSensors(List<SensorEntity> sensors) {
		this.sensors = sensors;
	}
	
	
}
