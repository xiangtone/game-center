package com.x.business.resource;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AlbumColumns;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Files.FileColumns;
import android.provider.MediaStore.Images;

import com.x.db.resource.NativeResourceConstant;
import com.x.db.resource.NativeResourceDBHelper;
import com.x.db.resource.NativeResourceConstant.Category;
import com.x.db.resource.NativeResourceConstant.SortMethod;
import com.x.db.resource.NativeResourceConstant.Voice;
import com.x.publics.model.AlbumBean;
import com.x.publics.model.FileBean;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.ThreadUtil;
import com.x.publics.utils.Utils;

/**
 * 本地资源 业务处理
 
 *
 */
public class ResourceManage {
	
	private static final String TAG = "ResourceManage";
	private static Context mContext;
	private static ResourceManage resourceManage = null;
	private onRefreshMessage message = null;
//	private HashMap<Category, CategoryInfo> mCategoryInfo = new HashMap<Category, CategoryInfo>();
	private ArrayList<FileBean> oldList = new ArrayList<FileBean>();
	//如有添加，下面getAllFile()也要相应添加
	private static final String[] DOCUMENTS = {"txt", "text", "pdf", 
		"doc", "xls", "ppt"};// 兼容2.3
	
	private ResourceManage() {}
	public static ResourceManage getInstance(Context context) {
		mContext = context;
		if (resourceManage == null) {
			resourceManage = new ResourceManage();
		}
		return resourceManage;
	}
	
	/**
	 * 获取文件路径
	 */
	public String getFilePath(Context con, Uri uri){
		if(uri == null)
			return null;
		String path = null;
		Cursor cursor = null;
		try {
			cursor = NativeResourceDBHelper.getInstance(mContext).queryFilePath(uri);
			if(cursor != null && cursor.getCount() > 0){
				while (cursor.moveToNext()) {
					path = cursor.getString(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null){
				cursor.close();
				cursor = null;
			}
		}
		return path;
	}
	
	
	/***********
	 * 查询各类型数据总和
	 */
//	public void refreshCategoryInfo(final onRefreshMessage message) {
//		Category[] sCategories = NativeResourceConstant.sCategories;
//		// clear
//		for (Category category : sCategories) {
//			setCategoryInfo(category, 0, 0);
//		}
//
//		this.message = message;
//		// query database
//		String volumeName = "external";
//
//		Uri uri = Audio.Media.getContentUri(volumeName);
//		refreshMediaCategory(Category.MUSIC, uri);
//
//		if(Build.VERSION.SDK_INT >= 11){
//			uri = Files.getContentUri(volumeName);
//		}else{//2.3
//			uri = Video.Media.getContentUri(volumeName);
//		}
//		refreshMediaCategoryTemp(Category.VIDEO, uri);
//
//		uri = Images.Media.getContentUri(volumeName);
//		refreshMediaCategory(Category.PICTURE, uri);
//		      
////		Log.i(TAG, "----------SDK_INT="+Build.VERSION.SDK_INT);
//		if(Build.VERSION.SDK_INT >= 11){//3.0以下未文件数据库，由queryDocment()处理
//			uri = Files.getContentUri(volumeName);
//			refreshMediaCategory(Category.DOC, uri);			
//		}  
//	}
//	
//	private void refreshMediaCategory(final Category category, final Uri uri){
//		Cursor cursor = NativeResourceDBHelper.getInstance(mContext).queryCategory(category, uri);
//		if(cursor != null){
//			if (cursor.moveToNext()) {
//				setCategoryInfo(category, cursor.getLong(0), cursor.getLong(1));
////				Log.v(TAG, "Retrieved " + category.name() + " info >>> count:" + cursor.getLong(0) + " size:"
////								+ cursor.getLong(1));
//			}
//			cursor.close();
//			message.onMessage(true);
//		}
//	}
//	
//	private void refreshMediaCategoryTemp(final Category category, final Uri uri){
//		Cursor cursor = NativeResourceDBHelper.getInstance(mContext).queryCategoryTemp(category, uri);
//		if(cursor != null){
//			int i = 0;
//			while (cursor.moveToNext()) {
//				String path = cursor.getString(0);
//				if(path.indexOf("/.") == -1){//过滤隐藏文件夹
//					setCategoryInfo(category, ++i, 0);
//				}
//			}
//			cursor.close();
//			i = 0;
//			message.onMessage(true);
//		}
//	}
//	
//	public class CategoryInfo {
//		public long count; //数量
//		public long size; //占用大小
//	}
//
//	private void setCategoryInfo(Category fc, long count, long size) {
//		CategoryInfo info = mCategoryInfo.get(fc);
//		if (info == null) {
//			info = new CategoryInfo();
//			mCategoryInfo.put(fc, info);
//		}
//		info.count = count;
//		info.size = size;
//	}
//	
//	public HashMap<Category, CategoryInfo> getCategoryInfos() {
//		return mCategoryInfo;
//	}
//
//	public CategoryInfo getCategoryInfo(Category fc) {
//		if (mCategoryInfo.containsKey(fc)) {
//			return mCategoryInfo.get(fc);
//		} else {
//			CategoryInfo info = new CategoryInfo();
//			mCategoryInfo.put(fc, info);
//			return info;
//		}
//	}
	/***************/
	
	
	/***************
	 * 查询音频文件
	 * @return
	 */
	public ArrayList<FileBean> queryMusic(){
		ArrayList<FileBean> musicList = new ArrayList<FileBean>();
		if (!StorageUtils.isSDCardPresent()) {
			return musicList;
		}
		Cursor cursor = NativeResourceDBHelper.getInstance(mContext).query(Category.MUSIC, null,
				SortMethod.NAME);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				FileBean fileInfo = Utils.GetFileInfo(Category.MUSIC, 
						cursor.getString(NativeResourceConstant.COLUMN_PATH), true);
				if(fileInfo == null){//数据库有数据，本地文件不存在
					NativeResourceDBHelper.getInstance(mContext).notifyFileSystemChanged(
							Category.MUSIC, mContext, cursor.getLong(NativeResourceConstant.COLUMN_ID));
				}else{
					fileInfo.setDbId(cursor.getLong(NativeResourceConstant.COLUMN_ID));
					fileInfo.setAlbumID(cursor.getLong(NativeResourceConstant.MUSIC_ALBUMID));
					fileInfo.setDuration(cursor.getLong(NativeResourceConstant.MUSIC_DURATION));
					musicList.add(fileInfo);
				}
			}
		}
		cursor.close();
		cursor = null;
		return musicList; 
	}
	
