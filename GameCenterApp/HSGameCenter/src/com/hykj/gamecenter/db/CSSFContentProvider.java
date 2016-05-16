
package com.hykj.gamecenter.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.hykj.gamecenter.db.CSSFDatabaseHelper.ConsumerRecordsColumns;
import com.hykj.gamecenter.db.CSSFDatabaseHelper.PlayedGamesColumns;
import com.hykj.gamecenter.db.CSSFDatabaseHelper.RechargeAmountsColumns;
import com.hykj.gamecenter.db.CSSFDatabaseHelper.RechargeRecordsColumns;
import com.hykj.gamecenter.db.CSSFDatabaseHelper.Tables;
import com.hykj.gamecenter.db.CSSFDatabaseHelper.UserInfoColumns;

import java.util.HashMap;

public class CSSFContentProvider extends ContentProvider
{

    private static final String AUTHORITY = "com.hykj.hssfdb";
    private CSSFDatabaseHelper mDbHelper;

    private static final int CONSUME_RECORDS = 1;
    private static final int RECHARGE_RECORDS = 2;
    private static final int USER_INFO = 3;
    private static final int PLAYED_GAMES = 4;
    private static final int RECHARGE_AMOUNTS = 5;

    public static final Uri CONSUME_CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/consume_records");
    public static final Uri RECHARGE_CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/recharge_records");
    public static final Uri USERINFO_CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/user_info");
    public static final Uri PLAYEDGAME_CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/played_game");
    public static final Uri RECHARGEAMOUNTS_CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/recharge_amounts");

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static
    {
        sURIMatcher.addURI(AUTHORITY, "consume_records", CONSUME_RECORDS);
        sURIMatcher.addURI(AUTHORITY, "recharge_records", RECHARGE_RECORDS);
        sURIMatcher.addURI(AUTHORITY, "user_info", USER_INFO);
        sURIMatcher.addURI(AUTHORITY, "played_game", PLAYED_GAMES);
        sURIMatcher.addURI(AUTHORITY, "recharge_amounts", RECHARGE_AMOUNTS);
    }

    private static final HashMap<String, String> consumeRecordsProjectionMap = new HashMap<String, String>();
    static
    {
        consumeRecordsProjectionMap.put(ConsumerRecordsColumns.CONSUME_ORDERNO,
                ConsumerRecordsColumns.CONSUME_ORDERNO);
        consumeRecordsProjectionMap.put(ConsumerRecordsColumns.CONSUME_OPENID,
                ConsumerRecordsColumns.CONSUME_OPENID);
        consumeRecordsProjectionMap.put(ConsumerRecordsColumns.CONSUME_ROLEID,
                ConsumerRecordsColumns.CONSUME_ROLEID);
        consumeRecordsProjectionMap.put(ConsumerRecordsColumns.CONSUME_CPORDER,
                ConsumerRecordsColumns.CONSUME_CPORDER);
        consumeRecordsProjectionMap.put(ConsumerRecordsColumns.CONSUME_APPID,
                ConsumerRecordsColumns.CONSUME_APPID);
        consumeRecordsProjectionMap.put(ConsumerRecordsColumns.CONSUME_APPNAME,
                ConsumerRecordsColumns.CONSUME_APPNAME);
        consumeRecordsProjectionMap.put(ConsumerRecordsColumns.CONSUME_PACKNAME,
                ConsumerRecordsColumns.CONSUME_PACKNAME);
        consumeRecordsProjectionMap.put(ConsumerRecordsColumns.CONSUME_CONSUMECOIN,
                ConsumerRecordsColumns.CONSUME_CONSUMECOIN);
        consumeRecordsProjectionMap.put(ConsumerRecordsColumns.CONSUME_PRODUCTCODE,
                ConsumerRecordsColumns.CONSUME_PRODUCTCODE);
        consumeRecordsProjectionMap.put(ConsumerRecordsColumns.CONSUME_PRODUCTNAME,
                ConsumerRecordsColumns.CONSUME_PRODUCTNAME);
        consumeRecordsProjectionMap.put(ConsumerRecordsColumns.CONSUME_PRODUCTCOUNT,
                ConsumerRecordsColumns.CONSUME_PRODUCTCOUNT);
        consumeRecordsProjectionMap.put(ConsumerRecordsColumns.CONSUME_TIME,
                ConsumerRecordsColumns.CONSUME_TIME);
        consumeRecordsProjectionMap.put(ConsumerRecordsColumns.CONSUME_STATUS,
                ConsumerRecordsColumns.CONSUME_STATUS);
    }

