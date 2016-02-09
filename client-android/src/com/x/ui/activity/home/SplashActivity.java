/**   
 * @Title: SplashActivity.java
 * @Package com.mas.amineappstore.activity
 * @Description: TODO 
 
 * @date 2014-1-15 下午03:04:35
 * @version V1.0   
 */

package com.x.ui.activity.home;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.x.R;
import com.x.business.account.InitPlatformManager;
import com.x.business.alarm.ApplockerAlarm;
import com.x.business.applocker.LockManager;
import com.x.business.applocker.LockService;
import com.x.business.country.CountryManager;
import com.x.business.statistic.DataEyeManager;
import com.x.db.LocalAppEntityManager;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.PlatFormInitRequest;
import com.x.publics.http.model.PlatFormInitResponse;
import com.x.publics.http.model.PlatFormInitRequest.PlatFormData;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.InstallAppBean;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.guide.GuideActivity;

/**
 * @ClassName: SplashActivity
 * @Description: TODO
 
 * @date 2014-1-15 下午03:04:35
 * 
 */

public class SplashActivity extends Activity {

	private Context context = this;
	private boolean initFinished, insertFinished, animFinished;
	AnimationDrawable animationDrawable;
	private int time;
	private LocalAppEntityManager localAppEntityManager;
	private boolean isFirst = true;
	private Drawable drawable;
	private ImageView imageView;
	private SplashHandler splashHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//防止按opne时候出现的页面显示混乱和退步出去的问题
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
			finish();
			return;
		}
		localAppEntityManager = LocalAppEntityManager.getInstance();
		splashHandler = new SplashHandler();
		showActivitySplash();
	}

	private void showActivitySplash() {
		setContentView(R.layout.activity_splash);
		imageView = (ImageView) findViewById(R.id.img_splash_logo);
		// 设置背景图片--大背景图片，不要在布局文件中设置，防止内存溢出, 在Activity
		// destory时注意，drawable.setCallback(null); 防止Activity得不到及时的释放
		// drawable = getResources().getDrawable(R.drawable.start);
		// imageView.setBackgroundDrawable(drawable);

		// 设置背景图片
		Utils.setBackgroundResource(imageView, R.drawable.start, context);
		startApplockerTask();
		getInstalledApp();
		InitPlatformManager initPlatformManager = new InitPlatformManager(this, splashHandler);
		initPlatformManager.initPlatForm();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				while (true) {
					SystemClock.sleep(200);
					if (insertFinished && initFinished)
						break;
				}
				enterActivity();
			}
		}, 2000);
	}

	/**
	 * applock组件
	* @Title: startApplockerTask 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	private void startApplockerTask() {
		LockManager.getInstance(this).initApplockerApp();
		LockManager.getInstance(this).startApplockerComponent();
	}

	class SplashHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case InitPlatformManager.HANDLER_WHAT_INIT_FINISHED:
				initFinished = true;
				break;

			default:
				break;
			}
		}

	}

	private void enterActivity() {
		isFirst = SharedPrefsUtil.getValue(SplashActivity.this, "isFirst2", true);
		if (isFirst == true)// 第一次进入应用，显示新手引导界面
		{
			Intent guideIntent = new Intent(SplashActivity.this, GuideActivity.class);
			startActivity(guideIntent);
			isFirst = false;
			SharedPrefsUtil.putValue(SplashActivity.this, "isFirst2", isFirst);
			SplashActivity.this.finish();
		} else {// 进入MainActivity
			launchMainActivity();
		}
	}

	private void getInstalledApp() {
		if (!SharedPrefsUtil.getValue(context, "initAllApp", false)) {
			Utils.executeAsyncTask(new AsyncTask<Void, Void, List<InstallAppBean>>() {

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					LogUtil.getLogger().w("load begin");
				}

				@Override
				protected List<InstallAppBean> doInBackground(Void... params) {
					localAppEntityManager.deleteAllApp();
					List<InstallAppBean> pList = new ArrayList<InstallAppBean>();
					PackageManager pm = getPackageManager();
					List<PackageInfo> pakList = pm.getInstalledPackages(0);
					LogUtil.getLogger().d(" (int) pakList.size = " + pakList.size());
					InstallAppBean bean = null;
					for (int i = 0; i < pakList.size(); i++) {
						PackageInfo pinfo = (PackageInfo) pakList.get(i);
						/** 判断是系统应用程序还不是用户应用程序 **/
						String appName = (String) pm.getApplicationLabel(pinfo.applicationInfo).toString();
						String packageName = pinfo.packageName.toString().trim();
						if (packageName != null && packageName.equals(context.getPackageName()))
							continue;
						String localtion = pinfo.applicationInfo.sourceDir;
						long appSize = Integer.valueOf((int) new File(localtion).length());
						String versionName = pinfo.versionName;
						int versionCode = pinfo.versionCode;

						boolean sysUpdate = (pinfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0;
						if (sysUpdate) {
							sysUpdate = !localtion.startsWith("/data/");
						}

						int sysFlag = sysUpdate ? 1 : 0;
						bean = new InstallAppBean(0, appName, packageName, versionName, versionCode, "" + appSize,
								localtion, 1, sysFlag, null);
						LogUtil.getLogger().d(" (InstallAppBean) bean = " + bean.toString());
						pList.add(bean);
					}
					for (int i = 0; i < pList.size(); i++) {
						localAppEntityManager.addApp(pList.get(i));
					}
					return pList;
				}

				@Override
				protected void onPostExecute(final List<InstallAppBean> result) {
					super.onPostExecute(result);
					LogUtil.getLogger().w("load finish");
					if (!result.isEmpty()) {
						SharedPrefsUtil.putValue(context, "initAllApp", true);
					}
					insertFinished = true;
				}
			});
		} else {
			insertFinished = true;
		}

	}

	private void launchMainActivity() {
		context.startActivity(new Intent(context, MainActivity.class));
		finish();
	}

	/**
	 * 屏蔽返回键
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		DataEyeManager.getInstance().onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		DataEyeManager.getInstance().onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// drawable.setCallback(null);
		Utils.recycleBackgroundResource(imageView);
	}
}
