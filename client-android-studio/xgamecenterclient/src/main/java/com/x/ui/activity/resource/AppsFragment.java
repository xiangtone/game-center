package com.x.ui.activity.resource;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.db.LocalAppEntityManager;
import com.x.db.resource.NativeResourceConstant;
import com.x.publics.model.FileBean;
import com.x.publics.model.InstallAppBean;
import com.x.publics.utils.ThreadUtil;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.LocalAppsShareAdapter;
import com.x.ui.adapter.LocalAppsShareAdapter.ViewHolder;
import com.x.ui.view.LinesGridView;

/**
 * 
* @ClassName: AppsFragment
* @Description: 本地APK分享

* @date 2014-4-8 上午10:40:39
*
 */
public class AppsFragment extends BaseFragment {

	private View loadingPb, loadingLogo;
	private View emptyView, loadingView;
	private LinesGridView gridView = null;
	private LocalAppsShareAdapter shareAdapter = null;
	private ArrayList<FileBean> list = new ArrayList<FileBean>();
	private ArrayList<FileBean> listPath = new ArrayList<FileBean>();
	private ResourceManagementActivity activity = null;

	public static Fragment newInstance(Bundle bundle) {
		AppsFragment fragment = new AppsFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activity = ((ResourceManagementActivity) mActivity);
		View converView = inflater.inflate(R.layout.fragment_resource_apps, null);
		loadingView = converView.findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		emptyView = converView.findViewById(R.id.empty_rl);
		gridView = (LinesGridView) converView.findViewById(R.id.video_grid);
		gridView.setOnItemClickListener(itemClickListener);
		return converView;
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		if (list.size() <= 0) {
			initAdapter();
		}
	}

	private void initAdapter() {
		loadingView.setVisibility(View.VISIBLE);
		ThreadUtil.start(new Runnable() {

			@Override
			public void run() {
				List<InstallAppBean> mlist = LocalAppEntityManager.getInstance().getAllLocalApps();
				for (InstallAppBean appBean : mlist) {
					FileBean bean = new FileBean();
					bean.setFileName(appBean.getAppName());
					if (appBean.getFileSize() != 0)
						bean.setFileSize(Long.valueOf(appBean.getFileSize()));
					else
						bean.setFileSize(0);
					bean.setIcon(appBean.getPackageName());
					bean.setFilePath(appBean.getSourceDir());
					list.add(bean);
				}
				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						loadingView.setVisibility(View.GONE);
						if (list.size() <= 0) {
							showEmptyView(); // show emptyView
						} else {
							shareAdapter = new LocalAppsShareAdapter(mActivity, list);
							gridView.setAdapter(shareAdapter);
						}
					}
				});
			}
		});
	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			FileBean bean = list.get(position);
			ViewHolder viewHolder = (ViewHolder) view.getTag();
			if (bean.getIscheck() == 0) {
				bean.setIscheck(1);
				viewHolder.checkBox.setChecked(true);
				listPath.add(bean);
			} else {
				bean.setIscheck(0);
				viewHolder.checkBox.setChecked(false);
				listPath.remove(bean);
			}
			((ResourceManagementActivity) mActivity).refreshFeed(NativeResourceConstant.APPS_FRAGMENT);
		}
	};

	private void showEmptyView() {
		emptyView.setVisibility(View.VISIBLE);
		gridView.setVisibility(View.GONE);
	}

	public ArrayList<FileBean> getCheckList() {
		return listPath;
	}

	public int getCheckListSize() {
		return listPath.size();
	}

	public void setViewHeight(boolean isShow) {
		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		if (isShow) {
			float height = getResources().getDimension(R.dimen.bot_tools_layout_h);
			((MarginLayoutParams) params).setMargins(0, 0, 0, (int) height);
		} else {
			((MarginLayoutParams) params).setMargins(0, 0, 0, 0);
		}
		gridView.setLayoutParams(params);
	}

	public void setCheckAll(boolean isChecked) {
		if (list.size() == 0)
			return;
		for (FileBean bean : list) {
			bean.setIscheck(isChecked == true ? 1 : 0);
		}
		if (isChecked) {
			listPath.removeAll(listPath);
			listPath.addAll(list);
		} else {
			listPath.removeAll(listPath);
		}
		shareAdapter.notifyDataSetChanged();
		activity.refreshFeed(NativeResourceConstant.APPS_FRAGMENT);
	}

	public boolean getCheckStatus() {
		if (listPath.size() == 0 && list.size() == 0)
			return false;
		if (listPath.size() == list.size()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return super.onBackPressed();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void
	 */
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setViewBackground(mActivity, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(mActivity, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
	}
}
