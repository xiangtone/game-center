package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TClientFeedback;

import java.util.List;

public interface TClientFeedbackService {
    int countByExample(Criteria example);

    TClientFeedback selectByPrimaryKey(Integer id);

    List<TClientFeedback> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TClientFeedback record);

    int updateByPrimaryKey(TClientFeedback record);

    int insert(TClientFeedback record);

    int insertSelective(TClientFeedback record);

	void updateLookoverForClient(Criteria cr);
}