
package com.hykj.gamecenter.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.adapter.IUpdateSingleView;
import com.hykj.gamecenter.download.ApkDownloadManager;
import com.hykj.gamecenter.download.DownloadService;
import com.hykj.gamecenter.download.DownloadTask;
import com.hykj.gamecenter.download.DownloadTask.TaskState;
import com.hykj.gamecenter.download.IDownloadTaskStateListener;
import com.hykj.gamecenter.logic.ApkInstalledManager;
import com.hykj.gamecenter.logic.DisplayOptions;
import com.hykj.gamecenter.logic.NotificationCenter;
import com.hykj.gamecenter.mta.MtaUtils;
import com.hykj.gamecenter.protocol.Apps.AppInfo;
import com.hykj.gamecenter.statistic.MSG_CONSTANTS;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.ProgressButtonShowStatus;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.ui.widget.EllipsizingTextView;
import com.hykj.gamecenter.ui.widget.RoundCornerImageView;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateListFragment extends ListFragment implements
        IDownloadTaskStateListener {
    public static final String TAG = "UpdateListFragment";
    private ListView mList;
    private UpdateAdapter mAdapter;
    private static ApkInstalledManager mApkInstalledManager;
    private static ApkDownloadManager mApkDownloadManager;
    private static Activity mActivity = null;
    private static Fragment mParentFragment;

    private StatisticManager mStatisticManager;

    public String show_more;
    public String hide_more;
    public final int DEFAULT_LINES = 4;

    RelativeLayout mOnekeyupdatelayout = null; // 包含 一键升级按钮与提示
    LinearLayout mUpdate_layout;// 包含listview 与 mOnekeyupdatelayout
    TextView mDivider; // RelativeLayout与listview的分割线
    TextView mOnekeytip; // 一键升级提示
    Button mOnekeyUpdate; // 一键升级按钮

    Resources mRes;
    private static boolean IsUpdataall = false;
    public void UpdateAppsUpdateList() {

    }

    public void ShowOnekeyUpdateButton(boolean bShow) {
        if (mOnekeyupdatelayout != null) {
            if (bShow) {
                // 更新可升级应用数量
                if (mOnekeytip != null) {
                    mOnekeytip.setText(mRes.getString(R.string.onekeytip,
                            ApkInstalledManager.getInstance()
                                    .getAppUpdateInfo().size()));
                }
            }
            mOnekeyupdatelayout.setVisibility(bShow ? View.VISIBLE : View.GONE);
            mDivider.setVisibility(bShow ? View.VISIBLE : View.GONE);
        }

        Log.d(TAG, "ShowOnekeyUpdateButton " + bShow);
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
//        if (activity != null)
//            ((AppManageActivity) activity).setmUpdateListFragment(this);
        show_more = activity.getString(R.string.detail_show_more);
        hide_more = activity.getString(R.string.detail_hide_more);
    }

    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApkInstalledManager = ApkInstalledManager.getInstance();
        mApkDownloadManager = DownloadService.getDownloadManager();
        mStatisticManager = StatisticManager.getInstance();
        mAdapter = new UpdateAdapter(getActivity());
        mRes = getResources();
        setHandler(mUpdateRefreshHandler);
        mActivity = getActivity();
        mParentFragment = getParentFragment();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_update_list,
                container, false);

        mOnekeyupdatelayout = (RelativeLayout) rootView
                .findViewById(R.id.onekeyupdatelayout);
        mUpdate_layout = (LinearLayout) rootView
                .findViewById(R.id.update_layout);
        mDivider = (TextView) rootView.findViewById(R.id.divider);
        mOnekeytip = (TextView) rootView.findViewById(R.id.onekeytip);
        mOnekeyUpdate = (Button) rootView.findViewById(R.id.onekeyupdate);
        mOnekeyUpdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                updateAll();
                ShowOnekeyUpdateButton(false);
            }
        });
        //oddshou#####bugs####### 当提示多款应用升级时，出现可升级0个应用显示一键升级
        mOnekeytip.setText(mRes.getString(R.string.onekeytip,
                ApkInstalledManager.getInstance().getAppUpdateInfo().size()));
        int size = ApkInstalledManager.getInstance()
                .getAppUpdateInfo().size();
        ShowOnekeyUpdateButton(size > 0);
        //oddshou############
        if (IsUpdataall) {
            ShowOnekeyUpdateButton(false);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mList = getListView();
        mList.setAdapter(mAdapter);
    }

    private static boolean isInActivity() {
        return mActivity != null && !mActivity.isFinishing();
    }

    @Override
    public void onResume() {
        // Log.i(TAG, "UpdateListFragment onResume");
        doOnResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mApkInstalledManager.removeUiRefreshHandler(mUpdateRefreshHandler);
        super.onPause();
    }

    private void doOnResume() {
        mAdapter.notifyDataSetChanged();
        mApkInstalledManager.addUiRefreshHandler(mUpdateRefreshHandler);
    }

    private final OnClickListener mInstallingClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AppInfo info = (AppInfo) v.getTag();
            DownloadTask dinfo = mApkDownloadManager
                    .getDownloadTaskByAppId(info.appId);
            if (dinfo != null) {
                switch (dinfo.getState()) {
                    case PREPARING:
                    case WAITING:
                    case STARTED:
                    case LOADING:
                        mApkDownloadManager.stopDownload(dinfo);
                        // 用户主动暂停下载任务上报
                        ReportConstants
                                .reportDownloadStop(
                                        dinfo.appId,
                                        dinfo.packId,
                                        ReportConstants.STAC_APP_POSITION_DOWNLOAD_UPDATE_APP,
                                        ReportConstants.STAC_DOWNLOAD_APK_USER_ACTIVE_STOP,
                                        "");
                        MtaUtils.trackDownloadStop(StatisticManager.STOP_REASON_USER_ACTIVE_STOP);
                        break;
                    case SUCCEEDED:
                        mApkDownloadManager.installDownload(dinfo);
                        break;
                    case STOPPED:
                    case FAILED_NETWORK:
                    case FAILED_SERVER:
                    case FAILED_NOFREESPACE:
                        mApkDownloadManager.resumeDownload(dinfo);
                        // 用户主动暂停下载任务上报
                        ReportConstants
                                .reportDownloadResume(
                                        dinfo.appId,
                                        dinfo.packId,
                                        ReportConstants.STAC_APP_POSITION_DOWNLOAD_UPDATE_APP,
                                        ReportConstants.STAC_DOWNLOAD_APK_STOP_BREAKPOINT_RESUME,
                                        "");
                        MtaUtils.trackDownloadResume();
                        break;
                    case FAILED_BROKEN:
                    case DELETED:
                        mApkDownloadManager.restartDownload(dinfo);
                        break;
                    case FAILED_NOEXIST:
                        mApkDownloadManager.removeDownload(dinfo);
                        break;
                }
            }
        }
    };

    private final OnClickListener mUpdateClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // if( UITools.isFastDoubleClick( ) )
            // {
            // return;
            // }
            AppInfo appInfo = (AppInfo) v.getTag();
            mApkDownloadManager.startUpgradeDownload(appInfo,
                    ReportConstants.STAC_APP_POSITION_DOWNLOAD_UPDATE_APP);