    private static final HashMap<String, String> rechargeRecordsProjectionMap = new HashMap<String, String>();
    static
    {
        rechargeRecordsProjectionMap.put(RechargeRecordsColumns.RECHARGE_ORDERNO,
                RechargeRecordsColumns.RECHARGE_ORDERNO);
        rechargeRecordsProjectionMap.put(RechargeRecordsColumns.RECHARGE_OPENID,
                RechargeRecordsColumns.RECHARGE_OPENID);
        rechargeRecordsProjectionMap.put(RechargeRecordsColumns.RECHARGE_AMT,
                RechargeRecordsColumns.RECHARGE_AMT);
        rechargeRecordsProjectionMap.put(RechargeRecordsColumns.RECHARGE_CONFIRMAMT,
                RechargeRecordsColumns.RECHARGE_CONFIRMAMT);
        rechargeRecordsProjectionMap.put(RechargeRecordsColumns.RECHARGE_CONFIRMCOIN,
                RechargeRecordsColumns.RECHARGE_CONFIRMCOIN);
        rechargeRecordsProjectionMap.put(RechargeRecordsColumns.RECHARGE_TYPE,
                RechargeRecordsColumns.RECHARGE_TYPE);
        rechargeRecordsProjectionMap.put(RechargeRecordsColumns.RECHARGE_FLAG,
                RechargeRecordsColumns.RECHARGE_FLAG);
        rechargeRecordsProjectionMap.put(RechargeRecordsColumns.RECHARGE_ACCOUNT,
                RechargeRecordsColumns.RECHARGE_ACCOUNT);
        rechargeRecordsProjectionMap.put(RechargeRecordsColumns.RECHARGE_SUBMITTIME,
                RechargeRecordsColumns.RECHARGE_SUBMITTIME);
        rechargeRecordsProjectionMap.put(RechargeRecordsColumns.RECHARGE_CONFIRMTIME,
                RechargeRecordsColumns.RECHARGE_CONFIRMTIME);
        rechargeRecordsProjectionMap.put(RechargeRecordsColumns.RECHARGE_STATUS,
                RechargeRecordsColumns.RECHARGE_STATUS);
    }

    private static final HashMap<String, String> userinfoProjectionMap = new HashMap<String, String>();
    static
    {
        userinfoProjectionMap.put(UserInfoColumns.OPEN_ID, UserInfoColumns.OPEN_ID);
        userinfoProjectionMap.put(UserInfoColumns.USER_NAME, UserInfoColumns.USER_NAME);
        userinfoProjectionMap.put(UserInfoColumns.USER_SEX, UserInfoColumns.USER_SEX);
        userinfoProjectionMap.put(UserInfoColumns.BIND_MAIL, UserInfoColumns.BIND_MAIL);
        userinfoProjectionMap.put(UserInfoColumns.BIND_MOBILE, UserInfoColumns.BIND_MOBILE);
        userinfoProjectionMap.put(UserInfoColumns.HEAD_PIC_URL, UserInfoColumns.HEAD_PIC_URL);
        userinfoProjectionMap.put(UserInfoColumns.LAST_LOG_TIME, UserInfoColumns.LAST_LOG_TIME);
        userinfoProjectionMap.put(UserInfoColumns.REGISTER_TIME, UserInfoColumns.REGISTER_TIME);
        userinfoProjectionMap.put(UserInfoColumns.USER_STATUS, UserInfoColumns.USER_STATUS);
        userinfoProjectionMap.put(UserInfoColumns.USER_ACCOUNT_STATUS,
                UserInfoColumns.USER_ACCOUNT_STATUS);
        userinfoProjectionMap.put(UserInfoColumns.USER_ACCOUNT_ACTIVATE_TIME,
                UserInfoColumns.USER_ACCOUNT_ACTIVATE_TIME);
        userinfoProjectionMap.put(UserInfoColumns.ACCOUNT_PACOIN, UserInfoColumns.ACCOUNT_PACOIN);
        userinfoProjectionMap.put(UserInfoColumns.UESR_TOKEN, UserInfoColumns.UESR_TOKEN);
        userinfoProjectionMap.put(UserInfoColumns.RECHARGE_COUNT, UserInfoColumns.RECHARGE_COUNT);
        userinfoProjectionMap.put(UserInfoColumns.CONSUME_COUNT, UserInfoColumns.CONSUME_COUNT);
    }

    private static final HashMap<String, String> playedgameProjectionMap = new HashMap<String, String>();
    static
    {
        playedgameProjectionMap.put(PlayedGamesColumns.APP_ID, PlayedGamesColumns.APP_ID);
        playedgameProjectionMap.put(PlayedGamesColumns.APP_NAME, PlayedGamesColumns.APP_NAME);
        playedgameProjectionMap.put(PlayedGamesColumns.APP_PACKAGE_NAME,
                PlayedGamesColumns.APP_PACKAGE_NAME);
        playedgameProjectionMap.put(PlayedGamesColumns.APP_SUB_ACCOUNT,
                PlayedGamesColumns.APP_SUB_ACCOUNT);
        playedgameProjectionMap.put(PlayedGamesColumns.APP_LAST_OPENED,
                PlayedGamesColumns.APP_LAST_OPENED);
        playedgameProjectionMap.put(PlayedGamesColumns.APP_ICON, PlayedGamesColumns.APP_ICON);
    }

