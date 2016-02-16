package com.mas.rave.service.impl;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.MasApkManifestParser;
import com.mas.rave.exception.ResParserException;
import com.mas.rave.exception.ServiceException;
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.main.vo.AppPic;
import com.mas.rave.main.vo.Channel;
import com.mas.rave.main.vo.Cp;
import com.mas.rave.service.AppBatchUploadService;
import com.mas.rave.service.AppFileListService;
import com.mas.rave.service.AppFilePatchService;
import com.mas.rave.service.AppFileService;
import com.mas.rave.service.AppInfoService;
import com.mas.rave.service.AppPicService;
import com.mas.rave.service.ChannelService;
import com.mas.rave.service.CpService;
import com.mas.rave.task.AppFilePatchTaskService;
import com.mas.rave.util.CheckFileUtil;
import com.mas.rave.util.Constant;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.MD5;
import com.mas.rave.util.PicUtil;
import com.mas.rave.util.RandNum;
import com.mas.rave.util.StringUtil;
import com.mas.rave.vo.AppResourceVO;

@Service(value = "appBatchUpload")
public class AppBatchUploadServiceImpl implements AppBatchUploadService {
	Logger logger = Logger.getLogger(AppBatchUploadServiceImpl.class);

	@Autowired
	private AppFileService appFileService;

	@Autowired
	private AppFileListService appFileListService;

	@Autowired
	private AppInfoService appService;

	@Autowired
	private CpService cpService;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private AppPicService appPicService;

	@Autowired
	private AppFilePatchService appFilePatchService;

