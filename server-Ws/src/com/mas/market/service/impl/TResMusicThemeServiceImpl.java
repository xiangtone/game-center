package com.mas.market.service.impl;

import com.mas.market.mapper.TResMusicThemeMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TResMusicTheme;
import com.mas.market.service.TResMusicThemeService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TResMusicThemeServiceImpl implements TResMusicThemeService {
    @Autowired
    private TResMusicThemeMapper tResMusicThemeMapper;

    private static final Logger logger = LoggerFactory.getLogger(TResMusicThemeServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tResMusicThemeMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TResMusicTheme selectByPrimaryKey(Integer themeId) {
        return this.tResMusicThemeMapper.selectByPrimaryKey(themeId);
    }

    public List<TResMusicTheme> selectByExample(Criteria example) {
        return this.tResMusicThemeMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TResMusicTheme record) {
        return this.tResMusicThemeMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TResMusicTheme record) {
        return this.tResMusicThemeMapper.updateByPrimaryKey(record);
    }

    public int insert(TResMusicTheme record) {
        return this.tResMusicThemeMapper.insert(record);
    }

    public int insertSelective(TResMusicTheme record) {
        return this.tResMusicThemeMapper.insertSelective(record);
    }

	@Override
	public List<TResMusicTheme> selectTheme(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tResMusicThemeMapper.selectTheme(cr);
	}
}