	/**
	 * 获取音频封面
	 * @param albumID
	 * @return
	 */
	private HashMap<String, String> musicAlbumPath = new HashMap<String, String>();
	public String queryMusicAlbum(long albumID){
		if(musicAlbumPath.containsKey(albumID+"")){
			return musicAlbumPath.get(albumID+"");
		}
		Uri uri = Albums.getContentUri("external");
		String selection = FileColumns._ID +"=?";
		String[] selectionArgs = { albumID+"" };
		String[] columns = new String[] { AlbumColumns.ALBUM_ART };

		String path = null;
		Cursor cursor = NativeResourceDBHelper.getInstance(mContext).query(uri, columns, selection, selectionArgs, null);
		if(cursor.getCount() > 0 && cursor.moveToFirst()){
			path = cursor.getString(0);
			musicAlbumPath.put(albumID+"", path);
		}
		cursor.close();
		cursor = null;
		return path;
	}
	
	/**
	 * 查询当前路径文件是否为默认声音
	 * @param path
	 * 
	 * Deprecated
	 */
	@Deprecated
	public boolean[] queryVoice(String path){
		boolean[] voiceStatus = null;
		Uri uri = MediaStore.Audio.Media.getContentUriForPath(path);
		String[] row = {
			MediaStore.Audio.Media.IS_RINGTONE,
			MediaStore.Audio.Media.IS_NOTIFICATION,
			MediaStore.Audio.Media.IS_ALARM
		};
		
		Cursor cursor = NativeResourceDBHelper.getInstance(mContext).query(uri, row,
				MediaStore.MediaColumns.DATA + "=?", new String[] { path },
				null);
		if (cursor.moveToFirst() && cursor.getCount() > 0) {
			voiceStatus = new boolean[]{
					cursor.getInt(0) == 1 ? true : false, 
					cursor.getInt(1) == 1 ? true : false, 
					cursor.getInt(2) == 1 ? true : false };
		}
		cursor.close();
		cursor = null;
		return voiceStatus;
	} 
	