//            mAdapter.notifyDataSetChanged();

            // 更新下载管理中心列表
//            ((AppManageActivity) getActivity()).refreshDownListFragment();
//            ((AppManageActivity) getActivity())
//                    .refreshActionBarWhenStatusIsAllUpgrade();

            // 更新下载管理中心列表
            ((AppManagerFragment) getParentFragment()).refreshDownListFragment();
            ((AppManagerFragment) getParentFragment())
                    .refreshActionBarWhenStatusIsAllUpgrade();

        }
    };

    // 全部更新
    public static void updateAll() {

        StringBuffer sbf = new StringBuffer("");
        if (mApkInstalledManager == null) {
            mApkInstalledManager = ApkInstalledManager.getInstance();
            mApkDownloadManager = DownloadService.getDownloadManager();
        }
        ArrayList<AppInfo> mListData = mApkInstalledManager.getAppUpdateInfo();
        for (AppInfo ai : mListData) {
            // 判断是否在下载队列中
            if (!mApkDownloadManager.isAppInTaskList(ai.appId)) {
                // LogUtils.e( "应用:" + ai.getShowName( ) + ",添加到下载队列当中." );
                mApkDownloadManager
                        .startUpgradeDownload(
                                ai,
                                ReportConstants.STAC_APP_POSITION_DOWNLOAD_UPDATE_APP);
            } else {
                sbf.append(ai.showName + ",");
            }
        }
        if (mActivity != null) {
            // 提示任务已经在下载中
            if (!"".equals(sbf.toString()) && isInActivity()) {
                CSToast.show(
                        mActivity,
                        sbf.toString()
                                + mActivity
                                        .getString(R.string.is_in_downloadtask_list));
            }
//            // 刷新头部按钮的状态
//            ((AppManageActivity) mActivity)
//                    .refreshActionBarWhenStatusIsAllUpgrade();
//            // 更新下载管理中心列表
//            ((AppManageActivity) mActivity).refreshDownListFragment();
            // 刷新头部按钮的状态
            ((AppManagerFragment) mParentFragment)
                    .refreshActionBarWhenStatusIsAllUpgrade();
            // 更新下载管理中心列表
            ((AppManagerFragment) mParentFragment).refreshDownListFragment();
            IsUpdataall = true;

        }
    }

    private class UpdateAdapter extends BaseAdapter implements
            IUpdateSingleView<Integer> {

        private ArrayList<AppInfo> mListData;
        private final LayoutInflater inflater;
        /**
         * 存放view的映射关系，用做单个view更新
         */
        protected HashMap<Integer, View> mViewMap = new HashMap<Integer, View>();

        private final HashMap<Integer, Boolean> mShowMap = new HashMap<Integer, Boolean>();

        public UpdateAdapter(Context context) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mListData = mApkInstalledManager.getAppUpdateInfo();
            if (mListData.size() > 0) {
                for (AppInfo ai : mListData) {
                    mShowMap.put(ai.appId, false);
                }
            }
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                if (App.getDevicesType() == App.PHONE)
                    convertView = inflater.inflate(
                            R.layout.phone_update_list_item, null);
                else
                    convertView = inflater.inflate(
                            R.layout.pad_update_list_item, null);

                holder = new ViewHolder();
                bindView(convertView, holder);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // empty view
            convertView.findViewById(R.id.empty_item_view).setVisibility(
                    position == 0 ? View.VISIBLE : View.GONE);
            if (mListData != null && mListData.size() > 0) {

                // 改为id 缓存view,而不使用packName ,因为有可能出现同包名但不同版本的应用apk
                setViewForSingleUpdate(mListData.get(position).appId,
                        convertView);
                // 放在缓存View操作的下方
                bindData(holder, mListData.get(position));
                showOrHideInfo(holder, mListData.get(position));
            }

            return convertView;
        }

        private void showOrHideInfo(final ViewHolder holder, final AppInfo info) {
            holder.upgradeinfo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mShowMap.get(info.appId)) {
                        holder.upgradeinfo.setMaxLines(Integer.MAX_VALUE);
                        mShowMap.put(info.appId, true);
                        holder.upgradeinfoshow
                                .setBackgroundResource(R.drawable.button_expansion);
                    } else {
                        holder.upgradeinfo.setMaxLines(DEFAULT_LINES);
                        mShowMap.put(info.appId, false);
                        holder.upgradeinfoshow
                                .setBackgroundResource(R.drawable.button_collapse);
                    }
                }
            });
        }

        private void bindView(View view, ViewHolder holder) {

            holder.appicon = (RoundCornerImageView) view
                    .findViewById(R.id.app_icon);
            holder.appname = (TextView) view.findViewById(R.id.app_name);
            holder.rating = (RatingBar) view.findViewById(R.id.app_rating);
            holder.appsize = (TextView) view.findViewById(R.id.app_size);
            holder.newVersionName = (TextView) view
                    .findViewById(R.id.new_version);
            holder.updateiconbtn = (Button) view
                    .findViewById(R.id.update_icon_btn);
            holder.installingiconbtn = (ProgressButtonShowStatus) view
                    .findViewById(R.id.installing_icon_btn);
            holder.updatelabel = (TextView) view
                    .findViewById(R.id.update_label);
            holder.upgradeinfo = (EllipsizingTextView) view
                    .findViewById(R.id.upgrade_info);
            holder.upgradeinfoshow = (ImageView) view
                    .findViewById(R.id.upgrade_info_show);
            holder.frame = (LinearLayout) view
                    .findViewById(R.id.upgrade_info_frame);
            holder.upgradeinfo.setMaxLines(DEFAULT_LINES);
            // holder.moreInfo = (TextView)view.findViewById( R.id.more_tv );

        }

        private void bindData(final ViewHolder holder, final AppInfo info) {

            ImageLoader.getInstance().displayImage(info.iconUrl,
                    holder.appicon, DisplayOptions.optionsIcon);

            holder.appname.setText(info.showName);
            holder.rating.setRating(info.recommLevel / 2);

            // 本地安装版本
            // String localVersionName =
            // mApkInstalledManager.getInstallVersionNameByPackageName(
            // info.getPackName( ) );
            // if( TextUtils.isEmpty( localVersionName ) )
            // {
            // localVersionName = getString(
            // R.string.current_version_label_unkown );
            // }

            holder.appsize
                    .setText(StringUtils.byteToString(info.packSize));
            holder.newVersionName
                    .setText(getString(R.string.update_version_label)
                            + info.verName);

            holder.updateiconbtn.setOnClickListener(mUpdateClickListener);
            holder.updateiconbtn.setText(getString(R.string.app_upgrade));
            holder.updateiconbtn
                    .setBackgroundResource(R.drawable.btn_green_selector);
            holder.updateiconbtn.setTextColor(getResources().getColorStateList(
                    R.color.btn_green_color));
            holder.installingiconbtn
                    .setOnClickListener(mInstallingClickListener);
            holder.installingiconbtn
                    .setOnTouchListener(holder.installingiconbtn);
            // TODO
            // holder.updatelabel.setText( getString( R.string.app_installed )
            // );

            // 设置显示更新详情的背景
            if (mShowMap.get(info.appId) != null) {
                holder.upgradeinfoshow.setBackgroundResource(mShowMap.get(info
                        .appId) ? R.drawable.button_expansion
                        : R.drawable.button_collapse);
            } else {
                mShowMap.put(info.appId, false);
            }

            if (mShowMap.get(info.appId)) {
                holder.upgradeinfo.setMaxLines(Integer.MAX_VALUE);
            } else {
                holder.upgradeinfo.setMaxLines(DEFAULT_LINES);
            }

            // 判断是否显示
            if (info.updateDesc != null
                    && !"".equals(info.updateDesc)) {
                holder.upgradeinfoshow.setVisibility(View.VISIBLE);
                // holder.upgradeinfo.setText( mShowMap.get( info.getAppId( ) )
                // ? getString( R.string.have_upgrade_details ) :
                // info.getUpdateDesc( ) );
                holder.upgradeinfo.setText(info.updateDesc.trim());
            } else {
                holder.upgradeinfoshow.setVisibility(View.GONE);
                // holder.upgradeinfo.setText( getString(
                // R.string.no_upgrade_details ) );
                holder.upgradeinfo.setText(info.updateDesc.trim());
            }

            holder.upgradeinfoshow.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mShowMap.get(info.appId)) {
                        holder.upgradeinfo.setText(getString(
                                R.string.have_upgrade_details).trim());
                        mShowMap.put(info.appId, true);
                        holder.upgradeinfoshow
                                .setBackgroundResource(R.drawable.button_expansion);
                    } else {
                        holder.upgradeinfo.setText(info.updateDesc.trim());
                        mShowMap.put(info.appId, false);
                        holder.upgradeinfoshow
                                .setBackgroundResource(R.drawable.button_collapse);
                    }
                }
            });

            holder.frame.setTag(holder.upgradeinfoshow);
            // 同步upgradeinfoshow事件
            holder.frame.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (info.updateDesc != null
                            && !"".equals(info.updateDesc)) {
                        ((ImageView) v.getTag()).performClick();
                    }

                }
            });

            holder.updateiconbtn.setTag(info);
            holder.installingiconbtn.setTag(info);

            // 是否在下载队列中
            DownloadTask dinfo = mApkDownloadManager
                    .getDownloadTaskByAppId(info.appId);

            if (dinfo != null) {
                holder.updateiconbtn.setVisibility(View.GONE);
                holder.installingiconbtn.setVisibility(View.VISIBLE);

                setDownloadState(dinfo, holder, dinfo.getState());
            } else {
                holder.updateiconbtn.setVisibility(View.VISIBLE);
                holder.installingiconbtn.setVisibility(View.GONE);
                holder.updatelabel.setVisibility(View.GONE);
            }
        }

        @Override
        public void notifyDataSetChanged() {
            mListData = mApkInstalledManager.getAppUpdateInfo();

            // 初始化checkbox的状态
            for (AppInfo ai : mListData) {
                if (mShowMap.get(ai.appId) == null) {
                    mShowMap.put(ai.appId, false);
                }
            }
            NotificationCenter.getInstance().cancelUpdateNotify();
            super.notifyDataSetChanged();
        }

        class ViewHolder {
            public LinearLayout frame;
            public RoundCornerImageView appicon;
            public TextView appname;
            public RatingBar rating;
            public TextView appsize;
            public Button updateiconbtn;
            public ProgressButtonShowStatus installingiconbtn;
            public TextView updatelabel;
            public TextView newVersionName;
            public EllipsizingTextView upgradeinfo;
            public ImageView upgradeinfoshow;
        }

        public void updateDownloadState(DownloadTask di) {
            View view = getViewByKey(di.appId);
            if (view == null) {
                return;
            }
            ViewHolder holder = (ViewHolder) view.getTag();
            AppInfo item = (AppInfo) holder.updateiconbtn.getTag();
            if (di.packageName.equals(item.packName)
                    && di.appId == item.appId) {
                setDownloadState(di, holder, di.getState());
            }
        }

        private void setDownloadState(DownloadTask di, ViewHolder holder,
                TaskState state) {
             Logger.e(TAG, "更新任务：" + di.appName + "| 包名：" + di.packageName + "| 状态："  +
                     di.getState() + "  ----  开始");
            holder.installingiconbtn.setVisibility(View.VISIBLE);
            holder.updatelabel.setVisibility(View.GONE);
            holder.updateiconbtn.setVisibility(View.GONE);
            holder.installingiconbtn.setProgressVisiable(false);

            switch (state) {
                case PREPARING:
                    holder.installingiconbtn
                            .setBackgroundResource(R.drawable.btn_green_selector);
                    /*
                     * holder.installingiconbtn.setTextColor(getResources()
                     * .getColorStateList(R.color.btn_green_color));
                     */
                    holder.installingiconbtn.setText(getString(R.string.app_pause));
                    holder.installingiconbtn.setEnabled(true);
                    holder.installingiconbtn.setProgressVisiable(true);
                    holder.installingiconbtn.setProgress(di
                            .getDownloadingProgress());
                    break;
                case WAITING:
                    holder.installingiconbtn
                            .setBackgroundResource(R.drawable.btn_green_selector);

                    holder.installingiconbtn.setTextColor(getResources()
                            .getColorStateList(R.color.btn_green_color));

                    holder.installingiconbtn.setText(getString(R.string.app_pause));
                    holder.installingiconbtn.setEnabled(true);
                    holder.installingiconbtn.setProgressVisiable(true);
                    holder.installingiconbtn.setProgress(di
                            .getDownloadingProgress());
                    break;
                case STARTED:
                case LOADING:
                    holder.installingiconbtn
                            .setBackgroundResource(R.drawable.btn_green_selector);

                    holder.installingiconbtn.setTextColor(getResources()
                            .getColorStateList(R.color.btn_green_color));

                    holder.installingiconbtn.setEnabled(true);
                    holder.installingiconbtn.setText(getString(R.string.app_pause));
                    holder.installingiconbtn.setProgressVisiable(true);
                    holder.installingiconbtn.setProgress(di
                            .getDownloadingProgress());
                    break;
                case STOPPED:
                    holder.installingiconbtn
                            .setBackgroundResource(R.drawable.btn_green_selector);

                    holder.installingiconbtn.setTextColor(getResources()
                            .getColorStateList(R.color.btn_green_color));

                    holder.installingiconbtn.setEnabled(true);
                    holder.installingiconbtn
                            .setText(getString(R.string.app_resume));
                    break;
                case SUCCEEDED:
                    // TODO
                    holder.installingiconbtn
                            .setBackgroundResource(R.drawable.btn_green_selector);

                    holder.installingiconbtn.setTextColor(getResources()
                            .getColorStateList(R.color.btn_green_color));

                    holder.installingiconbtn.setEnabled(true);
                    holder.installingiconbtn
                            .setText(getString(R.string.app_install));
                    if (mApkInstalledManager.isApkLocalInstalled(di.packageName)) {
                        // 如果安装立即刷新列表
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case DELETED:
                    holder.installingiconbtn
                            .setBackgroundResource(R.drawable.btn_green_selector);

                    holder.installingiconbtn.setTextColor(getResources()
                            .getColorStateList(R.color.btn_green_color));

                    holder.installingiconbtn.setEnabled(true);
                    holder.installingiconbtn
                            .setText(getString(R.string./*app_redownload*/app_pause));
                    break;
                case INSTALLING:
                    holder.installingiconbtn.setVisibility(View.GONE);
                    holder.updatelabel.setVisibility(View.VISIBLE);
                    holder.installingiconbtn
                            .setBackgroundResource(R.drawable.btn_green_selector);

                    holder.installingiconbtn.setTextColor(getResources()
                            .getColorStateList(R.color.btn_green_color));

                    holder.installingiconbtn.setEnabled(false);
                    holder.installingiconbtn
                            .setText(getString(R.string.app_install));
                    holder.updatelabel.setText(getString(R.string.app_installing));
                    break;
                case FAILED_NETWORK:
                    holder.installingiconbtn
                            .setBackgroundResource(R.drawable.btn_green_selector);

                    holder.installingiconbtn.setTextColor(getResources()
                            .getColorStateList(R.color.btn_green_color));

                    holder.installingiconbtn.setEnabled(true);
                    holder.installingiconbtn.setText(getString(R.string.app_retry));
                    break;
                case FAILED_BROKEN:
                    holder.installingiconbtn
                            .setBackgroundResource(R.drawable.btn_green_selector);

                    holder.installingiconbtn.setTextColor(getResources()
                            .getColorStateList(R.color.btn_green_color));

                    holder.installingiconbtn.setEnabled(true);
                    holder.installingiconbtn
                            .setText(getString(R.string.app_redownload));
                    break;
                case FAILED_NOEXIST:
                    holder.installingiconbtn
                            .setBackgroundResource(R.drawable.btn_gray_selector);

                    holder.installingiconbtn.setTextColor(getResources()
                            .getColorStateList(R.color.btn_gray_color));

                    holder.installingiconbtn.setEnabled(true);
                    holder.installingiconbtn
                            .setText(getString(R.string.app_delete));
                    break;
                case FAILED_SERVER:
                    holder.installingiconbtn
                            .setBackgroundResource(R.drawable.btn_green_selector);

                    holder.installingiconbtn.setTextColor(getResources()
                            .getColorStateList(R.color.btn_green_color));

                    holder.installingiconbtn.setEnabled(true);
                    holder.installingiconbtn.setText(getString(R.string.app_retry));
                    break;
                case FAILED_NOFREESPACE:
                    holder.installingiconbtn
                            .setBackgroundResource(R.drawable.btn_green_selector);

                    holder.installingiconbtn.setTextColor(getResources()
                            .getColorStateList(R.color.btn_green_color));

                    holder.installingiconbtn.setEnabled(true);
                    holder.installingiconbtn.setText(getString(R.string.app_retry));
                    break;
            }
        }

        @Override
        public View getViewByKey(Integer key) {
            View view = mViewMap.get(key);
            return view;
        }

        @Override
        public void setViewForSingleUpdate(Integer key, View view) {
            if (view == null) {
                return;
            }
            mViewMap.put(key, view);
        }

        @Override
        public void removeViewForSingleUpdate(View view) {

        }

        @Override
        public void removeViewForSingleUpdate(Integer key) {
            mViewMap.remove(key);
        }

    }

    private static Handler mHandler = null;

    public static Handler getHandler() {
        return mHandler;
    }

    public static void setHandler(Handler handler) {
        mHandler = handler;
    }

    private final Handler mUpdateRefreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CONSTANTS.MSG_CHECK_UPDATE: {
                    mAdapter.notifyDataSetChanged();
                    // ( (AppManageActivity)getActivity( ) ).refreshTitleTip( );
                    ((AppManagerFragment) getParentFragment())
                            .refreshActionBarWhenStatusIsAllUpgrade();

                }
                    break;
                case MSG_CONSTANTS.MSG_CHECK_TOAST: {
                    CSToast.show(getActivity(), (String) msg.obj);

                }
                    break;
                case MSG_CONSTANTS.MSG_ONE_KEY_UPDATE: {
                    Log.d(TAG, "MSG_CONSTANTS.MSG_ONE_KEY_UPDATE updateAll");
                    NotificationCenter.getInstance().cancelUpdateNotify();
                    updateAll();
                }
                    break;
            }
        }
    };

    /*
     * (non-Javadoc)
     * @see
     * com.niuwan.gamecenter.download.IDownloadTaskStateListener#onUpdateTaskProgress
     * (com.niuwan.gamecenter.download.DownloadTask)
     */
    @Override
    public void onUpdateTaskProgress(DownloadTask task) {
        if (isInActivity() == false || task == null || mAdapter == null
                || isVisible() == false)
            return;
        // mAdapter.updateProgress( task.packageName , (int)task.progress ,
        // (int)task.fileLength );
        mAdapter.updateDownloadState(task);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.niuwan.gamecenter.download.IDownloadTaskStateListener#onUpdateTaskState
     * (com.niuwan.gamecenter.download.DownloadTask)
     */
    @Override
    public void onUpdateTaskState(DownloadTask task) {
        if (isInActivity() == false || task == null || mAdapter == null
                || isVisible() == false)
            return;

        mAdapter.updateDownloadState(task);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.niuwan.gamecenter.download.IDownloadTaskStateListener#onUpdateTaskList()
     */
    @Override
    public void onUpdateTaskList(Object obj) {
        if (isInActivity() == false || mAdapter == null || isVisible() == false)
            return;

        mAdapter.notifyDataSetChanged();
    }

}
