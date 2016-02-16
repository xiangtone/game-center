package com.x.business.account;

import java.io.File;

import com.x.publics.http.model.MasUser;
import com.x.publics.utils.SystemInfo;
import com.x.publics.utils.Tools;

import android.content.Context;
import android.text.TextUtils;

/**
 * 账号管理
 
 *
 */
public class UserInfoManager {

	public Context context ;
	public static final String folderName = "systemConfig"+File.separator+"system"+File.separator+"config" ;
	public static final String fileName = "systemConfig.bin" ;
	public static final String pmasdKey = "pmasd" ;
	public static final String umasnKey = "umasn" ;
	public static final String umasnUserIdKey = "userId" ;
	public static final String umasNickNameKey = "nickName" ;
	public static final String umasclientIdKey = "clientId" ;
	
	public static final String uLoginKey = "uLogin" ;
	public static final String uLoginTimeKey = "uLoginTime" ;
	public static final String uLogoutKey = "uLogout" ;
	public static final String uLogoutTimeKey = "uLogoutTime" ;
	
	
	private static UserInfoManager userInfoManager ;
	public static final String SPLIT_FLAG ="," ;
	public static final String SPLIT_NEW_FLAG ="zapp_com_mas_amineappstore" ;
	private String accInfo =null ;
	
	public static final String MAS_SHARED_PREF_DB_NAME = "masUser" ;
	
	
	public UserInfoManager(Context context) {
		this.context = context;
		updateSDCardAccount() ;
	}
	
	/**
     * 获取单例模式
     * @return
     */
    public static  UserInfoManager getInstence(Context context) {
        if(userInfoManager==null)
        {
        	userInfoManager = new UserInfoManager(context) ;
        }
        return userInfoManager ;
    }
    
    /**
     * 获取登录状态
     * @return
     */
    public boolean isLogin()
    {
    	boolean isLogin = Tools.getSaveData(context.getApplicationContext(), MAS_SHARED_PREF_DB_NAME, uLoginKey, false) ;
    	return isLogin ;
    }
    
    /**
     * 登录
     */
    public void login(MasUser masUser)
    {
    	if(masUser!=null)
    	{
    	Tools.saveData(context.getApplicationContext(),  MAS_SHARED_PREF_DB_NAME, uLoginKey, true);
    	Tools.saveData(context.getApplicationContext(),  MAS_SHARED_PREF_DB_NAME, uLoginTimeKey, System.currentTimeMillis());
    	updateLocalAccInfo(masUser) ;
    	}
    } 
    
    /**
     * 自动登录失败
     */
    public void loginFailure()
    {
    	Tools.saveData(context.getApplicationContext(),  MAS_SHARED_PREF_DB_NAME, uLoginKey, false);
    } 
    /**
     * 
    * @Title: updateAccInfo 
    * @Description: TODO 
    * @param @param masUser    
    * @return void
     */
    public void updateLocalAccInfo(MasUser masUser)
    {
    	if(masUser!=null)
    	{
    	Tools.saveData(context.getApplicationContext(),  MAS_SHARED_PREF_DB_NAME, umasnKey, masUser.getUserName());
    	Tools.saveData(context.getApplicationContext(),  MAS_SHARED_PREF_DB_NAME, pmasdKey, masUser.getUserPwd());
    	Tools.saveData(context.getApplicationContext(),  MAS_SHARED_PREF_DB_NAME, umasnUserIdKey, masUser.getUserId());
    	Tools.saveData(context.getApplicationContext(),  MAS_SHARED_PREF_DB_NAME, umasNickNameKey, masUser.getNickName());
    	//保存用户信息到存储空间
    	saveAccInfo(masUser) ;
    	}
    }
    
    /**
     * 注销
     */
    public void logout()
    {
    	Tools.saveData(context.getApplicationContext(), MAS_SHARED_PREF_DB_NAME, uLoginKey, false);
    	Tools.saveData(context.getApplicationContext(), MAS_SHARED_PREF_DB_NAME, uLogoutTimeKey, System.currentTimeMillis());
    	accInfo = null ;
    	
    	MasUser masUser = getAccInfo() ;
    	masUser.setUserPwd("+=_-~`") ;
    	updateLocalAccInfo(masUser) ;
    }
    
