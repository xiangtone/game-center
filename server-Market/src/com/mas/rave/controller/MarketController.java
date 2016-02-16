package com.mas.rave.controller;

import java.io.File;
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
import com.mas.rave.main.vo.AppPic;
import com.mas.rave.main.vo.Category;
import com.mas.rave.main.vo.Channel;
import com.mas.rave.main.vo.Cp;
import com.mas.rave.service.AppFileService;
import com.mas.rave.service.AppInfoService;
import com.mas.rave.service.AppPicService;
import com.mas.rave.service.CategoryService;
import com.mas.rave.service.ChannelService;
import com.mas.rave.service.CpService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.MD5;
import com.mas.rave.util.PicUtil;
import com.mas.rave.util.RandNum;
import com.mas.rave.util.StringUtil;
import com.mas.rave.util.s3.S3Util;

/**
 * 平台业务处理　
 * 
 * @author liwei.sz
 * 
 */
@Controller
@RequestMapping("/market")
public class MarketController {
	Logger log = Logger.getLogger(MarketController.class);
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

	/**
	 * 查看平台信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
			String name = request.getParameter("name");
			// 获取 状态
			String state = request.getParameter("state");
			AppInfo market = new AppInfo();

			if (StringUtils.isNotEmpty(name)) {
				name = name.trim();
//				name = "%" + name + "%";
				market.setName(name);
			}
			// 设置默认状态为是.
			if (StringUtils.isEmpty(state)) {
				market.setState(true);
			} else {
				market.setState(Boolean.parseBoolean(state));
			}
			market.setFree(1);
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<AppInfo> result = appService.searchApps(market, currentPage, pageSize);
			request.setAttribute("result", result);
		} catch (Exception e) {
			log.error("MarketController list", e);
			PaginationVo<AppInfo> result = new PaginationVo<AppInfo>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "market/list";
	}

	/** 新增平台信息页 */
	@RequestMapping("/add/{tag}/{id}")
	public String showAddTag(@PathVariable("tag") Integer tag, @PathVariable("id") Integer id, HttpServletRequest request) {
		AppInfo market = appService.getApp(id);
		request.setAttribute("market", market);

		if (tag == 1) {
			// 获取所有渠道信息
			List<Channel> channels = channelService.getChannels(100000);
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
			request.getSession().setAttribute("menuFlag", "market");
			return "redirect:/appPic/list/" + id+"?src=1";
		} else {
			// 获取所有渠道信息
			List<Channel> channels = channelService.getAllChannels(0);
			request.setAttribute("channels", channels);
			return "market/add";
		}
	}

	/** 新增平台信息页 */
	@RequestMapping("/add")
	public String showAdd(HttpServletRequest request) {
		// 获取所有渠道信息
		List<Channel> channels = channelService.getChannels(100000);
		request.setAttribute("channels", channels);

		// 所有分类信息
		List<Category> categorys = categoryService.getSecondCategorys();
		request.setAttribute("categorys", categorys);

		// 所有cp信息
		List<Cp> cps = cpService.getAllCps();
		request.setAttribute("cps", cps);

		return "market/add";
	}

