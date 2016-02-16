package com.reportforms.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.datasource.DataSourceInstances;
import com.reportforms.datasource.DataSourceSwitch;
import com.reportforms.model.AppDistribute;
import com.reportforms.model.Country;
import com.reportforms.service.AppDistributeService;
import com.reportforms.service.CountryService;

@Controller
@RequestMapping("/appdistribute")
public class AppDistributeAction {
	
	private static Logger log = LoggerFactory.getLogger(AppDistributeAction.class);
	
	@Autowired
	private AppDistributeService<AppDistribute> appDistributeService;
	
	@Autowired
	private CountryService<Country> countryService;
	
	@RequestMapping("/list")
	public String toList(HttpServletRequest request){
		PaginationBean paramBean = new PaginationBean();
		paramBean.setLimit(1000);
		//切换数据源
		DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL3);
		List<Country> countrys = countryService.query(paramBean);
		DataSourceSwitch.clearDataSourceType();
		request.setAttribute("countrys", countrys);
		return "/appdistribute/list";
	}
	
	@ResponseBody
	@RequestMapping("/query")
	public Map<String, Object> query(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<AppDistribute> list = new ArrayList<AppDistribute>();
		PaginationBean paramBean = new PaginationBean(request);
		String Q_appNameId = request.getParameter("Q_appNameId");
		if(StringUtils.isNotEmpty(Q_appNameId)){
			try {
				int appId = Integer.parseInt(Q_appNameId.trim());
				paramBean.getParams().put("appId", appId);
			} catch (Exception e) {
				paramBean.getParams().put("appName", Q_appNameId.trim());
			}
		}
		try {
			//切换数据源
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL3);
			Integer total = appDistributeService.queryAllCounts(paramBean);
			if(null != total && total.intValue() > 0){
				//paramBean.setLimit(20);
				list = appDistributeService.query(paramBean);
				resultMap.put("total", total.intValue());
				resultMap.put("rows", list);
			}else{
				resultMap.put("total", 0);
				resultMap.put("rows", list);
			}
			DataSourceSwitch.clearDataSourceType();
		} catch (Exception e) {
			// TODO: handle exception
			log.error("query the appdistribute datas failed! ", e);
			resultMap.put("total", 0);
			resultMap.put("rows", list);
		}
		
		return resultMap;
	}

}
