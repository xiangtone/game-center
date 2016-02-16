package com.mas.market.service.impl;

import com.mas.market.mapper.TClientSkinCodeMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TClientSkinCode;
import com.mas.market.service.TClientSkinCodeService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TClientSkinCodeServiceImpl implements TClientSkinCodeService {
    @Autowired
    private TClientSkinCodeMapper tClientSkinCodeMapper;

    private static final Logger logger = LoggerFactory.getLogger(TClientSkinCodeServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tClientSkinCodeMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TClientSkinCode selectByPrimaryKey(Integer id) {
        return this.tClientSkinCodeMapper.selectByPrimaryKey(id);
    }

    public List<TClientSkinCode> selectByExample(Criteria example) {
        return this.tClientSkinCodeMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TClientSkinCode record) {
        return this.tClientSkinCodeMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TClientSkinCode record) {
        return this.tClientSkinCodeMapper.updateByPrimaryKey(record);
    }

    public int insert(TClientSkinCode record) {
        return this.tClientSkinCodeMapper.insert(record);
    }

    public int insertSelective(TClientSkinCode record) {
        return this.tClientSkinCodeMapper.insertSelective(record);
    }
}