
package com.hykj.gamecenter.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.GroupAppListActivity;
import com.hykj.gamecenter.activity.GroupListActivity;
import com.hykj.gamecenter.controller.ProtocolListener.GROUP_CLASS;
import com.hykj.gamecenter.controller.ProtocolListener.GROUP_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.ITEM_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.KEY;
import com.hykj.gamecenter.controller.ProtocolListener.MAIN_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.ORDER_BY;
import com.hykj.gamecenter.data.GroupInfo;
import com.hykj.gamecenter.data.TopicInfo;
import com.hykj.gamecenter.db.CSACDatabaseHelper;
import com.hykj.gamecenter.db.DatabaseUtils;
import com.hykj.gamecenter.logic.DisplayOptions;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.protocol.Reported.ReportedInfo;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.HorizonScrollLayout.OnTouchScrollListener;
import com.hykj.gamecenter.utils.Interface.IFragmentInfo;
import com.hykj.gamecenter.utils.Interface.IRecommendAdvInfo;
import com.hykj.gamecenter.utilscs.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * @author greatzhang
 */

public class HomeCirculeRecommendAdvView implements IRecommendAdvInfo {
    private static final String TAG = "HomeCirculeRecommendAdvView";

    private static final int MSG_HOME_CIRCULE = 0;

    public static final int MSG_HOME_REFRESH = 1;

    private static final int TIME_CIRCULE_START = 4 * 1000;

    private View mBtnEntryFavourite = null;
    private View mBtnEntryNewestapp = null;
    private View mBtnEntrySubject = null;
    private View mBtnEntryEssential = null;

    String mTabLabel = null; // 当前数据那个fragment

    /**
     * 从第几个位置开始循环标志
     */

    private CirculeAdvListener listener;

    private DotProgressBar mDotProgressBar;
//    private DotProgressBar mDotProgressBar2;
    private DotProgressBar mDotProgressBar3;
    private HorizonScrollLayout mHorizonScrollLayout;

    // 四个相同Id的ImageView的横竖屏下的父布局
    private LinearLayout mEntryategoryLinearLayout;
    private View mEntryategoryPortaitLinearLayout;
    private FrameLayout mAdvInfoFrameLayout;

    RelativeLayout relativeLayoutAdv;
    private TextView mAdvDesc;
    private TextView mAdvName;
    // private ImageView mAdvVirtual;
    private Context context;

    private boolean isCircule = true;

    private int infoSize = 0;
    private Resources mRes = null;
    private int mCurrentPage = 0;

    public void setmCurrentPage(int mCurrentPage) {
        this.mCurrentPage = mCurrentPage;
    }

    public interface CirculeAdvListener {
        void onCirCuleAdvListener(int i, GroupElemInfo info);
    }

