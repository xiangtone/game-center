/**
 * InitialListener.java
 * com.mesic.wpg.action
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2011-7-6 		zhaowei
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
*/

package com.mas.listener;

import java.util.Calendar;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.mas.util.VM;
/**
 * ClassName:InitialListener
 * @author   hhk
 * @since    Ver 0.9
 * @Date	 2013-7-6		上午11:52:37
 * @see 	 
 */
public class InitialListener implements ServletContextListener,InitializingBean {
	private final Logger logger = LoggerFactory.getLogger(InitialListener.class);
	
	public void contextDestroyed(ServletContextEvent event) {
		logger.info("web容器停止-可以再次将缓存的数据存进数据库");
	}

	public void contextInitialized(ServletContextEvent event) {
//		ApplicationContext context=WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
//		logger.info(event.getServletContext().getContextPath());
		logger.info(event.getServletContext().getRealPath("/"));
		//此处可以放置vm配置
		VM.getInatance(event.getServletContext().getRealPath("/"));
//		System.out.println(+event.getServletContext().getContextPath());
		event.getServletContext().setAttribute("vm", VM.getInatance());
		logger.info("web容器初始化开始");
		
//		org.apache.ibatis.logging.LogFactory.useLog4JLogging();
		new Thread(new Runnable() {
			public void run() {
				
				int currentMinite=Calendar.getInstance().get(Calendar.MINUTE);
				if(currentMinite<55){}/*启动索引更新*/ 
					//indexUpdateService.executeUpdate();
			}
		}).start();
	}

	public void afterPropertiesSet() throws Exception {
		logger.info("初始化在线用户.");
	}
	
}

