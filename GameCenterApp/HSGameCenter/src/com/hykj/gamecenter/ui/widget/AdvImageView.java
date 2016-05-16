package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AdvImageView extends ImageView
{
    private static final String TAG = "SubjectImageView";

    public AdvImageView( Context context )
    {
	super( context );
    }

    public AdvImageView( Context context , AttributeSet attrs )
    {
	super( context , attrs );
    }

    public AdvImageView( Context context , AttributeSet attrs , int defStyle )
    {
	super( context , attrs , defStyle );
    }

    @Override
    protected void onLayout( boolean changed , int left , int top , int right , int bottom )
    {
	super.onLayout( changed , left , top , right , bottom );
    }

    @Override
    public void invalidate()
    {
	super.invalidate( );
    }

    @Override
    protected void onMeasure( int widthMeasureSpec , int heightMeasureSpec )
    {
	int width = MeasureSpec.getSize( widthMeasureSpec );
	int height = ( width * 10 ) / 21;
	setMeasuredDimension( width , height );
    }

    @Override
    protected void onDraw( Canvas canvas )
    {

	super.onDraw( canvas );
    }
}
