package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.Log;

/**
 * log信息数据访问接口
 * 
 * @author jieding
 * 
 */
@Service
public interface LogService {
	static class LogCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public LogCriteria actionLike(String action) {
			params.put(2, action);
			return this;
		}

		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

	/**
	 * 分页显示Log信息
	 * 
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PaginationVo<Log> searchLog(Log criteria, int currentPage, int pageSize);

	// 查看单个log信息
	public Log getLog(long id);
	// 获取所有log信息
	public List<Log> getAllLogs();

	// 增加log信息
	public int addLog(Log log);

}
