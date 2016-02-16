package com.mas.rave.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.main.vo.AppPic;
import com.mas.rave.main.vo.Category;
import com.mas.rave.main.vo.Channel;
import com.mas.rave.main.vo.Country;
import com.mas.rave.main.vo.Cp;
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

/**
 * 公用app fatherChannelId=100000,channelId固定在100003,cpId=300001）
 * 
 * @author liwei.sz
 * 
 */
@Controller
@RequestMapping("/open")
public class OpenAppInfoController {
	Logger log = Logger.getLogger(OpenAppInfoController.class);
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

			String countryId = request.getParameter("countryId");
			String categroyId = request.getParameter("categoryId");
			String category_parent = request.getParameter("category_parent");
			AppInfo communal = new AppInfo();

			if (StringUtils.isNotEmpty(name)) {
				name = name.trim();
				try {
					communal.setId(Integer.parseInt(name));
				} catch (Exception e) {
					name = name.trim();
//					name = "%" + name + "%";
					communal.setName(name);
				}
			}
			if (StringUtils.isNotEmpty(packageName)) {
				communal.setPackageName(packageName.trim());
			}
			//System.out.println("###countryId="+countryId);			
			if (StringUtils.isNotEmpty(countryId)) {
				try{
					communal.setCountryId(Integer.parseInt(countryId));
				} catch (Exception e) {
					communal.setCountryId(0);
				}
			}

			if (StringUtils.isNotEmpty(categroyId) && !categroyId.equals("0")) {
				Category category = new Category();
				category.setId(Integer.parseInt(categroyId));
				communal.setCategory(category);
			} else if (StringUtils.isNotEmpty(category_parent) && !category_parent.equals("0")) {
				communal.setCategory_parent(Integer.parseInt(category_parent));
			}
			communal.setFree(3);

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
		return "open/list";
	}
	/** 新增app信息页 */
	@RequestMapping("/add/{tag}/{id}")
	public String showAddTag(@PathVariable("tag") Integer tag, @PathVariable("id") Integer id, HttpServletRequest request) {
		AppInfo communal = appService.getApp(id);
		request.setAttribute("communal", communal);

		if (tag == 1) {
			// 获取所有渠道信息
			List<Channel> channels = channelService.getAllChannels(0);
			request.setAttribute("channels", channels);

			// 所有分类信息
			List<Category> categorys = categoryService.getAllCategorys();
			request.setAttribute("categorys", categorys);

			// 所有cp信息
			List<Cp> cps = cpService.getAllCps();
			request.setAttribute("cps", cps);
			// 转到审核文件页面
			return "redirect:/appFile/verify/" + id;
		} else if (tag == 2) {
			// 转到增加图片页面
			request.getSession().setAttribute("menuFlag", "open");
			return "redirect:/appPic/list/" + id+"?src=3";
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
		// 所有分类信息
		List<Category> categorys = categoryService.getCategorys(1);
		request.setAttribute("categorys", categorys);
		return "open/add";
	}

	/** 新增app分类信息 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute AppInfo communal, @RequestParam Integer categoryId1, @RequestParam Integer channelId, @RequestParam Integer cpId, @RequestParam MultipartFile apkFile,
			@RequestParam MultipartFile bigApkFile, @RequestParam MultipartFile appApkFile1, @RequestParam MultipartFile appApkFile2, @RequestParam MultipartFile appApkFile3) {
		try {
			// 获取对应渠道信息
			Channel channel = channelService.getChannel(channelId);
			communal.setChannel(channel);
			// 对应cp信息
			Cp cp = cpService.getCp(cpId);
			communal.setCp(cp);

			String str = FileAddresUtil.getFilePath(channel.getProvince().getId(), cpId, channel.getId(), 0, Constant.LOGO_ADR);
			// 上传logo
			if (apkFile.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(apkFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					File apkLocalFile = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(apkFile.getOriginalFilename()));
					FileUtil.copyInputStream(apkFile.getInputStream(), apkLocalFile);// apk本地上传
					communal.setLogo(FileAddresUtil.getFilePath(apkLocalFile));
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
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			// 设置密码
			communal.setPwd(RandNum.randomPwd(6));
			communal.setPassword(MD5.getMd5Value(communal.getPwd()));
			appService.addApp(communal, categoryId1);

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
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
		} catch (Exception e) {
			try {
				FileUtil.deleteFile(communal.getLogo());
				FileUtil.deleteFile(communal.getBigLogo());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log.error("OpenAppInfoController add", e);
			return "{\"flag\":\"3\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 加载单个app分类 */
	@RequestMapping("/{id}")
	public String showEdit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			AppInfo communal = appService.getApp(id);
			model.addAttribute("communal", communal);

			// 所有分类信息
			List<Category> fatherCategorys = categoryService.getCategorys(1);
			request.setAttribute("fatherCategorys", fatherCategorys);

			// 所有分类信息
			List<Category> categorys = categoryService.getCategorys(communal.getCategory().getFatherId());
			request.setAttribute("categorys", categorys);
		} catch (Exception e) {
			log.error("CommunalController edit", e);
		}
		return "open/edit";
	}

	/** 更新app分类 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(AppInfo communal, @RequestParam Integer categoryId1, @RequestParam Integer channelId, @RequestParam Integer cpId, @RequestParam MultipartFile apkFile,
			@RequestParam MultipartFile bigApkFile) {
		try {
			// 获取对应渠道信息
			Channel channel = channelService.getChannel(channelId);
			communal.setChannel(channel);
			// 对应cp信息
			Cp cp = cpService.getCp(cpId);
			communal.setCp(cp);

			String str = FileAddresUtil.getFilePath(channel.getProvince().getId(), cpId, channel.getId(), 0, Constant.LOGO_ADR);
			// 上传LOG图路径
			if (apkFile.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(apkFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					// 删除原有数据包
					FileUtil.deleteFile(communal.getLogo());
					File apkLocalFile = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(apkFile.getOriginalFilename()));

					FileUtil.copyInputStream(apkFile.getInputStream(), apkLocalFile);
					communal.setLogo(FileAddresUtil.getFilePath(apkLocalFile));
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
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			communal.setPassword(MD5.getMd5Value(communal.getPwd()));
			appService.upApp(communal, categoryId1);
			// 通知更新分发
			appService.upAppCollectionRes(communal);
			appService.upAppAlbumRes(communal);
			// 通知更新分发临时表
			appService.upAppAlbumResTemp(communal);
			// 通知更新对应文件cpId
			appFileService.upAppInfoByAppId(communal.getId(), cpId, communal.getName());
			
			// 通知新分发
			appService.upAppCountryScoreByAppId(communal);
		} catch (Exception e) {
			log.error("OpenAppInfoController update", e);
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
			model.addAttribute("communal", communal);
		} catch (Exception e) {
			log.error("CommunalController info", e);
		}
		return "open/communalInfo";
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
			log.error("OpenAppInfoController info", e);
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
