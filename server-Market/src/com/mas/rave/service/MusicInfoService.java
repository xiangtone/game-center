package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.MusicInfo;

/**
 * music信息数据访问接口
 * 
 * @author jieding
 * 
 */
public interface MusicInfoService {
	static class MusicCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public MusicCriteria nameLike(String name) {
			params.put(2, name);
			return this;
		}

		public MusicCriteria adnState(Integer state) {
			params.put(3, state);
			return this;
		}

		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

	/**
	 * 分页显示music信息
	 * 
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PaginationVo<MusicInfo> searchMusics(MusicInfo criteria, int currentPage, int pageSize);

	public MusicInfo searchMusicInfo(MusicInfo criteria);

	// 查看单个music信息
	public MusicInfo getMusicInfo(long id);

	public List<MusicInfo> selectByName(String name);
	
	// 获取所有music信息
	public List<MusicInfo> getAllMusicInfos();

	// 增加music信息
	public int addMusicInfo(MusicInfo music, Integer categoryId);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	public int insertSelective(MusicInfo record);

	// 更新music信息
	public void upMusicInfo(MusicInfo music, Integer categoryId);

	// 删除music信息
	public void delMusicInfo(Integer id);

	void batchDelete(Integer[] ids);

	// 更新music信息
	public void upMusicInfoAlbumRes(MusicInfo musicInfo);
	
	public int getMusicInfoCountByCategory(int categoryId);

	
}
