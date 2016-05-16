package com.hykj.gamecenter.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/**
 * @author: greatzhang 由一块块图片拼成的进度条，根据总的块数和当前突出的块来标识进度
 */
public class DotProgressBar extends View {

    private int mTotalNum = 4;
    private int mCurNum = 0;
    private boolean isCountFrom0 = false;

    /**
     * 后期完善可设置颜色的Drawabe
     */
    private BitmapDrawable mNormalDrawable = null;
    private BitmapDrawable mLightDrawable = null;

    //	private Bitmap mNormalBitmap = null;
    //	private Bitmap mLightBitmap = null;

    private int mImageWidth = 0;
    private int mImageHeight = 0;
    private int mDotSpace = 5;

    private Paint mPaint = null;

    /**
     * 进度改变的侦听者
     * 
     * @author albertzhong
     */
    public interface DotProgressChangeListener {
	/**
	 * 进度改变回调,
	 * 
	 * @param cur
	 *            当前的进度
	 * @param max
	 *            总进度
	 */
	void onProgressChange(int cur, int max);
    }

    /**
     * 构造方法
     * 
     * @param context
     *            程序上下文
     * @param normalImage
     *            标识个数的图片
     * @param lightImage
     *            标识当前进度的图片
     * @param totalNum
     *            总的标识进度的图片个数(个数需要大于零)
     */
    public DotProgressBar( Context context , Drawable normalImage , Drawable lightImage , int totalNum ) {
	super( context );
	if( normalImage instanceof BitmapDrawable ) {
	    mNormalDrawable = (BitmapDrawable)normalImage;
	}
	if( lightImage instanceof BitmapDrawable ) {
	    mLightDrawable = (BitmapDrawable)lightImage;
	}
	mTotalNum = totalNum;
    }

