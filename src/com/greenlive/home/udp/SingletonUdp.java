package com.greenlive.home.udp;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public enum  SingletonUdp  {
	INSTANCE;
	
	UDPThread uDPThread;
	
	public void start(String url,String account,Integer port){
		new Thread(new UDPThread()).start();
	}

}
