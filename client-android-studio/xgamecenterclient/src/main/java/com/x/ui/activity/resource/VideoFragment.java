package com.x.ui.activity.resource;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.x.R;
import com.x.business.resource.ResourceManage;
import com.x.business.resource.ResourceManage.onRefreshMessage;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.db.resource.NativeResourceConstant;
import com.x.db.resource.NativeResourceConstant.Category;
import com.x.publics.model.FileBean;
import com.x.publics.utils.IntentUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ThreadUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.VideoAdapter;

/**
 * 
* @ClassName: VideoFragment
* @Description: 本地视频分类

* @date 2014-4-8 上午10:43:07
*
 */
public class VideoFragment extends BaseFragment implements OnClickListener {

	private RelativeLayout bot_layout, check = null;
	private ImageButton share, del, back;
	private CheckBox all = null;
	private GridView gridView = null;
	private View emptyView, loadingView;
	private View loadingPb, loadingLogo;
	private VideoAdapter videoAdapter = null;
	private int mode;
	private ArrayList<FileBean> videoList = new ArrayList<FileBean>();
	private ArrayList<FileBean> listPath = new ArrayList<FileBean>();

	public static Fragment newInstance(Bundle bundle) {
		VideoFragment fragment = new VideoFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View converView = inflater.inflate(R.layout.fragment_video, null);
		mode = getArguments().getInt("MODE", NativeResourceConstant.DEF_MODE);
		loadingView = converView.findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		bot_layout = (RelativeLayout) converView.findViewById(R.id.re_bot_layout);
		back = (ImageButton) converView.findViewById(R.id.re_bot_back);
		back.setOnClickListener(this);
		check = (RelativeLayout) converView.findViewById(R.id.re_bot_check);
		all = (CheckBox) converView.findViewById(R.id.re_bot_all);
		check.setOnClickListener(clickListener);
		share = (ImageButton) converView.findViewById(R.id.re_bot_share);
		share.setOnClickListener(this);
		del = (ImageButton) converView.findViewById(R.id.re_bot_del);
		del.setOnClickListener(this);
		emptyView = converView.findViewById(R.id.empty_rl);

		gridView = (GridView) converView.findViewById(R.id.video_grid);
		gridView.setOnItemClickListener(itemClickListener);

		videoAdapter = new VideoAdapter(mActivity, videoList);
		if (mode == NativeResourceConstant.SHARE_MODE) {
			videoAdapter.setMode(NativeResourceConstant.EDIT);
		} else {
			gridView.setOnItemLongClickListener(longClickListener);
		}
		videoAdapter.setGridView(gridView);
		gridView.setAdapter(videoAdapter);
		return converView;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (videoList != null && videoList.size() <= 0)
				initAdapter();
		}
		if (mode == NativeResourceConstant.DEF_MODE && gridView != null) {
			DataEyeManager.getInstance().module(ModuleName.MYCONTENTS_VIDEO, isVisibleToUser);
		}
	}

	private void initAdapter() {
		loadingView.setVisibility(View.VISIBLE);
		ThreadUtil.start(new Runnable() {

			@Override
			public void run() {
				videoList = ResourceManage.getInstance(mActivity).queryVideo();

				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (videoList == null || videoList.size() <= 0) {
							showEmptyView(); // show emptyView
						} else {
							videoAdapter.setAllList(videoList);
						}
						loadingView.setVisibility(View.GONE);
					}
				});
			}
		});
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (all.isChecked())
				setCheckAll(false);
			else
				setCheckAll(true);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.re_bot_back:
			listPath.removeAll(listPath);
			all.setChecked(false);
			bot_layout.setVisibility(View.GONE);
			videoAdapter.setMode(NativeResourceConstant.NORMAL);
			setViewHeight(false);
			break;
		case R.id.re_bot_share:
			if (listPath.size() > 0) {
				share.setEnabled(false);
				IntentUtil.shareFile(mActivity, listPath);
			} else {
				ToastUtil.show(mActivity, mActivity.getResources().getString(R.string.share_toast), Toast.LENGTH_SHORT);
			}
			break;
		case R.id.re_bot_del:
			if (listPath.size() > 0) {
				DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						loadingView.setVisibility(View.VISIBLE);
						ResourceManage.getInstance(mActivity).deleteAll(mActivity, listPath, loadMessage,
								Category.VIDEO);
					}
				};
				DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};

				if (listPath.size() > 1) {
					String toast = ResourceUtil.getString(mActivity, R.string.videos);
					Utils.showDialog(
							mActivity,
							ResourceUtil.getString(mActivity, R.string.warm_tips),
							ResourceUtil.getString(mActivity, R.string.re_dialog_delete_prompt_all,
									"" + listPath.size(), " " + toast),
							ResourceUtil.getString(mActivity, R.string.confirm), positiveListener,
							ResourceUtil.getString(mActivity, R.string.cancel), negativeListener);
				} else {
					String toast = ResourceUtil.getString(mActivity, R.string.video);
					Utils.showDialog(
							mActivity,
							ResourceUtil.getString(mActivity, R.string.warm_tips),
							ResourceUtil.getString(mActivity, R.string.re_dialog_delete_prompt_sigin,
									"" + listPath.size(), " " + toast),
							ResourceUtil.getString(mActivity, R.string.confirm), positiveListener,
							ResourceUtil.getString(mActivity, R.string.cancel), negativeListener);
				}

			}
			break;
		}
	}

	private void showEmptyView() {
		emptyView.setVisibility(View.VISIBLE);
		gridView.setVisibility(View.GONE);
		bot_layout.setVisibility(View.GONE);
	}

	private onRefreshMessage loadMessage = new onRefreshMessage() {

		@Override
		public void onMessage(boolean result) {
			loadingView.setVisibility(View.GONE);
			if (!result) {
				ToastUtil.show(mActivity, ResourceUtil.getString(mActivity, R.string.resource_del), Toast.LENGTH_SHORT);
				return;
			}
			initAdapter();
			listPath.removeAll(listPath);
		}
	};

	private OnItemLongClickListener longClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			videoAdapter.setMode(NativeResourceConstant.EDIT);
			setViewHeight(true);
			bot_layout.setVisibility(View.VISIBLE);
			return false;
		}
	};

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			FileBean bean = (FileBean) parent.getAdapter().getItem(position);
			if (videoAdapter.getMode() != NativeResourceConstant.NORMAL) {
				videoAdapter.setMode(NativeResourceConstant.EDIT);
				if (bean.getIscheck() == 0) {
					bean.setIscheck(1);
					listPath.add(bean);
				} else {
					bean.setIscheck(0);
					listPath.remove(bean);
				}
				all.setChecked(getCheckStatus());
				videoAdapter.notifyDataSetChanged();
				if (mode == NativeResourceConstant.SHARE_MODE) {
					((ResourceManagementActivity) mActivity).refreshFeed(NativeResourceConstant.VIDEO_FRAGMENT);
				}
			} else {
				IntentUtil.openFile(mActivity, bean.getFilePath());
			}
		}
	};

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

	public ArrayList<FileBean> getCheckList() {
		return listPath;
	}

	public int getCheckListSize() {
		return listPath.size();
	}

	public void setCheckAll(boolean isChecked) {
		for (FileBean bean : videoList) {
			bean.setIscheck(isChecked == true ? 1 : 0);
		}
		if (isChecked) {
			listPath.removeAll(listPath);
			listPath.addAll(videoList);
		} else {
			listPath.removeAll(listPath);
		}
		all.setChecked(getCheckStatus());
		videoAdapter.notifyDataSetChanged();
		if (mode == NativeResourceConstant.SHARE_MODE) {
			((ResourceManagementActivity) mActivity).refreshFeed(NativeResourceConstant.VIDEO_FRAGMENT);
		}
	}

	public boolean getCheckStatus() {
		if (listPath.size() == 0 && videoList.size() == 0)
			return false;
		if (listPath.size() == videoList.size()) {
			return true;
		}
		return false;
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		// 刷新界面
		if (videoAdapter != null) {
			videoAdapter.notifyDataSetChanged();
		}
		if (share != null) {
			share.setEnabled(true);
		}
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public String getPage() {
		return StatisticConstan.Pape.VIDEOFRAGMENT;
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
		SkinConfigManager.getInstance().setCheckBoxBtnDrawable(mActivity, all, SkinConstan.OPTION_BTN);
	}
}
