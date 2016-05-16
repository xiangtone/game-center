
package com.hykj.gamecenter.logic;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.AppManageActivity;
import com.hykj.gamecenter.activity.HomePageActivity;
import com.hykj.gamecenter.download.DownloadTaskManager;
import com.hykj.gamecenter.protocol.Apps.AppInfo;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.UITools;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationCenter
{

    private static NotificationCenter mInstance;

    private static NotificationManager mNotificationManager;
    private static NotificationManager mFirstNotificationManager;

    private static final int PROCESS_INVALIDATE_TIME = 500; // 进度条更新时间间隔500ms

    private static final int UPDATE_NOTIFY_ID = 10001;
    private static final int DOWNLOAD_NOTIFY_ID = 10002;
    private static final int SELF_DOWNLOAD_NOTIFY_ID = 10003;

    private static final String TAG = NotificationManager.class.getName();

    private NotificationCenter()
    {
        mNotificationManager = (NotificationManager) App.getAppContext().getSystemService(
                Context.NOTIFICATION_SERVICE);
    }

    public static NotificationCenter getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new NotificationCenter();
        }
        return mInstance;
    }

    public static boolean mbHomeNotRunFromNotification = false; // 是否是应用主界面没有启动时从通知进入的该界面

    // 是否是应用主界面没有启动时从通知进入的该界面
    public boolean getIsHomeNotRunFromNotification()
    {
        return mbHomeNotRunFromNotification;
    }

    // 是否是应用主界面没有启动时从通知进入的该界面
    public static void setIsHomeNotRunFromNotification(boolean bFromNotification)
    {
        mbHomeNotRunFromNotification = bFromNotification;
    }

    public static void resetIsHomeNotRunFromNotification()
    {
        mbHomeNotRunFromNotification = false;
    }

    public static String KEY_BUTTON_ID = "INTENT_BUTTON_BUTTON_ID";
    public static String BUTTON_ID_ONEKEYUPDATE = "ONEKEY_UPDATE";
    public static String ACTION_NOTIFICATION_MSG = "com.hykj.gamecenter.activity.AppManageActivity.NotificationMessageReceiver";
    public static String ACTION_NOTIFICATION_HOMEKEY_MSG = "com.hykj.gamecenter.activity.AppManageActivity.HomeKeyPressed";

    public static void sendHomeKeyPressedBroadcast()
    {
        Intent intent = new Intent(ACTION_NOTIFICATION_HOMEKEY_MSG);
        App.getAppContext().sendBroadcast(intent);
    }

    // 可更新app通知 
    public void sendUpdateNotificationCustom(ArrayList<AppInfo> updateInfo)
    {
        Intent intent = new Intent(App.getAppContext(), HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 重要
//        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(HomePageActivity.KEY_SELECT_ITEM, HomePageActivity.PAGE_INDEX.INDEX_UPDATE);
        // GOTO_UPDATE 这个标志位为会true 启动AppManageActivity时会查询可更新应用
        intent.putExtra(HomePageActivity.KEY_GOTO_UPDATE, true);

        if (getIsHomeNotRunFromNotification())
        {
            intent.putExtra(AppManageActivity.FROM_NOTIFICATION, true); //这个参数暂时没有用
            // resetIsHomeNotRunFromNotification();
        }

        Logger.d("AppManagerActivity", "GOTO_UPDATE =" + true);
        PendingIntent pendingIntent = PendingIntent.getActivity(App.getAppContext(),
                UPDATE_NOTIFY_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Intent intent = new Intent(App.getAppContext(),
        // HomePageActivity.class);
        // intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP
        // | intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 重要

        /*
         * FLAG_ACTIVITY_CLEAR_TOP: If set, and the activity being launched is
         * already running in the current task, then instead of launching a new
         * instance of that activity, all of the other activities on top of it
         * will be closed and this Intent will be delivered to the (now on top)
         * old activity as a new Intent.
         */

        // GOTO_UPDATE 这个标志位为会true 启动AppManageActivity时会查询可更新应用
        // intent.putExtra(HomePageActivity.FROM_NOTIFICATION, true);
        // PendingIntent pendingIntent = PendingIntent.getActivity(
        // App.getAppContext(), UPDATE_NOTIFY_ID, intent,
        // PendingIntent.FLAG_UPDATE_CURRENT);

        StringBuilder sb = new StringBuilder();
        for (AppInfo ainfo : updateInfo)
        {
            sb.append(ainfo.showName + " ");
        }

        if (mUpdateNotification == null)
        {
            mUpdateNotification = new Notification();
        }
        // 通过RemoteViews 设置notification中View 的属性
        RemoteViews rv = new RemoteViews(App.getAppContext().getPackageName(),
                R.layout.appsupdate_notification);
        rv.setImageViewResource(R.id.image, R.mipmap.icon);
        rv.setTextViewText(R.id.title,
                App.getAppContext().getString(R.string.update_notify_apps, updateInfo.size()));
        rv.setTextViewText(R.id.content, sb.toString());

        mUpdateNotification.icon = R.mipmap.icon;
        mUpdateNotification.contentView = rv;
        mUpdateNotification.contentIntent = pendingIntent;
        mUpdateNotification.when = System.currentTimeMillis();
        mUpdateNotification.flags = Notification.FLAG_AUTO_CANCEL;
        // mUpdateNotification.flags =
        // Notification.FLAG_ONGOING_EVENT;//使Notification常驻在状态栏

        rv.setImageViewResource(R.id.iv_onkeyupdate, R.drawable.btn_green_green_selector);

        // 带按钮的布局相应点击事件在3.0以下版本没有用，所以这边作了系统版本判断，来显示升级按钮
        // 如果版本号低于（3.0），那么不显示按钮
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB)
        {
            rv.setViewVisibility(R.id.iv_onkeyupdate, View.GONE);
        }
        else
        {
            rv.setViewVisibility(R.id.iv_onkeyupdate, View.VISIBLE);
        }
//        // 点击的事件处理
//        Intent buttonIntent = new Intent(ACTION_NOTIFICATION_MSG);
//        /* 上一首按钮 */
//        buttonIntent.putExtra(KEY_BUTTON_ID, BUTTON_ID_ONEKEYUPDATE);
//        // 这里加了广播，所及INTENT的必须用getBroadcast方法
//        PendingIntent intent_prev = PendingIntent.getBroadcast(App.getAppContext(), 0,
//                buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        rv.setOnClickPendingIntent(R.id.iv_onkeyupdate, intent_prev);
        //点击一键下载进入下载页，并开始一键下载
        Intent intentOneKey = new Intent(intent);
        intentOneKey.putExtra(HomePageActivity.KEY_UPDATE_ALL, true);
        intentOneKey.putExtra(HomePageActivity.KEY_SELECT_ITEM, HomePageActivity.PAGE_INDEX.INDEX_UPDATE);
        PendingIntent pendingIntentOneKey = PendingIntent.getActivity(App.getAppContext(),
                0, intentOneKey, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.iv_onkeyupdate, pendingIntentOneKey);

        mNotificationManager.notify(UPDATE_NOTIFY_ID, mUpdateNotification);
    }

    //    // 可更新app通知
    //    public void sendUpdateNotification(ArrayList<AppInfo> updateInfo)
    //    {
    //
    //        /*
    //         * Intent intent = new Intent(App.getAppContext(),
    //         * AppManageActivity.class);
    //         * 
    //         * // GOTO_UPDATE 这个标志位为会true 启动AppManageActivity时会查询可更新应用
    //         * intent.putExtra(AppManageActivity.GOTO_UPDATE, true);
    //         * Logger.d("AppManagerActivity", "GOTO_UPDATE =" + true); PendingIntent
    //         * pendingIntent = PendingIntent.getActivity( App.getAppContext(),
    //         * UPDATE_NOTIFY_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    //         */
    //
    //        Intent intent = new Intent(App.getAppContext(), HomePageActivity.class);
    //        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);// 重要
    //
    //        /*
    //         * FLAG_ACTIVITY_CLEAR_TOP: If set, and the activity being launched is
    //         * already running in the current task, then instead of launching a new
    //         * instance of that activity, all of the other activities on top of it
    //         * will be closed and this Intent will be delivered to the (now on top)
    //         * old activity as a new Intent.
    //         */
    //
    //        // GOTO_UPDATE 这个标志位为会true 启动AppManageActivity时会查询可更新应用
    //        intent.putExtra(HomePageActivity.FROM_NOTIFICATION, true);
    //        PendingIntent pendingIntent = PendingIntent.getActivity(App.getAppContext(),
    //                UPDATE_NOTIFY_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    //
    //        Notification.Builder nb = new Notification.Builder(App.getAppContext());
    //
    //        StringBuilder sb = new StringBuilder();
    //        for (AppInfo ainfo : updateInfo)
    //        {
    //            sb.append(ainfo.showName + " ");
    //        }
    //        // RemoteViews remoteView = new RemoteViews( App.PackageName( ) ,
    //        // R.layout.notification_remote_view );
    //        // remoteView.setTextViewText( R.id.content_title , App.getAppContext(
    //        // ).getString( R.string.update_notify_apps , updateInfo.size( ) ) );
    //        // remoteView.setTextViewText( R.id.content_text , sb.toString( ) );
    //        // remoteView.setTextViewText( R.id.when , new SimpleDateFormat( "HH:mm"
    //        // ).format( new Date( ) ) );
    //        nb.setContentTitle(App.getAppContext().getString(R.string.update_notify_apps,
    //                updateInfo.size()));
    //        nb.setContentText(sb.toString());
    //        nb.setSmallIcon(R.mipmap.icon);
    //        nb.setWhen(System.currentTimeMillis());
    //        // remoteView.notify( );
    //        // nb.setContent( remoteView );
    //        nb.setContentIntent(pendingIntent);
    //        nb.setOngoing(false);
    //        Notification notification = nb.getNotification();
    //        // notification.flags = Notification.FLAG_AUTO_CANCEL;
    //        mNotificationManager.notify(UPDATE_NOTIFY_ID, notification);
    //    }

    // 首次启动发送的 可更新app通知(不管是否有应用可更新)
    public void sendFirstLaunchUpdateNotification()
    {

        if (!UITools.getInstance().isNetworkAvailable(App.getAppContext()))
        {
            Log.d(TAG, "sendFirstLaunchUpdateNotification 网络不可用");
            return;
        }
        Intent intent = new Intent(App.getAppContext(), HomePageActivity.class);
        intent.putExtra(HomePageActivity.KEY_SELECT_ITEM, HomePageActivity.PAGE_INDEX.INDEX_UPDATE);
        intent.putExtra(HomePageActivity.KEY_GOTO_UPDATE, true);
        //        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 重要
        Logger.d("AppManagerActivity", "GOTO_UPDATE =" + true);
        PendingIntent pendingIntent = PendingIntent.getActivity(App.getAppContext(),
                UPDATE_NOTIFY_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder nb = new Notification.Builder(App.getAppContext());

        // RemoteViews remoteView = new RemoteViews( App.PackageName( ) ,
        // R.layout.notification_remote_view );
        // remoteView.setTextViewText( R.id.content_title , App.getAppContext(
        // ).getString( R.string.update_notify_apps , updateInfo.size( ) ) );
        // remoteView.setTextViewText( R.id.content_text , sb.toString( ) );
        // remoteView.setTextViewText( R.id.when , new SimpleDateFormat( "HH:mm"
        // ).format( new Date( ) ) );
        nb.setContentTitle(App.getAppContext().getString(R.string.first_launch_update_notify_apps));
        nb.setContentText(App.getAppContext().getString(
                R.string.first_launch_update_notify_apps_content));
        Bitmap bmp = BitmapFactory
                .decodeResource(App.getAppContext().getResources(), R.mipmap.icon);
        if (bmp != null)
        {
            nb.setSmallIcon(R.mipmap.icon);
        }
        else
        {
            nb.setLargeIcon(bmp);
        }
        nb.setWhen(System.currentTimeMillis());
        // remoteView.notify( );
        // nb.setContent( remoteView );
        nb.setContentIntent(pendingIntent);
        nb.setOngoing(false);
        Notification notification = nb.getNotification();
        // notification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(UPDATE_NOTIFY_ID, notification);
    }

    private static Timer mNotificationUpdateTimer = null; // 通知栏进度条更新timer
    private static TimerTask mNotificationUpdateTask = null;
    private static Notification mDownloadNotification = null;
    private static Notification mUpdateNotification = null;
    private DownloadTaskManager mDownloadInfoManager;

    // 停止更新下载通知定时器
    public synchronized void stopUpdateDownloadNotification()
    {
        // 停止更新进度条
        if (mNotificationUpdateTimer != null)
        {
            mNotificationUpdateTimer.purge();
            mNotificationUpdateTimer.cancel();
            mNotificationUpdateTask.cancel();
        }
        mNotificationUpdateTask = null;
        mNotificationUpdateTimer = null;
        //        Log.d(TAG, "mNotificationUpdateTask = " + mNotificationUpdateTask
        //                + " mNotificationUpdateTimer = " + mNotificationUpdateTimer);
    }

    // 正在下载通知
    public synchronized void sendDownloadNotification(int count, int progress)
    {
        if (count == 0)
        {
            mNotificationManager.cancel(DOWNLOAD_NOTIFY_ID);
            stopUpdateDownloadNotification();
        }
        else
        {
            Intent intent = new Intent(App.getAppContext(), HomePageActivity.class);
            intent.putExtra(HomePageActivity.KEY_SELECT_ITEM, HomePageActivity.PAGE_INDEX.INDEX_UPDATE);
            PendingIntent pendingIntent = PendingIntent.getActivity(App.getAppContext(), 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            /*
             * Notification.Builder nb = new Notification.Builder(
             * App.getAppContext());
             * nb.setContentTitle(App.getAppContext().getString(
             * R.string.download_notify_apps, count));
             * nb.setContentText(App.getAppContext().getString(
             * R.string.download_notify_apps_hint));
             * nb.setSmallIcon(R.drawable.icon);
             * nb.setWhen(System.currentTimeMillis());
             * nb.setContentIntent(pendingIntent); nb.setOngoing(true);
             */

            if (mDownloadNotification == null)
            {
                mDownloadNotification = new Notification();
            }
            // 通过RemoteViews 设置notification中View 的属性
            RemoteViews rv = new RemoteViews(App.getAppContext().getPackageName(),
                    R.layout.download_notification);
            rv.setProgressBar(R.id.pb, 100, progress, false);
            rv.setTextViewText(R.id.tv, progress + "%");
            rv.setImageViewResource(R.id.image, R.mipmap.icon);
            rv.setTextViewText(R.id.title,
                    App.getAppContext().getString(R.string.download_notify_apps, count));
            rv.setTextViewText(R.id.content,
                    App.getAppContext().getString(R.string.download_notify_apps_hint));

            mDownloadNotification.icon = R.mipmap.icon;
            mDownloadNotification.contentView = rv;
            mDownloadNotification.contentIntent = pendingIntent;
            mDownloadNotification.when = System.currentTimeMillis();
            mDownloadNotification.flags = Notification.FLAG_ONGOING_EVENT;

            mNotificationManager.notify(DOWNLOAD_NOTIFY_ID, mDownloadNotification/*
                                                                                 * nb. getNotification ()
                                                                                 */);

            if (null == mNotificationUpdateTimer)
            {
                mNotificationUpdateTask = new TimerTask()
                {

                    @Override
                    public void run()
                    {
                        // TODO Auto-generated method stub
                        // 更新进度条
                        mDownloadInfoManager = DownloadTaskManager.getInstance();
                        mDownloadInfoManager.sendDownloadUpdateProgressNotification();
                    }
                };
                Log.d(TAG, "sendDownloadNotification schedule mNotificationUpdateTask = "
                        + mNotificationUpdateTask
                        + " mNotificationUpdateTimer = " + mNotificationUpdateTimer);

                mNotificationUpdateTimer = new Timer(true);
                mNotificationUpdateTimer.schedule(mNotificationUpdateTask, 0,
                        PROCESS_INVALIDATE_TIME);
            }

        }
    }

    // 更新总任务下载状态进度条
    public synchronized void UpdateDownloadNotification(int count, int progress)
    {
        /*
         * Log.d(TAG,
         * "Timer UpdateDownload Notification notification.contentView =" +
         * mDownloadNotification.contentView);
         */

        if (0 == count)
        {
            stopUpdateDownloadNotification();
            mNotificationManager.cancel(DOWNLOAD_NOTIFY_ID);
            return;
        }

        if (mDownloadNotification != null && mDownloadNotification.contentView != null)
        {
            RemoteViews rv = mDownloadNotification.contentView;
            rv.setProgressBar(R.id.pb, 100, progress, false);
            rv.setTextViewText(R.id.tv, progress + "%");

            rv.setImageViewResource(R.id.image, R.mipmap.icon);
            rv.setTextViewText(R.id.title,
                    App.getAppContext().getString(R.string.download_notify_apps, count));
            rv.setTextViewText(R.id.content,
                    App.getAppContext().getString(R.string.download_notify_apps_hint));
            mNotificationManager.notify(DOWNLOAD_NOTIFY_ID, mDownloadNotification/*
                                                                                 * nb. getNotification ()
                                                                                 */);
        }
    }

    // 开始自更新下载通知
    public void addSelfDownloadStartNotification()
    {
        Notification.Builder nb = new Notification.Builder(App.getAppContext());
        nb.setContentTitle(App.getAppContext().getString(R.string.update_notify_start));
        nb.setProgress(100, 0, true);
        nb.setSmallIcon(R.mipmap.icon);
        nb.setWhen(System.currentTimeMillis());
        nb.setOngoing(true);

        mNotificationManager.notify(SELF_DOWNLOAD_NOTIFY_ID, nb.getNotification());
    }

    // 自更新下载完成通知
    public void addSelfDownloadFinishNotification(File file)
    {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);

        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        PendingIntent pendingIntent = PendingIntent.getActivity(App.getAppContext(), 0, intent, 0);

        Notification.Builder nb = new Notification.Builder(App.getAppContext());
        nb.setContentTitle(App.getAppContext().getString(R.string.update_notify_end));
        nb.setContentText(App.getAppContext().getString(R.string.update_notify_end_text));
        nb.setSmallIcon(R.mipmap.icon);
        nb.setWhen(System.currentTimeMillis());
        nb.setContentIntent(pendingIntent);
        nb.setOngoing(false);

        // 在升级完成之后，将首次启动重新设置为true
        App.getSharedPreference().edit().putBoolean(App.SHARED_FIRST_LAUNCH_NAME, true).commit();
        Notification notification = nb.getNotification();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(SELF_DOWNLOAD_NOTIFY_ID, notification);
    }

    public static Notification.Builder nb;

    // // 自更新下载进度通知
    public void addSelfDownloadProgressNotification(int dlsize, int totalsize)
    {
        NotificationManager mNotificationManager = (NotificationManager) App.getAppContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (nb == null)
        {
            nb = new Notification.Builder(App.getAppContext());
            nb.setContentTitle(App.getAppContext().getString(R.string.update_notify_start));
            nb.setSmallIcon(R.mipmap.icon);
            nb.setOngoing(true);
            // nb.setWhen(System.currentTimeMillis() +20);
        }
        nb.setProgress(100, (int) (((double) dlsize * 100 / totalsize)), false);
        mNotificationManager.notify(SELF_DOWNLOAD_NOTIFY_ID, nb.getNotification());
    }

    // 自更新下载失败通知
    public void addSelfDownloadErrorNotification()
    {
        NotificationManager mNotificationManager = (NotificationManager) App.getAppContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder nb = new Notification.Builder(App.getAppContext());
        nb.setContentTitle(App.getAppContext().getString(R.string.update_notify_error));
        nb.setContentText(App.getAppContext().getString(R.string.update_notify_error_text));
        nb.setSmallIcon(R.mipmap.icon);
        nb.setWhen(System.currentTimeMillis());
        nb.setOngoing(false);
        Notification notification = nb.getNotification();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(SELF_DOWNLOAD_NOTIFY_ID, notification);
    }

    // 有程序正在安装
    public void addInstallingNotification(String name)
    {

    }

    public static void cancelUpdateNotify()
    {
        mNotificationManager.cancel(UPDATE_NOTIFY_ID);
    }

}
