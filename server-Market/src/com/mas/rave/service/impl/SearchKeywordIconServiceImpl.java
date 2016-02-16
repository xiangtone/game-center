package com.mas.rave.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.SearchKeywordIconMapper;
import com.mas.rave.dao.SearchKeywordMapper;
import com.mas.rave.main.vo.SearchKeyword;
import com.mas.rave.main.vo.SearchKeywordIcon;
import com.mas.rave.main.vo.SearchKeywordIconExample;
import com.mas.rave.main.vo.SearchKeywordIconExample.Criteria;
import com.mas.rave.service.SearchKeywordIconService;
import com.mas.rave.util.FileUtil;

/**
 * country
 * 
 * @author jieding
 * 
 */
@Service
public class SearchKeywordIconServiceImpl implements SearchKeywordIconService {

	@Autowired
	private SearchKeywordIconMapper searchKeywordIconMapper;
	
	@Autowired
	private SearchKeywordMapper searchKeywordMapper;

	@Override
	public List<SearchKeywordIcon> getSearchKeywordIcons() {
		// TODO Auto-generated method stub
		return searchKeywordIconMapper.getSearchKeywordIcons();
	}

	@Override
	public PaginationVo<SearchKeywordIcon> selectByExample(SearchKeywordIconCriteria criteria, int currentPage, int pageSize) {
		SearchKeywordIconExample example = new SearchKeywordIconExample();
		Map<Integer, Object> params = criteria.getParams();
		Criteria criteria1 = example.createCriteria();
		for (Integer key : params.keySet()) {
			if (key.equals(1)) {				
				criteria1.andNameLike(params.get(key).toString());
			}
			if (key.equals(2)) {
				criteria1.andStateEqual((Boolean)params.get(key));
			}
		}
		example.setOrderByClause("createTime desc");
		List<SearchKeywordIcon> data = searchKeywordIconMapper.selectByExample(example, new RowBounds((currentPage - 1) * pageSize, pageSize));
		int recordCount = searchKeywordIconMapper.countByExample(example);
		PaginationVo<SearchKeywordIcon> result = new PaginationVo<SearchKeywordIcon>(data, recordCount, pageSize, currentPage);
		return result;
	}

	@Override
	public SearchKeywordIcon getSearchKeywordIcon(int id) {
		// TODO Auto-generated method stub
		return searchKeywordIconMapper.selectByPrimaryKey(id);
	}

	@Override
	public void addSearchKeywordIcon(SearchKeywordIcon searchKeywordIcon) {
		// TODO Auto-generated method stub
		searchKeywordIconMapper.insert(searchKeywordIcon);
	}

	@Override
	public void upSearchKeywordIcon(SearchKeywordIcon searchKeywordIcon) {
		// TODO Auto-generated method stub
		searchKeywordIconMapper.updateByPrimaryKey(searchKeywordIcon);
		List<SearchKeyword> searchkeywordList = searchKeywordMapper.selectByIconId(searchKeywordIcon.getId());
		for(SearchKeyword searchkeyword:searchkeywordList){
			searchkeyword.setIconId(searchKeywordIcon.getId());
			if(searchKeywordIcon.getState()==true){
				searchkeyword.setIconUrl(searchKeywordIcon.getUrl());			
			}else{
				searchkeyword.setIconUrl(null);
			}
			searchKeywordMapper.updateByPrimaryKey(searchkeyword);
		}

	}

	@Override
	public void delSearchKeywordIcon(int id) {
		SearchKeywordIcon searchIcon =searchKeywordIconMapper.selectByPrimaryKey(id);
		if(searchIcon!=null){
			try {
				FileUtil.deleteFile(searchIcon.getUrl());
				List<SearchKeyword> searchkeywordList = searchKeywordMapper.selectByIconId(id);
				for(SearchKeyword searchkeyword:searchkeywordList){
					searchkeyword.setIconId(null);			
					searchkeyword.setIconUrl(null);
					searchKeywordMapper.updateByPrimaryKey(searchkeyword);
				}
				searchKeywordIconMapper.deleteByPrimaryKey(id);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void batchDelete(Integer[] ids) {
		for (int id : ids) {
			delSearchKeywordIcon(id);
		}
	}

	@Override
	public void updateState(int id, int state) {
		HashMap<String, Object> map = new HashMap<String, Object>(2);
		map.put("id", id);
		map.put("state", state);
		searchKeywordIconMapper.updateState(map);
	}
}
