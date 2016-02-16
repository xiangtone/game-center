package com.mas.rave.controller;

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
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.Cp;
import com.mas.rave.service.AppInfoService;
import com.mas.rave.service.CpService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.MD5;
import com.mas.rave.util.RandNum;
import com.mas.rave.util.email.SendEmail;

/**
 * cp业务处理
 * 
 * @author liwei.sz
 * 
 */
@Controller
@RequestMapping("/cp")
public class CpController {
	Logger log = Logger.getLogger(CpController.class);
	@Autowired
	private CpService cpService;

	@Autowired
	private AppInfoService appService;

	/**
	 * 分布查看cp信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
			String name = request.getParameter("name");
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);
			CpService.CpCriteria criteria = new CpService.CpCriteria();
			if (StringUtils.isNotEmpty(name))
				criteria.nameLike(name.trim());
			PaginationVo<Cp> result = cpService.searchCps(criteria, currentPage, pageSize);
			request.setAttribute("cps", result);
		} catch (Exception e) {
			log.error("CpController list", e);
			PaginationVo<Cp> result = new PaginationVo<Cp>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "cp/list";
	}
	/**
	 * 审核查看cp信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/verify")
	public String verify(HttpServletRequest request) {
		try {
			String name = request.getParameter("name");
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);
			CpService.CpCriteria criteria = new CpService.CpCriteria();
			if (StringUtils.isNotEmpty(name))
				criteria.nameLike(name.trim());
			PaginationVo<Cp> result = cpService.searchCps(criteria, currentPage, pageSize);
			request.setAttribute("cps", result);
		} catch (Exception e) {
			log.error("CpController list", e);
			PaginationVo<Cp> result = new PaginationVo<Cp>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "cp/verify";
	}
	/**
	 * 操作cp state
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/doState/{id}", method = RequestMethod.POST)
	public String doState(HttpServletRequest request, @PathVariable("id") Integer id) {
		String state = request.getParameter("state");
		boolean _state = false;
		if ("1".equals(state)) {
			_state = true;
		}
		Cp cp = cpService.getCp(id);
		
		try {
			if (cp != null && cp.getCpState()==1 ) { //审核中
				cp.setState(_state);
				if(_state==true){
					cp.setCpState(2);//通过
				}else{
					cp.setCpState(3);//不通过
				}
				cpService.upCp(cp);	
				sendEmail(cp,_state);//发送通知邮件
				return "{\"flag\":\"0\"}";
			} else {
				return "{\"flag\":\"1\"}";
			}
		} catch (Exception e) {
			log.error("AppFileController doState", e);
			return "{\"flag\":\"1\"}";
		}
	}
	/**
	 * 通过/下线，发送邮件通知
	 * @param cp
	 * @param state
	 */
	private void sendEmail(Cp cp,boolean state){
		String content,title;
		if(state==true){
			title=Constant.MAIL_TITLE_CP_SUCC;
			content=Constant.MAIL_CONTENT_CP_SUCC;//审核通过
		}else{
			title=Constant.MAIL_TITLE_CP_FAIL;
			content=Constant.MAIL_CONTENT_CP_FAIL;//审核失败
		}
		String name=null,email=null;
		if(cp!=null){
			name = cp.getName();
			email = cp.getEmail();
		}else{
			System.out.println("@@@cp sendEmail: cp is null");
			return;
		}
		
		try{
			//send
			new SendEmail().defaultSendMethod(Constant.MAIL_SMTP,
											  Constant.MAIL_PORT,
											  Constant.MAIL_USERNAME,
											  Constant.MAIL_PASSOWRD,
											  Constant.MAIL_SENDER,
											  email,
											  title,
											  content.replace("#cp#", name));
		}catch(Exception e){
			System.out.println("@@@cp SendEmail:"+e.toString());
		}
	}
	/** 新增cp信息页 */
	@RequestMapping("/add")
	public String showAdd() {
		return "cp/add";
	}

	/** 新增cp信息 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(Cp cp) {
		try {
			CpService.CpCriteria criteria = new CpService.CpCriteria();
			if (StringUtils.isNotEmpty(cp.getName()))
				criteria.nameLike(cp.getName().trim());
			Cp result = cpService.getCpByName(criteria);
			if (result != null) {
				return "{\"flag\":\"2\"}";
			}

			// 设置私钥
			cp.setPriKey(RandNum.getRandNum());
			// 设置随机数
			cp.setN(RandNum.getRandNumStr());
			// 公钥
			cp.setPkey(RandNum.getRandNum1());
			cp.setPwd(RandNum.randomPwd(8));
			// 设置密码
			cp.setPassword(MD5.getMd5Value(cp.getPwd()));
			cpService.addCp(cp);
		} catch (Exception e) {
			log.error("CpController add", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 加载单个cp */
	@RequestMapping("/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		Cp cp = cpService.getCp(id);
		model.addAttribute("cp", cp);
		return "cp/edit";
	}

	/** 加载单个cp */
	@RequestMapping("/cpInfo/{id}")
	public String cpInfo(@PathVariable("id") Long id, Model model) {
		Cp cp = cpService.getCp(id);
		model.addAttribute("cp", cp);
		return "cp/cpInfo";
	}

	/** 更新cp */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(Cp cp) {
		try {
			Cp _Cp = cpService.getCp(cp.getId());
			if (_Cp != null && _Cp.getId() != (cp.getId()))
				return "{flag:1}";
			// 加密密码
			cp.setPassword(MD5.getMd5Value(cp.getPwd()));
			cpService.upCp(cp);
		} catch (Exception e) {
			log.error("CpController update", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 删除cp */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids, Model model) {
		Long[] idIntArray = MyCollectionUtils.splitToIntArray1(ids);
		String cp = checkCp(idIntArray);
		if (StringUtils.isNotEmpty(cp)) {
			return "{\"success\":\"" + cp + "\"}";
		}
		cpService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}

	// 检测cp
	public String checkCp(Long[] cps) {
		StringBuffer str = new StringBuffer();
		if (cps != null && cps.length > 0) {
			int m=0;
			for (long cp : cps) {
				if (appService.checkCp((int) cp) > 0) {
					if(++m>1){
						str.append(",");						
					}
					str.append(cp);
				}
			}
		}
		return str.toString();
	}

}
