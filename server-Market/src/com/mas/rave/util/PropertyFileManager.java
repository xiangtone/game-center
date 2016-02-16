package com.mas.rave.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 属性文件管理器工具类。
 * 
 * @author jacs
 * 
 */
public class PropertyFileManager {
	private static final Logger logger = LoggerFactory.getLogger(PropertyFileManager.class);
	/**
	 * 文件的修改次数
	 */
	private int modifyNumber = 0;
	/**
	 * 文件的最后修改时间
	 */
	private long lastModifyTime = 0;
	/**
	 * 文件对象
	 */
	private File propertyFile = null;
	/**
	 * 文件对应的属性配制
	 */
	private Properties properties = null;
	/**
	 * 文件列表
	 */
	private static final Map<String, PropertyFileManager> propertyFiles = new HashMap<String, PropertyFileManager>();
	/**
	 * 程序的工作目录
	 */
	public static final String USER_DIR = System.getProperty("user.dir");

	private PropertyFileManager(String path) {
		propertyFile = new File(this.getClass().getResource(path).getPath());
		lastModifyTime = propertyFile.lastModified();
		if (lastModifyTime == 0) {
			System.err.println(path + " is not exist!");
		}
		properties = new Properties();
		try {
			properties.load(new FileInputStream(propertyFile));
		} catch (FileNotFoundException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	private PropertyFileManager(File file) {
		propertyFile = file;
		lastModifyTime = propertyFile.lastModified();
		if (lastModifyTime == 0) {
			System.err.println(file.getAbsolutePath() + " is not exist!");
		}
		properties = new Properties();
		try {
			properties.load(new FileInputStream(propertyFile));
		} catch (FileNotFoundException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	private PropertyFileManager(InputStream inputStream) {
		properties = new Properties();
		try {
			properties.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private PropertyFileManager(Properties properties) {
		this.properties = properties;
	}

	public static void setInstance(String key, String path) {
		if (getInstance(key) == null) {
			PropertyFileManager manager = new PropertyFileManager(path);
			propertyFiles.put(key, manager);
		} else {
			logger.error("设置重复:" + key);
		}
	}

	public static void setInstance(String key, InputStream inputStream) {
		if (getInstance(key) == null) {
			PropertyFileManager manager = new PropertyFileManager(inputStream);
			propertyFiles.put(key, manager);
		} else {
			logger.error("设置重复:" + key);
		}
	}

	public void setPropertyFile(String key, File propertyFilePath) {
		propertyFile = propertyFilePath;
	}

	public static void setInstance(String key, Properties properties) {
		if (getInstance(key) == null) {
			PropertyFileManager manager = new PropertyFileManager(properties);
			propertyFiles.put(key, manager);
		} else {
			logger.error("设置重复:" + key);
		}
	}

	public static PropertyFileManager getInstance(String key) {
		return propertyFiles.get(key);
	}

	public boolean fileIsChange() {
		if (null != propertyFile) {
			long new_lastModifyTime = propertyFile.lastModified();
			return new_lastModifyTime != this.lastModifyTime;
		} else {
			return false;
		}
	}

	public String getPropertyValue(final String propertyName, final String defaultValue) {
		if (propertyFile != null) {
			long new_lastModifyTime = propertyFile.lastModified();
			if (new_lastModifyTime != 0) {
				if (new_lastModifyTime > lastModifyTime) {
					properties.clear();
					logger.info("reload properties!");
					try {
						properties.load(new FileInputStream(propertyFile));
					} catch (FileNotFoundException e) {
						logger.error("", e);
					} catch (IOException e) {
						logger.error("", e);
					}
					modifyNumber++;
					lastModifyTime = new_lastModifyTime;
				}
			} else {
				if (lastModifyTime == 0) {
					logger.error(propertyFile.getAbsolutePath() + " is not exist!");
				} else {
					logger.error(propertyFile.getAbsolutePath() + " is deleted");
				}
				return defaultValue;
			}
		}
		String obj = properties.getProperty(propertyName);
		return obj == null ? defaultValue : obj;
	}

	public String getPropertyValue(final String propertyName) {
		return getPropertyValue(propertyName, null);
	}

	public int getPropertyValueAsInt(String propertyName, int defaultValue) {
		String value = getPropertyValue(propertyName, null);
		if (null != value) {
			return Integer.parseInt(value);
		}
		return defaultValue;
	}

	public void setPropertyValue(final String propertyName, final String propertyValue) {
		properties.setProperty(propertyName, propertyValue);
		if (propertyFile != null) {
			storeToFile();
		}
	}

	private void storeToFile() {
		try {
			properties.store(new FileWriter(propertyFile), "moidfyNumber:" + (modifyNumber + 1));
		} catch (FileNotFoundException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	public String removeProperty(final String propertyName) {
		Object obj = properties.remove(propertyName);
		if (propertyFile != null) {
			storeToFile();
		}
		return obj.toString();
	}

	public int getModifyNumber() {
		return modifyNumber;
	}

	public Date getModifyDate() {
		return new Date(lastModifyTime);
	}

	/**
	 * @return the propertyfiles
	 */
	public static Map<String, PropertyFileManager> getPropertyfiles() {
		return propertyFiles;
	}

	/**
	 * @return the userDir
	 */
	public static String getUserDir() {
		return USER_DIR;
	}

	/**
	 * @return the lastModifyTime
	 */
	public long getLastModifyTime() {
		return lastModifyTime;
	}

	/**
	 * @return the propertyFile
	 */
	public File getPropertyFile() {
		return propertyFile;
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

}
