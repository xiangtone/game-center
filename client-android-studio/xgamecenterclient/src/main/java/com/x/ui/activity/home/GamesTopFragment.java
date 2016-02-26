/**   
* @Title: GamesTopFragment.java
* @Package com.x.activity
* @Description: TODO 

* @date 2014-2-13 下午04:50:16
* @version V1.0   
*/

package com.x.ui.activity.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.x.publics.utils.Constan;

/**
* @ClassName: GamesTopFragment
* @Description: TODO 

* @date 2014-2-13 下午04:50:16
* 
*/

public class GamesTopFragment extends HomeNewFragment {

	public static Fragment newInstance(Bundle bundle) {
		GamesTopFragment fragment = new GamesTopFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public int getCt() {
		return Constan.Ct.GAME_TOP;
	}

}
