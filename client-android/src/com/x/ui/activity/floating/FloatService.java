package com.x.ui.activity.floating;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.x.R;
import com.x.ui.activity.floating.RadialMenuView.onCloseView;
import com.x.ui.activity.floating.RadialMenuView.onMenuButtonClick;
import com.x.ui.activity.home.MainActivity;
import com.x.ui.view.floating.FloatPreferebce;
import com.x.ui.view.floating.FloatView;
import com.x.ui.view.floating.FloatPreferebce.MConstants;
import com.x.ui.view.floating.FloatView.onTouchViewListener;

/**
 * 
* @ClassName: FloatService
* @Description: 悬浮窗后台服务

* @date 2014-5-20 下午4:53:47
*
 */
public class FloatService extends Service {
	public static final int FLOATVIEW = 1;
	public static final int RADIALMENU = 2;
	public static final int NORADIALMENU = 3;
	//显示模式
	public static int showViewStatus = FLOATVIEW;
	private boolean isShow = false;
	private ListenHomeTask mListenTask = null;
	//检测home间隔时长
	private final long DELAYMILLIS = 500;
	//数据存储
	private FloatPreferebce	mfloatPreferebce = null;
	//初始悬浮窗方向：右边
	private int defaultGravity = MConstants.VERTICAL_RIGHT; 
	//悬浮球
	private FloatView floatView = null;
	//悬浮菜单
	private RadialMenuView mMenu = null;
	private WindowManager windowManager = null;
	private WindowManager.LayoutParams windowManagerParams = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mListenTask = new ListenHomeTask();
		initWindowsView();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (mListenTask != null) {
			mListenTask.start();
		}
		return START_STICKY;
	}
	
	/**
	 * 初始WindowManager
	 */
	private void initWindowsView(){
		mfloatPreferebce = FloatPreferebce.getInstance(getApplicationContext());
		windowManager = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		windowManagerParams = new WindowManager.LayoutParams();
		windowManagerParams.type = LayoutParams.TYPE_SYSTEM_ALERT; 
		windowManagerParams.format = PixelFormat.RGBA_8888;
		windowManagerParams.gravity = Gravity.LEFT | Gravity.TOP;//默认坐标 0,0
		int orientation = mfloatPreferebce.getOrientation();
		if(orientation == -1){
			mfloatPreferebce.setOrientation(defaultGravity);
		}
		float x = mfloatPreferebce.getFloatX();
		float y = mfloatPreferebce.getFloatY();
		if(x != 0 || y != 0){
			windowManagerParams.x = (int) x;
			windowManagerParams.y = (int) y;
		}else{
			WindowManager wm = (WindowManager) this
	                .getSystemService(Context.WINDOW_SERVICE);
			int width = wm.getDefaultDisplay().getWidth();
			int height = wm.getDefaultDisplay().getHeight();
			int view_h = getResources().getDrawable(R.drawable.desktop_btn).getIntrinsicHeight();
			// X位置
			int gx = defaultGravity == MConstants.VERTICAL_LEFT ? 0 : width;
			windowManagerParams.x = gx;
			// Y位置
			int gh = (height /2) - (view_h /2);
			windowManagerParams.y = gh;
			mfloatPreferebce.setFloatX(gx);
			mfloatPreferebce.setFloatY(gh);
		}
		windowManagerParams.width = LayoutParams.WRAP_CONTENT;
		windowManagerParams.height = LayoutParams.WRAP_CONTENT;
	}
	
	/**
	* @Title: addView 
	* @param @param view
	* @param @param isFocusable  是否需要聚焦
	* @throws
	 */
	public void addView(View view, boolean isFocusable){
		if(isFocusable){
			windowManagerParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL;
		}else{
			windowManagerParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
					| LayoutParams.FLAG_NOT_FOCUSABLE;
		}
		windowManager.addView(view, windowManagerParams);
	}
	
	public void removeView(View view){
		windowManager.removeView(view);
	}
	
	private void updateView(View view, int touchX, int touchY) {
		windowManagerParams.x = touchX;
		windowManagerParams.y = touchY;
		windowManager.updateViewLayout(view, windowManagerParams);
	}
	
	
	/***************添加悬浮球******************/
	private void addFloatView(){
		if(floatView == null){
			floatView = new FloatView(getApplicationContext());
			floatView.setOnClickListener(floatlistener);
			floatView.setOnTouchViewListener(floattouch);
			addView(floatView, false);
			showViewStatus = FLOATVIEW;
		}
	}
	
	private void removeFloatView(){
		if(floatView != null){
			removeView(floatView);
			floatView = null;
		}
	}
	
	private OnClickListener floatlistener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			removeFloatView();
			addRadialMenuView();
		}
	};
	
	private onTouchViewListener floattouch = new onTouchViewListener() {
		
		@Override
		public void onTouch(int x, int y) {
			if(floatView != null)
				updateView(floatView, x, y);
		}
	};
	/****************************************/
	
	
	
	/***************添加悬浮菜单******************/
	private void addRadialMenuView(){
		if(mMenu == null){
			RadialMenu menu = new RadialMenu(getApplicationContext());
			mMenu = menu.getView();
			mMenu.setOnCloseViewListener(closeView);
			mMenu.setonMenuButtonListener(buttonClick);
			addView(mMenu, true);
			showViewStatus = RADIALMENU;
		}
	}
	
	private void removeRadialMenuView(){
		if(mMenu != null){
			removeView(mMenu);
			mMenu = null;
		}
	}
	
	private onCloseView closeView = new onCloseView() {
		
		@Override
		public void closeView() {
			removeRadialMenuView();
			addFloatView();
		}
	};
	
	private onMenuButtonClick buttonClick = new onMenuButtonClick() {
		
		@Override
		public void onMenuButton() {
			long cooling = mfloatPreferebce.getCoolingTime();
			if(cooling != -1){
				long time = System.currentTimeMillis() - cooling;
				if(time < RadialMenu.COOLINGTIME){
					mfloatPreferebce.setCoolingTime(System.currentTimeMillis());
					return;
				}
			}
			mfloatPreferebce.setSearch(true);
			mfloatPreferebce.setCoolingTime(System.currentTimeMillis());
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
	};
	/****************************************/

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("Simple", "------------onDestroy-----------");
		mListenTask.stop();
		mListenTask = null;
		//销毁悬浮窗
//		if (isShow) {
//			switch (showViewStatus) {
//			case FLOATVIEW:
				removeFloatView();
//				break;
//			case RADIALMENU:
				removeRadialMenuView();
//				break;
//			}
//			isShow = false;
//		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0: //HOME
				if (!isShow) {
					switch (showViewStatus) {
					case RADIALMENU:
					case FLOATVIEW:
						addFloatView();
						break;
					case NORADIALMENU:
						addRadialMenuView();
						break;
					}
					isShow = true;
				}
				break;
			case 1: //NO_HOME
//				if (isShow) {
//					switch (showViewStatus) {
//					case FLOATVIEW:
						removeFloatView();
//						break;
//					case NORADIALMENU:
//					case RADIALMENU:
						removeRadialMenuView();
//						break;
//					}
//					showViewStatus = FLOATVIEW; //恢复默认
					isShow = false;
//				}
				break;
			}
		}
	};

	class ListenHomeTask {
		private List<String> homes = null;

		public ListenHomeTask() {
			homes = getHomes();
		}

		public void stop() {
			handler.removeCallbacks(runnable);
		}

		public void start() {
			handler.removeCallbacks(runnable);
			handler.post(runnable);
		}

		private Runnable runnable = new Runnable() {

			@Override
			public void run() {
				boolean isHome = isHome();
				if (isHome) {
					handler.sendEmptyMessage(0);
				} else {
					handler.sendEmptyMessage(1);
				}
				handler.postDelayed(runnable, DELAYMILLIS);
			}
		};

		private boolean isHome() {
			ActivityManager mActivityManager = (ActivityManager) getSystemService
					(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(Integer.MAX_VALUE);
			if(rti.size() > 0){
				if(homes.contains(rti.get(0).topActivity.getPackageName()) 
						|| homes.contains(rti.get(0).topActivity.toShortString()))
					return true;
			}
			return false;
		}

		private List<String> getHomes() {
			List<String> packages = new ArrayList<String>();
//			packages.add("{com.mas.amineappstore/" +
//				"com.mas.amineappstore.ui.activity.floating.RadialMenuActivity}"); //添加需要过滤的界面
			PackageManager packageManager = getPackageManager();
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
					PackageManager.MATCH_DEFAULT_ONLY);
			for (ResolveInfo info : resolveInfo) {
				packages.add(info.activityInfo.packageName);
			}
			return packages;
		}
	}
}
