
package com.hykj.gamecenter.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.GlobalConfigControllerManager;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.GroupAppListActivity;
import com.hykj.gamecenter.activity.HomePageActivity;
import com.hykj.gamecenter.activity.PhoneAppInfoActivity;
import com.hykj.gamecenter.activity.SearchActivity;
import com.hykj.gamecenter.activity.SettingListActivity;
import com.hykj.gamecenter.adapter.MixGridAdapter;
import com.hykj.gamecenter.controller.ProtocolListener;
import com.hykj.gamecenter.controller.ProtocolListener.ELEMENT_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.GROUP_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.HOME_PAGE_POSITION;
import com.hykj.gamecenter.controller.ProtocolListener.ITEM_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.KEY;
import com.hykj.gamecenter.controller.ProtocolListener.MAIN_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.ORDER_BY;
import com.hykj.gamecenter.controller.ProtocolListener.ReqGroupElemsListener;
import com.hykj.gamecenter.controller.ReqGroupElemsListController;
import com.hykj.gamecenter.data.GroupInfo;
import com.hykj.gamecenter.data.TopicInfo;
import com.hykj.gamecenter.db.CSACDatabaseHelper.GroupInfoColumns;
import com.hykj.gamecenter.db.DatabaseUtils;
import com.hykj.gamecenter.logic.entry.ISaveInfo;
import com.hykj.gamecenter.logic.entry.Msg;
import com.hykj.gamecenter.mta.MTAConstants;
import com.hykj.gamecenter.mta.MtaUtils;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.protocol.Reported.ReportedInfo;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.HomeCirculeAdvView;
import com.hykj.gamecenter.ui.HomeCirculeRecommendAdvView;
import com.hykj.gamecenter.ui.HorizonScrollLayout;
import com.hykj.gamecenter.ui.SearchBarCommon;
import com.hykj.gamecenter.ui.widget.CSLoadingUIListView;
import com.hykj.gamecenter.ui.widget.CSPullListView.ICSListViewListener;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.utils.Interface.IFragmentInfo;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.Tools;
import com.hykj.gamecenter.utils.UITools;
import com.hykj.gamecenter.utils.UpdateUtils;
import com.hykj.gamecenter.utilscs.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameFragment extends BaseFragment implements OnClickListener,
		HomeCirculeRecommendAdvView.CirculeAdvListener, IFragmentInfo {
	private static final String TAG = "GameFragment";

	private View mainView = null;
	private final List<GroupElemInfo> mGroupElemInfo = new ArrayList<GroupElemInfo>();
	private final ArrayList<GroupElemInfo> listInfos = new ArrayList<GroupElemInfo>(); // home界面list的数据

	// 广告位ID和数据对应的map
	private final SparseArray<GroupElemInfo> mPosToAppinfoMap = new SparseArray<GroupElemInfo>();

	private View headView = null;

	// 3个独立的广告位
	private HomeCirculeRecommendAdvView mHomeAdvView = null;

	private CSLoadingUIListView pUIListView = null;
	private boolean isFooterPullEnable = true;
	// public GroupGridAdapter homeListAdapter = null;
	public MixGridAdapter gameListAdapter;
	// 异常界面
	private View mLoadingFrame;
	private View mLoadingView;
	private View mNoNetworkView;

	private Context mContext;

	// 推荐页传入的hander 用于处理加载完首页，如果有未安装或者下载的任务，弹出提示操作
	private Handler mHandler;
	private int mCurrentPage = 1;
	private int mCirculeIndex = 0;
	private boolean hasLoadData = false;
	private boolean isLoading = true;
	private SearchBarCommon mSearchBarCommon;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		mContext = activity;

		if (activity != null)
			((HomePageActivity) activity).setmGameFragment(this);

	}

	public int getRecommendAdvCount() {
		return mHomeAdvView.getRecommendAdvCount();
	}

	// 放数据的请求
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.i(TAG, "onCreate()");
		// Debug.startMethodTracing( TAG );

		if (gameListAdapter == null) {
			gameListAdapter = new MixGridAdapter(mContext, /*MAIN_TYPE.GAME*/MAIN_TYPE.ALL,
					UITools.getColumnNumber(mContext));
			Logger.i(TAG, "ColumnNumber  ==  " + UITools.getColumnNumber(mContext));
		}
		String fragmentTabLabel = getFragmentTabLabel();
		if ( fragmentTabLabel!= null
				&& fragmentTabLabel
				.equals(IFragmentInfo.FragmentTabLabel.RECOM_LABEL)){
			gameListAdapter.setAppPosType(ReportConstants.STATIS_TYPE.RECOM);
		}else {
			gameListAdapter.setAppPosType(ReportConstants.STATIS_TYPE.GAME);	//华硕手机版只有游戏
		}
		GlobalConfigControllerManager.getInstance().registForUpdate(
				mUiDownLoadHandler, Msg.UPDATE_STATE, null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		Logger.i(TAG, "onCreateView()");
		mainView = inflater.inflate(R.layout.activity_home_page_content,
				container, false);

		if (App.getDevicesType() == App.PHONE){
			headView = inflater.inflate(
					R.layout.home_recommend_topic_top2, null,
					false);
		}else {
			headView = inflater.inflate(
					R.layout.home_recommend_topic_top2_pad, null,
					false);
		}

		getSaveState(savedInstanceState);
		// 初始化异常界面
		initExceptionView();
		initActionBar(mainView);

		// home界面list view初始化
		initListView();
		initCirculeAdvView();
		initGroupHeadView();
		setDisplayStatus(LOADING_STATUS);
		getDataList();
		return mainView;

	}


	private void getSaveState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			List<GroupElemInfo> infos;
			infos = (List<GroupElemInfo>) savedInstanceState
					.getSerializable(ISaveInfo.GROUP_ELEM_INFO);
			mGroupElemInfo.clear();
			mGroupElemInfo.addAll(infos);
			isFooterPullEnable = savedInstanceState.getBoolean(
					ISaveInfo.IS_FOOTER_PULL, true);
			mCurrentPage = savedInstanceState.getInt(ISaveInfo.CURRENT_PAGE, 1);
			mCirculeIndex = savedInstanceState
					.getInt(ISaveInfo.RECOMMED_CIRCLE_INDEX);
			savedInstanceState.clear();
		}
	}

	@Override
	public void onConfigurationChanged(
			android.content.res.Configuration newConfig) {
		Logger.i(TAG, "onConfigurationChanged() ");
		if (gameListAdapter != null) {
			gameListAdapter.setColumnCount(UITools.getColumnNumber(mContext));
		}
		mHomeAdvView.getHandler().sendEmptyMessage(
				HomeCirculeAdvView.MSG_HOME_REFRESH);
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onResume() {
		Logger.i(TAG, "onResume()");
		super.onResume();
		refreshTipVisible();
		// 将banner的开始放到onResume
		if (mGroupElemInfo != null && mGroupElemInfo.size() > 0) {
			// changeView( );
			gameListAdapter.notifyDataSetChanged();
			if (!mHomeAdvView.isCricle()) {
				mHomeAdvView.onReply();
			}
		}
		getFocusable(mainView); // 获取焦点，防止自动跳到list view首项
		/*
         * if(GlobalConfigControllerManager.getInstance().isHasLoaded()){
         * getDataList(); }
         */
	}

	public void refreshTipVisible() {
		mUiDownLoadHandler.sendEmptyMessage(MSG_REFRESH_TIPS);
	}

	@Override
	public void onPause() {
		Logger.i(TAG, "onPause");
		super.onPause();
		stopCircle();

	}

	private final ICSListViewListener mCSListViewListener = new ICSListViewListener() {
		@Override
		public void onRefresh() {
		}

		@Override
		public void onLoadMore() {
			reqGameList();
		}
	};

	public void stopCircle() {
		if (null != mHomeAdvView) {
			mHomeAdvView.stopCricule();
		}
	}

	public void startCircle() {
		if (null != mHomeAdvView) {
			mHomeAdvView.onReply();
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (View.VISIBLE == mNoNetworkView.getVisibility()) {
			mUiDownLoadHandler.sendEmptyMessage(Msg.RELOAD_PAGE);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		Logger.i(TAG, "onDestroy()");
		if (null != mPosToAppinfoMap) {
			mPosToAppinfoMap.clear();
		}
		if (null != listInfos) {
			listInfos.clear();
		}
		GlobalConfigControllerManager.getInstance().unregistForUpdate(mHandler);
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	private void setListInfosData(List<GroupElemInfo> infos) {
		listInfos.clear();
		for (GroupElemInfo info : infos) {
			if (info.posId >= 2) {
				listInfos.add(info);
			}
		}
	}

	private void pushData() {
		gameListAdapter.appendData(listInfos);
		gameListAdapter.notifyDataSetChanged();
	}

	/**
	 * 让控件获取焦点
	 *
	 * @param view
	 */
	private void getFocusable(View view) {
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		view.requestFocus();
	}

	private void initExceptionView() {
		// loading
		mLoadingFrame = mainView.findViewById(R.id.loading_interface);
		mLoadingView = mLoadingFrame.findViewById(R.id.csl_cs_loading);
		mNoNetworkView = mLoadingFrame
				.findViewById(R.id.csl_cs_listview_no_networking);

		Button settingbutton = (Button) mainView
				.findViewById(R.id.csl_network_setting_btn);
		settingbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toActivity = new Intent(Settings.ACTION_WIFI_SETTINGS);
				toActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(toActivity);
			}
		});

		Button button = (Button) mainView
				.findViewById(R.id.csl_network_retry_btn);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mUiDownLoadHandler.sendEmptyMessage(Msg.RELOAD_PAGE);
			}
		});
	}

	private void setCirculeDataToAdvView(ArrayList<GroupElemInfo> carouselData) {
		if (carouselData.size() == 0)
			return;
		mHomeAdvView.scrollViewAddData(carouselData,
				mHomeAdvView.getCurScreen());
		mHomeAdvView.setCurScreen(mCirculeIndex);
		mHomeAdvView.onReply();
	}

	private void initCirculeAdvView() {
		if (mHomeAdvView == null)
			mHomeAdvView = new HomeCirculeRecommendAdvView(headView,
					App.getAppContext(), getFragmentTabLabel());
		mHomeAdvView.setCurScreen(mCirculeIndex);
		mHomeAdvView.addAdvListener(this);
//		mHorizonScrollLayout = (HorizonScrollLayout)headView.findViewById(R.id.top_ad_1);

	}

	private void initGroupHeadView() {
		TextView title = (TextView) headView.findViewById(R.id.category_title);
		Button more = (Button) headView.findViewById(R.id.more);
		more.setVisibility(View.VISIBLE);
		title.setText(App.getAppContext().getString(R.string.nice_game_label));

		more.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, GroupAppListActivity.class);
				intent.putExtra(KEY.CATEGORY_NAME,
						mContext.getString(R.string.nice_game_label));
				int ints[] = DatabaseUtils.getGroupIdByDB(
						GROUP_TYPE.NICE_GAME_TYPE, ORDER_BY.AUTO);
				LogUtils.d("GROUP_ID = " + ints[0] + "GROUP_CLASS = " + ints[1]
						+ "GROUP_TYPE = " + ints[2] + "ORDERBY = " + ints[3]);
				intent.putExtra(KEY.GROUP_ID, ints[0]);
				intent.putExtra(KEY.GROUP_CLASS, ints[1]);
				intent.putExtra(KEY.GROUP_TYPE, ints[2]);
				intent.putExtra(KEY.ORDERBY, ints[3]);
				// intent.putExtra( KEY.ORDERBY , groupInfo.orderType );
				intent.putExtra(KEY.MAIN_TYPE, MAIN_TYPE.GAME_CLASS);
				// 统计位置
				intent.putExtra(StatisticManager.APP_POS_TYPE,
						ReportConstants.STATIS_TYPE.GAME_NICE);
				mContext.startActivity(intent);
			}
		});
	}

	private void initActionBar(View rootView) {
		mSearchBarCommon = (SearchBarCommon)rootView.findViewById(R.id.searchBarCommon);
		mSearchBarCommon.setSearchBarCommonListen(new SearchBarCommon.SearchBarCommonListen() {
			@Override
			public void clickSearch(View view) {
				//进入search界面进行上报
				// 上报进入个人中心
				ReportedInfo reportBuilder = new ReportedInfo();
				reportBuilder.statActId = ReportConstants.STATACT_ID_PAGE_VISIT;
				reportBuilder.statActId2 = 5;//搜索
				ReportConstants.getInstance().reportReportedInfo(reportBuilder);

				Intent intentSearch = new Intent(getActivity(), SearchActivity.class);
				intentSearch.putExtra(KEY.MAIN_TYPE, MAIN_TYPE./*GAME_CLASS*/ALL);
				intentSearch.putExtra(KEY.SUB_TYPE, ProtocolListener.SUB_TYPE.ALL);
				intentSearch.putExtra(KEY.ORDERBY, ORDER_BY.DOWNLOAD);
				startActivity(intentSearch);
			}

			@Override
			public void clickSettingImage(ImageView imageView) {
				Intent intentSetting = new Intent(getActivity(),
						SettingListActivity.class);
				startActivity(intentSetting);
			}
		});
//		setTitleAlpha(1.0f);
	}

	private void initListView() {
		pUIListView = (CSLoadingUIListView) mainView
				.findViewById(R.id.home_app_list);
		// 给listview设置头
		pUIListView.addListHeaderView(headView);
		pUIListView.setHeaderDividersEnabled(false);
		pUIListView.setCSListViewListener(mCSListViewListener);
		pUIListView.setFooterPullEnable(isFooterPullEnable);
		pUIListView.setHeaderPullEnable(false);
		pUIListView.setAdapter(gameListAdapter);
//		pUIListView.setOnScrollListener(mOnScrollListener);
	}

	public final static int MSG_REFRESH_TIPS = 0X01;
	private final Handler mUiDownLoadHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case Msg.RELOAD_PAGE:
					setDisplayStatus(LOADING_STATUS);
					Log.i(TAG, "global  "
							+ GlobalConfigControllerManager.getInstance()
							.getLoadingState());
					int global = GlobalConfigControllerManager.getInstance()
							.getLoadingState();
					switch (global) {
						case GlobalConfigControllerManager.NONETWORK_STATE:
							GlobalConfigControllerManager.getInstance()
									.reqGlobalConfig();
							Log.e(TAG, "game retry config");
							break;
						case GlobalConfigControllerManager.NORMAL_STATE:
							reqGameList();
							break;
						default:
							break;
					}
                    /*
                     * if (GlobalConfigControllerManager.getInstance()
                     * .getLoadingState() ==
                     * GlobalConfigControllerManager.NONETWORK_STATE) {
                     * GlobalConfigControllerManager.getInstance()
                     * .reqGlobalConfig(); Log.e(TAG, "game retry config"); } else
                     * reqGameList();
                     *
                     *
                     */
					break;
				case Msg.GET_DATA:
					Logger.i(TAG, "----- MSG_GET_DATA");
					// 界面显示
					setDisplayStatus(NORMAL_STATUS);
					// 设置广告为的数据
					LogUtils.e("设置广告位中数据");
					setCirculeDataToAdvView(getCarouselData((List<GroupElemInfo>) msg.obj));
					setListInfosData((List<GroupElemInfo>) msg.obj);
					// 设置数据
					pushData();
					// 推荐页数据加载完成后,发送消息给HomePageActivity
					if (null != mHandler && null != msg.obj && mCurrentPage == 1) {
						mHandler.sendEmptyMessage(HomePageActivity.MSG_NOTE_TO_HANDLE_DOWNLOAD_TASK);
					}
					pUIListView.stopFooterRefresh();
					mCurrentPage++;

					break;
				case Msg.SHOW_TOAST:
					String msgStr = (String) msg.obj;
					CSToast.show(getActivity(), msgStr);
					break;
				case Msg.NET_ERROR:
					Logger.i(TAG, "MSG_NET_ERROR");
					if (gameListAdapter != null && gameListAdapter.isEmpty()) {
						setDisplayStatus(NO_NETWORK_STATUS);
						break;
					}
					if (pUIListView != null) {
						pUIListView.stopFooterRefresh();
						if (hasLoadData)
							CSToast.show(mContext,
									mContext.getString(R.string.error_msg_net_fail));
					}
					break;

				case Msg.LOADING:
					break;
				case Msg.REFRESH_ADAPTER:
					if (gameListAdapter != null) {
						gameListAdapter.notifyDataSetChanged();
					}

					break;
				case Msg.LAST_PAGE:
					if (pUIListView != null) {
						pUIListView.setFooterPullEnable(false);
						isFooterPullEnable = false;
						pUIListView.stopFooterRefresh();
						setDisplayStatus(NORMAL_STATUS);
						CSToast.show(mContext,
								mContext.getString(R.string.tip_last_page));
					}
					break;
				case Msg.APPEND_DATA:
					if (GlobalConfigControllerManager.getInstance()
							.getLoadingState() != GlobalConfigControllerManager.NORMAL_STATE)
						break;

					if (null != mGroupElemInfo && mGroupElemInfo.size() > 0) {
						setDisplayStatus(NORMAL_STATUS);
						setCirculeDataToAdvView(getCarouselData(mGroupElemInfo));
						setListInfosData(mGroupElemInfo);
						Log.i(TAG, "Append data");
						// 设置数据
						pushData();
					} else {
						reqGameList();
					}
					break;
				case Msg.UPDATE_STATE:
					Log.i(TAG, "Game UPDATE_STATE");
					getDataList();
					break;
				case MSG_REFRESH_TIPS:
					mSearchBarCommon.setSettingTipVisible(UpdateUtils.hasUpdate() ? View.VISIBLE
							: View.GONE);
					break;
				default:
					break;
			}
		}

	};

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Logger.i(TAG, "onSaveInstanceState");
		if (mGroupElemInfo != null)
			outState.putSerializable(ISaveInfo.GROUP_ELEM_INFO,
					(ArrayList<GroupElemInfo>) mGroupElemInfo);
		if (mHomeAdvView != null)
			outState.putSerializable(ISaveInfo.RECOMMED_CIRCLE_INDEX,
					mHomeAdvView.getCurScreen());
		outState.putInt(ISaveInfo.CURRENT_PAGE, mCurrentPage);
		outState.putBoolean(ISaveInfo.IS_FOOTER_PULL, isFooterPullEnable);
		super.onSaveInstanceState(outState);
	}

	private ArrayList<GroupElemInfo> getCarouselData(List<GroupElemInfo> infos) {
		ArrayList<GroupElemInfo> carouseData = new ArrayList<GroupElemInfo>();
		for (GroupElemInfo info : infos) {
			if (HOME_PAGE_POSITION.CAROUSEL_POSITION == info.posId) {
				carouseData.add(info);
			}
		}
		LogUtils.d("carouseData.size : " + carouseData.size());
		// 将轮播按orderNo进行排序
		Collections.sort(carouseData, new Comparator<GroupElemInfo>() {

			@Override
			public int compare(GroupElemInfo lhs, GroupElemInfo rhs) {
				return lhs.orderNo - rhs.orderNo;
			}

		});
		return carouseData;
	}

	private static final int NORMAL_STATUS = 0;
	private static final int LOADING_STATUS = 1;
	private static final int NO_NETWORK_STATUS = 2;

	private void setDisplayStatus(int flag) {
		if (pUIListView == null) {
			return;
		}
		switch (flag) {
			case NORMAL_STATUS:
				pUIListView.setVisibility(View.VISIBLE);
				mLoadingFrame.setVisibility(View.GONE);
				mNoNetworkView.setVisibility(View.GONE);
				mLoadingView.setVisibility(View.GONE);
				isLoading = false;
				break;

			case LOADING_STATUS:
				pUIListView.setVisibility(View.GONE);
				mLoadingFrame.setVisibility(View.VISIBLE);
				mNoNetworkView.setVisibility(View.GONE);
				mLoadingView.setVisibility(View.VISIBLE);
				UITools.checkLoadingState(this);
				isLoading = true;
				break;

			case NO_NETWORK_STATUS:
				if (!mLoadingView.isShown())
					return;
				pUIListView.setVisibility(View.GONE);
				mLoadingFrame.setVisibility(View.VISIBLE);
				mNoNetworkView.setVisibility(View.VISIBLE);
				mLoadingView.setVisibility(View.GONE);
				isLoading = false;
				break;
			default:
				break;
		}
	}

	@Override
	public void onClick(View v) {
	}

	private void advOnClick(GroupElemInfo info) {
		//广告位 posId == 1
		if (info == null) {
			return;
		}
		int appPosType = ReportConstants.STATIS_TYPE.RECOM_ADV;
		int position = info.posId;
		Logger.i(TAG, "info  posId= " + info.posId);
		int posId = position + ReportConstants.STAC_APP_POSITION_RECOMM_PAGE;
		int orderIndex = mHomeAdvView.getCurScreen() + 1; // 因为后台从一开始计算的，而客户端是从零开始的
		int elemId = 0;
		int type = info.elemType;
		Intent intent = new Intent();
		switch (type) {
			case ELEMENT_TYPE.TYPE_APP:
				intent.setClass(
						getActivity(), PhoneAppInfoActivity.class
						/*App.getDevicesType() == App.PHONE ? PhoneAppInfoActivity.class
								: PadAppInfoActivity.class*/);
				intent.putExtra(KEY.GROUP_INFO, info);
				intent.putExtra(KEY.APP_POSITION, position);
				intent.putExtra(StatisticManager.APP_POS_TYPE, appPosType);
				intent.putExtra(MTAConstants.KEY_DETAIL_PAGE_FROM,
						MTAConstants.DETAIL_GAME_PAGE_CERCLE_ADV + orderIndex);
				elemId = info.appId;
				break;

			case ELEMENT_TYPE.TYPE_LINK:
				String link = info.jumpLinkUrl;
				Uri web = Uri.parse(link);
				intent = new Intent(Intent.ACTION_VIEW, web);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				elemId = info.jumpLinkId;
				break;

			case ELEMENT_TYPE.TYPE_SKIP_LOCAL_OR_ONLINE:
			case ELEMENT_TYPE.TYPE_SKIP_TIP:
			case ELEMENT_TYPE.TYPE_SKIP_CLASS:
				intent = jumpToTopicOrClass(intent, info, type, appPosType);
				elemId = info.jumpGroupId;
				break;

			// case ELEMENT_TYPE.TYPE_SKIP_LOCAL_OR_ONLINE :
			// intent.setClass( App.getAppContext( ) , .class );
			// intent.putExtra( AppListActivity.LIST_TYPE_KEY , getGroupIDByDB(
			// info ) );
			// break;
			default:
				break;
		}
		// 广告位点击上报
		ReportedInfo build = new ReportedInfo();
		build.statActId = ReportConstants.STATACT_ID_ADV;
		build.statActId2 = 1; //首页元素
		build.ext1 = posId + "";
		build.ext2 = type + "";
		build.ext3 = elemId + "";
		build.ext4 = 1 + ""; //showType 1 广告位, 0 表示其他
		build.ext5 = orderIndex + "";

		ReportConstants.getInstance().reportReportedInfo(build);
		MtaUtils.trackAdvClick(false, "CircleAdv " + orderIndex);
		startActivity(intent);
	}

	@Override
	public void onCirCuleAdvListener(int i, GroupElemInfo info) {
		Logger.i(TAG, "i = " + i);
		if (info == null) {
			return;
		}
		// 推荐页广告栏1
		advOnClick(info);
	}

	private Intent jumpToTopicOrClass(Intent intent, GroupElemInfo info,
									  int type, int appPosType) {
		Logger.i(TAG, "type = " + type + "---appPosType=" + appPosType);
		// if( type == ELEMENT_TYPE.TYPE_SKIP_TIP )
		// {
		// intent.putExtra( KEY.MAIN_TYPE , MAIN_TYPE.TOPIC );
		// }
		// else if( type == ELEMENT_TYPE.TYPE_SKIP_CLASS )
		// {
		// intent.putExtra( KEY.MAIN_TYPE , MAIN_TYPE.GAME_CLASS );
		// }
		if (info.jumpGroupId > 0) {
			GroupInfo mGroupInfo = new GroupInfo();
			String selection = GroupInfoColumns.GROUP_ID + " =?";
			String[] selectionArgs = new String[]{info.jumpGroupId + ""};

			mGroupInfo = DatabaseUtils.getGroupinfoByDB(selection, selectionArgs);

			TopicInfo topicInfo = new TopicInfo();
			topicInfo.mAppCount = mGroupInfo.recommWrod;
			topicInfo.mTopic = mGroupInfo.groupName;
			topicInfo.mTip = mGroupInfo.groupDesc;
			topicInfo.mPicUrl = mGroupInfo.groupPicUrl;

			intent.putExtra(KEY.TOPIC_INFO, topicInfo);

			intent.putExtra(KEY.MAIN_TYPE, MAIN_TYPE.TOPIC);
			intent.putExtra(KEY.ITEM_TYPE, ITEM_TYPE.UNSHOW_SNAPSHOT);

			intent.putExtra(KEY.GROUP_ID, mGroupInfo.groupId);
			intent.putExtra(KEY.GROUP_CLASS, mGroupInfo.groupClass);
			intent.putExtra(KEY.GROUP_TYPE, mGroupInfo.groupType);
			intent.putExtra(KEY.ORDERBY, mGroupInfo.orderType);
			intent.putExtra(KEY.CATEGORY_NAME, mGroupInfo.groupName
                    /*mContext.getString(R.string.topic_game_label)*/);
		} else {
			//跳转分类
			String selection = GroupInfoColumns.GROUP_TYPE + " =? and " + GroupInfoColumns.ORDER_TYPE + " =?";
			String[] selectionArgs = new String[]{info.jumpGroupType + "", info.jumpOrderType + ""};
			GroupInfo groupInfo = DatabaseUtils.getGroupinfoByDB(selection, selectionArgs);
			intent.putExtra(KEY.GROUP_ID, groupInfo.groupId);
			intent.putExtra(KEY.GROUP_CLASS, groupInfo.groupClass);
			intent.putExtra(KEY.GROUP_TYPE, groupInfo.groupType);
			intent.putExtra(KEY.ORDERBY, groupInfo.orderType);
			intent.putExtra(KEY.CATEGORY_NAME, groupInfo.groupName);
			intent.putExtra(KEY.MAIN_TYPE, MAIN_TYPE.JING_PIN);
		}

		intent.putExtra(KEY.ORDERBY, info.jumpOrderType);
		intent.putExtra(StatisticManager.APP_POS_TYPE, appPosType);
		intent.setClass(App.getAppContext(), GroupAppListActivity.class);

		return intent;
	}

	private void reqGameList() {


		int ints[] = DatabaseUtils.getGroupIdByDB(
				App.ismAllGame() ? GROUP_TYPE.GAME_RECOMMED_TYPE : GROUP_TYPE.HOME_RECOMMED_TYPE
				, ORDER_BY.AUTO);
		LogUtils.d("ints[0] = " + ints[0] + " ints[1] = " + ints[1]
				+ " ints[2] = " + ints[2] + " ints[3] = " + ints[3]);
		ReqGroupElemsListController controller = new ReqGroupElemsListController(
				ints[0], ints[1], ints[2], ints[3],
				HomePageActivity.REQ_PAGE_SIZE, mCurrentPage,
				mReqGroupElemsListener);
		// 云指令跳转渠道号设置
		// controller.setChnNo( ( (HomePageActivity)getActivity( ) ).getChnNo( )
		// );
		controller.doRequest();
	}

	private final ReqGroupElemsListener mReqGroupElemsListener = new ReqGroupElemsListener() {

		@Override
		public void onNetError(int errCode, String errorMsg) {
			Logger.e(TAG, "onNetError:" + errorMsg + ",errCode:" + errCode);
			mUiDownLoadHandler.sendEmptyMessage(Msg.NET_ERROR);
		}

		@Override
		public void onReqFailed(int statusCode, String errorMsg) {
			Logger.e(TAG, "errorMsg:" + errorMsg + ",statusCode:" + statusCode);
			mUiDownLoadHandler.sendEmptyMessage(Msg.NET_ERROR);
		}

		@Override
		public void onReqGroupElemsSucceed(GroupElemInfo[] infoList,
										   String serverDataVer) {
			Logger.i(TAG, "onReqGroupElemsSucceed:" + infoList.length);
			// 将infoList按照Position ID 进行排序
			if (infoList != null && infoList.length > 0) {
				mGroupElemInfo.addAll(Tools.arrayToList(infoList));
				Message msg = Message.obtain();
				msg.what = Msg.GET_DATA;
				msg.obj = Tools.arrayToList(infoList);
				mUiDownLoadHandler.sendMessage(msg);
			} else {
				mUiDownLoadHandler.sendEmptyMessage(Msg.LAST_PAGE);
			}
		}
	};

	@Override
	public Handler getHandler() {
		return mUiDownLoadHandler;
	}

	public void addHandler(Handler handler) {
		mHandler = handler;
	}

	private synchronized void getDataList() {
		UITools.notifyStateChange(this);
	}

	@Override
	public void setHasLoadedData(boolean loaded) {
		hasLoadData = loaded;
	}

	@Override
	public boolean hasLoadedData() {
		return hasLoadData;
	}

	@Override
	public void initFragmentListData() {
		reqGameList();
	}

	@Override
	public boolean isLoading() {
		return isLoading || mLoadingView.isShown();
	}

	@Override
	public String getFragmentTabLabel() {
		// TODO Auto-generated method stub
		//此处返回推荐的table，游戏中心平板版本
		if (App.getDevicesType() == App.PHONE) {
			return IFragmentInfo.FragmentTabLabel.GAME_LABEL;

		} else {
			return FragmentTabLabel.RECOM_LABEL;
		}
	}

	private int getInternalDimensionSize(Resources res, String key) {
		int result = 0;
		int resourceId = res.getIdentifier(key, "dimen", "android");
		if (resourceId > 0) {
			result = res.getDimensionPixelSize(resourceId);
		}
		return result;
	}
	//################渐变透明actionbar
	private int mMinHeaderTranslation;
	private HorizonScrollLayout mHorizonScrollLayout;
	private AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			mMinHeaderTranslation = -mHorizonScrollLayout.getHeight() + mSearchBarCommon.getHeight() /*+ mStautsBarHeight*/;
			int scrollY = getScrollY();
