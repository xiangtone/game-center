package com.mas.market.service.impl;

import com.mas.market.mapper.TClientFeedbackZappcodeMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TClientFeedbackZappcode;
import com.mas.market.service.TClientFeedbackZappcodeService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TClientFeedbackZappcodeServiceImpl implements TClientFeedbackZappcodeService {
    @Autowired
    private TClientFeedbackZappcodeMapper tClientFeedbackZappcodeMapper;

    private static final Logger logger = LoggerFactory.getLogger(TClientFeedbackZappcodeServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tClientFeedbackZappcodeMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TClientFeedbackZappcode selectByPrimaryKey(Integer id) {
        return this.tClientFeedbackZappcodeMapper.selectByPrimaryKey(id);
    }

    public List<TClientFeedbackZappcode> selectByExample(Criteria example) {
        return this.tClientFeedbackZappcodeMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TClientFeedbackZappcode record) {
        return this.tClientFeedbackZappcodeMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TClientFeedbackZappcode record) {
        return this.tClientFeedbackZappcodeMapper.updateByPrimaryKey(record);
    }

    public int insert(TClientFeedbackZappcode record) {
        return this.tClientFeedbackZappcodeMapper.insert(record);
    }

    public int insertSelective(TClientFeedbackZappcode record) {
        return this.tClientFeedbackZappcodeMapper.insertSelective(record);
    }
}