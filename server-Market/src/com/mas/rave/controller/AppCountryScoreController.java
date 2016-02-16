package com.mas.rave.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.AppAlbum;
import com.mas.rave.main.vo.AppAlbumColumn;
import com.mas.rave.main.vo.AppAlbumRes;
import com.mas.rave.main.vo.AppCountryScore;
import com.mas.rave.main.vo.AppScore;
import com.mas.rave.main.vo.Category;
import com.mas.rave.main.vo.Country;
import com.mas.rave.main.vo.CountryExample;
import com.mas.rave.service.AppAlbumColumnService;
import com.mas.rave.service.AppAlbumService;
import com.mas.rave.service.AppCountryScoreService;
import com.mas.rave.service.AppScoreService;
import com.mas.rave.service.CategoryService;
import com.mas.rave.service.CountryService;

@Controller
@RequestMapping("/appCountryScore")
public class AppCountryScoreController {
	Logger log = Logger.getLogger(AppCountryScoreController.class);
	@Autowired
	private AppCountryScoreService appCountryScoreService;
	
	@Autowired
	private CountryService countryService;	
	
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private AppScoreService appScoreService;	
	
	@Autowired
	private AppAlbumColumnService appAlbumColumnService;

	@Autowired
	private AppAlbumService appAlbumService;
	
