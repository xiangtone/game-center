package com.x.business.account;
import org.json.JSONObject;

import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.RegisterRequest;
import com.x.publics.http.model.RegisterResponse;
import com.x.publics.http.model.SearchResponse;
import com.x.publics.http.model.RegisterRequest.RegisterData;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.utils.JsonUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * 登陆接口管理
 
 *
 */
public class RegisterManager {

	public static  RegisterManager registerManager ;
	public Handler mHandler  ;
	public Context context ;
	public static final int HANDLER_WHAT_REGISTER_SUCCESSFUL = 200 ;
	public static final int HANDLER_WHAT_REGISTER_ALREADY_REGISTERED = 201 ;
	public static final int HANDLER_WHAT_REGISTER_INVALID_EMAIL= 202 ;
	public static final int HANDLER_WHAT_REGISTER_FAILURE_UNKNOW  = 404 ;
	
    /**
	* <p>Title: </p>
	* <p>Description: </p>
	* @param handler
	* @param context
	*/
	
	
	public RegisterManager(Context context,Handler mHandler) {
		super();
		this.mHandler = mHandler;
		this.context = context;
	}

	/**
     * 构造子
     * @param context
     */
	public RegisterManager() {
	}

	/**
     * 获取单例模式
     * @return
     */
    public static  RegisterManager getInstence(Context context, Handler handler) {
        if(registerManager==null)
        {
        	registerManager = new RegisterManager(context,handler) ;
        }
        return registerManager ;
    }
    
    
   
    /**
     * 注册接口
     */
    public void register( String userName,  String nickName)
    {   
    	RegisterRequest registerRequest = new RegisterRequest() ;
    	RegisterData registerData = new RegisterData() ;
    	registerData.setUserName(userName) ;
    	registerData.setNickName(nickName) ;
    	registerRequest.setData(registerData) ;
    	
    	DataFetcher.getInstance().register(registerRequest, myResponseListent, myErrorListener);
    }
    
    /**
     * 请求回响监听
     */
    public Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			RegisterResponse registerResponse = (RegisterResponse) JsonUtil.jsonToBean(response, RegisterResponse.class);
			if (registerResponse != null){
				
			if(registerResponse.state.code == 200) {
				mHandler.sendEmptyMessage(HANDLER_WHAT_REGISTER_SUCCESSFUL) ;
				
			}else if(registerResponse.state.code == 201)
			{
				mHandler.sendEmptyMessage(HANDLER_WHAT_REGISTER_ALREADY_REGISTERED) ;
			}else if(registerResponse.state.code == 202)
			{
				mHandler.sendEmptyMessage(HANDLER_WHAT_REGISTER_INVALID_EMAIL) ;
			}else {
				mHandler.sendEmptyMessage(HANDLER_WHAT_REGISTER_FAILURE_UNKNOW) ;
			}
			}else {
				mHandler.sendEmptyMessage(HANDLER_WHAT_REGISTER_FAILURE_UNKNOW) ;
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
		registerManager = null ;
	}

}
