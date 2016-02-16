package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TClientSkin;

import java.util.List;

public interface TClientSkinService {
    int countByExample(Criteria example);

    TClientSkin selectByPrimaryKey(Integer skinId);

    List<TClientSkin> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TClientSkin record);

    int updateByPrimaryKey(TClientSkin record);

    int insert(TClientSkin record);

    int insertSelective(TClientSkin record);

	void updateSkinDownLoad(TClientSkin tClientSkin);
}