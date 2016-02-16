package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TAppComment;

import java.util.List;

public interface TAppCommentService {
    int countByExample(Criteria example);

    TAppComment selectByPrimaryKey(Integer id);

    List<TAppComment> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TAppComment record);

    int updateByPrimaryKey(TAppComment record);

    int insert(TAppComment record);

    int insertSelective(TAppComment record);

	List<TAppComment> selectByStarsGroup(Criteria cr);

}