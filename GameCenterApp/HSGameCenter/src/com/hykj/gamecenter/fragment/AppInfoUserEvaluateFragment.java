
package com.hykj.gamecenter.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.protobuf.nano.MessageNano;
import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.PhoneAppInfoActivity;
import com.hykj.gamecenter.activity.WriteCommentActivity;
import com.hykj.gamecenter.adapter.UserCommentListAdapter;
import com.hykj.gamecenter.controller.ProtocolListener.PAGE_SIZE;
import com.hykj.gamecenter.controller.ProtocolListener.ReqUserCommentsListener;
import com.hykj.gamecenter.controller.ProtocolListener.ReqUserScoreInfoListener;
import com.hykj.gamecenter.controller.ReqUserCommentsListController;
import com.hykj.gamecenter.controller.ReqUserScoreInfoController;
import com.hykj.gamecenter.download.ApkDownloadManager;
import com.hykj.gamecenter.download.DownloadService;
import com.hykj.gamecenter.logic.ApkInstalledManager;
import com.hykj.gamecenter.protocol.Apps.AppInfo;
import com.hykj.gamecenter.protocol.Apps.UserCommentInfo;
import com.hykj.gamecenter.protocol.Apps.UserScoreInfo;
import com.hykj.gamecenter.ui.widget.CSLoadingUIListView;
import com.hykj.gamecenter.ui.widget.CSPagerSlidingTabStrip.OnDoubleClickTabListener;
import com.hykj.gamecenter.ui.widget.CSPullListView.ICSListViewListener;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.ui.widget.ICSLoadingViewListener;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.Tools;

import java.util.ArrayList;
import java.util.List;

public class AppInfoUserEvaluateFragment extends Fragment {
    private final static String TAG = "AppInfoUserEvaluateFragment";
    private final ApkInstalledManager mApkInstalledManager = ApkInstalledManager
            .getInstance();
    private ApkDownloadManager mApkDownloadManager;
    private View mainView;
    private CSLoadingUIListView mListView;
    private UserCommentListAdapter mAdapter;
    private UserScoreInfo userInfo = null;
    //    private View mNoUserComment;
    private View mUserCommentHeader;
    private UserCommentInfo submitUserCommentInfo = null;

    private int appId;
    private int verCode;
    private String verName;
    private String packName;
    private String showName;
    private AppInfo mAppInfo;
    private static final int COMMENT_REQUEST_CODE = 3000;

    private int mCurrentPage = 1;
    private final List<UserCommentInfo> userCommentList = new ArrayList<UserCommentInfo>();
    private final List<UserCommentInfo> backUpUserCommentList = new ArrayList<UserCommentInfo>();
    private View headerGrade;
    private UserGradeHolder headerGradeHolder;
    private boolean isFirstSubmit = true;
    private int lastScore;
    private Context mContext;
    private int commentInfoPos = -1; // 用于记录当前设备的评论在评论列表中的位置

