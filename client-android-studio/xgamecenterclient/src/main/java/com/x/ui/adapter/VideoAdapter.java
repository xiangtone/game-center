package com.x.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.db.resource.NativeResourceConstant;
import com.x.publics.model.FileBean;

/**
 * 视频 Adapter
 
 *
 */
public class VideoAdapter extends BaseAdapter {

	private LayoutInflater mFactory;
	private Context context;
	private GridView gridView;
	private VideoHolder holder;
	private ArrayList<FileBean> videoList = null;
	private int mode = NativeResourceConstant.NORMAL; // 0=正常，1=编辑，2=全选，3=反选

	public VideoAdapter(Context context, ArrayList<FileBean> videoList) {
		this.mFactory = LayoutInflater.from(context);
		this.context = context;
		this.videoList = videoList;
	}

	public void setGridView(GridView gridView) {
		this.gridView = gridView;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getMode() {
		return this.mode;
	}

	public void setAllList(ArrayList<FileBean> list) {
		this.videoList = list;
		notifyDataSetChanged();
	}

	public ArrayList<FileBean> getAllFiles() {
		return videoList;
	}

	@Override
	public int getCount() {
		return videoList.size();
	}

	@Override
	public Object getItem(int position) {
		return videoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mFactory.inflate(R.layout.fragment_video_item, null);
			holder = new VideoHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (VideoHolder) convertView.getTag();
		}
		FileBean fileBean = videoList.get(position);
		switch (mode) {
		case NativeResourceConstant.NORMAL:
			fileBean.setStatus(0);
			fileBean.setIscheck(0); //正常模式下恢复
			break;
		case NativeResourceConstant.EDIT:
		case NativeResourceConstant.ALL:
		case NativeResourceConstant.NOALL:
			fileBean.setStatus(1);
			break;
		}
		holder.initData(context, fileBean, gridView);

		// set skin theme
		holder.setSkinTheme(context);
		setSkinTheme(convertView);

		return convertView;
	}

	private void setSkinTheme(View convertView) {
		SkinConfigManager.getInstance().setViewBackground(context, convertView, SkinConstan.LIST_VIEW_ITEM_BG);
	}
}
