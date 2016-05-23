package com.hykj.gamecenter.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDiskIOException;
import android.util.Log;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.data.GroupInfo;
import com.hykj.gamecenter.download.DownloadTask;
import com.hykj.gamecenter.protocol.Reported;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OddsHou on 2015/11/25.
 */
public class DatabaseUtils {


	private static final String TAG = "DatabaseUtils";

	public static GroupInfo getGroupInfo(int groupType) {
		return getGroupinfoByDB(CSACDatabaseHelper.GroupInfoColumns.GROUP_TYPE + "=" + groupType, null);
	}

	public static GroupInfo getGroupinfoByDB(String selection, String[] selectionArgs) {
		Cursor classifyCursor = App
				.getAppContext()
				.getContentResolver()
				.query(CSACContentProvider.GROUPINFO_CONTENT_URI, null,
						selection, selectionArgs, null);
		if (classifyCursor.getCount() == 0) {
			classifyCursor.close();
			return null;
		}
		if (classifyCursor != null && classifyCursor.moveToNext()) {
			GroupInfo groupInfo = new GroupInfo();
			groupInfo.groupId = classifyCursor.getInt(classifyCursor
					.getColumnIndex(CSACDatabaseHelper.GroupInfoColumns.GROUP_ID));
			groupInfo.groupClass = classifyCursor
					.getInt(classifyCursor.getColumnIndex(CSACDatabaseHelper.GroupInfoColumns.GROUP_CLASS));
			groupInfo.groupType = classifyCursor.getInt(classifyCursor
					.getColumnIndex(CSACDatabaseHelper.GroupInfoColumns.GROUP_TYPE));
			groupInfo.orderType = classifyCursor.getInt(classifyCursor
					.getColumnIndex(CSACDatabaseHelper.GroupInfoColumns.ORDER_TYPE));
			groupInfo.orderNo = classifyCursor.getInt(classifyCursor
					.getColumnIndex(CSACDatabaseHelper.GroupInfoColumns.ORDER_NO));
			groupInfo.recommWrod = classifyCursor.getString(classifyCursor
					.getColumnIndex(CSACDatabaseHelper.GroupInfoColumns.RECOMM_WORD));
			groupInfo.groupName = classifyCursor
					.getString(classifyCursor.getColumnIndex(CSACDatabaseHelper.GroupInfoColumns.GROUP_NAME));
			groupInfo.groupDesc = classifyCursor
					.getString(classifyCursor.getColumnIndex(CSACDatabaseHelper.GroupInfoColumns.GROUP_DESC));
			groupInfo.groupPicUrl = classifyCursor.getString(classifyCursor
					.getColumnIndex(CSACDatabaseHelper.GroupInfoColumns.GROUP_PIC_URL));
			groupInfo.startTime = classifyCursor
					.getString(classifyCursor.getColumnIndex(CSACDatabaseHelper.GroupInfoColumns.START_TIME));
			groupInfo.endTime = classifyCursor.getString(classifyCursor
					.getColumnIndex(CSACDatabaseHelper.GroupInfoColumns.END_TIME));

			if (classifyCursor != null) {
				classifyCursor.close();
			}
			return groupInfo;
		}
		return null;
	}

	public static int[] getGroupIdByDB(int groupType, int orderType) {
		int[] ints = new int[4];
		Cursor classifyCursor = App
				.getAppContext()
				.getContentResolver()
				.query(CSACContentProvider.GROUPINFO_CONTENT_URI,
						null,
						CSACDatabaseHelper.GroupInfoColumns.GROUP_TYPE + "=" + groupType + " and "
								+ CSACDatabaseHelper.GroupInfoColumns.ORDER_TYPE + "="
								+ orderType, null, null);
		if (classifyCursor != null && classifyCursor.moveToNext()) {
			int gi = classifyCursor
					.getInt(classifyCursor.getColumnIndex(CSACDatabaseHelper.GroupInfoColumns.GROUP_ID));
			int gc = classifyCursor.getInt(classifyCursor
					.getColumnIndex(CSACDatabaseHelper.GroupInfoColumns.GROUP_CLASS));
			int gt = classifyCursor.getInt(classifyCursor
					.getColumnIndex(CSACDatabaseHelper.GroupInfoColumns.GROUP_TYPE));
			int go = classifyCursor.getInt(classifyCursor
					.getColumnIndex(CSACDatabaseHelper.GroupInfoColumns.ORDER_TYPE));
			ints[0] = gi;
			ints[1] = gc;
			ints[2] = gt;
			ints[3] = go;
			classifyCursor.close();
			return ints;
		}
		if (classifyCursor != null)
			classifyCursor.close();
		return ints;
	}


	public static ArrayList<DownloadTask> getDownloadedDataFromDb() {
		Cursor cursor = null;
		ArrayList<DownloadTask> downloadEdList = new ArrayList<DownloadTask>();
		try {
			cursor = App
					.getAppContext()
					.getContentResolver()
					.query(CSACContentProvider.DOWNLOADEDINFO_CONTENT_URI, null,
							null, null, null);
			if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
				while (!cursor.isAfterLast()) {
					DownloadTask task = new DownloadTask();
					createTaskFromCursor(cursor);
					downloadEdList.add(task);
					cursor.moveToNext();
				}
			}
		} catch (SQLiteDiskIOException e) {
			Log.e(TAG, e.toString());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return downloadEdList;
	}

