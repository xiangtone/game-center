package com.mas.ws.service.impl;

import com.mas.ws.mapper.ImageSearchLogMapper;
import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.ImageSearchLog;
import com.mas.ws.service.ImageSearchLogService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageSearchLogServiceImpl implements ImageSearchLogService {
    @Autowired
    private ImageSearchLogMapper imageSearchLogMapper;

    private static final Logger logger = LoggerFactory.getLogger(ImageSearchLogServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.imageSearchLogMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public ImageSearchLog selectByPrimaryKey(Integer id) {
        return this.imageSearchLogMapper.selectByPrimaryKey(id);
    }

    public List<ImageSearchLog> selectByExample(Criteria example) {
        return this.imageSearchLogMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(ImageSearchLog record) {
        return this.imageSearchLogMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(ImageSearchLog record) {
        return this.imageSearchLogMapper.updateByPrimaryKey(record);
    }

    public int insert(ImageSearchLog record) {
        return this.imageSearchLogMapper.insert(record);
    }

    public int insertSelective(ImageSearchLog record) {
        return this.imageSearchLogMapper.insertSelective(record);
    }
}