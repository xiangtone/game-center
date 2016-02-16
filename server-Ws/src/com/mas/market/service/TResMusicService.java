package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TResMusic;

import java.util.List;

public interface TResMusicService {
    int countByExample(Criteria example);

    TResMusic selectByPrimaryKey(Integer id);

    List<TResMusic> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TResMusic record);

    int updateByPrimaryKey(TResMusic record);

    int insert(TResMusic record);

    int insertSelective(TResMusic record);

	void updateDownLoad(Criteria cr);

	List<TResMusic> categorylist(Criteria cr);

	List<TResMusic> searchMusic(Criteria cr);

	List<TResMusic> keywordMusicList(Criteria cr);

	List<TResMusic> searchMusicTip(Criteria cr);
	
	List<TResMusic> searchMusicByArray(Criteria cr);
}