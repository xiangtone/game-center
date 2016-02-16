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
import com.reportforms.model.ClientUser;
import com.reportforms.service.ClientUserService;

/**
 * 平台安装用户
 * @author lisong.lan
 *
 */
@Controller
@RequestMapping("/clientuser")
public class ClientUserAction {
	
	private final Logger logger = Logger.getLogger(ClientUserAction.class);
	
	@Autowired
	private ClientUserService<ClientUser> clientUserService;
	
	@RequestMapping("/list")
	public String page(){
		return "clientuser/list";
	}

	@ResponseBody
	@RequestMapping("/query")
	public Map<String, Object> query(HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		PaginationBean paginationBean = new PaginationBean(request);
		List<ClientUser> list = null;
		Integer total = 0;
		try {
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL2);
			list = clientUserService.query(paginationBean);
			if(null != list && !list.isEmpty()){
				total = clientUserService.queryAllCounts(paginationBean);
			}
			result.put("total", total.intValue());
			result.put("rows", list);
			DataSourceSwitch.clearDataSourceType();
			logger.info("query clientuser datas success!");
		} catch (Exception e) {
			logger.error("query clientuser datas failed!",e);
		}
		return result;
	}
}
