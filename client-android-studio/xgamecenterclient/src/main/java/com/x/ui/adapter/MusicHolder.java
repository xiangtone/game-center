package com.x.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.resource.ResourceManage;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.model.FileBean;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.Utils;

/**
 * 
* @ClassName: MusicHolder
* @Description: 本地音乐 Adapter Holder

* @date 2014-4-9 下午5:13:39
*
 */
public class MusicHolder {
	public ImageView disk, play_but, disk_pic = null;
	public ProgressBar loading = null;
	public TextView name, size, duration = null;
	public CheckBox checkBox = null;
	private Button button = null;

	public MusicHolder(View view, OnClickListener listener) {
		if (view != null && listener != null) {
			disk_pic = (ImageView) view.findViewById(R.id.mus_disk_pic);
			disk = (ImageView) view.findViewById(R.id.mus_disk);
			loading = (ProgressBar) view.findViewById(R.id.mus_load);
			play_but = (ImageView) view.findViewById(R.id.mus_play);
			name = (TextView) view.findViewById(R.id.mus_name);
			size = (TextView) view.findViewById(R.id.mus_size);
			duration = (TextView) view.findViewById(R.id.mus_duration);
			checkBox = (CheckBox) view.findViewById(R.id.mus_check);
			button = (Button) view.findViewById(R.id.mus_setting);
			button.setOnClickListener(listener);
		}
	}

	public void initData(Context con, FileBean bean) {
		if (bean.getStatus() == 1) {
			checkBox.setVisibility(View.VISIBLE);
			button.setVisibility(View.INVISIBLE);
		} else {
			checkBox.setVisibility(View.GONE);
			button.setVisibility(View.VISIBLE);
		}
		button.setTag(bean.getFilePath());
		name.setText(bean.getFileName());
		size.setText(Utils.sizeFormat(bean.getFileSize()));
		duration.setText(Utils.millisTimeToDotFormat(bean.getDuration()));
		checkBox.setChecked(bean.getIscheck() == 1 ? true : false);
		play_but.setImageResource(R.drawable.ringtones_play);
		disk.clearAnimation();
		disk_pic.clearAnimation();
		loading.setVisibility(View.GONE);
		disk_pic.setImageResource(R.drawable.ringtones_disk_pic);

		//获取封面
		String path = ResourceManage.getInstance(con).queryMusicAlbum(bean.getAlbumID());
		if (path != null) {
			NetworkImageUtils.load(con, ImageType.LOCALPIC, path, R.drawable.ringtones_disk_pic,
					R.drawable.ringtones_disk_pic, disk_pic);
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
		String mdur = Utils.millisTimeToDotFormat(dur);
		return mdur;
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @param @param context    
	* @return void    
	*/
	public void setSkinTheme(Context context) {
		SkinConfigManager.getInstance().setTextViewDrawableTop(context, button, SkinConstan.SETTINGS_BTN);
		SkinConfigManager.getInstance().setCheckBoxBtnDrawable(context, checkBox, SkinConstan.OPTION_BTN);
	}

}
