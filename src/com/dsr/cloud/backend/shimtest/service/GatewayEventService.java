/* ********************************************************************
 *                      DSR Corporation. Cloud.
 * This is unpublished proprietary source code of DSR Corporation.
 * The copyright notice does not evidence any actual or intended
 * publication of such source code.
 *
 *       Copyright (c) 2013 DSR Corporation. Denver, USA.
 *
 *                       All rights reserved.
 *
 * $Author: maxim.volkov $
 * $Date: 2014-03-26 10:15:33 +0400 (Ср, 26 мар 2014) $
 * $Revision: 2075 $
 * ********************************************************************/
package com.dsr.cloud.backend.shimtest.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dsr.cloud.backend.cache.CommandCache;
import com.dsr.cloud.backend.cache.SessionCache;
import com.dsr.cloud.backend.cache.SysIdCache;
//import com.dsr.cloud.backend.cache.KeyMapTmp;
import com.dsr.cloud.backend.message.BocoData;
import com.dsr.cloud.backend.pojo.command.Command;
import com.dsr.cloud.backend.pojo.golbal.ServerInfo;
import com.dsr.cloud.backend.pojo.rep.CloudResponse;
import com.dsr.cloud.backend.pojo.rep.FindSensorResponse;
import com.dsr.cloud.backend.pojo.req.ActivateReq;
import com.dsr.cloud.backend.pojo.req.AlarmReq;
import com.dsr.cloud.backend.pojo.req.DataReportReq;
import com.dsr.cloud.backend.pojo.req.GatewayErrorReportReq;
import com.dsr.cloud.backend.pojo.req.LoginReq;
import com.dsr.cloud.backend.pojo.req.RegisterReq;
import com.dsr.cloud.backend.pojo.req.SensorEntity;
import com.dsr.cloud.backend.pojo.req.Sensors;
import com.dsr.cloud.backend.utils.HttpClientUtils;
import com.dsr.cloud.backend.utils.HttpUtils;
import com.dsr.cloud.backend.utils.MyLogger;
import com.dsr.cloud.backend.utils.PropertiesUtil;
import com.dsr.cloud.backend.utils.StringUtil;
import com.dsr.cloud.backend.utils.Uuid32Utils;
import com.dsr.gateway.manager.IGatewayEventService;
import com.dsr.gateway.manager.device.CommandFactory;
import com.dsr.gateway.manager.dto.api.gateway.AddDeviceEvent;
import com.dsr.gateway.manager.dto.api.gateway.CommandInfo;
import com.dsr.gateway.manager.dto.api.gateway.DeviceStatusEvent;
import com.dsr.gateway.manager.dto.api.gateway.GeneralResponse;
import com.dsr.gateway.manager.dto.api.gateway.GeneralResponseResultCode;
import com.dsr.gateway.manager.dto.api.gateway.PostDiagnosticInfoEvent;
import com.dsr.gateway.manager.dto.api.gateway.PostMeasurementsEvent;
import com.dsr.gateway.manager.dto.boco.ActivateRequestDto;
import com.dsr.gateway.manager.dto.boco.ActivateResponseDto;
import com.dsr.gateway.manager.dto.boco.BillingLoginRequestDto;
import com.dsr.gateway.manager.dto.boco.BillingLoginResponseDto;
import com.dsr.gateway.manager.dto.boco.BillingLogoutResponseDto;
import com.dsr.gateway.manager.dto.boco.DataReportRequestDto;
import com.dsr.gateway.manager.dto.boco.FindSensorResponseDto;
import com.dsr.gateway.manager.dto.boco.RegisterRequestDto;
import com.dsr.gateway.manager.dto.boco.RegisterResponseDto;
import com.dsr.gateway.manager.dto.boco.SensorInfoRequestDto;
import com.dsr.gateway.manager.dto.boco.SensorReportRequestDto;
import com.dsr.gateway.manager.exception.CloudException;
import com.dsr.gateway.manager.exception.PostMeasurementsUnknownDeviceException;
import com.dsr.gateway.manager.exception.ServerValidationException;
import com.dsr.gateway.manager.exception.UnknownDeviceException;
import com.dsr.gateway.manager.exception.UnknownGatewayException;
import com.google.gson.Gson;
import com.greenlive.home.GatewayCommand;

/**
 *
 * @author Sergey.Shaposhnikov
 * @version $Revision: 2075 $
 */
@Service(value = "gatewayEventService")
public class GatewayEventService implements IGatewayEventService {

	private static final String CONTENT_TYPE_JSON = "application/json";

	private static final Integer DEFAULT_COMMAND_ID = 1;
	
	private static final String DEFAULT_GWM_URL="https://gw2.gosmarthome.cn:8071/BocoGWM";

	private String CLOUD_BASE_URL = ServerInfo.getMainServerUrl() + PropertiesUtil.getValue(BocoData.CLOUD_BASE_URL);

	private static String CLOUD_COMMAND_URL = ServerInfo.getCommandServerUrl();
	
	private static final String BOCO_LOGIN_ERR_CODE="1001";
	
	private static final String BOCO_SESSION_ERR_CODE="5001";
	
	private static final String BOCO_NOT_LOGIN_CODE="1005";
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(GatewayEventService.class);

