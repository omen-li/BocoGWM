
package com.greenlive.home.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.util.StringTokenizer;

import com.dsr.cloud.backend.utils.MyLogger;


public class UDPClientDemo {
	

	private String serverUrl;
	private String  account;
	private Integer port;
	public static final String SUCCESS_KEY = "success";
	public static final String ERROR_KEY = "error";
	public static final String HIT_KEY = "0";
	public static final String LOGIN_KEY = "login";
	public static final String SPLITOR = "|";

	public UDPClientDemo(String url,String account,Integer port){
		this.serverUrl=url;
		this.account=account;
		this.port=port;
	}
	
	public void login() throws IOException{
		// UDP
		DatagramSocket ds= new DatagramSocket();
			
			String loginStr = LOGIN_KEY + SPLITOR + account;
		
			URL url=new URL(serverUrl);
			DatagramPacket lp = new DatagramPacket(loginStr.getBytes(), loginStr
					.length(), InetAddress.getByName(url.getHost()), port);
			String heartStr = "0";
			
//			DatagramPacket hp = new DatagramPacket(heartStr.getBytes(), heartStr
//					.length(), InetAddress.getByName(url.getHost()), port);
			
			new Thread(new HeartThread(ds, lp)).start();
			ds.send(lp);
		
			byte[] buf = new byte[1024];
			DatagramPacket rp = new DatagramPacket(buf, 1024);
			boolean isEnd = false;
			MyLogger.info("udp client started..");
			while (!isEnd) {
				ds.receive(rp);
				
				String content = new String(rp.getData(), 0, rp.getLength());
				String rip = rp.getAddress().getHostAddress();
				int rport = rp.getPort();
				
				System.out.println(rip + ":" + rport + " >>>> " + content);
             if (!content.equals(HIT_KEY) && !content.equals(SUCCESS_KEY)) {		
					
                    if (content.startsWith("{") && content.endsWith("}")){
                    	
                    
//                    	JSONObject jsonobj=	JSONObject.parseObject(content);
                    	System.out.println("content = " + content);
//                    	GatewayCommand data=(GatewayCommand)JSONObject.toJavaObject(jsonobj,GatewayCommand.class);
				    }
              }
			}
			ds.close();
		

	
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
	
	
	public static void main(String[] args) {
		
//		String url="http://gw2.gosmarthome.cn";
//		Integer port=8078;
		
//	    String url="http://123.124.236.149";
//		Integer port=8078;
		
//		String url="http://192.168.1.199";
//		Integer port=8078;
		
		String url="http://gw2.gosmarthome.cn";
		Integer port=8079;
		
//		String url="http://119.40.24.25";
//		Integer port=8079;
		
//		String url="http://10.41.1.29"; 
//		Integer port=8086;
		
//		10.32.2.57  8078
//		String url="http://10.32.2.232"; 
//		Integer port=8078;

		
		String factoryId="gptest2015";
	      UDPClientDemo client=new UDPClientDemo(url,factoryId,port);
	      try {
			client.login();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
