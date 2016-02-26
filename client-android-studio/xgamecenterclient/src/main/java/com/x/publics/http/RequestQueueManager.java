/**   
* @Title: RequestQueueManager.java
* @Package com.x.http
* @Description: TODO 

* @date 2014-1-7 下午01:49:58
* @version V1.0   
*/

package com.x.publics.http;

import android.content.Context;

import com.x.publics.http.volley.RequestQueue;
import com.x.publics.http.volley.toolbox.Volley;

/**
* @ClassName: RequestQueueManager
* @Description: 请求队列管理类 

* @date 2014-1-7 下午01:49:58
* 
*/

public class RequestQueueManager {

	private static RequestQueue mRequestQueue;

	private RequestQueueManager() {

	}

	public static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);
	}

	public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("Not initialized");
		}
	}

}
