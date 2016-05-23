
package com.hykj.gamecenter.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.hykj.gamecenter.db.CSACDatabaseHelper.Tables;

public class CSACContentProvider extends ContentProvider
{

    private static final String AUTHORITY = "com.hykj.hsgcdb";
    private CSACDatabaseHelper mDbHelper;

    private static final int DOWNLOAD_INFOES = 1;
    private static final int GROUP_INFO = 2;
    private static final int HOT_WORDS = 3;
    private static final int DOWNLOADED_INFOES = 4;
    private static final int REPORT = 5;

    public static final Uri DOWNLOADINFO_CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/download_infoes");
    public static final Uri GROUPINFO_CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/group_info");
    public static final Uri HOT_WORDS_URI = Uri.parse("content://" + AUTHORITY + "/hot_words");
    public static final Uri DOWNLOADEDINFO_CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/downloaded_infoes");
    public static final Uri REPORT_URI = Uri.parse("content://" + AUTHORITY
            + "/report_tmp");

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static
    {
        sURIMatcher.addURI(AUTHORITY, "download_infoes", DOWNLOAD_INFOES);
        sURIMatcher.addURI(AUTHORITY, "group_info", GROUP_INFO);
        sURIMatcher.addURI(AUTHORITY, "hot_words", HOT_WORDS);
        sURIMatcher.addURI(AUTHORITY, "downloaded_infoes", DOWNLOADED_INFOES);
        sURIMatcher.addURI(AUTHORITY, "report_tmp", REPORT);
    }

    @Override
    public boolean onCreate()
    {
        mDbHelper = new CSACDatabaseHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        switch (sURIMatcher.match(uri))
        {
            case DOWNLOAD_INFOES:
                return db.delete(Tables.DownloadInfoes, selection, selectionArgs);
            case GROUP_INFO:
                return db.delete(Tables.GroupInfo, selection, selectionArgs);
            case HOT_WORDS:
                return db.delete(Tables.HotWords, selection, selectionArgs);
            case DOWNLOADED_INFOES:
                return db.delete(Tables.DownloadedInfoes, selection, selectionArgs);
            case REPORT:
                return db.delete(Tables.Report, selection, selectionArgs);
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
            case DOWNLOAD_INFOES:
                if ((values != null) && (values.size() > 0))
                {
                    rowId = db.insert(Tables.DownloadInfoes, null, values);
                    if (rowId > 0)
                    {
                        return ContentUris.withAppendedId(uri, rowId);
                    }
                }
                break;
            case GROUP_INFO:
                if ((values != null) && (values.size() > 0))
                {
                    rowId = db.insert(Tables.GroupInfo, null, values);
                    if (rowId > 0)
                    {
                        return ContentUris.withAppendedId(uri, rowId);
                    }
                }
                break;
            case HOT_WORDS:
                if ((values != null) && (values.size() > 0))
                {
                    rowId = db.insert(Tables.HotWords, null, values);
                    if (rowId > 0)
                    {
                        return ContentUris.withAppendedId(uri, rowId);
                    }
                }
                break;
            case DOWNLOADED_INFOES:
                if ((values != null) && (values.size() > 0))
                {
                    rowId = db.insert(Tables.DownloadedInfoes, null, values);
                    if (rowId > 0)
                    {
                        return ContentUris.withAppendedId(uri, rowId);
                    }
                }
                break;
            case REPORT:
                if ((values != null) && (values.size() > 0))
                {
                    rowId = db.insert(Tables.Report, null, values);
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
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder)
    {
        final SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sURIMatcher.match(uri))
        {
            case DOWNLOAD_INFOES:
                qb.setTables(Tables.DownloadInfoes);
                break;
            case GROUP_INFO:
                qb.setTables(Tables.GroupInfo);
                break;
            case HOT_WORDS:
                qb.setTables(Tables.HotWords);
                break;
            case DOWNLOADED_INFOES:
                qb.setTables(Tables.DownloadedInfoes);
                break;
            case REPORT:
                qb.setTables(Tables.Report);
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
            case DOWNLOAD_INFOES:
                return db.update(Tables.DownloadInfoes, values, selection, selectionArgs);
            case GROUP_INFO:
                return db.update(Tables.GroupInfo, values, selection, selectionArgs);
            case HOT_WORDS:
                return db.update(Tables.HotWords, values, selection, selectionArgs);
            case DOWNLOADED_INFOES:
                return db.update(Tables.DownloadedInfoes, values, selection, selectionArgs);
            case REPORT:
                return db.update(Tables.Report, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

}
