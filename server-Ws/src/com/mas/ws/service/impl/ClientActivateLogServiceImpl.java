package com.mas.ws.service.impl;

import com.mas.ws.mapper.ClientActivateLogMapper;
import com.mas.ws.pojo.ClientActivateLog;
import com.mas.ws.pojo.Criteria;
import com.mas.ws.service.ClientActivateLogService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientActivateLogServiceImpl implements ClientActivateLogService {
    @Autowired
    private ClientActivateLogMapper clientActivateLogMapper;

    private static final Logger logger = LoggerFactory.getLogger(ClientActivateLogServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.clientActivateLogMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public ClientActivateLog selectByPrimaryKey(Integer id) {
        return this.clientActivateLogMapper.selectByPrimaryKey(id);
    }

    public List<ClientActivateLog> selectByExample(Criteria example) {
        return this.clientActivateLogMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(ClientActivateLog record) {
        return this.clientActivateLogMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(ClientActivateLog record) {
        return this.clientActivateLogMapper.updateByPrimaryKey(record);
    }

    public int insert(ClientActivateLog record) {
        return this.clientActivateLogMapper.insert(record);
    }

    public int insertSelective(ClientActivateLog record) {
        return this.clientActivateLogMapper.insertSelective(record);
    }
}