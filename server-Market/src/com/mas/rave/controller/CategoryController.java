package com.mas.rave.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.Category;
import com.mas.rave.main.vo.Country;
import com.mas.rave.service.AppInfoService;
import com.mas.rave.service.CategoryService;
import com.mas.rave.service.CountryService;
import com.mas.rave.service.ImageInfoService;
import com.mas.rave.service.MarketService;
import com.mas.rave.service.MusicInfoService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.RandNum;

/**
 * 分类业务处理　
 * 
 * @author liwei.sz
 * 
 */

@Controller
@RequestMapping("/category")
public class CategoryController {
	Logger log = Logger.getLogger(CategoryController.class);
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private MarketService marketService;

	@Autowired
	private AppInfoService appInfoService;
	@Autowired
	private MusicInfoService musicInfoService;
	@Autowired
	private ImageInfoService imageInfoService;

	@Autowired
	private CountryService countryService;

	/**
	 * 分页查看app分类信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
			// 获取父级id
			String str = request.getParameter("faterId");

			// 一级分类
			String firstId = request.getParameter("firstId");

			// 二级分类
			String secondId = request.getParameter("secondId");
			// 获取名字
			String name = request.getParameter("name");
			// 获取 状态
			String state = request.getParameter("state");
			String raveId = request.getParameter("raveId");
			Category category = new Category();
			if (StringUtils.isNotEmpty(secondId) && !secondId.equals("0")) {
				// 获取二级分类ＩＤ
				category.setId(Integer.parseInt(secondId));
			} else if (StringUtils.isNotEmpty(firstId) && !firstId.equals("0")) {
				// 获取一级分类ＩＤ
				category.setFatherId(Integer.parseInt(firstId));
			} else if (StringUtils.isNotEmpty(str) && !str.equals("0")) {
				category.setFatherId(Integer.parseInt(str));
			} else {
				// 设置不显示父级菜单
				category.setFatherId(2);
			}
			if (StringUtils.isNotEmpty(name)) {
				name = "%" + name.trim() + "%";
				category.setName(name);
			}

			// 设置默认状态为是.
			if (StringUtils.isEmpty(state)) {
				category.setState(true);
			} else {
				category.setState(Boolean.parseBoolean(state));
			}

			if (StringUtils.isEmpty(raveId)) {
				category.setRaveId(0);
			} else {
				category.setRaveId(Integer.parseInt(raveId));
			}

			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<Category> result = categoryService.searchCategorys(category, currentPage, pageSize);
			result = setCatetory(result);
			request.setAttribute("categorys", result);

			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);
			// 设置查询条件
			List<Category> categorys = categoryService.getCategorys(1);
			request.setAttribute("fatherIds", categorys);
		} catch (Exception e) {
			log.error("CategoryController list", e);
			PaginationVo<Category> result = new PaginationVo<Category>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "category/list";
	}

	/**
	 * 分页查看app分类信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/listSecond/{id}")
	public String listSecond(HttpServletRequest request, @PathVariable("id") Integer id) {
		try {
			// 获取父级id
			CategoryService.CategoryCriteria criteria = new CategoryService.CategoryCriteria();
			criteria.fatherIdEqualTo(id);

			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<Category> result = categoryService.searchCategorys(criteria, currentPage, pageSize);
			request.setAttribute("categorys", result);

			// 设置查询条件
			List<Category> categorys = categoryService.getCategorys(1);
			request.setAttribute("fatherIds", categorys);
		} catch (Exception e) {
			log.error("CategoryController listSecond", e);
		}
		return "category/listSecond";
	}

	/** 新增app分类信息页 */
	@RequestMapping("/add")
	public String showAdd(HttpServletRequest request) {
		List<Category> fatherIds = categoryService.getCategorys(1);
		request.setAttribute("fatherIds", fatherIds);

		List<Country> countrys = countryService.getCountrys();
		request.setAttribute("countrys", countrys);
		return "category/add";
	}

