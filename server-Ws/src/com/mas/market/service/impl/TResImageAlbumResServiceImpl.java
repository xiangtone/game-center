package com.mas.market.service.impl;

import com.mas.market.mapper.TResImageAlbumResMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TResImageAlbumRes;
import com.mas.market.service.TResImageAlbumResService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TResImageAlbumResServiceImpl implements TResImageAlbumResService {
    @Autowired
    private TResImageAlbumResMapper tResImageAlbumResMapper;

    private static final Logger logger = LoggerFactory.getLogger(TResImageAlbumResServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tResImageAlbumResMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TResImageAlbumRes selectByPrimaryKey(Integer id) {
        return this.tResImageAlbumResMapper.selectByPrimaryKey(id);
    }

    public List<TResImageAlbumRes> selectByExample(Criteria example) {
        return this.tResImageAlbumResMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TResImageAlbumRes record) {
        return this.tResImageAlbumResMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TResImageAlbumRes record) {
        return this.tResImageAlbumResMapper.updateByPrimaryKey(record);
    }

    public int insert(TResImageAlbumRes record) {
        return this.tResImageAlbumResMapper.insert(record);
    }

    public int insertSelective(TResImageAlbumRes record) {
        return this.tResImageAlbumResMapper.insertSelective(record);
    }
}