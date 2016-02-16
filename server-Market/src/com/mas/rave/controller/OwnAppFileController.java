package com.mas.rave.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.analysisapk.util.ReadApkInfo;
import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.OwnAppFile;
import com.mas.rave.main.vo.OwnAppFileList;
import com.mas.rave.main.vo.OwnAppInfo;
import com.mas.rave.service.OwnAppFileListService;
import com.mas.rave.service.OwnAppFilePatchService;
import com.mas.rave.service.OwnAppFileService;
import com.mas.rave.service.OwnAppInfoService;
import com.mas.rave.task.OwnAppFilePatchTaskService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.MD5;
import com.mas.rave.util.RandNum;
import com.mas.rave.util.StringUtil;
import com.mas.rave.util.s3.S3Util;
import com.molon.android.parser.ApkManifestParser;

/**
 * app文件业务处理createAppConfig
 * 
 * @author liwei.sz
 * 
 */

@Controller
@RequestMapping("/ownAppFile")
public class OwnAppFileController {

	Logger log = Logger.getLogger(OwnAppFileController.class);

	@Autowired
	private OwnAppInfoService ownAppService;

	@Autowired
	private OwnAppFileService ownAppFileService;

	@Autowired
	private OwnAppFileListService ownAppFileListService;

	@Autowired
	private OwnAppFilePatchService ownAppFilePatchService;

	/**
	 * 分页查看app文件信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list/{appId}")
	public String list(HttpServletRequest request, @PathVariable("appId") Integer appId) {
		try {
			List<OwnAppFile> result = ownAppFileService.getOwnAppFiles(appId);
			request.setAttribute("result", result);
			OwnAppInfo appInfo = ownAppService.getOwnApp(appId);
			request.setAttribute("appInfo", appInfo);
		} catch (Exception e) {
			log.error("OwnAppFileController list", e);
			PaginationVo<AppFile> result = new PaginationVo<AppFile>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "ownAppFile/list";
	}

	/** 新增app文件信息页 */
	@RequestMapping("/showAdd/{appId}")
	public String showAdd(HttpServletRequest request, @PathVariable("appId") Integer appId) {
		try {
			OwnAppInfo appInfo = ownAppService.getOwnApp(appId);
			request.setAttribute("appInfo", appInfo);
		} catch (Exception e) {
			log.error("OwnAppFileController showAdd", e);
		}
		return "ownAppFile/add";
	}

