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
import com.mas.rave.main.vo.AppCollection;
import com.mas.rave.main.vo.Country;
import com.mas.rave.service.AppCollectionService;
import com.mas.rave.service.CountryService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.DateUtil;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.RandNum;
import com.mas.rave.util.s3.S3Util;
/**
 * 应用专辑管理
 * @author jieding
 *
 */
@Controller
@RequestMapping("/appCollection")
public class AppCollectionController {
	
	Logger log = Logger.getLogger(AppCollectionController.class);
	@Autowired
	private AppCollectionService appCollectionService;
	
	@Autowired
	private CountryService countryService;

	/**判断主题名是否已经存在*/

	public boolean judgeNameExist(AppCollection criteria){
		List<AppCollection> listimage  = 	appCollectionService.selectByName(criteria);
		if(criteria.getCollectionId()==0){
			if(listimage.size()!=0){
				return false;//主题名称已经存在
			}
		}else{
			if(listimage.size()>1||(listimage.size()==1&&listimage.get(0).getCollectionId()!=criteria.getCollectionId())){
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

		AppCollectionService.AppCollectionCriteria criteria = new AppCollectionService.AppCollectionCriteria();
		if (StringUtils.isNotEmpty(str)) {			
				criteria.nameLike(str.trim());
		}

		if (StringUtils.isNotEmpty(raveId)&& !raveId.equals("0")) {
			try{
				criteria.raveIdEqual(Integer.parseInt(raveId));
			} catch (Exception e) {
				log.error("AppCollectionController list raveId", e);

			}
		}
		String type = request.getParameter("type");

		if (StringUtils.isNotEmpty(type)&& !type.equals("0")) {
			try{
				criteria.typeEqual(Integer.parseInt(type));
			} catch (Exception e) {
				log.error("AppCollectionController list type", e);

			}
		}
		request.setAttribute("type", type);
		int currentPage = RequestUtils.getInt(request, "currentPage", 1);
		int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);
		
		PaginationVo<AppCollection> result = appCollectionService.searchAppCollections(criteria, currentPage, pageSize);
		request.setAttribute("result", result);
		List<Country> countrys = countryService.getCountrys();
		request.setAttribute("countrys", countrys);
		} catch (Exception e) {
			log.error("AppCollectionController list", e);
			PaginationVo<AppCollection> result = new PaginationVo<AppCollection>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "appCollection/list";
	}
	/** 新增image主题信息 */
	@RequestMapping("/add")
	public String showAdd(HttpServletRequest request) {
		List<Country> countrys = countryService.getCountrys();
		request.setAttribute("countrys", countrys);
		String type = request.getParameter("type");
		request.setAttribute("type", type);
		return "appCollection/add";
	}
	
	/** 新增image主题信息页 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute AppCollection record,@RequestParam MultipartFile bigImageFile,
			@RequestParam MultipartFile logoFile) {
		try {
			AppCollection criteria = new AppCollection();
			criteria.setRaveId(record.getRaveId());
			criteria.setName(record.getName());
			criteria.setType(record.getType());
			boolean bool = judgeNameExist(criteria);
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
					File pic1 = new File(Constant.LOCAL_FILE_PATH+ Constant.LOCAL_APP_PATH+Constant.LOCAL_THEME_PATH+record.getName().replaceAll(" ", "").replaceAll("[\\/:?*<>|]", "")+  "/big", RandNum.randomFileName(bigImageFile.getOriginalFilename()));

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
					File pic1 = new File(Constant.LOCAL_FILE_PATH + Constant.LOCAL_APP_PATH+Constant.LOCAL_THEME_PATH+record.getName().replaceAll(" ", "").replaceAll("[\\/:?*<>|]", "")+"/small", RandNum.randomFileName(logoFile.getOriginalFilename()));

					FileUtil.copyInputStream(logoFile.getInputStream(), pic1);
					record.setIcon(FileAddresUtil.getFilePath(pic1));
					fileUrlList.add(record.getIcon());
					log.info(pic1.getAbsolutePath());

				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			record.setCreateTime(DateUtil.StringToDate(record.getCreateTime1()));
			appCollectionService.addAppCollection(record);
			//s3upload
			S3Util.uploadS3(fileUrlList, false);	
		} catch (Exception e) {
			log.error("AppCollectionController add", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}
	/** 删除image主题信息 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		appCollectionService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}
	/** 加载单个image主题信息 */
	@RequestMapping("/{id}")
	public String edit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			AppCollection appCollection = appCollectionService.getAppCollection(id);
			model.addAttribute("appCollection", appCollection);
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);
		} catch (Exception e) {
			log.error("AppCollectionController edit", e);
		}
		return "appCollection/edit";
	}
	/** 更新app主题信息 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute AppCollection record,@RequestParam MultipartFile bigImageFile,
			@RequestParam MultipartFile logoFile) {
		try {
			AppCollection criteria = new AppCollection();
			criteria.setCollectionId(record.getCollectionId());
			criteria.setRaveId(record.getRaveId());
			criteria.setName(record.getName());
			criteria.setType(record.getType());
			boolean bool = judgeNameExist(criteria);
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
					File pic1 = new File(Constant.LOCAL_FILE_PATH + Constant.LOCAL_APP_PATH+Constant.LOCAL_THEME_PATH +record.getName().replaceAll(" ", "").replaceAll("[\\/:?*<>|]", "")+  "/big", RandNum.randomFileName(bigImageFile.getOriginalFilename()));

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
					File pic1 = new File(Constant.LOCAL_FILE_PATH + Constant.LOCAL_APP_PATH+Constant.LOCAL_THEME_PATH+record.getName().replaceAll(" ", "").replaceAll("[\\/:?*<>|]", "")+"/small", RandNum.randomFileName(logoFile.getOriginalFilename()));

					FileUtil.copyInputStream(logoFile.getInputStream(), pic1);
					record.setIcon(FileAddresUtil.getFilePath(pic1));
					fileUrlList.add(record.getIcon());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			record.setCreateTime(DateUtil.StringToDate(record.getCreateTime1()));
			appCollectionService.upAppCollection(record);
			//s3upload
			S3Util.uploadS3(fileUrlList, false);	
		} catch (Exception e) {
			log.error("AppCollectionController update", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}
}
