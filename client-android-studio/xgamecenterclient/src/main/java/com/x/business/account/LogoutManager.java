package com.x.business.account;

import org.json.JSONObject;

import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.LoginResponse;
import com.x.publics.http.model.LogoutRequest;
import com.x.publics.http.model.MasUser;
import com.x.publics.http.model.LogoutRequest.LogoutData;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.SystemInfo;
import com.x.publics.utils.Tools;

import android.content.Context;
import android.os.Handler;

/**
 * 登陆接口管理
 
 *
 */
public class LogoutManager {

	public static  LogoutManager logoutManager ;
	public Handler mHandler  ;
	public Context context ;
	public static final int HANDLER_WHAT_LOGOUT_SUCCESSFUL = 2000 ;
	public static final int HANDLER_WHAT_LOGOUT_FAILURE_UNKNOW  = 4004 ;
	
	public LogoutManager(Context context,Handler mHandler) {
		super();
		this.mHandler = mHandler;
		this.context = context;
	}

	/**
     * 构造子
     * @param context
     */
	public LogoutManager() {
	}

	/**
     * 获取单例模式
     * @return
     */
    public static  LogoutManager getInstence(Context context, Handler handler) {
        if(logoutManager==null)
        {
        	logoutManager = new LogoutManager(context,handler) ;
        }
        return logoutManager ;
    }
    
    
   
    /**
     * 登陆接口
     */
    public void logout()
    {   
    	LogoutRequest logoutRequest = new LogoutRequest() ;
    	LogoutData logoutData = new LogoutData() ;
    	
    	MasUser masUser = UserInfoManager.getInstence(context).getAccInfo() ;
    	
    	logoutData.setUserName(masUser.getUserName()) ;
    	logoutData.setNickName(masUser.getNickName()) ;
    	logoutData.setUserId(masUser.getUserId()) ;
    	
    	logoutData.setApkKey(SystemInfo.getMetaDataStringValue(context, "apkKey")) ;
    	logoutData.setAppId(SystemInfo.getMetaDataIntValue(context, "appId")) ;
    	logoutData.setChannelId(SystemInfo.getMetaDataIntValue(context, "channelId")) ;
    	logoutData.setCpId(SystemInfo.getMetaDataIntValue(context, "cpId")) ;
    	logoutData.setServerId(SystemInfo.getMetaDataIntValue(context, "serverId")) ;
    	logoutData.setClientId(Tools.getSaveData(context.getApplicationContext(), UserInfoManager.MAS_SHARED_PREF_DB_NAME, UserInfoManager.umasclientIdKey, 0)) ;
		
    	logoutRequest.setData(logoutData) ;
    	
    	DataFetcher.getInstance().logout(logoutRequest, myResponseListent, myErrorListener);
    }
    
    /**
     * 请求回响监听
     */
    public Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			LoginResponse loginResponse = (LoginResponse) JsonUtil.jsonToBean(response, LoginResponse.class);
			if (loginResponse != null){
			if(loginResponse.state.code == 200) {
				UserInfoManager.getInstence(context).logout() ;	
				mHandler.sendEmptyMessage(HANDLER_WHAT_LOGOUT_SUCCESSFUL) ;
			}else {
				mHandler.sendEmptyMessage(HANDLER_WHAT_LOGOUT_FAILURE_UNKNOW) ;
			}
			}else {
				mHandler.sendEmptyMessage(HANDLER_WHAT_LOGOUT_FAILURE_UNKNOW) ;
			}
		}
    };
    
    /**
     * 请求失败监听
     */
    public ErrorListener myErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			error.printStackTrace();
			mHandler.sendEmptyMessage(AccountHelper.HANDLER_WHAT_NETWORK_ERROR) ;
		}
	};
    
   /**
	 * 资源卸载
	 */
	public void unInit() {
		logoutManager = null ;
	}

}
