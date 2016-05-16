package com.hykj.gamecenter.mta;


/**
 * @author lei
 * 由于首次使用MTA中MidService.getMid()方法时获取的值为
 * 0，因此采用回调方法来获取，详见 《MID接入指南（Android）》
 */
public class ReportSpeedThread extends Thread{
	public static final String TAG = "ReportSpeedThread";
	String appName;
	String speed;
	
	public ReportSpeedThread(String appName, String speed) {
		super();
		this.appName = appName;
		this.speed = speed;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
//		MidService.requestMid(App.getAppContext(), new MidCallback() {
//			@Override
//			public void onSuccess(Object mid) {
//				// TODO Auto-generated method stub
//				Logger.i(TAG, "requestMid success! " + mid);
//				MtaUtils.trackSpeed((String)mid, appName, speed);
//			}
//
//			@Override
//			public void onFail(int errorCode, String errorMSG) {
//				// TODO Auto-generated method stub
//				Logger.i(TAG, "requestMid fail:");
//				Logger.i(TAG, "errorCode	  " + errorCode);
//				Logger.i(TAG, "errorMSG	  " + errorMSG);
//			}
//		});
	}
}
