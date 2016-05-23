package com.hykj.gamecenter.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.utils.Logger;

import java.util.ArrayList;

/**
 * 
 * @author froyohuang 所有一行多个item的adapter的基类
 */
public abstract class GridAdapter extends BaseAdapter
{

    private static String TAG = "GridAdapter";
    protected final Context mContext;

    private int mColumnCount = 3;
    private int mColumnWidth = 0;

    private static final LinearLayout.LayoutParams llp = new LayoutParams( LayoutParams.MATCH_PARENT , LayoutParams.MATCH_PARENT );

    /**
     * 空白视图，用于充填没有数据的地方
     */
    private final ArrayList< View > mBlankView;

    public GridAdapter( int columnCountr , Context context )
    {
	super( );
	mContext = context;
	mColumnCount = columnCountr;
	mBlankView = new ArrayList< View >( );

	llp.weight = 1;
    }

    public int getColumnCount()
    {
	return mColumnCount;
    }

    public void setColumnCount( int count )
    {
	mColumnCount = count;
    }

    @Override
    public int getCount()
    {
	int nCount = (int)Math.ceil( getAllCount( ) * 1.0 / mColumnCount );
	// Log.d("GridAdapter", "getCount =" + nCount);
	return nCount;
    }

    @Override
    public Object getItem( int position )
    {
	return null;
    }

    @Override
    public long getItemId( int position )
    {
	return 0;
    }

