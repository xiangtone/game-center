package com.mas.market.service.impl;

import com.mas.market.mapper.TClientFeedbackZappMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TClientFeedbackZapp;
import com.mas.market.service.TClientFeedbackZappService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TClientFeedbackZappServiceImpl implements TClientFeedbackZappService {
    @Autowired
    private TClientFeedbackZappMapper tClientFeedbackZappMapper;

    private static final Logger logger = LoggerFactory.getLogger(TClientFeedbackZappServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tClientFeedbackZappMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TClientFeedbackZapp selectByPrimaryKey(Integer id) {
        return this.tClientFeedbackZappMapper.selectByPrimaryKey(id);
    }

    public List<TClientFeedbackZapp> selectByExample(Criteria example) {
        return this.tClientFeedbackZappMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TClientFeedbackZapp record) {
        return this.tClientFeedbackZappMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TClientFeedbackZapp record) {
        return this.tClientFeedbackZappMapper.updateByPrimaryKey(record);
    }

    public int insert(TClientFeedbackZapp record) {
        return this.tClientFeedbackZappMapper.insert(record);
    }

    public int insertSelective(TClientFeedbackZapp record) {
        return this.tClientFeedbackZappMapper.insertSelective(record);
    }
}