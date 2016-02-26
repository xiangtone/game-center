package com.x.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.x.R;
import com.x.publics.model.CommentBean;

import java.io.Serializable;

/**
 * @ClassName: CommentViewHolder
 * @Desciption: CommentListAdapter 的 ViewHolder
 
 * @Date: 2014-1-15 下午3:45:46
 */

public class CommentViewHolder implements Serializable {

	private static final long serialVersionUID = 1L;
	private TextView deviceModel; // 设备机型
	private TextView deviceVendor; // 设备厂商
	private TextView commentDate; // 评论时间
	private TextView commenterName; // 评论人名称
	private TextView commentContent; // 评论内容
	private RatingBar commentGrade; // 评论星级

	/* the method for initialize components */
	public CommentViewHolder(View view) {
		if (view != null) {
			deviceModel = (TextView) view.findViewById(R.id.tv_device_model);
			deviceVendor = (TextView) view.findViewById(R.id.tv_device_vendor);
			commentDate = (TextView) view.findViewById(R.id.tv_comment_date);
			commenterName = (TextView) view.findViewById(R.id.tv_commenter_name);
			commentContent = (TextView) view.findViewById(R.id.tv_comment_content);
			commentGrade = (RatingBar) view.findViewById(R.id.rb_comment_stars);
		}
	}

	/* the method for setter Data to components */
	public void initData(CommentBean commentBean, Context context) {
		deviceModel.setText(commentBean.getDeviceModel());
		deviceVendor.setText(commentBean.getDeviceVendor());
		commentDate.setText(commentBean.getPublishTime());
		commenterName.setText(commentBean.getUserName());
		commentContent.setText(commentBean.getContent());
		commentGrade.setRating(commentBean.getStars());
	}
}
