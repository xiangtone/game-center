
package com.mas.rave.service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.SearchKeywordResList;
import java.util.List;

public interface SearchKeywordResListService{

    public PaginationVo<SearchKeywordResList> selectByExample(SearchKeywordResList searchkeywordreslist,int currentPage, int pageSize);

    public SearchKeywordResList selectByPrimaryKey(int id);

    public List<SearchKeywordResList> selectBySearchId(int searchId);

    public int deleteByPrimaryKey(int id);

    public int deleteBySearchId(int searchId);

    public int insert(SearchKeywordResList searchkeywordreslist);

	public void updateByPrimaryKey(SearchKeywordResList searchKeywordResList);
}