	/** 新增app文件信息页 */
	@ResponseBody
	@RequestMapping(value = "/add/{appId}", method = RequestMethod.POST)
	public String add(@ModelAttribute OwnAppFile appFile, @RequestParam Integer appId, @RequestParam MultipartFile appApkFile, HttpServletRequest request) {
		try {
			if (appApkFile.getSize() == 0) {
				// log.info("apk file is empty ! upload failed !");
				// 文件有问题
				return getError(appFile.getPackageName(), 2);
			}
			OwnAppInfo appInfo = ownAppService.getOwnApp(appId);
			appFile.setAppInfo(appInfo);
			// 获取后缀
			String suffix = FileAddresUtil.getSuffix(appApkFile.getOriginalFilename());
			if (suffix.equals(Constant.APK_ADR)) {
				// 当文件名为了中文时随机生成名字
				String name = RandNum.getRandNumStr() + ".apk";
				File apkFile = new File(Constant.LOCAL_FILE_PATH + Constant.LOCAL_BBS_PATH, name);
				FileUtil.copyInputStream(appApkFile.getInputStream(), apkFile);// apk本地上传
				appFile.setUrl(apkFile.getPath().substring(Constant.LOCAL_FILE_PATH.length(), apkFile.getPath().length()));
				appFile.setAppName(appInfo.getName());

				appFile.setServerId(1);
				appFile.setFileSize((int) appApkFile.getSize());
				// 解析apk包
				try {
					analysisApk(appFile, request.getSession().getServletContext().getRealPath("/"));
				} catch (Exception e) {
					return "{\"flag\":\"5\"}"; // 解析包名出错
				}
				// 文件重命名
				String apkName = FileUtil.getFileName1(appFile);
				String url = FileUtil.renameFile(apkFile, apkName);
				if (StringUtils.isNotEmpty(url)) {
					appFile.setUrl(url.substring(Constant.LOCAL_FILE_PATH.length(), url.length()));
					S3Util.uploadS3(appFile.getUrl(), false);
				}

				appFile.setApkKey(MD5.getMd5Value(200001 + 300001 + appFile.getPackageName()));

				HashMap<String, Object> map = new HashMap<String, Object>(2);
				map.put("packageName", appFile.getPackageName());
				List<OwnAppFile> appFiles = ownAppFileService.getByParameter(map);
				OwnAppFile appFile1 = null;
				if (appFiles != null && appFiles.size() > 0) {
					for (OwnAppFile fil : appFiles) {
						if (!fil.getPackageName().equals(appFile.getPackageName())) {
							FileUtil.deleteFile(appFile.getUrl());
							return "{\"flag\":\"10\",\"pac\":\"" + "(" + name + ")" + appFile.getPackageName() + "\"}";
						} else if (fil.getPackageName().equals(appFile.getPackageName())) {
							appFile1 = fil;
							break;
						}
					}
				}

				if (appFile1 != null) {
					if (appFile.getPackageName().equals(appFile1.getPackageName())) {
						if (appFile.getVersionName() != null && !appFile.getVersionName().equals(appFile1.getVersionName()) && appFile.getVersionCode() > appFile1.getVersionCode()) {
							// 有高版本更新, 写入文件历史表
							ownAppFileListService.convertFile(appFile1);

							// 设置参数并入库
							appFile1.setFileSize(appFile.getFileSize());
							appFile1.setPackageName(appFile.getPackageName());
							appFile1.setVersionCode(appFile.getVersionCode());
							appFile1.setVersionName(appFile.getVersionName());
							appFile1.setUrl(appFile.getUrl());
							appFile1.setHaslist(true);
							appFile1.setType(appFile.getType());
							appFile1.setUpdateInfo(appFile.getUpdateInfo());
							ownAppFileService.upOwnAppFile(appFile1);

							createPack(appFile);
							return "{\"flag\":\"3\",\"pac\":\"" + "(" + appFile1.getAppName() + ")" + appFile1.getPackageName() + "\"}";
						} else {
							return "{\"flag\":\"1\",\"pac\":\"" + "(" + name + ")" + appFile.getPackageName() + "\"}";
						}
					}
				} else {
					createPack(appFile);
				}
				try {
					ownAppFileService.addOwnAppFile(appFile);
				} catch (Exception e) {
					e.printStackTrace();
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
			// } else {
			// // 文件有问题
			// return getError(appFile.getPackageName(), 2);
			// }
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
			log.error("AppFileController add", e);
			return "{\"flag\":\"4\",\"pac\":\"" + "(" + name + ")" + appFile.getPackageName() + "\"}";
		}
		return getError(appFile.getPackageName(), 0);
	}

	/** 加载单个app文件 */
	@RequestMapping("/edit/{id}")
	public String edit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			OwnAppFile appFile = ownAppFileService.selectByPrimaryKey(id);
			appFile.setUpdateInfo(StringUtil.convertBlankLinesToHashSymbolBrTo(appFile.getUpdateInfo()));
			model.addAttribute("appFile", appFile);
		} catch (Exception e) {
			log.error("AppFileController edit", e);
		}
		return "ownAppFile/edit";
	}

