package com.x.business.account;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.x.business.country.CountryManager;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.MasUser;
import com.x.publics.http.model.PlatFormInitRequest;
import com.x.publics.http.model.PlatFormInitResponse;
import com.x.publics.http.model.PlatFormInitRequest.ClientActive;
import com.x.publics.http.model.PlatFormInitRequest.ClientInfo;
import com.x.publics.http.model.PlatFormInitRequest.PlatFormData;
import com.x.publics.http.model.PlatFormInitRequest.PlatFormInitMasUser;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.SystemInfo;
import com.x.publics.utils.Tools;

/**
 * 登陆接口管理
 
 *
 */
public class InitPlatformManager {

	public static InitPlatformManager initPlatformManager;
	public Context context;
	public static final int HANDLER_WHAT_INIT_FINISHED = 200;
	public Handler handler;

	/**
	 * 构造子
	 * @param context
	 */
	public InitPlatformManager() {
	}

	public InitPlatformManager(Context context) {
		super();
		this.context = context;
	}

	//	/**
	//     * 获取单例模式
	//     * @return
	//     */
	//    public static  InitPlatformManager getInstence(Context contex) {
	//        if(initPlatformManager==null)
	//        {
	//        	initPlatformManager = new InitPlatformManager(contex) ;
	//        }
	//        return initPlatformManager ;
	//    }

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	/**
	* <p>Title: </p>
	* <p>Description: </p>
	* @param context
	* @param handler
	*/

	public InitPlatformManager(Context context, Handler handler) {
		super();
		this.context = context;
		this.handler = handler;
	}

	public void initPlatForm() {
		PlatFormInitRequest initRequest = new PlatFormInitRequest();

		/********************PlatFormData*************************/
		PlatFormData platFormData = new PlatFormData();
		if (CountryManager.getInstance().isAutoCountry(context)) {
			platFormData.setRaveId(0);
		}
		platFormData.setApkKey(SystemInfo.getMetaDataStringValue(context, "apkKey"));
		platFormData.setAppId(SystemInfo.getMetaDataIntValue(context, "appId"));
		platFormData.setAppPackageName(SystemInfo.getPackageName(context));
		platFormData.setAppVersionCode(SystemInfo.getVersionCode(context));
		platFormData.setAppVersionName(SystemInfo.getVersionName(context));
		platFormData.setChannelId(SystemInfo.getMetaDataIntValue(context, "channelId"));
		platFormData.setCpId(SystemInfo.getMetaDataIntValue(context, "cpId"));
		platFormData.setServerId(SystemInfo.getMetaDataIntValue(context, "serverId"));

		/********************ClientActive*************************/
		ClientActive clientActive = new ClientActive();
		clientActive.setCellId(SystemInfo.getCellId(context));
		clientActive.setLac(SystemInfo.getLac(context));
		clientActive.setMcc(SystemInfo.getMcc(context));
		clientActive.setMnc(SystemInfo.getMnc(context));
		clientActive.setLatitude(SystemInfo.getLatitude(context));
		clientActive.setLongitude(SystemInfo.getLongitude(context));

		/********************ClientInfo*************************/
		ClientInfo clientInfo = new ClientInfo();
		clientInfo.setImei(SystemInfo.getIMEI(context));
		clientInfo.setImsi(SystemInfo.getIMSI(context));
		clientInfo.setMac(SystemInfo.getMAC(context));
		clientInfo.setNetType(SystemInfo.getNetworkInfo(context));
		clientInfo.setOsAddtional(null);
		clientInfo.setOsVersion(SystemInfo.getAndroidSDKINT());
		clientInfo.setOsVersionName(SystemInfo.getAndroidRelease());
		clientInfo.setPhone(SystemInfo.getPhoneNumber(context));
		clientInfo.setDeviceModel(SystemInfo.getDeviceModel());
		clientInfo.setDeviceVendor(SystemInfo.getDeviceManufacturer());
		clientInfo.setScreenDensity(SystemInfo.getDensity(context));
		clientInfo.setScreenHeight(SystemInfo.getScreenHeight(context));
		clientInfo.setScreenWidth(SystemInfo.getScreenWidth(context));
		clientInfo.setServiceSupplier(SystemInfo.getCardType(context));
		clientInfo.setSmsc(null);
		clientInfo.setDeviceType(SystemInfo.isPad(context));

		/********************MasUser*************************/
		MasUser masUser = UserInfoManager.getInstence(this.context).getAccInfo();
		//优先获取存储空间的信息
		if (masUser != null) {
			PlatFormInitMasUser platFormInitMasUser = new PlatFormInitMasUser();
			platFormInitMasUser.setUserId(masUser.getUserId());
			platFormInitMasUser.setUserName(masUser.getUserName());
			platFormInitMasUser.setUserPwd(masUser.getUserPwd());
			initRequest.setMasUser(platFormInitMasUser);
		}
		/********************InitRequest*************************/
		initRequest.setClientActive(clientActive);
		initRequest.setClientInfo(clientInfo);

		initRequest.setData(platFormData);
		DataFetcher.getInstance().initPlatform(initRequest, myInitResponseListent, myInitErrorListener);
	}

	private Listener<JSONObject> myInitResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			LogUtil.getLogger().d("response==>" + response.toString());
			PlatFormInitResponse platFormInitResponse = (PlatFormInitResponse) JsonUtil.jsonToBean(response,
					PlatFormInitResponse.class);
			int countryId = 1;
			if (platFormInitResponse != null && platFormInitResponse.state.code == 200) {
				countryId = platFormInitResponse.autoRaveId;
				if (platFormInitResponse.islogin) {
					UserInfoManager.getInstence(context).login(platFormInitResponse.masUser);
				} else {
					UserInfoManager.getInstence(context).loginFailure();//自动登陆失败
				}
				Tools.saveData(context.getApplicationContext(), UserInfoManager.MAS_SHARED_PREF_DB_NAME,
						UserInfoManager.umasclientIdKey, platFormInitResponse.clientId);
			} else {
				UserInfoManager.getInstence(context).loginFailure();//自动登陆失败
			}
			if (CountryManager.getInstance().isAutoCountry(context)) {
				CountryManager.getInstance().saveCountryId(context, countryId, true);
			}
			handler.sendEmptyMessage(HANDLER_WHAT_INIT_FINISHED);
		}
	};

	private ErrorListener myInitErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			if (CountryManager.getInstance().isAutoCountry(context)) {
				CountryManager.getInstance().saveCountryId(context, 1, true);
			}
			UserInfoManager.getInstence(context).loginFailure();//自动登陆失败
			handler.sendEmptyMessage(HANDLER_WHAT_INIT_FINISHED);
			error.printStackTrace();
		}
	};

	/**
	 * 资源卸载
	 */
	public void unInit() {
		initPlatformManager = null;
	}

}
