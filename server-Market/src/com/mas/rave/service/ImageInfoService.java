package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.ImageInfo;

/**
 * image信息数据访问接口
 * 
 * @author jieding
 * 
 */
public interface ImageInfoService {
	static class ImageCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public ImageCriteria nameLike(String name) {
			params.put(2, name);
			return this;
		}

		public ImageCriteria adnState(Integer state) {
			params.put(3, state);
			return this;
		}

		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

	/**
	 * 分页显示Image信息
	 * 
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PaginationVo<ImageInfo> searchImages(ImageInfo criteria, int currentPage, int pageSize);

	public ImageInfo searchImageInfo(ImageInfo criteria);

	// 查看单个image信息
	public ImageInfo getImageInfo(long id);
	public List<ImageInfo> selectByName(String name);
	// 获取所有image信息
	public List<ImageInfo> getAllImageInfos();

	// 增加image信息
	public int addImageInfo(ImageInfo image, Integer categoryId);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	public int insertSelective(ImageInfo record);

	// 更新image信息
	public void upImageInfo(ImageInfo image, Integer categoryId);

	// 删除image信息
	public void delImageInfo(Integer id);

	void batchDelete(Integer[] ids);

	// 更新image信息
	public void upImageInfoAlbumRes(ImageInfo imageInfo);
	
	public int getImageInfoCountByCategory(int categoryId);
}
