package com.mas.rave.controller;


import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.Log;
import com.mas.rave.main.vo.MusicInfo;
import com.mas.rave.service.LogService;
import com.mas.rave.util.DateUtil;
import com.mas.rave.util.StringUtil;

/**
 * log业务处理
 * @author jieding
 *
 */
@Controller
@RequestMapping("/log")
public class LogController {
	Logger log = Logger.getLogger(LogController.class);

	
	@Autowired
	private LogService logService;

	
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
			String operator = request.getParameter("operator");
			String res = request.getParameter("res");
			String action = request.getParameter("action");
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			Log log = new Log();	
			
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);
			if(StringUtils.isNotEmpty(operator)){
				log.setOperator(operator.trim());
			}
			if(StringUtils.isNotEmpty(res)){
				log.setRes(res.trim());
			}
			if(StringUtils.isNotEmpty(action)){
				log.setAction(action.trim());
			}
			if(startTime!=null&&!startTime.equals("")){
				log.setStartTime(DateUtil.StringToDate(startTime+" 00:00:00"));	
			}
			if(endTime!=null&&!endTime.equals("")){
				log.setEndTime(DateUtil.StringToDate(endTime+" 23:59:59"));	
			}
			PaginationVo<Log> result = logService.searchLog(log, currentPage, pageSize);
			request.setAttribute("result", result);	
			
		} catch (Exception e) {
			log.error("LogController list", e);
			PaginationVo<Log> result = new PaginationVo<Log>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "log/list";
	}
	/** 查看详细 */
	@RequestMapping("/info/{id}")
	public String info(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			Log log = logService.getLog(id);
			model.addAttribute("log", log);
		} catch (Exception e) {
			log.error("LogController info", e);
		}
		return "log/logInfo";
	}
}
