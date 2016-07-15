
package com.hykj.gamecenter.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.GlobalConfigControllerManager;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.adapter.IListItemCheckedClickListener;
import com.hykj.gamecenter.adapter.IViewVisiableChangedListener;
import com.hykj.gamecenter.adapter.NoviceGridAdapter;
import com.hykj.gamecenter.controller.ProtocolListener.GROUP_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.MAIN_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.ORDER_BY;
import com.hykj.gamecenter.controller.ProtocolListener.PAGE_SIZE;
import com.hykj.gamecenter.controller.ProtocolListener.ReqGroupElemsListener;
import com.hykj.gamecenter.controller.ReqGroupElemsListController;
import com.hykj.gamecenter.db.DatabaseUtils;
import com.hykj.gamecenter.logic.entry.Msg;
import com.hykj.gamecenter.net.APNUtil;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.ui.InWifiDownLoadDialog.KnowBtnOnClickListener;
import com.hykj.gamecenter.ui.widget.BulkDownloadListButton;
import com.hykj.gamecenter.ui.widget.InterceptTouchFrameLayout;
import com.hykj.gamecenter.ui.widget.OnWifiClickListener.WifiDownLoadOnClickListener;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.Tools;
import com.hykj.gamecenter.utils.UITools;
import com.hykj.gamecenter.utils.UpdateUtils;
import com.hykj.gamecenter.utilscs.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class NoviceGuidanceAppView extends InterceptTouchFrameLayout implements
		IListItemCheckedClickListener {

	private static final String TAG = NoviceGuidanceAppView.class.getName();

	public static final int MSG_HOME_REFRESH = 1;

	private static final int NORMAL_STATUS = 0;
	private static final int LOADING_STATUS = 1;
	private static final int NO_NETWORK_STATUS = 2;
	//    private Button btnSkip = null;
	private BulkDownloadListButton btnInstall = null;
	private TextView mTextWifi;
	private final Context mContext;
	private Resources mRes = null;
	// 异常界面
	private View mLoadingFrame;
	private View mLoadingView;
	private View mNoNetworkView;
	private boolean isLoading = true;
	private boolean hasLoadData = false;
	private int iTabHeight, iHeightLanAdv, iHeightPorAdv;

	//    private NoviceGuidanceAppAdapter mNoviceGuidanceAppAdapter;
	private NoviceGridAdapter mNoviceGridAdapter;
	NoScrollListView mNoviceGuidanceAppListView = null;
	private GridView mGridView;
	TextView tvPaddingtextview = null;
	// ImageView mIVWelcome = null;
	// ImageView mIVWelcomeImageText = null;

	private final ArrayList<GroupElemInfo> mAppGroupElemInfoList = new ArrayList<GroupElemInfo>();
	private int mGroupElemRealSize = 0;
	//    private List<GroupElemInfo> mAppGroupElemInfoList = new ArrayList<GroupElemInfo>();

	IViewVisiableChangedListener mViewVisiableChangedListener = null;
	private ImageView imgClose;
	private TextView mTextHeight4GridView;

	public void setOnVisiableChangedListener(IViewVisiableChangedListener listener) {
		mViewVisiableChangedListener = listener;
	}

	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			// 暂停消息发送的时候，不需处理
			switch (msg.what) {
				case Msg.REFRESH_LIST:// 网络恢复时刷新列表
					setDisplayStatus(LOADING_STATUS);
					int global = GlobalConfigControllerManager.getInstance().getLoadingState();
					switch (global) {
						case GlobalConfigControllerManager.NONETWORK_STATE:
							GlobalConfigControllerManager.getInstance().reqGlobalConfig();
							Log.e(TAG, "game retry config");
							break;
						case GlobalConfigControllerManager.NORMAL_STATE:
							reqAppList();
							break;
						default:
							break;
					}
					/*
					 * if (GlobalConfigControllerManager.getInstance()
                     * .getLoadingState() ==
                     * GlobalConfigControllerManager.NONETWORK_STATE) {
                     * GlobalConfigControllerManager.getInstance()
                     * .reqGlobalConfig(); Log.e(TAG, "retry config"); } else
                     * reqAppList();
                     */
					break;
				case MSG_HOME_REFRESH:
					// if (!UITools.isPortrait()) {
					// mIVWelcomeImageText
					// .setBackgroundResource(R.drawable.start_page_words_portait);
					// mIVWelcome
					// .setBackgroundResource(R.drawable.start_page_portait);
					// } else {
					// mIVWelcomeImageText
					// .setBackgroundResource(R.drawable.start_page_words);
					// mIVWelcome.setBackgroundResource(R.drawable.start_page);
					// }

//                    if (mNoviceGuidanceAppAdapter != null) {
//                        mNoviceGuidanceAppAdapter.setDisplayImage(true);
//                        mNoviceGuidanceAppAdapter.notifyDataSetChanged();
//                    }
					if (mNoviceGridAdapter != null) {
						mNoviceGridAdapter.notifyDataSetChanged();
					}
					break;
				case Msg.NET_ERROR:
					if (mAppGroupElemInfoList.size() <= 0) {
						setDisplayStatus(NO_NETWORK_STATUS);
					}
					break;
				case Msg.LOADING:
					break;
				case Msg.GET_DATA: // 得到新手推荐页数据
					Logger.i(TAG, "----- Msg.GET_DATA");
					// 界面显示
					setDisplayStatus(NORMAL_STATUS);
					// 设置数据
					// 当前不需要，因为应用启动时已经加载过了
					// 推荐页数据加载完成后,发送消息给HomePageActivity
					/*
					 * if (null != mHandler && null != msg.obj) { // 应用商店本身更新查询
                     * mHandler.sendEmptyMessage(HomePageActivity.
                     * MSG_NOTE_TO_HANDLE_DOWNLOAD_TASK); }
                     */
					mAppGroupElemInfoList.clear();
					//只取前六个本地没有安装的应用
					ArrayList<GroupElemInfo> list = (ArrayList<GroupElemInfo>) msg.obj;
					List<PackageInfo> packageInfoes = getInstalledApps();
					for (GroupElemInfo groupElemInfo : list
							) {
						boolean add = true;
						for (PackageInfo packageInfo : packageInfoes) {
							if (groupElemInfo.packName.equals(packageInfo.packageName)) {
								add = false;
							}
						}
						if (add) {
							mAppGroupElemInfoList.add(groupElemInfo);
							if (mAppGroupElemInfoList.size() >= UITools.getNoviceGuidanceSize(mContext)) {
								break;
							}
						}
					}

//                    mAppGroupElemInfoList.addAll((List<GroupElemInfo>) msg.obj);
					//不显示已安装应用
//                    List<PackageInfo> packageInfoes = getInstalledApps();
//                    for (PackageInfo pInfo : packageInfoes) {
//                        //遍历的同时删除 用迭代器
//                        Iterator<GroupElemInfo> iter = mAppGroupElemInfoList.iterator();
//                        while (iter.hasNext()) {
//                            GroupElemInfo elemInfo = iter.next();
//                            if (pInfo.packageName.equals(elemInfo.packName)) {
//                                iter.remove();
//                            }
//                        }
//                    }

//                    mNoviceGuidanceAppAdapter.removeAllData();
//                    mNoviceGuidanceAppAdapter.appendData(mAppGroupElemInfoList, true);
//                    mNoviceGuidanceAppAdapter.notifyDataSetChanged();


					//没有可推荐的应用不显示该页面
					if (mAppGroupElemInfoList.size() <= 0) {
						Logger.i(TAG, "oddshou mappGroupEleminfoList size = 0  msg.obj.size "
								+ ((List<GroupElemInfo>) msg.obj).size());
						NoviceGuidanceAppView.this.setVisibility(View.GONE);
					}
					//appendData 会添加空白的item ，保存 size
					mGroupElemRealSize = mAppGroupElemInfoList.size();
					mNoviceGridAdapter.appendData(mAppGroupElemInfoList, true);

					if (btnInstall != null) {
						btnInstall.setEnabled(true);
						// 设置按钮字体
//                        btnInstall.setText(String.format(
//                                mRes.getString(R.string.novice_guidance_install),
//                                mAppGroupElemInfoList.size()));
					}
					initText();

					LogUtils.d("after GET_DATA app list mAppGroupInfoList = "
							+ mAppGroupElemInfoList.size());

					break;
				case Msg.REQUEST_DATA:// 请求新手推荐数据

					LogUtils.d("REQUEST_DATA app list" + mAppGroupElemInfoList.size()
							+ "loadingState = "
							+ GlobalConfigControllerManager.getInstance().getLoadingState());

					if (GlobalConfigControllerManager.getInstance().getLoadingState() != GlobalConfigControllerManager.NORMAL_STATE)
						break;

					if (mAppGroupElemInfoList.size() <= 0) {
						reqAppList();
					}

					setDisplayStatus(LOADING_STATUS);

					break;
				case Msg.UPDATE_STATE:// 应用初始是获取的索引数据(存入数据库的索引数据)
					Log.i(TAG, "Classify UPDATE_STATE");
					getDataList();
					break;
				case Msg.LAST_PAGE: //请求失败或者没有推荐的应用
					if (btnInstall != null) {
						btnInstall.setEnabled(false);
					}
					break;
				default:
					break;
			}
		}

	};

	//得到本机已安装应用,包括所用应用(包括系统应用)
	public List<PackageInfo> getInstalledApps() {
		PackageManager packageManager = App.getAppContext().getPackageManager();

		List<PackageInfo> packageInfoes = packageManager
				.getInstalledPackages(PackageManager.GET_SIGNATURES);
		return packageInfoes;
	}

	private void setDisplayStatus(int flag) {
		if (/*mNoviceGuidanceAppListView*/mGridView == null) {
			return;
		}
		switch (flag) {
			case NORMAL_STATUS:
				/*mNoviceGuidanceAppListView*/
				mGridView.setVisibility(View.VISIBLE);
				mLoadingFrame.setVisibility(View.GONE);
				mNoNetworkView.setVisibility(View.GONE);
				mLoadingView.setVisibility(View.GONE);
				isLoading = false;
				break;

			case LOADING_STATUS:
                /*mNoviceGuidanceAppListView*/
				mGridView.setVisibility(View.GONE);
				mLoadingFrame.setVisibility(View.VISIBLE);
				mNoNetworkView.setVisibility(View.GONE);
				mLoadingView.setVisibility(View.VISIBLE);
				isLoading = true;
				// 一段时间后还在载入中，显示网络错误页面
				checkLoadingState();
				break;

			case NO_NETWORK_STATUS:
                /*mNoviceGuidanceAppListView*/
				mGridView.setVisibility(View.GONE);
				mLoadingFrame.setVisibility(View.VISIBLE);
				mNoNetworkView.setVisibility(View.VISIBLE);
				mLoadingView.setVisibility(View.GONE);
				isLoading = false;
				break;
			default:
				break;
		}
	}

	// 一段时间后还在载入中，显示网络错误页面
	public void checkLoadingState() {

		Log.e(TAG, "checkLoadingState ");
		new Thread() {
			@Override
			public void run() {
				try {
					Log.e(TAG, "check ");
					Thread.sleep(90 * 1000);
					if (GlobalConfigControllerManager.getInstance().getLoadingState() == GlobalConfigControllerManager.LOADING_STATE
							|| isLoading) {
						Log.e(TAG, "isLoading ");
						Message msg = mHandler.obtainMessage();
						msg.what = Msg.NET_ERROR;
						msg.sendToTarget();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}.start();
	}

	public void setHasLoadedData(boolean loaded) {
		hasLoadData = loaded;
	}

	public boolean hasLoadedData() {
		return hasLoadData;
	}

	private void appendData() {
		Log.d(TAG, "appendData");
		setHasLoadedData(true);
		mHandler.sendEmptyMessage(Msg.REQUEST_DATA);
	}

	public synchronized void notifyStateChange() {
		final Handler handler = mHandler;
		if (handler == null) {
			return;
		}
		Message msg = handler.obtainMessage();
		if (msg == null) {
			return;
		}
		int isGlobal = GlobalConfigControllerManager.getInstance().getLoadingState();
		Log.i(TAG, "isGlobal" + isGlobal);
		switch (isGlobal) {
			case GlobalConfigControllerManager.NORMAL_STATE:
				Log.d(TAG, "hasLoadData = " + hasLoadData);
				if (!hasLoadedData()) {
					appendData();
				}
				break;
			case GlobalConfigControllerManager.LOADING_STATE:
				msg.what = Msg.LOADING;
				msg.sendToTarget();
				break;
			case GlobalConfigControllerManager.NONETWORK_STATE:
				msg.what = Msg.NET_ERROR;
				msg.sendToTarget();
				break;
			default:
				break;
		}

	}

	public synchronized void getDataList() {
		notifyStateChange();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		initView();
	}

	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
	}

	private void initExceptionView() {

		mLoadingFrame = findViewById(R.id.loading_interface);

		mLoadingView = mLoadingFrame.findViewById(R.id.csl_cs_loading);
		mNoNetworkView = mLoadingFrame.findViewById(R.id.csl_cs_listview_no_networking);
		Button settingbutton = (Button) mLoadingFrame.findViewById(R.id.csl_network_setting_btn);
		settingbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toActivity = new Intent(Settings.ACTION_WIFI_SETTINGS);
				toActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(toActivity);
			}
		});

		Button button = (Button) findViewById(R.id.csl_network_retry_btn);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mHandler.sendEmptyMessage(Msg.REFRESH_LIST);
			}
		});
	}

	private void reqAppList() {
		Log.d(TAG, "NoviceGuidanceAppView reqAppList");
		int ints[] = DatabaseUtils.getGroupIdByDB(GROUP_TYPE.BOOT_RECOMMED_TYPE, ORDER_BY.AUTO);
		LogUtils.d("ints[0] = " + ints[0] + " ints[1] = " + ints[1] + " ints[2] = " + ints[2]
				+ " ints[3] = "
				+ ints[3]);
		ReqGroupElemsListController controller = new ReqGroupElemsListController(ints[0], ints[1],
				ints[2],
				ints[3], PAGE_SIZE.MAX_COUNT, 0, mReqGroupElemsListener);
		// 云指令跳转渠道号设置
		// controller.setChnNo( ( (HomePageActivity)getActivity( ) ).getChnNo( )
		// );
		controller.doRequest();
	}

	private final ReqGroupElemsListener mReqGroupElemsListener = new ReqGroupElemsListener() {

		@Override
		public void onNetError(int errCode, String errorMsg) {
			Logger.e("mReqGameElemsListener", "onNetError errCode:" + errCode + ",errorMsg:"
					+ errorMsg);
			mHandler.sendEmptyMessage(Msg.NET_ERROR);
		}

		@Override
		public void onReqFailed(int statusCode, String errorMsg) {
			Logger.e("mReqGameElemsListener", "onReqFailed statusCode:" + statusCode + ",errorMsg:"
					+ errorMsg);
			mHandler.sendEmptyMessage(Msg.LAST_PAGE);
		}

		@Override
		public void onReqGroupElemsSucceed(GroupElemInfo[] infoList, String serverDataVer) {
			Logger.d(TAG, "onReqGroupElemsSucceed infoList.size()= " + infoList.length);
			if (NoviceGuidanceAppView.this.getVisibility() == View.VISIBLE) {
				UpdateUtils.setUpdateRecommTimePreference();
			}
			if (infoList.length <= 0) {
				mHandler.sendEmptyMessage(Msg.LAST_PAGE);
			} else {
				Message msg = Message.obtain();
				msg.what = Msg.GET_DATA;
				msg.obj = Tools.arrayToList(infoList);
				mHandler.sendMessage(msg);
			}

		}

	};

	public void initView() {
		Log.e(TAG, "initView() mHandler = " + mHandler);

		// 通知
		GlobalConfigControllerManager.getInstance().registForUpdate(mHandler, Msg.UPDATE_STATE,
				null);

		mRes = mContext.getResources();
		initExceptionView();

//        mNoviceGuidanceAppListView = (NoScrollListView) findViewById(R.id.app_list);
		mGridView = (GridView) findViewById(R.id.gridView);
		mGridView.setNumColumns(UITools.getNoviceGuidanceColumnNumber(mContext));
//        if (mNoviceGuidanceAppAdapter == null) {
//            mNoviceGuidanceAppAdapter = new NoviceGuidanceAppAdapter(mContext, MAIN_TYPE.APP,
//                    UITools.getNoviceGuidanceColumnNumber(mContext));
//        }

		// setAppPosType:上报腾讯云或后台用的
//        mNoviceGuidanceAppAdapter.setAppPosType(ReportConstants.STATIS_TYPE.RANKING);
//        mNoviceGuidanceAppAdapter.showSnapShot(false);
//
//        mNoviceGuidanceAppAdapter.setListItemCheckedClickListener(this);
		mNoviceGridAdapter = new NoviceGridAdapter(mContext, MAIN_TYPE.APP);
		// TODO: 2015/12/26 需要设置 行列
		mNoviceGridAdapter.setListItemCheckedClickListener(this);



		LayoutInflater lif = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /*
         * View headerView = lif.inflate(R.layout.novice_guide_headview, null);
         * 
         * mIVWelcome = (ImageView) headerView.findViewById(R.id.welcomeImage);
         * mIVWelcomeImageText = (ImageView) headerView
         * .findViewById(R.id.welcomeImageText);
         * 
         * mNoviceGuidanceAppListView.addHeaderView(headerView);
         */
//        mNoviceGuidanceAppListView.setAdapter(mNoviceGuidanceAppAdapter);
		mGridView.setAdapter(mNoviceGridAdapter);
		//设置 gridView 的默认高度
		mTextHeight4GridView = (TextView) findViewById(R.id.heigth4GridView);
		resetGridHeight();
//        btnSkip = (Button) findViewById(R.id.skipBtn);
//        btnSkip.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                NoviceGuidanceAppView.this.setVisibility(View.GONE);
//            }
//        });
		imgClose = (ImageView) findViewById(R.id.imgClose);
		imgClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NoviceGuidanceAppView.this.setVisibility(View.GONE);
			}
		});
		btnInstall = (BulkDownloadListButton) findViewById(R.id.installBtn);