	/**
	 * Adds new slave device to the cloud if it has not been added yet. <br/>
	 * Result codes of the operation: <br/>
	 * <i>ERR</i> - if the serial number of the slave device equals the serial
	 * number of the gateway, or DeviceType in request is unknown, or the slave
	 * device has already been added to the cloud while the request contains a
	 * different device type <br/>
	 * <i>UNKNOWN</i> - if Gateway is unknown. <br/>
	 * <i>ACK</i> - if SlaveDevice has been successfully added or it was added
	 * earlier <br/>
	 * <b>Note:</b> <i>gateway_id</i> and <i>device_id</i> in the request are
	 * the serial numbers of devices.
	 *
	 * @param request
	 *            AddDeviceCommandDevice DTO. It contains gateway_id (serial
	 *            number of the gateway), device_id (serial number of the slave
	 *            device) and slave device type.
	 * @return response GatewayApiResponse DTO. It contains resultCode.
	 * @throws ServerValidationException
	 * @throws UnknownDeviceException
	 */
	/**
	 * Deprecated.
	 */
	@Override
	public GeneralResponse addDevice(AddDeviceEvent request)
			throws ServerValidationException, UnknownGatewayException {

		LOGGER.info("addDevice: GW '{}', Device '{}'", request.getGatewayId(),
				request.getDeviceId());
		MyLogger.info("*******************get add device result from gw " + request.getGatewayId() + "]");
		//no use for this action 添加serialno 与deviceId对应关系
		/*String serialno = request.getDeviceId();
		String gatewayId  = Uuid32Utils.getId2Boco(request.getGatewayId());
		Boolean result = SessionCache.addSession2Sensor(serialno, gatewayId);
		if(result)
			MyLogger.info("*******************successfully to add sessionId to sensor[" + serialno + "]");*/
		
		return new GeneralResponse(GeneralResponseResultCode.ACK);
	}

	/**
	 * Posts measurements to the cloud.
	 *
	 * @param request
	 *            The request. It contains:
	 *            <ul>
	 *            <li>The gateway serial number</li>
	 *            <li>The list of measurement batches from devices connected to
	 *            the gateway. Each measurement batch contains:
	 *            <ul>
	 *            <li>The device serial number (may be the serial number of the
	 *            gateway itself)</li>
	 *            <li>The delay between the moment when the measurement was made
	 *            and the moment when this request is created</li>
	 *            <li>The list of parameters with their measured values</li>
	 *            </ul>
	 *            </li>
	 *            </ul>
	 * @return The response with the result code and optionally a command for
	 *         the gateway. The result code is
	 *         <ul>
	 *         <li><b>"ack"</b> if the measurements have been successfully
	 *         posted to the cloud,</li>
	 *         <li><b>"unknown"</b> if the gateway serial number or the serial
	 *         number of one of the devices is unknown,</li>
	 *         <li><b>"err"</b> if the request contains any other errors.</li>
	 *         </ul>
	 * @throws ServerValidationException
	 * @throws UnknownDeviceException
	 */
	/**
	 * Deprecated.
	 */
	@Override
	public GeneralResponse postMeasurements(PostMeasurementsEvent request,
			boolean isInternalCall) throws ServerValidationException,
			UnknownDeviceException, UnknownGatewayException,
			PostMeasurementsUnknownDeviceException {
		LOGGER.info("postMeasurements: GW '{}'", request.getGatewayId());
		String json = new Gson().toJson(request.getMeasurements());
		LOGGER.info("measurement: {}", json);

		return new GeneralResponse(GeneralResponseResultCode.ACK);
	}

	/**
	 * Deprecated.
	 */
	@Override
	public GeneralResponse changeDeviceStatus(DeviceStatusEvent request)
			throws ServerValidationException, UnknownDeviceException,
			UnknownGatewayException {
		LOGGER.info("changeDeviceStatus: GW '{}'", request.getGatewayId());
		LOGGER.info("device: {}, new status: {}", request.getDeviceId(),
				request.getDeviceStatus());
		return new GeneralResponse(GeneralResponseResultCode.ACK);
	}

	/**
	 * Deprecated.
	 */
	@Override
	public GeneralResponse postDiagnosticInfo(PostDiagnosticInfoEvent request)
			throws UnknownDeviceException, IOException {
		LOGGER.info("postDiagnosticInfoResult: GW '{}'", request.getGatewayId());
		return new GeneralResponse(GeneralResponseResultCode.ACK);
	}

	/****************************/
	/* NEW calls 以下是BOCO定制的方法 */
	/****************************/

