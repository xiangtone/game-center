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
import com.mas.rave.main.vo.AppAlbum;
import com.mas.rave.main.vo.AppAlbumColumn;
import com.mas.rave.main.vo.AppAlbumTheme;
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.Country;
import com.mas.rave.service.AppAlbumColumnService;
import com.mas.rave.service.AppAlbumService;
import com.mas.rave.service.AppAlbumThemeService;
import com.mas.rave.service.AppFileService;
import com.mas.rave.service.CountryService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.RandNum;
import com.mas.rave.util.s3.S3Util;

/**
 * app对应主题业务处理　
 * 
 * @author liwei.sz
 * 
 */
@Controller
@RequestMapping("/appAlbumTheme")
public class AppAlbumThemeController {
	Logger log = Logger.getLogger(AppAlbumThemeController.class);
	@Autowired
	private AppAlbumThemeService appAlbumThemeService;

	@Autowired
	private AppAlbumColumnService appAlbumColumnService;

	@Autowired
	private AppFileService appFileService;

	@Autowired
	private AppAlbumService appAlbumService;

	@Autowired
	private CountryService countryService;

	/**
	 * 分布查看app主题信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
			String str = request.getParameter("name");

			String appAlbumId = request.getParameter("appAlbumId");
			String appAlbumColumnId = request.getParameter("appAlbumColumnId");
			AppAlbumThemeService.AppAlbumThemeCriteria criteria = new AppAlbumThemeService.AppAlbumThemeCriteria();
			if (StringUtils.isNotEmpty(str)) {
				criteria.nameLike(str.trim());
			}
			String raveId = request.getParameter("raveId");

			if (StringUtils.isNotEmpty(raveId) && !raveId.equals("0")) {
				try {
					criteria.raveIdEqual(Integer.parseInt(raveId));
				} catch (Exception e) {
					log.error("ImageInfoController list raveId", e);

				}
			}
			criteria.flagEqual(1);
			// 如果一级
			if (StringUtils.isNotEmpty(appAlbumId) && !appAlbumId.equals("0")) {

				if ((StringUtils.isNotEmpty(appAlbumColumnId) && !appAlbumColumnId.equals("0"))) {
					criteria.appAlbumColumnIdEqualTo(Integer.parseInt(appAlbumColumnId));

				} else {
					List<AppAlbumColumn> appAlbumColumns = appAlbumColumnService.getAppAlbumColumnsByAppAlbumId(Integer.parseInt(appAlbumId));
					if (appAlbumColumns != null && appAlbumColumns.size() > 0) {
						int[] appAlbumColumnIds = new int[appAlbumColumns.size()];

						for (int i = 0; i < appAlbumColumns.size(); i++) {
							appAlbumColumnIds[i] = appAlbumColumns.get(i).getColumnId();
						}
						criteria.appAlbumIdEqualTo(appAlbumColumnIds);
					} else {
						criteria.appAlbumColumnIdEqualTo(0);

					}

				}
			}
			// 获取app主题顶级分类 @add by jieding
			List<AppAlbum> appAlbums = appAlbumService.getAppAlbum();
			request.setAttribute("appAlbums", appAlbums);
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<AppAlbumTheme> result = appAlbumThemeService.searchAppAlbumTheme(criteria, currentPage, pageSize,0);
			request.setAttribute("result", result);
		} catch (Exception e) {
			log.error("AppAlbumThemeController list", e);
			PaginationVo<AppAlbumTheme> result = new PaginationVo<AppAlbumTheme>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "appAlbumTheme/list";
	}

	/** 判断主题名是否已经存在 */
	@ResponseBody
	@RequestMapping(value = "/judgeThemeNameExist", method = RequestMethod.POST)
	public boolean judgeThemeNameExist(AppAlbumTheme criteria) {
		List<AppAlbumTheme> listapp = appAlbumThemeService.selectByThemeName(criteria.getName());
		if (criteria.getThemeId() == 0) {
			if (listapp.size() != 0) {
				return false;// 主题名称已经存在
			}
		} else {
			if (listapp.size() > 1 || (listapp.size() == 1 && listapp.get(0).getThemeId() != criteria.getThemeId())) {
				return false;// 主题名称已经存在
			}
		}
		return true;
	}

	/** 判断主题中文名是否已经存在 */
	@ResponseBody
	@RequestMapping(value = "/judgeThemeNameCnExist", method = RequestMethod.POST)
	public boolean judgeThemeNameCnExist(AppAlbumTheme criteria) {
		List<AppAlbumTheme> listapp = appAlbumThemeService.selectByThemeNameCn(criteria.getNameCn());
		if (criteria.getThemeId() == 0) {
			if (listapp.size() != 0) {
				return false;// 主题中文名称已经存在
			}
		} else {
			if (listapp.size() > 1 || (listapp.size() == 1 && listapp.get(0).getThemeId() != criteria.getThemeId())) {
				return false;// 主题中文名称已经存在
			}
		}
		return true;
	}

