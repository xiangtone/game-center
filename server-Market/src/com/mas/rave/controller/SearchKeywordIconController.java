package com.mas.rave.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.web.multipart.MultipartFile;

import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.Country;
import com.mas.rave.main.vo.SearchKeywordIcon;
import com.mas.rave.service.CountryService;
import com.mas.rave.service.SearchKeywordIconService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.RandNum;
import com.mas.rave.util.s3.S3Util;

/**
 * SearchKeywordIcon业务处理
 * 
 * @author jieding
 * 
 */
@Controller
@RequestMapping("/searchIcon")
public class SearchKeywordIconController {
	Logger log = Logger.getLogger(SearchKeywordIconController.class);
	@Autowired
	private SearchKeywordIconService searchKeywordIconService;	

	/**
	 * 分布查看SearchKeywordIcon信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
			String name = request.getParameter("name");
			String state = request.getParameter("state");
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);
			SearchKeywordIconService.SearchKeywordIconCriteria criteria = new SearchKeywordIconService.SearchKeywordIconCriteria();
			if (StringUtils.isNotEmpty(name)) {
				criteria.andNameLike(name.trim());
			}
			if (StringUtils.isEmpty(state)) {
				criteria.stateEqual(true);
			} else {
				criteria.stateEqual(Boolean.parseBoolean(state));
			}
			PaginationVo<SearchKeywordIcon> result = searchKeywordIconService.selectByExample(criteria, currentPage, pageSize);
			request.setAttribute("result", result);
		} catch (Exception e) {
			log.error("SearchKeywordIconController list", e);
			PaginationVo<SearchKeywordIcon> result = new PaginationVo<SearchKeywordIcon>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "searchIcon/list";
	}
	/** 新增SearchKeywordIcon信息页 */
	@RequestMapping("/add")
	public String showAdd(Model model) {
		return "searchIcon/add";
	}

	/** 新增SearchKeywordIcon信息 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(SearchKeywordIcon searchIcon,@RequestParam(required=false) MultipartFile urlFile) {
		try {
			List<String> fileUrlList = new ArrayList<String>();//s3 list
			// 上传url
			if (urlFile.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(urlFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					File apkLocalFile = new File(Constant.LOCAL_FILE_PATH + Constant.LOCAL_SEARCH_PATH+Constant.LOCAL_URL_PATH, 
							RandNum.randomFileName(urlFile.getOriginalFilename()));
					FileUtil.copyInputStream(urlFile.getInputStream(), apkLocalFile);// apk本地上传					
					searchIcon.setUrl(FileAddresUtil.getFilePath(apkLocalFile));
					fileUrlList.add(searchIcon.getUrl());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			searchKeywordIconService.addSearchKeywordIcon(searchIcon);
			S3Util.uploadS3(fileUrlList, false);
		} catch (Exception e) {
			try {
				FileUtil.deleteFile(searchIcon.getUrl());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				log.error("SearchKeywordIconController add deleteFile", e1);
			}
			log.error("SearchKeywordIconController add", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 加载单个SearchKeywordIcon */
	@RequestMapping("/{id}")
	public String edit(@PathVariable("id") int id, Model model) {
		SearchKeywordIcon searchKeywordIcon = searchKeywordIconService.getSearchKeywordIcon(id);
		model.addAttribute("searchIcon", searchKeywordIcon);
		return "searchIcon/edit";
	}

	/** 更新SearchKeywordIcon */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(SearchKeywordIcon searchIcon, @RequestParam(required=false) MultipartFile urlFile) {
		try {
			List<String> fileUrlList = new ArrayList<String>();//s3 list
			// 上传url
			if (urlFile.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(urlFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					File apkLocalFile = new File(Constant.LOCAL_FILE_PATH + Constant.LOCAL_SEARCH_PATH+Constant.LOCAL_URL_PATH, 
							RandNum.randomFileName(urlFile.getOriginalFilename()));
					FileUtil.copyInputStream(urlFile.getInputStream(), apkLocalFile);// apk本地上传
					FileUtil.deleteFile(searchIcon.getUrl());
					searchIcon.setUrl(FileAddresUtil.getFilePath(apkLocalFile));
					fileUrlList.add(searchIcon.getUrl());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			searchKeywordIconService.upSearchKeywordIcon(searchIcon);
			S3Util.uploadS3(fileUrlList, false);
		} catch (Exception e) {
			try {
				FileUtil.deleteFile(searchIcon.getUrl());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				log.error("SearchKeywordIconController update deleteFile", e1);
			}
			log.error("SearchKeywordIconController update", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}


	/** 删除SearchKeywordIcon */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids, Model model) {
		try{
			Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
			searchKeywordIconService.batchDelete(idIntArray);
			return "{\"success\":\"true\"}";
		}catch(Exception e){
			log.error("SearchKeywordIconController delete", e);
			return "{\"success\":\"false\"}";
		}
	
	}

}