//        btnInstall.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                return;
//            }
//        });

		btnInstall.setKownOnClickListener(mKnowBtnOnClickListener);
		btnInstall.setWifiLoadOnClickListener(mWifiDownLoadOnClickListener);
		//        btnInstall.setAppPosType(appPosType);
//        btnInstall.setText(String.format(mRes.getString(R.string.novice_guidance_install),
//                mAppGroupElemInfoList.size()));
		btnInstall.setEnabled(false);
        /*
         * //由于HomePageAcitvity在NORMAL_STATE状态没有请求数据，所以这里要自身请求 if
         * (GlobalConfigControllerManager.getInstance().getLoadingState() ==
         * GlobalConfigControllerManager.NORMAL_STATE) {
         * mHandler.sendEmptyMessage(Msg.REQUEST_DATA);//getDataList(); }
         */

		mTextWifi = (TextView) findViewById(R.id.text_wifi);

		tvPaddingtextview = (TextView) findViewById(R.id.paddingtextview);

		int iScreenWidth = Tools.getDisplayWidth(mContext);
		int iScreenHeight = Tools.getDisplayHeight(mContext);

		// tab 的高度
		iTabHeight = mRes.getDimensionPixelSize(R.dimen.csl_cs_action_bar_height);
		// 广告推荐位的高度 recommend adv height
		iHeightLanAdv = (int) ((iScreenWidth) / HorizonScrollLayout.RATIO_PAD + 0.5f); // 宽高5:1
		iHeightPorAdv = (int) ((iScreenHeight) / HorizonScrollLayout.RATIO_PAD + 0.5f); // 宽高5:1
		// int iHeightX = iScreenHeight - iHeightAdv - iTabHeight;
		// 设置view的位置
		// this.layout(0, iHeightX, iScreenWidth, iScreenHeight);
		// requestLayout();
