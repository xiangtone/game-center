package com.mas.rave.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mas.rave.appannie.AppannieService;
import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.AppAlbum;
import com.mas.rave.main.vo.AppAlbumColumn;
import com.mas.rave.main.vo.AppAlbumRes;
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.main.vo.AppScore;
import com.mas.rave.main.vo.AppannieInfo;
import com.mas.rave.main.vo.Country;
import com.mas.rave.service.AppAlbumColumnService;
import com.mas.rave.service.AppAlbumResService;
import com.mas.rave.service.AppAlbumResTempService;
import com.mas.rave.service.AppAlbumService;
import com.mas.rave.service.AppScoreService;
import com.mas.rave.service.AppannieInfoService;
import com.mas.rave.service.CountryService;
import com.mas.rave.task.AppBatchDistributionConstant;
import com.mas.rave.util.ConstantScore;
import com.mas.rave.vo.TopVO;

@Controller
@RequestMapping("/appannieInfo")
public class AppannieInfoController {
	Logger log = Logger.getLogger(AppannieInfoController.class);
	
	public static final int handSource = 0;
	public static final int autoSource = 1;
	public static final int rankAll=10000;
	public static final int globalKey = 1;
	@Autowired
	private AppannieInfoService appannieInfoService;
	@Autowired
	private AppAlbumColumnService appAlbumColumnService;

	@Autowired
	private CountryService countryService;	

	@Autowired
	private AppAlbumService appAlbumService;

	@Autowired
	private AppAlbumResTempService appAlbumResTempService;
	
	@Autowired
	private AppAlbumResService	appAlbumResService;
	
	@Autowired
	private PropertiesController controller;

