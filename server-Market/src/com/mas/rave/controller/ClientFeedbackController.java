package com.mas.rave.controller;

import java.util.Date;
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
import com.mas.rave.main.vo.ClientFeedback;
import com.mas.rave.main.vo.ClientFeedbackZapp;
import com.mas.rave.main.vo.ClientFeedbackZappCode;
import com.mas.rave.service.ClientFeedbackService;
import com.mas.rave.service.ClientFeedbackZappCodeService;
import com.mas.rave.service.ClientFeedbackZappService;
import com.mas.rave.util.DateUtil;

/**
 * 客户反馈信息业务处理
 * @author jieding
 *
 */
@Controller
@RequestMapping("/clientfeedback")
public class ClientFeedbackController {
	Logger log = Logger.getLogger(ClientFeedbackController.class);
	@Autowired
	private ClientFeedbackService clientFeedbackService;
	
	
	@Autowired
	private ClientFeedbackZappService clientFeedbackZappService;
	
	@Autowired
	private ClientFeedbackZappCodeService clientFeedbackZappCodeService;
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try{
		String clientId = request.getParameter("clientId");
		String deviceModel = request.getParameter("deviceModel");
		//String str3 = request.getParameter("deviceVendor");

		ClientFeedback criteria = new ClientFeedback();
		if (StringUtils.isNotEmpty(clientId) && !clientId.equals("")) {
			criteria.setClientId(Integer.parseInt(clientId.trim()));
		}
		if (StringUtils.isNotEmpty(deviceModel) && !deviceModel.equals("")) {
			criteria.setDeviceModel(deviceModel.trim());
		}
//		if (StringUtils.isNotEmpty(str3) && !str3.equals("")) {
//			criteria.deviceVendorLikeTo(str3);
//		}
		int currentPage = RequestUtils.getInt(request, "currentPage", 1);
		int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);
		
		PaginationVo<ClientFeedback> result = clientFeedbackService.searchClientFeedbacks(criteria, currentPage, pageSize);
		request.setAttribute("result", result);
		}catch(Exception e){
			log.error("ClientFeedbackController list" +e);
			PaginationVo<ClientFeedback> result = new PaginationVo<ClientFeedback>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "clientfeedback/list";
	}

	/** 新增回复信息页 */
	@RequestMapping("/{clientId}/add")
	public String showAdd(HttpServletRequest request,@PathVariable("clientId") Integer clientId) {
		int currentPage = RequestUtils.getInt(request, "currentPage", 1);
		int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);
		PaginationVo<ClientFeedback> clientFeedbackList = clientFeedbackService.selectByClientId(clientId, currentPage, pageSize);
