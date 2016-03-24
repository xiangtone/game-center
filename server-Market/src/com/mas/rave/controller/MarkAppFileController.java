package com.mas.rave.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
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
import com.mas.rave.common.parser.AppBatchUploadEngine;
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.AppFileList;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.main.vo.AppInfoConfig;
import com.mas.rave.main.vo.Channel;
import com.mas.rave.main.vo.Country;
import com.mas.rave.main.vo.Cp;
import com.mas.rave.service.AppFileListService;
import com.mas.rave.service.AppFilePatchService;
import com.mas.rave.service.AppFileService;
import com.mas.rave.service.AppInfoConfigService;
import com.mas.rave.service.AppInfoService;
import com.mas.rave.service.AppPicService;
import com.mas.rave.service.ChannelService;
import com.mas.rave.service.CountryService;
import com.mas.rave.service.CpService;
import com.mas.rave.task.AppFilePatchTaskService;
import com.mas.rave.util.AppConfig;
import com.mas.rave.util.Constant;
import com.mas.rave.util.DateUtil;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.MD5;
import com.mas.rave.util.RandNum;
import com.mas.rave.util.ReadApkInfo;
import com.mas.rave.util.StringUtil;
import com.mas.rave.util.s3.S3Util;
import com.molon.android.parser.ApkManifestParser;

/**
 * app平台文件业务处理
 * 
 * @author liwei.sz
 * 
 */

@Controller
@RequestMapping("/markAppFile")
public class MarkAppFileController {

	Logger log = Logger.getLogger(MarkAppFileController.class);

	private final String FTP_APK_PATH = "/home/ftpapk/";

	@Autowired
	private AppInfoService appService;

	@Autowired
	private AppFileService appFileService;

	@Autowired
	private AppFileListService appFileListService;

	@Autowired
	private CpService cpService;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private AppPicService appPicService;

	@Autowired
	private AppBatchUploadEngine appBatchUploadEngine;

	@Autowired
	private AppFilePatchService appFilePatchService;

	@Autowired
	private AppInfoConfigService appInfoConfigService;

	@Autowired
	private CountryService countryService;

