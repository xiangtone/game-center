package com.reportforms.common.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class ValidateLoginFilter implements Filter{

	@Override
	public void destroy() {
		// do nothing
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest _request = (HttpServletRequest)request;
		HttpServletResponse _response = (HttpServletResponse)response;
		String uri = _request.getRequestURI();
		String contextPath = _request.getContextPath();
		if(StringUtils.contains(uri, "versionquery") || StringUtils.equalsIgnoreCase(uri, contextPath + "/") || StringUtils.containsIgnoreCase(uri, "/static/") || StringUtils.containsIgnoreCase(uri, "/user/login")
				||StringUtils.containsIgnoreCase(uri, "/widget/")|| StringUtils.containsIgnoreCase(uri, "/ad/")
				||StringUtils.containsIgnoreCase(uri, "/elecCard/")||StringUtils.containsIgnoreCase(uri, "/onlineMusic/")){
			chain.doFilter(request, response);
		}else{
			if(_request.getSession().getAttribute("loginUser") != null){
				chain.doFilter(request, response);
			}else{
				_response.sendRedirect(contextPath + "/");
			}
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// do nothing
		
	}

}
