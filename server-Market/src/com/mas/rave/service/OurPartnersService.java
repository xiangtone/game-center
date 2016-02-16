package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.OurPartners;

/**
 * 广告合作　
 * 
 * @author liwei.sz
 * 
 */
public interface OurPartnersService {

	static class OurPartnersCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public OurPartnersCriteria nameLike(String name) {
			params.put(1, name);
			return this;
		}

		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

	/**
	 * 分页显示广告合作信息
	 * 
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */

	public PaginationVo<OurPartners> searchOurPartners(OurPartnersCriteria criteria, int currentPage, int pageSize);

	// 所有广告合作信息
	public List<OurPartners> getOurPartners();

	// 获取单个广告合伯信息
	public OurPartners getOurPartners(int id);

	// 增加广告合作信息
	public int addAppOurPartners(OurPartners obj);

	// 更新广告合作信息
	public int upOurPartners(OurPartners obj);

	// 删除广告合伯信息
	public void delAppOurPartners(int id);

	public void batchDelete(Integer[] ids);
	
	/**
	 * 更新排序
	 * 
	 * @param record
	 */
	public void updateSortByPrimarykey(OurPartners record);

}
