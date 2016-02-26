package com.x.ui.adapter;

import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.x.R;
import com.x.db.resource.NativeResourceConstant;
import com.x.publics.model.FileBean;

/**
 * 图片 Adapter
 
 *
 */
public class PictureAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<FileBean> list;
	private PictureHolder pictrueHolder;
	private Context context;
	private GridView mGridView;
	private int mode = NativeResourceConstant.NORMAL; //0=正常，1=编辑，2=全选，3=反选

	public PictureAdapter(Context context, List<FileBean> list, GridView mGridView) {
		mInflater = LayoutInflater.from(context);
		this.list = list;
		this.mGridView = mGridView;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getMode() {
		return this.mode;
	}

	public void setAllList(List<FileBean> list) {
		this.list = list;
	}

	public Collection<FileBean> getAllFiles() {
		return list;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.activity_picture_view, null);
			pictrueHolder = new PictureHolder(convertView);
			convertView.setTag(pictrueHolder);
		} else {
			pictrueHolder = (PictureHolder) convertView.getTag();
		}
		FileBean pictureBean = list.get(position);
		switch (mode) {
		case NativeResourceConstant.NORMAL:
			pictureBean.setStatus(0);
			pictureBean.setIscheck(0); //正常模式下恢复
			break;
		case NativeResourceConstant.EDIT:
		case NativeResourceConstant.ALL:
		case NativeResourceConstant.NOALL:
			pictureBean.setStatus(1);
			break;
		}
		pictrueHolder.initData(context, pictureBean, mGridView);
		pictrueHolder.setSkinTheme(context);
		return convertView;
	}
}
