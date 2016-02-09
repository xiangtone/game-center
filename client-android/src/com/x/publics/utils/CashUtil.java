package com.x.publics.utils;

import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CashUtil {
	private static final String TAG = "CashUtil";

	/**
	 * 获取已序列化对象
	 * @param <T>
	 * @param filename 保存序列化对象的文件名
	 * @param context
	 * @return
	 */
	public static <T extends Serializable> T readCash(String filename, Context context) {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(context.openFileInput(filename));
			return (T) in.readObject();
		} catch (FileNotFoundException fnfe) {
			Log.e(TAG, "" + fnfe);
			try {
				in = new ObjectInputStream(context.getAssets().open(filename, Context.MODE_PRIVATE));
				return (T) in.readObject();
			} catch (Exception e) {
				Log.e(TAG, "" + e);
			}
		} catch (IOException e) {
			Log.e(TAG, "" + e.getStackTrace());
		} catch (ClassNotFoundException e) {
			Log.e(TAG, "" + e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				Log.e(TAG, "" + e);
			}
		}
		return null;

	}

	/**
	 * 序列化对象到程序安装目录下
	 * @param <T>
	 * @param t        被序列化数据，需要实现可序列化接口
	 * @param filename 保存序列化数据的文件名，可以带后缀
	 * @param context  
	 */
	public static <T extends Serializable> void saveCash(T t, String filename, Context context) {
		ObjectOutputStream os = null;
		try {
			os = new ObjectOutputStream(context.openFileOutput(filename, Context.MODE_PRIVATE));
			os.writeObject(t);
			os.flush();
		} catch (SecurityException se) {
			Log.e(TAG, "" + se);
		} catch (IOException e) {
			Log.e(TAG, "" + e);
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				Log.e(TAG, "" + e);
			}
		}
	}
}
