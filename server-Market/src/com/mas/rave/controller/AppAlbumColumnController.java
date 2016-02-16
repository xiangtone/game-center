package com.mas.rave.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.mas.rave.appannie.NameMappingSerivce;
import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.AppAlbum;
import com.mas.rave.main.vo.AppAlbumColumn;
import com.mas.rave.main.vo.AppAlbumRes;
import com.mas.rave.main.vo.AppCollection;
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.main.vo.AppScore;
import com.mas.rave.main.vo.Category;
import com.mas.rave.main.vo.Country;
import com.mas.rave.main.vo.CountryExample;
import com.mas.rave.main.vo.User;
import com.mas.rave.report.ExportExcelView;
import com.mas.rave.report.XMLModelTemplate;
import com.mas.rave.service.AppAlbumColumnService;
import com.mas.rave.service.AppAlbumResService;
import com.mas.rave.service.AppAlbumResTempService;
import com.mas.rave.service.AppAlbumService;
import com.mas.rave.service.AppCollectionResService;
import com.mas.rave.service.AppCollectionService;
import com.mas.rave.service.AppFileService;
import com.mas.rave.service.AppInfoConfigService;
import com.mas.rave.service.AppInfoRankService;
import com.mas.rave.service.AppInfoService;
import com.mas.rave.service.AppScoreService;
import com.mas.rave.service.AppannieInfoBaseService;
import com.mas.rave.service.AppannieInfoCountryRankService;
import com.mas.rave.service.AppannieInfoService;
import com.mas.rave.service.CategoryService;
import com.mas.rave.service.CountryService;
import com.mas.rave.service.ProxyIPService;
import com.mas.rave.task.DealAppAnnieCountryRank;
import com.mas.rave.task.DealAppBatchDistribution;
import com.mas.rave.util.Constant;
import com.mas.rave.util.ConstantScore;
import com.mas.rave.util.CrawlerFileUtils;
import com.mas.rave.util.DateUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.StringEncoder;
import com.mas.rave.vo.AppBatchDistributionVO;

/**
 * 分发业务处理　
 * 
 * @author liwei.sz
 * 
 */
@Controller
@RequestMapping("/appAlbumColumn")
public class AppAlbumColumnController {
	public static final int fatherChannelId=200000;
	public static final int channelId=200001;
	public static final int cpId=300001;
	public static final int handSource = 0;
	public static final int autoSource = 1;
	public static final int rankAll=10000;
	private final String FTP_APK_PATH = "/home/ftpapk/";
	private static String filename="/usr/local/mas/content_log/appFileStateIsFalseOrappFileNotExist.log";
	public static Map<String,User> map = Collections.synchronizedMap(new HashMap<String,User>());
	public static LinkedBlockingQueue<AppBatchDistributionVO> distributionQueue = new LinkedBlockingQueue<AppBatchDistributionVO>();
	public static LinkedBlockingQueue<AppBatchDistributionVO> appannieCountryRankQueue = new LinkedBlockingQueue<AppBatchDistributionVO>();
	public static Map<String,User> mapAppannieCountryRank = Collections.synchronizedMap(new HashMap<String,User>());

	Logger log = Logger.getLogger(AppAlbumColumnController.class);
	@Autowired
	private AppAlbumColumnService appAlbumColumnService;

	@Autowired
	private AppAlbumService appAlbumService;

	@Autowired
	private AppAlbumResTempService appAlbumResTempService;
	
	@Autowired
	private AppAlbumResService	appAlbumResService;

	@Autowired
	private AppFileService appFileService;

	@Autowired
	private AppInfoService appInfoService;

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private AppInfoConfigService appInfoConfigService;

	@Autowired
	private AppInfoRankService appInfoRankService;
	
	
	@Autowired
	private CountryService countryService;
	
	@Autowired
	private PropertiesController controller;
	
	@Autowired
	private ProxyIPService proxyIPService;
	@Autowired
	private AppScoreService appScoreService;
	
	@Autowired
	private AppCollectionService appCollectionService;
	
	@Autowired
	private AppannieInfoService appannieInfoService;
	
	@Autowired
	private AppCollectionResService appCollectionResService;
	
	@Autowired
	private AppannieInfoBaseService appannieInfoBaseService;
	
