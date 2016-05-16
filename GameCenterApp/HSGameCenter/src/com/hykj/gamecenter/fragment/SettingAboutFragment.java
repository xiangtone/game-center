package com.hykj.gamecenter.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.App;

public class SettingAboutFragment extends Fragment
{

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
	super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState )
    {
	View rootView = inflater.inflate( R.layout.fragment_setting_about , container , false );

	TextView officeWeb = (TextView)rootView.findViewById( R.id.office_web );
	officeWeb.setOnClickListener( new View.OnClickListener( )
	{

	    @Override
	    public void onClick( View v )
	    {
		Uri web = Uri.parse( "http://www.cs.cc" );
		Intent i = new Intent( Intent.ACTION_VIEW , web );
		i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
		getActivity( ).startActivity( i );
	    }
	} );

	TextView dutyWeb = (TextView)rootView.findViewById( R.id.setting_reponse_daclare );
	dutyWeb.setOnClickListener( new View.OnClickListener( )
	{

	    @Override
	    public void onClick( View v )
	    {
		Uri web = Uri.parse( "http://www.cs.cc/apps/disclaimer" );
		Intent i = new Intent( Intent.ACTION_VIEW , web );
		i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
		getActivity( ).startActivity( i );
	    }
	} );

	TextView versionView = (TextView)rootView.findViewById( R.id.about_version );
	versionView.setText( getString( R.string.setting_about_version , App.VersionName( ) ) );

	return rootView;
    }
}
