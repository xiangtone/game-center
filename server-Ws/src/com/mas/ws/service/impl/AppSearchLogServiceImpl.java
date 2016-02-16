package com.mas.ws.service.impl;

import com.mas.ws.mapper.AppSearchLogMapper;
import com.mas.ws.pojo.AppSearchLog;
import com.mas.ws.pojo.Criteria;
import com.mas.ws.service.AppSearchLogService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppSearchLogServiceImpl implements AppSearchLogService {
    @Autowired
    private AppSearchLogMapper appSearchLogMapper;

    private static final Logger logger = LoggerFactory.getLogger(AppSearchLogServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.appSearchLogMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public AppSearchLog selectByPrimaryKey(Integer id) {
        return this.appSearchLogMapper.selectByPrimaryKey(id);
    }

    public List<AppSearchLog> selectByExample(Criteria example) {
        return this.appSearchLogMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(AppSearchLog record) {
        return this.appSearchLogMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(AppSearchLog record) {
        return this.appSearchLogMapper.updateByPrimaryKey(record);
    }

    public int insert(AppSearchLog record) {
        return this.appSearchLogMapper.insert(record);
    }

    public int insertSelective(AppSearchLog record) {
        return this.appSearchLogMapper.insertSelective(record);
    }
}