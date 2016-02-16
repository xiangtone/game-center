package com.mas.market.service.impl;

import com.mas.market.mapper.TAppPictureMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TAppPicture;
import com.mas.market.service.TAppPictureService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TAppPictureServiceImpl implements TAppPictureService {
    @Autowired
    private TAppPictureMapper tAppPictureMapper;

    private static final Logger logger = LoggerFactory.getLogger(TAppPictureServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tAppPictureMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TAppPicture selectByPrimaryKey(Integer id) {
        return this.tAppPictureMapper.selectByPrimaryKey(id);
    }

    public List<TAppPicture> selectByExample(Criteria example) {
        return this.tAppPictureMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TAppPicture record) {
        return this.tAppPictureMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TAppPicture record) {
        return this.tAppPictureMapper.updateByPrimaryKey(record);
    }

    public int insert(TAppPicture record) {
        return this.tAppPictureMapper.insert(record);
    }

    public int insertSelective(TAppPicture record) {
        return this.tAppPictureMapper.insertSelective(record);
    }
}