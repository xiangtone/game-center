package com.mas.rave.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationBean;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.CategoryMapper;
import com.mas.rave.dao.MusicAlbumResMapper;
import com.mas.rave.dao.MusicInfoMapper;
import com.mas.rave.dao.SearchKeywordMapper;
import com.mas.rave.dao.SearchKeywordResListMapper;
import com.mas.rave.main.vo.Category;
import com.mas.rave.main.vo.MusicAlbumRes;
import com.mas.rave.main.vo.MusicInfo;
import com.mas.rave.service.MusicInfoService;
import com.mas.rave.util.FileUtil;

@Service
public class MusicInfoServiceImpl implements MusicInfoService {
	@Autowired
	private MusicInfoMapper musicMapper;

	@Autowired
	private CategoryMapper categoryMapper;

	@Autowired
	private MusicAlbumResMapper musicAlbumResMapper;

	@Autowired
	private SearchKeywordMapper searchKeywordMapper;

	@Autowired
	private SearchKeywordResListMapper searchKeywordResListMapper;

	@Override
	public PaginationVo<MusicInfo> searchMusics(MusicInfo criteria, int currentPage, int pageSize) {
		int recordCount = musicMapper.countByExample(criteria);
		criteria.setCurrentPage((currentPage - 1) * pageSize);
		criteria.setPageSize(pageSize);
		List<MusicInfo> data = musicMapper.selectByExample(criteria);
		PaginationVo<MusicInfo> result = new PaginationVo<MusicInfo>(data, recordCount, pageSize, currentPage);
		return result;
	}

	@Override
	public MusicInfo searchMusicInfo(MusicInfo criteria) {
		// TODO Auto-generated method stub
		List<MusicInfo> data = musicMapper.selectByExample(criteria);
		if (data != null) {
			return data.get(0);
		} else {
			return null;
		}
	}

	@Override
	public MusicInfo getMusicInfo(long id) {
		// TODO Auto-generated method stub
		return musicMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<MusicInfo> getAllMusicInfos() {
		// TODO Auto-generated method stub
		return musicMapper.getAllMusicInfos();
	}

	@Override
	public int addMusicInfo(MusicInfo music, Integer categoryId) {
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		if (category != null) {
			music.setCategory(category);
		}
		return musicMapper.insert(music);
	}

	@Override
	public int insertSelective(MusicInfo record) {
		// TODO Auto-generated method stub
		return musicMapper.insertSelective(record);
	}

	@Override
	public void upMusicInfo(MusicInfo music, Integer categoryId) {
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		if (category != null) {
			music.setCategory(category);
		}

		MusicInfo musicInfo = musicMapper.selectByPrimaryKey(music.getId());
		if (musicInfo != null) {
			if (musicInfo.getCountry().getId() == music.getRaveId()) {
				musicMapper.updateByPrimaryKey(music);

				// 同步分发
				List<MusicAlbumRes> musicAlbumRes = musicAlbumResMapper.selectByMusicId(music.getId());
				if (musicAlbumRes != null) {
					for (MusicAlbumRes musicres : musicAlbumRes) {
						musicres.setCategory(category);
						musicres.setArtist(music.getArtist());
						musicres.setMusicName(music.getName());
						musicres.setBrief(music.getBrief());
						musicres.setDescription(music.getDescription());
						musicres.setDuration(music.getDuration());
						musicres.setFree(music.getFree());
						musicres.setFileSize(music.getFileSize());
						musicres.setStars(music.getStars());
						musicres.setLogo(music.getLogo());
						musicres.setUrl(music.getUrl());
						musicAlbumResMapper.updateByPrimaryKey(musicres);
					}
				}
			}else{
				musicMapper.updateByPrimaryKey(music);
				musicAlbumResMapper.deleteByMusicId(music.getId());
			}

		}

	}

	@Override
	public void delMusicInfo(Integer id) {

		MusicInfo musicInfo = musicMapper.selectByPrimaryKey(id);
		if (musicInfo != null) {
			// 删除分发信息
			List<MusicAlbumRes> musicAlbumRes = musicAlbumResMapper.selectByMusicId(id);
			if (musicAlbumRes != null) {
				for (MusicAlbumRes musicres : musicAlbumRes) {
					PaginationBean params = new PaginationBean();
					params.getParams().put("ids", Integer.toString(musicres.getId()));
					musicAlbumResMapper.deleteById(params);
				}
			}

			// 删除搜索关键字中的数据
			searchKeywordMapper.deleteByResId(id);
			searchKeywordResListMapper.deleteByResId(id);
			if (musicInfo.isState() == true) {
				musicInfo.setState(false);
				musicMapper.updateByPrimaryKey(musicInfo);
			} else {
				// 删除logo图片
				musicMapper.deleteByPrimaryKey(id);
				// 删除文件
				try {
					// 删除对应文件
					if (musicInfo.getLogo() != null && musicInfo.getLogo() != "") {
						FileUtil.deleteFile(musicInfo.getLogo());

					}
					if (musicInfo.getUrl() != null && musicInfo.getUrl() != "") {
						FileUtil.deleteFile(musicInfo.getUrl());

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	@Override
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delMusicInfo(id);
		}

	}

	@Override
	public void upMusicInfoAlbumRes(MusicInfo musicInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<MusicInfo> selectByName(String name) {
		// TODO Auto-generated method stub
		return musicMapper.selectByName(name);
	}

	@Override
	public int getMusicInfoCountByCategory(int categoryId) {
		// TODO Auto-generated method stub
		return musicMapper.getMusicInfoCountByCategory(categoryId);
	}

}
