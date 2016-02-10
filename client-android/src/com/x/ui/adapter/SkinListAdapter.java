/**   
* @Title: SkinListAdapter.java
* @Package com.x.ui.adapter
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-11-28 下午1:43:16
* @version V1.0   
*/

package com.x.ui.adapter;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.DownloadManager;
import com.x.publics.download.DownloadTask;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.SkinInfoBean;
import com.x.publics.utils.Constan.MediaType;

/**
* @ClassName: SkinListAdapter
* @Description: TODO(换肤界面的适配器)

* @date 2014-11-28 下午1:43:16
* 
*/

public class SkinListAdapter extends ArrayListBaseAdapter<SkinInfoBean> {

	private Handler handler;
	private Activity context;

	public SkinListAdapter(Activity context, Handler handler) {
		super(context);
		this.context = context;
		this.handler = handler;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = inflater.inflate(R.layout.skin_item, null);
		final SkinInfoBean skinBean = mList.get(position);
		SkinViewHolder viewHolder = new SkinViewHolder(convertView);
		convertView.setTag(skinBean.getApkUrl());//全量包url
		viewHolder.initData(context, skinBean, position);

		viewHolder.skinItemLl.setOnClickListener(new MyListener(skinBean, viewHolder, position));
		viewHolder.skinDownloadFra.setOnClickListener(new MyListener(skinBean, viewHolder, position));
		viewHolder.skinPauseFra.setOnClickListener(new MyListener(skinBean, viewHolder, position));
		viewHolder.skinContinueFra.setOnClickListener(new MyListener(skinBean, viewHolder, position));
		viewHolder.settingFra.setOnClickListener(new MyListener(skinBean, viewHolder, position));
		viewHolder.skinUsed.setOnClickListener(new MyListener(skinBean, viewHolder, position));
		return convertView;
	}

	private class MyListener implements OnClickListener {

		private SkinInfoBean skinInfoBean;
		private SkinViewHolder viewHolder;
		private int position;

		public MyListener(SkinInfoBean skinInfoBean, SkinViewHolder viewHolder, int position) {
			this.skinInfoBean = skinInfoBean;
			this.viewHolder = viewHolder;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
					"" + skinInfoBean.getSkinId(), "" + skinInfoBean.getVersionCode());

			switch (v.getId()) {
			case R.id.skin_item_ll://点击Item进行换肤
				changeOfSkin(skinInfoBean, downloadBean, position);
				break;
			case R.id.skin_download_fra:
				addDownload(skinInfoBean);
				break;

			case R.id.skin_pause_fra:
				if (downloadBean == null)
					return;
				DownloadManager.getInstance().pauseDownload(context, downloadBean.getUrl());
				break;

			case R.id.skin_continue_fra:
				if (downloadBean == null)
					return;
				DownloadManager.getInstance().continueDownload(context, downloadBean.getUrl());
				break;

			case R.id.skin_settings_fra://下载完毕，进行设置
				changeOfSkin(skinInfoBean, downloadBean, position);
				break;

			}
		}
	}

	private void addDownload(SkinInfoBean skinInfoBean) {
		DownloadBean downloadBean = new DownloadBean(skinInfoBean.getApkUrl(), skinInfoBean.getSkinName(),
				skinInfoBean.getApkSize(), skinInfoBean.getCurrentBytes(), skinInfoBean.getLogo(), MediaType.THEME,
				skinInfoBean.getSkinId(), skinInfoBean.getVersionName(), skinInfoBean.getPackageName(),
				DownloadTask.TASK_DOWNLOADING, skinInfoBean.getApkSize(), skinInfoBean.getVersionCode(), 0, 0, 0,
				false, skinInfoBean.getApkUrl());
		DownloadManager.getInstance().addDownload(context, downloadBean);
	}

	private void changeStatus(SkinInfoBean skinInfoBean, int position) {

		mList.get(position).setStatus(AppInfoBean.Status.CANLAUNCH);

		for (int i = 0; i < mList.size(); i++) {
			if (mList.get(i).getStatus() == AppInfoBean.Status.CANLAUNCH
					&& mList.get(i).getPackageName() != skinInfoBean.getPackageName()) {
				mList.get(i).setStatus(AppInfoBean.Status.CANINSTALL);
				break;
			}

		}
	}

	private void changeOfSkin(SkinInfoBean skinInfoBean, DownloadBean downloadBean, int position) {
		if (AppInfoBean.Status.CANINSTALL != skinInfoBean.getStatus())
			return;
		SkinConfigManager.getInstance().setSkinParams(context, handler, skinInfoBean.getPackageName(),
				downloadBean == null ? "" : downloadBean.getLocalPath());
		changeStatus(skinInfoBean, position);
		notifyDataSetChanged();
	}

}
