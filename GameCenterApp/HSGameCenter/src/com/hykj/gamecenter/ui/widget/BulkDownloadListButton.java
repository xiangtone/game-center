
package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.download.ApkDownloadManager;
import com.hykj.gamecenter.download.DownloadService;
import com.hykj.gamecenter.download.DownloadTask;
import com.hykj.gamecenter.download.DownloadTask.TaskState;
import com.hykj.gamecenter.mta.MtaUtils;
import com.hykj.gamecenter.net.APNUtil;
import com.hykj.gamecenter.protocol.Apps.AppInfo;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.InWifiDownLoadDialog;
import com.hykj.gamecenter.ui.InWifiDownLoadDialog.KnowBtnOnClickListener;
import com.hykj.gamecenter.ui.NoWifiContinueDownloadDialog;
import com.hykj.gamecenter.ui.widget.OnWifiClickListener.NoWifiContinueDownloadListener;
import com.hykj.gamecenter.ui.widget.OnWifiClickListener.WifiDownLoadOnClickListener;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.Tools;

import java.util.ArrayList;

//import cs.widget.CSAlertDialog;

//批量下载按钮
public class BulkDownloadListButton extends Button implements View.OnClickListener {
	private static final String TAG = "BulkDownloadListButton";

	private Context mContext = null;
	private WifiDownLoadOnClickListener mListener = null;
	private KnowBtnOnClickListener kListener = null;

	int mAppPosType = ReportConstants.STATIS_TYPE.NEW_PERSON_RECOM;

	private static final Object mAppInfoListLock = new Object();
	private final ArrayList<AppInfo> mAppInfoList = new ArrayList<AppInfo>();

	boolean bHasPromptedNoWifi = false; // 是否提示过没有wifi(对话框)
	boolean bHasPromptedContinueDownload = false; // 是否是否继续下载对话框
	boolean bOtherTaskContinueDownload = false; // 是否继续下载标志位
	boolean bHaveAddNullTaskToDownLoadList = false; // 已经把所有新任务加载到了下载队列
	private ApkDownloadManager mApkDownloadManager = null;


	public BulkDownloadListButton(Context context) {
		super(context);
		init(context);
	}

