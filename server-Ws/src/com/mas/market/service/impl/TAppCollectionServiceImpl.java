package com.mas.market.service.impl;

import com.mas.market.mapper.TAppCollectionMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TAppCollection;
import com.mas.market.service.TAppCollectionService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TAppCollectionServiceImpl implements TAppCollectionService {
    @Autowired
    private TAppCollectionMapper tAppCollectionMapper;

    private static final Logger logger = LoggerFactory.getLogger(TAppCollectionServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tAppCollectionMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TAppCollection selectByPrimaryKey(Integer collectionId) {
        return this.tAppCollectionMapper.selectByPrimaryKey(collectionId);
    }

    public List<TAppCollection> selectByExample(Criteria example) {
        return this.tAppCollectionMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TAppCollection record) {
        return this.tAppCollectionMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TAppCollection record) {
        return this.tAppCollectionMapper.updateByPrimaryKey(record);
    }

    public int insert(TAppCollection record) {
        return this.tAppCollectionMapper.insert(record);
    }

    public int insertSelective(TAppCollection record) {
        return this.tAppCollectionMapper.insertSelective(record);
    }

	@Override
	public List<TAppCollection> collection(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tAppCollectionMapper.collection(cr);
	}
}