	/** 新增app主题信息 */
	@RequestMapping("/add")
	public String showAdd(HttpServletRequest request) {
		try {
			// 所有分类信息
			List<AppAlbum> appAlbums = appAlbumService.getAppAlbum();
			request.setAttribute("appAlbums", appAlbums);
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);
			// 文件列表
			// List<AppFile> appFiles = appFileService.getAppFiles();
			// request.setAttribute("appFiles", appFiles);

		} catch (Exception e) {
			log.error("AppAlbumThemeController showAdd", e);
		}
		return "appAlbumTheme/add";
	}

	/** 新增app主题信息页 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute AppAlbumTheme record, @RequestParam Integer appAlbumColumnId, @RequestParam Integer raveId, @RequestParam Integer apkId, @RequestParam MultipartFile bigApkFile,
			@RequestParam MultipartFile apkFile) {
		try {
			List<String> imgUrlList = new ArrayList<String>();// s3 list
			// 设置分类
			AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(appAlbumColumnId);
			record.setAppAlbumColumn(appAlbumColumn);

			Country country = countryService.getCountry(raveId);
			record.setCountry(country);
			// 设置文件
			AppFile appFile = appFileService.getAppFile(apkId);
			record.setAppFile(appFile);
			// 文件目录分类id/apkId
			String str = FileAddresUtil.getTitlePath(appAlbumColumnId, apkId, Constant.TITLE_ADR);
			if (bigApkFile.getSize() != 0) {
				// 获取后缀,检测是否非法文件
				String suffix = FileAddresUtil.getSuffix(bigApkFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					File pic1 = new File(Constant.LOCAL_FILE_PATH + str + "big", RandNum.randomFileName(bigApkFile.getOriginalFilename()));

					FileUtil.copyInputStream(bigApkFile.getInputStream(), pic1);
					record.setBigicon(FileAddresUtil.getFilePath(pic1));
					imgUrlList.add(record.getBigicon());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			if (apkFile.getSize() != 0) {
				// 获取后缀,检测是否非法文件
				String suffix = FileAddresUtil.getSuffix(apkFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					File pic1 = new File(Constant.LOCAL_FILE_PATH + str + "small", RandNum.randomFileName(apkFile.getOriginalFilename()));

					FileUtil.copyInputStream(apkFile.getInputStream(), pic1);
					record.setIcon(FileAddresUtil.getFilePath(pic1));
					imgUrlList.add(record.getIcon());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			appAlbumThemeService.addAppAlbumTheme(record);
			S3Util.uploadS3(imgUrlList, false);
		} catch (Exception e) {
			log.error("AppAlbumThemeController add", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 加载单个app主题信息 */
	@RequestMapping("/{id}")
	public String edit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			AppAlbumTheme appAlbumTheme = appAlbumThemeService.getAppAlbumTheme(id);
			model.addAttribute("appAlbumTheme", appAlbumTheme);
			// 获取app主题顶级分类 @add by jieding
			List<AppAlbum> appAlbums = appAlbumService.getAppAlbum();
			request.setAttribute("appAlbums", appAlbums);
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);
			request.setAttribute("page", request.getParameter("page"));
			request.setAttribute("raveId", request.getParameter("raveId"));
			request.setAttribute("appAlbumId",request.getParameter("appAlbumId"));
			request.setAttribute("appAlbumColumnId",request.getParameter("appAlbumColumnId"));
			// app分发Id
			int appAlbumId = appAlbumTheme.getAppAlbumColumn().getAppAlbum().getId();

			// 所有分类信息
			List<AppAlbumColumn> appAlbumColumns = appAlbumColumnService.getAppAlbumColumnsByAppAlbumId(appAlbumId);
			request.setAttribute("appAlbumColumns", appAlbumColumns);

			// 文件列表
			// List<AppFile> appFiles = appFileService.getAppFiles();
			// request.setAttribute("appFiles", appFiles);
		} catch (Exception e) {
			log.error("AppAlbumThemeController edit", e);
		}
		return "appAlbumTheme/edit";
	}

	/** 更新app主题信息 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute AppAlbumTheme record, @RequestParam Integer appAlbumColumnId, @RequestParam Integer raveId, @RequestParam String apkId,
			@RequestParam MultipartFile bigApkFile, @RequestParam MultipartFile apkFile) {
		try {
			List<String> imgUrlList = new ArrayList<String>();// s3 list
			// 设置分类
			AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(appAlbumColumnId);
			record.setAppAlbumColumn(appAlbumColumn);
			Country country = countryService.getCountry(raveId);
			record.setCountry(country);
			// 设置文件
			AppFile appFile = appFileService.getAppFile(Integer.parseInt(apkId));
			record.setAppFile(appFile);
			// 文件目录分类id/apkId
			String str = FileAddresUtil.getTitlePath(appAlbumColumnId, Integer.parseInt(apkId), Constant.TITLE_ADR);
			if (bigApkFile.getSize() != 0) {
				// 获取后缀,检测是否非法文件
				String suffix = FileAddresUtil.getSuffix(bigApkFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					FileUtil.deleteFile(record.getBigicon());
					File pic1 = new File(Constant.LOCAL_FILE_PATH + str + "big", RandNum.randomFileName(bigApkFile.getOriginalFilename()));

					FileUtil.copyInputStream(bigApkFile.getInputStream(), pic1);
					record.setBigicon(FileAddresUtil.getFilePath(pic1));
					imgUrlList.add(record.getBigicon());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			if (apkFile.getSize() != 0) {
				// 获取后缀,检测是否非法文件
				String suffix = FileAddresUtil.getSuffix(apkFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					FileUtil.deleteFile(record.getIcon());
					File pic1 = new File(Constant.LOCAL_FILE_PATH + str + "small", RandNum.randomFileName(apkFile.getOriginalFilename()));

					FileUtil.copyInputStream(apkFile.getInputStream(), pic1);
					record.setIcon(FileAddresUtil.getFilePath(pic1));
					imgUrlList.add(record.getIcon());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			appAlbumThemeService.upAppAlbumTheme(record);
			S3Util.uploadS3(imgUrlList, false);
		} catch (Exception e) {
			log.error("AppAlbumThemeController update", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 删除app分类 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		appAlbumThemeService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}

	/** 查看详细 */
	@ResponseBody
	@RequestMapping("/getColumn")
	public String getColumn(HttpServletRequest request) {
		String str = "0";
		try {
			String id = request.getParameter("id");
			if (StringUtils.isNotEmpty(id)) {
				AppAlbumColumnService.AppAlbumColumnCriteria criteria = new AppAlbumColumnService.AppAlbumColumnCriteria();
				if (StringUtils.isNotEmpty(str)) {
					criteria.albumIdEquaTo(Integer.parseInt(id));
				}
				List<AppAlbumColumn> columns = appAlbumColumnService.searchAppAlbumColumn(criteria);
				if (columns != null && columns.size() > 0) {
					for (AppAlbumColumn column : columns) {
						str += "," + column.getName() + "-" + column.getColumnId();
					}
				}
			}
		} catch (Exception e) {
			log.error("AppInfoController info", e);
		}
		return "{\"success\":\"" + str + "\"}";
	}

	/** 文件检测 */
	@ResponseBody
	@RequestMapping("/checkFile")
	public String checkFile(HttpServletRequest request) {
		try {
			String file = request.getParameter("appFileName");
			String raveIdStr = request.getParameter("raveId");
			if (StringUtils.isNotEmpty(file)) {
				int raveId = 1;
				if (StringUtils.isNotEmpty(raveIdStr)) {
					raveId = Integer.parseInt(raveIdStr);
				}
				// 文件列表
				AppFileService.AppFileCriteria criteria = new AppFileService.AppFileCriteria();
				criteria.appNameLike(file);
				criteria.raveIdEqual(raveId);
				List<AppFile> appFiles = appFileService.searchAppFiles(criteria);
				request.setAttribute("appFiles", appFiles);
				if (CollectionUtils.isEmpty(appFiles)) {
					return "{\"success\":\"0\"}";
				} else {
					StringBuilder options = new StringBuilder();
					for (AppFile appFile : appFiles) {
						if (appFile.getAppInfo() != null) {
							// 如果开发者为未审核应用则无需显示
							if ((appFile.getAppInfo().getFree() == 3 && appFile.getCpState() == 1 && appFile.isState() == true) || (appFile.isState() == true && appFile.getAppInfo().getFree() != 3)) {
								options.append("<input type='radio' name='apkId' id='apkId' value='");
								options.append(appFile.getId());
								options.append(",");
								options.append(appFile.getAppName());
								options.append("'/>");
								options.append(appFile.getAppName() + " --> " + appFile.getCountry().getName() + "(" + appFile.getCountry().getNameCn() + ")"+ " --> "+appFile.getChannel().getName()+"("+appFile.getChannel().getId()+")");
								options.append("</br>");
							}
						}
					}
					return "{\"success\":\"1\",\"option\":\"" + options.toString() + "\"}";
				}
			}
		} catch (Exception e) {
			log.error("AppInfoController checkFile", e);
		}
		return "{\"success\":\"1\",\"option\":\"" + 0 + "\"}";
	}
}
