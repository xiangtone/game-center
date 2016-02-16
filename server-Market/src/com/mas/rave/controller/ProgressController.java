package com.mas.rave.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.mas.rave.common.web.FileUploadListener;

@Controller
@RequestMapping("/progress")
public class ProgressController{
	Logger log = Logger.getLogger(ProgressController.class);
	@ResponseBody
	@RequestMapping("/getprogress")
	public String getProgress(HttpServletRequest request){
	    	HttpSession session = request.getSession();
			if (session == null) {
				return "{\"flag\":\"session\"}";
			}
			FileUploadListener uploadProgressListener = (FileUploadListener) session
						.getAttribute("uploadProgressListener");
			if (uploadProgressListener == null) {
				//log.info("ProgressController getprogress 监听器未启动");
				return "{\"flag\":\"0\"}";
			}
			int ret = uploadProgressListener.getPercentDone();
			//System.out.println("new ->:" + String.valueOf(ret));
			//log.info("ProgressController getprogress  :"+ ret);
			if(ret==100){
				session.setAttribute("uploadProgressListener",null);
			}
			return "{\"flag\":\""+ret+"\"}";				   
	}
	@ResponseBody
	@RequestMapping("/closeprogress")
	public void closeProgress(HttpServletRequest request){
	    	HttpSession session = request.getSession();
			if (session != null) {
				session.setAttribute("uploadProgressListener",null);
				log.info("ProgressController closeProgress 进度条关闭 ....");
			}			
	}
}
