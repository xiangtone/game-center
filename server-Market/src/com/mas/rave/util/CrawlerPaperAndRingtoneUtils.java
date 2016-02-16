package com.mas.rave.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mas.rave.appannie.ProxyService;


public class CrawlerPaperAndRingtoneUtils {
	static Logger log = Logger.getLogger(CrawlerPaperAndRingtoneUtils.class);

	public static Elements downloadRingtoneSByURL(String url){
		Document doc = null;
		Elements elems = null;
		while(doc == null){
			int i = 0;
			try {
				ProxyService.closeProxy();
				doc = Jsoup.connect(url).timeout(10000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
	  			log.error("CrawlerPaperAndRingtoneUtils downloadRingtoneSByURL 第"+i+"次抓取数据异常", e);
			}
			if(i>=5){
				break;
			}
		}
		
			//获取到所有的ringtone列表
			elems = doc.body().select("div.content").first().getElementsByTag("ul").first().children();
			return elems;
	}
	

	public static Elements downloadFirstPageByWallpaperDoc(Document doc){
		Elements elems = null;
		while(doc == null){
			int i = 0;
				ProxyService.closeProxy();
			if(i>=5){
				break;
			}
		}		
			//获取到所有的wallpapers列表
			elems = doc.body().select("div.content").first().select("a.pretty");
			return elems;
	}
	
	/**
	 * 获取http://www.voga360.com 中的 head.js 文件路径
	 * @param urlcategory http://www.voga360.com
	 * @return
	 * @throws IOException 
	 */
	public static String getHeadJSForURL(String urlcategory){
		Document doc = null;
		Element elem1 = null;
		String headjs =	null;
		
		int i = 0;
		while(doc == null){
			i++;
			try {
				ProxyService.closeProxy();
				doc = Jsoup.connect(urlcategory).timeout(10000).get();
			} catch (IOException e) {
	  			log.error("CrawlerPaperAndRingtoneUtils getHeadJSForURL 第"+i+"次抓取数据异常", e);

			}
			if(i>=5){
				break;
			}
		}
		if(doc!=null){
			elem1 = doc.body().select("body").first();
			headjs =	elem1.select("script").attr("src");
		}		
		 return headjs;

	}
	/**
	 * 获取ringtones/wallpapers的菜单的url 
	 * @param urlcategory
	 * @return
	 * @throws IOException 
	 */
	public static Map<String, String>  getSecondCategoryForUrl(String urlcategory,String typeList){
		Document doc = null;
		Map<String, String> paperhref= new HashMap<String, String>();
		String jsstr =null;
		int i = 0;
		while(jsstr==null){
			i++;
			try {
				ProxyService.closeProxy();
				jsstr= CrawlerPaperAndRingtoneUtils.GetFromHtml(urlcategory);
			} catch (Exception e) {
				// TODO Auto-generated catch block
	  			log.error("CrawlerPaperAndRingtoneUtils getSecondCategoryForUrl 第"+i+"次抓取数据异常", e);

			}
			if(i>=3){
				break;
			}
		}
		
		if(jsstr!=null){
			doc = Jsoup.parse(jsstr);
			Element paper =	 doc.body().getElementById(typeList);
			Elements eles =paper.select("ul").get(0).getElementById("ringtonesCatsLeft").children();
			for(Element e:eles){
				Element a = e.select("a").get(0);
				paperhref.put(a.text(), a.attributes().get("href"));
			}
			 return paperhref;
		}
		return null;
	}

/**
 * 解析head.js 文件
 * @param htmladdr
 * @return
 * @throws IOException
 */
	public static String GetFromHtml(String htmladdr) throws Exception{   
			HttpURLConnection url=null;   	
			String str =null;
			ProxyService.closeProxy();
			URL url1 = new URL(htmladdr);          
			url =   (HttpURLConnection)url1.openConnection ();   
			url.setRequestProperty("User-Agent", "mozlla/5.0");   
			url.setRequestProperty("Accept-Encoding", "gzip, deflate"); 
			url.setConnectTimeout(10000);
			url.connect();     
 
			String temp = null;     
			if(url!=null){   
			InputStream stream= url.getInputStream();   
			BufferedReader reader =   
			         new BufferedReader(new InputStreamReader(stream));   	         
			          while (null != (temp = reader.readLine())) {   
			  			ProxyService.closeProxy();
			          temp=new String(temp.getBytes("iso-8859-1"),"utf-8"); 
			         break;

			          }   
			  reader.close();   
			}     
      	  if(temp!=null){
      		  ScriptEngineManager factory = new ScriptEngineManager();       
          	  ScriptEngine engine = factory.getEngineByName("JavaScript");
  				engine.eval(temp);
  				str = (String) engine.get("headerHtml");
      	  }
      	 
			return str;
	}  
	
	/**
	 * get wallpapers category number
	 * @param url
	 * @return
	 */
		public static String getPicCategoryNumber(String url) {   
			   if ((url != null) && (url.length() > 0)) {   
			        int i = url.lastIndexOf('.');   
			       if ((i >-1) && (i < (url.length()))) {   
			            String url1 = url.substring(0, i);  
			            int j = url.lastIndexOf('_'); 
			            if ((j >-1) && (j < (url1.length()))) {   
			            	 return url1.substring(j+1, i);   
			            }
			       }   
			    }   
			    return url;   
		}
		public static String getReturnData(String urlString){			
			String res = ""; 
			int i = 0;
			while(res.equals("")){
				i ++;
				try { 
		  				URL url = new URL(urlString);
						ProxyService.closeProxy();
		  				java.net.HttpURLConnection conn = (java.net.HttpURLConnection)url.openConnection();
		  				conn.setDoOutput(true);
		  				conn.setConnectTimeout(10000);
		  				conn.setRequestMethod("GET");
		  				java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(),"UTF-8"));
		  				String line;
		  				while ((line = in.readLine()) != null) {
		  					res += line;
		  				}
	  					in.close();
		  		} catch (Exception e) {
		  			log.error("CrawlerPaperAndRingtoneUtils crawlerJsStr 第"+i+"次抓取数据异常", e);
		  		}
				if(i>=3){
					break;
				}
			}
		
//			System.out.println(res);
			return res;
		}
}