	@Autowired
	private AppScoreService appScoreService;
	/**
	 * 分布查看appannieInfo
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
			String appName = request.getParameter("appName");
				
			String raveId = request.getParameter("raveId");

			String albumId = request.getParameter("albumId")== null ? "1" : request.getParameter("albumId");
			
			
			String columnId = request.getParameter("columnId");
			request.setAttribute("columnId", columnId);
			AppannieInfo appannieInfo = new AppannieInfo();
			if (StringUtils.isNotEmpty(appName)) {
				appannieInfo.setAppName(appName.trim());
			}
			if (StringUtils.isNotEmpty(raveId)&& !raveId.equals("0")) {
				try{
					appannieInfo.setRaveId(Integer.parseInt(raveId));
				} catch (Exception e) {
					log.error("AppannieInfoController list raveId", e);

				}
			}else{
				List<Country> countrys = countryService.getCountrys();
				for(Country country:countrys){
					if(country.getId()!=1){
						appannieInfo.setRaveId(country.getId());	
						break;
					}
				}
				
			}
			if (StringUtils.isNotEmpty(albumId)&& !albumId.equals("0")) {
				try{
					appannieInfo.setAlbumId(Integer.parseInt(albumId));
				} catch (Exception e) {
					log.error("AppannieInfoController list albumId", e);

				}
			}			
			if (StringUtils.isNotEmpty(columnId)&& !columnId.equals("0")) {
				try{
					appannieInfo.setColumnId(Integer.parseInt(columnId));
				} catch (Exception e) {
					log.error("AppannieInfoController list albumId", e);

				}
			}else{
				List<AppAlbumColumn> appAlbumColumnList = appAlbumColumnService.getAppAlbumColumnsByAppAlbumId(Integer.parseInt(albumId));
				int columnIdDefault = 0;
				for(AppAlbumColumn appAlbumColumn:appAlbumColumnList){
					if(appAlbumColumn.getFlag()==1){
						columnIdDefault = appAlbumColumn.getColumnId();
						break;
					}
				}
				appannieInfo.setColumnId(columnIdDefault);
			}
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<AppannieInfo> result = appannieInfoService.selectByExample(appannieInfo, currentPage, pageSize);
			request.setAttribute("result", result);
		
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);
		} catch (Exception e) {
			log.error("AppannieInfoController list", e);
			PaginationVo<AppannieInfo> result = new PaginationVo<AppannieInfo>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "appannieInfo/list";
	}
	
	/** 删除appannieInfo */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		appannieInfoService.batchDel(idIntArray);
		return "{\"success\":\"true\"}";
	}
	@ResponseBody
	@RequestMapping("/albumColumn")
	public String querySecondCategory(@RequestParam("id") Integer id, @RequestParam("columnId") Integer columnId) {
		try {
			List<AppAlbumColumn> list = appAlbumColumnService.getAppAlbumColumnsByAppAlbumId(id);
			if (CollectionUtils.isEmpty(list)) {
				return "{\"success\":\"0\"}";
			} else {
				StringBuilder options = new StringBuilder();
				for (AppAlbumColumn appAlbumColumn : list) {
					if(appAlbumColumn.getFlag()==1){
						String selected = "";
						if (columnId!=null&&appAlbumColumn.getColumnId() == columnId) {
							selected = "selected";
						}
						options.append("<option value='");
						options.append(appAlbumColumn.getColumnId() + "' " + selected + ">");
						options.append(appAlbumColumn.getName());
						options.append("("+appAlbumColumn.getNameCn()+")");
						options.append("</option>");						
					}
				}
				return "{\"success\":\"1\",\"option\":\"" + options.toString() + "\"}";
			}
		} catch (Exception e) {
			return "{\"success\":\"2\"}";
		}
	}
	@ResponseBody
	@RequestMapping("/simulatedDistribution")
	public String simulatedDistribution(@RequestParam("raveId") Integer raveId,@RequestParam("albumId") Integer albumId,@RequestParam("columnId") Integer columnId) {
		try{
			//删除分发数据
			init();
			Country country =  countryService.getCountry(raveId);		
			AppAlbum appAlbum = appAlbumService.getAppAlbum(albumId);
			//获取当前分发的类别
			String currentAlbum = country.getName() +"-"+ appAlbum.getName();
			if(AppAlbumColumnController.map.get(currentAlbum)!=null){
				return "{\"flag\":\"2\"}"; //分发程序未运行完，不能模拟分发
			}
			AppannieInfo criteria = new AppannieInfo();
			criteria.setColumnId(columnId);
			criteria.setRaveId(raveId);
			List<AppannieInfo> appannieInfoList =appannieInfoService.selectByCondition(criteria);
			if(appannieInfoList==null||appannieInfoList.size()==0){
				return "{\"flag\":\"3\"}";
			}
			appAlbumResTempService.deleteBySource(null,columnId,raveId,autoSource);
			AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(columnId);
			int listType = getListType(appAlbumColumn);
			for(AppannieInfo appannieInfo: appannieInfoList){
				Map<String, Object> map0 = new HashMap<String, Object>();
				AppannieService appScore = new AppannieService();
				map0.put(AppBatchDistributionConstant.INSTALLTOTAL, appannieInfo.getAnnieInstallTotal());
				map0.put(AppBatchDistributionConstant.SIZE, appannieInfo.getSize());
				map0.put(AppBatchDistributionConstant.RATER, appannieInfo.getAnnieRatings());
				if(appannieInfo.getInitialReleaseDate()!=null){
					map0.put(AppBatchDistributionConstant.INITIALRELEASEDATE, appannieInfo.getInitialReleaseDate());					
				}
				TopVO topVO = getAppAnnieScore(map0,appannieInfo.getAnnieRank()-1, listType,
						appannieInfo.getAppInfo(), appannieInfo.getAppFile(), appScore);
				addAppAlbumResTemp(appannieInfo.getAppName(), appAlbumColumn, appannieInfo.getAppInfo(), 
						appannieInfo.getAppFile(), topVO, country);
			}
			// 更新排序信息
			autoUploadAutoSort(appAlbumColumn,raveId);
			 //golbal分发处理
			globalHandOut(albumId,appAlbumColumn,country);	
			
			
			return "{\"flag\":\"0\"}";
		}catch(Exception e){
			log.error("AppannieInfoController simulatedDistribution", e);
			return "{\"flag\":\"1\"}";
		}
	
	}
	/**
	 * 是否把分发数据复制到global中
	 * @param appAlbumColumn
	 */
	private void globalHandOut(int albumId,AppAlbumColumn appAlbumColumn,Country country){
		String defaultCountry = "";
		List<AppScore> scores = appScoreService.getAllScore();
		for(AppScore appSocre:scores ){
			 if (appSocre.getScoreKey().equals("Global")) {
				 defaultCountry= appSocre.getScoreValue();
			 }
		}
		if(defaultCountry==null || defaultCountry.equals("")){
			defaultCountry = "Malaysia";
		}
		
		//忽略大小写,匹配查看分发名字是否是app排名配置中默认的global, 如果是,则将数据copy到golbal中
		if(country.getName().toLowerCase().trim().equals(defaultCountry.toLowerCase().trim())){
			//获取手动分发排名
			//Map<Integer,Map<Integer, AppAlbumRes>> maphandGlobal  = getHandDistribute(listAppAlbumColumn,globalKey);
			//按清除global 的自动分发数据
			appAlbumResTempService.deleteBySource(albumId,null,globalKey,autoSource);
			//获取所有defaultCountry 自动 global分发数据
				//查询出所有的数据
				List<AppAlbumRes> appAlbumResList = appAlbumResTempService.getByFileColumnAndSource(null, appAlbumColumn,autoSource, country.getId());
			
				for(AppAlbumRes appAlbumRes: appAlbumResList){
					appAlbumRes.setRaveId(globalKey);
					appAlbumRes.setId(null);	
					List<AppAlbumRes> appAlbumResList1 = appAlbumResTempService.getByFileColumnAndSource(appAlbumRes.getAppInfo(), appAlbumColumn,handSource, globalKey);
					//数据不存在,则添加
					if(appAlbumResList1==null ||appAlbumResList1.size()==0){
						appAlbumResTempService.addAppAlbumResTemp(appAlbumRes);
					}
				}
			// 更新排序信息
			autoUploadAutoSort(appAlbumColumn,globalKey);
	    }
	}
	/**
	 * 更新排序
	 * @param listAppAlbumColumn
	 * @param maphandGlobal
	 * @throws Exception 
	 */
	private void autoUploadAutoSort(AppAlbumColumn appAlbumColumn,int raveId){			
		    List<AppAlbumRes> handList = appAlbumResTempService.getByFileColumnAndSource(null, appAlbumColumn, handSource, raveId);
		    Map<Integer, AppAlbumRes> maphand = new LinkedHashMap<Integer, AppAlbumRes>();
		    //把所有的手动数据按照排名插入map中
		    for(AppAlbumRes hand:handList){
		    	if(hand.getRanking()!=null&&hand.getRanking()!=0){
		    		maphand.put(hand.getRanking(), hand);
		    	}
	    	}
	    	List<AppAlbumRes> auto = appAlbumResTempService.getByFileColumnAndSource(null, appAlbumColumn,autoSource, raveId);//获取所有自动分发数据
	    	//获取所有的自动排行
			if(auto!=null && auto.size()>0){
				// 遍历自动添加的数据，获取排名，	
				for(int i=0;i<auto.size();i++){
					AppAlbumRes auto0 = auto.get(i);
					maphand = SortOne(maphand, i, auto0);
				}
			}
	}
	/**
	 * 设置sort
	 * @param maphand
	 * @param i
	 * @param auto0
	 * @return
	 */
	private Map<Integer, AppAlbumRes> SortOne(Map<Integer, AppAlbumRes> maphand, int i,
			AppAlbumRes auto0) {
		    i++;
			if(maphand.containsKey(i)){
				maphand = SortOne(maphand, i, auto0);
			}else{
				auto0.setSort(rankAll-i);
				appAlbumResTempService.updateSortByPrimarykey(auto0);
				maphand.put(i, auto0);						
			}
		return maphand;
	}
	/**
	 * 写入分发
	 * @param appName
	 * @param appAlbumColumn
	 * @param appInfo
	 * @param appFile
	 * @param vo
	 */
	private void addAppAlbumResTemp(String appName,
			AppAlbumColumn appAlbumColumn, AppInfo appInfo, AppFile appFile,
			TopVO vo,Country country) {
		
		AppAlbumRes newappAlbumRes = new AppAlbumRes();
		AppAlbum appAlbum = appAlbumColumn.getAppAlbum();
		newappAlbumRes.setAppAlbumColumn(appAlbumColumn);
		newappAlbumRes.setAppAlbum(appAlbum);
		newappAlbumRes.setAppFile(appFile);
		newappAlbumRes.setAppInfo(appInfo);
		newappAlbumRes.setAppName(appName);
		if(appInfo.getCategory()!=null){
			newappAlbumRes.setCategoryId(appInfo.getCategory()
					.getId());
		}						
		newappAlbumRes.setRaveId(country.getId());
		newappAlbumRes.setBigLogo(appInfo.getBigLogo());
		newappAlbumRes.setLogo(appInfo.getLogo());
		newappAlbumRes.setBrief(appInfo.getBrief());
		newappAlbumRes.setSort(vo.getScore());// 公式计算出来的分数
		newappAlbumRes.setDescription(appInfo.getDescription());
		newappAlbumRes.setFree(appInfo.getFree());
		newappAlbumRes.setFileSize(appFile.getFileSize());
		newappAlbumRes.setStars(appInfo.getStars());
		newappAlbumRes.setPackageName(appFile.getPackageName());
		newappAlbumRes.setVersionCode(appFile.getVersionCode());
		newappAlbumRes.setVersionName(appFile.getVersionName());
		newappAlbumRes.setUrl(appFile.getUrl());
		newappAlbumRes.setInitDowdload(appInfo
				.getInitDowdload());
		newappAlbumRes.setSource(1);// 自动配置
		newappAlbumRes.setOperator("appannie");
		List<AppAlbumRes> appAlbumResautolist = appAlbumResTempService
				.getByFileColumnAndSource(appInfo, appAlbumColumn,
						autoSource, country.getId());
		if(appAlbumResautolist.size()>0){
			appAlbumResTempService.delAppAlbumResTemp(appAlbumResautolist.get(0).getId());
		}
		
		appAlbumResTempService.addAppAlbumResTemp(newappAlbumRes);
	}	
	
	/**
	 * 计算appannie的分数
	 * @param map0
	 * @param i
	 * @param listType
	 * @param appInfo
	 * @param appScore
	 * @return
	 */
	private TopVO getAppAnnieScore(Map<String, Object> map0, double i,
			int listType, AppInfo appInfo,AppFile appFile, AppannieService appScore) {
		TopVO topVO = new TopVO();
		// 排行榜
		topVO.setAnnieRank(i + 1);
		// annie 上的评分
		if(map0.get(AppBatchDistributionConstant.RATER)!=null){
			topVO.setAnnieRatings((double) map0.get(AppBatchDistributionConstant.RATER));			
		}else{
			topVO.setAnnieRatings(4.0);	
		}
		
		if(topVO.getAnnieRatings()==0.0){
			TopVO vo = new TopVO();
			vo.setScore(0.0);
			return vo;
		}
		// 安装量
		if(map0.get(AppBatchDistributionConstant.INSTALLTOTAL)!=null){
			topVO.setAnnieInstallTotal((double) map0
					.get(AppBatchDistributionConstant.INSTALLTOTAL));			
		}else{
			TopVO vo = new TopVO();
			vo.setScore(0.0);
			return vo;
		}
		// app大小
		if((double)map0.get(AppBatchDistributionConstant.SIZE)==0.0){
			topVO.setSize((double)appFile.getFileSize());
		}else{
			topVO.setSize((double) map0.get(AppBatchDistributionConstant.SIZE));			
		}
		//应用是自营
		Date initialReleaseDate =null;
		if(appInfo.getFree()==2){
			initialReleaseDate = appInfo.getCreateTime();
		}else{			
			initialReleaseDate = (Date) map0
					.get(AppBatchDistributionConstant.INITIALRELEASEDATE);
			
		}
		double initialTime = 0;
		if(map0.get(AppBatchDistributionConstant.INITIALRELEASEDATE)!=null){
			initialTime = new Date().getTime()
					- initialReleaseDate.getTime();			
			initialTime = initialTime / 1000 / 60 / 60 / 24;
		}else{
			TopVO vo = new TopVO();
			vo.setScore(0.0);
			return vo;
		}
		// 初始版本发布至今的天数
		topVO.setInitialTime(initialTime);
		// 平均每天的安装量
		topVO.setAnnieInstallAverage(topVO
				.getAnnieInstallTotal()
				/ topVO.getInitialTime());
		if(appInfo.getCategory()!=null){
			topVO.setCategoryId(appInfo.getCategory()
					.getId());
		}
		topVO.setPageviews(appInfo.getPageOpen());
		topVO.setRealDownload(appInfo.getRealDowdload());
		topVO.setRatings(appInfo.getStars());
		topVO.setDownloadAverage(appInfo.getRealDowdload()/topVO.getInitialTime());

		// 每个类别都得计算一次分数
		TopVO vo = appScore.setScore(topVO, listType);
		return vo;
	}
	/**
	 * 获取分发专辑等级
	 * @param appAlbumColumn
	 * @return
	 */
	public int getListType(AppAlbumColumn appAlbumColumn){
		int listType = 0;
		switch (appAlbumColumn.getColumnId()) {
		case 1:
			listType = 1;
			break;
		case 2:
			listType = 2;
			break;
		case 3:
			listType = 3;
			break;
		case 4:
			listType = 4;
			break;
		case 12:
			listType = 5;
			break;
		case 13:
			listType = 6;
			break;
		case 14:
			listType = 7;
			break;
		case 22:
			listType = 8;
			break;
		case 23:
			listType = 9;
			break;
		case 24:
			listType = 10;
			break;
		}
		return listType;
	}
	public void init() {
		if (ConstantScore.map.get("configList") == null) {
			// 启用初始
			controller.initAppConfig();
		}
	}
}
