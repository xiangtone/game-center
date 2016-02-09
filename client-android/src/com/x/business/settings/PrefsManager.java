/**   
* @Title: PrefsManager.java
* @Package com.mas.amineappstore.business.settings
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-10-21 上午9:54:52
* @version V1.0   
*/


package com.x.business.settings;

import android.content.Context;

import com.x.publics.utils.SharedPrefsUtil;


/**
* @ClassName: PrefsManager
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-10-21 上午9:54:52
* 
*/

public class PrefsManager {
	
	public static PrefsManager prefsManager;
	
	/*控制首次打开进入mustHave 栏目*/
	public static String HOME_FIRST = "homeFirst";
	
	public static PrefsManager getInstance() {
		if (prefsManager == null)
			prefsManager = new PrefsManager();
		return prefsManager;
	}
	
	/** 
	* @Title: isHomeFirst 
	* @Description: 控制首次打开进入mustHave 栏目
	* @param @param context
	* @param @return    
	* @return boolean    
	*/ 
	
	public boolean isHomeFirst(Context context){
		return SharedPrefsUtil.getValue(context, HOME_FIRST, true);
	}
	
	public void setHomeFirst(Context context  ,boolean first){
		SharedPrefsUtil.putValue(context, HOME_FIRST, first);
	}
	
}
