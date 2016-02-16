package com.mas.ws.service;

import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.PayUserPurchase;

import java.util.List;
import java.util.Map;

public interface PayUserPurchaseService {
    int countByExample(Criteria example);

    PayUserPurchase selectByPrimaryKey(Integer id);

    List<PayUserPurchase> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(PayUserPurchase record);

    int updateByPrimaryKey(PayUserPurchase record);

    int insert(PayUserPurchase record);

    int insertSelective(PayUserPurchase record);

	void updateByExampleSelective(PayUserPurchase payUserPurchase,
			Map<String, Object> condition);
}