package com.mas.rave.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.OwnAppInfoMapper;
import com.mas.rave.main.vo.OwnAppFile;
import com.mas.rave.main.vo.OwnAppInfo;
import com.mas.rave.service.OwnAppFileService;
import com.mas.rave.service.OwnAppInfoService;
import com.mas.rave.util.FileUtil;

/**
 * app信息
 * 
 * @author liwei.sz
 * 
 */

@Service
public class OwnAppInfoServiceImpl implements OwnAppInfoService {
	@Autowired
	private OwnAppInfoMapper ownAppMapper;

	@Autowired
	private OwnAppFileService ownAppFileService;

	public PaginationVo<OwnAppInfo> searchOwnApps(OwnAppInfo example, int currentPage, int pageSize) {
		int recordCount = ownAppMapper.countByExample(example);
		example.setCurrentPage((currentPage - 1) * pageSize);
		example.setPageSize(pageSize);
		List<OwnAppInfo> data = ownAppMapper.selectByExample(example);
		PaginationVo<OwnAppInfo> result = new PaginationVo<OwnAppInfo>(data, recordCount, pageSize, currentPage);
		return result;

	}

	public OwnAppInfo searchOwnApp(OwnAppInfo criteria) {
		List<OwnAppInfo> data = ownAppMapper.selectByExample(criteria);
		if (data != null) {
			return data.get(0);
		} else {
			return null;
		}
	}

	public List<OwnAppInfo> searchOwnApps(OwnAppInfo criteria) {
		List<OwnAppInfo> data = ownAppMapper.queryAll(criteria);
		return data;
	}

	// 获取所有app信息
	public List<OwnAppInfo> getAllOwnAppInfos() {
		return ownAppMapper.queryAll(null);
	}

	// 查看单个app信息
	public OwnAppInfo getOwnApp(long id) {
		return ownAppMapper.selectByPrimaryKey(id);
	}

	// 增加app信息
	public int addOwnApp(OwnAppInfo app) {
		return ownAppMapper.insert(app);
	}

	// 更新app信息
	public void upOwnApp(OwnAppInfo app) {
		ownAppMapper.updateByPrimaryKey(app);
	}

	// 删除app信息
	public void delOwnApp(Integer id) {
		// 删除logo图片
		OwnAppInfo appInfo = ownAppMapper.selectByPrimaryKey(id);
		if (appInfo != null) {
			// 删除文件
			try {
				// 删除对应文件
				List<OwnAppFile> files = ownAppFileService.getOwnAppFiles(id.intValue());
				if (files != null && files.size() > 0) {
					for (OwnAppFile file : files) {
						ownAppFileService.delOwnAppFile(file.getId());
					}
				}

				// 删除对应文件
				FileUtil.deleteFile(appInfo.getLogo());
				FileUtil.deleteFile(appInfo.getBigLogo());
				ownAppMapper.deleteByPrimaryKey(id);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 同时删除多个app
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delOwnApp(id);
		}
	}

	// 更新对应apk信息
	public void upOwnAppFile(OwnAppInfo appInfo) {
		List<OwnAppFile> files = ownAppFileService.getOwnAppFiles(appInfo.getId());
		if (files != null && files.size() > 0) {
			for (OwnAppFile file : files) {
				if (!appInfo.getName().trim().equals(file.getAppName())) {
					file.setAppName(appInfo.getName());
					ownAppFileService.updateOwnAppFileState(file);
				}
			}
		}
	}

	@Override
	public OwnAppInfo findOwnAppInfo(String appName) {
		return ownAppMapper.selectByName(appName);
	}

	@Override
	public int insertSelective(OwnAppInfo record) {
		// TODO Auto-generated method stub
		return 0;
	}

}
