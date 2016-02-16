package com.mas.rave.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.AppAlbumThemeMapper;
import com.mas.rave.dao.AppInfoMapper;
import com.mas.rave.main.vo.AppAlbumTheme;
import com.mas.rave.main.vo.AppAlbumThemeExample;
import com.mas.rave.main.vo.AppAlbumThemeExample.Criteria;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.service.AppAlbumThemeService;
import com.mas.rave.util.FileUtil;

/**
 * app对应主题
 * 
 * @author liwei.sz
 * 
 */
@Service
public class AppAlbumThemeServiceImpl implements AppAlbumThemeService {

	@Autowired
	private AppAlbumThemeMapper appAlbumThemeMapper;
	@Autowired
	private AppInfoMapper appInfoMapper;

	public PaginationVo<AppAlbumTheme> searchAppAlbumTheme(AppAlbumThemeCriteria criteria, int currentPage, int pageSize,int flag) {
		AppAlbumThemeExample example = new AppAlbumThemeExample();
		Map<Integer, Object> params = criteria.getParams();
		// if (params != null && params.size() > 0) {
		// if (StringUtils.isNotEmpty(params.get(1).toString())) {
		// // 根据id查看
		// example.createCriteria().andNameLike(params.get(1).toString());
		// }
		// }
		Criteria criteria1 = example.createCriteria();
		for (Integer key : params.keySet()) {
			if (key.equals(1)) {
				// 根据id查看
				criteria1.andNameLike(params.get(1).toString());
			} else if (key.equals(4)) {
				criteria1.andAppAlbumColumnIdEqualTo(Integer.parseInt(params.get(4).toString()));
			} else if (key.equals(5)) {
				int[] appAlbumColumnIds = (int[]) params.get(5);
				criteria1.andAppAlbumEqualTo(appAlbumColumnIds);
			}

			if (key.equals(6)) {
				criteria1.andRaveIdEqual(Integer.parseInt(params.get(6).toString()));
			} else if (key.equals(7)) {
				criteria1.andFlag(Integer.parseInt(params.get(7).toString()));
			}
		}

		example.setOrderByClause("createTime desc");
		List<AppAlbumTheme> data = appAlbumThemeMapper.selectByExample(example, new RowBounds((currentPage - 1) * pageSize, pageSize));
		if(flag==1){
			//开发者广告图处理
			for(AppAlbumTheme theme:data){
				AppInfo app = appInfoMapper.selectByPrimaryKey(theme.getAppId());
				if(app!=null){
					theme.setAppInfo(app);
				}
			}
		}
		int recordCount = appAlbumThemeMapper.countByExample(example);
		PaginationVo<AppAlbumTheme> result = new PaginationVo<AppAlbumTheme>(data, recordCount, pageSize, currentPage);
		return result;
	}

	// app主题信息
	public List<AppAlbumTheme> getAppAlbumTheme() {
		AppAlbumThemeExample example = new AppAlbumThemeExample();
		return appAlbumThemeMapper.selectByExample(example);
	}

	// 查看单个app主题信息
	public AppAlbumTheme getAppAlbumTheme(int id) {
		return appAlbumThemeMapper.selectByPrimaryKey(id);
	}

	// 增加app主题信息
	public void addAppAlbumTheme(AppAlbumTheme record) {
		appAlbumThemeMapper.insert(record);
	}

	// 更新app主题信息
	public void upAppAlbumTheme(AppAlbumTheme record) {
		appAlbumThemeMapper.updateByPrimaryKey(record);
	}

	// 删除app主题信息
	public void delAppAlbumTheme(int id) {
		// 设置删除文件路径
		AppAlbumTheme appAlbumTheme = appAlbumThemeMapper.selectByPrimaryKey(id);
		if (appAlbumTheme.isState() == true) {
			appAlbumTheme.setState(false);
			appAlbumThemeMapper.updateByPrimaryKey(appAlbumTheme);
		} else {
			String bigUrl = null;
			String smallUrl = null;
			if (appAlbumTheme != null) {
				bigUrl = appAlbumTheme.getBigicon();
				smallUrl = appAlbumTheme.getIcon();
			}
			appAlbumThemeMapper.deleteByPrimaryKey(id);
			try {
				// 删除对应文件
				FileUtil.deleteFile(bigUrl);
				FileUtil.deleteFile(smallUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delAppAlbumTheme(id);
		}
	}

	@Override
	public void updateSortByPrimarykey(AppAlbumTheme appAlbumTheme) {
		// TODO Auto-generated method stub
		appAlbumThemeMapper.updateSortByPrimarykey(appAlbumTheme);
	}

	@Override
	public List<AppAlbumTheme> selectByThemeName(String name) {
		// TODO Auto-generated method stub
		return appAlbumThemeMapper.selectByThemeName(name);
	}

	@Override
	public List<AppAlbumTheme> selectByThemeNameCn(String nameCn) {
		// TODO Auto-generated method stub
		return appAlbumThemeMapper.selectByThemeNameCn(nameCn);
	}

	/**
	 * 获取所有开发者banner图上
	 * 
	 * @param falg
	 * @return
	 */
	public List<AppAlbumTheme> getBanners(int falg) {
		return appAlbumThemeMapper.getBanners(falg);
	}

	/**
	 * 获取开者者banner图
	 * 
	 * @param id
	 * @return
	 */
	public AppAlbumTheme getappBanner(int id) {
		return appAlbumThemeMapper.getappBanner(id);
	}

}
