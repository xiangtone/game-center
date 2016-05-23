package com.hykj.gamecenter.mta;

import android.content.Context;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.utils.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author lei
 *
 */
public class MtaUtils {
	
	static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
	
	public static void trackEvent(Context context, MtaEvent event) {
		Logger.i("MtaUtils", "trackEvent  " + event.getEventId());
//		try{
//		if (event.getProperties() == null) {
//			StatService.trackCustomEvent(context, event.getEventId());
//		} else {
//			StatService.trackCustomKVEvent(context, event.getEventId(),
//					event.getProperties());
//		}
//		}catch(Exception e){
//
//		}
	}

	public static void trackDownloadStop(String reason) {
		final String mReason = reason;
		fixedThreadPool.execute(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EventDownLoadStop eventStop = new EventDownLoadStop();
				eventStop.setReason(mReason);
				trackEvent(App.getAppContext(), eventStop);
			}
		});
		/*new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EventDownLoadStop eventStop = new EventDownLoadStop();
				eventStop.setReason(mReason);
				trackEvent(App.getAppContext(), eventStop);
			}
		}).start();*/
	}

	public static void trackDownloadCancel(String appName) {
		final String mAppName = appName;
		fixedThreadPool.execute(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EventDownLoadCanceled eventCancel = new EventDownLoadCanceled();
				eventCancel.setAppName(mAppName);
				trackEvent(App.getAppContext(), eventCancel);
			}
		} );
		/*new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EventDownLoadCanceled eventCancel = new EventDownLoadCanceled();
				eventCancel.setAppName(mAppName);
				trackEvent(App.getAppContext(), eventCancel);
			}
		}).start();*/
	}

	public static void trackDownloadResume() {
		fixedThreadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				trackEvent(App.getAppContext(), new EventDownLoadResume());
			}
		});
		/*new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				trackEvent(App.getAppContext(), new EventDownLoadResume());
			}
		}).start();*/
	}

	public static void trackAdvClick(boolean isApp, String position) {
		final String mPosition = position; 
		final boolean mIsApp = isApp;
		// TODO Auto-generated method stub
		fixedThreadPool.execute(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mIsApp) {
					EventAdvClickRecommend eventAdvClickRecommend = new EventAdvClickRecommend();
					eventAdvClickRecommend.setAdvPosition(mPosition);
					trackEvent(App.getAppContext(), eventAdvClickRecommend);
				} else {
					EventAdvClickGame eventAdvClickGame = new EventAdvClickGame();
					eventAdvClickGame.setAdvPosition(mPosition);
					trackEvent(App.getAppContext(), eventAdvClickGame);
				}
			}
		});
		/*new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mIsApp) {
					EventAdvClickRecommend eventAdvClickRecommend = new EventAdvClickRecommend();
					eventAdvClickRecommend.setAdvPosition(mPosition);
					trackEvent(App.getAppContext(), eventAdvClickRecommend);
				} else {
					EventAdvClickGame eventAdvClickGame = new EventAdvClickGame();
					eventAdvClickGame.setAdvPosition(mPosition);
					trackEvent(App.getAppContext(), eventAdvClickGame);
				}
			}
		}).start();*/
	}

	public static void trackDetails(String position) {
		final String mPosition = position;
		fixedThreadPool.execute(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EventDetailsFrom detailsFrom = new EventDetailsFrom();
				detailsFrom.setFromPage(mPosition);
				trackEvent(App.getAppContext(), detailsFrom);
			}
		});
		/*new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EventDetailsFrom detailsFrom = new EventDetailsFrom();
				detailsFrom.setFromPage(mPosition);
				trackEvent(App.getAppContext(), detailsFrom);
			}
		}).start();*/
	}

	public static void trackSpeed(String mid, String appName, String speed) {
		final String mMid = mid;
		final String mAppName = appName;
		final String mSpeed = speed;
		fixedThreadPool.execute(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EventReportSpeed speedReport = new EventReportSpeed();
				speedReport.setAppName(mAppName);
				speedReport.setSpeed(mSpeed);
				speedReport.setMID(mMid);
				trackEvent(App.getAppContext(), speedReport);
			}
		});
		/*new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EventReportSpeed speedReport = new EventReportSpeed();
				speedReport.setAppName(mAppName);
				speedReport.setSpeed(mSpeed);
				speedReport.setMID(mMid);
				trackEvent(App.getAppContext(), speedReport);
			}
		}).start();*/
	}
	
	public static void trackReturn(){
		fixedThreadPool.execute(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				trackEvent(App.getAppContext(), new EventReturn());
			}
		});
		/*new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				trackEvent(App.getAppContext(), new EventReturn());
			}
		}).start();;*/
	}
	
	public static void trackUpgradeRequest() {
		fixedThreadPool.execute(new Runnable(){
			@Override
			public void run() {
				trackEvent(App.getAppContext(), new EventUpgradeRequest());
			}});
		/*new Thread(new Runnable(){
			@Override
			public void run() {
				trackEvent(App.getAppContext(), new EventUpgradeRequest());
			}}).start();*/
	}
	
	public static void trackDownloadFailed(String reason) {
		final String mReason = reason;
		fixedThreadPool.execute(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EventDownLoadFailed eventFailed = new EventDownLoadFailed();
				eventFailed.setReason(mReason);
				trackEvent(App.getAppContext(), eventFailed);
			}
		});
		/*new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EventDownLoadFailed eventFailed = new EventDownLoadFailed();
				eventFailed.setReason(mReason);
				trackEvent(App.getAppContext(), eventFailed);
			}
		}).start();*/
	}
	
	public static void trackDownloadSuccess(String appName) {
		final String mAppName = appName;
		fixedThreadPool.execute(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EventDownLoadSuccess eventSuccess = new EventDownLoadSuccess();
				eventSuccess.setAppName(mAppName);
				trackEvent(App.getAppContext(), eventSuccess);
			}
		});
		/*new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EventDownLoadSuccess eventSuccess = new EventDownLoadSuccess();
				eventSuccess.setAppName(mAppName);
				trackEvent(App.getAppContext(), eventSuccess);
			}
		}).start();*/
	}
	
	public static void trackDownloadRequest(String page) {
		final String mPage = page;
		fixedThreadPool.execute(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EventDownLoadRequest eventDownloadRequest = new EventDownLoadRequest();
				eventDownloadRequest.setFromPage(mPage);
				trackEvent(App.getAppContext(), eventDownloadRequest);
			}
		});
		/*new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EventDownLoadRequest eventDownloadRequest = new EventDownLoadRequest();
				eventDownloadRequest.setFromPage(mPage);
				trackEvent(App.getAppContext(), eventDownloadRequest);
			}
		}).start();*/
	}
	
	public static void trackUpdateDownloaded(String versionName, String versionCode){
		final String mVersion = versionName;
		final String mVersionCode = versionCode;
		fixedThreadPool.execute(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EventUpdateDownloaded eventDownloaded = new EventUpdateDownloaded();
				eventDownloaded.setVersionName(mVersion);
				eventDownloaded.setVersionCode(mVersionCode);
				trackEvent(App.getAppContext(), eventDownloaded);
			}
		});
		/*new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EventUpdateDownloaded eventDownloaded = new EventUpdateDownloaded();
				eventDownloaded.setVersionName(mVersion);
				eventDownloaded.setVersionCode(mVersionCode);
				trackEvent(App.getAppContext(), eventDownloaded);
			}
		}).start();;*/
	}
	
	public static void trackUpdateRequest(String versionName,String versionCode){
		final String mVersion = versionName;
		final String mVersionCode = versionCode;
		fixedThreadPool.execute(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EventUpdateRequest eventUpdateRequest = new EventUpdateRequest();
				eventUpdateRequest.setVersionName(mVersion);
				eventUpdateRequest.setVersionCode(mVersionCode);
				trackEvent(App.getAppContext(), eventUpdateRequest);
			}
		});
		/*new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EventUpdateRequest eventUpdateRequest = new EventUpdateRequest();
				eventUpdateRequest.setVersionName(mVersion);
				eventUpdateRequest.setVersionCode(mVersionCode);
				trackEvent(App.getAppContext(), eventUpdateRequest);
			}
		}).start();;*/
	}
	
	public static void trackUpdateInstalled(String versionName,String versionCode){
		final String mVersion = versionName;
		final String mVersionCode = versionCode;
		fixedThreadPool.execute(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EventUpdateInstalled eventUpdateInstalled = new EventUpdateInstalled();
				eventUpdateInstalled.setVersionName(mVersion);
				eventUpdateInstalled.setVersionCode(mVersionCode);
				trackEvent(App.getAppContext(), eventUpdateInstalled);
			}
		});
		/*new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EventUpdateInstalled eventUpdateInstalled = new EventUpdateInstalled();
				eventUpdateInstalled.setVersionName(mVersion);
				eventUpdateInstalled.setVersionCode(mVersionCode);
				trackEvent(App.getAppContext(), eventUpdateInstalled);
			}
		}).start();;*/
	}
}
