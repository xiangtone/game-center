package com.reportforms.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.datasource.DataSourceInstances;
import com.reportforms.datasource.DataSourceSwitch;
import com.reportforms.model.AppSearchLog;
import com.reportforms.service.AppSearchLogService;

@Controller
@RequestMapping("/appsearchlog")
public class AppSearchLogAction {
	
	private final Logger log = Logger.getLogger(AppSearchLogAction.class);
	
	@Autowired
	private AppSearchLogService<AppSearchLog> appSearchLogService;
	
	@RequestMapping("/list")
	public String list(){
		return "appsearchlog/list";
	}
	
	@RequestMapping("/group")
	public String group(){
		return "appsearchlog/group";
	}
	
	@RequestMapping("/showtop")
	public String groupTopPage(@Param("type") Integer type){
		if(type.intValue() == 1){
			return "appsearchlog/grouptop";
		}else{
			return "appsearchlog/top";
		}
	}
	
	@ResponseBody
	@RequestMapping("/grouptop")
	public Map<String, Object> queryGroupTop(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String,Object>();
		PaginationBean paginationBean = new PaginationBean(request);
		List<AppSearchLog> list = new ArrayList<AppSearchLog>();
		Integer total = 0;
		try {
			//切换数据源
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL2);
			list = appSearchLogService.queryByGroupByContent(paginationBean);
			if(null != list && !list.isEmpty()){
				total = appSearchLogService.queryByGroupByContentCounts(paginationBean);
			}
			map.put("total", total.intValue());
			map.put("rows", list);
			//移除数据源,使用默认数据源
			DataSourceSwitch.clearDataSourceType();
		} catch (Exception e) {
			log.error("query appsearchlog data has failed !",e);
		}
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/top")
	public Map<String, Object> queryTop(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String,Object>();
		PaginationBean paginationBean = new PaginationBean(request);
		List<AppSearchLog> list = new ArrayList<AppSearchLog>();
		Integer total = 0;
		try {
			//切换数据源
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL2);
			list = appSearchLogService.queryByGroupBy(paginationBean);
			if(null != list && !list.isEmpty()){
				total = appSearchLogService.queryByGroupByCounts(paginationBean);
			}
			map.put("total", total.intValue());
			map.put("rows", list);
			//移除数据源,使用默认数据源
			DataSourceSwitch.clearDataSourceType();
		} catch (Exception e) {
			log.error("query appsearchlog data has failed !",e);
		}
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/queryByGroup")
	public Map<String, Object> queryByGroup(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String,Object>();
		PaginationBean paginationBean = new PaginationBean(request);
		List<AppSearchLog> list = new ArrayList<AppSearchLog>();
		Integer total = 0;
		try {
			//切换数据源
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL2);
			list = appSearchLogService.queryByGroupBy(paginationBean);
			if(null != list && !list.isEmpty()){
				total = appSearchLogService.queryByGroupByCounts(paginationBean);
			}
			map.put("total", total.intValue());
			map.put("rows", list);
			//移除数据源,使用默认数据源
			DataSourceSwitch.clearDataSourceType();
		} catch (Exception e) {
			log.error("query appsearchlog data has failed !",e);
		}
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/query")
	public Map<String, Object> query(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		PaginationBean paginationBean = new PaginationBean(request);
		List<AppSearchLog> list = new ArrayList<AppSearchLog>();
		Integer total = 0;
		try {
			//切换数据源
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL2);
			list = appSearchLogService.query(paginationBean);
			if(null != list && !list.isEmpty()){
				total = appSearchLogService.queryAllCounts(paginationBean);
			}
			map.put("total", total.intValue());
			map.put("rows", list);
			//移除数据源,使用默认数据源
			DataSourceSwitch.clearDataSourceType();
		} catch (Exception e) {
			log.error("query appsearchlog data has failed !",e);
		}
		return map;
	}

}
