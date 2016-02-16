package com.mas.market.service.impl;

import com.mas.market.mapper.TResMusicAlbumResMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TResMusicAlbumRes;
import com.mas.market.service.TResMusicAlbumResService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TResMusicAlbumResServiceImpl implements TResMusicAlbumResService {
    @Autowired
    private TResMusicAlbumResMapper tResMusicAlbumResMapper;

    private static final Logger logger = LoggerFactory.getLogger(TResMusicAlbumResServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tResMusicAlbumResMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TResMusicAlbumRes selectByPrimaryKey(Integer id) {
        return this.tResMusicAlbumResMapper.selectByPrimaryKey(id);
    }

    public List<TResMusicAlbumRes> selectByExample(Criteria example) {
        return this.tResMusicAlbumResMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TResMusicAlbumRes record) {
        return this.tResMusicAlbumResMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TResMusicAlbumRes record) {
        return this.tResMusicAlbumResMapper.updateByPrimaryKey(record);
    }

    public int insert(TResMusicAlbumRes record) {
        return this.tResMusicAlbumResMapper.insert(record);
    }

    public int insertSelective(TResMusicAlbumRes record) {
        return this.tResMusicAlbumResMapper.insertSelective(record);
    }

	@Override
	public List<TResMusicAlbumRes> selectColunmList(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tResMusicAlbumResMapper.selectColunmList(cr);
	}
}