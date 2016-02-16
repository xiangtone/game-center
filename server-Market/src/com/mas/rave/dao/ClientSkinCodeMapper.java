package com.mas.rave.dao;

import com.mas.rave.main.vo.ClientSkinCode;

/**
 * app分发数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface ClientSkinCodeMapper {

	void updateTime(Integer id);

	ClientSkinCode selectByExample();
}