	/**
	 * 设置声音
	 * @param music
	 * @param path 文件路径
	 */
	public void setVoice(Voice voice, String path) {
		ContentValues values = new ContentValues();
		Uri newUri = null;
		Uri uri = MediaStore.Audio.Media.getContentUriForPath(path);
		// 查询音乐文件是否存在
		Cursor cursor = mContext.getContentResolver().query(uri, null,
				MediaStore.MediaColumns.DATA + "=?", new String[] { path },
				null);
		if (cursor.moveToFirst() && cursor.getCount() > 0) {
			String _id = cursor.getString(0);
			switch (voice) {
			case RINGTONES:
				values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
//				values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
//				values.put(MediaStore.Audio.Media.IS_ALARM, false);
//				values.put(MediaStore.Audio.Media.IS_MUSIC, true);
				break;
			case NOTIFICATION:
//				values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
				values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
//				values.put(MediaStore.Audio.Media.IS_ALARM, false);
//				values.put(MediaStore.Audio.Media.IS_MUSIC, true);
				break;
			case ALARM:
//				values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
//				values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
				values.put(MediaStore.Audio.Media.IS_ALARM, true);
//				values.put(MediaStore.Audio.Media.IS_MUSIC, true);
				break;
			case ALL:
				values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
				values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
				values.put(MediaStore.Audio.Media.IS_ALARM, true);
				values.put(MediaStore.Audio.Media.IS_MUSIC, true);
				break;
			}
			mContext.getContentResolver().update(uri, values,
					MediaStore.MediaColumns.DATA + "=?", new String[] { path });
			newUri = ContentUris.withAppendedId(uri, Long.valueOf(_id));
			//修改settings默认值
			switch (voice) {
			case RINGTONES:
				RingtoneManager.setActualDefaultRingtoneUri(mContext,
						RingtoneManager.TYPE_RINGTONE, newUri);
				break;
			case NOTIFICATION:
				RingtoneManager.setActualDefaultRingtoneUri(mContext,
						RingtoneManager.TYPE_NOTIFICATION, newUri);
				break;
			case ALARM:
				RingtoneManager.setActualDefaultRingtoneUri(mContext,
						RingtoneManager.TYPE_ALARM, newUri);
				break;
			case ALL:
				RingtoneManager.setActualDefaultRingtoneUri(mContext,
						RingtoneManager.TYPE_ALL, newUri);
				break;
			}
		}
		cursor.close();
		cursor = null;
	}
	/***************/
	
