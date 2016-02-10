/**   
* @Title: AccountManager.java
* @Package com.x.business.account
* @Description: TODO 

* @date 2014-3-17 下午04:49:39
* @version V1.0   
*/

package com.x.business.account;

import android.content.Context;

import com.x.publics.http.model.MasUser;
import com.x.publics.model.UserInfoBean;
import com.x.publics.utils.Tools;

/**
* @ClassName: AccountManager
* @Description: TODO 

* @date 2014-3-17 下午04:49:39
* 
*/

public class AccountManager {

	public static AccountManager accountManager;

	public static AccountManager getInstance() {
		if (accountManager == null)
			accountManager = new AccountManager();
		return accountManager;
	}

	public static final int PAGE_SIZE = 20;

	public UserInfoBean getUserInfo(Context context) {
//		String msg = XorPlus.encrypt(MasManage.getInstance().doPlatformGetExtraInfo(context));
//		if (msg == null)
//			return null;
//		UserInfoBean userInfoBean = (UserInfoBean) JsonUtil.jsonToBean(msg, UserInfoBean.class);
//		return userInfoBean;
		
			MasUser masUser  = UserInfoManager.getInstence(context).getAccInfo() ;
			if(masUser!=null )
			{
				UserInfoBean userInfoBean = new UserInfoBean() ;
				userInfoBean.setClientId(Tools.getSaveData(context.getApplicationContext(), UserInfoManager.MAS_SHARED_PREF_DB_NAME, UserInfoManager.umasclientIdKey, 0)) ;
				userInfoBean.setLogin(UserInfoManager.getInstence(context).isLogin()) ;
				userInfoBean.setUserId(masUser.getUserId()) ;
				userInfoBean.setUserName(masUser.getUserName()) ;
				userInfoBean.setUserPwd(masUser.getUserPwd()) ;
				userInfoBean.setNickName(masUser.getNickName());
				return userInfoBean ;
			}
			return null ;
	}

	public boolean isLogin(Context context) {
		return UserInfoManager.getInstence(context).isLogin() ;
	}

	public int getClientId(Context context) {
		return Tools.getSaveData(context.getApplicationContext(), UserInfoManager.MAS_SHARED_PREF_DB_NAME, UserInfoManager.umasclientIdKey, 0) ;
	}

	public int getUserId(Context context) {
		UserInfoBean userInfoBean = getUserInfo(context);
		if (userInfoBean == null)
			return 0;
		return userInfoBean.getUserId();
	}

	public String getUserName(Context context) {
		UserInfoBean userInfoBean = getUserInfo(context);
		if (userInfoBean == null)
			return "";
		return userInfoBean.getUserName() == null ? "" : userInfoBean.getUserName();
	}

	public String getPwd(Context context) {
		UserInfoBean userInfoBean = getUserInfo(context);
		if (userInfoBean == null)
			return "";
		return userInfoBean.getUserPwd() == null ? "" : userInfoBean.getUserPwd();

	}
	
	public String getNickName(Context context) {
		UserInfoBean userInfoBean = getUserInfo(context);
		if (userInfoBean == null)
			return "";
		return userInfoBean.getNickName() == null ? "" : userInfoBean.getNickName();

	}

//	public void init(Context context, MasCallBack callBack) {
//		MasManage.setDebug(Host.isMasSdkDebug());
//		MasManage.getInstance().doAct(context, false, callBack);
//	}

//	public void login(Context context, MasCallBack callBack) {
//		MasManage.getInstance().doPlatformAccount(context, callBack);
//	}
//
//	public void logout(Context context, MasCallBack callBack) {
//		MasManage.getInstance().doPlatformLogout(context, callBack);
//	}
//
//	public void recharge(Activity context, MasCallBack callBack) {
//		MasManage.getInstance().doPlatformRecharge(context, callBack);
//	}
//
//	public void getRechargeHistory(Context context, int page, MasCallBack callBack) {
//		MasManage.getInstance().doPlatformGetRechargeInfo(context, page, PAGE_SIZE, callBack);
//	}
//	
//	public int getSDKDomain(){
//		return MasManage.getInstance().doPlatformGetSDKDomain() ;
//	}

}
