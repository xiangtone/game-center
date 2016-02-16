package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TClientAppInfo;

import java.util.List;

public interface TClientAppInfoService {
    int countByExample(Criteria example);

    TClientAppInfo selectByPrimaryKey(Integer id);

    List<TClientAppInfo> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TClientAppInfo record);

    int updateByPrimaryKey(TClientAppInfo record);

    int insert(TClientAppInfo record);

    int insertSelective(TClientAppInfo record);

	void updateByExampleSelective(TClientAppInfo appInfo, Criteria criteria);
}