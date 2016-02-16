package com.x.ui.adapter;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
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
import com.x.db.resource.NativeResourceConstant.FileType;
import com.x.db.zerodata.TransferHistory;
import com.x.publics.model.FileBean;
import com.x.publics.utils.IntentUtil;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.PackageUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.resource.PhotoActivity;

public class ZeroDataTransferHistoryViewHoler implements Serializable {
	private static final long serialVersionUID = 2L;

	public LinearLayout catalog_ll, fileOfOperation_ll;
	public ImageView fileIconIv, fileOfOperationIv;
	public TextView flieNameTv, fileSizeTv, fileOfOperationTv, catalogTv;

	//Music
	public ImageView disk, play_but, disk_pic = null;
	public ProgressBar loading = null;
	public RelativeLayout musicLayout = null;
	public TextView duration = null;
	public View rootView;

	public Context context;

	public ZeroDataTransferHistoryViewHoler(View view) {
		if (view != null) {
			rootView = view.findViewById(R.id.zdth_top_rl);
			catalog_ll = (LinearLayout) view.findViewById(R.id.zdth_catalog_ll);
			fileOfOperation_ll = (LinearLayout) view.findViewById(R.id.zdth_fileOfOperationll);
			fileOfOperationIv = (ImageView) view.findViewById(R.id.zdth_fileOfOperationIv);
			fileIconIv = (ImageView) view.findViewById(R.id.zdth_file_icon_iv);

			catalogTv = (TextView) view.findViewById(R.id.zdth_catalog_tv);
			flieNameTv = (TextView) view.findViewById(R.id.zdth_file_name_tv);
			fileSizeTv = (TextView) view.findViewById(R.id.zdth_fileSizeTv);
			fileOfOperationTv = (TextView) view.findViewById(R.id.zdth_fileOfOperationTv);

			//Music  item左侧
			disk_pic = (ImageView) view.findViewById(R.id.zdth_disk_pic);
			disk = (ImageView) view.findViewById(R.id.zdth_disk);
			loading = (ProgressBar) view.findViewById(R.id.zdth_load);
			play_but = (ImageView) view.findViewById(R.id.zdth_play);
			musicLayout = (RelativeLayout) view.findViewById(R.id.zdth_disk_bg);
			duration = (TextView) view.findViewById(R.id.zdth_ring_time);
		}
	}

	public void initData(TransferHistory transferHistory, Context context) {
		this.context = context;

		int type = transferHistory.getFileType();
		switch (type) {
		case FileType.MUSIC:
			fileIconIv.setVisibility(View.GONE);
			musicLayout.setVisibility(View.VISIBLE);
			// set skin theme
			SkinConfigManager.getInstance().setViewBackground(context, fileOfOperationIv, SkinConstan.SETTINGS_BTN);

			//fileOfOperationIv.setImageDrawable(context.getResources().getDrawable(R.drawable.tranfer_history_settings));
			fileOfOperationTv.setText(context.getResources().getString(R.string.action_settings));//setting

			play_but.setImageResource(R.drawable.ringtones_play);
			disk.clearAnimation();
			disk_pic.clearAnimation();
			loading.setVisibility(View.GONE);
			disk_pic.setImageResource(R.drawable.ringtones_disk_pic);
			duration.setText("");
			break;
		case FileType.TXT:
			fileIconIv.setVisibility(View.VISIBLE);
			musicLayout.setVisibility(View.GONE);
			fileIconIv.setImageResource(R.drawable.ic_resource_txt);

			Utils.setBackgroundDrawable(fileOfOperationIv,
					context.getResources().getDrawable(R.drawable.selector_launch_btn));
			fileOfOperationTv.setText(context.getResources().getString(R.string.app_status_open));//open

			break;
		case FileType.DOC:
			fileIconIv.setVisibility(View.VISIBLE);
			musicLayout.setVisibility(View.GONE);
			fileIconIv.setImageResource(R.drawable.ic_resource_doc);

			Utils.setBackgroundDrawable(fileOfOperationIv,
					context.getResources().getDrawable(R.drawable.selector_launch_btn));
			fileOfOperationTv.setText(context.getResources().getString(R.string.app_status_open));//open

			break;
		case FileType.XLS:
			fileIconIv.setVisibility(View.VISIBLE);
			musicLayout.setVisibility(View.GONE);
			fileIconIv.setImageResource(R.drawable.ic_resource_xls);

			Utils.setBackgroundDrawable(fileOfOperationIv,
					context.getResources().getDrawable(R.drawable.selector_launch_btn));
			fileOfOperationTv.setText(context.getResources().getString(R.string.app_status_open));//open

			break;
		case FileType.PDF:
			fileIconIv.setVisibility(View.VISIBLE);
			musicLayout.setVisibility(View.GONE);
			fileIconIv.setImageResource(R.drawable.ic_resource_pdf);

			Utils.setBackgroundDrawable(fileOfOperationIv,
					context.getResources().getDrawable(R.drawable.selector_launch_btn));
			fileOfOperationTv.setText(context.getResources().getString(R.string.app_status_open));//open

			break;
		case FileType.PPT:
			fileIconIv.setVisibility(View.VISIBLE);
			musicLayout.setVisibility(View.GONE);
			fileIconIv.setImageResource(R.drawable.ic_resource_ppt);

			Utils.setBackgroundDrawable(fileOfOperationIv,
					context.getResources().getDrawable(R.drawable.selector_launch_btn));
			fileOfOperationTv.setText(context.getResources().getString(R.string.app_status_open));//open

			break;
		case FileType.VIDEO:
			fileIconIv.setVisibility(View.VISIBLE);
			musicLayout.setVisibility(View.GONE);
			fileIconIv.setImageResource(R.drawable.file_video);

			Utils.setBackgroundDrawable(fileOfOperationIv,
					context.getResources().getDrawable(R.drawable.selector_launch_btn));
			fileOfOperationTv.setText(context.getResources().getString(R.string.app_status_open));//open

			break;
		case FileType.PICTURE:
			fileIconIv.setVisibility(View.VISIBLE);
			musicLayout.setVisibility(View.GONE);
			NetworkImageUtils.load(context, ImageType.LOCALPIC, transferHistory.getFileSavePath(),
					R.drawable.ic_screen_default_picture, R.drawable.ic_screen_default_picture, fileIconIv);

			Utils.setBackgroundDrawable(fileOfOperationIv,
					context.getResources().getDrawable(R.drawable.selector_launch_btn));
			fileOfOperationTv.setText(context.getResources().getString(R.string.app_status_open));//open

			break;
		case FileType.APK:
			fileIconIv.setVisibility(View.VISIBLE);
			musicLayout.setVisibility(View.GONE);
			NetworkImageUtils.load(context, ImageType.APK, transferHistory.getFileSavePath(), R.drawable.ic_apk_50,
					R.drawable.ic_apk_50, fileIconIv);

			Utils.setBackgroundDrawable(fileOfOperationIv,
					context.getResources().getDrawable(R.drawable.selector_install_btn));
			fileOfOperationTv.setText(context.getResources().getString(R.string.app_status_install));//install

			break;
		case FileType.UNKNOWN:
			fileIconIv.setVisibility(View.VISIBLE);
			musicLayout.setVisibility(View.GONE);
			fileIconIv.setImageResource(R.drawable.ic_resource_unknown);

			Utils.setBackgroundDrawable(fileOfOperationIv,
					context.getResources().getDrawable(R.drawable.selector_launch_btn));
			fileOfOperationTv.setText(context.getResources().getString(R.string.app_status_open));//open

			break;
		}
		flieNameTv.setText(transferHistory.getNameSort());
		fileSizeTv.setText(Utils.sizeFormat(transferHistory.getFileSize()));
		if (true == transferHistory.isFinishTimeStatus()) {
			catalog_ll.setVisibility(View.VISIBLE);
			catalogTv.setText(context.getResources().getString(R.string.receive_recode_successfulreceived)
					+ transferHistory.getFinishTime());
		} else {
			catalog_ll.setVisibility(View.GONE);
		}
	}

