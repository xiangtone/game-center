package com.x.business.account;

import org.json.JSONObject;

import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.LoginRequest;
import com.x.publics.http.model.LoginResponse;
import com.x.publics.http.model.LoginRequest.LoginData;
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
public class LoginManager {

	public static  LoginManager loginManager ;
	public Handler mHandler  ;
	public Context context ;
	public static final int HANDLER_WHAT_LOGIN_SUCCESSFUL = 200 ;
	public static final int HANDLER_WHAT_LOGIN_FAILURE_UNKNOW  = 404 ;
	public static final int HANDLER_WHAT_LOGIN_FAILURE_UNPWD_ERROR  = 205 ;
	
	public LoginManager(Context context,Handler mHandler) {
		super();
		this.mHandler = mHandler;
		this.context = context;
	}
	public LoginManager(Context context) {
		super();
		this.context = context;
	}
	/**
     * 构造子
     * @param context
     */
	public LoginManager() {
	}

	/**
     * 获取单例模式
     * @return
     */
    public static  LoginManager getInstence(Context context, Handler handler) {
        if(loginManager==null)
        {
        	loginManager = new LoginManager(context,handler) ;
        }
        return loginManager ;
    }
    
    
   
    /**
     * 登陆接口
     */
    public void login( String userName,  String pwd)
    {   
    	LoginRequest loginRequest = new LoginRequest() ;
    	LoginData loginData = new LoginData() ;
    	loginData.setUserName(userName) ;
    	loginData.setUserPwd(Tools.MD5(pwd)) ;
    	
    	loginData.setApkKey(SystemInfo.getMetaDataStringValue(context, "apkKey")) ;
    	loginData.setAppId(SystemInfo.getMetaDataIntValue(context, "appId")) ;
    	loginData.setChannelId(SystemInfo.getMetaDataIntValue(context, "channelId")) ;
    	loginData.setCpId(SystemInfo.getMetaDataIntValue(context, "cpId")) ;
    	loginData.setServerId(SystemInfo.getMetaDataIntValue(context, "serverId")) ;
    	loginData.setClientId(Tools.getSaveData(context.getApplicationContext(), UserInfoManager.MAS_SHARED_PREF_DB_NAME, UserInfoManager.umasclientIdKey, 0)) ;
		
    	loginRequest.setData(loginData) ;
    	
    	DataFetcher.getInstance().login(loginRequest, myResponseListent, myErrorListener);
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
				if(loginResponse.islogin)
				{
					UserInfoManager.getInstence(context).login(loginResponse.masUser) ;	
				}
				mHandler.sendEmptyMessage(HANDLER_WHAT_LOGIN_SUCCESSFUL) ;
			}else if(loginResponse.state.code == 205){
				mHandler.sendEmptyMessage(HANDLER_WHAT_LOGIN_FAILURE_UNPWD_ERROR) ;
			}else {
				mHandler.sendEmptyMessage(HANDLER_WHAT_LOGIN_FAILURE_UNKNOW) ;
			}
			}else {
				mHandler.sendEmptyMessage(HANDLER_WHAT_LOGIN_FAILURE_UNKNOW) ;
			}
		}
    };
    
    /**
     * 请求失败监听
     */
    public ErrorListener myErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			mHandler.sendEmptyMessage(AccountHelper.HANDLER_WHAT_NETWORK_ERROR) ;
			error.printStackTrace();
		}
	};
    
   /**
	 * 资源卸载
	 */
	public void unInit() {
		loginManager = null ;
	}

}
