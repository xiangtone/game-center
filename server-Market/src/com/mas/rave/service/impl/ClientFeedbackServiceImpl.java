package com.mas.rave.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.ClientFeedbackMapper;
import com.mas.rave.main.vo.ClientFeedback;
import com.mas.rave.service.ClientFeedbackService;
@Service
public class ClientFeedbackServiceImpl implements ClientFeedbackService {

	@Autowired
	private ClientFeedbackMapper clientFeedbackMapper;
	@Override
	public PaginationVo<ClientFeedback> searchClientFeedbacks(
			ClientFeedback criteria, int currentPage, int pageSize) {
		criteria.setCurrentPage((currentPage - 1) * pageSize);
		criteria.setPageSize(pageSize);
		List<ClientFeedback> data = clientFeedbackMapper.selectByExample(criteria);
		int recordCount = clientFeedbackMapper.countByExample(criteria);
		PaginationVo<ClientFeedback> result = new PaginationVo<ClientFeedback>(data, recordCount, pageSize, currentPage);
		
		
		return result;
	}
	@Override
	public PaginationVo<ClientFeedback> selectByClientId(Integer clientId, int currentPage, int pageSize) {
		ClientFeedback client = new ClientFeedback();
		client.setCurrentPage((currentPage - 1) * pageSize);
		client.setPageSize(pageSize);
		client.setClientId(clientId);
		List<ClientFeedback> data = clientFeedbackMapper.selectByCondition(client);
		int recordCount = clientFeedbackMapper.countByCondition(client);
		PaginationVo<ClientFeedback> result = new PaginationVo<ClientFeedback>(data, recordCount, pageSize, currentPage);
		
		
		return result;
	}

	@Override
	public PaginationVo<ClientFeedback> selectByImei(String imei,
			int currentPage, int pageSize) {
		ClientFeedback client = new ClientFeedback();
		client.setCurrentPage((currentPage - 1) * pageSize);
		client.setPageSize(pageSize);
		client.setImei(imei);
		List<ClientFeedback> data = clientFeedbackMapper.selectByCondition(client);
		int recordCount = clientFeedbackMapper.countByCondition(client);
		PaginationVo<ClientFeedback> result = new PaginationVo<ClientFeedback>(data, recordCount, pageSize, currentPage);
		return result;
	}

	@Override
	public ClientFeedback getClientFeedback(int id) {
		// TODO Auto-generated method stub
		return clientFeedbackMapper.selectByPrimaryKey(id);
	}

	@Override
	public void addClientFeedback(ClientFeedback clientFeedback) {
		clientFeedbackMapper.insert(clientFeedback);

	}

	@Override
	public int insertSelective(ClientFeedback record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void upClientFeedback(ClientFeedback clientFeedback, Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delClientFeedback(Integer id) {
		clientFeedbackMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delClientFeedback(id);
		}

	}
	@Override
	public void delClientId(Integer clientId) {
		clientFeedbackMapper.deleteByClientId(clientId);
		
	}
	@Override
	public void batchDeleteClientId(Integer[] clientIds) {
		for (Integer clientId : clientIds) {
			delClientId(clientId);
		}
		
	}
	@Override
	public List<ClientFeedback> selectByClientFeedback(
			ClientFeedback clientFeedback) {
		// TODO Auto-generated method stub
		return clientFeedbackMapper.selectByClientFeedback(clientFeedback);
	}


	

}
