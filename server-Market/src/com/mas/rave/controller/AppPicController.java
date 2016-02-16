package com.mas.rave.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.multipart.MultipartFile;

import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.main.vo.AppPic;
import com.mas.rave.service.AppInfoService;
import com.mas.rave.service.AppPicService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.PicUtil;
import com.mas.rave.util.RandNum;
import com.mas.rave.util.s3.S3Util;

/**
 * app对应图片业务处理
 * 
 * @author liwei.sz
 * 
 */

@Controller
@RequestMapping("/appPic")
public class AppPicController {
	Logger log = Logger.getLogger(AppPicController.class);
	@Autowired
	private AppInfoService appService;

	@Autowired
	private AppPicService appPicService;

	/**
	 * 分页查看app图片信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list/{appId}")
	public String list(HttpServletRequest request, @PathVariable("appId") Integer appId) {
		try {
			List<AppPic> result = appPicService.getAppPics(appId);
			request.setAttribute("result", result);
			AppInfo appInfo = appService.getApp(appId);
			request.setAttribute("appInfo", appInfo);
			String src=request.getParameter("src");
			if(src==null){
				src="0";//公共
			}
			request.getSession().setAttribute("src", src);
		} catch (Exception e) {
			log.error("AppPicController list", e);
			PaginationVo<AppPic> result = new PaginationVo<AppPic>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "appPic/list";
	}

	/** 新增app截图信息页 */
	@RequestMapping("/showAdd/{appId}")
	public String showAdd(HttpServletRequest request, @PathVariable("appId") Integer appId) {
		AppInfo appInfo = appService.getApp(appId);
		request.setAttribute("appInfo", appInfo);
		return "appPic/add";

	}

	/** 新增app图片信息页 */
	@ResponseBody
	@RequestMapping(value = "/add/{appId}", method = RequestMethod.POST)
	public String add(@ModelAttribute AppPic appPic, @PathVariable("appId") Integer appId, @RequestParam MultipartFile appPicFile,HttpServletRequest request) {
		try {
			// 获取后缀,检测是否非法文件
			String suffix = FileAddresUtil.getSuffix(appPicFile.getOriginalFilename());
			if (suffix.equals(Constant.IMG_ADR)) {

				AppInfo appInfo = appService.getApp(appId);
				appPic.setAppInfo(appInfo);
				// 组装路径
				String str = FileAddresUtil.getFilePath(appInfo.getChannel().getProvince().getId(), appInfo.getCp().getId(), appInfo.getChannel().getId(), appInfo.getId(), Constant.IMG_ADR);
				if (appPicFile.getSize() != 0) {
					File pic1 = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(appPicFile.getOriginalFilename()));
					FileUtil.copyInputStream(appPicFile.getInputStream(), pic1);
					appPic.setUrl(FileAddresUtil.getFilePath(pic1));
					appPic.setWidth(Integer.parseInt(PicUtil.getPic(pic1)[1]));
					appPic.setLength(Integer.parseInt(PicUtil.getPic(pic1)[2]));
					appPic.setFileSize((int) appPicFile.getSize());
					appPicService.addAppPic(appPic);
					S3Util.uploadS3(appPic.getUrl(), false);
				}
			} else {
				// 文件有问题
				return "{\"flag\":\"2\"}";
			}
		} catch (Exception e) {
			log.error("AppPicController add", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 加载单个app图片 */
	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, Model model) {
		try {
			AppPic appPic = appPicService.getAppPic(id);
			model.addAttribute("appPic", appPic);
		} catch (Exception e) {
			log.error("AppPicController edit", e);
		}
		return "appPic/edit";
	}

	/** 更新app图片 */
	@ResponseBody
	@RequestMapping(value = "/update/{appId}", method = RequestMethod.POST)
	public String update(@ModelAttribute AppPic appPic, @PathVariable("appId") Integer appId, @RequestParam MultipartFile appPicFile,HttpServletRequest request) {
		try {
			AppInfo appInfo = appService.getApp(appId);
			appPic.setAppInfo(appInfo);
			if (appPicFile.getSize() != 0) {
				// 获取后缀,检测是否非法文件
				String suffix = FileAddresUtil.getSuffix(appPicFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					// 删除文件
					FileUtil.deleteFile(appPic.getUrl());
					String str = FileAddresUtil.getFilePath(appInfo.getChannel().getProvince().getId(), appInfo.getCp().getId(), appInfo.getChannel().getId(), appInfo.getId(), Constant.IMG_ADR);

					File pic1 = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(appPicFile.getOriginalFilename()));

					FileUtil.copyInputStream(appPicFile.getInputStream(), pic1);// apk本地上传
					appPic.setUrl(FileAddresUtil.getFilePath(pic1));
					appPic.setWidth(Integer.parseInt(PicUtil.getPic(pic1)[1]));
					appPic.setLength(Integer.parseInt(PicUtil.getPic(pic1)[2]));
					appPic.setFileSize((int) appPicFile.getSize());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			appPicService.upAppPic(appPic);
			S3Util.uploadS3(appPic.getUrl(), false);
		} catch (Exception e) {
			log.error("AppPicController update", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 删除app图片 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		appPicService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}

}
