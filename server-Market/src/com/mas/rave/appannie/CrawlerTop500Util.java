package com.mas.rave.appannie;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mas.rave.task.AppBatchDistributionConstant;


public class CrawlerTop500Util {

	private static String BASE_URL = "http://www.appannie.com";
	
	public static Map<String, Object> getAppInfoByUrl(String url) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(AppBatchDistributionConstant.APPURL, url);
		Document doc = Jsoup
						.connect(url)
						.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) Opera/9.80 AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
						.timeout(15000).get();
		
		// 版本初始日期
		Element appInfoVersion = doc.body().select("div.app-info-version")
				.first();
		 Map<String, Object> versionMap = getAppInfoVersion(appInfoVersion);		
		 map.putAll(versionMap);
		 
		//评分信息
		Element appInfoRating = doc.body().select("div.app-info-rating").first();
		if(appInfoRating!=null){
			Element appInfoRatingHeaderEle = appInfoRating.select("div.app_slide_header")
					.first();
			if(appInfoRatingHeaderEle!=null){
				double rater =Double.parseDouble(appInfoRatingHeaderEle.getElementsByTag("strong").first().text());
				map.put(AppBatchDistributionConstant.RATER, rater);				
			}else{
				map.put(AppBatchDistributionConstant.RATER, 0.0);
			}
		}else{
			map.put(AppBatchDistributionConstant.RATER, 0.0);
		}
		//发行商
		Element appInfoIssuer =  doc.body().select("div.summary-item").select("a.summary-item-text").last();
		if(appInfoIssuer!=null){
			Map<String, Object> aboutMap = getAppInfoIssuer(appInfoIssuer);
			map.putAll(aboutMap);	
		}

//		//大小，安装量
//		Element appInfoAbout = doc.body().select("div.about_app").first();
//		if(appInfoAbout!=null){
//			Map<String, Object> aboutMap = getAppInfoAbout(appInfoAbout);
//			map.putAll(aboutMap);			
//		}
//		

		return map;
	}
	public static Map<String, Object> getAppInfoByUrl(Element e) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		Element divElement = e.select("div.main-info").first();

		String appUrl = divElement.select("span.title-info").get(0).child(0)
				.attr("href");			
		String url = BASE_URL + appUrl;
		map.put(AppBatchDistributionConstant.APPURL, url);
		Document doc = Jsoup
						.connect(url)
						.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) Opera/9.80 AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
						.timeout(15000).get();

		// 版本初始日期
		Element appInfoVersion = doc.body().select("div.app-info-version")
				.first();
		 Map<String, Object> versionMap = getAppInfoVersion(appInfoVersion);		
		 map.putAll(versionMap);
		 
		//评分信息
		Element appInfoRating = doc.body().select("div.app-info-rating").first();
		if(appInfoRating!=null){
			Element appInfoRatingHeaderEle = appInfoRating.select("div.app_slide_header")
					.first();
			if(appInfoRatingHeaderEle!=null){
				double rater =Double.parseDouble(appInfoRatingHeaderEle.getElementsByTag("strong").first().text());
				map.put(AppBatchDistributionConstant.RATER, rater);				
			}else{
				map.put(AppBatchDistributionConstant.RATER, 0.0);
			}
		}else{
			map.put(AppBatchDistributionConstant.RATER, 0.0);
		}


		//大小，安装量
		Element appInfoAbout = doc.body().select("div.about_app").first();
		if(appInfoAbout!=null){
			Map<String, Object> aboutMap = getAppInfoAbout(appInfoAbout);
			map.putAll(aboutMap);			
		}
		
		//发行商
		Element appInfoIssuer =  doc.body().select("div.summary-item").select("a.summary-item-text").last();
		if(appInfoIssuer!=null){
			Map<String, Object> aboutMap = getAppInfoIssuer(appInfoIssuer);
			map.putAll(aboutMap);	
		}
		return map;
	}
	/**
	 * 发行商
	 * @param appInfoIsUser
	 * @return
	 * @throws Exception
	 */
	public static Map<String,Object> getAppInfoIssuer(Element appInfoIsUser)throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		String issuer = appInfoIsUser.text();
		
		map.put(AppBatchDistributionConstant.ISSUER, issuer);
		return map;
	}
/**
 * 获取版本信息
 * @param appInfoVersion
 * @return
 */
	public static Map<String, Object> getAppInfoVersion(Element appInfoVersion) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		DateFormat format =DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);

