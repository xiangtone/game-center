package com.hykj.gamecenter.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.GlobalConfigControllerManager;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.fragment.BaseFragment;
import com.hykj.gamecenter.logic.entry.Msg;
import com.hykj.gamecenter.net.APNUtil;
import com.hykj.gamecenter.ui.widget.CSToast;

public class UITools {

	private static UITools instance;
	public static final int SCREEN_LANDSCAPE = 0;
	public static final int SCREEN_PORTRAIT_NARROW = 1;
	public static final int SCREEN_PORTRAIT_LOOSE = 2;
	private static boolean hasHomePageDestroyed = false;
	public static final String TAG = "UITools";
	private final static int PORTRAIT_NARROW_COLUMN = 1;
	private final static int PORTRAIT_LOOSE_COLUMN = 1;
	private final static int LANDSCAPE_COLUMN = 2;
	private final static int NOVICE_GUIDANCE_PORTRAIT_NARROW_COLUMN = 3;
	private final static int NOVICE_GUIDANCE_PORTRAIT_LOOSE_COLUMN = 3;
	private final static int NOVICE_GUIDANCE_LANDSCAPE_COLUMN = 6;
	public final static int NOVICE_GUIDANCE_ROW_LANDSCAPE = 1;
	public final static int NOVICE_GUIDANCE_ROW_PORTRAIT = 2;



	private UITools() {
	}

	public static synchronized UITools getInstance() {
		if (instance == null) {
			instance = new UITools();
		}
		return instance;
	}

	private static long lastClickTime = 0;

