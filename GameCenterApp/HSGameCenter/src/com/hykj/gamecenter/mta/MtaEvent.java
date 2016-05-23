package com.hykj.gamecenter.mta;

import java.util.Properties;


/**
 * @author lei
 * 事件统一接口
 */
public interface MtaEvent {
	/*
	 * 设置各事件相应的参数
	 */
	void setProperties(Properties properties);

	Properties getProperties();

	/*
	 * 获取事件对应ID，需与MTA服务端设置一致
	 */
	String getEventId();
}
