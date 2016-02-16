
package com.mas.rave.service;

import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.SearchKeyword;

public interface SearchKeywordService{

    public PaginationVo<SearchKeyword> selectByExample(SearchKeyword searchkeyword, int currentPage, int pageSize);

    public SearchKeyword selectByPrimaryKey(int searchId);

    public List<SearchKeyword> selectByKeyword(String keyword);

    public void deleteByPrimaryKey(int searchId);
    
    public void batchDelete(Integer[] ids);

    public int insert(SearchKeyword searchkeyword);

    public int updateByPrimaryKey(SearchKeyword searchkeyword);

    public PaginationVo<AppFile> getSelectAppFiles(Map<String, Object> map, int currentPage, int pageSize);
}
