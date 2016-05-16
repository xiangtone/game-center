package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class AppListView extends ListView
{

    public AppListView( Context context )
    {
	super( context );
    }

    public AppListView( Context context , AttributeSet attrs )
    {
	super( context , attrs );
    }

    public AppListView( Context context , AttributeSet attrs , int defStyle )
    {
	super( context , attrs , defStyle );
    }

    @Override
    protected void onMeasure( int widthMeasureSpec , int heightMeasureSpec )
    {
	int expandSpec = MeasureSpec.makeMeasureSpec( Integer.MAX_VALUE >> 2 , MeasureSpec.AT_MOST );
	super.onMeasure( widthMeasureSpec , expandSpec );
    }

}
