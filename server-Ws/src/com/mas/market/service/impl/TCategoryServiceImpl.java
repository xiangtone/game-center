package com.mas.market.service.impl;

import com.mas.market.mapper.TCategoryMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TCategory;
import com.mas.market.service.TCategoryService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TCategoryServiceImpl implements TCategoryService {
    @Autowired
    private TCategoryMapper tCategoryMapper;

    private static final Logger logger = LoggerFactory.getLogger(TCategoryServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tCategoryMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TCategory selectByPrimaryKey(Integer id) {
        return this.tCategoryMapper.selectByPrimaryKey(id);
    }

    public List<TCategory> selectByExample(Criteria example) {
        return this.tCategoryMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TCategory record) {
        return this.tCategoryMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TCategory record) {
        return this.tCategoryMapper.updateByPrimaryKey(record);
    }

    public int insert(TCategory record) {
        return this.tCategoryMapper.insert(record);
    }

    public int insertSelective(TCategory record) {
        return this.tCategoryMapper.insertSelective(record);
    }

	@Override
	public List<TCategory> selectByCtRaveId(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tCategoryMapper.selectByCtRaveId(cr);
	}

	@Override
	public List<TCategory> selectLevelCat(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tCategoryMapper.selectLevelCat(cr);
	}

	@Override
	public List<Integer> selectAllCatIds(Criteria cr) {
		// TODO Auto-generated method stub
		return tCategoryMapper.selectAllCatIds(cr);
	}

	@Override
	public List<TCategory> selectByAllCatIds(Criteria cr) {
		// TODO Auto-generated method stub
		return tCategoryMapper.selectByAllCatIds(cr);
	}
}