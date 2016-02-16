package com.mas.rave.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.dao.ClientFeedbackZappCodeMapper;
import com.mas.rave.main.vo.ClientFeedbackZappCode;
import com.mas.rave.service.ClientFeedbackZappCodeService;
@Service
public class ClientFeedbackZappCodeServiceImpl implements
		ClientFeedbackZappCodeService {

	@Autowired
	private ClientFeedbackZappCodeMapper clientFeedbackZappCodeMapper;
	@Override
	public void insert(ClientFeedbackZappCode record) {
		clientFeedbackZappCodeMapper.insert(record);
	}

	@Override
	public ClientFeedbackZappCode selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return clientFeedbackZappCodeMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<ClientFeedbackZappCode> getAllClientFeedbackZappCode() {
		// TODO Auto-generated method stub
		return clientFeedbackZappCodeMapper.getAllClientFeedbackZappCode();
	}

	@Override
	public void updateByPrimaryKey(ClientFeedbackZappCode record) {
		clientFeedbackZappCodeMapper.updateByPrimaryKey(record);

	}

}