	/**
	 * GWM uses this call for registering new issued GW (or GW after factory
	 * reset) in server
	 */
	@Override
	public RegisterResponseDto register(RegisterRequestDto request)
			throws CloudException {
		try {
			MyLogger.info("get regist req from gw:" + request.getMacno());

			String macno = request.getMacno();
			String serialno = request.getSerialno();

			RegisterReq reqPojo = new RegisterReq(serialno, macno,"greenpeak");
			
			String reqJson = JSON.toJSONString(reqPojo);

			MyLogger.info("register reqJson=" + reqJson);
			String retJson = HttpUtils.sendPostUrl(CLOUD_BASE_URL
					+ BocoData.REGISTER, reqJson, "UTF-8", CONTENT_TYPE_JSON);
			
			
//			String retJson = HttpClientUtils.simplePostInvoke(url, params)
			MyLogger.info("===========================================regist ret INFO = "
					+ retJson);
			if(StringUtil.isEmpty(retJson)){
				MyLogger.error("retTurn json is null!");
				return new RegisterResponseDto();
			}
			CloudResponse reponse = JSON.parseObject(retJson,
					CloudResponse.class);

			if (reponse.getRequestStatus() == 0) {
				RegisterResponseDto result = new RegisterResponseDto();
				result.setMacno(request.getMacno());
				Map<String, String> data = reponse.getData();
				String deviceId = (String)data.get("deviceId");
				result.setDeviceId(Uuid32Utils.getId2Gwm(deviceId));
//				result.setDeviceId(deviceId);
				return result;
			}
			// errCode list:
			// 9001:deviceidalready exits(设备号已完成注册)
			// 9002:deviceidNot standard(非法设备号)
			// 3001:other error （其他错误）
			else if (reponse.getRequestStatus() == -1) {
				MyLogger.error("regist error! errCode=" + reponse.getErrcode()
						+ ", errDesc:" + reponse.getErrdes());

				return new RegisterResponseDto();
			} else {
				throw new CloudException(9, "unkown RequestStatus:"
						+ reponse.getRequestStatus());
			}
		} catch (Exception e) {
			MyLogger.error("regist error!", e);

			throw new CloudException(3001, "unkown exception");
		}

	}

	/**
	 * GWM uses this call for Activate action issued by GW
	 */
	@Override
	public ActivateResponseDto activate(ActivateRequestDto request)
			throws CloudException {
		try {
			String deviceId = request.getDeviceId();
			String version = request.getVersion();
			MyLogger.info("get activate req from gw,deviceId:" + deviceId);

			
			deviceId = Uuid32Utils.getId2Boco(deviceId);
			
			ActivateReq reqPojo = new ActivateReq(deviceId, version);

			String reqJson = JSON.toJSONString(reqPojo);
			
			MyLogger.info("activate reqJson=" + reqJson);
			String retJson = HttpUtils.sendPostUrl(CLOUD_BASE_URL
					+ BocoData.ACTIVATE, reqJson, "UTF-8", CONTENT_TYPE_JSON);
			MyLogger.info("===========================================activate ret INFO = "
					+ retJson);
			if(StringUtil.isEmpty(retJson)){
				MyLogger.error("retTurn json is null!");
				return new ActivateResponseDto();
			}
			CloudResponse reponse = JSON.parseObject(retJson,
					CloudResponse.class);

			if (reponse.getRequestStatus() == 0) {
				Map<String, String> data = reponse.getData();

				ActivateResponseDto result = new ActivateResponseDto();
				//TODO 接口文档里面有着三个字段，实际测试未返回,此处需要增加非空判断
				//Version 20
//				result.setDeviceId(DeviceIdUtils.getId2Gwm(data.get("deviceId")));
//				result.setMacno(data.get("macno"));
//				result.setSerialno(data.get("serialno"));
				String account = (String)data.get("gatewayAccount");
				String pwd = (String)data.get("keyword");
				
//				result.setGatewayAccount(account.substring(0, 8));
//				result.setKeyword(pwd.substring(0, 8));
//				KeyMapTmp.setAccount(account, pwd);
				
				
				result.setGatewayAccount(Uuid32Utils.getId2Gwm(account));
				result.setKeyword(Uuid32Utils.getId2Gwm(pwd));
				
				
				MyLogger.info("change account&pwd to GW...account:" + result.getGatewayAccount()
						+ " keyword:" + result.getKeyword());
				
//				result.setGatewayAccount(account);
//				result.setKeyword(pwd);
				//设置BOCO的返回值后GWM没有收到请求，怀疑GW直接通信这两个地址，因此换成GWM所在地址
				//Version 20 2015-04-07 
				//TODO 确认此处因为GWM地址，测试阶段手动设置成GWM地址
//				result.setMainServerUrl(data.get("mainServerUrl"));
//				result.setCommandServerUrl(data.get("commandServerUrl"));
				result.setMainServerUrl(DEFAULT_GWM_URL);
				result.setCommandServerUrl(DEFAULT_GWM_URL);
				ServerInfo.setMainServerUrl((String)data.get("mainServerUrl"));
				ServerInfo.setCommandServerUrl((String)data.get("commandServerUrl"));
				
				MyLogger.info("change the URLs to GWM url:" + result.getMainServerUrl());
				MyLogger.info("return to the GW---activate");
				
				return result;
			}
			// errCode list:
			// 9001:deviceidalready exits(设备号已完成注册)
			// 9002:deviceidNot standard(非法设备号)
			// 3001:other error （其他错误）
			else if (reponse.getRequestStatus() == -1) {
				MyLogger.error("ACTIVATE error! errCode="
						+ reponse.getErrcode() + ", errDesc:"
						+ reponse.getErrdes());

				return new ActivateResponseDto();

			} else {
				throw new CloudException(9, "unkown RequestStatus:"
						+ reponse.getRequestStatus());
			}
		} catch (Exception e) {
			MyLogger.error("activate error!", e);

			throw new CloudException(3001, "unkown exception");
		}

	}

