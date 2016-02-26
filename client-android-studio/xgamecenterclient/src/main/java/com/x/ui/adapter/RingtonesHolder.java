package com.x.ui.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.model.RingtonesBean;
import com.x.publics.model.AppInfoBean.Status;
import com.x.publics.utils.Constan;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.Utils;
import com.x.ui.view.RoundProgress;

/**
 * 
* @ClassName: RingtonesHolder
* @Description: 铃声 Adapter Holder

* @date 2014-4-9 下午1:46:07
*
 */
public class RingtonesHolder {
	public ImageView disk, play_but, disk_pic = null,ring_timeIv,ring_downloadIv;
	public View ringtongs_device;
	public ProgressBar loading = null;
	public TextView onlineTimeOrDownloadNum,name, size, duration = null;
	private boolean hasInited;
	public TextView ringDownloadBtn, ringPauseBtn, ringContinueBtn, ringSettingsBtn;
	public RoundProgress ringdownloadPercentPv;
	public View ringPauseView;
	public AnimationDrawable ringdownloadAnimation;

	public RingtonesHolder(View view/*, OnClickListener listener*/) {
		if (view != null/* && listener != null*/) {
			disk_pic = (ImageView) view.findViewById(R.id.ring_disk_pic);
			disk = (ImageView) view.findViewById(R.id.ring_disk);
			loading = (ProgressBar) view.findViewById(R.id.ring_load);
			play_but = (ImageView) view.findViewById(R.id.ring_play);
			name = (TextView) view.findViewById(R.id.ring_name);
			onlineTimeOrDownloadNum = (TextView) view.findViewById(R.id.ring_onlinetime_or_download);
			ring_timeIv  = (ImageView) view.findViewById(R.id.ring_timeIv);
			ring_downloadIv= (ImageView) view.findViewById(R.id.ring_downloadIv);
			ringtongs_device=  view.findViewById(R.id.ringtongs_device);
			size = (TextView) view.findViewById(R.id.ring_size);
			duration = (TextView) view.findViewById(R.id.ring_time);
			ringDownloadBtn = (TextView) view.findViewById(R.id.ring_download_btn);
			ringContinueBtn = (TextView) view.findViewById(R.id.ring_continue_btn);
			ringSettingsBtn = (TextView) view.findViewById(R.id.ring_settings_btn);

			ringPauseView = view.findViewById(R.id.hali_ring_pause_ll);
			ringPauseBtn = (TextView) view.findViewById(R.id.hali_ring_pause_btn);
			ringdownloadPercentPv = (RoundProgress) view.findViewById(R.id.hali_ring_pause_rp);
			ringdownloadPercentPv.setBackgroundResource(R.anim.download);
			ringdownloadAnimation = (AnimationDrawable) ringdownloadPercentPv.getBackground();
			hasInited = true;
		}
	}

	public void initData(RingtonesBean bean, Context con,int ct,String category,int flag) {
		NetworkImageUtils.load(con, ImageType.NETWORK, bean.getLogo(), R.drawable.ringtones_disk_pic,
				R.drawable.ringtones_disk_pic, disk_pic);
		name.setText(bean.getMusicName());
		size.setText(Utils.sizeFormat(bean.getFileSize()));
		duration.setText(Utils.millisTimeToDotFormat(bean.getDuration() * 1000));
		switch (flag) {
		case 1:
			if(ct == Constan.Ct.RINGTONES_NEW){
				ring_downloadIv.setVisibility(View.GONE);
				ring_timeIv.setVisibility(View.VISIBLE);
				onlineTimeOrDownloadNum.setText(bean.getOnlineTime());
			}else if(ct == Constan.Ct.RINGTONES_HOT){
				ring_timeIv.setVisibility(View.GONE);
				ring_downloadIv.setVisibility(View.VISIBLE);
				onlineTimeOrDownloadNum.setText(bean.getDownloadNum());
			}
			break;
		case 2:
			if(category.equals(Constan.Category.CATEGORY_NEW)){
				ring_downloadIv.setVisibility(View.GONE);
				ring_timeIv.setVisibility(View.VISIBLE);
				onlineTimeOrDownloadNum.setText(bean.getOnlineTime());
			}else if(category.equals(Constan.Category.CATEGORY_HOT)){
				ring_timeIv.setVisibility(View.GONE);
				ring_downloadIv.setVisibility(View.VISIBLE);
				onlineTimeOrDownloadNum.setText(bean.getDownloadNum());
			}
			break;
		case 3:
			ring_timeIv.setVisibility(View.GONE);
			ring_downloadIv.setVisibility(View.GONE);
			onlineTimeOrDownloadNum.setVisibility(View.GONE);
			ringtongs_device.setVisibility(View.GONE);
			break;

		default:
			break;
		}
		String percent = bean.getProcess();
		refreshRingStatus(bean.getStatus(), con, percent);

		play_but.setImageResource(R.drawable.ringtones_play);
		disk.clearAnimation();
		disk_pic.clearAnimation();
		loading.setVisibility(View.GONE);
	}

