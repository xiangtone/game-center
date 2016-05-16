package com.hykj.gamecenter.utils;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;

import com.hykj.gamecenter.App;

public class CipherHelper
{

    // 本版本初始的RSA_VER
    private static String PUBLIC_KEY = "737CC72385200604997F9FE3B7CC0D0273A6380DF71C0020E1CCB9F2E03A60A2AD87705783E4F7A6199A3A570904B083965CBE7E16BAA83B6BB11B27A15DBF21";
    private static String PUBLIC_EXPONENT = "65537";
    public static String RSA_VER = "1";

    // preference名
    private static final String PUBLIC_KEY_PREF = "rpk";
    private static final String PUBLIC_EXPONENT_PREF = "rpe";
    private static final String RSA_VER_PREF = "rv";

    public static void initialRsaParam()
    {
	SharedPreferences sp = App.getSharedPreference( );
	if( sp.contains( PUBLIC_KEY_PREF ) )
	{
	    PUBLIC_KEY = sp.getString( PUBLIC_KEY_PREF , PUBLIC_KEY );
	    PUBLIC_EXPONENT = sp.getString( PUBLIC_EXPONENT_PREF , PUBLIC_EXPONENT );
	    RSA_VER = sp.getString( RSA_VER_PREF , RSA_VER );
	    Log.e( "initialRsaP" , "1" );
	}
	else
	{
	    Editor editor = sp.edit( );
	    editor.putString( PUBLIC_KEY_PREF , PUBLIC_KEY );
	    editor.putString( PUBLIC_EXPONENT_PREF , PUBLIC_EXPONENT );
	    editor.putString( RSA_VER_PREF , RSA_VER );
	    editor.commit( );
	    Log.e( "initialRsaP" , "2" );
	}
	Log.e( "initialRsaParam" , "PUBLIC_KEY:" + PUBLIC_KEY + ",PUBLIC_EXPONENT:" + PUBLIC_EXPONENT );
    }

    public static void setRsaParam( String rsaVer , String pk , String pe )
    {
	Log.e( "setRsaParam" , "rsaVer:" + rsaVer + ",pk:" + pk + ",pe:" + pe );

	if( !TextUtils.isEmpty( rsaVer ) && !TextUtils.isEmpty( pk ) && !TextUtils.isEmpty( pe ) )
	{

	    PUBLIC_KEY = pk;
	    PUBLIC_EXPONENT = pe;
	    RSA_VER = rsaVer;

	    SharedPreferences sp = App.getSharedPreference( );
	    Editor editor = sp.edit( );
	    editor.putString( PUBLIC_KEY_PREF , PUBLIC_KEY );
	    editor.putString( PUBLIC_EXPONENT_PREF , PUBLIC_EXPONENT );
	    editor.putString( RSA_VER_PREF , RSA_VER );
	    editor.commit( );
	}
    }

    private static PublicKey getPublicKey( String modulus , String publicExponent ) throws Exception
    {

	BigInteger m = new BigInteger( modulus , 16 );

	BigInteger e = new BigInteger( publicExponent );

	RSAPublicKeySpec keySpec = new RSAPublicKeySpec( m , e );

	KeyFactory keyFactory = KeyFactory.getInstance( "RSA" );
	PublicKey publicKey = keyFactory.generatePublic( keySpec );

	return publicKey;

    }

    public static String rsaEncryptStr( String msg )
    {
	Log.e( "rsaEncryptStr" , "PUBLIC_KEY:" + PUBLIC_KEY + ",PUBLIC_EXPONENT:" + PUBLIC_EXPONENT );
	String resStr = "";
	try
	{
	    PublicKey pk = getPublicKey( PUBLIC_KEY , PUBLIC_EXPONENT );
	    Cipher cipher = Cipher.getInstance( "RSA" );
	    cipher.init( Cipher.ENCRYPT_MODE , pk );
	    byte [] enBytes = cipher.doFinal( msg.getBytes( Charset.forName( "UTF-8" ) ) );
	    resStr = bytesToHexString( enBytes );
	}
	catch ( Exception e )
	{
	    e.printStackTrace( );
	}
	return resStr;
    }

    private static String bytesToHexString( byte [] src )
    {

	StringBuilder stringBuilder = new StringBuilder( "" );
	if( src == null || src.length <= 0 )
	{
	    return null;
	}
	for( int i = 0 ; i < src.length ; i++ )
	{
	    int v = src[i] & 0xFF;
	    String hv = Integer.toHexString( v );
	    if( hv.length( ) < 2 )
	    {
		stringBuilder.append( 0 );
	    }
	    stringBuilder.append( hv );
	}
	return stringBuilder.toString( );
    }

    public static String encryptPwd( String pwd )
    {
	String ret = "";
	try
	{
	    MessageDigest digest = MessageDigest.getInstance( "MD5" );
	    digest.update( pwd.getBytes( Charset.forName( "UTF-8" ) ) );
	    byte messageDigest[] = digest.digest( );
	    ret = rsaEncryptStr( bytesToHexString( messageDigest ) );
	}
	catch ( NoSuchAlgorithmException e )
	{
	    e.printStackTrace( );
	}
	return ret;
    }
}
