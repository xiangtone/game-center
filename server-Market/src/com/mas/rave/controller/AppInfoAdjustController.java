package com.mas.rave.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.main.vo.Category;
import com.mas.rave.main.vo.Country;
import com.mas.rave.service.AppAlbumColumnService;
import com.mas.rave.service.AppAlbumResTempService;
import com.mas.rave.service.AppFileService;
import com.mas.rave.service.AppInfoService;
import com.mas.rave.service.AppPicService;
import com.mas.rave.service.CategoryService;
import com.mas.rave.service.ChannelService;
import com.mas.rave.service.CountryService;
import com.mas.rave.service.CpService;
import com.mas.rave.util.DateUtil;
import com.mas.rave.util.StringUtil;

/**
 * 公用app fatherChannelId=100000,channelId固定在100003,cpId=300001）
 * 
 * @author liwei.sz
 * 
 */
@Controller
@RequestMapping("/appAdjust")
public class AppInfoAdjustController {
	Logger log = Logger.getLogger(AppInfoAdjustController.class);
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
			AppInfo appAdjust = new AppInfo();

			if (StringUtils.isNotEmpty(name)) {
				name = name.trim();
				try {
					appAdjust.setId(Integer.parseInt(name));
				} catch (Exception e) {
					// name = "%" + name + "%";
					appAdjust.setName(name);
				}
			}
			if (StringUtils.isNotEmpty(packageName)) {
				appAdjust.setPackageName(packageName.trim());
			}
			if (StringUtils.isNotEmpty(countryId)) {
				try {
					appAdjust.setCountryId(Integer.parseInt(countryId));
				} catch (Exception e) {
					appAdjust.setCountryId(0);
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
				appAdjust.setCategory(category);
			} else if (StringUtils.isNotEmpty(categroyId) && !categroyId.equals("0")) {
				Category category = new Category();
				category.setId(Integer.parseInt(categroyId));
				appAdjust.setCategory(category);
				appAdjust.setCategory_parent1(Integer.parseInt(categroyId));
			} else if (StringUtils.isNotEmpty(category_parent) && !category_parent.equals("0")) {
				appAdjust.setCategory_parent(Integer.parseInt(category_parent));
			}
			// communal.setFree(0);
			appAdjust.setFree(-2); // 表示除=1外的

			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<AppInfo> result = appService.searchApps(appAdjust, currentPage, pageSize);
			request.setAttribute("result", result);
			// 所有一级分类信息
			List<Category> categorys = categoryService.getCategorys(1);
			request.setAttribute("categorys", categorys);
			// 所有国家
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);
		} catch (Exception e) {
			log.error("AppInfoAdjustController list", e);
			PaginationVo<AppInfo> result = new PaginationVo<AppInfo>(null, 0, 10, 1);
			request.setAttribute("result", result);

		}
		return "appAdjust/list";
	}

	/** 查看详细 */
	@RequestMapping("/info/{id}")
	public String info(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			AppInfo appAdjust = appService.getApp(id);
			if (appAdjust.getAnotherName() != null) {
				appAdjust.setAnotherName(StringUtil.convertHashSymbolToBlankLines(appAdjust.getAnotherName()));
			}
			model.addAttribute("appAdjust", appAdjust);
		} catch (Exception e) {
			log.error("AppInfoAdjustController info", e);
		}
		return "appAdjust/info";
	}

	/** 查看详细 */
	@ResponseBody
	@RequestMapping("/update/{id}")
	public String update(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			String free = request.getParameter("free");
			String initialReleaseDate = request.getParameter("initialReleaseDate");
			String source = request.getParameter("source");
			AppInfo appAdjust = appService.getApp(id);
			if (appAdjust != null) {
				if (StringUtils.isNotEmpty(free)) {
					appAdjust.setFree(Integer.parseInt(free));
				}
				if (StringUtils.isNotEmpty(initialReleaseDate)) {
					appAdjust.setInitialReleaseDate(DateUtil.StringToDate1(initialReleaseDate));
				}
				if (StringUtils.isNotEmpty(source)) {
					appAdjust.setSource(source);
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
			log.error("AppInfoAdjustController info", e);
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

}
