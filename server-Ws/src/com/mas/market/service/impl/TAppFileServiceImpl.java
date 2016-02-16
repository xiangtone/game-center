package com.mas.market.service.impl;

import com.mas.market.mapper.TAppFileMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TAppFile;
import com.mas.market.service.TAppFileService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TAppFileServiceImpl implements TAppFileService {
    @Autowired
    private TAppFileMapper tAppFileMapper;

    private static final Logger logger = LoggerFactory.getLogger(TAppFileServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tAppFileMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TAppFile selectByPrimaryKey(Integer id) {
        return this.tAppFileMapper.selectByPrimaryKey(id);
    }

    public List<TAppFile> selectByExample(Criteria example) {
        return this.tAppFileMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TAppFile record) {
        return this.tAppFileMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TAppFile record) {
        return this.tAppFileMapper.updateByPrimaryKey(record);
    }

    public int insert(TAppFile record) {
        return this.tAppFileMapper.insert(record);
    }

    public int insertSelective(TAppFile record) {
        return this.tAppFileMapper.insertSelective(record);
    }

	@Override
	public List<TAppFile> getApkForUpgrade(Criteria c) {
		// TODO Auto-generated method stub
		return this.tAppFileMapper.getApkForUpgrade(c);
	}

	@Override
	public TAppFile getApkPatch(Criteria criteria) {
		// TODO Auto-generated method stub
		return this.tAppFileMapper.getApkPatch(criteria);
	}

	@Override
	public List<TAppFile> getZappForUpgrade(Criteria c) {
		// TODO Auto-generated method stub
		return this.tAppFileMapper.getZappForUpgrade(c);
	}

	@Override
	public List<TAppFile> getApkForAppId(Integer appId) {
		// TODO Auto-generated method stub
		return this.tAppFileMapper.getApkForAppId(appId);
	}

	@Override
	public List<TAppFile> getCommonApkUpgrade(Criteria c) {
		// TODO Auto-generated method stub
		return this.tAppFileMapper.getCommonApkUpgrade(c);
	}

	@Override
	public List<TAppFile> getDownloadInfo(Criteria example) {
		// TODO Auto-generated method stub
		return this.tAppFileMapper.getDownloadInfo(example);
	}
}