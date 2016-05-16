package com.hykj.gamecenter.logic.entry;

import android.net.Uri;

import com.hykj.gamecenter.utilscs.LogUtils;

/**
 * bean
 * 用于Uri query to GroupInfo
 * 
*/
public class UriGroupInfo
{
    private int groupId; // 分组ID
    private int groupClass; // 分组类别
    private int groupType; // 分组类型
    private int orderType; // 排序类型
    private int orderNo; // 排序号（该值不一定连续）
    private String recommWord; // 推荐语，如：本专题共12款推荐、男人游戏，不玩后悔...
    private String groupName; // 分组名称（办公/社交...休闲/策略...网游/单机...专题...启动推荐/首页推荐...）
    private String groupDesc; // 分组描述，分类时代表推荐词，专题时代表专题说明
    private String groupPicUrl; // 分组图片URL
    private String startTime; // 开始时间
    private String endTime; // 结束时间

    private Uri mUri;

    public UriGroupInfo( Uri uri )
    {
	this.mUri = uri;
	//设置值
	setGroupInfo( );
    }

    public int getGroupId()
    {
	return groupId;
    }

    public void setGroupId( int groupId )
    {
	this.groupId = groupId;
    }

    public int getGroupClass()
    {
	return groupClass;
    }

    public void setGroupClass( int groupClass )
    {
	this.groupClass = groupClass;
    }

    public int getGroupType()
    {
	return groupType;
    }

    public void setGroupType( int groupType )
    {
	this.groupType = groupType;
    }

    public int getOrderType()
    {
	return orderType;
    }

    public void setOrderType( int orderType )
    {
	this.orderType = orderType;
    }

    public int getOrderNo()
    {
	return orderNo;
    }

    public void setOrderNo( int orderNo )
    {
	this.orderNo = orderNo;
    }

    public String getRecommWord()
    {
	return recommWord;
    }

    public void setRecommWord( String recommWord )
    {
	this.recommWord = recommWord;
    }

    public String getGroupName()
    {
	return groupName;
    }

    public void setGroupName( String groupName )
    {
	this.groupName = groupName;
    }

    public String getGroupDesc()
    {
	return groupDesc;
    }

    public void setGroupDesc( String groupDesc )
    {
	this.groupDesc = groupDesc;
    }

    public String getGroupPicUrl()
    {
	return groupPicUrl;
    }

    public void setGroupPicUrl( String groupPicUrl )
    {
	this.groupPicUrl = groupPicUrl;
    }

    public String getStartTime()
    {
	return startTime;
    }

    public void setStartTime( String startTime )
    {
	this.startTime = startTime;
    }

    public String getEndTime()
    {
	return endTime;
    }

    public void setEndTime( String endTime )
    {
	this.endTime = endTime;
    }

    private void setGroupInfo()
    {
	try
	{
	    this.setGroupId( Integer.parseInt( mUri.getQueryParameter( "groupId" ) ) );
	    this.setGroupClass( Integer.parseInt( mUri.getQueryParameter( "groupClass" ) ) );
	    this.setGroupName( mUri.getQueryParameter( "groupName" ) );
	    this.setGroupType( Integer.parseInt( mUri.getQueryParameter( "groupType" ) ) );
	    this.setOrderType( Integer.parseInt( mUri.getQueryParameter( "orderType" ) ) );
	    this.setOrderNo( Integer.parseInt( mUri.getQueryParameter( "orderNo" ) ) );
	    this.setRecommWord( mUri.getQueryParameter( "recommWord" ) );
	    this.setStartTime( mUri.getQueryParameter( "startTime" ) );
	    this.setEndTime( mUri.getQueryParameter( "endTime" ) );
	    this.setGroupClass( Integer.parseInt( mUri.getQueryParameter( "groupClass" ) ) );
	    this.setGroupDesc( mUri.getQueryParameter( "groupDesc" ) );
	    this.setGroupId( Integer.parseInt( mUri.getQueryParameter( "groupId" ) ) );
	}
	catch ( NumberFormatException e )
	{
	    LogUtils.e( "setGroupInfo,error:" + e.getMessage( ) );
	    return;
	}
	catch ( Exception e )
	{
	    LogUtils.e( "setGroupInfo,error:" + e.getMessage( ) );
	    return;
	}
    }
}
