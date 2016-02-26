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
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.db.resource.NativeResourceConstant;
import com.x.publics.model.AlbumBean;

/**
 * 图片相册 分类 Adapter
 
 *
 */
public class PictureFolderAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<AlbumBean> list;
	private PictureFolderHolder pictrueHolder;
	private Context context;
	private GridView mGridView;
	private int mode = NativeResourceConstant.NORMAL; //0=正常，1=编辑，2=全选，3=反选

	public PictureFolderAdapter(Context context, List<AlbumBean> list, GridView mGridView) {
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

	public void setAllList(List<AlbumBean> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public Collection<AlbumBean> getAllFiles() {
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
			convertView = mInflater.inflate(R.layout.fragment_pictureview, null);
			pictrueHolder = new PictureFolderHolder(convertView);
			convertView.setTag(pictrueHolder);
		} else {
			pictrueHolder = (PictureFolderHolder) convertView.getTag();
		}

		AlbumBean albumBean = list.get(position);
		switch (mode) {
		case NativeResourceConstant.NORMAL:
			albumBean.setStatus(0);
			albumBean.setIscheck(0); //正常模式下必须没有选中
			break;
		case NativeResourceConstant.EDIT:
		case NativeResourceConstant.ALL:
		case NativeResourceConstant.NOALL:
			albumBean.setStatus(1);
			break;
		}
		pictrueHolder.initData(context, albumBean, mGridView);

		// set skin theme
		pictrueHolder.setSkinTheme(context);
		setSkinTheme(convertView);

		return convertView;
	}

	private void setSkinTheme(View convertView) {
		SkinConfigManager.getInstance().setViewBackground(context, convertView, SkinConstan.LIST_VIEW_ITEM_BG);
	}
}
