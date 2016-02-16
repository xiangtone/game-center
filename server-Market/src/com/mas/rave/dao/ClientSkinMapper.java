package com.mas.rave.dao;

import java.util.List;

import com.mas.rave.main.vo.ClientSkin;

/**
 * app分发数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface ClientSkinMapper {

	int countByExample(ClientSkin example);

	int deleteByPrimaryKey(Integer id);

	int insert(ClientSkin record);

	List<ClientSkin> selectByExample(ClientSkin example);

	ClientSkin selectByPackName(String pac_name);

	ClientSkin selectByPrimaryKey(Integer id);

	int updateByPrimaryKey(ClientSkin record);

	/**
	 * 更新排序
	 * 
	 * @param record
	 */
	void updateSortByPrimarykey(ClientSkin record);
}