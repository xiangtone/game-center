package com.mas.rave.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.ClientFeedbackZappMapper;
import com.mas.rave.main.vo.ClientFeedbackZapp;
import com.mas.rave.main.vo.ClientFeedbackZappExample;
import com.mas.rave.main.vo.ClientFeedbackZappExample.Criteria;
import com.mas.rave.service.ClientFeedbackZappService;


@Service
public class ClientFeedbackZappServiceImpl implements ClientFeedbackZappService {
	@Autowired
	private ClientFeedbackZappMapper clientFeedbackZappMapper;
	
	@Override
	public PaginationVo<ClientFeedbackZapp> selectByExample(
			ClientFeedbackZappCriteria criteria, int currentPage, int pageSize) {
		ClientFeedbackZappExample example = new ClientFeedbackZappExample();
		Map<Integer, Object> params = criteria.getParams();
		Criteria criteria1 = example.createCriteria();
		Map<String, Object> map = new HashMap<String, Object>();
		for (Integer key : params.keySet()) {
			if(key.equals(1)){
				criteria1.andQuestionTo(params.get(1).toString());
				map.put("question", params.get(1).toString().trim());
			}else if(key.equals(2))	{
				criteria1.andReplyContentTo(params.get(2).toString());
				map.put("replyContent", params.get(2).toString().trim());
			}
		}	
		example.setMapOrderByClause(map);
		example.setOrderByClause(" createTime desc");
		List<ClientFeedbackZapp> data = clientFeedbackZappMapper.selectByExample(example, new RowBounds((currentPage - 1) * pageSize, pageSize));

		int recordCount = clientFeedbackZappMapper.countByExample(example);
		PaginationVo<ClientFeedbackZapp> result = new PaginationVo<ClientFeedbackZapp>(data, recordCount, pageSize, currentPage);
		
		
		return result;
	}
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return clientFeedbackZappMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ClientFeedbackZapp record) {
		// TODO Auto-generated method stub
		return clientFeedbackZappMapper.insert(record);
	}



	@Override
	public ClientFeedbackZapp selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return clientFeedbackZappMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(ClientFeedbackZapp record) {
		// TODO Auto-generated method stub
		return clientFeedbackZappMapper.updateByPrimaryKey(record);
	}
	@Override
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			deleteByPrimaryKey(id);
		}

	}
}
