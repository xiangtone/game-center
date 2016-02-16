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
import org.springframework.util.CollectionUtils;
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
 * 公用app fatherChannelId=100000,channelId固定在100003,cpId=300001）
 * 
 * @author liwei.sz
 * 
 */
@Controller
@RequestMapping("/communal")
public class CommunalController {
	Logger log = Logger.getLogger(CommunalController.class);
	@Autowired
	private AppInfoService appService;

	@Autowired
	private CpService cpService;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CountryService countryService;

	@Autowired
	private AppPicService appPicService;

	@Autowired
	private AppFileService appFileService;

	@Autowired
	private AppAlbumColumnService appAlbumColumnService;

	@Autowired
	private AppAlbumResTempService appAlbumResTempService;

	/**
	 * 分布查看app分类信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
			String name = request.getParameter("name");
			String packageName = request.getParameter("packageName");

			// 获取 状态
			// String state = request.getParameter("state");
			String countryId = request.getParameter("countryId");
			String categroyId = request.getParameter("categoryId");
			String category_parent = request.getParameter("category_parent");
			String secondId = request.getParameter("secondId");
			AppInfo communal = new AppInfo();

			if (StringUtils.isNotEmpty(name)) {
				name = name.trim();
				try {
					communal.setId(Integer.parseInt(name));
				} catch (Exception e) {
					// name = "%" + name + "%";
					communal.setName(name);
				}
			}
			if (StringUtils.isNotEmpty(packageName)) {
				communal.setPackageName(packageName.trim());
			}
			if (StringUtils.isNotEmpty(countryId)) {
				try {
					communal.setCountryId(Integer.parseInt(countryId));
				} catch (Exception e) {
					communal.setCountryId(0);
				}
			}
			// 设置默认状态为是.
			// if (StringUtils.isEmpty(state)) {
			// communal.setState(true);
			// } else {
			// communal.setState(Boolean.parseBoolean(state));
			// }
			
			if (StringUtils.isNotEmpty(secondId) && !secondId.equals("0")) {
				Category category = new Category();
				category.setId(Integer.parseInt(secondId));
				communal.setCategory(category);
			} else if (StringUtils.isNotEmpty(categroyId) && !categroyId.equals("0")) {
				Category category = new Category();
				category.setId(Integer.parseInt(categroyId));
				communal.setCategory(category);
				communal.setCategory_parent1(Integer.parseInt(categroyId));
			} else if (StringUtils.isNotEmpty(category_parent) && !category_parent.equals("0")) {
				communal.setCategory_parent(Integer.parseInt(category_parent));
			}
			// communal.setFree(0);
			communal.setFree(-1); // 表示除=1外的

			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<AppInfo> result = appService.searchApps(communal, currentPage, pageSize);
			request.setAttribute("result", result);
			// 所有一级分类信息
			List<Category> categorys = categoryService.getCategorys(1);
			request.setAttribute("categorys", categorys);
			// 所有国家
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);
		} catch (Exception e) {
			log.error("CommunalController list", e);
			PaginationVo<AppInfo> result = new PaginationVo<AppInfo>(null, 0, 10, 1);
			request.setAttribute("result", result);

		}
		return "communal/list";
	}

	/** 新增app信息页 */
	@RequestMapping("/add/{tag}/{id}")
	public String showAddTag(@PathVariable("tag") Integer tag, @PathVariable("id") Integer id, HttpServletRequest request) {
		AppInfo communal = appService.getApp(id);
		request.setAttribute("communal", communal);

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
			request.getSession().setAttribute("menuFlag", "communal");
			return "redirect:/appPic/list/" + id + "?src=0";
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

		// 获取所有渠道信息
		List<Channel> channels = channelService.getChannels(200000);
		request.setAttribute("channels", channels);

		// 所有分类信息
		List<Category> categorys = categoryService.getCategorys(1);
		request.setAttribute("categorys", categorys);

		Cp cp = cpService.getCp(300001);
		request.setAttribute("cp", cp);
		return "communal/add";
	}

