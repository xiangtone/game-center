package com.mas.ws.service.impl;

import com.mas.ws.mapper.RaveDebacleLogMapper;
import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.RaveDebacleLog;
import com.mas.ws.service.RaveDebacleLogService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RaveDebacleLogServiceImpl implements RaveDebacleLogService {
    @Autowired
    private RaveDebacleLogMapper raveDebacleLogMapper;

    private static final Logger logger = LoggerFactory.getLogger(RaveDebacleLogServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.raveDebacleLogMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public RaveDebacleLog selectByPrimaryKey(Integer id) {
        return this.raveDebacleLogMapper.selectByPrimaryKey(id);
    }

    public List<RaveDebacleLog> selectByExample(Criteria example) {
        return this.raveDebacleLogMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(RaveDebacleLog record) {
        return this.raveDebacleLogMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(RaveDebacleLog record) {
        return this.raveDebacleLogMapper.updateByPrimaryKey(record);
    }

    public int insert(RaveDebacleLog record) {
        return this.raveDebacleLogMapper.insert(record);
    }

    public int insertSelective(RaveDebacleLog record) {
        return this.raveDebacleLogMapper.insertSelective(record);
    }
}