	/**
	 * Result for command GWM uses this call for SensorReport command result
	 * sent by GW
	 */
	@Override
	public void sensorReport(SensorReportRequestDto request)
			throws CloudException {
		try {
			List<SensorInfoRequestDto> sensors = request.getSensors();
			
			//TODO 此处临时方案获取sessionId
			MyLogger.info("get sensorReport req from gw,deviceId:" + request.getDeviceId());
			String deviceId = Uuid32Utils.getId2Boco(request.getDeviceId());
			String sessionId = SessionCache.getSessionIdByDeviceId(deviceId);
			String  sysId =  SysIdCache.getSysId(deviceId);
			
			if(StringUtil.isEmpty(sessionId)){
				generateResetCommand(deviceId);
				MyLogger.info("gw[" + deviceId + "] lose his session, send reset command to this GW");
			}else{

				List<SensorEntity> sensorReqList = new ArrayList<SensorEntity>();
				for(SensorInfoRequestDto sensor :sensors){
					
					SensorEntity sensorEntity = new SensorEntity();
					sensorEntity.setBattery(sensor.getBattery());
					sensorEntity.setCurrVersion(sensor.getCurrVersion());
					sensorEntity.setDeviceName(sensor.getDeviceName());
					sensorEntity.setSensorType(sensor.getSensorType().getCode());
					sensorEntity.setSerialno(sensor.getSerialno());
					sensorEntity.setStatus(sensor.getStatus().getCode());
					
					sensorReqList.add(sensorEntity);
					
				}
				
				Sensors reqPojo = new Sensors(sensorReqList);
				MyLogger.info("get sensorReport req from gw " + deviceId );

				String reqJson = JSON.toJSONString(reqPojo);

				MyLogger.info("sensor report reqJson=" + reqJson);
				
				String retJson = HttpUtils.sendPostUrl(CLOUD_BASE_URL
						+ BocoData.SENSOR_REPORT 
						+ "?JSESSIONID=" + sessionId + "&SYSID=" + sysId, reqJson, "UTF-8",
						CONTENT_TYPE_JSON);
				MyLogger.info("sensorReport ret INFO = " + retJson);
				CommonDealWithRetJson(retJson, BocoData.SENSOR_REPORT,deviceId);

				
			}
//			

		} catch (Exception e) {
			MyLogger.error("sensorReport error!", e);

			throw new CloudException(3001, "unkown exception");
		}

	}

	/**
	 * Result for command GWM uses this call for FindSensor command result sent
	 * by GW
	 */
	@Override
	public FindSensorResponseDto findSensor(SensorReportRequestDto request)
			throws CloudException {
		
		try {
			List<SensorInfoRequestDto> sensors = request.getSensors();
			
			MyLogger.info("get findSensor req from gw,deviceId:" + request.getDeviceId());
			//TODO 此处临时方案获取sessionId
			String deviceId = Uuid32Utils.getId2Boco(request.getDeviceId());
			String sessionId = SessionCache.getSessionIdByDeviceId(deviceId);
			String  sysId =  SysIdCache.getSysId(deviceId);
			
			if(StringUtil.isEmpty(sessionId)){
				generateResetCommand(deviceId);
				MyLogger.info("gw[" + deviceId + "] lose his session, send reset command to this GW");
				throw new CloudException(10, "gw[" + deviceId + "] lose his session, send reset command to this GW");
			}else{
				List<SensorEntity> sensorReqList = new ArrayList<SensorEntity>();
				for(SensorInfoRequestDto sensor :sensors){
					
					SensorEntity sensorEntity = new SensorEntity();
					sensorEntity.setBattery(sensor.getBattery());
					sensorEntity.setCurrVersion(sensor.getCurrVersion());
					sensorEntity.setDeviceName(sensor.getDeviceName());
					sensorEntity.setSensorType(sensor.getSensorType().getCode());
					sensorEntity.setSerialno(sensor.getSerialno());
					sensorEntity.setStatus(sensor.getStatus().getCode());
					
					sensorReqList.add(sensorEntity);
					
				}
				
				Sensors reqPojo = new Sensors(sensorReqList);
				
				MyLogger.info("get findSensor req from gw[" + deviceId + "],sensor [" + sensors.size() + "] was found");
				
				String reqJson = JSON.toJSONString(reqPojo);
				
				MyLogger.info("findsensor reqJson=" + reqJson);
				String retJson = HttpUtils.sendPostUrl(CLOUD_BASE_URL
						+ BocoData.FIND_SENSOR
						+ "?JSESSIONID=" + sessionId + "&SYSID=" + sysId, reqJson, "UTF-8",
						CONTENT_TYPE_JSON);
				MyLogger.info("findSensor ret INFO = " + retJson);
				if(StringUtil.isEmpty(retJson)){
					MyLogger.error("retTurn json is null!");
					return new FindSensorResponseDto();
				}
				FindSensorResponse reponse = JSON.parseObject(retJson,
						FindSensorResponse.class);
				
				if (reponse.getRequestStatus() == 0) {
					FindSensorResponseDto result = new FindSensorResponseDto();
					List<FindSensorResponseDto.FindSensorInfo> retList = reponse.getData();
					result.setSensorData(retList);
					if(retList!=null){
						MyLogger.info("findSensor over successfully... ret sensor list size =" +retList.size());
					}else
						MyLogger.info("findSensor over successfully... ret sensor list size = 0");
				
					
					for(SensorEntity sensorEntity : sensorReqList){
						String reqSensorOnlineJson = JSON.toJSONString(sensorEntity);
						MyLogger.info("sensor online reqJson=" + reqSensorOnlineJson);
						String retSensorOnlineJson = HttpUtils.sendPostUrl(CLOUD_BASE_URL
								+ BocoData.SENSOR_ONLINE
								+ "?JSESSIONID=" + sessionId + "&SYSID=" + sysId, reqSensorOnlineJson, "UTF-8",
								CONTENT_TYPE_JSON);
						MyLogger.info("sensorOnline ret INFO = " + retSensorOnlineJson);
						CommonDealWithRetJson(retSensorOnlineJson, BocoData.SENSOR_ONLINE,deviceId);
					}
					
					
					return result;
					
				}
				// errCode list:
				// 3001:other error
				else if (reponse.getRequestStatus() == -1) {
					MyLogger.error("findSensor error! errCode="
							+ reponse.getErrcode() + ", errDesc:"
							+ reponse.getErrdes());
					
					if(BOCO_LOGIN_ERR_CODE.equals(reponse.getErrcode())
							|| BOCO_NOT_LOGIN_CODE.equals(reponse.getErrcode())
							|| BOCO_SESSION_ERR_CODE.equals(reponse.getErrcode()))
						 generateResetCommand(deviceId);
					return new FindSensorResponseDto();
					
				} else {
					throw new CloudException(9, "unkown RequestStatus:"
							+ reponse.getRequestStatus());
				}
				
			
			
			}

		} catch (Exception e) {
			MyLogger.error("findSensor error!", e);

			throw new CloudException(3001, "unkown exception");
		}
		
		
	}

