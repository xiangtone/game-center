/**   
* @Title: AppsHotFragment.java
* @Package com.x.activity
* @Description: TODO 

* @date 2014-2-13 下午04:47:52
* @version V1.0   
*/

package com.x.ui.activity.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.x.publics.utils.Constan;

/**
* @ClassName: AppsHotFragment
* @Description: TODO 

* @date 2014-2-13 下午04:47:52
* 
*/

public class AppsHotFragment extends HomeNewFragment {

	public static Fragment newInstance(Bundle bundle) {
		AppsHotFragment fragment = new AppsHotFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public int getCt() {
		return Constan.Ct.APP_HOT;
	}

}
