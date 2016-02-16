package com.mas.rave.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.dao.ClientSkinCodeMapper;
import com.mas.rave.main.vo.ClientSkinCode;
import com.mas.rave.service.ClientSkinCodeService;

/**
 * app对应主题
 * 
 * @author liwei.sz
 * 
 */
@Service
public class ClientSkinCodeServiceImpl implements ClientSkinCodeService {

	@Autowired
	private ClientSkinCodeMapper clientSkinCodeMapper;

	@Override
	public void updateTime(Integer id) {
		// TODO Auto-generated method stub
		clientSkinCodeMapper.updateTime(id);
	}

	@Override
	public ClientSkinCode selectByExample() {
		// TODO Auto-generated method stub
		return clientSkinCodeMapper.selectByExample();
	}

}
