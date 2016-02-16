package com.mas.rave.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.OwnAppFileListMapper;
import com.mas.rave.dao.OwnAppFileMapper;
import com.mas.rave.dao.OwnAppFilePatchMapper;
import com.mas.rave.main.vo.OwnAppFile;
import com.mas.rave.main.vo.OwnAppFileList;
import com.mas.rave.main.vo.OwnAppFilePatch;
import com.mas.rave.service.OwnAppFileListService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.FileUtil;

/**
 * app对应历史文件
 * 
 * @author liwei.sz
 * 
 */
@Service
public class OwnAppFileListServiceImpl implements OwnAppFileListService {
	@Autowired
	private OwnAppFileListMapper ownAppFileListMapper;

	@Autowired
	private OwnAppFileMapper ownAppFileMapper;

	@Autowired
	private OwnAppFilePatchMapper ownAppFilePatchMapper;

	@Override
	public PaginationVo<OwnAppFileList> searchOwnAppFileLists(OwnAppFileList criteria, int currentPage, int pageSize) {
		List<OwnAppFileList> data = ownAppFileListMapper.selectByExample(criteria, new RowBounds((currentPage - 1) * pageSize, pageSize));
		int recordCount = ownAppFileListMapper.countByExample(criteria);
		PaginationVo<OwnAppFileList> result = new PaginationVo<OwnAppFileList>(data, recordCount, pageSize, currentPage);
		return result;
	}

	@Override
	public List<OwnAppFileList> searchOwnAppFileLists(OwnAppFileList criteria) {
		return null;
	}

	@Override
	public List<OwnAppFileList> getOwnAppFileLists(Integer apkId) {
		OwnAppFileList li = new OwnAppFileList();
		li.setApkId(apkId);
		return ownAppFileListMapper.selectByExample(li);
	}

	@Override
	public List<OwnAppFileList> getOwnAppFileLists() {
		return ownAppFileListMapper.selectByExample(null);
	}

	@Override
	public OwnAppFileList getOwnAppFileList(int id) {
		return ownAppFileListMapper.selectByPrimaryKey(id);
	}

	@Override
	public int addOwnAppFileList(OwnAppFileList obj) {
		return ownAppFileListMapper.insert(obj);
	}

	@Override
	public int upOwnAppFileList(OwnAppFileList obj) {
		return ownAppFileListMapper.updateByPrimaryKey(obj);
	}

	@Override
	public void delOwnAppFileList(int id) {
		OwnAppFileList file = ownAppFileListMapper.selectByPrimaryKey(id);
		if (file != null) {
			List<OwnAppFileList> list = getOwnAppFileLists(file.getApkId());
			if (list != null && list.size() == 1) {
				// 如果最后一条数据更新文件状态
				OwnAppFile appFile = ownAppFileMapper.selectByPrimaryKey(list.get(0).getApkId());
				if (appFile != null) {
					appFile.setHaslist(false);
					ownAppFileMapper.updateByPrimaryKey(appFile);
				}
			}

			// 清除对应差异包
			HashMap<String, Object> map = new HashMap<String, Object>(2);
			map.put("apkId", file.getApkId());
			map.put("lowVersionCode", file.getVersionCode());
			OwnAppFilePatch patch = ownAppFilePatchMapper.getPtchByApkId(map);
			if (patch != null) {
				try {
					// 删除对应文件
					FileUtil.deleteFile(patch.getUrl());
					ownAppFilePatchMapper.deleteByPrimaryKey(patch.getId());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				// 删除对应文件
				FileUtil.deleteFile(file.getUrl());
				ownAppFileListMapper.deleteByPrimaryKey(id);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delOwnAppFileList(id);
		}
	}

	public OwnAppFileList checkFile(String pac, int versionCode, int apkId) {
		HashMap<String, Object> map = new HashMap<String, Object>(2);
		map.put("packageName", pac);
		map.put("versionCode", versionCode);
		map.put("apkId", apkId);
		return ownAppFileListMapper.checkFile(map);
	}

	// 转换文件
	public void convertFile(OwnAppFile file) {
		if (checkFile(file.getPackageName(), file.getId(), file.getVersionCode()) == null) {
			OwnAppFileList fileList = new OwnAppFileList();
			fileList.setAppInfo(file.getAppInfo());
			fileList.setApkId(file.getId());
			fileList.setAppName(file.getAppName());
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
			addOwnAppFileList(fileList);

			if (file.getAppInfo() != null) {
				// 获取所有文件
				List<OwnAppFileList> list = getOwnAppFileLists(file.getId());
				if (list != null && list.size() > Integer.parseInt(Constant.HAS_LIST)) {
					// 保留四条数据
					delOwnAppFileList(list.get(0).getId());
				}
			}
		}
	}

}
