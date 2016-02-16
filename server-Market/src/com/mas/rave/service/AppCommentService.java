package com.mas.rave.service;

import java.util.HashMap;
import java.util.List;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.AppComment;

/**
 * app评论信息数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface AppCommentService {

	public PaginationVo<AppComment> searchAppComment(HashMap<String, Object> map, int currentPage, int pageSize);

	// app评论信息
	public List<AppComment> getAppComment();

	// 查看单个app评论信息
	public AppComment getAppComment(int id);

	// 增加app评论
	public void addAppComment(AppComment res);

	// 更新app评论
	public void upAppComment(AppComment res);

	// 删除app评论信息
	public void delAppComment(int id);

	void batchDelete(Integer[] ids);

}
