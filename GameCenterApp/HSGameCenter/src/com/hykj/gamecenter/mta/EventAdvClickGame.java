package com.hykj.gamecenter.mta;

import java.util.Properties;


/**
 * @author thunderlei
 * 游戏页面广告位点击事件
 */
public class EventAdvClickGame implements MtaEvent {
	public static final String EVENT_ID = "event_adv_click_game";
	public static final String PARAM_ADV_POSITION = "advPosition";
	public Properties properties;

	public EventAdvClickGame() {
		super();
		this.properties = new Properties();
	}

	public void setAdvPosition(String position) {
		properties.setProperty(PARAM_ADV_POSITION, position);
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

	public EventAdvClickGame(Properties properties) {
		super();
		this.properties = properties;
	}

	public String getEventId() {
		return EVENT_ID;
	}
}
