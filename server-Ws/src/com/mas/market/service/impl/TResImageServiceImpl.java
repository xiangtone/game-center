package com.mas.market.service.impl;

import com.mas.market.mapper.TResImageMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TResImage;
import com.mas.market.service.TResImageService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TResImageServiceImpl implements TResImageService {
    @Autowired
    private TResImageMapper tResImageMapper;

    private static final Logger logger = LoggerFactory.getLogger(TResImageServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tResImageMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TResImage selectByPrimaryKey(Integer id) {
        return this.tResImageMapper.selectByPrimaryKey(id);
    }

    public List<TResImage> selectByExample(Criteria example) {
        return this.tResImageMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TResImage record) {
        return this.tResImageMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TResImage record) {
        return this.tResImageMapper.updateByPrimaryKey(record);
    }

    public int insert(TResImage record) {
        return this.tResImageMapper.insert(record);
    }

    public int insertSelective(TResImage record) {
        return this.tResImageMapper.insertSelective(record);
    }

	@Override
	public void updateDownLoad(Criteria cr) {
		// TODO Auto-generated method stub
		this.tResImageMapper.updateDownLoad(cr);
	}

	@Override
	public List<TResImage> categorylist(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tResImageMapper.categorylist(cr);
	}

	@Override
	public List<TResImage> searchImage(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tResImageMapper.searchImage(cr);
	}

	@Override
	public List<TResImage> keywordImageList(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tResImageMapper.keywordImageList(cr);
	}

	@Override
	public List<TResImage> searchImageTip(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tResImageMapper.searchImageTip(cr);
	}

	@Override
	public List<TResImage> searchImageByArray(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tResImageMapper.searchImageByArray(cr);
	}
}