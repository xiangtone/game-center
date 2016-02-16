package com.mas.rave.util;

/**
 * 文件处理类
 * 
 * @author liwei.sz
 * 
 */
public class ConfigDateApplication {
	private static ConfigDateApplication confi = null;

	public static ConfigDateApplication getInstance() {
		if (confi == null) {
			return new ConfigDateApplication();
		}
		return null;
	}

	//
	public String getPropertie(String str) {
		PropertyFileManager matchManager = PropertyFileRegister.getPropertyFileManager(PropertyFileRegister.RESPONESE_CONFIG);
		String value = matchManager.getPropertyValue(str);
		return value;
	}

	public String getServicePropertie(String str) {
		PropertyFileManager matchManager = PropertyFileRegister.getPropertyFileManager(PropertyFileRegister.SERVER_CONFIG);
		String value = matchManager.getPropertyValue(str);
		return value;
	}
}
