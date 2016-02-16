package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TResMusicAlbumRes;

import java.util.List;

public interface TResMusicAlbumResService {
    int countByExample(Criteria example);

    TResMusicAlbumRes selectByPrimaryKey(Integer id);

    List<TResMusicAlbumRes> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TResMusicAlbumRes record);

    int updateByPrimaryKey(TResMusicAlbumRes record);

    int insert(TResMusicAlbumRes record);

    int insertSelective(TResMusicAlbumRes record);

	List<TResMusicAlbumRes> selectColunmList(Criteria cr);
}