	/**
	 * GWM uses this call when sensor becomes online
	 */
	@Override
	public void sensorOnline(SensorInfoRequestDto request)
			throws CloudException {

		try {
			String serialno = request.getSerialno();
			Integer battery = request.getBattery();
			String deviceName = request.getDeviceName();
			String currVersion = request.getCurrVersion();
			Integer sensorType = request.getSensorType().getCode();
			Integer status = request.getStatus().getCode();
			MyLogger.info("get sensorOnline req from gw,deviceId:" + request.getDeviceId());
			//TODO 此处临时方案获取sessionId
			String deviceId = Uuid32Utils.getId2Boco(request.getDeviceId());
			String sessionId = SessionCache.getSessionIdByDeviceId(deviceId);
			String  sysId =  SysIdCache.getSysId(deviceId);
			
			if(StringUtil.isEmpty(sessionId)){
				generateResetCommand(deviceId);
				MyLogger.info("gw[" + deviceId + "] lose his session, send reset command to this GW");
				throw new CloudException(10, "gw[" + deviceId + "] lose his session, send reset command to this GW");
			}else{
				SensorEntity reqPojo = new SensorEntity(serialno, battery,
						deviceName, currVersion, sensorType, status);
				
				//在收到sensor online 的时候先构造一个find result 返回给cloud
				List<SensorEntity> sensorReqList = new ArrayList<SensorEntity>();
				sensorReqList.add(reqPojo);
				Sensors findResult = new Sensors(sensorReqList);
				MyLogger.info("create findSensor before sensor online from gw[" + deviceId + "]");
				
				String reqJsonFindResult = JSON.toJSONString(findResult);
				
				MyLogger.info("findsensor reqJson=" + reqJsonFindResult);
				String retJsonFindResult = HttpUtils.sendPostUrl(CLOUD_BASE_URL
						+ BocoData.FIND_SENSOR
						+ "?JSESSIONID=" + sessionId + "&SYSID=" + sysId, reqJsonFindResult, "UTF-8",
						CONTENT_TYPE_JSON);
				MyLogger.info("findSensor ret INFO = " + retJsonFindResult);
				CommonDealWithRetJson(retJsonFindResult, BocoData.FIND_SENSOR,deviceId);
				
				MyLogger.info("get sensorOnline req from gw,deviceId:" + deviceId);
				
				String reqJson = JSON.toJSONString(reqPojo);
				
				MyLogger.info("sensor online reqJson=" + reqJson);
				String retJson = HttpUtils.sendPostUrl(CLOUD_BASE_URL
						+ BocoData.SENSOR_ONLINE
						+ "?JSESSIONID=" + sessionId + "&SYSID=" + sysId, reqJson, "UTF-8",
						CONTENT_TYPE_JSON);
				MyLogger.info("sensorOnline ret INFO = " + retJson);
				CommonDealWithRetJson(retJson, BocoData.SENSOR_ONLINE,deviceId);

			
			}
		} catch (Exception e) {
			MyLogger.error("sensorOnline error!", e);

			throw new CloudException(3001, "unkown exception");
		}

	}

	/**
	 * GWM uses this call when sensor becomes offline
	 */
	@Override
	public void sensorOffline(SensorInfoRequestDto request)
			throws CloudException {

		try {
			LOGGER.info("sensorOffline: sensor serial number = {}",
					request.getSerialno());

			
			MyLogger.info("get sensorOffline req from gw,deviceId:" + request.getDeviceId());
			String serialno = request.getSerialno();
			Integer battery = request.getBattery();
			String deviceName = request.getDeviceName();
			String currVersion = request.getCurrVersion();
			Integer sensorType = request.getSensorType().getCode();
			Integer status = request.getStatus().getCode();

			//TODO 此处临时方案获取sessionId
			String deviceId = Uuid32Utils.getId2Boco(request.getDeviceId());
			String sessionId = SessionCache.getSessionIdByDeviceId(deviceId);
			String  sysId =  SysIdCache.getSysId(deviceId);
			
			if(StringUtil.isEmpty(sessionId)){
				generateResetCommand(deviceId);
				MyLogger.info("gw[" + deviceId + "] lose his session, send reset command to this GW");
				throw new CloudException(10, "gw[" + deviceId + "] lose his session, send reset command to this GW");
			}else{
				SensorEntity reqPojo = new SensorEntity(serialno, battery,
						deviceName, currVersion, sensorType, status);
				
				MyLogger.info("get sensorOffline req from gw,deviceId:" + deviceId );
				
				String reqJson = JSON.toJSONString(reqPojo);
				MyLogger.info("sensorOffline  reqJson=" + reqJson);
				
				String retJson = HttpUtils.sendPostUrl(CLOUD_BASE_URL
						+ BocoData.SENSOR_OFFLINE
						+ "?JSESSIONID=" + sessionId + "&SYSID=" + sysId, reqJson, "UTF-8",
						CONTENT_TYPE_JSON);
				CommonDealWithRetJson(retJson, BocoData.SENSOR_OFFLINE,deviceId);
			
			
			}

		} catch (Exception e) {
			MyLogger.error("sensorOffline error!", e);

			throw new CloudException(3001, "unkown exception");
		}

	}

