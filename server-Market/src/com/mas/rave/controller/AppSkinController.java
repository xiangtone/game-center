package com.mas.rave.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.main.vo.ClientSkin;
import com.mas.rave.main.vo.ClientSkinCode;
import com.mas.rave.service.ClientSkinCodeService;
import com.mas.rave.service.ClientSkinService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.DateUtil;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.RandNum;
import com.mas.rave.util.s3.S3Util;
import com.molon.android.parser.ApkManifestParser;

/**
 * app皮肤
 * 
 * @author liwei.sz
 * 
 */

@Controller
@RequestMapping("/appSkin")
public class AppSkinController {
	Logger log = Logger.getLogger(AppSkinController.class);

	@Autowired
	private ClientSkinCodeService clientSkinCodeService;

	@Autowired
	private ClientSkinService clientSkinService;

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
			ClientSkin skin = new ClientSkin();
			if (StringUtils.isNotEmpty(name)) {
				name = name.trim();
				// name = "%" + name + "%";
				skin.setSkinName(name);
			}
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<ClientSkin> result = clientSkinService.searchClientSkin(skin, currentPage, pageSize);
			request.setAttribute("result", result);

			ClientSkinCode code = clientSkinCodeService.selectByExample();
			request.setAttribute("code", code);
		} catch (Exception e) {
			log.error("AppInfoController list", e);
			PaginationVo<AppInfo> result = new PaginationVo<AppInfo>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "appSkin/list";
	}

	/** 新增app皮肤信息页 */
	@RequestMapping("/add")
	public String showAdd(HttpServletRequest request) {
		return "appSkin/add";
	}

	/** 新增app皮肤信息 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(HttpServletRequest request, @ModelAttribute ClientSkin skin) {
		try {
			if (StringUtils.isEmpty(skin.getApkFileUrl()) && StringUtils.isEmpty(skin.getLogoUrl())) {
				return getMsg("1", null, null);
			}

			List<String> fileUrlList = new ArrayList<String>();// s3 list
			// 上传logo
			String suffix = FileAddresUtil.getSuffix(skin.getLogoUrl());
			if (suffix.equals(Constant.IMG_ADR)) {
				File sourceFile = new File(Constant.LOCAL_FILE_PATH + skin.getLogoUrl());
				File targerFile = new File(Constant.LOCAL_FILE_PATH + FileAddresUtil.getSkinPath(1), RandNum.randomFileName(sourceFile.getName()));
				FileUtil.copyInputStream(new FileInputStream(sourceFile), targerFile);// logo本地上传
				skin.setLogo(FileAddresUtil.getFilePath(targerFile));
				fileUrlList.add(skin.getLogo());
			} else {
				return getMsg("2", null, null);
			}
			
			//上传apk
			File sourceFile = new File(Constant.LOCAL_FILE_PATH + skin.getApkFileUrl());
			File targerFile = new File(Constant.LOCAL_FILE_PATH + FileAddresUtil.getSkinPath(2), RandNum.randomFileName(sourceFile.getName()));
			FileUtil.copyInputStream(new FileInputStream(sourceFile), targerFile);// apk本地上传
			skin.setApkUrl(FileAddresUtil.getFilePath(targerFile));
			skin.setApkSize((int) targerFile.length());
			renameApk(skin, request, targerFile);

			fileUrlList.add(skin.getApkUrl());

			clientSkinService.insert(skin);
			// s3upload
			S3Util.uploadS3(fileUrlList, false);

			delFile(skin.getLogoUrl(), skin.getApkFileUrl());
		} catch (Exception e) {
			try {
				delFile(skin.getLogoUrl(), skin.getApkFileUrl());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log.error("AppSkinController add", e);

			return getMsg("3", null, null);
		}
		return getMsg("0", null, null);
	}

	/** 加载单个app皮肤 */
	@RequestMapping("/{id}")
	public String showEdit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			ClientSkin skin = clientSkinService.selectByPrimaryKey(id);
			model.addAttribute("skin", skin);
		} catch (Exception e) {
			log.error("AppSkinController edit", e);
		}
		return "appSkin/edit";
	}

	/** 更新app皮肤 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(ClientSkin skin, HttpServletRequest request) {
		try {
			List<String> fileUrlList = new ArrayList<String>();// s3 list
			if (StringUtils.isNotEmpty(skin.getLogoUrl())) {
				File sourceFile = new File(Constant.LOCAL_FILE_PATH + skin.getLogoUrl());
				File targerFile = new File(Constant.LOCAL_FILE_PATH + FileAddresUtil.getSkinPath(1), RandNum.randomFileName(sourceFile.getName()));
				FileUtil.copyInputStream(new FileInputStream(sourceFile), targerFile);// logo本地上传

				// 清除原有图片
				FileUtil.deleteFile(skin.getLogo());
				skin.setLogo(FileAddresUtil.getFilePath(targerFile));
				fileUrlList.add(skin.getLogo());
			}

			if (StringUtils.isNotEmpty(skin.getApkFileUrl())) {
				File sourceFile = new File(Constant.LOCAL_FILE_PATH + skin.getApkFileUrl());
				File targerFile = new File(Constant.LOCAL_FILE_PATH + FileAddresUtil.getSkinPath(2), RandNum.randomFileName(sourceFile.getName()));
				FileUtil.copyInputStream(new FileInputStream(sourceFile), targerFile);// apk本地上传
				// 清除原有apk
				FileUtil.deleteFile(skin.getApkUrl());
				skin.setApkUrl(FileAddresUtil.getFilePath(targerFile));
				skin.setApkSize((int) targerFile.length());
				fileUrlList.add(skin.getApkUrl());

				renameApk(skin, request, targerFile);

				fileUrlList.add(skin.getApkUrl());
			}
			clientSkinService.updateByPrimaryKey(skin);
			// s3 upload
			S3Util.uploadS3(fileUrlList, false);

			delFile(skin.getLogoUrl(), skin.getApkFileUrl());
		} catch (Exception e) {
			try {
				delFile(skin.getLogoUrl(), skin.getApkFileUrl());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log.error("AppSkinController update", e);
			return getMsg("3", null, null);
		}
		return getMsg("0", null, null);
	}

	/** 删除app */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids, Model model) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		clientSkinService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}

	/** 查看详细 */
	@RequestMapping("/skinCode")
	public String skinCode(HttpServletRequest request, Model model) {
		try {
			clientSkinCodeService.updateTime(1);
			ClientSkinCode code = clientSkinCodeService.selectByExample();
			model.addAttribute("code", code);
		} catch (Exception e) {
			log.error("AppSkinController info", e);
		}
		return "redirect:/appSkin/list/";
	}

	/** 新增app文件信息页 */
	@ResponseBody
	@RequestMapping("/uploadFile")
	public void uploadFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("flag") String flag) {
		PrintWriter pw = null;
		String res = "";
		try {
			pw = response.getWriter();
			MultipartHttpServletRequest mhs = (MultipartHttpServletRequest) request;
			MultipartFile file = mhs.getFile("file");
			if (file != null) {
				File apkFile = null;
				if (flag.equals("1")) {
					String suffix = FileUtil.getFileSuffix(file.getOriginalFilename());
					apkFile = new File(Constant.LOCAL_FILE_PATH + DateUtil.getTime() + File.separator + FileAddresUtil.getSkinPath(2) + RandNum.getRandNumStr() + "." + suffix);
				} else {
					apkFile = new File(Constant.LOCAL_FILE_PATH + DateUtil.getTime() + File.separator + FileAddresUtil.getSkinPath(1) + RandNum.randomFileName(file.getName()));
				}
				FileUtil.copyInputStream(file.getInputStream(), apkFile);// apk本地上传
				String msg = apkFile.getPath().substring(Constant.LOCAL_FILE_PATH.length(), apkFile.getPath().length());
				String pac = "";
				if (flag.equals("1")) {
					pac = analysisApkInfo(1, msg);
					ClientSkin clientSkin = clientSkinService.selectByPackName(pac);
					if (clientSkin == null) {
						pac = null;
					} else {
						// 文件存在及时删除
						FileUtil.deleteFile(msg);
					}
				}
				res = getMsg("1", msg, pac);
			} else {
				res = getMsg("2", null, null);
			}
		} catch (Exception e) {
			log.error("uploadFile error ", e);
			res = getMsg("2", null, null);
		}
		pw.println(res);
	}

	public String getMsg(String flag, String msg, String pac) {
		JSONObject obj = new JSONObject();
		obj.put("flag", flag);
		obj.put("msg", msg);
		obj.put("pac", pac);
		return obj.toString();
	}

	// 解析apk包
	public void analysisApk(ClientSkin skin, String url) {
		// 解析apk包
		ApkManifestParser parser = new ApkManifestParser();
		parser.parse(Constant.LOCAL_FILE_PATH + skin.getApkUrl());
		skin.setPackageName(parser.getPackageName());
		skin.setVersionCode(parser.getVersionCode());
		if (StringUtils.isEmpty(parser.getVersionName()) || parser.getVersionName().contains("@7F")) {
			url = url + "WEB-INF" + File.separator + "classes" + File.separator;
			if ("linux".equalsIgnoreCase(Constant.RUN_ENV)) {
				skin.setVersionName(com.mas.rave.util.ReadApkInfo.getVersionName(Constant.LOCAL_FILE_PATH + skin.getApkUrl(), url, 2));
			} else {
				skin.setVersionName(com.mas.rave.util.ReadApkInfo.getVersionName(Constant.LOCAL_FILE_PATH + skin.getApkUrl(), url, 1));
			}
		} else {
			skin.setVersionName(parser.getVersionName());
		}
	}

	public String renameApk(ClientSkin skin, HttpServletRequest request, File targerFile) throws Exception {
		// 解析apk包
		try {
			analysisApk(skin, request.getSession().getServletContext().getRealPath("/"));
		} catch (Exception e) {
			return getMsg("3", null, null);
		}
		// 文件重命名
		String apkName = getFileName(skin);
		String url = renameFile(targerFile, apkName);
		if (StringUtils.isNotEmpty(url)) {
			skin.setApkUrl(url.substring(Constant.LOCAL_FILE_PATH.length(), url.length()));
		}
		return null;
	}

	public String getFileName(ClientSkin skin) {
		return skin.getPackageName() + "_" + skin.getVersionName() + "_" + skin.getVersionCode();
	}

	public void delFile(String url1, String url2) throws IOException {
		FileUtil.deleteFile(url1);
		FileUtil.deleteFile(url2);
	}

	public static String analysisApkInfo(int flag, String url) {
		// 解析apk包
		ApkManifestParser parser = new ApkManifestParser();
		parser.parse(Constant.LOCAL_FILE_PATH + url);
		if (flag == 1) {
			// 获取包名
			return parser.getPackageName();
		} else if (flag == 2) {
			return parser.getVersionCode() + "";
		}
		return null;

	}

	/**
	 * 重命名文件或文件夹
	 * 
	 * @param resFilePath
	 *            源文件路径
	 * @param newFileName
	 *            重命名
	 * @return 操作成功标识
	 */
	public static String renameFile(File file, String newFileName) throws Exception {
		// String c = ;
		StringBuffer url = new StringBuffer();
		url.append(file.getParent());
		url.append(File.separator);
		url.append(newFileName);
		String suffix = FileUtil.getFileSuffix(file.getName());
		url.append("." + suffix);
		File newFile = new File(url.toString());
		boolean bol = file.renameTo(newFile);
		if (bol) {
			return newFile.getPath();
		} else {
			// 开始移动文件
			FileUtils.copyFile(file, newFile);
			// 清除当前文件
			FileUtil.deleteFile(file.getPath());
			return newFile.getPath();
		}
	}
}
