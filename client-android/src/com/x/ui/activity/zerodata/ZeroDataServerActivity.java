/**   
* @Title: ZeroDataServerActivity.java
* @Package com.x.activity
* @Description: TODO 

* @date 2014-1-14 下午02:56:34
* @version V1.0   
*/

package com.x.ui.activity.zerodata;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.x.business.audio.AudioEffectManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.business.zerodata.connection.helper.WifiState;
import com.x.business.zerodata.connection.helper.ZeroDataConnectHelper;
import com.x.business.zerodata.connection.listener.CreatHotspotListener;
import com.x.business.zerodata.connection.manager.ConnectHotspotManage;
import com.x.business.zerodata.connection.manager.CreatHotspotManager;
import com.x.business.zerodata.helper.ServiceHelper;
import com.x.business.zerodata.helper.ZeroDataConstant;
import com.x.business.zerodata.helper.ZeroDataResourceHelper;
import com.x.db.resource.NativeResourceConstant;
import com.x.publics.download.BroadcastManager;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.activity.resource.ResourceManagementActivity;
import com.x.ui.view.TabPageIndicator;

/**
* @Description: 零流量分享里的搜索好友分享及扫描二维码
*/

public class ZeroDataServerActivity extends BaseActivity implements OnClickListener {

	private Context context;
	private List<String> titleList = new ArrayList<String>();
	//private int[] titlePics = new int[] { R.drawable.indicator_search_bg, R.drawable.indicator_scanner_bg };
	private String searchTips, scannerTips;
	private ViewPager mViewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private TabPageIndicator indicator;
	private HotspotReceiver hotspotReceiver;
	private boolean isOnclick = true;

	Dialog hotspotDialog;

	private boolean isOpenNoApDialog = false;

	CreatHotspotListener creatHotspotListener = null;

