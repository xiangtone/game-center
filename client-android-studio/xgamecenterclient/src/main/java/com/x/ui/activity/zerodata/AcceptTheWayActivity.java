package com.x.ui.activity.zerodata;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.zerodata.client.http.TransferHost;
import com.x.business.zerodata.connection.helper.WifiState;
import com.x.business.zerodata.connection.helper.ZeroDataConnectHelper;
import com.x.business.zerodata.connection.listener.CreatHotspotListener;
import com.x.business.zerodata.connection.manager.ConnectHotspotManage;
import com.x.business.zerodata.connection.manager.CreatHotspotManager;
import com.x.business.zerodata.helper.ServiceHelper;
import com.x.business.zerodata.helper.ZeroDataConstant;
import com.x.business.zerodata.helper.ZeroDataResourceHelper;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;

/**
 * 接受方式页面
 * 
 
 * 
 */
public class AcceptTheWayActivity extends BaseActivity implements OnClickListener {

	private ImageView scannerIv;
	private RelativeLayout accept_way_rel;
	private LinearLayout accept_way_ll;
	private TextView inviteTv, downloadTv, zApp_wifi;
	CreatHotspotListener creatHotspotListener;
	ServiceHelper serviceHelper;
	String SSID = null;
	String serverUrl = null;

	private Context context = this;
	public boolean isServiceStart = false;
	public boolean isConnectingView = false;

	private TextView connecting;
	private boolean isInit = false;
	private int count = 0;
	private Timer timer;

	String serverIp = "192.168.43.1";

	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTabTitle(R.string.page_share_title);
		setContentView(R.layout.activity_accept_way);
		initTransitionPage();
		creatHotspotListener = new CreatHotspotListener(this, mHandler);
		connectService();
		initNavigation();
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

	/**
	 * 初始化过渡页
	 */
	private void initTransitionPage() {
		isConnectingView = true;
		accept_way_rel = (RelativeLayout) findViewById(R.id.accept_way_rel);
		connecting = (TextView) findViewById(R.id.tv_connecting);

		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (isInit) {
					count++;
					mHandler.sendEmptyMessage(10);
				} else {
					isInit = true;
				}
			}
		}, 0, 500);

	}

	private void switchUI(String SSID, String serverUrl) {
		timer.cancel();
		isConnectingView = false;

		accept_way_ll = (LinearLayout) findViewById(R.id.accept_way_ll);
		accept_way_ll.setVisibility(View.VISIBLE);
		// creatingIv.clearAnimation();
		accept_way_rel.setVisibility(View.GONE);
		scannerIv = (ImageView) findViewById(R.id.iu_scanner_iv);
		inviteTv = (TextView) findViewById(R.id.atw_invite_connect_tv);
		downloadTv = (TextView) findViewById(R.id.atw_InstallationNotes_tv);
		zApp_wifi = (TextView) findViewById(R.id.atw_zapp_wifi);

		if (SSID == null || serverUrl == null)
			return;

		// 根据信息，生成二维码
		Utils.setQRCodeBackground(scannerIv, R.drawable.mas_ic_launcher, serverUrl, context);

		colorOfTv(SSID, serverUrl);
	}

	/**
	 * 为SSID、Url文本设置颜色
	 */
	private void colorOfTv(String SSID, String serverUrl) {

		int ssidLength = SSID.length();
		int urlLength = serverUrl.length();
		String strSsid = this.getResources().getString(R.string.atw_invite_connect_zapp);
		String strUrl = this.getResources().getString(R.string.atw_Installation_notes);
		SpannableStringBuilder inviteBuilder = new SpannableStringBuilder(strSsid + " [" + SSID + "]");

		// set text color
		int textColor = SkinConfigManager.getInstance().getStringColor(context, SkinConstan.APP_THEME_COLOR);

		ForegroundColorSpan span = new ForegroundColorSpan(textColor);
		inviteBuilder.setSpan(span, strSsid.length() + 2, inviteBuilder.length() - 1,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		inviteTv.setText(inviteBuilder);
		zApp_wifi.setText(SSID);

		SpannableStringBuilder downloadBuilder = new SpannableStringBuilder(strUrl + " \"" + serverUrl + "\".");
		downloadBuilder.setSpan(span, strUrl.length() + 2, downloadBuilder.length() - 2,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		downloadTv.setText(downloadBuilder);
	}

	/**
	 * handler
	 */
	public Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_ENABLED:
				startServer();
				hanldeStartSwitchView();
				break;
			// 创建热点失败
			case ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_DISABLED:
			case ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_FAILED:
				if (ConnectHotspotManage.getInstance(context).getWifiState() != WifiState.WIFI_STATE_DISABLED) {
					ConnectHotspotManage.getInstance(context).closeWifi();
				} else if (ConnectHotspotManage.getInstance(context).getWifiState() == WifiState.WIFI_STATE_DISABLED) {
					CreatHotspotManager.getInstance(context).reCreateOpenHotspot();
				}
				// 清空当前连接数据
				break;

			// 连接中
			case 10:
				switch (count % 3) {
				case 0:
					connecting.setText(ResourceUtil.getString(context, R.string.share_connecting1));
					break;
				case 1:
					connecting.setText(ResourceUtil.getString(context, R.string.share_connecting2));
					break;
				case 2:
					connecting.setText(ResourceUtil.getString(context, R.string.share_connecting3));
					break;
				}
				break;
			}
		}

	};

	/**
	 * 服务启动成功
	 */
	public void hanldeStartSwitchView() {
		if (isConnectingView) {
			SSID = CreatHotspotManager.getCreatHotSpotName(AcceptTheWayActivity.this);
			if (!TextUtils.isEmpty(serverIp)) {
				serverUrl = getServerUrl(serverIp);
			}
			switchUI(SSID, serverUrl);
		}
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
	 * 
	 * @Title: uninit
	 * @Description: TODO(卸载资源)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void uninit() {

		try {
			serviceHelper.stopServer();
			serviceHelper.disconnect();
			creatHotspotListener.stop();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		uninit();
		Utils.recycleBackgroundResource(scannerIv);
	}

	public String getServerUrl(String serverIp) {
		String url = null;
		if (!TextUtils.isEmpty(serverIp)) {
			url = "http://" + serverIp + ":" + TransferHost.PORT + "/";
		}
		return url;
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		acquireWakeLock();
		ConnectHotspotManage.getInstance(this).setMobileDataEnabled(false);
		CreatHotspotManager.getInstance(this).reCreateOpenHotspot();
		creatHotspotListener.start();

	}

	@Override
	protected void onStop() {
		super.onStop();
		releaseWakeLock();
		creatHotspotListener.stop();
		CreatHotspotManager.getInstance(this).closeOpenHotspot();
		ConnectHotspotManage.getInstance(this).resumeServerNetwork();

	}

	@Override
	public void finish() {
		String fromActivity = ZeroDataResourceHelper.getFromActivity(this,
				ZeroDataConstant.ZERO_DATA_INVITE_ACTIVITY_KEY);
		if (ZeroDataShareActivity.class.getName().equals(fromActivity)) {
			ConnectHotspotManage.getInstance(this).resumeServerNetwork();
			ZeroDataResourceHelper.saveFromActivity(this, ZeroDataConstant.ZERO_DATA_INVITE_ACTIVITY_KEY, "");
		}
		super.finish();
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
