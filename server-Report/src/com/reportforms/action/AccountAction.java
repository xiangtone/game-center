package com.reportforms.action;

import java.util.ArrayList;
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
//import com.reportforms.datasource.DataSourceInstances;
//import com.reportforms.datasource.DataSourceSwitch;
import com.reportforms.model.CpAccount;
import com.reportforms.model.HpAccount;
import com.reportforms.service.CpAccountService;
import com.reportforms.service.HpAccountService;

/**
 * 与HP对账查询
 * @author lisong.lan
 *
 */
@Controller
@RequestMapping("/account")
public class AccountAction {
	
	private static final Logger log = Logger.getLogger(AccountAction.class);
	
	@Autowired
	private HpAccountService<HpAccount> hpAccountService;
	
	@Autowired
	private CpAccountService<CpAccount> cpAccountService;
	
	@RequestMapping("/accountRechargeSum")
	public String rechargeSum(HttpServletRequest request){
		return "account/accountRechargeSum";
	}
	
	/**
	 * 查询账户充值总额
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/queryAccountRechargeSum",method=RequestMethod.POST)
	public Map<String, Object> queryAccountRechargeSumDataGrid(HttpServletRequest request){
		
		Map<String, Object> result = new HashMap<String, Object>();
		try{
			PaginationBean pageBean = new PaginationBean(request);
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
			log.info("load AccountRechargeSum data error : ", e);
		}
		return result;
	}
	
	@RequestMapping("/accountRechargeDetail")
	public String rechargeDetail(HttpServletRequest request){
		return "account/accountRechargeDetail";
	}
	
	/**
	 * 查询账户充值明细
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/queryAccountRechargeDetail",method=RequestMethod.POST)
	public Map<String, Object> queryRechargeDetailDataGrid(HttpServletRequest request){
		
		Map<String, Object> result = new HashMap<String, Object>();
		try{
			PaginationBean pageBean = new PaginationBean(request);
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
			log.info("load accountRechargeDetail data error : ", e);
		}
		return result;
	}
	
	@RequestMapping("/accountConsumeSum")
	public String consumeSum(HttpServletRequest request){
		return "account/accountConsumeSum";
	}
	
	/**
	 * 查询账户消耗总额
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/queryAccountConsumeSum",method=RequestMethod.POST)
	public Map<String, Object> queryAccountConsumeSumDataGrid(HttpServletRequest request){
		
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
			log.info("load accountConsumeSum data error : ", e);
		}
		return result;
	}
	
	@RequestMapping("/accountConsumeDetail")
	public String consumeDetail(HttpServletRequest request){
		return "account/accountConsumeDetail";
	}
	
	/**
	 * 查询账户消耗明细
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/queryAccountConsumeDetail",method=RequestMethod.POST)
	public Map<String, Object> queryConsumeDetailDataGrid(HttpServletRequest request){
		
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
			log.info("load consumeDetail data error : ", e);
		}
		return result;
	}
	
	@RequestMapping("/accountStartupDetail")
	public String startupDetail(HttpServletRequest request){
		
		//System.out.println("请求已到达 accountStartupDetail!");
		log.info("load account/accountStartupDetail  report data ...");
		return "account/accountStartupDetail";
	}

}
