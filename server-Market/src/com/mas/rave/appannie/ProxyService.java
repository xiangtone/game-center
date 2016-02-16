package com.mas.rave.appannie;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Random;
public class ProxyService {
	/**
	 * 执行一个随机代理
	 */
	public static String doRandomProxy(List<String[]> list) {
		String[] s = randomProxy(list);
		if(s!=null && s.length==2){
			//System.out.println(s[0]+":"+s[1]);
			System.getProperties().setProperty("http.proxyHost", s[0]);
			System.getProperties().setProperty("http.proxyPort", s[1]);	
			return s[0]+":"+s[1];
		}else{
			closeProxy();
			return null;
		}
	}
	public static void closeProxy(){
		System.getProperties().setProperty("proxySet", "false");
		System.getProperties().remove("http.proxyHost");
		System.getProperties().remove("http.proxyPort");
	}
	private static String[] randomProxy(List<String[]> ipPortList){
		if(ipPortList!=null && ipPortList.size()>0){
			int index = new Random().nextInt(ipPortList.size());
			if(index<ipPortList.size()){
				return ipPortList.get(index);
			}
		}
		return null;
	}

	public static boolean checkIpPort(String ip,String port) {
		// System.getProperties().setProperty("proxySet", "true");
		// //如果不设置，只要代理IP和代理端口正确,此项不设置也可以
		System.getProperties().setProperty("http.proxyHost", ip);
		System.getProperties().setProperty("http.proxyPort", port);
//		System.out.println(getHtml("http://www.ip138.com/ip2city.asp")); // 判断代理是否设置成功
		
		//String s = getHtml("http://www.ip138.com/ip2city.asp");
		String s = getHtml("http://www.appannie.com");
		if(s!=null && s.length()>100){
			return true;
		}
		return false;
	}

	private static String getHtml(String address) {
		StringBuffer html = new StringBuffer();
		String result = null;
		try {
			URL url = new URL(address);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; GTB5; .NET CLR 2.0.50727; CIBA)");
			BufferedInputStream in = new BufferedInputStream(
					conn.getInputStream());
			try {
				String inputLine;
				byte[] buf = new byte[4096];
				int bytesRead = 0;
				while (bytesRead >= 0) {
					inputLine = new String(buf, 0, bytesRead, "ISO-8859-1");
					html.append(inputLine);
					bytesRead = in.read(buf);
					inputLine = null;
				}
				buf = null;
			} finally {
				in.close();
				conn = null;
				url = null;
			}
			result = new String(html.toString().trim().getBytes("ISO-8859-1"),
					"utf-8").toLowerCase();
		} catch (Exception e) {
//			e.printStackTrace();
			return null;
		}
		html = null;
		return result;
	}
}
