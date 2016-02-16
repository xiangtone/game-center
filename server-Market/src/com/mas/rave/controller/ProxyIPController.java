package com.mas.rave.controller;

import java.net.ProxySelector;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mas.rave.appannie.ProxyService;
import com.mas.rave.main.vo.ProxyIP;
import com.mas.rave.service.ProxyIPService;

/**
 * 代理IP管理配置中心
 * 
 * @author dingjie
 * 
 */

@Controller
@RequestMapping("/proxyIP")
public class ProxyIPController {


	Logger log = Logger.getLogger(ProxyIPController.class);
	@Autowired
	private ProxyIPService proxyIPService;

	@RequestMapping("/list")
	public String list(Model model, HttpServletRequest request) {
//		initProxyIp(request);
		getProxyIpCount(request);
		return "proxyIP/ipList";
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(HttpServletRequest request,String ips) {
		//获取页面所有IP ,匹配数据库,再判断IP是否可用,
		//如果可用,则添加到数据库,如果不可用,则删除
		if(ips!=null){
			ips = ips.replace("：", ":");
		}
		try{
			String[] iparray = ips.split("\n");
			for(String ip:iparray){
				List<ProxyIP> ipList =proxyIPService.selectByIP(ip);			
				String[] ss = ip.split(":");
				if (ss.length == 2) {
					//boolean bool = ProxyService.checkIpPort(ss[0], ss[1]);
					if(ipList==null||ipList.size()==0){
						ProxyIP proxyIP = new ProxyIP();
						proxyIP.setIp(ip);
						proxyIPService.insert(proxyIP);
					}
				}
			}
			return "{\"flag\":\"0\"}";
		}catch(Exception e){
			log.error("ProxyIPController add", e);;
			return "{\"flag\":\"1\"}";
		}

	}

	public void initProxyIp(HttpServletRequest request) {
		List<ProxyIP> ipList =proxyIPService.getAllIPConfigs();
		StringBuilder sb = new StringBuilder();
		for(ProxyIP ips :ipList){
			sb.append(ips.getIp()).append("\r\n");
		}
		request.setAttribute("ips", sb.toString().trim());
	}
	public void getProxyIpCount(HttpServletRequest request) {
		int count =proxyIPService.getProxyIpCount();
		request.setAttribute("ipCount", count);
	}
}
