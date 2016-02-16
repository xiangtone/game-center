package com.mas.market.service.impl;

import com.mas.market.mapper.TAppAlbumResMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TAppAlbumRes;
import com.mas.market.service.TAppAlbumResService;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TAppAlbumResServiceImpl implements TAppAlbumResService {
    @Autowired
    private TAppAlbumResMapper tAppAlbumResMapper;

    private static final Logger logger = LoggerFactory.getLogger(TAppAlbumResServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tAppAlbumResMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TAppAlbumRes selectByPrimaryKey(Integer id) {
        return this.tAppAlbumResMapper.selectByPrimaryKey(id);
    }

    public List<TAppAlbumRes> selectByExample(Criteria example) {
        return this.tAppAlbumResMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TAppAlbumRes record) {
        return this.tAppAlbumResMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TAppAlbumRes record) {
        return this.tAppAlbumResMapper.updateByPrimaryKey(record);
    }

    public int insert(TAppAlbumRes record) {
        return this.tAppAlbumResMapper.insert(record);
    }

    public int insertSelective(TAppAlbumRes record) {
        return this.tAppAlbumResMapper.insertSelective(record);
    }

	@Override
	public List<TAppAlbumRes> searchApps(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tAppAlbumResMapper.searchApps(cr);
	}

	@Override
	public List<TAppAlbumRes> searchRecommend(Criteria cr) {
		// TODO Auto-generated method stub
		return  this.tAppAlbumResMapper.searchRecommend(cr);
	}

	@Override
	public TAppAlbumRes appDetailByApkId(Integer id) {
		// TODO Auto-generated method stub
		return this.tAppAlbumResMapper.appDetailByApkId(id);
	}

	@Override
	public List<TAppAlbumRes> categorylist(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tAppAlbumResMapper.categorylist(cr);
	}

	@Override
	public void updateAppDownLoad(Criteria cr) {
		// TODO Auto-generated method stub
		this.tAppAlbumResMapper.updateAppDownLoad(cr);
	}

	@Override
	public List<TAppAlbumRes> getAppsForUpdate(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.tAppAlbumResMapper.getAppsForUpdate(map);
	}

	@Override
	public void updateAppOpenLog(Integer appId) {
		// TODO Auto-generated method stub
		this.tAppAlbumResMapper.updateAppOpenLog(appId);
	}

	@Override
	public List<TAppAlbumRes> columnlist(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tAppAlbumResMapper.columnlist(cr);
	}

	@Override
	public List<TAppAlbumRes> selectByRAND(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tAppAlbumResMapper.selectByRAND(cr);
	}

	@Override
	public List<TAppAlbumRes> keywordAppsList(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tAppAlbumResMapper.keywordAppsList(cr);
	}

	@Override
	public List<TAppAlbumRes> searchAppsTip(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tAppAlbumResMapper.searchAppsTip(cr);
	}

	@Override
	public TAppAlbumRes appDetailByAppId(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tAppAlbumResMapper.appDetailByAppId(cr);
	}

	@Override
	public TAppAlbumRes appDetailByPackageName(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tAppAlbumResMapper.appDetailByPackageName(cr);
	}

	@Override
	public List<TAppAlbumRes> collectionlist(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tAppAlbumResMapper.collectionlist(cr);
	}

	@Override
	public List<TAppAlbumRes> musthave(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tAppAlbumResMapper.musthave(cr);
	}

	@Override
	public List<TAppAlbumRes> columnlistByLiveWallpaper(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tAppAlbumResMapper.columnlistByLiveWallpaper(cr);
	}

	@Override
	public TAppAlbumRes getAppInfoByAppId(Integer appId) {
		// TODO Auto-generated method stub
		return this.tAppAlbumResMapper.getAppInfoByAppId(appId);
	}

	@Override
	public List<TAppAlbumRes> searchSameIssuerRecommend(Criteria cr) {
		return this.tAppAlbumResMapper.searchSameIssuerRecommend(cr);
	}

	@Override
	public List<TAppAlbumRes> searchAppsByArray(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tAppAlbumResMapper.searchAppsByArray(cr);
	}

	@Override
	public List<TAppAlbumRes> categoryHotList(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tAppAlbumResMapper.categoryHotList(cr);
	}
}