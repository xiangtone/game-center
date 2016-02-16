package com.mas.rave.util;

import java.io.File;

import org.apache.commons.lang.StringUtils;

public class ConfigHelper {
private static ConfigHelper instance = new ConfigHelper();
	
	private String configPath;
	
	private String moduleName;
	
	public static ConfigHelper getInstance(){
		return instance;
	}
	
	public static final String getConfigPath(){
		String confDir = instance.getConfDir();
		if(StringUtils.isEmpty(instance.configPath)){
			String confPath = System.getenv("conf_path");
			System.out.println("=============env path:" + confPath);
			if(StringUtils.isEmpty(confPath)){
				confPath = System.getProperty("user.dir");
			}
			instance.configPath = confPath;
			if(instance.configPath.endsWith("/")){
				instance.configPath = instance.configPath.substring(0,instance.configPath.length()-1);
			}
			if(StringUtils.isNotEmpty(confDir)){
				instance.configPath = instance.configPath + File.separator + confDir;
			}
		}else{
			if(instance.configPath.endsWith("/")){
				instance.configPath = instance.configPath.substring(0,instance.configPath.length()-1);
			}
		}
		System.out.println("===============instance.configPath:" + (instance.configPath));
		return instance.configPath;
	}
	
	private String getConfDir(){
		String confDir = "conf";
		if(StringUtils.isNotEmpty(instance.moduleName)){
			if(instance.moduleName.startsWith("/")){
				confDir = "conf" + instance.moduleName;
			}else{
				confDir = "conf" + File.separator +instance.moduleName;
			}
			if(confDir.endsWith("/")){
				confDir = confDir.substring(0, (confDir.length() - 1));
			}
			return confDir;
		}
		return confDir;
	}
	
	public static void setConfigPath(String basePath){
		instance.configPath = basePath; 
	}
	
	public static void setModuleName(String moduleName){
		instance.moduleName = moduleName;
	}
	
	public static final String getFullPath(String fileName){
		String configPath = getConfigPath();
		return configPath + File.separator + fileName;
	}
	
	public static void main(String[] args) {
		//ConfigHelper.setConfigPath("D:/workapps/ivx/vlife/trunk/core/base/");
		ConfigHelper.setModuleName("ivx/portal/");
		System.out.println(ConfigHelper.getConfigPath());
	}
}
