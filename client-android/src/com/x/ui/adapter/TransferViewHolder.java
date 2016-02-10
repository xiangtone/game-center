/**   
* @Title: TransferViewHolder.java
* @Package com.x.ui.adapter
* @Description: TODO 

* @date 2014-3-24 下午07:01:20
* @version V1.0   
*/

package com.x.ui.adapter;

import java.io.Serializable;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.zerodata.transfer.TransferBean;
import com.x.business.zerodata.transfer.TransferTask;
import com.x.db.resource.NativeResourceConstant.FileType;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.Utils;

/**
* @ClassName: TransferViewHolder
* @Description: TODO 

* @date 2014-3-24 下午07:01:20
* 
*/

public class TransferViewHolder implements Serializable {

	private static final long serialVersionUID = 1L;

	public ImageView fileIcon;
	public TextView fileName;
	public TextView fileSizeStatus;
	public TextView fileTransferSattus;
	public ProgressBar progressBar;
	public TextView transferCancleTv, transferInstallTv, transferLaunchTv, transferOpenTv, transferSettingTv,
			transferRetryTv;
	public boolean hasInited;
	public LinearLayout llytIsShow;

	//Music
	public ImageView disk, play_but, disk_pic = null;
	public ProgressBar loading = null;
	public RelativeLayout musicLayout = null;
	public TextView duration = null;

	public TransferViewHolder(View view) {
		if (view != null) {
			fileIcon = (ImageView) view.findViewById(R.id.til_transfer_icon_iv);
			fileName = (TextView) view.findViewById(R.id.til_file_name_tv);
			fileSizeStatus = (TextView) view.findViewById(R.id.til_file_transfer_size_tv);
			fileTransferSattus = (TextView) view.findViewById(R.id.til_file_transfer_status_tv);
			progressBar = (ProgressBar) view.findViewById(R.id.til_transfer_file_pb);
			transferCancleTv = (TextView) view.findViewById(R.id.til_transfer_cancle_tv);
			transferInstallTv = (TextView) view.findViewById(R.id.til_transfer_install_tv);
			transferLaunchTv = (TextView) view.findViewById(R.id.til_transfer_launch_tv);
			transferOpenTv = (TextView) view.findViewById(R.id.til_transfer_open_tv);
			transferSettingTv = (TextView) view.findViewById(R.id.til_transfer_setting_tv);
			transferRetryTv = (TextView) view.findViewById(R.id.til_transfer_retry_tv);

			//Music
			disk_pic = (ImageView) view.findViewById(R.id.til_disk_pic);
			disk = (ImageView) view.findViewById(R.id.til_disk);
			loading = (ProgressBar) view.findViewById(R.id.til_load);
			play_but = (ImageView) view.findViewById(R.id.til_play);
			duration = (TextView) view.findViewById(R.id.til_ring_time);
			musicLayout = (RelativeLayout) view.findViewById(R.id.til_disk_bg);
			hasInited = true;
		}
	}

	public void refreshData(String prompt, String speed, int progress) {
		if (hasInited) {
			if (prompt != null)
				fileSizeStatus.setText(prompt);
			if (speed != null)
				fileTransferSattus.setText(speed);
			if (progress != 0)
				progressBar.setProgress(progress);
		}
	}

	public void initData(TransferBean transferBean, Context context) {
		fileName.setText(transferBean.getExAttribute());
		int type = transferBean.getFileType();
		if (type == FileType.MUSIC) {
			fileIcon.setVisibility(View.GONE);
			musicLayout.setVisibility(View.VISIBLE);
		} else {
			fileIcon.setVisibility(View.VISIBLE);
			musicLayout.setVisibility(View.GONE);
			switch (type) {
			case FileType.TXT:
				fileIcon.setImageResource(R.drawable.ic_resource_txt);
				break;
			case FileType.DOC:
				fileIcon.setImageResource(R.drawable.ic_resource_doc);
				break;
			case FileType.XLS:
				fileIcon.setImageResource(R.drawable.ic_resource_xls);
				break;
			case FileType.PDF:
				fileIcon.setImageResource(R.drawable.ic_resource_pdf);
				break;
			case FileType.PPT:
				fileIcon.setImageResource(R.drawable.ic_resource_ppt);
				break;
			case FileType.VIDEO:
				fileIcon.setImageResource(R.drawable.file_video);
				break;
			case FileType.PICTURE:
				fileIcon.setImageResource(R.drawable.ic_screen_default_picture);
				break;
			case FileType.APK:
				fileIcon.setImageResource(R.drawable.ic_apk_50);
				break;
			case FileType.UNKNOWN:
				fileIcon.setImageResource(R.drawable.ic_resource_unknown);
				break;
			default:
				fileIcon.setImageResource(R.drawable.ic_resource_unknown);
				break;
			}
		}

		refreshDownloadStatus(transferBean, context);
	}

