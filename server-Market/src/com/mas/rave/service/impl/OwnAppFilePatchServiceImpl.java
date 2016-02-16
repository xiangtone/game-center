package com.mas.rave.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mas.rave.dao.OwnAppFilePatchMapper;
import com.mas.rave.main.vo.OwnAppFilePatch;
import com.mas.rave.service.OwnAppFilePatchService;
import com.mas.rave.util.FileUtil;

/**
 * app对应文件
 * 
 * @author lin.lu
 * 
 */
@Service
public class OwnAppFilePatchServiceImpl implements OwnAppFilePatchService {
	@Autowired
	private OwnAppFilePatchMapper ownAppFilePatchMapper;

	public List<OwnAppFilePatch> getOwnAppFilePatchs(Integer apkId) {
		List<OwnAppFilePatch> result = ownAppFilePatchMapper.getOwnAppFilePatchs(apkId);
		if (CollectionUtils.isEmpty(result))
			return null;
		else
			return result;
	}

	// 增加app文件信息
	public int addOwnAppFilePatch(OwnAppFilePatch appFilePatch) {
		return ownAppFilePatchMapper.insert(appFilePatch);
	}

	// 删除app文件信息
	public void delOwnAppFilePatch(int id) {
		OwnAppFilePatch file = ownAppFilePatchMapper.selectByPrimaryKey(id);
		String url = "";
		if (file != null) {
			url = file.getUrl();
		}
		int type = ownAppFilePatchMapper.deleteByPrimaryKey(id);
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
			delOwnAppFilePatch(id);
		}
	}

	// 获取对应差异包
	public OwnAppFilePatch getByApkId(int apkId, int lowVersionCode) {
		HashMap<String, Object> map = new HashMap<String, Object>(2);
		map.put("apkId", apkId);
		map.put("lowVersionCode", lowVersionCode);
		return ownAppFilePatchMapper.getPtchByApkId(map);
	}

}
