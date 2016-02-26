package com.x.ui.activity.ringtones;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.x.publics.utils.Constan;

/**
 * 
* @ClassName: RingtonesCategoryTopFragment
* @Description: 铃声分类 TOP 子页面

* @date 2014-4-8 上午10:46:12
*
 */
public class RingtonesCategoryTopFragment extends RingtonesCategoryHotFragment {
	
	public static Fragment newInstance(Bundle bundle) {
		RingtonesCategoryTopFragment fragment = new RingtonesCategoryTopFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public String getCategory() {
		return Constan.Category.CATEGORY_NEW;
	}
	
}

