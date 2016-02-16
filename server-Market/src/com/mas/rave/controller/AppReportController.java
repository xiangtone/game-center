package com.mas.rave.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.AppPic;
import com.mas.rave.main.vo.Category;
import com.mas.rave.main.vo.Country;
import com.mas.rave.main.vo.TAppDistribute;
import com.mas.rave.report.ExportExcelView;
import com.mas.rave.report.XMLModelTemplate;
import com.mas.rave.service.CategoryService;
import com.mas.rave.service.CountryService;
import com.mas.rave.service.TAppDistributeService;

@Controller
@RequestMapping("/appReport")
public class AppReportController {
	Logger log = Logger.getLogger(AppReportController.class);

	@Autowired
	private TAppDistributeService tAppDistributeService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CountryService countryService;
	
	@RequestMapping("/distribute/list")
	public String list(HttpServletRequest request, HttpServletResponse response)
	{
		try {
		com.mas.rave.main.vo.Criteria criteria = new com.mas.rave.main.vo.Criteria();
		String appnameid = request.getParameter("appnameid");
		String packageName = request.getParameter("packageName");
		String raveId = request.getParameter("raveId");

		String categoryId = request.getParameter("categoryId") == null ? "0" : request.getParameter("categoryId");
		String category_parent = request.getParameter("category_parent") == null ? "0" : request.getParameter("category_parent");
		request.setAttribute("categoryId", categoryId);
		request.setAttribute("category_parent", category_parent);
		if (null != appnameid && !"".equals(appnameid)) {
			try{
				int appId = Integer.parseInt(appnameid);
				criteria.put("appId", appnameid);
			} catch (Exception e) {
				criteria.put("appName", appnameid.trim());
			}
		}
		request.setAttribute("appnameid", appnameid);
		if (null != packageName && !"".equals(packageName)) {
			criteria.put("packageName", packageName.trim());
		}
		request.setAttribute("packageName", packageName);
		if (!"0".equals(categoryId)) {
			criteria.put("categoryId", Integer.parseInt(categoryId));
		} else if (!"0".equals(category_parent)) {
			criteria.put("category_parent", Integer.parseInt(category_parent));
		}
		if (StringUtils.isNotEmpty(raveId)&&!"".equals(raveId)&&!"0".equals(raveId)) {
			int raveId1 = Integer.parseInt(raveId.trim());
			criteria.put("raveId", raveId1);
//			Country country = countryService.getCountry(raveId1);
//			request.setAttribute("countryNow", country);
		}else{
			criteria.put("raveId", 1);
		}
		List<Country> countrys = countryService.getCountrys();
		request.setAttribute("countrys", countrys);
		
		int currentPage = RequestUtils.getInt(request, "currentPage", 1);
		int pageSize = RequestUtils.getInt(request, "pageSize",20);
		
		criteria.setMysqlOffset((currentPage - 1) * pageSize);
		criteria.setMysqlLength(pageSize);
		Integer count = tAppDistributeService.countByExample(criteria);
		List<TAppDistribute> list = tAppDistributeService.selectByExample(criteria);
		request.setAttribute("result", new PaginationVo<TAppDistribute>(list, count, pageSize, currentPage));
		// 所有一级分类信息
		List<Category> categorys = categoryService.getCategorys(1);
		request.setAttribute("categorys", categorys);
		} catch (Exception e) {
			log.error("AppReportController list", e);
			PaginationVo<TAppDistribute> result = new PaginationVo<TAppDistribute>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "/appReport/list";
	}
	
	@RequestMapping("/export2Excel")
	public ModelAndView exportToExcel(HttpServletRequest request,HttpServletResponse response){
		XMLModelTemplate template = ExportExcelView.getTemplate(request, response);
		Map<String, Object> map = new HashMap<String, Object>();
		
		com.mas.rave.main.vo.Criteria criteria = new com.mas.rave.main.vo.Criteria();
		String appnameid = request.getParameter("appnameid");
		try {
			appnameid = new String(appnameid.getBytes("iso8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			
		}
		String packageName = request.getParameter("packageName");
		String raveId = request.getParameter("raveId");
		String categoryId = request.getParameter("categoryId") == null ? "0" : request.getParameter("categoryId");
		String category_parent = request.getParameter("category_parent") == null ? "0" : request.getParameter("category_parent");
		if (null != appnameid && !"".equals(appnameid)) {
			try{
				int appId = Integer.parseInt(appnameid);
				criteria.put("appId", appnameid);
//				criteria.put("appName", appnameid);
			} catch (Exception e) {
				criteria.put("appName", appnameid.trim());
			}
		}
		if (null != packageName && !"".equals(packageName)) {
			criteria.put("packageName", packageName.trim());
		}
		if (!"0".equals(categoryId)) {
			criteria.put("categoryId", Integer.parseInt(categoryId));
		} else if (!"0".equals(category_parent)) {
			criteria.put("category_parent", Integer.parseInt(category_parent));
		}
		if (StringUtils.isNotEmpty(raveId)&&!"".equals(raveId)&&!"0".equals(raveId)) {
			int raveId1 = Integer.parseInt(raveId.trim());
			criteria.put("raveId", raveId1);
		}
		List<TAppDistribute> dataList = tAppDistributeService.selectByExample(criteria);
		map.put("list", dataList);
		map.put("template", template);
		//生成excel文件并返回给页面
		return new ModelAndView(new ExportExcelView(),map);
	}
}
