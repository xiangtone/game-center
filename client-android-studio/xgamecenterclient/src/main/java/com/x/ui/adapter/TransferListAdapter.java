/**   
* @Title: TransferListAdapter.java
* @Package com.x.ui.adapter
* @Description: TODO 

* @date 2014-3-24 下午07:01:00
* @version V1.0   
*/

package com.x.ui.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.zerodata.transfer.TransferBean;
import com.x.business.zerodata.transfer.TransferIntent;
import com.x.business.zerodata.transfer.TransferManager;
import com.x.db.LocalAppEntityManager;
import com.x.db.resource.NativeResourceConstant.FileType;
import com.x.publics.download.BroadcastManager;
import com.x.publics.model.FileBean;
import com.x.publics.model.InstallAppBean;
import com.x.publics.model.RingtonesBean;
import com.x.publics.utils.IntentUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.MediaPlayerUtil;
import com.x.publics.utils.PackageUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.Utils;
import com.x.publics.utils.MediaPlayerUtil.onRingtonesListener;
import com.x.ui.activity.resource.PhotoActivity;

/**
* @ClassName: TransferListAdapter
* @Description: TODO 

* @date 2014-3-24 下午07:01:00
* 
*/

public class TransferListAdapter extends ArrayListBaseAdapter<TransferBean> implements onRingtonesListener {

	private Animation operatingAnim = null;
	private String path = "";

	public TransferListAdapter(Activity context) {
		super(context);
		operatingAnim = AnimationUtils.loadAnimation(context, R.anim.ring_play);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = inflater.inflate(R.layout.transfer_item_layout, null);
		final TransferBean transferBean = mList.get(position);
		final TransferViewHolder transferViewHolder = new TransferViewHolder(convertView);
		convertView.setTag(transferBean.getFileUrl());
		transferViewHolder.initData(transferBean, context);
		transferViewHolder.setSkinTheme(context);

		transferViewHolder.transferCancleTv.setOnClickListener(new MyBtnListener(transferViewHolder, position));
		transferViewHolder.transferInstallTv.setOnClickListener(new MyBtnListener(transferViewHolder, position));
		transferViewHolder.transferLaunchTv.setOnClickListener(new MyBtnListener(transferViewHolder, position));
		transferViewHolder.transferOpenTv.setOnClickListener(new MyBtnListener(transferViewHolder, position));
		transferViewHolder.transferSettingTv.setOnClickListener(new MyBtnListener(transferViewHolder, position));
		transferViewHolder.transferRetryTv.setOnClickListener(new MyBtnListener(transferViewHolder, position));
		transferViewHolder.musicLayout.setOnClickListener(new MyBtnListener(transferViewHolder, position));

		// set skin theme 
		setSkinTheme(convertView);

		return convertView;
	}

	private class MyBtnListener implements OnClickListener {
		TransferViewHolder viewHolder;
		TransferBean transferBean;
		int pos;

		public MyBtnListener(TransferViewHolder viewHolder, int pos) {
			this.viewHolder = viewHolder;
			this.pos = pos;
		}

		@Override
		public void onClick(View v) {
			transferBean = mList.get(pos);
			switch (v.getId()) {
			/*取消传输*/
			case R.id.til_transfer_cancle_tv:
				TransferManager.getInstance().deleteDownload(context, transferBean.getFileUrl());
				break;
			/*安装app*/
			case R.id.til_transfer_install_tv:
				PackageUtil.normalInstall(context, transferBean.getFileSavePath());
				break;
			/*运行app*/
			case R.id.til_transfer_launch_tv:
				InstallAppBean installAppBean = LocalAppEntityManager.getInstance().getLocalAppByAppName(
						transferBean.getExAttribute().replace(".apk", ""));
				Utils.launchAnotherApp(context, installAppBean.getPackageName());
				break;
			/*打开 图片视频文档*/
			case R.id.til_transfer_open_tv:
				int type = transferBean.getFileType();
				if (type == FileType.PICTURE) {
					FileBean fileBean = new FileBean();
					fileBean.setFilePath(transferBean.getFileSavePath());
					ArrayList<FileBean> list = new ArrayList<FileBean>();
					list.add(fileBean);
					Intent intent = new Intent(context, PhotoActivity.class);
					intent.putParcelableArrayListExtra("data", list);
					intent.putExtra("Item_id", 0);
					context.startActivity(intent);
				} else {
					IntentUtil.openFile(context, transferBean.getFileSavePath());
				}
				break;
			/*设置音乐*/
			case R.id.til_transfer_setting_tv:
				Utils.showRingtonesSettingDialog(context, transferBean.getFileSavePath());
				break;
			/*重新下载*/
			case R.id.til_transfer_retry_tv:
				BroadcastManager.sendBroadcast(new Intent(TransferIntent.INTENT_TRANSFER_RECONNECT));//重连
				break;
			/*音乐播放*/
			case R.id.til_disk_bg:
				if (mListView != null && transferBean.getFileUrl() != null
						&& !TextUtils.isEmpty(transferBean.getFileSavePath())
						&& StorageUtils.isFileExit(transferBean.getFileSavePath())) {
					MediaPlayerUtil playerUtil = MediaPlayerUtil.getInstance(context);
					RingtonesBean ringtonesBean = new RingtonesBean();
					ringtonesBean.setUrl(transferBean.getFileUrl());
					ringtonesBean.setFilepath(transferBean.getFileSavePath());
					playerUtil.start(ringtonesBean);
					playerUtil.setRingtonesListener(TransferListAdapter.this);
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onRingtonesLoading(String mTag) {
		View view = mListView.findViewWithTag(mTag);
		if (view != null) {
			TransferViewHolder mHolder = new TransferViewHolder(view);
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
			TransferViewHolder mHolder = new TransferViewHolder(view);
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
		LogUtil.getLogger().d("onRingtonesPause", mTag);
		View view = mListView.findViewWithTag(mTag);
		if (view != null) {
			TransferViewHolder mHolder = new TransferViewHolder(view);
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
			TransferViewHolder mHolder = new TransferViewHolder(view);
			mHolder.play_but.setImageResource(R.drawable.ringtones_play);
			mHolder.disk.clearAnimation();
			mHolder.disk_pic.clearAnimation();
			mHolder.duration.setVisibility(View.GONE);
		}
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @param @param convertView    
	* @return void    
	*/
	private void setSkinTheme(View convertView) {
		SkinConfigManager.getInstance().setViewBackground(context, convertView, SkinConstan.LIST_VIEW_ITEM_BG);
	}

}
