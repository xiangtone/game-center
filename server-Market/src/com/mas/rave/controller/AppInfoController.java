package com.mas.rave.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.servlet.ModelAndView;

import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.AppAlbumColumn;
import com.mas.rave.main.vo.AppAlbumRes;
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.main.vo.AppPic;
import com.mas.rave.main.vo.Category;
import com.mas.rave.main.vo.Channel;
import com.mas.rave.main.vo.Country;
import com.mas.rave.main.vo.Cp;
import com.mas.rave.report.ExportExcelView;
import com.mas.rave.report.XMLModelTemplate;
import com.mas.rave.service.AppAlbumColumnService;
import com.mas.rave.service.AppAlbumResTempService;
import com.mas.rave.service.AppFileService;
import com.mas.rave.service.AppInfoService;
import com.mas.rave.service.AppPicService;
import com.mas.rave.service.CategoryService;
import com.mas.rave.service.ChannelService;
import com.mas.rave.service.CountryService;
import com.mas.rave.service.CpService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.DateUtil;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.MD5;
import com.mas.rave.util.PicUtil;
import com.mas.rave.util.RandNum;
import com.mas.rave.util.StringUtil;
import com.mas.rave.util.s3.S3Util;

/**
 * app业务处理　
 * 
 * @author liwei.sz
 * 
 */

@Controller
@RequestMapping("/app")
public class AppInfoController {
	Logger log = Logger.getLogger(AppInfoController.class);
	@Autowired
	private AppInfoService appService;

	@Autowired
	private CpService cpService;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private AppPicService appPicService;

	@Autowired
	private AppFileService appFileService;

	@Autowired
	private AppAlbumColumnService appAlbumColumnService;

	@Autowired
	private AppAlbumResTempService appAlbumResTempService;

	@Autowired
	private CountryService countryService;

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
			String cpId = request.getParameter("cpId");
			String source = request.getParameter("source");
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			AppInfo appInfo = new AppInfo();
			if (StringUtils.isNotEmpty(name)) {
				name = name.trim();
				// name = "%" + name + "%";
				appInfo.setName(name);
			}
			if (StringUtils.isNotEmpty(cpId)) {
				appInfo.setCpId(Integer.parseInt(cpId.trim().toString()));
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
			appInfo.setFree(2);
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<AppInfo> result = appService.searchApps(appInfo, currentPage, pageSize);
			request.setAttribute("result", result);

			List<Cp> cps = cpService.getAllCps();
			request.setAttribute("cps", cps);
		} catch (Exception e) {
			log.error("AppInfoController list", e);
			PaginationVo<AppInfo> result = new PaginationVo<AppInfo>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "app/list";
	}

	@RequestMapping("/export2Excel")
	public ModelAndView exportToExcel(HttpServletRequest request, HttpServletResponse response) {
		XMLModelTemplate template = ExportExcelView.getTemplate(request, response);
		Map<String, Object> map = new HashMap<String, Object>();

		String name = request.getParameter("name");
		// 获取 状态
		AppInfo appInfo = new AppInfo();
		if (StringUtils.isNotEmpty(name)) {
			name = name.trim();
			appInfo.setName(name);
		}
		appInfo.setFree(2);
		List<AppInfo> dataList = appService.searchApps(appInfo);
		map.put("list", dataList);
		map.put("template", template);
		// 生成excel文件并返回给页面
		return new ModelAndView(new ExportExcelView(), map);
	}

	/** 新增app信息页 */
	@RequestMapping("/add/{tag}/{id}")
	public String showAddTag(@PathVariable("tag") Integer tag, @PathVariable("id") Integer id, HttpServletRequest request) {
		AppInfo app = appService.getApp(id);
		request.setAttribute("app", app);
		if (tag == 1) {
			// 获取所有渠道信息
			List<Channel> channels = channelService.getChannels(200000);
			request.setAttribute("channels", channels);

			// 所有分类信息
			List<Category> categorys = categoryService.getAllCategorys();
			request.setAttribute("categorys", categorys);

			// 所有cp信息
			List<Cp> cps = cpService.getAllCps();
			request.setAttribute("cps", cps);
			// 转到增加文件页面
			return "redirect:/appFile/list/" + id;
		} else if (tag == 2) {
			// 转到增加图片页面
			request.getSession().setAttribute("menuFlag", "app");
			return "redirect:/appPic/list/" + id + "?src=2";
		} else {
			// 获取所有渠道信息
			List<Channel> channels = channelService.getAllChannels(0);
			request.setAttribute("channels", channels);
			return "app/add";
		}
	}

