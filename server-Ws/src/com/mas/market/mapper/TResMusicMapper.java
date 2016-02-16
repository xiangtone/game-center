package com.mas.market.mapper;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TResMusic;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface TResMusicMapper {
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
    int insert(TResMusic record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TResMusic record);

    /**
     * 根据条件查询记录集
     */
    List<TResMusic> selectByExample(Criteria example);

    /**
     * 根据主键查询记录
     */
    TResMusic selectByPrimaryKey(Integer id);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByExampleSelective(@Param("record") TResMusic record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByExample(@Param("record") TResMusic record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TResMusic record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TResMusic record);

	void updateDownLoad(Criteria cr);

	List<TResMusic> categorylist(Criteria cr);

	List<TResMusic> searchMusic(Criteria cr);

	List<TResMusic> keywordMusicList(Criteria cr);

	List<TResMusic> searchMusicTip(Criteria cr);
	
	List<TResMusic> searchMusicByArray(Criteria cr);
}