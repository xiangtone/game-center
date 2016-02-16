package com.mas.market.mapper;

import com.mas.market.pojo.TAppInfo;


public interface TAppInfoMapper {

    /**
     * 根据主键查询记录
     */
	TAppInfo selectByPrimaryKey(Integer id);

   
}