package com.hykj.gamecenter.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.hykj.gamecenter.account.CSAuthenticator;

public class CSAuthenticatorService extends Service
{

    @Override
    public IBinder onBind( Intent arg0 )
    {
	Log.i( "CSAuthenticatorService" , "CSAuthenticatorService onBind" );
	return new CSAuthenticator( this ).getIBinder( );
    }

}
