package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TResImageAlbumRes;

import java.util.List;

public interface TResImageAlbumResService {
    int countByExample(Criteria example);

    TResImageAlbumRes selectByPrimaryKey(Integer id);

    List<TResImageAlbumRes> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TResImageAlbumRes record);

    int updateByPrimaryKey(TResImageAlbumRes record);

    int insert(TResImageAlbumRes record);

    int insertSelective(TResImageAlbumRes record);
}