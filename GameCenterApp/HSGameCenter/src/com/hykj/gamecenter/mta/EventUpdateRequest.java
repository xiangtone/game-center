package com.hykj.gamecenter.mta;

import java.util.Properties;

/**
 * @author thunderlei
 * 自升级下载请求事件
 */
public class EventUpdateRequest implements MtaEvent{
	public static final String EVENT_ID = "event_update_request";
	public static final String PARAM_VERSION_CODE = "version_code";
	public static final String PARAM_VERSION_NAME = "version_name";
	public Properties properties ; 
	public EventUpdateRequest(){
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
	public EventUpdateRequest(Properties properties) {
		super();
		this.properties = properties;
	}
	public String getEventId(){
		return EVENT_ID;
	}
}