    private static final HashMap<String, String> rechargeAmountsProjectionMap = new HashMap<String, String>();
    static
    {
        rechargeAmountsProjectionMap.put(RechargeAmountsColumns.RECHARGE_AMOUNTS_TYPE,
                RechargeAmountsColumns.RECHARGE_AMOUNTS_TYPE);
        rechargeAmountsProjectionMap.put(RechargeAmountsColumns.RECHARGE_AMOUNTS_PRICE,
                RechargeAmountsColumns.RECHARGE_AMOUNTS_PRICE);
        rechargeAmountsProjectionMap.put(RechargeAmountsColumns.RECHARGE_AMOUNTS_SHOWVALUE,
                RechargeAmountsColumns.RECHARGE_AMOUNTS_SHOWVALUE);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        switch (sURIMatcher.match(uri))
        {
            case CONSUME_RECORDS:
                return db.delete(Tables.ConsumeRecords, selection, selectionArgs);
            case RECHARGE_RECORDS:
                return db.delete(Tables.RechargeRecords, selection, selectionArgs);
            case USER_INFO:
                return db.delete(Tables.UserInfo, selection, selectionArgs);
            case PLAYED_GAMES:
                return db.delete(Tables.PlayedGames, selection, selectionArgs);
            case RECHARGE_AMOUNTS:
                return db.delete(Tables.RechargeAmounts, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public String getType(Uri uri)
    {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long rowId = 0;

        switch (sURIMatcher.match(uri))
        {
            case CONSUME_RECORDS:
                if ((values != null) && (values.size() > 0))
                {
                    rowId = db.insert(Tables.ConsumeRecords, null, values);
                    if (rowId > 0)
                    {
                        return ContentUris.withAppendedId(uri, rowId);
                    }
                }
                break;
            case RECHARGE_RECORDS:
                if ((values != null) && (values.size() > 0))
                {
                    rowId = db.insert(Tables.RechargeRecords, null, values);
                    if (rowId > 0)
                    {
                        return ContentUris.withAppendedId(uri, rowId);
                    }
                }
                break;
            case USER_INFO:
                if ((values != null) && (values.size() > 0))
                {
                    rowId = db.insert(Tables.UserInfo, null, values);
                    if (rowId > 0)
                    {
                        return ContentUris.withAppendedId(uri, rowId);
                    }
                }
                break;
            case PLAYED_GAMES:
                if ((values != null) && (values.size() > 0))
                {
                    rowId = db.insert(Tables.PlayedGames, null, values);
                    if (rowId > 0)
                    {
                        return ContentUris.withAppendedId(uri, rowId);
                    }
                }
                break;
            case RECHARGE_AMOUNTS:
                if ((values != null) && (values.size() > 0))
                {
                    rowId = db.insert(Tables.RechargeAmounts, null, values);
                    if (rowId > 0)
                    {
                        return ContentUris.withAppendedId(uri, rowId);
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return null;
    }

    @Override
    public boolean onCreate()
    {
        mDbHelper = new CSSFDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder)
    {

        final SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setStrict(true);

        switch (sURIMatcher.match(uri))
        {
            case CONSUME_RECORDS:
                qb.setTables(Tables.ConsumeRecords);
                qb.setProjectionMap(consumeRecordsProjectionMap);
                break;
            case RECHARGE_RECORDS:
                qb.setTables(Tables.RechargeRecords);
                qb.setProjectionMap(rechargeRecordsProjectionMap);
                break;
            case USER_INFO:
                qb.setTables(Tables.UserInfo);
                qb.setProjectionMap(userinfoProjectionMap);
                break;
            case PLAYED_GAMES:
                qb.setTables(Tables.PlayedGames);
                qb.setProjectionMap(playedgameProjectionMap);
                break;
            case RECHARGE_AMOUNTS:
                qb.setTables(Tables.RechargeAmounts);
                qb.setProjectionMap(rechargeAmountsProjectionMap);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (sURIMatcher.match(uri))
        {
            case CONSUME_RECORDS:
                return db.update(Tables.ConsumeRecords, values, selection, selectionArgs);
            case RECHARGE_RECORDS:
                return db.update(Tables.RechargeRecords, values, selection, selectionArgs);
            case USER_INFO:
                return db.update(Tables.UserInfo, values, selection, selectionArgs);
            case PLAYED_GAMES:
                return db.update(Tables.PlayedGames, values, selection, selectionArgs);
            case RECHARGE_AMOUNTS:
                return db.update(Tables.RechargeAmounts, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        // return 0;
    }

}
