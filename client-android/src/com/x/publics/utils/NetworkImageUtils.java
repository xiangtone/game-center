/**   
* @Title: ImageUtils.java
* @Package com.mas.amineappstore.util
* @Description: TODO 

* @date 2014-1-13 下午02:58:43
* @version V1.0   
*/

package com.x.publics.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

/**
* @ClassName: ImageUtils
* @Description: TODO 

* @date 2014-1-13 下午02:58:43
* 
*/

public class NetworkImageUtils {
	static DisplayImageOptions options;

	public NetworkImageUtils() {
	}

	public static void load(Context context, ImageType imageType, String path, int placeholderResId, int errorResId,
			ImageView target) {
		options = new DisplayImageOptions.Builder().showImageOnLoading(placeholderResId)
				.bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.cacheInMemory(true).showImageForEmptyUri(placeholderResId).showImageOnFail(errorResId)
				.cacheOnDisc(true).considerExifParams(false).displayer(new SimpleBitmapDisplayer()).build();
		if (imageType == ImageType.APK || imageType == ImageType.APP || imageType == ImageType.LOCALPIC) {
			if (imageType == ImageType.LOCALPIC) {
				path = Scheme.FILE.wrap(path);
			}
			ImageLoader.getInstance().displayImage(context, true, imageType, path, target, options, null);
		} else {
			boolean toLoad = !Utils.getSettingModel(context).isGprsSavingMode()
					|| NetworkUtils.getNetworkInfo(context).equals(NetworkUtils.NETWORK_TYPE_WIFI);
			ImageLoader.getInstance().displayImage(context, toLoad, imageType, path, target, options, null);
		}
	}

	public static void cancleDisplay(ImageView iamgeView, Context context) {
		ImageLoader.getInstance().cancelDisplayTask(iamgeView);
	}

	public static void pauseDisplay() {
		ImageLoader.getInstance().pause();
	}

	public static void resumeDisplay() {
		ImageLoader.getInstance().resume();
	}

	public static void recycle(ImageView imageview) {/*
														if (imageview != null) {
														try {
														Bitmap bitmap = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
														if (!bitmap.isRecycled()) {
														LogUtil.getLogger().d(bitmap.toString() + " bitmap recycle ");
														bitmap.recycle();
														bitmap = null;
														System.gc();
														}
														} catch (Exception e) {
														e.printStackTrace();
														}
														}

														*/
	}
}
