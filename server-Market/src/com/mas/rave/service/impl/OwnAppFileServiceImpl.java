package com.mas.rave.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mas.rave.dao.OwnAppFileListMapper;
import com.mas.rave.dao.OwnAppFileMapper;
import com.mas.rave.dao.OwnAppFilePatchMapper;
import com.mas.rave.main.vo.OwnAppFile;
import com.mas.rave.main.vo.OwnAppFileList;
import com.mas.rave.main.vo.OwnAppFilePatch;
import com.mas.rave.service.OwnAppFileListService;
import com.mas.rave.service.OwnAppFileService;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.StringUtil;

/**
 * app对应文件
 * 
 * @author liwei.sz
 * 
 */
@Service
public class OwnAppFileServiceImpl implements OwnAppFileService {
	@Autowired
	private OwnAppFileMapper ownAppFileMapper;

	@Autowired
	private OwnAppFileListService ownAppFileListService;

	public List<OwnAppFile> searchOwnAppFiles(OwnAppFile criteria) {
		List<OwnAppFile> result = ownAppFileMapper.selectByExample(criteria);
		return result;
	}

	public List<OwnAppFile> getOwnAppFiles(Integer appId) {
		List<OwnAppFile> result = ownAppFileMapper.getOwnAppFiles(appId);
		if (CollectionUtils.isEmpty(result))
			return null;
		else
			return result;
	}

	public List<OwnAppFile> getOwnAppFiles() {
		List<OwnAppFile> result = ownAppFileMapper.selectByExample(new OwnAppFile());
		if (CollectionUtils.isEmpty(result))
			return null;
		else
			return result;
	}

	// 查看单个app文件信息
	public OwnAppFile getOwnAppFile(int id) {
		return ownAppFileMapper.selectByPrimaryKey(id);
	}

	// 增加app文件信息
	public int addOwnAppFile(OwnAppFile OwnAppFile) {
		OwnAppFile.setUpdateInfo(StringUtil.convertBlankLinesToHashSymbolBr(OwnAppFile.getUpdateInfo()));
		return ownAppFileMapper.insert(OwnAppFile);
	}

	// 更新app文件信息
	public int upOwnAppFile(OwnAppFile OwnAppFile) {
		OwnAppFile.setUpdateInfo(StringUtil.convertBlankLinesToHashSymbolBr(OwnAppFile.getUpdateInfo()));
		return ownAppFileMapper.updateByPrimaryKey(OwnAppFile);
	}

	public int updateOwnAppFileState(OwnAppFile OwnAppFile) {
		return ownAppFileMapper.updateState(OwnAppFile);
	}

	// 删除app文件信息
	public void delOwnAppFile(int id) {
		OwnAppFile file = ownAppFileMapper.selectByPrimaryKey(id);
		if (file != null) {
			try {
				// 清除对应文件
				// 获取历史记录
				List<OwnAppFileList> files = ownAppFileListService.getOwnAppFileLists(id);
				if (files != null && files.size() > 0) {
					for (OwnAppFileList list : files) {
						try {
							// 删除历史对应文件
							FileUtil.deleteFile(list.getUrl());
							ownAppFileListService.delOwnAppFileList(list.getId());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				// 删除对应文件
				FileUtil.deleteFile(file.getUrl());
				ownAppFileMapper.deleteByPrimaryKey(id);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// 同时删除多个app文件
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delOwnAppFile(id);
		}
	}

	public List<OwnAppFile> getByParameter(HashMap<String, Object> map) {
		return ownAppFileMapper.getByParameter(map);
	}

	public OwnAppFile selectByPrimaryKey(int id) {
		return ownAppFileMapper.selectByPrimaryKey(id);
	}

}
