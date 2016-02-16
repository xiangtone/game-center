package com.mas.rave.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/versionquery")
public class VersionqueryController {
	Logger log = Logger.getLogger(VersionqueryController.class);

	/**
	 * 分布查看生效列表
	 * 
	 * @param request
	 * @return
	 */
	 @RequestMapping("/versionquery")
	public void versionquery(HttpServletRequest request, HttpServletResponse response) {
		RequestDispatcher dispatcher = request.getRequestDispatcher("versionquery.jsp");
		try {
			dispatcher.forward(request, response);
			// response.sendRedirect("../../versionquery.html");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
