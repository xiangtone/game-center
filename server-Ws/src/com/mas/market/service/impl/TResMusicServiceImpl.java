package com.mas.market.service.impl;

import com.mas.market.mapper.TResMusicMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TResMusic;
import com.mas.market.service.TResMusicService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TResMusicServiceImpl implements TResMusicService {
    @Autowired
    private TResMusicMapper tResMusicMapper;

    private static final Logger logger = LoggerFactory.getLogger(TResMusicServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tResMusicMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TResMusic selectByPrimaryKey(Integer id) {
        return this.tResMusicMapper.selectByPrimaryKey(id);
    }

    public List<TResMusic> selectByExample(Criteria example) {
        return this.tResMusicMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TResMusic record) {
        return this.tResMusicMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TResMusic record) {
        return this.tResMusicMapper.updateByPrimaryKey(record);
    }

    public int insert(TResMusic record) {
        return this.tResMusicMapper.insert(record);
    }

    public int insertSelective(TResMusic record) {
        return this.tResMusicMapper.insertSelective(record);
    }

	@Override
	public void updateDownLoad(Criteria cr) {
		// TODO Auto-generated method stub
		this.tResMusicMapper.updateDownLoad(cr);
	}

	@Override
	public List<TResMusic> categorylist(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tResMusicMapper.categorylist(cr);
	}

	@Override
	public List<TResMusic> searchMusic(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tResMusicMapper.searchMusic(cr);
	}

	@Override
	public List<TResMusic> keywordMusicList(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tResMusicMapper.keywordMusicList(cr);
	}

	@Override
	public List<TResMusic> searchMusicTip(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tResMusicMapper.searchMusicTip(cr);
	}

	@Override
	public List<TResMusic> searchMusicByArray(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tResMusicMapper.searchMusicByArray(cr);
	}
}