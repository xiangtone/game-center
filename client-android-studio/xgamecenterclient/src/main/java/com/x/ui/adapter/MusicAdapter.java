package com.x.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.model.FileBean;

/**
 * Music Adapter
 
 *
 */
public class MusicAdapter extends BaseAdapter {

	private LayoutInflater mFactory;
	private Context context;
	private OnClickListener listener;
	private MusicHolder holder;
	private ArrayList<FileBean> musicList = null;
	public static final int NORMAL = 0;
	public static final int EDIT = 1;
	public static final int ALL = 2;
	public static final int NOALL = 3;
	private int mode = NORMAL; //0=正常，1=编辑，2=全选，3=反选

	public MusicAdapter(Context context, ArrayList<FileBean> musicList, OnClickListener listener) {
		this.context = context;
		this.mFactory = LayoutInflater.from(context);
		this.musicList = musicList;
		this.listener = listener;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getMode() {
		return this.mode;
	}

	public void setAllList(ArrayList<FileBean> list) {
		this.musicList = list;
		notifyDataSetChanged();
	}

	public ArrayList<FileBean> getAllFiles() {
		return musicList;
	}

	@Override
	public int getCount() {
		return musicList.size();
	}

	@Override
	public Object getItem(int position) {
		return musicList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mFactory.inflate(R.layout.fragment_music_item, null);
			holder = new MusicHolder(convertView, listener);
			convertView.setTag(holder);
		} else {
			holder = (MusicHolder) convertView.getTag();
		}
		RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.mus_layout);
		FileBean fileBean = musicList.get(position);
		layout.setTag(fileBean.getFilePath());
		switch (mode) {
		case NORMAL:
			fileBean.setStatus(0);
			fileBean.setIscheck(0); //正常模式下恢复
			break;
		case EDIT:
		case ALL:
		case NOALL:
			fileBean.setStatus(1);
			break;
		}
		holder.initData(context, fileBean);

		// set skin theme
		holder.setSkinTheme(context);
		setSkinTheme(convertView);

		return convertView;
	}

	private void setSkinTheme(View convertView) {
		SkinConfigManager.getInstance().setViewBackground(context, convertView, SkinConstan.LIST_VIEW_ITEM_BG);
	}
}
