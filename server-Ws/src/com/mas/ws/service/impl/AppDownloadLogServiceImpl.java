package com.mas.ws.service.impl;

import com.mas.ws.mapper.AppDownloadLogMapper;
import com.mas.ws.pojo.AppDownloadLog;
import com.mas.ws.pojo.AppDownloadLogIcon;
import com.mas.ws.pojo.Criteria;
import com.mas.ws.service.AppDownloadLogService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppDownloadLogServiceImpl implements AppDownloadLogService {
    @Autowired
    private AppDownloadLogMapper appDownloadLogMapper;

    private static final Logger logger = LoggerFactory.getLogger(AppDownloadLogServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.appDownloadLogMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public AppDownloadLog selectByPrimaryKey(Integer id) {
        return this.appDownloadLogMapper.selectByPrimaryKey(id);
    }

    public List<AppDownloadLog> selectByExample(Criteria example) {
        return this.appDownloadLogMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(AppDownloadLog record) {
        return this.appDownloadLogMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(AppDownloadLog record) {
        return this.appDownloadLogMapper.updateByPrimaryKey(record);
    }

    public int insert(AppDownloadLog record) {
        return this.appDownloadLogMapper.insert(record);
    }

    public int insertSelective(AppDownloadLog record) {
        return this.appDownloadLogMapper.insertSelective(record);
    }

	@Override
	public List<AppDownloadLogIcon> queryList() {
		return appDownloadLogMapper.queryList();
	}

	@Override
	public String getIconUrlByCountryNm(String country) {
		// TODO Auto-generated method stub
		return appDownloadLogMapper.getIconUrlByCountryNm(country);
	}
}