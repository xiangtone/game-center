package com.mas.ws.service.impl;

import com.mas.ws.mapper.PayRechargeFailingMapper;
import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.PayRechargeFailing;
import com.mas.ws.service.PayRechargeFailingService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayRechargeFailingServiceImpl implements PayRechargeFailingService {
    @Autowired
    private PayRechargeFailingMapper payRechargeFailingMapper;

    private static final Logger logger = LoggerFactory.getLogger(PayRechargeFailingServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.payRechargeFailingMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public PayRechargeFailing selectByPrimaryKey(Integer id) {
        return this.payRechargeFailingMapper.selectByPrimaryKey(id);
    }

    public List<PayRechargeFailing> selectByExample(Criteria example) {
        return this.payRechargeFailingMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(PayRechargeFailing record) {
        return this.payRechargeFailingMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(PayRechargeFailing record) {
        return this.payRechargeFailingMapper.updateByPrimaryKey(record);
    }

    public int insert(PayRechargeFailing record) {
        return this.payRechargeFailingMapper.insert(record);
    }

    public int insertSelective(PayRechargeFailing record) {
        return this.payRechargeFailingMapper.insertSelective(record);
    }
}