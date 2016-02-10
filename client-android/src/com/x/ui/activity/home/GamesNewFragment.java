/**   
* @Title: GamesNewFragment.java
* @Package com.x.activity
* @Description: TODO 

* @date 2014-2-13 下午04:50:26
* @version V1.0   
*/

package com.x.ui.activity.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.x.publics.utils.Constan;

/**
* @ClassName: GamesNewFragment
* @Description: TODO 

* @date 2014-2-13 下午04:50:26
* 
*/

public class GamesNewFragment extends HomeRecommendFragment {

	public static Fragment newInstance(Bundle bundle) {
		GamesNewFragment fragment = new GamesNewFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public int getCt() {
		return Constan.Ct.GAME_NEW;
	}
	
	@Override
	public ViewPager getPargentViewPager() {
		return ((GamesFragment) getParentFragment()).mViewPager;
	}

}
