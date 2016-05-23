
package com.hykj.gamecenter.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.adapter.GeneralAppAdapter;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION_PATH;
import com.hykj.gamecenter.controller.ProtocolListener.KEY;
import com.hykj.gamecenter.controller.ProtocolListener.MAIN_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.ORDER_BY;
import com.hykj.gamecenter.controller.ProtocolListener.ReqGroupElemsListener;
import com.hykj.gamecenter.controller.ProtocolListener.SUB_TYPE;
import com.hykj.gamecenter.controller.ReqGroupElemsListController;
import com.hykj.gamecenter.data.TopicInfo;
import com.hykj.gamecenter.download.ApkDownloadManager;
import com.hykj.gamecenter.download.DownloadService;
import com.hykj.gamecenter.download.DownloadTask;
import com.hykj.gamecenter.download.DownloadTaskStateListener;
import com.hykj.gamecenter.logic.ApkInstalledManager;
import com.hykj.gamecenter.logic.ApkInstalledManager.InstallFinishListener;
import com.hykj.gamecenter.logic.DisplayOptions;
import com.hykj.gamecenter.logic.entry.Msg;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.statistic.MSG_CONSTANTS;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.DownloadStateViewCustomization;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.ui.widget.CSLoadingUIListView;
import com.hykj.gamecenter.ui.widget.CSPullListView.ICSListViewListener;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.ui.widget.EllipsizingTextView;
import com.hykj.gamecenter.ui.widget.ICSLoadingViewListener;
import com.hykj.gamecenter.utils.Interface.IDownloadTaskCountChangeListener;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.SystemBarTintManager;
import com.hykj.gamecenter.utils.Tools;
import com.hykj.gamecenter.utils.UITools;
import com.hykj.gamecenter.utils.UpdateUtils;
import com.hykj.gamecenter.utilscs.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

//import cs.widget.ICSLoadingViewListener;

