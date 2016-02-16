package com.mas.rave.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.AppAlbumColumn;
import com.mas.rave.main.vo.AppAlbumRes;
import com.mas.rave.main.vo.AppAlbumStatistics;
import com.mas.rave.main.vo.Country;
import com.mas.rave.service.AppAlbumColumnService;
import com.mas.rave.service.AppAlbumResService;
import com.mas.rave.service.AppAlbumService;
import com.mas.rave.service.AppAlbumStatisticsService;
import com.mas.rave.service.CountryService;

@Controller
@RequestMapping("/appAlbumStatistics")
public class AppAlbumStatisticsController {
	Logger log = Logger.getLogger(AppAlbumStatisticsController.class);

	@Autowired
	private AppAlbumColumnService appAlbumColumnService;

	@Autowired
	private CountryService countryService;	

	@Autowired
	private AppAlbumService appAlbumService;
	
	
	@Autowired
	private AppAlbumResService	appAlbumResService;
	
	@Autowired
	private AppAlbumStatisticsService appAlbumStatisticsService;
	
	/**
	 * 分布查看生效列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/show")
	public String show(HttpServletRequest request) {
		try{
			String appName = request.getParameter("appName");
			
			String raveId = request.getParameter("raveId");
	
			String albumId = request.getParameter("albumId")== null ? "1" : request.getParameter("albumId");
			
			
			String columnId = request.getParameter("columnId");
			request.setAttribute("columnId", columnId);
			AppAlbumResService.AppAlbumResCriteria criteria = new AppAlbumResService.AppAlbumResCriteria();
			if (StringUtils.isNotEmpty(appName)) {
				criteria.nameLike(appName);
			}
			if (StringUtils.isNotEmpty(raveId)&& !raveId.equals("0")) {
				try{
					criteria.raveIdEqualTo(Integer.parseInt(raveId));
				} catch (Exception e) {
					log.error("AppannieInfoController list raveId", e);
	
				}
			}else{
				List<Country> countrys = countryService.getCountrys();
				for(Country country:countrys){
						criteria.raveIdEqualTo(country.getId());	
						break;
				}
				
			}			
			if (StringUtils.isNotEmpty(columnId)&& !columnId.equals("0")) {
				try{
					criteria.columnIdEqualTo(Integer.parseInt(columnId));
				} catch (Exception e) {
					log.error("AppannieInfoController list albumId", e);
	
				}
			}else{
				List<AppAlbumColumn> appAlbumColumnList = appAlbumColumnService.getAppAlbumColumnsByAppAlbumId(Integer.parseInt(albumId));
				int columnIdDefault = 0;
				for(AppAlbumColumn appAlbumColumn:appAlbumColumnList){
					if(appAlbumColumn.getFlag()==1){
						columnIdDefault = appAlbumColumn.getColumnId();
						break;
					}
				}
				criteria.columnIdEqualTo(columnIdDefault);
			}
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", 200);
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);	
			PaginationVo<AppAlbumRes> result = appAlbumResService.searchAlbumRess(criteria, currentPage, pageSize);
			request.setAttribute("result", result);
		} catch (Exception e) {
			log.error("AppAlbumStatisticsController show", e);
			PaginationVo<AppAlbumRes> result = new PaginationVo<AppAlbumRes>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "appAlbumStatistics/show";
	}
	@ResponseBody
	@RequestMapping("/albumColumn")
	public String querySecondCategory(@RequestParam("id") Integer id, @RequestParam("columnId") Integer columnId) {
		try {
			List<AppAlbumColumn> list = appAlbumColumnService.getAppAlbumColumnsByAppAlbumId(id);
			if (CollectionUtils.isEmpty(list)) {
				return "{\"success\":\"0\"}";
			} else {
				StringBuilder options = new StringBuilder();
				for (AppAlbumColumn appAlbumColumn : list) {
					if(appAlbumColumn.getFlag()==1){
						String selected = "";
						if (columnId!=null&&appAlbumColumn.getColumnId() == columnId) {
							selected = "selected";
						}
						options.append("<option value='");
						options.append(appAlbumColumn.getColumnId() + "' " + selected + ">");
						options.append(appAlbumColumn.getName());
						options.append("("+appAlbumColumn.getNameCn()+")");
						options.append("</option>");						
					}
				}
				return "{\"success\":\"1\",\"option\":\"" + options.toString() + "\"}";
			}
		} catch (Exception e) {
			return "{\"success\":\"2\"}";
		}
	}
	
	/**
	 * 分页查看
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
			String appName = request.getParameter("appName");
				
			String raveId = request.getParameter("raveId");

			String albumId = request.getParameter("albumId")== null ? "2" : request.getParameter("albumId");
			
			String columnId = request.getParameter("columnId");
			request.setAttribute("columnId", columnId);
			
			AppAlbumStatistics appAlbumStatistics = new AppAlbumStatistics();
			if (StringUtils.isNotEmpty(appName)) {
				appAlbumStatistics.setAppName(appName.trim());
			}
			if (StringUtils.isNotEmpty(raveId)&& !raveId.equals("0")) {
				try{
					appAlbumStatistics.setRaveId(Integer.parseInt(raveId));
				} catch (Exception e) {
					log.error("AppannieCountryRankController list raveId", e);

				}
			}else{
				List<Country> countrys = countryService.getCountrys();
				for(Country country:countrys){
						appAlbumStatistics.setRaveId(country.getId());	
						break;
				}
				
			}
			if (StringUtils.isNotEmpty(albumId)&& !albumId.equals("0")) {
				try{
					appAlbumStatistics.setAlbumId(Integer.parseInt(albumId));
				} catch (Exception e) {
					log.error("AppannieCountryRankController list raveId", e);

				}
			}
			if (StringUtils.isNotEmpty(columnId)&& !columnId.equals("0")) {
				try{
					appAlbumStatistics.setColumnId(Integer.parseInt(columnId));
				} catch (Exception e) {
					log.error("AppannieCountryRankController list raveId", e);

				}
			}else{
				List<AppAlbumColumn> appAlbumColumnList = appAlbumColumnService.getAppAlbumColumnsByAppAlbumId(Integer.parseInt(albumId));
				int columnIdDefault = 0;
				for(AppAlbumColumn appAlbumColumn:appAlbumColumnList){
					if(appAlbumColumn.getFlag()==1){
						columnIdDefault = appAlbumColumn.getColumnId();
						break;
					}
				}
				appAlbumStatistics.setColumnId(columnIdDefault);
			}
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", 200);
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);

			PaginationVo<AppAlbumStatistics> result = appAlbumStatisticsService.selectByExample(appAlbumStatistics, currentPage, pageSize);
			request.setAttribute("result", result);
		
		} catch (Exception e) {
			log.error("AppAlbumStatisticsController list", e);
			PaginationVo<AppAlbumStatistics> result = new PaginationVo<AppAlbumStatistics>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "appAlbumStatistics/list";
	}
	
}
