package com.hykj.gamecenter.utilscs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class BitmapUtils
{
    public static float ROUND_R = 14f;
    public final static int FLATFOLDERICON = 1;
    public final static int FLATICONVIEW = 2;

    //    public static float mDpi = 160;

    //    public static float getDensityDpi( Activity activity )
    //    {
    //	WindowManager windowManager = activity.getWindowManager( );
    //	DisplayMetrics displayMetrics = new DisplayMetrics( );
    //	windowManager.getDefaultDisplay( ).getMetrics( displayMetrics );
    //	return displayMetrics.densityDpi;
    //    }

    public static float getDensityDpi( Context context )
    {
	DisplayMetrics metrics = context.getResources( ).getDisplayMetrics( );
	float densityDpi = metrics.densityDpi;
	return densityDpi;
    }

    //    public static void setDpi( float dpi )
    //    {
    //	mDpi = dpi;
    //    }

    public static Bitmap drawable2bitmap( Drawable dw )
    {

	Bitmap bg = Bitmap.createBitmap( dw.getIntrinsicWidth( ) , dw.getIntrinsicHeight( ) , Config.ARGB_8888 );

	Canvas canvas = new Canvas( bg );

	dw.setBounds( 0 , 0 , dw.getIntrinsicWidth( ) , dw.getIntrinsicHeight( ) );
	dw.draw( canvas );

	canvas.setBitmap( null );

	return bg;
    }

    public static Bitmap reduceBitmapAlpha( Bitmap bm )
    {

	int [] arr = new int [bm.getWidth( ) * bm.getHeight( )];

	bm.getPixels( arr , 0 , bm.getWidth( ) , 0 , 0 , bm.getWidth( ) , bm.getHeight( ) );

	for( int i = 0 ; i < bm.getWidth( ) * bm.getHeight( ) ; i++ )
	{
	    if( arr[i] != 0 )
	    {
		arr[i] &= 0x00FFFFFF;
		arr[i] |= 0x20000000;
	    }
	}

	return Bitmap.createBitmap( arr , bm.getWidth( ) , bm.getHeight( ) , Config.ARGB_8888 );
    }

    public static Bitmap getRoundedBitmap( Context context , Bitmap bm )
    {

	Bitmap bg = Bitmap.createBitmap( bm.getWidth( ) , bm.getHeight( ) , Config.ARGB_8888 );

	Canvas canvas = new Canvas( bg );
	Paint paint = new Paint( );
	//	Log.i( "test" , "+++++++++++,Height" + bg.getHeight( ) + ",Width" + bg.getWidth( ) );
	Rect rect = new Rect( 0 , 0 , bg.getWidth( ) , bg.getHeight( ) );
	RectF rectF = new RectF( rect );
	float roundR = 12 * ( getDensityDpi( context ) / 160 );
	paint.setAntiAlias( true );

	canvas.drawRoundRect( rectF , roundR , roundR , paint );

	paint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.SRC_IN ) );

	canvas.drawBitmap( bm , rect , rect , paint );

	canvas.setBitmap( null );

