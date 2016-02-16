package com.mas.rave.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.MessagePushMapper;
import com.mas.rave.main.vo.MessagePush;
import com.mas.rave.service.MessagePushService;
import com.mas.rave.util.FileUtil;

@Service
public class MessagePushServiceImpl implements MessagePushService {
	@Autowired
	private MessagePushMapper messagePushMapper;

	@Override
	public PaginationVo<MessagePush> searchMessagePush(MessagePush criteria, int currentPage, int pageSize) {
		int recordCount = messagePushMapper.countByExample(criteria);
		criteria.setCurrentPage((currentPage - 1) * pageSize);
		criteria.setPageSize(pageSize);
		List<MessagePush> data = messagePushMapper.selectByExample(criteria);
		PaginationVo<MessagePush> result = new PaginationVo<MessagePush>(data, recordCount, pageSize, currentPage);
		return result;
	}

	@Override
	public int countByExample(MessagePush example) {
		// TODO Auto-generated method stub
		return messagePushMapper.countByExample(example);
	}

	@Override
	public int deleteByPrimaryKey(long id) {
		MessagePush push = messagePushMapper.selectByPrimaryKey(id);
		if (push != null) {
			try {
				FileUtil.deleteFile(push.getPic());
				FileUtil.deleteFile(push.getIcon());
			} catch (IOException e) {

			}
		}
		// TODO Auto-generated method stub
		return messagePushMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(MessagePush record) {
		// TODO Auto-generated method stub
		return messagePushMapper.insert(record);
	}

	@Override
	public MessagePush selectByPrimaryKey(long id) {
		// TODO Auto-generated method stub
		return messagePushMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(MessagePush record) {
		// TODO Auto-generated method stub
		return messagePushMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<MessagePush> selectByExample(MessagePush example) {
		// TODO Auto-generated method stub
		return messagePushMapper.selectByExample(example);
	}

	@Override
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			deleteByPrimaryKey(id);
		}

	}
}
