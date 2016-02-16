package com.mas.rave.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mas.rave.dao.AppAlbumResMapper;
import com.mas.rave.dao.AppAlbumResTempMapper;
import com.mas.rave.dao.AppAlbumThemeMapper;
import com.mas.rave.dao.AppCollectionResMapper;
import com.mas.rave.dao.AppFileListMapper;
import com.mas.rave.dao.AppFileMapper;
import com.mas.rave.dao.AppFilePatchMapper;
import com.mas.rave.main.vo.AppAlbumRes;
import com.mas.rave.main.vo.AppAlbumResExample;
import com.mas.rave.main.vo.AppAlbumTheme;
import com.mas.rave.main.vo.AppAlbumThemeExample;
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.AppFileExample;
import com.mas.rave.main.vo.AppFileList;
import com.mas.rave.main.vo.AppFileListExample;
import com.mas.rave.main.vo.AppFilePatch;
import com.mas.rave.main.vo.Channel;
import com.mas.rave.service.AppFileService;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.StringUtil;

/**
 * app对应文件
 * 
 * @author liwei.sz
 * 
 */
@Service
public class AppFileServiceImpl implements AppFileService {
	@Autowired
	private AppFileMapper appFileMapper;

	@Autowired
	private AppAlbumResMapper appAlbumResMapper;

	@Autowired
	private AppFilePatchMapper appFilePatchMapper;

	@Autowired
	private AppFileListMapper appFileListMapper;

	@Autowired
	private AppAlbumResTempMapper appAlbumResTempMapper;

	@Autowired
	private AppAlbumThemeMapper appAlbumThemeMapper;

	@Autowired
	private AppCollectionResMapper appCollectionResMapper;
	
	public List<AppFile> searchAppFiles(AppFileCriteria criteria) {
		AppFileExample example = new AppFileExample();
		Map<Integer, Object> params = criteria.getParams();
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("raveId", params.get(5));
		for (Integer key : params.keySet()) {
			if (key.equals(4)) {
				example.createCriteria().andAppNameLike(params.get(4).toString());
			}

		}
		example.setMap(params1);
		List<AppFile> result = appFileMapper.selectByExample(example);
		return result;
	}

	public List<AppFile> getAppFiles(Integer appId) {
		List<AppFile> result = appFileMapper.getAppFiles(appId);
		if (CollectionUtils.isEmpty(result))
			return null;
		else
			return result;
	}

	public List<AppFile> getAppFilesGroupBy(Integer appId) {
		List<AppFile> result = appFileMapper.getAppFilesGroupBy(appId);
		if (CollectionUtils.isEmpty(result))
			return null;
		else
			return result;
	}

