package com.mas.rave.appannie;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ScoreManager {
	//private static final String CONFIG_PATH = Thread.currentThread().getContextClassLoader().getResource("").toString().replaceAll("file:/", "") + "config.properties";
//	private static final String CONFIG_PATH = System.getProperty("user.dir") + "config.properties";
	//private static final String CONFIG_PATH = ConfigManager.class.getResourceAsStream("config.properties");
	private static Properties prop = null;
//	private static long lastModifyTime = 0;

	private ScoreManager() {
	}

	public static InputStream getResourceAsStream(String resource) throws IOException{
		InputStream in = null;
		ClassLoader loader = ScoreManager.class.getClassLoader();
		if(loader != null)
			in = loader.getResourceAsStream(resource);
		if(in == null)
			in = ClassLoader.getSystemResourceAsStream(resource);
		if(in == null){
			in = new FileInputStream(System.getProperty("user.dir")+"/"+resource);
			//ClassLoader.getSystemResourceAsStream(System.getProperty("user.dir")+"/"+resource);
		}
		if(in == null)
			throw new IOException("Could not find resource " + resource);
		return in;
	}

	private static void init(String filePath){

		prop = new Properties();
		try{
//			filePath = (filePath == null || filePath.length() == 0) ? CONFIG_PATH : filePath;
//			if(filePath.indexOf(":") == 2)
//				filePath = filePath.substring(1, filePath.length());
			//System.out.println(filePath);
			//prop.load(new FileInputStream(filePath));
			prop.load(getResourceAsStream("config/score.properties"));
//			File file = new File(CONFIG_PATH);
//			lastModifyTime = file.lastModified();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static String getProperty(String key){
		String result = "";
		if(prop == null){
			init("");
		}
		try{
			// File file = new File(CONFIG_PATH);
			// long tempTime = file.lastModified();
			// if (tempTime > lastModifyTime) {
			// prop.clear();
			// init("");
			// }
			if(prop.containsKey(key)){
				result = prop.getProperty(key);
			}
		}catch(Exception exce){
			exce.printStackTrace();
		}
		return result;
	}

	public static String getConfigData(String key){
		return getProperty(key);
	}

	public static String getConfigData(String key,String defaultValue){
		return getProperty(key).length() == 0 ? defaultValue : getProperty(key);
	}
}
