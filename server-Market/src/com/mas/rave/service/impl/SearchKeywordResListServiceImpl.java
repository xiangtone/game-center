package com.mas.rave.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.SearchKeywordResListMapper;
import com.mas.rave.main.vo.SearchKeywordResList;
import com.mas.rave.service.SearchKeywordResListService;
@Service
public class SearchKeywordResListServiceImpl implements SearchKeywordResListService{
	@Autowired
    private SearchKeywordResListMapper searchKeywordResListMapper;
	@Override
	public PaginationVo<SearchKeywordResList> selectByExample(
			SearchKeywordResList record, int currentPage,
			int pageSize) {
		int recordCount = searchKeywordResListMapper.countByExample(record);
        record.setCurrentPage((currentPage - 1) * pageSize);
        record.setPageSize(pageSize);
        List<SearchKeywordResList> data = searchKeywordResListMapper.selectByExample(record);
        PaginationVo<SearchKeywordResList> result = new PaginationVo<SearchKeywordResList>(data, recordCount, pageSize, currentPage);
        return result;
	}

	@Override
	public SearchKeywordResList selectByPrimaryKey(int id) {
		 return searchKeywordResListMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<SearchKeywordResList> selectBySearchId(int searchId) {
		   return searchKeywordResListMapper.selectBySearchId(searchId);
	}

	@Override
	public int deleteByPrimaryKey(int id) {
		 return searchKeywordResListMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int deleteBySearchId(int searchId) {
		 return searchKeywordResListMapper.deleteBySearchId(searchId);
	}

	@Override
	public int insert(SearchKeywordResList record) {
		return searchKeywordResListMapper.insert(record);
	}

	@Override
	public void updateByPrimaryKey(SearchKeywordResList record) {
		searchKeywordResListMapper.updateByPrimaryKey(record);
		
	}

}
