package com.mas.rave.util;


public class PropertyFileRegister {
	public static final String RESPONESE_CONFIG = "resource.properties";
	public static final String SERVER_CONFIG = "server.properties";
	public static final String AAPT_CONFIG = "aapt";
	/**
	 * 配置目录
	 */
	//private static final String CONFIG_ROOT = PropertyFileManager.USER_DIR.concat(File.separator).concat("config").concat(File.separator);

	static {
		registerPropertyFile();
	}

	private static void registerPropertyFile() {
		registerInnerPropertyFile();
	}

	private static void registerInnerPropertyFile() {
		PropertyFileManager.setInstance(RESPONESE_CONFIG, PropertyFileManager.class.getResourceAsStream("/config/resource.properties"));
		PropertyFileManager.setInstance(SERVER_CONFIG, PropertyFileManager.class.getResourceAsStream("/config/server.properties"));
	}

	public static PropertyFileManager getPropertyFileManager(String fileKey) {
		return PropertyFileManager.getInstance(fileKey);
	}
}
