package com.mas.rave.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.CountryMapper;
import com.mas.rave.main.vo.Country;
import com.mas.rave.main.vo.CountryExample;
import com.mas.rave.service.CountryService;

/**
 * country
 * 
 * @author jieding
 * 
 */
@Service
public class CountryServiceImpl implements CountryService {

	@Autowired
	private CountryMapper countryMapper;

	@Override
	public List<Country> getCountrys() {
		// TODO Auto-generated method stub
		return countryMapper.getCountrys();
	}

	@Override
	public PaginationVo<Country> selectByExample(CountryCriteria criteria, int currentPage, int pageSize) {
		CountryExample example = new CountryExample();
		Map<Integer, Object> params = criteria.getParams();
		for (Integer key : params.keySet()) {
			if (key.equals(1)) {
				example.createCriteria().andNameLike(params.get(key).toString());
			}
		}
		example.setOrderByClause("createTime desc");
		List<Country> data = countryMapper.selectByExample(example, new RowBounds((currentPage - 1) * pageSize, pageSize));
		int recordCount = countryMapper.countByExample(example);
		PaginationVo<Country> result = new PaginationVo<Country>(data, recordCount, pageSize, currentPage);
		return result;
	}
	/**
	 * 根据条件查询
	 * @param example
	 * @return
	 */
	public List<Country> getCountry(CountryExample example){
		
		return countryMapper.selectByExample(example);
	}

	@Override
	public Country getCountry(int id) {
		// TODO Auto-generated method stub
		return countryMapper.selectByPrimaryKey(id);
	}

	@Override
	public void addCountry(Country country) {
		// TODO Auto-generated method stub
		countryMapper.insert(country);
	}

	@Override
	public void upCountry(Country country) {
		// TODO Auto-generated method stub
		countryMapper.updateByPrimaryKey(country);
	}

	@Override
	public void delCountry(int id) {
		countryMapper.deleteByPrimaryKey(id);

	}

	@Override
	public void batchDelete(Integer[] ids) {
		for (int id : ids) {
			countryMapper.deleteByPrimaryKey(id);
		}
	}

	@Override
	public void updateState(int id, int state) {
		HashMap<String, Object> map = new HashMap<String, Object>(2);
		map.put("id", id);
		map.put("state", state);
		countryMapper.updateState(map);
	}
}