	ServiceHelper serviceHelper;

	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		connectService();
		context = this;
		setTabTitle(R.string.page_share_title);
		setContentView(R.layout.activity_scan_wifiap);
		initViewPager();
		initNavigation();
		creatHotspotListener = new CreatHotspotListener(this, serverHandler);
		registerHotspotReceiver();
	}

	/**
	* @Title: initNavigation 
	* @Description: 初始化导航栏 
	* @param     
	* @return void
	 */
	private void initNavigation() {
		mTitleView = findViewById(R.id.rl_title_bar);
		mTitlePendant = findViewById(R.id.title_pendant);
		mNavigationView = findViewById(R.id.mh_navigate_ll);
		mGobackIv = (ImageView) findViewById(R.id.mh_slidingpane_iv);
		mTitleTv = (TextView) findViewById(R.id.mh_navigate_title_tv);
		mGobackIv.setBackgroundResource(R.drawable.ic_back);
		mTitleTv.setText(R.string.page_share_title);
		mNavigationView.setOnClickListener(this);
	}

	private void initViewPager() {
		searchTips = (String) getResources().getText(R.string.sharing_search_friends);
		scannerTips = (String) getResources().getText(R.string.sharing_qr_code);
		titleList.clear();
		titleList.add(searchTips);
		titleList.add(scannerTips);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), titleList);

		mViewPager = (ViewPager) findViewById(R.id.scan_hot_spot_pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
			}
		});

		indicator = (TabPageIndicator) findViewById(R.id.scan_hot_spot_indicator);
		indicator.setViewPager(mViewPager);
	}

	private class SectionsPagerAdapter extends FragmentPagerAdapter {

		List<String> titles;

		public SectionsPagerAdapter(FragmentManager fm, List<String> titles) {
			super(fm);
			this.titles = titles;
		}

		private void setTitles(List<String> titles) {
			this.titles = titles;
			indicator.notifyDataSetChanged();
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = ZeroDataAutoScanFragment.newInstance(null);
				break;
			case 1:

				fragment = ZeroDataQrScanFragment.newInstance(null);
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return titles.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles.get(position);
		}

		@Override
		public int getItemPosition(Object object) {
			return PagerAdapter.POSITION_NONE;
		}

		/*	@Override 
			public int getIconResId(int index) {
				// TODO Auto-generated method stub
				return titlePics[index];
			}*/
	}

	WakeLock wakeLock;

	private void acquireWakeLock() {
		if (wakeLock == null) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass().getCanonicalName());
			wakeLock.acquire();
		}
	}

	private void releaseWakeLock() {
		if (wakeLock != null && wakeLock.isHeld()) {
			wakeLock.release();
			wakeLock = null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		acquireWakeLock();
		ConnectHotspotManage.getInstance(this).setMobileDataEnabled(false);
		CreatHotspotManager.getInstance(this).reCreateOpenHotspot();
		creatHotspotListener.start();
		DataEyeManager.getInstance().module(ModuleName.SHARE_FREE_SEND, true);
	}

	@Override
	protected void onStop() {
		//此处增加super。onStop方法，让子类调用到父类的onStop（）方法，否则会抛出SuperNotCalledException异常。
		super.onStop();
		AudioEffectManager.getInstance().releaseMusicPlayer();
		onServerStop();
	}

	@Override
	public void onPause() {
		super.onPause();
		DataEyeManager.getInstance().module(ModuleName.SHARE_FREE_SEND, false);
	}

	/**
	 * 
	* @Title: onServerStop 
	* @Description: TODO(处理焦点遗失) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void onServerStop() {
		releaseWakeLock();
		creatHotspotListener.stop();
		CreatHotspotManager.getInstance(this).closeOpenHotspot();
		ConnectHotspotManage.getInstance(this).resumeServerNetwork();
		sendDisconnectBroadcast(ZeroDataConstant.ACTION_ACTIVITY_CLEAR_CLIENT_DEVICE_HANDLER);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		uninit();
	}

	/**
	 * 连接服务
	 */
	private void connectService() {
		serviceHelper = new ServiceHelper(this);
		serviceHelper.connect(new Runnable() {
			@Override
			public void run() {
			}
		});
	}

	/**
	 * handler
	 */
	public Handler serverHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			//创建热点失败
			case ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_DISABLED:
			case ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_FAILED:
			case ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_UNKNOWN:
				if (ConnectHotspotManage.getInstance(context).getWifiState() != WifiState.WIFI_STATE_DISABLED) {
					ConnectHotspotManage.getInstance(context).closeWifi();
				} else if (ConnectHotspotManage.getInstance(context).getWifiState() == WifiState.WIFI_STATE_DISABLED) {
					CreatHotspotManager.getInstance(context).reCreateOpenHotspot();
				}
				//清空当前连接数据
				sendDisconnectBroadcast(ZeroDataConstant.ACTION_ACTIVITY_CLEAR_CLIENT_DEVICE_HANDLER);
				break;
			case ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_ENABLED:
				startServer();
				break;

			default:
				break;
			}
		}

	};

	/**
	 * 启动服务
	 */
	private void startServer() {
		try {

			serviceHelper.startServer();

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重启服务
	 */
	public void reStartServer() {

		if (!isServiceRun(this)) {
			startServer();
		}

	}

	/**
	 * 处理创建热点失败
	 */
	public void handleCreatHotsopt() {
		if (!isOpenNoApDialog) {
			isOpenNoApDialog = true;
			Utils.showDialog(context, ResourceUtil.getString(context, R.string.warm_tips),
					ResourceUtil.getString(context, R.string.dialog_hotspot_disabled_tips),
					ResourceUtil.getString(context, R.string.confirm), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							CreatHotspotManager.getInstance(ZeroDataServerActivity.this).reCreateHotspot();
							isOpenNoApDialog = false;
							dialog.dismiss();
						};
					}, ResourceUtil.getString(context, R.string.dialog_zerodata_exit),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							startActivity(new Intent(context, ZeroDataShareActivity.class));
							ZeroDataServerActivity.this.finish();
						}
					});
		}
	}

	@Override
	public void onBackPressed() {
		cancelDataShare();
	}

	/**
	 * 取消分享
	 */
	public void cancelDataShare() {

		Utils.showDialog(context, ResourceUtil.getString(context, R.string.warm_tips),
				ResourceUtil.getString(context, R.string.dialog_cancle_zerodata_share),
				ResourceUtil.getString(context, R.string.confirm), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						isOnclick = true;
						creatHotspotListener.stop();

						//						CreatHotspotManager.getInstance(ZeroDataServerActivity.this).closeZeroDataHotspot();
						String fromActivity = ZeroDataResourceHelper.getFromActivity(ZeroDataServerActivity.this,
								ZeroDataConstant.ZERO_DATA_SERVER_ACTIVITY_KEY);
						if (ZeroDataTransferHistoryActivity.class.getName().equals(fromActivity)) {
							ZeroDataResourceHelper.saveFromActivity(ZeroDataServerActivity.this,
									ZeroDataConstant.ZERO_DATA_SERVER_ACTIVITY_KEY, "");
							// 跳转接受记录
							startActivity(new Intent(context, ZeroDataTransferHistoryActivity.class));
						} else if (ZeroDataShareActivity.class.getName().equals(fromActivity)) {
							ZeroDataResourceHelper.saveFromActivity(ZeroDataServerActivity.this,
									ZeroDataConstant.ZERO_DATA_SERVER_ACTIVITY_KEY, "");
							// 跳转资源管理
							Intent resourceintent = new Intent(context, ResourceManagementActivity.class);
							resourceintent.putExtra("MODE", NativeResourceConstant.SHARE_MODE);
							startActivity(resourceintent);
						} /*else {
							ConnectHotspotManage.getInstance(ZeroDataServerActivity.this).resumeServerNetwork();
							}*/

						//						uninit();//停止服务，卸载资源
						dialog.dismiss();
						ZeroDataServerActivity.this.finish();
					};
				}, ResourceUtil.getString(context, R.string.cancel), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						isOnclick = true;
					}
				});
	}

	/**
	 * 注册监听
	 */
	private void registerHotspotReceiver() {

		hotspotReceiver = new HotspotReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ZeroDataConstant.WIFI_AP_STATE_CHANGED_ACTION);
		BroadcastManager.registerReceiver(hotspotReceiver, filter);
	}

	/**
	 * 监听热点
	 
	 *
	 */
	public class HotspotReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null && ZeroDataConstant.WIFI_AP_STATE_CHANGED_ACTION.equals(intent.getAction())) {
				int hotspotState = intent.getIntExtra("wifi_state", 0);
				//如果热点关闭
				if (WifiState.WIFI_AP_STATE_DISABLED == hotspotState) {
					Message msg = serverHandler.obtainMessage(ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_DISABLED);
					serverHandler.sendMessage(msg);
				}
			}
		}
	}

	/**
	 * 服务是否启动服务
	 * @param context
	 * @return
	 */
	public boolean isServiceRun(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> list = am.getRunningServices(100);
		for (RunningServiceInfo info : list) {
			if (info.service.getClassName().equals(
					"com.x.business.zerodata.server.service.ServerService")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	* @Title: uninit 
	* @Description: TODO(卸载资源) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void uninit() {

		try {
			BroadcastManager.unregisterReceiver(hotspotReceiver);
			serviceHelper.stopServer();
			serviceHelper.disconnect();
			creatHotspotListener.stop();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			if (isOnclick) {
				isOnclick = false;
				onBackPressed();
			} else {
				return false;
			}
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * 
	* @Title: sendDisconnectBroadcast 
	* @Description: TODO(清空连接) 
	* @param @param action    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void sendDisconnectBroadcast(String action) {
		Intent intent = new Intent(action);
		BroadcastManager.sendBroadcast(intent);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.mh_navigate_ll:
			onBackPressed();
			break;
		default:
			break;
		}
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(context, mTitleView, mNavigationView, mTitlePendant, null);
	}
}
