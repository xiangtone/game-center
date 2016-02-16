package com.reportforms.common.web;

import javax.servlet.ServletRequest;

public class RequestUtils {
	public static int getInt(ServletRequest request,String paramName,int defaultValue){
		try{
			return Integer.valueOf(request.getParameter(paramName));
		}catch(NumberFormatException ex){
			return defaultValue;
		}
	}
}
