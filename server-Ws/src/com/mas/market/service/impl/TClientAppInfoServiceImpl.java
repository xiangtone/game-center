package com.mas.market.service.impl;

import com.mas.market.mapper.TClientAppInfoMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TClientAppInfo;
import com.mas.market.service.TClientAppInfoService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TClientAppInfoServiceImpl implements TClientAppInfoService {
    @Autowired
    private TClientAppInfoMapper tClientAppInfoMapper;

    private static final Logger logger = LoggerFactory.getLogger(TClientAppInfoServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tClientAppInfoMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TClientAppInfo selectByPrimaryKey(Integer id) {
        return this.tClientAppInfoMapper.selectByPrimaryKey(id);
    }

    public List<TClientAppInfo> selectByExample(Criteria example) {
        return this.tClientAppInfoMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TClientAppInfo record) {
        return this.tClientAppInfoMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TClientAppInfo record) {
        return this.tClientAppInfoMapper.updateByPrimaryKey(record);
    }

    public int insert(TClientAppInfo record) {
        return this.tClientAppInfoMapper.insert(record);
    }

    public int insertSelective(TClientAppInfo record) {
        return this.tClientAppInfoMapper.insertSelective(record);
    }

	@Override
	public void updateByExampleSelective(TClientAppInfo appInfo,
			Criteria criteria) {
		// TODO Auto-generated method stub
		this.tClientAppInfoMapper.updateByExampleSelective(appInfo,criteria.getCondition());
	}
}