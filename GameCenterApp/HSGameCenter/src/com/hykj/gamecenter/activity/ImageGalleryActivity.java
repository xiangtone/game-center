package com.hykj.gamecenter.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.App;
import com.hykj.gamecenter.adapter.ImageGalleryAdapter;
import com.hykj.gamecenter.net.logic.ImageManager;
import com.hykj.gamecenter.statistic.MSG_CONSTANTS;
import com.hykj.gamecenter.ui.DotProgressBar;
import com.hykj.gamecenter.ui.ImageSlideGallery;
import com.hykj.gamecenter.ui.ImageSlideGallery.OnImageChangeListener;

public class ImageGalleryActivity extends Activity implements OnImageChangeListener
{

    private static final String TAG = "ImageGalleryActivity";

    private ImageManager mIconManager;
    private ImageView mImageView;
    private ImageSlideGallery mGallery;
    //        private OverScrollGallery mGallery;
    private DotProgressBar mDotProgressBar;
    private ImageGalleryAdapter mAdapter;
    private ArrayList< String > urls;
    private String url = "";
    private int urlPosition = 0;
    public static final String URL_DATA = "urls";
    public static final String ORIENTATION = "orientation";
    public static final String URL = "url";

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
	super.onCreate( savedInstanceState );

	if( App.getDevicesType( ) == App.PHONE )
	    setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
	
	setContentView( R.layout.activity_image_gallery );
	//这个页面不需要沉浸式 状态栏
//    SystemBarTintManager.useSystemBar( this , R.color.action_blue_color);
	mIconManager = ImageManager.getInstance( );
	urls = getIntent( ).getStringArrayListExtra( URL_DATA );
	url = getIntent( ).getStringExtra( URL );
	for( int i = 0 ; i < urls.size( ) ; i++ )
	{
	    if( urls.get( i ).equals( url ) )
	    {
		urlPosition = i;
		break;
	    }
	}
	initialViews( );
    }

    private void initialViews()
    {
      getImgs( );
	mAdapter = new ImageGalleryAdapter( this , null);
	mAdapter.setData( urls );
	mAdapter.setOrientation( getIntent( ).getIntExtra( ImageGalleryActivity.ORIENTATION , ImageGalleryAdapter.LANDSCAPE ) );
	mGallery = (ImageSlideGallery)findViewById( R.id.img_gallery );
	mGallery.setAdapter( mAdapter );
	mGallery.setSelection( urlPosition );
	//	mGallery.setUnselectedAlpha( 1f );
	mGallery.setOnImageChangeListener( this );

	mImageView = (ImageView)findViewById( R.id.close_image );
	mImageView.setOnClickListener( new OnClickListener( )
	{
	    @Override
	    public void onClick( View v )
	    {
		finish( );
	    }
	} );

	mDotProgressBar = (DotProgressBar)findViewById( R.id.dot );
	mDotProgressBar.setDotbarIconResource( R.drawable.home_scroll_ad_dot_white , R.drawable.home_scroll_ad_dot_black );
	mDotProgressBar.setVisibility( View.VISIBLE );
	mDotProgressBar.setCurProgress( urlPosition );
	mDotProgressBar.setTotalNum( urls.size( ) );
    }

    private void getImgs()
    {
	for( String url : urls )
	{
	    Bitmap bitmap = mIconManager.getImage( url , imageHandler , true );
	}
    }

    private final Handler imageHandler = new Handler( )
    {
	@Override
	public void handleMessage( Message msg )
	{
	    switch ( msg.what )
	    {
		case MSG_CONSTANTS.MSG_IMAGE_DECODE_FINISH :
		    Object [] obj = (Object [])msg.obj;
		    String url = (String)obj[0];
		    if( urls.contains( url ) )
		    {
		        //############ oddshou 以下方法并不能刷新。
//			mAdapter.notifyDataSetChanged( );
			mGallery.notifyData();
			
		    }
		    break;
		default :
		    break;
	    }
	}
    };

    @Override
    public void onConfigurationChanged( Configuration newConfig )
    {

	//	int position = mGallery.getSelectedItemPosition( );
	//	mAdapter.notifyDataSetChanged( );
	//	mGallery.setSelection( position );
	//	mDotProgressBar.setCurProgress( position );
	super.onConfigurationChanged( newConfig );
    }

    @Override
    public void onPageSelected( int position )
    {
	mDotProgressBar.setCurProgress( position );
    }

}
