package com.mas.ws.service;

import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.PayRechargeFailing;

import java.util.List;

public interface PayRechargeFailingService {
    int countByExample(Criteria example);

    PayRechargeFailing selectByPrimaryKey(Integer id);

    List<PayRechargeFailing> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(PayRechargeFailing record);

    int updateByPrimaryKey(PayRechargeFailing record);

    int insert(PayRechargeFailing record);

    int insertSelective(PayRechargeFailing record);
}