    @Override
    public View getView( int position , View convertView , ViewGroup parent )
    {
	LinearLayout lineContainer = (LinearLayout)convertView;
	LinearLayout lineContent = null;
	View topDivier = null;
	View bottomDivider = null;

	int nItemWidth = 0;

	// 首先创建带有上下分割线的的整行的Layout
	if( lineContainer == null )
	{

	    Logger.d( TAG , "lineContainer = null" + "|parent=" + parent + "|paddingleft=" + parent.getPaddingLeft( ) + "|paddingRight=" + parent.getPaddingRight( ) );
	    lineContainer = new LinearLayout( mContext );
	    lineContainer.setOrientation( LinearLayout.VERTICAL );
	    lineContainer.setGravity( Gravity.TOP );
	    lineContainer.setPadding( 0 , 0 , 0 , 0 );

	    lineContent = new LinearLayout( mContext );
	    lineContent.setOrientation( LinearLayout.HORIZONTAL );
	    lineContent.setGravity( Gravity.CENTER_VERTICAL );

	    lineContainer.addOnLayoutChangeListener( new OnLayoutChangeListener( )
	    {

		@Override
		public void onLayoutChange( View v , int left , int top , int right , int bottom , int oldLeft , int oldTop , int oldRight , int oldBottom )
		{
		    Logger.d( TAG , "onLayoutChange View =" + v.toString( ) );

		    int nFillItem = (Integer) v.getTag(R.id.tag_fillItems);
		    // int nWidth = (Integer) ((LinearLayout)
		    // v).getTag(R.id.tag_itemWidth);

		    Logger.d( TAG , "onLayoutChange getAllCount() =" + getAllCount( ) + "|nWidth=" + mColumnWidth );
		    LinearLayout.LayoutParams dividerllp = getHorizontalDiveiderLayoutParams( nFillItem * mColumnWidth );
		    if( getAllCount( ) < mColumnCount )
		    {
			( (LinearLayout)v ).getChildAt( 0 ).setLayoutParams( dividerllp );
		    }
		    ( (LinearLayout)v ).getChildAt( 2 ).setLayoutParams( dividerllp );
		}
	    } );

	    LinearLayout.LayoutParams linelp = new LayoutParams( android.view.ViewGroup.LayoutParams.MATCH_PARENT , android.view.ViewGroup.LayoutParams.MATCH_PARENT );
	    linelp.weight = 1;

	    topDivier = getHorizontalDivider( );
	    bottomDivider = getHorizontalDivider( );

	    lineContainer.addView( topDivier );
	    lineContainer.addView( lineContent , linelp );
	    lineContainer.addView( bottomDivider );

	    // Logger.d(TAG, "lineContainer == null | nItemWidth =" +
	    // nItemWidth);

	    lineContainer.setTag( R.id.tag_top_line , topDivier );
	    lineContainer.setTag( R.id.tag_bottom_line , bottomDivider );
	    lineContainer.setTag( R.id.tag_linecontent , lineContent );
	}
	else
	{

	    lineContent = (LinearLayout)lineContainer.getTag( R.id.tag_linecontent );
	    topDivier = (View)lineContainer.getTag( R.id.tag_top_line );
	    bottomDivider = (View)lineContainer.getTag( R.id.tag_bottom_line );
	    lineContent.removeAllViews( );
	}

	nItemWidth = ( parent.getWidth( ) - parent.getPaddingLeft( ) - parent.getPaddingRight( ) - lineContainer.getPaddingLeft( ) - lineContainer.getPaddingRight( ) ) / mColumnCount;

	if( position < getCount( ) - 1 )
	{
	    bottomDivider.setVisibility( View.GONE );
	}
	else
	{
	    bottomDivider.setVisibility( View.VISIBLE );
	}

	/*
	 * if(childPosition == 0){ topDivier.setVisibility(View.GONE); }else{
	 * topDivier.setVisibility(View.VISIBLE); }
	 */

	// 获取或者初始化重用的itemview和divider的view
	ArrayList< View > viewHolder = (ArrayList< View >)lineContent.getTag( R.id.tag_viewHolder );
	if( viewHolder == null )
	{
	    viewHolder = new ArrayList< View >( );
	    lineContent.setTag( R.id.tag_viewHolder , viewHolder );
	}

	ArrayList< View > dividerView = (ArrayList< View >)lineContent.getTag( R.id.tag_dividerHolder );
	if( dividerView == null )
	{
	    dividerView = new ArrayList< View >( 4 );
	    lineContent.setTag( R.id.tag_dividerHolder , dividerView );
	}

	// 对每一行，生成单独的实际itemview加入上面创建的layout中
	int index = position * mColumnCount;
	int end = index + mColumnCount;
	int did = 0;
	// int nBottomDividerWidth = 0;
	int nFillItem = 0;
	View verticalDivider;
	for( ; index < end && index < getAllCount( ) ; index++ , did++ )
	{
	    int posInLine = mColumnCount - ( end - index );
	    View itemView = null;
	    if( did >= dividerView.size( ) )
	    {
		View divider = getVerticalDivider( );
		dividerView.add( divider );
	    }
	    // lineContent.addView(dividerView.get(did));
	    verticalDivider = dividerView.get( did );
	    verticalDivider.setVisibility( View.VISIBLE );
	    lineContent.addView( verticalDivider );

	    if( viewHolder.size( ) > posInLine )
	    {
		itemView = viewHolder.get( posInLine );
		itemView = getPeaceView( index , itemView , lineContent );

		// Logger.d(TAG, "if 1  index =" + index + "|itemView =" +
		// itemView.toString());
		// Logger.d(TAG, "lineContent =" + lineContent.toString());
	    }
	    else
	    {
		itemView = getPeaceView( index , itemView , lineContent );
		viewHolder.add( itemView );

		// Logger.d(TAG, "*if 2  index =" + index + "|itemView =" +
		// itemView.toString());
		// Logger.d(TAG, "*lineContent =" + lineContent.toString());
	    }

	    if( itemView != null )
	    {
		itemView.setTag( R.id.tag_blankView , Boolean.FALSE );
		itemView.setTag( R.id.tag_positionView , Integer.valueOf( index ) );
		// itemView.setTag(R.id.tag_blankView,
		// Integer.valueOf(groupPosition));
		lineContent.addView( itemView , llp );
		// add column width
		// nBottomDividerWidth += itemView.getWidth();
		nFillItem++;
	    }
	}
	if( did >= dividerView.size( ) )
	{
	    View divider = getVerticalDivider( );
	    dividerView.add( divider );
	}
	verticalDivider = dividerView.get( did++ );
	verticalDivider.setVisibility( View.VISIBLE );
	lineContent.addView( verticalDivider );
	// nBottomDividerWidth += verticalDivider.getWidth();

	// reset divider
	LinearLayout.LayoutParams tmpLP = (LinearLayout.LayoutParams)topDivier.getLayoutParams( );
	tmpLP.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
	topDivier.setLayoutParams( tmpLP );

	tmpLP = (LinearLayout.LayoutParams)bottomDivider.getLayoutParams( );
	tmpLP.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
	bottomDivider.setLayoutParams( tmpLP );

	// set horizontal divider width
	if( nFillItem < mColumnCount )
	{
	    // Logger.d(TAG, "nFillItem =" + nFillItem + "|nItemWidth=" +
	    // nItemWidth);
	    LinearLayout.LayoutParams dividerllp = (LinearLayout.LayoutParams)bottomDivider.getLayoutParams( );
	    dividerllp.width = nFillItem * nItemWidth;
	    if( getAllCount( ) < mColumnCount )
	    {
		topDivier.setLayoutParams( dividerllp );
	    }
	    bottomDivider.setLayoutParams( dividerllp );

	    // Logger.d(TAG, "dividerllp =" + dividerllp.width);
	}

	lineContainer.setTag( R.id.tag_fillItems , nFillItem );
	mColumnWidth = nItemWidth; // lineContainer.setTag(R.id.tag_itemWidth,
				   // nItemWidth);

	// Logger.d(TAG, "lineContent =" + lineContent.toString() +
	// "| gettag_nItemWidth=" + lineContainer.getTag(R.id.tag_itemWidth));
	// Logger.d(TAG, "bottomDivider =" + bottomDivider.toString() +
	// "|topDivier=" + topDivier.toString());

	// 当一行未填满，填充空白view 

	if( index < end )
	{
	    for( int blankNum = end - index - 1 ; blankNum >= 0 ; blankNum-- )
	    {
		View bk = null;
		for( int j = mBlankView.size( ) - 1 ; j >= 0 ; j-- )
		{

		    if( mBlankView.get( j ).getParent( ) == null )
		    {
			bk = mBlankView.get( j );
			break;
		    }
		}
		if( bk == null )
		{
		    bk = genBlankView( );
		    mBlankView.add( bk );
		}
		lineContent.addView( bk , llp );
		if( dividerView.size( ) >= did )
		{
		    View divider = getVerticalDivider( );
		    dividerView.add( divider ); //
		}
		verticalDivider = dividerView.get( did++ );
		verticalDivider.setVisibility( View.INVISIBLE );
		lineContent.addView( verticalDivider );
	    }
	}

	return lineContainer;
    }

