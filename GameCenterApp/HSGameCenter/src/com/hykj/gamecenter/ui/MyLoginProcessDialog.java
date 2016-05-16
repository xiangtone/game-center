package com.hykj.gamecenter.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hykj.gamecenter.R;

public class MyLoginProcessDialog extends Dialog
{

    private ProgressBar progressBar = null;
    private String tiptext;
    private TextView textView;

    public MyLoginProcessDialog( Context context , int theme )
    {
	super( context , theme );
    }

    public MyLoginProcessDialog( Context context , String tip )
    {
	this( context , R.style.transMyDialog );
	tiptext = tip;
    }

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
	// TODO Auto-generated method stub
	super.onCreate( savedInstanceState );
	setContentView( R.layout.csl_cs_general_loading );
	findViewById( R.id.csl_cs_loading_frame ).setVisibility( View.VISIBLE );
	progressBar = (ProgressBar)findViewById( R.id.csl_loading_progress );
	textView = (TextView)findViewById( R.id.csl_loading_tip );
	textView.setText( tiptext );
	View loadingView = findViewById( R.id.csl_cs_loading );
	loadingView.setVisibility( View.VISIBLE );
	View noNetworkView = findViewById( R.id.csl_cs_listview_no_networking );
	noNetworkView.setVisibility( View.GONE );
	//	Animation operatingAnim = AnimationUtils.loadAnimation( App.getAppContext( ) , R.anim.my_dialog_progress );
	//	progressBar.setAnimation( operatingAnim );
	//	operatingAnim.start( );
    }

}
