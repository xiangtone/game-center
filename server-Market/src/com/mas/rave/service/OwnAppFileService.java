package com.mas.rave.service;

import java.util.HashMap;
import java.util.List;

import com.mas.rave.main.vo.OwnAppFile;

/**
 * app对应文件信息数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface OwnAppFileService {
	/**
	 * 分页显示app文件信息
	 * 
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	/*
	 * public PaginationVo<AppPic> searchAppPics(AppFileCriteria criteria,
	 * Integer appId, int currentPage, int pageSize);
	 */

	public List<OwnAppFile> searchOwnAppFiles(OwnAppFile criteria);

	// app对应所有文件信息
	public List<OwnAppFile> getOwnAppFiles(Integer appId);

	// 增加app文件信息
	public int addOwnAppFile(OwnAppFile appFile);

	// 更新app文件信息
	public int upOwnAppFile(OwnAppFile appFile);

	// 删除app文件信息
	public void delOwnAppFile(int id);

	public int updateOwnAppFileState(OwnAppFile appFile);

	void batchDelete(Integer[] ids);

	List<OwnAppFile> getByParameter(HashMap<String, Object> map);

	/**
	 * 根据id查看app文件信息
	 */
	OwnAppFile selectByPrimaryKey(int id);

}
