package com.x.ui.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.DownloadManager;
import com.x.publics.model.RingtonesBean;
import com.x.publics.model.AppInfoBean.Status;
import com.x.publics.utils.Constan;
import com.x.publics.utils.MediaPlayerUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.publics.utils.MediaPlayerUtil.onRingtonesListener;

/**
 * 
* @ClassName: RingtonesAdapter
* @Description: 铃声 适配(Adapter)

* @date 2014-4-9 下午1:27:44
*
 */
public class RingtonesAdapter extends ArrayListBaseAdapter<RingtonesBean> implements onRingtonesListener {

	private ListView listView = null;
	private Activity context = null;
	private static Animation operatingAnim = null;
	private MediaPlayerUtil playerUtil = null;
	private int ct = 34;
	private String category ;
	private int flag = 1;//1 为Ringtones界面 2 为categories界面进入 3 为从搜索进入

	// constructor
	public RingtonesAdapter(Activity context,int flag,int ct,String category) {
		super(context);
		this.context = context;
		this.flag = flag;
		this.ct = ct;
		this.category = category;
		operatingAnim = AnimationUtils.loadAnimation(context, R.anim.ring_play);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		playerUtil = MediaPlayerUtil.getInstance(context);
	}

	public void setListView(ListView listView) {
		this.listView = listView;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		RingtonesHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.fragment_ringtones_item, null);
			holder = new RingtonesHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (RingtonesHolder) convertView.getTag();
		}
		RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.ring_layout);
		final RingtonesBean ringtonesBean = mList.get(position);
		holder.initData(ringtonesBean, context,ct,category,flag);
		holder.setSkinTheme(context);

		holder.ringDownloadBtn.setOnClickListener(new MyListener(ringtonesBean, holder));
		holder.ringPauseView.setOnClickListener(new MyListener(ringtonesBean, holder));
		holder.ringContinueBtn.setOnClickListener(new MyListener(ringtonesBean, holder));
		holder.ringSettingsBtn.setOnClickListener(new MyListener(ringtonesBean, holder));

		layout.setTag(ringtonesBean.getUrl());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ringtonesBean.setFromMain(true) ;
				boolean result = playerUtil.start(ringtonesBean);
				if (!result) {
					ringtonesBean.setFilepath(null);
					playerUtil.start(ringtonesBean);
				}
				playerUtil.setRingtonesListener(RingtonesAdapter.this);
			}
		});

		setSkinTheme(convertView);// set skin theme

		return convertView;
	}

	private void setSkinTheme(View convertView) {
		SkinConfigManager.getInstance().setViewBackground(context, convertView, SkinConstan.LIST_VIEW_ITEM_BG);
	}

	@Override
	public void onRingtonesLoading(String mTag) {
		View view = listView.findViewWithTag(mTag);
		if (view != null) {
			RingtonesHolder mHolder = new RingtonesHolder(view);
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
			RingtonesHolder mHolder = new RingtonesHolder(view);
			mHolder.play_but.setImageResource(R.drawable.ringtones_pause);
			if (mHolder.disk.getAnimation() == null) {
				mHolder.disk.startAnimation(operatingAnim);
			}
			if (mHolder.disk_pic.getAnimation() == null) {
				mHolder.disk_pic.startAnimation(operatingAnim);
			}
			String from = mHolder.refreshDuration(cur, dur, context);
			mHolder.duration.setText(Html.fromHtml(from));
		}
	}

	@Override
	public void onRingtonesPause(String mTag, int cur, int dur) {
		View view = listView.findViewWithTag(mTag);
		if (view != null) {
			RingtonesHolder mHolder = new RingtonesHolder(view);
			mHolder.play_but.setImageResource(R.drawable.ringtones_play);
			if (mHolder.disk.getAnimation() != null) {
				mHolder.disk.clearAnimation();
			}
			if (mHolder.disk_pic.getAnimation() != null) {
				mHolder.disk_pic.clearAnimation();
			}
			String from = mHolder.refreshDuration(cur, dur, context);
			mHolder.duration.setText(Html.fromHtml(from));
		}
	}

	@Override
	public void onRingtonesStop(String mTag, int defDuration) {
		View view = listView.findViewWithTag(mTag);
		if (view != null) {
			RingtonesHolder mHolder = new RingtonesHolder(view);
			mHolder.play_but.setImageResource(R.drawable.ringtones_play);
			mHolder.disk.clearAnimation();
			mHolder.disk_pic.clearAnimation();
			String mdur = mHolder.recoverDuration(defDuration);
			mHolder.duration.setText(mdur);
		}
	}

	private class MyListener implements OnClickListener {

		private RingtonesBean ringtonesBean;
		private RingtonesHolder viewHolder;

		public MyListener(RingtonesBean ringtonesBean, RingtonesHolder viewHolder) {
			this.ringtonesBean = ringtonesBean;
			this.viewHolder = viewHolder;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ring_download_btn:
				Utils.addDownload(ringtonesBean, context);
				break;
			case R.id.hali_ring_pause_ll:
				DownloadManager.getInstance().pauseDownload(context, ringtonesBean.getUrl());
				break;
			case R.id.ring_continue_btn:
				DownloadManager.getInstance().continueDownload(context, ringtonesBean.getUrl());
				break;
			case R.id.ring_settings_btn:
				String path = ringtonesBean.getFilepath();
				boolean result = false;
				if (path != null) {
					result = Utils.showRingtonesSettingDialog(context, path);
				}
				if (!result) {//恢复
					ToastUtil.show(context, R.string.ringtones_set_failure, Toast.LENGTH_SHORT);
					DownloadEntityManager.getInstance().deleteByUrl(ringtonesBean.getUrl());
					ringtonesBean.setStatus(Status.NORMAL);
					ringtonesBean.setFilepath(null);
				}
				break;
			default:
				break;
			}
			viewHolder.refreshRingStatus(ringtonesBean.getStatus(), context, null);
		}
	}
}
