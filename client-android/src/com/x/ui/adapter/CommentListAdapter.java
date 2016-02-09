package com.x.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.x.R;
import com.x.publics.model.CommentBean;

/**
 * @ClassName: commentListAdapter
 * @Desciption: 评论listView适配器
 
 * @Date: 2014-1-15 下午4:07:59
 */

public class CommentListAdapter extends ArrayListBaseAdapter<CommentBean> {

	// constructor
	public CommentListAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = inflater.inflate(R.layout.comment_listview_item, null);

		// initialize CommentBean Object to used
		CommentBean commentBean = mList.get(position);
		CommentViewHolder holder = new CommentViewHolder(convertView);
		holder.initData(commentBean, context);

		return convertView;
	}

}
