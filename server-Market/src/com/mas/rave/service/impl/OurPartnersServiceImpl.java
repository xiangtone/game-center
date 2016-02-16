package com.mas.rave.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.OurPartnersMapper;
import com.mas.rave.main.vo.AppPic;
import com.mas.rave.main.vo.OurPartners;
import com.mas.rave.main.vo.OurPartnersExample;
import com.mas.rave.service.OurPartnersService;
import com.mas.rave.util.FileUtil;

/**
 * 广告全作
 * 
 * @author liwei.sz
 * 
 */
@Service
public class OurPartnersServiceImpl implements OurPartnersService {

	@Autowired
	private OurPartnersMapper ourPartnersMapper;

	@Override
	public PaginationVo<OurPartners> searchOurPartners(OurPartnersCriteria criteria, int currentPage, int pageSize) {
		OurPartnersExample example = new OurPartnersExample();
		Map<Integer, Object> params = criteria.getParams();
		for (Integer key : params.keySet()) {
			if (key.equals(1)) {
				// 根据id查看
				example.createCriteria().andNameLike(params.get(1).toString());
			}
		}
		List<OurPartners> data = ourPartnersMapper.selectByExample(example, new RowBounds((currentPage - 1) * pageSize, pageSize));
		int recordCount = ourPartnersMapper.countByExample(example);
		PaginationVo<OurPartners> result = new PaginationVo<OurPartners>(data, recordCount, pageSize, currentPage);
		return result;
	}

	@Override
	public List<OurPartners> getOurPartners() {
		// TODO Auto-generated method stub
		OurPartnersExample example = new OurPartnersExample();
		return ourPartnersMapper.selectByExample(example);
	}

	@Override
	public OurPartners getOurPartners(int id) {
		// TODO Auto-generated method stub
		return ourPartnersMapper.selectByPrimaryKey(id);
	}

	@Override
	public int addAppOurPartners(OurPartners obj) {
		// TODO Auto-generated method stub
		return ourPartnersMapper.insert(obj);
	}

	@Override
	public int upOurPartners(OurPartners obj) {
		return ourPartnersMapper.updateByPrimaryKey(obj);
	}

	@Override
	public void delAppOurPartners(int id) {
		OurPartners our = ourPartnersMapper.selectByPrimaryKey(id);
		String url = "";
		if (our != null) {
			url = our.getLogo();
		}
		int type = ourPartnersMapper.deleteByPrimaryKey(id);
		if (type > 0) {
			try {
				// 删除对应文件
				FileUtil.deleteFile(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delAppOurPartners(id);
		}
	}

	/**
	 * 更新排序
	 * 
	 * @param record
	 */
	public void updateSortByPrimarykey(OurPartners record) {
		ourPartnersMapper.updateSortByPrimarykey(record);
	}

}