	/** 新增app信息页 */
	@RequestMapping("/add")
	public String showAdd(HttpServletRequest request) {
		try {
			// 获取所有渠道信息
			List<Channel> channels = channelService.getAllChannels(0);
			request.setAttribute("channels", channels);

			// 所有分类信息
			List<Category> categorys = categoryService.getCategorys(1);
			request.setAttribute("categorys", categorys);

			// 所有cp信息
			List<Cp> cps = cpService.getAllCps();
			request.setAttribute("cps", cps);
		} catch (Exception e) {
			log.error("AppInfoController showAdd", e);
		}
		return "app/add";
	}

	/** 新增app信息 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute AppInfo app, @RequestParam Integer categoryId1,@RequestParam Integer categoryId2, @RequestParam Integer channelId, @RequestParam Integer cpId, @RequestParam MultipartFile apkFile,
			@RequestParam MultipartFile bigApkFile, @RequestParam MultipartFile appApkFile1, @RequestParam MultipartFile appApkFile2, @RequestParam MultipartFile appApkFile3) {
		try {
			// 获取对应渠道信息
			Channel channel = channelService.getChannel(channelId);
			if (app.getAnotherName() != null) {
				app.setAnotherName(StringUtil.convertBlankLinesToHashSymbol(app.getAnotherName()));
			}
			app.setChannel(channel);
			app.setInitialReleaseDate(new SimpleDateFormat("yyyy-MM-dd").parse(app.getInitialReleaseDate1()));
			// 对应cp信息
			Cp cp = cpService.getCp(cpId);
			app.setCp(cp);
			List<String> fileUrlList = new ArrayList<String>();// s3 list
			String str = FileAddresUtil.getFilePath(channel.getProvince().getId(), cpId, channelId, 0, Constant.LOGO_ADR);
			// 上传logo
			if (apkFile.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(apkFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					File apkLocalFile = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(apkFile.getOriginalFilename()));
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
					File bigApkLocalFile = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(bigApkFile.getOriginalFilename()));
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
			Integer categoryId = 0;
			if(null == categoryId2 || categoryId2 == 0){
				categoryId = categoryId1;
			}else{
				categoryId = categoryId2;
			}
			appService.addApp(app, categoryId);

			// 设置截图地址
			str = FileAddresUtil.getFilePath(channel.getProvince().getId(), cpId, channelId, app.getId(), Constant.IMG_ADR);
			// 上传第一张图
			if (appApkFile1.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(appApkFile1.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					AppPic pic = new AppPic();
					pic.setAppInfo(app);

					File pic1 = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(appApkFile1.getOriginalFilename()));

					FileUtil.copyInputStream(appApkFile1.getInputStream(), pic1);// apk本地上传
					pic.setUrl(FileAddresUtil.getFilePath(pic1));
					pic.setState(true);
					pic.setWidth(Integer.parseInt(PicUtil.getPic(pic1)[1]));
					pic.setLength(Integer.parseInt(PicUtil.getPic(pic1)[2]));
					pic.setFileSize((int) appApkFile1.getSize());
					appPicService.addAppPic(pic);
					fileUrlList.add(pic.getUrl());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			// 上传第二张图
			if (appApkFile2.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(appApkFile2.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					AppPic pic = new AppPic();
					pic.setAppInfo(app);
					File pic1 = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(appApkFile2.getOriginalFilename()));

					FileUtil.copyInputStream(appApkFile2.getInputStream(), pic1);// apk本地上传
					pic.setUrl(FileAddresUtil.getFilePath(pic1));
					pic.setState(true);
					pic.setWidth(Integer.parseInt(PicUtil.getPic(pic1)[1]));
					pic.setLength(Integer.parseInt(PicUtil.getPic(pic1)[2]));
					pic.setFileSize((int) appApkFile2.getSize());
					appPicService.addAppPic(pic);
					fileUrlList.add(pic.getUrl());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			// 上传第三张图
			if (appApkFile3.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(appApkFile3.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					AppPic pic = new AppPic();
					pic.setAppInfo(app);
					File pic1 = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(appApkFile3.getOriginalFilename()));

					FileUtil.copyInputStream(appApkFile3.getInputStream(), pic1);// apk本地上传
					pic.setUrl(FileAddresUtil.getFilePath(pic1));
					pic.setState(true);
					pic.setWidth(Integer.parseInt(PicUtil.getPic(pic1)[1]));
					pic.setLength(Integer.parseInt(PicUtil.getPic(pic1)[2]));
					pic.setFileSize((int) appApkFile3.getSize());
					appPicService.addAppPic(pic);
					fileUrlList.add(pic.getUrl());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
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
			log.error("AppInfoController add", e);

			return "{\"flag\":\"3\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 加载单个app */
	@RequestMapping("/{id}")
	public String showEdit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			AppInfo app = appService.getApp(id);
			// app.setName(app.getName().replaceAll("\"",
			// "&quot;").replaceAll("'", "&apos;"));
			if (app.getAnotherName() != null) {
				app.setAnotherName(StringUtil.convertHashSymbolToBlankLines(app.getAnotherName()));
			}
			// app.setDescription(StringUtil.convertBlankLinesToHashSymbolBrTo(app.getDescription()));
			// app.setBrief(StringUtil.convertBlankLinesToHashSymbolBrTo(app.getBrief()));
			model.addAttribute("app", app);

