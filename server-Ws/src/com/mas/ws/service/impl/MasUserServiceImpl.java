package com.mas.ws.service.impl;

import com.mas.ws.mapper.MasUserMapper;
import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.MasUser;
import com.mas.ws.service.MasUserService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MasUserServiceImpl implements MasUserService {
    @Autowired
    private MasUserMapper masUserMapper;

    private static final Logger logger = LoggerFactory.getLogger(MasUserServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.masUserMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public MasUser selectByPrimaryKey(Integer userId) {
        return this.masUserMapper.selectByPrimaryKey(userId);
    }

    public List<MasUser> selectByExample(Criteria example) {
        return this.masUserMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(MasUser record) {
        return this.masUserMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(MasUser record) {
        return this.masUserMapper.updateByPrimaryKey(record);
    }

    public int insert(MasUser record) {
        return this.masUserMapper.insert(record);
    }

    public int insertSelective(MasUser record) {
        return this.masUserMapper.insertSelective(record);
    }

	@Override
	public void updateByUserName(MasUser masUser) {
		// TODO Auto-generated method stub
		this.masUserMapper.updateByUserName(masUser);
	}

	@Override
	public void deleteByPrimaryKey(Integer userId) {
		// TODO Auto-generated method stub
		this.masUserMapper.deleteByPrimaryKey(userId);
	}
}