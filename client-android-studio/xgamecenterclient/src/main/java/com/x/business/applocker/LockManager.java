/**   
 * @Title: LockManager.java
 * @Package com.x.business.applocker
 * @Description: TODO(用一句话描述该文件做仿و)
 
 * @date 2014-10-10 上午9:49:30
 * @version V1.0   
 */

package com.x.business.applocker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.x.R;
import com.x.business.alarm.ApplockerAlarm;
import com.x.business.applocker.LockHelper.RecommendApp;
import com.x.business.statistic.DataEyeHelper;
import com.x.db.applocker.AppLockerDBHelper;
import com.x.db.dao.CommonLockerApp;
import com.x.publics.model.AppLockerBean;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SharedPrefsUtil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;

/**
 * @ClassName: LockManager
 * @Description: TODO(Applocker Manager Class)
 
 * @date 2014-10-10 上午9:49:30
 * 
 */

public class LockManager {
	private static String TAG = "LockManager";
	private static final String PASSWORD = "password";
	private static final String FIRST_FLAG = "first_use";
	private static final String PACKAGE_NAME = "package_name";
	private static final String SECURITY_QUESTION = "security_question";
	private static final String SECURITY_ANSWER = "security_answer";
	private static final String LOCK_SWITCH = "lock_switch";
	private static final String LOCK_TIME_START = "lock_time_start";
	private static final String LOCK_TIME_END = "lock_time_end";
	private Context context;
	public static LockManager lockManager;

	public static LockManager getInstance(Context context) {
		if (lockManager == null)
			lockManager = new LockManager(context);
		return lockManager;
	}

	public LockManager(Context context) {
		this.context = context;
	}

	/**
	 * check weather this flag exist in the SharedPreference.
	 * 
	 * @Title: isFirstUse
	 * @Description: check weather this flag exist in the sharedpreference.if it
	 *               exists,return false.if not ,return true.
	 * @param @return
	 * @return boolean
	 */
	public boolean isFirstUse() {
		boolean flag = SharedPrefsUtil.isFlagExist(context, FIRST_FLAG);
		if (flag == true) {
			return false;
		} else {
			return true;
		}
	}

	public void changeFirstUseState() {
		SharedPrefsUtil.putLockerValue(context, FIRST_FLAG, "0");
	}

	/**
	 * 
	 * @Title: savePassword
	 * @Description: TODO save password in SharedPreference
	 * @param @param password
	 * @return void
	 */
	public void savePassword(String password) {
		SharedPrefsUtil.putLockerValue(context, PASSWORD, password);
	}

	/**
	 * check weather the password you input is right.
	 * 
	 * @Title: checkPassword
	 * @Description: TODO
	 * @param @param password
	 * @param @return
	 * @return boolean
	 */
	public boolean checkPassword(String password) {
		String savedLocker = SharedPrefsUtil.getLockerValue(context, PASSWORD, "0");
		return savedLocker.equals(password);
	}

	/**
	 * change the password you already saved.
	 * 
	 * @Title: changePassword
	 * @Description: TODO
	 * @param @param password
	 * @return void
	 */
	public void changePassword(String password) {
		savePassword(password);
	}

	/**
	 * 
	 * @Title: saveCurrentLockPackageName
	 * @Description: TODO
	 * @param @param packageName
	 * @return void
	 */
	public void saveCurrentLockPackageName(String packageName) {
		SharedPrefsUtil.putLockerValue(context, PACKAGE_NAME, packageName);
	}

	/**
	 * 
	 * @Title: getCurrentLockPackageName
	 * @Description: TODO
	 * @param @return
	 * @return String
	 */
	public String getCurrentLockPackageName() {
		return SharedPrefsUtil.getLockerValue(context, PACKAGE_NAME, "");
	}

	/**
	 * save security question in sp.only one question in sp
	 * 
	 * @Title: saveSecurityQuestion
	 * @Description: TODO
	 * @param @param securityQuestion
	 * @return void
	 */
	public void saveSecurityQuestion(String securityQuestion) {
		SharedPrefsUtil.putLockerValue(context, SECURITY_QUESTION, securityQuestion);
	}

	/**
	 * get security question from SP
	 * @Title: getSecurityQuestion
	 * @Description: TODO
	 * @param @return
	 * @return String
	 */
	public String getSecurityQuestion() {
		return SharedPrefsUtil.getLockerValue(context, SECURITY_QUESTION, "");
	}

