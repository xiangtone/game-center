package com.mas.rave.service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.ClientSkin;

/**
 * app专辑信息数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface ClientSkinService {

	public int countByExample(ClientSkin example);

	public int deleteByPrimaryKey(Integer id);

	public int insert(ClientSkin record);

	public PaginationVo<ClientSkin> searchClientSkin(ClientSkin criteria, int currentPage, int pageSize);

	public ClientSkin selectByPackName(String pac_name);

	public ClientSkin selectByPrimaryKey(Integer id);

	public int updateByPrimaryKey(ClientSkin record);
	
	public void batchDelete(Integer[] ids);
	
	/**
	 * 更新排序
	 * 
	 * @param record
	 */
	public void updateSortByPrimarykey(ClientSkin record);

}
