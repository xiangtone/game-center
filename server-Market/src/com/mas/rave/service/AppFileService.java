package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.main.vo.AppFile;

/**
 * app对应文件信息数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface AppFileService {
	static class AppFileCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public AppFileCriteria appIdEqualTo(Integer appId) {
			params.put(1, appId);
			return this;
		}

		public AppFileCriteria channelIdEqualTo(Integer channelId) {
			params.put(1, channelId);
			return this;
		}

		public AppFileCriteria pacIdEqualTo(String pack) {
			params.put(3, pack);
			return this;
		}

		public AppFileCriteria appNameLike(String appName) {
			params.put(4, appName);
			return this;
		}

		public AppFileCriteria raveIdEqual(int raveId) {
			params.put(5, raveId);
			return this;
		}

		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

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

	public List<AppFile> searchAppFiles(AppFileCriteria criteria);

	// app对应所有文件信息
	public List<AppFile> getAppFiles(Integer appId);

	// app对应所有文件信息
	public List<AppFile> getAppFilesGroupBy(Integer appId);

	// app对应所有文件信息
	public List<AppFile> getAppFiles();

	// 查看单个app文件信息
	public AppFile getAppFile(int id);

	// 根据包名获取文件
	public AppFile getAppFileByPac(String pack);

	// 根据包名获取文件
	public List<AppFile> getAppFileByPacs(String pack, Integer appId, Integer channleId, Integer raveId);

	// 根据app及渠道查看单个app文件信息
	public AppFile getAppFileByappIdAndCountry(Integer appId, Integer raveId, Integer channelId);

	// 增加app文件信息
	public int addAppFile(AppFile appFile);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	public int insertSelective(AppFile record);

	// 更新app文件信息
	public int upAppFile(AppFile appFile);

	// 删除app文件信息
	public void delAppFile(int id);

	// 删除app文件信息
	public void delAppFiles(int id);

	public int updateAppFileState(AppFile appFile);

	void batchDelete(Integer[] ids);
	// 更新app信息 t_appcollection_res add by dingjie
	public void upAppCollectionRes(AppFile appFile);
	// 更新app信息
	public void upAppAlbumRes(AppFile appFile);

	// 更新app分发信息temp表 add by dingjie
	public void upAppAlbumResTemp(AppFile appFile);

	/**
	 * app信息变动通知更新cpId
	 * 
	 * @param appId
	 * @param cpId
	 * @return
	 */
	public int upAppInfoByAppId(int appId, int cpId, String appName);

	/**
	 * 获取appFile信息
	 * 
	 * @param channelId
	 *            子渠道ID
	 * @param packageName
	 *            app的包名
	 * @return
	 */
	public AppFile finAppFile(int channelId, String packageName);

	/**
	 * 获取appFile信息
	 * 
	 * @param channelId
	 *            子渠道ID
	 * @param packageName
	 *            app的包名
	 * @return
	 */
	public AppFile finAppFileByName(int channelId, String appName);

	/**
	 * 根据app名称获取app文件
	 * 
	 * @param appName
	 * @return
	 */
	public List<AppFile> getAppFileByName(String appName);

	public void updateResourceByApkId(int apkId);

	// 获取未审核的应用
	public List<AppFile> getAllCpFile();

}
