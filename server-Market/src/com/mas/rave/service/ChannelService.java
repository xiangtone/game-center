package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.Channel;

/**
 * 渠道信息数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface ChannelService {
	static class ChannelCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();
		
		public ChannelCriteria idEqualTo(Integer channelId){
			params.put(1,channelId);
			return this;
		}
		public ChannelCriteria fatherIdEqualTo(Integer fatherId){
			params.put(2,fatherId);
			return this;
		}
		public Map<Integer,Object> getParams(){
			return Collections.unmodifiableMap(params);
		}
	}

	/**
	 * 分页显示渠道信息
	 * 
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PaginationVo<Channel> searchChannels(ChannelCriteria criteria,
			int currentPage, int pageSize);

	// 查找所有一级或二级分类
	public List<Channel> getChannels(Integer fatherId);

	// 获取所有渠道信息(根据条件查看)
	public List<Channel> getAllChannels(int type);
	

	// 查看单个渠道信息
	public Channel getChannel(int id);

	// 增加渠道信息
	public void addChannel(Channel Channel,Integer provinceId);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	public int insertSelective(Channel record);

	// 更新渠道信息
	public void upChannel(Channel channel,Integer provinceId);

	// 删除渠道信息
	public void delChannel(Integer id);

	void batchDelete(Integer[] ids);
	
	public void updateSortByPrimarykey(Channel channel);
	
	int deletSecondChannel(int fatherId);
}