	/** 更新app文件 */
	@ResponseBody
	@RequestMapping(value = "/update/{appId}", method = RequestMethod.POST)
	public String update(@ModelAttribute OwnAppFile appFile, @PathVariable("appId") Integer appId, @RequestParam MultipartFile appApkFile, HttpServletRequest request) {
		try {
			OwnAppInfo appInfo = ownAppService.getOwnApp(appId);
			appFile.setAppInfo(appInfo);
			if (appApkFile.getSize() != 0) {
				// 获取后缀
				String suffix = FileAddresUtil.getSuffix(appApkFile.getOriginalFilename());
				if (suffix.equals(Constant.APK_ADR)) {

					// 当文件名为了中文时随机生成名字
					String name = RandNum.getRandNumStr() + ".apk";
					File apkFile = new File(Constant.LOCAL_FILE_PATH + Constant.LOCAL_BBS_PATH, name);
					FileUtil.copyInputStream(appApkFile.getInputStream(), apkFile);// apk本地上传
					appFile.setUrl(apkFile.getPath().substring(Constant.LOCAL_FILE_PATH.length(), apkFile.getPath().length()));
					appFile.setFileSize((int) appApkFile.getSize());
					// 解析apk包
					try {
						analysisApk(appFile, request.getSession().getServletContext().getRealPath("/"));
					} catch (Exception e) {
						return "{\"flag\":\"5\"}"; // 解析包名出错
					}
					// 文件重命名
					String apkName = FileUtil.getFileName1(appFile);
					String url = FileUtil.renameFile(apkFile, apkName);
					if (StringUtils.isNotEmpty(url)) {
						appFile.setUrl(url.substring(Constant.LOCAL_FILE_PATH.length(), url.length()));
						S3Util.uploadS3(appFile.getUrl(), false);
					}

					OwnAppFile conFile = null;
					HashMap<String, Object> map = new HashMap<String, Object>(2);
					map.put("packageName", appFile.getPackageName());
					List<OwnAppFile> appFiles = ownAppFileService.getByParameter(map);
					if (appFiles != null && appFiles.size() > 0) {
						for (OwnAppFile fil : appFiles) {
							if (!fil.getPackageName().equals(appFile.getPackageName())) {
								FileUtil.deleteFile(appFile.getUrl());
								return "{\"flag\":\"10\",\"pac\":\"" + "(" + name + ")" + appFile.getPackageName() + "\"}";
							} else if (fil.getPackageName().equals(appFile.getPackageName())) {
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
								ownAppFileListService.convertFile(conFile);
								createPack(appFile);
								appFile.setUpdateInfo(StringUtil.convertBlankLinesToHashSymbolBr(appFile.getUpdateInfo()));
							} else {
								FileUtil.deleteFile(appFile.getUrl());
								// 低版本不能更新
								return "{\"flag\":\"1\",\"pac\":\"" + "(" + name + ")" + appFile.getPackageName() + "\"}";
							}
						}
					} else {
						createPack(appFile);
					}
				} else {
					// 文件有问题
					return getError(appFile.getPackageName(), 2);
				}
			}
			try {
				ownAppFileService.upOwnAppFile(appFile);
			} catch (Exception e) {
				if (e instanceof DuplicateKeyException) {
					return "{\"flag\":\"6\",\"pac\":\"" + appFile.getPackageName() + "\"}";
				} else if (e instanceof DataIntegrityViolationException) {
					return "{\"flag\":\"7\"}";
				} else {
					return "{\"flag\":\"8\"}";
				}
			}

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
				if (appApkFile.getSize() != 0) {
					FileUtil.deleteFile(appFile.getUrl());
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			log.error("AppFileController update", e);
			return "{\"flag\":\"4\",\"pac\":\"" + "(" + name + ")" + appFile.getPackageName() + "\"}";
		}
		return getError(appFile.getPackageName(), 0);
	}

	/** 删除app文件 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		ownAppFileService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}

	/** 获取历史文件 */
	@RequestMapping(value = "/fileList/{apkId}")
	public String fileList(HttpServletRequest request, @PathVariable("apkId") Integer apkId) {
		List<OwnAppFileList> fileList = ownAppFileListService.getOwnAppFileLists(apkId);
		request.setAttribute("result", fileList);
		OwnAppFile appFile = ownAppFileService.selectByPrimaryKey(apkId);
		if (appFile != null) {
			if (appFile.getAppInfo() != null) {
				request.setAttribute("appId", appFile.getAppInfo().getId());
			}
		}
		return "ownAppFile/fileList";
	}

	/** 删除app文件 */
	@ResponseBody
	@RequestMapping("/deleteFile")
	public String deleteFile(@RequestParam("id") String ids) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		ownAppFileListService.batchDelete(idIntArray);
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
	public void analysisApk(OwnAppFile appFile, String url) {
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

	// 生成差异包
	public void createPack(OwnAppFile appFile) {
		if (appFile.getType() == 2) {
			OwnAppFilePatchTaskService.addAppFilePatchTask(appFile, ownAppFileListService, ownAppFilePatchService);
		}
	}

}
