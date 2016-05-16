package com.hykj.gamecenter.adapter;

import android.view.View;

/**
 * 一个接口，定义了不用notifyDataChangle去刷新整个adapter, 只需要根据key来查找某个view,进行单独更新，可用于下载进度更新等
 * 
 * @author froyohuang
 * 
 * @param <K>
 */
public interface IUpdateSingleView<K> {

	/**
	 * 根据 Key值获取一个view
	 * 
	 * @param key
	 * @return
	 */
	View getViewByKey(K key);

	/**
	 * 添加一个映射关系
	 * 
	 * @param key
	 * @param view
	 */
	void setViewForSingleUpdate(K key, View view);

	/**
	 * 删除一个映射关系
	 * 
	 * @param view
	 */
	void removeViewForSingleUpdate(View view);

	/**
	 * 删除一个映射关系
	 * 
	 * @param key
	 */
	void removeViewForSingleUpdate(K key);
}