	/** 新增app分类信息 */
	/** 新增app分类信息 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(Category category, @RequestParam MultipartFile bigApkFile, @RequestParam MultipartFile apkFile) {
		try {
			// 文件目录分类id/apkId
			String str = FileAddresUtil.getTitlePath(category.getFatherId(), 0, Constant.CATEGORY_ADR);
			if (bigApkFile.getSize() != 0) {
				// 获取后缀,检测是否非法文件
				String suffix = FileAddresUtil.getSuffix(bigApkFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					File pic1 = new File(Constant.LOCAL_FILE_PATH + str + "big", RandNum.randomFileName(bigApkFile.getOriginalFilename()));

					FileUtil.copyInputStream(bigApkFile.getInputStream(), pic1);
					category.setBigicon(FileAddresUtil.getFilePath(pic1));
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
					category.setIcon(FileAddresUtil.getFilePath(pic1));
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			category.setLevel(1);
			categoryService.addCategory(category);
		} catch (Exception e) {
			try {
				FileUtil.deleteFile(category.getBigicon());
				FileUtil.deleteFile(category.getIcon());
			} catch (IOException e1) {
				log.error("CategoryController deleteFile", e1);
			}
			log.error("CategoryController addSecond", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 新增app分类信息页 */
	@RequestMapping("/addSecond")
	public String showAddSecond(HttpServletRequest request) {
		List<Category> fatherIds = categoryService.getCategorys(1);
		request.setAttribute("fatherIds", fatherIds);

		List<Country> countrys = countryService.getCountrys();
		request.setAttribute("countrys", countrys);
		return "category/addSecond";
	}