	public void refreshDownloadStatus(TransferBean transferBean, Context context) {
		if (!hasInited)
			return;
		int status = transferBean.getFileStatus();
		progressBar.setVisibility(View.VISIBLE);
		fileSizeStatus.setText(Utils.sizeFormat(transferBean.getCurrentBytes()) + " / "
				+ Utils.sizeFormat(transferBean.getFileSize()));
		if (transferBean.getFileSize() == 0) {
			progressBar.setProgress(0);
		} else {
			progressBar.setProgress((int) (transferBean.getCurrentBytes() * 100 / transferBean.getFileSize()));
		}
		int fileType = transferBean.getFileType();
		if (fileType == FileType.MUSIC) {
			play_but.setImageResource(R.drawable.ringtones_play);
		}
		if (disk.getAnimation() != null) {
			disk.clearAnimation();
		}
		if (disk_pic.getAnimation() != null) {
			disk_pic.clearAnimation();
		}
		duration.setText("");
		switch (status) {
		case TransferTask.TASK_FINISH:
			if (fileType == FileType.APK) {
				transferInstallTv.setVisibility(View.VISIBLE);
				transferOpenTv.setVisibility(View.INVISIBLE);
				transferSettingTv.setVisibility(View.INVISIBLE);

				NetworkImageUtils.load(context, ImageType.APK, transferBean.getFileSavePath(), R.drawable.ic_apk_50,
						R.drawable.ic_apk_50, fileIcon);
			} else if (fileType == FileType.MUSIC) {
				transferInstallTv.setVisibility(View.INVISIBLE);
				transferOpenTv.setVisibility(View.INVISIBLE);
				transferSettingTv.setVisibility(View.VISIBLE);

				play_but.setImageResource(R.drawable.ringtones_play);
				disk.clearAnimation();
				disk_pic.clearAnimation();
				loading.setVisibility(View.GONE);
				disk_pic.setImageResource(R.drawable.ringtones_disk_pic);
				duration.setText("");
			} else {
				if (fileType == FileType.PICTURE) {
					NetworkImageUtils.load(context, ImageType.LOCALPIC, transferBean.getFileSavePath(),
							R.drawable.ic_screen_default_picture, R.drawable.ic_screen_default_picture, fileIcon);
				}
				transferInstallTv.setVisibility(View.INVISIBLE);
				transferOpenTv.setVisibility(View.VISIBLE);
				transferSettingTv.setVisibility(View.INVISIBLE);
			}
			transferCancleTv.setVisibility(View.INVISIBLE);
			transferLaunchTv.setVisibility(View.INVISIBLE);
			transferRetryTv.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.GONE);
			fileTransferSattus.setText(context.getString(R.string.download_status_finished));
			fileSizeStatus.setText(Utils.sizeFormat(transferBean.getFileSize()));
			break;
		case TransferTask.TASK_LAUNCH:
			transferCancleTv.setVisibility(View.INVISIBLE);
			transferLaunchTv.setVisibility(View.VISIBLE);
			transferRetryTv.setVisibility(View.INVISIBLE);
			transferInstallTv.setVisibility(View.INVISIBLE);
			transferOpenTv.setVisibility(View.INVISIBLE);
			transferSettingTv.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.GONE);
			fileTransferSattus.setText(context.getString(R.string.download_status_finished));
			break;
		case TransferTask.TASK_DOWNLOADING:

			transferCancleTv.setVisibility(View.VISIBLE);
			transferLaunchTv.setVisibility(View.INVISIBLE);
			transferRetryTv.setVisibility(View.INVISIBLE);
			transferInstallTv.setVisibility(View.INVISIBLE);
			transferOpenTv.setVisibility(View.INVISIBLE);
			transferSettingTv.setVisibility(View.INVISIBLE);
			if (transferBean.getSpeed() != null)
				fileTransferSattus.setText(transferBean.getSpeed());
			break;
		case TransferTask.TASK_WAITING:

			transferCancleTv.setVisibility(View.VISIBLE);
			transferLaunchTv.setVisibility(View.INVISIBLE);
			transferRetryTv.setVisibility(View.INVISIBLE);
			transferInstallTv.setVisibility(View.INVISIBLE);
			transferOpenTv.setVisibility(View.INVISIBLE);
			transferSettingTv.setVisibility(View.INVISIBLE);
			fileTransferSattus.setText(context.getResources().getString(R.string.download_status_waiting));
			break;
		case TransferTask.TASK_CONNECTING:
			transferCancleTv.setVisibility(View.VISIBLE);
			transferLaunchTv.setVisibility(View.INVISIBLE);
			transferRetryTv.setVisibility(View.INVISIBLE);
			transferInstallTv.setVisibility(View.INVISIBLE);
			transferOpenTv.setVisibility(View.INVISIBLE);
			transferSettingTv.setVisibility(View.INVISIBLE);
			fileTransferSattus.setText(context.getResources().getString(R.string.download_status_connecting));
			break;
		case TransferTask.TASK_PAUSE:
			transferCancleTv.setVisibility(View.INVISIBLE);
			transferLaunchTv.setVisibility(View.INVISIBLE);
			transferRetryTv.setVisibility(View.VISIBLE);
			transferInstallTv.setVisibility(View.INVISIBLE);
			transferOpenTv.setVisibility(View.INVISIBLE);
			transferSettingTv.setVisibility(View.INVISIBLE);
			fileTransferSattus.setText(context.getResources().getString(R.string.download_status_paused));
			break;
		default:
			break;
		}
	}

	public String refreshDuration(int cur, int dur, Context con) {
		String mdur = Utils.millisTimeToDotFormat(dur);
		String mcur = Utils.millisTimeToDotFormat(cur);
		String bg = con.getResources().getString(R.color.music_dur_bg);
		String from = "<font color=\"" + bg + "\">" + mcur + "</font>" + "/" + mdur;
		return from;
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @param @param context    
	* @return void    
	*/
	public void setSkinTheme(Context context) {
		SkinConfigManager.getInstance().setProgressDrawable(context, progressBar, SkinConstan.DOWNLOAD_PROGRESS_BG);
		SkinConfigManager.getInstance().setTextViewDrawableTop(context, transferSettingTv, SkinConstan.SETTINGS_BTN);
		SkinConfigManager.getInstance().setTextViewDrawableTop(context, transferCancleTv, SkinConstan.DELETE_BTN);
		SkinConfigManager.getInstance().setTextViewDrawableTop(context, transferRetryTv, SkinConstan.DOWNLOAD_BTN);
	}
}
