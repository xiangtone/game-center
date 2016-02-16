

package com.mas.rave.dao;

import com.mas.rave.main.vo.SearchKeywordResList;
import java.util.List;

public interface SearchKeywordResListMapper
{

    public abstract List<SearchKeywordResList> selectByExample(SearchKeywordResList searchkeywordreslist);

    public abstract SearchKeywordResList selectByPrimaryKey(int id);

    public abstract List<SearchKeywordResList> selectBySearchId(int searchId);

    public abstract int deleteByPrimaryKey(int id);
    public abstract int deleteByResId(int resId);
    public abstract int deleteBySearchId(int searchId);

    public abstract int insert(SearchKeywordResList searchkeywordreslist);

    public abstract int countByExample(SearchKeywordResList searchkeywordreslist);

	public abstract int updateByPrimaryKey(SearchKeywordResList record);
}
