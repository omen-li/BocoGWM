package com.greenlive.home.udp;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dsr.cloud.backend.utils.MyLogger;
import com.dsr.cloud.backend.utils.PropertiesUtil;

@Component
public class UdpClienMain {

/*	static{
		MyLogger.info("start to connect command server...");
		String url = PropertiesUtil.getValue("boco_command_server_upd_url");
		String account = PropertiesUtil.getValue("boco_command_server_account");
		String portStr = PropertiesUtil.getValue("boco_command_server_port");
		Integer port = Integer.parseInt(portStr);
		SingletonUdp udpClient = SingletonUdp.INSTANCE;
		udpClient.start(url, account, port);
		MyLogger.info("command server connect successfully...");
		MyLogger.info("url    :" + url);
		MyLogger.info("port   :" + portStr);
	}*/
	
	
	public static void main(String[] args) {
		System.out.println("1");
	}
	
	
	@Autowired
	UDPThread uDPThread;
	
	@PostConstruct
	public void start(){
		new Thread(uDPThread).start();
	}
	
	
}
