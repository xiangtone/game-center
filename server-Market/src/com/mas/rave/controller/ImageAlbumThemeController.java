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
import com.mas.rave.main.vo.Country;
import com.mas.rave.main.vo.ImageAlbumTheme;
import com.mas.rave.service.CountryService;
import com.mas.rave.service.ImageAlbumThemeService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.IsChineseOrEnglish;
import com.mas.rave.util.RandNum;
import com.mas.rave.util.s3.S3Util;
/**
 * image主题信息业务处理中心
 * @author jieding
 *
 */
@Controller
@RequestMapping("/imageAlbumTheme")
public class ImageAlbumThemeController {
	
	Logger log = Logger.getLogger(ImageAlbumThemeController.class);
	@Autowired
	private ImageAlbumThemeService imageAlbumThemeService;
	
	@Autowired
	private CountryService countryService;

	/**判断主题名是否已经存在*/

	public boolean judgeThemeNameExist(ImageAlbumTheme criteria){
		List<ImageAlbumTheme> listimage  = 	imageAlbumThemeService.selectByThemeName(criteria);
		if(criteria.getThemeId()==0){
			if(listimage.size()!=0){
				return false;//主题名称已经存在
			}
		}else{
			if(listimage.size()>1||(listimage.size()==1&&listimage.get(0).getThemeId()!=criteria.getThemeId())){
				return false;//主题名称已经存在
			}
		}
		return true;
	}
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
		String str = request.getParameter("name");
		
		String raveId = request.getParameter("raveId");

		ImageAlbumThemeService.ImageAlbumThemeCriteria criteria = new ImageAlbumThemeService.ImageAlbumThemeCriteria();
		if (StringUtils.isNotEmpty(str)) {			
				criteria.nameLike(str.trim());
		}