	/**
	 * save security answer in sp.only one question in SP
	 * 
	 * @Title: saveSecurityAnswer
	 * @Description: TODO
	 * @param @param securityAnswer
	 * @return void
	 */
	public void saveSecurityAnswer(String securityAnswer) {
		SharedPrefsUtil.putLockerValue(context, SECURITY_ANSWER, securityAnswer);
	}

	/**
	 * save the status of app locker lock switch in SP 
	* @Title: saveLockStatus 
	* @Description: TODO 
	* @param @param isLocked    
	* @return void
	 */
	public void saveLockStatus(boolean isLocked) {
		SharedPrefsUtil.putLockerValue(context, LOCK_SWITCH, isLocked);
	}

	/**
	 * get the status of app locker lock switch from SP 
	* @Title: getLockStatus 
	* @Description: TODO 
	* @param @return    
	* @return boolean
	 */
	public boolean getLockStatus() {
		return SharedPrefsUtil.getLockerValue(context, LOCK_SWITCH, false);
	}

	/**
	 * save the time when to start lock component in SP
	* @Title: saveStartLockTime 
	* @Description: TODO 
	* @param @param startTime    
	* @return void
	 */
	public void saveStartLockTime(String startTime) {
		SharedPrefsUtil.putLockerValue(context, LOCK_TIME_START, startTime);
	}

	/**
	 * get start lock component time
	* @Title: getStartLockTime 
	* @Description: TODO 
	* @param @return    
	* @return String
	 */
	public String getStartLockTime() {
		return SharedPrefsUtil.getLockerValue(context, LOCK_TIME_START, "0");
	}

	/**
	 * save the time when to stop lock component in SP
	* @Title: saveStopLockTime 
	* @Description: TODO 
	* @param @param endTime    
	* @return void
	 */
	public void saveStopLockTime(String endTime) {
		SharedPrefsUtil.putLockerValue(context, LOCK_TIME_END, endTime);
	}

	/**
	 * get stop lock component time
	* @Title: getStopLockTime 
	* @Description: TODO 
	* @param @return    
	* @return String
	 */
	public String getStopLockTime() {
		return SharedPrefsUtil.getLockerValue(context, LOCK_TIME_END, "24");
	}

	/**
	 * get security answer from SP
	 * @Title: getSecurityAnswer
	 * @Description: TODO
	 * @param @return
	 * @return String
	 */
	public String getSecurityAnswer() {
		return SharedPrefsUtil.getLockerValue(context, SECURITY_ANSWER, "");
	}

	/**
	 * 查找被锁住的应用
	* @Title: findLockeredApp 
	* @Description: TODO 
	* @param @return    
	* @return List<String>
	 */
	public List<String> findLockeredApp() {
		return AppLockerDBHelper.getInstance(context).findLockeredApp();
	}

	/**
	 * 锁定该xpp
	* @Title: lockApp 
	* @Description: TODO 
	* @param @param packageName    
	* @return void
	 */
	public void lockApp(String packageName) {
		AppLockerDBHelper.getInstance(context).updateSetLocked(packageName);
		LockTask.isUpdateList = true;
	}

	/**
	 * 锁定该xpp
	* @Title: lockApp 
	* @Description: TODO 
	* @param @param packageName    
	* @return void
	 */
	public void unLockApp(String packageName) {
		AppLockerDBHelper.getInstance(context).updateSetUnLocked(packageName);
		LockTask.isUpdateList = true;
	}

