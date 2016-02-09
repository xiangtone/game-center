/**   
* @Title: DynamicListViewManager.java
* @Package com.mas.amineappstore.business.dynamiclistview
* @Description: TODO(用一句话描述该文件做什么)

* @date 2015-3-17 下午3:38:16
* @version V1.0   
*/

package com.x.business.dynamiclistview;

import java.util.Random;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.x.R;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.AppDownloadLogRequest;
import com.x.publics.http.model.AppDownloadLogResponse;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SharedPrefsUtil;

/**
* @ClassName: DynamicListViewManager
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2015-3-17 下午3:38:16
* 
*/

public class DynamicListViewManager {

	public static final int onSuccess = 200;
	public static final int onFailure = 500;
	private static DynamicListViewManager dynamicListViewManager;
	private static final int[] colors = { R.color.red_background_color, R.color.yellow_background_color,
			R.color.blue_background_color, R.color.green_background_color, R.color.brown_background_color };

	private DynamicListViewManager() {
		// TODO Auto-generated constructor stub
	}

	public static synchronized DynamicListViewManager getInstance() {
		if (null == dynamicListViewManager)
			dynamicListViewManager = new DynamicListViewManager();
		return dynamicListViewManager;
	}

	/**
	* @Title: setBackgroudResource 
	* @Description: TODO 
	* @param @param views    
	* @return void
	 */
	public void setBackgroudResource(Context context, View... viewArgs) {
		Random random = new Random();
		int index = random.nextInt(colors.length);
		for (View view : viewArgs) {
			setColorId(context, colors[index]);
			view.setBackgroundResource(colors[index]);
		}
	}

	private void setColorId(Context context, int colorId) {
		SharedPrefsUtil.putValue(context, "DynamicListView_textColor", colorId);
	}

	public int getColorId(Context context) {
		return SharedPrefsUtil.getValue(context, "DynamicListView_textColor", colors[0]);
	}

	/**
	* @Title: setTextColor 
	* @Description: TODO 
	* @param @param textView    
	* @return void
	 */
	public void setTextColor(Context context, TextView textView) {
		Random random = new Random();
		int index = random.nextInt(colors.length);
		int color = ResourceUtil.getInteger(context, colors[index]);
		textView.setTextColor(color);
	}

	public void getDynamicListViewResult(Context context, final Handler handler) {
		AppDownloadLogRequest request = new AppDownloadLogRequest();
		DataFetcher.getInstance().getAppDownloadLogResult(request, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				LogUtil.getLogger().d("response==>" + response.toString()); // 日志输出
				AppDownloadLogResponse appDownloadLogResponse = (AppDownloadLogResponse) JsonUtil.jsonToBean(response,
						AppDownloadLogResponse.class);
				if (appDownloadLogResponse != null && appDownloadLogResponse.dataList != null) {
					// 处理成功响应数据
					handler.sendMessage(handler.obtainMessage(onSuccess, appDownloadLogResponse));
				} else {
					// 木有资源
					handler.sendEmptyMessage(onFailure);
				}
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(onFailure);
			}
		}, true);
	}
}
