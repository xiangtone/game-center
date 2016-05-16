package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hykj.gamecenter.R;

public class CSActionBar extends LinearLayout implements OnClickListener
{
    private CharSequence mTitle;
    //    private TextView mTitleView = null;

    private CharSequence mHomeUpTitle;
    private TextView mHomeUpTitleView = null;

    private ImageView mHomeUpImageView = null;
    private ImageView mSearchImageView = null;
    private ImageView mSettingImageView = null;
    private ImageView mClearImageView = null;
    private ImageView mBackImageView = null;
    private Button mRechargeButton = null;
    private Button mSearchButton = null;
    private Button mHomeUpButton = null;
    private EditText mSearchEditText = null;

    private boolean mShowTitle = false;
    private boolean mShowHomeUpTitle = false;
    private CSActionBar.AttchViewType mAttchViewType;

    private OnActionBarClickListener mOnActionBarListener = null;

    private final int mAttchButtonsMask;

    private View mButtonView = null;
    private View mSearchView = null;

    private OnSearchEditTextWatcherListener mOnSearchEditTextWatcherListener = null;

    public interface OnActionBarClickListener
    {
	int HOMEUP_BNT = 0x01;
	int DO_SEARCH_BNT = 0x02;
	int RETURN_BNT = 0x03;
	int SETTING_BNT = 0x04;
	int RECHARGE_BNT = 0x05;

	void onActionBarClicked( int position , String strEdit );
    }

    public interface OnSearchEditTextWatcherListener
    {
	void beforeSearchEditTextChanged( CharSequence s , int arg1 , int arg2 , int arg3 );

	void onSearchEditTextChanged( CharSequence s , int arg1 , int arg2 , int arg3 );

	void afterSearchEditTextChanged( Editable s );
    }

    /**
     * Bit field indicating that search button should be matched in methods that
     * take an options mask
     */
    private static final int ATTCH_SEARCH_BNT = 0x01;

    /**
     * Bit field indicating that setting button should be matched in methods
     * that take an options mask
     */
    private static final int ATTCH_SETTING_BNT = 0x02;

    /**
     * Bit field indicating that recharges button should be matched in methods
     * that take an options mask
     */
    private static final int ATTCH_RECHARGE_BNT = 0x04;

    /**
     * Bit mask indicating that all available patterns should be matched in
     * methods that take an options mask
     */
    private static final int ATTCH_ALL_BNT = ATTCH_SEARCH_BNT | ATTCH_SETTING_BNT | ATTCH_RECHARGE_BNT;

    private enum AttchViewType
    {
	NoneView , ButtonView , SearchView
    }

    public CSActionBar( Context context )
    {
	this( context , null );
    }

    public CSActionBar( Context context , AttributeSet attrs )
    {
	this( context , attrs , R.attr.plCSActionBarStyle );
    }

    public CSActionBar( Context context , AttributeSet attrs , int defStyle )
    {
	super( context , attrs , defStyle );

	TypedArray a = context.obtainStyledAttributes( attrs , R.styleable.CSActionBar , defStyle , 0 );

	mTitle = a.getString( R.styleable.CSActionBar_title );
	mHomeUpTitle = a.getString( R.styleable.CSActionBar_homeUpTitle );

	mShowTitle = a.getBoolean( R.styleable.CSActionBar_showTitle , true );
	mShowHomeUpTitle = a.getBoolean( R.styleable.CSActionBar_showHomeUpTitle , true );

	int type = a.getInteger( R.styleable.CSActionBar_attchView , 0 );
	switch ( type )
	{
	    case 1 :
		mAttchViewType = AttchViewType.ButtonView;
		break;
	    case 2 :
		mAttchViewType = AttchViewType.SearchView;
		break;
	    case 0 :
	    default :
		mAttchViewType = AttchViewType.NoneView;
		break;
	}

	mAttchButtonsMask = a.getInteger( R.styleable.CSActionBar_attchButtons , 0x00 );

	LayoutInflater.from( context ).inflate( R.layout.csl_cs_actionbar_general , this );
	a.recycle( );

    }

