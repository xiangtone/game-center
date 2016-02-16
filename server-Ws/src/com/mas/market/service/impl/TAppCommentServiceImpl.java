package com.mas.market.service.impl;

import com.mas.market.mapper.TAppCommentMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TAppComment;
import com.mas.market.service.TAppCommentService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TAppCommentServiceImpl implements TAppCommentService {
    @Autowired
    private TAppCommentMapper tAppCommentMapper;

    private static final Logger logger = LoggerFactory.getLogger(TAppCommentServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tAppCommentMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TAppComment selectByPrimaryKey(Integer id) {
        return this.tAppCommentMapper.selectByPrimaryKey(id);
    }

    public List<TAppComment> selectByExample(Criteria example) {
        return this.tAppCommentMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TAppComment record) {
        return this.tAppCommentMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TAppComment record) {
        return this.tAppCommentMapper.updateByPrimaryKey(record);
    }

    public int insert(TAppComment record) {
        return this.tAppCommentMapper.insert(record);
    }

    public int insertSelective(TAppComment record) {
        return this.tAppCommentMapper.insertSelective(record);
    }

	@Override
	public List<TAppComment> selectByStarsGroup(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tAppCommentMapper.selectByStarsGroup(cr);
	}

}