package com.mas.market.service.impl;

import com.mas.market.mapper.TPayIndomogMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TPayIndomog;
import com.mas.market.service.TPayIndomogService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TPayIndomogServiceImpl implements TPayIndomogService {
    @Autowired
    private TPayIndomogMapper tPayIndomogMapper;

    private static final Logger logger = LoggerFactory.getLogger(TPayIndomogServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tPayIndomogMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TPayIndomog selectByPrimaryKey(Integer id) {
        return this.tPayIndomogMapper.selectByPrimaryKey(id);
    }

    public List<TPayIndomog> selectByExample(Criteria example) {
        return this.tPayIndomogMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TPayIndomog record) {
        return this.tPayIndomogMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TPayIndomog record) {
        return this.tPayIndomogMapper.updateByPrimaryKey(record);
    }

    public int insert(TPayIndomog record) {
        return this.tPayIndomogMapper.insert(record);
    }

    public int insertSelective(TPayIndomog record) {
        return this.tPayIndomogMapper.insertSelective(record);
    }

	@Override
	public List<TPayIndomog> selectByExchange(Criteria criteria) {
		// TODO Auto-generated method stub
		return this.tPayIndomogMapper.selectByExchange(criteria);
	}
}