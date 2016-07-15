package com.hykj.gamecenter.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.GlobalConfigControllerManager;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.adapter.ClassifyListAdapter;
import com.hykj.gamecenter.controller.ProtocolListener.CLASSIFY_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.GROUP_CLASS;
import com.hykj.gamecenter.controller.ProtocolListener.GROUP_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.MAIN_TYPE;
import com.hykj.gamecenter.data.GroupInfo;
import com.hykj.gamecenter.db.CSACContentProvider;
import com.hykj.gamecenter.db.CSACDatabaseHelper.GroupInfoColumns;
import com.hykj.gamecenter.logic.entry.ISaveInfo;
import com.hykj.gamecenter.logic.entry.Msg;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.UITools;
import com.hykj.gamecenter.utilscs.LogUtils;

import java.util.ArrayList;

public class ClassifyAppFragment extends BaseFragment {
	private static final String TAG = "ClassifyFragment";

	private View mainView = null;
	private Activity mActivity;

	private final ArrayList<GroupInfo> mAppGroupInfoList = new ArrayList<GroupInfo>();

	private ListView mListView;
	private ClassifyListAdapter mAdapter;

	private View mLoadingFrame;
	private View mLoadingView;
	private View mNoNetworkView;
	private boolean hasLoadData = false;
	private boolean isLoading = true;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		/*
		 * if (activity != null) ((HomePageActivity)
		 * activity).setmClassifyFragment(this);
		 */
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalConfigControllerManager.getInstance().registForUpdate(mHandler,
				Msg.UPDATE_STATE, null);

		LogUtils.d("owenli onCreate()");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtils.d("owenli onCreateView()");
		mActivity = getActivity();
		mainView = inflater.inflate(R.layout.classify_list, container, false);

