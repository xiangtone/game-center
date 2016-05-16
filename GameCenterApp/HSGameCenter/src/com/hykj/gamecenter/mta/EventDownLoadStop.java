package com.hykj.gamecenter.mta;

import java.util.Properties;

/**
 * @author thunderlei
 * 下载暂停事件
 */
public class EventDownLoadStop implements MtaEvent{
	public static final String EVENT_ID = "event_download_stop";
	public static final String PARAM_REASON = "stop_reason";
	public Properties properties ; 
	public EventDownLoadStop(){
		super();
		this.properties = new Properties();
	}
	public void setReason(String reason){
		properties.setProperty(PARAM_REASON, reason);
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
	public EventDownLoadStop(Properties properties) {
		super();
		this.properties = properties;
	}
	public String getEventId(){
		return EVENT_ID;
	}
}
