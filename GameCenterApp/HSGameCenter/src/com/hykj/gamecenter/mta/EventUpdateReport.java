package com.hykj.gamecenter.mta;

import java.util.Properties;

public class EventUpdateReport implements MtaEvent{
	public static final String EVENT_ID = "event_report_update";
	public static final String PARAM_VERSION_NAME = "version_name";
	public Properties properties ; 
	public EventUpdateReport(){
		super();
		properties = new Properties();
	}
	public void setVersionName(String versionName){
		properties.setProperty(PARAM_VERSION_NAME, versionName);
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
	public EventUpdateReport(Properties properties) {
		super();
		this.properties = properties;
	}
	public String getEventId(){
		return EVENT_ID;
	}
}
