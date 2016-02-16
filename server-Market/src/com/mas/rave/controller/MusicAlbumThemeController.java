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
import com.mas.rave.main.vo.MusicAlbumTheme;
import com.mas.rave.service.CountryService;
import com.mas.rave.service.MusicAlbumThemeService;
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
@RequestMapping("/musicAlbumTheme")
public class MusicAlbumThemeController {
	
	Logger log = Logger.getLogger(MusicAlbumThemeController.class);
	@Autowired
	private MusicAlbumThemeService musicAlbumThemeService;
	
	@Autowired
	private CountryService countryService;

	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
		String str = request.getParameter("name");
		
		String raveId = request.getParameter("raveId");

		MusicAlbumThemeService.MusicAlbumThemeCriteria criteria = new MusicAlbumThemeService.MusicAlbumThemeCriteria();
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
		
		PaginationVo<MusicAlbumTheme> result = musicAlbumThemeService.searchMusicAlbumThemes(criteria, currentPage, pageSize);
		request.setAttribute("result", result);
		List<Country> countrys = countryService.getCountrys();
		request.setAttribute("countrys", countrys);
		} catch (Exception e) {
			log.error("MusicAlbumThemeController list", e);
			PaginationVo<MusicAlbumTheme> result = new PaginationVo<MusicAlbumTheme>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "musicAlbumTheme/list";
	}
	/** 新增image主题信息 */
	@RequestMapping("/add")
	public String showAdd(HttpServletRequest request) {
		List<Country> countrys = countryService.getCountrys();
		request.setAttribute("countrys", countrys);
		return "musicAlbumTheme/add";
	}
	public boolean judgeThemeNameExist(MusicAlbumTheme criteria){
		List<MusicAlbumTheme> listmusic = 	musicAlbumThemeService.selectByThemeName(criteria);
		if(criteria.getThemeId()==0){
			if(listmusic.size()!=0){
				return false;//主题名称已经存在
			}
		}else{
			if(listmusic.size()>1||(listmusic.size()==1&&listmusic.get(0).getThemeId()!=criteria.getThemeId())){
				return false;//主题名称已经存在
			}
		}
		return true;
	}
	/** 新增image主题信息页 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute MusicAlbumTheme record,@RequestParam MultipartFile bigMusicFile,
			@RequestParam MultipartFile logoFile) {
		try {
			MusicAlbumTheme criteria = new MusicAlbumTheme();
			criteria.setRaveId(record.getRaveId());
			criteria.setName(record.getName());
			boolean bool = judgeThemeNameExist(criteria);
			if(!bool){
				return "{\"flag\":\"3\"}";//该国家下的主题名称已经存在
			}
 			List<String> fileUrlList = new ArrayList<String>();//s3 list

			//设置文件分类  /music/theme/名字/big(small)/文件名
			//大图标
			if (bigMusicFile.getSize() != 0) {
				// 获取后缀,检测是否非法文件
				String suffix = FileAddresUtil.getSuffix(bigMusicFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					File pic1 = new File(Constant.LOCAL_FILE_PATH + Constant.LOCAL_MUSIC_PATH+Constant.LOCAL_THEME_PATH
							+record.getName().replaceAll(" ", "").replaceAll("[\\/:?*<>|]", "")+  "/big", RandNum.randomFileName(bigMusicFile.getOriginalFilename()));

					FileUtil.copyInputStream(bigMusicFile.getInputStream(), pic1);
					record.setBigicon(FileAddresUtil.getFilePath(pic1));
					log.info(pic1.getAbsolutePath());
					fileUrlList.add(record.getBigicon());

				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			//小图标
			if (logoFile.getSize() != 0) {
				// 获取后缀,检测是否非法文件
				String suffix = FileAddresUtil.getSuffix(logoFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					File pic1 = new File(Constant.LOCAL_FILE_PATH +  Constant.LOCAL_MUSIC_PATH+Constant.LOCAL_THEME_PATH
							+record.getName().replaceAll(" ", "").replaceAll("[\\/:?*<>|]", "")+"/small", RandNum.randomFileName(logoFile.getOriginalFilename()));

					FileUtil.copyInputStream(logoFile.getInputStream(), pic1);
					record.setIcon(FileAddresUtil.getFilePath(pic1));
					log.info(pic1.getAbsolutePath());
					fileUrlList.add(record.getIcon());

				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			musicAlbumThemeService.addMusicAlbumTheme(record);
			//s3upload
			S3Util.uploadS3(fileUrlList, false);				
		} catch (Exception e) {
			log.error("MusicAlbumThemeController add", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}
	/** 删除image主题信息 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		musicAlbumThemeService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}
	/** 加载单个image主题信息 */
	@RequestMapping("/{id}")
	public String edit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			MusicAlbumTheme musicAlbumTheme = musicAlbumThemeService.getMusicAlbumTheme(id);
			model.addAttribute("musicAlbumTheme", musicAlbumTheme);
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);
		} catch (Exception e) {
			log.error("MusicAlbumThemeController edit", e);
		}
		return "musicAlbumTheme/edit";
	}
	/** 更新app主题信息 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute MusicAlbumTheme record,@RequestParam MultipartFile bigMusicFile,
			@RequestParam MultipartFile logoFile) {
		try {
			MusicAlbumTheme criteria = new MusicAlbumTheme();
			criteria.setThemeId(record.getThemeId());
			criteria.setRaveId(record.getRaveId());
			criteria.setName(record.getName());
			boolean bool = judgeThemeNameExist(criteria);
			if(!bool){
				return "{\"flag\":\"3\"}";//该国家下的主题名称已经存在
			}
 			List<String> fileUrlList = new ArrayList<String>();//s3 list

			//设置文件分类  /music/theme/名字/big(small)/文件名
			if (bigMusicFile.getSize() != 0) {
				// 获取后缀,检测是否非法文件
				String suffix = FileAddresUtil.getSuffix(bigMusicFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					FileUtil.deleteFile(record.getBigicon());
					File pic1 = new File(Constant.LOCAL_FILE_PATH +  Constant.LOCAL_MUSIC_PATH+Constant.LOCAL_THEME_PATH
							+record.getName().replaceAll(" ", "").replaceAll("[\\/:?*<>|]", "")+  "/big", RandNum.randomFileName(bigMusicFile.getOriginalFilename()));

					FileUtil.copyInputStream(bigMusicFile.getInputStream(), pic1);
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
					File pic1 = new File(Constant.LOCAL_FILE_PATH +  Constant.LOCAL_MUSIC_PATH+Constant.LOCAL_THEME_PATH
							+record.getName().replaceAll(" ", "").replaceAll("[\\/:?*<>|]", "")+"/small", RandNum.randomFileName(logoFile.getOriginalFilename()));

					FileUtil.copyInputStream(logoFile.getInputStream(), pic1);
					record.setIcon(FileAddresUtil.getFilePath(pic1));
					fileUrlList.add(record.getIcon());

				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			musicAlbumThemeService.upMusicAlbumTheme(record);
			//s3upload
			S3Util.uploadS3(fileUrlList, false);	
		} catch (Exception e) {
			log.error("MusicAlbumThemeController update", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}
}
