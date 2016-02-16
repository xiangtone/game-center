package com.mas.rave.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.mas.rave.main.vo.AppannieInfoCountryRank;
import com.mas.rave.main.vo.Country;
import com.mas.rave.service.AppAlbumColumnService;
import com.mas.rave.service.AppAlbumResService;
import com.mas.rave.service.AppAlbumResTempService;
import com.mas.rave.service.AppAlbumService;
import com.mas.rave.service.AppCountryScoreService;
import com.mas.rave.service.AppScoreService;
import com.mas.rave.service.AppannieInfoCountryRankService;
import com.mas.rave.service.CountryService;

@Controller
@RequestMapping("/appannieCountryRank")
public class AppannieCountryRankController {
	Logger log = Logger.getLogger(AppannieCountryRankController.class);
	
	public static final int handSource = 0;
	public static final int autoSource = 1;
	public static final int rankAll=10000;
	public static final int globalKey = 1;
	@Autowired
	private AppannieInfoCountryRankService appannieInfoCountryRankService;
	@Autowired
	private AppAlbumColumnService appAlbumColumnService;

	@Autowired
	private CountryService countryService;	

	@Autowired
	private AppAlbumService appAlbumService;

	@Autowired
	private AppAlbumResTempService appAlbumResTempService;
	
	@Autowired
	private AppAlbumResService	appAlbumResService;
	
	@Autowired
	private PropertiesController controller;

	@Autowired
	private AppScoreService appScoreService;
	
	@Autowired
	private AppCountryScoreService appCountryScoreService;
	/**
	 * 分布查看
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
			String appName = request.getParameter("appName");
				
			String raveId = request.getParameter("raveId");

			String albumId = request.getParameter("albumId")== null ? "1" : request.getParameter("albumId");
			
			String annieType = request.getParameter("annieType");
	
			
			AppannieInfoCountryRank appannieInfo = new AppannieInfoCountryRank();
			if (StringUtils.isNotEmpty(appName)) {
				appannieInfo.setAppName(appName.trim());
			}
			if (StringUtils.isNotEmpty(raveId)&& !raveId.equals("0")) {
				try{
					appannieInfo.setRaveId(Integer.parseInt(raveId));
				} catch (Exception e) {
					log.error("AppannieCountryRankController list raveId", e);

				}
			}else{
				List<Country> countrys = countryService.getCountrys();
				for(Country country:countrys){
					if(country.getId()!=1){
						appannieInfo.setRaveId(country.getId());	
						break;
					}
				}
				
			}
			if (StringUtils.isNotEmpty(albumId)&& !albumId.equals("0")) {
				try{
					appannieInfo.setAlbumId(Integer.parseInt(albumId));
				} catch (Exception e) {
					log.error("AppannieCountryRankController list albumId", e);

				}
			}
			if (StringUtils.isNotEmpty(annieType)) {
				try{
					appannieInfo.setAnnieType(Integer.parseInt(annieType));
				} catch (Exception e) {
					log.error("AppannieCountryRankController list raveId", e);

				}
			}
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);

			PaginationVo<AppannieInfoCountryRank> result = appannieInfoCountryRankService.selectByExample(appannieInfo, currentPage, pageSize);
			request.setAttribute("result", result);
		
		} catch (Exception e) {
			log.error("AppannieCountryRankController list", e);
			PaginationVo<AppannieInfoCountryRank> result = new PaginationVo<AppannieInfoCountryRank>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "appannieCountryRank/list";
	}
	
	/** 删除appannieInfo */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		appannieInfoCountryRankService.batchDel(idIntArray);
		return "{\"success\":\"true\"}";
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
	
	
	/** 加载单个app评论 */
	@RequestMapping("/{id}")
	public String edit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			AppannieInfoCountryRank appannieInfoCountryRank = appannieInfoCountryRankService.selectByPrimaryKey(id);
			request.setAttribute("appannieInfoCountryRank", appannieInfoCountryRank);
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);
			// 设置专题
			List<AppAlbum> appAlbums = appAlbumService.getAppAlbum();
			request.setAttribute("appAlbums", appAlbums);
		} catch (Exception e) {
			log.error("AppannieCountryRankController edit", e);
		}
		return "appannieCountryRank/edit";
	}
	
	/** 更新app评论 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(AppannieInfoCountryRank appannieInfoCountryRank,@RequestParam("initialReleaseDate1") String initialReleaseDate1) {
		try {	
			appannieInfoCountryRank.setInitialReleaseDate(new SimpleDateFormat("yyyy-MM-dd").parse(initialReleaseDate1));
			appannieInfoCountryRankService.update(appannieInfoCountryRank);
		} catch (Exception e) {
			log.error("AppannieCountryRankController update", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}
	/**
	 * 执行强制排名
	 * @param raveId
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/doConfig", method = RequestMethod.POST)
	@ResponseBody
	public String doConfig(@RequestParam(value = "id") String ids) {
		int i=0;
		try {
			Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
			for(Integer id:idIntArray){
				List<Integer> idList = new ArrayList<Integer>();
				try {
				AppannieInfoCountryRank appannieInfoCountryRank = appannieInfoCountryRankService.selectByPrimaryKey(id);
				idList.add(appannieInfoCountryRank.getAppInfo().getId());
				appCountryScoreService.insertSelectApps(appannieInfoCountryRank.getCountry().getId(),idList);
				} catch (Exception e) {
					i++;
					log.error("AppCountryScoreController doConfig", e);
				}
			}
		} catch (Exception e) {
			log.error("AppCountryScoreController doConfig", e);
			return "{\"flag\":\"1\"}";
		}
		return getError(0,i);
	}
	// 错误返回
	public String getError(int flag,int errorTotal) {
		return "{\"flag\":\"" + flag + "\",\"errorTotal\":\"" + errorTotal + "\"}";
	}
}
