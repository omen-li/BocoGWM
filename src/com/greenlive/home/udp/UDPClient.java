
package com.greenlive.home.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.dsr.cloud.backend.cache.CommandCache;
import com.dsr.cloud.backend.cache.SourceIdCache;
import com.dsr.cloud.backend.message.BocoData;
import com.dsr.cloud.backend.pojo.golbal.HeartBeatTimer;
import com.dsr.cloud.backend.shimtest.service.ServerCommandService;
import com.dsr.cloud.backend.utils.MyLogger;
import com.dsr.cloud.backend.utils.PropertiesUtil;
import com.dsr.cloud.backend.utils.StringUtil;
import com.dsr.cloud.backend.utils.Uuid32Utils;
import com.dsr.gateway.manager.device.CommandFactory;
import com.dsr.gateway.manager.dto.api.gateway.CommandInfo;
import com.greenlive.home.GatewayCommand;

public class UDPClient {
	
	@Autowired
	private ServerCommandService serverCommandService;
	
	private static String serverUrl;
	private static  String  account;
	private static Integer port;
	public static final String SUCCESS_KEY = "success";
	public static final String ERROR_KEY = "error";
	public static final String HIT_KEY = "0";
	public static final String LOGIN_KEY = "login";
	public static final String SPLITOR = "|";
	private static final int LOST_SERVER_MAX_TIME=10;
	private static final Integer DEFAULT_COMMAND_ID=1;

	
	static{
		
		serverUrl=PropertiesUtil.getValue("boco_command_server_upd_url");;
		account=PropertiesUtil.getValue("boco_command_server_account");
		String portStr = PropertiesUtil.getValue("boco_command_server_port");
		port= Integer.parseInt(portStr);
		
		MyLogger.info("command server connect successfully...");
		MyLogger.info("url    :" + serverUrl);
		MyLogger.info("port   :" + port);
	}
	
	public UDPClient(String url,String account,Integer port){
		this.serverUrl=url;
		this.account=account;
		this.port=port;
	}
	
	public void login() throws IOException{
		try {
			// UDP
			DatagramSocket ds= new DatagramSocket();
			HeartBeatTimer.setHeartBeatTimer(0);
				// 登陆服务器
				String loginStr = LOGIN_KEY + SPLITOR + account;
				// 构造要发送的包
				URL url=new URL(serverUrl);
				DatagramPacket lp = new DatagramPacket(loginStr.getBytes(), loginStr
						.length(), InetAddress.getByName(url.getHost()), port);
				String heartStr = "0";
				DatagramPacket hp = new DatagramPacket(heartStr.getBytes(), heartStr
						.length(), InetAddress.getByName(url.getHost()), port);
				
//				new Thread(new HeartThread(ds, hp)).start();
				new Thread(new HeartThread(ds, lp)).start();
				// 发送登陆信息
				ds.send(lp);
				// 循环接收
				byte[] buf = new byte[1024];
				DatagramPacket rp = new DatagramPacket(buf, 1024);
				boolean isEnd = false;
					MyLogger.info("udp client started..");
				while (!isEnd) {
//					int lostNum=HeartBeatTimer.getHeartBeatTimer();
//					if(lostNum<LOST_SERVER_MAX_TIME){
//						MyLogger.info("heart beat still " +  lostNum + "seconds");
//						if(lostNum>=5)
//							MyLogger.info("lost url for " +  lostNum + "seconds");
						
						ds.receive(rp);
						// 取出信息
						String content = new String(rp.getData(), 0, rp.getLength());
						String rip = rp.getAddress().getHostAddress();
						int rport = rp.getPort();
						// 输出接收到的数据
//						if("success".equals(content)){
//							if(lostNum>=1){
//								HeartBeatTimer.setHeartBeatTimer(lostNum-1);
//							}
//						}else
							MyLogger.info(rip + ":" + rport + " >>>> " + content);
						
						try{
				            if (!content.equals(HIT_KEY) && !content.equals(SUCCESS_KEY)) {		
								
			                    if (content.startsWith("{") && content.endsWith("}")){
			                    	//此处接收消息
			                    	JSONObject jsonobj=	JSONObject.parseObject(content);
			                    	
			                    	MyLogger.info("UDPClient recieve the command add request:" + jsonobj);
			                    	GatewayCommand data=(GatewayCommand)JSONObject.toJavaObject(jsonobj,GatewayCommand.class);
//			                    	setCache(data);
			                    	
			                    	CommandInfo command = getCommands(data);
			                    	
			                    	serverCommandService.processCommand(null, null, command,data.getDeviceId());
			                    	
			                    	MyLogger.info("UDPClient successfully deal the command:" + data.getCommand() 
			                    			+ "; sensorieee:"+ data.getSensorieee()
			                    			+ "; sensorieees:" + data.getSensorieees()
			                    			);
			                    }
			              }
						}catch(Exception e){
							MyLogger.error("udp client deal message error!", e);
						}
//					}else{
//						throw new IOException("UDP CLIENT LOSE CONNECTION! START TO RE-CONNECT");
//					}
				}
				ds.close();
		}catch (IOException e) {
			throw new IOException(e);
		} catch (Exception e) {
		
			MyLogger.error("udpClien error", e);
			throw new IOException(e);
		}
		
		

	
	}
	