	/**
	 * GWM uses this call when sensor send new attribute normal data
	 */
	@Override
	public void dataReport(DataReportRequestDto request) throws CloudException {
		try {
			LOGGER.info("dataReport: sensor serial number = {}",
					request.getSerialno());

			String serialno = request.getSerialno();
			String attributename = request.getAttributeName();
			String value = request.getValue();
			Integer status = request.getStatus().getCode();
			
			
			MyLogger.info("get dataReport req from gw,deviceId:" + request.getDeviceId());
			//TODO 此处临时方案获取sessionId
			String deviceId = Uuid32Utils.getId2Boco(request.getDeviceId());
			String sessionId = SessionCache.getSessionIdByDeviceId(deviceId);
			String  sysId =  SysIdCache.getSysId(deviceId);
			
			if(StringUtil.isEmpty(sessionId)){
				generateResetCommand(deviceId);
				MyLogger.info("gw[" + deviceId + "] lose his session, send reset command to this GW");
				throw new CloudException(10, "gw[" + deviceId + "] lose his session, send reset command to this GW");
			}else{

				DataReportReq reqPojo = new DataReportReq(serialno, attributename,
						status, value);
				
				MyLogger.info("get DATA_REPORT req from gw,deviceId:" + deviceId);
				
				String reqJson = JSON.toJSONString(reqPojo);
				
				MyLogger.info("DATA REPORT reqJson=" + reqJson);
				
				String retJson = HttpUtils
						.sendPostUrl(CLOUD_BASE_URL + BocoData.DATA_REPORT
								+ "?JSESSIONID=" + sessionId + "&SYSID=" + sysId,
								reqJson, "UTF-8", CONTENT_TYPE_JSON);
				
				
				MyLogger.info("DATA REPORT retJson=" + retJson);
				CommonDealWithRetJson(retJson, BocoData.DATA_REPORT,deviceId);
			}
		} catch (Exception e) {
			MyLogger.error("dataReport error!", e);

			throw new CloudException(3001, "unkown exception");
		}

	}

	/**
	 * GWM uses this call when sensor send new attribute alarm data
	 */
	@Override
	public void alarmReport(DataReportRequestDto request) throws CloudException {
		try {
			String serialno = request.getSerialno();
			String attributename = request.getAttributeName();
			String value = request.getValue();
			Integer status = request.getStatus().getCode();
			
			MyLogger.info("get alarmReport req from gw,deviceId:" + request.getDeviceId());
			//TODO 此处临时方案获取sessionId
			String deviceId = Uuid32Utils.getId2Boco(request.getDeviceId());
			String sessionId = SessionCache.getSessionIdByDeviceId(deviceId);
			String  sysId =  SysIdCache.getSysId(deviceId);
			
			if(StringUtil.isEmpty(sessionId)){
				generateResetCommand(deviceId);
				MyLogger.info("gw[" + deviceId + "] lose his session, send reset command to this GW");
				throw new CloudException(10, "gw[" + deviceId + "] lose his session, send reset command to this GW");
			}else{
			
				
				AlarmReq reqPojo = new AlarmReq(serialno, attributename, status,
						value);
				
				MyLogger.info("get ALARM_REPORT req from gw,serialno:" + serialno + "; deviceId=" + deviceId);
				
				String reqJson = JSON.toJSONString(reqPojo);
				MyLogger.info("ALARM_REPORT reqJson=" + reqJson);
				String retJson = HttpUtils.sendPostUrl(CLOUD_BASE_URL
						+ BocoData.ALARM_REPORT
						+ "?JSESSIONID=" + sessionId + "&SYSID=" + sysId, reqJson, "UTF-8",
						CONTENT_TYPE_JSON);
				
				
				MyLogger.info("ALARM_REPORT retJson=" + retJson);
				CommonDealWithRetJson(retJson, BocoData.ALARM_REPORT,deviceId);
			
			}

		} catch (Exception e) {
			MyLogger.error("alarmReport error!", e);

			throw new CloudException(3001, "unkown exception");
		}
	}

