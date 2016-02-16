package com.mas.rave.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mas.rave.appannie.CrawlerTop500Util;
import com.mas.rave.appannie.ProxyService;
import com.mas.rave.controller.AppAlbumColumnController;
import com.mas.rave.main.vo.AppAlbum;
import com.mas.rave.main.vo.AppAlbumColumn;
import com.mas.rave.main.vo.AppInfoConfig;
import com.mas.rave.main.vo.AppannieInfoBase;
import com.mas.rave.main.vo.AppannieInfoCountryRank;
import com.mas.rave.main.vo.Country;
import com.mas.rave.service.AppAlbumColumnService;
import com.mas.rave.service.AppAlbumResService;
import com.mas.rave.service.AppAlbumResTempService;
import com.mas.rave.service.AppAlbumService;
import com.mas.rave.service.AppFileService;
import com.mas.rave.service.AppInfoConfigService;
import com.mas.rave.service.AppInfoService;
import com.mas.rave.service.AppScoreService;
import com.mas.rave.service.AppannieInfoBaseService;
import com.mas.rave.service.AppannieInfoCountryRankService;
import com.mas.rave.service.CategoryService;
import com.mas.rave.service.CountryService;
import com.mas.rave.service.ProxyIPService;

public class AppAnnieCountryRankTask{

	//private static final int fatherChannelId=200000;
	//private static final int cpId = 300011;
	private static int getIpNum=0;
	private static String BASE_URL = "http://www.appannie.com";
	public static Map<String,Integer> tempMap=Collections.synchronizedMap(new HashMap<String,Integer>());
	public static List<String> tempList= Collections.synchronizedList(new ArrayList<String>());
	Logger log = Logger.getLogger(AppAnnieCountryRankTask.class);
	private AppAlbumColumnService appAlbumColumnService;

	private AppAlbumService appAlbumService;

	private AppAlbumResTempService appAlbumResTempService;

	private AppFileService appFileService;

	private AppInfoService appInfoService;

	private AppInfoConfigService appInfoConfigService;

	private CountryService countryService;
	
	private AppannieInfoCountryRankService appannieInfoCountryRankService;
	
	private ProxyIPService proxyIPService;
	
	private AppScoreService appScoreService;
	
	private AppannieInfoBaseService appannieInfoBaseService;
	
	private Elements elements;
	
	private int appType;
	private int raveId;
	private int stateType;
	private static List<String[]> ipPortList=null;
	
	private Country country;
	
	private AppAlbum appAlbum;

	public AppAnnieCountryRankTask() {
	}

	public AppAnnieCountryRankTask(int raveId,int appType,Elements elements,int stateType) {
		this.appType = appType;
		this.elements = elements;
		this.raveId = raveId;
		this.stateType = stateType;
	}

	public void setAppAlbumColumnService(
			AppAlbumColumnService appAlbumColumnService) {
		this.appAlbumColumnService = appAlbumColumnService;
	}

	public void setAppAlbumService(AppAlbumService appAlbumService) {
		this.appAlbumService = appAlbumService;
	}

	public void setAppAlbumResTempService(
			AppAlbumResTempService appAlbumResTempService) {
		this.appAlbumResTempService = appAlbumResTempService;
	}

	public void setAppAlbumResService(AppAlbumResService appAlbumResService) {
	}

	public void setAppFileService(AppFileService appFileService) {
		this.appFileService = appFileService;
	}

	public void setAppInfoService(AppInfoService appInfoService) {
		this.appInfoService = appInfoService;
	}

	public void setCategoryService(CategoryService categoryService) {
	}

