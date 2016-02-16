package com.mas.rave.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.AppCommentMapper;
import com.mas.rave.main.vo.AppComment;
import com.mas.rave.service.AppCommentService;

/**
 * app评论
 * 
 * @author liwei.sz
 * 
 */
@Service
public class AppCommentServiceImpl implements AppCommentService {

	@Autowired
	private AppCommentMapper appCommentMapper;

	public PaginationVo<AppComment> searchAppComment(HashMap<String, Object> map, int currentPage, int pageSize) {
		map.put("currentPage", ((currentPage - 1) * pageSize));
		map.put("pageSize", pageSize);
		List<AppComment> data = appCommentMapper.selectByExample(map);

		int recordCount = appCommentMapper.countByExample(map);
		PaginationVo<AppComment> result = new PaginationVo<AppComment>(data, recordCount, pageSize, currentPage);
		return result;
	}

	// app评论信息
	public List<AppComment> getAppComment() {
		return appCommentMapper.selectByExample(new HashMap<String, Object>());
	}

	// 查看单个app评论信息
	public AppComment getAppComment(int id) {
		return appCommentMapper.selectByPrimaryKey(id);
	}

	// 增加app评论
	public void addAppComment(AppComment res) {
		appCommentMapper.insert(res);
	}

	// 更新app评论
	public void upAppComment(AppComment res) {
		appCommentMapper.updateByPrimaryKey(res);
	}

	// 删除app评论信息
	public void delAppComment(int id) {
		appCommentMapper.deleteByPrimaryKey(id);
	}
	
	// 删除app评论信息
	public void deleteByAppId(int appId) {
		appCommentMapper.deleteByAppId(appId);
	}

	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delAppComment(id);
		}
	}

}
