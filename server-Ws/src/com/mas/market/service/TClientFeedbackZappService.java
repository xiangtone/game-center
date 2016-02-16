package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TClientFeedbackZapp;

import java.util.List;

public interface TClientFeedbackZappService {
    int countByExample(Criteria example);

    TClientFeedbackZapp selectByPrimaryKey(Integer id);

    List<TClientFeedbackZapp> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TClientFeedbackZapp record);

    int updateByPrimaryKey(TClientFeedbackZapp record);

    int insert(TClientFeedbackZapp record);

    int insertSelective(TClientFeedbackZapp record);
}