package com.hykj.gamecenter.mta;

import java.util.Properties;

/**
 * @author thunderlei
 * 暂停后继续下载事件
 */
public class EventDownLoadResume implements MtaEvent {
	public static final String EVENT_ID = "event_download_resume";
	public Properties properties;

	public EventDownLoadResume() {
		super();
		this.properties = new Properties();
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

	public EventDownLoadResume(Properties properties) {
		super();
		this.properties = properties;
	}

	public String getEventId() {
		return EVENT_ID;
	}
}
