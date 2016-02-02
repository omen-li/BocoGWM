package com.dsr.cloud.backend.message;

public class BocoData extends XmlData {
	
	public static final String CLOUD_BASE_URL="boco_cloud_base_url";
	
	public static final String REGISTER="Register";

	public static final String ACTIVATE="Activate";
	
	public static final String LOGIN="Login";
	
	public static final String LOGOUT="Logout";
	
	public static final String FIND_SENSOR="FindSensor";
	
	public static final String SENSOR_REPORT="SensorReport";
	
	public static final String SENSOR_ONLINE="SensorOnLine";
	
	public static final String SENSOR_OFFLINE="SensorOffLine";
	
	public static final String ALARM_REPORT="AlarmReport";
	
	public static final String DATA_REPORT="DataReport";
	
	public static final String GATEWAY_ERROR_REPORT="GatewayErrorReport";
	
	public static final Integer DEVICE_STATUS_OFFLINE=0;
	
	public static final Integer DEVICE_STATUS_ONLINE=1;
	
	public static final Integer DEVICE_STATUS_ACTIVE=2;
	
	public static final Integer DEVICE_STATUS_ERROR=3;
	
	public static final String COMMAND_TYPE_SENSOR_REPORT="SensorReport";
	
	public static final String COMMAND_TYPE_FIND_SENSOR="FindSensor";
	
	public static final String COMMAND_TYPE_DATA_REPORT="DataReport";
	
	public static final String COMMAND_TYPE_INIT_GATEWAY="InitGateway";
	
	public static final String COMMAND_TYPE_REMOVE_SENSOR="RemoveSensor";
	
	public static final String COMMAND_TYPE_ADD_SENSOR="AddSensor";
	
	public static final String COMMAND_TYPE_FIRMWARE_DOWNLOAD="FirmwareDownload";
	
	public static final String COMMAND_TYPE_CHECK_UPGRADE="CheckUpgrade";
	
	
	
	
	
}
