/**   
* @Title: FeedbackManager.java
* @Package com.mas.amineappstore.business.feedback
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-8-18 上午10:39:45
* @version V1.0   
*/

package com.x.business.feedback;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.x.business.account.AccountManager;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.FeedbackDialogueBean;
import com.x.publics.http.model.FeedbackRequest;
import com.x.publics.http.model.FeedbackWarnRequest;
import com.x.publics.http.model.FeedbackWarnResponse;
import com.x.publics.http.model.MasUser;
import com.x.publics.http.model.FeedbackRequest.FeedbackData;
import com.x.publics.http.model.FeedbackRequest.MasPlay;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.home.SlidingPaneMenuFragment;

/**
* @ClassName: FeedbackManager
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-8-18 上午10:39:45
* 
*/

public class FeedbackManager {

	public static FeedbackManager feedbackManager;
	public String feedback_time = "feedback_time";
	public String feedback_content = "feedback_content";

	public static FeedbackManager getInstance() {
		if (feedbackManager == null)
			feedbackManager = new FeedbackManager();
		return feedbackManager;
	}

	public FeedbackManager() {

	}

	public void feedbackWarn(Context context, Listener<JSONObject> warnListent, ErrorListener warnErrorListener) {
		FeedbackWarnRequest request = new FeedbackWarnRequest();
		request.setImei(Utils.getIMEI(context));
		DataFetcher.getInstance().feedbackWarn(request, warnListent, warnErrorListener);
	}

	public void sendFeedBack(Context context, String content, Listener<JSONObject> feedbackResponseListent,
			ErrorListener feedbackErrorListener) {
		//请求
		FeedbackRequest request = new FeedbackRequest();
		FeedbackData data = new FeedbackData();
		data.setClientId(AccountManager.getInstance().getClientId(context));
		data.setContent(content);
		data.setEmail("");
		data.setDeviceModel(android.os.Build.MODEL);
		data.setDeviceVendor(android.os.Build.MANUFACTURER);
		data.setDeviceType(Utils.isTablet(context) == false ? 1 : 2);
		data.setOsVersion(String.valueOf(android.os.Build.VERSION.SDK_INT));
		data.setOsVersionName(android.os.Build.VERSION.RELEASE);
		data.setImei(Utils.getIMEI(context));

		MasUser masUser = new MasUser();
		masUser.setUserId(AccountManager.getInstance().getUserId(context));
		masUser.setUserName(AccountManager.getInstance().getUserName(context));
		masUser.setUserPwd(AccountManager.getInstance().getPwd(context));

		MasPlay masPlay = new MasPlay();
		masPlay.setMasPackageName(Utils.getPackageName(context));
		masPlay.setMasVersionCode(Utils.getVersionCode(context));
		masPlay.setMasVersionName(Utils.getVersionName(context));

		request.setData(data);
		request.setMasPlay(masPlay);
		request.setMasUser(masUser);

		DataFetcher.getInstance().feedbackData(request, feedbackResponseListent, feedbackErrorListener);
	}

	public void saveCommonFeedback(Context context, String commonFeedback) {
		SharedPrefsUtil.putValue(context, "commonFeedback", commonFeedback);
	}

	public String getCommonFeedback(Context context) {
		return SharedPrefsUtil.getValue(context, "commonFeedback", "");
	}

	public void setFeedbackCode(Context context, int feedbackCode) {
		SharedPrefsUtil.putValue(context, "feedbackCode", feedbackCode);
	}

	public int getFeedbackCode(Context context) {
		return SharedPrefsUtil.getValue(context, "feedbackCode", 0);
	}

	public void setFeedbackRead(Context context, boolean read) {
		SharedPrefsUtil.putValue(context, "feedbackRead", read);
	}

	public boolean isFeedbackRead(Context context) {
		return SharedPrefsUtil.getValue(context, "feedbackRead", false);
	}

	public void setFeedbackAttention(Context context, boolean attention) {
		SharedPrefsUtil.putValue(context, "attention", attention);
	}

	public boolean getFeedbackAttention(Context context) {
		return SharedPrefsUtil.getValue(context, "attention", false);
	}

	public void saveFeedBackFailList(Context context, List<FeedbackDialogueBean> list) {
		String contentJson = JsonUtil.objectToJson(list);
		SharedPrefsUtil.putValue(context, "fail_feedback", contentJson);
	}

	public String getFeedBackFailContent(Context context) {
		return SharedPrefsUtil.getValue(context, "fail_feedback", "");
	}

	public List<FeedbackDialogueBean> getFeedBackFailList(Context context) {
		List<FeedbackDialogueBean> result = new ArrayList<FeedbackDialogueBean>();
		try {
			String contentJson = SharedPrefsUtil.getValue(context, "fail_feedback", "");
			if (!TextUtils.isEmpty(contentJson)) {
				result = (List<FeedbackDialogueBean>) JsonUtil.jsonToList(contentJson,
						new com.google.gson.reflect.TypeToken<List<FeedbackDialogueBean>>() {
						}.getType());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
