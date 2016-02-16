package com.mas.ws.service.impl;

import com.mas.ws.mapper.AppPageopenLogMapper;
import com.mas.ws.pojo.AppPageopenLog;
import com.mas.ws.pojo.Criteria;
import com.mas.ws.service.AppPageopenLogService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppPageopenLogServiceImpl implements AppPageopenLogService {
    @Autowired
    private AppPageopenLogMapper appPageopenLogMapper;

    private static final Logger logger = LoggerFactory.getLogger(AppPageopenLogServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.appPageopenLogMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public AppPageopenLog selectByPrimaryKey(Integer id) {
        return this.appPageopenLogMapper.selectByPrimaryKey(id);
    }

    public List<AppPageopenLog> selectByExample(Criteria example) {
        return this.appPageopenLogMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(AppPageopenLog record) {
        return this.appPageopenLogMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(AppPageopenLog record) {
        return this.appPageopenLogMapper.updateByPrimaryKey(record);
    }

    public int insert(AppPageopenLog record) {
        return this.appPageopenLogMapper.insert(record);
    }

    public int insertSelective(AppPageopenLog record) {
        return this.appPageopenLogMapper.insertSelective(record);
    }
}