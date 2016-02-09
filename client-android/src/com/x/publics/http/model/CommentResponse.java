package com.x.publics.http.model;

import com.x.publics.model.CommentBean;

import java.util.ArrayList;

/**
 * @ClassName:CommentResponse
 * @Desciption: 应用评论，数据响应
 
 * @Date: 2014-2-12 下午7:05:40
 */

public class CommentResponse extends CommonResponse {

	public int commentNum; // 评论条数（当前一次请求返回的数量）
	public boolean isLast; // 是否最后一页
	public ArrayList<CommentBean> commentlist; // 评论数据集合

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public ArrayList<CommentBean> getCommentlist() {
		return commentlist;
	}

	public void setCommentlist(ArrayList<CommentBean> commentlist) {
		this.commentlist = commentlist;
	}

}
