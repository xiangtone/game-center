
package com.hykj.gamecenter.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.protocol.Updater.RspUpdate;
import com.hykj.gamecenter.utils.PackageUtils;
import com.hykj.gamecenter.utilscs.LogUtils;

public class UpdateDialog extends Dialog
{

    private Button mUpdateBtn;
    private Button mCancelBtn;
    private TextView mUpdateAlert;
    private TextView mUpdateHint;
    private Button mInstallBtn;
    private TextView mTitle;

    private View.OnClickListener mUpdateListener;
    private View.OnClickListener mCancelListener;

    private ProgressBar progress;
    private TextView progressIndicator;
    private RspUpdate mUpdateInfo;
    private Context mContext;
    private String appPath;
    private boolean hasDownload = false;

    public UpdateDialog(Context context, RspUpdate info)
    {
        //		this(context, R.style.CSDialog);
        this(context, R.style.CSAlertDialog/*R.style.MyDialog*/);
        mUpdateInfo = info;
        mContext = context;
    }

    public UpdateDialog(Context context, int theme)
    {
        super(context, theme);
        //	mContext = context;
    }

    public void setListeners(View.OnClickListener updateListener,
            View.OnClickListener cancelListener)
    {
        mUpdateListener = updateListener;
        mCancelListener = cancelListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.dialog_self_update);
        mUpdateBtn = (Button) findViewById(R.id.update_btn);
        mCancelBtn = (Button) findViewById(R.id.cancel_btn);
        mUpdateAlert = (TextView) findViewById(R.id.update_alert);
        mUpdateHint = (TextView) findViewById(R.id.update_hint);
        mInstallBtn = (Button) findViewById(R.id.install);
        mTitle = (TextView) findViewById(R.id.title_tv);

        progress = (ProgressBar) findViewById(R.id.download_progress);
        progressIndicator = (TextView) findViewById(R.id.download_progress_indicator);

        mUpdateBtn.setTag(mUpdateInfo);
        mCancelBtn.setTag(mUpdateInfo);

        mUpdateBtn.setOnClickListener(mUpdateListener);
        mCancelBtn.setOnClickListener(mCancelListener);
        mInstallBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                PackageUtils.installNormal(mContext, appPath);
            }

        });
        //mUpdateInfo.updateType == 3 表示强制升级
        if (mUpdateInfo.updateType == 3)
        {
            mUpdateAlert.setTextColor(mContext.getResources().getColor(R.color.red));
            LogUtils.d("mUpdateInfo.getUpdatePrompt: " + mUpdateInfo.updatePrompt);
            mUpdateAlert.setText(mUpdateInfo.updatePrompt);
            if (mUpdateInfo.updatePrompt.length() > 0) {
                mUpdateAlert.setVisibility(View.VISIBLE);
            }
            mCancelBtn.setText(R.string.cancel);

            setCancelable(false);
            setCanceledOnTouchOutside(false);
            mCancelBtn.setVisibility(View.GONE);
//            mUpdateBtn.setBackground(mContext.getResources().getDrawable(
//                    R.drawable.csls_alert_dialog_all_btn));
        }
        mUpdateHint.setText(mUpdateInfo.updateDesc);
        if ("".equals(mUpdateInfo.updateDesc) || null == mUpdateInfo.updateDesc)
        {
            mUpdateHint.setVisibility(View.GONE);
        }
        else
        {
            mUpdateHint.setVisibility(View.VISIBLE);
        }

    }

    public RspUpdate getmUpdateInfo()
    {
        return mUpdateInfo;
    }

    public void setmUpdateInfo(RspUpdate mUpdateInfo)
    {
        this.mUpdateInfo = mUpdateInfo;
    }

    public void setUpdateBtnText(int resId) {
        mUpdateBtn.setText(resId);
    }

    public void startDownload() {
        View updateInfoView = findViewById(R.id.update_info);
        updateInfoView.setVisibility(View.GONE);
        View updateProgress = findViewById(R.id.update_progressbar);
        updateProgress.setVisibility(View.VISIBLE);
        mUpdateBtn.setClickable(false);
//        mUpdateBtn.setBackground(mContext.getResources().getDrawable(
//                R.drawable.csl_alert_dialog_all_btn_press));
        setProgress(0);
        mTitle.setText(R.string.update_notify_start);
        mCancelBtn.setVisibility(View.GONE);
        hasDownload = false;
    }

    public void setProgress(int current) {
        progressIndicator.setText("" + current + "%");
        progress.setProgress(current);
        if (current == 100) {
            mInstallBtn.setVisibility(View.VISIBLE);
            mUpdateBtn.setVisibility(View.GONE);
            mCancelBtn.setVisibility(View.GONE);
            mTitle.setText(R.string.update_notify_end);
        }
    }

    public void setAppPath(String path) {
        appPath = path;
        hasDownload = true;

    }

    public void setDownloadFailStatus() {
        // TODO Auto-generated method stub
        if (!hasDownload)
            mTitle.setText(R.string.update_notify_error);
        mUpdateBtn.setClickable(true);
//        mUpdateBtn.setBackground(mContext.getResources().getDrawable(
//                R.drawable.csls_alert_dialog_all_btn));
        mUpdateBtn.setText(R.string.app_retry);
    }
}
