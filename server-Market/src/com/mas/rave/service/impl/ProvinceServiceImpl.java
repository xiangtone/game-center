package com.mas.rave.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mas.rave.dao.ProvinceMapper;
import com.mas.rave.main.vo.Province;
import com.mas.rave.main.vo.ProvinceExample;
import com.mas.rave.service.ProvinceService;

@Service
public class ProvinceServiceImpl implements ProvinceService {

	@Autowired
	private ProvinceMapper provinceMapper;

	public List<Province> getProvinces() {
		ProvinceExample example = new ProvinceExample();
		List<Province> result = provinceMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(result))
			return null;
		else
			return result;
	}

	// 查看单个省份文件信息
	public Province getProvince(int id) {
		return provinceMapper.selectByPrimaryKey(id);
	}

	// 增加省份文件信息
	public void addProvince(Province province) {
		provinceMapper.insert(province);
	}

	// 更新省份文件信息
	public void upProvince(Province province) {
		provinceMapper.updateByPrimaryKey(province);
	}

	// 删除省份文件信息
	public void delProvince(int id) {
		provinceMapper.deleteByPrimaryKey(id);
	}

	// 同时删除多个消息文件
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			provinceMapper.deleteByPrimaryKey(id);
		}
	}

}
