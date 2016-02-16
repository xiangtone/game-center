package com.mas.market.service.impl;

import com.mas.market.mapper.TResImageThemeMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TResImageTheme;
import com.mas.market.service.TResImageThemeService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TResImageThemeServiceImpl implements TResImageThemeService {
    @Autowired
    private TResImageThemeMapper tResImageThemeMapper;

    private static final Logger logger = LoggerFactory.getLogger(TResImageThemeServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tResImageThemeMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TResImageTheme selectByPrimaryKey(Integer themeId) {
        return this.tResImageThemeMapper.selectByPrimaryKey(themeId);
    }

    public List<TResImageTheme> selectByExample(Criteria example) {
        return this.tResImageThemeMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TResImageTheme record) {
        return this.tResImageThemeMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TResImageTheme record) {
        return this.tResImageThemeMapper.updateByPrimaryKey(record);
    }

    public int insert(TResImageTheme record) {
        return this.tResImageThemeMapper.insert(record);
    }

    public int insertSelective(TResImageTheme record) {
        return this.tResImageThemeMapper.insertSelective(record);
    }

	@Override
	public List<TResImageTheme> selectTheme(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tResImageThemeMapper.selectTheme(cr);
	}
}