package com.mas.util;


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.mas.listener.InitialListener;

public class VM
{
	private final Logger log = LoggerFactory.getLogger(InitialListener.class);
	
	public Resource resource = new ClassPathResource("vm.properties");
	
	private Properties vmConfig;

	private static VM manager;

	private String webContextPath;// 应用的根目录
	private String hostPayUrl;
	private String resServer;
	
	private VM(String wcp)
	{
		init(wcp);
	}

	public VM()
	{
	}

	public String getWebContextPath()
	{
		return webContextPath;
	}

	public void setWebContextPath(String webContextPath)
	{
		this.webContextPath = webContextPath;
	}


	/**
	 * 重新初始化
	 * @author hhk
	 * @return void
	 * @date 2011-8-23 上午10:23:05
	 * @since 1.0.0
	 */
	public void reInit()
	{
		vmConfig = new Properties();
		try
		{
			InputStream is = new FileInputStream(resource.getFile());
			vmConfig.load(is);
			hostPayUrl=getProperty("hostPayUrl");
			resServer=getProperty("resServer");
		}
		catch (Exception e)
		{
			log.error(e.getLocalizedMessage(), e);
		}
		if (log.isInfoEnabled())
		{
			log.info("VM装载完成");
		}
	}

	public void init(String wcp)
	{
		reInit();
		webContextPath = wcp;
	}

	public synchronized static VM getInatance(String wcp)
	{
		if (manager == null)
		{
			manager = new VM(wcp);
		}
		return manager;
	}

	// ----------------------------------
	public synchronized static VM getInatance()
	{
		if (manager == null)
		{
			manager = new VM();
			manager.reInit();
		}
		return manager;
	}

	public String getProperty(String key)
	{
		log.info("VM提取属性 [" + key + "=" + vmConfig.getProperty(key) + "]");
		return vmConfig.getProperty(key).trim();
	}

	public boolean getBooleanProperty(String name)
	{
		String value = getProperty(name);

		if (value == null)
			return false;

		return (new Boolean(value)).booleanValue();
	}

	public int getIntProperty(String name)
	{
		String value = getProperty(name);
		try
		{
			return Integer.parseInt(value);
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public String getHostPayUrl() {
		return hostPayUrl;
	}

	public String getResServer() {
		return resServer;
	}

	public void setResServer(String resServer) {
		this.resServer = resServer;
	}

}
