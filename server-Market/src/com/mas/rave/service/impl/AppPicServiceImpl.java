package com.mas.rave.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.AppInfoMapper;
import com.mas.rave.dao.AppPicMapper;
import com.mas.rave.main.vo.AppPic;
import com.mas.rave.main.vo.AppPicExample;
import com.mas.rave.service.AppPicService;
import com.mas.rave.util.FileUtil;

/**
 * app对应图片
 * 
 * @author liwei.sz
 * 
 */
@Service
public class AppPicServiceImpl implements AppPicService {

	@Autowired
	private AppPicMapper appPicMapper;

	@Autowired
	private AppInfoMapper appInfoMapper;

	/**
	 * 分页显示app截图信息
	 * 
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PaginationVo<AppPic> searchAppPics(AppPicCriteria criteria, Integer appId, int currentPage, int pageSize) {
		AppPicExample example = new AppPicExample();
		example.setOrderByClause("createTime desc");
		List<AppPic> data = appPicMapper.selectByExample(example, new RowBounds((currentPage - 1) * pageSize, pageSize));
		int recordCount = appPicMapper.countByExample(example);
		PaginationVo<AppPic> result = new PaginationVo<AppPic>(data, recordCount, pageSize, currentPage);
		return result;

	}

	public List<AppPic> getAppPics(Integer appId) {
		AppPicExample example = new AppPicExample();
		example.createCriteria().andAppInfoEqualTo(appId);
		List<AppPic> result = appPicMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(result))
			return null;
		else
			return result;
	}

	// 查看单个appPic信息
	public AppPic getAppPic(int id) {
		return appPicMapper.selectByPrimaryKey(id);
	}

	// 增加appPic信息
	public void addAppPic(AppPic appPic) {
		appPicMapper.insert(appPic);
	}

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	public int insertSelective(AppPic record) {
		return appPicMapper.insertSelective(record);
	}

	// 更新appPic信息
	public void upAppPic(AppPic appPic) {
		appPicMapper.updateByPrimaryKey(appPic);
	}

	// 删除appPic信息
	public void delAppPic(int id) {
		AppPic pic = appPicMapper.selectByPrimaryKey(id);
		if (pic.isState() == true) {
			pic.setState(false);
			appPicMapper.updateByPrimaryKey(pic);
		} else {
			String url = "";
			if (pic != null) {
				url = pic.getUrl();
			}
			int type = appPicMapper.deleteByPrimaryKey(id);
			if (type > 0) {
				try {
					// 删除对应文件
					FileUtil.deleteFile(url);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 删除appPic信息
	public void delAppPics(int id) {
		AppPic pic = appPicMapper.selectByPrimaryKey(id);
		String url = "";
		if (pic != null) {
			url = pic.getUrl();
		}
		int type = appPicMapper.deleteByPrimaryKey(id);
		if (type > 0) {
			try {
				// 删除对应文件
				FileUtil.deleteFile(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 同时删除多个appPic
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delAppPic(id);
		}
	}

	public void updateSortByPrimarykey(AppPic entity) {
		appPicMapper.updateSortByPrimarykey(entity);
	}

}
