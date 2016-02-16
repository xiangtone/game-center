package com.mas.market.service.impl;

import com.mas.market.mapper.TAppAlbumColumnMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TAppAlbumColumn;
import com.mas.market.service.TAppAlbumColumnService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TAppAlbumColumnServiceImpl implements TAppAlbumColumnService {
    @Autowired
    private TAppAlbumColumnMapper tAppAlbumColumnMapper;

    private static final Logger logger = LoggerFactory.getLogger(TAppAlbumColumnServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tAppAlbumColumnMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TAppAlbumColumn selectByPrimaryKey(Integer columnId) {
        return this.tAppAlbumColumnMapper.selectByPrimaryKey(columnId);
    }

    public List<TAppAlbumColumn> selectByExample(Criteria example) {
        return this.tAppAlbumColumnMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TAppAlbumColumn record) {
        return this.tAppAlbumColumnMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TAppAlbumColumn record) {
        return this.tAppAlbumColumnMapper.updateByPrimaryKey(record);
    }

    public int insert(TAppAlbumColumn record) {
        return this.tAppAlbumColumnMapper.insert(record);
    }

    public int insertSelective(TAppAlbumColumn record) {
        return this.tAppAlbumColumnMapper.insertSelective(record);
    }
}