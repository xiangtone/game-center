package com.mas.rave.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.CpMapper;
import com.mas.rave.main.vo.Cp;
import com.mas.rave.main.vo.CpExample;
import com.mas.rave.service.CpService;

/**
 * cp
 * 
 * @author liwei.sz
 * 
 */
@Service
public class CpServiceImpl implements CpService {

	@Autowired
	private CpMapper cpMapper;

	public PaginationVo<Cp> searchCps(CpCriteria criteria, int currentPage, int pageSize) {
		CpExample example = new CpExample();
		Map<Integer, Object> params = criteria.getParams();
		for (Integer key : params.keySet()) {
			if (key.equals(1)) {
				example.createCriteria().andNameLike(params.get(key).toString());
			}
		}
		example.setOrderByClause("createTime desc");
		List<Cp> data = cpMapper.selectByExample(example, new RowBounds((currentPage - 1) * pageSize, pageSize));
		int recordCount = cpMapper.countByExample(example);
		PaginationVo<Cp> result = new PaginationVo<Cp>(data, recordCount, pageSize, currentPage);
		return result;

	}

	// 查找所有一级或二级分类
	public List<Cp> getCps(Integer fatherId) {
		CpExample example = new CpExample();
		List<Cp> result = cpMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(result))
			return null;
		else
			return result;
	}

	// 查看单个平台信息
	public Cp getCp(long id) {
		return cpMapper.selectByPrimaryKey(id);
	}

	// 增加平台信息
	public void addCp(Cp Cp) {
		cpMapper.insert(Cp);
	}

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	public int insertSelective(Cp record) {
		return cpMapper.insertSelective(record);
	}

	// 更新平台信息
	public void upCp(Cp Cp) {
		cpMapper.updateByPrimaryKey(Cp);
	}

	// 删除平台信息
	public void delCp(Long id) {
		cpMapper.deleteByPrimaryKey(id);
	}

	// 同时删除多个平台
	public void batchDelete(Long[] ids) {
		for (Long id : ids) {
			cpMapper.deleteByPrimaryKey(id);
		}
	}

	// 获取所有cp信息
	public List<Cp> getAllCps() {
		CpExample example = new CpExample();
		return cpMapper.selectByExample(example);
	}

	public Cp getCpByName(CpCriteria criteria) {
		CpExample example = new CpExample();
		Map<Integer, Object> params = criteria.getParams();
		for (Integer key : params.keySet()) {
			if (key.equals(1)) {
				example.createCriteria().andName(params.get(key).toString());
			}
		}
		List<Cp> result = cpMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(result))
			return null;
		else
			return result.get(0);
	}

	/**
	 * 获取cp状态
	 * 
	 * @param cpState
	 * @return
	 */
	public List<Cp> getCpStates(int cpState) {
		return cpMapper.getCpStates(cpState);
	}
}
