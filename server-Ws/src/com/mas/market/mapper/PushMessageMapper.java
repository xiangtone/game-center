package com.mas.market.mapper;

import com.mas.data.PushMessageData;
import com.mas.market.pojo.Criteria;

import java.util.List;

public interface PushMessageMapper {
	public List<PushMessageData> queryPushMessage(Criteria cr);
}