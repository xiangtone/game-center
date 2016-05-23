package com.hykj.gamecenter.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hykj.gamecenter.statistic.ReportConstants;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.GroupAppListActivity;
import com.hykj.gamecenter.controller.ProtocolListener.ITEM_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.KEY;
import com.hykj.gamecenter.controller.ProtocolListener.MAIN_TYPE;
import com.hykj.gamecenter.data.GroupInfo;
import com.hykj.gamecenter.data.TopicInfo;
import com.hykj.gamecenter.logic.DisplayOptions;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.widget.SubjectImageView;
import com.hykj.gamecenter.utils.UITools;

public class SubjectListAdapter extends BaseAdapter
{

    private Context mContext = null;
    private int mColumnCount = 0;
    private LayoutInflater mInflater = null;
    private ArrayList< GroupInfo > infoList = new ArrayList< GroupInfo >( );

    public SubjectListAdapter( ArrayList< GroupInfo > infoList , Context context , int columnCount )
    {
	mContext = context;
	this.infoList = infoList;
	this.mColumnCount = columnCount;
	mInflater = (LayoutInflater)mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    }

    public void addData( ArrayList< GroupInfo > infoList )
    {
	this.infoList = infoList;
    }

    public void setColumnCount( int columnCount )
    {
	this.mColumnCount = columnCount;
    }

    @Override
    public int getCount()
    {
	int nCount = (int)Math.ceil( infoList.size( ) * 1.0 / mColumnCount );
	return nCount;
    }

    @Override
    public Object getItem( int position )
    {
	return infoList.get( position );
    }

    private class LineHolder
    {
	ItemHolder item1;
	ItemHolder item2;
	ItemHolder item3;
    }

    private class ItemHolder
    {
	View frame;
	SubjectImageView icon;

	public ItemHolder( View view )
	{
	    frame = view;
	    icon = (SubjectImageView)view.findViewById( R.id.topic_item );
	}

    }

    @Override
    public long getItemId( int position )
    {
	return position;
    }

    @Override
    public View getView( int position , View convertView , ViewGroup parent )
    {
	LineHolder holder;
	if( convertView == null )
	{
	    convertView = mInflater.inflate( R.layout.topic_content_line , null );

	    holder = new LineHolder( );
	    View line = convertView.findViewById( R.id.item1 );
	    holder.item1 = new ItemHolder( line );

	    line = convertView.findViewById( R.id.item2 );
	    holder.item2 = new ItemHolder( line );

	    line = convertView.findViewById( R.id.item3 );
	    holder.item3 = new ItemHolder( line );

	    convertView.setTag( holder );
	}
	else
	{
	    holder = (LineHolder)convertView.getTag( );
	}

	if( position == 0 )
	{
	    convertView.findViewById( R.id.fill_view ).setVisibility( View.VISIBLE );
	}
	else
	{
	    convertView.findViewById( R.id.fill_view ).setVisibility( View.GONE );
	}

	switch ( mColumnCount )
	{
	    case 2 :
		holder.item1.frame.setVisibility( View.INVISIBLE );
		holder.item2.frame.setVisibility( View.INVISIBLE );
		holder.item3.frame.setVisibility( View.GONE );
		break;
	    default :
		holder.item1.frame.setVisibility( View.INVISIBLE );
		holder.item2.frame.setVisibility( View.INVISIBLE );
		holder.item3.frame.setVisibility( View.INVISIBLE );
		break;
	}

	int index = position * mColumnCount;
	int end = index + mColumnCount;
	int did = 0;
	for( ; index < end && index < infoList.size( ) ; index++ , did++ )
	{
	    switch ( did )
	    {
		case 0 :
		    bindItemView( holder.item1 , index );
		    break;
		case 1 :
		    bindItemView( holder.item2 , index );
		    break;
		case 2 :
		    bindItemView( holder.item3 , index );
		    break;
		default :
		    break;
	    }

	}

	return convertView;
    }

    private void bindItemView( ItemHolder holder , int position )
    {
	final GroupInfo info = infoList.get( position );

	ImageLoader.getInstance( ).displayImage( info.groupPicUrl , holder.icon , DisplayOptions.optionSubject );

	holder.frame.setVisibility( View.VISIBLE );

	//TODO 进入游戏专题中的具体某一专题
	holder.frame.setOnClickListener( new View.OnClickListener( )
	{
	    @Override
	    public void onClick( View v )
	    {
		if( UITools.isFastDoubleClick( ) )

		    return;

		Intent intentAppList = new Intent( mContext , GroupAppListActivity.class );
		intentAppList.putExtra( KEY.ORDERBY , info.orderType );
		intentAppList.putExtra( KEY.GROUP_ID , info.groupId );
		intentAppList.putExtra( KEY.GROUP_CLASS , info.groupClass );
		intentAppList.putExtra( KEY.GROUP_TYPE , info.groupType );

		intentAppList.putExtra( KEY.CATEGORY_NAME , mContext.getString( R.string.topic_game_label ) );
		intentAppList.putExtra( KEY.MAIN_TYPE , MAIN_TYPE.TOPIC );
		intentAppList.putExtra( KEY.ITEM_TYPE , ITEM_TYPE.UNSHOW_SNAPSHOT );
		intentAppList.putExtra( StatisticManager.APP_POS_TYPE , ReportConstants.STATIS_TYPE.SUBJECT );

		TopicInfo topicInfo = new TopicInfo( );
		topicInfo.mAppCount = info.recommWrod;
		topicInfo.mTopic = info.groupName;
		topicInfo.mTip = info.groupDesc;
		topicInfo.mPicUrl = info.groupPicUrl;

		intentAppList.putExtra( KEY.TOPIC_INFO , topicInfo );

		intentAppList.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
		mContext.startActivity( intentAppList );
	    }
	} );
    }
}
