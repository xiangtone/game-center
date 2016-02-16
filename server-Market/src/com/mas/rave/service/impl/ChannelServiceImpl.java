package com.mas.rave.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.ChannelMapper;
import com.mas.rave.dao.ProvinceMapper;
import com.mas.rave.main.vo.Channel;
import com.mas.rave.main.vo.ChannelExample;
import com.mas.rave.main.vo.Province;
import com.mas.rave.service.ChannelService;

/**
 * 渠道
 * 
 * @author liwei.sz
 * 
 */

@Service
public class ChannelServiceImpl implements ChannelService {

	@Autowired
	private ChannelMapper channelMapper;

	@Autowired
	private ProvinceMapper provinceMapper;

	public PaginationVo<Channel> searchChannels(ChannelCriteria criteria, int currentPage, int pageSize) {
		ChannelExample example = new ChannelExample();
		Map<Integer, Object> params = criteria.getParams();
		for (Integer key : params.keySet()) {
			if (key.equals(1)) {
				example.createCriteria().andOrValue(Integer.parseInt(params.get(key).toString()));
			} else if (key.equals(2)) {
				example.createCriteria().andFatherIdEqualTo(Integer.parseInt(params.get(key).toString()));
			}
		}
		example.setOrderByClause("createTime desc");
		List<Channel> data = channelMapper.selectByExample(example, new RowBounds((currentPage - 1) * pageSize, pageSize));

		int recordCount = channelMapper.countByExample(example);
		PaginationVo<Channel> result = new PaginationVo<Channel>(data, recordCount, pageSize, currentPage);
		return result;

	}

	// 查找所有一级或二级分类
	public List<Channel> getChannels(Integer fatherId) {
		ChannelExample example = new ChannelExample();
		example.createCriteria().andFatherIdEqualTo(fatherId);
		List<Channel> result = channelMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(result))
			return null;
		else
			return result;
	}

	// 查看单个渠道信息
	public Channel getChannel(int id) {
		return channelMapper.selectByPrimaryKey(id);
	}

	// 增加渠道信息
	public void addChannel(Channel channel, Integer provinceId) {
		Province province = provinceMapper.selectByPrimaryKey(provinceId);
		if (province != null) {
			channel.setProvince(province);
		}
		channelMapper.insert(channel);
	}

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	public int insertSelective(Channel record) {
		return channelMapper.insertSelective(record);
	}

	// 更新渠道信息
	public void upChannel(Channel channel, Integer provinceId) {
		Province province = provinceMapper.selectByPrimaryKey(provinceId);
		if (province != null) {
			channel.setProvince(province);
		}
		channelMapper.updateByPrimaryKey(channel);
	}

	// 删除渠道信息
	public void delChannel(Integer id) {
		// 一级渠道对应子级渠道会相应删除
		Channel cha = channelMapper.selectByPrimaryKey(id);
		if (cha != null && cha.getFatherId() == 1) {
			// 删除对应子渠道
			deletSecondChannel(cha.getId());
		}
		channelMapper.deleteByPrimaryKey(id);
	}

	// 同时删除多个渠道
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delChannel(id);
		}
	}

	// 获取所有渠道信息
	public List<Channel> getAllChannels(int type) {
		ChannelExample example = new ChannelExample();
		if (type == 1) {
			// 查找渠道
			example.createCriteria().andIdBetween(1, 5);
		}
		return channelMapper.selectByExample(example);
	}

	// 重新查询
	public List<Channel> searchChannel(int flag) {
		if (flag == 1) {
			// 根据父ＩＤ查询
		} else {
			// 根据id查询

		}
		return null;
	}

	@Override
	public void updateSortByPrimarykey(Channel channel) {
		// TODO Auto-generated method stub
		channelMapper.updateSortByPrimarykey(channel);
	}
	
	public int deletSecondChannel(int fatherId){
		return channelMapper.deletSecondChannel(fatherId);
	}
}
