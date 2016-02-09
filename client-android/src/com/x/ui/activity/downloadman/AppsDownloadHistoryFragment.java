/**   
* @Title: AppsDownloadHistoryFragment.java
* @Package com.mas.amineappstore.activity
* @Description: TODO 

* @date 2014-2-18 下午07:20:38
* @version V1.0   
*/

package com.x.ui.activity.downloadman;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.x.db.DownloadEntityManager;
import com.x.publics.model.DownloadBean;
import com.x.publics.utils.MyIntents;

import java.util.ArrayList;

/**
* @ClassName: AppsDownloadHistoryFragment
* @Description: TODO 

* @date 2014-2-18 下午07:20:38
* 
*/

public class AppsDownloadHistoryFragment extends AppsDownloadingFragment {
	public static Fragment newInstance(Bundle bundle) {
		AppsDownloadHistoryFragment fragment = new AppsDownloadHistoryFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public ArrayList<DownloadBean> getData() {

		hist = DownloadEntityManager.getInstance().getAllFinishedDownload();
		return hist;

	}

	@Override
	public int getDeleteType() {
		return MyIntents.Types.DELETE_ALL_HISTORY;
	}
}