		if (StringUtils.isNotEmpty(raveId)&& !raveId.equals("0")) {
			try{
				criteria.raveIdEqual(Integer.parseInt(raveId));
			} catch (Exception e) {
				log.error("ImageInfoController list raveId", e);

			}
		}
		int currentPage = RequestUtils.getInt(request, "currentPage", 1);
		int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);
		
		PaginationVo<ImageAlbumTheme> result = imageAlbumThemeService.searchImageAlbumThemes(criteria, currentPage, pageSize);
		request.setAttribute("result", result);
		List<Country> countrys = countryService.getCountrys();
		request.setAttribute("countrys", countrys);
		} catch (Exception e) {
			log.error("ImageAlbumThemeController list", e);
			PaginationVo<ImageAlbumTheme> result = new PaginationVo<ImageAlbumTheme>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "imageAlbumTheme/list";
	}
	/** 新增image主题信息 */
	@RequestMapping("/add")
	public String showAdd(HttpServletRequest request) {
		List<Country> countrys = countryService.getCountrys();
		request.setAttribute("countrys", countrys);
		return "imageAlbumTheme/add";
	}
	
	/** 新增image主题信息页 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute ImageAlbumTheme record,@RequestParam MultipartFile bigImageFile,
			@RequestParam MultipartFile logoFile) {
		try {
			ImageAlbumTheme criteria = new ImageAlbumTheme();
			criteria.setRaveId(record.getRaveId());
			criteria.setName(record.getName());
			boolean bool = judgeThemeNameExist(criteria);
			if(!bool){
				return "{\"flag\":\"3\"}";//该国家下的主题名称已经存在
			}
 			List<String> fileUrlList = new ArrayList<String>();//s3 list

			//设置文件分类  /music/theme/名字/big(small)/文件名
			//大图标
			if (bigImageFile.getSize() != 0) {
				// 获取后缀,检测是否非法文件
				String suffix = FileAddresUtil.getSuffix(bigImageFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					File pic1 = new File(Constant.LOCAL_FILE_PATH+ Constant.LOCAL_IMAGE_PATH+Constant.LOCAL_THEME_PATH+record.getName().replaceAll(" ", "").replaceAll("[\\/:?*<>|]", "")+  "/big", RandNum.randomFileName(bigImageFile.getOriginalFilename()));

					FileUtil.copyInputStream(bigImageFile.getInputStream(), pic1);
					record.setBigicon(FileAddresUtil.getFilePath(pic1));
					fileUrlList.add(record.getBigicon());
					log.info(pic1.getAbsolutePath());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			//小图标
			if (logoFile.getSize() != 0) {
				// 获取后缀,检测是否非法文件
				String suffix = FileAddresUtil.getSuffix(logoFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					File pic1 = new File(Constant.LOCAL_FILE_PATH + Constant.LOCAL_IMAGE_PATH+Constant.LOCAL_THEME_PATH+record.getName().replaceAll(" ","").replaceAll("[\\/:?*<>|]", "")+"/small", RandNum.randomFileName(logoFile.getOriginalFilename()));

					FileUtil.copyInputStream(logoFile.getInputStream(), pic1);
					record.setIcon(FileAddresUtil.getFilePath(pic1));
					fileUrlList.add(record.getIcon());
					log.info(pic1.getAbsolutePath());

				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			imageAlbumThemeService.addImageAlbumTheme(record);
			//s3upload
			S3Util.uploadS3(fileUrlList, false);	
		} catch (Exception e) {
			log.error("ImageAlbumThemeController add", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}
	/** 删除image主题信息 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		imageAlbumThemeService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}
	/** 加载单个image主题信息 */
	@RequestMapping("/{id}")
	public String edit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			ImageAlbumTheme imageAlbumTheme = imageAlbumThemeService.getImageAlbumTheme(id);
			model.addAttribute("imageAlbumTheme", imageAlbumTheme);
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);
		} catch (Exception e) {
			log.error("ImageAlbumThemeController edit", e);
		}
		return "imageAlbumTheme/edit";
	}
	/** 更新app主题信息 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute ImageAlbumTheme record,@RequestParam MultipartFile bigImageFile,
			@RequestParam MultipartFile logoFile) {
		try {
			ImageAlbumTheme criteria = new ImageAlbumTheme();
			criteria.setThemeId(record.getThemeId());
			criteria.setRaveId(record.getRaveId());
			criteria.setName(record.getName());
			boolean bool = judgeThemeNameExist(criteria);
			if(!bool){
				return "{\"flag\":\"3\"}";//该国家下的主题名称已经存在
			}
			List<String> fileUrlList = new ArrayList<String>();//s3 list
			//设置文件分类  /image/theme/名字/big(small)/文件名
			if (bigImageFile.getSize() != 0) {
				// 获取后缀,检测是否非法文件
				String suffix = FileAddresUtil.getSuffix(bigImageFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					FileUtil.deleteFile(record.getBigicon());
					File pic1 = new File(Constant.LOCAL_FILE_PATH + Constant.LOCAL_IMAGE_PATH+Constant.LOCAL_THEME_PATH +record.getName().replaceAll(" ", "").replaceAll("[\\/:?*<>|]", "")+  "/big", RandNum.randomFileName(bigImageFile.getOriginalFilename()));

					FileUtil.copyInputStream(bigImageFile.getInputStream(), pic1);
					record.setBigicon(FileAddresUtil.getFilePath(pic1));
					fileUrlList.add(record.getBigicon());

				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			if (logoFile.getSize() != 0) {
				// 获取后缀,检测是否非法文件
				String suffix = FileAddresUtil.getSuffix(logoFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					FileUtil.deleteFile(record.getIcon());
					File pic1 = new File(Constant.LOCAL_FILE_PATH + Constant.LOCAL_IMAGE_PATH+Constant.LOCAL_THEME_PATH+record.getName().replaceAll(" ", "").replaceAll("[\\/:?*<>|]", "")+"/small", RandNum.randomFileName(logoFile.getOriginalFilename()));

					FileUtil.copyInputStream(logoFile.getInputStream(), pic1);
					record.setIcon(FileAddresUtil.getFilePath(pic1));
					fileUrlList.add(record.getIcon());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			imageAlbumThemeService.upImageAlbumTheme(record);
			//s3upload
			S3Util.uploadS3(fileUrlList, false);	
		} catch (Exception e) {
			log.error("AppAlbumThemeController update", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}
}
