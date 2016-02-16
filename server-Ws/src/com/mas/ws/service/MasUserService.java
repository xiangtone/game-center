package com.mas.ws.service;

import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.MasUser;

import java.util.List;

public interface MasUserService {
    int countByExample(Criteria example);

    MasUser selectByPrimaryKey(Integer userId);

    List<MasUser> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(MasUser record);

    int updateByPrimaryKey(MasUser record);

    int insert(MasUser record);

    int insertSelective(MasUser record);

	void updateByUserName(MasUser masUser);

	void deleteByPrimaryKey(Integer userId);
}