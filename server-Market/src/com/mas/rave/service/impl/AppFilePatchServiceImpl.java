package com.mas.rave.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mas.rave.dao.AppFilePatchMapper;
import com.mas.rave.main.vo.AppFilePatch;
import com.mas.rave.service.AppFilePatchService;
import com.mas.rave.util.FileUtil;

/**
 * app对应文件
 * 
 * @author lin.lu
 * 
 */
@Service
public class AppFilePatchServiceImpl implements AppFilePatchService {
	@Autowired
	private AppFilePatchMapper appFilePatchMapper;

	public List<AppFilePatch> getAppFilePatchs(Integer apkId) {
		List<AppFilePatch> result = appFilePatchMapper.getAppFilePatchs(apkId);
		if (CollectionUtils.isEmpty(result))
			return null;
		else
			return result;
	}

	// 增加app文件信息
	public int addAppFilePatch(AppFilePatch appFilePatch) {
		return appFilePatchMapper.insert(appFilePatch);
	}

	// 删除app文件信息
	public void delAppFilePatch(int id) {
		AppFilePatch file = appFilePatchMapper.selectByPrimaryKey(id);
		String url = "";
		if (file != null) {
			url = file.getUrl();
		}
		int type = appFilePatchMapper.deleteByPrimaryKey(id);
		if (type > 0) {
			try {
				// 删除对应文件
				FileUtil.deleteFile(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 同时删除多个app文件
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delAppFilePatch(id);
		}
	}

	// 获取对应差异包
	public AppFilePatch getAppFilePtchByApkId(int apkId, int lowVersionCode) {
		HashMap<String, Object> map = new HashMap<String, Object>(2);
		map.put("apkId", apkId);
		map.put("lowVersionCode", lowVersionCode);
		return appFilePatchMapper.getAppFilePtchByApkId(map);
	}

}
