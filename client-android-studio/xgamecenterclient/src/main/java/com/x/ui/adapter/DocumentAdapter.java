package com.x.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.db.resource.NativeResourceConstant;
import com.x.publics.model.FileBean;

/**
 * 文档Adapter
 * 
 
 * 
 */
public class DocumentAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mFactory;
	private DocumentHolder holder;
	private int mode = NativeResourceConstant.NORMAL; // 0=正常，1=编辑，2=全选，3=反选
	private ArrayList<FileBean> documentList = null;

	public DocumentAdapter(Context context, ArrayList<FileBean> documentList) {
		mFactory = LayoutInflater.from(context);
		this.documentList = documentList;
		this.context = context;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getMode() {
		return this.mode;
	}

	public void setAllList(ArrayList<FileBean> list) {
		this.documentList = list;
		notifyDataSetChanged();
	}

	public ArrayList<FileBean> getAllFiles() {
		return documentList;
	}

	@Override
	public int getCount() {
		return documentList.size();
	}

	@Override
	public Object getItem(int position) {
		return documentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mFactory.inflate(R.layout.fragment_documents_item, null);
			holder = new DocumentHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (DocumentHolder) convertView.getTag();
		}
		FileBean fileBean = documentList.get(position);
		switch (mode) {
		case NativeResourceConstant.NORMAL:
			fileBean.setStatus(0);
			fileBean.setIscheck(0); // 正常模式下恢复
			break;
		case NativeResourceConstant.EDIT:
		case NativeResourceConstant.ALL:
		case NativeResourceConstant.NOALL:
			fileBean.setStatus(1);
			break;
		}
		holder.initData(fileBean);

		// set skin theme
		holder.setSkinTheme(context);
		setSkinTheme(convertView);

		return convertView;
	}

	private void setSkinTheme(View convertView) {
		SkinConfigManager.getInstance().setViewBackground(context, convertView, SkinConstan.LIST_VIEW_ITEM_BG);
	}
}