	/** 新增app分类信息 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute AppInfo communal, @RequestParam Integer categoryId1,@RequestParam Integer categoryId2, @RequestParam Integer channelId, @RequestParam Integer cpId, @RequestParam MultipartFile apkFile,
			@RequestParam MultipartFile bigApkFile, @RequestParam MultipartFile appApkFile1, @RequestParam MultipartFile appApkFile2, @RequestParam MultipartFile appApkFile3) {
		try {
			// 获取对应渠道信息
			Channel channel = channelService.getChannel(channelId);
			communal.setChannel(channel);
			if (communal.getAnotherName() != null) {
				communal.setAnotherName(StringUtil.convertBlankLinesToHashSymbol(communal.getAnotherName()));
			}
			communal.setInitialReleaseDate(new SimpleDateFormat("yyyy-MM-dd").parse(communal.getInitialReleaseDate1()));
			// 对应cp信息
			Cp cp = cpService.getCp(cpId);
			communal.setCp(cp);
			List<String> fileUrlList = new ArrayList<String>();// s3 list
			String str = FileAddresUtil.getFilePath(channel.getProvince().getId(), cpId, channel.getFatherId(), 0, Constant.LOGO_ADR);
			// 上传logo
			if (apkFile.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(apkFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					File apkLocalFile = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(apkFile.getOriginalFilename()));
					FileUtil.copyInputStream(apkFile.getInputStream(), apkLocalFile);// apk本地上传
					communal.setLogo(FileAddresUtil.getFilePath(apkLocalFile));
					fileUrlList.add(communal.getLogo());
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
					communal.setBigLogo(FileAddresUtil.getFilePath(bigApkLocalFile));
					fileUrlList.add(communal.getBigLogo());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			// 设置密码
			communal.setPwd(RandNum.randomPwd(6));
			communal.setPassword(MD5.getMd5Value(communal.getPwd()));
			Integer categoryId = 0;
			if(null == categoryId2 || categoryId2 == 0){
				categoryId = categoryId1;
			}else{
				categoryId = categoryId2;
			}
			appService.addApp(communal, categoryId);

			str = FileAddresUtil.getFilePath(channel.getProvince().getId(), cpId, channelId, communal.getId(), Constant.IMG_ADR);
			// 上传第一张图
			if (appApkFile1.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(appApkFile1.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					AppPic pic = new AppPic();
					pic.setAppInfo(communal);
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
					pic.setAppInfo(communal);
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
					pic.setAppInfo(communal);
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
			// s3 upload
			S3Util.uploadS3(fileUrlList, false);
		} catch (Exception e) {
			try {
				FileUtil.deleteFile(communal.getLogo());
				FileUtil.deleteFile(communal.getBigLogo());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log.error("CommunalController add", e);
			return "{\"flag\":\"3\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 加载单个app分类 */
	@RequestMapping("/{id}")
	public String showEdit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			AppInfo communal = appService.getApp(id);
			// communal.setName(communal.getName().replaceAll("\"",
			// "&quot;").replaceAll("'", "&apos;"));
			if (communal.getAnotherName() != null) {
				communal.setAnotherName(StringUtil.convertHashSymbolToBlankLines(communal.getAnotherName()));
			}
			// communal.setBrief(StringUtil.convertBlankLinesToHashSymbolBrTo(communal.getBrief()));
			// communal.setDescription(StringUtil.convertBlankLinesToHashSymbolBrTo(communal.getDescription()));
			model.addAttribute("communal", communal);
			// 获取所有渠道信息
			List<Channel> channels = channelService.getChannels(200000);
			request.setAttribute("channels", channels);
			String page = request.getParameter("page");
			// System.out.println("######page="+page);
			request.setAttribute("page", page);
			// 所有应用分类信息
			List<Category> fatherCategorys = categoryService.getCategorys(1);
			request.setAttribute("fatherCategorys", fatherCategorys);

			//当前category的fatherId是一级分类的id,查询出一级分类
			Category firstCategory = categoryService.getCategory(communal.getCategory().getFatherId());
			request.setAttribute("firstCategory", firstCategory);
			
//			// 所有分类信息
//			List<Category> categorys = categoryService.getCategorys(communal.getCategory().getFatherId());
//			request.setAttribute("categorys", categorys);