	public static DownloadTask createTaskFromCursor(Cursor cursor) {
		DownloadTask task = new DownloadTask();

		task.appId = cursor.getInt(1);
		task.fileSavePath = cursor.getString(2);
		task.appDownloadURL = cursor.getString(3);
		task.packageName = cursor.getString(4);
		task.appName = cursor.getString(5);
		// info.rating = cursor.getInt( 6 );
		task.fileLength = cursor.getInt(7);
		task.progress = cursor.getInt(8);
		task.appIconURL = cursor.getString(9);
		task.packMD5 = cursor.getString(10);

		int nState = cursor.getInt(11);
		DownloadTask.TaskState enState = DownloadTask.TaskState.valueOf(nState);

		switch (enState) {
			case PREPARING:
			case WAITING:
			case LOADING:
			case STARTED:
				enState = DownloadTask.TaskState.STOPPED;
				break;
			case INSTALLING:
				enState = DownloadTask.TaskState.SUCCEEDED;
				break;
		}
		task.setState(enState);
		task.nFromPos = cursor.getInt(12);
		return task;
	}

	public static ContentValues createContentValues(DownloadTask dinfo) {
		ContentValues values = new ContentValues();
		values.put(CSACDatabaseHelper.DownloadInfoColumns.APP_ID, dinfo.appId);
		values.put(CSACDatabaseHelper.DownloadInfoColumns.LOCAL_PATH, dinfo.getFileSavePath());
		values.put(CSACDatabaseHelper.DownloadInfoColumns.APP_URL, dinfo.getDownloadUrl());
		values.put(CSACDatabaseHelper.DownloadInfoColumns.PACAKGE_NAME, dinfo.packageName);
		values.put(CSACDatabaseHelper.DownloadInfoColumns.APP_NAME, dinfo.appName);
		values.put(CSACDatabaseHelper.DownloadInfoColumns.APP_RATING, 0);
		values.put(CSACDatabaseHelper.DownloadInfoColumns.TOTAL_SIZE, dinfo.fileLength);
		values.put(CSACDatabaseHelper.DownloadInfoColumns.DOWNLOAD_SIZE, dinfo.progress);
		values.put(CSACDatabaseHelper.DownloadInfoColumns.ICON_URL, dinfo.appIconURL);
		values.put(CSACDatabaseHelper.DownloadInfoColumns.PACK_MD5, dinfo.packMD5);
		values.put(CSACDatabaseHelper.DownloadInfoColumns.STATE, dinfo.getState().value());
		values.put(CSACDatabaseHelper.DownloadInfoColumns.MFROMPOS, dinfo.nFromPos);

		return values;
	}

	public static void queryReportData(List<Reported.ReportedInfo> list) {
		Cursor cursor = null;
		try {
			cursor = App
					.getAppContext()
					.getContentResolver()
					.query(CSACContentProvider.REPORT_URI, null,
							null, null, null);
			if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
				while (!cursor.isAfterLast()) {
					Reported.ReportedInfo reportedInfo = new Reported.ReportedInfo();
					reportedInfo.statActId = cursor.getInt(cursor.getColumnIndex(CSACDatabaseHelper.ReportColumns.START_ACTID));
					reportedInfo.statActId2 = cursor.getInt(cursor.getColumnIndex(CSACDatabaseHelper.ReportColumns.START_ACTID2));
					reportedInfo.actionTime = cursor.getString(cursor.getColumnIndex(CSACDatabaseHelper.ReportColumns.ACTION_TIME));
					reportedInfo.ext1 = cursor.getString(cursor.getColumnIndex(CSACDatabaseHelper.ReportColumns.EXT1));
					reportedInfo.ext2 = cursor.getString(cursor.getColumnIndex(CSACDatabaseHelper.ReportColumns.EXT2));
					reportedInfo.ext3 = cursor.getString(cursor.getColumnIndex(CSACDatabaseHelper.ReportColumns.EXT3));
					reportedInfo.ext4 = cursor.getString(cursor.getColumnIndex(CSACDatabaseHelper.ReportColumns.EXT4));
					reportedInfo.ext5 = cursor.getString(cursor.getColumnIndex(CSACDatabaseHelper.ReportColumns.EXT5));
					list.add(reportedInfo);
					cursor.moveToNext();
				}
			}
		} catch (SQLiteDiskIOException e) {
			Log.e(TAG, e.toString());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

	}

	public static void insertReportData(Reported.ReportedInfo reportedInfo) {
		ContentResolver contentResolver = App.getAppContext().getContentResolver();
		ContentValues values = new ContentValues();
		values.put(CSACDatabaseHelper.ReportColumns.START_ACTID, reportedInfo.statActId);
		values.put(CSACDatabaseHelper.ReportColumns.START_ACTID2, reportedInfo.statActId2);
		values.put(CSACDatabaseHelper.ReportColumns.ACTION_TIME, reportedInfo.actionTime);
		values.put(CSACDatabaseHelper.ReportColumns.EXT1, reportedInfo.ext1);
		values.put(CSACDatabaseHelper.ReportColumns.EXT2, reportedInfo.ext2);
		values.put(CSACDatabaseHelper.ReportColumns.EXT3, reportedInfo.ext3);
		values.put(CSACDatabaseHelper.ReportColumns.EXT4, reportedInfo.ext4);
		values.put(CSACDatabaseHelper.ReportColumns.EXT5, reportedInfo.ext5);
		contentResolver.insert(CSACContentProvider.REPORT_URI, values);
	}

	public static void deleteAllReport() {
		ContentResolver contentResolver = App.getAppContext().getContentResolver();
		contentResolver.delete(CSACContentProvider.REPORT_URI, null, null);
	}
}
