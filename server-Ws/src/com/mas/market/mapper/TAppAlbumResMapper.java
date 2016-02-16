package com.mas.market.mapper;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TAppAlbumRes;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TAppAlbumResMapper {
    /**
     * 根据条件查询记录总数
     */
    int countByExample(Criteria example);

    /**
     * 根据条件删除记录
     */
    int deleteByExample(Criteria example);

    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TAppAlbumRes record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TAppAlbumRes record);

    /**
     * 根据条件查询记录集
     */
    List<TAppAlbumRes> selectByExample(Criteria example);

    /**
     * 根据主键查询记录
     */
    TAppAlbumRes selectByPrimaryKey(Integer id);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByExampleSelective(@Param("record") TAppAlbumRes record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByExample(@Param("record") TAppAlbumRes record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TAppAlbumRes record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TAppAlbumRes record);

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
	
	List<TAppAlbumRes> categoryHotList(Criteria cr);
}