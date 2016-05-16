
package com.hykj.gamecenter.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.adapter.IUpdateSingleView;
import com.hykj.gamecenter.db.CSACContentProvider;
import com.hykj.gamecenter.db.CSACDatabaseHelper;
import com.hykj.gamecenter.download.ApkDownloadManager;
import com.hykj.gamecenter.download.DownloadService;
import com.hykj.gamecenter.download.DownloadTask;
import com.hykj.gamecenter.download.DownloadTask.TaskState;
import com.hykj.gamecenter.download.DownloadTaskManager;
import com.hykj.gamecenter.download.IDownloadTaskStateListener;
import com.hykj.gamecenter.logic.ApkInstalledManager;
import com.hykj.gamecenter.logic.DisplayOptions;
import com.hykj.gamecenter.logic.entry.MsgDownload;
import com.hykj.gamecenter.mta.MtaUtils;
import com.hykj.gamecenter.protocol.Apps;
import com.hykj.gamecenter.statistic.MSG_CONSTANTS;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.AlwaysMarqueeTextView;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.ui.widget.DownloadAnimationView;
import com.hykj.gamecenter.ui.widget.DownloadListButton;
import com.hykj.gamecenter.ui.widget.OnWifiClickListener.WifiDownLoadOnClickListener;
import com.hykj.gamecenter.ui.widget.RoundCornerImageView;
import com.hykj.gamecenter.utils.DateUtil;
import com.hykj.gamecenter.utils.FileUtils;
import com.hykj.gamecenter.utils.Interface.IDownloadTaskObserver;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.StringUtils;
import com.hykj.gamecenter.utilscs.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DownloadExpandListFragment extends Fragment implements IDownloadTaskStateListener,
        IDownloadTaskObserver
{

    //    private static final String TAG = "DownloadExpandListFragment";

    private DownloadExpandListAdapter mAdapter;
    private ExpandableListView mList;
    private View emptyView;
    private final ApkDownloadManager mApkDownloadManager = DownloadService.getDownloadManager();
    private final ApkInstalledManager mApkInstalledManager = ApkInstalledManager.getInstance();
    private ArrayList<DownloadTask> mDownloadListData;
    private ArrayList<DownloadTask> mDownloadedTasks = new ArrayList<DownloadTask>();

    private Handler mHandler;

    private static final int EDIT_STATUS = 3001;
    private static final int NOTMAL_STATUS = 3002;

    private int mListViewStatus = NOTMAL_STATUS;
    private int mDownloadedListViewStatus = NOTMAL_STATUS;

    private Object lockNotify = new Object();

    // 列表是否已停止滑动
    public boolean mIsScrollStateIdle = true;

    // private int mListMode = MODE_NORMAL;
    // private static final int MODE_NORMAL = 1;
    // private static final int MODE_PATCH = 2;
    private final static String TAG = "DownloadExpandListFragment";

    public void setHandler(Handler mHandler)
    {
        this.mHandler = mHandler;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // debug模式在这里触发
        if (App.isDebugMode())
        {
            mApkDownloadManager.openDebugMode();
            // 创建调试文件
            String filePath = FileUtils.createDebugLogFile();

            LogUtils.e("调试 filePath = " + filePath);
            if (!filePath.isEmpty())
            {
                App.setDebugLogFilePath(filePath);
            }
        }
        else
        {
            mApkDownloadManager.closeDebugMode();
        }

    }

    @Override
    public void onAttach(Activity activity)
    {
        // LogUtils.e( "DownloadListFragment , onAttach" );
        // TODO Auto-generated method stub
        super.onAttach(activity);
//        if (activity != null)
//            ((AppManageActivity) activity).setmDownloadedFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // LogUtils.e( "DownloadListFragment , onCreateView" );
        View rootView = inflater.inflate(R.layout.fragment_download_expandlist, container, false);
        mList = (ExpandableListView) rootView.findViewById(R.id.download_expandlist);
        mList.setGroupIndicator(null);//置空即为无箭头
        emptyView = rootView.findViewById(R.id.download_expandlist_empty);

        initListView();
        return rootView;
    }

    private void initListView() {
        mAdapter = new DownloadExpandListAdapter(getActivity());
        mAdapter.addGroupName(App.getAppContext().getResources()
                .getString(R.string.download_title_continue).trim());
        mAdapter.addGroupName(App.getAppContext().getResources()
                .getString(R.string.download_title_finished).trim());
        mList.setAdapter(mAdapter);
        mList.expandGroup(0);
        mList.expandGroup(1);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // LogUtils.e( "DownloadListFragment , onActivityCreated" );
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume()
    {
        // LogUtils.e( "DownloadListFragment , onResume" );
        super.onResume();
        doOnResume();
    }

    @Override
    public void onStart()
    {
        // TODO Auto-generated method stub
        super.onStart();
        // 注册下载任务个数变化监听
        DownloadTaskManager.addDownloadTaskCountChangeListener(this);
    }

    @Override
    public void onStop()
    {
        // TODO Auto-generated method stub
        super.onStop();
        // 反注册下载任务个数变化监听
        DownloadTaskManager.removeDownloadTaskCountChangeListener(this);
    }

    @Override
    public void onPause()
    {
        // LogUtils.e( "DownloadListFragment, onPause" );
        super.onPause();
        mApkInstalledManager.removeUiRefreshHandler(mUpdateRefreshHandler);
    }

    private void doOnResume()
    {
        mAdapter.notifyDataSetChanged();
        mApkInstalledManager.addUiRefreshHandler(mUpdateRefreshHandler);
//        if (isInActivity())
//            ((AppManageActivity) getActivity()).refreshActionBarWhenStatusIsEdit();
    }

    private final Handler mUpdateRefreshHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MSG_CONSTANTS.MSG_CHECK_UPDATE:
                {
                    // LogUtils.e( "DownloadListFragment----MSG_CHECK_UPDATE" );
                    mAdapter.notifyDataSetChanged();
                    // ( (AppManageActivity)getActivity( ) ).refreshTitleTip( );
                    break;
                }
                case MSG_CONSTANTS.MSG_CHECK_TOAST:
                {
                    CSToast.show(App.getAppContext(), (String) msg.obj);
                    break;
                }
            }
        }
    };

    // @Override
    // public void onConfigurationChanged( android.content.res.Configuration
    // newConfig )
    // {
    // if( mAdapter != null )
    // {
    // mAdapter.notifyDataSetChanged( );
    // }
    // };

    private final WifiDownLoadOnClickListener mDownloadClickListener = new WifiDownLoadOnClickListener()
    {

        @Override
        public void onWifiClickListener(View v)
        {

            v.setEnabled(false);
            DownloadTask dinfo = (DownloadTask) v.getTag();
            if (dinfo != null)
            {
                //                Log.i(TAG, "dinfo.State = " + dinfo.getState());
                //                Log.i(TAG, "dinfo.nFromPos = " + dinfo.nFromPos);
                switch (dinfo.getState())
                {
                    case PREPARING:
                    case WAITING:
                    case STARTED:
                    case LOADING:
                        mApkDownloadManager.stopDownload(dinfo);
                        // 用户主动暂停下载任务上报
                        ReportConstants.reportDownloadStop(dinfo.appId, dinfo.packId,
                                ReportConstants.STAC_APP_POSITION_DOWNLOAD_MANAGER_APP,
                                ReportConstants.STAC_DOWNLOAD_APK_USER_ACTIVE_STOP, "");
                        MtaUtils.trackDownloadStop(StatisticManager.STOP_REASON_USER_ACTIVE_STOP);
                        break;
                    case SUCCEEDED:
                        mApkDownloadManager.installDownload(dinfo);
                        break;
                    // case FAILED_NETWORK: // 网络错误
                    // mApkDownloadManager.restartDownload(dinfo);//
                    // FAILED_NETWORK
                    // break; // 从resumeDownload修改为restartDownload
                    case STOPPED:
                    case FAILED_NETWORK:
                    case FAILED_SERVER:
                    case FAILED_NOFREESPACE:

                        mApkDownloadManager.resumeDownload(dinfo);
                        // 暂停下载任务继续下载上报
                        ReportConstants.reportDownloadResume(dinfo.appId, dinfo.packId,
                                ReportConstants.STAC_APP_POSITION_DOWNLOAD_MANAGER_APP,
                                ReportConstants.STAC_DOWNLOAD_APK_STOP_BREAKPOINT_RESUME, "");
                        MtaUtils.trackDownloadResume();
                        break;
                    case FAILED_BROKEN:
                    case DELETED:
                        mApkDownloadManager.restartDownload(dinfo);
                        mAdapter.updateProgress(dinfo.appId, dinfo.packageName, 0,
                                (int) dinfo.fileLength);
                        break;
                    case FAILED_NOEXIST:
                        mApkDownloadManager.removeDownload(dinfo);
                        // 取消下载任务
                        ReportConstants.reportDownloadStop(dinfo.appId, dinfo.packId,
                                ReportConstants.STAC_APP_POSITION_DOWNLOAD_MANAGER_APP,
                                ReportConstants.STAC_DOWNLOAD_APK_CANCEL_TASK, "");
                        MtaUtils.trackDownloadCancel(dinfo.appName);
                        break;
                }
            }
        }

    };

    /**
     * 是不是已经包含在activity中，防止调用getActivity为空
     * 
     * @return
     */
    private boolean isInActivity()
    {
        return getActivity() != null && !getActivity().isFinishing();
    }

    // 根据下载的数据量通知父界面更新ui
    public void noticeAppManageActivty(List<DownloadTask> mListData)
    {
        if (mHandler == null)
            return;

        if (mListData.size() <= 0)
        {
            mHandler.sendEmptyMessage(MsgDownload.DOWNLOAD_SIZE_EMPTY);
        }
        else
        {
            mHandler.sendEmptyMessage(MsgDownload.DOWNLOAD_SIZE_NOT_EMPTY);
        }
    }

    public void downloadAdapterCheckBoxChanged(boolean b)
    {
        // LogUtils.e(
        // "DownloadListFragment,downloadAdapterCheckBoxChanged , b = " + b );
        mAdapter.updateAllCheckBox(b);
        mAdapter.updateAllDownloadedCheckBox(b);
        mAdapter.finishUpdateAllCheckBoxNotChecked();
        mAdapter.finishDownloadedAllCheckBoxNotChecked();
    }

    public void updateAllCheckBoxNotChecked()
    {
        // LogUtils.e( "DownloadListFragment,updateAllCheckBoxNotChecked" );
        mAdapter.updateAllCheckBoxNotChecked();
    }

    protected class DownloadExpandListAdapter extends BaseExpandableListAdapter implements
            IUpdateSingleView<Integer>
    {

        public static final String TAG = "DownloadExpandListAdapter";
        private final ApkDownloadManager mApkDownloadManager = DownloadService.getDownloadManager();
        private final LayoutInflater mInflater;
        private final Context mContext;
        protected Resources mRes = null;
        private final ArrayList<ArrayList<DownloadTask>> groupTasks = new ArrayList<ArrayList<DownloadTask>>();
        private final ArrayList<String> mGroupNameList = new ArrayList<String>();//显示分组名

        private final static int CHECK_NETWORK_STATUS = 50001;
        private final static int CLEAN_STATUS = 50002;
        /**
         * 存放view的映射关系，用做单个view更新
         */
        protected HashMap<Integer, View> mViewMap = new HashMap<Integer, View>();
        protected HashMap<Integer, Boolean> mCheckBoxStatusMap = new HashMap<Integer, Boolean>();
        protected HashMap<Integer, Boolean> mDownLoadedCheckBoxStatusMap = new HashMap<Integer, Boolean>();

        public DownloadExpandListAdapter(Context context) {
            super();
            this.mContext = context;
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mRes = context.getResources();

            // 添加数据前先清空数据
            if (mDownloadListData != null) {
                mDownloadListData.clear();
            }
            if (mDownloadedTasks != null) {
                mDownloadedTasks.clear();
            }
            if (groupTasks != null) {
                groupTasks.clear();
            }
            /*emptyView.setVisibility(
            childPosition == 0 ? View.VISIBLE : View.GONE);*/
            mDownloadListData = (ArrayList<DownloadTask>) mApkDownloadManager.getTaskList();
            getDownloadedInfo();

            if (mDownloadListData.size() != 0 || mDownloadedTasks.size() != 0) {
                emptyView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.VISIBLE);
            }

            Logger.e(TAG, "mDownloadListData === " + (mDownloadListData.size() > 0));
            groupTasks.add(mDownloadListData);//添加正在下载数据

            Logger.e(TAG, "mDownloadedTasks === " + (mDownloadedTasks.size() > 0));
            groupTasks.add(mDownloadedTasks);//添加下载历史数据

            /* mList.expandGroup(0);
            mList.expandGroup(1);*/

            // 初始化 正在下载任务checkbox的状态
            for (DownloadTask di : mDownloadListData)
            {
                mCheckBoxStatusMap.put(di.appId, false);
            }

            // 初始化 下载历史任务checkbox的状态
            for (DownloadTask di : mDownloadedTasks)
            {
                mDownLoadedCheckBoxStatusMap.put(di.appId, false);
            }

        }

        private void getDownloadedInfo() {
            List<DownloadTask> list = mApkDownloadManager.getDownloadedTaskList();

            Logger.i(TAG, "getDownloadedInfo " + "size "+ list.size() + "  "+ list, "oddshou");
            if (null == list || list.size() == 0) {
                return;
            }
            Iterator<DownloadTask> iterator = list.iterator();
            while (iterator.hasNext()) {
                DownloadTask di = iterator.next();
                if (!mApkInstalledManager.isApkLocalInstalled(di.packageName))
                {
                    Logger.e(TAG, "移除任务  dinfo.appId = " + di.appId
                            + " dinfo.appName = "
                            + di.appName);
                    int row = 0;
                    try {
                        row = App.getAppContext().getContentResolver()
                                .delete(CSACContentProvider.DOWNLOADEDINFO_CONTENT_URI,
                                        CSACDatabaseHelper.DownloadInfoColumns.APP_ID + " = " + di.appId, null);
                        iterator.remove();
                        Logger.e(TAG, "移除任务  row === " + row);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            mDownloadedTasks.clear();
            mDownloadedTasks.addAll(list);

        }

        public void addGroupName(String groupName) {
            mGroupNameList.add(groupName);
        }

        @Override
        public int getGroupCount() {
            // TODO Auto-generated method stub
            return groupTasks.size();

        }

        @Override
        public int getChildrenCount(int groupPosition) {
            // TODO Auto-generated method stub
            if (groupTasks.size() == 0) {
                return 0;
            }
            if (groupTasks.get(groupPosition).isEmpty()) {
                return 0;
            } else {
                return groupTasks.get(groupPosition).size();
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            // TODO Auto-generated method stub
            return groupTasks.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return groupTasks.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            // TODO Auto-generated method stub
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                ViewGroup parent) {
            // TODO Auto-generated method stub
            View view = LinearLayout.inflate(mContext, R.layout.group_download_headview,
                    null);
            TextView nameView = (TextView) view.findViewById(R.id.category_title);
            if (getChildrenCount(groupPosition) == 0) {
                nameView.setVisibility(View.GONE);
            } else {
                nameView.setVisibility(View.VISIBLE);
                nameView.setText((mGroupNameList.size() > groupPosition ? (mGroupNameList
                        .get(groupPosition)) : ""));
            }
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                View convertView, ViewGroup parent) {

            Logger.i(TAG, "getChildView " + "groupPositon "+ groupPosition +"  "+ childPosition , "oddshou");
            // TODO Auto-generated method stub
            ViewHolder holder;
            final DownloadTask task = groupTasks.get(groupPosition).get(childPosition);// 数组越界问题
            if (convertView == null)
            {
                convertView = mInflater.inflate(R.layout.download_list_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            /* if (childPosition >= groupTasks.get(groupPosition).size())
             {
                 setViewForSingleUpdate(task.appId, convertView);
                 notifyDataSetChanged();
                 return convertView;
             }*/

            // emptyview
            convertView.findViewById(R.id.empty_item_view).setVisibility(
                    childPosition == 0 ? View.VISIBLE : View.GONE);
            // 改为id 缓存view,而不使用packName ,因为有可能出现同包名但不同版本的应用apk
            setViewForSingleUpdate(task.appId, convertView);
            bindData(holder, task, groupPosition);
            return convertView;
        }

        private void bindData(final ViewHolder holder, final DownloadTask info, int groupPosition)
        {

            ImageLoader.getInstance().displayImage(info.appIconURL, holder.appicon,
                    DisplayOptions.optionsIcon);

            // holder.appname.setText( info.appName );
            // holder.rating.setRating( info.rating / 2 );
            if (App.isDebugMode())
            {
                holder.appname.setText(info.appName + "(" + info.appDownloadURL + ")");
            }
            else
            {
                holder.appname.setText(info.appName);
            }

            int percent = 0;

            if (info.progress > info.fileLength)
            {
                info.progress = info.fileLength;
            }

            if (info.fileLength > 0)
            {
                percent = (int) (((double) info.progress / (double) info.fileLength) * 100);
            }
            holder.percent.setText(percent + " %");

            holder.downloadinfo.setText(StringUtils.byteToString(info.progress) + "/"
                    + StringUtils.byteToString(info.fileLength));

            // mAnimationInt = percent;

            holder.progress.setProgress(percent);

            // LogUtils.d( "info.fileLength = " + info.fileLength );

            holder.appsize.setText(StringUtils.byteToString(info.fileLength));
            holder.stateMsg.setText(info.stateMsg);
            // holder.downloadinfo.setText( "已下载：" + StringUtils.byteToString(
            // info.progress ) );

            holder.editcheck.setTag(info);

            /* // 设置是否是勾选
             if (mCheckBoxStatusMap.get(info.appId) == null)
             {
                 mCheckBoxStatusMap.put(info.appId, false);
                 holder.editcheck.setChecked(false);
             }
             else
             {
                 holder.editcheck.setChecked(mCheckBoxStatusMap.get(info.appId).booleanValue());
             }*/

            if (mListViewStatus == EDIT_STATUS)
            {
                // holder.downloadiconbtn.startAnimation(
                // AnimationUtils.loadAnimation( getActivity( ) ,
                // R.anim.fade_out ) );
                // holder.downloadlabel.startAnimation(
                // AnimationUtils.loadAnimation( getActivity( ) ,
                // R.anim.fade_out ) );
                holder.downloadiconbtn.setVisibility(View.INVISIBLE);
                holder.downloadlabel.setVisibility(View.GONE);
                //                holder.mAppOpenOrUpdateBtn.setVisibility(View.GONE);
                holder.editcheck.setVisibility(View.VISIBLE);
                // holder.editcheck.startAnimation(
                // AnimationUtils.loadAnimation( getActivity( ) , R.anim.fade_in
                // ) );

            }
            else if (mListViewStatus == NOTMAL_STATUS)
            {
                if (info.getState() == TaskState.INSTALLING)
                {
                    holder.downloadiconbtn.setVisibility(View.INVISIBLE);
                }
                else
                {
                    holder.downloadiconbtn.setVisibility(View.VISIBLE);
                }
                // holder.downloadiconbtn.startAnimation(
                // AnimationUtils.loadAnimation( getActivity( ) , R.anim.fade_in
                // ) );
                // holder.downloadlabel.startAnimation(
                // AnimationUtils.loadAnimation( getActivity( ) , R.anim.fade_in
                // ) );
                // holder.editcheck.startAnimation(
                // AnimationUtils.loadAnimation( getActivity( ) ,
                // R.anim.fade_out ) );
                holder.editcheck.setVisibility(View.GONE);
            }

            // check点击事件处理
            /* holder.editcheck.setOnClickListener(new OnClickListener()
             {

                 @Override
                 public void onClick(View v)
                 {

                     Message msg = Message.obtain();
                     if (!holder.editcheck.isChecked())
                     {
                         holder.editcheck.setChecked(false);
                         mCheckBoxStatusMap.put(info.appId, false);
                         msg.obj = holder.editcheck.getTag();
                         msg.what = AppManageActivity.DOWNLOAD_DEL_REMOVE;
                     }
                     else
                     {
                         holder.editcheck.setChecked(true);
                         mCheckBoxStatusMap.put(info.appId, true);
                         msg.obj = holder.editcheck.getTag();
                         msg.what = AppManageActivity.DOWNLOAD_DEL_ADD;
                     }
                     mHandler.sendMessage(msg);
                     // refreshDownloadList( );
                 }
             });*/

            // 同步checkBox事件
            holder.frameview.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mListViewStatus == EDIT_STATUS)
                    {
                        ((ViewHolder) v.getTag()).editcheck.performClick();
                    }
                }
            });

            // holder.downloadiconbtn.setOnClickListener( mDownloadClickListener
            // );
            holder.downloadiconbtn.setWifiLoadOnClickListener(mDownloadClickListener);
            // 把信息藏到button的tag里，点击事件和单独更新view的时候做比较用
            holder.downloadiconbtn.setTag(info);
//            holder.downloadiconbtn.setOnClickListener(new OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//
//                }
//            });
            holder.mAppOpenOrUpdateBtn.setWifiLoadOnClickListener(mOpenOrUpdateOnClickListener);

            // 立即处理点击事件
            holder.immediatehandle.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int status = (Integer) v.getTag();
                    if (status == CHECK_NETWORK_STATUS)
                    {
                        Intent toActivity = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        toActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(toActivity);
                    }
                    else if (status == CLEAN_STATUS)
                    {
                        Intent toActivity = new Intent(Settings.ACTION_MEMORY_CARD_SETTINGS);
                        toActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(toActivity);

                    }

                }
            });

            // 再次进入初始化状态
            updateProgress(info.appId, info.packageName, (int) info.progress, (int) info.fileLength);
            //            updateDownloadState(info);
            if (groupPosition == 0) {

                // 设置是否是勾选
                if (mCheckBoxStatusMap.get(info.appId) == null)
                {
                    mCheckBoxStatusMap.put(info.appId, false);
                    holder.editcheck.setChecked(false);
                }
                else
                {
                    holder.editcheck.setChecked(mCheckBoxStatusMap.get(info.appId).booleanValue());
                }

                // check点击事件处理   维护正在下载任务列表
                holder.editcheck.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {

                        Message msg = Message.obtain();
                        if (!holder.editcheck.isChecked())
                        {
                            holder.editcheck.setChecked(false);
                            mCheckBoxStatusMap.put(info.appId, false);
                            msg.obj = holder.editcheck.getTag();
                            msg.what = MsgDownload.DOWNLOAD_DEL_REMOVE;
                        }
                        else
                        {
                            holder.editcheck.setChecked(true);
                            mCheckBoxStatusMap.put(info.appId, true);
                            msg.obj = holder.editcheck.getTag();
                            msg.what = MsgDownload.DOWNLOAD_DEL_ADD;
                        }
                        mHandler.sendMessage(msg);
                        // refreshDownloadList( );
                    }
                });

                setDownloadState(info, holder, info.getState(), 0);
            } else {

                // 设置是否是勾选
                if (mDownLoadedCheckBoxStatusMap.get(info.appId) == null)
                {
                    mDownLoadedCheckBoxStatusMap.put(info.appId, false);
                    holder.editcheck.setChecked(false);
                }
                else
                {
                    holder.editcheck.setChecked(mDownLoadedCheckBoxStatusMap.get(info.appId)
                            .booleanValue());
                }

                // check点击事件处理   维护历史下载任务列表
                holder.editcheck.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {

                        Message msg = Message.obtain();
                        if (!holder.editcheck.isChecked())
                        {
                            holder.editcheck.setChecked(false);
                            mDownLoadedCheckBoxStatusMap.put(info.appId, false);
                            msg.obj = holder.editcheck.getTag();
                            msg.what = MsgDownload.HISTORY_DOWNLOAD_DEL_REMOVE;
                        }
                        else
                        {
                            holder.editcheck.setChecked(true);
                            mDownLoadedCheckBoxStatusMap.put(info.appId, true);
                            msg.obj = holder.editcheck.getTag();
                            msg.what = MsgDownload.HISTORY_DOWNLOAD_DEL_ADD;
                        }
                        mHandler.sendMessage(msg);
                        // refreshDownloadList( );
                    }
                });

                setDownloadState(info, holder, info.getState(), 1);
            }
        }

        private void setDownloadState(DownloadTask di, ViewHolder holder, TaskState state,
                int posotion)
        {
            // LogUtils.e( "更新任务：" + di.appName + "|" + di.packageName + "状态" +
            // di.getState( ) + "开始" );
            holder.stateMsg.setText(di.stateMsg);
            if (App.isDebugMode())
            {
                holder.appname.setText(di.appName + "(" + di.appDownloadURL + ")");
            }
            //            Logger.i(TAG, "setDownloadState "+ di.appName + " state "+ state, "oddshou");

            //#########oddshou 修改,判断在编辑模式下的状态更新和非编辑模式。
            if (mListViewStatus == EDIT_STATUS)
            {
                holder.downloadiconbtn.setVisibility(View.INVISIBLE);
                holder.downloadlabel.setVisibility(View.GONE);
                holder.editcheck.setVisibility(View.VISIBLE);
            }
            else if (mListViewStatus == NOTMAL_STATUS)
            {
                if (state == TaskState.INSTALLING)
                {
                    holder.downloadiconbtn.setVisibility(View.INVISIBLE);
                }
                else
                {
                    holder.downloadiconbtn.setVisibility(View.VISIBLE);
                }
                holder.editcheck.setVisibility(View.GONE);
            }

            if (posotion == 1) {
                final String packageName = di.packageName;
                //                Logger.e(TAG, "packageName===" + packageName + "   appName===" + di.appName);
                /*Logger.e(
                        TAG,
                        "isApkLocalInstalled==="
                                + mApkInstalledManager.isApkLocalInstalled(packageName));*/
                if (mApkInstalledManager.isApkLocalInstalled(packageName))
                {
                    downloadBtnToOpenOrUpdate(holder, packageName, di);
                    return;
                }
            }

            //            holder.editcheck.setVisibility(View.GONE);
            holder.progress.setVisibility(View.VISIBLE);
            holder.percent.setVisibility(View.INVISIBLE);
            holder.downloadinfo.setVisibility(View.VISIBLE);
            holder.msg1.setMaxEms(6);// msg1,downloadinfo都显示时设置msg1显示最大字符数
            holder.appsize.setVisibility(View.GONE);

            holder.msg1.setVisibility(View.VISIBLE);
            holder.msg2.setVisibility(View.GONE);
            holder.immediatehandle.setVisibility(View.GONE);

            //            holder.downloadiconbtn.setVisibility(View.VISIBLE);
            holder.downloadlabel.setVisibility(View.GONE);
            holder.remainTime.setVisibility(View.GONE);
            holder.mAppOpenOrUpdateBtn.setVisibility(View.GONE);

            switch (state)
            {
                case PREPARING:
                    holder.msg1.setTextColor(mRes.getColor(R.color.csl_black_4c));
                    holder.msg1.setText(R.string.download_tip_preparing);
                    holder.downloadiconbtn.setBackgroundResource(R.drawable.btn_green_selector);
                    holder.downloadiconbtn.setTextColor(mRes.getColorStateList(
                            R.color.btn_green_color));
                    holder.downloadiconbtn.setText(mRes.getString(R.string.app_pause));
                    holder.downloadiconbtn.setEnabled(true);
                    break;
                case WAITING:
                    holder.msg1.setTextColor(mRes.getColor(R.color.csl_black_4c));
                    holder.msg1.setText(R.string.download_tip_waiting);
                    holder.downloadiconbtn.setBackgroundResource(R.drawable.btn_green_selector);
                    holder.downloadiconbtn.setTextColor(mRes.getColorStateList(
                            R.color.btn_green_color));
                    holder.downloadiconbtn.setText(mRes.getString(R.string.app_pause));
                    holder.downloadiconbtn.setEnabled(true);
                    break;
                case STARTED:
                case LOADING:
                    holder.msg1.setTextColor(mRes.getColor(R.color.csl_black_4c));
                    holder.remainTime.setVisibility(View.VISIBLE);
                    holder.msg1.setText(R.string.download_tip_loading);
                    // holder.msg1.setVisibility( View.GONE );
                    holder.downloadiconbtn.setBackgroundResource(R.drawable.btn_green_selector);
                    holder.downloadiconbtn.setTextColor(mRes.getColorStateList(
                            R.color.btn_green_color));
                    holder.downloadiconbtn.setEnabled(true);
                    holder.downloadiconbtn.setText(mRes.getString(R.string.app_pause));
                    break;
                case STOPPED:
                    holder.msg1.setTextColor(mRes.getColor(R.color.csl_black_4c));
                    holder.msg1.setText(R.string.pauseing);
                    holder.downloadiconbtn.setBackgroundResource(R.drawable.btn_green_selector);
                    holder.downloadiconbtn.setTextColor(mRes.getColorStateList(
                            R.color.btn_green_color));
                    holder.downloadiconbtn.setEnabled(true);
                    holder.downloadiconbtn.setText(mRes.getString(R.string.app_resume));
                    break;
                case SUCCEEDED:
                    // holder.progress.setProgress( 100 );
                    // holder.percent.setText( "100 %" );
                    // 关闭动画
                    holder.msg1.setTextColor(mRes.getColor(R.color.csl_black_4c));
                    holder.progress.setVisibility(View.INVISIBLE);
                    holder.percent.setVisibility(View.INVISIBLE);
                    holder.downloadinfo.setVisibility(View.INVISIBLE);

//                    holder.appsize.setVisibility(View.VISIBLE);

                    holder.msg1.setText(R.string.installing_donwload);
                    holder.msg1.setMaxEms(holder.msg1.getText().length());
                    // holder.downloadinfo.setText( "已下载：" +
                    // StringUtils.byteToString( di.fileLength ) );
                    // holder.downloadlabel.setVisibility( View.GONE );
                    holder.downloadiconbtn.setBackgroundResource(R.drawable.btn_green_selector);
                    holder.downloadiconbtn.setTextColor(mRes.getColorStateList(
                            R.color.btn_green_color));
                    holder.downloadiconbtn.setEnabled(true);
                    holder.downloadiconbtn.setText(mRes.getString(R.string.app_install));
                    if (mApkInstalledManager.isApkLocalInstalled(di.packageName))
                    {
                        // 如果安装立即刷新列表
                        // add at 20140116
                        // LogUtils.e( "应用:" + di.appName + "|" + di.packageName
                        // +
                        // ",已安装" );
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case DELETED:
                    holder.msg1.setTextColor(mRes.getColor(R.color.csl_black_4c));
                    holder.msg1.setText(R.string.download_tip_file_deleted);
                    holder.downloadiconbtn.setBackgroundResource(R.drawable.btn_green_selector);
                    holder.downloadiconbtn.setTextColor(mRes.getColorStateList(
                            R.color.btn_green_color));
                    holder.downloadiconbtn.setEnabled(true);
                    holder.downloadiconbtn.setText(mRes.getString(R.string.app_redownload));
                    break;
                case INSTALLING:
                    // holder.progress.setProgress( 100 );
                    // holder.pecent.setText( "100%" );
                    // 关闭动画
                    // endAnimation( holder );
                    holder.msg1.setTextColor(mRes.getColor(R.color.csl_black_4c));
                    holder.progress.setVisibility(View.INVISIBLE);
                    holder.percent.setVisibility(View.INVISIBLE);
                    holder.downloadinfo.setVisibility(View.INVISIBLE);
//                    holder.appsize.setVisibility(View.VISIBLE);

                    holder.msg1.setText(R.string.installing_donwload);
                    holder.msg1.setMaxEms(holder.msg1.getText().length());
                    // holder.downloadinfo.setText( "已下载：" +
                    // StringUtils.byteToString( di.fileLength ) );
                    holder.downloadiconbtn.setVisibility(View.INVISIBLE);
                    holder.downloadiconbtn.setBackgroundResource(R.drawable.btn_green_selector);
                    holder.downloadiconbtn.setTextColor(mRes.getColorStateList(
                            R.color.btn_green_color));
                    holder.downloadiconbtn.setEnabled(false);
                    if (mListViewStatus == NOTMAL_STATUS)
                    {
                        holder.downloadlabel.setVisibility(View.VISIBLE);
                    }
                    holder.downloadlabel.setText(mRes.getString(R.string.app_installing));
                    break;
                case FAILED_NETWORK:
                    holder.msg1.setText(R.string.download_tip_network_error1);
                    holder.msg1.setTextColor(mRes.getColor(R.color.csl_black_4c));
                    holder.downloadiconbtn.setBackgroundResource(R.drawable.btn_green_selector);
                    holder.downloadiconbtn.setTextColor(mRes.getColorStateList(
                            R.color.btn_green_color));
                    holder.downloadiconbtn.setEnabled(true);
                    holder.downloadiconbtn.setText(mRes.getString(R.string.app_retry));

                    holder.msg2.setText(R.string.download_tip_network_error2);
                    holder.msg2.setVisibility(View.VISIBLE);
                    holder.msg2.setTextColor(mRes.getColor(R.color.csl_black_4c));

                    holder.immediatehandle.setVisibility(View.VISIBLE);
                    holder.immediatehandle.setText(Html.fromHtml("<u color='green'>"
                            + mRes.getString(R.string.check_network) + "</u>"));
                    holder.immediatehandle.setTag(CHECK_NETWORK_STATUS);
                    break;
                case FAILED_BROKEN:
                    holder.msg1.setTextColor(mRes.getColor(R.color.csl_black_4c));
                    holder.msg1.setText(R.string.download_tip_file_error);
                    holder.downloadiconbtn.setBackgroundResource(R.drawable.btn_green_selector);
                    holder.downloadiconbtn.setTextColor(mRes.getColorStateList(
                            R.color.btn_green_color));
                    holder.downloadiconbtn.setEnabled(true);
                    holder.downloadiconbtn.setText(mRes.getString(R.string.app_redownload));

                    break;
                case FAILED_NOEXIST:
                    holder.msg1.setTextColor(mRes.getColor(R.color.csl_black_4c));
                    holder.msg1.setText(R.string.download_tip_file_noexist);
                    holder.downloadiconbtn.setBackgroundResource(R.drawable.btn_gray_selector);
                    holder.downloadiconbtn.setTextColor(mRes.getColorStateList(
                            R.color.btn_gray_color));
                    holder.downloadiconbtn.setEnabled(true);
                    holder.downloadiconbtn.setText(mRes.getString(R.string.app_delete));
                    break;
                case FAILED_SERVER:
                    holder.msg1.setTextColor(mRes.getColor(R.color.csl_black_4c));
                    holder.msg1.setText(R.string.download_tip_server_error);
                    holder.downloadiconbtn.setBackgroundResource(R.drawable.btn_green_selector);
                    holder.downloadiconbtn.setTextColor(mRes.getColorStateList(
                            R.color.btn_green_color));
                    holder.downloadiconbtn.setEnabled(true);
                    holder.downloadiconbtn.setText(mRes.getString(R.string.app_retry));
                    break;
                case FAILED_NOFREESPACE:
                    holder.msg1.setText(R.string.download_tip_no_freesapce1);
                    holder.msg1.setTextColor(mRes.getColor(R.color.red));
                    holder.downloadiconbtn.setBackgroundResource(R.drawable.btn_green_selector);
                    holder.downloadiconbtn.setTextColor(mRes.getColorStateList(
                            R.color.btn_green_color));
                    holder.downloadiconbtn.setEnabled(true);
                    holder.downloadiconbtn.setText(mRes.getString(R.string.app_retry));
                    //存储空间不足，立即清理后重试， #####oddshou 修改为  存储空间不足
                    holder.msg2.setVisibility(View.GONE);
                    holder.msg2.setText(R.string.download_tip_no_freesapce2);
                    holder.msg2.setTextColor(mRes.getColor(R.color.red));
                    holder.immediatehandle.setVisibility(View.GONE);
                    holder.immediatehandle.setText(Html.fromHtml("<u color='green'>"
                            + mRes.getString(R.string.immediate_clean) + "</u>"));
                    holder.immediatehandle.setTag(CLEAN_STATUS);

                    break;
                default:
                    break;
            }
        }

        private final WifiDownLoadOnClickListener mOpenOrUpdateOnClickListener = new WifiDownLoadOnClickListener()
        {

            @Override
            public void onWifiClickListener(View v)
            {
                //                sendDownloadTaskCountChangeNotify();

                // Log.d(TAG, "mOpenOrUpdateOnClickListener onWifiClickListener ");
                Object obj = v.getTag();
                if (obj instanceof String[])
                {
                    String[] args = (String[]) obj;
                    Intent intent = new Intent("android.intent.action.MAIN");
                    LogUtils.e(args[0] + "|" + args[1]);
                    intent.setClassName(args[1], args[0]);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
                else if (obj instanceof DownloadTask)
                {
                    Logger.i(TAG,
                            "mOpenOrUpdateOnClickListener DownloadTask === " + obj.toString());
//                    mApkDownloadManager.removeDownload((DownloadTask) obj);
                    mApkDownloadManager.removeLoadedTask((DownloadTask) obj);
//                    mApkDownloadManager.startUpgradeDownload((DownloadTask) obj);
                    startUpdate((DownloadTask) obj);
                }
            }
        };

        private void startUpdate(DownloadTask task){
            ArrayList<Apps.AppInfo> mListData = mApkInstalledManager.getAppUpdateInfo();
            for (Apps.AppInfo ai : mListData) {
                if(ai.packName.equals(task.packageName) && ai.appId == task.appId){
                    mApkDownloadManager
                            .startUpgradeDownload(
                                    ai,
                                    ReportConstants.STAC_APP_POSITION_DOWNLOAD_MANAGER_APP);
                }
            }
        }

        public void downloadBtnToOpenOrUpdate(ViewHolder holder, String packName, DownloadTask di)
        {
//            public RoundCornerImageView appicon;
//            public TextView appname;
//            public AlwaysMarqueeTextView msg1;
//            public TextView msg2;
//            public TextView immediatehandle;
//            // public RatingBar rating;
//            public ProgressBar progress;
//            public DownloadAnimationView animation;
//            public TextView appsize;
//            public TextView downloadinfo;
//            public TextView percent;
//
//            public CheckBox editcheck;
//            public DownloadListButton downloadiconbtn;
//            private DownloadListButton mAppOpenOrUpdateBtn;
//            public TextView downloadlabel;
//            public TextView stateMsg;
//            public TextView remainTime;

            // LogUtils.e("应用:" + packName + ",已安装且未在下载队列中。");
            holder.msg2.setVisibility(View.GONE);
            holder.immediatehandle.setVisibility(View.GONE);
            holder.downloadiconbtn.setVisibility(View.GONE);
            holder.downloadlabel.setVisibility(View.GONE);
            holder.progress.setVisibility(View.GONE);
            holder.percent.setVisibility(View.GONE);
            holder.stateMsg.setVisibility(View.GONE);
            holder.remainTime.setVisibility(View.GONE);
            holder.appsize.setVisibility(View.GONE);
            holder.msg1.setVisibility(View.GONE);
            holder.downloadinfo.setVisibility(View.GONE);
            holder.downloadlabel.setText(mRes.getString(R.string.app_installed));

            if (mListViewStatus == EDIT_STATUS)
            {
                holder.mAppOpenOrUpdateBtn.setVisibility(View.GONE);

            }
            else if (mListViewStatus == NOTMAL_STATUS)
            {
                holder.mAppOpenOrUpdateBtn.setVisibility(View.VISIBLE);
            }

            if (mApkInstalledManager.isApkNeedToUpdate(packName))
            {
                holder.mAppOpenOrUpdateBtn.setText(mContext.getString(R.string.app_update));
                holder.mAppOpenOrUpdateBtn.setBackgroundResource(R.drawable.btn_green_selector);
                holder.mAppOpenOrUpdateBtn.setTextColor(mRes
                        .getColorStateList(R.color.btn_green_color));
                holder.mAppOpenOrUpdateBtn.setTag(di);

            }
            else
            {
                holder.mAppOpenOrUpdateBtn.setText(mContext.getString(R.string.app_open));
                holder.mAppOpenOrUpdateBtn.setBackgroundResource(R.drawable.btn_gray_selector);
                holder.mAppOpenOrUpdateBtn.setTextColor(mRes
                        .getColorStateList(R.color.btn_gray_color));

                // LogUtils.e("activity class name = "
                // + mApkInstalledManager.getActivityClassName(packName));
                // LogUtils.e("packname = " + packName);
                holder.mAppOpenOrUpdateBtn.setTag(new String[] {
                        mApkInstalledManager.getActivityClassName(packName), packName
                });
            }
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public View getViewByKey(Integer key) {
            // TODO Auto-generated method stub
            View view = mViewMap.get(key);
            return view;
        }

        @Override
        public void setViewForSingleUpdate(Integer key, View view) {
            // TODO Auto-generated method stub
            if (view == null)
            {
                return;
            }
            mViewMap.put(key, view);
        }

        @Override
        public void removeViewForSingleUpdate(View view) {
            // TODO Auto-generated method stub

        }

        @Override
        public void removeViewForSingleUpdate(Integer key) {
            // TODO Auto-generated method stub
            mViewMap.remove(key);
        }

        class ViewHolder
        {
            public View frameview;
            // public View emptyview;
            public RoundCornerImageView appicon;
            public TextView appname;
            public AlwaysMarqueeTextView msg1;
            public TextView msg2;
            public TextView immediatehandle;
            // public RatingBar rating;
            public ProgressBar progress;
            public DownloadAnimationView animation;
            public TextView appsize;
            public TextView downloadinfo;
            public TextView percent;

            public CheckBox editcheck;
            public DownloadListButton downloadiconbtn;
            private DownloadListButton mAppOpenOrUpdateBtn;
            public TextView downloadlabel;
            public TextView stateMsg;
            public TextView remainTime;

            public ViewHolder(View view) {
                frameview = view.findViewById(R.id.frame_view);
                // holder.emptyview = view.findViewById( R.id.empty_item_view );
                appicon = (RoundCornerImageView) view.findViewById(R.id.app_icon);
                appname = (TextView) view.findViewById(R.id.app_name);
                msg1 = (AlwaysMarqueeTextView) view.findViewById(R.id.download_msg1);
                msg2 = (TextView) view.findViewById(R.id.download_msg2);

                immediatehandle = (TextView) view.findViewById(R.id.immediate_handle);
                // 下载进度条
                progress = (ProgressBar) view.findViewById(R.id.download_progress);

                animation = (DownloadAnimationView) view.findViewById(R.id.animation_view);
                appsize = (TextView) view.findViewById(R.id.app_size);
                downloadinfo = (TextView) view.findViewById(R.id.download_info);
                percent = (TextView) view.findViewById(R.id.percent);
                editcheck = (CheckBox) view.findViewById(R.id.app_manager_edit_check);
                downloadiconbtn = (DownloadListButton) view.findViewById(R.id.download_icon_btn);
                mAppOpenOrUpdateBtn = (DownloadListButton) view.findViewById(R.id.app_openorupdate);
                downloadlabel = (TextView) view.findViewById(R.id.download_label);
                stateMsg = (TextView) view.findViewById(R.id.state_msg);
                remainTime = (TextView) view.findViewById(R.id.remaintime_and_speed);
            }

        }

        public void updateProgress(int appId, String packagename, int dlsize, int totalsize)
        {
            View view = getViewByKey(appId);
            if (view == null)
            {
                return;
            }
            // view.setOnLongClickListener( listLongClickListener );
            ViewHolder holder = (ViewHolder) view.getTag();
            DownloadTask di = (DownloadTask) holder.downloadiconbtn.getTag();
            if (di == null) {
                return;
            }
            if (packagename.equals(di.packageName) && appId == di.appId)
            {
                // TODO 百分比计算的地方

                if (dlsize > totalsize)
                {
                    dlsize = totalsize;
                }

                int percent = (int) (((double) dlsize / (double) totalsize) * 100);
                if (percent >= 100)
                {
                    percent = 100;
                }

                holder.stateMsg.setText(di.stateMsg);
                holder.progress.setProgress(percent);
                holder.appsize.setText(StringUtils.byteToString(totalsize));
                holder.percent.setText(percent + " %");
                holder.downloadinfo.setText(StringUtils.byteToStringNoUnit(dlsize) + "/"
                        + StringUtils.byteToString(totalsize));

                String remainStr = DateUtil.remainTimeByTimeFormat(di.remainSeconds);
                holder.remainTime.setText(StringUtils.byteToString(di.avgSpeed) + "/s");// +
                                                                                        // "  "
                                                                                        // +
                                                                                        // remainStr
            }
        }

        public void updateDownloadState(DownloadTask di)
        {
            // LogUtils.e(
            // "当前列表的状态(NOTMAL_STATUS:3002|EDIT_STATUS:3001) ,mListViewStatus = "
            // + mListViewStatus );

            //            Logger.e(TAG, "setDownloadState " + di.appName + " state " + di.getState(), "oddshou");
            View view = getViewByKey(di.appId);
            if (view == null)
            {
                return;
            }
            ViewHolder holder = (ViewHolder) view.getTag();
            DownloadTask item = (DownloadTask) holder.downloadiconbtn.getTag();
//            DownloadTask item2 = (DownloadTask) holder.mAppOpenOrUpdateBtn.getTag();
            if (di.packageName.equals(item.packageName) && di.appId == item.appId)
            {
                //                if (mListViewStatus == NOTMAL_STATUS)
                //                {
                //                    setDownloadState(di, holder, di.getState());
                //                }else{
                //                    //编辑 模式 下更新 状态
                //                    Logger.e(TAG, "setDownloadState 异常 "+ mListViewStatus, "oddshou");
                //                }
                setDownloadState(di, holder, di.getState(), 0);
            } else {
//                setDownloadState(di, holder, di.getState(), 1);
                //出现这个异常的话, 所有状态的更新 都会出错
                /*Logger.e(TAG, "setDownloadState 异常  " + di.packageName + " item.packageName "
                        + item.packageName + " appid " + di.appId + " item.appid " + item.appId,
                        "oddshou");
                Logger.e(TAG, "setDownloadState 异常  " + di.appName + " item.appName "
                        + item.appName + " appid " + di.appId + " item.appid " + item.appId,
                        "oddshou");*/

            }

//            if(null != item2){
//                if (di.packageName.equals(item2.packageName) && di.appId == item2.appId)
//                {
//                    setDownloadState(di, holder, di.getState(), 1);
//                }
//            }

            // //当请求成功后显示文件的大小
            // if( di.getState( ) == TaskState.LOADING || di.getState( ) ==
            // TaskState.PREPARING || di.getState( ) == TaskState.WAITING )
            // {
            // if( "0B".equals( holder.appsize.getText( ).toString( ) ) )
            // {
            // holder.appsize.setText( StringUtils.byteToString( di.fileLength )
            // );
            // }
            // }
        }

        public void updateAllCheckBox(boolean b)
        {

            // LogUtils.e( "updateAllCheckBox before = " + mListViewStatus );
            mListViewStatus = b ? EDIT_STATUS : NOTMAL_STATUS;
            // LogUtils.e( "updateAllCheckBox after = " + mListViewStatus );
            // LogUtils.e( "updateAllCheckBox mListData = " + mListData.size( )
            // );
            for (DownloadTask di : mDownloadListData)
            {
                updateCheckBox(di, b);
            }
        }

        public void updateAllDownloadedCheckBox(boolean b)
        {

            // LogUtils.e( "updateAllCheckBox before = " + mListViewStatus );
            mDownloadedListViewStatus = b ? EDIT_STATUS : NOTMAL_STATUS;
            // LogUtils.e( "updateAllCheckBox after = " + mListViewStatus );
            // LogUtils.e( "updateAllCheckBox mListData = " + mListData.size( )
            // );
            for (DownloadTask di : mDownloadedTasks)
            {
                updateCheckBox(di, b, 0);//holder.mAppOpenOrUpdateBtn
            }
        }

        public void updateCheckBox(DownloadTask di, boolean b)
        {

            // LogUtils.e( "DownloadListFragment ,updateCheckBox,b=" + b );
            // LogUtils.e( "DownloadListFragment ,updateCheckBox,di.appId=" +
            // di.appId );
            // LogUtils.e(
            // "DownloadListFragment ,updateCheckBox,mListViewStatus=" +
            // mListViewStatus );
            View view = getViewByKey(di.appId);
            // LogUtils.e( "DownloadListFragment ,di.appId=" + di.appId );
            if (view == null)
            {
                return;
            }
            // LogUtils.e( "DownloadListFragment ,view=" + view.toString( ) );
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.editcheck.setVisibility(b ? View.VISIBLE : View.GONE);
            if (di.getState() == TaskState.INSTALLING && !b)
            {
                holder.downloadlabel.setVisibility(View.VISIBLE);
                holder.downloadiconbtn.setVisibility(View.INVISIBLE);
            }
            else
            {
                holder.downloadlabel.setVisibility(View.GONE);
                holder.downloadiconbtn.setVisibility(b ? View.INVISIBLE : View.VISIBLE);
            }

        }

        /** 重载方法，供已下载列表使用，第三个参数无意义 */
        public void updateCheckBox(DownloadTask di, boolean b, int flag)
        {

            // LogUtils.e( "DownloadListFragment ,updateCheckBox,b=" + b );
            // LogUtils.e( "DownloadListFragment ,updateCheckBox,di.appId=" +
            // di.appId );
            // LogUtils.e(
            // "DownloadListFragment ,updateCheckBox,mListViewStatus=" +
            // mListViewStatus );
            View view = getViewByKey(di.appId);
            // LogUtils.e( "DownloadListFragment ,di.appId=" + di.appId );
            if (view == null)
            {
                return;
            }
            // LogUtils.e( "DownloadListFragment ,view=" + view.toString( ) );
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.editcheck.setVisibility(b ? View.VISIBLE : View.GONE);
            if (di.getState() == TaskState.INSTALLING && !b)
            {
                holder.downloadlabel.setVisibility(View.VISIBLE);
                holder.downloadiconbtn.setVisibility(View.INVISIBLE);
                holder.mAppOpenOrUpdateBtn.setVisibility(View.INVISIBLE);
            }
            else
            {
                holder.downloadlabel.setVisibility(View.GONE);
                holder.downloadiconbtn.setVisibility(b ? View.INVISIBLE : View.VISIBLE);
                holder.mAppOpenOrUpdateBtn.setVisibility(b ? View.INVISIBLE : View.VISIBLE);
            }

        }

        public void finishUpdateAllCheckBoxNotChecked()
        {

            // LogUtils.e(
            // "DownloadListFragment , finishUpdateAllCheckBoxNotChecked di.appId"
            // );
            for (DownloadTask di : mDownloadListData)
            {
                View view = getViewByKey(di.appId);
                if (view == null)
                {
                    return;
                }
                ViewHolder holder = (ViewHolder) view.getTag();

                holder.editcheck.setChecked(false);
                mCheckBoxStatusMap.put(di.appId, false);
                // if( holder.downloadlabel.isShown( ) )
                // {
                // holder.downloadlabel.setVisibility( View.GONE );
                // }
                // if( holder.downloadiconbtn.isShown( ) )
                // {
                // holder.downloadiconbtn.setVisibility( View.GONE );
                // }
            }
        }

        public void finishDownloadedAllCheckBoxNotChecked()
        {

            // LogUtils.e(
            // "DownloadListFragment , finishUpdateAllCheckBoxNotChecked di.appId"
            // );
            for (DownloadTask di : mDownloadedTasks)
            {
                View view = getViewByKey(di.appId);
                if (view == null)
                {
                    return;
                }
                ViewHolder holder = (ViewHolder) view.getTag();

                holder.editcheck.setChecked(false);
                mCheckBoxStatusMap.put(di.appId, false);
                // if( holder.downloadlabel.isShown( ) )
                // {
                // holder.downloadlabel.setVisibility( View.GONE );
                // }
                // if( holder.downloadiconbtn.isShown( ) )
                // {
                // holder.downloadiconbtn.setVisibility( View.GONE );
                // }
            }
        }

        public void updateAllCheckBoxNotChecked()
        {

            // LogUtils.e(
            // "DownloadListFragment , updateAllCheckBoxNotChecked di.appId" );
            for (DownloadTask di : mDownloadListData)
            {
                View view = getViewByKey(di.appId);
                if (view == null)
                {
                    return;
                }
                ViewHolder holder = (ViewHolder) view.getTag();
                if (di.getState() != TaskState.INSTALLING)
                {
                    holder.editcheck.setChecked(false);
                    mCheckBoxStatusMap.put(di.appId, false);
                }
                // if( holder.downloadlabel.isShown( ) )
                // {
                // holder.downloadlabel.setVisibility( View.GONE );
                // }
                // if( holder.downloadiconbtn.isShown( ) )
                // {
                // holder.downloadiconbtn.setVisibility( View.GONE );
                // }
            }

            for (DownloadTask di : mDownloadedTasks)
            {
                View view = getViewByKey(di.appId);
                if (view == null)
                {
                    return;
                }
                ViewHolder holder = (ViewHolder) view.getTag();
                holder.mAppOpenOrUpdateBtn.setVisibility(View.GONE);
                if (di.getState() != TaskState.INSTALLING)
                {
                    holder.editcheck.setChecked(false);
                    mDownLoadedCheckBoxStatusMap.put(di.appId, false);
                }
                // if( holder.downloadlabel.isShown( ) )
                // {
                // holder.downloadlabel.setVisibility( View.GONE );
                // }
                // if( holder.downloadiconbtn.isShown( ) )
                // {
                // holder.downloadiconbtn.setVisibility( View.GONE );
                // }
            }
        }

        @Override
        public void notifyDataSetChanged()
        {
            // 添加数据前先清空数据
            Logger.e(TAG,
                    "notifyDataSetChanged mDownloadedTasks   11=== " + mDownloadedTasks.size());
            if (mDownloadedTasks != null) {
                mDownloadedTasks.clear();
            }
            if (groupTasks != null) {
                groupTasks.clear();
            }
            Logger.e(TAG, "notifyDataSetChanged mDownloadedTasks  22=== " + mDownloadedTasks.size());

            mDownloadListData = (ArrayList<DownloadTask>) mApkDownloadManager.getTaskList();
            Iterator<DownloadTask> iterator = mDownloadListData.iterator();
            while (iterator.hasNext()) {
                DownloadTask di = iterator.next();
                if (mCheckBoxStatusMap.get(di.appId) == null)
                {
                    mCheckBoxStatusMap.put(di.appId, false);
                }

            }
            groupTasks.add(mDownloadListData);//添加正在下载数据

            //            mDownloadedTasks = mApkDownloadManager.getDownloadedTaskList();
            getDownloadedInfo();
            Logger.e(TAG, "notifyDataSetChanged mDownloadedTasks  33=== " + mDownloadedTasks.size());

            if (mDownloadListData.size() != 0 || mDownloadedTasks.size() != 0) {
                emptyView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.VISIBLE);
            }
            groupTasks.add(mDownloadedTasks);//添加下载历史数据
            super.notifyDataSetChanged();
        }

    }

    public void refreshDownloadList()
    {
        if (mAdapter != null) {
            Logger.e("DownloadExpandListAdapter", "refreshDownloadList ");
            mAdapter.notifyDataSetChanged();
        }
    }

    // public boolean isInPatchMode()
    // {
    // return mListMode == MODE_PATCH;
    // }

    // public void quitPatchMode()
    // {
    // mListMode = MODE_NORMAL;
    // if( mAdapter != null )
    // mAdapter.notifyDataSetChanged( );
    // }

    /*
     * (non-Javadoc)
     * @see
     * com.niuwan.gamecenter.download.IDownloadTaskStateListener#onUpdateTaskProgress
     * (com.niuwan.gamecenter.download.DownloadTask)
     */
    @Override
    public void onUpdateTaskProgress(DownloadTask task)
    {
        if (isInActivity() == false || task == null || mAdapter == null || isVisible() == false)
            return;

        mAdapter.updateProgress(task.appId, task.packageName, (int) task.progress,
                (int) task.fileLength);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.niuwan.gamecenter.download.IDownloadTaskStateListener#onUpdateTaskState
     * (com.niuwan.gamecenter.download.DownloadTask)
     */
    @Override
    public void onUpdateTaskState(DownloadTask task)
    {
        // LogUtils.d( "task=" + task );
        // LogUtils.e( "isInActivity( )=" + isInActivity( ) + "|task=" + task +
        // "|mAdapter=" + mAdapter + "|isVisible=" + isVisible( ) );
        if (isInActivity() == false || task == null || mAdapter == null || isVisible() == false)
            return;
                Logger.i(TAG, "onUpdateTaskState " +task, "tom");
        //        mAdapter.updateDownloadState(task);
        mAdapter.updateDownloadState(task);

        //#########oddshou 若是改用 全部更新，则不会出现状态异常，但是非常影响 性能
        //        mAdapter.notifyDataSetChanged();
    }

    /*
     * (non-Javadoc)
     * @see
     * com.niuwan.gamecenter.download.IDownloadTaskStateListener#onUpdateTaskList()
     */
    @Override
    public void onUpdateTaskList(Object obj)
    {
        if (isInActivity() == false || mAdapter == null || isVisible() == false)
            return;
        Logger.e("DownloadExpandListAdapter", "onUpdateTaskList ");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDownloadTaskCountChange()
    {

        // TODO Auto-generated method stub
        if (mAdapter != null)
        {
            Logger.e("DownloadExpandListAdapter", "onDownloadTaskCountChange ");
            mAdapter.notifyDataSetChanged();
        }
    }
}