	public static synchronized boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 500) { // 500ms内不能同事起效
			return true;
		}
		lastClickTime = time;
		return false;
	}

	public boolean isNetworkAvailable(Context context) {
		if (APNUtil.isNetworkAvailable(context) == false) {
			CSToast.show(context,
					context.getString(R.string.error_msg_net_fail));
			return false;
		}
		return true;
	}

	/**判断是否竖屏*/
	public static boolean isPortrait() {
		DisplayMetrics dm = App.getAppContext().getResources()
				.getDisplayMetrics();
//		Logger.i(TAG, "dm.widthPixels == " + dm.widthPixels, "tom");//1280
//		Logger.i(TAG, "dm.heightPixels == " + dm.heightPixels, "tom");//756
		return dm.widthPixels < dm.heightPixels;
	}

	public static int getWindowsWidthPixel() {
		DisplayMetrics dm = App.getAppContext().getResources()
				.getDisplayMetrics();
		return dm.widthPixels;
	}

	public static int getWindowsHeightPixel() {
		DisplayMetrics dm = App.getAppContext().getResources()
				.getDisplayMetrics();
		return dm.heightPixels;
	}

	public static float getWindowsDensity() {
		DisplayMetrics dm = App.getAppContext().getResources()
				.getDisplayMetrics();
		return dm.density;
	}

	/**
	 * 获取顶部状态栏高度
	 *
	 * @param activity
	 * @return
	 */
	public static int getSystemTitleBarHeight(Activity activity) {
		Rect frame = new Rect();
		if (activity != null) {
			activity.getWindow().getDecorView()
					.getWindowVisibleDisplayFrame(frame);
		}
		int statusBarHeight = frame.top;
		return statusBarHeight;
	}

	public static int dpTopx(Context context, int dpValue) {
		DisplayMetrics dm = context.getApplicationContext().getResources()
				.getDisplayMetrics();

		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpValue, dm);
	}

	public static void setViewHeight(View view, int height) {
		LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) view
				.getLayoutParams();
		view.getLayoutParams();
		lParams.height = height;
		view.setLayoutParams(lParams);
	}

	public static int screenWidthToDip(Context context) {
		float pxValue = context.getResources().getDisplayMetrics().widthPixels;
		float scale = context.getResources().getDisplayMetrics().density;
		Logger.i(TAG, "dm.density == " + scale, "tom");
		Logger.i(TAG, "screenWidthToDip == " + (int) (pxValue / scale + 0.5f), "tom");
		return (int) (pxValue / scale + 0.5f);
	}

	public static int getScreenStatus(Context context) {
		if (!isPortrait()) {
			return SCREEN_LANDSCAPE;
		} else {
			return (screenWidthToDip(context) / 240 >= 3) ? SCREEN_PORTRAIT_LOOSE
					: SCREEN_PORTRAIT_NARROW;
		}
	}

	public static int getColumnNumber(Context context) {
		switch (UITools.getScreenStatus(context)) {
			case UITools.SCREEN_LANDSCAPE:
				return LANDSCAPE_COLUMN;
			case UITools.SCREEN_PORTRAIT_LOOSE:
				return PORTRAIT_LOOSE_COLUMN;
			case UITools.SCREEN_PORTRAIT_NARROW:
				return PORTRAIT_NARROW_COLUMN;
			default:
				return LANDSCAPE_COLUMN;
		}
	}

	public static int getNoviceGuidanceColumnNumber(Context context) {
		switch (UITools.getScreenStatus(context)) {
			case UITools.SCREEN_LANDSCAPE:
				return NOVICE_GUIDANCE_LANDSCAPE_COLUMN;
			case UITools.SCREEN_PORTRAIT_LOOSE:
				return NOVICE_GUIDANCE_PORTRAIT_LOOSE_COLUMN;
			case UITools.SCREEN_PORTRAIT_NARROW:
				return NOVICE_GUIDANCE_PORTRAIT_NARROW_COLUMN;
			default:
				return NOVICE_GUIDANCE_LANDSCAPE_COLUMN;
		}
	}

	public static int getNoviceGuidanceSize(Context context) {
		switch (UITools.getScreenStatus(context)) {
			case UITools.SCREEN_LANDSCAPE:
				return NOVICE_GUIDANCE_LANDSCAPE_COLUMN * NOVICE_GUIDANCE_ROW_LANDSCAPE;
			case UITools.SCREEN_PORTRAIT_LOOSE:
				return NOVICE_GUIDANCE_PORTRAIT_LOOSE_COLUMN * NOVICE_GUIDANCE_ROW_PORTRAIT;
			case UITools.SCREEN_PORTRAIT_NARROW:
				return NOVICE_GUIDANCE_PORTRAIT_NARROW_COLUMN * NOVICE_GUIDANCE_ROW_PORTRAIT;
			default:
				return NOVICE_GUIDANCE_LANDSCAPE_COLUMN * NOVICE_GUIDANCE_ROW_LANDSCAPE;
		}
	}


	public static synchronized void notifyStateChange(
			final BaseFragment fragment) {
		final Handler handler = fragment.getHandler();
		if (handler == null) {
			return;
		}
		Message msg = handler.obtainMessage();
		if (msg == null) {
			return;
		}
		int isGlobal = GlobalConfigControllerManager.getInstance()
				.getLoadingState();
		Logger.i(TAG, "isGlobal" + isGlobal, "oddshou");
		Logger.i(TAG, "fragment-->" + fragment, "oddshou");
		switch (isGlobal) {
			case GlobalConfigControllerManager.NORMAL_STATE:
				if (!fragment.hasLoadedData()) {
					appendData(fragment);
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

	//################# oddshou 修改，因为这个方法会出现错误 msg.net_error
	public static void checkLoadingState(BaseFragment mFragment) {
//		if (mFragment == null) {
//			Log.e(TAG, "checkLoadingState fragment is null");
//			return;
//		}
//		Logger.e(TAG, "checkLoadingState fragment " + mFragment, "oddshou");
//		final BaseFragment fragment = mFragment;
//		new Thread() {
//			@Override
//			public void run() {
//				try {
//					/* Log.e(TAG, "check " + fragment); */
//					Thread.sleep(90 * 1000);
//					if (GlobalConfigControllerManager.getInstance()
//							.getLoadingState() == GlobalConfigControllerManager.LOADING_STATE
//							|| fragment.isLoading()) {
//						Log.e(TAG, "isLoading " + fragment);
//						Message msg = fragment.getHandler().obtainMessage();
//						msg.what = Msg.NET_ERROR;
//						msg.sendToTarget();
//					}
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			};
//		}.start();
	}

	private static void appendData(BaseFragment fragment) {
		fragment.setHasLoadedData(true);
		// fragment.initFragmentListData( );
//		if (fragment.getActivitySelectedItem() != 0) {
			fragment.getHandler().sendEmptyMessageDelayed(Msg.APPEND_DATA, 500);
//		} else {
//			fragment.getHandler().sendEmptyMessage(Msg.APPEND_DATA);
//		}
	}
}
