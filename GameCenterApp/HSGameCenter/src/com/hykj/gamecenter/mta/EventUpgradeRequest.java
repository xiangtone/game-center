package com.hykj.gamecenter.mta;

import java.util.Properties;

/**
 * @author thunderlei
 * 应用升级请求事件
 */
public class EventUpgradeRequest implements MtaEvent{
	public static final String EVENT_ID = "upgrade_request";
	public Properties properties ; 
	public EventUpgradeRequest(){
		super();
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
	public EventUpgradeRequest(Properties properties) {
		super();
		this.properties = properties;
	}
	public String getEventId(){
		return EVENT_ID;
	}
}