	/**
	 * 查询所有图片
	 * @return ArrayList<FileBean>
	 */
	public ArrayList<FileBean> queryAllImage(){
		ArrayList<FileBean> listImage = new ArrayList<FileBean>();
		if (!StorageUtils.isSDCardPresent()) {
			return listImage;
		}
		Cursor cursor = NativeResourceDBHelper.getInstance(mContext).query(
				Category.ALBUM, null, SortMethod.ALBUM);
		int _id = NativeResourceConstant.COLUMN_ID;
		int _path = NativeResourceConstant.COLUMN_PATH;
		FileBean fileBean = null;
		if (cursor != null && cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(_id);
				String path = cursor.getString(_path);
				fileBean = new FileBean();
				fileBean.setDbId(id);
				fileBean.setFilePath(path);
				listImage.add(fileBean);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return listImage;
	}
	
	/**
	 * 查询图片各文件夹总数，相册分类
	 * @return ArrayList<AlbumBean>
	 */
	public ArrayList<AlbumBean> queryAlbum() {
		ArrayList<AlbumBean> listImage = new ArrayList<AlbumBean>();
		if (!StorageUtils.isSDCardPresent()) {
			return listImage;
		}
		Cursor cursor = NativeResourceDBHelper.getInstance(mContext).query(
				Category.ALBUM, null, SortMethod.ALBUM);
		int _id = NativeResourceConstant.COLUMN_ID;
		int _path = NativeResourceConstant.COLUMN_PATH;
		String _album = MediaStore.Images.Media.BUCKET_DISPLAY_NAME;
		HashMap<String, AlbumBean> myhash = new HashMap<String, AlbumBean>();
		AlbumBean albumBean = null;
		FileBean fileBean = null;
		if (cursor != null && cursor.moveToFirst()) {
			do {
				int index = 0;
				int id = cursor.getInt(_id);
				String path = cursor.getString(_path);
				//检查文件
				if(! new File(path).exists()){
					continue;
				}
				String album = cursor.getString(cursor.getColumnIndex(_album));
				List<FileBean> stringList = new ArrayList<FileBean>();
				fileBean = new FileBean();
				if (myhash.containsKey(album)) {
					albumBean = myhash.remove(album);
					if (listImage.contains(albumBean))
						index = listImage.indexOf(albumBean);
					fileBean.setDbId(id);
					fileBean.setFilePath(path);
					albumBean.getList().add(fileBean);
					listImage.set(index, albumBean);
					myhash.put(album, albumBean);
				} else {
					albumBean = new AlbumBean();
					stringList.clear();
					fileBean.setDbId(id);
					fileBean.setFilePath(path);
					stringList.add(fileBean);
					albumBean.setImage_id(id);
					albumBean.setPath_top_file(path);
					albumBean.setName_album(album);
					albumBean.setList(stringList);
					listImage.add(albumBean);
					myhash.put(album, albumBean);
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		// for (int i = 0; i < listImage.size(); i++) {
		// AlbumBean albumBean = listImage.get(i);
		// Log.i("Simple", "--------文件夹：" + albumBean.getName_album()
		// + ",,封面path=" + albumBean.getPath_top_file()
		// + ",,图片个数=" + albumBean.getList().size());
		// List<FileBean> pictureBean = albumBean.getList();
		// for (int j = 0; j < pictureBean.size(); j++) {
		// Log.i("Simple", "--------图片id："
		// + pictureBean.get(j).getDbId()
		// + ",,图片path="
		// + pictureBean.get(j).getFilePath());
		// }
		// }
		return listImage;
	}
	
	/**
	 * 查询文档资源
	 * @return
	 */
	public ArrayList<FileBean> queryDocment(){
		ArrayList<FileBean> documentList = new ArrayList<FileBean>();
		if (!StorageUtils.isSDCardPresent()) {
			return documentList;
		}
		if(Build.VERSION.SDK_INT >= 11){
			Cursor cursor = NativeResourceDBHelper.getInstance(mContext).query(Category.DOC, 
						null, SortMethod.NAME);
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					FileBean fileInfo = Utils.GetFileInfo(Category.DOC, 
							cursor.getString(NativeResourceConstant.COLUMN_PATH), false);
					if(fileInfo == null){//数据库有数据，本地文件不存在
						NativeResourceDBHelper.getInstance(mContext).notifyFileSystemChanged(
								Category.DOC, mContext, cursor.getLong(NativeResourceConstant.COLUMN_ID));
					}else{
						fileInfo.setDbId(cursor.getLong(NativeResourceConstant.COLUMN_ID));
						documentList.add(fileInfo);
					}
				}
			}else{
				cursor.close();
				return documentList;
			}
			cursor.close();
			cursor = null;
		}else{
			if(oldList.size() == 0){
				getAllFiles(Environment.getExternalStorageDirectory());
			}
			documentList.addAll(oldList);
//			setCategoryInfo(Category.DOC, documentList.size(), documentList.size());
			message.onMessage(true);
		}
		return documentList;
	}
	
	/**
	 * 查询视频资源
	 * @return
	 */
	public ArrayList<FileBean> queryVideo(){
		ArrayList<FileBean> videoList = new ArrayList<FileBean>();
		if (!StorageUtils.isSDCardPresent()) {
			return videoList;
		}
		Cursor cursor = NativeResourceDBHelper.getInstance(mContext).query(Category.VIDEO, 
				null, SortMethod.NAME);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				String path = cursor.getString(NativeResourceConstant.COLUMN_PATH);
				if(path.indexOf("/.") == -1){//过滤隐藏文件夹
					FileBean fileInfo = Utils.GetFileInfo(Category.VIDEO,
							path, true);
					if(fileInfo == null){//数据库有数据，本地文件不存在
						NativeResourceDBHelper.getInstance(mContext).notifyFileSystemChanged(
								Category.VIDEO, mContext, cursor.getLong(NativeResourceConstant.COLUMN_ID));
					}else{
						fileInfo.setDbId(cursor.getLong(NativeResourceConstant.COLUMN_ID));
						videoList.add(fileInfo);
					}
				}
			}
		}
		cursor.close();
		cursor = null;
		return videoList;
	}
	