			Cp cp = cpService.getCp(communal.getCpId());
			request.setAttribute("cp", cp);
		} catch (Exception e) {
			log.error("CommunalController edit", e);
		}
		return "communal/edit";
	}

	/** 更新app分类 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(AppInfo communal, @RequestParam Integer categoryId1,@RequestParam Integer categoryId2, @RequestParam Integer channelId, @RequestParam Integer cpId, @RequestParam MultipartFile apkFile,
			@RequestParam MultipartFile bigApkFile) {
		try {
			// 获取对应渠道信息
			Channel channel = channelService.getChannel(channelId);
			communal.setChannel(channel);
			if (communal.getAnotherName() != null) {
				communal.setAnotherName(StringUtil.convertBlankLinesToHashSymbol(communal.getAnotherName()));
			}
			communal.setInitialReleaseDate(new SimpleDateFormat("yyyy-MM-dd").parse(communal.getInitialReleaseDate1()));
			// 对应cp信息
			Cp cp = cpService.getCp(cpId);
			communal.setCp(cp);
			List<String> fileUrlList = new ArrayList<String>();// s3 list
			String str = FileAddresUtil.getFilePath(channel.getProvince().getId(), cpId, channel.getFatherId(), 0, Constant.LOGO_ADR);
			// 上传LOG图路径
			if (apkFile.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(apkFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					// 删除原有数据包
					FileUtil.deleteFile(communal.getLogo());
					File apkLocalFile = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(apkFile.getOriginalFilename()));

					FileUtil.copyInputStream(apkFile.getInputStream(), apkLocalFile);
					communal.setLogo(FileAddresUtil.getFilePath(apkLocalFile));
					fileUrlList.add(communal.getLogo());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}

			// 上传大LOG图路径
			if (bigApkFile.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(bigApkFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					// 删除原有数据包
					FileUtil.deleteFile(communal.getBigLogo());
					File bigApkLocalFile = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(apkFile.getOriginalFilename()));
					FileUtil.copyInputStream(bigApkFile.getInputStream(), bigApkLocalFile);
					communal.setBigLogo(FileAddresUtil.getFilePath(bigApkLocalFile));
					fileUrlList.add(communal.getBigLogo());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			// 通知更新apk文件名
			appService.upAppFile(communal);
			communal.setPassword(MD5.getMd5Value(communal.getPwd()));
			// communal.setDescription(StringUtil.convertBlankLinesToHashSymbolBr(communal.getDescription()));
			// communal.setBrief(StringUtil.convertBlankLinesToHashSymbolBr(communal.getBrief()));
			Integer categoryId = 0;
			if(null == categoryId2 || categoryId2 == 0){
				categoryId = categoryId1;
			}else{
				categoryId = categoryId2;
			}
			appService.upApp(communal, categoryId);
			appService.upAppCollectionRes(communal);
			// 通知更新分发
			appService.upAppAlbumRes(communal);
			// 通知更新分发临时表
			appService.upAppAlbumResTemp(communal);
			// 通知更新对应文件cpId
			appFileService.upAppInfoByAppId(communal.getId(), cpId, communal.getName());
			// 通知新分发
			appService.upAppCountryScoreByAppId(communal);
			// s3 upload
			S3Util.uploadS3(fileUrlList, false);
		} catch (Exception e) {
			log.error("CommunalController update", e);
			return "{\"flag\":\"3\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 删除app分类 */
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
			AppInfo communal = appService.getApp(id);
			if (communal.getAnotherName() != null) {
				communal.setAnotherName(StringUtil.convertHashSymbolToBlankLines(communal.getAnotherName()));
			}
			model.addAttribute("communal", communal);
		} catch (Exception e) {
			log.error("CommunalController info", e);
		}
		return "communal/communalInfo";
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
			log.error("CommunalController info", e);
		}
		return "{\"success\":\"" + str + "\"}";
	}

	@ResponseBody
	@RequestMapping("/secondCategory")
	public String querySecondCategory(@RequestParam Integer id) {
		try {
			List<Category> list = categoryService.getCategorys(id);
			if (CollectionUtils.isEmpty(list)) {
				return "{\"success\":\"0\"}";
			} else {
				StringBuilder options = new StringBuilder();
				options.append("<option value='0'>--all--</option>");
				for (Category category : list) {
					options.append("<option value='");
					options.append(category.getId() + "'>");
					options.append(category.getName());
					options.append("</option>");
				}
				return "{\"success\":\"1\",\"option\":\"" + options.toString() + "\"}";
			}
		} catch (Exception e) {
			// TODO: handle exception
			return "{\"success\":\"2\"}";
		}
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
		return "communal/appConfig";
	}

	@ResponseBody
	@RequestMapping("/doConfig")
	public String doConfig(@RequestParam("id") String ids, @RequestParam("apkId") Integer apkId, @RequestParam("countyId") Integer countyId) {
		// 分发来源，0手动，1自动
		int source = 0;
		AppFile file = appFileService.getAppFile(apkId);
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		int re =0;
		try {
			List<Integer> list = new ArrayList<Integer>(1);
			list.add(file.getId());
			
			for (int i = 0; i < idIntArray.length; i++) {
				AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(idIntArray[i]);
				List<AppAlbumRes> res = appAlbumResTempService.getByFileColumnAndSource(file.getAppInfo(), appAlbumColumn, -1, countyId);
				if (res == null || res.size()<=0) {
					re++;
					appAlbumResTempService.resetAppAlbumResTemp(countyId, appAlbumColumn.getColumnId(), appAlbumColumn.getAppAlbum().getId(), source, list);
				}
			}

		} catch (Exception e) {
			log.error("AppInfoController doConfig", e);
			return "{\"flag\":\"1\",\"res\":\""+re+"\"}";
		}
		return "{\"flag\":\"0\",\"res\":\""+re+"\"}";
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
	@RequestMapping("/export2Excel")
	public ModelAndView exportToExcel(HttpServletRequest request, HttpServletResponse response) {
		XMLModelTemplate template = ExportExcelView.getTemplate(request, response);
		Map<String, Object> map = new HashMap<String, Object>();
		List<AppInfo> dataList = appService.selectADayAgoInfo();
		map.put("list", dataList);
		map.put("template", template);
		// 生成excel文件并返回给页面
		return new ModelAndView(new ExportExcelView(), map);
	}
}
