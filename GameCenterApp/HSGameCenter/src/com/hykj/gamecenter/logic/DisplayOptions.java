package com.hykj.gamecenter.logic;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.hykj.gamecenter.R;

public class DisplayOptions
{

    public static DisplayImageOptions optionsIcon = new DisplayImageOptions.Builder( ).showImageOnLoading( R.drawable.app_icon_default ).showImageForEmptyUri( R.drawable.app_icon_default )
	    .showImageOnFail( R.drawable.app_icon_default ).cacheInMemory( true ).cacheOnDisc( true ).bitmapConfig( Bitmap.Config.RGB_565 ).build( );

    public static DisplayImageOptions optionsSnapshot = new DisplayImageOptions.Builder( ).showImageOnLoading( R.drawable.homepage_default_big_image )
	    .showImageForEmptyUri( R.drawable.homepage_default_big_image ).showImageOnFail( R.drawable.homepage_default_big_image ).cacheInMemory( true ).cacheOnDisc( true )
	    .bitmapConfig( Bitmap.Config.RGB_565 ).build( );

    public static DisplayImageOptions optionsTopic = new DisplayImageOptions.Builder( ).showImageOnLoading( R.drawable.app_icon_default )
	    .showImageForEmptyUri( R.drawable.app_icon_default ).showImageOnFail( R.drawable.app_icon_default ).cacheInMemory( true ).cacheOnDisc( true )
	    .bitmapConfig( Bitmap.Config.RGB_565 ).build( );

    public static DisplayImageOptions optionsHomepage = new DisplayImageOptions.Builder( ).showImageOnLoading( R.drawable.app_icon_default )
	    .showImageForEmptyUri( R.drawable.app_icon_default ).showImageOnFail( R.drawable.app_icon_default ).cacheInMemory( true ).cacheOnDisc( true )
	    .bitmapConfig( Bitmap.Config.RGB_565 ).build( );

    public static DisplayImageOptions optionBigMapHomepage = new DisplayImageOptions.Builder( )/*.showImageOnLoading( R.drawable.homepage_default_big_image )
	    .showImageForEmptyUri( R.drawable.homepage_default_big_image ).showImageOnFail( R.drawable.homepage_default_big_image )*/.cacheInMemory( true ).cacheOnDisc( true )
	    .bitmapConfig( Bitmap.Config.RGB_565 ).build( );

    public static DisplayImageOptions optionSubject = new DisplayImageOptions.Builder( ).cacheInMemory( true ).cacheOnDisc( true ).bitmapConfig( Bitmap.Config.RGB_565 ).build( );
    public static DisplayImageOptions optionsLoginIcon = new DisplayImageOptions.Builder( ).showImageOnLoading( R.drawable.img_person_logined ).showImageForEmptyUri( R.drawable.img_person_logined )
            .showImageOnFail( R.drawable.img_person_logined ).cacheInMemory( true ).cacheOnDisc( true ).bitmapConfig( Bitmap.Config.RGB_565 ).build( );
    public static DisplayImageOptions optionsNotLoginIcon = new DisplayImageOptions.Builder( ).showImageOnLoading( R.drawable.img_person_logined ).showImageForEmptyUri( R.drawable.img_person_logined )
            .showImageOnFail( R.drawable.img_person_logined ).cacheInMemory( true ).cacheOnDisc( true ).bitmapConfig( Bitmap.Config.RGB_565 ).build( );

    public static DisplayImageOptions optionsWifi = new DisplayImageOptions.Builder( ).cacheInMemory( true ).cacheOnDisc( true ).bitmapConfig( Bitmap.Config.RGB_565 ).build( );
}
