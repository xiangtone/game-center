package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TResMusicTheme;

import java.util.List;

public interface TResMusicThemeService {
    int countByExample(Criteria example);

    TResMusicTheme selectByPrimaryKey(Integer themeId);

    List<TResMusicTheme> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TResMusicTheme record);

    int updateByPrimaryKey(TResMusicTheme record);

    int insert(TResMusicTheme record);

    int insertSelective(TResMusicTheme record);

	List<TResMusicTheme> selectTheme(Criteria cr);
}