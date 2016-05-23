package com.hykj.gamecenter.mta;

import java.util.Properties;

/**
 * @author thunderlei
 * 回流事件，用户一个月及以上事件未使用后再使用时触发
 */
public class EventReturn implements MtaEvent{
	public static final String EVENT_ID = "event_return";
	public Properties properties = null; 
	public EventReturn(){
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
	public EventReturn(Properties properties) {
		super();
		this.properties = properties;
	}
	public String getEventId(){
		return EVENT_ID;
	}
}
