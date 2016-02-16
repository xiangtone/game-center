package com.reportforms.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.datasource.DataSourceInstances;
import com.reportforms.datasource.DataSourceSwitch;
import com.reportforms.model.Channel;
//import com.reportforms.datasource.DataSourceInstances;
//import com.reportforms.datasource.DataSourceSwitch;
import com.reportforms.model.CpAccount;
import com.reportforms.model.HpAccount;
import com.reportforms.model.User;
import com.reportforms.service.ChannelInfoService;
import com.reportforms.service.CpAccountService;
import com.reportforms.service.HpAccountService;

/**
 * 与CP对账
 * @author lisong.lan
 *
 */
@Controller
@RequestMapping("/pay")
public class PayAction {
	
	private static final Logger log = Logger.getLogger(PayAction.class);
	
	@Autowired
	private CpAccountService<CpAccount> cpAccountService;
	
	@Autowired
	private HpAccountService<HpAccount> hpAccountService;
	
	@Autowired
	private ChannelInfoService<Channel> channelInfoService;
	
	@RequestMapping("/rechargeCollect")
	public String rechargeCollect(HttpServletRequest request){
		return "pay/rechargeCollect";
	}
	
	/**
	 * 查询充值渠道汇总
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/queryRechargeCollect",method=RequestMethod.POST)
	public Map<String, Object> queryRechargeCollect(HttpServletRequest request){
		
		Map<String, Object> result = new HashMap<String, Object>();
		try{
			PaginationBean pageBean = new PaginationBean(request);
			/*User user = (User)request.getSession().getAttribute("loginUser");
			//如果用户为渠道商
			if(StringUtils.isNotEmpty(user.getQueryRequirement()) && user.getQueryRequirement().equals("channelId")){
				if(StringUtils.isNotEmpty((String)pageBean.getParams().get("channelId"))){
					//切换数据源
					DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL3);
					List<Channel> channels = channelInfoService.query(pageBean);
					DataSourceSwitch.clearDataSourceType();
					pageBean.getParams().put("channels", channels);
				}
			}*/
			//切换数据源
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL2);
			List<HpAccount> list = new ArrayList<HpAccount>();
			Integer total = 0;
			total = hpAccountService.queryByGroupByCounts(pageBean);
			if(total.intValue() > 0){
				list = hpAccountService.queryByGroupBy(pageBean);
			}
			result.put("total", total.intValue());
			result.put("rows", list);
			//移除数据源,使用默认数据源
			DataSourceSwitch.clearDataSourceType();
		}catch(Exception e){
			//e.printStackTrace();
			log.info("load rechargeCollect data error : ", e);
		}
		return result;
	}
	
	@RequestMapping("/rechargeAlleywayDetail")
	public String rechargeAlleywayDetail(HttpServletRequest request){
		return "pay/rechargeAlleywayDetail";
	}
	
	/**
	 * 查询充值渠道明细
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/queryChannelDetail",method=RequestMethod.POST)
	public Map<String, Object> queryChannelDetail(HttpServletRequest request){
		
		Map<String, Object> result = new HashMap<String, Object>();
		try{
			PaginationBean pageBean = new PaginationBean(request);
			/*User user = (User)request.getSession().getAttribute("loginUser");
			//如果用户为渠道商
			if(StringUtils.isNotEmpty(user.getQueryRequirement()) && user.getQueryRequirement().equals("channelId")){
				if(StringUtils.isNotEmpty((String)pageBean.getParams().get("channelId"))){
					//切换数据源
					DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL3);
					List<Channel> channels = channelInfoService.query(pageBean);
					DataSourceSwitch.clearDataSourceType();
					pageBean.getParams().put("channels", channels);
				}
			}*/
			//切换数据源
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL2);
			List<HpAccount> list = new ArrayList<HpAccount>();
			Integer total = 0;
			total = hpAccountService.queryAllCounts(pageBean);
			if(total.intValue() > 0){
				list = hpAccountService.query(pageBean);
			}
			result.put("total", total.intValue());
			result.put("rows", list);
			//移除数据源,使用默认数据源
			DataSourceSwitch.clearDataSourceType();
		}catch(Exception e){
			//e.printStackTrace();
			log.error("load rechargeCollect data error : ", e);
		}
		return result;
	}
	
	@RequestMapping("/productConsumeCollect")
	public String consumeCollect(HttpServletRequest request){
		return "pay/productConsumeCollect";
	}
	
	/**
	 * 查询厂商消耗汇总
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/queryConsumeDateGrid",method=RequestMethod.POST)
	public Map<String, Object> queryConsumeDateGrid(HttpServletRequest request){
		
		Map<String, Object> result = new HashMap<String, Object>();
		try{
			PaginationBean pageBean = new PaginationBean(request);
			//切换数据源
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL2);
			List<CpAccount> list = new ArrayList<CpAccount>();
			Integer total = 0;
			total = cpAccountService.queryByGroupByCounts(pageBean);
			if(total.intValue() > 0){
				list = cpAccountService.queryByGroupBy(pageBean);
			}
			result.put("total", total.intValue());
			result.put("rows", list);
			//移除数据源,使用默认数据源
			DataSourceSwitch.clearDataSourceType();
		}catch(Exception e){
			//e.printStackTrace();
			log.info("load rechargeCollect data error : ", e);
		}
		return result;
	}
	
	@RequestMapping("/productConsumeDetail")
	public String productConsumeCollect(HttpServletRequest request){
		return "pay/productConsumeDetail";
	}

	/**
	 * 厂商产品消耗明细
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/queryConsumeDetailDateGrid",method=RequestMethod.POST)
	public Map<String, Object> queryConsumeDetailDateGrid(HttpServletRequest request){
		
		Map<String, Object> result = new HashMap<String, Object>();
		try{
			PaginationBean pageBean = new PaginationBean(request);
			//切换数据源
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL2);
			List<CpAccount> list = new ArrayList<CpAccount>();
			Integer total = 0;
			total = cpAccountService.queryAllCounts(pageBean);
			if(total.intValue() > 0){
				list = cpAccountService.query(pageBean);
			}
			result.put("total", total.intValue());
			result.put("rows", list);
			//移除数据源,使用默认数据源
			DataSourceSwitch.clearDataSourceType();
		}catch(Exception e){
			//e.printStackTrace();
			log.info("load rechargeCollect data error : ", e);
		}
		return result;
	}
}
