/**   
* @Title: CategoryNewFragment.java
* @Package com.mas.amineappstore.activity
* @Description: TODO 

* @date 2014-2-14 上午10:30:12
* @version V1.0   
*/

package com.x.ui.activity.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.x.publics.utils.Constan;

/**
* @ClassName: CategoryNewFragment
* @Description: TODO 

* @date 2014-2-14 上午10:30:12
* 
*/

public class CategoryNewFragment extends CategoryHotFragment {

	public static Fragment newInstance(Bundle bundle) {
		CategoryNewFragment fragment = new CategoryNewFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public String getType() {
		return Constan.Category.CATEGORY_NEW;
	}
}
