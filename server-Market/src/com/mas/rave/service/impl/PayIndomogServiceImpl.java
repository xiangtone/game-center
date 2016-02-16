package com.mas.rave.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.PayMapper;
import com.mas.rave.main.vo.Pay;
import com.mas.rave.main.vo.PayExample;
import com.mas.rave.service.PayService;

/**
 * 支付
 * 
 * @author liwei.sz
 * 
 */

@Service
public class PayIndomogServiceImpl implements PayService {

	@Autowired
	private PayMapper payIndomogMapper;

	public PaginationVo<Pay> searchPays(PayCriteria criteria, int currentPage, int pageSize) {
		PayExample example = new PayExample();
		Map<Integer, Object> params = criteria.getParams();
		for (Integer key : params.keySet()) {
			if (key.equals(1)) {
				// 根据id查看
				example.createCriteria().andMogValue(Integer.parseInt(params.get(1).toString()));
			}
		}
		example.setOrderByClause(" mogValue asc ");
		List<Pay> data = payIndomogMapper.selectByExample(example, new RowBounds((currentPage - 1) * pageSize, pageSize));
		int recordCount = payIndomogMapper.countByExample(example);
		PaginationVo<Pay> result = new PaginationVo<Pay>(data, recordCount, pageSize, currentPage);
		return result;

	}

	// 查看单个app信息
	public Pay getPay(int id) {
		return payIndomogMapper.selectByPrimaryKey(id);
	}

	// 增加app信息
	public void addPay(Pay pay) {
		payIndomogMapper.insert(pay);
	}

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	public int insertSelective(Pay record) {
		return payIndomogMapper.insertSelective(record);
	}

	// 更新app信息
	public void upPay(Pay pay) {
		payIndomogMapper.updateByPrimaryKey(pay);
	}

	// 删除app信息
	public void delPay(int id) {
		payIndomogMapper.deleteByPrimaryKey(id);
	}

	// 同时删除多个app
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			payIndomogMapper.deleteByPrimaryKey(id);
		}
	}
}