	public void setAppInfoConfigService(AppInfoConfigService appInfoConfigService) {
		this.appInfoConfigService = appInfoConfigService;
	}
	
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}
	public ProxyIPService getProxyIPService() {
		return proxyIPService;
	}

	public void setProxyIPService(ProxyIPService proxyIPService) {
		this.proxyIPService = proxyIPService;
	}

	public void setAppScoreService(AppScoreService appScoreService) {
		this.appScoreService = appScoreService;
	}

	public void setAppannieInfoCountryRankService(
			AppannieInfoCountryRankService appannieInfoCountryRankService) {
		this.appannieInfoCountryRankService = appannieInfoCountryRankService;
	}

	public void setAppannieInfoBaseService(
			AppannieInfoBaseService appannieInfoBaseService) {
		this.appannieInfoBaseService = appannieInfoBaseService;
	}

	public void runAppAnnieCountryRank() {
		tempMap.clear(); //清除临时列表ip		
		country =  countryService.getCountry(raveId);		
		appAlbum = appAlbumService.getAppAlbum(appType);
		//获取当前分发的类别
		String currentAlbum = country.getName() +"-"+ appAlbum.getName();
		try{
			List<AppAlbumColumn> listAppAlbumColumn = appAlbumColumnService
					.getAppAlbumColumnsByAppAlbumId(appType);
			ipPortList = proxyIPService.getIpPort();
			Calendar calendar = Calendar.getInstance();//获取的是系统当前时间  
			calendar.add(Calendar.DATE, -4);  
			//根据创建时间删除			
			appInfoConfigService.deleteByTime(3,calendar.getTime());
			appInfoConfigService.updateStatusBy(false);
			//写入配置文件
			for (int i = 0; i < elements.size(); i++) {
				// i 表示排行榜+1
				try{
					updateAppConfigHot(elements.get(i),i);					
				}catch(Exception e){
					log.error("AppBatchDistributionTask run" + i +"   "+ elements.get(i), e);
				}
			}
			for (int i = 0; i < elements.size(); i++) {
				// i 表示排行榜+1
				try{
					updateAppConfigNew(elements.get(i),i);
					
				}catch(Exception e){
					log.error("AppBatchDistributionTask run" + i +"   "+ elements.get(i), e);
				}
			}
			//将旧的资源的状态改为false
			AppannieInfoCountryRank criteria = new AppannieInfoCountryRank();
			criteria.setRaveId(raveId);
			criteria.setAlbumId(appType);
			criteria.setState(false);
			appannieInfoCountryRankService.updateStatusBy(criteria);				

			//批量分发Hot
			appBatchResHot(elements,listAppAlbumColumn,1);
			
			//批量分发New
			appBatchResNew(elements,listAppAlbumColumn,2);
			
			//删除剩余旧的资源
			appannieInfoCountryRankService.deleteByCondition(criteria);
			AppAlbumColumnController.mapAppannieCountryRank.remove(currentAlbum);
			log.info("AppBatchDistributionTask run  分发完成   AppAlbumColumnController currentAlbum :"+country.getName()+"---"+appAlbum.getName()+"---"+AppAlbumColumnController.mapAppannieCountryRank.containsKey(currentAlbum) );
			clearFailIp();
		} catch (Exception e) {
			AppAlbumColumnController.mapAppannieCountryRank.remove(currentAlbum);
			log.error("AppBatchDistributionTask run", e);
		}
	}
	/**
	 * 写入配置文件
	 * @param ei
	 * @param i
	 */
	private void updateAppConfigHot(Element ei,int i) {
		// eitds 中包含五个元素，第一个表示免费排行，eitds.get(0)
		Element eitds = ei.getElementsByTag("td").get(0);
		Element divElement = eitds.select("div.main-info").first();
		String appName = divElement.select("span.title-info").get(0)
				.attr("title");	
		String packageName = divElement.select("span").last()
				.text();
		AppInfoConfig config = new AppInfoConfig();
		config.setName(appName);
		config.setDescription(String.valueOf(i+1)+" Hot");
		config.setPackageName(packageName);
		config.setType(3);//type=3表示自动更新
		config.setState(true);
		//将top 500 写入配置文件
		AppInfoConfig config1 =	appInfoConfigService.getAppConfig(appName, 3);
		if(config1==null){
			//appName是否已经是忽略的.
			AppInfoConfig config2 =	appInfoConfigService.getAppConfig(appName, 1);
			if(config2==null){
				appInfoConfigService.addAppInfoConfig(config);				
			}

		}else{
			config1.setState(true);
			config1.setUpdateTime(new Date());
			config1.setPackageName(packageName);
			appInfoConfigService.upAppInfoConfig(config1);
		}
	}
	/**
	 * 写入配置文件
	 * @param ei
	 * @param i
	 */
	private void updateAppConfigNew(Element ei,int i) {
		// eitds 中包含五个元素，第四个表示最新排行，eitds.get(0)
		Element eitds = ei.getElementsByTag("td").get(3);
		Element divElement = eitds.select("div.main-info").first();
		String appName = divElement.select("span.title-info").get(0)
				.attr("title");	
		String packageName = divElement.select("span").last()
				.text();
		AppInfoConfig config = new AppInfoConfig();
		config.setName(appName);
		config.setDescription(String.valueOf(i+1)+" New");
		config.setPackageName(packageName);		
		config.setType(3);//type=3表示自动更新
		config.setState(true);
		if(appName.equals("Snake Attack 3D Simulator")){
			System.out.println(appName);
		}
		//将top 500 写入配置文件
		AppInfoConfig config1 =	appInfoConfigService.getAppConfig(appName, 3);
		if(config1==null){
			//appName是否已经是忽略的.
			AppInfoConfig config2 =	appInfoConfigService.getAppConfig(appName, 1);
			if(config2==null){
				appInfoConfigService.addAppInfoConfig(config);				
			}
		}else{
			config1.setState(true);
			config1.setUpdateTime(new Date());
			config1.setPackageName(packageName);
			appInfoConfigService.upAppInfoConfig(config1);
		}
	}
	private void clearFailIp() {
		for(String ss:tempMap.keySet()){
			int value = tempMap.get(ss);
			if(value>=3){//失败三次的，就删除
				proxyIPService.deleteByIP(ss);
			}
		}
	}
	/**
	 * 获取随机IP
	 * @return
	 */
	private String getRomdonIP(){
		String ip = ProxyService.doRandomProxy(ipPortList);	
		Integer value = tempMap.get(ip);
		if(value!=null && value>=3){
			if(++getIpNum>100){//连续100次取ip失败，就直接返回，否则再取。
	    		getIpNum=0;
	    		return ip;
	    	}
			return getRomdonIP();
		}
		getIpNum=0;
	    return ip;
	}
	private int getAnnieExtentData(Element divAnnieExtent){
		/**
		 *  <span class="var change_same">=</span>
			<span class="var change_down">▼1</span>
			<span class="var change_up">▲1</span>
		 */
		int i=0;
		Element spantag = divAnnieExtent.getElementsByTag("span").first();
		String spanStr = spantag.text().trim();
		if(spantag.getElementsByClass("change_same")!=null&&spantag.getElementsByClass("change_same").size()>0){
			i=0;
			return i;
		}else if(spantag.getElementsByClass("change_down")!=null&&spantag.getElementsByClass("change_down").size()>0){
			i = -Integer.parseInt(spanStr.substring(1, spanStr.length()));
		}else if(spantag.getElementsByClass("change_up")!=null&&spantag.getElementsByClass("change_up").size()>0){
			i = Integer.parseInt(spanStr.substring(1, spanStr.length()));

		}
		return i;

	}
	/**
	 * 批量分发Hot
	 * @param elements
	 */
	public void appBatchResHot(Elements elements,List<AppAlbumColumn> listAppAlbumColumn, int annieType){
		for (int i = 0; i < elements.size(); i++) {
			try{	
			// i 表示排行榜+1
		    //Element ei = elements.get(i);
			// eitds 中包含五个元素，第一个表示免费排行，eitds.get(0)
			Element eitds = elements.get(i).getElementsByTag("td").get(0);
//			Element divElement = eitds.select("div.main-info").first();
			String appName = eitds.select("div.main-info").first().select("span.title-info").get(0)
					.attr("title");	
			//appannie上的排行幅度
			Element divAnnieExtent = eitds.select("div.var-info").first();
			int annieExtent= getAnnieExtentData(divAnnieExtent);
			//判断appName 是否已经录入
				//map中获取
				AppannieInfoBase appannieInfoBase1 = new AppannieInfoBase();
				appannieInfoBase1.setAppName(appName);
				appannieInfoBase1.setAnnieRatings(0.0);
				List<AppannieInfoBase> appannieInfoBaseList  = appannieInfoBaseService.selectByName(appannieInfoBase1);
				Map<String, Object> map0 = new HashMap<String, Object>();
				if(appannieInfoBaseList!=null&&appannieInfoBaseList.size()!=0){
					AppannieInfoBase appannieInfoBase = appannieInfoBaseList.get(0);
					map0.put(AppBatchDistributionConstant.INSTALLTOTAL, appannieInfoBase.getAnnieInstallTotal());
					map0.put(AppBatchDistributionConstant.SIZE, appannieInfoBase.getSize());
					map0.put(AppBatchDistributionConstant.RATER, appannieInfoBase.getAnnieRatings());
					map0.put(AppBatchDistributionConstant.INITIALRELEASEDATE, appannieInfoBase.getInitialReleaseDate());
					//map0.put(AppBatchDistributionConstant.ISSUER, appannieInfoBase.getIssuer());

				}else{				
					map0.putAll(getAppannieInfoByHttpRequest(eitds, appName,map0));
				}
				if(map0.size()>0){
					map0.put(AppBatchDistributionConstant.ANNIEEXTENT, annieExtent);
					appresByElement(map0,i,appName,annieType);
						
				}
				}catch(Exception e){
					continue;
				}

			}
			
		
	}
	/**
	 * 批量分发New
	 * @param elements
	 */
	public void appBatchResNew(Elements elements,List<AppAlbumColumn> listAppAlbumColumn,int annieType){
		for (int i = 0; i < elements.size(); i++) {
		 try{				
			// i 表示排行榜+1
		    //Element ei = elements.get(i);
			// eitds 中包含五个元素，第一个表示免费排行，eitds.get(0)
			Element eitds = elements.get(i).getElementsByTag("td").get(3);
//			Element divElement = eitds.select("div.main-info").first();
			String appName = eitds.select("div.main-info").first().select("span.title-info").get(0)
					.attr("title");	
			//appannie上的排行幅度
			Element divAnnieExtent = eitds.select("div.var-info").first();
			int annieExtent= getAnnieExtentData(divAnnieExtent);
			//判断appName 是否已经录入
				//map中获取
				AppannieInfoBase appannieInfoBase1 = new AppannieInfoBase();
				appannieInfoBase1.setAppName(appName);
				appannieInfoBase1.setAnnieRatings(0.0);
				List<AppannieInfoBase> appannieInfoBaseList  = appannieInfoBaseService.selectByName(appannieInfoBase1);
				Map<String, Object> map0 = new HashMap<String, Object>();
				if(appannieInfoBaseList!=null&&appannieInfoBaseList.size()!=0){
					AppannieInfoBase appannieInfoBase = appannieInfoBaseList.get(0);
					map0.put(AppBatchDistributionConstant.INSTALLTOTAL, appannieInfoBase.getAnnieInstallTotal());
					map0.put(AppBatchDistributionConstant.SIZE, appannieInfoBase.getSize());
					map0.put(AppBatchDistributionConstant.RATER, appannieInfoBase.getAnnieRatings());
					map0.put(AppBatchDistributionConstant.INITIALRELEASEDATE, appannieInfoBase.getInitialReleaseDate());
					//map0.put(AppBatchDistributionConstant.ISSUER, appannieInfoBase.getIssuer());

				}else{				
					map0.putAll(getAppannieInfoByHttpRequest(eitds, appName,map0));
				}
				if(map0.size()>0){
					map0.put(AppBatchDistributionConstant.ANNIEEXTENT, annieExtent);
					appresByElement(map0,i,appName,annieType);
						
				}
			}catch(Exception e){
				continue;
			}

		}		
		
	}
   /**
    * 网页爬取资源
    * @param eitds
    * @param appName
    * @param appInfo
    * @param map0
    * @return
    */
	private Map<String, Object> getAppannieInfoByHttpRequest(Element eitds,
			String appName,Map<String, Object> map0) {
		if(stateType==1){
			int n = 0;
			while(map0.size()==0){
				String ip = "";
				n++;
				if(n<=3){
					//每次分发时，从库中查询代理IP，随机代理IP来做分发访问						
				    ip = getRomdonIP();
				}else{
					ProxyService.closeProxy();
				}
				try {
					map0 = CrawlerTop500Util.getAppInfoByUrl(eitds);	
					addAppannieInfoBase(appName, map0);//将信息加到t_appannie_info_base表	
					try {
						Thread.sleep((new Random().nextInt(10)+20)*1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						log.error("AppBatchDistributionTask run", e);
					}
				}catch (Exception e) {
					//将异常IP写入临时Map
					addIpToTempMap(ip);
					//睡眠一个随机时间
					try {
						Thread.sleep((new Random().nextInt(10)+20)*1000);
					} catch (InterruptedException e2) {
						log.error("AppBatchDistributionTask run", e);
					}
					//异常处理
					log.error("AppBatchDistributionTask appBatchDistribution ip:"+ip+"第"+n+"次爬取资源出错！"+appName);
					if(n>3){
						//记录爬取失败的记录
						String str = "爬取分发资源失败!";
						recordAppBatchError(appName,country.getName(),appAlbum.getName(),str);
						addAppannieInfoBaseUrl(eitds, appName, map0);

					}
				}
				
				if(n>3){
					break;
				}
			}
		}else{
			addAppannieInfoBaseUrl(eitds, appName, map0);
		}
		
		return map0;
	}

private void addAppannieInfoBaseUrl(Element eitds, String appName,
		Map<String, Object> map0) {
	Element divElement = eitds.select("div.main-info").first();

	String appUrl = divElement.select("span.title-info").get(0).child(0)
			.attr("href");			
	String url = BASE_URL + appUrl;
	map0.put(AppBatchDistributionConstant.APPURL, url);
	map0.put(AppBatchDistributionConstant.INSTALLTOTAL,null);
	map0.put(AppBatchDistributionConstant.SIZE,0.0);
	map0.put(AppBatchDistributionConstant.RATER,0.0);
	map0.put(AppBatchDistributionConstant.INITIALRELEASEDATE,null);
	map0.put(AppBatchDistributionConstant.ISSUER,null);
	addAppannieInfoBase(appName, map0);//将信息加到t_appannie_info_base表				
}

	/**
	 * 插入appannieInfoBase表
	 * @param appName
	 * @param map0
	 */
	private void addAppannieInfoBase(String appName, Map<String, Object> map0) {
		AppannieInfoBase appannieInfoBase = new AppannieInfoBase();
		appannieInfoBase.setAnnieInstallTotal((Double)map0.get(AppBatchDistributionConstant.INSTALLTOTAL));
		appannieInfoBase.setAnnieRatings((Double)map0.get(AppBatchDistributionConstant.RATER));
		appannieInfoBase.setInitialReleaseDate( (Date) map0.get(AppBatchDistributionConstant.INITIALRELEASEDATE));
		appannieInfoBase.setSize((Double)map0.get(AppBatchDistributionConstant.SIZE));
		appannieInfoBase.setIssuer((String)map0.get(AppBatchDistributionConstant.ISSUER));
		appannieInfoBase.setAppName(appName);
		appannieInfoBase.setUrl((String)map0.get(AppBatchDistributionConstant.APPURL));
		AppannieInfoBase appannieInfoBase1 = new AppannieInfoBase();
		appannieInfoBase1.setAppName(appName);
		try{
			List<AppannieInfoBase> appannieInfoBaseList  = appannieInfoBaseService.selectByName(appannieInfoBase1);
			if(appannieInfoBaseList==null||appannieInfoBaseList.size()==0){		
				appannieInfoBaseService.insert(appannieInfoBase);
			}else{
				AppannieInfoBase appannieInfoBase2 = new AppannieInfoBase();
				appannieInfoBase2.setAppName(appName);
				appannieInfoBase2.setAnnieRatings(0.0);
				List<AppannieInfoBase> appannieInfoBaseList2  = appannieInfoBaseService.selectByName(appannieInfoBase2);
				if(appannieInfoBaseList2==null || appannieInfoBaseList2.size()==0){
					appannieInfoBaseService.deleteByName(appName);
					appannieInfoBaseService.insert(appannieInfoBase);
				}
				
			}
			
		}catch(Exception e){
			log.error("AppBatchDistributionTask appBatchDistribution addAppannieInfoBase"+appName);
		}
	}

	private void addIpToTempMap(String ip) {
		Integer value = tempMap.get(ip);
		if(value!=null){
			tempMap.put(ip, value+1);
		}else{
			tempMap.put(ip,1);
		}
	}

	/**
	 * 记录分发不成功的数据
	 * @param appName
	 */
	private void recordAppBatchError(String appName,String countryName,String appAlbumName,String str) {		
		tempList.add("国家---  "+ countryName+" ---分发专辑类别--- "+ appAlbumName+"---app名称---"+ appName +" ---> "+str);				
	}
	
	public void appresByElement(Map<String, Object> map0 ,int i, String appName,
			int annieType) {
		try{
			//添加
			addAppannieInfoCountryRankService(map0, i,
					appName,annieType);	
			//根据应用名删除
			AppannieInfoCountryRank criteria = new AppannieInfoCountryRank();
			criteria.setAppName(appName);
			criteria.setRaveId(raveId);
			criteria.setAlbumId(appType);
			criteria.setAnnieType(annieType);
			criteria.setState(false);
			appannieInfoCountryRankService.deleteByCondition(criteria);
		}catch(Exception e){
			String str="资源录入失败!";
			recordAppBatchError(appName,country.getName(),appAlbum.getName(),str );
			log.error("AppBatchDistributionTask appresByElement", e);
			log.error("AppBatchDistributionTask appresByElement appannie分发临时表出错");
		}


	}
/**
 * 插入网络获取信息
 * @param map0
 * @param i
 * @param appAlbumColumn
 * @param appInfo
 * @param appFile
 */
	private void addAppannieInfoCountryRankService(Map<String, Object> map0, int i,
			String appName,int annieType) {
		AppannieInfoCountryRank appannieInfo = new AppannieInfoCountryRank();
		appannieInfo.setAlbumId(appType);
		appannieInfo.setRaveId(raveId);
		appannieInfo.setAppName(appName);
		appannieInfo.setState(true);
		appannieInfo.setAnnieType(annieType);
		List<AppannieInfoCountryRank> list = appannieInfoCountryRankService.selectByCondition(appannieInfo);
		//升降
		if(map0.get(AppBatchDistributionConstant.ANNIEEXTENT)!=null){
			appannieInfo.setAnnieExtent((int)map0.get(AppBatchDistributionConstant.ANNIEEXTENT));			
		}
		if(map0.get(AppBatchDistributionConstant.INSTALLTOTAL)!=null){
			appannieInfo.setAnnieInstallTotal((double)map0.get(AppBatchDistributionConstant.INSTALLTOTAL));			
		}
		if(map0.get(AppBatchDistributionConstant.INITIALRELEASEDATE)!=null){
			appannieInfo.setInitialReleaseDate((Date)map0.get(AppBatchDistributionConstant.INITIALRELEASEDATE));			
		}
		if(map0.get(AppBatchDistributionConstant.RATER)!=null){
			appannieInfo.setAnnieRatings((double)map0.get(AppBatchDistributionConstant.RATER));
		}
		appannieInfo.setAnnieRank(i + 1);
		if(list==null||list.size()==0){
			try{
			appannieInfoCountryRankService.insert(appannieInfo);	
			}catch(Exception e){
				log.error("AppBatchDistributionTask appBatchDistribution addAppannieInfoBase"+appName);
			}
		}
	}

}
