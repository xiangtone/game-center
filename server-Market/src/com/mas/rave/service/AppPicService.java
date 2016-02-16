package com.mas.rave.service;

import java.util.List;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.AppPic;

/**
 * appPic信息数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface AppPicService {
	static class AppPicCriteria {
		// private Map<Integer, Object> params = new HashMap<Integer, Object>();
	}

	/**
	 * 分页显示app截图信息
	 * 
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PaginationVo<AppPic> searchAppPics(AppPicCriteria criteria, Integer appId, int currentPage, int pageSize);

	public List<AppPic> getAppPics(Integer appId);

	// 查看单个appPic信息
	public AppPic getAppPic(int id);

	// 增加appPic信息
	public void addAppPic(AppPic appPic);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	public int insertSelective(AppPic record);

	// 更新appPic信息
	public void upAppPic(AppPic appPic);

	// 删除appPic信息
	public void delAppPic(int id);

	// 删除appPic信息
	public void delAppPics(int id);

	void batchDelete(Integer[] ids);

	public void updateSortByPrimarykey(AppPic entity);

}
