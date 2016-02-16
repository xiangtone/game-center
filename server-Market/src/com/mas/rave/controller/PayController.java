package com.mas.rave.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.main.vo.Channel;
import com.mas.rave.main.vo.Cp;
import com.mas.rave.main.vo.Pay;
import com.mas.rave.service.AppInfoService;
import com.mas.rave.service.ChannelService;
import com.mas.rave.service.CpService;
import com.mas.rave.service.PayService;

@Controller
@RequestMapping("/pay")
public class PayController {
	Logger log = Logger.getLogger(PayController.class);
	@Autowired
	private PayService payService;

	@Autowired
	private CpService cpService;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private AppInfoService appInfoService;

	/**
	 * 分页查看pay信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);
			PayService.PayCriteria criteria = new PayService.PayCriteria();
			String mogValue = request.getParameter("mogValue");
			if (StringUtils.isNotEmpty(mogValue) && !mogValue.equals("0")) {
				criteria.mogValueEqualTo(Integer.parseInt(mogValue));
			}
			PaginationVo<Pay> result = payService.searchPays(criteria, currentPage, pageSize);
			request.setAttribute("pays", result);
		} catch (Exception e) {
			log.error("PayController list", e);
			PaginationVo<Pay> result = new PaginationVo<Pay>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "pay/list";
	}

	/** 新增pay信息页 */
	@RequestMapping("/add")
	public String showAdd(HttpServletRequest request) {
		// 获取所有渠道信息
		List<Channel> channels = channelService.getAllChannels(0);
		request.setAttribute("channels", channels);

		// 所有cp信息
		List<Cp> cps = cpService.getAllCps();
		request.setAttribute("cps", cps);

		// 所有app应用
		List<AppInfo> apps = appInfoService.getAllAppInfos();
		request.setAttribute("apps", apps);
		return "pay/add";

	}

	/** 新增pay信息 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(Pay pay) {
		try {
			payService.addPay(pay);
		} catch (Exception e) {
			log.error("PayController add", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 加载单个pay分类 */
	@RequestMapping("/{id}")
	public String edit(@PathVariable("id") Integer id, Model model, HttpServletRequest request) {
		try {
			Pay pay = payService.getPay(id);
			model.addAttribute("pay", pay);
			// 获取所有渠道信息
			List<Channel> channels = channelService.getAllChannels(0);
			request.setAttribute("channels", channels);

			// 所有cp信息
			List<Cp> cps = cpService.getAllCps();
			request.setAttribute("cps", cps);
			
			// 所有app应用
			List<AppInfo> apps = appInfoService.getAllAppInfos();
			request.setAttribute("apps", apps);
		} catch (Exception e) {
			log.error("PayController edit", e);
		}
		return "pay/edit";
	}

	/** 更新pay分类 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(Pay pay) {
		try {
			Pay _pay = payService.getPay(pay.getId());
			if (_pay != null && _pay.getId() != (pay.getId()))
				return "{flag:1}";
			payService.upPay(pay);
		} catch (Exception e) {
			log.error("PayController update", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 删除pay分类 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids, Model model) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		payService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}

}
