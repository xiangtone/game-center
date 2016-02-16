package com.mas.ws.service.impl;

import com.mas.ws.mapper.AppChangeuserLogMapper;
import com.mas.ws.pojo.AppChangeuserLog;
import com.mas.ws.pojo.Criteria;
import com.mas.ws.service.AppChangeuserLogService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppChangeuserLogServiceImpl implements AppChangeuserLogService {
    @Autowired
    private AppChangeuserLogMapper appChangeuserLogMapper;

    private static final Logger logger = LoggerFactory.getLogger(AppChangeuserLogServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.appChangeuserLogMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public AppChangeuserLog selectByPrimaryKey() {
        return this.appChangeuserLogMapper.selectByPrimaryKey();
    }

    public List<AppChangeuserLog> selectByExample(Criteria example) {
        return this.appChangeuserLogMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(AppChangeuserLog record) {
        return this.appChangeuserLogMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(AppChangeuserLog record) {
        return this.appChangeuserLogMapper.updateByPrimaryKeySelective(record);
    }

    public int insert(AppChangeuserLog record) {
        return this.appChangeuserLogMapper.insert(record);
    }

    public int insertSelective(AppChangeuserLog record) {
        return this.appChangeuserLogMapper.insertSelective(record);
    }
}