//		Element currentVersionEles = appInfoVersion.select("tr.first").first();
//		Element currentVersionDateEle = currentVersionEles.select("td.date").first();
//		Element currentVersionEle = currentVersionEles.select("td.version").first();

		Element initialReleaseEle = appInfoVersion.select("tr.last").first();
		Element initialReleaseDateEle = initialReleaseEle.select("td.date").first();

		//Feb 19, 2014
		try {
//			Date currentVersionDate = format.parse(currentVersionDateEle.text());
			Date initialReleaseDate = format.parse(initialReleaseDateEle.text());
//			map.put("currentVersion", currentVersionEle.text());
//			map.put("currentVersionDate", sdf.format(currentVersionDate));
			map.put(AppBatchDistributionConstant.INITIALRELEASEDATE, initialReleaseDate);

		} catch (ParseException e) {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日");
			Date initialReleaseDate;
			try {
				initialReleaseDate = format1.parse(initialReleaseDateEle.text());
				map.put(AppBatchDistributionConstant.INITIALRELEASEDATE, initialReleaseDate);

			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				map.put(AppBatchDistributionConstant.INITIALRELEASEDATE, null);
			}
		}


		return map;

	}
	/**
	 * 获取app 大小和安装量
	 * @param appInfoAbout
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getAppInfoAbout(Element appInfoAbout) throws Exception{
		 NumberFormat f = NumberFormat.getInstance( Locale.ENGLISH);
		Map<String, Object> map = new HashMap<String, Object>();
		Elements aboutEles =appInfoAbout.getElementsByTag("p");
		double size =0.0;
		double installTotal=0.0;
		if(aboutEles.size()==6){
			String sizeStr0 = aboutEles.get(1).select("b").remove().text();
			String sizeStr = aboutEles.get(1).text().replace(sizeStr0, "").trim();

			try {
				if (sizeStr.toUpperCase().endsWith("MB")) {
					size = (f.parse(sizeStr.substring(0, sizeStr.length() - 2).trim()))
							.doubleValue() * 1024 * 1024;
				} else if (sizeStr.toUpperCase().endsWith("KB")) {
					size = (f.parse(sizeStr.substring(0, sizeStr.length() - 2).trim()))
							.doubleValue() * 1024;
				}else{
					size=0.0;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			map.put(AppBatchDistributionConstant.SIZE, size);
			
			String annieInstallStr = aboutEles.get(5).select("b").remove().text();
			String annieInstallTotal = aboutEles.get(5).text().replace(annieInstallStr, "").trim();
			String[] annieInstallTotals = annieInstallTotal.split("-");
			if(annieInstallTotals.length==2){
				try {
					installTotal=(f.parse(annieInstallTotals[0].trim()).doubleValue()+f.parse(annieInstallTotals[1].trim()).doubleValue())/2;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		map.put(AppBatchDistributionConstant.INSTALLTOTAL, installTotal);
		return map;	
	}
//	/**
//	 * 获取评分信息
//	 * @param appInfoRating
//	 * @return
//	 */
//	public static Map<Object, Object> getAppInfoRating(Element appInfoRating) throws Exception{
//		Map<Object, Object> map = new HashMap<Object, Object>();
//		Element appInfoRatingHeaderEle = appInfoRating.select("div.app_slide_header")
//				.first();
//		String rater = appInfoRatingHeaderEle.getElementsByTag("strong").first().text();
//		map.put(AppBatchDistributionConstant.RATER, rater);
//
//		String raterNum =appInfoRatingHeaderEle.getElementsByTag("strong").last().text() ;
//		 NumberFormat f = NumberFormat.getInstance( Locale.ENGLISH);
//		try {
//			map.put("raterNum", f.parse(raterNum));
//		
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		Elements appInfoRatingTrEle = appInfoRating.select("table.rating_table").first()
//				.getElementsByTag("tr");
//		for(int i=0;i<appInfoRatingTrEle.size();i++){
//			//i=0
//			Element stars = appInfoRatingTrEle.get(i).select("td.stars").first();
//			int starsNum = stars.select("i.icon-star").size();
//             String  countStr = appInfoRatingTrEle.get(i).select("td.count").first().text();
//			String count =countStr.substring(1, countStr.length()-1);
//			try {
//				map.put(starsNum, f.parse(count));
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//		//评分系统更新时间
//		String appInfoRatingDate = appInfoRating.select("div.rating_date").first().text().replace("Updated on ", "");
//		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
//		DateFormat format =DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
//		try {
//			map.put("ratingDate",sdf.format(format.parse(appInfoRatingDate)));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return map;
//		
//	}

	
	public static void main(String[] args) {
		String url = "http://www.appannie.com/apps/google-play/app/com.gameloft.android.ANMP.GloftLBCR/";
		try {
			Map<String, Object>  map = getAppInfoByUrl(url);
			for(String key:map.keySet()){
				System.out.println(key+"   "+map.get(key));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
