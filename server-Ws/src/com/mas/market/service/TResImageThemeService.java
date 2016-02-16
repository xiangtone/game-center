package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TResImageTheme;

import java.util.List;

public interface TResImageThemeService {
    int countByExample(Criteria example);

    TResImageTheme selectByPrimaryKey(Integer themeId);

    List<TResImageTheme> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TResImageTheme record);

    int updateByPrimaryKey(TResImageTheme record);

    int insert(TResImageTheme record);

    int insertSelective(TResImageTheme record);

	List<TResImageTheme> selectTheme(Criteria cr);
}