package com.mas.rave.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.ChannelMapper;
import com.mas.rave.dao.MarketMapper;
import com.mas.rave.main.vo.Channel;
import com.mas.rave.main.vo.Market;
import com.mas.rave.main.vo.MarketExample;
import com.mas.rave.service.MarketService;

/**
 * 平台
 * 
 * @author liwei.sz
 * 
 */

@Service
public class MarketServiceImpl implements MarketService {

	@Autowired
	private MarketMapper marketMapper;

	@Autowired
	private ChannelMapper channelMapper;

	public PaginationVo<Market> searchMarkets(MarketCriteria criteria,
			int currentPage, int pageSize) {
		MarketExample example = new MarketExample();
		Map<Integer, Object> params = criteria.getParams();
		for (Integer key : params.keySet()) {
			if (key.equals(1)) {
				example.createCriteria()
						.andNameLike(params.get(key).toString());
			}
		}

		example.createCriteria().andIdBetween(1, 10);
		example.setOrderByClause("createTime desc");
		List<Market> data = marketMapper.selectByExample(example,
				new RowBounds((currentPage - 1) * pageSize, pageSize));
		int recordCount = marketMapper.countByExample(example);
		PaginationVo<Market> result = new PaginationVo<Market>(data,
				recordCount, pageSize, currentPage);
		return result;
	}

	// 查看单个平台信息
	public Market getMarket(int id) {
		return marketMapper.selectByPrimaryKey(id);
	}

	// 根据包名查找
	public Market getMarketPac(String pac_name) {
		MarketExample example = new MarketExample();
		List<Market> list = marketMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	// 增加平台信息
	public void addMarket(Market market, Integer channelId) {
		Channel channel = channelMapper.selectByPrimaryKey(channelId);
		if (channel != null) {
			market.setChannel(channel);
		}
		marketMapper.insert(market);
	}

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	public int insertSelective(Market record) {
		return marketMapper.insertSelective(record);
	}

	// 更新平台信息
	public void upMarket(Market market, Integer channelId) {
		Channel channel = channelMapper.selectByPrimaryKey(channelId);
		if (channel != null) {
			market.setChannel(channel);
		}
		marketMapper.updateByPrimaryKey(market);
	}

	// 删除平台信息
	public void delMarket(Integer id) {
		marketMapper.deleteByPrimaryKey(id);
	}

	// 同时删除多个平台
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			marketMapper.deleteByPrimaryKey(id);
		}
	}

	@Override
	public List<Market> getAllMarkets() {
		MarketExample example = new MarketExample();
		return marketMapper.selectByExample(example);
	}

}
