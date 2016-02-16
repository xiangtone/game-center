package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.Cp;

/**
 * cp信息数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface CpService {
	static class CpCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public CpCriteria nameLike(String name) {
			params.put(1, name);
			return this;
		}

		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

	/**
	 * 分页显示cp信息
	 * 
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PaginationVo<Cp> searchCps(CpCriteria criteria, int currentPage, int pageSize);

	// 查找所有一级或二级分类
	public List<Cp> getCps(Integer fatherId);

	// 获取所有cp信息
	public List<Cp> getAllCps();

	// 查看单个cp信息
	public Cp getCp(long id);

	// 增加cp信息
	public void addCp(Cp Cp);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	public int insertSelective(Cp record);

	// 更新cp信息
	public void upCp(Cp Cp);

	// 删除cp信息
	public void delCp(Long id);

	void batchDelete(Long[] ids);

	public Cp getCpByName(CpCriteria criteria);

	/**
	 * 获取cp状态
	 * 
	 * @param cpState
	 * @return
	 */
	public List<Cp> getCpStates(int cpState);
}
