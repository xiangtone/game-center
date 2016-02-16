package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TAppAlbumTheme;

import java.util.List;

public interface TAppAlbumThemeService {
    int countByExample(Criteria example);

    TAppAlbumTheme selectByPrimaryKey(Integer themeId);

    List<TAppAlbumTheme> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TAppAlbumTheme record);

    int updateByPrimaryKey(TAppAlbumTheme record);

    int insert(TAppAlbumTheme record);

    int insertSelective(TAppAlbumTheme record);

	List<TAppAlbumTheme> hometheme(Criteria cr);
}