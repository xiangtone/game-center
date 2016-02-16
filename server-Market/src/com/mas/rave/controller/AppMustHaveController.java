package com.mas.rave.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.AppCollection;
import com.mas.rave.main.vo.Country;
import com.mas.rave.service.AppCollectionService;
import com.mas.rave.service.CountryService;
import com.mas.rave.util.DateUtil;
/**
 * 应用专辑管理
 * @author jieding
 *
 */
@Controller
@RequestMapping("/appMusthave")
public class AppMustHaveController {
	
	Logger log = Logger.getLogger(AppMustHaveController.class);
	@Autowired
	private AppCollectionService appCollectionService;
	
	@Autowired
	private CountryService countryService;

	/**判断主题名是否已经存在*/

	public boolean judgeNameExist(AppCollection criteria){
		List<AppCollection> listappMusthave  = 	appCollectionService.selectByName(criteria);
		if(criteria.getCollectionId()==0){
			if(listappMusthave.size()!=0){
				return false;//主题名称已经存在
			}
		}else{
			if(listappMusthave.size()>1||(listappMusthave.size()==1&&listappMusthave.get(0).getCollectionId()!=criteria.getCollectionId())){
				return false;//主题名称已经存在
			}
		}
		return true;
	}
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
		String str = request.getParameter("name");
		
		String raveId = request.getParameter("raveId");

		AppCollectionService.AppCollectionCriteria criteria = new AppCollectionService.AppCollectionCriteria();
		if (StringUtils.isNotEmpty(str)) {			
				criteria.nameLike(str.trim());
		}

		if (StringUtils.isNotEmpty(raveId)&& !raveId.equals("0")) {
			try{
				criteria.raveIdEqual(Integer.parseInt(raveId));
			} catch (Exception e) {
				log.error("AppMustHaveController list raveId", e);

			}
		}
		String type = request.getParameter("type");

		if (StringUtils.isNotEmpty(type)&& !type.equals("0")) {
			try{
				criteria.typeEqual(Integer.parseInt(type));
			} catch (Exception e) {
				log.error("AppMustHaveController list type", e);

			}
		}
		request.setAttribute("type", type);
		int currentPage = RequestUtils.getInt(request, "currentPage", 1);
		int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);
		
		PaginationVo<AppCollection> result = appCollectionService.searchAppCollections1(criteria, currentPage, pageSize);
		request.setAttribute("result", result);
		List<Country> countrys = countryService.getCountrys();
		request.setAttribute("countrys", countrys);
		} catch (Exception e) {
			log.error("AppMustHaveController list", e);
			PaginationVo<AppCollection> result = new PaginationVo<AppCollection>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "appMusthave/list";
	}
	/** 新增appMusthave主题信息 */
	@RequestMapping("/add")
	public String showAdd(HttpServletRequest request) {
		List<Country> countrys = countryService.getCountrys();
		request.setAttribute("countrys", countrys);
		String type = request.getParameter("type");
		request.setAttribute("type", type);
		return "appMusthave/add";
	}
	
	/** 新增appMusthave主题信息页 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute AppCollection record) {
		try {
			AppCollection criteria = new AppCollection();
			criteria.setRaveId(record.getRaveId());
			criteria.setName(record.getName());
			criteria.setType(record.getType());
			boolean bool = judgeNameExist(criteria);
			if(!bool){
				return "{\"flag\":\"3\"}";//该国家下的主题名称已经存在
			}
			record.setCreateTime(DateUtil.StringToDate(record.getCreateTime1()));
			appCollectionService.addAppCollection(record);
		} catch (Exception e) {
			log.error("AppMustHaveController add", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}
	/** 删除appMusthave主题信息 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		appCollectionService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}
	/** 加载单个appMusthave主题信息 */
	@RequestMapping("/{id}")
	public String edit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			AppCollection appCollection = appCollectionService.getAppCollection(id);
			model.addAttribute("appCollection", appCollection);
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);
		} catch (Exception e) {
			log.error("AppMustHaveController edit", e);
		}
		return "appMusthave/edit";
	}
	/** 更新app主题信息 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute AppCollection record) {
		try {
			AppCollection criteria = new AppCollection();
			criteria.setCollectionId(record.getCollectionId());
			criteria.setRaveId(record.getRaveId());
			criteria.setName(record.getName());
			criteria.setType(record.getType());
			boolean bool = judgeNameExist(criteria);
			if(!bool){
				return "{\"flag\":\"3\"}";//该国家下的主题名称已经存在
			}	
			record.setCreateTime(DateUtil.StringToDate(record.getCreateTime1()));
			appCollectionService.upAppCollection(record);	
		} catch (Exception e) {
			log.error("AppMustHaveController update", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}
}
