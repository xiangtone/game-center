package com.mas.market.service.impl;

import com.mas.market.mapper.TAppAlbumThemeMapper;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TAppAlbumTheme;
import com.mas.market.service.TAppAlbumThemeService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TAppAlbumThemeServiceImpl implements TAppAlbumThemeService {
    @Autowired
    private TAppAlbumThemeMapper tAppAlbumThemeMapper;

    private static final Logger logger = LoggerFactory.getLogger(TAppAlbumThemeServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.tAppAlbumThemeMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public TAppAlbumTheme selectByPrimaryKey(Integer themeId) {
        return this.tAppAlbumThemeMapper.selectByPrimaryKey(themeId);
    }

    public List<TAppAlbumTheme> selectByExample(Criteria example) {
        return this.tAppAlbumThemeMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(TAppAlbumTheme record) {
        return this.tAppAlbumThemeMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TAppAlbumTheme record) {
        return this.tAppAlbumThemeMapper.updateByPrimaryKey(record);
    }

    public int insert(TAppAlbumTheme record) {
        return this.tAppAlbumThemeMapper.insert(record);
    }

    public int insertSelective(TAppAlbumTheme record) {
        return this.tAppAlbumThemeMapper.insertSelective(record);
    }

	@Override
	public List<TAppAlbumTheme> hometheme(Criteria cr) {
		// TODO Auto-generated method stub
		return this.tAppAlbumThemeMapper.hometheme(cr);
	}
}