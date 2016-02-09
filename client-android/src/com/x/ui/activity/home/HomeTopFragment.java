/**   
* @Title: HomeTopFragment.java
* @Package com.mas.amineappstore.activity
* @Description: TODO 

* @date 2014-2-13 下午03:43:16
* @version V1.0   
*/

package com.x.ui.activity.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.x.publics.utils.Constan;
import com.x.publics.utils.Utils;

/**
* @ClassName: HomeTopFragment
* @Description: TODO 

* @date 2014-2-13 下午03:43:16
* 
*/

public class HomeTopFragment extends HomeNewFragment {

	public static Fragment newInstance(Bundle bundle) {
		HomeTopFragment fragment = new HomeTopFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public int getCt() {
		return Constan.Ct.HOME_TOP;
	}
}
