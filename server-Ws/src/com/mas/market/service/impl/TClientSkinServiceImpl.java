package com.mas.market.service.impl;

import com.mas.market.mapper.TClientSkinMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TClientSkin;
import com.mas.market.service.TClientSkinService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TClientSkinServiceImpl implements TClientSkinService {
    @Autowired
    private TClientSkinMapper tClientSkinMapper;

    private static final Logger logger = LoggerFactory.getLogger(TClientSkinServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tClientSkinMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TClientSkin selectByPrimaryKey(Integer skinId) {
        return this.tClientSkinMapper.selectByPrimaryKey(skinId);
    }

    public List<TClientSkin> selectByExample(Criteria example) {
        return this.tClientSkinMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TClientSkin record) {
        return this.tClientSkinMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TClientSkin record) {
        return this.tClientSkinMapper.updateByPrimaryKey(record);
    }

    public int insert(TClientSkin record) {
        return this.tClientSkinMapper.insert(record);
    }

    public int insertSelective(TClientSkin record) {
        return this.tClientSkinMapper.insertSelective(record);
    }

	@Override
	public void updateSkinDownLoad(TClientSkin tClientSkin) {
		// TODO Auto-generated method stub
		this.tClientSkinMapper.updateSkinDownLoad(tClientSkin);
	}
}