		initListView();
		initExceptionView();
		setDisplayStatus(LOADING_STATUS);
		getSaveState(savedInstanceState);
		// sendAppendDataMsg( savedInstanceState );
		getDataList();
		return mainView;
	}

	@Override
	public void onConfigurationChanged(
			android.content.res.Configuration newConfig) {
		LogUtils.d("owenli onConfigurationChanged() ");                                                                                                                                                                                                                                                      
		if (mAdapter != null) {
			mAdapter.setColumnCount(UITools.getColumnNumber(mActivity));
		}
		super.onConfigurationChanged(newConfig);
	}

	private void getSaveState(Bundle savedInstanceState) {
		if (null != savedInstanceState) {
			ArrayList<GroupInfo> appGroupInfoList = (ArrayList<GroupInfo>) savedInstanceState
					.getSerializable(ISaveInfo.APP_CLASSIFY);

			mAppGroupInfoList.clear();
			mAppGroupInfoList.addAll(appGroupInfoList);
		}
	}

	private void initListView() {
		mListView = (ListView) mainView.findViewById(R.id.app_list);
		mAdapter = new ClassifyListAdapter(mActivity, MAIN_TYPE.APP_CLASS,
				ReportConstants.STATIS_TYPE.CLASSIFY, UITools.getColumnNumber(mActivity),
				CLASSIFY_TYPE.APP_CLASSIFY);

		mListView.setAdapter(mAdapter);
		/*
		 * for(int i = 0; i < expandableListAdapter.getGroupCount(); i++) {
		 * 
		 * expandableListView.expandGroup(i);
		 * 
		 * }
		 */
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
				int global = GlobalConfigControllerManager.getInstance()
						.getLoadingState();
				setDisplayStatus(LOADING_STATUS);
				switch (global) {
				case GlobalConfigControllerManager.NONETWORK_STATE:
					GlobalConfigControllerManager.getInstance()
							.reqGlobalConfig();
					Log.e(TAG, "game retry config");
					break;
				case GlobalConfigControllerManager.NORMAL_STATE:
					reqDataList(mAppGroupInfoList,
							GROUP_CLASS.APP_CLASSIFY_CLASS,
							GROUP_TYPE.ALL_APP_AND_GAME_TYPE);
					break;
				default:
					break;
				}
				/*
				 * if( GlobalConfigControllerManager.getInstance(
				 * ).getLoadingState( ) ==
				 * GlobalConfigControllerManager.NONETWORK_STATE ) {
				 * GlobalConfigControllerManager.getInstance( ).reqGlobalConfig(
				 * ); Log.e(TAG, "classify retry config"); } else { reqDataList(
				 * mGameGroupInfoList , GROUP_CLASS.GAME_CLASSIFY_CLASS ,
				 * GROUP_TYPE.ALL_GAMES_TYPE ); reqDataList( mAppGroupInfoList ,
				 * GROUP_CLASS.APP_CLASSIFY_CLASS ,
				 * GROUP_TYPE.ALL_APP_AND_GAME_TYPE ); }
				 */
			}
		});
	}

	private static final int NORMAL_STATUS = 0;
	private static final int LOADING_STATUS = 1;
	private static final int NO_NETWORK_STATUS = 2;

	private void setDisplayStatus(int flag) {
		if (mListView == null) {
			return;
		}
		switch (flag) {
		case NORMAL_STATUS:
			mListView.setVisibility(View.VISIBLE);
			mLoadingFrame.setVisibility(View.GONE);
			mNoNetworkView.setVisibility(View.GONE);
			mLoadingView.setVisibility(View.GONE);
			isLoading = false;
			break;

		case LOADING_STATUS:
			mListView.setVisibility(View.GONE);
			mLoadingFrame.setVisibility(View.VISIBLE);
			mNoNetworkView.setVisibility(View.GONE);
			mLoadingView.setVisibility(View.VISIBLE);
			isLoading = true;
			UITools.checkLoadingState(this);
			break;

		case NO_NETWORK_STATUS:
			mListView.setVisibility(View.GONE);
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
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(ISaveInfo.APP_CLASSIFY, mAppGroupInfoList);
		super.onSaveInstanceState(outState);
	}

	private void reqDataList(ArrayList<GroupInfo> infoList, int groupClass,
			int groupType) {
		infoList.clear();
		Cursor classifyCursor = App
				.getAppContext()
				.getContentResolver()
				.query(CSACContentProvider.GROUPINFO_CONTENT_URI,
						null,
						GroupInfoColumns.GROUP_CLASS + "=" + groupClass
								+ " and " + GroupInfoColumns.GROUP_TYPE + ">"
								+ groupType, null, " order_no asc");
		if (classifyCursor != null && classifyCursor.moveToNext()) {
			while (!classifyCursor.isAfterLast()) {
				GroupInfo groupInfo = new GroupInfo();
				groupInfo.groupId = classifyCursor.getInt(classifyCursor
						.getColumnIndex(GroupInfoColumns.GROUP_ID));
				groupInfo.groupClass = classifyCursor.getInt(classifyCursor
						.getColumnIndex(GroupInfoColumns.GROUP_CLASS));
				groupInfo.groupType = classifyCursor.getInt(classifyCursor
						.getColumnIndex(GroupInfoColumns.GROUP_TYPE));
				groupInfo.orderType = classifyCursor.getInt(classifyCursor
						.getColumnIndex(GroupInfoColumns.ORDER_TYPE));
				groupInfo.orderNo = classifyCursor.getInt(classifyCursor
						.getColumnIndex(GroupInfoColumns.ORDER_NO));
				groupInfo.recommWrod = classifyCursor.getString(classifyCursor
						.getColumnIndex(GroupInfoColumns.RECOMM_WORD));
				groupInfo.groupName = classifyCursor.getString(classifyCursor
						.getColumnIndex(GroupInfoColumns.GROUP_NAME));
				groupInfo.groupDesc = classifyCursor.getString(classifyCursor
						.getColumnIndex(GroupInfoColumns.GROUP_DESC));
				groupInfo.groupPicUrl = classifyCursor.getString(classifyCursor
						.getColumnIndex(GroupInfoColumns.GROUP_PIC_URL));
				groupInfo.startTime = classifyCursor.getString(classifyCursor
						.getColumnIndex(GroupInfoColumns.START_TIME));
				groupInfo.endTime = classifyCursor.getString(classifyCursor
						.getColumnIndex(GroupInfoColumns.END_TIME));
				// Logger.d( "TAG" , "groupInfo.groupId --- " +
				// groupInfo.groupId );
				// Logger.d( "TAG" , "groupInfo.groupClass --- " +
				// groupInfo.groupClass );
				// Logger.d( "TAG" , "groupInfo.groupType --- " +
				// groupInfo.groupType );
				// Logger.d( "TAG" , "groupInfo.orderType --- " +
				// groupInfo.orderType );
				// Logger.d( "TAG" , "groupInfo.orderNo --- " +
				// groupInfo.orderNo );
				// Logger.d( "TAG" , "groupInfo.recommWrod --- " +
				// groupInfo.recommWrod );
				// Logger.d( "TAG" , "groupInfo.groupName --- " +
				// groupInfo.groupName );
				// Logger.d( "TAG" , "groupInfo.groupDesc --- " +
				// groupInfo.groupDesc );
				// Logger.d( "TAG" , "groupInfo.groupClass --- " +
				// groupInfo.groupClass );
				LogUtils.d("groupInfo.groupId : " + groupInfo.groupId
						+ "groupInfo.groupName : " + groupInfo.groupName);
				infoList.add(groupInfo);

				classifyCursor.moveToNext();
			}
			classifyCursor.close();
		}
	}

	@Override
	public void onResume() {
		/*
		 * if(GlobalConfigControllerManager.getInstance().isHasLoaded()){
		 * getDataList(); }
		 */
		LogUtils.d("owenli onResume()");
		super.onResume();
	}

	@Override
	public void onDestroyView() {
		Logger.i("ClassifyFragment", "onDestoryView");
		super.onDestroy();
	}

	@Override
	public void onDestroy() {
		Logger.i("ClassifyFragment", "onDestory");
		GlobalConfigControllerManager.getInstance().unregistForUpdate(mHandler);
		super.onDestroy();
	}

	/**
	 * 获取横竖屏需要保存的数据
	 */
	public ArrayList<GroupInfo> saveInfo() {
		return mAppGroupInfoList;
	}

	// private boolean isActive = true;
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case Msg.NET_ERROR:
				if (mAppGroupInfoList.size() <= 0) {
					setDisplayStatus(NO_NETWORK_STATUS);
				}
				break;
			case Msg.LOADING:
				break;
			case Msg.APPEND_DATA:
				if (GlobalConfigControllerManager.getInstance()
						.getLoadingState() != GlobalConfigControllerManager.NORMAL_STATE)
					break;
				LogUtils.d("1 app list" + mAppGroupInfoList.size());

				LogUtils.d("2 app list" + mAppGroupInfoList.size());
				if (mAppGroupInfoList.size() <= 0) {

					reqDataList(mAppGroupInfoList,
							GROUP_CLASS.APP_CLASSIFY_CLASS,
							GROUP_TYPE.ALL_APP_AND_GAME_TYPE);
				}
				LogUtils.d("Owenli 3 app list" + mAppGroupInfoList.size());

				setDisplayStatus(NORMAL_STATUS);
				mAdapter.removeAllData();
				mAdapter.appendData(mAppGroupInfoList);
				mAdapter.notifyDataSetChanged();
				LogUtils.d("Owenli 4 app list" + mAppGroupInfoList.size());
				break;
			case Msg.UPDATE_STATE:
				Log.i(TAG, "Classify UPDATE_STATE");
				getDataList();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public Handler getHandler() {
		return mHandler;
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
		reqDataList(mAppGroupInfoList, GROUP_CLASS.APP_CLASSIFY_CLASS,
				GROUP_TYPE.ALL_APP_AND_GAME_TYPE);
		setDisplayStatus(NORMAL_STATUS);
		mAdapter.removeAllData();
		mAdapter.appendData(mAppGroupInfoList);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean isLoading() {
		return mLoadingView.isShown();
	}
}