    public DotProgressBar( Context context , AttributeSet attrs ) {
	super( context , attrs );
	
	DisplayMetrics dm = getResources( ).getDisplayMetrics( );
	mDotSpace = (int)TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP , mDotSpace , dm );
    }

    /**
     * 设置当前进度
     * 
     * @param curNum
     *            当前显示的是总个数中的第几个
     */
    public void setCurProgress( int curNum ) {
	mCurNum = curNum;
	postInvalidate( );
    }

    /**
     * 改变总的个数
     * 
     * @param totalNum
     *            比啊是进度的图片的个数（大于零）
     */
    public void setTotalNum( int totalNum ) {
	mTotalNum = totalNum;
	invalidate( );
    }

    public void setCountFrom0( boolean isCountFrom0 ) {
	this.isCountFrom0 = isCountFrom0;
    }

    /**
     * 获取进度总数
     * 
     * @return 返回进度总数
     */
    public int getTotalNum() {
	return mTotalNum;
    }

    /**
     * 获取进度条需要的宽度
     * 
     * @return 进度条显示需要的宽度
     */
    public int getNeedW() {
	int w = 23 * mTotalNum;
	if( null != mLightDrawable ) {
	    w = mLightDrawable.getIntrinsicWidth( ) * mTotalNum;
	}
	return w;
    }

    @Override
    protected void onMeasure( int widthMeasureSpec , int heightMeasureSpec ) {
	super.onMeasure( widthMeasureSpec , heightMeasureSpec );

	final int widthMode = MeasureSpec.getMode( widthMeasureSpec );
	final int widthSize = MeasureSpec.getSize( widthMeasureSpec );

	final int heightMode = MeasureSpec.getMode( heightMeasureSpec );
	final int heightSize = MeasureSpec.getSize( heightMeasureSpec );

	/*
	 * if (widthMode != MeasureSpec.EXACTLY || heightMode !=
	 * MeasureSpec.EXACTLY)
	 * 
	 * { throw new
	 * IllegalStateException("ApplicationsStackLayout can only be used with "
	 * + "measure spec mode=EXACTLY"); }
	 */
	int h = heightSize;
	int w = widthSize;
	if( null != mLightDrawable ) {
	    h = mLightDrawable.getIntrinsicHeight( ) + 2 * mDotSpace;
	    //			if( mTotalNum > 0){
	    //				w = mTotalNum * mLightDrawable.getIntrinsicWidth() + mDotSpace * (mTotalNum + 1);
	    //			}
	}

	setMeasuredDimension( w , h );

	/*
	 * //根据个数设置大小，高度为图片的高度 int w = getNeedW(); int h = 23; if(null !=
	 * mLightImage) { h = mLightImage.getHeight(); } setMeasuredDimension(w,
	 * h);
	 */}

    @Override
    protected void onDraw( Canvas canvas ) {
	if( mTotalNum <= 0 ) {
	    return;
	}
	int width = getWidth( );
	int height = getHeight( );

	int startX = ( width - mTotalNum * mImageWidth - mDotSpace * ( mTotalNum - 1 ) ) / 2;
	int startY = ( height - mImageHeight ) - 5;
	// 每次都调用，是怕图片在外边也有用到，改变了锚点
	// setImageAnchor();
	int x = startX , y;

	if( isCountFrom0 ) {
	    for( int i = 1 ; i <= mTotalNum ; i++ ) {
		// 如果是第一个则不需要加上空隔
		if( i == 1 ) {
		    x = startX /*+ i * mImageWidth + mImageWidth / 2*/;
		} else {
		    x += mImageWidth + mDotSpace;
		    //				x = startX + i * mImageWidth + mImageWidth / 2 + i * mDotSpace;
		}

		if( i > mCurNum ) {
		    if( null != mNormalDrawable ) {
			canvas.drawBitmap( mNormalDrawable.getBitmap( ) , x , startY , getPaint( ) );
		    }
		} else {
		    if( null != mLightDrawable ) {
			canvas.drawBitmap( mLightDrawable.getBitmap( ) , x , startY , getPaint( ) );
		    }
		}
	    }
	} else {
	    for( int i = 0 ; i < mTotalNum ; i++ ) {
		// 如果是第一个则不需要加上空隔
		if( i == 0 ) {
		    x = startX /*+ i * mImageWidth + mImageWidth / 2*/;
		} else {
		    x += mImageWidth + mDotSpace;
		    //				x = startX + i * mImageWidth + mImageWidth / 2 + i * mDotSpace;
		}

		if( i != mCurNum ) {
		    if( null != mNormalDrawable ) {
			canvas.drawBitmap( mNormalDrawable.getBitmap( ) , x , startY , getPaint( ) );
		    }
		} else {
		    if( null != mLightDrawable ) {
			canvas.drawBitmap( mLightDrawable.getBitmap( ) , x , startY , getPaint( ) );
		    }
		}
	    }
	}
    }

    private Paint getPaint() {
	if( mPaint == null ) {
	    mPaint = new Paint( );
	    // mPaint.setAntiAlias(true);
	    // mPaint.setColor(0x4C000000);
	    // mPaint.setTextAlign(Align.CENTER);
	    // mPaint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
	}
	return mPaint;
    }

    public void setDotbarIconResource( int normalImage , int lightImage ) {
	Drawable drawable = getResources( ).getDrawable( normalImage );
	if( drawable instanceof BitmapDrawable ) {
	    mNormalDrawable = (BitmapDrawable)drawable;
	}
	drawable = getResources( ).getDrawable( lightImage );
	if( drawable instanceof BitmapDrawable ) {
	    mLightDrawable = (BitmapDrawable)drawable;
	    mImageWidth = mLightDrawable.getIntrinsicWidth( );
	    mImageHeight = mLightDrawable.getIntrinsicHeight( );
	}
    }

    public void setDotbarNum( int nTotalNum ) {
	mTotalNum = nTotalNum;
    }

    public void destroy() {
	mNormalDrawable = null;
	mLightDrawable = null;
	mPaint = null;
    }
}