    @Override
    protected void onFinishInflate()
    {
	super.onFinishInflate( );
	Log.v( "csActionbar" , "csActionbar onFinishInflate" );

	//	mTitleView = (TextView)findViewById( R.id.csl_cs_actionbar_title );

	mHomeUpTitleView = (TextView)findViewById( R.id.csl_cs_actionbar_homeup_title );
	mHomeUpImageView = (ImageView)findViewById( R.id.csl_cs_actionbar_homeup_image );

	//	mButtonView = findViewById( R.id.csl_cs_actionbar_buttonview );
	mSearchView = findViewById( R.id.csl_cs_actionbar_searchview );

	switch ( mAttchViewType )
	{
	//	    case ButtonView :
	//	    {
	//		mSearchView.setVisibility( GONE );
	//		mButtonView.setVisibility( VISIBLE );
	//
	//		if( ( mAttchButtonsMask & ATTCH_SEARCH_BNT ) != 0 )
	//		{
	//		    mSearchImageView = (ImageView)findViewById( R.id.csl_cs_actionbar_search_image );
	//		    mSearchImageView.setVisibility( VISIBLE );
	//		    mSearchImageView.setOnClickListener( this );
	//		    mSearchImageView.setTag( OnActionBarClickListener.SETTING_BNT );
	//		}
	//
	//		if( ( mAttchButtonsMask & ATTCH_SETTING_BNT ) != 0 )
	//		{
	//		    mSettingImageView = (ImageView)findViewById( R.id.csl_cs_actionbar_setting_image );
	//		    mSettingImageView.setVisibility( VISIBLE );
	//		    mSettingImageView.setOnClickListener( this );
	//		    mSettingImageView.setTag( OnActionBarClickListener.SETTING_BNT );
	//
	//		}
	//
	//		if( ( mAttchButtonsMask & ATTCH_RECHARGE_BNT ) != 0 )
	//		{
	//		    mRechargeButton = (Button)findViewById( R.id.csl_cs_actionbar_recharge_button );
	//		    mRechargeButton.setVisibility( VISIBLE );
	//		    mRechargeButton.setOnClickListener( this );
	//		    mRechargeButton.setTag( OnActionBarClickListener.RECHARGE_BNT );
	//		}
	//		break;
	//	    }
	    case SearchView :
		mSearchView.setVisibility( VISIBLE );
		//		mButtonView.setVisibility( GONE );
		//		mTitleView.setVisibility( GONE );
		mShowTitle = false;

		mSearchButton = (Button)findViewById( R.id.csl_cs_actionbar_search_button );
		mSearchButton.setOnClickListener( this );
		mSearchButton.setTag( OnActionBarClickListener.DO_SEARCH_BNT );

		mHomeUpButton = (Button)findViewById( R.id.csl_cs_actionbar_homeup_button );
		mHomeUpButton.setOnClickListener( this );
		mHomeUpButton.setTag( OnActionBarClickListener.HOMEUP_BNT );

		mSearchEditText = (EditText)findViewById( R.id.csl_cs_actionbar_search_edit );
		mSearchEditText.setOnKeyListener( mEditorKeyListener );
		mSearchEditText.addTextChangedListener( mSearchEditTextWatcher );
		mSearchEditText.setOnFocusChangeListener( mEditTextFocusChangeListener );

		mClearImageView = (ImageView)findViewById( R.id.csl_cs_actionbar_clear_image );
		mClearImageView.setOnClickListener( this );
		
		mBackImageView = (ImageView)findViewById( R.id.return_icon );
		mBackImageView.setOnClickListener( this );
		mBackImageView.setTag( OnActionBarClickListener.RETURN_BNT );
		
		mSearchButton.setVisibility( GONE );
		mHomeUpButton.setVisibility( VISIBLE );
		mClearImageView.setVisibility( GONE );
		break;
	    case NoneView :
	    default :
		mSearchView.setVisibility( GONE );
		mButtonView.setVisibility( VISIBLE );
		break;
	}

	//	if( mShowTitle )
	//	{
	//	    mTitleView.setText( mTitle );
	//	    mTitleView.setVisibility( VISIBLE );
	//	}
	//	else
	//	{
	//	    mTitleView.setVisibility( INVISIBLE );
	//	}

	//		if (mShowHomeUpTitle) {
	//			mHomeUpTitleView.setText(mHomeUpTitle);
	//			mHomeUpImageView.setVisibility(VISIBLE);
	//			mHomeUpImageView.setOnClickListener(this);
	//			mHomeUpImageView.setTag(OnActionBarClickListener.HOMEUP_BNT);
	//
	//			mHomeUpTitleView.setVisibility(VISIBLE);
	//			mHomeUpTitleView.setOnClickListener(this);
	//			mHomeUpTitleView.setTag(OnActionBarClickListener.HOMEUP_BNT);
	//		} else {
	//			mHomeUpImageView.setVisibility(INVISIBLE);
	//			mHomeUpTitleView.setVisibility(INVISIBLE);
	//		}

    }

