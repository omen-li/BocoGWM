package com.greenlive.home.udp;



import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.alibaba.fastjson.JSONObject;
import com.dsr.cloud.backend.pojo.golbal.HeartBeatTimer;
import com.dsr.cloud.backend.utils.MyLogger;
import com.greenlive.home.GatewayCommand;


public class HeartThread implements Runnable {
	private DatagramSocket ds;
	private DatagramPacket p;

	public HeartThread(DatagramSocket ds, DatagramPacket p) {
		this.ds = ds;
		this.p = p;
	}

	public void run(){
		
		
		
		while (true) {
			try {
					ds.send(p);
//					HeartBeatTimer.setHeartBeatTimer(HeartBeatTimer.getHeartBeatTimer()+1);
					
					Thread.sleep(5000);
				
			} catch (IOException e) {
				MyLogger.error("heart beat io error",e);
			} catch (InterruptedException e) {
				MyLogger.error("heart beat InterruptedException",e);
			}

		}
	}
}
