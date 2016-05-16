
package com.hykj.gamecenter.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.download.DownloadTask;
import com.hykj.gamecenter.utilscs.LogUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtils {
    /**
     * 程序数据保存的根路径
     */
    public static final String BASE_DIR = "/CS/CSAppStore/";
    /** APK保存路径 */
    public static final String APK_DIR = BASE_DIR + "apk/";
    /** 图片保存路径 */
    public final static String ICON_CACHE_PATH = BASE_DIR + "img/";
    /** debug 图片保存路径 */
    public final static String ICON_CACHE_PATH_DEBUG = BASE_DIR + "imgdebug/";
    /** 闪屏图片保存路径 */
    public final static String SPLASH_IMAGE_PATH = ICON_CACHE_PATH + "SplashImage/";
    /** 自更新包路径 */
    public final static String UPDATE_PATH = BASE_DIR + "update/";
    /** 调试log保存路径 */
    public final static String DEBUG_LOG_PATH = BASE_DIR + "debuglog/";
    private static final String TAG = FileUtils.class.getName();

    /**
     * 获取当前有效的存储路径, 以"/"结尾
     * 
     * @param path
     * @return
     */
    public static String getStorePath(String path) {
        // 获取SdCard状态
        String state = android.os.Environment.getExternalStorageState();
        // 判断SdCard是否存在并且是可用的
        if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
            if (android.os.Environment.getExternalStorageDirectory().canWrite()) {
                File file = new File(android.os.Environment.getExternalStorageDirectory().getPath()
                        + path);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String absolutePath = file.getAbsolutePath();
                if (!absolutePath.endsWith("/")) {// 保证以"/"结尾
                    absolutePath += "/";
                }
                Log.d(TAG, "getStorePath absolutePath=" + absolutePath);
                return absolutePath;
            }
        }
        String absolutePath = App.getAppContext().getFilesDir().getAbsolutePath();
        if (!absolutePath.endsWith("/")) {// 保证以"/"结尾
            absolutePath += "/";
        }
        Log.d(TAG, "getStorePath 2 absolutePath=" + absolutePath);
        return absolutePath;
    }

    /*
     * download fileUrl =
     * http://apps.wandoujia.com/redirect?signature=b468318&url
     * =http%3A%2F%2Fapk.
     * wandoujia.com%2F6%2F7f%2Feb5df5a8050bf04a2b5a6163d10d37f6
     * .apk&pn=com.lingan
     * .yunqi&md5=eb5df5a8050bf04a2b5a6163d10d37f6&apkid=11834275
     * &vc=1&size=8736764&pos=open/allApps&tokenId=niuwan&appType=APP
     */
    /**
     * 从下载链接中提取文件名
     * 
     * @param fileUrl
     * @return
     */
    public synchronized static String decodeUrl2FileName(String fileUrl, String suffix) {
        // 这里如果fileUrl相同，目标文件名称必须相同，为了断点续传
        Log.d(TAG, "download fileUrl = " + fileUrl);
        String fileName = "";

        if (fileUrl != null) {
            int idx = fileUrl.indexOf("?");
            if (idx > 0) {
                fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1, idx);
            }
            else {
                fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            }
        }

        int index = fileName.indexOf(suffix);
        if (index > 0) {
            fileName = fileName.substring(0, index);
        }

        // Log.v(TAG, "fileName:"+fileName);
        // 保证文件名的唯一性
        // fileName = fileName + System.currentTimeMillis();
        // 转换成英文小写
        return fileName.toLowerCase(Locale.getDefault());
    }

    public static String getImageFileNameByUrl(String url) {
        String path = FileUtils.getStorePath(FileUtils.ICON_CACHE_PATH);
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        return path + url.hashCode() + ".cs";
    }
    public static String getImageFileNameByUrlDebug(String url) {
        String path = FileUtils.getStorePath(FileUtils.ICON_CACHE_PATH_DEBUG);
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        return path + url.hashCode() + ".cs";
    }

    public static String getSplashImageFileNameByUrl(String url) {
        return url.hashCode() + ".cs";
    }

    /*
     * 返回存储的image图片path 如果为null，则图片不存在
     */
    public static String getSaveImagePathByUrl(String url) {
        String filePath = getImageFileNameByUrl(url);
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        return filePath;
    }

    public static String saveBitmap(Bitmap bitmap, String url) {
        try {
            String fileName = getImageFileNameByUrl(url);
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

            return fileName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String saveBitmapDebug(Bitmap bitmap, String url) {
        try {
            String fileName = getImageFileNameByUrlDebug(url);
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

            return fileName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // TODO
    // public static void installApk( final DownloadTask dinfo )
    // {
    // if( dinfo == null )
    // {
    // return;
    // }
    // final File f = new File( dinfo.fileSavePath );
    // if( !f.exists( ) )
    // {
    // CSToast.show( App.getAppContext( ) , App.getAppContext( ).getString(
    // R.string.file_not_exist ) );
    // ApkDownloadManager.getInstance( ).onInstallEnd( dinfo );
    // }
    // else
    // {
    // ApkDownloadManager.getInstance( ).onStartInstall( dinfo );
    // SilentInstallThreadTask.postTask( new Runnable( )
    // {
    // @Override
    // public void run()
    // {
    // install( dinfo.tempPath , f , dinfo );
    // }
    // } );
    // }
    //
    // }

    public static int install(String filePath, File f, DownloadTask dInfo) {
        // 安装
        int ret = -1;
        try {
            ret = PackageUtils.install(App.getAppContext(), f.getAbsolutePath(), dInfo);
        } catch (Exception e) {
            e.printStackTrace();
        } /*
          * catch (Exception e) { e.printStackTrace(); }
          */

        // if (ret == PackageUtils.INSTALL_SILENT_SUCCEEDED) {
        // // Toast.makeText(App.getAppContext(),
        // // App.getAppContext().getString(R.string.install_succeed,
        // // dInfo.appName), Toast.LENGTH_SHORT).show();
        // // CSToast.show(App.getAppContext(),
        // // App.getAppContext().getString(R.string.install_succeed,
        // // dInfo.appName));
        // } else {
        // 更改安装成功状态
//        DownloadService.getDownloadManager().onInstallEnd(dInfo);
        // }
        return ret;

    }

    // modify at 20140104
    // 取消包检测
    public static boolean isApkFileBroken(String apkPath, String packMD5) {
        PackageManager pm = App.getAppContext().getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_SIGNATURES);
        if (info == null || info.applicationInfo == null) {
            return true;
        }
        // return !md5CheckFilePass( apkPath , packMD5 );
        return false;
    }

    // md5校验
    public static boolean md5CheckFilePass(String apkPath, String packMD5) {
        MessageDigest messagedigest = null;
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            LogUtils.e("不存在MD5校验方式");
        }
        byte before[] = null;
        byte last[] = null;
        File file = new File(apkPath);
        long length = file.length();
        try {
            byte bytes[] = new byte[1024 * 10];
            RandomAccessFile randomAccessFile = new RandomAccessFile(apkPath, "r");
            randomAccessFile.seek(0);
            if (randomAccessFile.read(bytes) != -1) {
                before = bytes;
            }

            messagedigest.update(before);
            randomAccessFile.seek(length - 1024 * 10);
            if (randomAccessFile.read(bytes) != -1) {
                last = bytes;
            }

            messagedigest.update(last);

            // byte digest[] = messagedigest.digest( );

            // String md5 = MD5FileUtil.bufferToHex( digest );

            // LogUtils.e( "md5=" + md5 );
            LogUtils.e("packMD5=" + packMD5);

            // 测试md5校验时间 如果包大小在100M会内存溢出
            // long begin = System.currentTimeMillis( );
            // // File big = new File( "D:\\wKgBDVNfBpOEWY8BAAAAAHsyibY852.apk"
            // );
            // File big = new File( apkPath );
            // String md5File = MD5FileUtil.getFileMD5String( big );
            // long end = System.currentTimeMillis( );
            // LogUtils.e( "md5File:" + md5File );
            // LogUtils.e( "time:" + ( ( end - begin ) / 1000 ) + "s" );

            // String fileMD5 = MD5FileUtil.getFileMD5String( new File( apkPath
            // ) );
            // LogUtils.e( "fileMD5=" + fileMD5 );

            // if( md5.equalsIgnoreCase( packMD5 ) )
            // {
            // return true;
            // }
            randomAccessFile.close();
        } catch (FileNotFoundException e) {
            LogUtils.e(apkPath + ",此文件不存在");
        } catch (IOException e) {
            LogUtils.e(apkPath + ",IO 异常");
        } catch (NullPointerException e) {
            LogUtils.e("messagedigest is null");
        }

        return false;
    }

    /**
     * 获取可用的手机SD卡或内存的容量大小
     * 
     * @return
     */
    public static long getAvailableStorageSize() {
        String root = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            root = Environment.getExternalStorageDirectory().getPath();
        }
        else {
            root = Environment.getDataDirectory().getPath();
        }
        LogUtils.e("检测的磁盘，root=" + root);
        File base = new File(root);
        StatFs stat = new StatFs(base.getPath());
        long nAvailableCount = stat.getBlockSize() * ((long) stat.getAvailableBlocks() - 4);
        LogUtils.e("磁盘剩余的空间，nAvailableCount=" + nAvailableCount);
        return nAvailableCount;
    }

    /**
     * 在SPLASH_IMAGE_PATH文件夹下创建文件
     */
    public static File creatFile(String path) throws IOException {
        File file = new File(path);
        file.createNewFile();
        return file;
    }

    /**
     * 判断SPLASH_IMAGE_PATH文件夹下的文件夹是否存在
     */
    public static boolean isFileExist(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    /**
     * 将一个InputStream里面的数据写入storage
     */
    public static File write2StorageFromInput(String path, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            file = creatFile(path);
            output = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            while ((input.read(buffer)) != -1) {
                output.write(buffer);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static void makeSplashDir() {
        File directory = new File(FileUtils.getStorePath(SPLASH_IMAGE_PATH));
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    // 调试日志文件创建
    public static String createDebugLogFile() {
        boolean b = false;
        String path = DEBUG_LOG_PATH;

        String storagePath = android.os.Environment.getExternalStorageDirectory().getPath();
        // if( !storagePath.endsWith( "/" ) )
        // {
        // storagePath = storagePath + "/";
        // }
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                .format(new Date());

        File dir = new File(storagePath + path);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = storagePath + path + fileName + ".log";
        LogUtils.e("filePath=" + filePath);
        File logFile = new File(filePath);
        if (!logFile.exists()) {
            try {
                b = logFile.createNewFile();
            } catch (IOException e) {
                LogUtils.e("create file error!");
            }
        }
        else {
            b = true;
        }

        if (b) {
            return filePath;
        }
        else {
            return null;
        }

    }

    public static void writeDebugLog(String debugInfo) {
        final StringBuffer sbf = new StringBuffer("");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String dateTime = sdf.format(new Date());

        sbf.append(dateTime + ": ");
        sbf.append(debugInfo);
        sbf.append("\r\n");

        final String path = App.getDebugLogFilePath();
        if (path != null) {
            final File file = new File(path);
            // LogUtils.e( "debug,file,path=" + path );
            if (file.exists()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileWriter fw;
                        try {
                            // 增量写入
                            fw = new FileWriter(path, true);
                            BufferedWriter bw = new BufferedWriter(fw);
                            bw.write(sbf.toString());
                            bw.close();
                        }
                        catch (IOException e) {
                            LogUtils.e("write file error!");
                        }
                    }
                }).start();
            }
        }
    }

    /** 清空文件内容 */
    public static void emptyFileContent(File file) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(new byte[0]);
        } catch (Exception e) {
            Log.e(TAG, "Can't not empty " + file.getName());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 使用文件通道的方式复制文件
     * 
     * @param s 源文件
     * @param t 复制到的新文件
     */

    public static void fileChannelCopy(File s, File t) {

        FileInputStream fi = null;

        FileOutputStream fo = null;

        FileChannel in = null;

        FileChannel out = null;

        try {

            fi = new FileInputStream(s);

            fo = new FileOutputStream(t);

            in = fi.getChannel();//得到对应的文件通道

            out = fo.getChannel();//得到对应的文件通道

            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (fi != null) {
                    fi.close();
                }

                if (in != null) {
                    in.close();
                }

                if (fo != null) {
                    fo.close();
                }

                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }

    public static boolean createFile(File file) throws IOException {
        if (!file.exists()) {
            makeDir(file.getParentFile());//file.mkdirs( );
        }
        return file.createNewFile();
    }

    /**
     * Enhancement of java.io.File#mkdir() Create the given directory . If the
     * parent folders don't exists, we will create them all.
     * 
     * @see java.io.File#mkdir()
     * @param dir the directory to be created
     */
    public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }
}
