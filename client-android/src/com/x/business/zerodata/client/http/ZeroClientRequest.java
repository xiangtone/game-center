/**   
* @Title: ZeroClientRequest.java
* @Package com.mas.amineappstore.business.zerodata.client.http
* @Description: TODO 

* @date 2014-3-20 上午11:03:51
* @version V1.0   
*/

package com.x.business.zerodata.client.http;

import android.content.Context;

import com.x.publics.http.volley.RequestQueue;
import com.x.publics.http.volley.RetryPolicy;
import com.x.publics.http.volley.Request.Method;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.http.volley.toolbox.JsonObjectRequest;
import com.x.publics.http.volley.toolbox.Volley;
import com.x.publics.utils.LogUtil;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
* @ClassName: ZeroClientRequest
* @Description: TODO 

* @date 2014-3-20 上午11:03:51
* 
*/

public class ZeroClientRequest {

	private static ZeroClientRequest zeroClientRequest;
	private RequestQueue requestQueue;
	private static final String REQUEST_TAG = "ZeroClientRequest";

	private ZeroClientRequest(Context context) {
		requestQueue = Volley.newRequestQueue(context);
	}

	public static ZeroClientRequest getInstance(Context context) {
		if (zeroClientRequest == null)
			zeroClientRequest = new ZeroClientRequest(context);
		return zeroClientRequest;
	}

	// "GET" Request
	private void get(String url, Listener<JSONObject> listener, ErrorListener errorListener, boolean shouldCache,
			RetryPolicy retryPolicy) {
		JsonObjectRequest request = new JsonObjectRequest(Method.GET, url, null, listener, errorListener);
		request.setShouldCache(shouldCache);
		if (retryPolicy != null)
			request.setRetryPolicy(retryPolicy);
		LogUtil.getLogger().d("get data ==>url:" + url);
		LogUtil.getLogger().i("zerodata", "ZeroClientRequest[url=" + url + "]");
		request.setTag(REQUEST_TAG);
		requestQueue.add(request);
	}

	// "POST" Request
	private void post(String url, JSONObject object, Listener<JSONObject> listener, ErrorListener errorListener) {
		JsonObjectRequest request = new JsonObjectRequest(Method.POST, url, object, listener, errorListener);
		LogUtil.getLogger().d("post data ==>url:" + url + ",JSONObject==>" + object.toString());
		requestQueue.add(request);
	}

	/** 
	* @Title: cancleAll 
	* @Description: 取消所有请求 
	* @param      
	* @return void    
	*/

	public void cancleAll() {
		requestQueue.cancelAll(REQUEST_TAG);
	}

	/** 
	* @Title: getTransferInfo 
	* @Description: 获取传输信息请求 
	* @param @param originalUrl
	* @param @param request
	* @param @param listener
	* @param @param errorListener     
	* @return void    
	*/

	public void getTransferInfo(String originalUrl, HashMap<String, Object> request, Listener<JSONObject> listener,
			ErrorListener errorListener, RetryPolicy retryPolicy) {
		request(originalUrl, request, listener, errorListener, retryPolicy);
	}

	public void updateServerProgress(String originalUrl, HashMap<String, Object> request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		request(originalUrl, request, listener, errorListener, null);
	}

	/** 
	* @Title: disconnectRequest 
	* @Description: 断开连接请求
	* @param @param originalUrl
	* @param @param request
	* @param @param listener
	* @param @param errorListener     
	* @return void    
	*/

	public void disconnectRequest(String originalUrl, HashMap<String, Object> request, Listener<JSONObject> listener,
			ErrorListener errorListener) {
		request(originalUrl, request, listener, errorListener, null);
	}
	
	/** 
	* @Title: reconnectRequest 
	* @Description: 重新连接请求
	* @param @param originalUrl
	* @param @param request
	* @param @param listener
	* @param @param errorListener    
	* @return void    
	*/ 
	
	public void reconnectRequest(String originalUrl, HashMap<String, Object> request, Listener<JSONObject> listener,
			ErrorListener errorListener) {
		request(originalUrl, request, listener, errorListener, null);
	}

	public void request(String originalUrl, HashMap<String, Object> request, Listener<JSONObject> listener,
			ErrorListener errorListener, RetryPolicy retryPolicy) {
		StringBuilder url = new StringBuilder(originalUrl);
		if (request != null && !request.isEmpty()) {
			url.append("?");
			StringBuilder tempUrl = new StringBuilder();
			Iterator it = request.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				tempUrl.append(entry.getKey());
				tempUrl.append("=");
				tempUrl.append(entry.getValue());
				tempUrl.append("&");
			}
			tempUrl = tempUrl.replace(tempUrl.length() - 1, tempUrl.length(), "");
			try {
				url.append(URLEncoder.encode(tempUrl.toString(), "UTF-8"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		get(url.toString(), listener, errorListener, false, retryPolicy);
	}

}
