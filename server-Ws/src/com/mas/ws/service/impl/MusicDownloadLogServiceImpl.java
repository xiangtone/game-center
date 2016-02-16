package com.mas.ws.service.impl;

import com.mas.ws.mapper.MusicDownloadLogMapper;
import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.MusicDownloadLog;
import com.mas.ws.service.MusicDownloadLogService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MusicDownloadLogServiceImpl implements MusicDownloadLogService {
    @Autowired
    private MusicDownloadLogMapper musicDownloadLogMapper;

    private static final Logger logger = LoggerFactory.getLogger(MusicDownloadLogServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.musicDownloadLogMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public MusicDownloadLog selectByPrimaryKey(Integer id) {
        return this.musicDownloadLogMapper.selectByPrimaryKey(id);
    }

    public List<MusicDownloadLog> selectByExample(Criteria example) {
        return this.musicDownloadLogMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(MusicDownloadLog record) {
        return this.musicDownloadLogMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(MusicDownloadLog record) {
        return this.musicDownloadLogMapper.updateByPrimaryKey(record);
    }

    public int insert(MusicDownloadLog record) {
        return this.musicDownloadLogMapper.insert(record);
    }

    public int insertSelective(MusicDownloadLog record) {
        return this.musicDownloadLogMapper.insertSelective(record);
    }
}