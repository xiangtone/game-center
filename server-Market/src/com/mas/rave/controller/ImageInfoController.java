package com.mas.rave.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
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

import com.mas.rave.appannie.ProxyService;
import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.Category;
import com.mas.rave.main.vo.Country;
import com.mas.rave.main.vo.ImageInfo;
import com.mas.rave.service.CategoryService;
import com.mas.rave.service.CountryService;
import com.mas.rave.service.ImageInfoService;
import com.mas.rave.service.MarketService;
import com.mas.rave.service.UserService;
import com.mas.rave.task.WallpapersTask;
import com.mas.rave.util.Constant;
import com.mas.rave.util.CrawlerPaperAndRingtoneUtils;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.MD5;
import com.mas.rave.util.PinyinToolkit;
import com.mas.rave.util.RandNum;
import com.mas.rave.util.StringUtil;
import com.mas.rave.util.s3.S3Util;

/**
 * music业务处理
 * 
 * @author jieding
 * 
 */
@Controller
@RequestMapping("/image")
public class ImageInfoController {
	private static String basicUrl = "http://na.voga360.com";
	private static String morepageUrl = "http://na.server.voga360.com/category/wallpapers/morePic.htm?";
	public static boolean isImageBatchAddThreadRun = false;
	private final String FTP_APK_PATH = "/home/ftpapk/";
	Logger log = Logger.getLogger(ImageInfoController.class);

	@Autowired
	private ImageInfoService imageInfoService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private MarketService marketService;
	@Autowired
	private UserService userService;

	@Autowired
	private CountryService countryService;

	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
			String name = request.getParameter("name");
			// 获取 状态
			String state = request.getParameter("state");

			String categroyId = request.getParameter("categoryId");

			String categoryId1 = request.getParameter("categoryId1");
			String raveId = request.getParameter("raveId");

