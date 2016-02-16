package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TAppAlbumColumn;

import java.util.List;

public interface TAppAlbumColumnService {
    int countByExample(Criteria example);

    TAppAlbumColumn selectByPrimaryKey(Integer columnId);

    List<TAppAlbumColumn> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TAppAlbumColumn record);

    int updateByPrimaryKey(TAppAlbumColumn record);

    int insert(TAppAlbumColumn record);

    int insertSelective(TAppAlbumColumn record);
}