package com.reportforms.action;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Controller
@RequestMapping("/wellcome")
public class WellcomeAction {
	
	private static final Logger log = Logger.getLogger(WellcomeAction.class);
	
	@RequestMapping("/execute")
	public String execute(){
		
		log.debug("load wellcome jsp ...");
		return "wellcome";
	}

}
