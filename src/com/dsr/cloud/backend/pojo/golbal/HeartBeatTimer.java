package com.dsr.cloud.backend.pojo.golbal;

public enum HeartBeatTimer {
	INSTANCE;
	private static Integer heartBeatTimer=0;
	
	public static Integer getHeartBeatTimer() {
		return heartBeatTimer;
	}
	
	public static void setHeartBeatTimer(Integer heartBeatTimer) {
		HeartBeatTimer.heartBeatTimer = heartBeatTimer;
	}

}