    /**
     * 获取列表数据项的总长度
     * 
     * @return
     */
    public abstract int getAllCount();

    /**
     * 获取每个数据项的视图
     * 
     * @param positionInTotal
     * @param convertView
     * @param parentView
     * @return
     */
    public abstract View getPeaceView( int positionInTotal , View convertView , ViewGroup parentView );

    protected View getHorizontalDivider()
    {
	ImageView imageView = new ImageView( mContext );
	//	imageView.setBackgroundResource( R.drawable.divider_horizontal );
	imageView.setBackgroundResource( R.color.divider );
	LinearLayout.LayoutParams llp = getHorizontalDiveiderLayoutParams( android.view.ViewGroup.LayoutParams.MATCH_PARENT );
	imageView.setLayoutParams( llp );
	return imageView;
    }

    private LinearLayout.LayoutParams getHorizontalDiveiderLayoutParams( int nWidth )
    {
	LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams( nWidth > 0 ? nWidth : android.view.ViewGroup.LayoutParams.MATCH_PARENT , 1 );
	return llp;
    }

    protected View getVerticalDivider()
    {
	ImageView imageView = new ImageView( mContext );
	LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams( 1 , android.view.ViewGroup.LayoutParams.MATCH_PARENT );
	imageView.setBackgroundResource( R.color.divider );
	//	imageView.setBackgroundResource( R.drawable.divider_vertical );
	imageView.setLayoutParams( llp );
	return imageView;
    }

    protected View genBlankView()
    {
	View blankView = new LinearLayout( mContext );
	blankView.setTag( R.id.tag_blankView , Boolean.TRUE );
	return blankView;
    }
}
