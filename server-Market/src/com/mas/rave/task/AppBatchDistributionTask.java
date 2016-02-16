package com.mas.rave.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mas.rave.appannie.AppannieService;
import com.mas.rave.appannie.CrawlerTop500Util;
import com.mas.rave.appannie.NameMappingSerivce;
import com.mas.rave.appannie.ProxyService;
import com.mas.rave.controller.AppAlbumColumnController;
import com.mas.rave.main.vo.AppAlbum;
import com.mas.rave.main.vo.AppAlbumColumn;
import com.mas.rave.main.vo.AppAlbumRes;
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.main.vo.AppInfoConfig;
import com.mas.rave.main.vo.AppScore;
import com.mas.rave.main.vo.AppannieInfo;
import com.mas.rave.main.vo.AppannieInfoBase;
import com.mas.rave.main.vo.Country;
import com.mas.rave.main.vo.User;
import com.mas.rave.service.AppAlbumColumnService;
import com.mas.rave.service.AppAlbumResService;
import com.mas.rave.service.AppAlbumResTempService;
import com.mas.rave.service.AppAlbumService;
import com.mas.rave.service.AppFileService;
import com.mas.rave.service.AppInfoConfigService;
import com.mas.rave.service.AppInfoService;
import com.mas.rave.service.AppScoreService;
import com.mas.rave.service.AppannieInfoBaseService;
import com.mas.rave.service.AppannieInfoService;
import com.mas.rave.service.CategoryService;
import com.mas.rave.service.CountryService;
import com.mas.rave.service.ProxyIPService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.email.SendEmail;
import com.mas.rave.vo.TopVO;

public class AppBatchDistributionTask{

	private static final int fatherChannelId=200000;
	private static final int channelId=200001;
	//private static final int cpId = 300011;
	private static final int handSource = 0;
	private static final int autoSource = 1;
	private static final int globalKey = 1;
	private static final int rankAll=10000;
	private static int getIpNum=0;
	private static String BASE_URL = "http://www.appannie.com";
	public static Map<String,Integer> tempMap=Collections.synchronizedMap(new HashMap<String,Integer>());
	public static List<String> tempList= Collections.synchronizedList(new ArrayList<String>());
	Logger log = Logger.getLogger(AppBatchDistributionTask.class);
	private AppAlbumColumnService appAlbumColumnService;

	private AppAlbumService appAlbumService;

	private AppAlbumResTempService appAlbumResTempService;

	private AppFileService appFileService;

	private AppInfoService appInfoService;

	private AppInfoConfigService appInfoConfigService;

	private CountryService countryService;
	
	private AppannieInfoService appannieInfoService;
	
	private ProxyIPService proxyIPService;
	
	private AppScoreService appScoreService;
	
	private AppannieInfoBaseService appannieInfoBaseService;
	
	private Elements elements;
	
	private int appType;
	private int raveId;
	private static List<String[]> ipPortList=null;
	
	private Country country;
	
	private AppAlbum appAlbum;

	public AppBatchDistributionTask() {
	}

