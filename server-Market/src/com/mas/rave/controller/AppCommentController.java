package com.mas.rave.controller;

import java.util.HashMap;

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
import com.mas.rave.main.vo.AppComment;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.service.AppCommentService;
import com.mas.rave.service.AppInfoService;

/**
 * app
 * 
 * @author liwei.sz
 * 
 */
@Controller
@RequestMapping("/appComment")
public class AppCommentController {
	Logger log = Logger.getLogger(AppCommentController.class);
	@Autowired
	private AppCommentService appCommentService;

	@Autowired
	private AppInfoService appInfoService;

	/**
	 * 分页查看app评论信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
			String str = request.getParameter("appId");
			String flag = request.getParameter("flag");
			HashMap<String, Object> map = new HashMap<String, Object>(5);
			if (StringUtils.isNotEmpty(str) && !str.equals("0")) {
				if (StringUtils.isNotEmpty(flag) && flag.equals("2")) {
					// 其他页面查找无需转码
					str = new String(str.getBytes("ISO8859-1"), "UTF-8");
				}
				map.put("appName", str);
			}
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<AppComment> result = appCommentService.searchAppComment(map, currentPage, pageSize);
			request.setAttribute("result", result);
			request.setAttribute("appId", str);

			// List<AppInfo> apps = appInfoService.getAllAppInfos();
			// request.setAttribute("apps", apps);
		} catch (Exception e) {
			log.error("AppCommentController list", e);
			PaginationVo<AppComment> result = new PaginationVo<AppComment>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "appComment/list";
	}

	/** 新增app评论 */
	@RequestMapping("/add/{appId}")
	public String showAdd(HttpServletRequest request, @PathVariable("appId") Integer appId) {
		try {
			// app列表　
			AppInfo appInfo = appInfoService.getApp(appId);
			request.setAttribute("appInfo", appInfo);
		} catch (Exception e) {
			log.error("AppCommentController showAdd", e);
		}
		return "appComment/add";
	}

	/** 新增app评论 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute AppComment comment, @RequestParam Integer appId) {
		try {
			AppInfo appInfo = appInfoService.getApp(appId);
			comment.setAppInfo(appInfo);

			appCommentService.addAppComment(comment);
		} catch (Exception e) {
			log.error("AppCommentController add", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 加载单个app评论 */
	@RequestMapping("/{id}")
	public String edit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			AppComment appComment = appCommentService.getAppComment(id);
			request.setAttribute("appComment", appComment);

		} catch (Exception e) {
			log.error("AppCommentController edit", e);
		}
		return "appComment/edit";
	}

	/** 更新app评论 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(AppComment comment, @RequestParam Integer appId) {
		try {
			AppInfo appInfo = appInfoService.getApp(appId);
			comment.setAppInfo(appInfo);

			appCommentService.upAppComment(comment);
		} catch (Exception e) {
			log.error("AppCommentController update", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 删除app评论 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids, Model model) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		appCommentService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}

}
