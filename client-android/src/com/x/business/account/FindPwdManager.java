package com.x.business.account;

import org.json.JSONObject;

import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.FindPwdRequest;
import com.x.publics.http.model.FindPwdResponse;
import com.x.publics.http.model.FindPwdRequest.UserInfo;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.utils.JsonUtil;

import android.content.Context;
import android.os.Handler;

/**
 * 登陆接口管理
 
 *
 */
public class FindPwdManager {

	public static  FindPwdManager findManager ;
	public Handler mHandler  ;
	public Context context ;
	public static final int HANDLER_WHAT_FIND_PWD_SUCCESSFUL = 200 ;
	public static final int HANDLER_WHAT_FIND_PWD_FAILURE_UNKNOW  = 404 ;
	public static final int HANDLER_WHAT_FIND_PWD_FAILURE_NOT_REGISTER  = 203 ;
	public static final int HANDLER_WHAT_FIND_PWD_FAILURE_EMAIL_ERROR  = 202 ;
	
	public FindPwdManager(Context context,Handler mHandler) {
		super();
		this.mHandler = mHandler;
		this.context = context;
	}

	/**
     * 构造子
     * @param context
     */
	public FindPwdManager() {
	}

	/**
     * 获取单例模式
     * @return
     */
    public static  FindPwdManager getInstence(Context context, Handler handler) {
        if(findManager==null)
        {
        	findManager = new FindPwdManager(context,handler) ;
        }
        return findManager ;
    }
    
    
   
    /**
     * 找回密码接口
     */
    public void findPwd( String userName)
    {   
    	FindPwdRequest findPwdRequest = new FindPwdRequest() ;
    	UserInfo userInfo = new UserInfo() ;
    	userInfo.setUserName(userName) ;
    	findPwdRequest.setData(userInfo) ;
    	
    	DataFetcher.getInstance().findPwd(findPwdRequest, myResponseListent, myErrorListener);
    }
    
    /**
     * 请求回响监听
     */
    public Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			FindPwdResponse findPwdResponse = (FindPwdResponse) JsonUtil.jsonToBean(response, FindPwdResponse.class);
			if (findPwdResponse != null) {
				if (findPwdResponse.state.code == 200) {
					mHandler.sendEmptyMessage(HANDLER_WHAT_FIND_PWD_SUCCESSFUL);
				} else if (findPwdResponse.state.code == 202) {
					mHandler.sendEmptyMessage(HANDLER_WHAT_FIND_PWD_FAILURE_EMAIL_ERROR);
				} else if (findPwdResponse.state.code == 203) {
					mHandler.sendEmptyMessage(HANDLER_WHAT_FIND_PWD_FAILURE_NOT_REGISTER);
				} else {
					mHandler.sendEmptyMessage(HANDLER_WHAT_FIND_PWD_FAILURE_UNKNOW);
				}
			} else {
				mHandler.sendEmptyMessage(HANDLER_WHAT_FIND_PWD_FAILURE_UNKNOW);
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
		findManager = null ;
	}

}