	public AppBatchDistributionTask(int raveId,int appType,Elements elements) {
		this.appType = appType;
		this.elements = elements;
		this.raveId = raveId;
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

	public void setAppannieInfoService(AppannieInfoService appannieInfoService) {
		this.appannieInfoService = appannieInfoService;
	}

	public void setAppannieInfoBaseService(
			AppannieInfoBaseService appannieInfoBaseService) {
		this.appannieInfoBaseService = appannieInfoBaseService;
	}

	public void runAppBatchDistribution() {
		tempMap.clear(); //清除临时列表ip		
		country =  countryService.getCountry(raveId);		
		appAlbum = appAlbumService.getAppAlbum(appType);
		//获取当前分发的类别
		String currentAlbum = country.getName() +"-"+ appAlbum.getName();
		try{
			List<AppAlbumColumn> listAppAlbumColumn = appAlbumColumnService
					.getAppAlbumColumnsByAppAlbumId(appType);
			ipPortList = proxyIPService.getIpPort();
			//获取手动分发的排行
//			Map<Integer, Map<Integer, AppAlbumRes>> maphand = getHandDistribute(listAppAlbumColumn,raveId);
			//按国家清除
			appAlbumResTempService.deleteBySource(appType,null,raveId,autoSource);
			Calendar calendar = Calendar.getInstance();//获取的是系统当前时间  
			calendar.add(Calendar.DATE, -4);  
			//根据创建时间删除			
			appInfoConfigService.deleteByTime(3,calendar.getTime());
			appInfoConfigService.updateStatusBy(false);
			//写入配置文件
			for (int i = 0; i < elements.size(); i++) {
				// i 表示排行榜+1
				updateAppConfig(elements.get(i),i);
			}
			String simulatedSwitch="false";
			List<AppScore> scores = appScoreService.getAllScore();
			for(AppScore appSocre:scores ){
				 if (appSocre.getScoreKey().equals("SimulatedSwitch")) {
					 simulatedSwitch= appSocre.getScoreValue();
				 }
			}
			if("true".equals(simulatedSwitch)){
				//删除旧的分发资源
				AppannieInfo criteria = new AppannieInfo();
				criteria.setRaveId(raveId);
				criteria.setAlbumId(appType);
				appannieInfoService.deleteByCondition(criteria);				
			}
			
			//批量分发
			appBatchRes(simulatedSwitch,elements,listAppAlbumColumn);
					
			// 更新排序信息
			autoUploadAutoSort(listAppAlbumColumn,raveId);
            //golbal分发处理
			globalHandOut(listAppAlbumColumn);	
			User user = AppAlbumColumnController.map.get(currentAlbum);
			AppAlbumColumnController.map.remove(currentAlbum);
			log.info("AppBatchDistributionTask run  分发完成   AppAlbumColumnController currentAlbum :"+country.getName()+"---"+appAlbum.getName()+"---"+AppAlbumColumnController.map.containsKey(currentAlbum) );
			//Email发送错误列表给客户
			sendDistributeFailMail(currentAlbum,user);
			//在分发操作时，记录访问失败的代理IP，清除
			clearFailIp();
		} catch (Exception e) {
//			AppAlbumColumnController.map.remove(currentAlbum);
			log.error("AppBatchDistributionTask run", e);
		}
	}

	private void sendDistributeFailMail(String currentAlbum,User user) {
		String ccList = "";
		List<AppScore> scores = appScoreService.getAllScore();
		for(AppScore appSocre:scores ){
			 if (appSocre.getScoreKey().equals("Email")) {
				 ccList= appSocre.getScoreValue();
				String[] ccArray =  ccList.split(",");
				StringBuilder sb = new StringBuilder();
				for(String cc:ccArray){
					Pattern p = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
					Matcher m = p.matcher(cc);
					if( m.matches()){
						sb.append(cc).append(",");
					}
				}
				if(sb!=null&&sb.length()>0){					
					ccList = sb.substring(0, sb.length()-1);
				}else{
					ccList=null;
				}
			 }
		}
		String title="分发" +currentAlbum+"完成通知";
		StringBuilder sb = new StringBuilder();
		sb.append(currentAlbum+" 分发已经完成!!!");
		for(String temp:tempList){
			sb.append("\r\n").append(temp);
		}	
		tempList.clear();
		if(user!=null&&user.getEmail()!=null&&!user.getEmail().equals("")){
			try {
			//send 
			SendEmail sendEmail = new SendEmail();
			boolean bool = sendEmail.defaultSendMethod(Constant.MAIL_SMTP, Constant.MAIL_PORT, 
						Constant.MAIL_USERNAME, Constant.MAIL_PASSOWRD,Constant.MAIL_SENDER,
						user.getEmail(), ccList, title, sb.toString());
			//失败重发
			if(bool==false){
				sendEmail.defaultSendMethod(Constant.MAIL_SMTP, Constant.MAIL_PORT, 
						Constant.MAIL_USERNAME, Constant.MAIL_PASSOWRD,Constant.MAIL_SENDER,
						user.getEmail(), ccList, title, sb.toString());
			}
			} catch (Exception e) {
				log.error("@@@appannie 分发 SendEmail:" + e.toString());
			}
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

//	/**
//	 * 根据AppAlbumColumn获取手动分发的数据和排行
//	 * @param listAppAlbumColumn
//	 * @return
//	 */
//	private Map<Integer, Map<Integer, AppAlbumRes>> getHandDistribute(
//			List<AppAlbumColumn> listAppAlbumColumn,int raveId) {
//		//第一个key表示albumColumnId 第二个key 表示排序号，value表示数据AppAlbumRes
//		Map<Integer,Map<Integer, AppAlbumRes>> maphand  = new HashMap<Integer, Map<Integer,AppAlbumRes>>();
//		//查询出所有的手动生效的数据， 放入maphand 中			
//		//List<Integer>  ids = appAlbumResTempService.getids();			
//		
//			for (AppAlbumColumn appAlbumColumn : listAppAlbumColumn) {
//				//------------modify by lulin 20140508---------------
//				List<AppAlbumRes> appAlbumReshandlist = appAlbumResTempService
//						.getByFileColumnAndSource(null, appAlbumColumn,-1, raveId);//all=-1
//				Map<Integer,AppAlbumRes> maphand1 = new LinkedHashMap<Integer, AppAlbumRes>();//必须采用有序hashMap
//				int n=0;//每一个分类下的排名,从0开始编排名，即0表示第一名
//				for(AppAlbumRes appAlbumRes:appAlbumReshandlist){
//					if(appAlbumRes.getSource()==0){
//						maphand1.put(n, appAlbumRes);
//						//System.out.println("####ColumnId="+appAlbumColumn.getColumnId()+",appAlbumRes="+appAlbumRes.getAppName()+",n="+n);
//					}
//					n++;
//				}
//				maphand.put(appAlbumColumn.getColumnId(), maphand1)	;
//				//---------------------------				
//			}
//		return maphand;
//	}

	/**
	 * 写入配置文件
	 * @param ei
	 * @param i
	 */
	private void updateAppConfig(Element ei,int i) {
		// eitds 中包含五个元素，第一个表示免费排行，eitds.get(0)
		Element eitds = ei.getElementsByTag("td").get(0);
		Element divElement = eitds.select("div.main-info").first();
		String appName = divElement.select("span.title-info").get(0)
				.attr("title");
		String packageName = divElement.select("span").last()
				.text();
		AppInfoConfig config = new AppInfoConfig();
		config.setName(appName);
		config.setDescription(String.valueOf(i+1));
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
			appInfoConfigService.upAppInfoConfig(config1);
		}
	}
	/**
	 * 是否把分发数据复制到global中
	 * @param listAppAlbumColumn
	 */
	private void globalHandOut(List<AppAlbumColumn> listAppAlbumColumn){
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
			appAlbumResTempService.deleteBySource(appType,null,globalKey,autoSource);
			//获取所有defaultCountry 自动 global分发数据
			for (AppAlbumColumn appAlbumColumn : listAppAlbumColumn) {	
				//查询出所有的数据
				List<AppAlbumRes> appAlbumResList = appAlbumResTempService.getByFileColumnAndSource(null, appAlbumColumn,autoSource, raveId);
			
				for(AppAlbumRes appAlbumRes: appAlbumResList){
					appAlbumRes.setRaveId(globalKey);
					appAlbumRes.setId(null);	
					List<AppAlbumRes> appAlbumResList1 = appAlbumResTempService.getByFileColumnAndSource(appAlbumRes.getAppInfo(), appAlbumColumn,handSource, globalKey);
					//数据不存在,则添加
					if(appAlbumResList1==null ||appAlbumResList1.size()==0){
						appAlbumResTempService.addAppAlbumResTemp(appAlbumRes);
					}else{
						String str="已经被手动配置了!";
						recordAppBatchError(appAlbumRes.getAppName(),"Global",appAlbum.getName()+"--"+appAlbumColumn.getName(),str );
					}
				}
			}
			
			// 更新排序信息
			autoUploadAutoSort(listAppAlbumColumn,globalKey);
	    }
	}

	/**
	 * 更新排序
	 * @param listAppAlbumColumn
	 * @param maphandGlobal
	 * @throws Exception 
	 */
	private void autoUploadAutoSort(List<AppAlbumColumn> listAppAlbumColumn,int raveId){
		for (AppAlbumColumn appAlbumColumn : listAppAlbumColumn){			
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
	/**
	 * 批量分发
	 * @param elements
	 */
	public void appBatchRes(String simulatedSwitch,Elements elements,List<AppAlbumColumn> listAppAlbumColumn){
		AppInfo appInfo = new AppInfo();
		for (int i = 0; i < elements.size(); i++) {
			// i 表示排行榜+1
		    //Element ei = elements.get(i);
			// eitds 中包含五个元素，第一个表示免费排行，eitds.get(0)
			Element eitds = elements.get(i).getElementsByTag("td").get(0);
//			Element divElement = eitds.select("div.main-info").first();
			String appName = eitds.select("div.main-info").first().select("span.title-info").get(0)
					.attr("title");	
			//判断appName 是否已经录入
			appInfo = appInfoService.findAppInfo(fatherChannelId, appName);			
			if(appInfo==null){
				String mappingName = null;
				try {
					mappingName = NameMappingSerivce.getNameMapping(appName);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.error("AppAlbumColumnController top", e);
				}
				if(mappingName!=null){
					appInfo = appInfoService.findAppInfo(fatherChannelId, mappingName);
				}
			}
           
			if(appInfo!=null){
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
				}else{
					map0 = getAppannieInfoByHttpRequest(eitds, appName,
							appInfo, map0);
					try {
						Thread.sleep((new Random().nextInt(10)+20)*1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						log.error("AppBatchDistributionTask run", e);
					}
				}
				
				for (AppAlbumColumn appAlbumColumn : listAppAlbumColumn) {
							
						int listType = getListType(appAlbumColumn);
						if(map0.size()>0){
							appresByElement(simulatedSwitch,map0,i,appName,listType,appAlbumColumn,appInfo);
						}
				}

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
			String appName, AppInfo appInfo, Map<String, Object> map0) {
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
				log.error("AppBatchDistributionTask appBatchDistribution ip:"+ip+"第"+n+"次爬取资源出错！"+appInfo.getName());
				if(n>3){
					//记录爬取失败的记录
					String str = "爬取分发资源失败!";
					recordAppBatchError(appName,country.getName(),appAlbum.getName(),str);
					Element divElement = eitds.select("div.main-info").first();

					String appUrl = divElement.select("span.title-info").get(0).child(0)
							.attr("href");			
					String url = BASE_URL + appUrl;
					map0.put(AppBatchDistributionConstant.APPURL, url);
					map0.put(AppBatchDistributionConstant.INSTALLTOTAL,null);
					map0.put(AppBatchDistributionConstant.SIZE,0.0);
					map0.put(AppBatchDistributionConstant.RATER,0.0);
					map0.put(AppBatchDistributionConstant.INITIALRELEASEDATE,null);
					addAppannieInfoBase(appName, map0);//将信息加到t_appannie_info_base表				

				}
			}
			
			if(n>3){
				break;
			}
		}
		return map0;
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
		appannieInfoBase.setAppName(appName);
		appannieInfoBase.setUrl((String)map0.get(AppBatchDistributionConstant.APPURL));
		AppannieInfoBase appannieInfoBase1 = new AppannieInfoBase();
		appannieInfoBase1.setAppName(appName);
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
	
	public void appresByElement(String simulatedSwitch,Map<String, Object> map0 ,int i, String appName, int listType,
			AppAlbumColumn appAlbumColumn,AppInfo appInfo) {
		try{
		if (appInfo != null) {	
			if(appInfo.getCategory()!=null){
					// 获取appfile
					AppFile appFile = appFileService.getAppFileByappIdAndCountry(appInfo.getId(),
							raveId,channelId);
					if (appFile != null) {
						AppannieService appScore = new AppannieService();

						if (listType != 0) {
							// 查看是否是手动配置
							List<AppAlbumRes> appAlbumReshandlist = appAlbumResTempService
									.getByFileColumnAndSource(appInfo, appAlbumColumn,
											handSource, raveId);

							if (appAlbumReshandlist.size() == 0) {

								TopVO vo = getAppAnnieScore(map0, i, listType,
										appInfo,appFile, appScore);
//								System.out.println(vo.getScore());
								//获取数据成功
								if("true".equals(simulatedSwitch)&&(double)map0.get(AppBatchDistributionConstant.RATER)>0){
									addAppannieInfoService(map0, i, appAlbumColumn,
											appInfo, appFile);									
								}
								listType = 0;
								addAppAlbumResTemp(appName, appAlbumColumn,
										appInfo, appFile, vo);
								
							}else{
								String str="已经被手动配置了!";
								recordAppBatchError(appName,country.getName(),appAlbum.getName()+"--"+appAlbumColumn.getName(),str );
							}
						}
					}
					
		    	}
		}
		}catch(Exception e){
			String str="资源录入失败!";
			recordAppBatchError(appName,country.getName(),appAlbum.getName()+"--"+appAlbumColumn.getName(),str );
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
	private void addAppannieInfoService(Map<String, Object> map0, int i,
			AppAlbumColumn appAlbumColumn, AppInfo appInfo, AppFile appFile) {
		AppannieInfo appannieInfo = new AppannieInfo();
		appannieInfo.setAlbumId(appType);
		appannieInfo.setRaveId(raveId);
		appannieInfo.setAppId(appInfo.getId());
		appannieInfo.setAppName(appInfo.getName());
		appannieInfo.setApkId(appFile.getId());
		appannieInfo.setColumnId(appAlbumColumn.getColumnId());
		if(map0.get(AppBatchDistributionConstant.INSTALLTOTAL)!=null){
			appannieInfo.setAnnieInstallTotal((double)map0.get(AppBatchDistributionConstant.INSTALLTOTAL));			
		}
		if(map0.get(AppBatchDistributionConstant.INITIALRELEASEDATE)!=null){
			appannieInfo.setInitialReleaseDate((Date)map0.get(AppBatchDistributionConstant.INITIALRELEASEDATE));			
		}
		if(map0.get(AppBatchDistributionConstant.SIZE)==null||(double)map0.get(AppBatchDistributionConstant.SIZE)==0.0){
			appannieInfo.setSize(appFile.getFileSize());
		}else{
			appannieInfo.setSize((double)map0.get(AppBatchDistributionConstant.SIZE));		
		}
		if(map0.get(AppBatchDistributionConstant.RATER)!=null){
			appannieInfo.setAnnieRatings((double)map0.get(AppBatchDistributionConstant.RATER));
		}
		appannieInfo.setAnnieRank(i + 1);
		appannieInfoService.insert(appannieInfo);
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
			TopVO vo) {
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
		newappAlbumRes.setRaveId(raveId);
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
						autoSource, raveId);
		if(appAlbumResautolist.size()>0){
			appAlbumResTempService.delAppAlbumResTemp(appAlbumResautolist.get(0).getId());
//			String str="资源已经录入!";
//			recordAppBatchError(appName,country.getName(),appAlbum.getName()+"--"+appAlbumColumn.getName(),str );
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
	private TopVO getAppAnnieScore(Map<String, Object> map0, int i,
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
		if(initialReleaseDate!=null){
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
}
