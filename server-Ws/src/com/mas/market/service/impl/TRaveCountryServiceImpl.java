package com.mas.market.service.impl;

import com.mas.market.mapper.TRaveCountryMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TRaveCountry;
import com.mas.market.service.TRaveCountryService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TRaveCountryServiceImpl implements TRaveCountryService {
    @Autowired
    private TRaveCountryMapper tRaveCountryMapper;

    private static final Logger logger = LoggerFactory.getLogger(TRaveCountryServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tRaveCountryMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TRaveCountry selectByPrimaryKey(Integer id) {
        return this.tRaveCountryMapper.selectByPrimaryKey(id);
    }

    public List<TRaveCountry> selectByExample(Criteria example) {
        return this.tRaveCountryMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TRaveCountry record) {
        return this.tRaveCountryMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TRaveCountry record) {
        return this.tRaveCountryMapper.updateByPrimaryKey(record);
    }

    public int insert(TRaveCountry record) {
        return this.tRaveCountryMapper.insert(record);
    }

    public int insertSelective(TRaveCountry record) {
        return this.tRaveCountryMapper.insertSelective(record);
    }
}