//			if(clientFeedbackList==null||clientFeedbackList.getRecordCount()==0){
//				clientFeedbackList = clientFeedbackService.selectByImei(imei, currentPage, pageSize);
//			}
		String imei = request.getParameter("imei");
		request.setAttribute("result", clientFeedbackList);
		request.setAttribute("clientId", clientId);
		request.setAttribute("imei", imei);	
		return "clientfeedback/add";
	}
	/** 新增回复信息页 */
	@ResponseBody
	@RequestMapping(value = "/{clientId}/addContent", method = RequestMethod.POST)
	public String add(HttpServletRequest request,ClientFeedback clientFeedback) {
		try{
		String imei = request.getParameter("imei");
		clientFeedback.setFeedbackType(2);
		clientFeedback.setCreateTime(new Date());
		clientFeedback.setImei(imei);
		clientFeedbackService.addClientFeedback(clientFeedback);
		return "{\"flag\":\"0\"}";
		}catch(Exception e){
			log.error("ClientFeedbackController add",e);
			return "{\"flag\":\"1\"}";
		}
	}
	
	/** 删除常用回馈 */
	@ResponseBody
	@RequestMapping("/deleteByClientId")
	public String deleteByClientId(@RequestParam("id") String clientIds, Model model) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(clientIds);
		clientFeedbackService.batchDeleteClientId(idIntArray);
		return "{\"success\":\"true\"}";
	}
	/** 删除常用回馈 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids, Model model) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		clientFeedbackService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}
	@RequestMapping("/zapp/list")
	public String zappList(HttpServletRequest request) {
		try{
		String str1 = request.getParameter("question");
		String str2 = request.getParameter("replyContent");

		ClientFeedbackZappService.ClientFeedbackZappCriteria criteria = new ClientFeedbackZappService.ClientFeedbackZappCriteria ();
		if (StringUtils.isNotEmpty(str1) && !str1.equals("")) {
			criteria.questionLikeTo(str1.trim());
		}
		if (StringUtils.isNotEmpty(str2) && !str2.equals("")) {
			criteria.replyContentLikeTo(str2.trim());
		}
		int currentPage = RequestUtils.getInt(request, "currentPage", 1);
		int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);
		
		PaginationVo<ClientFeedbackZapp> result = clientFeedbackZappService.selectByExample(criteria, currentPage, pageSize);
		request.setAttribute("result", result);
		List<ClientFeedbackZappCode> clientFeedbackZappCodeList =clientFeedbackZappCodeService.getAllClientFeedbackZappCode();
		if(clientFeedbackZappCodeList!=null&&clientFeedbackZappCodeList.size()!=0){
			request.setAttribute("clientFeedbackZappCode", clientFeedbackZappCodeList.get(0));
		}
		}catch(Exception e){
			log.error("ClientFeedbackController list");
			PaginationVo<ClientFeedbackZapp> result = new PaginationVo<ClientFeedbackZapp>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "clientfeedback/zappList";
	}
	/** 新增常用回馈信息页 */
	@RequestMapping("/zapp/updateZappCode")
	public String updateZappCode() {
		try{
			List<ClientFeedbackZappCode> clientFeedbackZappCodeList =clientFeedbackZappCodeService.getAllClientFeedbackZappCode();
			if(clientFeedbackZappCodeList!=null&&clientFeedbackZappCodeList.size()!=0){
				ClientFeedbackZappCode	clientFeedbackZappCode = clientFeedbackZappCodeList.get(0);
				clientFeedbackZappCode.setCreateTime(new Date());
				clientFeedbackZappCode.setZappcode(clientFeedbackZappCode.getZappcode()+1);
				clientFeedbackZappCodeService.updateByPrimaryKey(clientFeedbackZappCode);
			}else{
				ClientFeedbackZappCode	clientFeedbackZappCode =new ClientFeedbackZappCode();
				clientFeedbackZappCode.setCreateTime(new Date());
				clientFeedbackZappCode.setZappcode(1);
				clientFeedbackZappCodeService.insert(clientFeedbackZappCode);
			}
		  return "redirect:/clientfeedback/zapp/list/";
		}catch(Exception e){
			log.error("ClientFeedbackController updateZappCode" ,e);
		  return "redirect:/clientfeedback/zapp/list/";
		}
	}
	
	/** 新增常用回馈信息页 */
	@RequestMapping("/zapp/add")
	public String showAddZapp() {
		return "clientfeedback/addZapp";
	}
	/** 新增常用回馈 */
	@ResponseBody
	@RequestMapping(value = "/zapp/add", method = RequestMethod.POST)
	public String addZapp(HttpServletRequest request,ClientFeedbackZapp clientFeedbackZapp) {
		try{		
			clientFeedbackZapp.setCreateTime(DateUtil.StringToDate(clientFeedbackZapp.getCreateTime1()));
			clientFeedbackZappService.insert(clientFeedbackZapp);
		}catch(Exception e){
			log.error("ClientFeedbackController addZapp" ,e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";

	}
	/** 修改常用回馈信息页 */
	@RequestMapping("/zapp/{id}")
	public String showEditZapp(HttpServletRequest request, @PathVariable("id") Integer id) {
		ClientFeedbackZapp clientFeedbackZapp =clientFeedbackZappService.selectByPrimaryKey(id);
		request.setAttribute("clientFeedbackZapp", clientFeedbackZapp);
		return "clientfeedback/editZapp";
	}
	/** 修改常用回馈 */
	@ResponseBody
	@RequestMapping(value = "/zapp/{id}", method = RequestMethod.POST)
	public String updateZapp(HttpServletRequest request,ClientFeedbackZapp clientFeedbackZapp) {
		try{
			clientFeedbackZapp.setCreateTime(DateUtil.StringToDate(clientFeedbackZapp.getCreateTime1()));
			clientFeedbackZappService.updateByPrimaryKey(clientFeedbackZapp);
		}catch(Exception e){
			log.error("ClientFeedbackController updateZapp" ,e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";

	}
	/** 查看详细 */
	@RequestMapping("/zapp/info/{id}")
	public String zappInfo(@PathVariable("id") Integer id, Model model) {
		try {
			ClientFeedbackZapp clientFeedbackZapp =clientFeedbackZappService.selectByPrimaryKey(id);
			model.addAttribute("clientFeedbackZapp", clientFeedbackZapp);
		} catch (Exception e) {
			log.error("ClientFeedbackController zappInfo", e);
		}
		return "clientfeedback/zappInfoDetail";
	}
	/** 删除常用回馈 */
	@ResponseBody
	@RequestMapping("/zapp/delete")
	public String deleteZapp(@RequestParam("id") String ids, Model model) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		clientFeedbackZappService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}
}
