package com.mas.rave.service;

import java.util.List;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.OwnAppFile;
import com.mas.rave.main.vo.OwnAppFileList;

/**
 * app对应历史文件信息数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface OwnAppFileListService {
	/**
	 * 分页显示app历史文件信息
	 * 
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */

	public PaginationVo<OwnAppFileList> searchOwnAppFileLists(OwnAppFileList criteria, int currentPage, int pageSize);

	public List<OwnAppFileList> searchOwnAppFileLists(OwnAppFileList criteria);

	// app对应所有历史文件信息
	public List<OwnAppFileList> getOwnAppFileLists(Integer appId);

	// app对应所有历史文件信息
	public List<OwnAppFileList> getOwnAppFileLists();

	// 查看单个app历史文件信息
	public OwnAppFileList getOwnAppFileList(int id);

	// 增加app历史文件信息
	public int addOwnAppFileList(OwnAppFileList obj);

	// 更新app历史文件信息
	public int upOwnAppFileList(OwnAppFileList obj);

	// 删除app历史文件信息
	public void delOwnAppFileList(int id);

	void batchDelete(Integer[] ids);

	// 转换文件
	public void convertFile(OwnAppFile file);

	public OwnAppFileList checkFile(String pac, int versionCode, int apkId);
}

