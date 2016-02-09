package com.x.publics.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;

import com.x.business.resource.ResourceManage;
import com.x.publics.model.FileBean;

/**
 * 异步解析本地图片
 * 
 
 *
 */
public class NativeImageLoader {
	private LruCache<String, Bitmap> mMemoryCacheThumb; //缩略图
	private LruCache<String, Bitmap> mMemoryCacheHigh; //原始图片
	private static NativeImageLoader mInstance = new NativeImageLoader();
//	private ExecutorService mImageThreadPool = Executors.newFixedThreadPool(2);
	
	private NativeImageLoader(){
		//获取应用程序的最大内存
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		mMemoryCacheThumb = new LruCache<String, Bitmap>(maxMemory / 8) {
			
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
			}
		};
		
		final int cacheSize = maxMemory / 6;
		mMemoryCacheHigh = new LruCache<String, Bitmap>(cacheSize) {
			
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
			}
		};
	}
	
	/**
	 * Instance 模式
	 * @return
	 */
	public static NativeImageLoader getInstance(){
		return mInstance;
	}
	
	
//	/**
//	 * 加载本地图片，对图片不进行裁剪
//	 * @param path
//	 * @param mCallBack
//	 * @param isHigh
//	 * @return
//	 */
//	public Bitmap loadNativeImage(final String path, final NativeImageCallBack mCallBack, 
//			final boolean isHigh){
//		return this.loadNativeImage(path, null, mCallBack, isHigh);
//	}
	
	/**
	* @Title: loadNativeThumbnails 
	* @Description: 加载本地视频缩略图 
	* @param @param bean
	* @param @param mCallBack
	* @param @param isHigh
	* @param @return 
	* @throws
	 */
	public Bitmap loadNativeThumbnails(final Context con, final FileBean bean, 
			final NativeImageCallBack mCallBack){
		//先获取内存中的Bitmap
		Bitmap bitmap = getBitmapFromMemCache(bean.getFilePath(), true);
		final Handler mHander = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(msg.obj != null){
					mCallBack.onImageLoader((Bitmap)msg.obj, bean.getFilePath());
				}else{
					//限制次数。
//					loadNativeImage(path, mPoint, mCallBack, isHigh); //重新load
				}
			}
		};
		if(bitmap == null){
			ThreadUtil.start(new Runnable() {
				
				@Override
				public void run() {
					//获取视频缩略图
					Bitmap mBitmap =  ResourceManage.getInstance(con).
							queryVideoThumbnails(con, bean.getFilePath());
					if(mBitmap != null){
						Message msg = mHander.obtainMessage();
						msg.obj = mBitmap;
						mHander.sendMessage(msg);
						//将图片加入到内存缓存
						addBitmapToMemoryCache(bean.getFilePath(), mBitmap, true);
					} else {
						Message msg = mHander.obtainMessage();
						mHander.sendMessage(msg);
					}
				}
			});
		}
		return bitmap;
	}
	
	/**
	 * 加载本地图片
	 * @param path
	 * @param mPoint  封装ImageView的宽和高
	 * @param mCallBack
	 * @param isHigh : 是否原始图片
	 * @return
	 */
	public Bitmap loadNativeImage(final String path,final Activity con, 
			final NativeImageCallBack mCallBack, final boolean isHigh){
		//先获取内存中的Bitmap
		Bitmap bitmap = getBitmapFromMemCache(path, isHigh);
		
		final Handler mHander = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(msg.obj != null){
					mCallBack.onImageLoader((Bitmap)msg.obj, path);
				}else{
					evictCacheHigh();
					System.gc();
					//限制次数。
//					loadNativeImage(path, con, mCallBack, isHigh);
				}
			}
		};
		
		//获取分辨率
		final int mWidth = con.getWindowManager().getDefaultDisplay().getWidth();
		final int mHeight = con.getWindowManager().getDefaultDisplay().getHeight();
		
		//若该Bitmap不在内存缓存中，则启用线程去加载本地的图片，并将Bitmap加入到mMemoryCache中
		if(bitmap == null){
			ThreadUtil.start(new Runnable() {
				
				@Override
				public void run() {
					//先获取图片的缩略图
					Bitmap mBitmap = decodeThumbBitmapForFile(path, mWidth, mHeight);
					if(mBitmap != null){
						Message msg = mHander.obtainMessage();
						msg.obj = mBitmap;
						mHander.sendMessage(msg);
						
						//将图片加入到内存缓存
						addBitmapToMemoryCache(path, mBitmap, isHigh);
					} else {
						Message msg = mHander.obtainMessage();
						mHander.sendMessage(msg);
						return;
					}
				}
			});
		}
		return bitmap;
	}

	/**
	 * 往内存缓存中添加Bitmap
	 * 
	 * @param key
	 * @param bitmap
	 */
	private void addBitmapToMemoryCache(String key, Bitmap bitmap, boolean isHigh) {
		if (getBitmapFromMemCache(key, isHigh) == null && bitmap != null) {
			if(isHigh)
				mMemoryCacheHigh.put(key, bitmap);
			else
				mMemoryCacheThumb.put(key, bitmap);
		}
	}

	/**
	 * 根据key来获取内存中的图片
	 * @param key
	 * @return
	 */
	private Bitmap getBitmapFromMemCache(String key, boolean isHigh) {
		if(isHigh)
			return mMemoryCacheHigh.get(key);
		return mMemoryCacheThumb.get(key);
	}
	
	
	/**
	 * 根据View(主要是ImageView)的宽和高来获取图片的缩略图
	 * @param path
	 * @param viewWidth
	 * @param viewHeight
	 * @return
	 */
	private Bitmap decodeThumbBitmapForFile(String path, int viewWidth, int viewHeight){
		Bitmap bitmap = null;
		FileInputStream fs=null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither=false;                     
		options.inPurgeable=true;                 
		options.inInputShareable=true;             
		options.inTempStorage=new byte[32 * 1024];
	    try {
	    	fs = new FileInputStream(new File(path));
	        if(fs!=null){
	    		options.inJustDecodeBounds = true;
	    		BitmapFactory.decodeFileDescriptor(fs.getFD(), null, options);
	    		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	    		//设置缩放比例
//	    		options.inSampleSize = computeScale(options, viewWidth, viewHeight);
	    		options.inSampleSize = computeSampleSize(options, -1, viewWidth * viewHeight);
	    		options.inJustDecodeBounds = false;
	    		
	        	bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, options);
	    		int digree = getOrentation(path);
	    		if (digree != 0) {  
	                // 旋转图片  
	                Matrix m = new Matrix();  
	                m.postRotate(digree);  
	                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), 
	                		bitmap.getHeight(), m, false); 
    	            return bitmap;
	            } 
	        }
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	    	bitmap = null;
	    } catch (OutOfMemoryError e) { //OOM
	    	bitmap = null;
	    	System.gc();
	    } finally{ 
	        if(fs!=null) {
	            try {
	                fs.close();
	            } catch (IOException e) {}
	        }
	    }
	    return bitmap;
	}
	
	/**
	 * 检测图片方向 
	 */
	private int getOrentation(String path){
		int digree = 0;  
        ExifInterface exif = null;  
        try {  
            exif = new ExifInterface(path);  
        } catch (IOException e) {  
            e.printStackTrace();  
            exif = null;  
        }  
        if (exif != null) {  
            // 读取图片中相机方向信息  
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,  
                    ExifInterface.ORIENTATION_UNDEFINED);  
            // 计算旋转角度  
            switch (ori) {  
            case ExifInterface.ORIENTATION_ROTATE_90:  
                digree = 90;  
                break;  
            case ExifInterface.ORIENTATION_ROTATE_180:  
                digree = 180;  
                break;  
            case ExifInterface.ORIENTATION_ROTATE_270:  
                digree = 270;  
                break;  
            default:  
                digree = 0;  
                break;  
            }  
        }  
        return digree;
	}
	
	/**
	 * 根据像素点动态计算Bitmap缩放比例
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 */
	private int computeSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {
	    int initialSize = computeInitialSampleSize(options, minSideLength,
	            maxNumOfPixels);

	    int roundedSize;
	    if (initialSize <= 8) {
	        roundedSize = 1;
	        while (roundedSize < initialSize) {
	            roundedSize <<= 1;
	        }
	    } else {
	        roundedSize = (initialSize + 7) / 8 * 8;
	    }
	    return roundedSize;
	}
	
	private int computeInitialSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {
	    double w = options.outWidth;
	    double h = options.outHeight;

	    int lowerBound = (maxNumOfPixels == -1) ? 1 :
	            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
	    int upperBound = (minSideLength == -1) ? 128 :
	            (int) Math.min(Math.floor(w / minSideLength),
	            Math.floor(h / minSideLength));

	    if (upperBound < lowerBound) {
	        // return the larger one when there is no overlapping zone.
	        return lowerBound;
	    }

	    if ((maxNumOfPixels == -1) &&
	            (minSideLength == -1)) {
	        return 1;
	    } else if (minSideLength == -1) {
	        return lowerBound;
	    } else {
	        return upperBound;
	    }
	} 
	
	/**
	 * (弃用)
	 * 根据View(主要是ImageView)的宽和高来计算Bitmap缩放比例。默认不缩放
	 * @param options
	 * @param width
	 * @param height
	 */
	@Deprecated
	private int computeScale(BitmapFactory.Options options, int viewWidth, int viewHeight){
		int inSampleSize = 1; 
		if(viewWidth == 0 || viewWidth == 0){
			return inSampleSize;
		}
		int bitmapWidth = options.outWidth;
		int bitmapHeight = options.outHeight;
		//假如Bitmap的宽度或高度大于我们设定图片的View的宽高，则计算缩放比例
		if(bitmapWidth > viewWidth || bitmapHeight > viewWidth){
			int widthScale = Math.round((float) bitmapWidth / (float) viewWidth);
			int heightScale = Math.round((float) bitmapHeight / (float) viewWidth);
			inSampleSize = widthScale < heightScale ? widthScale : heightScale;
		}
		return inSampleSize;
	}
	
	/**
	 * 清除原始图片缓存
	 */
	public void evictCacheHigh(){
		mMemoryCacheHigh.evictAll();
		mMemoryCacheThumb.evictAll();
	} 
	
	/**
	 * 加载本地图片的回调接口
	 * 
	 
	 *
	 */
	public interface NativeImageCallBack{
		public void onImageLoader(Bitmap bitmap, String path);
	}
}
