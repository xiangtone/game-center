
package com.mas.rave.main.vo;

import java.util.Date;

public class SearchKeyword extends PagingBean
{

    private int searchId;
    private int raveId;
    private String raveName;
    private String keyword;
    private int resId;
    private int flag;
    private int albumId;
    private int sort;
    private Date createTime;
    private Date updateTime;
    private String operator;
    private int searchNum;
    private Country country;
    private AppAlbum appAlbum;
    private String resName;
    //add by dingjie 2014/07/29 搜索关键字功能更新
    private String resLogo;
    private SearchKeywordIcon searchIcon;
    private Integer iconId;
    private String iconUrl;
    
    public int getSearchId()
    {
        return searchId;
    }

    public void setSearchId(int searchId)
    {
        this.searchId = searchId;
    }

    public int getRaveId()
    {
        return raveId;
    }

    public void setRaveId(int raveId)
    {
        this.raveId = raveId;
    }

    public String getRaveName()
    {
        return raveName;
    }

    public void setRaveName(String raveName)
    {
        this.raveName = raveName;
    }

    public String getKeyword()
    {
        return keyword;
    }

    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
    }

    public int getResId()
    {
        return resId;
    }

    public void setResId(int resId)
    {
        this.resId = resId;
    }

    public int getFlag()
    {
        return flag;
    }

    public void setFlag(int flag)
    {
        this.flag = flag;
    }

    public int getAlbumId()
    {
        return albumId;
    }

    public void setAlbumId(int albumId)
    {
        this.albumId = albumId;
    }

    public int getSort()
    {
        return sort;
    }

    public void setSort(int sort)
    {
        this.sort = sort;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getOperator()
    {
        return operator;
    }

    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    public int getSearchNum()
    {
        return searchNum;
    }

    public void setSearchNum(int searchNum)
    {
        this.searchNum = searchNum;
    }

    public Country getCountry()
    {
        return country;
    }

    public void setCountry(Country country)
    {
        if(country != null)
        {
            setRaveId(country.getId());
            setRaveName(country.getName());
        }
        this.country = country;
    }

    public AppAlbum getAppAlbum()
    {
        return appAlbum;
    }

    public void setAppAlbum(AppAlbum appAlbum)
    {
        if(appAlbum != null)
            setAlbumId(appAlbum.getId());
        this.appAlbum = appAlbum;
    }

    public String getResName()
    {
        return resName;
    }

    public void setResName(String resName)
    {
        this.resName = resName;
    }

	public String getResLogo() {
		return resLogo;
	}

	public void setResLogo(String resLogo) {
		this.resLogo = resLogo;
	}

	public Integer getIconId() {
		return iconId;
	}

	public void setIconId(Integer iconId) {
		this.iconId = iconId;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public SearchKeywordIcon getSearchIcon() {
		return searchIcon;
	}

	public void setSearchIcon(SearchKeywordIcon searchIcon) {
		if(searchIcon!=null){
			this.setIconId(searchIcon.getId());
		}
		this.searchIcon = searchIcon;
	}

}