	public static String[] splitString(String src, String splitor) {
		StringTokenizer s = new StringTokenizer(src, splitor);

		String[] strs = new String[s.countTokens()];
		int i = 0;
		while (s.hasMoreTokens()) {
			strs[i++] = s.nextToken();
		}

		return strs;
	}
	
	
	
	
	
	public static CommandInfo getCommands(GatewayCommand data){
		CommandInfo command = new CommandInfo();
		String deviceId = data.getDeviceId();
		if(data.getCommand().equals(BocoData.COMMAND_TYPE_SENSOR_REPORT)){
			command = CommandFactory.createSensorReportCommand(1);
		}
		else if(data.getCommand().equals(BocoData.COMMAND_TYPE_FIND_SENSOR)){
			command = CommandFactory.createFindSensorCommand(2);
		}
		else if(data.getCommand().equals(BocoData.COMMAND_TYPE_DATA_REPORT)){
			String sensorieee = data.getSensorieee().toLowerCase();
			
			
//			String sourceId = SourceIdCache.getSourceId(sensorieee);
			if(!StringUtil.isEmpty(sensorieee)){
				command = CommandFactory.createDataReportCommand(3,sensorieee);
				MyLogger.info("[DATA REPORT COMMAND] sensorIeee :" + sensorieee);
			}
			else
				MyLogger.error("can not find sourceId in the sourceId cache, pls to check the excel!");
		}
		else if(data.getCommand().equals(BocoData.COMMAND_TYPE_INIT_GATEWAY)){
			command = CommandFactory.createInitGatewayCommand(4);
		}
		else if(data.getCommand().equals(BocoData.COMMAND_TYPE_REMOVE_SENSOR)){
			List<String> sensorList = data.getSensorieees();
			sensorList = getLowerCase(sensorList);
//			sensorList = getSourceId(sensorList);
			if(sensorList==null || sensorList.size()==0){
				MyLogger.error("empty sensor ieee list ");
			}else{
				command = CommandFactory.createRemoveSensorCommand(5, sensorList);
				MyLogger.info("[REMOVE SENSOR COMMAND] sensorIeees :" + sensorList);
			}
			
			CommandInfo command2 = CommandFactory.createSensorReportCommand(1);
			if (command2.getDeviceId()==null) {
				command2.setDeviceId(Uuid32Utils.getId2Gwm(deviceId));
	        } 
			CommandCache.add(deviceId, command2);
		}
		else if(data.getCommand().equals(BocoData.COMMAND_TYPE_ADD_SENSOR)){
			List<String> sensorList = data.getSensorieees();
			sensorList = getLowerCase(sensorList);
//			sensorList = getSourceId(sensorList);
			if(sensorList==null || sensorList.size()==0){
				MyLogger.error("empty sensor ieee list " );
			}else{
				command = CommandFactory.createAddSensorCommand(6, sensorList);
				MyLogger.info("[ADD SENSOR COMMAND] sensorIeees " + sensorList);
			}
		}
		if (command.getDeviceId()==null) {
            command.setDeviceId(Uuid32Utils.getId2Gwm(deviceId));
        } 
		//add find sensor command when it is add sensor command
		if(data.getCommand().equals(BocoData.COMMAND_TYPE_ADD_SENSOR)){
			CommandInfo command2 = CommandFactory.createFindSensorCommand(2);
			if (command2.getDeviceId()==null) {
				command2.setDeviceId(Uuid32Utils.getId2Gwm(deviceId));
	        } 
			CommandCache.add(deviceId, command2);
		}
		
		
		return command;
	}
	
	
	
	
	
	
	
