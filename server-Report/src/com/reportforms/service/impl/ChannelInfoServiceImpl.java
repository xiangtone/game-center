package com.reportforms.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.dao.ChannelInfoMapper;
import com.reportforms.model.Channel;
import com.reportforms.service.ChannelInfoService;

@Service
public class ChannelInfoServiceImpl extends BaseServiceImpl<Channel> implements
		ChannelInfoService<Channel> {
	
	@Autowired
	private ChannelInfoMapper<Channel> channelInfoMapper; 
	
	@Override
	public List<Channel> query(PaginationBean paginationBean) {
		return channelInfoMapper.query(paginationBean, new RowBounds(0, 100));
	}
}
