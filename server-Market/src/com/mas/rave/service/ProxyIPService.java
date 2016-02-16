package com.mas.rave.service;

import java.util.List;

import com.mas.rave.main.vo.ProxyIP;

public interface ProxyIPService {
	
	public int getProxyIpCount();
	
	/**
	 * 获取当前总页数
	 */
	public int countByExample(ProxyIP example);
	
	/**
	 * 增加IP
	 */
	public int insert(ProxyIP record);

	/**
	 * 分页查询IP
	 */
	public List<ProxyIP> selectByExample(ProxyIP example);

	/**
	 * 根据id查看IP
	 */
	public ProxyIP selectByPrimaryKey(long id);
	/**
	 * 根据ip查询
	 * @param ip
	 * @return
	 */
	public List<ProxyIP>  selectByIP(String ip);
	/**
	 * 根据Id删除IP
	 * @param id
	 * @return
	 */
	public void deleteByPrimaryKey(long id);
	/**
	 * 获取所有的IP
	 * @return
	 */
	public List<String[]> getIpPort();
	/**
	 * 获取所有的ProxyIP
	 * @return
	 */
	public List <ProxyIP> getAllIPConfigs();
	
	/**
	 * 根据ip删除
	 * @return
	 */
	public void deleteByIP (String ip);

}
