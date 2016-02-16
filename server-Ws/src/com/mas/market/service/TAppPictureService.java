package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TAppPicture;

import java.util.List;

public interface TAppPictureService {
    int countByExample(Criteria example);

    TAppPicture selectByPrimaryKey(Integer id);

    List<TAppPicture> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TAppPicture record);

    int updateByPrimaryKey(TAppPicture record);

    int insert(TAppPicture record);

    int insertSelective(TAppPicture record);
}