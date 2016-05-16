package com.hykj.gamecenter.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.net.logic.ImageManager;
import com.hykj.gamecenter.utils.Logger;

import java.util.List;

public class ImageGalleryAdapter extends BaseAdapter
{

    private final Context mContext;

    private List< String > mImgPathList;

    private final ImageGalleryAdapterListener mListener;

    private final ImageManager mIconManager;

    public static final int PORTRAIT = 1;

    public static final int LANDSCAPE = 2;

    private static final String TAG = "ImageGalleryAdapter";

    private int orientation = PORTRAIT;


    public ImageGalleryAdapter( Context context , ImageGalleryAdapterListener listener)
    {

	mContext = context;
	mIconManager = ImageManager.getInstance( );

	mListener = listener;
    }

    public void setData( List< String > imgList )
    {
	mImgPathList = imgList;
    }

    public void setOrientation( int orientation )
    {
	this.orientation = orientation;
    }

    @Override
    public View getView( int position , View convertView , ViewGroup parent )
    {
	if( convertView == null )
	{
	    convertView = LayoutInflater.from( mContext ).inflate( R.layout.image_gallery_item , null );

	    ImageView imgView = (ImageView)convertView.findViewById( R.id.img_item );
	    imgView.setOnClickListener( new OnClickListener( )
	    {

		@Override
		public void onClick( View v )
		{
		    ( (Activity)mContext ).finish( );
		}
	    } );
	    //	    android.view.ViewGroup.LayoutParams ps = imgView.getLayoutParams( );

	    //	    if( orientation == PORTRAIT )
	    //	    {
	    //		ps.width = UITools.getWindowsHeightPixel( ) / 16 * 8;
	    //	    }
	    //	    else
	    //	    {
	    //		ps.width = UITools.getWindowsWidthPixel( ) / 5 * 3;
	    //	    }
	    //
	    //	    imgView.setLayoutParams( ps );

	}
	ImageView imgView = (ImageView)convertView.findViewById( R.id.img_item );
	ProgressBar progressBar = (ProgressBar)convertView.findViewById( R.id.loading_progress );
	if( mListener != null )
	{
	    imgView.setOnClickListener( mListener.getAdvancedViewClickListener( ) );
	}
	Bitmap bitmap = mIconManager.getImage( mImgPathList.get( position ) , null , true );
	Logger.i(TAG, "getView " + position + " bitmap == null " + (bitmap == null) + " url "+ mImgPathList.get(position), "oddshou");
	if( bitmap != null )
	{
	    imgView.setImageBitmap( bitmap );
	    imgView.setVisibility( View.VISIBLE );
	}
	return convertView;
    }
    
    @Override
    public int getCount()
    {
	if( mImgPathList != null )
	{
	    return mImgPathList.size( );
	}
	else
	{
	    return 0;
	}
    }

    @Override
    public Object getItem( int position )
    {
	if( position >= 0 && mImgPathList != null && position < mImgPathList.size( ) )
	{
	    return mImgPathList.get( position );
	}
	else
	{
	    return null;
	}
    }

    @Override
    public long getItemId( int position )
    {
	return position;
    }

    public interface ImageGalleryAdapterListener
    {

	/**
	 * 通知加载图片开始
	 * 
	 * @param position
	 */
	void notifyLoadImgStart(int position);

	/**
	 * 通知加载图片结束
	 * 
	 * @param position
	 * @param state
	 */
	void notifyLoadFinished(int position, int state);

	/**
	 * 获取当前选中的View
	 * 
	 * @return
	 */
	ImageView getCurSelAdvancedView();

	/**
	 * 获取AdvanceView的View.OnClickListener
	 * 
	 * @return
	 */
	View.OnClickListener getAdvancedViewClickListener();
    }
}
