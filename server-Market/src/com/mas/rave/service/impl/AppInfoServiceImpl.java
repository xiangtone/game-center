package com.mas.rave.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.AppAlbumResMapper;
import com.mas.rave.dao.AppAlbumResTempMapper;
import com.mas.rave.dao.AppCollectionResMapper;
import com.mas.rave.dao.AppCommentMapper;
import com.mas.rave.dao.AppCountryScoreMapper;
import com.mas.rave.dao.AppFilePatchMapper;
import com.mas.rave.dao.AppInfoConfigMapper;
import com.mas.rave.dao.AppInfoMapper;
import com.mas.rave.dao.AppannieInfoCountryRankMapper;
import com.mas.rave.dao.CategoryMapper;
import com.mas.rave.dao.MarketMapper;
import com.mas.rave.dao.SearchKeywordMapper;
import com.mas.rave.dao.SearchKeywordResListMapper;
import com.mas.rave.main.vo.AppAlbumRes;
import com.mas.rave.main.vo.AppAlbumResExample;
import com.mas.rave.main.vo.AppCountryScore;
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.main.vo.AppPic;
import com.mas.rave.main.vo.Category;
import com.mas.rave.service.AppFileService;
import com.mas.rave.service.AppInfoService;
import com.mas.rave.service.AppPicService;
import com.mas.rave.util.FileUtil;

/**
 * app信息
 * 
 * @author liwei.sz
 * 
 */

@Service
public class AppInfoServiceImpl implements AppInfoService {
	@Autowired
	private AppInfoMapper appMapper;

	@Autowired
	private AppPicService appPicService;

	@Autowired
	private AppFileService appFileService;

	@Autowired
	private MarketMapper marketMapper;

	@Autowired
	private CategoryMapper categoryMapper;

	@Autowired
	private AppAlbumResMapper appAlbumResMapper;

	@Autowired
	private AppAlbumResTempMapper appAlbumResTempMapper;

	@Autowired
	private AppInfoConfigMapper appInfoConfigMapper;

	@Autowired
	private AppFilePatchMapper appFilePatchMapper;

	@Autowired
	private AppCommentMapper appCommentMapper;

	@Autowired
	private SearchKeywordMapper searchKeywordMapper;

	@Autowired
	private SearchKeywordResListMapper searchKeywordResListMapper;

	@Autowired
	private AppCollectionResMapper appCollectionResMapper;

	@Autowired
	private AppCountryScoreMapper appCountryScoreMapper;

	@Autowired
	private AppannieInfoCountryRankMapper appannieInfoCountryRankMapper;

	public PaginationVo<AppInfo> searchApps(AppInfo example, int currentPage, int pageSize) {
		int recordCount = appMapper.countByExample(example);
		example.setCurrentPage((currentPage - 1) * pageSize);
		example.setPageSize(pageSize);
		List<AppInfo> data = appMapper.selectByExample(example);
		PaginationVo<AppInfo> result = new PaginationVo<AppInfo>(data, recordCount, pageSize, currentPage);
		return result;

	}

	public AppInfo searchApp(AppInfo criteria) {
		List<AppInfo> data = appMapper.selectByExample(criteria);
		if (data != null) {
			return data.get(0);
		} else {
			return null;
		}
	}

	public List<AppInfo> searchApps(AppInfo criteria) {
		List<AppInfo> data = appMapper.selectByExample1(criteria);
		return data;
	}

	// 获取所有app信息
	public List<AppInfo> getAllAppInfos() {
		return appMapper.getAllAppInfos();
	}

	// 查看单个app信息
	public AppInfo getApp(long id) {
		return appMapper.selectByPrimaryKey(id);
	}

	// 增加app信息
	public int addApp(AppInfo app, Integer categoryId) {
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		if (category != null) {
			app.setCategory(category);
		}
		return appMapper.insert(app);
	}

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	public int insertSelective(AppInfo record) {
		return appMapper.insertSelective(record);
	}

	// 更新app信息
	public void upApp(AppInfo app, Integer categoryId) {
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		if (category != null) {
			app.setCategory(category);
		}
		appMapper.updateByPrimaryKey(app);
	}

