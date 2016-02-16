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
import com.mas.rave.main.vo.Country;
import com.mas.rave.service.CountryService;

/**
 * country业务处理
 * 
 * @author jieding
 * 
 */
@Controller
@RequestMapping("/country")
public class CountryController {
	Logger log = Logger.getLogger(CountryController.class);
	@Autowired
	private CountryService countryService;	        
	/**
	 * 分布查看country信息
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
			CountryService.CountryCriteria criteria = new CountryService.CountryCriteria();
			if (StringUtils.isNotEmpty(name))
				criteria.nameLike(name.trim());
			PaginationVo<Country> result = countryService.selectByExample(criteria, currentPage, pageSize);
			request.setAttribute("countrys", result);
		} catch (Exception e) {
			log.error("CountryController list", e);
			PaginationVo<Country> result = new PaginationVo<Country>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "country/list";
	}
	
	@RequestMapping("/listInfo")
	public String listInfo(HttpServletRequest request) {
		try {
			String name = request.getParameter("name");
			String temp = request.getParameter("temp");
			request.setAttribute("temp", temp);
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);
			CountryService.CountryCriteria criteria = new CountryService.CountryCriteria();
			if (StringUtils.isNotEmpty(name))
				criteria.nameLike(name.trim());
			PaginationVo<Country> result = countryService.selectByExample(criteria, currentPage, pageSize);
			request.setAttribute("countrys", result);
		} catch (Exception e) {
			log.error("CountryController list", e);
			PaginationVo<Country> result = new PaginationVo<Country>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "country/listInfo";
	}

	/** 新增country信息页 */
	@RequestMapping("/add")
	public String showAdd() {
		return "country/add";
	}

	/** 新增country信息 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(Country country) {
		try {
			countryService.addCountry(country);
		} catch (Exception e) {
			log.error("CountryController add", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 加载单个country */
	@RequestMapping("/{id}")
	public String edit(@PathVariable("id") int id, Model model) {
		Country country = countryService.getCountry(id);
		model.addAttribute("country", country);
		return "country/edit";
	}

	/** 加载单个country */
	@RequestMapping("/countryInfo/{id}")
	public String countryInfo(@PathVariable("id") int id, Model model) {
		Country country = countryService.getCountry(id);
		model.addAttribute("country", country);
		return "country/countryInfo";
	}

	/** 更新country */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(Country country) {
		try {
			countryService.upCountry(country);		
		} catch (Exception e) {
			log.error("CountryController update", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}
	
	/** 更新country */
	@RequestMapping(value = "/updateState/{id}/{state}")
	public String updateState(@PathVariable("id") int id,@PathVariable("state") int state) {
		try {
			countryService.updateState(id, state);	
		} catch (Exception e) {
			log.error("CountryController updateState", e);
			return "redirect:/country/listInfo";
		}
		return "redirect:/country/listInfo";
	}

	/** 删除country */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids, Model model) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		countryService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}

}
