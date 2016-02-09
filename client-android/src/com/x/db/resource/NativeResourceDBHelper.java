package com.x.db.resource;

import java.io.File;
import java.util.Iterator;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.AlbumColumns;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Files.FileColumns;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Video;
import android.util.Log;

import com.x.db.resource.NativeResourceConstant.Category;
import com.x.db.resource.NativeResourceConstant.SortMethod;

/**
 * 本地资源 DB操作
 * 
 
 */
public class NativeResourceDBHelper {
	private static final String TAG = "NativeResourceDBHelper";
	public static NativeResourceDBHelper dbHelper = null;
	private static Context mContext = null;
	
	private NativeResourceDBHelper() {}
	public static NativeResourceDBHelper getInstance(Context context) {
		if (dbHelper == null) {
			mContext = context;
			dbHelper = new NativeResourceDBHelper();
		}
		return dbHelper;
	}
	
	
	/**
	 * 分类查询
	 * @param category 类型
	 * @param sort   排序方式
	 * @return
	 */
	public Cursor query(Category category, String[] selectionArgs, SortMethod sort) {
		Uri uri = getContentUriByCategory(category);
		String selection = buildSelectionByCategory(category);
		String sortOrder = buildSortOrder(sort);
		String[] columns = buildColumns(category);
		if (uri == null) {
			Log.e(TAG, "invalid uri, category:" + category.name());
			return null;
		}
		return mContext.getContentResolver().query(uri, columns, selection, selectionArgs, sortOrder);
	}
	
	/**
	 * 查询分类总和
	 * @param category
	 * @param uri
	 */
	public Cursor queryCategory(Category category, Uri uri) {
		String[] columns = new String[] { "COUNT(*)", "SUM(_size)" };
		Cursor c = query(uri, columns, buildSelectionByCategory(category), null,
				null);
		if (c == null) {
			Log.e(TAG, "fail to query uri:" + uri);
		}
		return c;
	}
	public Cursor queryCategoryTemp(Category category, Uri uri) {
		String[] columns = new String[] { FileColumns.DATA };
		Cursor c = query(uri, columns, buildSelectionByCategory(category), null,
				null);
		if (c == null) {
			Log.e(TAG, "fail to query uri:" + uri);
		}
		return c;
	}
	
	/**
	 * 查询文件路径
	 * URI: Context://XXXXX/id
	 */
	public Cursor queryFilePath(Uri uri){
		String[] columns = new String[] { FileColumns._ID, FileColumns.DATA };
		Cursor c = query(uri, columns, null, null, null);
		if (c == null) {
			Log.e(TAG, "fail to query uri:" + uri);
		}
		return c;
	}
	