    /**
     * 获取Umasn
     * @return
     */
    public MasUser getAccInfo()
    {
    	MasUser masUser = null ;
    	try {
    		String accInfo = SystemInfo.decryptData(readAccInfo()) ;
        	if(!TextUtils.isEmpty(accInfo))
        	{
        		String umasn = getValue(accInfo,umasnKey) ; //获取umasn
            	String pmasd = getValue(accInfo,pmasdKey) ; //获取pmasd
            	String nickName = getValue(accInfo,umasNickNameKey) ; //获取pmasd
            	int umasUserid = Integer.valueOf(getValue(accInfo,umasnUserIdKey)==null ?"0" : getValue(accInfo,umasnUserIdKey)) ; //获取umasUserid
            	if(!TextUtils.isEmpty(umasn) && umasUserid!=0)
            	{
            		masUser = new MasUser() ;
            		masUser.setUserName(umasn) ;
            		masUser.setUserPwd(pmasd) ;
            		masUser.setUserId(umasUserid) ;
            		masUser.setNickName(nickName) ;
            		
                	
            	}
        	}else
        	{
        		String umasn = Tools.getSaveData(context.getApplicationContext(),  MAS_SHARED_PREF_DB_NAME,  umasnKey, "");  //获取umasn
            	String pmasd = Tools.getSaveData(context.getApplicationContext(),  MAS_SHARED_PREF_DB_NAME,  pmasdKey, ""); //获取pmasd
            	String nickName = Tools.getSaveData(context.getApplicationContext(),  MAS_SHARED_PREF_DB_NAME,  umasNickNameKey, ""); //获取nickname
            	int umasUserid = Tools.getSaveData(context.getApplicationContext(),  MAS_SHARED_PREF_DB_NAME,  umasnUserIdKey, 0) ;//获取umasUserid
            	if(!TextUtils.isEmpty(umasn) && !TextUtils.isEmpty(pmasd) && umasUserid!=0)
            	{
            		masUser = new MasUser() ;
            		masUser.setUserName(umasn) ;
            		masUser.setUserPwd(pmasd) ;
            		masUser.setUserId(umasUserid) ;
            		masUser.setNickName(nickName) ;
            	}
        	}
        	return masUser ;
		} catch (Exception e) 
		{
			e.printStackTrace() ;
		}
    	return masUser ;
    }
    
    /**
     * 保存用户信息
     * @param info
     */
    public void saveAccInfo(MasUser masUser)
    {
    	String infoData = SystemInfo.encryptData(masUser.getUserName()+SPLIT_NEW_FLAG+masUser.getUserPwd()+SPLIT_NEW_FLAG+masUser.getUserId()+SPLIT_NEW_FLAG+masUser.getNickName()) ;
    	Tools.saveFroEnStorage(context.getApplicationContext(), folderName, fileName,infoData ) ;
    	accInfo = null ;
    }
    
    /**
     * 读取信息
     * @return
     */
    private String  readAccInfo()
    {
    	accInfo = Tools.readFromEnStorage(context, folderName, fileName) ;
    	return accInfo  ;
    }
    
    /**
     * 获取匹配字段
     * @param info
     * @param key
     * @return
     */
    private String getValue(String info,String key)
    {
    	try {
    		if(!TextUtils.isEmpty(info))
        	{
        		if(info.contains(SPLIT_NEW_FLAG))
        		{
        			 String[] infos = info.split(SPLIT_NEW_FLAG) ;
        			if(umasnKey.equals(key))
        			{
        				return infos[0] ;
        			}else if(pmasdKey.equals(key))
        			{
        				return infos[1] ;
        			}else if(umasnUserIdKey.equals(key))
        			{
        				return infos[2] ;
        			}else if(umasNickNameKey.equals(key))
        			{
        				return infos[3] ;
        			}
        		}
        	}
    		return null ;
		} catch (Exception e) {
			e.printStackTrace() ;
		}
    	
    	return null ;
    }
    
    
    /**
     * 升级账号
    * @Title: updateSDCardAccount 
    * @Description: TODO 
    * @param     
    * @return void
     */
    public  void updateSDCardAccount() {
    	String ai = SystemInfo.decryptData(readAccInfo()) ;
     	if(!TextUtils.isEmpty(ai)){
    		String[] info = ai.split(SPLIT_FLAG) ;
    		if(info != null && info.length == 4)
    		{
    			ai = ai.replaceAll(SPLIT_FLAG, SPLIT_NEW_FLAG) ;
    			String infoData = SystemInfo.encryptData(ai) ;
    			Tools.saveFroEnStorage(context.getApplicationContext(), folderName, fileName,infoData) ;
    		}
    	}
    }
}
