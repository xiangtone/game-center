package com.reportforms.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.datasource.DataSourceInstances;
import com.reportforms.datasource.DataSourceSwitch;
import com.reportforms.model.RaveDebacleLog;
import com.reportforms.service.RaveDebacleLogService;

@Controller
@RequestMapping("/ravedebaclelog")
public class RaveDebacleLogAction {
	
	private final Logger log = Logger.getLogger(RaveDebacleLogAction.class);
	
	@Autowired
	private RaveDebacleLogService<RaveDebacleLog> raveDebacleLogService;
	
	@RequestMapping("/list")
	public String page(){
		return "ravedebaclelog/list";
	}
	
	@ResponseBody
	@RequestMapping("/query")
	public Map<String, Object> query(HttpServletRequest request){
		
		Map<String, Object> map = new HashMap<String, Object>();
		PaginationBean paginationBean = new PaginationBean(request);
		
		List<RaveDebacleLog> list = null;
		Integer counts = 0;
		try {
			//切换数据源
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL2);
			counts = raveDebacleLogService.queryAllCounts(paginationBean);
			if(null == counts || counts.intValue() == 0){
				list = new ArrayList<RaveDebacleLog>();
			}else{
				list = raveDebacleLogService.query(paginationBean);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("query ravedebaclelog datas failure !", e);
		}
		map.put("total", null == counts ? 0 : counts.intValue());
		map.put("rows", list);
		//移除数据源,使用默认数据源
		DataSourceSwitch.clearDataSourceType();
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/queryByPrimarykey")
	public Map<String, Object> queryByPrimarykey(HttpServletRequest request){
		
		Map<String, Object> map = new HashMap<String, Object>();
		PaginationBean paginationBean = new PaginationBean(request);
		List<RaveDebacleLog> list = new ArrayList<RaveDebacleLog>();
		try {
			//切换数据源
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL2);
			list = raveDebacleLogService.query(paginationBean);
		} catch (Exception e) {
			log.error("query ravedebaclelog datas failure !", e);
		}
		if(null != list && !CollectionUtils.isEmpty(list)){
			map.put("content", list.get(0).getContent());
		}else{
			map.put("content", "");
		}
		//移除数据源,使用默认数据源
		DataSourceSwitch.clearDataSourceType();
		return map;
	}

}