	@Autowired
	private AppannieInfoCountryRankService appannieInfoCountryRankService;
	/**
	 * 分页查看app分发信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
			AppAlbumColumnService.AppAlbumColumnCriteria criteria = new AppAlbumColumnService.AppAlbumColumnCriteria();
			// 根据应用名查看
			String appAlbumId = request.getParameter("appAlbumId");
//			String raveId = request.getParameter("raveId");

			if (StringUtils.isNotEmpty(appAlbumId) && !appAlbumId.equals("0")) {
				criteria.albumIdEquaTo(Integer.parseInt(appAlbumId));
			} else {
				criteria.flagEquaTo(1);
			}
//			
//			if (StringUtils.isNotEmpty(raveId)) {
//				int raveId1 = Integer.parseInt(raveId.trim());
//				Country country = countryService.getCountry(raveId1);
//				request.setAttribute("countryNow", country);
//			}
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize",20);

			PaginationVo<AppAlbumColumn> result = appAlbumColumnService.searchAppAlbumColumn(criteria, currentPage, pageSize);
			request.setAttribute("result", result);
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);
			// 设置专题
			List<AppAlbum> appAlbums = appAlbumService.getAppAlbum();
			request.setAttribute("appAlbums", appAlbums);

		} catch (Exception e) {
			log.error("AppAlbumColumnController list", e);
			PaginationVo<AppAlbumColumn> result = new PaginationVo<AppAlbumColumn>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "appAlbumColumn/list";
	}	
	/**
	 *	生效分发列表
	 * 对应t_app_album_res表
	 * @param request
	 * @return
	 */
	@RequestMapping("/show/{id}")
	public String show(HttpServletRequest request, @PathVariable("id") Integer id) {
		try {
			AppAlbumResService.AppAlbumResCriteria criteria = new AppAlbumResService.AppAlbumResCriteria();
			criteria.columnIdEqualTo(id);
			// 根据应用名查看
			String name = request.getParameter("name");
			if (StringUtils.isNotEmpty(name)) {
				name =StringEncoder.encode(name.trim());
				criteria.nameLike(name);
			}
			String source = request.getParameter("source");
			if (StringUtils.isNotEmpty(source)&&!source.trim().equals("-1")) {
				int source1 = Integer.parseInt(source.trim());
				criteria.sourceEqualTo(source1);
			}
			String raveId = request.getParameter("raveId");
			if (StringUtils.isNotEmpty(raveId)) {
				int raveId1 = Integer.parseInt(raveId.trim());
				criteria.raveIdEqualTo(raveId1);
				Country country = countryService.getCountry(raveId1);
				request.setAttribute("country", country);
			}
			request.setAttribute("raveId", raveId);

			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<AppAlbumRes> result = appAlbumResService.searchAlbumRess(criteria, currentPage, pageSize);
			request.setAttribute("result", result);

			// 设置实体
			AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(id);
			request.setAttribute("appAlbumColumn", appAlbumColumn);

			// 所有一级分类信息
			List<Category> categorys = categoryService.getCategorys(1);
			request.setAttribute("categorys", categorys);
			String temp  =  request.getParameter("temp");
			request.setAttribute("temp", temp);
		} catch (Exception e) {
			log.error("AppAlbumColumnController show", e);
			PaginationVo<AppAlbumRes> result = new PaginationVo<AppAlbumRes>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "appAlbumColumn/show";
	}
	
	@RequestMapping("/export2Excel/{id}")
	public ModelAndView exportToExcel(HttpServletRequest request,HttpServletResponse response, @PathVariable("id") Integer id){
		XMLModelTemplate template = ExportExcelView.getTemplate(request, response);
		Map<String, Object> map = new HashMap<String, Object>();
		
		AppAlbumResService.AppAlbumResCriteria criteria = new AppAlbumResService.AppAlbumResCriteria();
		criteria.columnIdEqualTo(id);
		// 根据应用名查看
		String temp  =  request.getParameter("temp");
		String name = request.getParameter("name");
		try {
			name = new String(name.getBytes("iso8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			
		}
		if (StringUtils.isNotEmpty(name)) {
			criteria.nameLike(name.trim());
		}
		String source = request.getParameter("source");
		if (StringUtils.isNotEmpty(source)&&!source.trim().equals("-1")) {
			try{
			int source1 = Integer.parseInt(source.trim());
			criteria.sourceEqualTo(source1);
			} catch (Exception e) {
				log.error("AppAlbumColumnController exportToExcel", e);
			}
		}
		String raveId = request.getParameter("raveId");
		if (StringUtils.isNotEmpty(raveId)) {
			int raveId1 = Integer.parseInt(raveId.trim());
			criteria.raveIdEqualTo(raveId1);
		}
		List<AppAlbumRes> dataList = appAlbumResService.getAppAlbumRes(criteria);
		map.put("list", dataList);
		map.put("template", template);
		request.setAttribute("temp", temp);
		//生成excel文件并返回给页面
		return new ModelAndView(new ExportExcelView(),map);
	}
	@ResponseBody
	@RequestMapping(value = "/doOperant/{id}", method = RequestMethod.POST)
	public String doOperant(HttpServletRequest request, @PathVariable("id") Integer id,@RequestParam("raveId") String raveId) {	
		AppAlbumColumn appAlbumColumn =  appAlbumColumnService.getAppAlbumColumn(id);

		//add by lulin 20140507
		String currentAlbum = countryService.getCountry(Integer.parseInt(raveId)).getName() +"-"+ appAlbumColumn.getAppAlbum().getName();
		if(map.get(currentAlbum)!=null){
			return "{\"flag\":\"2\"}"; //分发程序未运行完，不能生效
		}
		//===============================
		
		try{
			AppAlbumRes appAlbumRes =new AppAlbumRes();
			appAlbumRes.setAppAlbumColumn(appAlbumColumn);
			int raveId1 = 1;
			if (StringUtils.isNotEmpty(raveId)) {
				raveId1 = Integer.parseInt(raveId.trim());
				appAlbumRes.setRaveId(raveId1);
			}
			appAlbumResTempService.doOperant(appAlbumRes);
			//生成历史排名  add by lin.lu
			appInfoRankService.updateRank(id,raveId1);
			
			return "{\"flag\":\"0\"}";

		} catch (Exception e) {
			log.error("AppAlbumColumnController doOperant", e);
			return "{\"flag\":\"1\"}";

		}
	}
	@ResponseBody
	@RequestMapping(value = "/batchOperant/{id}", method = RequestMethod.POST)
	public String batchOperant(HttpServletRequest request, @PathVariable("id") Integer columnId, @RequestParam("ids") String ids) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		try{
			appAlbumResTempService.doBatchOperant(idIntArray, columnId);
			return "{\"flag\":\"0\"}";

		} catch (Exception e) {
			log.error("AppAlbumColumnController doOperant", e);
			return "{\"flag\":\"1\"}";

		}
	}
	/**
	 * 对应分发信息
	 * 对应t_app_album_res_temp表
	 * @param request
	 * @return
	 */
	@RequestMapping("/listRes/{id}")
	public String listRes(HttpServletRequest request, @PathVariable("id") Integer id) {
		try {
			AppAlbumResTempService.AppAlbumResTempCriteria criteria = new AppAlbumResTempService.AppAlbumResTempCriteria();
			criteria.columnIdEqualTo(id);
			// 根据应用名查看
			String name = request.getParameter("name");
			if (StringUtils.isNotEmpty(name)) {
				name =StringEncoder.encode(name.trim());
				criteria.nameLike(name);
			}
			String source = request.getParameter("source");
			if (StringUtils.isNotEmpty(source)&&!source.trim().equals("-1")) {
				int source1 = Integer.parseInt(source.trim());
				criteria.sourceEqualTo(source1);
			}
			String raveId= request.getParameter("raveIds");
			if(StringUtils.isEmpty(raveId)){
				raveId=request.getParameter("raveId");
			}
			//String raveId = request.getParameter("raveId");
			if (StringUtils.isNotEmpty(raveId)) {
				int raveId1 = Integer.parseInt(raveId.trim());
				criteria.raveIdEqualTo(raveId1);
				Country country = countryService.getCountry(raveId1);
				request.setAttribute("country", country);
			}
			criteria.flagEqualTo(0);
			request.setAttribute("raveId", raveId);

			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<AppAlbumRes> result = appAlbumResTempService.searchAlbumResTemps(criteria, currentPage, pageSize);
			request.setAttribute("result", result);

			// 设置实体
			AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(id);
			request.setAttribute("appAlbumColumn", appAlbumColumn);

			// 所有一级分类信息
			List<Category> categorys = categoryService.getCategorys(1);
			request.setAttribute("categorys", categorys);
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);					
		   return "appAlbumColumn/listRes";
						
		} catch (Exception e) {
			log.error("AppAlbumColumnController update", e);
			PaginationVo<AppAlbumRes> result = new PaginationVo<AppAlbumRes>(null, 0, 10, 1);
			request.setAttribute("result", result);
			
		}
		return "appAlbumColumn/listRes";
	}
	@RequestMapping("/exportlistRes2Excel/{id}")
	public ModelAndView exportlistRes2Excel(HttpServletRequest request,HttpServletResponse response, @PathVariable("id") Integer id){
		XMLModelTemplate template = ExportExcelView.getTemplate(request, response);
		Map<String, Object> map = new HashMap<String, Object>();
		
		AppAlbumResTempService.AppAlbumResTempCriteria criteria = new AppAlbumResTempService.AppAlbumResTempCriteria();
		criteria.columnIdEqualTo(id);
		// 根据应用名查看
		String name = request.getParameter("name");
		try {
			name = new String(name.getBytes("iso8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			
		}
		if (StringUtils.isNotEmpty(name)) {
			criteria.nameLike(name.trim());
		}
		String source = request.getParameter("source");
		if (StringUtils.isNotEmpty(source)&&!source.trim().equals("-1")) {
			try{
			int source1 = Integer.parseInt(source.trim());
			criteria.sourceEqualTo(source1);
			} catch (Exception e) {
				log.error("AppAlbumColumnController exportToExcel", e);
			}
		}
		String raveId = request.getParameter("raveId");
		if (StringUtils.isNotEmpty(raveId)) {
			int raveId1 = Integer.parseInt(raveId.trim());
			criteria.raveIdEqualTo(raveId1);
		}
		List<AppAlbumRes> dataList = appAlbumResTempService.getAppAlbumResTemp(criteria);
		map.put("list", dataList);
		map.put("template", template);
		//生成excel文件并返回给页面
		return new ModelAndView(new ExportExcelView(),map);
	}
	@RequestMapping("/exporthand2Excel/{id}")
	public ModelAndView exportHand2Excel(HttpServletRequest request,HttpServletResponse response, @PathVariable("id") Integer id){
		XMLModelTemplate template = ExportExcelView.getTemplate(request, response);
		Map<String, Object> map = new HashMap<String, Object>();
		
		AppAlbumResTempService.AppAlbumResTempCriteria criteria = new AppAlbumResTempService.AppAlbumResTempCriteria();
		criteria.columnIdEqualTo(id);
		// 根据应用名查看
		String temp  =  request.getParameter("temp");
		String name = request.getParameter("name");
		try {
			name = new String(name.getBytes("iso8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			name="";
			
		}
		if (StringUtils.isNotEmpty(name)) {
			criteria.nameLike(name.trim());
		}	
		
		criteria.sourceEqualTo(handSource);//			
		String raveId = request.getParameter("raveId");
		if (StringUtils.isNotEmpty(raveId)) {
			int raveId1 = Integer.parseInt(raveId.trim());
			criteria.raveIdEqualTo(raveId1);
		}
		List<AppAlbumRes> dataList = appAlbumResTempService.getAppAlbumResTemp(criteria);
		map.put("list", dataList);
		map.put("template", template);
		request.setAttribute("temp", temp);
		//生成excel文件并返回给页面
		return new ModelAndView(new ExportExcelView(),map);
	}
	@RequestMapping("/{id}/config")
	public String toConfig(HttpServletRequest request, @PathVariable("id") Integer id) {
		try {
			String temp  =  request.getParameter("temp");
			request.setAttribute("temp", temp);
			String appnameid = request.getParameter("appnameid");
			String categoryId = request.getParameter("categoryId") == null ? "0" : request.getParameter("categoryId");		
			String category_parent = request.getParameter("category_parent") == null ? "0" : request.getParameter("category_parent");
			request.setAttribute("categoryId", categoryId);
			request.setAttribute("category_parent", category_parent);
			// 获取标签
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", 48);

			AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(id);
			request.setAttribute("appAlbumColumn", appAlbumColumn);
			// 当前所有apk信息
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (null != appnameid && !"".equals(appnameid)) {
				try{
					int appid = Integer.parseInt(appnameid.trim());
					map.put("appid", appnameid.trim());
				} catch (Exception e) {
					map.put("appname", appnameid.trim());
				}
			}
			request.setAttribute("appnameid", appnameid);
			if (!"0".equals(categoryId)) {
				map.put("categoryId", Integer.parseInt(categoryId));
			} else if (!"0".equals(category_parent)) {
				map.put("category_parent", Integer.parseInt(category_parent));
			}
			map.put("columnId", id);
			String raveId = request.getParameter("raveId");
			if (StringUtils.isNotEmpty(raveId)) {
				int raveId1 = Integer.parseInt(raveId.trim());
				map.put("raveId", raveId1);
				Country country = countryService.getCountry(raveId1);
				request.setAttribute("country", country);
			}
			request.setAttribute("raveId", raveId);
			PaginationVo<AppAlbumRes> result = appAlbumResTempService.getSelectAppFiles(map, currentPage, pageSize);		
			request.setAttribute("result", result);
			int size = result.getData().size();
			request.setAttribute("counts", size % 4 == 0 ? size / 4 : (size / 4) + 1);
			return "appAlbumColumn/config";
			
		} catch (Exception e) {
			log.error("AppAlbumColumnController toConfig", e);
			PaginationVo<AppAlbumRes> result = new PaginationVo<AppAlbumRes>(null, 0, 48, 1);
			request.setAttribute("result", result);
			return "appAlbumColumn/config";
		}
	
	}
	@RequestMapping(value = "/judgeAlbumIsRun", method = RequestMethod.POST)
	@ResponseBody
	public String judgeAlbumIsRun(@RequestParam("raveId") Integer raveId,@RequestParam("columnId") Integer columnId) {
		AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(columnId);
		String currentAlbum = countryService.getCountry(raveId).getName() +"-"+ appAlbumColumn.getAppAlbum().getName();
		if(map.get(currentAlbum)!=null){
			return "{\"flag\":\"2\"}"; //分发程序未运行完，不能生效
		}
		return "{\"flag\":\"0\"}";
	}

	@RequestMapping(value = "/{id}/doConfig", method = RequestMethod.POST)
	@ResponseBody
	public String doConfig(@RequestParam("raveId") Integer raveId,@RequestParam("columnId") Integer columnId,@RequestParam("albumId") Integer albumId, @RequestParam(value = "menus", required = false) List<Integer> ids) {
		AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(columnId);
		String currentAlbum = countryService.getCountry(raveId).getName() +"-"+ appAlbumColumn.getAppAlbum().getName();
		if(map.get(currentAlbum)!=null){
			return "{\"flag\":\"2\"}"; //分发程序未运行完，不能生效
		}
		
		//分发来源，0手动，1自动
		int source = 0;
		try {
			appAlbumResTempService.resetAppAlbumResTemp(raveId,columnId,albumId,source,ids);
		} catch (Exception e) {
			log.error("AppAlbumColumnController doConfig", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}
	/**
	 * 对应分发信息
	 * 对应t_app_album_res表
	 * @param request
	 * @return
	 */
	@RequestMapping("/liveWallpaperListRes/{id}")
	public String liveWallpaperListRes(HttpServletRequest request, @PathVariable("id") Integer id) {
		try {

			AppAlbumResService.AppAlbumResCriteria criteria = new AppAlbumResService.AppAlbumResCriteria();
			criteria.columnIdEqualTo(id);
			// 根据应用名查看
			String name = request.getParameter("name");
			if (StringUtils.isNotEmpty(name)) {
				name =StringEncoder.encode(name.trim());
				criteria.nameLike(name);
			}
			String source = request.getParameter("source");
			if (StringUtils.isNotEmpty(source)&&!source.trim().equals("-1")) {
				int source1 = Integer.parseInt(source.trim());
				criteria.sourceEqualTo(source1);
			}
			String raveId= request.getParameter("raveIds");
			if(StringUtils.isEmpty(raveId)){
				raveId=request.getParameter("raveId");
			}
			//String raveId = request.getParameter("raveId");
			if (StringUtils.isNotEmpty(raveId)) {
				int raveId1 = Integer.parseInt(raveId.trim());
				criteria.raveIdEqualTo(raveId1);
				Country country = countryService.getCountry(raveId1);
				request.setAttribute("country", country);
			}
			request.setAttribute("raveId", raveId);

			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<AppAlbumRes> result = appAlbumResService.searchAlbumRess(criteria, currentPage, pageSize);
			request.setAttribute("result", result);

			// 设置实体
			AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(id);
			request.setAttribute("appAlbumColumn", appAlbumColumn);

			// 所有一级分类信息
			List<Category> categorys = categoryService.getCategorys(1);
			request.setAttribute("categorys", categorys);
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);					
		} catch (Exception e) {
			log.error("AppAlbumColumnController update", e);
			PaginationVo<AppAlbumRes> result = new PaginationVo<AppAlbumRes>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "appAlbumColumn/liveWallpaperListRes";

	}
	@RequestMapping("/{id}/liveWallpapertoConfig")
	public String liveWallpapertoConfig(HttpServletRequest request, @PathVariable("id") Integer id) {
		try {
			String temp  =  request.getParameter("temp");
			request.setAttribute("temp", temp);
			String appnameid = request.getParameter("appnameid");
			String categoryId ="45";
			// 获取标签
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", 48);

			AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(id);
			request.setAttribute("appAlbumColumn", appAlbumColumn);
			// 当前所有apk信息
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (null != appnameid && !"".equals(appnameid)) {
				try{
					int appid = Integer.parseInt(appnameid.trim());
					map.put("appid", appnameid.trim());
				} catch (Exception e) {
					map.put("appname", appnameid.trim());
				}
			}
			request.setAttribute("appnameid", appnameid);
			if (!"0".equals(categoryId)) {
				map.put("categoryId", Integer.parseInt(categoryId));
			}
			map.put("columnId", id);
			String raveId = request.getParameter("raveId");
			if (StringUtils.isNotEmpty(raveId)) {
				int raveId1 = Integer.parseInt(raveId.trim());
				map.put("raveId", raveId1);
				Country country = countryService.getCountry(raveId1);
				request.setAttribute("country", country);
			}
			request.setAttribute("raveId", raveId);	
			PaginationVo<AppAlbumRes> result = appAlbumResService.getSelectAppFiles(map, currentPage, pageSize);
			request.setAttribute("result", result);
			int size = result.getData().size();
			request.setAttribute("counts", size % 4 == 0 ? size / 4 : (size / 4) + 1);
			
		} catch (Exception e) {
			log.error("AppAlbumColumnController toConfig", e);
			PaginationVo<AppAlbumRes> result = new PaginationVo<AppAlbumRes>(null, 0, 48, 1);
			request.setAttribute("result", result);
		}
		return "appAlbumColumn/liveWallpaperConfig";

	}
	@RequestMapping(value = "/{id}/liveWallpaperDoConfig", method = RequestMethod.POST)
	@ResponseBody
	public String liveWallpaperDoConfig(@RequestParam("raveId") Integer raveId,@RequestParam("columnId") Integer columnId,@RequestParam("albumId") Integer albumId, @RequestParam(value = "menus", required = false) List<Integer> ids) {					
		try {
			appAlbumResService.resetAppAlbumRes(raveId,null,columnId,albumId,ids);
		} catch (Exception e) {
			log.error("AppAlbumColumnController doConfig", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}
	/** 删除app分类 */
	@ResponseBody
	@RequestMapping("/liveWallpaperDelete")
	public String liveWallpaperDelete(@RequestParam("id") String ids) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		appAlbumResService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}
	/**
	 *	应用专题分发显示
	 * 对应t_app_collection_res表
	 * @param request
	 * @return
	 */
	@RequestMapping("/collectionRes/{id}")
	public String collectionRes(HttpServletRequest request, @PathVariable("id") Integer id,@RequestParam("type") Integer type) {
		try {
			AppCollectionResService.AppCollectionResCriteria criteria = new AppCollectionResService.AppCollectionResCriteria();
			
			criteria.collectionIdEqualTo(id);
			// 根据应用名查看
			String name = request.getParameter("name");
			if (StringUtils.isNotEmpty(name)) {
				name =StringEncoder.encode(name.trim());
				criteria.nameLike(name);
			}
			
			String raveId = request.getParameter("raveId");
			if (StringUtils.isNotEmpty(raveId)) {
				int raveId1 = Integer.parseInt(raveId.trim());
				criteria.raveIdEqualTo(raveId1);
				Country country = countryService.getCountry(raveId1);
				request.setAttribute("country", country);
			}
			request.setAttribute("raveId", raveId);
			request.setAttribute("type", type);
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);
			PaginationVo<AppAlbumRes> result=null;
			if(type==2){
				result = appCollectionResService.searchAlbumRess1(criteria, currentPage, pageSize);
			}else{
				result = appCollectionResService.searchAlbumRess(criteria, currentPage, pageSize);
			}
			request.setAttribute("result", result);

			// 设置实体
			AppCollection appCollection = appCollectionService.getAppCollection(id);
			request.setAttribute("appCollection", appCollection);
			// 所有一级分类信息
			List<Category> categorys = categoryService.getCategorys(1);
			request.setAttribute("categorys", categorys);
		} catch (Exception e) {
			log.error("AppAlbumColumnController show", e);
			PaginationVo<AppAlbumRes> result = new PaginationVo<AppAlbumRes>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		if(type==1){
			return "appCollection/listRes";
		}else{
			return "appMusthave/listRes";
		}	
	}
	
	@RequestMapping("/{id}/collectionConfig")
	public String collectionToConfig(HttpServletRequest request, @PathVariable("id") Integer id,@RequestParam("type") Integer type) {
		try {
			String appnameid = request.getParameter("appnameid");
			String categoryId = request.getParameter("categoryId") == null ? "0" : request.getParameter("categoryId");
			String category_parent = request.getParameter("category_parent") == null ? "0" : request.getParameter("category_parent");
			request.setAttribute("categoryId", categoryId);
			request.setAttribute("category_parent", category_parent);
			// 获取标签
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", 48);

			// 设置实体
			AppCollection appCollection = appCollectionService.getAppCollection(id);
			request.setAttribute("appCollection", appCollection);
			request.setAttribute("type", type);
			// 当前所有apk信息
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("collectionId", id);
			
			if (null != appnameid && !"".equals(appnameid)) {
				try{
					int appid = Integer.parseInt(appnameid.trim());
					map.put("appid", appnameid.trim());
				} catch (Exception e) {
					map.put("appname", appnameid.trim());
				}
			}
			request.setAttribute("appnameid", appnameid);
			if (!"0".equals(categoryId)) {
				map.put("categoryId", Integer.parseInt(categoryId));
			} else if (!"0".equals(category_parent)) {
				map.put("category_parent", Integer.parseInt(category_parent));
			}
			String raveId = request.getParameter("raveId");
			if (StringUtils.isNotEmpty(raveId)) {
				int raveId1 = Integer.parseInt(raveId.trim());
				map.put("raveId", raveId1);
				Country country = countryService.getCountry(raveId1);
				request.setAttribute("country", country);
			}
			request.setAttribute("raveId", raveId);
			PaginationVo<AppAlbumRes> result = appCollectionResService.getSelectAppFiles(map, currentPage, pageSize);
			request.setAttribute("result", result);
			int size = result.getData().size();
			request.setAttribute("counts", size % 4 == 0 ? size / 4 : (size / 4) + 1);

		} catch (Exception e) {
			log.error("AppAlbumColumnController toConfig", e);
			PaginationVo<AppAlbumRes> result = new PaginationVo<AppAlbumRes>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		if(type==1){
			return "appCollection/config";
		}else{
			return "appMusthave/config";
		}
	}

	@RequestMapping(value = "/{id}/collectionDoConfig", method = RequestMethod.POST)
	@ResponseBody
	public String collectionDoConfig(@RequestParam("raveId") Integer raveId,@PathVariable("id") Integer collectionId, @RequestParam(value = "menus", required = false) List<Integer> ids,
			@RequestParam("type") Integer type) {
		try {
			appCollectionResService.resetAppAlbumRes(raveId,collectionId,null,null,ids);
		} catch (Exception e) {
			log.error("AppAlbumColumnController doConfig", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	
	/** 删除app分类 */
	@ResponseBody
	@RequestMapping("/collectionDelete")
	public String collectionDelete(@RequestParam("id") String ids,@RequestParam("type") Integer type) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		appCollectionResService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}
	/**
	 * 对应手动分发信息
	 * 对应t_app_album_res_temp表
	 * @param request
	 * @return
	 */
	@RequestMapping("/handListRes/{id}")
	public String handListRes(HttpServletRequest request, @PathVariable("id") Integer id) {
		try {
			AppAlbumResTempService.AppAlbumResTempCriteria criteria = new AppAlbumResTempService.AppAlbumResTempCriteria();
			criteria.columnIdEqualTo(id);
			// 根据应用名查看
			String temp  =  request.getParameter("temp");
			request.setAttribute("temp", temp);
			String name = request.getParameter("name");
			if (StringUtils.isNotEmpty(name)) {
				name =StringEncoder.encode(name.trim());
				criteria.nameLike(name);
			}
			//手动
			criteria.sourceEqualTo(handSource);
			
			String raveId= request.getParameter("raveId");
			//String raveId = request.getParameter("raveId");
			if (StringUtils.isNotEmpty(raveId)) {
				int raveId1 = Integer.parseInt(raveId.trim());
				criteria.raveIdEqualTo(raveId1);
				Country country = countryService.getCountry(raveId1);
				request.setAttribute("country", country);
			}
			request.setAttribute("raveId", raveId);
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
			criteria.flagEqualTo(0);
			request.setAttribute("defaultCountry", defaultCountry);
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", 50);

			PaginationVo<AppAlbumRes> result = appAlbumResTempService.searchAlbumResTemps(criteria, currentPage, pageSize);
			request.setAttribute("result", result);

			// 设置实体
			AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(id);
			request.setAttribute("appAlbumColumn", appAlbumColumn);

			// 所有一级分类信息
			List<Category> categorys = categoryService.getCategorys(1);
			request.setAttribute("categorys", categorys);
			
		} catch (Exception e) {
			log.error("AppAlbumColumnController update", e);
			PaginationVo<AppAlbumRes> result = new PaginationVo<AppAlbumRes>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "appAlbumColumn/handListRes";
	}
	/**
	 * 获取指定分发类型的手动排行的数据并合并修改的排名生成新排行
	 * @param paramstr
	 * @param appAlbumResList
	 * @return
	 */
	private Map<Integer,Integer> getNewDataSourceRanking(String paramstr,
			List<AppAlbumRes> appAlbumResList) {
		Map<Integer,Integer> mapDataSource = new HashMap<Integer, Integer>();
		for(AppAlbumRes appAlbumRes:appAlbumResList){
			mapDataSource.put(appAlbumRes.getId(), appAlbumRes.getRanking());
	    }
		String[] params = paramstr.split("&");
		for (int i = 0; i < params.length; i++) {
			String[] param = params[i].split("#");
			int id = Integer.parseInt(param[0]);
			Integer ranking = null;
			try{
				ranking =  Integer.parseInt(param[1]);
			}catch(Exception e1){
				//排名不是正整数
				ranking=null;
			}
			//更新mapDataSource中的ranking值
			mapDataSource.put(id, ranking);
		}
		return mapDataSource;
	}
	/**
	 * 判断排名是否有重复
	 * @param entries
	 * @return
	 */
	private boolean isRankingRepeat(Map<Integer,Integer> mapDataSource){
		//第二步遍历当mapDataSource,判断是否有重复的ranking值
		Set<Entry<Integer,Integer>> entries = mapDataSource.entrySet();
		for (Entry<Integer,Integer> entry : entries) { 
			for (Entry<Integer,Integer> entry1 : entries) { 
				if(entry.getKey()!=entry1.getKey()&&
						(entry.getValue()!=null&&entry1.getValue()!=null&&entry.getValue()==entry1.getValue())){
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * 更新排名
	 * @param appAlbumColumn
	 * @param maphand map key=ranking value=AppAlbumRes
	 * @param raveId
	 */
	private void autoUploadHandSort(AppAlbumColumn appAlbumColumn,int raveId) throws Exception{	
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
	@ResponseBody
	@RequestMapping(value = "/updateRanking/{columnId}", method = RequestMethod.POST)
	public String updateRanking(@PathVariable("columnId") Integer columnId,@RequestParam("raveId") Integer raveId,
			@RequestParam("paramstr") String paramstr){
	
		String[] params = paramstr.split("&");
		//上传的排名修改的数据
		Map<Integer,Integer> mapNewSource = new HashMap<Integer, Integer>();
		for (int i = 0; i < params.length; i++) {
			String[] param = params[i].split("#");
			int id = Integer.parseInt(param[0]);
			Integer ranking =null;		
			if(param.length==2&&param[1]!=null&&!param[1].equals("")&&!param[1].equals("0")){
				try{
					ranking = Integer.parseInt(param[1]);
				}catch(Exception e1){
					//排名不是自然数
					return "{\"success\":\"2\"}";
				}			
		   }
			
			mapNewSource.put(id, ranking);
		}
		AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(columnId);
		List<AppAlbumRes> appAlbumResList = appAlbumResTempService.getByFileColumnAndSource(null, appAlbumColumn, handSource, raveId);
		Map<Integer,Integer> mapDataSource =  getNewDataSourceRanking(paramstr, appAlbumResList);
		
		if(!isRankingRepeat(mapDataSource)){
			//有重复排名 
			return "{\"success\":\"4\"}";
		}	
		//插入ranking
		for (Integer id:mapNewSource.keySet()) { 
			AppAlbumRes appAlbumResTemp  =new AppAlbumRes();
			appAlbumResTemp.setId(id);
			appAlbumResTemp.setRanking(mapNewSource.get(id));
			//设置手动分数
			if(mapNewSource.get(id)!=null&&mapNewSource.get(id)!=0){				
				appAlbumResTemp.setSort(rankAll-mapNewSource.get(id));
			}
			appAlbumResTempService.updateRankingByPrimarykey(appAlbumResTemp);
			appAlbumResTempService.updateSortByPrimarykey(appAlbumResTemp);
		}
		
		//计算新的sort,重新排名
		try {
			autoUploadHandSort(appAlbumColumn,raveId);
			return "{\"success\":\"1\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("AppAlbumColumnController updateRanking", e);
			return "{\"success\":\"3\"}";
		}

	}
	@ResponseBody
	@RequestMapping(value = "/clearRanking/{columnId}", method = RequestMethod.POST)
	public String clearRanking(@PathVariable("columnId") Integer columnId,@RequestParam("raveId") Integer raveId){
		try{
			AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(columnId);		
			List<AppAlbumRes> appAlbumResList = appAlbumResTempService.getByFileColumnAndSource(null, appAlbumColumn, handSource, raveId);
			for(AppAlbumRes appAlbumRes:appAlbumResList){
				AppAlbumRes appAlbumResTemp  =new AppAlbumRes();
				appAlbumResTemp.setId(appAlbumRes.getId());
				appAlbumResTemp.setRanking(null);
				appAlbumResTemp.setSort(0.0);
				appAlbumResTempService.updateRankingByPrimarykey(appAlbumResTemp);
				appAlbumResTempService.updateSortByPrimarykey(appAlbumResTemp);
			}
			return "{\"success\":\"1\"}"; //清除排序成功
		}catch(Exception e){
			log.error("AppAlbumColumnController clearRanking", e);
			return "{\"success\":\"2\"}";//清除排序失败
		}
	}
	/**
	 * 复制手动分发
	 * @param columnId
	 * @param raveId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/handDataCopy/{columnId}", method = RequestMethod.POST)
	public String handDataCopy(@PathVariable("columnId") Integer columnId,@RequestParam("raveId") Integer raveId){
		try{			
			Country country = getDefaultCountry();
			if(country!=null){
				AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(columnId);	
			   //删除所以Global下的分发数据,同时将不是Global的数据中ranking改为null;
				List<AppAlbumRes> appAlbumResList2 = appAlbumResTempService.getByFileColumnAndSource(null, appAlbumColumn,handSource, raveId);
				for(AppAlbumRes appAlbumRes:appAlbumResList2){
					//如果是分发国家所属Global
					if(appAlbumRes.getAppFile()!=null&&appAlbumRes.getAppFile().getRaveId()==1){
						appAlbumResTempService.delAppAlbumResTemp(appAlbumRes.getId());
					}else{
						//清空排行数据
						appAlbumRes.setRanking(null);
						appAlbumRes.setSort(0.0);
						appAlbumResTempService.upAppAlbumResTemp(appAlbumRes);
					}
				}
				
				//获取想要复制的数据
				List<AppAlbumRes> appAlbumResList = appAlbumResTempService.getByFileColumnAndSource(null, appAlbumColumn, handSource, country.getId());
				//	appAlbumResTempService.deleteBySource(null, columnId, raveId, handSource);
				for(AppAlbumRes appAlbumRes:appAlbumResList){
					if(appAlbumRes.getAppFile()!=null&&appAlbumRes.getAppFile().getRaveId()==1){
					appAlbumRes.setRaveId(raveId);
					appAlbumRes.setId(null);
					List<AppAlbumRes> appAlbumResList1 = appAlbumResTempService.getByFileColumnAndSource(appAlbumRes.getAppInfo(), appAlbumColumn,-1, raveId);
					//数据不存在,则添加
					   if(appAlbumResList1==null ||appAlbumResList1.size()==0){
							appAlbumResTempService.addAppAlbumResTemp(appAlbumRes);							
						}
					}
				}
				
				//更新排行
				//计算新的sort,重新排名
				try {
					autoUploadHandSort(appAlbumColumn,raveId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error("AppAlbumColumnController updateRanking", e);
					return "{\"success\":\"3\"}";
				}
				return "{\"success\":\"1\"}";//复制手动分发成功
			}else{
				return "{\"success\":\"3\"}";//获取默认国家失败
			}			
		}catch(Exception e){
			log.error("AppAlbumColumnController handDataCopy", e);
			return "{\"success\":\"2\"}";//复制手动分发失败
		}
	}
	/**
	 * 获取默认的复制手动分发到Global的国家
	 * @return
	 */
	private Country getDefaultCountry() {
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
		CountryExample example = new CountryExample();
		example.createCriteria().andNameEqual(defaultCountry);
		List<Country> countryList = countryService.getCountry(example);
		
		 if(countryList!=null&&countryList.size()!=0){
			 return countryList.get(0);
		 }
		return null;
	}
	/**
	 * 
	 * @param request
	 * @param response
	 * @param columnId 需要延迟的分类
	 * @param ids 延迟的id号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/laterRanking/{columnId}", method = RequestMethod.POST)
	public String laterRanking(@PathVariable("columnId") Integer columnId,@RequestParam("raveId") Integer raveId,
			@RequestParam("ids") String ids,@RequestParam("num") Integer num){
		try{
			//获取专辑
			AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(columnId);		
			//获取所有手动数据
			List<AppAlbumRes> appAlbumResList = appAlbumResTempService.getByFileColumnAndSource(null, appAlbumColumn, handSource, raveId);
			Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
			
			Map<Integer, Integer> mapDataSource = getLaterRanking(idIntArray, num,
					appAlbumResList);
			//判断是否有重复的
			if(!isRankingRepeat(mapDataSource)){
				//有重复排名 
				return "{\"success\":\"3\"}";
			}
			//插入ranking
			for(Integer id:idIntArray){
				AppAlbumRes appAlbumRes = appAlbumResTempService.getAppAlbumResTemp(id);
				if(appAlbumRes.getRanking()!=null&&appAlbumRes.getRanking()!=0){
					appAlbumRes.setRanking(appAlbumRes.getRanking()+num);
					//设置手动分数
					appAlbumRes.setSort(rankAll-(appAlbumRes.getRanking()+num));	
					appAlbumResTempService.updateRankingByPrimarykey(appAlbumRes);
					appAlbumResTempService.updateSortByPrimarykey(appAlbumRes);
				}
			}
			//计算新的sort,重新排名
			try {
				autoUploadHandSort(appAlbumColumn,raveId);
				return "{\"success\":\"1\"}";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("AppAlbumColumnController updateRanking", e);
				return "{\"success\":\"2\"}";
			}		
		}catch(Exception e){
			log.error("AppAlbumColumnController clearRanking", e);
			return "{\"success\":\"2\"}";//清除排序失败
		}
	}
	/**
	 * 获取延迟后的排名值
	 * @param ids
	 * @param num
	 * @param appAlbumResList
	 * @return
	 */
	private Map<Integer, Integer> getLaterRanking(Integer[] idIntArray, Integer num,
			List<AppAlbumRes> appAlbumResList) {
		Map<Integer,Integer> mapDataSource  = new HashMap<Integer, Integer>();
		for(AppAlbumRes appAlbumRes:appAlbumResList){
			mapDataSource.put(appAlbumRes.getId(), appAlbumRes.getRanking());
		}
		//获取到当前想要延迟的id
		for(Integer id:idIntArray){
			AppAlbumRes appAlbumRes = appAlbumResTempService.getAppAlbumResTemp(id);	
			if(appAlbumRes.getRanking()!=null&&appAlbumRes.getRanking()!=0){
				mapDataSource.put(id, appAlbumRes.getRanking()+num);				
			}
		}
		return mapDataSource;
	}


	/** 新增app分发信息 */
	@RequestMapping("/showAdd")
	public String showAdd(HttpServletRequest request, @RequestParam Integer id) {
		try {
			AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(id);
			request.setAttribute("appAlbumColumn", appAlbumColumn);
		} catch (Exception e) {
			log.error("AppAlbumColumnController showAdd", e);
		}
		return "appAlbumColumn/add";
	}

	/** 新增app分发信息页 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute AppAlbumColumn record) {
		try {
			appAlbumColumnService.addAppAlbumColumn(record);
		} catch (Exception e) {
			log.error("AppAlbumColumnController add", e);
		}
		return "{flag:0}";
	}

	/** 加载单个app分发信息 */
	@RequestMapping("/edit")
	public String edit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(id);
			model.addAttribute("appAlbumColumn", appAlbumColumn);
		} catch (Exception e) {
			log.error("AppAlbumColumnController edit", e);
		}
		return "appAlbumColumn/edit";
	}

	/** 更新app分发信息 */
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@ModelAttribute AppAlbumColumn record) {
		try {
			appAlbumColumnService.upAppAlbumColumn(record);
		} catch (Exception e) {
			log.error("AppAlbumColumnController update", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 删除app分类 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		appAlbumResTempService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}

	@ResponseBody
	@RequestMapping("/secondCategory")
	public String querySecondCategory(@RequestParam("id") Integer id, @RequestParam("categoryId") Integer categoryId) {
		try {
			List<Category> list = categoryService.getCategorys(id);
			if (CollectionUtils.isEmpty(list)) {
				return "{\"success\":\"0\"}";
			} else {
				StringBuilder options = new StringBuilder();
				options.append("<option value='0'>--all--</option>");
				for (Category category : list) {
					String selected = "";
					if (category.getId() == categoryId) {
						selected = "selected";
					}
					options.append("<option value='");
					options.append(category.getId() + "' " + selected + ">");
					options.append(category.getName());
					options.append("</option>");
				}
				return "{\"success\":\"1\",\"option\":\"" + options.toString() + "\"}";
			}
		} catch (Exception e) {
			return "{\"success\":\"2\"}";
		}
	}
	
	/** 新增Top信息 */
	@RequestMapping("/showTop")
	public String showTop(HttpServletRequest request) {
		request.setAttribute("ftp_apk_defaul_path", Constant.APK_FTP_PATH);
		log.info("AppAlbumColumnController showTop");
		List<Country> countrys = countryService.getCountrys();
		request.setAttribute("countrys", countrys);
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
		request.setAttribute("defaultCountry", defaultCountry);
		return "appAlbumColumn/top";
	}
	

	public static Document readuploadFile(MultipartFile  htmlFile) throws Exception{
		String code =  FileUtil.codeString(htmlFile);
		if(code!=null){
			System.out.println(code);
		}
		Document doc = null;
		StringBuilder sb = new StringBuilder();
		InputStreamReader isr =new InputStreamReader(htmlFile.getInputStream(),code);
//		System.out.println(isr.getEncoding());
		BufferedReader br= new BufferedReader(isr);
			
			String data = null;
			while ((data = br.readLine()) != null) {
				sb.append(data);
			}

//			Entities.EscapeMode.base.getMap().clear();

			doc = Jsoup.parse(new String(sb.toString().getBytes(code),
					code));
			return doc;
		
	}
	public static Document readftpFile(File  ftpFile) throws Exception{
		String code =  FileUtil.getTextEncode(ftpFile);
		if(code!=null){
			System.out.println(code);
		}
		Document doc = null;
		StringBuilder sb = new StringBuilder();
		InputStreamReader isr =new InputStreamReader(new FileInputStream(ftpFile),code);
//		System.out.println(isr.getEncoding());
		BufferedReader br= new BufferedReader(isr);			
			String data = null;
			while ((data = br.readLine()) != null) {
				sb.append(data);
			}
			doc = Jsoup.parse(new String(sb.toString().getBytes(code),
					code));
			return doc;
		
	}
	/**
	 * Top500排行榜
	 * @param categoryId apps 1 games 2
	 * @param htmlFile
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/top")	
	public String top(  @RequestParam("categoryId")Integer categoryId,@RequestParam("raveId")Integer raveId, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("loginUser");
		if(user==null){
			return "{\"flag\":\"6\"}";
		}
		StringBuilder result = new StringBuilder();
		Country country =  countryService.getCountry(raveId);
		AppAlbum appAlbum= appAlbumService.getAppAlbum(categoryId);
		String currentAlbum = country.getName() +"-"+ appAlbum.getName();
		String currentAlbumDetail = country.getName()+"("+country.getNameCn()+")" +"--"+ appAlbum.getName();
		if(map.get(currentAlbum)!=null){
			result.append(currentAlbumDetail+" 正在分发中，请不要重复导入!");
			return "{\"flag\":\"5\",\"result\":\"" + result.toString() + "\"}";
		}
		// ftp上传路径
		// 选择上传方式,1:本地apk文件上传,2:通过ftp路径上传
		String uploadType = request.getParameter("uploadType");
		MultipartFile htmlFile = null;
		Document doc = null;
	
		if (uploadType.equals("1")) {
			MultipartHttpServletRequest mhs = (MultipartHttpServletRequest) request;
			htmlFile = mhs.getFile("htmlFile");
			if(htmlFile==null||htmlFile.getSize()==0){
				//上传文件失败
				return "{\"flag\":\"3\"}";
			}
			String contentType = htmlFile.getContentType();
			if(!contentType.equals("text/html")){
				//上传文件出错
				return "{\"flag\":\"2\"}";
			}
			try {
				doc = readuploadFile(htmlFile);
			} catch (Exception e1) {
				log.error("AppAlbumColumnController top read htmlFile error", e1);

				return "{\"flag\":\"1\"}";
			}
			
		}// 选择ftp上传方式,但是没提交路径为空
		else if (uploadType.equals("2")) {
			String ftphtmlFile = request.getParameter("ftphtmlFile");
			//ftp上传失败
			if( StringUtils.isEmpty(ftphtmlFile)){
				return "{\"flag\":\"4\"}";
			}
			File ftpFile = new File(ftphtmlFile);
			if(ftpFile.length()==0||(!ftpFile.isFile())){
				//ftp上传失败
				return "{\"flag\":\"4\"}";
			}
			try {
				doc = readftpFile(ftpFile);
			} catch (Exception e1) {
				log.error("AppAlbumColumnController top read ftpFile error", e1);

				return "{\"flag\":\"1\"}";
			}
			
		}
		try {
			Element body = doc.body();
			Element ele = body.select("div.region-main-inner").last();
			Elements tabletrs = ele.getElementById("storestats-top-table")
					.getElementsByTag("tr");
			String fileName1 = createAppAnnieFile();
			int n = getMayDistributeNum(appAlbum, country, fileName1,
					tabletrs);
			result.append("匹配到公用app管理的资源有"+n+"条！");
			if(n>0){
				AppBatchDistributionVO appBatchDistributionVO 
				= new AppBatchDistributionVO();
				appBatchDistributionVO.setAppAlbumColumnService(appAlbumColumnService);
				appBatchDistributionVO.setAppAlbumResTempService(appAlbumResTempService);
				appBatchDistributionVO.setAppAlbumService(appAlbumService);
				appBatchDistributionVO.setAppFileService(appFileService);
				appBatchDistributionVO.setAppInfoConfigService(appInfoConfigService);
				appBatchDistributionVO.setAppInfoService(appInfoService);
				appBatchDistributionVO.setAppScoreService(appScoreService);
				appBatchDistributionVO.setCountryService(countryService);
				appBatchDistributionVO.setProxyIPService(proxyIPService);
				appBatchDistributionVO.setAppannieInfoService(appannieInfoService);
				appBatchDistributionVO.setAppannieInfoBaseService(appannieInfoBaseService);				
				appBatchDistributionVO.setAppType(categoryId);
				appBatchDistributionVO.setRaveId(raveId);
				appBatchDistributionVO.setElements(tabletrs);
				distributionQueue.put(appBatchDistributionVO);
				if(map==null ||map.size()==0){
					init();
					DealAppBatchDistribution deal = new DealAppBatchDistribution();
					deal.start();
				}
				map.put(currentAlbum, user);//进入队列，就标记状态为true
			}
		} catch (Exception e) {
			map.remove(currentAlbum);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\",\"result\":\"" + result.toString() + "\"}";
	}


	@ResponseBody
	@RequestMapping("/ftpUrl")
	public Map<String, String> queryFtpFilesURL(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		String htmlPath = request.getParameter("htmlPath");
		try {
			// 输入路径为空,则取系统默认路径
			if (StringUtils.isEmpty(htmlPath)) {
				Resource rs = new ClassPathResource("config/resource.properties");
				Properties props = PropertiesLoaderUtils.loadProperties(rs);
				htmlPath = props.getProperty("apk.ftp.path", FTP_APK_PATH);
			}
			if (StringUtils.isNotEmpty(htmlPath)) {
				getFilePath(htmlPath, map);
			}
		} catch (Exception e) {
			log.error("read apk resource failed !", e);
		}
		return map;
	}	
	/**
	 * 获取可以分发的数据
	 * @param categoryId
	 * @param raveId
	 * @param fileName1
	 * @param tabletrs
	 * @return
	 */
	private int getMayDistributeNum(AppAlbum appAlbum, Country country,
			String fileName1, Elements tabletrs) {
		int n=0;
		List<String> listAppName = new ArrayList<String>();
		for (int i = 0; i < tabletrs.size(); i++) {
			// i 表示排行榜+1
			Element ei = tabletrs.get(i);
			// eitds 中包含五个元素，第一个表示免费排行，eitds.get(0)
			Element eitds = ei.getElementsByTag("td").get(0);			
			Element divElement = eitds.select("div.main-info").first();
			String appName = divElement.select("span.title-info").get(0)
					.attr("title");
			if(listAppName.contains(appName.toLowerCase())){
				continue;
			}
			AppInfo appInfo = appInfoService.findAppInfo(fatherChannelId, appName);
			if(appInfo==null){
				String mappingName = null;
				try {
					mappingName = NameMappingSerivce.getNameMapping(appName);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.error("AppAlbumColumnController getMayDistributeNum", e);
				}
				if(mappingName!=null){
					appInfo = appInfoService.findAppInfo(fatherChannelId, mappingName);
				}
			}
			
			if(appInfo!=null){
				if(appInfo.getCategory()!=null){
					// 获取appfile
					AppFile appFile = appFileService.getAppFileByappIdAndCountry(appInfo.getId(),
							country.getId(),channelId);
					if (appFile != null) {
						recordMayDistribute(fileName1, appName);
						listAppName.add(appName.toLowerCase());
						n++;								
					}else{
						recordAppBatchError(filename,appName,country.getName(),appAlbum.getName());
					}
				}
				
			}
		}
		return n;
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
					if (f.getName().toLowerCase().endsWith(".htm")) {
						map.put(f.getName().substring(0, f.getName().toLowerCase().lastIndexOf(".htm")), f.getPath());
					}else if(f.getName().toLowerCase().endsWith(".html")){
						map.put(f.getName().substring(0, f.getName().toLowerCase().lastIndexOf(".html")), f.getPath());

					}
				}
			}
		}
	}
	public void init() {
		if (ConstantScore.map.get("configList") == null) {
			// 启用初始
			controller.initAppConfig();
		}
	}
	
	/**
	 * 记录分发不成功的数据
	 * @param appName
	 */
	private void recordAppBatchError(String filename,String appName,String countryName,String appAlbumName) {
		java.io.FileWriter fw;
		try {
			CrawlerFileUtils.createFile(filename);
			fw = new java.io.FileWriter(filename, true);
			java.io.PrintWriter pw = new java.io.PrintWriter(fw);
			pw.println(DateUtil.getCurrentDateTime()+" ---国家---  "+ countryName+" ---分发专辑类别--- "+ appAlbumName+"---app名称---"+ appName +" ---> appFile不存在或者状态为不可用");
			pw.close(); 
			fw.close();
		} catch (Exception e1) {
			log.error( "AppAlbumColumnController recordAppBatchError  写文件出错",e1);
			
		}
	}
	/**
	 * 记录可以分发的数据
	 * @param fileName1
	 * @param appName
	 */
	private void recordMayDistribute(String fileName1, String appName) {
		java.io.FileWriter fw;
		try {
			fw = new java.io.FileWriter(fileName1, true);
			java.io.PrintWriter pw = new java.io.PrintWriter(fw);
			pw.println(appName);
			pw.close(); 
			fw.close();
		} catch (Exception e1) {
			log.error( "AppAlbumColumnController recordMayDistribute 写文件出错",e1);
			
		}
	}
	/**
	 * 创建appannie分发的文件
	 * @return
	 */
	private String createAppAnnieFile() {
		String fileName1 = Constant.LOCAL_FILE_PATH+"appannie/"+"appannie.txt";
		File objFile = new File(fileName1);
		if(objFile.exists()){
			objFile.delete();
		}
		if (!objFile.getParentFile().exists()) {
		  objFile.getParentFile().mkdirs();
    	}
    	try {
			objFile.createNewFile();
		} catch (IOException e) {
			log.error("AppAlbumColumnController createAppAnnieFile 创建文件出错", e);
		}
		return fileName1;
	}
	
	
	/** 新增Top信息 */
	@RequestMapping("/showAppannieContryRank")
	public String showAppannieContryRank(HttpServletRequest request) {
		request.setAttribute("ftp_apk_defaul_path", Constant.APK_FTP_PATH);
		log.info("AppAlbumColumnController showTop");
		List<Country> countrys = countryService.getCountrys();
		request.setAttribute("countrys", countrys);
		String defaultCountry = "";
		List<AppScore> scores = appScoreService.getAllScore();
		for(AppScore appSocre:scores ){
			 if (appSocre.getScoreKey().equals("Global")) {
				 defaultCountry= appSocre.getScoreValue();
			 }
		}
		if(defaultCountry==null || defaultCountry.equals("")){
			defaultCountry = "Philippines";
		}
		request.setAttribute("defaultCountry", defaultCountry);
		return "appAlbumColumn/appannieCountryRank";
	}
	/**
	 * Top500排行榜
	 * @param categoryId apps 1 games 2
	 * @param htmlFile
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/appannieCountryRank")	
	public String appannieCountryRank(  @RequestParam("categoryId")Integer categoryId,
			@RequestParam("raveId")Integer raveId,@RequestParam("stateType")Integer stateType, 
			HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("loginUser");
		if(user==null){
			return "{\"flag\":\"6\"}";
		}
		StringBuilder result = new StringBuilder();
		Country country =  countryService.getCountry(raveId);
		AppAlbum appAlbum= appAlbumService.getAppAlbum(categoryId);
		String currentAlbum = country.getName() +"-"+ appAlbum.getName();
		String currentAlbumDetail = country.getName()+"("+country.getNameCn()+")" +"--"+ appAlbum.getName();
		if(mapAppannieCountryRank.get(currentAlbum)!=null){
			result.append(currentAlbumDetail+" 正在分发中，请不要重复导入!");
			return "{\"flag\":\"5\",\"result\":\"" + result.toString() + "\"}";
		}
		// ftp上传路径
		// 选择上传方式,1:本地apk文件上传,2:通过ftp路径上传
		String uploadType = request.getParameter("uploadType");
		MultipartFile htmlFile = null;
		Document doc = null;
	
		if (uploadType.equals("1")) {
			MultipartHttpServletRequest mhs = (MultipartHttpServletRequest) request;
			htmlFile = mhs.getFile("htmlFile");
			if(htmlFile==null||htmlFile.getSize()==0){
				//上传文件失败
				return "{\"flag\":\"3\"}";
			}
			String contentType = htmlFile.getContentType();
			if(!contentType.equals("text/html")){
				//上传文件出错
				return "{\"flag\":\"2\"}";
			}
			try {
				doc = readuploadFile(htmlFile);
			} catch (Exception e1) {
				log.error("AppAlbumColumnController top read htmlFile error", e1);

				return "{\"flag\":\"1\"}";
			}
			
		}// 选择ftp上传方式,但是没提交路径为空
		else if (uploadType.equals("2")) {
			String ftphtmlFile = request.getParameter("ftphtmlFile");
			//ftp上传失败
			if( StringUtils.isEmpty(ftphtmlFile)){
				return "{\"flag\":\"4\"}";
			}
			File ftpFile = new File(ftphtmlFile);
			if(ftpFile.length()==0||(!ftpFile.isFile())){
				//ftp上传失败
				return "{\"flag\":\"4\"}";
			}
			try {
				doc = readftpFile(ftpFile);
			} catch (Exception e1) {
				log.error("AppAlbumColumnController top read ftpFile error", e1);

				return "{\"flag\":\"1\"}";
			}
			
		}
		try {
			Element body = doc.body();
			Element ele = body.select("div.region-main-inner").last();
			Elements tabletrs = ele.getElementById("storestats-top-table")
					.getElementsByTag("tr");
			String fileName1 = createAppAnnieFile();
			int n = getCountryRankHotNum(appAlbum, country, fileName1,
					tabletrs);	
			int j = getCountryRankNewNum(appAlbum, country, fileName1,
					tabletrs);	
			result.append("匹配到公用app管理的Hot的资源有"+n+"条！");
			result.append("匹配到公用app管理的New的资源有"+j+"条！");
			if(n>0){
				AppBatchDistributionVO appBatchDistributionVO 
				= new AppBatchDistributionVO();
				appBatchDistributionVO.setAppAlbumColumnService(appAlbumColumnService);
				appBatchDistributionVO.setAppAlbumResTempService(appAlbumResTempService);
				appBatchDistributionVO.setAppAlbumService(appAlbumService);
				appBatchDistributionVO.setAppFileService(appFileService);
				appBatchDistributionVO.setAppInfoConfigService(appInfoConfigService);
				appBatchDistributionVO.setAppInfoService(appInfoService);
				appBatchDistributionVO.setAppScoreService(appScoreService);
				appBatchDistributionVO.setCountryService(countryService);
				appBatchDistributionVO.setProxyIPService(proxyIPService);
				appBatchDistributionVO.setAppannieInfoCountryRankService(appannieInfoCountryRankService);
				appBatchDistributionVO.setAppannieInfoBaseService(appannieInfoBaseService);				
				appBatchDistributionVO.setAppType(categoryId);
				appBatchDistributionVO.setRaveId(raveId);
				appBatchDistributionVO.setElements(tabletrs);
				appBatchDistributionVO.setStateType(stateType);
				appannieCountryRankQueue.put(appBatchDistributionVO);
				if(mapAppannieCountryRank==null ||mapAppannieCountryRank.size()==0){
					init();
					DealAppAnnieCountryRank deal = new DealAppAnnieCountryRank();
					deal.start();
				}
				mapAppannieCountryRank.put(currentAlbum, user);//进入队列，就标记状态为true
			}
		} catch (Exception e) {
			mapAppannieCountryRank.remove(currentAlbum);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\",\"result\":\"" + result.toString() + "\"}";
	}
	/**
	 * 获取可以分发的免费排行的数据
	 * @param categoryId
	 * @param raveId
	 * @param fileName1
	 * @param tabletrs
	 * @return
	 */
	private int getCountryRankHotNum(AppAlbum appAlbum, Country country,
			String fileName1, Elements tabletrs) {
		int n=0;
		List<String> listAppName = new ArrayList<String>();
		for (int i = 0; i < tabletrs.size(); i++) {
			// i 表示排行榜+1
			try{
				Element ei = tabletrs.get(i);
				// eitds 中包含五个元素，第一个表示免费排行，eitds.get(0)
				Element eitds = ei.getElementsByTag("td").get(0);			
				Element divElement = eitds.select("div.main-info").first();
				String appName = divElement.select("span.title-info").get(0)
						.attr("title");
				if(listAppName.contains(appName.toLowerCase())){
					continue;
				}
				recordMayDistribute(fileName1, appName);
				listAppName.add(appName.toLowerCase());
				n++;
			}catch(Exception e){
				continue;
			}
		}
		return n;
	}
	/**
	 * 获取可以分发的最新免费的数据
	 * @param categoryId
	 * @param raveId
	 * @param fileName1
	 * @param tabletrs
	 * @return
	 */
	private int getCountryRankNewNum(AppAlbum appAlbum, Country country,
			String fileName1, Elements tabletrs) {
		int n=0;
		List<String> listAppName = new ArrayList<String>();
		for (int i = 0; i < tabletrs.size(); i++) {
			// i 表示排行榜+1
			try{
				Element ei = tabletrs.get(i);
				// eitds 中包含五个元素，第一个表示免费排行，eitds.get(0)
				Element eitds = ei.getElementsByTag("td").get(3);			
				Element divElement = eitds.select("div.main-info").first();
				String appName = divElement.select("span.title-info").get(0)
						.attr("title");
				if(listAppName.contains(appName.toLowerCase())){
					continue;
				}
				recordMayDistribute(fileName1, appName);
				listAppName.add(appName.toLowerCase());
				n++;
				
			}catch(Exception e){
				continue;
			}
		}
		return n;
	}
}
