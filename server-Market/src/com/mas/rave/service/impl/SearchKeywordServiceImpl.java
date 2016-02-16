package com.mas.rave.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.SearchKeywordMapper;
import com.mas.rave.dao.SearchKeywordResListMapper;
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.main.vo.SearchKeyword;
import com.mas.rave.main.vo.SearchKeywordResList;
import com.mas.rave.service.SearchKeywordService;
import com.mas.rave.util.FileUtil;
@Service
public class SearchKeywordServiceImpl implements SearchKeywordService {

	@Autowired
    private SearchKeywordMapper searchKeywordMapper;
	@Autowired
    private SearchKeywordResListMapper searchKeywordResListMapper;
	@Override
	public PaginationVo<SearchKeyword> selectByExample(
			SearchKeyword record, int currentPage, int pageSize) {
		 	int recordCount = searchKeywordMapper.countByExample(record);
	        record.setCurrentPage((currentPage - 1) * pageSize);
	        record.setPageSize(pageSize);
	        List<SearchKeyword>  data = searchKeywordMapper.selectByExample(record);
	        PaginationVo<SearchKeyword>  result = new PaginationVo<SearchKeyword> (data, recordCount, pageSize, currentPage);
	        return result;
	}

	@Override
	public SearchKeyword selectByPrimaryKey(int searchId) {
		return searchKeywordMapper.selectByPrimaryKey(searchId);
	}

	@Override
	public List<SearchKeyword> selectByKeyword(String keyword) {
		return searchKeywordMapper.selectByKeyword(keyword);
	}

	@Override
	public void deleteByPrimaryKey(int searchId) {
		SearchKeyword searchKeyword = searchKeywordMapper.selectByPrimaryKey(searchId);
		if(searchKeyword!=null){
			try {
				FileUtil.deleteFile(searchKeyword.getResLogo());
				searchKeywordMapper.deleteByPrimaryKey(searchId);
				 List<SearchKeywordResList> resLists  =	searchKeywordResListMapper.selectBySearchId(searchId);
				 for(SearchKeywordResList res:resLists){
					 searchKeywordResListMapper.deleteByPrimaryKey(res.getId());
				 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@Override
	public void batchDelete(Integer[] ids) {
		for(Integer id:ids){
			deleteByPrimaryKey(id);
		}
		
	}
	@Override
	public int insert(SearchKeyword record) {
		 return searchKeywordMapper.insert(record);
	}

	@Override
	public int updateByPrimaryKey(SearchKeyword record) {
		 return searchKeywordMapper.updateByPrimaryKey(record);
	}

	@Override
	public PaginationVo<AppFile> getSelectAppFiles(
			Map<String, Object> map, int currentPage, int pageSize) {
		    map.put("currentIndex", (currentPage - 1) * pageSize);
	        map.put("pageSize", Integer.valueOf(pageSize));
	        System.out.println("index :"+(currentPage - 1) * pageSize);
	        Integer count = searchKeywordMapper.getSelectAppFilesCount(map);
	        List<AppFile> apks = searchKeywordMapper.getSelectAppFiles(map);
	        return new PaginationVo<AppFile> (apks, count, pageSize, currentPage);
	}

	

}
