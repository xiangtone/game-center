package com.mas.market.mapper;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TResMusicAlbumRes;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface TResMusicAlbumResMapper {
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
    int insert(TResMusicAlbumRes record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TResMusicAlbumRes record);

    /**
     * 根据条件查询记录集
     */
    List<TResMusicAlbumRes> selectByExample(Criteria example);

    /**
     * 根据主键查询记录
     */
    TResMusicAlbumRes selectByPrimaryKey(Integer id);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByExampleSelective(@Param("record") TResMusicAlbumRes record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByExample(@Param("record") TResMusicAlbumRes record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TResMusicAlbumRes record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TResMusicAlbumRes record);

	List<TResMusicAlbumRes> selectColunmList(Criteria cr);
}