//	if (null != bm && !bm.isRecycled()) {
//		bm.recycle();
//		bm = null;
//	}

	return bg;
    }

    //需传参圆角半径 roundRadius
    public static Bitmap getRoundedBitmap( Context context , Bitmap bm , int roundRadius )
    {

	Bitmap bg = Bitmap.createBitmap( bm.getWidth( ) , bm.getHeight( ) , Config.ARGB_8888 );

	Canvas canvas = new Canvas( bg );
	Paint paint = new Paint( );
	//	Log.i( "test" , "+++++++++++,Height" + bg.getHeight( ) + ",Width" + bg.getWidth( ) );
	Rect rect = new Rect( 0 , 0 , bg.getWidth( ) , bg.getHeight( ) );
	RectF rectF = new RectF( rect );
	float roundR = roundRadius * ( getDensityDpi( context ) / 160 );
	paint.setAntiAlias( true );

	canvas.drawRoundRect( rectF , roundR , roundR , paint );

	paint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.SRC_IN ) );

	canvas.drawBitmap( bm , rect , rect , paint );

	canvas.setBitmap( null );

	return bg;
    }

    public static Bitmap getCircleBitmap( Bitmap bitmap )
    {
//	int x = bitmap.getWidth( );
	int x = Math.min(bitmap.getWidth(), bitmap.getHeight());
	Bitmap output = Bitmap.createBitmap( x , x , Config.ARGB_8888 );
	Canvas canvas = new Canvas( output );

	final Paint paint = new Paint( );
	// 根据原来图片大小画一个矩形
	final Rect rect = new Rect( 0 , 0 , bitmap.getWidth( ) , bitmap.getHeight( ) );
	paint.setAntiAlias( true );
	// 画出一个圆
	canvas.drawCircle( x / 2 , x / 2 , x / 2 , paint );
	// 取两层绘制交集,显示上层
	paint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.SRC_IN ) );
	// 将图片画上去
	canvas.drawBitmap( bitmap , rect , rect , paint );
	canvas.setBitmap( null );
	// 返回Bitmap对象
	return output;
    }

    public static Bitmap getRoundedBitmap( Bitmap bm , Context mContext , int round )
    {

	Bitmap bg = Bitmap.createBitmap( bm.getWidth( ) , bm.getHeight( ) , Config.ARGB_8888 );
	Canvas canvas = new Canvas( bg );
	Paint paint = new Paint( );
	Rect rect = new Rect( 0 , 0 , bg.getWidth( ) , bg.getHeight( ) );
	RectF rectF = new RectF( rect );
	paint.setAntiAlias( true );

	canvas.drawRoundRect( rectF , round , round , paint );

	paint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.SRC_IN ) );

	canvas.drawBitmap( bm , rect , rect , paint );

	canvas.setBitmap( null );

	return bg;
    }

    public static Bitmap getShadowBitmap1( Bitmap bm , Drawable shadow )
    {

	final int SHADOW_LEN = 5;

	Bitmap bg = Bitmap.createBitmap( bm.getWidth( ) + SHADOW_LEN , bm.getHeight( ) + SHADOW_LEN , Config.ARGB_8888 );

	Canvas canvas = new Canvas( bg );
	Paint paint = new Paint( );

	shadow.setBounds( 0 , 0 , bg.getWidth( ) , bg.getHeight( ) );
	shadow.draw( canvas );

	canvas.drawBitmap( bm , ( bg.getWidth( ) - bm.getWidth( ) ) / 2.0f , ( bg.getHeight( ) - bm.getHeight( ) ) / 2.0f , paint );

	canvas.setBitmap( null );

	return bg;
    }

    public static Bitmap getShadowBitmap2( Bitmap bm )
    {

	BlurMaskFilter blurMaskFilter = new BlurMaskFilter( 10 , BlurMaskFilter.Blur.SOLID );

	Paint shadowPaint = new Paint( );
	shadowPaint.setMaskFilter( blurMaskFilter );

	int [] offsetXY = new int [2];
	Bitmap shadow = bm.extractAlpha( shadowPaint , offsetXY );
	shadow = shadow.copy( Config.ARGB_8888 , true );

	Bitmap bg = Bitmap.createBitmap( shadow.getWidth( ) , shadow.getHeight( ) , Config.ARGB_8888 );

	Canvas canvas = new Canvas( bg );
	Paint paint = new Paint( );

	canvas.drawBitmap( shadow , 0 , 0 , paint );

	canvas.drawBitmap( bm , ( shadow.getWidth( ) - bm.getWidth( ) ) / 2.0f , ( shadow.getHeight( ) - bm.getHeight( ) ) / 2.0f , paint );

	canvas.setBitmap( null );

	return bg;
    }

    public static Bitmap getShadowBitmap3( Bitmap bm )
    {

	Bitmap bg = Bitmap.createBitmap( bm.getWidth( ) + 5 , bm.getHeight( ) + 5 , Config.ARGB_8888 );

	Canvas canvas = new Canvas( bg );

	Paint sdPaint = new Paint( );
	sdPaint.setAntiAlias( true );
	sdPaint.setShadowLayer( 6.0f , 6.0f , 6.0f , Color.argb( 200 , 0 , 0 , 0 ) );

	Rect rect = new Rect( 5 , 80 , 0 , 0 );
	RectF rectF = new RectF( rect );
	canvas.drawRoundRect( rectF , 15f , 15f , sdPaint );

	Paint paint = new Paint( );
	paint.setAntiAlias( true );
	canvas.drawBitmap( bm , 5 , 5 , paint );

	canvas.setBitmap( null );

	return bg;
    }

    public static Bitmap getShadowBitmap4( Bitmap bm , Context mContext )
    {
	// add by chenming
	int SHADOW_LEN = 17;
	DisplayMetrics displayMetrics = new DisplayMetrics( );
	WindowManager windowManager = (WindowManager)mContext.getSystemService( Context.WINDOW_SERVICE );
	windowManager.getDefaultDisplay( ).getMetrics( displayMetrics );
	if( displayMetrics.widthPixels < 1025 )
	{
	    SHADOW_LEN = 20;
	}
	BlurMaskFilter blurMaskFilter = new BlurMaskFilter( 12 , BlurMaskFilter.Blur.NORMAL );

	Paint shadowPaint = new Paint( );
	shadowPaint.setMaskFilter( blurMaskFilter );

	int [] offsetXY = new int [2];
	Bitmap shadow = bm.extractAlpha( shadowPaint , offsetXY );
	shadow = shadow.copy( Config.ARGB_8888 , true );

	shadow = Bitmap.createScaledBitmap( shadow , shadow.getWidth( ) - SHADOW_LEN , shadow.getHeight( ) - SHADOW_LEN , true );
	Bitmap bg = Bitmap.createBitmap( shadow.getWidth( ) , shadow.getHeight( ) , Config.ARGB_8888 );

	Canvas canvas = new Canvas( bg );
	Paint paint = new Paint( );

	canvas.drawBitmap( shadow , 0 , 1 , paint );

	canvas.drawBitmap( bm , ( shadow.getWidth( ) - bm.getWidth( ) ) / 2.0f , 0 , null );

	canvas.setBitmap( null );

	return bg;
    }

    /**
     * 柔化效果(高斯模糊)
     * 
     * @param bmp
     * @return
     */
    public static Bitmap blurImageAmeliorate( Bitmap bmp )
    {
	long start = System.currentTimeMillis( );
	// 高斯矩阵
	int [] gauss = new int [] { 1 , 2 , 1 , 2 , 4 , 2 , 1 , 2 , 1 };

	int width = bmp.getWidth( );
	int height = bmp.getHeight( );
	Bitmap bitmap = Bitmap.createBitmap( width , height , Bitmap.Config.RGB_565 );

	int pixR = 0;
	int pixG = 0;
	int pixB = 0;

	int pixColor = 0;

	int newR = 0;
	int newG = 0;
	int newB = 0;

	int delta = 50; // 值越小图片会越亮，越大则越暗

	int idx = 0;
	int [] pixels = new int [width * height];
	bmp.getPixels( pixels , 0 , width , 0 , 0 , width , height );
	for( int i = 1 , length = height - 1 ; i < length ; i++ )
	{
	    for( int k = 1 , len = width - 1 ; k < len ; k++ )
	    {
		idx = 0;
		for( int m = -1 ; m <= 1 ; m++ )
		{
		    for( int n = -1 ; n <= 1 ; n++ )
		    {
			pixColor = pixels[ ( i + m ) * width + k + n];
			pixR = Color.red( pixColor );
			pixG = Color.green( pixColor );
			pixB = Color.blue( pixColor );

			newR = newR + pixR * gauss[idx];
			newG = newG + pixG * gauss[idx];
			newB = newB + pixB * gauss[idx];
			idx++;
		    }
		}

		newR /= delta;
		newG /= delta;
		newB /= delta;

		newR = Math.min( 255 , Math.max( 0 , newR ) );
		newG = Math.min( 255 , Math.max( 0 , newG ) );
		newB = Math.min( 255 , Math.max( 0 , newB ) );

		pixels[i * width + k] = Color.argb( 255 , newR , newG , newB );

		newR = 0;
		newG = 0;
		newB = 0;
	    }
	}

	bitmap.setPixels( pixels , 0 , width , 0 , 0 , width , height );
	long end = System.currentTimeMillis( );
	Log.d( "may" , "used time=" + ( end - start ) );
	return bitmap;
    }

    /** 水平方向模糊度 */
    private static float hRadius = 10;
    /** 竖直方向模糊度 */
    private static float vRadius = 10;
    /** 模糊迭代度 */
    private static int iterations = 10;

    /**
     * 高斯模糊
     */
    public static Drawable BoxBlurFilter( Bitmap bmp )
    {
	int width = bmp.getWidth( );
	int height = bmp.getHeight( );
	int [] inPixels = new int [width * height];
	int [] outPixels = new int [width * height];
	Bitmap bitmap = Bitmap.createBitmap( width , height , Bitmap.Config.ARGB_8888 );
	bmp.getPixels( inPixels , 0 , width , 0 , 0 , width , height );
	for( int i = 0 ; i < iterations ; i++ )
	{
	    blur( inPixels , outPixels , width , height , hRadius );
	    blur( outPixels , inPixels , height , width , vRadius );
	}
	blurFractional( inPixels , outPixels , width , height , hRadius );
	blurFractional( outPixels , inPixels , height , width , vRadius );
	bitmap.setPixels( inPixels , 0 , width , 0 , 0 , width , height );
	Drawable drawable = new BitmapDrawable( bitmap );
	return drawable;
    }

    public static void blur( int [] in , int [] out , int width , int height , float radius )
    {
	int widthMinus1 = width - 1;
	int r = (int)radius;
	int tableSize = 2 * r + 1;
	int divide[] = new int [256 * tableSize];

	for( int i = 0 ; i < 256 * tableSize ; i++ )
	    divide[i] = i / tableSize;

	int inIndex = 0;

	for( int y = 0 ; y < height ; y++ )
	{
	    int outIndex = y;
	    int ta = 0 , tr = 0 , tg = 0 , tb = 0;

	    for( int i = -r ; i <= r ; i++ )
	    {
		int rgb = in[inIndex + clamp( i , 0 , width - 1 )];
		ta += ( rgb >> 24 ) & 0xff;
		tr += ( rgb >> 16 ) & 0xff;
		tg += ( rgb >> 8 ) & 0xff;
		tb += rgb & 0xff;
	    }

	    for( int x = 0 ; x < width ; x++ )
	    {
		out[outIndex] = ( divide[ta] << 24 ) | ( divide[tr] << 16 ) | ( divide[tg] << 8 ) | divide[tb];

		int i1 = x + r + 1;
		if( i1 > widthMinus1 )
		    i1 = widthMinus1;
		int i2 = x - r;
		if( i2 < 0 )
		    i2 = 0;
		int rgb1 = in[inIndex + i1];
		int rgb2 = in[inIndex + i2];

		ta += ( ( rgb1 >> 24 ) & 0xff ) - ( ( rgb2 >> 24 ) & 0xff );
		tr += ( ( rgb1 & 0xff0000 ) - ( rgb2 & 0xff0000 ) ) >> 16;
		tg += ( ( rgb1 & 0xff00 ) - ( rgb2 & 0xff00 ) ) >> 8;
		tb += ( rgb1 & 0xff ) - ( rgb2 & 0xff );
		outIndex += height;
	    }
	    inIndex += width;
	}
    }

    public static void blurFractional( int [] in , int [] out , int width , int height , float radius )
    {
	radius -= (int)radius;
	float f = 1.0f / ( 1 + 2 * radius );
	int inIndex = 0;

	for( int y = 0 ; y < height ; y++ )
	{
	    int outIndex = y;

	    out[outIndex] = in[0];
	    outIndex += height;
	    for( int x = 1 ; x < width - 1 ; x++ )
	    {
		int i = inIndex + x;
		int rgb1 = in[i - 1];
		int rgb2 = in[i];
		int rgb3 = in[i + 1];

		int a1 = ( rgb1 >> 24 ) & 0xff;
		int r1 = ( rgb1 >> 16 ) & 0xff;
		int g1 = ( rgb1 >> 8 ) & 0xff;
		int b1 = rgb1 & 0xff;
		int a2 = ( rgb2 >> 24 ) & 0xff;
		int r2 = ( rgb2 >> 16 ) & 0xff;
		int g2 = ( rgb2 >> 8 ) & 0xff;
		int b2 = rgb2 & 0xff;
		int a3 = ( rgb3 >> 24 ) & 0xff;
		int r3 = ( rgb3 >> 16 ) & 0xff;
		int g3 = ( rgb3 >> 8 ) & 0xff;
		int b3 = rgb3 & 0xff;
		a1 = a2 + (int) ( ( a1 + a3 ) * radius );
		r1 = r2 + (int) ( ( r1 + r3 ) * radius );
		g1 = g2 + (int) ( ( g1 + g3 ) * radius );
		b1 = b2 + (int) ( ( b1 + b3 ) * radius );
		a1 *= f;
		r1 *= f;
		g1 *= f;
		b1 *= f;
		out[outIndex] = ( a1 << 24 ) | ( r1 << 16 ) | ( g1 << 8 ) | b1;
		outIndex += height;
	    }
	    out[outIndex] = in[width - 1];
	    inIndex += width;
	}
    }

    public static int clamp( int x , int a , int b )
    {
	return ( x < a ) ? a : ( x > b ) ? b : x;
    }

    public static Bitmap big( Bitmap b , float x , float y )
    {
	int w = b.getWidth( );
	int h = b.getHeight( );
	float sx = x / w;
	float sy = y / h;
	Matrix matrix = new Matrix( );
	matrix.postScale( sx , sy ); // 长和宽放大缩小的比例
	Bitmap resizeBmp = Bitmap.createBitmap( b , 0 , 0 , w , h , matrix , true );
//	if (null != b && b.isRecycled()) {
//		b.recycle();
//		b = null;
//	}
	return resizeBmp;
    }

    public static Bitmap createFitWidthBitmap( Bitmap b , float x )
    {
	int w = b.getWidth( );
	int h = b.getHeight( );
	float sx = x / w;
	Matrix matrix = new Matrix( );
	matrix.postScale( sx , sx ); // 长和宽放大缩小的比例
	Bitmap resizeBmp = Bitmap.createBitmap( b , 0 , 0 , w , h , matrix , true );
	return resizeBmp;
    }

    public static Bitmap createColorBitmap( int w , int h , int color )
    {
	int pixNumber;
	pixNumber = w * h;
	int [] pix = new int [pixNumber];

	for( int x = 0 ; x < pixNumber ; x++ )
	{
	    pix[x] = color;
	}
	Bitmap resizeBmp = Bitmap.createBitmap( pix , w , h , Config.ARGB_8888 );
	return resizeBmp;
    }

    /** 
     * 合并两张bitmap为一张 
     * @param background 
     * @param foreground 
     * @return Bitmap 
     */
    public static Bitmap combineBitmap( Bitmap background , Bitmap foreground )
    {
	if( background == null )
	{
	    return null;
	}
	int bgWidth = background.getWidth( );
	int bgHeight = background.getHeight( );
	int fgWidth = foreground.getWidth( );
	int fgHeight = foreground.getHeight( );
	Bitmap newmap = Bitmap.createBitmap( bgWidth , bgHeight , Config.ARGB_8888 );
	Canvas canvas = new Canvas( newmap );
	canvas.drawBitmap( background , 0 , 0 , null );
	canvas.drawBitmap( foreground , ( bgWidth - fgWidth ) / 2 , ( bgHeight - fgHeight ) / 2 , null );
	canvas.save( Canvas.ALL_SAVE_FLAG );
	canvas.restore( );
	return newmap;
    }

    public static void saveBitmap( Bitmap bitmap , String path ) throws IOException
    {
	File file = new File( path );
	FileOutputStream out;
	try
	{
	    out = new FileOutputStream( file );
	    if( bitmap.compress( Bitmap.CompressFormat.JPEG , 70 , out ) )
	    {
		out.flush( );
		out.close( );
	    }
	}
	catch ( FileNotFoundException e )
	{
	    e.printStackTrace( );
	}
	catch ( IOException e )
	{
	    e.printStackTrace( );
	}
    }

    /** 
     * @param  id :resource ID for use as a size in raw pixels
     * @return Drawable 
     */
    public static Drawable getRoundeDrawable( Context context , Drawable drawable , int id )
    {
	BitmapDrawable bd;
	Bitmap bm;
	int width;
	int height;

	width = height = context.getResources( ).getDimensionPixelSize( id );

	bd = (BitmapDrawable)drawable;
	bm = bd.getBitmap( );
	bm = BitmapUtils.big( bm , width - 4 , height - 4 );
	bm = BitmapUtils.getRoundedBitmap( context , bm );

	BitmapDrawable bd1 = new BitmapDrawable( bm );
	bd1.setBounds( 0 , 0 , bm.getWidth( ) , bm.getHeight( ) );
	return bd1;
    }

    public static Bitmap decodeResource( Resources res , int id , int reqWidth , int reqHeight )
    {
	// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小  
	final BitmapFactory.Options options = new BitmapFactory.Options( );
	options.inJustDecodeBounds = true;
	BitmapFactory.decodeResource( res , id , options );
	// 调用上面定义的方法计算inSampleSize值  
	options.inSampleSize = calculateInSampleSize( options , reqWidth , reqHeight );
	// 使用获取到的inSampleSize值再次解析图片  
	options.inJustDecodeBounds = false;
	return BitmapFactory.decodeResource( res , id , options );
    }

    public static Bitmap decodeFile( String pathName , int reqWidth , int reqHeight )
    {
	// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小  
	final BitmapFactory.Options options = new BitmapFactory.Options( );
	options.inJustDecodeBounds = true;
	BitmapFactory.decodeFile( pathName , options );
	// 调用上面定义的方法计算inSampleSize值  
	options.inSampleSize = calculateInSampleSize( options , reqWidth , reqHeight );
	// 使用获取到的inSampleSize值再次解析图片  
	options.inJustDecodeBounds = false;
	return BitmapFactory.decodeFile( pathName , options );
    }

    public static int calculateInSampleSize( BitmapFactory.Options options , int reqWidth , int reqHeight )
    {
	// 源图片的高度和宽度  
	final int height = options.outHeight;
	final int width = options.outWidth;
	int inSampleSize = 1;
	if( height > reqHeight || width > reqWidth )
	{
	    // 计算出实际宽高和目标宽高的比率  
	    final int heightRatio = Math.round( (float)height / (float)reqHeight );
	    final int widthRatio = Math.round( (float)width / (float)reqWidth );
	    // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高  
	    // 一定都会大于等于目标的宽和高。  
	    inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	}
	return inSampleSize;
    }

    /*判断第三方图标图标透明区域的大小，透明区域越大alpha越小*/
    public static boolean bounaryBitmapAplah( Bitmap bmpSource )
    {
	int w = bmpSource.getWidth( );
	int h = bmpSource.getHeight( );
	int a = 0;
	for( int k = 0 ; k < w ; k++ )
	{
	    for( int i = 0 ; i < h ; i++ )
	    {
		int color = bmpSource.getPixel( k , i );
		a = a + Color.alpha( color );
	    }
	}
	int alpha = a / ( w * h );
	return alpha < 240;
    }

    /*计算图片的主色调*/
    public static int averageRGB( Bitmap bmpSource )
    {
	int w = bmpSource.getWidth( );
	int h = bmpSource.getHeight( );
	int r = 0;
	int g = 0;
	int b = 0;
	for( int k = 0 ; k < w ; k++ )
	{
	    for( int i = 0 ; i < h ; i++ )
	    {
		int color = bmpSource.getPixel( k , i );
		r = r + Color.red( color );
		g = g + Color.green( color );
		b = b + Color.blue( color );
	    }
	}
	int green = g / ( w * h );
	/*对整个图标的绿色色值进行微调*/
	return Color.rgb( r / ( w * h ) , green < 70 ? green + 20 : green , b / ( w * h ) );
    }

    public static Bitmap getBitmap( Bitmap bm , int color )
    {
	// 创建新的位图
	Bitmap bg = Bitmap.createBitmap( bm.getWidth( ) , bm.getHeight( ) , Config.ARGB_8888 );
	// 创建位图画板
	Canvas canvas = new Canvas( bg );
	// 绘制图形
	Paint paint = new Paint( );
	paint.setColor( color );
	ColorMatrix cMatrix = new ColorMatrix( );
	// 设置饱和度    
	cMatrix.setSaturation( 1.0f );
	//	cMatrix.setRGB2YUV( );
	//设置图标亮度
	cMatrix.set( new float [] { 1 , 0 , 0 , 0 , 40 , 0 , 1 , 0 , 0 , 40 , 0 , 0 , 1 , 0 , 40 , 0 , 0 , 0 , 1 , 0 } );
	paint.setColorFilter( new ColorMatrixColorFilter( cMatrix ) );

	float [] hsv = new float [3];
	Color.RGBToHSV( Color.red( color ) , Color.green( color ) , Color.blue( color ) , hsv );
	hsv[0] = hsv[0] + 50;
	int color2 = Color.HSVToColor( hsv );

	LinearGradient linearGradient = new LinearGradient( 0 , 0 , 0 , bm.getHeight( ) , color2 , color , TileMode.CLAMP );
	paint.setShader( linearGradient );
	// 去除锯齿。
	paint.setAntiAlias( true );
	Rect rect = new Rect( 0 , 0 , bg.getWidth( ) , bg.getHeight( ) );
	RectF rectF = new RectF( rect );
	canvas.drawRect( rectF , paint );

	// 释放资源
	canvas.setBitmap( null );

	bg = combineBitmap( bg , big( bm , (float) ( bm.getWidth( ) * 0.9 ) , (float) ( bm.getHeight( ) * 0.9 ) ) );

	return bg;
    }

}