//                Logger.i(TAG, "onscrolly "+ scrollY+ "  mMinHeaderTranslation "+ mMinHeaderTranslation, "oddshou");
			//sticky actionbar
//            if (mMinHeaderTranslation != mStautsBarHeight){
//                mTopicTop.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));
//            }
			//header_logo --> actionbar icon
			float ratio = clamp(/*mTopicTop.getTranslationY()*/ -(float)scrollY/mMinHeaderTranslation, 0.0f, 1.0f);
//			Logger.i(TAG, "-scrollY/mMinHeaderTranslation " + -scrollY + "/"+ mMinHeaderTranslation, "oddshou");

//                interpolate(mHeaderLogo, getActionBarIconView(), mSmoothInterpolator.getInterpolation(ratio));
			//actionbar title alpha
			//getActionBarTitleView().setAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
			//---------------------------------
			//better way thanks to @cyrilmottier
//                Logger.i(TAG, "onscrolly "+ scrollY+ "  alpha "+ clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F) + "  ratio "+ ratio  + " mMinHeaderTranslation " + mMinHeaderTranslation, "oddshou");
			setTitleAlpha(clamp(5.0F * ratio - 3.0F, 0.0F, 1.0F));
		}
	};

	private void setTitleAlpha(float alpha) {
		mSearchBarCommon.getBackground().mutate().setAlpha((int) (alpha * 255));
//		mSearchBarCommon.getmTitleView().setAlpha(alpha);
//		if (mTintManager != null) {
//			mTintManager.setStatusBarAlpha(alpha);
//		}
		//        mAlphaForegroundColorSpan.setAlpha(alpha);
//        mSpannableString.setSpan(mAlphaForegroundColorSpan, 0, mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        getActionBar().setTitle(mSpannableString);
	}

	/**
	 * @param value
	 * @param min
	 * @param max
	 * @return 返回一个 介于 min 和 max 间的 value
	 */
	public static float clamp(float value, float min, float max) {
		return Math.max(min, Math.min(value, max));
	}

	/**
	 * 起始位置 0，向上滑动 增加
	 *
	 * @return
	 */
	public int getScrollY() {
		View c = pUIListView.getChildAt(0);
		if (c == null) {
			return 0;
		}
		int firstVisiblePosition = pUIListView.getFirstVisiblePosition();
		int top = c.getTop();
		int headerHeight = 0;
		if (firstVisiblePosition >= 1) {
			headerHeight = mHorizonScrollLayout.getHeight();
		}

		Logger.i(TAG, "-top " + -top + " firstVisiblePosition * c.getHeight() " + firstVisiblePosition + "*" + c.getHeight()
				+ " headerHeight " + headerHeight, "oddshou");
		/**
		 *  oddshou
		 *  -top 代表单个item 划出屏幕而没有回收的高度
		 *  firstVisiblePosition * c.getHeight(), firstVisiblePosition >=1 才有效，而此时 head 已超出被回收
		 *  c.getHeight()， firstVisiblePosition >=1 时 为普通 item 高度
		 */
		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}
	//################渐变透明actionbar
}
