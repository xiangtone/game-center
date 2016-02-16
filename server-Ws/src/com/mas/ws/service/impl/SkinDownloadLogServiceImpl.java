package com.mas.ws.service.impl;

import com.mas.ws.mapper.SkinDownloadLogMapper;
import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.SkinDownloadLog;
import com.mas.ws.service.SkinDownloadLogService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkinDownloadLogServiceImpl implements SkinDownloadLogService {
    @Autowired
    private SkinDownloadLogMapper skinDownloadLogMapper;

    private static final Logger logger = LoggerFactory.getLogger(SkinDownloadLogServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.skinDownloadLogMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public SkinDownloadLog selectByPrimaryKey(Integer id) {
        return this.skinDownloadLogMapper.selectByPrimaryKey(id);
    }

    public List<SkinDownloadLog> selectByExample(Criteria example) {
        return this.skinDownloadLogMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(SkinDownloadLog record) {
        return this.skinDownloadLogMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(SkinDownloadLog record) {
        return this.skinDownloadLogMapper.updateByPrimaryKey(record);
    }

    public int insert(SkinDownloadLog record) {
        return this.skinDownloadLogMapper.insert(record);
    }

    public int insertSelective(SkinDownloadLog record) {
        return this.skinDownloadLogMapper.insertSelective(record);
    }
}