    @SuppressLint("HandlerLeak")
    private Handler mScrollHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // 暂停消息发送的时候，不需处理
            switch (msg.what) {
                case MSG_HOME_CIRCULE:
                    if (isCircule && infoSize > 0) {
                        if (mScrollHandler != null) {
                            // TODO 循环下个列表
                            circuleAdv();
                            mScrollHandler.sendEmptyMessageDelayed(
                                    MSG_HOME_CIRCULE, TIME_CIRCULE_START);
                        }
                    }
                    break;
                case MSG_HOME_REFRESH:
                    /*if (!UITools.isPortrait()) {
                        mEntryategoryLinearLayout.setVisibility(View.VISIBLE);
                        mEntryategoryPortaitLinearLayout.setVisibility(View.GONE);
                        mDotProgressBar3.setVisibility(View.VISIBLE);
                        mDotProgressBar.setVisibility(View.GONE);
                        mDotProgressBar3.setCurProgress(getCurScreen());

                        mBtnEntryFavourite = (ImageView) mEntryategoryLinearLayout
                                .findViewById(R.id.entry_favourite);
                        mBtnEntryFavourite
                                .setOnClickListener(mHotAppsClickListener);
                        mBtnEntryNewestapp = (ImageView) mEntryategoryLinearLayout
                                .findViewById(R.id.entry_newestapp);
                        mBtnEntryNewestapp
                                .setOnClickListener(mNewestAppsClickListener);
                        mBtnEntrySubject = (ImageView) mEntryategoryLinearLayout
                                .findViewById(R.id.entry_subject);
                        mBtnEntrySubject.setOnClickListener(mSubjectClickListener);
                        mBtnEntryEssential = (ImageView) mEntryategoryLinearLayout
                                .findViewById(R.id.entry_essential);
                        mBtnEntryEssential
                                .setOnClickListener(mRequiredClickListener);
                    } else {

                    }*/
                        mEntryategoryLinearLayout.setVisibility(View.GONE);
                        mEntryategoryPortaitLinearLayout
                                .setVisibility(View.VISIBLE);
                        mDotProgressBar3.setVisibility(View.GONE);
                        mDotProgressBar.setVisibility(View.VISIBLE);
                        mDotProgressBar.setCurProgress(getCurScreen());

                        mBtnEntryFavourite =  mEntryategoryPortaitLinearLayout
                                .findViewById(R.id.entry_favourite);
                        mBtnEntryFavourite
                                .setOnClickListener(mHotAppsClickListener);
                        mBtnEntryNewestapp =  mEntryategoryPortaitLinearLayout
                                .findViewById(R.id.entry_newestapp);
                        mBtnEntryNewestapp
                                .setOnClickListener(mNewestAppsClickListener);
                        mBtnEntrySubject =  mEntryategoryPortaitLinearLayout
                                .findViewById(R.id.entry_subject);
                        mBtnEntrySubject.setOnClickListener(mSubjectClickListener);
                        mBtnEntryEssential =  mEntryategoryPortaitLinearLayout
                                .findViewById(R.id.entry_essential);
                        mBtnEntryEssential
                                .setOnClickListener(mRequiredClickListener);

                    break;
                default:
                    break;
            }
        }
    };

    public Handler getHandler() {
        return mScrollHandler;
    }

    OnClickListener mSubjectClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent intentGroupList = new Intent(context,
                    GroupListActivity.class);
            int GROUPCLASS = GROUP_CLASS.SUBJECT_CLASS;// 主题类别
            if (mTabLabel != null
                    && mTabLabel
                            .equals(IFragmentInfo.FragmentTabLabel.RECOM_LABEL)) {

                GROUPCLASS = GROUP_CLASS.SUBJECT_CLASS;
                intentGroupList.putExtra(StatisticManager.APP_POS_TYPE, ReportConstants.STATIS_TYPE.RECOM);

            } else if (mTabLabel != null
                    && mTabLabel
                            .equals(IFragmentInfo.FragmentTabLabel.GAME_LABEL)) {
                GROUPCLASS = GROUP_CLASS.GAME_SUBJECT_CLASS;
                intentGroupList.putExtra(StatisticManager.APP_POS_TYPE, ReportConstants.STATIS_TYPE.GAME);
            }


            intentGroupList.putExtra(GroupListActivity.KEY_TITLE,
                    mRes.getString(R.string.subjectapps));

            intentGroupList.putExtra(KEY.SUBJECT_APPLIST, true);
            intentGroupList.putExtra(KEY.GROUP_CLASS, GROUPCLASS);

            intentGroupList.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // 广告位点击上报
            ReportedInfo build = new ReportedInfo();
            build.statActId = ReportConstants.STATACT_ID_ADV;
            build.statActId2 = 2; //首页类型导航按钮
            build.ext1 = "" + 3;

            ReportConstants.getInstance().reportReportedInfo(build);

            context.startActivity(intentGroupList);
        }
    };

    OnClickListener mRequiredClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent intentAppList = new Intent(context,
                    GroupAppListActivity.class);
            int GROUPTYPE = GROUP_TYPE.MUST_APP_TYPE;
            int MAINTYPE = MAIN_TYPE.ALL;
            if (mTabLabel != null
                    && mTabLabel
                            .equals(IFragmentInfo.FragmentTabLabel.RECOM_LABEL)) {
                GROUPTYPE = GROUP_TYPE.MUST_APP_TYPE;
                MAINTYPE = MAIN_TYPE.ALL;
                intentAppList.putExtra(StatisticManager.APP_POS_TYPE, ReportConstants.STATIS_TYPE.RECOM);
            } else if (mTabLabel != null
                    && mTabLabel
                            .equals(IFragmentInfo.FragmentTabLabel.GAME_LABEL)) {
                GROUPTYPE = GROUP_TYPE.MUST_GAMES_TYPE;
                MAINTYPE = MAIN_TYPE.GAME;
                intentAppList.putExtra(StatisticManager.APP_POS_TYPE, ReportConstants.STATIS_TYPE.GAME);
            }

            int ints[] = DatabaseUtils.getGroupIdByDB(GROUPTYPE,
                    ORDER_BY.AUTO);

            GroupInfo mGroupInfo = DatabaseUtils.getGroupInfo(GROUPTYPE);

            TopicInfo topicInfo = new TopicInfo();
            if (mGroupInfo != null) {
                topicInfo.mAppCount = mGroupInfo.recommWrod;
                topicInfo.mTopic = mGroupInfo.groupName;
                topicInfo.mTip = mGroupInfo.groupDesc;
                topicInfo.mPicUrl = mGroupInfo.groupPicUrl;
            }



            if (!topicInfo.mPicUrl.equals("")) {
                intentAppList.putExtra(KEY.TOPIC_INFO, topicInfo);
                intentAppList.putExtra(KEY.MAIN_TYPE, MAIN_TYPE.TOPIC);// 不是这个就不会显示主题图片
                intentAppList
                        .putExtra(KEY.ITEM_TYPE, ITEM_TYPE.UNSHOW_SNAPSHOT);
            } else {
                intentAppList.putExtra(KEY.MAIN_TYPE, MAINTYPE);
            }

            intentAppList.putExtra(KEY.SUBJECT_APPLIST, true);
            intentAppList.putExtra(KEY.GROUP_ID, ints[0]);
            intentAppList.putExtra(KEY.GROUP_CLASS, ints[1]);
            intentAppList.putExtra(KEY.GROUP_TYPE, ints[2]);
            intentAppList.putExtra(KEY.ORDERBY, ints[3]);
            intentAppList.putExtra(KEY.CATEGORY_NAME,
                    mRes.getString(R.string.requiredapps));

            intentAppList.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // 广告位点击上报
            ReportedInfo build = new ReportedInfo();
            build.statActId = ReportConstants.STATACT_ID_ADV;
            build.statActId2 = 2; //首页类型导航按钮
            build.ext1 = "" + 4;

            ReportConstants.getInstance().reportReportedInfo(build);
            context.startActivity(intentAppList);
        }
    };

    OnClickListener mHotAppsClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent intentAppList = new Intent(context,
                    GroupAppListActivity.class);
            int GROUPTYPE = GROUP_TYPE.ALL_APP_AND_GAME_TYPE;
            int MAINTYPE = MAIN_TYPE.ALL;
            if (mTabLabel != null
                    && mTabLabel
                            .equals(IFragmentInfo.FragmentTabLabel.RECOM_LABEL)) {

                MAINTYPE = MAIN_TYPE.ALL;
                GROUPTYPE = GROUP_TYPE.NICE_APP_TYPE;
                intentAppList.putExtra(StatisticManager.APP_POS_TYPE, ReportConstants.STATIS_TYPE.RECOM_NICE_LIST);
            } else if (mTabLabel != null
                    && mTabLabel
                            .equals(IFragmentInfo.FragmentTabLabel.GAME_LABEL)) {
                MAINTYPE = MAIN_TYPE.GAME;
                GROUPTYPE = GROUP_TYPE.NICE_GAME_TYPE;
                intentAppList.putExtra(StatisticManager.APP_POS_TYPE, ReportConstants.STATIS_TYPE.GAME_HOTEST_LIST);
            }

            GroupInfo mGroupInfo = DatabaseUtils.getGroupInfo(GROUPTYPE);
            if (null == mGroupInfo) {
                return;
            }

            TopicInfo topicInfo = new TopicInfo();
            topicInfo.mAppCount = mGroupInfo.recommWrod;
            topicInfo.mTopic = mGroupInfo.groupName;
            topicInfo.mTip = mGroupInfo.groupDesc;
            topicInfo.mPicUrl = mGroupInfo.groupPicUrl;



            if (!topicInfo.mPicUrl.equals("")) {
                intentAppList.putExtra(KEY.TOPIC_INFO, topicInfo);
                intentAppList.putExtra(KEY.MAIN_TYPE, MAIN_TYPE.TOPIC);// 不是这个就不会显示主题图片
                intentAppList
                        .putExtra(KEY.ITEM_TYPE, ITEM_TYPE.UNSHOW_SNAPSHOT);
            } else {
                intentAppList.putExtra(KEY.MAIN_TYPE, MAINTYPE);
            }

            intentAppList.putExtra(KEY.SUBJECT_APPLIST, true);
            intentAppList.putExtra(KEY.GROUP_ID, mGroupInfo.groupId);
            intentAppList.putExtra(KEY.GROUP_CLASS, mGroupInfo.groupClass);
            intentAppList.putExtra(KEY.GROUP_TYPE, mGroupInfo.groupType);
            intentAppList.putExtra(KEY.ORDERBY, mGroupInfo.orderType);
            intentAppList.putExtra(KEY.CATEGORY_NAME,
                    mRes.getString(R.string.hotapps));

            intentAppList.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // 广告位点击上报
            ReportedInfo build = new ReportedInfo();
            build.statActId = ReportConstants.STATACT_ID_ADV;
            build.statActId2 = 2; //首页类型导航按钮
            build.ext1 = "" + 1;

            ReportConstants.getInstance().reportReportedInfo(build);

            context.startActivity(intentAppList);

        }
    };

    OnClickListener mNewestAppsClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intentAppList = new Intent(context,
                    GroupAppListActivity.class);
            // TODO Auto-generated method stub
            int GROUPTYPE = GROUP_TYPE.ALL_APP_AND_GAME_TYPE;
            int MAINTYPE = MAIN_TYPE.ALL;
            if (mTabLabel != null
                    && mTabLabel
                            .equals(IFragmentInfo.FragmentTabLabel.RECOM_LABEL)) {

                MAINTYPE = MAIN_TYPE.ALL;
                GROUPTYPE = GROUP_TYPE.ALL_APP_AND_GAME_TYPE;
                intentAppList.putExtra(StatisticManager.APP_POS_TYPE, ReportConstants.STATIS_TYPE.RECOM_NEWEST_LIST);
            } else if (mTabLabel != null
                    && mTabLabel
                            .equals(IFragmentInfo.FragmentTabLabel.GAME_LABEL)) {
                MAINTYPE = MAIN_TYPE.GAME;
                GROUPTYPE = GROUP_TYPE.ALL_ONLY_GAMES_TYPE;
                intentAppList.putExtra(StatisticManager.APP_POS_TYPE, ReportConstants.STATIS_TYPE.GAME_NEWEST_LIST);
            }

            int ints[] = DatabaseUtils.getGroupIdByDB(GROUPTYPE,
                    ORDER_BY.TIME);

            String selection = CSACDatabaseHelper.GroupInfoColumns.GROUP_TYPE + " =? and " + CSACDatabaseHelper.GroupInfoColumns.ORDER_TYPE + " =?";
            String[] selectionArgs = new String[]{GROUPTYPE + "", ORDER_BY.TIME + ""};
            GroupInfo mGroupInfo = DatabaseUtils.getGroupinfoByDB(selection, selectionArgs);

