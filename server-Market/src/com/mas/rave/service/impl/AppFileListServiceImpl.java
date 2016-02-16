package com.mas.rave.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.AppFileListMapper;
import com.mas.rave.dao.AppFileMapper;
import com.mas.rave.dao.AppFilePatchMapper;
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.AppFileList;
import com.mas.rave.main.vo.AppFileListExample;
import com.mas.rave.main.vo.AppFilePatch;
import com.mas.rave.service.AppFileListService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.FileUtil;

/**
 * app对应历史文件
 * 
 * @author liwei.sz
 * 
 */
@Service
public class AppFileListServiceImpl implements AppFileListService {
	@Autowired
	private AppFileListMapper appFileListMapper;

	@Autowired
	private AppFileMapper appFileMapper;

	@Autowired
	private AppFilePatchMapper appFilePatchMapper;

	@Override
	public PaginationVo<AppFileList> searchAppFileLists(AppFileListCriteria criteria, int currentPage, int pageSize) {
		AppFileListExample example = new AppFileListExample();
		Map<Integer, Object> params = criteria.getParams();
		if (params != null && params.size() > 0) {
			if (StringUtils.isNotEmpty(params.get(1).toString())) {
				// 根据id查看
				// example.createCriteria().andNameLike(params.get(1).toString());
			}
		}
		List<AppFileList> data = appFileListMapper.selectByExample(example, new RowBounds((currentPage - 1) * pageSize, pageSize));

		int recordCount = appFileListMapper.countByExample(example);
		PaginationVo<AppFileList> result = new PaginationVo<AppFileList>(data, recordCount, pageSize, currentPage);
		return result;
	}

	@Override
	public List<AppFileList> searchAppFileLists(AppFileListCriteria criteria) {
		return null;
	}

	@Override
	public List<AppFileList> getAppFileLists(Integer apkId) {
		AppFileListExample example = new AppFileListExample();
		example.createCriteria().andApkIdEqualTo(apkId);
		return appFileListMapper.selectByExample(example);
	}

	@Override
	public List<AppFileList> getAppFileLists() {
		AppFileListExample example = new AppFileListExample();
		example.setOrderByClause("versionCode");
		return appFileListMapper.selectByExample(example);
	}

	@Override
	public AppFileList getAppFileList(int id) {
		return appFileListMapper.selectByPrimaryKey(id);
	}

	@Override
	public int addAppFileList(AppFileList obj) {
		return appFileListMapper.insert(obj);
	}

	@Override
	public int upAppFileList(AppFileList obj) {
		return appFileListMapper.updateByPrimaryKey(obj);
	}

	@Override
	public void delAppFileList(int id) {
		AppFileList file = appFileListMapper.selectByPrimaryKey(id);

		if (file != null) {
			List<AppFileList> list = getAppFileLists(file.getApkId());
			if (list != null && list.size() == 1) {
				// 如果最后一条数据更新文件状态
				AppFile appFile = appFileMapper.selectByPrimaryKey(list.get(0).getApkId());
				if (appFile != null) {
					appFile.setHaslist(false);
					appFileMapper.updateByPrimaryKey(appFile);
				}
			}

			// 清除对应差异包
			HashMap<String, Object> map = new HashMap<String, Object>(2);
			map.put("apkId", file.getApkId());
			map.put("lowVersionCode", file.getVersionCode());
			AppFilePatch patch = appFilePatchMapper.getAppFilePtchByApkId(map);
			if (patch != null) {
				try {
					// 删除对应文件
					FileUtil.deleteFile(patch.getUrl());
					appFilePatchMapper.deleteByPrimaryKey(patch.getId());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				// 删除对应文件
				FileUtil.deleteFile(file.getUrl());
				appFileListMapper.deleteByPrimaryKey(id);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delAppFileList(id);
		}
	}

	public AppFileList checkFile(String pac, int versionCode, int apkId) {
		HashMap<String, Object> map = new HashMap<String, Object>(2);
		map.put("packageName", pac);
		map.put("versionCode", versionCode);
		map.put("apkId", apkId);
		return appFileListMapper.checkFile(map);
	}

	// 转换文件
	public void convertFile(AppFile file) {
		if (checkFile(file.getPackageName(), file.getId(), file.getVersionCode()) == null) {
			AppFileList fileList = new AppFileList();
			fileList.setAppInfo(file.getAppInfo());
			fileList.setApkId(file.getId());
			fileList.setAppName(file.getAppName());
			fileList.setChannel(file.getChannel());
			fileList.setCp(file.getCp());
			fileList.setApkKey(file.getApkKey());
			fileList.setServerId(file.getServerId());
			fileList.setUpgradeType(file.getUpgradeType());
			fileList.setPackageName(file.getPackageName());
			fileList.setOsType(file.getOsType());
			fileList.setVersionCode(file.getVersionCode());
			fileList.setVersionName(file.getVersionName());
			fileList.setUrl(file.getUrl());
			fileList.setResolution(file.getResolution());
			fileList.setFileSize(file.getFileSize());
			fileList.setCpChannelCode(file.getCpChannelCode());
			fileList.setUpdateInfo(file.getUpdateInfo());
			fileList.setRemark(file.getRemark());
			fileList.setState(file.isState());
			fileList.setLanguage(file.getLanguage());
			addAppFileList(fileList);

			if (file.getAppInfo()!=null && file.getAppInfo().getFree() != 1) {
				// 获取所有文件
				List<AppFileList> list = getAppFileLists(file.getId());
				if (list != null && list.size() > Integer.parseInt(Constant.HAS_LIST)) {
					// 保留四条数据
					delAppFileList(list.get(0).getId());
				}
			}
		}
	}
}
