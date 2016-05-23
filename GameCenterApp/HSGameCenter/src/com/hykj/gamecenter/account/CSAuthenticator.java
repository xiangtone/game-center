package com.hykj.gamecenter.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.PersonLogin;

public class CSAuthenticator extends AbstractAccountAuthenticator
{

    private static final String TAG = "CSAuthenticator";
    private final Context mContext;

    public CSAuthenticator( Context context )
    {
	super( context );
	mContext = context;
    }

    @Override
    public Bundle addAccount( AccountAuthenticatorResponse response , String accountType , String authTokenType , String [] requiredFeatures , Bundle options ) throws NetworkErrorException
    {
	final Intent intent = new Intent( mContext , PersonLogin.class );
	intent.putExtra( AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE , response );
	final Bundle bundle = new Bundle( );
	bundle.putParcelable( AccountManager.KEY_INTENT , intent );
	return bundle;
    }

    @Override
    public Bundle confirmCredentials( AccountAuthenticatorResponse response , Account account , Bundle options ) throws NetworkErrorException
    {
	return null;
    }

    @Override
    public Bundle editProperties( AccountAuthenticatorResponse response , String accountType )
    {
	throw new UnsupportedOperationException( );
    }

    @Override
    public Bundle getAuthToken( AccountAuthenticatorResponse response , Account account , String authTokenType , Bundle options ) throws NetworkErrorException
    {
	// If the caller requested an authToken type we don't support, then
	// return an error
	if( !authTokenType.equals( "1" ) )
	{
	    final Bundle result = new Bundle( );
	    result.putString( AccountManager.KEY_ERROR_MESSAGE , "invalid authTokenType" );
	    return result;
	}

	// Extract the username and password from the Account Manager, and ask
	// the server for an appropriate AuthToken.
	final AccountManager am = AccountManager.get( mContext );
	final String password = am.getPassword( account );
	if( password != null )
	{
	    // final String authToken =
	    // NetworkUtilities.authenticate(account.name, password);
	    final String authToken = "123";
	    if( !TextUtils.isEmpty( authToken ) )
	    {
		final Bundle result = new Bundle( );
		result.putString( AccountManager.KEY_ACCOUNT_NAME , account.name );
		result.putString( AccountManager.KEY_ACCOUNT_TYPE , mContext.getString( R.string.cs_accounttype ) );
		result.putString( AccountManager.KEY_AUTHTOKEN , authToken );
		return result;
	    }
	}
	// String token = aManager.getUserData(accounts[0],
	// AccountManager.KEY_AUTHTOKEN);

	// If we get here, then we couldn't access the user's password - so we
	// need to re-prompt them for their credentials. We do that by creating
	// an intent to display our AuthenticatorActivity panel.
	final Intent intent = new Intent( mContext , PersonLogin.class );
	intent.putExtra( "username" , account.name );
	intent.putExtra( "authtokentype" , authTokenType );
	intent.putExtra( AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE , response );
	final Bundle bundle = new Bundle( );
	bundle.putParcelable( AccountManager.KEY_INTENT , intent );
	return bundle;
    }

    @Override
    public String getAuthTokenLabel( String authTokenType )
    {
	return null;
    }

    @Override
    public Bundle hasFeatures( AccountAuthenticatorResponse response , Account account , String [] features ) throws NetworkErrorException
    {
	return null;
    }

    @Override
    public Bundle updateCredentials( AccountAuthenticatorResponse response , Account account , String authTokenType , Bundle options ) throws NetworkErrorException
    {
	throw new UnsupportedOperationException( "updateCredentials not supported" );
    }

}
