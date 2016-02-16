package com.reportforms.dao;

import com.reportforms.model.Country;


public interface CountryMapper<T> extends BaseMapper<T, Long> {

	/**
	 * 根据id查看国家文件信息
	 */
	Country selectByPrimaryKey(int id);
}
