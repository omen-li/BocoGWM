package com.dsr.cloud.backend.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetHtml {
	
	public static void main(String[] args) throws InterruptedException {
//		// TODO Auto-generated method stub
//		String url = "http://www.baitv.com/program/cctv-cctv1-w1.html";
//		for(int i =0;i<=1000;i++){
//			String content = getURLContent(url,"UTF-8");
//			Document doc = Jsoup.parse(content);
//			int nodes = doc.select("div[class=schedule-list]").select("li").size();
//			System.out.println("第"+i+"次获取,node size=[" + nodes+"]");
//			Thread.sleep(5000);
//		}

	}
	
	/**
	 * get方式登陆并清楚缓存和cookie
	 * 返回的List 0=cookieVal  1=content
	 * @param surl
	 * @param encoding
	 * @param reqestBody
	 * @return
	 * @throws Exception
	 */
	public static List<String> getContent(String surl,String encoding,String cookieVal)throws Exception{
		
		List<String> retList= new ArrayList<String>();
		
		/** 
		* 首先要和URL下的URLConnection对话。 URLConnection可以很容易的从URL得到。比如： // Using 
		* java.net.URL and //java.net.URLConnection 
		*/  
		
		URL url = new URL(surl);  
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();  
		
		
		/** 
		* 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。 
		* 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做： 
		*/  
		
		//发送cookie信息上去，以表明自己的身份，否则会被认为没有权限  
		connection.setRequestProperty("Cookie", cookieVal);//设置登陆配置	
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0");
		connection.setRequestProperty("Connection", "keep-alive");
		
//		connection.setDoOutput(true);  //打开输出，向服务器输出参数（POST方式、字节）（写参数之前应先将参数的字节长度设置到配置"Content-Length"<字节长度>）
//		connection.setDoInput(true);//打开输入，从服务器读取返回数据
		connection.setRequestMethod("GET"); //设置登录模式为POST（字符串大写）
		connection.setInstanceFollowRedirects(false); 
		connection.connect();
		
		//取得cookie，相当于记录了身份，供下次访问时使用  
		//用于迭代读取Cookie，为以后使用
//		HttpURLConnection.getHeaderFields()).get("Set-Cookie")
		//HttpURLConnection.getHeaderField("Set-Cookie")也可用于读取Cookie，但不一定能读取完全
		//格式:JSESSIONID=541884418E77E7F07363CCEE91D4FF7E; Path=/
//		String cookieVal = connection.getHeaderField("Set-Cookie");
		List<String> cookie2 = connection.getHeaderFields().get("Set-Cookie"); 
		Map<String,String> cookieMap = new HashMap<String, String>();
		
		if(cookie2 !=null){
			for(String cookie: cookie2){
				String[] cookies = cookie.split(";",-1);
				for(int i = 0 ;i<cookies.length;i++){
					String[] cookieValue = cookies[i].split("=",-1);
					cookieMap.put(cookieValue[0], cookieValue[1]);
				}
			}
		}
		
		StringBuffer cookieBuffer = new StringBuffer();
		Set<Map.Entry<String, String>> set = cookieMap.entrySet();
		for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
			cookieBuffer.append(entry.getKey())
			.append("=")
			.append(entry.getValue())
			.append(";");
			
		}
		cookieVal =cookieBuffer.toString();
		System.out.println("重新刷新cookie");
		System.out.println("coockie [" + cookieVal + "]");
		
		retList.add(cookieVal);
		
		
		InputStream urlStream = connection.getInputStream();  
		
		StringBuffer content = new StringBuffer();
		 InputStreamReader theHTML = new InputStreamReader(urlStream, encoding);
		   int c;
		   while ((c = theHTML.read()) != -1) {
		    content.append((char) c);
		   }
		
//		reqPage++;
		urlStream.close();
		urlStream=null;
		connection.disconnect();
		System.out.println("重新刷新cookie完成");
		retList.add(content.toString());
		return    retList; 

	}
		
}
