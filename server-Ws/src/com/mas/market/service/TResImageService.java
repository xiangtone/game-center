package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TResImage;

import java.util.List;

public interface TResImageService {
    int countByExample(Criteria example);

    TResImage selectByPrimaryKey(Integer id);

    List<TResImage> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TResImage record);

    int updateByPrimaryKey(TResImage record);

    int insert(TResImage record);

    int insertSelective(TResImage record);

	void updateDownLoad(Criteria cr);

	List<TResImage> categorylist(Criteria cr);

	List<TResImage> searchImage(Criteria cr);

	List<TResImage> keywordImageList(Criteria cr);

	List<TResImage> searchImageTip(Criteria cr);
	
	List<TResImage> searchImageByArray(Criteria cr);
}