package com.mas.rave.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.ClientSkinMapper;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.main.vo.ClientSkin;
import com.mas.rave.service.ClientSkinService;
import com.mas.rave.util.FileUtil;

/**
 * 皮肤管理
 * 
 * @author liwei.sz
 * 
 */
@Service
public class ClientSkinServiceImpl implements ClientSkinService {

	@Autowired
	private ClientSkinMapper clientSkinMapper;

	@Override
	public int countByExample(ClientSkin example) {
		// TODO Auto-generated method stub
		return clientSkinMapper.countByExample(example);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		ClientSkin skin = clientSkinMapper.selectByPrimaryKey(id);
		if (skin != null) {
			try {
				FileUtil.deleteFile(skin.getApkUrl());
				FileUtil.deleteFile(skin.getLogo());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return clientSkinMapper.deleteByPrimaryKey(id);
		}
		return 0;
	}

	@Override
	public int insert(ClientSkin record) {
		// TODO Auto-generated method stub
		return clientSkinMapper.insert(record);
	}

	@Override
	public ClientSkin selectByPackName(String pac_name) {
		// TODO Auto-generated method stub
		return clientSkinMapper.selectByPackName(pac_name);
	}

	@Override
	public ClientSkin selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return clientSkinMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(ClientSkin record) {
		// TODO Auto-generated method stub
		return clientSkinMapper.updateByPrimaryKey(record);
	}

	@Override
	public PaginationVo<ClientSkin> searchClientSkin(ClientSkin example, int currentPage, int pageSize) {
		int recordCount = clientSkinMapper.countByExample(example);
		example.setCurrentPage((currentPage - 1) * pageSize);
		example.setPageSize(pageSize);
		List<ClientSkin> data = clientSkinMapper.selectByExample(example);
		PaginationVo<ClientSkin> result = new PaginationVo<ClientSkin>(data, recordCount, pageSize, currentPage);
		return result;
	}

	// 同时删除多个app文件
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			deleteByPrimaryKey(id);
		}
	}

	/**
	 * 更新排序
	 * 
	 * @param record
	 */
	public void updateSortByPrimarykey(ClientSkin record) {
		clientSkinMapper.updateSortByPrimarykey(record);
	}
}