	/**
	 * item右侧，根据文件的后缀显示对应的图片和文字
	 */
	private void AccordingToTheDataShow(ImageView fileOfOperationIv, TextView fileOfOperationTv, String fileSuffix) {

		if (fileSuffix.equals("apk")) {
			Utils.setBackgroundDrawable(fileOfOperationIv,
					context.getResources().getDrawable(R.drawable.selector_install_btn));
			fileOfOperationTv.setText(context.getResources().getString(R.string.app_status_install));//install
		} else if (fileSuffix.equals("mp3") || fileSuffix.equals("ogg")) {
			Utils.setBackgroundDrawable(fileOfOperationIv,
					context.getResources().getDrawable(R.drawable.selector_settings_btn));
			fileOfOperationTv.setText(context.getResources().getString(R.string.action_settings));//setting
		} else if (fileSuffix.equals("txt") || fileSuffix.equals("ppt") || fileSuffix.equals("xls")
				|| fileSuffix.equals("doc") || fileSuffix.equals("pdf") || fileSuffix.equals("chm")
				|| fileSuffix.equals("html") || fileSuffix.equals("jpg") || fileSuffix.equals("gif")
				|| fileSuffix.equals("png") || fileSuffix.equals("jpeg") || fileSuffix.equals("bmp")
				|| fileSuffix.equals("3gp") || fileSuffix.equals("mp4") || fileSuffix.equals("gif")) {
			Utils.setBackgroundDrawable(fileOfOperationIv,
					context.getResources().getDrawable(R.drawable.selector_launch_btn));
			fileOfOperationTv.setText(context.getResources().getString(R.string.app_status_launch));//open
		}
	}

	/**
	 * 对Item右侧的操作 open 、install、settings
	 */
	public void operationOfTheFile(TransferHistory transferHistory) {
		int type = transferHistory.getFileType();

		if (type == FileType.APK) {
			String fullPath = transferHistory.getFileSavePath();
			PackageUtil.normalInstall(context, fullPath);
		} else if (type == FileType.MUSIC) {
			Utils.showRingtonesSettingDialog(context, transferHistory.getFileSavePath());
		} else if (type == FileType.PICTURE) {
			FileBean fileBean = new FileBean();
			fileBean.setFilePath(transferHistory.getFileSavePath());
			ArrayList<FileBean> list = new ArrayList<FileBean>();
			list.add(fileBean);
			Intent intent = new Intent(context, PhotoActivity.class);
			intent.putParcelableArrayListExtra("data", list);
			intent.putExtra("Item_id", 0);
			context.startActivity(intent);
		} else {
			IntentUtil.openFile(context, transferHistory.getFileSavePath());
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
	* @param convertView 
	 * @Title: setSkinTheme 
	* @Description: TODO 
	* @param @param context    
	* @return void    
	*/
	public void setSkinTheme(Context context) {
		SkinConfigManager.getInstance().setViewBackground(context, rootView, SkinConstan.LIST_VIEW_ITEM_BG);
	}
}
