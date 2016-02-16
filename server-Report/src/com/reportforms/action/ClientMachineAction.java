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
import com.reportforms.model.ClientMachine;
import com.reportforms.model.HpAccount;
import com.reportforms.service.ClientMachineService;

/**
 * 平台预装设备激活统计
 * 
 * @author lisong.lan
 * @since 2014/07/02
 * 
 */
@Controller
@RequestMapping("/machine")
public class ClientMachineAction {
	
	private static final Logger log = Logger.getLogger(ClientMachineAction.class);
	
	@Autowired
	private ClientMachineService<ClientMachine> clientMachineService;

	/**
	 * 进入列表展示页面
	 * @return
	 */
	@RequestMapping("/list")
	public String page(){
		return "machine/list";
	}
	
	/**
	 * 数据查询
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query")
	public Map<String, Object> list(HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		try{
			PaginationBean pageBean = new PaginationBean(request);
			//切换数据源
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL2);
			List<ClientMachine> list = clientMachineService.query(pageBean);
			Integer total = 0;
			if(null != list && !list.isEmpty()){
				total = clientMachineService.queryAllCounts(pageBean);
			}
			result.put("total", total.intValue());
			result.put("rows", list);
			//移除数据源,使用默认数据源
			DataSourceSwitch.clearDataSourceType();
		}catch(Exception e){
			//e.printStackTrace();
			log.info("load clientmachine data error : ", e);
		}
		return result;
	}
}