	/**
	 * 分页查看app文件信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list/{appId}")
	public String list(HttpServletRequest request, @PathVariable("appId") Integer appId) {
		try {
			List<AppFile> result = appFileService.getAppFiles(appId);
			request.setAttribute("result", result);
			AppInfo appInfo = appService.getApp(appId);
			request.setAttribute("appInfo", appInfo);
		} catch (Exception e) {
			log.error("MarkAppFileController list", e);
			PaginationVo<AppFile> result = new PaginationVo<AppFile>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "markAppFile/list";
	}

	/** 新增app文件信息页 */
	@RequestMapping("/showAdd/{appId}")
	public String showAdd(HttpServletRequest request, @PathVariable("appId") Integer appId) {
		try {
			AppInfo appInfo = appService.getApp(appId);
			request.setAttribute("appInfo", appInfo);

			// 获取所有渠道信息
			List<Channel> channels = channelService.getChannels(appInfo.getChannel().getId());
			request.setAttribute("channels", channels);

			request.setAttribute("ftp_apk_defaul_path", Constant.APK_FTP_PATH);

			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);
		} catch (Exception e) {
			log.error("MarkAppFileController showAdd", e);
		}
		return "markAppFile/add";
	}

	/** 新增app文件信息页 */
	@ResponseBody
	@RequestMapping(value = "/add/{appId}", method = RequestMethod.POST)
	public String add(@ModelAttribute AppFile appFile, @RequestParam Integer appId, @RequestParam Integer channelId, @RequestParam Integer cpId, @RequestParam MultipartFile appApkFile,
			HttpServletRequest request) {
		try {
			// ftp上传路径
			String apkPath = request.getParameter("apkInfo");
			// 选择上传方式,1:本地apk文件上传,2:通过ftp路径上传
			String uploadType = request.getParameter("uploadType");
			// 选择本地apk上传方式,但提交文件为空
			if (uploadType.equals("1") && appApkFile.getSize() == 0) {
				// 文件有问题
				return getError(appFile.getPackageName(), 2);
			}
			// 选择ftp上传方式,但是没提交路径为空
			if (uploadType.equals("2") && StringUtils.isEmpty(apkPath)) {
				// 文件有问题
				return getError("上传失败!", 100);
			}

			// 组装文件路径国家/cp/渠道/apk
			AppInfo appInfo = appService.getApp(appId);
			appFile.setAppInfo(appInfo);

			Channel channel = channelService.getChannel(channelId);
			appFile.setChannel(channel);

			Cp cp = cpService.getCp(cpId);
			appFile.setCp(cp);

			int index = apkPath.lastIndexOf("/") == -1 ? apkPath.lastIndexOf("\\") : apkPath.lastIndexOf("/");
			String apkName = StringUtils.isEmpty(apkPath) ? appApkFile.getOriginalFilename() : apkPath.substring(index + 1).trim();

			// 获取后缀
			String suffix = FileAddresUtil.getSuffix(apkName);
			if (suffix.equals(Constant.APK_ADR)) {
				// 组装文件地址
				String str = FileAddresUtil.getFilePath(appFile.getRaveId(), cpId, channel.getId(), appId, suffix);
				str = str.substring(0, str.length() - 1);
				// 平台
				str += Constant.LOCAL_APK_MARKET;

				// 当文件名为了中文时随机生成名字
				String name = RandNum.getRandNumStr() + ".apk";
				File apkFile = new File(Constant.LOCAL_FILE_PATH + str, name);
				File srcFile = new File(apkPath.trim());
				if (uploadType.equals("1")) {
					FileUtil.copyInputStream(appApkFile.getInputStream(), apkFile);// apk本地上传
				} else {
					File destDir = new File(Constant.LOCAL_FILE_PATH + str);
					FileUtils.copyFileToDirectory(srcFile, destDir);
					// 移动到新目录后,重命名该文件
					File newFile = new File(Constant.LOCAL_FILE_PATH + str, apkName);
					if (newFile.isFile()) {
						newFile.renameTo(apkFile);
					}
				}

				appFile.setUrl(apkFile.getPath().substring(Constant.LOCAL_FILE_PATH.length(), apkFile.getPath().length()));
				appFile.setAppName(appInfo.getName());

				appFile.setServerId(1);
				if (uploadType.equals("1")) {
					appFile.setFileSize((int) appApkFile.getSize());
				} else {
					appFile.setFileSize((int) srcFile.length());
				}
				// 解析apk包
				try {
					analysisApk(appFile, request.getSession().getServletContext().getRealPath("/"));
				} catch (Exception e) {
					return "{\"flag\":\"5\"}"; // 解析包名出错
				}
				// 对文件重新命名
				apkName = FileUtil.getFileName(appFile);
				String url = FileUtil.renameFile(apkFile, apkName);
				if (StringUtils.isNotEmpty(url)) {
					appFile.setUrl(url.substring(Constant.LOCAL_FILE_PATH.length(), url.length()));
					S3Util.uploadS3(appFile.getUrl(), false);
				}
				if ((appFile.getPackageName() + channelId).equals(AppConfig.MARKETT_PAC)) {
					// 平台key
					appFile.setApkKey(AppConfig.MARKETT_KEY);
				} else {
					// 新平台指定key
					appFile.setApkKey(MD5.getMd5Value(cpId + channelId + appFile.getPackageName()));
				}
				AppFile appFile1 = null;
				List<AppFile> appFiles = appFileService.getAppFileByPacs(null, appId, channelId, null);
				if (appFiles != null && appFiles.size() > 0) {
					for (AppFile fil : appFiles) {
						if (!fil.getPackageName().equals(appFile.getPackageName())) {
							FileUtil.deleteFile(appFile.getUrl());
							return "{\"flag\":\"10\",\"pac\":\"" + "(" + name + ")" + appFile.getPackageName() + "\"}";
						} else if (fil.getPackageName().equals(appFile.getPackageName()) && fil.getRaveId() == appFile.getRaveId()) {
							appFile1 = fil;
							break;
						}
					}
				}
				if (appFile1 != null) {
					if (appFile.getPackageName().equals(appFile1.getPackageName())) {
						if (appFile.getVersionName() != null && !appFile.getVersionName().equals(appFile1.getVersionName()) && appFile.getVersionCode() > appFile1.getVersionCode()) {
							// 有高版本更新, 写入文件历史表
							appFileListService.convertFile(appFile1);

							// 设置参数并入库
							appFile1.setFileSize(appFile.getFileSize());
							appFile1.setPackageName(appFile.getPackageName());
							appFile1.setVersionCode(appFile.getVersionCode());
							appFile1.setVersionName(appFile.getVersionName());
							appFile1.setUrl(appFile.getUrl());
							appFile1.setHaslist(true);
							appFile1.setUpdateInfo(appFile.getUpdateInfo());
							appFileService.upAppFile(appFile1);

							// 更新分发
							appFileService.upAppCollectionRes(appFile1);
							appFileService.upAppAlbumRes(appFile1);
							appFileService.upAppAlbumResTemp(appFile1);
							// 更新下载数
							appService.updateNum(appId);

							createPack(appFile1);
							return "{\"flag\":\"3\",\"pac\":\"" + "(" + appFile1.getAppName() + ")" + appFile1.getPackageName() + "\"}";
						} else {
							return "{\"flag\":\"1\",\"pac\":\"" + "(" + name + ")" + appFile.getPackageName() + "\"}";
						}
					}
				} else {
					createAppConfig(appFile);
				}
				try {
					appFileService.addAppFile(appFile);
				} catch (Exception e) {
					if (e instanceof DuplicateKeyException) {
						return "{\"flag\":\"6\",\"pac\":\"" + appFile.getPackageName() + "\"}";
					} else if (e instanceof DataIntegrityViolationException) {
						return "{\"flag\":\"7\"}";
					} else {
						return "{\"flag\":\"8\"}";
					}
				}
			} else {
				// 文件有问题
				return getError(appFile.getPackageName(), 2);
			}
		} catch (Exception e) {
			String name = "";
			if (StringUtil.chineseValid(appApkFile.getOriginalFilename())) {
				// 文件为中文随机生成名字
				name = RandNum.getRandNumStr() + "." + FileUtil.getFileSuffix(appApkFile.getOriginalFilename());
			} else {
				name = appApkFile.getOriginalFilename();
			}
			try {
				FileUtil.deleteFile(appFile.getUrl());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			log.error("MarkAppFileController add", e);
			return "{\"flag\":\"4\",\"pac\":\"" + "(" + name + ")" + appFile.getPackageName() + "\"}";
		}
		return getError(appFile.getPackageName(), 0);
	}

	/** 加载单个app文件 */
	@RequestMapping("/edit/{id}")
	public String edit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			AppFile appFile = appFileService.getAppFile(id);
			appFile.setUpdateInfo(StringUtil.convertBlankLinesToHashSymbolBrTo(appFile.getUpdateInfo()));
			model.addAttribute("appFile", appFile);
			// 获取所有渠道信息
			List<Channel> channels = channelService.getChannels(appFile.getAppInfo().getChannel().getId());
			request.setAttribute("channels", channels);

			// 所有cp信息
			List<Cp> cps = cpService.getAllCps();
			request.setAttribute("cps", cps);
			request.setAttribute("ftp_apk_defaul_path", Constant.APK_FTP_PATH);

			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);
		} catch (Exception e) {
			log.error("MarkAppFileController edit", e);
		}
		return "markAppFile/edit";
	}

	/** 更新app文件 */
	@ResponseBody
	@RequestMapping(value = "/update/{appId}", method = RequestMethod.POST)
	public String update(@ModelAttribute AppFile appFile, @PathVariable("appId") Integer appId, @RequestParam Integer channelId, @RequestParam Integer cpId, @RequestParam MultipartFile appApkFile,
			@RequestParam String updateTime1, HttpServletRequest request) {

		// ftp上传路径
		String apkPath = request.getParameter("apkInfo") == null ? "" : request.getParameter("apkInfo").trim();
		// 选择上传方式,1:本地apk文件上传,2:通过ftp路径上传
		String uploadType = request.getParameter("uploadType") == null ? "" : request.getParameter("uploadType").trim();

		try {
			AppInfo appInfo = appService.getApp(appId);
			appFile.setAppInfo(appInfo);

			Channel channel = channelService.getChannel(channelId);
			appFile.setChannel(channel);

			Cp cp = cpService.getCp(cpId);
			appFile.setCp(cp);
			if (appApkFile.getSize() != 0 || (StringUtils.isNotEmpty(apkPath)) && uploadType.equals("2")) {
				// 获取后缀
				String suffix = FileAddresUtil.getSuffix(StringUtils.isEmpty(appApkFile.getOriginalFilename()) ? apkPath : appApkFile.getOriginalFilename());
				if (suffix.equals(Constant.APK_ADR)) {
					// 组装文件地址
					String str = FileAddresUtil.getFilePath(appFile.getRaveId(), cpId, channel.getId(), appId, suffix);
					str = str.substring(0, str.length() - 1);
					// 平台
					str += Constant.LOCAL_APK_MARKET;

					int index = apkPath.lastIndexOf("/") == -1 ? apkPath.lastIndexOf("\\") : apkPath.lastIndexOf("/");
					String apkName = StringUtils.isEmpty(apkPath) ? appApkFile.getOriginalFilename() : apkPath.substring(index + 1).trim();

					// 当文件名为了中文时随机生成名字
					String name = RandNum.getRandNumStr() + ".apk";
					File apkFile = new File(Constant.LOCAL_FILE_PATH + str, name);
					File srcFile = new File(apkPath.trim());
					if (uploadType.equals("1")) {
						FileUtil.copyInputStream(appApkFile.getInputStream(), apkFile);// apk本地上传
					} else {
						File destDir = new File(Constant.LOCAL_FILE_PATH + str);
						FileUtils.copyFileToDirectory(srcFile, destDir);
						// 移动到新目录后,重命名该文件
						File newFile = new File(Constant.LOCAL_FILE_PATH + str, apkName);
						if (newFile.isFile()) {
							newFile.renameTo(apkFile);
						}
					}

					appFile.setUrl(apkFile.getPath().substring(Constant.LOCAL_FILE_PATH.length(), apkFile.getPath().length()));
					if (uploadType.equals("1")) {
						appFile.setFileSize((int) appApkFile.getSize());
					} else {
						appFile.setFileSize((int) srcFile.length());
					}
					// 解析apk包
					try {
						analysisApk(appFile, request.getSession().getServletContext().getRealPath("/"));
					} catch (Exception e) {
						return "{\"flag\":\"5\"}"; // 解析包名出错
					}
					// 重命名文件
					apkName = FileUtil.getFileName(appFile);
					String url = FileUtil.renameFile(apkFile, apkName);
					if (StringUtils.isNotEmpty(url)) {
						appFile.setUrl(url.substring(Constant.LOCAL_FILE_PATH.length(), url.length()));
						S3Util.uploadS3(appFile.getUrl(), false);
					}

					AppFile conFile = null;
					List<AppFile> appFiles = appFileService.getAppFileByPacs(null, appId, channelId, null);
					if (appFiles != null && appFiles.size() > 0) {
						for (AppFile fil : appFiles) {
							if (!fil.getPackageName().equals(appFile.getPackageName())) {
								FileUtil.deleteFile(appFile.getUrl());
								return "{\"flag\":\"10\",\"pac\":\"" + "(" + name + ")" + appFile.getPackageName() + "\"}";
							} else if (fil.getPackageName().equals(appFile.getPackageName()) && fil.getRaveId() == appFile.getRaveId()) {
								conFile = fil;
								break;
							}
						}
					}
					if (conFile != null) {
						if (appFile.getPackageName().equals(conFile.getPackageName())) {
							if (appFile.getVersionName() != null && !appFile.getVersionName().equals(conFile.getVersionName()) && appFile.getVersionCode() > conFile.getVersionCode()) {
								// 有高版本更新, 写入文件历史表
								appFile.setHaslist(true);
								appFileListService.convertFile(conFile);
								createPack(appFile);
								// appFile.setUpdateInfo(StringUtil.convertBlankLinesToHashSymbolBr(appFile.getUpdateInfo()));
							} else {
								FileUtil.deleteFile(appFile.getUrl());
								// 低版本不能更新
								return "{\"flag\":\"1\",\"pac\":\"" + "(" + name + ")" + appFile.getPackageName() + "\"}";
							}
						}
					} else {
						createAppConfig(appFile);
					}
				} else {
					// 文件有问题
					return getError(appFile.getPackageName(), 2);
				}
			}
			// 设置更新时间
			appFile.setUpdateTime(DateUtil.StringToDate(updateTime1));
			try {
				appFileService.upAppFile(appFile);
			} catch (Exception e) {
				if (e instanceof DuplicateKeyException) {
					return "{\"flag\":\"6\",\"pac\":\"" + appFile.getPackageName() + "\"}";
				} else if (e instanceof DataIntegrityViolationException) {
					return "{\"flag\":\"7\"}";
				} else {
					return "{\"flag\":\"8\"}";
				}
			}

			// 更新分发
			appFileService.upAppCollectionRes(appFile);
			appFileService.upAppAlbumRes(appFile);
			// 更新分发temp表 add by dingjie
			appFileService.upAppAlbumResTemp(appFile);
			if (appFile.isState() == false) {
				appFileService.updateResourceByApkId(appFile.getId());
			}
			// 更新下载数
			appService.updateNum(appId);

		} catch (Exception e) {
			String name = "";
			if (StringUtil.chineseValid(appApkFile.getOriginalFilename())) {
				// 文件为中文随机生成名字
				name = RandNum.getRandNumStr() + "." + FileUtil.getFileSuffix(appApkFile.getOriginalFilename());
			} else {
				name = appApkFile.getOriginalFilename();
			}
			// 清除当前文件
			try {
				FileUtil.deleteFile(appFile.getUrl());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			log.error("MarkAppFileController update", e);
			return "{\"flag\":\"4\",\"pac\":\"" + "(" + name + ")" + appFile.getPackageName() + "\"}";
		}
		return getError(appFile.getPackageName(), 0);
	}

	/** 删除app文件 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		appFileService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}

	/** 获取历史文件 */
	@RequestMapping(value = "/fileList/{apkId}")
	public String fileList(HttpServletRequest request, @PathVariable("apkId") Integer apkId) {
		List<AppFileList> fileList = appFileListService.getAppFileLists(apkId);
		request.setAttribute("result", fileList);
		AppFile appFile = appFileService.getAppFile(apkId);
		if (appFile != null) {
			if (appFile.getAppInfo() != null) {
				request.setAttribute("appId", appFile.getAppInfo().getId());
			}
		}
		return "markAppFile/fileList";
	}

	/** 删除app文件 */
	@ResponseBody
	@RequestMapping("/deleteFile")
	public String deleteFile(@RequestParam("id") String ids) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		appFileListService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}

	// 获取文件名
	public String getFileName(MultipartFile appApkFile) {
		String name = "";
		if (StringUtil.chineseValid(appApkFile.getOriginalFilename())) {
			// 文件为中文随机生成名字
			name = RandNum.getRandNumStr() + "." + FileUtil.getFileSuffix(appApkFile.getOriginalFilename());
		} else {
			name = appApkFile.getOriginalFilename();
		}
		return name;
	}

	// 获取文件名
	public String getFileName(String apkName) {
		String name = "";
		if (StringUtil.chineseValid(apkName)) {
			// 文件为中文随机生成名字
			name = RandNum.getRandNumStr() + ".apk";
		} else {
			name = apkName;
		}
		return name;
	}

	// 解析apk包
	public void analysisApk(AppFile appFile, String url) {
		// 解析apk包
		ApkManifestParser parser = new ApkManifestParser();
		parser.parse(Constant.LOCAL_FILE_PATH + appFile.getUrl());
		appFile.setPackageName(parser.getPackageName());
		appFile.setVersionCode(parser.getVersionCode());
		if (StringUtils.isEmpty(parser.getVersionName()) || parser.getVersionName().contains("@7F")) {
			url = url + "WEB-INF" + File.separator + "classes" + File.separator;
			if ("linux".equalsIgnoreCase(Constant.RUN_ENV)) {
				appFile.setVersionName(ReadApkInfo.getVersionName(Constant.LOCAL_FILE_PATH + appFile.getUrl(), url, 2));
			} else {
				appFile.setVersionName(ReadApkInfo.getVersionName(Constant.LOCAL_FILE_PATH + appFile.getUrl(), url, 1));
			}
		} else {
			appFile.setVersionName(parser.getVersionName());
		}

	}

	// 错误返回
	public String getError(String name, int flag) {
		return "{\"flag\":\"" + flag + "\",\"pac\":\"" + flag + "\"}";
	}

	@ResponseBody
	@RequestMapping("/ftpUrl")
	public Map<String, String> queryFtpFilesURL(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		String apkPath = request.getParameter("apkPath");
		try {
			// 输入路径为空,则取系统默认路径
			if (StringUtils.isEmpty(apkPath)) {
				Resource rs = new ClassPathResource("config/resource.properties");
				Properties props = PropertiesLoaderUtils.loadProperties(rs);
				apkPath = props.getProperty("apk.ftp.path", FTP_APK_PATH);
			}
			if (StringUtils.isNotEmpty(apkPath)) {
				getFilePath(apkPath, map);
			}
		} catch (Exception e) {
			log.error("read apk resource failed !", e);
		}
		return map;
	}

	/**
	 * 遍历文件目录,取得所有apk的名称和路径
	 * 
	 * @param path
	 * @param map
	 */
	public void getFilePath(String path, Map<String, String> map) {
		File file = new File(path);
		File[] files = file.listFiles();
		if (null != files && files.length > 0) {
			for (File f : files) {
				// 如果目录下还有子目录,递归遍历
				if (f.isDirectory()) {
					getFilePath(f.getAbsolutePath(), map);
				} else {
					if (f.getName().endsWith(".apk")) {
						map.put(f.getName().substring(0, f.getName().lastIndexOf(".apk")), f.getPath());
					}
				}
			}
		}
	}

	// 生成差异包
	public void createPack(AppFile appFile) {
		// 是否需要生成差异包
		AppInfoConfig config = appInfoConfigService.getAppConfig(appFile.getAppName(), Constant.INCREMENT);
		if (appFile.getType() == Constant.INCREMENT || config != null) {
			// patch task
			AppFilePatchTaskService.addAppFilePatchTask(appFile, appFileListService, appFilePatchService);

			// 生成配置信息
			if (config == null) {
				config = new AppInfoConfig();
				config.setName(appFile.getAppName());
				config.setType(Constant.INCREMENT);
				config.setState(true);
				appInfoConfigService.addAppInfoConfig(config);
			}
		}
	}

	// 生成差异包
	public void createAppConfig(AppFile appFile) {
		if (appFile.getType() == Constant.INCREMENT) {
			// 是否需要生成差异包
			AppInfoConfig config = appInfoConfigService.getAppConfig(appFile.getAppName(), Constant.INCREMENT);
			// 生成配置信息
			if (config == null) {
				config = new AppInfoConfig();
				config.setName(appFile.getAppName());
				config.setType(Constant.INCREMENT);
				config.setState(true);
				appInfoConfigService.addAppInfoConfig(config);
			}
		}
	}
}