	public static void setCache(GatewayCommand data){
		CommandInfo command = new CommandInfo();
		String deviceId = data.getDeviceId();
		if(data.getCommand().equals(BocoData.COMMAND_TYPE_SENSOR_REPORT)){
			command = CommandFactory.createSensorReportCommand(1);
		}
		else if(data.getCommand().equals(BocoData.COMMAND_TYPE_FIND_SENSOR)){
			command = CommandFactory.createFindSensorCommand(2);
		}
		else if(data.getCommand().equals(BocoData.COMMAND_TYPE_DATA_REPORT)){
			String sensorieee = data.getSensorieee().toLowerCase();
//			String sourceId = SourceIdCache.getSourceId(sensorieee);
			if(!StringUtil.isEmpty(sensorieee)){
				command = CommandFactory.createDataReportCommand(3,sensorieee);
				MyLogger.info("[DATA REPORT COMMAND] sensorIeee :" + sensorieee);
			}
			else
				MyLogger.error("can not find sourceId in the sourceId cache, pls to check the excel!");
		}
		else if(data.getCommand().equals(BocoData.COMMAND_TYPE_INIT_GATEWAY)){
			command = CommandFactory.createInitGatewayCommand(4);
		}
		else if(data.getCommand().equals(BocoData.COMMAND_TYPE_REMOVE_SENSOR)){
			List<String> sensorList = data.getSensorieees();
			sensorList = getLowerCase(sensorList);
//			sensorList = getSourceId(sensorList);
			if(sensorList==null || sensorList.size()==0){
				MyLogger.error("empty sensor ieee list ");
			}else{
				command = CommandFactory.createRemoveSensorCommand(5, sensorList);
				MyLogger.info("[REMOVE SENSOR COMMAND] sensorIeees :" + sensorList);
			}
			
			CommandInfo command2 = CommandFactory.createSensorReportCommand(1);
			if (command2.getDeviceId()==null) {
				command2.setDeviceId(Uuid32Utils.getId2Gwm(deviceId));
	        } 
			CommandCache.add(deviceId, command2);
		}
		else if(data.getCommand().equals(BocoData.COMMAND_TYPE_ADD_SENSOR)){
			List<String> sensorList = data.getSensorieees();
			sensorList = getLowerCase(sensorList);
//			sensorList = getSourceId(sensorList);
			if(sensorList==null || sensorList.size()==0){
				MyLogger.error("empty sensor ieee list " );
			}else{
				command = CommandFactory.createAddSensorCommand(6, sensorList);
				MyLogger.info("[ADD SENSOR COMMAND] sensorIeees " + sensorList);
			}
		}
		if (command.getDeviceId()==null) {
            command.setDeviceId(Uuid32Utils.getId2Gwm(deviceId));
        } 
		CommandCache.add(deviceId, command);
		//add find sensor command when it is add sensor command
		if(data.getCommand().equals(BocoData.COMMAND_TYPE_ADD_SENSOR)){
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
	
	private static List<String> getSourceId(List<String> sensorList){
		List<String> result = new ArrayList<String>();
		
		for(String sensorIeee:sensorList){
			String sourceId = SourceIdCache.getSourceId(sensorIeee.toLowerCase());
			if(!StringUtil.isEmpty(sourceId))
				result.add(sourceId);
			
		}
		
		return result;
		
		
	}
	
	
	public static void main(String[] args) {
//		String a="a";
//		System.out.println(a.toUpperCase());
		
		
	     String url="http://gw2.gosmarthome.cn";
	     Integer port=8079;
	     String factoryId="gptest2015";
	  
	     UDPClient client=new UDPClient(url,factoryId,port);
	      try {
			client.login();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
