package com.mas.ws.service.impl;

import com.mas.ws.mapper.AppLoginLogMapper;
import com.mas.ws.pojo.AppLoginLog;
import com.mas.ws.pojo.Criteria;
import com.mas.ws.service.AppLoginLogService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppLoginLogServiceImpl implements AppLoginLogService {
    @Autowired
    private AppLoginLogMapper appLoginLogMapper;

    private static final Logger logger = LoggerFactory.getLogger(AppLoginLogServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.appLoginLogMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public AppLoginLog selectByPrimaryKey(Integer id) {
        return this.appLoginLogMapper.selectByPrimaryKey(id);
    }

    public List<AppLoginLog> selectByExample(Criteria example) {
        return this.appLoginLogMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(AppLoginLog record) {
        return this.appLoginLogMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(AppLoginLog record) {
        return this.appLoginLogMapper.updateByPrimaryKey(record);
    }

    public int insert(AppLoginLog record) {
        return this.appLoginLogMapper.insert(record);
    }

    public int insertSelective(AppLoginLog record) {
        return this.appLoginLogMapper.insertSelective(record);
    }
}