package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TClientFeedbackZappcode;

import java.util.List;

public interface TClientFeedbackZappcodeService {
    int countByExample(Criteria example);

    TClientFeedbackZappcode selectByPrimaryKey(Integer id);

    List<TClientFeedbackZappcode> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TClientFeedbackZappcode record);

    int updateByPrimaryKey(TClientFeedbackZappcode record);

    int insert(TClientFeedbackZappcode record);

    int insertSelective(TClientFeedbackZappcode record);
}