	// 删除app信息
	public void delApp(Integer id) {
		// 删除logo图片
		AppInfo appInfo = appMapper.selectByPrimaryKey(id);
		if (appInfo != null) {
			// 删除文件
			try {
				// 清空对象配置信息
				appInfoConfigMapper.deleteByName(appInfo.getName());

				// 清除对应文件及图片
				List<AppPic> pics = appPicService.getAppPics(id);
				if (pics != null && pics.size() > 0) {
					for (AppPic pic : pics) {
						appPicService.delAppPics(pic.getId());
					}
				}
				// 删除对应文件
				List<AppFile> files = appFileService.getAppFiles(id);
				if (files != null && files.size() > 0) {
					for (AppFile file : files) {
						appFileService.delAppFiles(file.getId());
					}
				}
				// 删除对应分发信息 包括正式表和临时表
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("apkId", null);
				map.put("appId", id);
				appAlbumResTempMapper.deleteByApp(map);
				appAlbumResMapper.deleteByApp(map);
				appCollectionResMapper.deleteByApp(map);
				appCommentMapper.deleteByAppId(id);
				// 删除搜索关键字中的数据
				searchKeywordMapper.deleteByResId(id);
				searchKeywordResListMapper.deleteByResId(id);

				// 删除对应的AppCountryScore
				AppCountryScore appCountryScore = new AppCountryScore();
				appCountryScore.setAppId(id);
				appCountryScoreMapper.deleteByCondition(appCountryScore);
				// 删除对应文件
				FileUtil.deleteFile(appInfo.getLogo());
				FileUtil.deleteFile(appInfo.getBigLogo());
				appMapper.deleteByPrimaryKey(id);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 同时删除多个app
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delApp(id);
		}
	}

	// 更新对应的AppCountryScore
	public void upAppCountryScoreByAppId(AppInfo appInfo) {
		AppCountryScore appCountryScore = new AppCountryScore();
		appCountryScore.setAppId(appInfo.getId());
		List<AppCountryScore> lists = appCountryScoreMapper.selectByCondition(appCountryScore);
		if (lists != null && lists.size() > 0) {
			for (AppCountryScore appCountryScore1 : lists) {
				appCountryScore1.setAppName(appInfo.getName());
				appCountryScoreMapper.update(appCountryScore1);
			}
		}
	}

	// 更新
	public void upAppCollectionRes(AppInfo appInfo) {
		AppAlbumResExample example = new AppAlbumResExample();
		// 获取对应app分发信息
		example.createCriteria().andAppId(appInfo.getId());
		List<AppAlbumRes> res = appCollectionResMapper.selectByExample(example);
		if (res != null && res.size() > 0)
			for (AppAlbumRes re : res) {
				re.setAppInfo(appInfo);
				re.setFree(appInfo.getFree());
				re.setAppName(appInfo.getName());
				re.setLogo(appInfo.getLogo());
				re.setBigLogo(appInfo.getBigLogo());
				re.setBrief(appInfo.getBrief());
				re.setDescription(appInfo.getDescription());
				re.setStars(appInfo.getStars());
				re.setBrief(appInfo.getBrief());
				re.setCategoryId(appInfo.getCategory().getId());
				re.setInitDowdload(re.getInitDowdload());
				re.setRealDowdload(re.getRealDowdload());
				appCollectionResMapper.updateByPrimaryKey(re);
			}

	}

	// 更新
	public void upAppAlbumRes(AppInfo appInfo) {
		AppAlbumResExample example = new AppAlbumResExample();
		// 获取对应app分发信息
		example.createCriteria().andAppId(appInfo.getId());
		List<AppAlbumRes> res = appAlbumResMapper.selectByExample(example);
		if (res != null && res.size() > 0)
			for (AppAlbumRes re : res) {
				re.setAppInfo(appInfo);
				re.setFree(appInfo.getFree());
				re.setAppName(appInfo.getName());
				re.setLogo(appInfo.getLogo());
				re.setBigLogo(appInfo.getBigLogo());
				re.setBrief(appInfo.getBrief());
				re.setDescription(appInfo.getDescription());
				re.setStars(appInfo.getStars());
				re.setBrief(appInfo.getBrief());
				re.setCategoryId(appInfo.getCategory().getId());
				re.setInitDowdload(re.getInitDowdload());
				re.setRealDowdload(re.getRealDowdload());
				appAlbumResMapper.updateByPrimaryKey(re);
			}

	}

	/**
	 * 更新app分发信息temp表 add by dingjie
	 */
	public void upAppAlbumResTemp(AppInfo appInfo) {
		AppAlbumResExample example = new AppAlbumResExample();
		// 获取对应app分发信息
		example.createCriteria().andAppId(appInfo.getId());
		List<AppAlbumRes> res = appAlbumResTempMapper.selectByExample(example);
		if (res != null && res.size() > 0)
			for (AppAlbumRes re : res) {
				re.setAppInfo(appInfo);
				re.setFree(appInfo.getFree());
				re.setAppName(appInfo.getName());
				re.setLogo(appInfo.getLogo());
				re.setBigLogo(appInfo.getBigLogo());
				re.setBrief(appInfo.getBrief());
				re.setDescription(appInfo.getDescription());
				re.setStars(appInfo.getStars());
				re.setBrief(appInfo.getBrief());
				re.setCategoryId(appInfo.getCategory().getId());
				re.setInitDowdload(re.getInitDowdload());
				re.setRealDowdload(re.getRealDowdload());
				appAlbumResTempMapper.updateByPrimaryKey(re);
			}

	}

	// 更新对应apk信息
	public void upAppFile(AppInfo appInfo) {
		List<AppFile> files = appFileService.getAppFiles(appInfo.getId());
		if (files != null && files.size() > 0) {
			for (AppFile file : files) {
				if (!appInfo.getName().trim().equals(file.getAppName())) {
					file.setAppName(appInfo.getName());
					appFileService.upAppFile(file);
				}
			}
		}
	}

	@Override
	public AppInfo findAppInfo(int fatherChannelId, String appName) {

		AppInfo appInfo = new AppInfo();
		appInfo.setFatherChannelId(fatherChannelId);
		appInfo.setName(appName);
		appInfo = appMapper.selectByFatherChannelIdAndName(appInfo);
		return appInfo;
	}

	public int updateNum(int id) {
		return appMapper.updateNum(id);
	}

	@Override
	public int getAppInfoCountByCategory(int categoryId) {
		// TODO Auto-generated method stub
		return appMapper.getAppInfoCountByCategory(categoryId);
	}

	// 检测cp是存在
	public int checkCp(Integer cpId) {
		return appMapper.checkCp(cpId);
	}

	public void upFree(AppInfo appInfo) {
		appMapper.upFree(appInfo);
	}

	@Override
	public List<AppInfo> selectADayAgoInfo() {
		// TODO Auto-generated method stub
		return appMapper.selectADayAgoInfo();
	}
}