	public List<AppFile> getAppFiles() {
		AppFileExample example = new AppFileExample();
		List<AppFile> result = appFileMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(result))
			return null;
		else
			return result;
	}

	// 查看单个app文件信息
	public AppFile getAppFile(int id) {
		return appFileMapper.selectByPrimaryKey(id);
	}

	// 根据包名获取文件
	public AppFile getAppFileByPac(String pack) {
		AppFileExample example = new AppFileExample();
		example.createCriteria().andPacEqualTo(pack);
		List<AppFile> result = appFileMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(result))
			return null;
		else
			return result.get(0);
	}

	// 根据包名获取文件
	public List<AppFile> getAppFileByPacs(String pack, Integer appId, Integer channelId, Integer raveId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("packageName", pack);
		map.put("appId", appId);
		map.put("channelId", channelId);
		map.put("raveId", raveId);
		return appFileMapper.getAppFileByPacs(map);
	}

	// 根据packageName及国家ID查看单个app文件信息
	public AppFile getAppFileByappIdAndCountry(Integer appId, Integer raveId, Integer channelId) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("appId", appId);
		map.put("raveId", raveId);
		map.put("channelId", channelId);
		List<AppFile> list = appFileMapper.getAppFileByAppIdAndRaveIdAndChannel(map);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	// 增加app文件信息
	public int addAppFile(AppFile appFile) {
		appFile.setUpdateInfo(StringUtil.convertBlankLinesToHashSymbolBr(appFile.getUpdateInfo()));
		return appFileMapper.insert(appFile);
	}

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	public int insertSelective(AppFile record) {
		return appFileMapper.insertSelective(record);
	}

	// 更新app文件信息
	public int upAppFile(AppFile appFile) {
		appFile.setUpdateInfo(StringUtil.convertBlankLinesToHashSymbolBr(appFile.getUpdateInfo()));
		return appFileMapper.updateByPrimaryKey(appFile);
	}

	public int updateAppFileState(AppFile appFile) {
		if (appFile.isState() == false) {
			updateResourceByApkId(appFile.getId());// 删除分发记录
		}
		return appFileMapper.updateState(appFile);
	}

	// 删除app文件信息
	public void delAppFile(int id) {
		AppFile file = appFileMapper.selectByPrimaryKey(id);
		if (file != null) {

			// 清除对应分发信息
			// AppAlbumResExample example = new AppAlbumResExample(); //
			// 获取对应app信息
			// example.createCriteria().andApkId(id);
			// List<AppAlbumRes> res =
			// appAlbumResMapper.selectByExample(example);
			// if (res != null && res.size() > 0) {
			// for (AppAlbumRes re : res) {
			// appAlbumResMapper.deleteByPrimaryKey(re.getId());
			// }
			// }
			if (file.isState() == true) {
				file.setState(false);
				appFileMapper.updateByPrimaryKey(file);

				updateResourceByApkId(id);

				AppFileListExample example1 = new AppFileListExample();
				example1.createCriteria().andApkIdEqualTo(id);
				// 获取历史记录
				List<AppFileList> files = appFileListMapper.selectByExample(example1);
				if (files != null && files.size() > 0) {
					for (AppFileList list : files) {
						try {
							// 删除历史对应文件
							FileUtil.deleteFile(list.getUrl());
							appFileListMapper.deleteByPrimaryKey(list.getId());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				// 获取差异列表　
				List<AppFilePatch> patchs = appFilePatchMapper.getAppFilePatchs(id);
				if (patchs != null && patchs.size() > 0) {
					for (AppFilePatch patch : patchs) {
						try {
							// 删除差异对应文件
							FileUtil.deleteFile(patch.getUrl());
							appFilePatchMapper.deleteByPrimaryKey(patch.getId());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			} else {
				try {
					// 删除对应文件
					FileUtil.deleteFile(file.getUrl());
					appFileMapper.deleteByPrimaryKey(id);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	// 删除app文件信息
	public void delAppFiles(int id) {
		AppFile file = appFileMapper.selectByPrimaryKey(id);
		if (file != null) {
			file.setState(false);
			appFileMapper.updateByPrimaryKey(file);

			updateResourceByApkId(id);

			AppFileListExample example1 = new AppFileListExample();
			example1.createCriteria().andApkIdEqualTo(id);
			// 获取历史记录
			List<AppFileList> files = appFileListMapper.selectByExample(example1);
			if (files != null && files.size() > 0) {
				for (AppFileList list : files) {
					try {
						// 删除历史对应文件
						FileUtil.deleteFile(list.getUrl());
						appFileListMapper.deleteByPrimaryKey(list.getId());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			// 获取差异列表　
			List<AppFilePatch> patchs = appFilePatchMapper.getAppFilePatchs(id);
			if (patchs != null && patchs.size() > 0) {
				for (AppFilePatch patch : patchs) {
					try {
						// 删除差异对应文件
						FileUtil.deleteFile(patch.getUrl());
						appFilePatchMapper.deleteByPrimaryKey(patch.getId());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				// 删除对应文件
				FileUtil.deleteFile(file.getUrl());
				appFileMapper.deleteByPrimaryKey(id);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 更新相关资源
	 * 
	 * @param apkId
	 */
	public void updateResourceByApkId(int apkId) {
		deleteAlbumResByApkId(apkId); // 分发
		updateAppAlbumThemeByApkId(apkId);// 广告
	}

	/**
	 * 删除分发记录
	 * 
	 * @param apkId
	 */
	public void deleteAlbumResByApkId(int apkId) {
		HashMap<String, Object> map = new HashMap<String, Object>(2);
		map.put("apkId", apkId);
		map.put("appId", null);
		appAlbumResTempMapper.deleteByApp(map);
		appAlbumResMapper.deleteByApp(map);
		appCollectionResMapper.deleteByApp(map);
	}

	/**
	 * 更新apkId关联的广告资源
	 */
	public void updateAppAlbumThemeByApkId(int apkId) {
		// 平台广告
		AppAlbumThemeExample example = new AppAlbumThemeExample();
		example.createCriteria().andApkIdEqualTo(apkId);
		List<AppAlbumTheme> list = appAlbumThemeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			for (AppAlbumTheme one : list) {
				one.setState(false);// 失效
				one.setAppFile(null);// 不关联
				appAlbumThemeMapper.updateByPrimaryKey(one);
			}
		}
	}

	// 同时删除多个app文件
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delAppFile(id);
		}
	}

	// 获取app信息
	public AppAlbumRes getAppAlbumRes(int id, int type) {
		AppAlbumResExample example = new AppAlbumResExample();
		if (type == 1) {
			// 获取对应app信息
			example.createCriteria().andAppId(id);
		} else if (type == 2) {
			example.createCriteria().andApkId(id);
		}
		List<AppAlbumRes> res = appAlbumResMapper.selectByExample(example);
		if (res != null && res.size() > 0) {
			return res.get(0);
		} else {
			return null;
		}
	}
	// 更新对应内容分发信息
	public void upAppCollectionRes(AppFile appFile) {
		AppAlbumResExample example = new AppAlbumResExample();
		// 获取对应app信息
		example.createCriteria().andApkId(appFile.getId());
		List<AppAlbumRes> res = appCollectionResMapper.selectByExample(example);
		if (res != null && res.size() > 0)
			for (AppAlbumRes re : res) {
				re.setAppFile(appFile);
				re.setFileSize(appFile.getFileSize());
				re.setPackageName(appFile.getPackageName());
				re.setVersionCode(appFile.getVersionCode());
				re.setVersionName(appFile.getVersionName());
				re.setUrl(appFile.getUrl());
				re.setOperator("无");
				appCollectionResMapper.updateByPrimaryKey(re);
			}
	}
	// 更新对应内容分发信息
	public void upAppAlbumRes(AppFile appFile) {
		AppAlbumResExample example = new AppAlbumResExample();
		// 获取对应app信息
		example.createCriteria().andApkId(appFile.getId());
		List<AppAlbumRes> res = appAlbumResMapper.selectByExample(example);
		if (res != null && res.size() > 0)
			for (AppAlbumRes re : res) {
				re.setAppFile(appFile);
				re.setFileSize(appFile.getFileSize());
				re.setPackageName(appFile.getPackageName());
				re.setVersionCode(appFile.getVersionCode());
				re.setVersionName(appFile.getVersionName());
				re.setUrl(appFile.getUrl());
				re.setOperator("无");
				appAlbumResMapper.updateByPrimaryKey(re);
			}
	}

	/**
	 * 更新对应内容分发信息temp表 add by dingjie
	 */
	public void upAppAlbumResTemp(AppFile appFile) {
		AppAlbumResExample example = new AppAlbumResExample();
		// 获取对应app信息
		example.createCriteria().andApkId(appFile.getId());
		List<AppAlbumRes> res = appAlbumResTempMapper.selectByExample(example);
		if (res != null && res.size() > 0)
			for (AppAlbumRes re : res) {
				re.setAppFile(appFile);
				re.setFileSize(appFile.getFileSize());
				re.setPackageName(appFile.getPackageName());
				re.setVersionCode(appFile.getVersionCode());
				re.setVersionName(appFile.getVersionName());
				re.setUrl(appFile.getUrl());
				re.setOperator("无");
				appAlbumResTempMapper.updateByPrimaryKey(re);
			}
	}

	/**
	 * app信息变动
	 * 
	 * @param appId
	 * @param cpId
	 * @return
	 */
	public int upAppInfoByAppId(int appId, int cpId, String appName) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("appId", appId);
		map.put("cpId", cpId);
		map.put("appName", appName);
		// return appFileMapper.upAppFileByAppId(map);
		// add by lulin 20140520
		int re = 0;
		re += appFileMapper.upAppFileByAppId(map);
		re += appFileListMapper.upAppFileListByAppId(map);
		re += appFilePatchMapper.upAppFilePatchByAppId(map);
		return re;
	}

	@Override
	public AppFile finAppFile(int channelId, String packageName) {
		AppFile appFile = new AppFile();
		Channel channel = new Channel();
		channel.setId(channelId);
		appFile.setChannel(channel);
		appFile.setPackageName(packageName);
		appFile = appFileMapper.selectByChannelIdAndPack(appFile);
		return appFile;
	}

	public AppFile finAppFileByName(int channelId, String appName) {
		AppFile appFile = new AppFile();
		Channel channel = new Channel();
		channel.setId(channelId);
		appFile.setChannel(channel);
		appFile.setAppName(appName);
		appFile = appFileMapper.selectByChannelIdAndName(appFile);
		return appFile;
	}

	@Override
	public List<AppFile> getAppFileByName(String appName) {
		AppFile appFile = new AppFile();
		appFile.setAppName(appName);
		return appFileMapper.getAppFileByName(appFile);
	}

	// 获取未审核的应用
	public List<AppFile> getAllCpFile() {
		return appFileMapper.getAllCpFile();
	}

}
