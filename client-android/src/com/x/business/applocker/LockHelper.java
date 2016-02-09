
package com.x.business.applocker;

import java.util.ArrayList;
import java.util.List;

import com.x.R;
import com.x.publics.model.AppLockerBean;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

/**
 * 
* @ClassName: LockHelper
* @Description: TODO(应用锁辅助页靿

* @date 2014-10-21 上午9:34:06
*
 */
public class LockHelper {
	private Context context;
	public static LockHelper lockManager;
	//RecommendApp.TASK_MANAGER,
	String[] recommendApp = {RecommendApp.INCOMING_CALL,RecommendApp.INSTALL,RecommendApp.SETTINGS,
			RecommendApp.MESSAGES,RecommendApp.EMAIL,RecommendApp.GALLERY} ;
	
	/**
	 * 
	* @Title: getInstance 
	* @Description: TODO 
	* @param @param context
	* @param @return    
	* @return LockHelper
	 */
	public static LockHelper getInstance(Context context) {
		if (lockManager == null)
			lockManager = new LockHelper(context);
		return lockManager;
	}

	/**
	 * 
	* <p>Title: </p>
	* <p>Description: </p>
	* @param context
	 */
	public LockHelper(Context context) {
		this.context = context;
	}

	/**
	 * 设置分类
	* @Title: getSortType 
	* @Description: TODO 
	* @param @param packageName
	* @param @return    
	* @return int
	 */
	public int getSortType(String packageName) {
		
		if (!TextUtils.isEmpty(packageName)) {
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
				recommendApp[0] = RecommendApp.INCOMING_CALL_2;
			for (int i = 0; i < recommendApp.length; i++) {
				if(packageName.equals(recommendApp[i]))
				{
					return AppLockerBean.lockerType.Recommend;
				}
			}
		}
		return AppLockerBean.lockerType.General;
	}
	
	public List<String> getSortList()
	{
		List<String> list = new ArrayList<String>() ;
		for (int i = 0; i < recommendApp.length; i++) {
			 list.add(recommendApp[i]) ;
		}
		return list ;
	}
	
	/**
	 * 
	* @ClassName: RecommendApp
	* @Description: TODO(这里用一句话描述这个类的作用)
	
	* @date 2014-10-21 上午9:33:02
	*
	 */
	public class RecommendApp {
		
		public static final String INCOMING_CALL = "com.android.phone";
		public static final String INCOMING_CALL_2 = "com.android.dialer";
		public static final String INSTALL = "com.android.packageinstaller";
		public static final String SETTINGS = "com.android.settings";
//		public static final String TASK_MANAGER = "com.android.systemui";
		public static final String MESSAGES = "com.android.mms";
		public static final String EMAIL = "com.android.email";
		public static final String GALLERY = "com.android.gallery3d";
	}
	
	/**
	 * 
	* @ClassName: LockActivity
	* @Description: TODO(这里用一句话描述这个类的作用)
	
	* @date 2014-10-21 上午9:32:57
	*
	 */
	public class LockActivity {
		public static final String InCallScreen = "com.android.phone.InCallScreen";
		public static final String InCallUi = "com.android.incallui";
	}
	
	/**
	 * 
	* @Title: isLockActivity 
	* @Description: TODO 
	* @param @param activityName
	* @param @return    
	* @return boolean
	 */
	public boolean isLockActivity(String activityName)
	{
		if(!TextUtils.isEmpty(activityName))
		{
			if(LockActivity.InCallScreen.equals(activityName))
			{
				return true ;
			}
			if(LockActivity.InCallUi.equals(activityName));{
				return true;
			}
		}
		return false ;
	}
	/**
	 * 
	* @Title: lockAppDesc 
	* @Description: TODO 
	* @param @param packageName
	* @param @return    
	* @return String
	 */
	public String getLockAppDesc(String packageName) {
		if(!TextUtils.isEmpty(packageName))
		{
			if(packageName.equals(RecommendApp.INCOMING_CALL)|packageName.equals(RecommendApp.INCOMING_CALL_2))
			{
				return context.getString(R.string.incoming_call_app_desc) ;
			}
			if(packageName.equals(RecommendApp.INSTALL))
			{
				return context.getString(R.string.install_app_desc) ;
			}
			if(packageName.equals(RecommendApp.SETTINGS))
			{
				return context.getString(R.string.settings_app_desc) ;
			}
//			if(packageName.equals(RecommendApp.TASK_MANAGER))
//			{
//				return context.getString(R.string.task_manager_app_desc) ;
//			}
			if(packageName.equals(RecommendApp.MESSAGES))
			{
				return context.getString(R.string.messages_app_desc) ;
			}
			if(packageName.equals(RecommendApp.EMAIL))
			{
				return context.getString(R.string.email_app_desc) ;
			}
			if(packageName.equals(RecommendApp.GALLERY))
			{
				return context.getString(R.string.gallery_app_desc) ;
			}
		}
		return context.getString(R.string.third_party_app_desc) ;
	}
	
	/**
	 * 
	* @Title: getlockAppName 
	* @Description: TODO 
	* @param @param packageName
	* @param @return    
	* @return String
	 */
	public String getLockAppName(String packageName) {
		if(!TextUtils.isEmpty(packageName))
		{
			if(packageName.equals(RecommendApp.INCOMING_CALL) | packageName.endsWith(RecommendApp.INCOMING_CALL_2))
			{ 
				return context.getString(R.string.incoming_call_app_name) ;
			}
			if(packageName.equals(RecommendApp.INSTALL))
			{
				return context.getString(R.string.install_app_name) ;
			}
			if(packageName.equals(RecommendApp.SETTINGS))
			{
				return context.getString(R.string.settings_app_name) ;
			}
//			if(packageName.equals(RecommendApp.TASK_MANAGER))
//			{
//				return context.getString(R.string.task_manager_app_name) ;
//			}
			if(packageName.equals(RecommendApp.MESSAGES))
			{
				return context.getString(R.string.messages_app_name) ;
			}
			if(packageName.equals(RecommendApp.EMAIL))
			{
				return context.getString(R.string.email_app_name) ;
			}
			if(packageName.equals(RecommendApp.GALLERY))
			{
				return context.getString(R.string.gallery_app_name) ;
			}
		}
		return packageName ;
	}
}