	/** 新增平台信息 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute AppInfo market, @RequestParam Integer channelId, @RequestParam Integer categoryId, @RequestParam Integer cpId, @RequestParam MultipartFile apkFile,
			@RequestParam MultipartFile bigApkFile, @RequestParam MultipartFile appApkFile1, @RequestParam MultipartFile appApkFile2, @RequestParam MultipartFile appApkFile3) {
		try {
			// 获取对应渠道信息
			Channel channel = channelService.getChannel(channelId);
			market.setChannel(channel);
			if(market.getAnotherName()!=null){
				market.setAnotherName(StringUtil.convertBlankLinesToHashSymbol(market.getAnotherName()));
			}
			// 对应cp信息
			Cp cp = cpService.getCp(cpId);
			market.setCp(cp);
			List<String> fileUrlList = new ArrayList<String>();
			String str = FileAddresUtil.getFilePath(channel.getProvince().getId(), cpId, channelId, 0, Constant.LOGO_ADR);
			// 上传logo
			if (apkFile.getSize() != 0) {
				File apkLocalFile = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(apkFile.getOriginalFilename()));
				FileUtil.copyInputStream(apkFile.getInputStream(), apkLocalFile);// apk本地上传
				market.setLogo(FileAddresUtil.getFilePath(apkLocalFile));
				fileUrlList.add(market.getLogo());
			}
			// 上传大图
			if (bigApkFile.getSize() != 0) {
				File bigApkLocalFile = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(bigApkFile.getOriginalFilename()));
				FileUtil.copyInputStream(bigApkFile.getInputStream(), bigApkLocalFile);// apk本地上传
				market.setBigLogo(FileAddresUtil.getFilePath(bigApkLocalFile));
				fileUrlList.add(market.getBigLogo());
			}
			// 设置密码
			market.setPwd(RandNum.randomPwd(6));
			market.setPassword(MD5.getMd5Value(market.getPwd()));

			appService.addApp(market, categoryId);

			str = FileAddresUtil.getFilePath(channel.getProvince().getId(), cpId, channelId, market.getId(), Constant.IMG_ADR);
			// 上传第一张图
			if (appApkFile1.getSize() != 0) {
				AppPic pic = new AppPic();
				pic.setAppInfo(market);

				File pic1 = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(appApkFile1.getOriginalFilename()));

				FileUtil.copyInputStream(appApkFile1.getInputStream(), pic1);// apk本地上传
				pic.setUrl(FileAddresUtil.getFilePath(pic1));
				pic.setWidth(Integer.parseInt(PicUtil.getPic(pic1)[1]));
				pic.setLength(Integer.parseInt(PicUtil.getPic(pic1)[2]));
				pic.setFileSize((int) appApkFile1.getSize());
				appPicService.addAppPic(pic);
				fileUrlList.add(pic.getUrl());
			}
			// 上传第二张图
			if (appApkFile2.getSize() != 0) {
				AppPic pic = new AppPic();
				pic.setAppInfo(market);
				File pic1 = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(appApkFile2.getOriginalFilename()));

				FileUtil.copyInputStream(appApkFile2.getInputStream(), pic1);// apk本地上传
				pic.setUrl(FileAddresUtil.getFilePath(pic1));
				pic.setWidth(Integer.parseInt(PicUtil.getPic(pic1)[1]));
				pic.setLength(Integer.parseInt(PicUtil.getPic(pic1)[2]));
				pic.setFileSize((int) appApkFile2.getSize());
				appPicService.addAppPic(pic);
				fileUrlList.add(pic.getUrl());
			}
			// 上传第三张图
			if (appApkFile3.getSize() != 0) {
				AppPic pic = new AppPic();
				pic.setAppInfo(market);
				File pic1 = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(appApkFile3.getOriginalFilename()));

				FileUtil.copyInputStream(appApkFile3.getInputStream(), pic1);// apk本地上传
				pic.setUrl(FileAddresUtil.getFilePath(pic1));
				pic.setWidth(Integer.parseInt(PicUtil.getPic(pic1)[1]));
				pic.setLength(Integer.parseInt(PicUtil.getPic(pic1)[2]));
				pic.setFileSize((int) appApkFile3.getSize());
				appPicService.addAppPic(pic);
				fileUrlList.add(pic.getUrl());
			}
			//upload s3
			S3Util.uploadS3(fileUrlList, false);
		} catch (Exception e) {
			log.error("MarketController add", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 加载单个平台信息 */
	@RequestMapping("/{id}")
	public String showEdit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			AppInfo market = appService.getApp(id);
			if(market.getAnotherName()!=null){
				market.setAnotherName(StringUtil.convertHashSymbolToBlankLines(market.getAnotherName()));
			}
			//market.setBrief(StringUtil.convertBlankLinesToHashSymbolBrTo(market.getBrief()));
			//market.setDescription(StringUtil.convertBlankLinesToHashSymbolBrTo(market.getDescription()));
			model.addAttribute("market", market);
		} catch (Exception e) {
			log.error("MarketController edit", e);
		}
		return "market/edit";
	}

	/** 更新平台信息 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(AppInfo market, @RequestParam Integer categoryId, @RequestParam Integer channelId, @RequestParam Integer cpId, @RequestParam MultipartFile apkFile,
			@RequestParam MultipartFile bigApkFile) {
		try {
			// 获取对应渠道信息
			Channel channel = channelService.getChannel(channelId);
			market.setChannel(channel);
			if(market.getAnotherName()!=null){
				market.setAnotherName(StringUtil.convertBlankLinesToHashSymbol(market.getAnotherName()));
			}
			// 对应cp信息
			Cp cp = cpService.getCp(cpId);
			market.setCp(cp);
			List<String> fileUrlList = new ArrayList<String>();//s3 list
			String str = FileAddresUtil.getFilePath(channel.getProvince().getId(), cpId, channelId, 0, Constant.LOGO_ADR);
			// 上传LOG图路径
			if (apkFile.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(apkFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					// 删除原有数据包
					FileUtil.deleteFile(market.getLogo());
					File apkLocalFile = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(apkFile.getOriginalFilename()));

					FileUtil.copyInputStream(apkFile.getInputStream(), apkLocalFile);
					market.setLogo(FileAddresUtil.getFilePath(apkLocalFile));
					fileUrlList.add(market.getLogo());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}

			// 上传大LOG图路径
			if (bigApkFile.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(bigApkFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					// 删除原有数据包
					FileUtil.deleteFile(market.getBigLogo());
					File bigApkLocalFile = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(apkFile.getOriginalFilename()));
					FileUtil.copyInputStream(bigApkFile.getInputStream(), bigApkLocalFile);
					market.setBigLogo(FileAddresUtil.getFilePath(bigApkLocalFile));
					fileUrlList.add(market.getBigLogo());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			market.setPassword(MD5.getMd5Value(market.getPwd()));
			//market.setDescription(StringUtil.convertBlankLinesToHashSymbolBr(market.getDescription()));
			//market.setBrief(StringUtil.convertBlankLinesToHashSymbolBr(market.getBrief()));
			appService.upApp(market, categoryId);
			// 通知更新apk文件名
			appService.upAppFile(market);
			// 通知更新分发
			appService.upAppCollectionRes(market);
			appService.upAppAlbumRes(market);
			appService.upAppAlbumResTemp(market);
			// 通知更新对应文件cpId
			appFileService.upAppInfoByAppId(market.getId(), cpId, market.getName());
			
			// 通知新分发
			appService.upAppCountryScoreByAppId(market);
			//s3 upload
			S3Util.uploadS3(fileUrlList, false);
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("MarketController update", e);

			return "{\"flag\":\"3\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 删除平台信息 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids, Model model) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		appService.batchDelete(idIntArray);
		return "{success:true}";
	}

	/** 查看详细 */
	@RequestMapping("/info/{id}")
	public String info(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			AppInfo market = appService.getApp(id);
			if(market.getAnotherName()!=null){
				market.setAnotherName(StringUtil.convertHashSymbolToBlankLines(market.getAnotherName()));
			}
			model.addAttribute("market", market);
		} catch (Exception e) {
			log.error("MarketController info", e);
		}
		return "market/marketInfo";
	}

	// 获取文件根路径
	public String getPath(AppInfo app) {
		String str = app.getChannel().getProvince().getId() + File.separator + app.getCp().getId() + File.separator + app.getChannel().getId() + File.separator + 0 + Constant.LOCAL_APK_MARKET
				+ Constant.LOCAL_APK_PIC;
		return str;
	}

}
