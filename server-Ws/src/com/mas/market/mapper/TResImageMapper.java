package com.mas.market.mapper;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TResImage;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface TResImageMapper {
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
    int insert(TResImage record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TResImage record);

    /**
     * 根据条件查询记录集
     */
    List<TResImage> selectByExample(Criteria example);

    /**
     * 根据主键查询记录
     */
    TResImage selectByPrimaryKey(Integer id);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByExampleSelective(@Param("record") TResImage record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByExample(@Param("record") TResImage record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TResImage record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TResImage record);

	void updateDownLoad(Criteria cr);

	List<TResImage> categorylist(Criteria cr);

	List<TResImage> searchImage(Criteria cr);

	List<TResImage> keywordImageList(Criteria cr);

	List<TResImage> searchImageTip(Criteria cr);
	
	List<TResImage> searchImageByArray(Criteria cr);
}