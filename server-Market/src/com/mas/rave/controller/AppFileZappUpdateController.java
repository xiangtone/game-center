package com.mas.rave.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.AppFileZappUpdate;
import com.mas.rave.service.AppFileService;
import com.mas.rave.service.AppFileZappUpdateService;
import com.mas.rave.util.StringUtil;

/**
 * country业务处理
 * 
 * @author jieding
 * 
 */
@Controller
@RequestMapping("/zappUpdate")
public class AppFileZappUpdateController {
	Logger log = Logger.getLogger(AppFileZappUpdateController.class);
	@Autowired
	private  AppFileZappUpdateService  appFileZappUpdateService;	        
	@Autowired
	private  AppFileService appFileService;
	@RequestMapping("/{apkId}/list")
	public String list(HttpServletRequest request,@PathVariable("apkId") int apkId) {
		try {
			String versionName = request.getParameter("versionName");
			String upgradeTypeStr = request.getParameter("upgradeType");
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);
			AppFile appFile = appFileService.getAppFile(apkId);
			String apkKey ="";
			if(appFile!=null){
				apkKey = appFile.getApkKey();
			}
			if(StringUtils.isNotEmpty(apkKey)){
				AppFileZappUpdateService.AppFileZappUpdateCriteria criteria = new AppFileZappUpdateService.AppFileZappUpdateCriteria();
				
				criteria.apkKeyEqualTo(apkKey.trim());
				
				if (StringUtils.isNotEmpty(versionName)){
					criteria.versionNameLikeTo(versionName.trim());
				}
				if (StringUtils.isNotEmpty(upgradeTypeStr)&&!upgradeTypeStr.equals("0")){
					int upgradeType = Integer.parseInt(upgradeTypeStr);
					criteria.upgradeTypeEqualTo(upgradeType);
				}
				PaginationVo<AppFileZappUpdate> result = appFileZappUpdateService.searchAppFileZappUpdates(criteria, currentPage, pageSize);
				request.setAttribute("result", result);
				request.setAttribute("appFile", appFile);
				request.setAttribute("apkId", apkId);
			}else{
				log.error("AppFileZappUpdateController list apkKey 为空或者appFile不存在");
				PaginationVo<AppFileZappUpdate> result = new PaginationVo<AppFileZappUpdate>(null, 0, 10, 1);
				request.setAttribute("result", result);
			}
			
		} catch (Exception e) {
			log.error("AppFileZappUpdateController list", e);
			PaginationVo<AppFileZappUpdate> result = new PaginationVo<AppFileZappUpdate>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "zappUpdate/list";
	}

	/** 新增页加载 */
	@RequestMapping("/{apkId}/add")
	public String showAdd(@PathVariable("apkId") int apkId, Model model) {
		model.addAttribute("apkId", apkId);
		return "zappUpdate/add";
	}

	/** 新增*/
	@ResponseBody
	@RequestMapping(value = "/{apkId}/add", method = RequestMethod.POST)
	public String add(AppFileZappUpdate appFileZappUpdate,@PathVariable("apkId") int apkId) {
		try {
			AppFile appFile = appFileService.getAppFile(apkId);
			if(appFile!=null){
				appFileZappUpdate.setApkKey(appFile.getApkKey());				
			}
			AppFileZappUpdate appFileZappUpdate1 = new AppFileZappUpdate();
			appFileZappUpdate1.setApkKey(appFile.getApkKey());
			appFileZappUpdate1.setVersionName(appFileZappUpdate.getVersionName());
			boolean boolVersionName =judgeExist(appFileZappUpdate1);
			if(!boolVersionName){
				return "{\"flag\":\"2\"}";//版本名已经存在
			}
			AppFileZappUpdate appFileZappUpdate2 = new AppFileZappUpdate();
			appFileZappUpdate2.setApkKey(appFile.getApkKey());
			appFileZappUpdate2.setVersionCode(appFileZappUpdate.getVersionCode());
			
			boolean boolVersionCode =judgeExist(appFileZappUpdate2);
			if(!boolVersionCode){
				return "{\"flag\":\"3\"}";//版本名已经存在
			}
			appFileZappUpdate.setUpdateInfo(StringUtil.convertNtoBr(appFileZappUpdate.getUpdateInfo()));

			appFileZappUpdateService.addAppFileZappUpdate(appFileZappUpdate);
		} catch (Exception e) {
			log.error("AppFileZappUpdateController add", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 加载单个 */
	@RequestMapping("/{apkId}/{id}")
	public String edit(@PathVariable("id") int id,@PathVariable("apkId") int apkId, Model model) {
		AppFileZappUpdate appFileZappUpdate = appFileZappUpdateService.selectByPrimaryKey(id);
		appFileZappUpdate.setUpdateInfo(StringUtil.convertBrToN(appFileZappUpdate.getUpdateInfo()));
		model.addAttribute("appFileZappUpdate", appFileZappUpdate);
		model.addAttribute("apkId", apkId);
		return "zappUpdate/edit";
	}

	/** 更新*/
	@ResponseBody
	@RequestMapping(value = "/{apkId}/{id}", method = RequestMethod.POST)
	public String update(AppFileZappUpdate appFileZappUpdate,@PathVariable("apkId") int apkId) {
		try {
			AppFileZappUpdate appFileZappUpdate0 = appFileZappUpdateService.selectByPrimaryKey(appFileZappUpdate.getId());
			AppFileZappUpdate appFileZappUpdate1 = new AppFileZappUpdate();
			appFileZappUpdate1.setApkKey(appFileZappUpdate0.getApkKey());
			appFileZappUpdate1.setVersionName(appFileZappUpdate.getVersionName());
			appFileZappUpdate1.setId(appFileZappUpdate.getId());
			boolean boolVersionName =judgeExist(appFileZappUpdate1);
			if(!boolVersionName){
				return "{\"flag\":\"2\"}";//版本名已经存在
			}
			AppFileZappUpdate appFileZappUpdate2 = new AppFileZappUpdate();
			appFileZappUpdate2.setApkKey(appFileZappUpdate0.getApkKey());
			appFileZappUpdate2.setVersionCode(appFileZappUpdate.getVersionCode());
			appFileZappUpdate2.setId(appFileZappUpdate.getId());
			boolean boolVersionCode =judgeExist(appFileZappUpdate2);
			if(!boolVersionCode){
				return "{\"flag\":\"3\"}";//版本名已经存在
			}
			appFileZappUpdate.setApkKey(appFileZappUpdate0.getApkKey());
			appFileZappUpdate.setUpdateInfo(StringUtil.convertNtoBr(appFileZappUpdate.getUpdateInfo()));
			appFileZappUpdateService.updateByPrimarykey(appFileZappUpdate);	
		} catch (Exception e) {
			log.error("AppFileZappUpdateController update", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}
	

	/** 删除*/
	@ResponseBody
	@RequestMapping("/{apkId}/delete")
	public String delete(@RequestParam("id") String ids, Model model) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		appFileZappUpdateService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}
	/**判断版本名或版本号是否已经存在*/

	public boolean judgeExist(AppFileZappUpdate appFileZappUpdate){
		List<AppFileZappUpdate> list  = 	appFileZappUpdateService.selectByCondition(appFileZappUpdate);
		if(appFileZappUpdate.getId()==null||appFileZappUpdate.getId()==0){
			if(list.size()!=0){
				return false;//已经存在
			}
		}else{
			if(list.size()>1||(list.size()==1&&list.get(0).getId()!=appFileZappUpdate.getId())){
				return false;//已经存在
			}
		}
		return true;
	}

}
