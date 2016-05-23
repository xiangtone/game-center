package com.hykj.gamecenter.mta;

import java.util.Properties;

/**
 * @author thunderlei
 * 自升级下载成功事件
 */
public class EventUpdateDownloaded implements MtaEvent{
	public static final String EVENT_ID = "event_update_downloaded";
	public static final String PARAM_VERSION_CODE = "version_code";
	public static final String PARAM_VERSION_NAME = "version_name";
	public Properties properties ; 
	public EventUpdateDownloaded(){
		super();
		properties = new Properties();
	}
	public void setVersionName(String versionName){
		properties.setProperty(PARAM_VERSION_NAME, versionName);
	}
	
	public void setVersionCode(String versionCode){
		properties.setProperty(PARAM_VERSION_CODE, versionCode);
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
	public EventUpdateDownloaded(Properties properties) {
		super();
		this.properties = properties;
	}
	public String getEventId(){
		return EVENT_ID;
	}
}
