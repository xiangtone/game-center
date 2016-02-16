package com.mas.rave.controller;

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

import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.AppAlbum;
import com.mas.rave.main.vo.AppAlbumColumn;
import com.mas.rave.main.vo.AppAlbumTheme;
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.main.vo.Country;
import com.mas.rave.service.AppAlbumColumnService;
import com.mas.rave.service.AppAlbumService;
import com.mas.rave.service.AppAlbumThemeService;
import com.mas.rave.service.AppFileService;
import com.mas.rave.service.AppInfoService;
import com.mas.rave.service.CountryService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;

/**
 * 开发者app主题信息
 * 
 * @author liwei.sz
 * 
 */
@Controller
@RequestMapping("/openBanner")
public class OpenBannerController {
	Logger log = Logger.getLogger(OpenBannerController.class);
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

	@Autowired
	private AppInfoService appService;

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
				criteria.nameLike(str);
			}
			String raveId = request.getParameter("raveId");

			if (StringUtils.isNotEmpty(raveId) && !raveId.equals("0")) {
				try {
					criteria.raveIdEqual(Integer.parseInt(raveId));
				} catch (Exception e) {
					log.error("ImageInfoController list raveId", e);

				}
			}
			criteria.flagEqual(3);
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

			PaginationVo<AppAlbumTheme> result = appAlbumThemeService.searchAppAlbumTheme(criteria, currentPage, pageSize,1);
			
			request.setAttribute("result", result);
		} catch (Exception e) {
			log.error("AppAlbumThemeController list", e);
			PaginationVo<AppAlbumTheme> result = new PaginationVo<AppAlbumTheme>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "openBanner/list";
	}

	/** 加载单个app主题信息 */
	@RequestMapping("/{id}")
	public String edit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			AppAlbumTheme openBanner = appAlbumThemeService.getappBanner(id);
			model.addAttribute("openBanner", openBanner);
			List<AppAlbumTheme> appAlbumThems = appAlbumThemeService.getBanners(3);
			request.setAttribute("appAlbumThems", appAlbumThems);
		} catch (Exception e) {
			log.error("AppAlbumThemeController edit", e);
		}
		return "openBanner/edit";
	}

	/** 更新app主题信息 */
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(HttpServletRequest request, @ModelAttribute AppAlbumTheme record, @RequestParam Integer appAlbumThemId) {
		try {
			String msg = "";
			// 获取app 信息
			AppInfo app = appService.getApp(record.getApkId());
			if (app != null) {
				List<AppFile> files = appFileService.getAppFiles(app.getId());
				if (files != null && files.size() > 0) {
					AppAlbumTheme themem = appAlbumThemeService.getAppAlbumTheme(appAlbumThemId);
					if (themem != null) {
						// 对应国家的广告
						AppFile file = null;
						for (AppFile fil : files) {
							//如果有对应的给对应无则给全球
							if (fil.getRaveId() == themem.getRaveId() || fil.getRaveId()==1) {
								file = fil;
								break;
							}
						}
						if (file != null) {
							if (file.isState() == true && file.getCpState() == 1) {
								// 清除文件
								FileUtil.deleteFile(themem.getBigicon());
								String str = FileAddresUtil.getTitlePath(themem.getThemeId(), file.getId(), Constant.TITLE_ADR) + "big";
								String path = FileUtil.copyFile(Constant.LOCAL_FILE_PATH + record.getBigicon(), Constant.LOCAL_FILE_PATH + str);
								themem.setBigicon(path.substring(Constant.LOCAL_FILE_PATH.length(), path.length()));

								themem.setApkId(file.getId());
								themem.setAppFile(file);
								appAlbumThemeService.upAppAlbumTheme(themem);
							} else {
								msg = "{\"flag\":\"2\",\"msg\":\"未审核勇冠的应用不能替换\"}";
							}
						} else {
							// 没有对应国家的广告图
							msg = "{\"flag\":\"2\",\"msg\":\"对应apk文件不存在\"}";
						}

					} else {
						msg = "{\"flag\":\"2\",\"msg\":\"无效的广告图\"}";
					}
				} else {
					msg = "{\"flag\":\"2\",\"msg\":\"app无对应apk文件\"}";
				}

			} else {
				msg = "{\"flag\":\"2\",\"msg\":\"无效的app信息\"}";
			}
			if (StringUtils.isNotEmpty(msg)) {
				return msg;
			}
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
}
