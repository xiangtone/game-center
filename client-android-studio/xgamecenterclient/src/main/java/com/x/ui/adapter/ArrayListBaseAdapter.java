package com.x.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
* @ClassName: ArrayListBaseAdapter
* @Description:  

* @date 2014-1-10 上午09:21:23
* 
* @param <T>
*/

public abstract class ArrayListBaseAdapter<T> extends BaseAdapter {
	protected List<T> mList;
	protected Context context;
	protected ListView mListView;
	protected LayoutInflater inflater;

	public ArrayListBaseAdapter(Activity context) {
		this.context = context;
		this.inflater = context.getLayoutInflater();
	}

	@Override
	public int getCount() {
		if (mList != null)
			return mList.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		return mList == null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	abstract public View getView(int position, View convertView, ViewGroup parent);

	public void setList(List<T> list) {
		this.mList = list;
		notifyDataSetChanged();
	}
	
	public void setListNotnotify(List<T> list) {
		this.mList = list;
	}


	public List<T> getList() {
		return mList;
	}

	public void addItem(T t) {
		mList.add(t);
		notifyDataSetChanged();
	}

	public void removeItem(T t) {
		mList.remove(t);
		notifyDataSetChanged();
	}

	public void setList(T[] list) {
		ArrayList<T> arrayList = new ArrayList<T>(list.length);
		for (T t : list) {
			arrayList.add(t);
		}
		setList(arrayList);
	}

	public ListView getListView() {
		return mListView;
	}

	public void setListView(ListView listView) {
		mListView = listView;
	}
	
}
