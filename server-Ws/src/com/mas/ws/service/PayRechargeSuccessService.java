package com.mas.ws.service;

import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.PayRechargeSuccess;

import java.util.List;

public interface PayRechargeSuccessService {
    int countByExample(Criteria example);

    PayRechargeSuccess selectByPrimaryKey(Integer id);

    List<PayRechargeSuccess> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(PayRechargeSuccess record);

    int updateByPrimaryKey(PayRechargeSuccess record);

    int insert(PayRechargeSuccess record);

    int insertSelective(PayRechargeSuccess record);
}