package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.ImageAlbumTheme;
import com.mas.rave.main.vo.ImageAlbumThemeExample;

/**
 * image信息数据访问接口
 * 
 * @author jieding
 * 
 */
public interface ImageAlbumThemeService {
	static class ImageAlbumThemeCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public ImageAlbumThemeCriteria nameLike(String name) {
			params.put(1, name);
			return this;
		}
		public ImageAlbumThemeCriteria nameCnLike(String nameCn) {
			params.put(2, nameCn);
			return this;
		}
		public ImageAlbumThemeCriteria adnState(Integer state) {
			params.put(3, state);
			return this;
		}
		public ImageAlbumThemeCriteria raveIdEqual(Integer raveId) {
			params.put(4, raveId);
			return this;
		}
		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

	/**
	 * 分页显示Image主题信息
	 * 
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PaginationVo<ImageAlbumTheme> searchImageAlbumThemes(ImageAlbumThemeCriteria criteria, int currentPage, int pageSize);

	public List<ImageAlbumTheme> searchImageAlbumTheme(ImageAlbumThemeExample example);

	// 查看单个Image主题信息
	public ImageAlbumTheme getImageAlbumTheme(long id);
	/**
	 * 根据name查看Image主题信息
	 */
	List<ImageAlbumTheme>  selectByThemeName(ImageAlbumTheme record);
	/**
	 * 根据nameCn查看Image主题信息
	 */
	List<ImageAlbumTheme>  selectByThemeNameCn(String nameCn);

	// 获取所有Image主题信息
	public List<ImageAlbumTheme> getAllImageAlbumThemes();

	// 增加Image主题信息
	public void addImageAlbumTheme(ImageAlbumTheme imageAlbumTheme);


	// 更新Image主题信息
	public void upImageAlbumTheme(ImageAlbumTheme imageAlbumTheme);

	// 删除Image主题信息
	public void delImageAlbumTheme(Integer id);

	void batchDelete(Integer[] ids);

	/**
	 * 根据id更新排序信息
	 * @param imageAlbumTheme
	 */
	public void updateSortByPrimarykey(ImageAlbumTheme imageAlbumTheme);
	
}
