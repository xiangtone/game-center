package com.x.publics.http.model;

import java.util.List;

import com.x.publics.model.KeywordBean;

/**
 * 
 * @ClassName: KeywordsResponse
 * @Description: 搜索关键字，数据响应
 
 * @date 2014-7-7 下午2:53:41
 * 
 */
public class KeywordsResponse extends CommonResponse {

	public int keywordNum;
	public boolean isLast = false;
	public List<KeywordBean> keywordlist;

}
