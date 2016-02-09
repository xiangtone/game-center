/**   
* @Title: GamesHotFragment.java
* @Package com.mas.amineappstore.activity
* @Description: TODO 

* @date 2014-2-13 下午04:49:57
* @version V1.0   
*/

package com.x.ui.activity.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.x.publics.utils.Constan;

/**
* @ClassName: GamesHotFragment
* @Description: TODO 

* @date 2014-2-13 下午04:49:57
* 
*/

public class GamesHotFragment extends HomeNewFragment {

	public static Fragment newInstance(Bundle bundle) {
		GamesHotFragment fragment = new GamesHotFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public int getCt() {
		return Constan.Ct.GAME_HOT;
	}

}
