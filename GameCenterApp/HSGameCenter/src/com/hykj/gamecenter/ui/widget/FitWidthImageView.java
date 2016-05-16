package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * author owenli
 * */

public class FitWidthImageView extends ImageView
{
    private final static String TAG = "FitWidthImageView";
    private int mDrawableWidth = 0;
    private int mLayoutWidth = 0;
    private Matrix mMatrix;

    public FitWidthImageView( Context context )
    {
	super( context );
	setScaleType( ScaleType.MATRIX );
    }

    public FitWidthImageView( Context context , AttributeSet attrs )
    {
	super( context , attrs );
	setScaleType( ScaleType.MATRIX );
    }

    public FitWidthImageView( Context context , AttributeSet attrs , int defStyle )
    {
	super( context , attrs , defStyle );
	setScaleType( ScaleType.MATRIX );
    }

    @Override
    public void setImageDrawable( Drawable drawable )
    {
	if( drawable != null )
	{
	    float scale = 0;
	    mDrawableWidth = drawable.getIntrinsicWidth( );
	    if( mMatrix == null )
		mMatrix = new Matrix( );
	    scale = (float) ( mLayoutWidth * 1.0 / mDrawableWidth );
	    mMatrix.setScale( scale , scale );
	    setImageMatrix( mMatrix );
	}

	super.setImageDrawable( drawable );
    }

    @Override
    protected void onMeasure( int widthMeasureSpec , int heightMeasureSpec )
    {
	mLayoutWidth = MeasureSpec.getSize( widthMeasureSpec );

	if( mMatrix != null && mDrawableWidth != 0 && mLayoutWidth != 0 )
	{
	    float scale = 0;
	    scale = (float) ( mLayoutWidth * 1.0 / mDrawableWidth );
	    mMatrix.setScale( scale , scale );
	    setImageMatrix( mMatrix );
	}

	super.onMeasure( widthMeasureSpec , heightMeasureSpec );
    }

    @Override
    protected void onDraw( Canvas canvas )
    {
	//	int width = 0;
	//	int height = 0;
	//
	//	width = getWidth( );
	//	height = getHeight( );
	//
	//	if( width != 0 && height != 0 && mDrawableWidth != 0 )
	//	{
	//	    float toScale = (float) ( width * 1.0 / mDrawableWidth );
	//	    canvas.scale( toScale , toScale );
	//	    canvas.clipRect( 0 , 0 , width , height );
	//	}

	super.onDraw( canvas );
    }
}