	public BulkDownloadListButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public BulkDownloadListButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		mApkDownloadManager = DownloadService.getDownloadManager();
		bHasPromptedNoWifi = false;
		bHasPromptedContinueDownload = false;
		bOtherTaskContinueDownload = false;
		bHaveAddNullTaskToDownLoadList = false;
		setOnClickListener(this);
	}

	public void setWifiLoadOnClickListener(WifiDownLoadOnClickListener listener) {
		mListener = listener;
	}

	public void setKownOnClickListener(KnowBtnOnClickListener listener) {
		kListener = listener;
	}

	public void setAppPosType(int appPosType) {
		mAppPosType = appPosType;
	}

	public void AddDownloadTasks(ArrayList<AppInfo> AppInfoList, boolean bClean) {

		synchronized (mAppInfoListLock) {
			if (bClean) {

				mAppInfoList.clear();

				// bHasPromptedNoWifi = false; // 是否提示过没有wifi(对话框)
				// bHasPromptedContinueDownload = false; // 是否是否继续下载对话框
				// bOtherTaskContinueDownload = false; // 是否继续下载标志位
				// bHaveAddNullTaskToDownLoadList = false; // 已经把所有新任务加载到了下载队列
			}
			mAppInfoList.addAll(AppInfoList);
			//Log.d( TAG , "AddDownloadTasks AppInfos mAppInfoList size = " + mAppInfoList.size( ) );
		}
	}

	public void AddDownloadTasks2(ArrayList<GroupElemInfo> groupElemInfoList, boolean bClean) {
		synchronized (mAppInfoListLock) {
			if (bClean) {
				mAppInfoList.clear();
				// bHasPromptedNoWifi = false; // 是否提示过没有wifi(对话框)
				// bHasPromptedContinueDownload = false; // 是否是否继续下载对话框
				// bOtherTaskContinueDownload = false; // 是否继续下载标志位
				// bHaveAddNullTaskToDownLoadList = false; // 已经把所有新任务加载到了下载队列
			}

			AppInfo appInfo = null;
			for (int i = 0; i < groupElemInfoList.size(); i++) {
				appInfo = Tools.createAppInfo(groupElemInfoList.get(i));
				mAppInfoList.add(appInfo);
			}
			//Log.d( TAG , "AddDownloadTasks2 GroupElemInfos mAppInfoList size = " + mAppInfoList.size( ) );
		}
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//
//		int[] location = new int[2];
//		getLocationInWindow(location);// 获取在当前窗口内的绝对坐标
//
//		Rect rect = new Rect();
//		rect.left = 0;
//		rect.top = 0;
//		rect.right = getRight() - getLeft();
//		rect.bottom = getBottom() - getTop();
//        /*
//         * Log.d(TAG, "event.X = " + event.getX() + " event.Y = " + event.getY()
//         * + " rect.left=" + rect.left + " rect.top = " + rect.top +
//         * " rect.right=" + rect.right + " rect.bottom = " + rect.bottom);
//         */
//		if (event.getAction() == MotionEvent.ACTION_UP
//				&& rect.contains((int) event.getX(), (int) event.getY())) {
//			mWifiDownLoadOnClickListener.onWifiClickListener(this);
//		}
//		return super.onTouchEvent(event);
//	}

	private void showContinueDialog(final DownloadTask dinfo) {
		bHasPromptedContinueDownload = true;
		NoWifiContinueDownloadDialog dialog = new NoWifiContinueDownloadDialog(mContext,
				R.style.MyDialog);
		dialog.setView(this);
		dialog.setNoWifiContinueDownloadListener(new NoWifiContinueDownloadListener() {

			@Override
			public void NoWifiContinueListener(View v) {
				mApkDownloadManager.resumeDownload(dinfo);
				bOtherTaskContinueDownload = true;// 其余在下载任务队列中暂停的继续下载
			}
		});
		dialog.show();
	}

	private void showInWifiDownLoadDialog() {
		bHasPromptedNoWifi = true;
		InWifiDownLoadDialog dialog = new InWifiDownLoadDialog(mContext, R.style.MyDialog);
		dialog.setView(this);
		dialog.setKnowBtnOnClickListener(mKnowBtnOnClickListener);
		dialog.show();

	}

//    private void showNoWifiDialog() {
        /*	Resources res = mContext.getResources( );
        	if( null == mNoWifiDialog ) {
        	    mNoWifiDialog = new CSAlertDialog( mContext , res.getString( R.string.tip_clean_cache ) , false );
        	}
        	mNoWifiDialog.setTitle( res.getString( R.string.clean_cache )null );
        	mNoWifiDialog.setmLeftBtnTitle( res.getString( R.string.do_setting ) );
        	mNoWifiDialog.setmRightBtnTitle( res.getString( R.string.do_kown ) );
        	mNoWifiDialog.addRightBtnListener( new OnClickListener( ) {
        	    @Override
        	    public void onClick( View v ) {
        		// TODO Auto-generated method stub

        		mNoWifiDialog.dismiss( );
        	    }

        	} );
        	mNoWifiDialog.setCanceledOnTouchOutside( true );

        	if( !mNoWifiDialog.isShowing( ) ) {
        	    mNoWifiDialog.show( );
        	}*/
//    }

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		// canvas.translate(0,canvas.getHeight( )/4 - (int)
		// this.getTextSize()/2);
		super.onDraw(canvas);
	}

	// 非wifi状态下点击 取消 才会调用此函数
	private final KnowBtnOnClickListener mKnowBtnOnClickListener = new KnowBtnOnClickListener() {

		@Override
		public void onKnowClickListener(View v) {
			Log.d(TAG, "BulkDownloadListButton KnowBtnOnClickListener");
			// 调用用户注册的onKnowClickListener
			kListener.onKnowClickListener(v);
			synchronized (mAppInfoListLock) {
				for (AppInfo appInfo : mAppInfoList) {
					DownloadTask task = mApkDownloadManager.getDownloadTaskByAppId(appInfo.appId);
					if (task == null) {
//                        addTaskToDownLoadList(appInfo);
						Log.d(TAG, "BulkDownloadListButton addTaskToDownLoadList");
						//###############oddshou 非 wifi状态下 继续下载
						mApkDownloadManager.startDownload(appInfo, ReportConstants.STAC_APP_POSITION_NEW_PERSON,
								mAppPosType);

					} else {
						// 加入过任务队列的不用再加入了

					}
				}
			}
			bHaveAddNullTaskToDownLoadList = true;
		}
	};

	// 当前是没有WiFi的情况下调用该函数
