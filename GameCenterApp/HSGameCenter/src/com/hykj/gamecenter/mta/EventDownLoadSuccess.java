package com.hykj.gamecenter.mta;

import java.util.Properties;

/**
 * @author thunderlei
 * 下载成功事件
 */
public class EventDownLoadSuccess implements MtaEvent{
	public static final String EVENT_ID = "event_download_success";
	public static final String PARAM_APP_NAME = "app_name";
	public Properties properties ; 
	public EventDownLoadSuccess(){
		super();
		properties = new Properties();
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
	public EventDownLoadSuccess(Properties properties) {
		super();
		this.properties = properties;
	}
	public String getEventId(){
		return EVENT_ID;
	}
}
