package com.x.ui.activity.ringtones;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.x.publics.utils.Constan;

/**
 * 
* @ClassName: RingtonesNewFragment
* @Description: 铃声 NEW 界面

* @date 2014-4-8 上午10:47:35
*
 */
public class RingtonesNewFragment extends RingtonesHotFragment {
	
	public static Fragment newInstance(Bundle bundle) {
		RingtonesNewFragment fragment = new RingtonesNewFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	public int getCt() {
		return Constan.Ct.RINGTONES_NEW;
	}
	
}
