package com.reportforms.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.datasource.DataSourceInstances;
import com.reportforms.datasource.DataSourceSwitch;
import com.reportforms.model.MasUser;
import com.reportforms.service.MasUserService;

/**
 * 平台注册用户
 * @author lisong.lan
 *
 */
@Controller
@RequestMapping("/masuser")
public class MasUserAction {
	
	private final Logger logger = Logger.getLogger(MasUserAction.class);
	
	@Autowired
	private MasUserService<MasUser> masUserService;
	
	@RequestMapping("/list")
	public String page(){
		return "masuser/list";
	}
	
	@ResponseBody
	@RequestMapping(value="/query",method=RequestMethod.POST)
	public Map<String, Object> query(HttpServletRequest request){
		
		PaginationBean paginationBean = new PaginationBean(request);
		Map<String, Object> result = new HashMap<String, Object>();
		List<MasUser> list = null;
		//SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
		try {
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL2);
			list = masUserService.query(paginationBean);
			Integer total = 0;
			if(null != list && ! list.isEmpty()){
				total = masUserService.queryAllCounts(paginationBean);
			}
			result.put("total", total.intValue());
			result.put("rows", list);
			DataSourceSwitch.clearDataSourceType();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("query masuser datas failed! ",e);
		}
		
		return result;
	}

}
