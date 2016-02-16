package com.mas.market.mapper;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TAppFile;
import com.mas.market.service.TAppFileService;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TAppFileMapper {
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
    int insert(TAppFile record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TAppFile record);

    /**
     * 根据条件查询记录集
     */
    List<TAppFile> selectByExample(Criteria example);

    /**
     * 根据主键查询记录
     */
    TAppFile selectByPrimaryKey(Integer id);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByExampleSelective(@Param("record") TAppFile record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByExample(@Param("record") TAppFile record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TAppFile record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TAppFile record);

	List<TAppFile> getApkForUpgrade(Criteria c);

	TAppFile getApkPatch(Criteria criteria);

	List<TAppFile> getZappForUpgrade(Criteria c);

	List<TAppFile> getApkForAppId(Integer appId);
	
	/**
     * 公用获取apk更新接口
     */
	List<TAppFile> getCommonApkUpgrade(Criteria c);
	public List<TAppFile> getDownloadInfo(Criteria example);
}