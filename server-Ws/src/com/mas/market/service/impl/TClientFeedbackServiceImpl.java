package com.mas.market.service.impl;

import com.mas.market.mapper.TClientFeedbackMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TClientFeedback;
import com.mas.market.service.TClientFeedbackService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TClientFeedbackServiceImpl implements TClientFeedbackService {
    @Autowired
    private TClientFeedbackMapper tClientFeedbackMapper;

    private static final Logger logger = LoggerFactory.getLogger(TClientFeedbackServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tClientFeedbackMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TClientFeedback selectByPrimaryKey(Integer id) {
        return this.tClientFeedbackMapper.selectByPrimaryKey(id);
    }

    public List<TClientFeedback> selectByExample(Criteria example) {
        return this.tClientFeedbackMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TClientFeedback record) {
        return this.tClientFeedbackMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TClientFeedback record) {
        return this.tClientFeedbackMapper.updateByPrimaryKey(record);
    }

    public int insert(TClientFeedback record) {
        return this.tClientFeedbackMapper.insert(record);
    }

    public int insertSelective(TClientFeedback record) {
        return this.tClientFeedbackMapper.insertSelective(record);
    }

	@Override
	public void updateLookoverForClient(Criteria cr) {
		// TODO Auto-generated method stub
		this.tClientFeedbackMapper.updateLookoverForClient(cr);
	}
}