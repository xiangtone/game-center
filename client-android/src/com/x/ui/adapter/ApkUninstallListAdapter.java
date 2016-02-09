/**   
* @Title: ApkListAdapter.java
* @Package com.mas.amineappstore.adapter
* @Description: TODO 

* @date 2014-2-15 上午11:12:06
* @version V1.0   
*/

package com.x.ui.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.download.BroadcastManager;
import com.x.publics.model.ApkInfoBean;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.Utils;

/**
* @ClassName: ApkListAdapter
* @Description: TODO 

* @date 2014-2-15 上午11:12:06
* 
*/

public class ApkUninstallListAdapter extends ArrayListBaseAdapter<ApkInfoBean> {

	private Dialog mDialog;

	public ApkUninstallListAdapter(Activity context) {
		super(context);
	}

	public class ViewHolder {
		public View rootView;
		public ImageView iconIv, arrowIv;
		public TextView appNameTv, appSizeTv, appVersionNameTv, apkDeleteTv;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		final ApkInfoBean apkInfoBean = mList.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.uninstall_apk_item, null);
			viewHolder = new ViewHolder();
			viewHolder.rootView = convertView.findViewById(R.id.uai_top_rl);
			viewHolder.iconIv = (ImageView) convertView.findViewById(R.id.uai_app_icon_iv);
			viewHolder.arrowIv = (ImageView) convertView.findViewById(R.id.uai_arrow_iv);
			viewHolder.appNameTv = (TextView) convertView.findViewById(R.id.uai_app_name_tv);
			viewHolder.appSizeTv = (TextView) convertView.findViewById(R.id.uai_app_size_tv);
			viewHolder.appVersionNameTv = (TextView) convertView.findViewById(R.id.uai_app_version_tv);
			viewHolder.apkDeleteTv = (TextView) convertView.findViewById(R.id.uai_apk_delete_tv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.arrowIv.setBackgroundResource(R.drawable.ic_download_manager_arrow_down);
		NetworkImageUtils.load(context, ImageType.APK, apkInfoBean.getApkPath(), R.drawable.ic_screen_default_picture,
				R.drawable.ic_screen_default_picture, viewHolder.iconIv);
		viewHolder.appNameTv.setText(apkInfoBean.getAppName());
		viewHolder.appSizeTv.setText(apkInfoBean.getFileSize() == 0 ? "" : ResourceUtil.getString(context,
				R.string.app_size) + Utils.sizeFormat(apkInfoBean.getFileSize()));
		viewHolder.appVersionNameTv.setText(ResourceUtil.getString(context, R.string.app_version)
				+ apkInfoBean.getVersionName());
		viewHolder.apkDeleteTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

						Intent i = new Intent(MyIntents.INTENT_APKFILE_DELETE_UPDATE_UI);
						int value = apkInfoBean.isInstalled() ? MyIntents.EXTRA_TYPE_INSTALL
								: MyIntents.EXTRA_TYPE_UNINSTALL;
						i.putExtra(MyIntents.INTENT_DELETE_EXTRA_TYPE, value);
						ArrayList<String> paths = new ArrayList<String>();
						paths.add(apkInfoBean.getApkPath());
						i.putStringArrayListExtra(MyIntents.INTENT_DELETE_EXTRA_PATH, paths);
						BroadcastManager.sendBroadcast(i);

						Utils.deleteFile(apkInfoBean.getApkPath());
					}
				};

				DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};
				if (mDialog != null) {
					boolean boo = mDialog.isShowing();
					if (boo == true)
						return;
				}
				mDialog = Utils.showDialog(context, ResourceUtil.getString(context, R.string.warm_tips),
						ResourceUtil.getString(context, R.string.dialog_delete_prompt_one, "" + 1),
						ResourceUtil.getString(context, R.string.confirm), positiveListener,
						ResourceUtil.getString(context, R.string.cancel), negativeListener);

			}
		});

		setSkinTheme(viewHolder.rootView, viewHolder.apkDeleteTv);// set skin theme

		return convertView;
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @param     
	* @return void    
	*/
	private void setSkinTheme(View view, TextView textView) {
		SkinConfigManager.getInstance().setViewBackground(context, view, SkinConstan.LIST_VIEW_ITEM_BG);
		SkinConfigManager.getInstance().setTextViewDrawableTop(context, textView, SkinConstan.UNINSTALL_BTN);
	}

}
