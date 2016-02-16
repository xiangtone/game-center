package com.mas.rave.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.dao.ProxyIPMapper;
import com.mas.rave.main.vo.ProxyIP;
import com.mas.rave.service.ProxyIPService;
@Service
public class ProxyIPServiceImpl implements ProxyIPService {

	@Autowired
	private ProxyIPMapper proxyIPMapper;
	
	@Override
	public int getProxyIpCount() {
		// TODO Auto-generated method stub
		return proxyIPMapper.getProxyIpCount();
	}
	
	@Override
	public int countByExample(ProxyIP example) {
		// TODO Auto-generated method stub
		return proxyIPMapper.countByExample(example);
	}

	@Override
	public int insert(ProxyIP record) {
		// TODO Auto-generated method stub
		return proxyIPMapper.insert(record);
	}

	@Override
	public List<ProxyIP> selectByExample(ProxyIP example) {
		// TODO Auto-generated method stub
		return proxyIPMapper.selectByExample(example);
	}

	@Override
	public ProxyIP selectByPrimaryKey(long id) {
		// TODO Auto-generated method stub
		return proxyIPMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<ProxyIP> selectByIP(String ip) {
		// TODO Auto-generated method stub
		return proxyIPMapper.selectByIP(ip);
	}

	@Override
	public void deleteByPrimaryKey(long id) {
		proxyIPMapper.deleteByPrimaryKey(id);
	}
	@Override
	public List<String[]> getIpPort() {
		List<String[]> list = new ArrayList<String[]>();		
		//读取数据库中的所有Ip文件
		List <ProxyIP> list1 = proxyIPMapper.getAllIPConfigs();
			for(ProxyIP proxyIP:list1){
				String s = proxyIP.getIp();
				String[] ss = s.split(":");
				if (ss.length == 2) {
					list.add(ss);
				}
			}
			
		return list;
	}
	@Override
	public List <ProxyIP> getAllIPConfigs(){
		return proxyIPMapper.getAllIPConfigs();
	}
	@Override
	public void deleteByIP (String ip){
		proxyIPMapper.deleteByIP(ip);
	}
}