//            GroupInfo mGroupInfo = DatabaseUtils.getGroupInfo(GROUPTYPE);
            TopicInfo topicInfo = new TopicInfo();
            if (mGroupInfo != null) {
                topicInfo.mAppCount = mGroupInfo.recommWrod;
                topicInfo.mTopic = mGroupInfo.groupName;
                topicInfo.mTip = mGroupInfo.groupDesc;
                topicInfo.mPicUrl = mGroupInfo.groupPicUrl;
            } else {
                topicInfo.mAppCount = "";
                topicInfo.mTopic = "";
                topicInfo.mTip = "";
                topicInfo.mPicUrl = "";
            }



            if (!topicInfo.mPicUrl.equals("")) {
                intentAppList.putExtra(KEY.TOPIC_INFO, topicInfo);
                intentAppList.putExtra(KEY.MAIN_TYPE, MAIN_TYPE.TOPIC);// 不是这个就不会显示主题图片
                intentAppList
                        .putExtra(KEY.ITEM_TYPE, ITEM_TYPE.UNSHOW_SNAPSHOT);
            } else {
                intentAppList.putExtra(KEY.MAIN_TYPE, MAINTYPE);
            }

            intentAppList.putExtra(KEY.SUBJECT_APPLIST, true);
            intentAppList.putExtra(KEY.GROUP_ID, ints[0]);
            intentAppList.putExtra(KEY.GROUP_CLASS, ints[1]);
            intentAppList.putExtra(KEY.GROUP_TYPE, ints[2]);
            intentAppList.putExtra(KEY.ORDERBY, ints[3]);
            intentAppList.putExtra(KEY.CATEGORY_NAME,
                    mRes.getString(R.string.newestapps));

            intentAppList.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // 广告位点击上报
            ReportedInfo build = new ReportedInfo();
            build.statActId = ReportConstants.STATACT_ID_ADV;
            build.statActId2 = 2; //首页类型导航按钮
            build.ext1 = "" + 2;

            ReportConstants.getInstance().reportReportedInfo(build);

            context.startActivity(intentAppList);
        }
    };

    public HomeCirculeRecommendAdvView(View view, final Context context,
            String sTabLabel) {
        // infoSize = infos.size( );
        this.context = context;
        mRes = context.getResources();
        mAdvInfoFrameLayout = (FrameLayout) view
                .findViewById(R.id.adv_info_framelayout);
        mAdvDesc = (TextView) view.findViewById(R.id.adv_desc);
        mAdvName = (TextView) view.findViewById(R.id.adv_name);
        mTabLabel = sTabLabel;
        relativeLayoutAdv = (RelativeLayout) view
                .findViewById(R.id.relativeLayoutAdv);//图片区

        // 横屏时 入口类别
        mEntryategoryLinearLayout = (LinearLayout) view
                .findViewById(R.id.entry_category);

        // 竖屏时 入口类别
        mEntryategoryPortaitLinearLayout = view
                .findViewById(R.id.layout_portait_category);

        // mAdvVirtual = (ImageView)view.findViewById( R.id.adv_virtual );

        // ################  取消横竖屏
        /*if (!UITools.isPortrait()) {
            // 横屏
            // mAdvInfoFrameLayout.setVisibility(View.VISIBLE);
            mEntryategoryLinearLayout.setVisibility(View.VISIBLE);
            mEntryategoryPortaitLinearLayout.setVisibility(View.GONE);

            mBtnEntryFavourite = (ImageView) mEntryategoryLinearLayout
                    .findViewById(R.id.entry_favourite);
            mBtnEntryFavourite.setOnClickListener(mHotAppsClickListener);
            mBtnEntryNewestapp = (ImageView) mEntryategoryLinearLayout
                    .findViewById(R.id.entry_newestapp);
            mBtnEntryNewestapp.setOnClickListener(mNewestAppsClickListener);
            mBtnEntrySubject = (ImageView) mEntryategoryLinearLayout
                    .findViewById(R.id.entry_subject);
            mBtnEntrySubject.setOnClickListener(mSubjectClickListener);
            mBtnEntryEssential = (ImageView) mEntryategoryLinearLayout
                    .findViewById(R.id.entry_essential);
            mBtnEntryEssential.setOnClickListener(mRequiredClickListener);
         } else {

         }*/
        // ################  tomqian

        // mAdvInfoFrameLayout.setVisibility(View.GONE);
        mEntryategoryLinearLayout.setVisibility(View.GONE);
        mEntryategoryPortaitLinearLayout.setVisibility(View.VISIBLE);

        mBtnEntryFavourite =  mEntryategoryPortaitLinearLayout
                    .findViewById(R.id.entry_favourite);
        mBtnEntryFavourite.setOnClickListener(mHotAppsClickListener);
        mBtnEntryNewestapp =  mEntryategoryPortaitLinearLayout
                    .findViewById(R.id.entry_newestapp);
        mBtnEntryNewestapp.setOnClickListener(mNewestAppsClickListener);
        mBtnEntrySubject =  mEntryategoryPortaitLinearLayout
                    .findViewById(R.id.entry_subject);
        mBtnEntrySubject.setOnClickListener(mSubjectClickListener);
        mBtnEntryEssential =  mEntryategoryPortaitLinearLayout
                    .findViewById(R.id.entry_essential);
        mBtnEntryEssential.setOnClickListener(mRequiredClickListener);

        // 捕获touch事件
        /*mAdvInfoFrameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });*/

        mHorizonScrollLayout = (HorizonScrollLayout) view
                .findViewById(R.id.top_ad_1);
        mHorizonScrollLayout.setEnableOverScroll(false);
        mHorizonScrollLayout.setLockAllWhenTouch(true);
        mHorizonScrollLayout.setScrollSlop(1.75f);
        mHorizonScrollLayout.setCircle(true);

        // 竖屏的时候启用
        mDotProgressBar = (DotProgressBar) view.findViewById(R.id.top_ad_dot);
        // mDotProgressBar.setTotalNum( infos.size( ) );
        mDotProgressBar.setDotbarIconResource(
                R.drawable.home_scroll_ad_dot_white,
                R.drawable.home_scroll_ad_dot_black);

        // 原 横屏的时候启用
//        mDotProgressBar2 = (DotProgressBar) view.findViewById(R.id.top_ad_dot2);
//        mDotProgressBar2.setDotbarIconResource(
//                R.drawable.home_scroll_ad_dot_white,
//                R.drawable.home_scroll_ad_dot_black);

        // 现 横屏的时候启用
        mDotProgressBar3 = (DotProgressBar) view.findViewById(R.id.top_ad_dot3);
        mDotProgressBar3.setDotbarIconResource(
                R.drawable.home_scroll_ad_dot_white,
                R.drawable.home_scroll_ad_dot_black);

        // ################  取消横竖屏
       /* if (!UITools.isPortrait()) {

            mDotProgressBar3.setVisibility(View.VISIBLE);
            mDotProgressBar.setVisibility(View.GONE);

        } else {

        }*/
        // ################  tomqian

        mDotProgressBar.setVisibility(View.VISIBLE);
        mDotProgressBar3.setVisibility(View.GONE);


        mHorizonScrollLayout
                .setOnTouchScrollListener(new OnTouchScrollListener() {

                    @Override
                    public void onScrollStateChanged(int scrollState,
                            int currentScreem) {
                        // 当手滚动广告位是, 不播放
                        if (OnTouchScrollListener.SCROLL_STATE_TOUCH_SCROLL == scrollState) {
                            // 滑动过程中停止循环
                            stopCricule();
                        } else if (OnTouchScrollListener.SCROLL_STATE_IDLE == scrollState) {
                            // 滑动暂停循环
                            startCricle();
                        }
                    }

                    @Override
                    public void onScroll(View view, float leftX,
                            float screemWidth) {
                    }

                    @Override
                    public void onScreenChange(int displayScreem, Object obj) {

                        /*if (!UITools.isPortrait()) {

                            mDotProgressBar3.setCurProgress(displayScreem);
                        } else {
                        }*/
                        mDotProgressBar.setCurProgress(displayScreem);

                        // 改变遮罩层中的内容
                        if (obj == null)
                            return;
                        GroupElemInfo elemInfo = (GroupElemInfo) obj;
                        mAdvName.setText(elemInfo.showName);
                        mAdvDesc.setText(getAdcDesc(elemInfo.recommWord));
                    }
                });

    }

    // 对游戏内容进行分行处理
    private String getAdcDesc(String advdesc) {
        StringBuffer sbf = new StringBuffer("");
        if (!"".equals(advdesc) && null != advdesc) {
            int descLength = advdesc.length();
            int start = 0;
            int line = descLength / 10;
            if (line == 0) {
                return advdesc;
            }
            for (int i = 1; i <= line; i++) {
                sbf.append(advdesc.substring(start, i * 10)).append("\n");
                start = i * 10;
            }
            sbf.append(advdesc.substring(start));
        }

        return sbf.toString();
    }

    // private void refreshScrollView()
    // {
    //
    // mHorizonScrollLayout.removeAllViews( );
    //
    // LogUtils.e( "linkedListCache size = " + linkedListCache.size( ) );
    //
    // for( GroupElemInfo ginfo : linkedListCache )
    // {
    // LogUtils.e( "ginfo's appId = " + ginfo.getAppId( ) );
    // }
    //
    // LayoutInflater inflater = (LayoutInflater)context.getSystemService(
    // Context.LAYOUT_INFLATER_SERVICE );
    // this.infoSize = linkedListCache.size( );
    //
    // mDotProgressBar.setTotalNum( linkedListCache.size( ) );
    // mDotProgressBar.setDotbarNum( linkedListCache.size( ) );
    //
    // for( int i = 0 ; i < linkedListCache.size( ) ; i++ )
    // {
    // final int j = i;
    // final GroupElemInfo info = linkedListCache.get( i );
    // ViewGroup view = (ViewGroup)inflater.inflate( R.layout.home_adv_child ,
    // null );
    // mHorizonScrollLayout.addView( view );
    // ImageView imageView = (ImageView)view.findViewById(
    // R.id.home_first_adv_child );
    // ImageLoader.getInstance( ).displayImage( info.getAdsPicUrl( ) , imageView
    // , DisplayOptions.optionBigMapHomepage );
    //
    // // if(!UITools.isPortrait( ) && i == 0){
    // // ImageLoader.getInstance( ).displayImage( info.getAdsPicUrl( ) ,
    // mAdvVirtual , DisplayOptions.optionBigMapHomepage );
    // // }
    //
    // view.setOnClickListener( new OnClickListener( )
    // {
    // @Override
    // public void onClick( View v )
    // {
    // listener.onCirCuleAdvListener( j , info );
    // }
    // } );
    // }
    // }

    public void addAdvListener(CirculeAdvListener listener) {
        this.listener = listener;
    }

    private void circuleAdv() {

        // LogUtils.e( "circuleAdv 循环播放！" );
        if (infoSize > 0) {
            mHorizonScrollLayout.displayNextScreen();
            /*if (!UITools.isPortrait()) {
                mDotProgressBar3.setCurProgress(getCurScreen());
            } else {
            }*/
            mDotProgressBar.setCurProgress(getCurScreen());

        }
    }

    public int mAdvCount = 0;

    @Override
    public int getRecommendAdvCount() {
        /* Log.d(TAG, "mAdvCount = " + mAdvCount); */
        return mAdvCount;
    }

    public void scrollViewAddData(ArrayList<GroupElemInfo> infos, int current) {
        // mBeforeScreen = cureen;
        // LogUtils.e( "mBeforeScreen =" + mBeforeScreen );
        // LinkedList< GroupElemInfo > groupInfos = init( infos , cureen );
        LogUtils.e("scrollViewAddData,往轮播当中设置数据！");
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.infoSize = infos.size();
        mAdvCount = this.infoSize;
//        mDotProgressBar2.setTotalNum(infos.size());
//        mDotProgressBar2.setDotbarNum(infos.size());
        mDotProgressBar3.setTotalNum(infos.size());
        mDotProgressBar3.setDotbarNum(infos.size());
        mDotProgressBar.setTotalNum(infos.size());
        mDotProgressBar.setDotbarNum(infos.size());

        mScrollHandler.sendEmptyMessage(MSG_HOME_REFRESH);

        for (int i = 0; i < infos.size(); i++) {
            final int j = i;
            final GroupElemInfo info = infos.get(i);

            if (i == current) {
                mAdvName.setText(info.showName);
                mAdvDesc.setText(getAdcDesc(info.recommWord));
            }

            ViewGroup view = (ViewGroup) inflater.inflate(
                    R.layout.home_adv_child, null);
            mHorizonScrollLayout.addView(view);
            ImageView imageView = (ImageView) view
                    .findViewById(R.id.home_first_adv_child);
            ImageLoader.getInstance().displayImage(info.adsPicUrl,
                    imageView, DisplayOptions.optionBigMapHomepage);
            view.setTag(info);

            // if(!UITools.isPortrait( ) && i == 0){
            // ImageLoader.getInstance( ).displayImage( info.getAdsPicUrl( ) ,
            // mAdvVirtual , DisplayOptions.optionBigMapHomepage );
            // }

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCirCuleAdvListener(j, info);
                }
            });
        }

    }

    // //初始化
    // private LinkedList< GroupElemInfo > linkedListCache = new LinkedList<
    // GroupElemInfo >( );
    //
    // public LinkedList< GroupElemInfo > init( ArrayList< GroupElemInfo > infos
    // , int screen )
    // {
    // LogUtils.e( "现在是第几屏,screen =" + screen );
    // for( GroupElemInfo gInfo : infos )
    // {
    // linkedListCache.add( gInfo );
    // }
    //
    // for( GroupElemInfo ginfo : linkedListCache )
    // {
    //
    // LogUtils.e( "ginfo's appId = " + ginfo.getAppId( ) );
    // }
    //
    // if( linkedListCache.size( ) > 0 )
    // {
    // for( int i = 1 ; i <= screen ; i++ )
    // {
    // GroupElemInfo gei = linkedListCache.getFirst( );
    // linkedListCache.removeFirst( );
    // linkedListCache.add( gei );
    // }
    // }
    //
    // for( GroupElemInfo ginfo : linkedListCache )
    // {
    //
    // LogUtils.e( "ginfo's appId = " + ginfo.getAppId( ) );
    // }
    // return linkedListCache;
    // }
    //
    // //获取
    // public void getGroupInfos( boolean b )
    // {
    //
    // if( linkedListCache.size( ) > 0 )
    // {
    // if( b )
    // {
    // GroupElemInfo finfo = linkedListCache.getFirst( );
    // linkedListCache.removeFirst( );
    // linkedListCache.add( finfo );
    // }
    // else
    // {
    // GroupElemInfo linfo = linkedListCache.getLast( );
    // linkedListCache.removeLast( );
    // linkedListCache.addFirst( linfo );
    // }
    // }
    //
    // }

    public void stopCricule() {
        LogUtils.e("stopCricule");
        isCircule = false;
        if (mScrollHandler != null) {
            mScrollHandler.removeMessages(MSG_HOME_CIRCULE);
        }
    }

    public boolean isCricle() {
        return isCircule;
    }

    private void startCricle() {
        isCircule = true;
        if (mScrollHandler != null) {
            mScrollHandler.removeMessages(MSG_HOME_CIRCULE);
            mScrollHandler.sendEmptyMessageDelayed(MSG_HOME_CIRCULE,
                    TIME_CIRCULE_START);
        }
    }

    public void setCurScreen(int position) {
        mHorizonScrollLayout.setDefaultScreem(position);
        /*if (!UITools.isPortrait()) {
            mDotProgressBar3.setCurProgress(position);
        } else {
        }*/
        mDotProgressBar.setCurProgress(position);

    }

    /**
     * 得到当前是第几个屏幕
     * 
     * @return
     */
    public int getCurScreen() {
        return mHorizonScrollLayout.getCurScreen();
    }

    /**
     * 回复首页第一个广告位的循环
     */
    public void onReply() {
        LogUtils.e("onReply");
        startCricle();
    }

    /**
     * 停止广告位的循环动作
     */
    public void destoryCirclue() {
        mScrollHandler = null;
    }

}