	/**
	 * 分页查看app评论信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request,AppCountryScore appCountryScore) {
		try {
			//首次进入，加载默认的APP专题和APP页签
			if(null == appCountryScore.getAlbumId() || appCountryScore.getAlbumId() == 0){
				List<AppAlbum> albumList = appAlbumService.getAppAlbum();
				if(null != albumList && albumList.size()>0){
					appCountryScore.setAlbumId(albumList.get(0).getId());
					if(null == appCountryScore.getColumnId() || appCountryScore.getColumnId() == 0){
						List<AppAlbumColumn> list = appAlbumColumnService.getAppAlbumColumnsByAppAlbumId(albumList.get(0).getId());
						if(null != list && list.size()>0){
							appCountryScore.setColumnId(list.get(0).getColumnId());
						}
					}
				}
			}
			
			String appName = appCountryScore.getAppName();
				
			Integer raveId = appCountryScore.getRaveId();
			
			if (StringUtils.isNotEmpty(appName)) {
				appCountryScore.setAppName(appName.trim());
			}
			if (!raveId.equals("0")) {
				try{
					appCountryScore.setRaveId(raveId);
				} catch (Exception e) {
					log.error("AppannieInfoController list raveId", e);

				}
			}else{
				List<Country> countrys = countryService.getCountrys();
				for(Country country:countrys){
						appCountryScore.setRaveId(country.getId());	
						break;	
				}
				
			}
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);
			PaginationVo<AppCountryScore> result = appCountryScoreService.selectByExample(appCountryScore, currentPage, pageSize);
			request.setAttribute("result", result);
			
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);
			request.setAttribute("columnId", appCountryScore.getColumnId());
		} catch (Exception e) {
			log.error("AppCountryScoreController list", e);
			PaginationVo<AppCountryScore> result = new PaginationVo<AppCountryScore>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "appCountryScore/list";
	}
	
	/** 更新 */
	@RequestMapping(value = "/updateState/{id}/{state}")
	public String updateState(@PathVariable("id") int id,@PathVariable("state") int state,@RequestParam("raveId") int raveId) {
		try {
			AppCountryScore	appCountryScore = new AppCountryScore();
			appCountryScore.setId(id);
			if(state==0){
				appCountryScore.setState(false);
			}else if(state==1){
				appCountryScore.setState(true);
			}
			appCountryScoreService.updateByState(appCountryScore);	
		} catch (Exception e) {
			log.error("AppCountryScoreController updateState", e);
			return "redirect:/appCountryScore/list?raveId="+raveId;
		}
		return "redirect:/appCountryScore/list?raveId="+raveId;
	}
	/** 删除 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids, Model model) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		appCountryScoreService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}
	
	/** 加载单个app */
	@RequestMapping("/{id}")
	public String edit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			AppCountryScore appCountryScore = appCountryScoreService.selectByPrimaryKey(id);
			request.setAttribute("appCountryScore", appCountryScore);
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);
		} catch (Exception e) {
			log.error("AppCountryScoreController edit", e);
		}
		return "appCountryScore/edit";
	}
	/** 更新app */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(AppCountryScore appCountryScore,@RequestParam("startDate1") String startDate1) {
		try {	
			appCountryScore.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse(startDate1));
			appCountryScoreService.update(appCountryScore);
		} catch (Exception e) {
			log.error("AppCountryScoreController update", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}
	
	@ResponseBody
	@RequestMapping("/secondCategory")
	public String querySecondCategory(@RequestParam("id") Integer id, @RequestParam("categoryId") Integer categoryId) {
		try {
			List<Category> list = categoryService.getCategorys(id);
			if (CollectionUtils.isEmpty(list)) {
				return "{\"success\":\"0\"}";
			} else {
				StringBuilder options = new StringBuilder();
				options.append("<option value='0'>--all--</option>");
				for (Category category : list) {
					String selected = "";
					if (category.getId() == categoryId) {
						selected = "selected";
					}
					options.append("<option value='");
					options.append(category.getId() + "' " + selected + ">");
					options.append(category.getName());
					options.append("</option>");
				}
				return "{\"success\":\"1\",\"option\":\"" + options.toString() + "\"}";
			}
		} catch (Exception e) {
			return "{\"success\":\"2\"}";
		}
	}
	@RequestMapping("/config")
	public String toConfig(HttpServletRequest request) {
		try {
			String appnameid = request.getParameter("appnameid");
			String categoryId = request.getParameter("categoryId") == null ? "0" : request.getParameter("categoryId");		
			String category_parent = request.getParameter("category_parent") == null ? "0" : request.getParameter("category_parent");
			request.setAttribute("categoryId", categoryId);
			request.setAttribute("category_parent", category_parent);
			// 获取标签
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", 48);
			// 当前所有apk信息
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (null != appnameid && !"".equals(appnameid)) {
				try{
					Integer.parseInt(appnameid.trim());
					map.put("appid", appnameid.trim());
				} catch (Exception e) {
					map.put("appname", appnameid.trim());
				}
			}
			request.setAttribute("appnameid", appnameid);
			if (!"0".equals(categoryId)) {
				map.put("categoryId", Integer.parseInt(categoryId));
			} else if (!"0".equals(category_parent)) {
				map.put("category_parent", Integer.parseInt(category_parent));
			}
			String raveId = request.getParameter("raveId");
			String albumId = request.getParameter("albumId");
			String columnId = request.getParameter("columnId");
			
			if (StringUtils.isNotEmpty(raveId)) {
				int raveId1 = Integer.parseInt(raveId.trim());
				map.put("raveId", raveId1);
				Country country = countryService.getCountry(raveId1);
				request.setAttribute("country", country);
			}
			
			if(StringUtils.isNotEmpty(columnId)){
				map.put("columnId", columnId);
				AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(Integer.parseInt(columnId));
				request.setAttribute("appAlbumColumn", appAlbumColumn);
			}
			request.setAttribute("raveId", raveId);
			PaginationVo<AppCountryScore> result = appCountryScoreService.getSelectAppFiles(map, currentPage, pageSize);		
			request.setAttribute("result", result);
			int size = result.getData().size();
			request.setAttribute("counts", size % 4 == 0 ? size / 4 : (size / 4) + 1);
			
			request.setAttribute("albumId", albumId);
			request.setAttribute("columnId", columnId);
			return "appCountryScore/config";
			
		} catch (Exception e) {
			log.error("AppCountryScoreController toConfig", e);
			PaginationVo<AppAlbumRes> result = new PaginationVo<AppAlbumRes>(null, 0, 48, 1);
			request.setAttribute("result", result);
			return "appCountryScore/config";
		}
	
	}
	
	@RequestMapping(value = "/doConfig", method = RequestMethod.POST)
	@ResponseBody
	public String doConfig(@RequestParam("raveId") Integer raveId,@RequestParam(value = "menus", required = false) List<Integer> ids,@RequestParam("albumId") Integer albumId,@RequestParam("columnId") Integer columnId) {
		try {
//			appCountryScoreService.insertSelectApps(raveId,ids);  
			//使用这个方法是因为AppannieCountryRankController中doConfig用到了insertSelectApps
			appCountryScoreService.insertByApp(raveId,albumId,columnId,ids);
		} catch (Exception e) {
			log.error("AppCountryScoreController doConfig", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}
	@RequestMapping(value = "/copyRanking", method = RequestMethod.POST)
	@ResponseBody
	public String copyRanking(@RequestParam("raveId") Integer raveId) {
		try {
			Country defaultCountry = getDefaultCountry();
			if(defaultCountry.getId()==raveId){
				return "{\"flag\":\"2\"}";
			}
			AppCountryScore appCountryScore=new AppCountryScore();
			appCountryScore.setRaveId(defaultCountry.getId());
			List<AppCountryScore> appCountryScores = appCountryScoreService.selectByCondition(appCountryScore);
			AppCountryScore appCountryScore0=new AppCountryScore();
			appCountryScore0.setRaveId(raveId);
			appCountryScoreService.deleteByCondition(appCountryScore0);
			for(AppCountryScore appScore:appCountryScores){
				appScore.setRaveId(raveId);
				appScore.setId(null);
				appCountryScoreService.insert(appScore);
			}
		} catch (Exception e) {
			log.error("AppCountryScoreController doConfig", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}
	
	/**
	 * 获取默认的复制手动分发到Global的国家
	 * @return
	 */
	private Country getDefaultCountry() {
		String defaultCountry = "";
		List<AppScore> scores = appScoreService.getAllScore();
		for(AppScore appSocre:scores ){
			 if (appSocre.getScoreKey().equals("Global")) {
				 defaultCountry= appSocre.getScoreValue();
			 }
		}
		if(defaultCountry==null || defaultCountry.equals("")){
			defaultCountry = "Malaysia";
		}
		CountryExample example = new CountryExample();
		example.createCriteria().andNameEqual(defaultCountry);
		List<Country> countryList = countryService.getCountry(example);
		
		 if(countryList!=null&&countryList.size()!=0){
			 return countryList.get(0);
		 }
		return null;
	}
}
