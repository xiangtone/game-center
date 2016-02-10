/**   
* @Title: AccountHelper.java
* @Package com.x.business.account
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-8-28 上午9:58:33
* @version V1.0   
*/


package com.x.business.account;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.x.business.statistic.DataEyeHelper;
import com.x.publics.utils.SharedPrefsUtil;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.EditText;

/**
* @ClassName: AccountHelper
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-8-28 上午9:58:33
* 
*/

public class AccountHelper {

	public static String[] defualNickName = {"Dancinggirl","Promiseme","love","BrokenDreams","loveme"} ;
	public static final int HANDLER_WHAT_NETWORK_ERROR = 5000 ;
	public static final int HANDLER_WHAT_HORBIDDEN_ACTION = 100000 ;
	public static final int HANDLER_WHAT_ENABLE_ACTION = 100001 ;
	public static final int SKIP_CASE_LOGIN_FIND_PWD = 1 ; 
	public static final int SKIP_CASE_REGISTER_SUCCESS = 2 ; 
	public static final int SKIP_CASE_ACCOUNT_FIND_PWD = 3 ; 
	public static final int HORBIDDEN_MAX_TIMES = 5*60 ; 
	public static final int HORBIDDEN_MAX_NEG_TIMES = -5*60 ; 
	private static List<Activity> actList = new LinkedList<Activity>();
	private static final String HANDLER_WHAT_HORBIDDEN_STATUS_TIMES_KEY = "accountHorbiddenKey" ;
	
	
	public AccountHelper() {
		super();
	}

	/**
	 * 处理禁用逻辑
	* @Title: doHorbiddenHandler 
	* @Description: TODO 
	* @param @param context
	* @param @param mHandler    
	* @return void
	 */
	public  void doHorbiddenHandler(Context mContext,Handler handler,int currentRemainTime)
	{
		long hTimes ;
		long lHTimes ;
		long checkTimes ;
		long saveTiems ;
		Handler mHandler = handler ;
		if(mContext== null || mHandler==null)
		{
			return ;
		}
		lHTimes =  SharedPrefsUtil.getValue(mContext, HANDLER_WHAT_HORBIDDEN_STATUS_TIMES_KEY, -1L);
		saveTiems = System.currentTimeMillis() ;
		if(currentRemainTime!=-1)
		{
			hTimes = currentRemainTime ;
		}else 
		{
			hTimes = HORBIDDEN_MAX_TIMES -  (saveTiems - lHTimes)/1000L ;
		}
		checkTimes = HORBIDDEN_MAX_TIMES -  (saveTiems - lHTimes)/1000L;

			if(checkTimes >= HORBIDDEN_MAX_TIMES || checkTimes <= HORBIDDEN_MAX_NEG_TIMES){
				mHandler.sendEmptyMessage(HANDLER_WHAT_ENABLE_ACTION) ;
				SharedPrefsUtil.removeValue(mContext, HANDLER_WHAT_HORBIDDEN_STATUS_TIMES_KEY);
				return ;
			}
		
		if(currentRemainTime !=0 && hTimes<=HORBIDDEN_MAX_TIMES && hTimes > 0 )
		{
			int time = Integer.parseInt(String.valueOf(hTimes)) ;
			Message msg = mHandler.obtainMessage(HANDLER_WHAT_HORBIDDEN_ACTION, time -1 ) ;
			SharedPrefsUtil.putValue(mContext, HANDLER_WHAT_HORBIDDEN_STATUS_TIMES_KEY,saveTiems -(HORBIDDEN_MAX_TIMES-hTimes-1)*1000L);
			if(currentRemainTime ==-1 )
			{
				mHandler.sendMessage(msg) ;
			}else{
				mHandler.sendMessageDelayed(msg, 1000L) ;	
			}
			
		}else
		{
			SharedPrefsUtil.removeValue(mContext, HANDLER_WHAT_HORBIDDEN_STATUS_TIMES_KEY);
			mHandler.sendEmptyMessage(HANDLER_WHAT_ENABLE_ACTION) ;
		}
			
			
		}
	
	/**
	 * 启用禁用
	* @Title: launchHorbiddenHandler 
	* @Description: TODO 
	* @param @param context
	* @param @param mHandler    
	* @return void
	 */
	public  void launchHorbiddenHandler(Context context,Handler mHandler)
	{
		SharedPrefsUtil.putValue(context, HANDLER_WHAT_HORBIDDEN_STATUS_TIMES_KEY,System.currentTimeMillis());
	}
	
	/**
	 * 获取默认用户名
	* @Title: getDefualUserName 
	* @Description: TODO 
	* @param @return    
	* @return String
	 */
	public static String getDefualUserName(){
		
		Random random = new Random(System.currentTimeMillis());
		int defualNickNameIndex = random.nextInt(defualNickName.length);
		
		return defualNickName[defualNickNameIndex] ;
	}
		
	
	/**
	 * use Account Manager to get Google Account
	 * 
	 * @param activity
	 */
	public static String getSystemEmailAccount(Activity activity) {
			String accountName = "" ;
			android.accounts.AccountManager  accountManager =  android.accounts.AccountManager.get(activity); 
			Account[] accounts = accountManager.getAccounts();
			if(accounts!=null)
			{
				for (int i = 0; i < accounts.length; i++) {
					if(isEmail(accounts[i].name))
					{
						if(accounts[i].name.contains("@gmail.com"))
						{
							return accounts[i].name ;
						}
						if(TextUtils.isEmpty(accountName))
						{
							accountName = accounts[i].name  ;
						}
					}
				}
			}
			return accountName ;
	}

	/**
	 * 检测是否有效邮箱
	* @Title: isEmail 
	* @Description: TODO 
	* @param @param email
	* @param @return    
	* @return boolean
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
		}
	
	/**
	 * 
	* @Title: getWarnStyle 
	* @Description: TODO 
	* @param @param warnStr
	* @param @return    
	* @return String
	 */
	public static SpannableStringBuilder getAccountWarnString(String warnStr)
	{
		if(TextUtils.isEmpty(warnStr))
		{
			warnStr = "" ;
		}
		
		SpannableStringBuilder builder = new SpannableStringBuilder(warnStr) ;
		try {
				builder.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
		} catch (Exception e) {
			e.printStackTrace() ;
		}
		
		return builder;
	}
	
	
	public static void addAct(Activity activity) {
		actList.add(activity);
	}

	public static void finishAct() {
		for (int i = 0; i < actList.size(); i++) {
			actList.get(i).finish();
		}
	}
	
	
	 public static void editEndFocus(EditText editText)
	    {
	    	if(editText!=null)
	    	{
	        	CharSequence text = editText.getText();
	        	 if (text instanceof Spannable) {
	        	     Spannable spanText = (Spannable)text;
	        	     Selection.setSelection(spanText, text.length());
	        	 }
	    	}
	    
	    }
}
