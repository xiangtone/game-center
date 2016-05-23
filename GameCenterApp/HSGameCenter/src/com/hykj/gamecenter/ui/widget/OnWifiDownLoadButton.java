
package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.download.ApkDownloadManager;
import com.hykj.gamecenter.download.DownloadService;
import com.hykj.gamecenter.download.DownloadTask;
import com.hykj.gamecenter.download.DownloadTask.TaskState;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.ui.InWifiDownLoadDialog;
import com.hykj.gamecenter.ui.InWifiDownLoadDialog.KnowBtnOnClickListener;
import com.hykj.gamecenter.ui.NoWifiContinueDownloadDialog;
import com.hykj.gamecenter.ui.widget.OnWifiClickListener.NoWifiContinueDownloadListener;
import com.hykj.gamecenter.ui.widget.OnWifiClickListener.WifiDownLoadOnClickListener;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.Tools;

public class OnWifiDownLoadButton extends Button
{
    private static final String TAG = "DownLoadButton";

    private Context mContext = null;
    private WifiDownLoadOnClickListener mListener = null;
    private KnowBtnOnClickListener kListener = null;

    private ApkDownloadManager mApkDownloadManager = null;

    public OnWifiDownLoadButton(Context context)
    {
        super(context);
        mContext = context;
        mApkDownloadManager = DownloadService.getDownloadManager();
    }

    public OnWifiDownLoadButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        mApkDownloadManager = DownloadService.getDownloadManager();
    }

    public OnWifiDownLoadButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mContext = context;
        mApkDownloadManager = DownloadService.getDownloadManager();
    }

    public void setWifiLoadOnClickListener(WifiDownLoadOnClickListener listener)
    {
        mListener = listener;
    }

    public void setKownOnClickListener(KnowBtnOnClickListener listener)
    {
        kListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            boolean bWifiToDownload = App.getSettingContent().getSettingData().bWifiToDownload;
            Logger.i(TAG, "bWifiToDownload = " + bWifiToDownload);

            GroupElemInfo task = null;

            // 
            if (getTag() instanceof GroupElemInfo)
            {
                task = (GroupElemInfo) getTag();
            }

            if (bWifiToDownload)
            {
                if (Tools.is3G(mContext))
                {
                    Logger.i(TAG, "it is 3g net");
                    if (null != task)
                    {
                        DownloadTask dinfo = mApkDownloadManager.getDownloadTaskByAppId(task.appId);
                        if (null != dinfo)
                        {
                            if (dinfo.getState().equals(TaskState.SUCCEEDED))
                            {
                                mListener.onWifiClickListener(this);
                            }
                            else if (dinfo.getState().equals(TaskState.STOPPED)
                                    || dinfo.getState().equals(TaskState.FAILED_SERVER))
                            {
                                showContinueDialog(dinfo);
                            }
                            else if (dinfo.getState().equals(TaskState.LOADING)
                                    || dinfo.getState().equals(TaskState.STARTED))
                            {
                                mListener.onWifiClickListener(this);
                            }
                            return super.onTouchEvent(event);
                        }
                    }
                    showOnWifiDialog();
                }
                else
                {
                    mListener.onWifiClickListener(this);
                }
            }
            else
            {
                mListener.onWifiClickListener(this);
            }
        }
        return super.onTouchEvent(event);
    }

    private void showContinueDialog(final DownloadTask dinfo)
    {
        NoWifiContinueDownloadDialog dialog = new NoWifiContinueDownloadDialog(mContext,
                R.style.MyDialog);
        dialog.setView(this);
        dialog.setNoWifiContinueDownloadListener(new NoWifiContinueDownloadListener()
        {

            @Override
            public void NoWifiContinueListener(View v)
            {
                mApkDownloadManager.resumeDownload(dinfo);
            }
        });
        dialog.show();
    }

    private void showOnWifiDialog()
    {
        InWifiDownLoadDialog dialog = new InWifiDownLoadDialog(mContext, R.style.MyDialog);
        dialog.setView(this);
        dialog.setKnowBtnOnClickListener(kListener);
        dialog.show();
    }

    //    /** 
    //    * 判断当前网络是否是3G网络 
    //    * 
    //    * @param context 
    //    * @return boolean 
    //    */
    //    public static boolean is3G( Context context )
    //    {
    //	ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService( Context.CONNECTIVITY_SERVICE );
    //	NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo( );
    //	if( activeNetInfo != null && activeNetInfo.getType( ) == ConnectivityManager.TYPE_MOBILE )
    //	{
    //	    return true;
    //	}
    //	return false;
    //    }

}