	/**
	 * 查询视频缩略图
	 * 
	 * @param path
	 * @return Bitmap
	 */
	public Bitmap queryVideoThumbnails(Context con, String path){
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, Images.Thumbnails.MINI_KIND);
        if(bitmap == null)
        	return null;
        return bitmap; 
	}
	
	/**
	* @Title: deleteAll 
	* @Description: 批量删除
	* @return void 
	 */
	public void deleteAll(final Context context, final ArrayList<FileBean> fileList, 
			final onRefreshMessage message, final Category category){
		ThreadUtil.start(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < fileList.size(); i++) {
					Utils.deleteFile(new File(fileList.get(i).getFilePath()));
					final int result = NativeResourceDBHelper.getInstance(mContext).notifyFileSystemChanged(
							category, context, fileList.get(i).getDbId());
					if(result != 1){
						NativeResourceDBHelper.getInstance(mContext).
										notifyFileSystemChanged(fileList.get(i).getFilePath());
					}
				}

				//更新
				((Activity) context).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						message.onMessage(true);
					}
				});
			}
		});
	}
	
	/*
	 * 获取SDCard上所有文本文件，兼容2.3
	 */
	public void getAllFiles(File file) {
		if (file.exists()) {
			addoldList(file.listFiles(new FileFilters(DOCUMENTS[0])));
			addoldList(file.listFiles(new FileFilters(DOCUMENTS[1])));
			addoldList(file.listFiles(new FileFilters(DOCUMENTS[2])));
			addoldList(file.listFiles(new FileFilters(DOCUMENTS[3])));
			addoldList(file.listFiles(new FileFilters(DOCUMENTS[4])));
			addoldList(file.listFiles(new FileFilters(DOCUMENTS[5])));
			if (file.isDirectory()) {
				File files[] = file.listFiles(); 
				if(files != null){
					for (int i = 0; i < files.length; i++) { 
						getAllFiles(files[i]);
					}	
				}
			}
		}
	}
	
	private void addoldList(File[] files){
		if(files != null){
			for (File f : files) {
				if(f.length() > 10){
					FileBean fileInfo = Utils.GetFileInfo(Category.DOC, 
							f.getAbsolutePath(), false);
					if(fileInfo == null){
						//..过滤
					}else{
						fileInfo.setDbId(0); // null
						oldList.add(fileInfo);
					}
				}
			}  
		} 
	}
	
	/*
	 * 过滤文件
	 */
	public class FileFilters implements FilenameFilter{
		String extension = ".";
		public FileFilters(String fileExtensionNoDot) {
			extension += fileExtensionNoDot;
		}
		
		@Override
		public boolean accept(File dir, String filename) {
			if (dir.isDirectory() && dir.isHidden()) { // 忽略隐藏文件夹
                return false;
            }
            if (dir.isFile() && dir.isHidden()) {// 忽略隐藏文件
                return false;
            }
			return filename.endsWith(extension);
		}
	}
	
	/*************
	 * 普通消息回调
	 * 
	 */
	public interface onRefreshMessage {
		public void onMessage(boolean result);
	}
}