			// 获取所有渠道信息
			List<Channel> channels = channelService.getChannels(200000);
			request.setAttribute("channels", channels);

			// 所有应用分类信息
			List<Category> fatherCategorys = categoryService.getCategorys(1);
			request.setAttribute("fatherCategorys", fatherCategorys);
			
			//当前category的fatherId是一级分类的id,查询出一级分类
			Category firstCategory = categoryService.getCategory(app.getCategory().getFatherId());
			request.setAttribute("firstCategory", firstCategory);

//			// 所有分类信息
//			List<Category> categorys = categoryService.getCategorys(app.getCategory().getFatherId());
//			request.setAttribute("categorys", categorys);

			request.setAttribute("page", request.getParameter("page"));
			// 所有cp信息
			List<Cp> cps = cpService.getAllCps();
			request.setAttribute("cps", cps);
		} catch (Exception e) {
			log.error("AppInfoController edit", e);
		}
		return "app/edit";
	}

	/** 更新app */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(AppInfo app, @RequestParam Integer categoryId1,@RequestParam Integer categoryId2, @RequestParam Integer channelId, @RequestParam Integer cpId, @RequestParam MultipartFile apkFile,
			@RequestParam MultipartFile bigApkFile, HttpServletRequest request) {
		try {
			// 获取对应渠道信息
			Channel channel = channelService.getChannel(channelId);
			app.setChannel(channel);
			if (app.getAnotherName() != null) {
				app.setAnotherName(StringUtil.convertBlankLinesToHashSymbol(app.getAnotherName()));
			}
			app.setInitialReleaseDate(new SimpleDateFormat("yyyy-MM-dd").parse(app.getInitialReleaseDate1()));
			// 对应cp信息
			Cp cp = cpService.getCp(cpId);
			app.setCp(cp);
			List<String> fileUrlList = new ArrayList<String>();// s3 list
			String str = FileAddresUtil.getFilePath(channel.getProvince().getId(), cpId, channelId, app.getId(), Constant.LOGO_ADR);
			// 上传LOG图路径
			if (apkFile.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(apkFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					// 删除原有数据包
					FileUtil.deleteFile(app.getLogo());
					File apkLocalFile = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(apkFile.getOriginalFilename()));

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
					File bigApkLocalFile = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(apkFile.getOriginalFilename()));
					FileUtil.copyInputStream(bigApkFile.getInputStream(), bigApkLocalFile);
					app.setBigLogo(FileAddresUtil.getFilePath(bigApkLocalFile));
					fileUrlList.add(app.getBigLogo());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			app.setPassword(MD5.getMd5Value(app.getPwd()));
			// app.setDescription(StringUtil.convertBlankLinesToHashSymbolBr(app.getDescription()));
			// app.setBrief(StringUtil.convertBlankLinesToHashSymbolBr(app.getBrief()));
			Integer categoryId = 0;
			if(null == categoryId2 || categoryId2 == 0){
				categoryId = categoryId1;
			}else{
				categoryId = categoryId2;
			}
			appService.upApp(app, categoryId);

			// 通知更新apk文件名
			appService.upAppFile(app);
			// 通知更新分发
			appService.upAppCollectionRes(app);
			appService.upAppAlbumRes(app);
			// 通知更新分发临时表
			appService.upAppAlbumResTemp(app);
			// 通知更新对应文件cpId
			appFileService.upAppInfoByAppId(app.getId(), cpId, app.getName());
			// 通知新分发
			appService.upAppCountryScoreByAppId(app);
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
		appService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}

	/** 查看详细 */
	@RequestMapping("/info/{id}")
	public String info(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			AppInfo app = appService.getApp(id);
			if (app.getAnotherName() != null) {
				app.setAnotherName(StringUtil.convertHashSymbolToBlankLines(app.getAnotherName()));
			}
			model.addAttribute("app", app);
		} catch (Exception e) {
			log.error("AppInfoController info", e);
		}
		return "app/appInfo";
	}

	/** 查看详细 */
	@ResponseBody
	@RequestMapping("/getCategory")
	public String getCategory(HttpServletRequest request) {
		String str = "0";
		try {
			String id = request.getParameter("id");
			if (StringUtils.isNotEmpty(id)) {
				List<Category> categorys = categoryService.getCategorys(Integer.parseInt(id));
				if (categorys != null && categorys.size() > 0) {
					for (Category cate : categorys) {
						str += "," + cate.getName() + "-" + cate.getId();
					}
				}
			}
		} catch (Exception e) {
			log.error("AppInfoController info", e);
		}
		return "{\"success\":\"" + str + "\"}";
	}

	@RequestMapping("/appConfig/{appId}")
	public String appConfig(HttpServletRequest request, @PathVariable("appId") Integer appId) {
		try {
			int apkId = Integer.parseInt(request.getParameter("apkId"));
			String countyId = request.getParameter("countyId");
			List<AppFile> files = appFileService.getAppFiles(appId);
			List<AppAlbumColumn> res1 = new ArrayList<AppAlbumColumn>(12);

			List<AppAlbumColumn> result = appAlbumColumnService.getAppAlbumColumn();
			AppFile file = null;
			if (files != null && files.size() > 0) {
				if (apkId == 0) {
					file = files.get(0);
				} else {
					file = appFileService.getAppFile(apkId);
				}
				if (result != null && result.size() > 0) {
					for (AppAlbumColumn res : result) {
						if (res.getAppAlbum().getId() <= 3 && res.getFlag() == 1) {
							AppAlbumRes albumRes = null;
							if (StringUtils.isNotEmpty(countyId)) {
								albumRes = appAlbumResTempService.getApkRes(Integer.parseInt(countyId), res.getColumnId(), file.getId());
							} else {
								albumRes = appAlbumResTempService.getApkRes(file.getRaveId(), res.getColumnId(), file.getId());
							}
							if (albumRes != null) {
								res.setChecked(true);
							}
							if (file.getAppInfo().getCategory().getId() == 45) {
								res1.add(res);
							} else {
								if (res.getColumnId() != 45) {
									res1.add(res);
								}
							}

						}
					}
				}
			}

			List<Country> countys = countryService.getCountrys();
			request.setAttribute("countys", countys);
			if (file.getRaveId() != 1) {
				request.setAttribute("raveId", file.getRaveId());
			} else {
				request.setAttribute("raveId", countyId);
			}
			request.setAttribute("files", files);
			request.setAttribute("file", file);
			request.setAttribute("result", res1);
			AppInfo appInfo = appService.getApp(appId);
			request.setAttribute("appInfo", appInfo);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("AppInfoController info", e);
		}
		return "app/appConfig";
	}

	@ResponseBody
	@RequestMapping("/doConfig")
	public String doConfig(@RequestParam("id") String ids, @RequestParam("apkId") Integer apkId, @RequestParam("countyId") Integer countyId) {
		// 分发来源，0手动，1自动
		int source = 0;
		AppFile file = appFileService.getAppFile(apkId);
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		int re = 0;
		try {
			List<Integer> list = new ArrayList<Integer>(1);
			list.add(file.getId());

			for (int i = 0; i < idIntArray.length; i++) {
				AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(idIntArray[i]);
				List<AppAlbumRes> res = appAlbumResTempService.getByFileColumnAndSource(file.getAppInfo(), appAlbumColumn, -1, countyId);
				if (res == null || res.size() <= 0) {
					re++;
					appAlbumResTempService.resetAppAlbumResTemp(countyId, appAlbumColumn.getColumnId(), appAlbumColumn.getAppAlbum().getId(), source, list);
				}
			}

		} catch (Exception e) {
			log.error("AppInfoController doConfig", e);
			return "{\"flag\":\"1\",\"res\":\"" + re + "\"}";
		}
		return "{\"flag\":\"0\",\"res\":\"" + re + "\"}";
	}
	/** 查看详细 */
	@ResponseBody
	@RequestMapping("/update/{id}")
	public String update(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			String initialReleaseDate = request.getParameter("initialReleaseDate");
			AppInfo appAdjust = appService.getApp(id);
			if (appAdjust != null) {
				if (StringUtils.isNotEmpty(initialReleaseDate)) {
					appAdjust.setInitialReleaseDate(DateUtil.StringToDate1(initialReleaseDate));
				}
				appService.upFree(appAdjust);
				return "{\"flag\":\"0\"}";
			} else {
				return "{\"flag\":\"1\"}";
			}
		} catch (Exception e) {
			log.error("AppInfoAdjustController info", e);
			return "{\"flag\":\"2\"}";
		}
	}

}
