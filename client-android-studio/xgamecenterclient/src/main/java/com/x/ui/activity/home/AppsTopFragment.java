/**   
* @Title: AppsTopFragment.java
* @Package com.mas.amineappstore
* @Description: TODO 

* @date 2014-2-13 下午04:48:06
* @version V1.0   
*/

package com.x.ui.activity.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.x.publics.utils.Constan;

/**
* @ClassName: AppsTopFragment
* @Description: TODO 

* @date 2014-2-13 下午04:48:06
* 
*/

public class AppsTopFragment extends HomeNewFragment {

	public static Fragment newInstance(Bundle bundle) {
		AppsTopFragment fragment = new AppsTopFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public int getCt() {
		return Constan.Ct.APP_TOP;
	}

}