    OnFocusChangeListener mEditTextFocusChangeListener = new OnFocusChangeListener( )
    {
	@Override
	public void onFocusChange( View v , boolean hasFocus )
	{
	    EditText editText = (EditText)v;
	    if( !hasFocus )
	    {
		editText.setHint( R.string.csl_search_hint );
	    }
	    else
	    {
		String hint = editText.getHint( ).toString( );
		editText.setTag( hint );
		editText.setHint( "" );
	    }
	}
    };

    private final OnKeyListener mEditorKeyListener = new OnKeyListener( )
    {

	@Override
	public boolean onKey( View v , int keyCode , KeyEvent event )
	{
	    if( keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_ENTER)
	    {
		if( event.getAction( ) == KeyEvent.ACTION_UP )
		{
		    onClick( mSearchButton );
		}
		return true;
	    }
	    return false;
	}
    };

    //    public void setTitle( CharSequence title )
    //    {
    //	mTitle = title;
    //	if( mTitleView != null )
    //	    mTitleView.setText( title );
    //    }

    public void setHomeUpTitle( CharSequence homeUpTitle )
    {
	mHomeUpTitle = homeUpTitle;
	if( mHomeUpTitleView != null )
	    mHomeUpTitleView.setText( homeUpTitle );
    }

    public void setSearchEditorText( String strText )
    {
	if( mSearchEditText != null )
	{
	    mSearchEditText.setText( strText );

	    CharSequence text = mSearchEditText.getText( );
	    if( text instanceof Spannable )
	    {
		Spannable spanText = (Spannable)text;
		Selection.setSelection( spanText , text.length( ) );
	    }
	}
    }
    
    public void setSearchEditorTextFocusable(boolean focusable)
    {
	if( mSearchEditText != null )
	{
	    mSearchEditText.setFocusable( focusable );
	}
    }

    public void hideKeyboard ()
    {
	InputMethodManager imm = (InputMethodManager)getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
	imm.hideSoftInputFromWindow( mSearchEditText.getWindowToken( ) , 0 );
    }
    
    @Override
    public void onClick( View view )
    {
	if( mOnActionBarListener == null )
	    return;

	if( view == mSearchButton )
	{
	    if( mSearchEditText.getText( ).toString( ).length( ) > 0 )
	    {
		InputMethodManager imm = (InputMethodManager)getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
		imm.hideSoftInputFromWindow( mSearchEditText.getWindowToken( ) , 0 );
		mOnActionBarListener.onActionBarClicked( (Integer)view.getTag( ) , mSearchEditText.getText( ).toString( ) );
	    }
	}
	else if( view == mClearImageView )
	{
	    mSearchEditText.setText( "" );
	}
	else
	{
	    mOnActionBarListener.onActionBarClicked( (Integer)view.getTag( ) , null );
	}
    }

    public void SetOnActionBarClickListener( OnActionBarClickListener listener )
    {
	mOnActionBarListener = listener;
    }

    TextWatcher mSearchEditTextWatcher = new TextWatcher( )
    {
	@Override
	public void beforeTextChanged( CharSequence s , int arg1 , int arg2 , int arg3 )
	{
	    if( mOnSearchEditTextWatcherListener != null )
	    {
		mOnSearchEditTextWatcherListener.beforeSearchEditTextChanged( s , arg1 , arg2 , arg3 );
	    }
	}

	@Override
	public void onTextChanged( CharSequence s , int arg1 , int arg2 , int arg3 )
	{
	    if( s.length( ) == 0 )
	    {
		mSearchButton.setVisibility( GONE );
		mHomeUpButton.setVisibility( VISIBLE );
		mClearImageView.setVisibility( GONE );

	    }
	    else
	    {
		mSearchButton.setVisibility( VISIBLE );
		mHomeUpButton.setVisibility( GONE );
		mClearImageView.setVisibility( VISIBLE );
	    }
	    if( mOnSearchEditTextWatcherListener != null )
	    {
		mOnSearchEditTextWatcherListener.onSearchEditTextChanged( s , arg1 , arg2 , arg3 );
	    }
	}

	@Override
	public void afterTextChanged( Editable s )
	{
	    if( mOnSearchEditTextWatcherListener != null )
	    {
		mOnSearchEditTextWatcherListener.afterSearchEditTextChanged( s );
	    }
	}
    };

    public void setmOnSearchEditTextWatcherListener( OnSearchEditTextWatcherListener mOnSearchEditTextWatcherListener )
    {
	this.mOnSearchEditTextWatcherListener = mOnSearchEditTextWatcherListener;
    }
}
