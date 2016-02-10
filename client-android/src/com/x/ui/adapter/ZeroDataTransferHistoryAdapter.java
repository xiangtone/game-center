/**   
 * @Title: LocalAppListAdapter.java
 * @Package com.x.adapter
 * @Description: TODO 
 
 * @date 2014-2-10 上午10:27:32
 * @version V1.0   
 */

package com.x.ui.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.x.R;
import com.x.db.zerodata.TransferHistory;
import com.x.publics.model.RingtonesBean;
import com.x.publics.utils.MediaPlayerUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.MediaPlayerUtil.onRingtonesListener;

/**
 * @Description: 零流量分享中的接收记录页面的Adapter
 */

public class ZeroDataTransferHistoryAdapter extends ArrayListBaseAdapter<TransferHistory> implements
		onRingtonesListener {

	private Animation operatingAnim = null;
	private String path = "";

	public ZeroDataTransferHistoryAdapter(Activity context) {
		super(context);
		operatingAnim = AnimationUtils.loadAnimation(context, R.anim.ring_play);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = inflater.inflate(R.layout.zdth_item_layout, null);
		final TransferHistory transferHistory = mList.get(position);
		final ZeroDataTransferHistoryViewHoler viewHolder = new ZeroDataTransferHistoryViewHoler(convertView);
		convertView.setTag(transferHistory.getFileSavePath());
		viewHolder.initData(transferHistory, context);
		viewHolder.setSkinTheme(context);

		viewHolder.fileOfOperation_ll.setOnClickListener(new MyLayoutListener(viewHolder, transferHistory));
		viewHolder.musicLayout.setOnClickListener(new MyLayoutListener(viewHolder, transferHistory));

		return convertView;
	}

	private class MyLayoutListener implements OnClickListener {
		ZeroDataTransferHistoryViewHoler viewHoler;
		TransferHistory transferHistory;

		public MyLayoutListener(ZeroDataTransferHistoryViewHoler aViewHoler, TransferHistory aTransferHistory) {
			this.viewHoler = aViewHoler;
			this.transferHistory = aTransferHistory;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.zdth_fileOfOperationll:
				if (!StorageUtils.isFileExit(transferHistory.getFileSavePath())) {
					ToastUtil.show(
							context,
							ResourceUtil.getString(context, R.string.share_history_file_not_exit,
									transferHistory.getFileName()), Toast.LENGTH_SHORT);
					return;
				}
				/* item右侧 */
				viewHoler.operationOfTheFile(transferHistory);
				break;
			/* item左侧，只有是音乐才有点击事件， 音乐播放 */
			case R.id.zdth_disk_bg:
				if (!StorageUtils.isFileExit(transferHistory.getFileSavePath())) {
					ToastUtil.show(
							context,
							ResourceUtil.getString(context, R.string.share_history_file_not_exit,
									transferHistory.getFileName()), Toast.LENGTH_SHORT);
					return;
				}
				if (mListView != null && transferHistory.getFileSavePath() != null) {
					MediaPlayerUtil playerUtil = MediaPlayerUtil.getInstance(context);
					RingtonesBean ringtonesBean = new RingtonesBean();
					ringtonesBean.setFilepath(transferHistory.getFileSavePath());
					playerUtil.start(ringtonesBean);
					playerUtil.setRingtonesListener(ZeroDataTransferHistoryAdapter.this);
				}
			default:
				break;
			}
		}
	}

	@Override
	public void onRingtonesLoading(String mTag) {
		View view = mListView.findViewWithTag(mTag);
		if (view != null) {
			ZeroDataTransferHistoryViewHoler mHolder = new ZeroDataTransferHistoryViewHoler(view);
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
		View view = mListView.findViewWithTag(mTag);
		if (view != null) {
			ZeroDataTransferHistoryViewHoler mHolder = new ZeroDataTransferHistoryViewHoler(view);
			mHolder.play_but.setImageResource(R.drawable.ringtones_pause);
			if (mHolder.disk.getAnimation() == null) {
				mHolder.disk.startAnimation(operatingAnim);
			}
			if (mHolder.disk_pic.getAnimation() == null) {
				mHolder.disk_pic.startAnimation(operatingAnim);
			}
			String from = mHolder.refreshDuration(cur, dur, context);
			mHolder.duration.setVisibility(View.VISIBLE);
			mHolder.duration.setText(Html.fromHtml(from));
		}
	}

	@Override
	public void onRingtonesPause(String mTag, int cur, int dur) {
		View view = mListView.findViewWithTag(mTag);
		if (view != null) {
			ZeroDataTransferHistoryViewHoler mHolder = new ZeroDataTransferHistoryViewHoler(view);
			mHolder.play_but.setImageResource(R.drawable.ringtones_play);
			if (mHolder.disk.getAnimation() != null) {
				mHolder.disk.clearAnimation();
			}
			if (mHolder.disk_pic.getAnimation() != null) {
				mHolder.disk_pic.clearAnimation();
			}
			String from = mHolder.refreshDuration(cur, dur, context);
			mHolder.duration.setVisibility(View.VISIBLE);
			mHolder.duration.setText(Html.fromHtml(from));
		}
	}

	@Override
	public void onRingtonesStop(String mTag, int defDuration) {
		View view = mListView.findViewWithTag(mTag);
		if (view != null) {
			ZeroDataTransferHistoryViewHoler mHolder = new ZeroDataTransferHistoryViewHoler(view);
			mHolder.play_but.setImageResource(R.drawable.ringtones_play);
			mHolder.disk.clearAnimation();
			mHolder.disk_pic.clearAnimation();
			mHolder.duration.setVisibility(View.GONE);
		}
	}

}
