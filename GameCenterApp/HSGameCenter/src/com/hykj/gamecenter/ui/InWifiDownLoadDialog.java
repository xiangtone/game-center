package com.hykj.gamecenter.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.App;
import com.hykj.gamecenter.activity.SettingListActivity;

//仅在wifi下下载的对话框
public class InWifiDownLoadDialog extends Dialog implements android.view.View.OnClickListener {
    private Context mContext = null;

    private Button settingBtn = null;
    private Button knowBtn = null;
    private TextView _title = null;
    private TextView _content = null;

    private View view = null;

    public static final int NO_WIFI = 0;
    public static final int IS_DOWNLOAD = 1;

    public InWifiDownLoadDialog( Context context , int theme ) {
	super( context , theme );
	mContext = context;
    }

    public void setView( View v ) {
	this.view = v;
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
	super.onCreate( savedInstanceState );
	setContentView( R.layout.dialog_accountecenter_update );

	settingBtn = (Button)findViewById( R.id.download_btn );
	knowBtn = (Button)findViewById( R.id.cancel_btn );
	settingBtn.setText( App.getAppContext( ).getResources( ).getString( R.string.do_setting ) );
	knowBtn.setText( App.getAppContext( ).getResources( ).getString( R.string.do_kown ) );

	settingBtn.setOnClickListener( this );
	knowBtn.setOnClickListener( this );

	_title = (TextView)findViewById( R.id.title );
	_content = (TextView)findViewById( R.id.update_hint );
	_content.setText( App.getAppContext( ).getResources( ).getString( R.string.do_no_wifi_do_download ) );

	_title.setVisibility( View.GONE );
    }

    public void setContent( String content ) {
	_content.setText( content );
    }

    @Override
    public void onClick( View v ) {
	int flag = v.getId( );
	switch ( flag ) {
	    case R.id.download_btn :
		Intent intent = new Intent( );
		intent.setClass( mContext , SettingListActivity.class );
		intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
		App.getAppContext( ).startActivity( intent );
		this.cancel( );
		break;

	    case R.id.cancel_btn :
		listener.onKnowClickListener( view );
//		listener.onKnowClickListener( v );
		this.cancel( );
		break;

	    default :
		break;
	}
    }

    public KnowBtnOnClickListener listener = null;

    public void setKnowBtnOnClickListener( KnowBtnOnClickListener listener ) {
	this.listener = listener;
    }

    public interface KnowBtnOnClickListener {
	void onKnowClickListener( View v );
    }
}
