package com.reportforms.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.datasource.DataSourceInstances;
import com.reportforms.datasource.DataSourceSwitch;
import com.reportforms.model.ClientActivateLog;
import com.reportforms.service.ClientActivateLogService;

@Controller
@RequestMapping("/activateLog")
public class ClientActivateLogAction {
	
	private final Logger logger = Logger.getLogger(ClientActivateLogAction.class);
	
	@Autowired
	private ClientActivateLogService<ClientActivateLog> clientActivateLogService;
	
	@RequestMapping("/list")
	public String page(){
		return "activatelog/list";
	}
	
	@ResponseBody
	@RequestMapping("/query")
	public Map<String, Object> list(HttpServletRequest request){
		
		Map<String, Object> result = new HashMap<String, Object>();
		PaginationBean paramBean = new PaginationBean(request);
		List<ClientActivateLog> list = null;
		Integer total = 0;
		try {
			//切换数据源
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL2);
			list = clientActivateLogService.query(paramBean);
			if(null != list && !list.isEmpty()){
				//切换数据源
				DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL2);
				total = clientActivateLogService.queryAllCounts(paramBean);
				//移除数据源,使用默认数据源
				DataSourceSwitch.clearDataSourceType();
			}
			result.put("total", total.intValue());
			result.put("rows", list);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("query clientactivatelog datas has failed! ",e);
		}
		
		return result;
	}

}
