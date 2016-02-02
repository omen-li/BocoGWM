/**
 * Project Name:
 * File Name:Xmldata.java
 * Package Name:cn.net.nbse.open.message
 * Date:2013-7-26上午9:05:36
 * Copyright (c) 2013, liyidong@nbse.net.cn All Rights Reserved.
 *
*/

package com.dsr.cloud.backend.message;


/**
 * ClassName:Xmldata <br/>
 * Function: 数据字典 <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2013-7-26 上午9:05:36 <br/>
 * @author   Liyidong
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class XmlData {
	
	public static final String CONTENT="content";
	
	public static final String RET_CODE="retCode";
	
	public static final String RET_MSG="retMsg";
	
	public static final String STATUS="status";
	
	public static final int YES=1;
	
	public static final int NO=0;
	
	public static final String TRUE="true";
	
	public static final String FLASE="flase";
	/**
	 * 成功状态
	 */
	public static final String SUCCESS="0000";
	
	/**
	 * 失败状态
	 */
	public static final String FAIL="1111";
	
	public static final String REPEAT="1112";
	
	public static final String REPEAT_MSG="数据重复!";
	
	public static final String TIME_OUT="1113";
	
	public static final String TIME_OUT_MSG="连接超时!";
	
	public static final String DECODE_ERROR="1114";
	
	public static final String DECODE_ERROR_MSG="数据验签未通过!";
	
	/**
	 * 失败通用提示
	 */
	public static final String FAIL_MSG="系统忙,请稍后再试";
	
	
	//===================================DB
	
	//节目类型：电视剧
	public static final int PROGRAM_TYPE_TV=0;
	
	//节目类型：电影
	public static final int PROGRAM_TYPE_FILM=1;
	
	//节目类型：其他
	public static final int PROGRAM_TYPE_OTHERS=2;
	
	//设备类型：机顶盒
	public static final int DEVICE_TYPE_BOX= 1;
	
	//设备类型：电视机
	public static final int DEVICE_TYPE_TV= 2;
	
	//运营商归属地类型：城市
	public static final int LOCALE_TYPE_CITY=0;
	
	//运营商归属地类型：地区
	public static final int LOCALE_TYPE_AREA=1;
	
	//央视
	public static final int STATION_TYPE_YS=1;
	
	//卫视
	public static final int STATION_TYPE_WS=2;
	
	//数字
	public static final int STATION_TYPE_SZ=3;
	
	//地区城市
	public static final int STATION_TYPE_DQCS=4;
	
	//CETV
	public static final int STATION_TYPE_CETV=5;
	
	//原创
	public static final int STATION_TYPE_YC=6;
}
