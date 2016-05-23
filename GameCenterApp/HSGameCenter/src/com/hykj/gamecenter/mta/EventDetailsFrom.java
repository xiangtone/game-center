package com.hykj.gamecenter.mta;

import java.util.Properties;

/**
 * @author thunderlei
 *
 * 详情页打开来源
 */
public class EventDetailsFrom implements MtaEvent{
	public static final String EVENT_ID = "details_page_from";
	public static final String PARAM_FROM_PAGE = "from_page";
	public Properties properties ; 
	public EventDetailsFrom(){
		super();
		properties = new Properties();
	}
	public void setFromPage(String fromPage){
		properties.setProperty(PARAM_FROM_PAGE, fromPage);
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
	public EventDetailsFrom(Properties properties) {
		super();
		this.properties = properties;
	}
	public String getEventId(){
		return EVENT_ID;
	}
}
