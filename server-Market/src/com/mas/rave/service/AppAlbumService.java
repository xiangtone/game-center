package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.main.vo.AppAlbum;

/**
 * app分发内容信息数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface AppAlbumService {
	static class AppAlbumCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();
		
		public AppAlbumCriteria appIdEqualTo(Integer appId){
			params.put(1,appId);
			return this;
		}
		
		public AppAlbumCriteria channelIdEqualTo(Integer channelId){
			params.put(1,channelId);
			return this;
		}
		public Map<Integer,Object> getParams(){
			return Collections.unmodifiableMap(params);
		}
	}


	//app分发信息
	public List<AppAlbum> getAppAlbum();

	// 查看单个app分发信息
	public AppAlbum getAppAlbum(int id);

	// 增加app分发信息
	public void addAppAlbum(AppAlbum res);


	// 更新app分发信息
	public void upAppAlbum(AppAlbum res);

	// 删除app分发信息
	public void delAppAlbum(int id);

	void batchDelete(Integer[] ids);

}
