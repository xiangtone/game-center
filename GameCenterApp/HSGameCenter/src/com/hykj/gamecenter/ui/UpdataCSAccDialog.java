package com.hykj.gamecenter.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hykj.gamecenter.R;

public class UpdataCSAccDialog extends Dialog implements android.view.View.OnClickListener
{

    private UpdateCSAccListener listener = null;

    private Button downloadBtn = null;
    private Button cancel = null;
    private TextView _title = null;
    private TextView _content = null;

    public static final int NO_WIFI = 0;
    public static final int IS_DOWNLOAD = 1;

    public UpdataCSAccDialog( Context context , int theme )
    {
	super( context , theme );
    }

    public interface UpdateCSAccListener
    {
	void UpdateCSAccClick(View view);
    }

    public void addUpdatecsAccListener( UpdateCSAccListener listener )
    {
	this.listener = listener;
    }

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
	super.onCreate( savedInstanceState );
	setContentView( R.layout.dialog_accountecenter_update );

	downloadBtn = (Button)findViewById( R.id.download_btn );
	cancel = (Button)findViewById( R.id.cancel_btn );

	downloadBtn.setOnClickListener( this );
	cancel.setOnClickListener( this );

	_title = (TextView)findViewById( R.id.title );
	_content = (TextView)findViewById( R.id.update_hint );

    }

    public void setContent( String content )
    {
	_title.setVisibility( View.GONE );
	_content.setText( content );
    }

    public void setButtonName( int flag , String left , String right )
    {
	downloadBtn.setText( left );
	cancel.setText( right );
	downloadBtn.setTag( flag );
	cancel.setTag( flag );
    }

    @Override
    public void onClick( View v )
    {
	int flag = v.getId( );
	if( flag == downloadBtn.getId( ) )
	{
	    listener.UpdateCSAccClick( v );
	}
	else if( flag == cancel.getId( ) )
	{
	    cancel( );
	}
    }
}
