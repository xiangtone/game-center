
package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.download.DownloadTask;
import com.hykj.gamecenter.protocol.Apps.AppInfo;
import com.hykj.gamecenter.ui.help.BtnDownloadUpdateController;
import com.hykj.gamecenter.ui.help.BtnDownloadUpdateController.BtnState;
import com.hykj.gamecenter.utilscs.LogUtils;

/**
 * 功能：显示下载、暂停、继续、打开，暂时不显示进度条
 * <p>
 * 设置辅助controller托管所有逻辑，本身只负责显示
 * 
 * @author oddshou
 */
public class BtnDownloadUpdate extends Button {

    private static final String TAG = "BtnDownloadUpdate";
    private BtnDownloadUpdateController mBtnController;
    private Resources mRes;

    public BtnDownloadUpdate(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initData(context);
    }

    public BtnDownloadUpdate(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initData(context);
    }

    public BtnDownloadUpdate(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initData(context);
    }
    /**
     * 这个方法 只包含当前控件的展示
     * @param taskState
     */
    public void updateDownLoadBtn(BtnState btnState,DownloadTask dinfo) {
        LogUtils.e("updateDownLoadBtn");
        
        switch (btnState.updateState) {
            case NORMAL:
                this.setText(mRes.getString(R.string.app_download));
                this.setBackgroundResource(R.drawable.btn_first_framework_selector);
                this.setTextColor(mRes.getColorStateList(R.color.color_first_white_selector));
                this.setEnabled(true);
                return;
            case NEEDUPDATE:
                this.setText(mRes.getString(R.string.app_update));
                this.setBackgroundResource(R.drawable.btn_first_framework_selector);
                this.setTextColor(mRes.getColorStateList(R.color.color_first_white_selector));
                return;
            case OPEN:
                this.setText(mRes.getString(R.string.app_open));
                this.setBackgroundResource(R.drawable.btn_gray_selector);
                this.setTextColor(mRes.getColorStateList(R.color.btn_gray_color));
                return;
            case NULL:
                
                break;

            default:
                break;
        }
        if (dinfo != null) {
            switch (btnState.downloadState) {
                case PREPARING:
                    this.setBackgroundResource(R.drawable.btn_first_framework_selector);
                    this.setTextColor(mRes.getColorStateList(R.color.color_first_white_selector));
                    this.setText(mRes.getString(R.string.app_pause));
                    this.setEnabled(true);
                    break;
                case WAITING:
                    this.setBackgroundResource(R.drawable.btn_first_framework_selector);
                    this.setTextColor(mRes.getColorStateList(R.color.color_first_white_selector));
                    this.setEnabled(true);
                    this.setText(mRes.getString(R.string.app_pause));
                    break;
                case STARTED:
                case LOADING:
                    this.setBackgroundResource(R.drawable.btn_first_framework_selector);
                    this.setTextColor(mRes.getColorStateList(R.color.color_first_white_selector));
                    this.setEnabled(true);
                    this.setText(mRes.getString(R.string.app_pause));
                    break;
                case STOPPED:
                    this.setBackgroundResource(R.drawable.btn_first_framework_selector);
                    this.setTextColor(mRes.getColorStateList(R.color.color_first_white_selector));
                    this.setEnabled(true);
                    this.setText(mRes.getString(R.string.app_resume));
                    break;
                case SUCCEEDED:
                    // TODO
                    this.setBackgroundResource(R.drawable.btn_first_framework_selector);
                    this.setTextColor(mRes.getColorStateList(R.color.color_first_white_selector));
                    this.setEnabled(true);
                    this.setText(mRes.getString(R.string.app_install));
                    break;
                case DELETED:
                    this.setBackgroundResource(R.drawable.btn_first_framework_selector);
                    this.setTextColor(mRes.getColorStateList(R.color.color_first_white_selector));
                    this.setEnabled(true);
                    this.setText(mRes.getString(R.string.app_redownload));
                    break;
                case INSTALLING:
                    // TODO
                    //####################oddshou 暂未处理
//                mAppInstalled.setVisibility(View.VISIBLE);
//                mAppInstalled.setText(mRes.getString(R.string.app_installing));
                    this.setBackgroundResource(R.drawable.btn_first_framework_selector);
                    this.setTextColor(mRes.getColorStateList(R.color.color_first_white_selector));
                    this.setEnabled(false);
                    this.setText(mRes.getString(R.string.app_install));
                    break;

                case FAILED_NETWORK:
                    this.setBackgroundResource(R.drawable.btn_first_framework_selector);
                    this.setTextColor(mRes.getColorStateList(R.color.color_first_white_selector));
                    this.setEnabled(true);
                    this.setText(mRes.getString(R.string.app_retry));
                    break;
                case FAILED_BROKEN:
                    this.setBackgroundResource(R.drawable.btn_first_framework_selector);
                    this.setTextColor(mRes.getColorStateList(R.color.color_first_white_selector));
                    this.setEnabled(true);
                    this.setText(mRes.getString(R.string.app_redownload));
                    break;
                case FAILED_NOEXIST:
                    this.setTextColor(mRes.getColorStateList(R.color.btn_gray_color));
                    this.setBackgroundResource(R.drawable.btn_gray_selector);
                    this.setEnabled(true);
                    this.setText(mRes.getString(R.string.app_delete));
                    break;
                case FAILED_SERVER:
                    this.setTextColor(mRes.getColorStateList(R.color.color_first_white_selector));
                    this.setBackgroundResource(R.drawable.btn_first_framework_selector);
                    this.setEnabled(true);
                    this.setText(mRes.getString(R.string.app_retry));

                    break;
                case FAILED_NOFREESPACE:
                    this.setTextColor(mRes.getColorStateList(R.color.color_first_white_selector));
                    this.setBackgroundResource(R.drawable.btn_first_framework_selector);
                    this.setEnabled(true);
                    this.setText(mRes.getString(R.string.app_retry));
                    break;
            }
        }
    }

    private void initData(Context context) {
        mBtnController = new BtnDownloadUpdateController(context, this);
        mRes = context.getResources();
        setOnClickListener(mOnClickListener);
    }

    OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mBtnController.onBtnClick(v);
        }
    };
    
    public void bindData(AppInfo groupElementInfo, int appPositionType){
        mBtnController.bindData(groupElementInfo, appPositionType);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        return super.onTouchEvent(event);
    }
    
    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        mBtnController.remove();
        super.onDetachedFromWindow();
    }

}
