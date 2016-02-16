package com.mas.ws.service.impl;

import com.mas.ws.mapper.ImageDownloadLogMapper;
import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.ImageDownloadLog;
import com.mas.ws.service.ImageDownloadLogService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageDownloadLogServiceImpl implements ImageDownloadLogService {
    @Autowired
    private ImageDownloadLogMapper imageDownloadLogMapper;

    private static final Logger logger = LoggerFactory.getLogger(ImageDownloadLogServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.imageDownloadLogMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public ImageDownloadLog selectByPrimaryKey(Integer id) {
        return this.imageDownloadLogMapper.selectByPrimaryKey(id);
    }

    public List<ImageDownloadLog> selectByExample(Criteria example) {
        return this.imageDownloadLogMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(ImageDownloadLog record) {
        return this.imageDownloadLogMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(ImageDownloadLog record) {
        return this.imageDownloadLogMapper.updateByPrimaryKey(record);
    }

    public int insert(ImageDownloadLog record) {
        return this.imageDownloadLogMapper.insert(record);
    }

    public int insertSelective(ImageDownloadLog record) {
        return this.imageDownloadLogMapper.insertSelective(record);
    }
}