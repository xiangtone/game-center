package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TAppFile;

import java.util.List;

public interface TAppFileService {
    int countByExample(Criteria example);

    TAppFile selectByPrimaryKey(Integer id);

    List<TAppFile> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TAppFile record);

    int updateByPrimaryKey(TAppFile record);

    int insert(TAppFile record);

    int insertSelective(TAppFile record);

	List<TAppFile> getApkForUpgrade(Criteria c);

	TAppFile getApkPatch(Criteria criteria);

	List<TAppFile> getZappForUpgrade(Criteria c);

	List<TAppFile> getApkForAppId(Integer appId);
	
	List<TAppFile> getCommonApkUpgrade(Criteria c);
	
	List<TAppFile> getDownloadInfo(Criteria example);
}