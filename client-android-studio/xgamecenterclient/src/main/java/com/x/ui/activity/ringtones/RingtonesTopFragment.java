package com.x.ui.activity.ringtones;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.x.publics.utils.Constan;

/**
 * 
* @ClassName: RingtonesTopFragment
* @Description: 铃声 TOP 界面

* @date 2014-4-8 上午10:47:49
*
 */
public class RingtonesTopFragment extends RingtonesHotFragment {
	
	public static Fragment newInstance(Bundle bundle) {
		RingtonesTopFragment fragment = new RingtonesTopFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	public int getCt() {
		return Constan.Ct.RINGTONES_TOP;
	}
	
}

