package com.mas.ws.service.impl;

import com.mas.ws.mapper.AppLogoutLogMapper;
import com.mas.ws.pojo.AppLogoutLog;
import com.mas.ws.pojo.Criteria;
import com.mas.ws.service.AppLogoutLogService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppLogoutLogServiceImpl implements AppLogoutLogService {
    @Autowired
    private AppLogoutLogMapper appLogoutLogMapper;

    private static final Logger logger = LoggerFactory.getLogger(AppLogoutLogServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.appLogoutLogMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public AppLogoutLog selectByPrimaryKey(Integer id) {
        return this.appLogoutLogMapper.selectByPrimaryKey(id);
    }

    public List<AppLogoutLog> selectByExample(Criteria example) {
        return this.appLogoutLogMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(AppLogoutLog record) {
        return this.appLogoutLogMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(AppLogoutLog record) {
        return this.appLogoutLogMapper.updateByPrimaryKey(record);
    }

    public int insert(AppLogoutLog record) {
        return this.appLogoutLogMapper.insert(record);
    }

    public int insertSelective(AppLogoutLog record) {
        return this.appLogoutLogMapper.insertSelective(record);
    }
}