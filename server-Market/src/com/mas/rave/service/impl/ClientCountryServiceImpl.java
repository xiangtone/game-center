package com.mas.rave.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.ClientCountryMapper;
import com.mas.rave.main.vo.ClientCountry;
import com.mas.rave.service.ClientCountryService;

@Service
public class ClientCountryServiceImpl implements ClientCountryService {

	@Autowired
	private ClientCountryMapper clientCountryMapper;

	@Override
	public void insert(ClientCountry record) {
		clientCountryMapper.insert(record);
	}

	@Override
	public PaginationVo<ClientCountry> selectByExample(ClientCountry example, int currentPage, int pageSize) {
		int recordCount = clientCountryMapper.countByExample(example);
		example.setCurrentPage((currentPage - 1) * pageSize);
		example.setPageSize(pageSize);
		List<ClientCountry> data = clientCountryMapper.selectByExample(example);
		PaginationVo<ClientCountry> result = new PaginationVo<ClientCountry>(data, recordCount, pageSize, currentPage);
		return result;
	}

	@Override
	public ClientCountry selectByCountryCn(String countryCn) {
		// TODO Auto-generated method stub
		return clientCountryMapper.selectByCountryCn(countryCn);
	}

	@Override
	public void updateByCountryCn(ClientCountry record) {
		clientCountryMapper.updateByCountryCn(record);

	}

	@Override
	public List<ClientCountry> getClientCountrys() {
		// TODO Auto-generated method stub
		return clientCountryMapper.getClientCountrys();
	}

	/**
	 * 参数查找
	 * 
	 * @param map
	 * @return
	 */
	public ClientCountry getParament(HashMap<String, Object> map) {
		return clientCountryMapper.getParament(map);
	}

	/**
	 * 根据名字删除
	 */
	public int deleteByCountryCn(String countryCn) {
		return clientCountryMapper.deleteByCountryCn(countryCn);
	}
}
