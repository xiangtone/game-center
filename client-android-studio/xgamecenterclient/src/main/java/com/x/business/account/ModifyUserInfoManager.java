package com.x.business.account;

import org.json.JSONObject;

import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.LoginResponse;
import com.x.publics.http.model.MasUser;
import com.x.publics.http.model.ModifyUserInfoResponse;
import com.x.publics.http.model.ModifyUserNickNameRequest;
import com.x.publics.http.model.ModifyUserPwdRequest;
import com.x.publics.http.model.ModifyUserNickNameRequest.ModifyNickNameUser;
import com.x.publics.http.model.ModifyUserPwdRequest.ModifyPwdUser;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.Tools;

import android.content.Context;
import android.os.Handler;

/**
 * 登陆接口管理
 
 *
 */
public class ModifyUserInfoManager {

	public static  ModifyUserInfoManager modifyUserInfoManager ;
	public Handler mHandler  ;
	public Context context ;
	public static final int HANDLER_WHAT_MODIFY_SUCCESSFUL = 200 ;
	public static final int HANDLER_WHAT_MODIFY_PWD_FAILURE_OLD_PWD_ERRROR = 204 ;
	public static final int HANDLER_WHAT_MODIFY_FAILURE_UNKNOW  = 404 ;
	public static final int HANDLER_WHAT_MODIFY_NICKNAME_FAILURE_NETWORK_ERROR = 50004 ;
	
	public ModifyUserInfoManager(Context context,Handler mHandler) {
		super();
		this.mHandler = mHandler;
		this.context = context;
	}

	/**
     * 构造子
     * @param context
     */
	public ModifyUserInfoManager() {
	}

	/**
     * 获取单例模式
     * @return
     */
    public static  ModifyUserInfoManager getInstence(Context context, Handler handler) {
        if(modifyUserInfoManager==null)
        {
        	modifyUserInfoManager = new ModifyUserInfoManager(context,handler) ;
        }
        return modifyUserInfoManager ;
    }
    
    /**
    * @Title: modifyPwd 
    * @Description: TODO 
    * @param @param userNewPwd    
    * @return void
     */
    public void modifyPwd(String userNewPwd,String userOldPwd)
    {   
    	ModifyUserPwdRequest modifyUserPwdRequest = new ModifyUserPwdRequest() ;
    	ModifyPwdUser data = new ModifyPwdUser() ;
    	MasUser masUser = UserInfoManager.getInstence(context).getAccInfo() ;
    	data.setUserName(masUser.getUserName()) ;
    	data.setUserPwd(Tools.MD5(userOldPwd)) ;
    	data.setUserPwdNew(Tools.MD5(userNewPwd)) ;
    	modifyUserPwdRequest.setData(data) ;
    	DataFetcher.getInstance().modifyUserPwd(modifyUserPwdRequest, modifyPwdResponseListent, modifyPwdErrorListener);
    }
    /**
     * 请求回响监听
     */
    public Listener<JSONObject> modifyPwdResponseListent = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			ModifyUserInfoResponse modifyUserInfoResponse = (ModifyUserInfoResponse) JsonUtil.jsonToBean(response, ModifyUserInfoResponse.class);
			if (modifyUserInfoResponse != null){
			if(modifyUserInfoResponse.state.code == 200) {
				UserInfoManager.getInstence(context).updateLocalAccInfo(modifyUserInfoResponse.masUser);	//更新本地用户信息
				mHandler.sendEmptyMessage(HANDLER_WHAT_MODIFY_SUCCESSFUL) ;
			}else if(modifyUserInfoResponse.state.code == 204) {
				mHandler.sendEmptyMessage(HANDLER_WHAT_MODIFY_PWD_FAILURE_OLD_PWD_ERRROR) ;
			}else {
				mHandler.sendEmptyMessage(HANDLER_WHAT_MODIFY_FAILURE_UNKNOW) ;
			}
			}else {
				mHandler.sendEmptyMessage(HANDLER_WHAT_MODIFY_FAILURE_UNKNOW) ;
			}
		}
    };
    /**
     * 
    * @Title: modifyNickName 
    * @Description: TODO 
    * @param @param nickName    
    * @return void
     */
    public void modifyNickName(String userName,String nickName)
    {   
    	ModifyUserNickNameRequest modifyUserNickNameRequest = new ModifyUserNickNameRequest() ;
    	ModifyNickNameUser data = new ModifyNickNameUser() ;
    	data.setUserName(userName) ;
    	data.setNickName(nickName) ;
    	modifyUserNickNameRequest.setData(data) ;
    	DataFetcher.getInstance().modifyUserNickName(modifyUserNickNameRequest, modifyNickNameResponseListent, modifyNickNameErrorListener);
    }
    /**
     * 请求回响监听
     */
    public Listener<JSONObject> modifyNickNameResponseListent = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			ModifyUserInfoResponse modifyUserInfoResponse = (ModifyUserInfoResponse) JsonUtil.jsonToBean(response, ModifyUserInfoResponse.class);
			if (modifyUserInfoResponse != null){
			if(modifyUserInfoResponse.state.code == 200) {
				updateLocalAccInfo(modifyUserInfoResponse.masUser) ;
				mHandler.sendEmptyMessage(HANDLER_WHAT_MODIFY_SUCCESSFUL) ;
			}else {
				mHandler.sendEmptyMessage(HANDLER_WHAT_MODIFY_FAILURE_UNKNOW) ;
			}
			}else {
				mHandler.sendEmptyMessage(HANDLER_WHAT_MODIFY_FAILURE_UNKNOW) ;
			}
		}
    };
    
    /**
     * 
    * @Title: updateLocalAccInfo 
    * @Description: TODO 
    * @param @param masuser    
    * @return void
     */
    public void updateLocalAccInfo(MasUser masuser)
    {
    	if(masuser!=null)
		{
			MasUser  masUser = UserInfoManager.getInstence(context).getAccInfo() ;
			if(masUser!=null )
			{
				masuser.setUserPwd(masUser.getUserPwd()) ;	
				UserInfoManager.getInstence(context).updateLocalAccInfo(masuser);	//更新本地用户信息
			}
			
		}
    }
    
    /**
     * 请求失败监听
     */
    public ErrorListener modifyNickNameErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			error.printStackTrace();
			mHandler.sendEmptyMessage(HANDLER_WHAT_MODIFY_NICKNAME_FAILURE_NETWORK_ERROR) ;
		}
	};
	
	 /**
     * 请求失败监听
     */
    public ErrorListener modifyPwdErrorListener = new ErrorListener() {

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
		modifyUserInfoManager = null ;
	}

}
