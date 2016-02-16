package com.mas.market.service.impl;

import com.mas.market.mapper.TCpMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TCp;
import com.mas.market.service.TCpService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TCpServiceImpl implements TCpService {
    @Autowired
    private TCpMapper tCpMapper;

    private static final Logger logger = LoggerFactory.getLogger(TCpServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tCpMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TCp selectByPrimaryKey(Integer id) {
        return this.tCpMapper.selectByPrimaryKey(id);
    }

    public List<TCp> selectByExample(Criteria example) {
        return this.tCpMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TCp record) {
        return this.tCpMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TCp record) {
        return this.tCpMapper.updateByPrimaryKey(record);
    }

    public int insert(TCp record) {
        return this.tCpMapper.insert(record);
    }

    public int insertSelective(TCp record) {
        return this.tCpMapper.insertSelective(record);
    }
}