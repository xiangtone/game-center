package com.x.publics.utils;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.x.R;
import com.x.publics.model.FileBean;

/**
 * Intent 工具类
 
 * */
public class IntentUtil {

	public static void openFile(Context con, String path) {
		String type = getMimeType(path);

		if (!TextUtils.isEmpty(type) && !TextUtils.equals(type, "*/*")) {
			/* 设置intent的file与MimeType */
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(path)), type);
			try {
				con.startActivity(intent);
			} catch (Exception e) {
				ToastUtil.show(con, con.getResources().getString(R.string.open_file_toast), 
						Toast.LENGTH_SHORT);
			}
		}
	}
	
	public static void shareFile(Activity activity, ArrayList<FileBean> fileBean){
		ArrayList<Uri> list = new ArrayList<Uri>();
		Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
		for (int i = 0; i < fileBean.size(); i++) {
			String path = fileBean.get(i).getFilePath();
			shareIntent.setType(getMimeType(path));
			Uri uri = Uri.fromFile(new File(path));
			list.add(uri);
		}
//		shareIntent.setType("*/*");
		shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, list);
		shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(Intent.createChooser(shareIntent, activity.getTitle()));	
	}
	
	public static void shareFile(Activity activity, String path){
		Intent shareIntent = null;
		shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType(getMimeType(path));
		shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
		shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(Intent.createChooser(shareIntent, activity.getTitle()));	
	}

	private static String getMimeType(String filePath) {
		int dotPosition = filePath.lastIndexOf('.');
		if (dotPosition == -1)
			return "*/*";

		String ext = filePath.substring(dotPosition + 1, filePath.length())
				.toLowerCase();
		String mimeType = MimeUtils.guessMimeTypeFromExtension(ext);
		if (ext.equals("mtz")) {
			mimeType = "application/miui-mtz";
		}
		return mimeType != null ? mimeType : "*/*";
	}

}