	public void saveResource(AppResourceVO appResourceVO) throws ServiceException {
		if (logger.isDebugEnabled()) {
			logger.debug("[saveResource]appResourceVO:" + appResourceVO);
		}
		try {
			checkDataValid(appResourceVO);

			// 对应cp信息
			Cp cp = cpService.getCp(appResourceVO.getCpId());
			if (cp == null) {
				throw new ServiceException("应用[" + appResourceVO.getAppName() + "]的CP信息在数据库中不存在");
			}

			// 获取对应渠道信息
			Channel channel = channelService.getChannel(100000);
			if (channel == null) {
				throw new ServiceException("应用[" + appResourceVO.getAppName() + "]的channel信息在数据库中不存在");
			}

			// 保存appinfo信息
			AppInfo appInfo = doSaveAppInfo(appResourceVO, channel, cp);
			if (appInfo.isChecked()) {
				appResourceVO.setReplaceType(1);
			}

			// 获取对应渠道信息
			Channel subChannel = channelService.getChannel(100003);
			if (subChannel == null) {
				throw new ServiceException("应用[" + appResourceVO.getAppName() + "]的channel信息在数据库中不存在");
			}
			// 保存apk文件信息
			AppFile appFile = doSaveAppFile(appResourceVO, subChannel, cp, appInfo);
			if (appFile.isChecked()) {
				appResourceVO.setUpType(1);
			}
			// 保存截图信息
			doSaveAppPic(appResourceVO, subChannel, cp, appInfo);
		} catch (Throwable e) {
			throw new ServiceException(e.getMessage(), e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("[saveResource]执行完毕");
		}
	}

	public void checkDataValid(AppResourceVO appResourceVO) throws ServiceException {
		/*
		 * AppInfo appInfo =
		 * appService.findAppInfo(appResourceVO.getFatherChannelId(),
		 * appResourceVO.getAppName()); if (appInfo != null) { throw new
		 * ServiceException("应用[" + appResourceVO.getAppName() +
		 * "]在app_info表已经存在"); }
		 */

		// logo图片检测
		File[] logoFile = getUploadSrcFile(appResourceVO, Constant.LOGO_ADR);
		if (logoFile == null || logoFile.length != 1) {
			throw new ServiceException("应用[" + appResourceVO.getAppName() + "]下存在多张logo图");
		}

		// apk文件检测
		File[] srcFile = getUploadSrcFile(appResourceVO, Constant.APK_ADR);
		if (srcFile == null || srcFile.length != 1) {
			throw new ResParserException("应用[" + appResourceVO.getAppName() + "]下存在多个APK资源");
		} else {
			analysisApk(appResourceVO, srcFile[0]);
		}

		// 是否有更新包
		AppFile appFile = appFileService.finAppFile(100003, appResourceVO.getPackageName());
		if (appFile != null) {
			if (appFile.getPackageName().equals(appResourceVO.getPackageName())) {
				if (appResourceVO.getVersionCode() <= appFile.getVersionCode()) {
					throw new ServiceException("应用[" + appResourceVO.getAppName() + "]在app_file表已经存在,重复文件" + appFile.getPackageName() + "," + appFile.getAppName());
				}
			}
		}

		// 处理相同应用，不同包
		appFile = appFileService.finAppFileByName(100003, appResourceVO.getAppName());
		if (appFile != null && !appFile.getPackageName().equals(appResourceVO.getPackageName())) {
			throw new ServiceException("应用[" + appResourceVO.getAppName() + "]在app_info表已经存在");
		}

		File[] picFiles = getUploadSrcFile(appResourceVO, Constant.IMG_ADR);
		if (picFiles == null || picFiles.length <= 0) {
			throw new ServiceException("应用[" + appResourceVO.getAppName() + "]下存异常");
		}
		// 描述检测
		getUploadSrcFile(appResourceVO, Constant.APP_UPDATEINFO_ADR);
	}

	public AppInfo doSaveAppInfo(AppResourceVO appResourceVO, Channel channel, Cp cp) throws ServiceException {
		if (logger.isDebugEnabled()) {
			logger.debug("[saveAppInfo]appResourceVO:" + appResourceVO + ",channel:" + channel + ",cp:" + cp);
		}
		AppInfo appInfo = new AppInfo();
		appInfo.setChannel(channel);
		appInfo.setCp(cp);
		appInfo.setName(appResourceVO.getAppName());
		appInfo.setFree(appResourceVO.getFree());
		appInfo.setBrief(appResourceVO.getBrief());
		appInfo.setInitDowdload(appResourceVO.getInitDowdload());
		File[] describeFile = getUploadSrcFile(appResourceVO, Constant.APP_DESCRIBE_ADR);
		if (describeFile == null || describeFile.length == 0) {
			throw new ServiceException("应用[" + appResourceVO.getAppName() + "]没有描述信息");
		}

		String desc = FileUtil.readFileContent(describeFile[0]);
		if (StringUtils.isEmpty(desc) || desc.length() > 5000) {
			throw new ServiceException("应用[" + appResourceVO.getAppName() + "]描述信息超过5000字符");
		}
		appInfo.setDescription(desc);

		appInfo.setState(appResourceVO.getState() == 0 ? false : true);
		appInfo.setSort(appResourceVO.getSort());
		appInfo.setStars(appResourceVO.getStars());
		appInfo.setOsType(appResourceVO.getOsType());

		// 组装文件地址
		String dynamicDir = FileAddresUtil.getFilePath(channel.getProvince().getId(), cp.getId(), channel.getId(), 0, Constant.LOGO_ADR);
		if (logger.isDebugEnabled()) {
			logger.debug("dynamicDir:" + dynamicDir);
		}
		File[] logoFiles = getUploadSrcFile(appResourceVO, Constant.LOGO_ADR);
		if (logoFiles == null) {
			throw new ServiceException("应用[" + appResourceVO.getAppName() + "]logo资源不合法");
		}

		// 生成服务端的相对路径
		String logoName = "", bigLogoName = "";
		for (File f : logoFiles) {
			if (f.getName().startsWith("small_")) {
				logoName = RandNum.randomFileName(f.getName());
				String relativeLogoPath = getUploadDestFile(dynamicDir, logoName);
				appInfo.setLogo(relativeLogoPath);
				appResourceVO.setLogo(relativeLogoPath);
			} else {
				bigLogoName = RandNum.randomFileName(f.getName());
				String relativeBigLogoPath = getUploadDestFile(dynamicDir, bigLogoName);
				appInfo.setBigLogo(relativeBigLogoPath);
				appResourceVO.setBigLogo(relativeBigLogoPath);
			}
		}

		// 设置密码
		String pwd = RandNum.randomPwd(6);
		appInfo.setPwd(RandNum.randomPwd(6));
		appInfo.setPassword(MD5.getMd5Value(pwd));

		if (logger.isDebugEnabled()) {
			logger.debug("============== appInfo:" + appInfo);
		}

		// 检测包是否已经存在当前目录,同一个包不同应用
		AppFile appFile = appFileService.finAppFile(100003, appResourceVO.getPackageName());
		if (appFile != null) {
			// 有当前文件
			AppInfo appInfo1 = appService.getApp(appFile.getAppId());
			if (appInfo1 != null) {
				appInfo1.setChecked(true);
				if (StringUtils.isNotEmpty(appInfo.getBigLogo())) {
					appInfo1.setBigLogo(appInfo.getBigLogo());
				}
				if (StringUtils.isNotEmpty(appInfo.getLogo())) {
					appInfo1.setLogo(appInfo.getLogo());
				}
				appInfo1.setDescription(appInfo.getDescription());
				appService.upApp(appInfo1, appResourceVO.getCategoryId());
				// 通知更新分发
				appService.upAppAlbumRes(appInfo1);
				return appInfo1;
			} else {
				throw new ServiceException("应用[" + appResourceVO.getAppName() + "]不存在");
			}
		}
		AppInfo appInfos = appService.findAppInfo(100000, appResourceVO.getAppName());
		if (appInfos != null) {
			appInfo.setId(appInfos.getId());
			appInfo.setChecked(true);
			appService.upApp(appInfo, appResourceVO.getCategoryId());
			// 通知更新分发
			appService.upAppAlbumRes(appInfo);
		} else {
			appService.addApp(appInfo, appResourceVO.getCategoryId());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("========== 保存appinfo sucess=============");
		}
		return appInfo;
	}

	public AppFile doSaveAppFile(AppResourceVO appResourceVO, Channel channel, Cp cp, AppInfo appInfo) throws ServiceException {
		if (logger.isDebugEnabled()) {
			logger.debug("[saveAppFile]appResourceVO:" + appResourceVO + ",channel:" + channel + ",cp:" + cp + "appInfo:" + appInfo);
		}
		AppFile appFile = new AppFile();

		appFile.setAppInfo(appInfo);
		appFile.setChannel(channel);
		appFile.setCp(cp);

		// 组装文件地址
		String dynamicDir = FileAddresUtil.getFilePath(channel.getProvince().getId(), appResourceVO.getCpId(), channel.getId(), appInfo.getId(), Constant.APK_ADR);
		if (appInfo.getFree() == 1) {
			// 平台
			dynamicDir += Constant.LOCAL_APK_MARKET;

		}
		if (logger.isDebugEnabled()) {
			logger.debug("dynamicDir:" + dynamicDir);
		}

		File[] apkFiles = getUploadSrcFile(appResourceVO, Constant.APK_ADR);
		File srcFile = apkFiles[0];

		// 生成服务端的相对路径
		String apkFileName = generateNewFileName(srcFile);
		String relativeApkPath = getUploadDestFile(dynamicDir, apkFileName);

		appFile.setUrl(relativeApkPath);
		appFile.setAppName(appInfo.getName());
		appFile.setApkKey(MD5.getMd5Value(RandNum.getRandNumStr()));
		appFile.setServerId(1);
		appFile.setFileSize((int) srcFile.length());
		appFile.setLanguage(appResourceVO.getLanguage());
		appFile.setState(appResourceVO.getState() == 0 ? false : true);

		appFile.setPackageName(appResourceVO.getPackageName());
		appFile.setVersionCode(appResourceVO.getVersionCode());
		appFile.setVersionName(appResourceVO.getVersionName());

		appResourceVO.setApkUrl(relativeApkPath);

		File[] updateInfoFiles = getUploadSrcFile(appResourceVO, Constant.APP_UPDATEINFO_ADR);
		if (updateInfoFiles != null && updateInfoFiles.length > 0) {
			File updateInfoFile = updateInfoFiles[0];
			String updateInfo = FileUtil.readFileContent(updateInfoFile);
			if (StringUtils.isNotEmpty(updateInfo) && updateInfo.length() > 5000) {
				throw new ServiceException("应用[" + appResourceVO.getAppName() + "]更新信息超过5000字符");
			}
			appFile.setUpdateInfo(updateInfo);
		}
		// 解析apk包
		// analysisApk(appFile,srcFile);
		List<AppFile> app = appFileService.getAppFileByPacs(appFile.getPackageName(), appInfo.getId(), channel.getId(), 1);
		if (app != null && app.size() > 0) {
			if (appFile.getPackageName().equals(app.get(0).getPackageName())) {
				if (appFile.getVersionCode() > app.get(0).getVersionCode()) {
					// 有高版本更新, 写入文件历史表
					appFileListService.convertFile(app.get(0));
					appFile.setId(app.get(0).getId());
					appFile.setHaslist(true);
					appFileService.upAppFile(appFile);
					// 更新分发
					appFileService.upAppAlbumRes(appFile);
					appFile.setChecked(true);
					// patch task
					AppFilePatchTaskService.addAppFilePatchTask(appFile, appFileListService, appFilePatchService);
				}
			}
		} else {
			// 无记录保存数据
			appFileService.addAppFile(appFile);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("=========保存appFile成功=========");
		}
		return appFile;
	}

	public void doSaveAppPic(AppResourceVO appResourceVO, Channel channel, Cp cp, AppInfo appInfo) throws ServiceException {
		if (logger.isDebugEnabled()) {
			logger.debug("[saveAppPic]appResourceVO:" + appResourceVO);
		}
		File[] picFiles = getUploadSrcFile(appResourceVO, Constant.IMG_ADR);
		if (picFiles == null || picFiles.length == 0) {
			throw new ServiceException("应用[" + appResourceVO.getAppName() + "]pic资源不合法");
		}
		String dynamicDir = FileAddresUtil.getFilePath(channel.getProvince().getId(), appResourceVO.getCpId(), channel.getId(), appInfo.getId(), Constant.IMG_ADR);
		if (logger.isDebugEnabled()) {
			logger.debug("dynamicDir:" + dynamicDir);
		}

		delPic(appInfo.getId());

		for (File srcFile : picFiles) {
			AppPic pic = new AppPic();
			pic.setAppInfo(appInfo);
			pic.setTitle(appResourceVO.getPicTitle());
			pic.setDescription(appResourceVO.getPicDesc());
			// pic.setState(true);
			pic.setState(appResourceVO.getState() == 0 ? false : true);

			// 生成服务端相对路径
			String picFileName = RandNum.randomFileName(srcFile.getName());
			String relativePath = getUploadDestFile(dynamicDir, picFileName);
			pic.setUrl(relativePath);

			String[] infos = PicUtil.getPic(srcFile);
			pic.setWidth(Integer.parseInt(infos[1]));
			pic.setLength(Integer.parseInt(infos[2]));
			pic.setFileSize(Integer.valueOf(infos[0]));
			if (logger.isDebugEnabled()) {
				logger.debug("pic:" + pic);
			}
			appPicService.addAppPic(pic);
			appResourceVO.getPicUrls().put(srcFile.getName(), relativePath);
		}
		if (logger.isDebugEnabled()) {
			logger.debug(" ========= 保存截图成功 =========");
		}
	}

	public File[] getUploadSrcFile(AppResourceVO appResourceVO, String recType) throws ServiceException {
		// String resourceUploadPath = Constant.RESOURCE_UPLOAD_PATH;
		String resourceUploadPath = Constant.FTP_UPLOAD_PATH == "" ? Constant.RESOURCE_UPLOAD_PATH : Constant.FTP_UPLOAD_PATH;
		resourceUploadPath += appResourceVO.getType() + File.separator + appResourceVO.getAppName();
		CheckFileUtil check = new CheckFileUtil();
		if (recType.equals(Constant.APK_ADR)) {
			resourceUploadPath += "/apk/";
			check.setType(Constant.APK_ADR);
		} else if (recType.equals(Constant.LOGO_ADR)) {
			resourceUploadPath += "/logo/";
			check.setType(Constant.LOGO_ADR);
		} else if (recType.equals(Constant.IMG_ADR)) {
			resourceUploadPath += "/pic/";
			check.setType(Constant.IMG_ADR);
		} else if (recType.equals(Constant.APP_DESCRIBE_ADR)) {
			resourceUploadPath += "/description/";
			check.setType(Constant.APP_TXT);
		} else if (recType.equals(Constant.APP_UPDATEINFO_ADR)) {
			resourceUploadPath += "/updateInfo/";
			check.setType(Constant.APP_TXT);
			File f = new File(resourceUploadPath);
			if (f.exists()) {
				File[] files = f.listFiles();
				if (files != null && files.length > 0) {
					File updateInfoFile = files[0];
					String updateInfo = FileUtil.readFileContent(updateInfoFile);
					if (StringUtils.isNotEmpty(updateInfo) && updateInfo.length() > 5000) {
						throw new ServiceException("应用[" + appResourceVO.getAppName() + "]更新信息超过5000字符");
					} else {
						return files;
					}
				}
			}
			return null;
		} else {
			logger.error("[getUploadSrcFile]不支持的文件上传类型");
			throw new ServiceException("不支持的文件上传类型，请检查");
		}
		File f = new File(resourceUploadPath);
		if (!f.exists()) {
			throw new ServiceException("路径[" + resourceUploadPath + "]不存在，请检查");
		}

		File[] files = f.listFiles();
		if (files == null || files.length == 0) {
			throw new ServiceException("路径[" + resourceUploadPath + "]下不存在文件，请检查");
		} else {
			for (File file : files) {
				if (!check.accept(file, file.getName())) {
					file.delete();
					// throw new ServiceException("路径[" + resourceUploadPath +
					// "]下存在异常文件，请检查");
				}
			}
		}
		// return f.listFiles();
		return f.listFiles();
	}

	// 生成新的文件名
	private String generateNewFileName(File appApkFile) {
		String name = "";
		if (StringUtil.chineseValid(appApkFile.getName())) {
			// 文件为中文随机生成名字
			name = RandNum.getRandNumStr() + "." + FileUtil.getFileSuffix(appApkFile.getName());
		} else {
			name = appApkFile.getName();
		}
		return name;
	}

	// 解析apk包
	private void analysisApk(AppResourceVO resourceVo, File apkFile) {
		// 解析apk包
		MasApkManifestParser parser = new MasApkManifestParser();
		parser.parse(apkFile);
		resourceVo.setPackageName(parser.getPackageName());
		resourceVo.setVersionCode(parser.getVersionCode());
		resourceVo.setVersionName(parser.getVersionName());
	}

	private String getUploadDestFile(String dynamicDir, String resourceName) {
		if (resourceName.startsWith("/")) {
			return dynamicDir + resourceName.substring(1);
		} else {
			return dynamicDir + resourceName;
		}
	}

	// 清空当前图片
	public void delPic(int appId) {
		List<AppPic> pics = appPicService.getAppPics(appId);
		if (pics != null && pics.size() > 0) {
			for (AppPic pic : pics) {
				appPicService.delAppPic(pic.getId());
			}
		}
	}

}
