package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.MusicAlbumTheme;
import com.mas.rave.main.vo.MusicAlbumThemeExample;

/**
 * Music信息数据访问接口
 * 
 * @author jieding
 * 
 */
public interface MusicAlbumThemeService {
	static class MusicAlbumThemeCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public MusicAlbumThemeCriteria nameLike(String name) {
			params.put(1, name);
			return this;
		}
		public MusicAlbumThemeCriteria nameCnLike(String nameCn) {
			params.put(2, nameCn);
			return this;
		}
		public MusicAlbumThemeCriteria adnState(Integer state) {
			params.put(3, state);
			return this;
		}
		public MusicAlbumThemeCriteria raveIdEqual(Integer raveId) {
			params.put(4, raveId);
			return this;
		}
		
		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

	/**
	 * 分页显示Music主题信息
	 * 
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PaginationVo<MusicAlbumTheme> searchMusicAlbumThemes(MusicAlbumThemeCriteria criteria, int currentPage, int pageSize);

	public List<MusicAlbumTheme> searchMusicAlbumTheme(MusicAlbumThemeExample criteria);

	// 查看单个Music主题信息
	public MusicAlbumTheme getMusicAlbumTheme(long id);

	// 获取所有Music主题信息
	public List<MusicAlbumTheme> getAllMusicAlbumThemes();

	// 增加Music主题信息
	public void addMusicAlbumTheme(MusicAlbumTheme musicAlbumTheme);


	// 更新Music主题信息
	public void upMusicAlbumTheme(MusicAlbumTheme musicAlbumTheme);

	// 删除Music主题信息
	public void delMusicAlbumTheme(Integer id);

	void batchDelete(Integer[] ids);

	/**
	 * 根据id更新排序信息
	 * @param imageAlbumTheme
	 */
	public void updateSortByPrimarykey(MusicAlbumTheme musicAlbumTheme);
	/**
	 * 根据主题名查看主题信息
	 * @param nameCn
	 * @return
	 */
	public List<MusicAlbumTheme> selectByThemeName(MusicAlbumTheme criteria);
	/**
	 * 根据主题中文名查看主题信息
	 * @param nameCn
	 * @return
	 */
	public List<MusicAlbumTheme> selectByThemeNameCn(String nameCn);
}
