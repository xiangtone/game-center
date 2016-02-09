/**   
* @Title: GamesCategoriesFragment.java
* @Package com.mas.amineappstore.activity
* @Description: TODO 

* @date 2014-2-13 下午04:49:43
* @version V1.0   
*/

package com.x.ui.activity.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.x.publics.utils.Constan;

/**
* @ClassName: GamesCategoriesFragment
* @Description: TODO 

* @date 2014-2-13 下午04:49:43
* 
*/

public class GamesCategoriesFragment extends AppsCategoriesFragment {

	public static Fragment newInstance(Bundle bundle) {
		GamesCategoriesFragment fragment = new GamesCategoriesFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public int getCt() {
		return Constan.Ct.GAME_CATEGORY;
	}
	
	@Override
	public int getCt2() {
		return Constan.Ct.GAME_CATEGORY_DETAIL;
	}
}
