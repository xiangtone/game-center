package com.mas.ws.service.impl;

import com.mas.ws.mapper.PayUserPurchaseMapper;
import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.PayUserPurchase;
import com.mas.ws.service.PayUserPurchaseService;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayUserPurchaseServiceImpl implements PayUserPurchaseService {
    @Autowired
    private PayUserPurchaseMapper payUserPurchaseMapper;

    private static final Logger logger = LoggerFactory.getLogger(PayUserPurchaseServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.payUserPurchaseMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public PayUserPurchase selectByPrimaryKey(Integer id) {
        return this.payUserPurchaseMapper.selectByPrimaryKey(id);
    }

    public List<PayUserPurchase> selectByExample(Criteria example) {
        return this.payUserPurchaseMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(PayUserPurchase record) {
        return this.payUserPurchaseMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(PayUserPurchase record) {
        return this.payUserPurchaseMapper.updateByPrimaryKey(record);
    }

    public int insert(PayUserPurchase record) {
        return this.payUserPurchaseMapper.insert(record);
    }

    public int insertSelective(PayUserPurchase record) {
        return this.payUserPurchaseMapper.insertSelective(record);
    }

	@Override
	public void updateByExampleSelective(PayUserPurchase payUserPurchase,
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		this.payUserPurchaseMapper.updateByExampleSelective(payUserPurchase,condition);
	}
}