	/**
	 * GWM uses this call when GW requests the command “login” first time
	 * 登录完成后王find sensor cache里面新增一条 记录，如果存在则覆盖
	 * 
	 */
	@Override
	public BillingLoginResponseDto billingLogin(BillingLoginRequestDto request)
			throws CloudException {
		try {
			String deviceId = request.getDeviceId();
			String account = request.getAccount();
			String keyword = request.getKeyword();
			
//			String[] keyMap = KeyMapTmp.getAccount(account);
//			account=keyMap[0];
//			keyword=keyMap[1];
			
			MyLogger.info("get billingLogin req from gw,deviceId:" + request.getDeviceId());
			account=Uuid32Utils.getId2Boco(account);
			keyword=Uuid32Utils.getId2Boco(keyword);
			deviceId=Uuid32Utils.getId2Boco(deviceId);
			
			LoginReq reqPojo = new LoginReq(account, keyword);

			MyLogger.info("get login req from gw,account:" + account 
					+ "  deviceId:" + deviceId);

			
			String reqJson = JSON.toJSONString(reqPojo);

			String retJson = HttpUtils.sendPostUrl(CLOUD_BASE_URL
					+ BocoData.LOGIN, reqJson, "UTF-8", CONTENT_TYPE_JSON);
			if(StringUtil.isEmpty(retJson)){
				MyLogger.error("retTurn json is null!");
				return new BillingLoginResponseDto();
			}
			CloudResponse reponse = JSON.parseObject(retJson,
					CloudResponse.class);

			if (reponse.getRequestStatus() == 0) {
				Map<String, String> data = reponse.getData();

				BillingLoginResponseDto result = new BillingLoginResponseDto();
				result.setSessionId((String)data.get("sessionId"));
				result.setSystemCode((String)data.get("systemCode"));
				
				MyLogger.info("success login...");
				
				//TODO 缓存sessionId
				SessionCache.loginSession(deviceId, result.getSessionId());
				SysIdCache.putSysID(deviceId, (String)data.get("systemCode"));
				
				return result;
			}
			// errCode list:
			// 9001:deviceidalready exits(设备号已完成注册)
			// 9002:deviceidNot standard(非法设备号)
			// 3001:other error （其他错误）
			else if (reponse.getRequestStatus() == -1) {
				MyLogger.error("LOGIN error! errCode=" + reponse.getErrcode()
						+ ", errDesc:" + reponse.getErrdes());
				
				if(BOCO_LOGIN_ERR_CODE.equals(reponse.getErrcode())
						|| BOCO_NOT_LOGIN_CODE.equals(reponse.getErrcode())
						|| BOCO_SESSION_ERR_CODE.equals(reponse.getErrcode()))
					 generateResetCommand(deviceId);
				return new BillingLoginResponseDto();
			} else {
				throw new CloudException(9, "unkown RequestStatus:"
						+ reponse.getRequestStatus());
			}

		} catch (Exception e) {
			MyLogger.error("billingLogin error!", e);

			throw new CloudException(3001, "unkown exception");
		}

	}

