package com.mas.rave.dao;

import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.SearchKeyword;

import java.util.List;
import java.util.Map;

public interface SearchKeywordMapper
{

    public abstract List<SearchKeyword> selectByExample(SearchKeyword searchkeyword);

    public abstract SearchKeyword selectByPrimaryKey(int i);

    public abstract List<SearchKeyword> selectByKeyword(String s);

    public abstract int deleteByPrimaryKey(int i);
    public abstract int deleteByResId(int resId);
    public abstract int insert(SearchKeyword searchkeyword);

    public abstract int countByExample(SearchKeyword searchkeyword);

    public abstract int updateByPrimaryKey(SearchKeyword searchkeyword);
   
    public abstract List<SearchKeyword> selectByIconId(int IconId);

    public abstract List<AppFile> getSelectAppFiles(Map<String,Object> map);

    public abstract Integer getSelectAppFilesCount(Map<String,Object> map);
}
