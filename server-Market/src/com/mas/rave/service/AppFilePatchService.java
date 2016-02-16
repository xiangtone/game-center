package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.main.vo.AppFilePatch;

/**
 * AppFilePatchPatch对应文件信息数据访问接口
 * 
 * @author lin.lu
 * 
 */
public interface AppFilePatchService {
	static class AppFilePatchCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public AppFilePatchCriteria appIdEqualTo(Integer appId) {
			params.put(1, appId);
			return this;
		}

		public AppFilePatchCriteria channelIdEqualTo(Integer channelId) {
			params.put(1, channelId);
			return this;
		}

		public AppFilePatchCriteria pacIdEqualTo(String pack) {
			params.put(3, pack);
			return this;
		}

		public AppFilePatchCriteria appNameLike(String appName) {
			params.put(4, appName);
			return this;
		}

		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

	// app对应所有文件信息
	public List<AppFilePatch> getAppFilePatchs(Integer apkId);

	// 增加app文件信息
	public int addAppFilePatch(AppFilePatch appFilePatch);

	// 删除app文件信息
	public void delAppFilePatch(int id);

	void batchDelete(Integer[] ids);

	// 获取对应差异包
	public AppFilePatch getAppFilePtchByApkId(int apkId,int lowVersionCode);
}
