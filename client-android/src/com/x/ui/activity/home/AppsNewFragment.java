/**   
* @Title: AppsNewFragment.java
* @Package com.x.activity
* @Description: TODO 

* @date 2014-2-13 下午04:49:09
* @version V1.0   
*/

package com.x.ui.activity.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.x.publics.utils.Constan;

/**
* @ClassName: AppsNewFragment
* @Description: TODO 

* @date 2014-2-13 下午04:49:09
* 
*/

public class AppsNewFragment extends HomeRecommendFragment {

	public static Fragment newInstance(Bundle bundle) {
		AppsNewFragment fragment = new AppsNewFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public int getCt() {
		return Constan.Ct.APP_NEW;
	}
	
	@Override
	public ViewPager getPargentViewPager() {
		return ((AppsFragment) getParentFragment()).mViewPager;
	}

}
