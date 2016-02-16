package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TCategory;

import java.util.List;

public interface TCategoryService {
    int countByExample(Criteria example);

    TCategory selectByPrimaryKey(Integer id);

    List<TCategory> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TCategory record);

    int updateByPrimaryKey(TCategory record);

    int insert(TCategory record);

    int insertSelective(TCategory record);

	List<TCategory> selectByCtRaveId(Criteria cr);
	
	List<TCategory> selectLevelCat(Criteria cr);
	
	List<Integer> selectAllCatIds(Criteria cr);
	
	List<TCategory> selectByAllCatIds(Criteria cr);
}