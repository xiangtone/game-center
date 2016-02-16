package com.mas.rave.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.dao.TAppDistributeMapper;
import com.mas.rave.main.vo.Criteria;
import com.mas.rave.main.vo.TAppDistribute;
import com.mas.rave.service.TAppDistributeService;

@Service
public class TAppDistributeServiceImpl implements TAppDistributeService {
    @Autowired
    private TAppDistributeMapper tAppDistributeMapper;

    private static final Logger logger = LoggerFactory.getLogger(TAppDistributeServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tAppDistributeMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public List<TAppDistribute> selectByExample(Criteria example) {
        return this.tAppDistributeMapper.selectByExample(example);
    }
}