package com.x.ui.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.model.FileBean;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.Utils;

/**
 * 分享 > 本地APPS
 
 *
 */
public class LocalAppsShareAdapter extends BaseAdapter {

	public static final int ALL = 2; //全选
	public static final int NOALL = 3; //反选
	private LayoutInflater mInflater;
	private ArrayList<FileBean> list;
	private Activity context;

	public LocalAppsShareAdapter(Activity context, ArrayList<FileBean> list) {
		mInflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
	}

	public void setList(ArrayList<FileBean> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public final static class ViewHolder {
		public ImageView appIcon;
		public TextView appName, appSize;
		public CheckBox checkBox;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		final FileBean fileBean = list.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.fragment_resource_apps_item, null);
			viewHolder.appIcon = (ImageView) convertView.findViewById(R.id.apps_icon);
			viewHolder.appName = (TextView) convertView.findViewById(R.id.apps_title);
			viewHolder.appSize = (TextView) convertView.findViewById(R.id.apps_size);
			viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.apps_check);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		NetworkImageUtils.load(context, ImageType.APP, fileBean.getIcon(), R.drawable.ic_screen_default_picture,
				R.drawable.ic_screen_default_picture, viewHolder.appIcon);
		viewHolder.appName.setText(fileBean.getFileName());
		viewHolder.appSize.setText(fileBean.getFileSize() == 0 ? "" : Utils.sizeFormat(fileBean.getFileSize()));
		if (fileBean.getIscheck() == 1)
			viewHolder.checkBox.setChecked(true);
		else
			viewHolder.checkBox.setChecked(false);

		setSkinTheme(viewHolder.checkBox);//set skin theme

		return convertView;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @param @param context2    
	* @return void    
	*/
	private void setSkinTheme(CheckBox checkBox) {
		SkinConfigManager.getInstance().setCheckBoxBtnDrawable(context, checkBox, SkinConstan.OPTION_BTN);
	}

}
