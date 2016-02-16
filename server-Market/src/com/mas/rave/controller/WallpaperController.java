package com.mas.rave.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mas.rave.common.page.PaginationBean;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.AppAlbumColumn;
import com.mas.rave.main.vo.Category;
import com.mas.rave.main.vo.Country;
import com.mas.rave.main.vo.ImageAlbumRes;
import com.mas.rave.main.vo.ImageInfo;
import com.mas.rave.service.AppAlbumColumnService;
import com.mas.rave.service.CountryService;
import com.mas.rave.service.ImageAlbumResService;
import com.mas.rave.service.ImageInfoService;

/**
 * 
 * @author lisong.lan
 * @since 2014/03/22
 *
 */

@Controller
@RequestMapping("/wallpaper")
public class WallpaperController {
	
	private final Logger log = Logger.getLogger(WallpaperController.class);
	
	@Autowired
	private ImageAlbumResService<ImageAlbumRes> imageAlbumResService;
	
	@Autowired
	private ImageInfoService imageInfoService;
	
	@Autowired
	private AppAlbumColumnService appAlbumColumnService;
	
	@Autowired
	private CountryService countryService;

	
	@RequestMapping("/listRes/{id}")
	public String listRes(HttpServletRequest request, @PathVariable("id") Integer id){
		
		PaginationBean params = new PaginationBean(request);
		String resType = request.getParameter("resType");
		int currentPage = RequestUtils.getInt(request, "currentPage", 1);
		int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);
		
		params.setPage(currentPage);
		params.setDefaultPageSize(pageSize);
		params.setStart((currentPage - 1) * pageSize);
		//区分主题分发还是其它分发
		if(StringUtils.isEmpty(resType)){
			params.getParams().put("columnId", id + "");
			// 设置实体
			AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(id);
			request.setAttribute("appAlbumColumn", appAlbumColumn);
			request.setAttribute("columnId", id);
		}else{
			params.getParams().put("themeId", id + "");
			request.setAttribute("themeId", id);
		}
		//String raveId = request.getParameter("raveId");
		String raveId= request.getParameter("raveIds");
		if(StringUtils.isEmpty(raveId)){
			raveId=request.getParameter("raveId");
		}
		if (StringUtils.isNotEmpty(raveId)) {
			int raveId1 = Integer.parseInt(raveId.trim());
			params.getParams().put("raveId",raveId);
			Country country = countryService.getCountry(raveId1);
			request.setAttribute("country", country);
		}
		request.setAttribute("raveId", raveId);

		params.setOrder("desc");
		params.setSort("sort");
		if(null != params.getParams().get("imageName")){
			params.getParams().put("imageName",params.getParams().get("imageName").trim());
		}
		List<ImageAlbumRes> list = new ArrayList<ImageAlbumRes>();
		Integer total = 0;
		try {
			total = imageAlbumResService.queryCounts(params);
			if(total.intValue() != 0){
				list = imageAlbumResService.query(params);
			}
			PaginationVo<ImageAlbumRes> result = new PaginationVo<ImageAlbumRes>(list, total, pageSize, currentPage);
			request.setAttribute("result", result);
		} catch (Exception e) {
			log.error(" query imagealbumres datas has failed !", e);
			PaginationVo<ImageAlbumRes> result = new PaginationVo<ImageAlbumRes>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		
		List<Country> countrys = countryService.getCountrys();
		request.setAttribute("countrys", countrys);
		
		if(StringUtils.isEmpty(resType)){
			return "wallpaper/listRes";
		}else{
			return "imageAlbumTheme/listRes";
		}
	}
	
	@ResponseBody
	@RequestMapping("/delete")
	public Map<String, String> deleteById(HttpServletRequest request, @RequestParam("id") String ids){
		
		Map<String, String> map = new HashMap<String, String>();
//		String ids = request.getParameter("id");
		if(StringUtils.isEmpty(ids)){
			map.put("success", "false");
			return map;
		}
		try {
			PaginationBean params = new PaginationBean();
			params.getParams().put("ids", ids);
			imageAlbumResService.deleteById(params);
			map.put("success", "true");
		} catch (Exception e) {
			// TODO: handle exception
			log.error("delete musicalbumres datas has failed !", e);
			map.put("success", "false");
		}
		return map;
	}
	
