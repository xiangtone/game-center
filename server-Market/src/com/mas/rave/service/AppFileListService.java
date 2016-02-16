package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.AppFileList;

/**
 * app对应历史文件信息数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface AppFileListService {
	static class AppFileListCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public AppFileListCriteria appIdEqualTo(Integer appId) {
			params.put(1, appId);
			return this;
		}

		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

	/**
	 * 分页显示app历史文件信息
	 * 
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */

	public PaginationVo<AppFileList> searchAppFileLists(AppFileListCriteria criteria, int currentPage, int pageSize);

	public List<AppFileList> searchAppFileLists(AppFileListCriteria criteria);

	// app对应所有历史文件信息
	public List<AppFileList> getAppFileLists(Integer appId);

	// app对应所有历史文件信息
	public List<AppFileList> getAppFileLists();

	// 查看单个app历史文件信息
	public AppFileList getAppFileList(int id);

	// 增加app历史文件信息
	public int addAppFileList(AppFileList obj);

	// 更新app历史文件信息
	public int upAppFileList(AppFileList obj);

	// 删除app历史文件信息
	public void delAppFileList(int id);

	void batchDelete(Integer[] ids);

	// 转换文件
	public void convertFile(AppFile file);
	
	public AppFileList checkFile(String pac,int versionCode,int apkId);
}
