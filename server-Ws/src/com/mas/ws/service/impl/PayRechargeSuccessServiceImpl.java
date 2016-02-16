package com.mas.ws.service.impl;

import com.mas.ws.mapper.PayRechargeSuccessMapper;
import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.PayRechargeSuccess;
import com.mas.ws.service.PayRechargeSuccessService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayRechargeSuccessServiceImpl implements PayRechargeSuccessService {
    @Autowired
    private PayRechargeSuccessMapper payRechargeSuccessMapper;

    private static final Logger logger = LoggerFactory.getLogger(PayRechargeSuccessServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.payRechargeSuccessMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public PayRechargeSuccess selectByPrimaryKey(Integer id) {
        return this.payRechargeSuccessMapper.selectByPrimaryKey(id);
    }

    public List<PayRechargeSuccess> selectByExample(Criteria example) {
        return this.payRechargeSuccessMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(PayRechargeSuccess record) {
        return this.payRechargeSuccessMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(PayRechargeSuccess record) {
        return this.payRechargeSuccessMapper.updateByPrimaryKey(record);
    }

    public int insert(PayRechargeSuccess record) {
        return this.payRechargeSuccessMapper.insert(record);
    }

    public int insertSelective(PayRechargeSuccess record) {
        return this.payRechargeSuccessMapper.insertSelective(record);
    }
}