    private boolean isReqUserScoreInfoSucceed = false;
    private boolean isReqUserCommentsSucceed = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // reqUserScore( );
        mContext = getActivity();
        mApkDownloadManager = DownloadService.getDownloadManager();

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.app_user_evaluate, container,
                false);

        OnClickListener listener = new CommentButtonListener();

        /*    mNoUserComment = mainView.findViewById(R.id.no_user_comment);
            Button commentButton = (Button) mNoUserComment
                    .findViewById(R.id.commentBuuton);
            commentButton.setOnClickListener(listener);*/

        mListView = (CSLoadingUIListView) mainView.findViewById(R.id.app_list);
        mListView.setCSListViewListener(mCSListViewListener);
        mListView.setCSLoadingViewListener(mCSLoadingViewListener);
        mListView.setFooterPullEnable(true);
        mListView.setHeaderPullEnable(false);

        /*  View header = inflater.inflate(R.layout.user_comment_list_header, null);
          headerGrade = header.findViewById(R.id.user_grade);
          headerGradeHolder = new UserGradeHolder(headerGrade);
          headerGradeHolder.commentButton.setOnClickListener(listener);

          mListView.addListHeaderView(header);*/
        // initRequestData( );
        return mainView;
    }

    public void setData(AppInfo appInfo) {
        mAppInfo = appInfo;

        if (mAppInfo != null && isInActivity()) {
            appId = mAppInfo.appId;
            verCode = mAppInfo.verCode;
            verName = mAppInfo.verName;
            packName = mAppInfo.packName;
            showName = mAppInfo.showName;
            bindData();
        } else {
            return;
        }
        // mListView.initRequestData( );
    }

    public void bindData() {
        mAdapter = new UserCommentListAdapter(mContext, verCode);
        mListView.setAdapter(mAdapter);
        mListView.initRequestData();
    }

    public final OnDoubleClickTabListener mOnDoubleClickTabListener = new OnDoubleClickTabListener() {

        @Override
        public void onDoubleClickTabToHandle(int position) {
            mListView.setSelection(0);
        }

    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity != null) {
//            if (App.getDevicesType() == App.PHONE)
                ((PhoneAppInfoActivity) activity)
                        .setmUserEvaluateFragment(this);
//            else
//                ((PadAppInfoActivity) activity).setmUserEvaluateFragment(this);
        }
    }

    private class CommentButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (!mApkInstalledManager.isApkLocalInstalled(packName)) {
                Logger.d(TAG, "appId : " + appId + " 未安装");
                CSToast.show(App.getAppContext(),
                        getString(R.string.app_user_comment_tip));
                // mUserCommentDialog = new UserCommentDialog( getActivity( ) ,
                // getString( R.string.app_user_comment_tip ) );
                // mUserCommentDialog.setLintener( new CommentDialogListener( )
                // );
                // mUserCommentDialog.show( );
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("appId", appId);
            intent.putExtra("verCode", verCode);
            intent.putExtra("verName", verName);
            intent.putExtra("showName", showName);
            if (submitUserCommentInfo != null) {
                Logger.d(TAG, "submitUserCommentInfo : "
                        + submitUserCommentInfo);
                intent.putExtra("userCommentInfo",
                        MessageNano.toByteArray(submitUserCommentInfo));
            } else if (userInfo != null && userInfo.userCommentInfo != null) {
                Logger.d(
                        TAG,
                        "userInfo.getUserCommentInfo : "
                                + userInfo.userCommentInfo);
                intent.putExtra("userCommentInfo", MessageNano.toByteArray(userInfo));
            }
            intent.setClass(mContext, WriteCommentActivity.class);
            startActivityForResult(intent, COMMENT_REQUEST_CODE);

        }
    }

    // private class CommentDialogListener implements View.OnClickListener
    // {
    //
    // @Override
    // public void onClick( View v )
    // {
    // switch ( v.getId( ) )
    // {
    // case R.id.download :
    // //TODO
    // mApkDownloadManager.startDownload( mAppInfo , 0 );
    // mUserCommentDialog.dismiss( );
    // break;
    // case R.id.cancel :
    // mUserCommentDialog.dismiss( );
    // break;
    // default :
    // break;
    // }
    //
    // }
    //
    // }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Logger.d(TAG, "onActivityResult");
        if (requestCode == COMMENT_REQUEST_CODE
                && resultCode == WriteCommentActivity.SUBMIT_RESULT_CODE) {
            /*UserCommentInfo.Builder builder = UserCommentInfo.newBuilder();
            builder.setUserId(0);
            builder.setUserName(data.getStringExtra("userName"));
            builder.setUserScore(data.getIntExtra("starRating", 0));
            Logger.d(TAG, "commentTime: " + data.getStringExtra("commentTime"));
            builder.setComments(data.getStringExtra("commentContent"));
            builder.setCommentTime(data.getStringExtra("commentTime"));
            builder.setLocalVerCode(verCode);
            submitUserCommentInfo = builder.build();*/
            UserCommentInfo info = new UserCommentInfo();
            info.userId = 0;
            info.userName = data.getStringExtra("userName");
            info.userScore = data.getIntExtra("starRating", 0);
            info.comments = data.getStringExtra("commentContent");
            info.commentTime = data.getStringExtra("commentTime");
            info.localVerCode = verCode;
            submitUserCommentInfo = info;

            updateUserScoreInfo(data.getIntExtra("starRating", 0));

            if (mAdapter != null && mListView != null) {
                // 用于判断下发的用户评论列表中是否存在当前设备上次提交的评论，如果存在，则用户再次评论的时候会覆盖它
                if (commentInfoPos != -1) {
                    mAdapter.removeData(commentInfoPos);
                    removeCommentInfo();
                    commentInfoPos = -1;
                }
                mAdapter.setIsFirstSubmit(isFirstSubmit);
                isFirstSubmit = false;
                mAdapter.appendFirstData(submitUserCommentInfo);
                userCommentList.add(submitUserCommentInfo);
                mAdapter.notifyDataSetChanged();
                refreshUI();
                Logger.d(TAG, "---------appendFirstData");
            }
            // CSToast.show( getActivity( ) , getString(
            // R.string.app_user_comment_submit_toast ) );
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // 移除评分栏中，服务器下发的当前设备上次提交的评论参数
    private void removeCommentInfo() {
        /* if (userInfo == null || !userInfo.hasUserCommentInfo())
             return;
         UserCommentInfo info = userInfo.getUserCommentInfo();
         UserScoreInfo.Builder builder = UserScoreInfo.newBuilder(userInfo);
         builder.setCommentTimes(builder.build().getCommentTimes() - 1);
         builder.setScoreTimes(builder.build().getScoreTimes() - 1);
         builder.setScoreSum(builder.build().getScoreSum() - info.getUserScore());
         updateScoreTimes(info.getUserScore(), builder);
         userInfo = builder.build();*/
    }

    /*private void updateScoreTimes(int score, UserScoreInfo.Builder builder) {
        switch (score) {
            case 5:
                builder.setScoreTime5(builder.build().getScoreTime5() - 1);
                break;
            case 4:
                builder.setScoreTime4(builder.build().getScoreTime4() - 1);

                break;
            case 3:
                builder.setScoreTime3(builder.build().getScoreTime3() - 1);

                break;
            case 2:
                builder.setScoreTime2(builder.build().getScoreTime2() - 1);

                break;
            case 1:
                builder.setScoreTime1(builder.build().getScoreTime1() - 1);

                break;

            default:
                break;
        }
    }*/

    private void updateScoreTimes(int score, UserScoreInfo builder) {
        switch (score) {
            case 5:
                builder.scoreTime5 = (builder.scoreTime5 - 1);
                break;
            case 4:
                builder.scoreTime4 = (builder.scoreTime4 - 1);

                break;
            case 3:
                builder.scoreTime3 = (builder.scoreTime3 - 1);

                break;
            case 2:
                builder.scoreTime2 = (builder.scoreTime2 - 1);

                break;
            case 1:
                builder.scoreTime1 = (builder.scoreTime1 - 1);

                break;

            default:
                break;
        }
    }

    // 当提交评论的时候，更新用户评分栏的相关参数
    private void updateUserScoreInfo(int score) {
        UserScoreInfo info = new UserScoreInfo();
        if (isFirstSubmit) {
            info.commentTimes = info.commentTimes + 1;
            info.scoreTimes = info.scoreTimes + 1;
            info.scoreSum = info.scoreSum + score;
        } else {
            Logger.d(TAG, "lastScore :  " + lastScore);
            info.scoreSum = info.scoreSum + score - lastScore;
            updateScoreTimes(lastScore, info);
        }
        info.scoreAvg = info.scoreSum / info.commentTimes * 2;
        switch (score) {
            case 5:
                info.scoreTime5 = (info.scoreTime5 + 1);
                break;
            case 4:
                info.scoreTime4 = (info.scoreTime4 + 1);

                break;
            case 3:
                info.scoreTime3 = (info.scoreTime3 + 1);

                break;
            case 2:
                info.scoreTime2 = (info.scoreTime2 + 1);

                break;
            case 1:
                info.scoreTime1 = (info.scoreTime1 + 1);

                break;

            default:
                break;
        }

        lastScore = score;
        userInfo = info;

        /* UserScoreInfo.Builder builder = UserScoreInfo.newBuilder(userInfo);
         Logger.d(TAG, "score :  " + score);
         if (isFirstSubmit) {
             // UserScoreInfo.Builder builder = UserScoreInfo.newBuilder( );
             builder.setCommentTimes(builder.build().getCommentTimes() + 1);
             builder.setScoreTimes(builder.build().getScoreTimes() + 1);
             builder.setScoreSum(builder.build().getScoreSum() + score);

         } else {
             Logger.d(TAG, "lastScore :  " + lastScore);
             builder.setScoreSum(builder.build().getScoreSum() + score
                     - lastScore);
             updateScoreTimes(lastScore, builder);
         }

         builder.setScoreAvg((builder.build().getScoreSum() / builder.build()
                 .getCommentTimes()) * 2);
         switch (score) {
             case 5:
                 builder.setScoreTime5(builder.build().getScoreTime5() + 1);
                 break;
             case 4:
                 builder.setScoreTime4(builder.build().getScoreTime4() + 1);

                 break;
             case 3:
                 builder.setScoreTime3(builder.build().getScoreTime3() + 1);

                 break;
             case 2:
                 builder.setScoreTime2(builder.build().getScoreTime2() + 1);

                 break;
             case 1:
                 builder.setScoreTime1(builder.build().getScoreTime1() + 1);

                 break;

             default:
                 break;
         }

         lastScore = score;
         userInfo = builder.build();*/
    }

    private final ICSLoadingViewListener mCSLoadingViewListener = new ICSLoadingViewListener() {

        @Override
        public void onRetryRequestData() {
            mHandler.sendEmptyMessage(MSG_REQUEST_DATA);
            Logger.d(TAG, "----------onRetryRequestData");
        }

        @Override
        public void onInitRequestData() {
            mHandler.sendEmptyMessage(MSG_REQUEST_DATA);
            Logger.d(TAG, " ----------onInitRequestData ");
        }
    };

    private final ICSListViewListener mCSListViewListener = new ICSListViewListener() {

        @Override
        public void onRefresh() {
        }

        @Override
        public void onLoadMore() {
            reqCommentList();
        }

    };

    private final ReqUserCommentsListener mReqUserCommentsListener = new ReqUserCommentsListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            Logger.e(TAG, "onNetError errCode:" + errCode + ",errorMsg:"
                    + errorMsg);
            mHandler.sendEmptyMessage(MSG_NET_ERROR);
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.e(TAG, "onReqFailed statusCode:" + statusCode + ",errorMsg:"
                    + errorMsg);
            mHandler.sendEmptyMessage(MSG_NET_ERROR);
        }

        @Override
        public void onReqUserCommentsSucceed(UserCommentInfo[] list) {
            // TODO Auto-generated method stub
            Logger.d(TAG, "list.size()= " + list.length);
            if (list.length <= 0 && !mAdapter.isEmpty()) {
                mHandler.sendEmptyMessage(MSG_LAST_PAGE);
            } else {
                Message msg = Message.obtain();
                msg.what = MSG_GET_DATA_COMMENT_LIST;
                msg.obj = Tools.arrayToList(list);
                boolean isSend = mHandler.sendMessage(msg);
                Log.d(TAG,
                        "---------onReqUserCommentsSucceed----------isSend---- "
                                + isSend);
            }

        }

    };

    private final ReqUserScoreInfoListener mReqUserScoreInfoListener = new ReqUserScoreInfoListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            Logger.e(TAG, "onNetError errCode:"
                    + errCode + ",errorMsg:" + errorMsg);
            mHandler.sendEmptyMessage(MSG_NET_ERROR);
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            Logger.e(TAG, "onReqFailed statusCode:"
                    + statusCode + ",errorMsg:" + errorMsg);
            mHandler.sendEmptyMessage(MSG_NET_ERROR);
        }

        @Override
        public void onReqUserScoreInfoSucceed(UserScoreInfo userScoreInfo) {
            Logger.d(TAG, "userScoreInfo "
                    + userScoreInfo);
            Message msg = Message.obtain();
            msg.what = MSG_GET_DATA_USER_SCORE;
            msg.obj = userScoreInfo;
            mHandler.sendMessage(msg);
            Log.d(TAG, "onReqUserScoreInfoSucceed ");
            // userInfo = userScoreInfo;

            // refreshUI( );

        }

    };

    private void refreshUI() {
        if (userCommentList.size() > 0) {
            // mUserEvaluateEmpty.setVisibility( View.GONE );
            // userGrade.setVisibility( View.GONE );
            mListView.setVisibility(View.VISIBLE);
            refreshGradeColumn();
            Logger.d(TAG, "refreshUI ---------- mListView  show");
        } else {
            mListView.setVisibility(View.GONE);
            mListView.hideLoadingUI();
            Logger.d(TAG,
                    "refreshUI ---------- userGrade and mUserEvaluateEmpty show");

        }
    }

    /**
     * 是不是已经包含在activity中，防止调用getActivity为空
     * 
     * @return
     */
    private boolean isInActivity() {
        return getActivity() != null && !getActivity().isFinishing();
    }

    private void refreshGradeColumn() {

        if (!isInActivity())
            return;
        if (userInfo != null) {
            if (userInfo.scoreTimes == 0
                    || userInfo.commentTimes == 0) {
                /*Logger.e(
                        TAG,
                        "数据异常 ：userCommentList.size=  "
                                + userCommentList.size()
                                + "  userInfo.getScoreTimes= "
                                + userInfo.getScoreTimes()
                                + "userInfo.getCommentTimes="
                                + userInfo.getCommentTimes());*/
                return;
            }
            headerGradeHolder.rating
                    .setRating((float) (userInfo.scoreAvg / 2.0));
            headerGradeHolder.grade.setText(getString(
                    R.string.app_user_comment_grade, userInfo.scoreSum
                            * 1.0 / userInfo.commentTimes));
            headerGradeHolder.commentSum.setVisibility(View.VISIBLE);
            headerGradeHolder.commentSum.setText(getString(
                    R.string.app_user_comment_sum, userInfo.scoreTimes));
            headerGradeHolder.progressFive.setProgress(userInfo.scoreTime5
                    * 100 / userInfo.scoreTimes);
            headerGradeHolder.progressFour.setProgress(userInfo.scoreTime4
                    * 100 / userInfo.scoreTimes);
            headerGradeHolder.progressThree.setProgress(userInfo
                    .scoreTime3 * 100 / userInfo.scoreTimes);
            headerGradeHolder.progressTwo.setProgress(userInfo.scoreTime2
                    * 100 / userInfo.scoreTimes);
            headerGradeHolder.progressOne.setProgress(userInfo.scoreTime1
                    * 100 / userInfo.scoreTimes);
            headerGradeHolder.commentFive.setText(getString(
                    R.string.app_user_comment_num, userInfo.scoreTime5));
            headerGradeHolder.commentFour.setText(getString(
                    R.string.app_user_comment_num, userInfo.scoreTime4));
            headerGradeHolder.commentThree.setText(getString(
                    R.string.app_user_comment_num, userInfo.scoreTime3));
            headerGradeHolder.commentTwo.setText(getString(
                    R.string.app_user_comment_num, userInfo.scoreTime2));
            headerGradeHolder.commentOne.setText(getString(
                    R.string.app_user_comment_num, userInfo.scoreTime1));
            Log.d(TAG, "refreshGradeColumn---------headerGradeHolder  show");
        }

    }

    private void reqCommentList() {
        // TODO Auto-generated method stub
        ReqUserCommentsListController controller = new ReqUserCommentsListController(
                appId, PAGE_SIZE.USER_COMMENT_LIST, mCurrentPage,
                mReqUserCommentsListener);
        controller.doRequest();
        Logger.d(TAG, "reqCommentList");
    }

    private void reqUserScore() {
        ReqUserScoreInfoController scoreController = new ReqUserScoreInfoController(
                appId, mReqUserScoreInfoListener);
        scoreController.doRequest();
        Logger.d(TAG, "reqUserScore");
    }

    private static final int MSG_REQUEST_DATA = 1000;
    private static final int MSG_GET_DATA_COMMENT_LIST = 1001;
    private static final int MSG_GET_DATA_USER_SCORE = 1002;
    private static final int MSG_LAST_PAGE = 1003;
    private static final int MSG_NET_ERROR = 1004;
    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REQUEST_DATA:
                    reqUserScore();
                    reqCommentList();
                    break;

                case MSG_GET_DATA_COMMENT_LIST:
                    isReqUserCommentsSucceed = true;
                    Logger.d(TAG,
                            "MSG_GET_DATA_COMMENT_LIST  isReqUserCommentsSucceed : "
                                    + isReqUserCommentsSucceed);
                    backUpUserCommentList.addAll((List<UserCommentInfo>) msg.obj);
                    userCommentList.addAll((List<UserCommentInfo>) msg.obj);
                    reqDataSuccess();
                    break;

                case MSG_GET_DATA_USER_SCORE:
                    isReqUserScoreInfoSucceed = true;
                    Logger.d(TAG,
                            "MSG_GET_DATA_USER_SCORE  isReqUserScoreInfoSucceed : "
                                    + isReqUserScoreInfoSucceed);
                    userInfo = (UserScoreInfo) msg.obj;
                    reqDataSuccess();
                    // refreshUI( );
                    break;
                case MSG_LAST_PAGE:
                    if (mListView != null && !mAdapter.isEmpty()) {
                        mListView.setFooterPullEnable(false);
                        mListView.stopFooterRefresh();
                        mListView.hideLoadingUI();
                        CSToast.show(App.getAppContext(),
                                getString(R.string.tip_last_page));
                    }
                    break;
                case MSG_NET_ERROR:
                    if (mAdapter != null && mAdapter.isEmpty()) {
                        mAdapter.notifyDataSetInvalidated();
                        break;
                    }
                    if (mListView != null) {
                        mListView.stopFooterRefresh();
                        CSToast.show(App.getAppContext(),
                                mContext.getString(R.string.error_msg_net_fail));
                    }
                    break;
                default:
                    break;
            }

        }

    };

    private void reqDataSuccess() {
        if (isReqUserCommentsSucceed && isReqUserScoreInfoSucceed) {
            if (mAdapter != null && mListView != null) {
                userCommentList.addAll(backUpUserCommentList);
                mAdapter.appendData(backUpUserCommentList);
                mAdapter.notifyDataSetChanged();
                if (backUpUserCommentList.size() < PAGE_SIZE.USER_COMMENT_LIST
                        && mCurrentPage == 1) {
                    mListView.setFooterPullEnable(false);
                    for (int i = 0; i < backUpUserCommentList.size(); i++) {
                        if (backUpUserCommentList
                                .get(i).userEI
                                .equals(userInfo.userCommentInfo
                                .userEI)) {
                            commentInfoPos = i;
                        }
                    }
                }
                mCurrentPage++;
                mListView.stopFooterRefresh();
                refreshUI();
                backUpUserCommentList.clear();
                Log.d(TAG, "----------MSG_GET_DATA_INFO");
            }
        }
    }

    private class UserGradeHolder {
        private final RatingBar rating;
        private final TextView grade;
        private final TextView commentSum;
        private final ProgressBar progressFive;
        private final ProgressBar progressFour;
        private final ProgressBar progressThree;
        private final ProgressBar progressTwo;
        private final ProgressBar progressOne;
        private final TextView commentFive;
        private final TextView commentFour;
        private final TextView commentThree;
        private final TextView commentTwo;
        private final TextView commentOne;
        private final Button commentButton;

        private UserGradeHolder(View view) {
            rating = (RatingBar) view.findViewById(R.id.app_rating);
            grade = (TextView) view.findViewById(R.id.avg_grade);
            commentSum = (TextView) view.findViewById(R.id.app_comment_sum);
            progressFive = (ProgressBar) view
                    .findViewById(R.id.user_comment_progress_five);
            progressFour = (ProgressBar) view
                    .findViewById(R.id.user_comment_progress_four);
            progressThree = (ProgressBar) view
                    .findViewById(R.id.user_comment_progress_three);
            progressTwo = (ProgressBar) view
                    .findViewById(R.id.user_comment_progress_two);
            progressOne = (ProgressBar) view
                    .findViewById(R.id.user_comment_progress_one);
            commentFive = (TextView) view
                    .findViewById(R.id.app_comment_num_five);
            commentFour = (TextView) view
                    .findViewById(R.id.app_comment_num_four);
            commentThree = (TextView) view
                    .findViewById(R.id.app_comment_num_three);
            commentTwo = (TextView) view.findViewById(R.id.app_comment_num_two);
            commentOne = (TextView) view.findViewById(R.id.app_comment_num_one);
            commentButton = (Button) view.findViewById(R.id.comment_button);
        }
    }

}