//		getPaddingHeight();


		mHandler.sendEmptyMessage(MSG_HOME_REFRESH);
		setDisplayStatus(LOADING_STATUS);

	}

	private void resetGridHeight() {
		int pix4Grid = mRes.getDimensionPixelSize(R.dimen.novice_griditem_h) *
				(UITools.isPortrait() ? UITools.NOVICE_GUIDANCE_ROW_PORTRAIT : UITools.NOVICE_GUIDANCE_ROW_LANDSCAPE);
		mTextHeight4GridView.setHeight(pix4Grid);
	}

	private void initText() {
		boolean showWifiTips = APNUtil.isWifiDataEnable(mContext);
		mTextWifi.setVisibility(showWifiTips ? View.VISIBLE : View.INVISIBLE);
	}

	private void getPaddingHeight() {
		if (!UITools.isPortrait()) {
			tvPaddingtextview.setHeight(iTabHeight + iHeightPorAdv);
		} else {
			tvPaddingtextview.setHeight(iTabHeight + iHeightLanAdv);
		}
	}

	public void onSwitchScreenOrientation(android.content.res.Configuration newConfig) {
		Logger.i(TAG, "onSwitchScreenOrientation() ");
		// TODO: 2015/12/26
//        if (mNoviceGuidanceAppAdapter != null) {
//            mNoviceGuidanceAppAdapter.setColumnCount(UITools
//                    .getNoviceGuidanceColumnNumber(mContext));
//        }
		if (mNoviceGridAdapter != null && mGridView != null) {
			mGridView.setNumColumns(UITools.getNoviceGuidanceColumnNumber(mContext));
		}
		resetGridHeight();
//		getPaddingHeight();
		mHandler.sendEmptyMessage(MSG_HOME_REFRESH);

	}

	// 这里不用做具体的下载任务，因为BulkDownloadListButton中已做，这里只需要处理在wifi情况的工作，如：提示用户下载开始等
	private final WifiDownLoadOnClickListener mWifiDownLoadOnClickListener = new WifiDownLoadOnClickListener() {

		@Override
		public void onWifiClickListener(View v) {
			Log.d(TAG, "NoviceGuidanceAppView onWifiClickListener");

			ArrayList<GroupElemInfo> appGroupElemInfoList = new ArrayList<GroupElemInfo>();
//            ArrayList<Boolean> checkedStateList = (ArrayList<Boolean>) mNoviceGuidanceAppAdapter
//                    .GetObject();
			ArrayList<Boolean> checkedStateList = mNoviceGridAdapter.getCheckList();
			for (int i = 0, j = 0; i < mGroupElemRealSize; i++) {
				if (checkedStateList.get(i)) {
					appGroupElemInfoList.add(j++, mAppGroupElemInfoList.get(i));
				}
			}
			btnInstall.AddDownloadTasks2(appGroupElemInfoList, true);

			NoviceGuidanceAppView.this.setVisibility(View.GONE);

		}
	};

	// 这里不用做具体的下载任务，因为BulkDownloadListButton中已做，这里只需要处理在非wifi情况的工作，如：提示用户下载开始等
	// 没有wifi状态下点击取消才会调用此函数
	private final KnowBtnOnClickListener mKnowBtnOnClickListener = new KnowBtnOnClickListener() {

		@Override
		public void onKnowClickListener(View v) {
			Log.d(TAG, "NoviceGuidanceAppView KnowBtnOnClickListener");

			ArrayList<GroupElemInfo> appGroupElemInfoList = new ArrayList<GroupElemInfo>();
//            ArrayList<Boolean> checkedStateList = (ArrayList<Boolean>) mNoviceGuidanceAppAdapter
//                    .GetObject();
			ArrayList<Boolean> checkedStateList = mNoviceGridAdapter.getCheckList();
			for (int i = 0, j = 0; i < mGroupElemRealSize; i++) {
				if (checkedStateList.get(i)) {
					appGroupElemInfoList.add(j++, mAppGroupElemInfoList.get(i));
				}
			}
			btnInstall.AddDownloadTasks2(appGroupElemInfoList, true);

			NoviceGuidanceAppView.this.setVisibility(View.GONE);
		}
	};

	@Override
	public Handler getHandler() {
		return mHandler;
	}

	public NoviceGuidanceAppView(Context context) {
		super(context);
		this.mContext = context;
        /*
         * // 通知
         * GlobalConfigControllerManager.getInstance().registForUpdate(mHandler,
         * Msg.UPDATE_STATE, null);
         */
	}

	public NoviceGuidanceAppView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
        /*
         * // 通知
         * GlobalConfigControllerManager.getInstance().registForUpdate(mHandler,
         * Msg.UPDATE_STATE, null);
         */
	}

	public NoviceGuidanceAppView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
        /*
         * // 通知
         * GlobalConfigControllerManager.getInstance().registForUpdate(mHandler,
         * Msg.UPDATE_STATE, null);
         */
	}

	@Override
	public void OnListItemCheckedClick(Boolean bChecked) {
		// TODO Auto-generated method stub

//        ArrayList<Boolean> checkedStateList = (ArrayList<Boolean>) mNoviceGuidanceAppAdapter
//                .GetObject();
		ArrayList<Boolean> checkedStateList = mNoviceGridAdapter.getCheckList();
		int iCnt = 0;
		if (checkedStateList != null) {

			for (int i = 0; i < mGroupElemRealSize; i++) {
				if (checkedStateList.get(i)) {
					iCnt++;
				}
			}
		}
		if (iCnt > 0) {
			btnInstall.setEnabled(true);
		} else {
			btnInstall.setEnabled(false);
		}
		// 设置按钮字体
//        btnInstall.setText(String.format(mRes.getString(R.string.novice_guidance_install), iCnt));
	}

	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		// TODO Auto-generated method stub
		super.onVisibilityChanged(changedView, visibility);

		Log.d(TAG, "NoviceGuidanceAppView onVisibilityChanged mViewVisiableChangedListener = "
				+ mViewVisiableChangedListener);
		if (mViewVisiableChangedListener != null) {
			mViewVisiableChangedListener.onVisibilityChanged(changedView, visibility);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}
}
