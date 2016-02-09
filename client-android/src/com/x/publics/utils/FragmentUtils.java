/**   
* @Title: FragmentUtils.java
* @Package com.mas.amineappstore.publics.utils
* @Description: TODO(用一句话描述该文件做什么)

* @date 2015-4-21 下午3:57:51
* @version V1.0   
*/

package com.x.publics.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.x.R;

/**
* @ClassName: FragmentUtils
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2015-4-21 下午3:57:51
* 
*/

public class FragmentUtils {

	/**
	 * Fragment跳转
	 * @param fm
	 * @param fragmentClass
	 * @param tag
	 * @param args
	 */
	public static Fragment turnToFragment(FragmentManager fm, Fragment desFragment, int containerId) {
		String tag = desFragment.getClass().getName();
		Fragment fragment = fm.findFragmentByTag(tag);
		boolean isFragmentExist = true;
		if (fragment == null) {
			isFragmentExist = false;
			fragment = desFragment;
		}
		if (fragment.isAdded()) {
			return fragment;
		}
		FragmentTransaction ft = fm.beginTransaction();
		if (isFragmentExist) {
			ft.replace(containerId, fragment);
		} else {
			ft.add(containerId, fragment, tag);
		}

		ft.addToBackStack(tag);
		ft.commitAllowingStateLoss();
		return fragment;
	}

}
