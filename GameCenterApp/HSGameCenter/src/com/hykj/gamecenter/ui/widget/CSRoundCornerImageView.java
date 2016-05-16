package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.hykj.gamecenter.utilscs.BitmapUtils;

public class CSRoundCornerImageView extends ImageView
{

    private int mWidth;
    private int mHeight;
    private Context mContext;

    private final Paint paint = new Paint( );

    public CSRoundCornerImageView( Context context )
    {
	super( context );
	mContext = context;
    }

    public CSRoundCornerImageView( Context context , AttributeSet attrs )
    {
	super( context , attrs );
	mContext = context;
    }

    public CSRoundCornerImageView( Context context , AttributeSet attrs , int defStyle )
    {
	super( context , attrs , defStyle );
	mContext = context;
    }

    //    @Override
    //    public void setImageBitmap( Bitmap bm )
    //    {
    //
    //	//	if( getWidth( ) != 0 )
    //	//	{
    //	//	    mWidth = getWidth( );
    //	//	    mHeight = getHeight( );
    //	//	}
    //	mWidth = mHeight = mContext.getResources( ).getDimensionPixelSize( R.dimen.icon_size );
    //
    //	//	Bitmap backgroudBitmap = BitmapUtils.createColorBitmap( mWidth - 4 , mHeight - 4 , 0xcc000000 );
    //	//	Bitmap paddingBitmap = BitmapUtils.createColorBitmap( mWidth , mHeight , 0xccffffff );
    //	//	bm = BitmapUtils.big( bm , mWidth - 4 , mHeight - 4 );
    //	//	bm = BitmapUtils.combineBitmap( backgroudBitmap , bm );
    //	//	bm = BitmapUtils.getRoundedBitmap( mContext , bm );
    //	//
    //	//	bm = BitmapUtils.combineBitmap( paddingBitmap , bm );
    //	//	bm = BitmapUtils.getRoundedBitmap( mContext , bm );
    //
    //	Bitmap backgroudBitmap = BitmapUtils.createColorBitmap( mWidth - 4 , mHeight - 4 , 0xcc000000 );
    //	bm = Bitmap.createBitmap( bm , 3 , 3 , bm.getWidth( ) - 6 , bm.getHeight( ) - 6 );
    //	bm = BitmapUtils.big( bm , mWidth , mHeight );
    //	bm = BitmapUtils.combineBitmap( backgroudBitmap , bm );
    //	bm = BitmapUtils.getRoundedBitmap( mContext , bm );
    //
    //	super.setImageBitmap( bm );
    //    }

    @Override
    protected void onDraw( Canvas canvas )
    {
	int width;
	int height;
	width = getWidth( );
	height = getHeight( );
	Drawable drawable = getDrawable( );

	if( null != drawable && width > 0 && height > 0 && ( drawable instanceof BitmapDrawable ) )
	{
	    Bitmap bitmap = ( (BitmapDrawable)drawable ).getBitmap( );
	    bitmap = BitmapUtils.big( bitmap , width , height );
	    bitmap = BitmapUtils.getRoundedBitmap( mContext , BitmapUtils.bounaryBitmapAplah( bitmap ) ? BitmapUtils.getBitmap( bitmap , BitmapUtils.averageRGB( bitmap ) ) : bitmap , 6 );
	    final Rect rect = new Rect( 0 , 0 , bitmap.getWidth( ) , bitmap.getHeight( ) );
	    paint.reset( );
	    canvas.drawBitmap( bitmap , rect , rect , paint );
	}
	else
	{
	    super.onDraw( canvas );
	}
    }

}
