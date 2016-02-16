package com.mas.ws.service.impl;

import com.mas.ws.mapper.MusicSearchLogMapper;
import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.MusicSearchLog;
import com.mas.ws.service.MusicSearchLogService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MusicSearchLogServiceImpl implements MusicSearchLogService {
    @Autowired
    private MusicSearchLogMapper musicSearchLogMapper;

    private static final Logger logger = LoggerFactory.getLogger(MusicSearchLogServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.musicSearchLogMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public MusicSearchLog selectByPrimaryKey(Integer id) {
        return this.musicSearchLogMapper.selectByPrimaryKey(id);
    }

    public List<MusicSearchLog> selectByExample(Criteria example) {
        return this.musicSearchLogMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(MusicSearchLog record) {
        return this.musicSearchLogMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(MusicSearchLog record) {
        return this.musicSearchLogMapper.updateByPrimaryKey(record);
    }

    public int insert(MusicSearchLog record) {
        return this.musicSearchLogMapper.insert(record);
    }

    public int insertSelective(MusicSearchLog record) {
        return this.musicSearchLogMapper.insertSelective(record);
    }
}