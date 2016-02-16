package com.mas.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * 工具类。
 */
public class HttpUtils {
	
	 /** 
     * 执行一个HTTP POST请求，返回请求响应的内容
     * @param url        请求的URL地址 
     * @param params 请求的查询参数,可以为null 
     * @return 返回请求响应的内容 
     */ 
     public static String doPost(String url, String body) { 
    	 StringBuffer stringBuffer = new StringBuffer();
			HttpEntity entity = null;
			BufferedReader in = null;
			HttpResponse response = null;
			try {
				DefaultHttpClient httpclient = new DefaultHttpClient();
				HttpParams params = httpclient.getParams();
				HttpConnectionParams.setConnectionTimeout(params, 20000);
				HttpConnectionParams.setSoTimeout(params, 20000);
				HttpPost httppost = new HttpPost(url);
				httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");

				httppost.setEntity(new ByteArrayEntity(body.getBytes("UTF-8")));
				response = httpclient.execute(httppost);
				entity = response.getEntity();
				in = new BufferedReader(new InputStreamReader(entity.getContent()));
				String ln;
				while ((ln = in.readLine()) != null) {
					stringBuffer.append(ln);
					stringBuffer.append("\r\n");
				}
				httpclient.getConnectionManager().shutdown();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (IllegalStateException e2) {
				e2.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != in) {
					try {
						in.close();
						in = null;
					} catch (IOException e3) {
						e3.printStackTrace();
					}
				}
			}
			return stringBuffer.toString();
    } 
     
     /** 
      * 执行一个HTTP POST请求，返回请求响应的内容
      * @param url        请求的URL地址 
      * @param params 请求的查询参数,可以为null 
      * @return 返回请求响应的内容 
      */ 
      public static String doPostJson(String url, String body) { 
     	 StringBuffer stringBuffer = new StringBuffer();
 			HttpEntity entity = null;
 			BufferedReader in = null;
 			HttpResponse response = null;
 			try {
 				DefaultHttpClient httpclient = new DefaultHttpClient();
 				HttpParams params = httpclient.getParams();
 				HttpConnectionParams.setConnectionTimeout(params, 30000);
 				HttpConnectionParams.setSoTimeout(params, 30000);
 				HttpPost httppost = new HttpPost(url);
 				httppost.setHeader("Content-Type", "application/json");

 				httppost.setEntity(new ByteArrayEntity(body.getBytes("UTF-8")));
 				response = httpclient.execute(httppost);
 				entity = response.getEntity();
 				in = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));
 				String ln;
 				while ((ln = in.readLine()) != null) {
 					stringBuffer.append(ln);
 					stringBuffer.append("\r\n");
 				}
 				httpclient.getConnectionManager().shutdown();
 			} catch (ClientProtocolException e) {
 				e.printStackTrace();
 				return "timeOut";
 			} catch (IOException e1) {
 				e1.printStackTrace();
 			} catch (IllegalStateException e2) {
 				e2.printStackTrace();
 			} catch (Exception e) {
 				e.printStackTrace();
 			} finally {
 				if (null != in) {
 					try {
 						in.close();
 						in = null;
 					} catch (IOException e3) {
 						e3.printStackTrace();
 					}
 				}
 			}
 			return stringBuffer.toString();
     } 
}
