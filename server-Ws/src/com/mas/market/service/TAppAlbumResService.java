package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TAppAlbumRes;

import java.util.List;
import java.util.Map;

public interface TAppAlbumResService {
    int countByExample(Criteria example);

    TAppAlbumRes selectByPrimaryKey(Integer id);

    List<TAppAlbumRes> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TAppAlbumRes record);

    int updateByPrimaryKey(TAppAlbumRes record);

    int insert(TAppAlbumRes record);

    int insertSelective(TAppAlbumRes record);

	List<TAppAlbumRes> searchApps(Criteria cr);

	List<TAppAlbumRes> searchRecommend(Criteria cr);

	TAppAlbumRes appDetailByApkId(Integer id);

	List<TAppAlbumRes> categorylist(Criteria cr);

	void updateAppDownLoad(Criteria cr);

	List<TAppAlbumRes> getAppsForUpdate(Map<String, Object> map);

	void updateAppOpenLog(Integer appId);

	List<TAppAlbumRes> columnlist(Criteria cr);

	List<TAppAlbumRes> selectByRAND(Criteria cr);

	List<TAppAlbumRes> keywordAppsList(Criteria cr);

	List<TAppAlbumRes> searchAppsTip(Criteria cr);

	TAppAlbumRes appDetailByPackageName(Criteria cr);

	TAppAlbumRes appDetailByAppId(Criteria cr);

	List<TAppAlbumRes> collectionlist(Criteria cr);

	List<TAppAlbumRes> musthave(Criteria cr);

	List<TAppAlbumRes> columnlistByLiveWallpaper(Criteria cr);

	TAppAlbumRes getAppInfoByAppId(Integer appId);
	
	List<TAppAlbumRes> searchSameIssuerRecommend(Criteria cr);
	
	List<TAppAlbumRes> searchAppsByArray(Criteria cr);
	
	//apps and games 通过category拿hot 2015-10-28
	List<TAppAlbumRes> categoryHotList(Criteria cr);
}