			ImageInfo imageInfo = new ImageInfo();
			if (StringUtils.isNotEmpty(name)) {
				// name = "%" + name + "%";
				imageInfo.setName(name.trim());
			}
			// 设置默认状态为是.
			if (StringUtils.isEmpty(state)) {
				imageInfo.setState(true);
			} else {
				imageInfo.setState(Boolean.parseBoolean(state));
			}
			if (StringUtils.isNotEmpty(categoryId1) && !categoryId1.equals("0")) {
				Category category = new Category();
				category.setId(Integer.parseInt(categoryId1));
				imageInfo.setCategory(category);
			} else if (StringUtils.isNotEmpty(categroyId) && !categroyId.equals("0")) {
				Category category = new Category();
				category.setId(Integer.parseInt(categroyId));
				imageInfo.setCategory(category);
				imageInfo.setCategory_parent1(Integer.parseInt(categroyId));
			}
			if (StringUtils.isNotEmpty(raveId) && !raveId.equals("0")) {
				try {
					imageInfo.setRaveId(Integer.parseInt(raveId));
				} catch (Exception e) {
					log.error("ImageInfoController list raveId", e);

				}
			}

			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<ImageInfo> result = imageInfoService.searchImages(imageInfo, currentPage, pageSize);
			request.setAttribute("result", result);
			// 所有一级分类信息
			List<Category> categorys = categoryService.getCategorys(1);
			Category category0 = new Category();
			category0.setName("Wallpaper");
			category0.setFatherId(1);
			List<Category> wallPapersCategorys = categoryService.getCategorysByName(category0);
			Category wallPapersCategory = null;
			if (wallPapersCategorys != null && wallPapersCategorys.size() != 0) {
				wallPapersCategory = wallPapersCategorys.get(0);
			}
			request.setAttribute("wallPapersCategory", wallPapersCategory);
			request.setAttribute("categorys", categorys);

			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);

		} catch (Exception e) {
			log.error("ImageInfoController list", e);
			PaginationVo<ImageInfo> result = new PaginationVo<ImageInfo>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "image/list";
	}

	/** 判断主题名是否已经存在 */
	@ResponseBody
	@RequestMapping(value = "/judgeNameExist", method = RequestMethod.POST)
	public boolean judgeNameExist(ImageInfo criteria) {
		List<ImageInfo> listimage = imageInfoService.selectByName(criteria.getName());
		if (criteria.getId() == 0) {
			if (listimage.size() != 0) {
				return false;// 名称已经存在
			}
		} else {
			if (listimage.size() > 1 || (listimage.size() == 1 && listimage.get(0).getId() != criteria.getId())) {
				return false;// 名称已经存在
			}
		}
		return true;
	}

	@ResponseBody
	@RequestMapping("/ftpUrl")
	public Map<String, String> queryFtpFilesURL(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		String musicPath = request.getParameter("imagePath");
		try {
			// 输入路径为空,则取系统默认路径
			if (StringUtils.isEmpty(musicPath)) {
				Resource rs = new ClassPathResource("config/resource.properties");
				Properties props = PropertiesLoaderUtils.loadProperties(rs);
				musicPath = props.getProperty("apk.ftp.path", FTP_APK_PATH);
			}
			if (StringUtils.isNotEmpty(musicPath)) {
				getFilePath(musicPath, map);
			}
		} catch (Exception e) {
			log.error("read apk resource failed !", e);
		}
		return map;
	}

	@RequestMapping("/add")
	public String showAdd(HttpServletRequest request) {
		// 所有分类信息
		List<Category> categorys = categoryService.getCategorys(1);
		request.setAttribute("categorys", categorys);
		List<Country> countrys = countryService.getCountrys();
		request.setAttribute("countrys", countrys);
		return "image/add";
	}

	/** 查看详细 */
	@ResponseBody
	@RequestMapping("/getCategory")
	public String getCategory(@RequestParam Integer id, @RequestParam Integer level) {
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
			log.error("ImageInfoController info", e);
			return "{\"success\":\"2\"}";
		}
	}

	@ResponseBody
	@RequestMapping("/secondCategory")
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
			log.error("ImageInfoController info", e);
			return "{\"success\":\"2\"}";
		}
	}

	/** 新增app信息 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute ImageInfo image, @RequestParam Integer categoryId1, @RequestParam Integer categoryId2, @RequestParam(required = false) MultipartFile biglogoFile, @RequestParam(required = false) MultipartFile imageFile) {
		try {
			if (image.getAnotherName() != null) {
				image.setAnotherName(StringUtil.convertBlankLinesToHashSymbol(image.getAnotherName()));
			}
			String fileName = Constant.LOCAL_FILE_PATH + Constant.LOCAL_IMAGE_PATH + MD5.getMd5Value(System.currentTimeMillis() + PinyinToolkit.cn2Spell(image.getName()).replaceAll("[\\/:?*<>|]", ""));
			List<String> fileUrlList = new ArrayList<String>();// s3 list

			// ftp上传路径
			String imageAddress = image.getImageAddress().trim();
			// 选择上传方式,1:本地apk文件上传,2:通过ftp路径上传
			String uploadType = image.getUploadType();

			// 设置logo
			// if(logoFile.getSize()!=0){
			// String suffix =
			// FileAddresUtil.getSuffix(logoFile.getOriginalFilename());
			// if (suffix.equals(Constant.IMG_ADR)) {
			// File imageLogoLocalFile = new File(fileName +
			// Constant.LOCAL_APK_LOG,
			// RandNum.randomFileName(logoFile.getOriginalFilename()));
			// FileUtil.copyInputStream(logoFile.getInputStream(),
			// imageLogoLocalFile);// apk本地上传
			// image.setLogo(FileAddresUtil.getFilePath(imageLogoLocalFile));
			// } else {
			// return "{\"flag\":\"2\"}";//logo不合法
			// }
			// }
			// 设置中图
			if (biglogoFile.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(biglogoFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					File imagebigLogoLocalFile = new File(fileName + Constant.LOCAL_PIC_BIGLOGO, RandNum.randomFileName(biglogoFile.getOriginalFilename()));
					FileUtil.copyInputStream(biglogoFile.getInputStream(), imagebigLogoLocalFile);// apk本地上传
					image.setBiglogo(FileAddresUtil.getFilePath(imagebigLogoLocalFile));
					fileUrlList.add(image.getBiglogo());
				} else {
					return "{\"flag\":\"5\"}";// biglogo图标不合法
				}
			}
			if ((uploadType.equals("1") && (imageFile != null && imageFile.getSize() != 0)) || (uploadType.equals("2") && !StringUtils.isEmpty(imageAddress))) {
				int index = imageAddress.lastIndexOf("/") == -1 ? imageAddress.lastIndexOf("\\") : imageAddress.lastIndexOf("/");
				String imageName = StringUtils.isEmpty(imageAddress) ? imageFile.getOriginalFilename() : imageAddress.substring(index + 1).trim();
				// 获取上传文件后缀
				String suffix = FileAddresUtil.getSuffix(imageName);
				// 设置墙纸
				image.setPinyin(PinyinToolkit.cn2Spell(image.getName()).replaceAll("[\\/:?*<>|]", ""));
				// 获取上传文件后缀
				if (suffix.equals(Constant.IMG_ADR)) {
					File imageLocalFile = new File(fileName + Constant.LOCAL_IMAGE_PATH, RandNum.randomFileName(imageName));
					if (uploadType.equals("1")) {
						FileUtil.copyInputStream(imageFile.getInputStream(), imageLocalFile);// apk本地上传
					} else {
						// File destDir = new
						// File(musicLocalFile.getAbsolutePath());
						File srcFile = new File(imageAddress);
						FileUtils.copyFileToDirectory(srcFile, imageLocalFile.getParentFile());
						// 移动到新目录后,重命名该文件
						File newFile = new File(imageLocalFile.getParent(), imageName);
						if (newFile.isFile()) {
							newFile.renameTo(imageLocalFile);
						}
					}
					BufferedImage bufferImage = ImageIO.read(imageLocalFile);
					// 设置长宽、宽度、文件大小、路径
					image.setWidth(bufferImage.getWidth());
					image.setLength(bufferImage.getHeight());
					image.setFileSize((int) imageLocalFile.length());
					image.setUrl(FileAddresUtil.getFilePath(imageLocalFile));
					fileUrlList.add(image.getUrl());
				} else {
					return "{\"flag\":\"3\"}";// image文件不合法
				}
			}

			List<ImageInfo> listimage = imageInfoService.selectByName(image.getName());
			if (image.getId() == 0) {
				if (listimage.size() != 0) {
					return "{\"flag\":\"4\"}";// 名称已经存在
				} else {
					Integer categoryId = 0;
					if (null == categoryId2 || categoryId2 == 0) {
						categoryId = categoryId1;
					} else {
						categoryId = categoryId2;
					}
					// 将一级id设置为二级国家id
					Category cate = categoryService.getCategory(categoryId);
					if (cate != null) {
						image.setRaveId(cate.getRaveId());
					} else {
						image.setRaveId(1);
					}
					imageInfoService.addImageInfo(image, categoryId);

				}
			} else {
				if (listimage.size() > 1 || (listimage.size() == 1 && listimage.get(0).getId() != image.getId())) {
					return "{\"flag\":\"4\"}";// 名称已经存在
				} else {
					Integer categoryId = 0;
					if (null == categoryId2 || categoryId2 == 0) {
						categoryId = categoryId1;
					} else {
						categoryId = categoryId2;
					}
					// 将一级id设置为二级国家id
					Category cate = categoryService.getCategory(categoryId);
					if (cate != null) {
						image.setRaveId(cate.getRaveId());
					} else {
						image.setRaveId(1);
					}
					imageInfoService.addImageInfo(image, categoryId);
				}
			}
			// s3upload
			S3Util.uploadS3(fileUrlList, false);
			// System.out.println(image.getName());
			// System.out.println(imageFile.getName());
		} catch (Exception e) {
			try {
				// 删除已经下载的文件
				FileUtil.deleteFile(image.getLogo());
				FileUtil.deleteFile(image.getBiglogo());
				FileUtil.deleteFile(image.getUrl());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log.error("ImageInfoController add", e);

			return "{\"flag\":\"6\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 修改app信息 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute ImageInfo image, @RequestParam Integer categoryId1, @RequestParam Integer categoryId2, @RequestParam(required = false) MultipartFile biglogoFile, @RequestParam(required = false) MultipartFile imageFile, HttpServletRequest request) {

		try {
			if (image.getAnotherName() != null) {
				image.setAnotherName(StringUtil.convertBlankLinesToHashSymbol(image.getAnotherName()));
			}
			List<String> fileUrlList = new ArrayList<String>();// s3 list

			Category category = categoryService.getCategory(categoryId1);
			if (category != null) {

			} else {
				// 选择的类别不存在
				return "{\"flag\":\"7\"}";
			}
			String fileName = Constant.LOCAL_FILE_PATH + Constant.LOCAL_IMAGE_PATH + MD5.getMd5Value(System.currentTimeMillis() + PinyinToolkit.cn2Spell(image.getName()).replaceAll("[\\/:?*<>|]", ""));
			// ftp上传路径
			String imageAddress = image.getImageAddress().trim();
			// 选择上传方式,1:本地apk文件上传,2:通过ftp路径上传
			String uploadType = image.getUploadType();

			ImageInfo imageInfo1 = imageInfoService.getImageInfo(image.getId());

			// 设置logo
			// if(logoFile.getSize()!=0){
			// String suffix =
			// FileAddresUtil.getSuffix(logoFile.getOriginalFilename());
			// if (suffix.equals(Constant.IMG_ADR)) {
			// File imageLogoLocalFile = new File(fileName+
			// Constant.LOCAL_APK_LOG,
			// RandNum.randomFileName(logoFile.getOriginalFilename()));
			// FileUtil.copyInputStream(logoFile.getInputStream(),
			// imageLogoLocalFile);// apk本地上传
			// FileUtil.deleteFile(image.getLogo());
			// image.setLogo(FileAddresUtil.getFilePath(imageLogoLocalFile));
			// } else {
			// return "{\"flag\":\"2\"}";
			// }
			// }else{
			// }
			// 设置中图
			if (biglogoFile.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(biglogoFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					File imagebigLogoLocalFile = new File(fileName + Constant.LOCAL_PIC_BIGLOGO, RandNum.randomFileName(biglogoFile.getOriginalFilename()));
					FileUtil.copyInputStream(biglogoFile.getInputStream(), imagebigLogoLocalFile);// apk本地上传
					FileUtil.deleteFile(image.getBiglogo());

					image.setBiglogo(FileAddresUtil.getFilePath(imagebigLogoLocalFile));
					fileUrlList.add(image.getBiglogo());
				} else {
					return "{\"flag\":\"5\"}";
				}
			}

			if ((uploadType.equals("1") && (imageFile != null && imageFile.getSize() != 0)) || (uploadType.equals("2") && !StringUtils.isEmpty(imageAddress))) {
				int index = imageAddress.lastIndexOf("/") == -1 ? imageAddress.lastIndexOf("\\") : imageAddress.lastIndexOf("/");
				String imageName = StringUtils.isEmpty(imageAddress) ? imageFile.getOriginalFilename() : imageAddress.substring(index + 1).trim();
				// 获取上传文件后缀
				String suffix = FileAddresUtil.getSuffix(imageName);
				// 设置墙纸
				image.setPinyin(PinyinToolkit.cn2Spell(image.getName()).replaceAll("[\\/:?*<>|]", ""));
				// 获取上传文件后缀
				if (suffix.equals(Constant.IMG_ADR)) {
					File imageLocalFile = new File(fileName + Constant.LOCAL_IMAGE_PATH, RandNum.randomFileName(imageName));
					if (uploadType.equals("1")) {
						FileUtil.copyInputStream(imageFile.getInputStream(), imageLocalFile);// apk本地上传
					} else {
						// File destDir = new
						// File(musicLocalFile.getAbsolutePath());
						File srcFile = new File(imageAddress);
						FileUtils.copyFileToDirectory(srcFile, imageLocalFile.getParentFile());
						// 移动到新目录后,重命名该文件
						File newFile = new File(imageLocalFile.getParent(), imageName);
						if (newFile.isFile()) {
							newFile.renameTo(imageLocalFile);
						}
					}
					FileUtil.deleteFile(image.getUrl());
					BufferedImage bufferImage = ImageIO.read(imageLocalFile);
					// 设置长宽、宽度、文件大小、路径
					image.setWidth(bufferImage.getWidth());
					image.setLength(bufferImage.getHeight());
					image.setFileSize((int) imageLocalFile.length());
					image.setUrl(FileAddresUtil.getFilePath(imageLocalFile));
					fileUrlList.add(image.getUrl());
				} else {
					return "{\"flag\":\"3\"}";// image文件不合法
				}
			} else {
				image.setWidth(imageInfo1.getWidth());
				image.setLength(imageInfo1.getLength());
				image.setFileSize((int) imageInfo1.getFileSize());
			}
			image.setInitDowdload(imageInfo1.getInitDowdload());
			image.setRealDowdload(imageInfo1.getRealDowdload());
			List<ImageInfo> listimage = imageInfoService.selectByName(image.getName());
			if (image.getId() == 0) {
				if (listimage.size() != 0) {
					return "{\"flag\":\"4\"}";// 名称已经存在
				} else {
					Integer categoryId = 0;
					if (null == categoryId2 || categoryId2 == 0) {
						categoryId = categoryId1;
					} else {
						categoryId = categoryId2;
					}
					// 将一级id设置为二级国家id
					Category cate = categoryService.getCategory(categoryId);
					if (cate != null) {
						image.setRaveId(cate.getRaveId());
					} else {
						image.setRaveId(1);
					}
					imageInfoService.upImageInfo(image, categoryId);
				}
			} else {
				if (listimage.size() > 1 || (listimage.size() == 1 && listimage.get(0).getId() != image.getId())) {
					return "{\"flag\":\"4\"}";// 名称已经存在
				} else {
					Integer categoryId = 0;
					if (null == categoryId2 || categoryId2 == 0) {
						categoryId = categoryId1;
					} else {
						categoryId = categoryId2;
					}
					// 将一级id设置为二级国家id
					Category cate = categoryService.getCategory(categoryId);
					if (cate != null) {
						image.setRaveId(cate.getRaveId());
					} else {
						image.setRaveId(1);
					}
					imageInfoService.upImageInfo(image, categoryId);
				}
			}

			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);

			// s3upload
			S3Util.uploadS3(fileUrlList, false);
			// System.out.println(image.getName());
			// System.out.println(imageFile.getName());
		} catch (Exception e) {
			try {
				// 删除已经下载的文件
				FileUtil.deleteFile(image.getLogo());
				FileUtil.deleteFile(image.getBiglogo());
				FileUtil.deleteFile(image.getUrl());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log.error("ImageInfoController add", e);

			return "{\"flag\":\"6\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 加载单个image信息 */
	@RequestMapping("/{id}")
	public String edit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			ImageInfo image = imageInfoService.getImageInfo(id);
			if (image.getAnotherName() != null) {
				image.setAnotherName(StringUtil.convertHashSymbolToBlankLines(image.getAnotherName()));
			}
			model.addAttribute("image", image);
			// 所有分类信息
			List<Category> fatherCategorys = categoryService.getCategorys(1);
			request.setAttribute("fatherCategorys", fatherCategorys);
			request.setAttribute("page", request.getParameter("page"));
			// 所有分类信息
			if (image.getCategory() != null) {
				List<Category> categorys = categoryService.getCategorys(image.getCategory().getFatherId());
				request.setAttribute("categorys", categorys);
			}
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);
		} catch (Exception e) {
			log.error("AppInfoController edit", e);
		}
		return "image/edit";
	}

	/** 删除music */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids, Model model) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		imageInfoService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}

	/** 查看详细 */
	@RequestMapping("/info/{id}")
	public String info(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			ImageInfo image = imageInfoService.getImageInfo(id);
			if (image.getAnotherName() != null) {
				image.setAnotherName(StringUtil.convertHashSymbolToBlankLines(image.getAnotherName()));
			}
			model.addAttribute("image", image);
		} catch (Exception e) {
			log.error("ImageInfoController info", e);
		}
		return "image/imageInfo";
	}

	/** 查看详细 */
	@RequestMapping("/batchAddShow")
	public String batchAddShow(HttpServletRequest request) {
		Map<String, String> map2 = null;
		String jsstr = CrawlerPaperAndRingtoneUtils.getHeadJSForURL(basicUrl);

		if (jsstr != null && !jsstr.equals("")) {
			map2 = CrawlerPaperAndRingtoneUtils.getSecondCategoryForUrl(jsstr, "paperTypeList");

		}
		List<String> category = new ArrayList<String>();
		if (map2 != null) {
			Set<Map.Entry<String, String>> entryseSet2 = map2.entrySet();
			for (Map.Entry<String, String> entry2 : entryseSet2) {
				try {
					category.add(new String(entry2.getKey().getBytes("iso8859-1"), "UTF-8"));
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
				}
			}
		}
		request.setAttribute("categorys", category);
		return "image/batchAdd";
	}

	/** 批量入库 */
	@ResponseBody
	@RequestMapping("/batchAdd")
	public List<String> batchAdd(HttpServletRequest request, String loginUser, String category, int pageNum) {
		List<String> resList = new ArrayList<String>();
		Map<String, String> map2 = null;
		String jsstr = CrawlerPaperAndRingtoneUtils.getHeadJSForURL(basicUrl);

		if (jsstr != null && !jsstr.equals("")) {
			map2 = CrawlerPaperAndRingtoneUtils.getSecondCategoryForUrl(jsstr, "paperTypeList");

		}
		if (map2 != null) {
			String filePath = Constant.LOCAL_FILE_PATH + Constant.LOCAL_IMAGE_PATH + category;
			Document doc = null;
			String cacheTime = null;
			try {
				ProxyService.closeProxy();
				doc = Jsoup.connect(map2.get(category)).timeout(5000).get();
				Element e = doc.body().getElementById("cacheTime");
				cacheTime = e.attr("value");
				if (pageNum == 1) {
					Elements es = CrawlerPaperAndRingtoneUtils.downloadFirstPageByWallpaperDoc(doc);
					int n = 0;
					int m = 0;
					for (Element e0 : es) {
						String imageitem = e0.attr("imgname");
						if (imageitem != null && !imageitem.equals("")) {
							String result = null;
							try {
								result = isWallpapersExistInDB(imageitem);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								log.error("ImageInfoController batchAdd", e1);
							}
							if (result != null) {
								resList.add("<font color='red'>" + result + "</font>");
								m++;

							}
							n++;
						}
					}
					int mm = 0;
					resList.add("------------------------------------------------------");
					for (Element e0 : es) {
						String imageitem = e0.attr("imgname");
						if (imageitem != null && !imageitem.equals("")) {
							String result = null;
							try {
								result = isWallpapersCategoryTrue(imageitem);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								log.error("ImageInfoController batchAdd", e1);
							}
							if (result != null) {
								resList.add("<font color='blue'>" + result + "</font>");
								m++;
								mm++;
							}
						}

					}
					resList.add("------------------------------------------------------");
					for (Element e0 : es) {
						String imageitem = e0.attr("imgname");
						if (imageitem != null && !imageitem.equals("")) {
							String result = null;
							try {
								result = isWallpapersNeedDownload(category, imageitem);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								log.error("ImageInfoController batchAdd", e1);
							}
							if (result != null) {
								resList.add(result);
							}
						}

					}
					resList.add("------------------------------------------------------");
					resList.add("已经成功录入" + n + "个文件," + "其中" + m + "个文件已经存在" + "但" + mm + "个文件所属类别唯一标识有误");
					if (isImageBatchAddThreadRun == false) {
						WallpapersTask task = new WallpapersTask(es, category, null, filePath, loginUser);
						task.setCategoryService(categoryService);
						task.setImageInfoService(imageInfoService);
						task.setMarketService(marketService);
						task.start();
						isImageBatchAddThreadRun = true;
					} else {
						resList.add("<font color='red'>已经有其它操作人在执行该操作，请先处理其它操作！</font>");
						return resList;
					}

					resList.add("下载操作正在后台执行，请先处理其它操作！");

				} else {
					String[] data = null;
					String type = CrawlerPaperAndRingtoneUtils.getPicCategoryNumber(map2.get(category));
					// 每次 startNum+21 直到 调用url 返回 imgs 为空
					int[] startNum = null;
					int n = 0;
					int m = 0;
					if (pageNum == 0) {
						resList.add("您输入的下载页数无效！！！");

						return resList;
					} else {
						startNum = new int[1];
						data = new String[1];
						startNum[0] = 42 + (pageNum - 1) * 21;
					}
					for (int i = 0; i < startNum.length; i++) {

						StringBuilder sb = new StringBuilder();
						// 拼凑morePic 的 url
						sb.append(morepageUrl);
						sb.append("type=").append(type).append("&");
						sb.append("startNum=").append(startNum[i]).append("&");
						sb.append("cacheTime=").append(cacheTime);
						data[i] = CrawlerPaperAndRingtoneUtils.getReturnData(sb.toString());
					}
					for (int i = 0; i < data.length; i++) {
						if (data[i] != null && !data[i].equals("")) {
							JSONObject json = JSONObject.fromObject(data[i]);
							if (json.get("images") != null && !json.get("images").equals("")) {
								JSONArray images = json.getJSONArray("images");
								if (images != null) {
									for (Iterator j = images.iterator(); j.hasNext();) {
										Object imageitem = j.next();
										String result = null;
										try {
											JSONObject imgItem = JSONObject.fromObject(imageitem);

											String picName = ((String) imgItem.get("name")).replaceAll("[\\/:?*<>|.#'\"$@%^&*,;:]", "");
											result = isWallpapersExistInDB(picName);
										} catch (Exception e1) {
											// TODO Auto-generated catch block
											log.error("ImageInfoController batchAdd", e1);
										}
										if (result != null) {
											resList.add("<font color='red'>" + result + "</font>");
											m++;

										}
										n++;
									}
								} else {
									resList.add("很抱歉，系统出现无法预知的异常或者网络连接异常，请联系管理员！！！");
									return resList;
								}
							} else {
								resList.add("很抱歉，系统出现无法预知的异常或者网络连接异常，请联系管理员！！！");
								return resList;
							}
						} else {
							resList.add("很抱歉，系统出现无法预知的异常或者网络连接异常，请联系管理员！！！");
							return resList;
						}
					}
					resList.add("------------------------------------------------------");
					int mm = 0;
					for (int i = 0; i < data.length; i++) {
						if (data[i] != null && !data[i].equals("")) {
							JSONObject json = JSONObject.fromObject(data[i]);
							if (json.get("images") != null && !json.get("images").equals("")) {
								JSONArray images = json.getJSONArray("images");
								if (images != null) {
									for (Iterator j = images.iterator(); j.hasNext();) {
										Object imageitem = j.next();
										String result = null;
										try {
											JSONObject imgItem = JSONObject.fromObject(imageitem);

											String picName = ((String) imgItem.get("name")).replaceAll("[\\/:?*<>|.#'\"$@%^&*,;:]", "");
											result = isWallpapersCategoryTrue(picName);
										} catch (Exception e1) {
											// TODO Auto-generated catch block
											log.error("ImageInfoController batchAdd", e1);
										}
										if (result != null) {
											resList.add("<font color='blue'>" + result + "</font>");
											m++;
											mm++;
										}
									}
								} else {
									resList.add("很抱歉，系统出现无法预知的异常或者网络连接异常，请联系管理员！！！");
									return resList;
								}
							} else {
								resList.add("很抱歉，系统出现无法预知的异常或者网络连接异常，请联系管理员！！！");
								return resList;
							}
						}
					}

					resList.add("------------------------------------------------------");
					for (int i = 0; i < data.length; i++) {
						if (data[i] != null && !data[i].equals("")) {
							JSONObject json = JSONObject.fromObject(data[i]);
							if (json.get("images") != null && !json.get("images").equals("")) {
								JSONArray images = json.getJSONArray("images");
								if (images != null) {
									for (Iterator j = images.iterator(); j.hasNext();) {
										Object imageitem = j.next();
										String result = null;
										try {
											JSONObject imgItem = JSONObject.fromObject(imageitem);

											String picName = ((String) imgItem.get("name")).replaceAll("[\\/:?*<>|.#'\"$@%^&*,;:]", "");
											result = isWallpapersNeedDownload(category, picName);
										} catch (Exception e1) {
											// TODO Auto-generated catch block
											log.error("ImageInfoController batchAdd", e1);
										}
										if (result != null) {
											resList.add(result);
										}
									}
								}
							}
						}
					}
					resList.add("------------------------------------------------------");
					resList.add("已经成功录入" + n + "个文件," + "其中" + m + "个文件已经存在" + "但" + mm + "个文件所属类别唯一标识有误");
					if (isImageBatchAddThreadRun == false) {
						WallpapersTask task = new WallpapersTask(null, null, data, filePath, loginUser);
						task.setCategoryService(categoryService);
						task.setImageInfoService(imageInfoService);
						task.setMarketService(marketService);
						task.start();
						isImageBatchAddThreadRun = true;
					} else {
						resList.add("<font color='red'>已经有其它操作人在执行该操作，请先处理其它操作！</font>");
						return resList;
					}

					resList.add("下载操作正在后台执行，请先处理其它操作！");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				resList.add("很抱歉，系统出现无法预知的异常或者网络连接异常，请联系管理员！！！");
				return resList;

			}

		} else {
			resList.add("很抱歉，系统出现无法预知的异常或者网络连接异常，请联系管理员！！！");
			return resList;
		}

		return resList;

	}

	/**
	 * 查询下载的壁纸是否已经存在数据库
	 * 
	 * @param element
	 */
	public String isWallpapersExistInDB(String picName) throws Exception {
		List<ImageInfo> imageList = imageInfoService.selectByName(picName);
		if (imageList != null && imageList.size() != 0) {
			if (imageList.get(0).getCategory() != null) {
				return imageList.get(0).getCategory().getName() + "/" + imageList.get(0).getName() + "已经存在";
			}
		}
		return null;
	}

	/**
	 * 查询下载的壁纸类别是否已经和t_category匹配
	 * 
	 * @param element
	 */
	public String isWallpapersCategoryTrue(String picName) throws Exception {
		List<ImageInfo> imageList = imageInfoService.selectByName(picName);
		if (imageList != null && imageList.size() != 0) {
			if (imageList.get(0).getCategory() == null) {
				return picName + "  已存在，但其库中的对应类别不存在，不能正常下载！";
			}
		}
		return null;
	}

	/**
	 * 查询下载的壁纸是否需要下载
	 * 
	 * @param category
	 * @param element
	 * @return
	 */
	public String isWallpapersNeedDownload(String category, String picName) throws Exception {

		List<ImageInfo> imageList = imageInfoService.selectByName(picName);
		if (imageList == null || imageList.size() == 0) {
			return category + "/" + picName + "正在下载";
		}
		return null;
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
					String suffix = FileAddresUtil.getSuffix(f.getName());
					if (suffix.equals(Constant.IMG_ADR)) {
						map.put(f.getName(), f.getPath());
					}
				}
			}
		}
	}
}
