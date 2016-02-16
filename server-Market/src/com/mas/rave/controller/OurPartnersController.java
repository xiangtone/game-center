package com.mas.rave.controller;

import java.io.File;
import java.io.IOException;

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
import com.mas.rave.main.vo.OurPartners;
import com.mas.rave.service.OurPartnersService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.RandNum;
import com.mas.rave.util.s3.S3Util;

/**
 * 合伯伙伴
 * 
 * @author liwei.sz
 * 
 */
@Controller
@RequestMapping("/ourPartners")
public class OurPartnersController {
	Logger log = Logger.getLogger(OurPartnersController.class);
	@Autowired
	private OurPartnersService ourPartnersService;

	/**
	 * 查看广告合作
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
			String str = request.getParameter("name");

			OurPartnersService.OurPartnersCriteria criteria = new OurPartnersService.OurPartnersCriteria();
			if (StringUtils.isNotEmpty(str)) {
				criteria.nameLike(str.trim());
			}

			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<OurPartners> result = ourPartnersService.searchOurPartners(criteria, currentPage, pageSize);
			request.setAttribute("result", result);
		} catch (Exception e) {
			log.error("AppAlbumThemeController list", e);
			PaginationVo<OurPartners> result = new PaginationVo<OurPartners>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "ourPartners/list";
	}

	/** 新增广告合作 */
	@RequestMapping("/add")
	public String showAdd(HttpServletRequest request) {
		return "ourPartners/add";
	}

	/** 新增广告合作 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute OurPartners record, @RequestParam MultipartFile ico) {
		try {
			// 文件目录分类id/apkId
			String str = FileAddresUtil.getTitlePath(0, 0, Constant.PARTNERS_ADR);
			if (ico.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(ico.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					File icoFile = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(ico.getOriginalFilename()));
					FileUtil.copyInputStream(ico.getInputStream(), icoFile);// apk本地上传
					record.setLogo(FileAddresUtil.getFilePath(icoFile));
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			ourPartnersService.addAppOurPartners(record);
			if (ico.getSize() != 0) {
				S3Util.uploadS3(record.getLogo(), false);
			}

		} catch (Exception e) {
			try {
				FileUtil.deleteFile(record.getLogo());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log.error("OurPartnersController add", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 加载单个app主题信息 */
	@RequestMapping("/{id}")
	public String edit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			OurPartners ourPartners = ourPartnersService.getOurPartners(id);
			model.addAttribute("ourPartners", ourPartners);
		} catch (Exception e) {
			log.error("OurPartnersController edit", e);
		}
		return "ourPartners/edit";
	}

	/** 更新app主题信息 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute OurPartners record, @RequestParam MultipartFile ico) {
		try {
			String str = FileAddresUtil.getTitlePath(0, 0, Constant.PARTNERS_ADR);
			if (ico.getSize() != 0) {
				// 获取后缀,检测是否非法文件
				String suffix = FileAddresUtil.getSuffix(ico.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					FileUtil.deleteFile(record.getLogo());
					File icoFile = new File(Constant.LOCAL_FILE_PATH + str, RandNum.randomFileName(ico.getOriginalFilename()));
					FileUtil.copyInputStream(ico.getInputStream(), icoFile);// apk本地上传
					record.setLogo(FileAddresUtil.getFilePath(icoFile));
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			ourPartnersService.upOurPartners(record);
			if (ico.getSize() != 0) {
				S3Util.uploadS3(record.getLogo(), false);
			}

		} catch (Exception e) {
			try {
				FileUtil.deleteFile(record.getLogo());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log.error("OurPartnersController update", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 删除app分类 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		ourPartnersService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}
}
