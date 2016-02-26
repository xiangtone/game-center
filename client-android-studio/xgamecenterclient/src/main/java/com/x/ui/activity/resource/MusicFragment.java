package com.x.ui.activity.resource;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.x.publics.model.RingtonesBean;
import com.x.publics.utils.IntentUtil;
import com.x.publics.utils.MediaPlayerUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ThreadUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.publics.utils.MediaPlayerUtil.onRingtonesListener;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.MusicAdapter;
import com.x.ui.adapter.MusicHolder;

/**
 * 
* @ClassName: MusicFragment
* @Description: 本地铃声分类

* @date 2014-4-8 上午10:41:40
*
 */
public class MusicFragment extends BaseFragment implements OnClickListener, onRingtonesListener {

	private RelativeLayout bot_layout, check = null;
	private ImageButton share, del, back;
	private CheckBox all = null;
	private ListView listView = null;
	private View emptyView, loadingView;
	private View loadingPb, loadingLogo;
	private MusicAdapter musicAdapter = null;
	private int mode;
	private ArrayList<FileBean> musicList = new ArrayList<FileBean>();
	private ArrayList<FileBean> listPath = new ArrayList<FileBean>();
	private ResourceManage resourceManage = null;
	private Animation operatingAnim = null;

	public static Fragment newInstance(Bundle bundle) {
		MusicFragment fragment = new MusicFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		resourceManage = ResourceManage.getInstance(mActivity);
		mode = getArguments().getInt("MODE", NativeResourceConstant.DEF_MODE);
		operatingAnim = AnimationUtils.loadAnimation(mActivity, R.anim.ring_play);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		View converView = inflater.inflate(R.layout.fragment_music, null);
		loadingView = converView.findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		bot_layout = (RelativeLayout) converView.findViewById(R.id.re_bot_layout);
		back = (ImageButton) converView.findViewById(R.id.re_bot_back);
		back.setOnClickListener(this);
		if (mode == NativeResourceConstant.SHARE_MODE)
			bot_layout.setVisibility(View.GONE);
		check = (RelativeLayout) converView.findViewById(R.id.re_bot_check);
		all = (CheckBox) converView.findViewById(R.id.re_bot_all);
		check.setOnClickListener(clickListener);
		share = (ImageButton) converView.findViewById(R.id.re_bot_share);
		share.setOnClickListener(this);
		del = (ImageButton) converView.findViewById(R.id.re_bot_del);
		del.setOnClickListener(this);
		emptyView = converView.findViewById(R.id.empty_rl);

		listView = (ListView) converView.findViewById(R.id.music_list);
		listView.setOnItemClickListener(itemClickListener);

		musicAdapter = new MusicAdapter(mActivity, musicList, settingsClick);
		if (mode == NativeResourceConstant.SHARE_MODE) {
			musicAdapter.setMode(NativeResourceConstant.EDIT);
		} else {
			listView.setOnItemLongClickListener(longClickListener);
		}
		listView.setAdapter(musicAdapter);
		return converView;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (musicList != null && musicList.size() == 0) {
				loadMusicAdapter();
			}
		}
		if (mode == NativeResourceConstant.DEF_MODE && listView != null) {
			DataEyeManager.getInstance().module(ModuleName.MYCONTENTS_MUSIC, isVisibleToUser);
		}
	}

	/**
	 * 处理数据
	 */
	public void loadMusicAdapter() {
		loadingView.setVisibility(View.VISIBLE);
		ThreadUtil.start(new Runnable() {

			@Override
			public void run() {
				musicList = resourceManage.queryMusic();

				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (musicList == null || musicList.size() == 0) {
							showEmptyView(); // show emptyView
						} else {
							musicAdapter.setAllList(musicList);
						}
						loadingView.setVisibility(View.GONE);
					}
				});
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.re_bot_back:
			listPath.removeAll(listPath);
			all.setChecked(false);
			bot_layout.setVisibility(View.GONE);
			musicAdapter.setMode(NativeResourceConstant.NORMAL);
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
						resourceManage.deleteAll(mActivity, listPath, loadMessage, Category.MUSIC);
					}
				};
				DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};

				String toast = ResourceUtil.getString(mActivity, R.string.music);
				if (listPath.size() > 1) {
					Utils.showDialog(
							mActivity,
							ResourceUtil.getString(mActivity, R.string.warm_tips),
							ResourceUtil.getString(mActivity, R.string.re_dialog_delete_prompt_all,
									"" + listPath.size(), " " + toast),
							ResourceUtil.getString(mActivity, R.string.confirm), positiveListener,
							ResourceUtil.getString(mActivity, R.string.cancel), negativeListener);
				} else {
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

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (all.isChecked())
				setCheckAll(false);
			else
				setCheckAll(true);
		}
	};

	private void showEmptyView() {
		emptyView.setVisibility(View.VISIBLE);
		listView.setVisibility(View.GONE);
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
			loadMusicAdapter();
			listPath.removeAll(listPath);
		}
	};

	/**
	 * Settings Listener
	 */
	private OnClickListener settingsClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final String path = (String) v.getTag();
			Utils.showRingtonesSettingDialog(mActivity, path);
		}
	};

	public void setViewHeight(boolean isShow) {
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		if (isShow) {
			float height = getResources().getDimension(R.dimen.bot_tools_layout_h);
			((MarginLayoutParams) params).setMargins(0, 0, 0, (int) height);
		} else {
			((MarginLayoutParams) params).setMargins(0, 0, 0, 0);
		}
		listView.setLayoutParams(params);
	}

	public ArrayList<FileBean> getCheckList() {
		return listPath;
	}

	public int getCheckListSize() {
		return listPath.size();
	}

	public boolean getCheckStatus() {
		if (listPath.size() == 0 && musicList.size() == 0)
			return false;
		if (listPath.size() == musicList.size()) {
			return true;
		}
		return false;
	}

	public void setCheckAll(boolean isChecked) {
		for (FileBean bean : musicList) {
			bean.setIscheck(isChecked == true ? 1 : 0);
		}
		if (isChecked) {
			listPath.removeAll(listPath);
			listPath.addAll(musicList);
		} else {
			listPath.removeAll(listPath);
		}
		all.setChecked(getCheckStatus());
		musicAdapter.notifyDataSetChanged();
		if (mode == NativeResourceConstant.SHARE_MODE) {
			((ResourceManagementActivity) mActivity).refreshFeed(NativeResourceConstant.MUSIC_FRAGMENT);
		}
	}

	private OnItemLongClickListener longClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			if (MediaPlayerUtil.getInstance(mActivity).getIsPlaying())
				MediaPlayerUtil.getInstance(mActivity).release();
			musicAdapter.setMode(NativeResourceConstant.EDIT);
			setViewHeight(true);
			bot_layout.setVisibility(View.VISIBLE);
			return false;
		}
	};

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			FileBean bean = (FileBean) parent.getAdapter().getItem(position);
			if (musicAdapter.getMode() != NativeResourceConstant.NORMAL) {
				musicAdapter.setMode(NativeResourceConstant.EDIT);
				if (bean.getIscheck() == 0) {
					bean.setIscheck(1);
					listPath.add(bean);
				} else {
					bean.setIscheck(0);
					listPath.remove(bean);
				}
				all.setChecked(getCheckStatus());
				musicAdapter.notifyDataSetChanged();
				if (mode == NativeResourceConstant.SHARE_MODE) {
					((ResourceManagementActivity) mActivity).refreshFeed(NativeResourceConstant.MUSIC_FRAGMENT);
				}
			} else {
				MediaPlayerUtil playerUtil = MediaPlayerUtil.getInstance(mActivity);
				RingtonesBean ringtonesBean = new RingtonesBean();
				ringtonesBean.setFilepath(bean.getFilePath());
				ringtonesBean.setDuration((int) bean.getDuration());
				playerUtil.start(ringtonesBean);
				playerUtil.setRingtonesListener(MusicFragment.this);
			}
		}
	};

	@Override
	public void onRingtonesLoading(String mTag) {
		View view = listView.findViewWithTag(mTag);
		if (view != null) {
			MusicHolder mHolder = new MusicHolder(view, settingsClick);
			if (mHolder.disk.getAnimation() == null) {
				mHolder.disk.startAnimation(operatingAnim);
			}
			if (mHolder.disk_pic.getAnimation() == null) {
				mHolder.disk_pic.startAnimation(operatingAnim);
			}
		}
	}

	@Override
	public void onRingtonesPlayer(String mTag, int cur, int dur) {
		View view = listView.findViewWithTag(mTag);
		if (view != null) {
			MusicHolder mHolder = new MusicHolder(view, settingsClick);
			mHolder.play_but.setImageResource(R.drawable.ringtones_pause);
			if (mHolder.disk.getAnimation() == null) {
				mHolder.disk.startAnimation(operatingAnim);
			}
			if (mHolder.disk_pic.getAnimation() == null) {
				mHolder.disk_pic.startAnimation(operatingAnim);
			}
			String from = mHolder.refreshDuration(cur, dur, mActivity);
			mHolder.duration.setText(Html.fromHtml(from));
		}
	}

	@Override
	public void onRingtonesPause(String mTag, int cur, int dur) {
		View view = listView.findViewWithTag(mTag);
		if (view != null) {
			MusicHolder mHolder = new MusicHolder(view, settingsClick);
			mHolder.play_but.setImageResource(R.drawable.ringtones_play);
			if (mHolder.disk.getAnimation() != null) {
				mHolder.disk.clearAnimation();
			}
			if (mHolder.disk_pic.getAnimation() != null) {
				mHolder.disk_pic.clearAnimation();
			}
			String from = mHolder.refreshDuration(cur, dur, mActivity);
			mHolder.duration.setText(Html.fromHtml(from));
		}
	}

	@Override
	public void onRingtonesStop(String mTag, int defDuration) {
		View view = listView.findViewWithTag(mTag);
		if (view != null) {
			MusicHolder mHolder = new MusicHolder(view, settingsClick);
			mHolder.play_but.setImageResource(R.drawable.ringtones_play);
			mHolder.disk.clearAnimation();
			mHolder.disk_pic.clearAnimation();
			String mdur = mHolder.recoverDuration(defDuration);
			mHolder.duration.setText(mdur);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		// 刷新界面
		if (musicAdapter != null) {
			musicAdapter.notifyDataSetChanged();
		}
		if (share != null) {
			share.setEnabled(true);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		//pause
		MediaPlayerUtil.getInstance(mActivity).pause();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		//release
		MediaPlayerUtil.getInstance(mActivity).release();
	}

	@Override
	public String getPage() {
		return StatisticConstan.Pape.MUSICFRAGMENT;
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
