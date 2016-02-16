package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TClientSkinCode;

import java.util.List;

public interface TClientSkinCodeService {
    int countByExample(Criteria example);

    TClientSkinCode selectByPrimaryKey(Integer id);

    List<TClientSkinCode> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TClientSkinCode record);

    int updateByPrimaryKey(TClientSkinCode record);

    int insert(TClientSkinCode record);

    int insertSelective(TClientSkinCode record);
}