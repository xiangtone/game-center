package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TAppCollection;

import java.util.List;

public interface TAppCollectionService {
    int countByExample(Criteria example);

    TAppCollection selectByPrimaryKey(Integer collectionId);

    List<TAppCollection> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TAppCollection record);

    int updateByPrimaryKey(TAppCollection record);

    int insert(TAppCollection record);

    int insertSelective(TAppCollection record);

	List<TAppCollection> collection(Criteria cr);
}