public class GroupAppListActivity extends Activity implements OnClickListener,
		InstallFinishListener, IDownloadTaskCountChangeListener {

	private static final int SHOW_SNAPSHOT_RESULT_GROUP_NUMBER_LANDSCAPE = 3;
	private static final int SHOW_SNAPSHOT_RESULT_GROUP_NUMBER_PORTRAIT = 2;
	private boolean mbExpandText = false; // 文字是否展开，当文字太多时
	private static final String TAG = "GroupAppListActivity";

	private CSCommonActionBar mActionBar;
	private CSLoadingUIListView mListView;
	// private GroupGridAdapter mAdapter;
	private GeneralAppAdapter mAdapter;
	private Context mContext;
	private ArrayList<GroupElemInfo> InfoList = new ArrayList<GroupElemInfo>();

	private LinearLayout mTopicTop; // 顶部标题

	// 获取应用的页数，第一页和后台确认为1
	private int mCurrentPage = 1;
	private int mAppType;
	private int mAppSubType;
	private int mAppPosType;
	private int mOrderBy;
	private int mGroupId;
	private int mGroupClass;
	private int mGroupType;

	private ApkInstalledManager mApkInstalledManager;
	private ApkDownloadManager mApkDownloadManager;

	// topic
	private TextView mTopicName;
	private TextView mTopicAppCount;
	private ImageView mTopicImageView;
	private EllipsizingTextView mTopicTip;
	private boolean isFooterPullEnable = true;
	private boolean mShowSnapShot = true;
	private String mTitleName;

	private TopicInfo mTopicInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (App.getDevicesType() == App.PHONE)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		mContext = App.getAppContext();
		mApkInstalledManager = ApkInstalledManager.getInstance();
		mApkDownloadManager = DownloadService.getDownloadManager();

		mAppType = getIntent().getIntExtra(KEY.MAIN_TYPE, MAIN_TYPE.ALL);
		mAppSubType = getIntent().getIntExtra(KEY.SUB_TYPE, SUB_TYPE.ALL);
		mOrderBy = getIntent().getIntExtra(KEY.ORDERBY, ORDER_BY.AUTO);
		mGroupId = getIntent().getIntExtra(KEY.GROUP_ID, 0);
		mGroupClass = getIntent().getIntExtra(KEY.GROUP_CLASS, 0);
		mGroupType = getIntent().getIntExtra(KEY.GROUP_TYPE, 0);

		mAppPosType = getIntent()
				.getIntExtra(StatisticManager.APP_POS_TYPE, -1);
		Log.i(TAG, "mGroupType = " + mGroupType);
		Log.i(TAG, "mAppType = " + mAppType);
		Log.i(TAG, "mAppPosType = " + mAppPosType);

		mTopicInfo = getIntent().getParcelableExtra(KEY.TOPIC_INFO);

		mTitleName = getIntent().getStringExtra(KEY.CATEGORY_NAME);
		//        mGroupType = getIntent().getIntExtra(KEY.GROUP_TYPE, 0);

		boolean bSubjectAppList = getIntent().getBooleanExtra(
				KEY.SUBJECT_APPLIST, false);

		if (mAppType == MAIN_TYPE.TOPIC) {
			bSubjectAppList = true;
		}

		// TODO
		handleAction();

		if (savedInstanceState != null) {
			InfoList = (ArrayList<GroupElemInfo>) savedInstanceState
					.getSerializable("info_list");
			isFooterPullEnable = savedInstanceState.getBoolean("footer_pull",
					true);
			mCurrentPage = savedInstanceState.getInt("current_page", 1);
		}
		mShowSnapShot = false;

		setContentView(R.layout.activity_group_app_list);
		SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);
		Resources res = getResources();

        /*
		 * int iColorRId = getIntent().getIntExtra(KEY.BK_COLOR,
         * R.color.background);
         */

		int iColorRId = R.color.background;
		Drawable drawable = res.getDrawable(iColorRId);
		this.getWindow().setBackgroundDrawable(drawable);

		mActionBar = (CSCommonActionBar) findViewById(R.id.ActionBar);
		mActionBar.SetOnActionBarClickListener(actionBarListener);

		mListView = (CSLoadingUIListView) findViewById(R.id.app_list);
		mListView.setFooterPullEnable(isFooterPullEnable);
		mListView.setHeaderPullEnable(false);

		mListView.setCSListViewListener(mCSListViewListener);
		mListView.setCSLoadingViewListener(mCSLoadingViewListener);

		Log.i(TAG, "mShowSnapShot = " + mShowSnapShot);
		Log.i(TAG, "mAppType = " + mAppType);
		Log.i(TAG, "bSubjectAppList = " + bSubjectAppList);

		if (mShowSnapShot) {
			mAdapter = new GeneralAppAdapter(
					this,
					mAppType,
					UITools.isPortrait() ? SHOW_SNAPSHOT_RESULT_GROUP_NUMBER_PORTRAIT
							: SHOW_SNAPSHOT_RESULT_GROUP_NUMBER_LANDSCAPE,
					bSubjectAppList);
		} else {
			WindowManager m = getWindowManager();
			Display d = m.getDefaultDisplay();
			mAdapter = new GeneralAppAdapter(this, mAppType,
					UITools.getColumnNumber(mContext), bSubjectAppList);
		}

		if (mAppType == MAIN_TYPE.TOPIC) {
			mTopicTop = (LinearLayout) LayoutInflater.from(mContext).inflate(
					R.layout.topic_top_block, null);
			mListView.addListHeaderView(mTopicTop);
			mTopicName = (TextView) mTopicTop.findViewById(R.id.group_name);
			mTopicImageView = (ImageView) mTopicTop
					.findViewById(R.id.group_image);
			if(!UITools.isPortrait()){
				FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,getResources().getDimensionPixelSize(R.dimen.sbuject_topic_image_height_por));
				mTopicImageView.setLayoutParams(params);
			}
			mTopicTip = (EllipsizingTextView) mTopicTop
					.findViewById(R.id.group_tip);
			mTopicTip.setMaxLines(2);

			mTopicTip.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!mbExpandText) {
						// ViewGroup.LayoutParams p =
						// mTopicTip.getLayoutParams();
						// p.height = LayoutParams.MATCH_PARENT;
						// mTopicTip.setLayoutParams(p);
						mTopicTip.setMaxLines(20);
                        /*
                         * RelativeLayout.LayoutParams answeParam_c_c =
                         * newRelativeLayout.LayoutParams((int) (220 *
                         * CommonUtilities.fDensity),
                         * 
                         * (int) (48 * CommonUtilities.fDensity));
                         * 
                         * answeParam_c_c.addRule(RelativeLayout.
                         * ALIGN_PARENT_BOTTOM );
                         * 
                         * answerLayout2.updateViewLayout(answer_c_c,
                         * answeParam_c_c);
                         */

        //################# oddshou 注释
						mbExpandText = true;
					} else {
						mTopicTip.setMaxLines(2);
						// mTopicTip.invalidate();
						mbExpandText = false;
					}
				}
			});
			mTopicAppCount = (TextView) mTopicTop
					.findViewById(R.id.group_app_count);
			setTopicInfo(mTopicInfo);

		} else {
			mActionBar.setTitle(mTitleName);
		}

		mListView.setAdapter(mAdapter);

		Log.d(TAG, "InfoList = " + InfoList);
		if (InfoList != null && InfoList.size() > 0) {
			mAdapter.appendData(InfoList, false);
			Log.d(TAG, "InfoList size > 0");
		} else {
			mListView.initRequestData();
		}

	}

	private int chnNo = 0;

	public void handleAction() {
		String action = getIntent().getAction();
		if (action != null
				&& action
				.equals("com.hykj.gamecenter.activity.GroupAppListActivity")) {
			Uri uri = getIntent().getData();
			if (uri != null && uri.getPath().equals(ACTION_PATH.GROUP_APP_LIST)) {
				mGroupId = Integer.parseInt(uri.getQueryParameter("groupId"));
				mGroupClass = Integer.parseInt(uri
						.getQueryParameter("groupClass"));
				mGroupType = Integer.parseInt(uri
						.getQueryParameter("groupType"));
				mOrderBy = Integer.parseInt(uri.getQueryParameter("orderType"));
				mTitleName = getString(R.string.topic_game_label);
				mAppType = MAIN_TYPE.TOPIC;
				TopicInfo topicInfo = new TopicInfo();
				topicInfo.mAppCount = uri.getQueryParameter("recommWrod");
				topicInfo.mTip = uri.getQueryParameter("groupDesc");
				topicInfo.mTopic = uri.getQueryParameter("groupName");
				mTopicInfo = topicInfo;

				// 统计位置pos
				chnNo = getIntent().getIntExtra("chnNo", 0);
				// TODO 统计参数
				//                App.init();
				App.initChnNo(chnNo);

			} else {
				LogUtils.e("uri is null");
				return;
			}
		}
	}

	private void setTopicInfo(TopicInfo info) {
		mTopicName.setText(info.mTopic);
		mTopicTip.setText(info.mTip);
		mTopicAppCount.setText(info.mAppCount);
		if (mTitleName != null && !mTitleName.equals("")) {
			mActionBar.setTitle(mTitleName);
		} else {
			mActionBar.setTitle(info.mTopic);
		}
		ImageLoader.getInstance().displayImage(info.mPicUrl, mTopicImageView,
				DisplayOptions.optionSubject);
	}

	private final ICSLoadingViewListener mCSLoadingViewListener = new ICSLoadingViewListener() {

		// 重试
		@Override
		public void onRetryRequestData() {
			mUiHandler.sendEmptyMessage(MSG_REQUEST_DATA);
		}

		@Override
		public void onInitRequestData() {
			mUiHandler.sendEmptyMessage(MSG_REQUEST_DATA);
		}
	};

	// TODO 请求应用的个数
	private void reqAppList() {
		Logger.i(TAG, "reqAppList,mGroupId =" + mGroupId);
		Logger.i(TAG, "reqAppList,mGroupClass=" + mGroupClass);
		Logger.i(TAG, "reqAppList,mGroupType=" + mGroupType);
		Logger.i(TAG, "reqAppList,mOrderBy=" + mOrderBy);
		ReqGroupElemsListController controller = new ReqGroupElemsListController(
				mGroupId, mGroupClass, mGroupType, mOrderBy,
				HomePageActivity.REQ_PAGE_SIZE, mCurrentPage,
				mReqGameElemsListener);
		Log.i(TAG, "reqAppList mAppPosType = " + mAppPosType);
		controller.setClientPos(ReportConstants.reportPos(
				mAppPosType));
		controller.doRequest();
	}

	/**
	 * 上报应用的位置
	 */
	public void reportAppPosition() {
		// 统计类型
		if (mAppPosType == ReportConstants.STATIS_TYPE.CLASSIFY) {
			mAppPosType = ReportConstants.STATIS_TYPE.CLASSIFY_LIST;
		} else if (mAppPosType == ReportConstants.STATIS_TYPE.SUBJECT) {
			mAppPosType = ReportConstants.STATIS_TYPE.SUBJECT_LIST;
		} else if (mAppPosType == ReportConstants.STATIS_TYPE.RECOM_NEWEST) {
			mAppPosType = ReportConstants.STATIS_TYPE.RECOM_NEWEST_LIST;
		} else if (mAppPosType == ReportConstants.STATIS_TYPE.RECOM_NICE) {
			mAppPosType = ReportConstants.STATIS_TYPE.RECOM_NICE_LIST;
		} else if (mAppPosType == ReportConstants.STATIS_TYPE.NEW_PERSON_RECOM) {
			mAppPosType = ReportConstants.STATIS_TYPE.NEW_PERSON_RECOM;
		} else if (mAppPosType == ReportConstants.STATIS_TYPE.GAME_NEWEST) {
			mAppPosType = ReportConstants.STATIS_TYPE.GAME_NEWEST_LIST;
		} else if (mAppPosType == ReportConstants.STATIS_TYPE.GAME_NICE) {
			mAppPosType = ReportConstants.STATIS_TYPE.GAME_NICE_LIST;
		}
		//################# oddshou 注释
//        if (getString(R.string.topic_app_fragement_pcgame).equals(mTitleName)) {
//            mAppPosType = STATIS_TYPE.LOC;
//        } else if (getString(R.string.topic_app_fragement_webgame).equals(
//                mTitleName)) {
//            mAppPosType = STATIS_TYPE.WEB;
//        } else if (getString(R.string.web_game_label).equals(mTitleName)) {
//            mAppPosType = STATIS_TYPE.WEB;
//        }


		if (mAppType == MAIN_TYPE.TOPIC) {
			mAppPosType = ReportConstants.STATIS_TYPE.SUBJECT_LIST;
		} else if (mAppType == MAIN_TYPE.JING_PIN) {
			mAppPosType = ReportConstants.STATIS_TYPE.CLASSIFY_LIST;

		}
	}

	private final ICSListViewListener mCSListViewListener = new ICSListViewListener() {
		@Override
		public void onRefresh() {
		}

		@Override
		public void onLoadMore() {
			reqAppList();
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		mAdapter.showSnapShot(mShowSnapShot);
		mApkInstalledManager.addUiRefreshHandler(mUpdateRefreshHandler);
		mApkDownloadManager.registerListener(downloadTaskStateListener);
		mAdapter.notifyDataSetChanged();
		refreshActionBarTip();
		mUiHandler.sendEmptyMessageDelayed(MSG_REFRESH_LIST, 100);
	}

	@Override
	public void onPause() {
		super.onPause();
		mApkInstalledManager.removeUiRefreshHandler(mUpdateRefreshHandler);
		mApkDownloadManager.unregisterListener(downloadTaskStateListener);
	}

	@Override
	public void onDownloadTaskCountChange() {
		// TODO Auto-generated method stub

		mUpdateRefreshHandler.sendEmptyMessage(Msg.REFRESH_HEADBAR);// 刷新下载图标个数
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// 注册下载任务个数变化监听
		DownloadStateViewCustomization.addDownloadTaskCountChangeListener(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// 反注册下载任务个数变化监听
		DownloadStateViewCustomization
				.removeDownloadTaskCountChangeListener(this);
	}

	private final ReqGroupElemsListener mReqGameElemsListener = new ReqGroupElemsListener() {

		@Override
		public void onNetError(int errCode, String errorMsg) {
			Logger.e("mReqGameElemsListener", "onNetError errCode:" + errCode
					+ ",errorMsg:" + errorMsg);
			mUiHandler.sendEmptyMessage(MSG_NET_ERROR);
		}

		@Override
		public void onReqFailed(int statusCode, String errorMsg) {
			Logger.e("mReqGameElemsListener", "onReqFailed statusCode:"
					+ statusCode + ",errorMsg:" + errorMsg);
			mUiHandler.sendEmptyMessage(MSG_LAST_PAGE);
		}

		@Override
		public void onReqGroupElemsSucceed(GroupElemInfo[] infoList,
										   String serverDataVer) {
			Logger.d(TAG, "infoList.size()= " + infoList.length);
			if (infoList.length <= 0) {
				mUiHandler.sendEmptyMessage(MSG_LAST_PAGE);
			} else {
				InfoList.addAll(Tools.arrayToList(infoList));
				Message msg = Message.obtain();
				msg.what = MSG_GET_DATA;
				msg.obj = (Tools.arrayToList(infoList));
				mUiHandler.sendMessage(msg);
			}

		}

	};

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable("info_list", InfoList);
		outState.putInt("current_page", mCurrentPage);
		outState.putBoolean("footer_pull", isFooterPullEnable);
		super.onSaveInstanceState(outState);
	}

	private static final int MSG_REQUEST_DATA = 1000;
	private static final int MSG_GET_DATA = 1001;
	private static final int MSG_LAST_PAGE = 1002;
	private static final int MSG_NET_ERROR = 2000;
	public static final int MSG_SHOW_TOAST = 2001;
	public static final int MSG_REFRESH_LIST = 2003;

	private final Handler mUiHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_REQUEST_DATA:
					reqAppList();
					break;
				case MSG_GET_DATA: {
					// 向后台上报统计信息
					reportAppPosition();
					if (mAdapter != null && mListView != null) {
						Log.i(TAG, "new mAppPosType = " + mAppPosType);
						mAdapter.setAppPosType(mAppPosType);
						mAdapter.appendData((List<GroupElemInfo>) msg.obj, false);

                        /*
                         * if( ( (List< GroupElemInfo >)msg.obj ).size( ) <
                         * PAGE_SIZE.CATEGORY_LIST ) {
                         * mListView.setFooterPullEnable( false );
                         * isFooterPullEnable = false; }
                         */

						mListView.stopFooterRefresh();
						mCurrentPage++;
					}

					break;
				}
				case MSG_NET_ERROR:
					if (mAdapter != null && mAdapter.isEmpty()) {
						mAdapter.notifyDataSetInvalidated();// 出发listview的onInvalidated方法导致显示无网络对话框
						break;
					}
					if (mListView != null) {
						mListView.stopFooterRefresh();
						CSToast.show(mContext,
								mContext.getString(R.string.error_msg_net_fail));
					}
					break;
				case MSG_LAST_PAGE:
					if (mListView != null) {
						mListView.setFooterPullEnable(false);
						isFooterPullEnable = false;
						mListView.stopFooterRefresh();
						mListView.hideLoadingUI();
						CSToast.show(mContext,
								mContext.getString(R.string.tip_last_page));
					}
					break;

				case MSG_SHOW_TOAST:
					CSToast.show(mContext, (String) msg.obj);
					break;

				case MSG_REFRESH_LIST:
					if (mAdapter != null) {
						mAdapter.setDisplayImage(true);
						if (mAdapter.getAllCount() > 1)
							mAdapter.notifyDataSetChanged();
					}

					break;
			}
		}
	};

	private final DownloadTaskStateListener downloadTaskStateListener = new DownloadTaskStateListener() {
		@Override
		public void onUpdateTaskState(DownloadTask task) {
		}

		@Override
		public void onUpdateTaskProgress(DownloadTask task) {
		}

		@Override
		public void onUpdateTaskList(Object obj) {
			mAdapter.notifyDataSetChanged();
		}
	};

	private final Handler mUpdateRefreshHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case Msg.REFRESH_HEADBAR:
					refreshActionBarTip();
					break;
				case MSG_CONSTANTS.MSG_CHECK_UPDATE: {
					mAdapter.notifyDataSetChanged();
					refreshActionBarTip();
					break;
				}
				case MSG_CONSTANTS.MSG_CHECK_TOAST: {
					CSToast.show(GroupAppListActivity.this, (String) msg.obj);
					break;
				}
			}
		}
	};

	private final OnActionBarClickListener actionBarListener = new OnActionBarClickListener() {

		@Override
		public void onActionBarClicked(int position, View view) {

			switch (position) {
				case CSCommonActionBar.OnActionBarClickListener.RETURN_BNT:
					onBackPressed();
					break;
				case CSCommonActionBar.OnActionBarClickListener.MANAGE_BNT:
//					Intent intentManage = new Intent(GroupAppListActivity.this,
//							AppManageActivity.class);
//					startActivity(intentManage);
					Intent intent = new Intent(GroupAppListActivity.this, HomePageActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra(HomePageActivity.KEY_SELECT_ITEM, HomePageActivity.PAGE_INDEX.INDEX_UPDATE);
					startActivity(intent);
					break;
				case CSCommonActionBar.OnActionBarClickListener.SETTING_BNT:
					Intent intentSetting = new Intent(GroupAppListActivity.this,
							SettingListActivity.class);
					startActivity(intentSetting);
					break;
				case CSCommonActionBar.OnActionBarClickListener.SEARCH_BNT:
					Intent intentSearch = new Intent(GroupAppListActivity.this,
							SearchActivity.class);
					intentSearch.putExtra(KEY.MAIN_TYPE, MAIN_TYPE.ALL);
					intentSearch.putExtra(KEY.SUB_TYPE, SUB_TYPE.ALL);
					intentSearch.putExtra(KEY.ORDERBY, ORDER_BY.DOWNLOAD);
					startActivity(intentSearch);
					break;
				default:
					break;
			}
		}
	};

	private void refreshActionBarTip() {
		mActionBar.setSettingTipVisible(UpdateUtils.hasUpdate() ? View.VISIBLE
				: View.GONE);
		mActionBar.setManageTipVisible(
				mApkInstalledManager.hasUpdateOrDownload(),
				mApkDownloadManager.getTaskCount()
						+ mApkInstalledManager.getUpdateInfoNotInTaskCount());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	@Override
	public void apkInstallFinish(DownloadTask info) {

	}

}