	public void refreshDownloadStatus(int status, Context context, String percent) {
		if (hasInited)
			refreshRingStatus(status, context, percent);
	}

	public void refreshRingStatus(int status, Context context, String percent) {
		switch (status) {
		case Status.NORMAL:
			ringDownloadBtn.setVisibility(View.VISIBLE);
			ringPauseView.setVisibility(View.GONE);
			ringContinueBtn.setVisibility(View.GONE);
			ringSettingsBtn.setVisibility(View.GONE);
			break;
		case Status.WAITING:
			ringDownloadBtn.setVisibility(View.GONE);
			ringPauseView.setVisibility(View.VISIBLE);
			ringPauseBtn.setText(context.getResources().getString(R.string.download_status_waiting));
			ringContinueBtn.setVisibility(View.GONE);
			ringSettingsBtn.setVisibility(View.GONE);
			ringdownloadPercentPv.setProgress(RoundProgress.SPECIAL_PROGRESS);
			startDownloadAnimation();
			break;
		case Status.CONNECTING:
			ringDownloadBtn.setVisibility(View.GONE);
			ringPauseView.setVisibility(View.VISIBLE);
			ringPauseBtn.setText(context.getResources().getString(R.string.download_status_connecting));
			ringContinueBtn.setVisibility(View.GONE);
			ringSettingsBtn.setVisibility(View.GONE);
			ringdownloadPercentPv.setProgress(RoundProgress.SPECIAL_PROGRESS);
			startDownloadAnimation();
			break;
		case Status.DOWNLOADING:
			ringDownloadBtn.setVisibility(View.GONE);
			ringPauseView.setVisibility(View.VISIBLE);
			ringdownloadPercentPv.setVisibility(View.VISIBLE);
			if (percent != null) {
				ringdownloadPercentPv.setProgress(Integer.valueOf(percent));
			}
			ringPauseBtn.setText(context.getResources().getString(R.string.download_status_paused));
			startDownloadAnimation();
			ringContinueBtn.setVisibility(View.GONE);
			ringSettingsBtn.setVisibility(View.GONE);
			break;
		case Status.PAUSED:
			ringDownloadBtn.setVisibility(View.GONE);
			ringPauseView.setVisibility(View.GONE);
			ringContinueBtn.setVisibility(View.VISIBLE);
			ringSettingsBtn.setVisibility(View.GONE);
			stopDownloadAnimation();
			break;
		case Status.CANINSTALL:
			ringDownloadBtn.setVisibility(View.GONE);
			ringPauseView.setVisibility(View.GONE);
			ringContinueBtn.setVisibility(View.GONE);
			ringSettingsBtn.setVisibility(View.VISIBLE);
			stopDownloadAnimation();
			break;
		default:
			break;
		}
	}

	private void startDownloadAnimation() {
		if (ringdownloadAnimation == null) {
			ringdownloadAnimation = (AnimationDrawable) ringdownloadPercentPv.getBackground();
		}
		if (ringdownloadAnimation.isRunning()) {
			return;
		}
		ringdownloadAnimation.start();
	}

	private void stopDownloadAnimation() {
		if (ringdownloadAnimation != null && ringdownloadAnimation.isRunning()) {
			ringdownloadAnimation.stop();
			Utils.setBackgroundDrawable(ringdownloadPercentPv, null);
			ringdownloadPercentPv.setBackgroundResource(R.anim.download);
			ringdownloadAnimation = null;
		}
	}

	/**
	 * 更新时长
	 * @param cur
	 * @param dur
	 * @param con
	 */
	public String refreshDuration(int cur, int dur, Context con) {
		String mdur = Utils.millisTimeToDotFormat(dur);
		String mcur = Utils.millisTimeToDotFormat(cur);
		String bg = con.getResources().getString(R.color.music_dur_bg);
		String from = "<font color=\"" + bg + "\">" + mcur + "</font>" + "/" + mdur;
		return from;
	}

	/**
	 * 恢复默认时长
	 * @param dur
	 */
	public String recoverDuration(int dur) {
		String mdur = Utils.millisTimeToDotFormat(dur * 1000);
		return mdur;
	}

	/** 
	* @param context 
	 * @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	public void setSkinTheme(Context context) {
		if (SkinConstan.skinEnabled) {
			SkinConfigManager.getInstance().setTextViewDrawableTop(context, ringDownloadBtn, SkinConstan.DOWNLOAD_BTN);
			SkinConfigManager.getInstance().setTextViewDrawableTop(context, ringContinueBtn, SkinConstan.CONTINUE_BTN);
			SkinConfigManager.getInstance().setTextViewDrawableTop(context, ringSettingsBtn, SkinConstan.SETTINGS_BTN);

			// RoundProgress Color
			SkinConfigManager.getInstance().setRoundProgressColor(context, ringdownloadPercentPv,
					SkinConstan.ROUND_PROGRESS_COLOR);
		}
	}
}
