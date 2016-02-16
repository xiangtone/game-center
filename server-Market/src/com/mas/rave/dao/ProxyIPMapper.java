package com.mas.rave.dao;

import java.util.List;

import com.mas.rave.main.vo.ProxyIP;

/**
 * IP数据访问接口
 * 
 * @author jieding
 * 
 */
public interface ProxyIPMapper {

	/**
	 * IP总数
	 * @return
	 */
	int getProxyIpCount();
	/**
	 * 获取当前总页数
	 */
	int countByExample(ProxyIP example);
	
	/**
	 * 增加IP
	 */
	int insert(ProxyIP record);

	/**
	 * 分页查询IP
	 */
	List<ProxyIP> selectByExample(ProxyIP example);

	/**
	 * 根据id查看IP
	 */
	ProxyIP selectByPrimaryKey(long id);
	/**
	 * 根据ip查询
	 * @param ip
	 * @return
	 */
	List<ProxyIP>  selectByIP(String ip);
	/**
	 * 根据Id删除IP
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(long id);
	
	int deleteByIP (String ip);
	/**
	 * 获取所有的IP
	 * @return
	 */
	public List<ProxyIP> getAllIPConfigs();
	
}