	/**
	 * 获取承܉应用
	* @Title: getAppLockerList 
	* @Description: TODO 
	* @param @return    
	* @return List<AppLockerBean>
	 */
	public List<AppLockerBean> getAppLockerList() {
		List<AppLockerBean> appLockerList = new ArrayList<AppLockerBean>();
		List<CommonLockerApp> commonLockerAppList = AppLockerDBHelper.getInstance(context).findAllCommonLockerApp();
		if (commonLockerAppList != null && commonLockerAppList.size() > 0) {
			for (CommonLockerApp commonLockerApp : commonLockerAppList) {

				AppLockerBean appLockerBean = new AppLockerBean();
				appLockerBean.setAppName(commonLockerApp.getAppName());
				appLockerBean.setId(commonLockerApp.getId());
				appLockerBean.setLockerSortType(commonLockerApp.getSortType());
				appLockerBean.setPackageName(commonLockerApp.getPackageName());
				appLockerBean.setLockerSortTypeName(ResourceUtil.getString(context,R.string.app_lock_general));
				appLockerBean.setLockerDesc(context.getString(R.string.third_party_app_desc));
				appLockerBean.setAppName(commonLockerApp.getAppName());
				appLockerBean.setLocked(commonLockerApp.isLocked);
				appLockerBean.setGroupApp(commonLockerApp.isGroupApp);
				appLockerBean.setActivityName(commonLockerApp.getActivityName());
				if(commonLockerApp.isGroupApp)
				{
					appLockerBean.setActivityName(commonLockerApp.getActivityName());
				}
				//如果是推介应甿
				if (commonLockerApp.getSortType() == AppLockerBean.lockerType.Recommend) {
					
					appLockerBean.setLockerSortTypeName(ResourceUtil.getString(context,R.string.app_lock_recommend));
					appLockerBean.setLockerDesc(LockHelper.getInstance(context).getLockAppDesc(commonLockerApp.getPackageName()));
					appLockerBean.setAppName(LockHelper.getInstance(context).getLockAppName(commonLockerApp.getPackageName()));
				}
				appLockerList.add(appLockerBean);
			}
		}
		Collections.sort(appLockerList);
		return appLockerList;

	}

