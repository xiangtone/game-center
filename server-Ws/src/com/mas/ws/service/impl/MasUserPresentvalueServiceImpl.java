package com.mas.ws.service.impl;

import com.mas.ws.mapper.MasUserPresentvalueMapper;
import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.MasUserPresentvalue;
import com.mas.ws.service.MasUserPresentvalueService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MasUserPresentvalueServiceImpl implements MasUserPresentvalueService {
    @Autowired
    private MasUserPresentvalueMapper masUserPresentvalueMapper;

    private static final Logger logger = LoggerFactory.getLogger(MasUserPresentvalueServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.masUserPresentvalueMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public MasUserPresentvalue selectByPrimaryKey(Integer id) {
        return this.masUserPresentvalueMapper.selectByPrimaryKey(id);
    }

    public List<MasUserPresentvalue> selectByExample(Criteria example) {
        return this.masUserPresentvalueMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(MasUserPresentvalue record) {
        return this.masUserPresentvalueMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(MasUserPresentvalue record) {
        return this.masUserPresentvalueMapper.updateByPrimaryKey(record);
    }

    public int insert(MasUserPresentvalue record) {
        return this.masUserPresentvalueMapper.insert(record);
    }

    public int insertSelective(MasUserPresentvalue record) {
        return this.masUserPresentvalueMapper.insertSelective(record);
    }
}