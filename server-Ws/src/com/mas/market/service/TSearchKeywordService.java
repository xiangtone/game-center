package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TSearchKeyword;

import java.util.List;

public interface TSearchKeywordService {
    int countByExample(Criteria example);

    TSearchKeyword selectByPrimaryKey(Integer searchId);

    List<TSearchKeyword> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TSearchKeyword record);

    int updateByPrimaryKey(TSearchKeyword record);

    int insert(TSearchKeyword record);

    int insertSelective(TSearchKeyword record);

	List<TSearchKeyword> selectKeywords(Criteria cr);

	void updateSearchNum(Integer searchId);
}