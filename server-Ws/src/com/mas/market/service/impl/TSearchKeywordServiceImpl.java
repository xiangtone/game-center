package com.mas.market.service.impl;

import com.mas.market.mapper.TSearchKeywordMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TSearchKeyword;
import com.mas.market.service.TSearchKeywordService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TSearchKeywordServiceImpl implements TSearchKeywordService {
    @Autowired
    private TSearchKeywordMapper tSearchKeywordMapper;

    private static final Logger logger = LoggerFactory.getLogger(TSearchKeywordServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tSearchKeywordMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TSearchKeyword selectByPrimaryKey(Integer searchId) {
        return this.tSearchKeywordMapper.selectByPrimaryKey(searchId);
    }

    public List<TSearchKeyword> selectByExample(Criteria example) {
        return this.tSearchKeywordMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TSearchKeyword record) {
        return this.tSearchKeywordMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TSearchKeyword record) {
        return this.tSearchKeywordMapper.updateByPrimaryKey(record);
    }

    public int insert(TSearchKeyword record) {
        return this.tSearchKeywordMapper.insert(record);
    }

    public int insertSelective(TSearchKeyword record) {
        return this.tSearchKeywordMapper.insertSelective(record);
    }

	@Override
	public List<TSearchKeyword> selectKeywords(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tSearchKeywordMapper.selectKeywords(cr);
	}

	@Override
	public void updateSearchNum(Integer searchId) {
		// TODO Auto-generated method stub
		this.tSearchKeywordMapper.updateSearchNum(searchId);
	}
}