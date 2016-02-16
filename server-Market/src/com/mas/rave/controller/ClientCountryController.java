package com.mas.rave.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.ClientCountry;
import com.mas.rave.service.ClientCountryService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.RandNum;

/**
 * 中英对照表业务处理
 * 
 * @author jieding
 * 
 */
@Controller
@RequestMapping("/clientCountry")
public class ClientCountryController {
	Logger log = Logger.getLogger(ClientCountryController.class);
	@Autowired
	private ClientCountryService clientCountryService;

	/**
	 * 分布查看country信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
			/*
			 * List<ClientCountry> result =
			 * clientCountryService.getClientCountrys(country);
			 * request.setAttribute("clientCountrys", result);
			 */
			ClientCountry obj = new ClientCountry();
			String country = request.getParameter("country");
			if (StringUtils.isNotEmpty(country)) {
				obj.setCountry(country);
			}
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<ClientCountry> result = clientCountryService.selectByExample(obj, currentPage, pageSize);
			request.setAttribute("result", result);

		} catch (Exception e) {
			log.error("ClientCountryController list", e);
			List<ClientCountry> result = new ArrayList<ClientCountry>();
			request.setAttribute("result", result);
		}
		return "clientCountry/list";
	}

	@RequestMapping("/add")
	public String showAdd(HttpServletRequest request) {
		return "clientCountry/add";

	}

	/** 新增 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute ClientCountry clientCountry, @RequestParam MultipartFile icoFile, HttpServletRequest request) {
		try {
			if (checkCountry(clientCountry, 1, 0) == 1) {
				return "{\"flag\":\"3\"}";
			} else if (checkCountry(clientCountry, 0, 0) == 1) {
				return "{\"flag\":\"4\"}";
			}
			// 获取后缀,检测是否非法文件
			String suffix = FileAddresUtil.getSuffix(icoFile.getOriginalFilename());
			if (suffix.equals(Constant.IMG_ADR)) {
				File ico = new File(Constant.LOCAL_FILE_PATH + "country" + File.separator, RandNum.randomFileName(icoFile.getOriginalFilename()));
				FileUtil.copyInputStream(icoFile.getInputStream(), ico);
				clientCountry.setIconUrl(FileAddresUtil.getFilePath(ico));
				clientCountry.setCountry(clientCountry.getCountry().trim());
				clientCountryService.insert(clientCountry);
			} else {
				return "{\"flag\":\"2\"}";
			}
		} catch (Exception e) {
			log.error("AppPicController add", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 加载单个app */
	@RequestMapping("/edit")
	public String showEdit(HttpServletRequest request, Model model) {
		try {
			String countryCn = request.getParameter("countryCn");
			String cn = new String(countryCn.getBytes("iso-8859-1"), "UTF-8");
			ClientCountry country = clientCountryService.selectByCountryCn(cn);
			model.addAttribute("country", country);
			request.setAttribute("currentPage", request.getParameter("currentPage"));
		} catch (Exception e) {
			log.error("ClientCountryController edit", e);
		}
		return "clientCountry/edit";
	}

	/** 更新country */
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@ModelAttribute ClientCountry clientCountry, @RequestParam MultipartFile icoFile) {
		try {
			if (checkCountry(clientCountry, 1, 1) == 1) {
				return "{\"flag\":\"3\"}";
			} else if (checkCountry(clientCountry, 0, 1) == 1) {
				return "{\"flag\":\"4\"}";
			}
			if (icoFile != null && icoFile.getSize() > 0) {
				// 获取后缀,检测是否非法文件
				String suffix = FileAddresUtil.getSuffix(icoFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					// 删除文件
					FileUtil.deleteFile(clientCountry.getIconUrl());
					File ico = new File(Constant.LOCAL_FILE_PATH + "country" + File.separator, RandNum.randomFileName(icoFile.getOriginalFilename()));
					FileUtil.copyInputStream(icoFile.getInputStream(), ico);
					clientCountry.setIconUrl(FileAddresUtil.getFilePath(ico));
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			clientCountry.setCountry(clientCountry.getCountry().trim());
			clientCountryService.updateByCountryCn(clientCountry);
		} catch (Exception e) {
			log.error("ClientCountryController update", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	public int checkCountry(ClientCountry country, int flag, int tag) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (flag == 1) {
			// 中文名检测
			map.put("countryCn", country.getCountryCn());
		} else {
			// 英文名检测
			map.put("country", country.getCountry());
		}
		ClientCountry cns = clientCountryService.getParament(map);
		if (cns != null) {
			if (tag == 1) {
				if (cns.getCountryCn().equals(country.getCountryCnStr())) {
					return 0;
				} else if (StringUtils.isNotEmpty(cns.getCountry()) && cns.getCountry().equals(country.getCountryStr())) {
					return 0;
				}
			}
			return 1;
		} else {
			return 0;
		}

	}

	/** 删除常用回馈 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids, Model model) {
		try {
			String[] strs = ids.split(",");
			for (String str : strs) {
				String cn = new String(str.getBytes("iso-8859-1"), "UTF-8");
				ClientCountry country = clientCountryService.selectByCountryCn(cn);
				FileUtil.deleteFile(country.getIconUrl());
				clientCountryService.deleteByCountryCn(cn);
			}
			return "{\"success\":\"true\"}";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "{\"success\":\"false\"}";
	}

}
