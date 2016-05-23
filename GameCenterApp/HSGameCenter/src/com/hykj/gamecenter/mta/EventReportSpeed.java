package com.hykj.gamecenter.mta;

import java.util.Properties;

/**
 * @author thunderlei
 * 下载速度上报事件，为计算事件，记录单个设备平均速度和单个应用平均速度
 */
public class EventReportSpeed implements MtaEvent {
	public static final String EVENT_ID = "event_report_speed";
	public static final String PARAM_MID = "MID";
	public static final String PARAM_SPEED ="Speed";
	public static final String PARAM_APP_NAME = "App_Name";
	public Properties properties;

	public EventReportSpeed() {
		super();
		this.properties = new Properties();
	}

	public void setMID(String mid) {
		properties.setProperty(PARAM_MID, mid);
	}

	public void setSpeed(String speed){
		properties.setProperty(PARAM_SPEED, speed);
	}
	
	public void setAppName(String appName){
		properties.setProperty(PARAM_APP_NAME, appName);
	}
	
	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
		this.properties = properties;
	}

	@Override
	public Properties getProperties() {
		// TODO Auto-generated method stub
		return properties;
	}

	public EventReportSpeed(Properties properties) {
		super();
		this.properties = properties;
	}

	public String getEventId() {
		return EVENT_ID;
	}
}
