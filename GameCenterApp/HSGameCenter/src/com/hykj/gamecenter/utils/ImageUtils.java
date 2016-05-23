
package com.hykj.gamecenter.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.hykj.gamecenter.utilscs.LogUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    public static Bitmap getRoundBitmap(Drawable drawable) {
        return makeRoundIcon(drawableToBitmap(drawable));
    }

    // 对详情中的应用截图，缩放到固定尺寸
    // 如果是横图：则缩放为固定225dp的高度
    // 如果是竖图：则缩放为固定245dp的宽度
    public static Bitmap getDetailGalleryBitmap(Bitmap bitmap, int resizeHeight, int resizeWidth) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        Matrix matrix = new Matrix();
        float scale = 1f;
        // 横屏截图
        if (width > height) {
            scale = (float) resizeHeight / (float) height;
        } else {
            scale = (float) resizeWidth / (float) width;
        }
        matrix.setScale(scale, scale);
        Bitmap bMap = null;
        try {
            LogUtils.e("创建BitMap开始!");
            bMap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        } catch (OutOfMemoryError e) {
            if (null != bitmap && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
            throw new RuntimeException("创建BitMap时出现异常!");
        }
        if (null != bitmap && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }

        return bMap;
    }

    public static Bitmap getGalleryBitmap(Bitmap bitmap, int resizePortWidth, int resizeLanWidth) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        Matrix matrix = new Matrix();
        float scale = 1f;
        // 横屏截图
        if (width > height) {
            scale = (float) resizeLanWidth / (float) width;
        } else {
            scale = (float) resizePortWidth / (float) width;
        }
        matrix.setScale(scale, scale);
        Bitmap bMap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        if (null != bitmap && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return bMap;
    }

    /**
     * 这个方法不建议使用，OOM非常难处理
     *
     * @param bitmap
     * @param resizeHeight
     * @return
     */
    public static Bitmap getGalleryBitmap(Bitmap bitmap, int resizeHeight) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        //        Logger.i("tom", "resizeHeight=====" + resizeHeight);
        //        Logger.i("tom", "height=====" + height + "   width=====" + width);
        Matrix matrix = new Matrix();
        float scale = 1f;
        scale = (float) resizeHeight / (float) height;
        matrix.postScale(scale, scale);
        try {
            Bitmap bMap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            if (null != bitmap && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
            return bMap;
        } catch (OutOfMemoryError e) {
            if (null != bitmap && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
            e.printStackTrace();
            return null;
        }
    }

    /*public static float floatToInt(Float scale) {
        int a = (int) (scale * 1000);
        if (a % 10 > 0)
            scale = (a - a % 10 + 10 * 1.0f) / 1000.0f;
        else {
            scale = a * 1.0f / 1000.0f;
        }
        return scale;
    }*/

    public static Bitmap safeDecodeStream(String pathName, int resizeHeight) {
        Logger.i("tom", "path====" + pathName);
        float scale = 1.00f;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        int height = options.outHeight;
        int width = options.outWidth;
        Logger.i("tom",
                "bMap outHeight=====" + options.outHeight + "   bMap outWidth====="
                        + options.outWidth);

        scale = resizeHeight * 1.0f / height;
        Logger.i("tom", "scale=====" + scale);
        //        options.inSampleSize = 2;
        Logger.i("tom", "scale=====" + options.inSampleSize);
        options.inDither = false; /*不进行图片抖动处理*/
        options.inPreferredConfig = null; /*设置让解码器以最佳方式解码*/
        /* 下面两个字段需要组合使用 */

        options.outWidth = (int) (width * scale);
        options.outHeight = resizeHeight;
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        Logger.i("tom",
                "bMap outHeight=====" + options.outHeight + "   bMap outWidth====="
                        + options.outWidth);
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
            Logger.i(
                    "tom", "    bitmap.w = " + bitmap.getWidth()
                            + "    bitmap.h = "
                            + bitmap.getHeight());
            return bitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
        //        Logger.i(
        //                "tom",
        //                "bMap height=====" + nBitmap.getHeight() + "   bMap width====="
        //                        + nBitmap.getWidth());
        /*Matrix matrix = new Matrix( );
        float scale = 1f;
        scale = (float)resizeHeight / (float)height;
        matrix.setScale( scale , scale );
        Bitmap bMap = Bitmap.createBitmap( bitmap , 0 , 0 , width , height , matrix , true );
        if( null != bitmap && bitmap.isRecycled( ) )
        {
            bitmap.recycle( );
            bitmap = null;
        }*/
        //        return nBitmap;
    }

    /**
     * 将Bitmap转换成InputStream
     */
    public static InputStream Bitmap2InputStream(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    public static Bitmap safeDecodeStream(Bitmap bitmap, int resizeHeight) {
        float scale = 1.00f;
        InputStream is = Bitmap2InputStream(bitmap, 100);
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100]; //buff用于存放循环读取的临时数据 
        int rc = 0;
        try {
            while ((rc = is.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeStream(is, null, options);

        int height = options.outHeight;
        int width = options.outWidth;
        Logger.i("tom",
                "bMap outHeight=====" + options.outHeight + "   bMap outWidth====="
                        + options.outWidth);

        scale = resizeHeight * 1.0f / height;
        Logger.i("tom", "scale=====" + scale);
        options.inSampleSize = (int) scale;
        options.inDither = false; /*不进行图片抖动处理*/
        options.inPreferredConfig = null; /*设置让解码器以最佳方式解码*/
        /* 下面两个字段需要组合使用 */

        options.outWidth = (int) (width * scale);
        options.outHeight = resizeHeight;
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        Logger.i("tom",
                "bMap outHeight=====" + options.outHeight + "   bMap outWidth====="
                        + options.outWidth);
        try {
            bmp = BitmapFactory.decodeByteArray(swapStream.toByteArray(), 0,
                    swapStream.toByteArray().length, options);
            Logger.i(
                    "tom", "    bitmap.w = " + bitmap.getWidth()
                            + "    bitmap.h = "
                            + bitmap.getHeight());
            return bmp;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null || drawable.getIntrinsicWidth() == 0) {
            return null;
        }
        Bitmap bitmap = Bitmap
                .createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable
                        .getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private static final float ICON_ROUND = 10.0f;

    private static Bitmap makeRoundIcon(Bitmap bitmap) {

        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        paint.setColor(color);

        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas2 = new Canvas(output);
        canvas2.drawARGB(0, 0, 0, 0);
        canvas2.drawRoundRect(rectF, ICON_ROUND, ICON_ROUND, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas2.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     *
     */
    public static Bitmap getFileBitmap(File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false; /*不进行图片抖动处理*/
        options.inPreferredConfig = null; /*设置让解码器以最佳方式解码*/
        options.inTempStorage = new byte[12 * 1024];//创建了一个12kb的临时空间
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bmp = null;
        if (fs != null) {
            try {
                bmp = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, options);
                return bmp;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (fs != null) {
                    try {
                        fs.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
