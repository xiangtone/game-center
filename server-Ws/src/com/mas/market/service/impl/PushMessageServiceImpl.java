package com.mas.market.service.impl;

import com.mas.data.PushMessageData;
import com.mas.market.mapper.PushMessageMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.service.PushMessageService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PushMessageServiceImpl implements PushMessageService {
    @Autowired
    private PushMessageMapper pushMessageMapper;

	@Override
	public List<PushMessageData> queryPushMessage(Criteria cr) {
		// TODO Auto-generated method stub
		return pushMessageMapper.queryPushMessage(cr);
	}
}