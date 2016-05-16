package com.hykj.gamecenter.mta;

import java.util.Properties;

/**
 * @author thunderlei
 * 下载请求事件
 */
public class EventDownLoadRequest implements MtaEvent{
	public static final String EVENT_ID = "event_download_request1";
	public static final String PARAM_FROM_PAGE = "from_page";
	public Properties properties ; 
	public EventDownLoadRequest(){
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
	public EventDownLoadRequest(Properties properties) {
		super();
		this.properties = properties;
	}
	public String getEventId(){
		return EVENT_ID;
	}
}