	@RequestMapping("/{id}/config")
	public String toConfig(HttpServletRequest request, @PathVariable("id") Integer id) {
		
		String imageNameId = request.getParameter("imageNameId");
		String categoryId = null == request.getParameter("categoryId") ? "0" : request.getParameter("categoryId");
		String category_parent = null == request.getParameter("category_parent") ? "0" : request.getParameter("category_parent");
		request.setAttribute("categoryId", categoryId);
		request.setAttribute("category_parent", category_parent);
		// 获取标签
		int currentPage = RequestUtils.getInt(request, "currentPage", 1);
		int pageSize = RequestUtils.getInt(request, "pageSize", 48);

		ImageInfo imageInfo = new ImageInfo();
		if (StringUtils.isNotEmpty(imageNameId)) {
			try{
				int musicId = Integer.parseInt(imageNameId.trim());
				imageInfo.setId(musicId);
			} catch (Exception e) {
				imageInfo.setName(imageNameId.trim());
			}
		}
		request.setAttribute("imageNameId", imageNameId);
		if (!"0".equals(categoryId)) {
			Category category = new Category();
			category.setId(Integer.parseInt(categoryId));
			imageInfo.setCategory(category);
		} else if (!"0".equals(category_parent)) {
			imageInfo.setCategory_parent(Integer.parseInt(category_parent));
		}
		imageInfo.setState(true);
		String resType = request.getParameter("resType");
		ImageAlbumRes imageAlbumRes = new ImageAlbumRes();
		if(StringUtils.isEmpty(resType)){
			String albumId = request.getParameter("appAlbumId");
			if(StringUtils.isNotEmpty(albumId)){
				imageAlbumRes.setAlbumId(Integer.parseInt(albumId));
				imageAlbumRes.setColumnId(id);
				imageInfo.setImageAlbumRes(imageAlbumRes);
			}
			request.setAttribute("columnId", id);
		}else{
			imageAlbumRes.setThemeId(id);
			imageInfo.setImageAlbumRes(imageAlbumRes);
			request.setAttribute("themeId", id);
		}
		String raveId = request.getParameter("raveId");
		if (StringUtils.isNotEmpty(raveId)) {
			int raveId1 = Integer.parseInt(raveId.trim());
			imageInfo.setRaveId(raveId1);
			Country country = countryService.getCountry(raveId1);
			request.setAttribute("country", country);
		}
		request.setAttribute("raveId", raveId);

		try {
			PaginationVo<ImageInfo> result = imageInfoService.searchImages(imageInfo, currentPage, pageSize);
			request.setAttribute("result", result);
			int size = result.getData().size();
			request.setAttribute("counts", size % 4 == 0 ? size / 4 : (size / 4) + 1);
		} catch (Exception e) {
			log.error("WallpaperController toConfig", e);
		}
		return "wallpaper/config";
	}
	
	@RequestMapping(value = "/{id}/doConfig", method = RequestMethod.POST)
	@ResponseBody
	public String doConfig(HttpServletRequest request,@RequestParam("raveId") Integer raveId,@RequestParam("columnId") Integer columnId,@RequestParam("albumId") Integer albumId, @RequestParam(value = "menus", required = false) List<Integer> ids) {
		Map<String, Object> map = new HashMap<String,Object>();
		String themeId = request.getParameter("themeId");
		String resType = request.getParameter("resType");
		try {
			if(StringUtils.isEmpty(resType)){
				map.put("columnId", columnId);
				map.put("albumId", albumId);
			}else{
				map.put("themeId", Integer.parseInt(themeId));
			}
			if (raveId!=0) {			
				map.put("raveId", raveId);
				Country country = countryService.getCountry(raveId);
				request.setAttribute("country", country);
			}
			map.put("list", ids);
			imageAlbumResService.insert(map);
		} catch (Exception e) {
			log.error("WallpaperController insert imagealbumres datas failed !", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

}
