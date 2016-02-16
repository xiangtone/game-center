package com.mas.market.service.impl;

import com.mas.market.mapper.TAppTestinMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TAppTestin;
import com.mas.market.service.TAppTestinService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TAppTestinServiceImpl implements TAppTestinService {
    @Autowired
    private TAppTestinMapper tAppTestinMapper;

    private static final Logger logger = LoggerFactory.getLogger(TAppTestinServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tAppTestinMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TAppTestin selectByPrimaryKey(String adaptId) {
        return this.tAppTestinMapper.selectByPrimaryKey(adaptId);
    }

    public List<TAppTestin> selectByExample(Criteria example) {
        return this.tAppTestinMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TAppTestin record) {
        return this.tAppTestinMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TAppTestin record) {
        return this.tAppTestinMapper.updateByPrimaryKey(record);
    }

    public int insert(TAppTestin record) {
        return this.tAppTestinMapper.insert(record);
    }

    public int insertSelective(TAppTestin record) {
        return this.tAppTestinMapper.insertSelective(record);
    }

	@Override
	public void updateByAdaptId(TAppTestin tAppTestin) {
		// TODO Auto-generated method stub
		this.tAppTestinMapper.updateByAdaptId(tAppTestin);
	}
}