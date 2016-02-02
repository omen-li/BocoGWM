package com.greenlive.home.udp;



import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dsr.cloud.backend.utils.MyLogger;
import com.dsr.cloud.backend.utils.PropertiesUtil;

@Component
public class UDPThread implements Runnable {
	@Autowired
	UDPClient uDPClient;
	
	public void run() {
		while (true) {
				  try {
					  uDPClient.login();
					} 
				  catch (IOException e) {
					  MyLogger.error("error for udp client to login...",e);
				  }
				  catch (Exception e) {
					  MyLogger.error("error to run UDPThread",e);
				  }
				 

		}
	}
	
	

	
}
