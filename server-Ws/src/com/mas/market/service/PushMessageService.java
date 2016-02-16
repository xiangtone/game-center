package com.mas.market.service;

import com.mas.data.PushMessageData;
import com.mas.market.pojo.Criteria;

import java.util.List;

public interface PushMessageService {
	List<PushMessageData> queryPushMessage(Criteria cr);
}