	/**
	 * 初妾化应甿
	* @Title: initApplockerApp 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	public void initApplockerApp() {
		boolean isInitApplockerApp = SharedPrefsUtil.getValue(context, "initApplockerApp", false);

		if (!isInitApplockerApp) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					if (insertAppLockerData(null)) {
						SharedPrefsUtil.putValue(context, "initApplockerApp", true);
					}
				}
			}).start();

		}
	}

	/**
	 * 增加应用
	* @Title: addLockApp 
	* @Description: TODO 
	* @param @param packageName    
	* @return void
	 */
	public void addLockApp(final String packageName) {
//		ResolveInfo resolveInfo = getExistResolveInfo(packageName);
//		if (resolveInfo != null) {
//			PackageManager pm = context.getPackageManager(); // 获得PackageManager对象  
//			AppLockerDBHelper.getInstance(context).insertCommonLocker(
//					getCommonLockerApp(resolveInfo.loadLabel(pm).toString(), packageName));
//		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				insertAppLockerData(packageName);
			}
		}).start();
	}

	/**
	 * 
	* @Title: deleteLockApp 
	* @Description: TODO 
	* @param @param packageName    
	* @return void
	 */
	public void deleteLockApp(String packageName) {
		if (!TextUtils.isEmpty(packageName)) {
			AppLockerDBHelper.getInstance(context).deleteLockerAppByPackageName(packageName);
		}
	}

	/**
	 * 封裑实例
	* @Title: getCommonLockerApp 
	* @Description: TODO 
	* @param @param appName
	* @param @param packageName
	* @param @return    
	* @return CommonLockerApp
	 */
	public CommonLockerApp getCommonLockerApp(String appName, String packageName) {
		CommonLockerApp commonLockerApp = new CommonLockerApp();
		commonLockerApp.setAppName(appName);
		commonLockerApp.setPackageName(packageName);
		commonLockerApp.setLocked(false);
		commonLockerApp.setEnable(true);
		commonLockerApp.setSortType(LockHelper.getInstance(context).getSortType(packageName));
		return commonLockerApp;
	}

	/**
	 * 
	* @Title: removerRepeat 
	* @Description: TODO 
	* @param @param resolveInfoList
	* @param @return    
	* @return List<ResolveInfo>
	 */
	public ResolveInfo getExistResolveInfo(String packageName) {

		PackageManager pm = context.getPackageManager(); // 获得PackageManager对象  
		Intent mainIntent = new Intent(Intent.ACTION_MAIN);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		// 通过查询，获得所有ResolveInfo对象.  
		List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(mainIntent,
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
		if (resolveInfoList != null && resolveInfoList.size() > 0 && !TextUtils.isEmpty(packageName)) {
			for (int i = 0; i < resolveInfoList.size(); i++) {
				if (resolveInfoList.get(i).activityInfo.packageName.equals(packageName)) {
					return resolveInfoList.get(i);
				}
			}
		}
		return null;
	}

	public List<ResolveInfo> getResolveInfoList(String packageName) {
		if (!TextUtils.isEmpty(packageName)) {
			PackageManager pm = context.getPackageManager(); // 获得PackageManager对象  
			Intent mainIntent = new Intent(Intent.ACTION_MAIN);
			mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			// 通过查询，获得所有ResolveInfo对象.  
			List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(mainIntent,
					PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
		}
		return null;
	}

	/**
	 * 插入数据庿
	* @Title: insertAppLockerData 
	* @Description: TODO 
	* @param @return    
	* @return boolean
	 */
	public boolean insertAppLockerData(String packageName) {
		try {
			PackageManager pm = context.getPackageManager(); // 获得PackageManager对象  
			Intent mainIntent = new Intent(Intent.ACTION_MAIN);
			mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(mainIntent,
					PackageManager.COMPONENT_ENABLED_STATE_ENABLED);

			if (resolveInfoList == null || resolveInfoList.size() == 0) {
				return false;
			}
			
			List<String> sortList = LockHelper.getInstance(context).getSortList() ;
			
			for (ResolveInfo resolveInfo : resolveInfoList) {
				// 如果是新增应用
				if(!TextUtils.isEmpty(packageName) &&!resolveInfo.activityInfo.packageName.equals(packageName)){
					continue ;
				}
				CommonLockerApp commonLockerApp = new CommonLockerApp();
				commonLockerApp.setAppName(resolveInfo.loadLabel(pm).toString());
				commonLockerApp.setPackageName(resolveInfo.activityInfo.packageName);
				commonLockerApp.setLocked(false);
				commonLockerApp.setEnable(true);
				commonLockerApp.setSortType(AppLockerBean.lockerType.General);
				
				
				if(LockHelper.getInstance(context).getSortType(resolveInfo.activityInfo.packageName) == AppLockerBean.lockerType.Recommend)
				{
					if(sortList.contains(resolveInfo.activityInfo.packageName))
					{
						commonLockerApp.setSortType(AppLockerBean.lockerType.Recommend) ;
						sortList.remove(resolveInfo.activityInfo.packageName) ;
					}
				}
				
				if(isGroupApp(resolveInfoList,resolveInfo))
				{
					commonLockerApp.isGroupApp = true ;
					commonLockerApp.setActivityName(resolveInfo.activityInfo.name) ;
					commonLockerApp.setAppName(resolveInfo.loadLabel(pm).toString()) ;
				}
				
				//排除zapp
				if(!context.getPackageName().equals(commonLockerApp.getPackageName()))
				{
					AppLockerDBHelper.getInstance(context).insertCommonLocker(commonLockerApp);	
				}
				
			}
			
			if(TextUtils.isEmpty(packageName)){
				appendLockerApp(sortList) ;
			}
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	* @Title: appendLockerApp 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	public void appendLockerApp(List<String> sortList){
		
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> pakList = pm.getInstalledPackages(0);
		
		for (PackageInfo packageInfo : pakList) {
		if(LockHelper.getInstance(context).getSortType(packageInfo.packageName) == AppLockerBean.lockerType.Recommend)
		{
			if(sortList.contains(packageInfo.packageName))
			{
				AppLockerDBHelper.getInstance(context).insertCommonLocker(getCommonLockerApp(pm.getApplicationLabel(packageInfo.applicationInfo).toString(), packageInfo.packageName));	
			}
			
		}
		}
		
	}
	/**
	 * 去重
	* @Title: removerRepeat 
	* @Description: TODO 
	* @param @param resolveInfoList
	* @param @return    
	* @return List<ResolveInfo>
	 */
	public boolean isGroupApp(List<ResolveInfo> resolveInfoList, ResolveInfo resolveInfo) {
		if (resolveInfoList != null && resolveInfoList.size() > 0) {
			int count = 0 ;
			for (int i = 0; i < resolveInfoList.size(); i++) {
				if(resolveInfo.activityInfo.packageName.equals(resolveInfoList.get(i).activityInfo.packageName)){
					count++ ;
				}
				if(count >=2)
				{
					return true ;
				}
			}
		}
		return false;
	}

	/**
	 * start
	* @Title: startApplockerComponent 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	public void startApplockerComponent() {
		if (getLockStatus()) {
			context.startService(new Intent(context, LockService.class));
			ApplockerAlarm applockerAlarm= new ApplockerAlarm(context) ;
			applockerAlarm.runAlarmTask();
		}
	}

	/**
	 * start
	* @Title: startApplockerComponent 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	public void stopApplockerComponent() {
		context.stopService(new Intent(context, LockService.class));
		ApplockerAlarm.getInstance(context).cancelAlarm();
	}
}
