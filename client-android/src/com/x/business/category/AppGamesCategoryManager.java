/**   
 * @Title: AppGamesCategoryManager.java
 * @Package com.x.business.category
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2015-10-20 上午11:08:57
 * @version V1.0   
 */

package com.x.business.category;

import org.json.JSONObject;

import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.AppGamesCategoryRequest;
import com.x.publics.http.model.AppGamesCategoryResponse;
import com.x.publics.http.model.Pager;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.utils.Constan;
import com.x.publics.utils.JsonUtil;

import android.content.Context;
import android.os.Handler;

/**
 * @ClassName: AppGamesCategoryManager
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2015-10-20 上午11:08:57
 * 
 */

public class AppGamesCategoryManager {
	public static final int onSuccess = 200;
	public static final int onFailure = 500;

	private Context context;
	public static AppGamesCategoryManager appGamesCategoryManager;

	public AppGamesCategoryManager() {
		super();
	}

	public AppGamesCategoryManager(Context context) {
		super();
		this.context = context;
	}

	public static AppGamesCategoryManager getInstance(Context context) {
		if (appGamesCategoryManager == null) {
			appGamesCategoryManager = new AppGamesCategoryManager(context);
		}
		return appGamesCategoryManager;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	/* get app games categories list */
	// /////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * @Title: getSubCategoriesList
	 * @Description: TODO
	 * @param @param context
	 * @param @param mHandler
	 * @param @param raveId
	 * @param @param ct
	 * @param @param ps
	 * @param @param pn
	 * @return void
	 */
	public void getSubCategoriesList(Context context, final Handler mHandler,Pager pager,int cat) {
		AppGamesCategoryRequest request = new AppGamesCategoryRequest();
		request.setPager(pager);
		request.setCt(cat);
		DataFetcher.getInstance().getAppGameCategory(request,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						AppGamesCategoryResponse appGamesCategoryResponse = (AppGamesCategoryResponse) JsonUtil
								.jsonToBean(response,
										AppGamesCategoryResponse.class);
						if (appGamesCategoryResponse != null
								&& appGamesCategoryResponse.categorylist != null) {
							mHandler.sendMessage(mHandler.obtainMessage(
									onSuccess, appGamesCategoryResponse));
						} else {
							mHandler.sendEmptyMessage(onFailure);
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						mHandler.sendEmptyMessage(onFailure);
					}

				}, true);
	}

}