	/**
	 * 普通查询
	 * @param uri
	 * @param projection
	 * @param selection
	 * @param selectionArgs
	 * @param sortOrder
	 * @return
	 */
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
		return mContext.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
	}

	/**
	 * 更新媒体库,delete
	 * @param context
	 * @param dbID
	 */
	public int notifyFileSystemChanged(Category category, Context context, long dbID) {
		int result = context.getContentResolver().delete(
				getContentUriByCategory(category), BaseColumns._ID + "=?",
				new String[] { dbID + "" });
		return result;
	}
	
	/**
	 * 更新媒体库,changed
	 * @param path
	 */
    public void notifyFileSystemChanged(String path) {
        if (path == null)
            return;
        final File f = new File(path);
        final Intent intent;
        if (f.isDirectory()) {
            intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
            intent.setClassName("com.android.providers.media", "com.android.providers.media.MediaScannerReceiver");
            intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
        } else {
            intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(new File(path)));
        }
        mContext.sendBroadcast(intent);
    }
	
	/**
	 * 根据类型调用对应的 Media 获取URI
	 * @param cat
	 * @return
	 */
	private Uri getContentUriByCategory(Category cat) {
		Uri uri;
		String volumeName = "external";
		switch (cat) {
		case DOC:
			uri = Files.getContentUri(volumeName);
			break;
		case MUSIC:
			uri = Audio.Media.getContentUri(volumeName);
			break;
		case VIDEO:
//			uri = Video.Media.getContentUri(volumeName);
			uri = Files.getContentUri(volumeName);
			break;
        case VIDEO_THUMBNAILS:
        	uri = Video.Thumbnails.getContentUri(volumeName);
        	break;  
		case ALBUM:
		case PICTURE:
			uri = Images.Media.getContentUri(volumeName);
			break;
		default:
			uri = null;
		}
		return uri;
	}
	
	private String buildSelectionByCategory(Category cat) {
		String selection = null;
		switch (cat) {
		case DOC:
			selection = buildDocSelection();
			break;
		case VIDEO:
			selection = MediaStore.Video.VideoColumns.MIME_TYPE + " LIKE \'video/%\' or "
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.rmvb\' or "
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.3gp\' or "	
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.dl\' or "	
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.dif\' or "	
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.dv\' or "	
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.xv\' or "	
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.fli\' or "	
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.flv\' or "	
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.m4v\' or "	
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.mpeg\' or "	
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.mpg\' or "	
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.mpe\' or "	
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.mp4\' or "	
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.mkv\' or "	
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.mov\' or "	
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.mxu\' or "	
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.mng\' or "	
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.movie\' or "	
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.VOB\' or "
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.qt\' or "
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.lsf\' or "
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.lsx\' or "
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.asf\' or "
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.asx\' or "
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.avi\' or "
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.wm\' or "
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.wmv\' or "
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.wmx\' or " 
				+ MediaStore.Video.VideoColumns.DATA + " LIKE \'%_.wvx\' and "
				+ MediaStore.Video.VideoColumns.SIZE + " >\'1024\'";
			
//			selection = "(" + MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME +"=\'video\' or "
//					+ MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME +"=\'Camera\') "
//					+ "and " + MediaStore.Video.VideoColumns.SIZE + ">\'1024\'";
			break;
        case VIDEO_THUMBNAILS:
        	selection = MediaStore.Video.Thumbnails.VIDEO_ID +"=?";
        	break;
		default:
			selection = null;
		}
		return selection;
	}
	
	/**
	 * 拼接 where
	 * @return SQL where
	 */
	private String buildDocSelection() {
		StringBuilder selection = new StringBuilder();
		Iterator<String> iter = NativeResourceConstant.sDocMimeTypesSet.iterator();
		while (iter.hasNext()) {
			selection.append("(" + FileColumns.DATA + " LIKE '" + iter.next() + "') OR ");
		}
		return selection.substring(0, selection.lastIndexOf(")") + 1);
	}
	
	/**
	 * 排序方式
	 * @param sort
	 * @return
	 */
	private String buildSortOrder(SortMethod sort) {
		String sortOrder = null;
		switch (sort) {
		case NAME:
			sortOrder = FileColumns.TITLE + " ASC";
			break;
		case SIZE:
			sortOrder = FileColumns.SIZE + " ASC";
			break;
		case ALBUM:
		case DATE:
			sortOrder = FileColumns.DATE_MODIFIED + " DESC";
			break;
		case TYPE:
			sortOrder = FileColumns.MIME_TYPE + " ASC, " + FileColumns.TITLE + " ASC";
			break;
        case NOT:
        	sortOrder = null;
        	break;
		}
		return sortOrder;
	}
	
	/**
	 * 过滤不需要的列，优化查询速度
	 * @param category
	 * @return
	 */
	private String[] buildColumns(Category category) {
		String[] columns = null;
		switch (category) {
		case VIDEO:
		case DOC:
		case PICTURE:
			columns = new String[] { FileColumns._ID, FileColumns.DATA, FileColumns.SIZE, FileColumns.DATE_MODIFIED };
			break;
		case MUSIC:
			columns = new String[] { FileColumns._ID, FileColumns.DATA, FileColumns.SIZE, FileColumns.DATE_MODIFIED, 
					AlbumColumns.ALBUM_ID, Audio.Media.DURATION };
			break;
		case ALBUM:
			columns = new String[] { FileColumns._ID, FileColumns.DATA, ImageColumns.BUCKET_DISPLAY_NAME };
			break;
		case VIDEO_THUMBNAILS:
			columns = new String[] { Video.Thumbnails.VIDEO_ID, Video.Thumbnails.DATA };
			break;			
		}
		return columns;
	}
}