	/**
	 * GWM uses this call when GW did not request the command 3 min
	 */
	@Override
	public BillingLogoutResponseDto billingLogout(BillingLoginRequestDto request)
			throws CloudException {
		LOGGER.info("billingLogout: account = {}", request.getAccount());
		
		MyLogger.info("get billingLogout req from gw,deviceId:" + request.getDeviceId());
		
		String account = request.getAccount();
		String keyword = request.getKeyword();
		String deviceId = request.getDeviceId();
		
		GatewayErrorReportReq reqPojo = new GatewayErrorReportReq(deviceId, account, keyword);
		//长连接超时/网关超时
		reqPojo.setErrorCode(1002);
		
		MyLogger.info("get GW OFFLINE( billing logout) req from gw, deviceId=" + deviceId);
		
		String reqJson = JSON.toJSONString(reqPojo);
		MyLogger.info("GATEWAY_ERROR_REPORT OFFLINE reqJson=" + reqJson);
		String retJson = HttpUtils.sendPostUrl(CLOUD_BASE_URL
				+ BocoData.GATEWAY_ERROR_REPORT, reqJson, "UTF-8",CONTENT_TYPE_JSON);
		
		
		if(StringUtil.isEmpty(retJson)){
			MyLogger.error("retTurn json is null!");
			return new BillingLogoutResponseDto();
		}
			
		CloudResponse reponse = JSON.parseObject(retJson,
				CloudResponse.class);
		
		if (reponse.getRequestStatus() == 0) {
			MyLogger.info(BocoData.GATEWAY_ERROR_REPORT + " 上送完毕");
			String commandListJson = reponse.getData().get("commandList");
			List<Command> commandList = JSON.parseArray(commandListJson, Command.class);
			if(commandList !=null && commandList.size()>=1){
				for(Command command: commandList){
					setCache(command, deviceId);
				}
			}
		}
		// errCode list:
		// 3001:other error
		else if (reponse.getRequestStatus() == -1) {
			MyLogger.error(BocoData.GATEWAY_ERROR_REPORT + " error! errCode="
					+ reponse.getErrcode() + ", errDesc:"
					+ reponse.getErrdes());
			
			if(BOCO_LOGIN_ERR_CODE.equals(reponse.getErrcode())
					|| BOCO_NOT_LOGIN_CODE.equals(reponse.getErrcode())
					|| BOCO_SESSION_ERR_CODE.equals(reponse.getErrcode()))
				 generateResetCommand(deviceId);
			
		} else {
			throw new CloudException(9, "unkown RequestStatus:"
					+ reponse.getRequestStatus());
		}
	
		return new BillingLogoutResponseDto();
	}
	
	
	private static void generateResetCommand(String deviceId){
		CommandInfo command = CommandFactory.createResetCommand(7);
		command.setDeviceId(Uuid32Utils.getId2Gwm(deviceId));
		MyLogger.info("reset command name: " + command.getName());
		
		List<CommandInfo> commandList = CommandCache.get(deviceId);
		if(commandList!=null && commandList.size()>0){
			CommandInfo commandFirst = commandList.get(0);
			
			if(!"reset".equals(commandFirst.getName())){
				CommandCache.add(deviceId, command);
				MyLogger.info("gw[" + deviceId + "]insert reset command success.");
			}else
				MyLogger.info("gw[" + deviceId + "]exists reset command.");
		}else{
			CommandCache.add(deviceId, command);
			MyLogger.info("gw[" + deviceId + "]insert reset command success.");
		}
		
	}
	
	
	
	
	private static void CommonDealWithRetJson(String retJson,String commandType,String deviceId)throws CloudException{
		if(StringUtil.isEmpty(retJson)){
			MyLogger.error("retTurn json is null!");
		}else{
			CloudResponse reponse = JSON.parseObject(retJson,
					CloudResponse.class);
			
			if (reponse.getRequestStatus() == 0) {
				MyLogger.info(commandType + " 上送完毕");
			}
			// errCode list:
			// 3001:other error
			else if (reponse.getRequestStatus() == -1) {
				MyLogger.error(commandType + " error! errCode="
						+ reponse.getErrcode() + ", errDesc:"
						+ reponse.getErrdes());
				
				if(BOCO_LOGIN_ERR_CODE.equals(reponse.getErrcode())
						|| BOCO_NOT_LOGIN_CODE.equals(reponse.getErrcode())
						|| BOCO_SESSION_ERR_CODE.equals(reponse.getErrcode())){
					MyLogger.info("ready to reset GW...");
					generateResetCommand(deviceId);
				}
				
				
//				throw new CloudException(reponse.getErrcode(),
//						reponse.getErrdes());
				
			} else {
				throw new CloudException(9, "unkown RequestStatus:"
						+ reponse.getRequestStatus());
			}
		}
		
	}
	
	
	public static void setCache(Command command ,String deviceId){
		CommandInfo commandInfo = new CommandInfo();
		if(command.getCommand().equals(BocoData.COMMAND_TYPE_SENSOR_REPORT)){
			commandInfo = CommandFactory.createSensorReportCommand(1);
		}
		else if(command.getCommand().equals(BocoData.COMMAND_TYPE_FIND_SENSOR)){
			commandInfo = CommandFactory.createFindSensorCommand(2);
		}
		else if(command.getCommand().equals(BocoData.COMMAND_TYPE_DATA_REPORT)){
			String sensorieee = command.getSensorieee().toLowerCase();
//			String sourceId = SourceIdCache.getSourceId(sensorieee);
			if(!StringUtil.isEmpty(sensorieee)){
				commandInfo = CommandFactory.createDataReportCommand(3,sensorieee);
				MyLogger.info("[DATA REPORT COMMAND] sensorIeee :" + sensorieee);
			}
			else
				MyLogger.error("can not find sourceId in the sourceId cache, pls to check the excel!");
		}
		else if(command.getCommand().equals(BocoData.COMMAND_TYPE_INIT_GATEWAY)){
			commandInfo = CommandFactory.createInitGatewayCommand(4);
		}
		else if(command.getCommand().equals(BocoData.COMMAND_TYPE_REMOVE_SENSOR)){
			List<String> sensorList = command.getSensorieees();
			sensorList = getLowerCase(sensorList);
//			sensorList = getSourceId(sensorList);
			if(sensorList==null || sensorList.size()==0){
				MyLogger.error("empty sensor ieee list ");
			}else{
				commandInfo = CommandFactory.createRemoveSensorCommand(5, sensorList);
				MyLogger.info("[REMOVE SENSOR COMMAND] sensorIeees :" + sensorList);
			}
			
		}
		else if(command.getCommand().equals(BocoData.COMMAND_TYPE_ADD_SENSOR)){
			List<String> sensorList = command.getSensorieees();
			sensorList = getLowerCase(sensorList);
//			sensorList = getSourceId(sensorList);
			if(sensorList==null || sensorList.size()==0){
				MyLogger.error("empty sensor ieee list " );
			}else{
				commandInfo = CommandFactory.createAddSensorCommand(6, sensorList);
				MyLogger.info("[ADD SENSOR COMMAND] sensorIeees " + sensorList);
			}
		}
		if (commandInfo.getDeviceId()==null) {
            commandInfo.setDeviceId(Uuid32Utils.getId2Gwm(deviceId));
        } 
		CommandCache.add(deviceId, commandInfo);
		//add find sensor command when it is add sensor command
		if(command.getCommand().equals(BocoData.COMMAND_TYPE_ADD_SENSOR)){
			CommandInfo command2 = CommandFactory.createFindSensorCommand(2);
			if (command2.getDeviceId()==null) {
				command2.setDeviceId(Uuid32Utils.getId2Gwm(deviceId));
	        } 
			CommandCache.add(deviceId, command2);
		}
		
	}
	
	private static List<String> getLowerCase(List<String> sensorList){
		List<String> result = new ArrayList<String>();
		if(sensorList ==null || sensorList.size()==0)
			return null;
		else{
			for(String sensorIeee:sensorList){
				if(!StringUtil.isEmpty(sensorIeee))
					result.add(sensorIeee.toLowerCase());
			}
			return result;
			
		}
	}

}
