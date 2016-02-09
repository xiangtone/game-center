package com.x.ui.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.x.R;
import com.x.business.statistic.DataEyeManager;
import com.x.business.wallpaper.WallpaperManage;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.DownloadProcesser;
import com.x.publics.model.ThemeBean;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.wallpaper.WallpaperAlbumActivity;

/**
 * @ClassName: WallpaperAlbumListAdapter
 * @Desciption: 壁纸专辑适配器 
 
 * @Date: 2014-3-14 下午4:00:03
 */

public class WallpaperAlbumListAdapter extends ArrayListBaseAdapter<ThemeBean> {

	// constructor
	public WallpaperAlbumListAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = inflater.inflate(R.layout.wallpaper_album_item, null);

		// initialize CommentBean Object to used
		final ThemeBean themeBean = mList.get(position);
		final WallpaperAlbumViewHolder holder = new WallpaperAlbumViewHolder(convertView); //初始化item布局源
		holder.initData(themeBean, context);
		holder.setSkinTheme(context);

		holder.oneClickNormalBtn.setOnClickListener(new MyListener(themeBean, holder));

		// 按钮触摸事件
		holder.albumView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					holder.logo.setAlpha(255);
					// check network
					if (!NetworkUtils.isNetworkAvailable(context)) {
						ToastUtil.show(context, context.getResources().getString(R.string.network_canot_work),
								Toast.LENGTH_SHORT);
						return true;
					}
					Intent intent = new Intent(context, WallpaperAlbumActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("themeBean", themeBean);
					intent.putExtras(bundle);
					context.startActivity(intent);
					break;

				case MotionEvent.ACTION_MOVE:
					// imageView.setAlpha(255);
					break;

				case MotionEvent.ACTION_DOWN:
					holder.logo.setAlpha(180);
					break;

				case MotionEvent.ACTION_CANCEL:
					holder.logo.setAlpha(255);
					break;

				default:
					break;
				}

				return true;
			}
		});

		return convertView;
	}

	private class MyListener implements OnClickListener {

		private ThemeBean themeBean;
		private WallpaperAlbumViewHolder viewHolder;

		public MyListener(ThemeBean themeBean, WallpaperAlbumViewHolder viewHolder) {
			this.themeBean = themeBean;
			this.viewHolder = viewHolder;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_one_click_download_normal:
				// 网络检测
				if (!NetworkUtils.isNetworkAvailable(context)) {
					ToastUtil.show(context, context.getResources().getString(R.string.network_canot_work),
							Toast.LENGTH_SHORT);
					return;
				}

				// 是否存在SD卡检测
				if (!StorageUtils.isSDCardPresent()) {
					ToastUtil.show(context, context.getResources().getString(R.string.sdcard_not_found),
							Toast.LENGTH_SHORT);
					return;
				}

				// 一键下载前，当前下载数量判断
				int downloadNum = DownloadEntityManager.getInstance().getAllUnfinishedDownloadCount();
				if (downloadNum >= DownloadProcesser.MAX_TASK_COUNT) {
					ToastUtil.show(context, ResourceUtil.getString(context, R.string.download_too_many_downloads),
							ToastUtil.LENGTH_SHORT);
					return;
				}

				if (!NetworkUtils.getNetworkInfo(context).equals(NetworkUtils.NETWORK_TYPE_WIFI)
						&& Utils.getSettingModel(context).isGprsDownloadPromt()) {
					DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							DataEyeManager.getInstance().source(themeBean.getThemeId(), 0, null, 0L, null, null, false);
							WallpaperManage.getInstance(context).downloadAll(themeBean.getThemeId(), mHandler);
						}
					};

					DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

						}
					};
					Utils.showDialog(context, ResourceUtil.getString(context, R.string.warm_tips),
							ResourceUtil.getString(context, R.string.dialog_download_prompt),
							ResourceUtil.getString(context, R.string.dialog_download_prompt_download),
							positiveListener, ResourceUtil.getString(context, R.string.dialog_download_prompt_cancle),
							negativeListener);
				} else {
					WallpaperManage.getInstance(context).downloadAll(themeBean.getThemeId(), mHandler);
				}

				break;
			}
		}

		/**
		 * 更新UI界面，Handler
		 */
		private Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					SharedPrefsUtil.putThemeValue(context, "theme_" + themeBean.getThemeId(), false); // 记录一键下载的主题
					viewHolder.oneClickNormalBtn.setVisibility(View.VISIBLE);
					viewHolder.oneClickPressedBtn.setVisibility(View.GONE);
					break;

				case 1:
					SharedPrefsUtil.putThemeValue(context, "theme_" + themeBean.getThemeId(), true); // 记录一键下载的主题
					viewHolder.oneClickNormalBtn.setVisibility(View.GONE);
					viewHolder.oneClickPressedBtn.setVisibility(View.VISIBLE);
					break;
				}
			};
		};
	}
}
