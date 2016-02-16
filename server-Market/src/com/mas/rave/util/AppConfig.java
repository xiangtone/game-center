package com.mas.rave.util;

public class AppConfig {
	public static final String MARKETT_PAC=ConfigDateApplication.getInstance().getServicePropertie("markett.pac").trim();
	public static final String RUNNING_FRED_PAC=ConfigDateApplication.getInstance().getServicePropertie("running.fred.pac").trim();
	public static final String ROPE_RESCUE_PAC=ConfigDateApplication.getInstance().getServicePropertie("rope.rescue.pac").trim();
	
	
	public static final String MARKETT_KEY=ConfigDateApplication.getInstance().getServicePropertie("markett.key").trim();
	public static final String RUNNING_FRED_KEY=ConfigDateApplication.getInstance().getServicePropertie("running.fred.key").trim();
	public static final String ROPE_RESCUE_KEY=ConfigDateApplication.getInstance().getServicePropertie("rope.rescue.key").trim();
}
