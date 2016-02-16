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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.main.vo.OwnAppInfo;
import com.mas.rave.service.OwnAppInfoService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.DateUtil;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.MD5;
import com.mas.rave.util.RandNum;
import com.mas.rave.util.s3.S3Util;

/**
 * app业务处理　
 * 
 * @author liwei.sz
 * 
 */

@Controller
@RequestMapping("/ownApp")
public class OwnAppInfoController {
	Logger log = Logger.getLogger(OwnAppInfoController.class);
	@Autowired
	private OwnAppInfoService ownAppService;

	/**
	 * 分页查看app信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
			String name = request.getParameter("name");
			String source = request.getParameter("source");
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			OwnAppInfo appInfo = new OwnAppInfo();
			if (StringUtils.isNotEmpty(name)) {
				name = name.trim();
				// name = "%" + name + "%";
				appInfo.setName(name);
			}
			if (StringUtils.isNotEmpty(source)) {
				appInfo.setSource(source.trim());
			}
			if (startTime != null && !startTime.equals("")) {
				appInfo.setStartTime(DateUtil.StringToDate(startTime + " 00:00:00"));
			}
			if (endTime != null && !endTime.equals("")) {
				appInfo.setEndTime(DateUtil.StringToDate(endTime + " 23:59:59"));
			}
			appInfo.setFree(1);
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<OwnAppInfo> result = ownAppService.searchOwnApps(appInfo, currentPage, pageSize);
			request.setAttribute("result", result);
		} catch (Exception e) {
			log.error("OwnAppInfoController list", e);
			PaginationVo<AppInfo> result = new PaginationVo<AppInfo>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "ownApp/list";
	}

	/** 新增app信息页 */
	@RequestMapping("/add")
	public String showAdd(HttpServletRequest request) {
		return "ownApp/add";
	}

	/** 新增app信息 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute OwnAppInfo app, @RequestParam MultipartFile apkFile, @RequestParam MultipartFile bigApkFile) {
		try {
			List<String> fileUrlList = new ArrayList<String>();// s3 list
			// 上传logo
			if (apkFile.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(apkFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					File apkLocalFile = new File(Constant.LOCAL_FILE_PATH + Constant.LOCAL_BBS_PATH, RandNum.randomFileName(apkFile.getOriginalFilename()));
					FileUtil.copyInputStream(apkFile.getInputStream(), apkLocalFile);// apk本地上传
					app.setLogo(FileAddresUtil.getFilePath(apkLocalFile));
					fileUrlList.add(app.getLogo());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			// 上传大图
			if (bigApkFile.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(bigApkFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					File bigApkLocalFile = new File(Constant.LOCAL_FILE_PATH + Constant.LOCAL_BBS_PATH, RandNum.randomFileName(bigApkFile.getOriginalFilename()));
					FileUtil.copyInputStream(bigApkFile.getInputStream(), bigApkLocalFile);// apk本地上传
					app.setBigLogo(FileAddresUtil.getFilePath(bigApkLocalFile));
					fileUrlList.add(app.getBigLogo());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			// 设置密码
			app.setPwd(RandNum.randomPwd(6));
			app.setPassword(MD5.getMd5Value(app.getPwd()));
			ownAppService.addOwnApp(app);
			// s3upload
			S3Util.uploadS3(fileUrlList, false);
		} catch (Exception e) {
			try {
				FileUtil.deleteFile(app.getLogo());
				FileUtil.deleteFile(app.getBigLogo());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log.error("OwnAppInfoController add", e);

			return "{\"flag\":\"3\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 加载单个app */
	@RequestMapping("/{id}")
	public String showEdit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			OwnAppInfo app = ownAppService.getOwnApp(id.longValue());
			model.addAttribute("app", app);
		} catch (Exception e) {
			log.error("AppInfoController edit", e);
		}
		return "ownApp/edit";
	}

	/** 更新app */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(OwnAppInfo app, @RequestParam MultipartFile apkFile, @RequestParam MultipartFile bigApkFile, HttpServletRequest request) {
		try {
			List<String> fileUrlList = new ArrayList<String>();// s3 list
			// 上传LOG图路径
			if (apkFile.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(apkFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					// 删除原有数据包
					FileUtil.deleteFile(app.getLogo());
					File apkLocalFile = new File(Constant.LOCAL_FILE_PATH + Constant.LOCAL_BBS_PATH, RandNum.randomFileName(apkFile.getOriginalFilename()));

					FileUtil.copyInputStream(apkFile.getInputStream(), apkLocalFile);
					app.setLogo(FileAddresUtil.getFilePath(apkLocalFile));
					fileUrlList.add(app.getLogo());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}

			// 上传大LOG图路径
			if (bigApkFile.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(bigApkFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					// 删除原有数据包
					FileUtil.deleteFile(app.getBigLogo());
					File bigApkLocalFile = new File(Constant.LOCAL_FILE_PATH + Constant.LOCAL_BBS_PATH, RandNum.randomFileName(apkFile.getOriginalFilename()));
					FileUtil.copyInputStream(bigApkFile.getInputStream(), bigApkLocalFile);
					app.setBigLogo(FileAddresUtil.getFilePath(bigApkLocalFile));
					fileUrlList.add(app.getBigLogo());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			app.setFree(1);
			app.setPassword(MD5.getMd5Value(app.getPwd()));
			ownAppService.upOwnApp(app);

			// TODO 更新file表
			ownAppService.upOwnAppFile(app);
			// s3 upload
			S3Util.uploadS3(fileUrlList, false);
		} catch (Exception e) {
			log.error("AppInfoController update", e);
			return "{\"flag\":\"3\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 删除app */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids, Model model) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		ownAppService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}

}
