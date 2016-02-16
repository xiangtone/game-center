package com.mas.rave.common;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

import com.mas.rave.util.ConfigHelper;

public class ContextInitListener extends ContextLoaderListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ConfigHelper.setModuleName("marketservice");
		//LogManager.init("logback.xml");
		//SystemConfig.load("SysConfig.groovy");
		super.contextInitialized(event);
	}

	
}