	/** 新增app分类信息 */
	@ResponseBody
	@RequestMapping(value = "/addSecond", method = RequestMethod.POST)
	public String addSecond(Category category, @RequestParam MultipartFile bigApkFile, @RequestParam MultipartFile apkFile) {
		try {
			// 文件目录分类id/apkId
			String str = FileAddresUtil.getTitlePath(category.getFatherId(), 0, Constant.CATEGORY_ADR);
			if (bigApkFile.getSize() != 0) {
				// 获取后缀,检测是否非法文件
				String suffix = FileAddresUtil.getSuffix(bigApkFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					File pic1 = new File(Constant.LOCAL_FILE_PATH + str + "big", RandNum.randomFileName(bigApkFile.getOriginalFilename()));

					FileUtil.copyInputStream(bigApkFile.getInputStream(), pic1);
					category.setBigicon(FileAddresUtil.getFilePath(pic1));
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
					category.setIcon(FileAddresUtil.getFilePath(pic1));
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			category.setFatherId(category.getFirstId());
			category.setLevel(2);
			// 将一级id设置为二级国家id
			Category cate = categoryService.getCategory(category.getFirstId());
			if (cate != null) {
				category.setRaveId(cate.getRaveId());
			} else {
				category.setRaveId(1);
			}
			categoryService.addCategory(category);
		} catch (Exception e) {
			try {
				FileUtil.deleteFile(category.getBigicon());
				FileUtil.deleteFile(category.getIcon());
			} catch (IOException e1) {
				log.error("CategoryController deleteFile", e1);
			}
			log.error("CategoryController addSecond", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 加载单个app分类 */
	@RequestMapping("/{id},{level}")
	public String edit(@PathVariable("id") Integer id, @PathVariable("level") Integer level, Model model) {
		try {
			Category category = categoryService.getCategory(id);
			List<Category> categorys = categoryService.getCategorys(1);
			model.addAttribute("categorys", categorys);
			List<Country> countrys = countryService.getCountrys();
			model.addAttribute("countrys", countrys);
			if (category != null && level == 2) {
				// 重新设置分类　
				Category first = categoryService.getCategory(category.getFatherId());
				category.setFirstId(first.getId());
				category.setFatherId(first.getFatherId());
				model.addAttribute("category", category);
				// 设置一级分类列表
				List<Category> firsts = categoryService.getCategorys(category.getFatherId());
				model.addAttribute("firsts", firsts);
				// 转到二级分类
				return "category/editSecond";
			} else {
				model.addAttribute("category", category);
			}
		} catch (Exception e) {
			log.error("CategoryController edit", e);
		}
		return "category/edit";
	}

	/** 加载单个app分类 */
	@RequestMapping("/categoryInfo/{id}")
	public String categoryInfo(@PathVariable("id") Integer id, Model model) {
		try {
			Category Category = categoryService.getCategory(id);
			model.addAttribute("category", Category);
		} catch (Exception e) {
			log.error("CategoryController categoryInfo", e);
		}
		return "category/categoryInfo";
	}

	/** 更新app分类 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(Category category, @RequestParam MultipartFile bigApkFile, @RequestParam MultipartFile apkFile) {
		try {
			// 文件目录分类id/apkId
			String str = FileAddresUtil.getTitlePath(category.getFatherId(), 0, Constant.CATEGORY_ADR);
			if (bigApkFile.getSize() != 0) {
				// 获取后缀,检测是否非法文件
				String suffix = FileAddresUtil.getSuffix(bigApkFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					FileUtil.deleteFile(category.getBigicon());
					File pic1 = new File(Constant.LOCAL_FILE_PATH + str + "big", RandNum.randomFileName(bigApkFile.getOriginalFilename()));

					FileUtil.copyInputStream(bigApkFile.getInputStream(), pic1);
					category.setBigicon(FileAddresUtil.getFilePath(pic1));
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			if (apkFile.getSize() != 0) {
				// 获取后缀,检测是否非法文件
				String suffix = FileAddresUtil.getSuffix(apkFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					FileUtil.deleteFile(category.getIcon());
					File pic1 = new File(Constant.LOCAL_FILE_PATH + str + "small", RandNum.randomFileName(apkFile.getOriginalFilename()));

					FileUtil.copyInputStream(apkFile.getInputStream(), pic1);
					category.setIcon(FileAddresUtil.getFilePath(pic1));
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			if (category.getLevel() == 2) {
				category.setFatherId(category.getFirstId());
			}
			categoryService.upCategory(category);
		} catch (Exception e) {
			log.error("CategoryController update", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 删除app分类 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids, Model model) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		for (Integer id : idIntArray) {
			Category category = categoryService.getCategory(id);
			int fatherId = category.getFatherId();
			if (fatherId == 2 || fatherId == 3) {
				// 应用或者游戏 查找t_app_info 中categoryId 是否已经被引用
				int count = appInfoService.getAppInfoCountByCategory(id);
				if (count > 0) {
					return "{\"flag\":\"1\"}";
				}
			}
			if (fatherId == 4) {
				// 铃声 查找t_res_music 中categoryId 是否已经被引用
				int count = musicInfoService.getMusicInfoCountByCategory(id);
				if (count > 0) {
					return "{\"flag\":\"1\"}";
				}
			}
			if (fatherId == 5) {
				// 墙纸 查找t_res_image 中category 是否已经被引用
				int count = imageInfoService.getImageInfoCountByCategory(id);
				if (count > 0) {
					return "{\"flag\":\"1\"}";
				}
			}
			if (category.getMarketInfoId() != 1) {
				return "{\"flag\":\"2\"}";
			}
		}

		categoryService.batchDelete(idIntArray);
		return "{\"flag\":\"0\"}";
	}

	@ResponseBody
	@RequestMapping("/getCategory")
	public String querySecondCategory(@RequestParam Integer id, @RequestParam Integer level) {
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
					if (level == 2) {
						options.append("(").append(category.getCateName()).append(")");
					}
					options.append("</option>");
				}

				return "{\"success\":\"1\",\"option\":\"" + options.toString() + "\",\"rave\":\"" + options.toString() + "\"}";
			}
		} catch (Exception e) {
			// TODO: handle exception
			return "{\"success\":\"2\"}";
		}
	}

	// 设置分类名
	public PaginationVo<Category> setCatetory(PaginationVo<Category> result) {
		if (result != null && result.getData().size() > 0) {
			// 取出集合
			List<Category> data = new ArrayList<Category>();
			for (Category category : result.getData()) {
				Category ca = categoryService.getCategory(category.getFatherId());
				if (ca != null) {
					category.setFirstName(ca.getName());
					data.add(category);
				}
			}
			PaginationVo<Category> result1 = new PaginationVo<Category>(data, result.getRecordCount(), result.getPageSize(), result.getCurrentPage());
			return result1;
		}
		return result;
	}
}