//    private void addTaskToDownLoadList(final AppInfo appInfo) {
//        mApkDownloadManager.startDownload(appInfo, StatisticManager.STAC_APP_POSITION_NEW_PERSON,
//                mAppPosType);
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    sleep(500); // 睡眠500ms是等待下载的状态发生改变
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                DownloadTask task = mApkDownloadManager.getDownloadTaskByAppId(appInfo.appId);
//                mApkDownloadManager.stopDownload(task);
//                // 非wifi情况下，系统将任务加入下载队列之后，自动暂停下载任务
//                mStatisticManager.reportDownloadStop(task.appId, task.packId, task.nFromPos,
//                        StatisticManager.STAC_DOWNLOAD_APK_OTHERS_STOP, App.getAppContext()
//                                .getResources()
//                                .getString(R.string.not_wifi_stop));
//                MtaUtils.trackDownloadStop(StatisticManager.STOP_REASON_OTHERS);
//            };
//        }.start();
//    }

	// 点击下载
	private final WifiDownLoadOnClickListener mWifiDownLoadOnClickListener = new WifiDownLoadOnClickListener() {

		@Override
		public void onWifiClickListener(View v) {

			// if( UITools.isFastDoubleClick( ) )
			// {
			// return;
			// }

			Log.d(TAG, "BulkDownloadListButton onWifiClickListener");
			// 调用用户的onWifiClickListener
			mListener.onWifiClickListener(v);

			boolean bWifiToDownload = App.getSettingContent().getSettingData().bWifiToDownload;
			Log.d(TAG, "no1 bWifiToDownload = " + bWifiToDownload);
			boolean bOnlyHasNet = false; // 有移动网络
			// 设置了wifi状态下下载时
			if (bWifiToDownload) {

				if (APNUtil.isMobileDataEnable(mContext) /*&& ( APNUtil.getApnType( mContext ) == APNUtil.APNTYPE_3GNET || APNUtil.getApnType( mContext ) == APNUtil.APNTYPE_3GWAP )*/) {
					// 此时跑的是移动网络流量
					bOnlyHasNet = true;
				}
			}
			Logger.i(TAG, "bWifiToDownload = " + bWifiToDownload + " bOnlyHasNet = " + bOnlyHasNet);
			synchronized (mAppInfoListLock) {
				for (int i = 0; mAppInfoList != null && i < mAppInfoList.size(); i++) {
					AppInfo appInfo = mAppInfoList.get(i);
					if (appInfo == null) {
						continue;
					}
					DownloadTask dinfo = mApkDownloadManager.getDownloadTaskByAppId(appInfo.appId);
					Logger.i(TAG, " bHasPromptedContinueDownload = " + bHasPromptedContinueDownload
							+ " bOtherTaskContinueDownload =" + bOtherTaskContinueDownload
							+ " bHasPromptedNoWifi = "
							+ bHasPromptedNoWifi + " bHaveAddNullTaskToDownLoadList = "
							+ bHaveAddNullTaskToDownLoadList + " dinfo = " + dinfo);
					if (dinfo != null) {

						TaskState state = dinfo.getState();
						Logger.i(TAG, "state = " + state);
                        /* 条件 bWifiToDownload && bOnlyHasNet && dinfo != null */
						if (bWifiToDownload && bOnlyHasNet) {
							// 设置只能在wifi状态下下载，但是当前只有移动网络 ，并且正在下载任务中已有该任务
							switch (state) {
								case SUCCEEDED:
								case STARTED:
								case LOADING:
									// 继续执行
									break;
								case STOPPED:
								case FAILED_SERVER:
									if (!bHasPromptedContinueDownload) {
										showContinueDialog(dinfo);
									} else {

										if (bOtherTaskContinueDownload) {// 其余在下载任务队列中暂停的直接继续下载
											mApkDownloadManager.resumeDownload(dinfo);
										} else {
											// 否则暂停的不下载，还放在任务队列中
										}
									}
									return;
								default:
									// 其他情况不继续执行
									return;
							}
						}

						switch (state) {
							case PREPARING:
							case WAITING:
							case STARTED:
							case LOADING:
								mApkDownloadManager.stopDownload(dinfo);
								// 用户主动暂停下载任务上报
								Logger.i("AppInfoActivity", "dinfo.packId=" + dinfo.packId);
								ReportConstants.reportDownloadStop(dinfo.appId, dinfo.packId,
										dinfo.nFromPos,
										ReportConstants.STAC_DOWNLOAD_APK_USER_ACTIVE_STOP, "");
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
								// 暂停下载任务继续下载上报
								ReportConstants.reportDownloadResume(dinfo.appId, dinfo.packId,
										dinfo.nFromPos,
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
								// 取消下载任务
								ReportConstants.reportDownloadStop(dinfo.appId, dinfo.packId,
										dinfo.nFromPos,
										ReportConstants.STAC_DOWNLOAD_APK_CANCEL_TASK, "");
								MtaUtils.trackDownloadCancel(dinfo.appName);
								break;
							default:
								break;
						}
					} else {

						if (bOnlyHasNet) {
							Log.d(TAG, "BulkDownloadListButton 5");
							// 只有移动网络
							if (bWifiToDownload) {// 设置只有在wifi下才能下载

								Log.d(TAG, "BulkDownloadListButton 6");
								if (!bHasPromptedNoWifi && !bHaveAddNullTaskToDownLoadList) {
									//Log.d( TAG , "BulkDownloadListButton 7" );
									showInWifiDownLoadDialog();
								} else if (bHasPromptedNoWifi && !bHaveAddNullTaskToDownLoadList) {
									//Log.d( TAG , "BulkDownloadListButton 8" );
									mKnowBtnOnClickListener.onKnowClickListener(v);
								}
							} else {
								//Log.d( TAG , "BulkDownloadListButton 9" );
								mApkDownloadManager.startDownload(appInfo,
										ReportConstants.STAC_APP_POSITION_NEW_PERSON, mAppPosType);
							}

						} else if (!APNUtil.isWifiDataEnable(mContext)) {
							Log.d(TAG, "BulkDownloadListButton 4");
							// 有wifi
							mApkDownloadManager.startDownload(appInfo,
									ReportConstants.STAC_APP_POSITION_NEW_PERSON,
									mAppPosType);
						} else {
							if (APNUtil.isWifiDataEnable(mContext)
									|| APNUtil.isMobileDataEnable(mContext)
									&& !bWifiToDownload) {
								Log.d(TAG, "BulkDownloadListButton 11");
								mApkDownloadManager.startDownload(appInfo,
										ReportConstants.STAC_APP_POSITION_NEW_PERSON, mAppPosType);
							} else {
								Log.d(TAG, "BulkDownloadListButton 12");
								// 没有网络
								mKnowBtnOnClickListener.onKnowClickListener(v);
							}
							if (!bHasPromptedNoWifi && !bHaveAddNullTaskToDownLoadList) {
								if (bWifiToDownload && bOnlyHasNet
										&& !APNUtil.isWifiDataEnable(mContext)) {
									showInWifiDownLoadDialog();
								}
								//showNoWifiDialog( );
								//Toast.makeText( mContext , mRes.getString( R.string.net_exception ) , 500 ).show( );
								//Log.d( TAG , "BulkDownloadListButton 1" );

							} else if (bHasPromptedNoWifi && !bHaveAddNullTaskToDownLoadList) {
								//Log.d( TAG , "BulkDownloadListButton 2" );
								//mKnowBtnOnClickListener.onKnowClickListener( v );

							} else {
								//Log.d( TAG , "BulkDownloadListButton 3" );
								//mKnowBtnOnClickListener.onKnowClickListener( v );
							}
						}// if (bOnlyHasNet)
					}// end of if (dinfo != null)

				}// end of for( int i = 0 ; mAppInfoList != null && i < mAppInfoList.size( ) ; i++ )
			}
		}
	};

	/**
	 * Called when a view has been clicked.
	 *
	 * @param v The view that was clicked.
	 */
	@Override
	public void onClick(View v) {
		mWifiDownLoadOnClickListener.onWifiClickListener(this);
	}
}
