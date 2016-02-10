/**   
* @Title: ZeroDataClientTransferActivity.java
* @Package com.x.ui.activity.zerodata
* @Description: TODO 

* @date 2014-3-24 上午11:41:22
* @version V1.0   
*/

package com.x.ui.activity.zerodata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.x.R;
import com.x.business.audio.AudioEffectManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.business.zerodata.client.ClientManager;
import com.x.business.zerodata.client.http.TransferHost;
import com.x.business.zerodata.client.http.model.TransferResponse;
import com.x.business.zerodata.client.http.model.Transfers;
import com.x.business.zerodata.connection.helper.ZeroDataConnectHelper;
import com.x.business.zerodata.connection.listener.ConnectWifiListener;
import com.x.business.zerodata.connection.manager.ConnectHotspotManage;
import com.x.business.zerodata.helper.ZeroDataConstant;
import com.x.business.zerodata.transfer.TransferBean;
import com.x.business.zerodata.transfer.TransferIntent;
import com.x.business.zerodata.transfer.TransferManager;
import com.x.db.resource.NativeResourceConstant.FileType;
import com.x.publics.download.BroadcastManager;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.MediaPlayerUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.adapter.TransferListAdapter;
import com.x.ui.adapter.TransferViewHolder;

/**
* @ClassName: ZeroDataClientTransferActivity
* @Description: 零流量分享正在接受页面 

* @date 2014-3-24 上午11:41:22
* 
*/

@SuppressLint("HandlerLeak")
public class ZeroDataClientTransferActivity extends BaseActivity implements OnClickListener {

	public static final String TRANSFER_DATA = "transfer_data";
	public static final String SERVER_IP = "server_ip";

	private Context context = this;
	private ListView transferListView;
	private TransferListAdapter transferListAdapter;
	private List<TransferBean> transferBeanList;
	private TextView transferPercentTv, transferSaveStreamTv;
	private View emptyView, loadingView;
	private TransferUiReceiver transferUiReceiver;
	private ReconnectReceiver reconnectReceiver;
	private boolean inited;
	private long totalByte, finishByte;
	private int fileCount;
	private long lastProgress = -1;
	private String serverIp = null;
	private ConnectHotspotManage chsm;
	private String ssid;
	private Dialog reconnetDialog = null;
	private Dialog errorDialog = null;
	public ConnectWifiListener connectWifiListener; //WiFi搜索进度条线程
	private boolean isConnectHotSpot = false;
	private boolean isConnectServer = false;
	private String tips;

	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View loadingPb, loadingLogo;
	private View mNavigationView, mTitleView, mTitlePendant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zero_data_client_transfer);
		initUi();
		initNavigation();
		initTransfer();
		initSSID();
		getSaveInstanceState(savedInstanceState);
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

	@Override
	public void onSaveInstanceState(Bundle outStateBundle) {
		saveState(outStateBundle);
	}

	public Bundle saveState(Bundle outStateBundle) {
		if (outStateBundle != null) {
			outStateBundle.putString("ssid", ssid);
		}
		return outStateBundle;
	}

	public void getSaveInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			ssid = savedInstanceState.getString("ssid");
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		acquireWakeLock();
		if (!inited) {
			sendKillActivity();
			registReceiver();
		}
		DataEyeManager.getInstance().module(ModuleName.SHARE_FREE_RECEIVE, true);
	}

	@Override
	protected void onStop() {
		super.onStop();
		releaseWakeLock();
		ClientManager.getInstance(context).disconnectHostSpot(serverIp, true, false);
		TransferManager.getInstance().pauseAllDownload(context);
	}

	@Override
	public void onPause() {
		super.onPause();
		DataEyeManager.getInstance().module(ModuleName.SHARE_FREE_RECEIVE, false);
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

	private Handler connectHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ZeroDataConnectHelper.CLIENT_WIFI_STATE_DISABLED:
				chsm.openWifi();
				isConnectHotSpot = false;
				break;
			case ZeroDataConnectHelper.CLIENT_WIFI_STATE_ENABLED:
				if (chsm.isDisConnectSSID(chsm.getSSID())) {
					connectAp();
				} else if (!chsm.getSSID().contains(ZeroDataConnectHelper.SERVER_HOTSPOT_HEADER)) {
					chsm.disconnectWifi();
					connectAp();
				} else if (chsm.isConnectWifi()) {
					if (!isConnectServer && chsm.getServerIp().startsWith("192.168")) {
						disReconnectHostSpot();
						hanlderConnectedResultsDisplay();
					}
				}
				break;
			case ZeroDataConnectHelper.CLIENT_WIFI_WIFI_STATE_UNKNOWN:
				chsm.openWifi();
				break;

			case ZeroDataConnectHelper.CLIENT_RETRY_CONNECT_AP:
				showReConnectDialog();
				break;

			default:
				break;
			}
			;
		}
	};

	/**
	 * 
	* @Title: connectAp 
	* @Description: TODO(连接热点) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void connectAp() {
		if (!isConnectHotSpot) {
			chsm.doConnectOpenAp(ssid);
			isConnectHotSpot = true;
		}
	}

	private void initUi() {
		emptyView = findViewById(R.id.empty_rl);
		loadingView = findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		transferPercentTv = (TextView) findViewById(R.id.azdct_transfer_percent_tips_tv);
		transferSaveStreamTv = (TextView) findViewById(R.id.azdct_transfer_save_tips_tv);
		transferListView = (ListView) findViewById(R.id.azdct_transfer_lv);
		// 赋值=去除ListView点击时，默认的黄色背景色
		transferListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		transferListAdapter = new TransferListAdapter(this);
		transferListView.setAdapter(transferListAdapter);
		transferListAdapter.setListView(transferListView);
	}

	private void initTransfer() {
		String transferData = null;
		if (getIntent() != null) {
			transferData = getIntent().getStringExtra(TRANSFER_DATA);
			serverIp = getIntent().getStringExtra(SERVER_IP);
		}
		if (transferData != null && serverIp != null) {
			TransferResponse transferResponse = (TransferResponse) JsonUtil.jsonToBean(transferData,
					TransferResponse.class);
			refreshTransferListView(transferResponse);
		}
	}

	private void refreshTransferListView(TransferResponse transferResponse) {

		Utils.executeAsyncTask(new AsyncTask<TransferResponse, Void, Void>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loadingView.setVisibility(View.VISIBLE);
			}

			@Override
			protected Void doInBackground(TransferResponse... params) {
				totalByte = 0;
				fileCount = 0;
				List<Transfers> transferList = params[0].transferList;
				transferBeanList = new ArrayList<TransferBean>();
				for (Transfers transfers : transferList) {
					TransferBean transferBean = TransferManager.getInstance().get(transfers, serverIp, context);
					transferBeanList.add(transferBean);
					totalByte += transfers.getFileSize();
					fileCount++;
				}
				List<TransferBean> list = transferListAdapter.getList();
				boolean isLastRequest = false;
				if (list != null && !list.isEmpty() && !transferBeanList.isEmpty()
						&& list.size() == transferBeanList.size()) {
					for (TransferBean transferBean : transferBeanList) {
						isLastRequest = false;
						for (TransferBean oldTransferBean : list) {
							if (oldTransferBean.getFileUrl().endsWith(transferBean.getFileUrl())) {
								isLastRequest = true;
								break;
							}
						}
						if (!isLastRequest)
							break;
					}
				} else {
					isLastRequest = false;
				}

				if (isLastRequest) {
					transferBeanList = list;
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				loadingView.setVisibility(View.GONE);
				percentHandler.sendEmptyMessage(2);
				if (!transferBeanList.isEmpty()) {
					transferListAdapter.setList(transferBeanList);
					TransferManager.getInstance().addDownloadList(context, transferBeanList);
				}
			}

		}, transferResponse);
	}

	private void initSSID() {
		chsm = ConnectHotspotManage.getInstance(context);
		connectWifiListener = new ConnectWifiListener(this.context, connectHandler);
		connectWifiListener.setDetectWifiTimeOut(3 * 1000L);
		ssid = chsm.getSSID();
		if (!TextUtils.isEmpty(ssid)) {
			ssid = ssid.substring(1, ssid.length() - 1);
		}
	}

	private void showEmptyView() {
		if (emptyView != null)
			emptyView.setVisibility(View.VISIBLE);
	}

	private class ReconnectReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Message msg = connectHandler.obtainMessage(ZeroDataConnectHelper.CLIENT_RETRY_CONNECT_AP);
			connectHandler.sendMessage(msg);
		}

	}

	private void showReConnectDialog() {
		lastProgress = -1;
		reconnectHostSpot();
		DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				disReconnectHostSpot();
				dialog.dismiss();
			}
		};
		reconnetDialog = Utils.showAloneDialog(context, ResourceUtil.getString(context, R.string.warm_tips),
				ResourceUtil.getString(context, R.string.dialog_zerodata_reconnect),
				ResourceUtil.getString(context, R.string.cancel), negativeListener, true);

	}

	private void dismissReconnectDialog() {
		if (reconnetDialog != null && reconnetDialog.isShowing()) {
			reconnetDialog.dismiss();
			reconnetDialog = null;
		}
	}

	private void disReconnectHostSpot() {
		isConnectServer = true;
		isConnectHotSpot = false;
		connectWifiListener.stop();

	}

	private void reconnectHostSpot() {
		isConnectServer = false;
		isConnectHotSpot = false;
		connectWifiListener.start();

	}

	final Listener<JSONObject> listener = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			dismissReconnectDialog();
			if (response != null) {
				TransferResponse transferResponse = (TransferResponse) JsonUtil.jsonToBean(response,
						TransferResponse.class);
				if (transferResponse != null && transferResponse.state != null) {
					if (transferResponse.state.code == TransferHost.ResponseCode.SUCCESS) {
						refreshTransferListView(transferResponse);
					} else if (transferResponse.state.code == TransferHost.ResponseCode.CLIENT_OUT_SIZE) {
						showErrorDialog(ResourceUtil.getString(context, R.string.dialog_zerodata_out_of_client_tips));
					}
				}
			}

		}
	};
	final ErrorListener errorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			dismissReconnectDialog();
			showErrorDialog(ResourceUtil.getString(context, R.string.dialog_zerodata_connect_error_tips));
		}
	};

	/** 
	* @Title: hanlderConnectedResultsDisplay 
	* @Description: TODO 
	* @param      
	* @return void    
	*/

	private void hanlderConnectedResultsDisplay() {
		ClientManager.getInstance(context).reconnectHostSpot(serverIp, listener, errorListener);
	}

	private void showErrorDialog(String tips) {
		if (errorDialog != null && errorDialog.isShowing())
			return;
		DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		errorDialog = Utils.showAloneDialog(context, ResourceUtil.getString(context, R.string.warm_tips), tips,
				ResourceUtil.getString(context, R.string.confirm), negativeListener, false);
	}

	private Handler percentHandler = new Handler() {
		String percent = null;
		long progress;

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: //更新界面
				if (msg.obj == null)
					break;
				HashMap<String, Long> sizeMap = (HashMap<String, Long>) msg.obj;
				totalByte = sizeMap.get("totalByte");
				finishByte = sizeMap.get("finishByte");
				if (totalByte == 0) {
					progress = 0;
				} else {
					progress = finishByte * 100 / totalByte;
				}
				percent = progress + "%";
				fileCount = sizeMap.get("fileCount").intValue();
				tips = (fileCount <= 1) ? "file" : "files";

				transferPercentTv.setText(ResourceUtil.getString(context, R.string.share_receive_percent, percent));
				transferSaveStreamTv.setText(ResourceUtil.getString(context, R.string.share_receive_save_stream,
						StorageUtils.size(totalByte), "" + fileCount, tips));
				refreshServerProgress(progress);
				break;
			case 2: //初始化界面
				tips = (fileCount <= 1) ? "file" : "files";
				transferPercentTv.setText(ResourceUtil.getString(context, R.string.share_receive_percent, "0%"));
				transferSaveStreamTv.setText(ResourceUtil.getString(context, R.string.share_receive_save_stream,
						StorageUtils.size(totalByte), "" + fileCount, tips));
				break;

			default:
				break;
			}
		};
	};

	public class TransferUiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			handleIntent(intent);

		}

		private void handleIntent(Intent intent) {

			if (intent != null && intent.getAction().equals(TransferIntent.INTENT_UPDATE_UI)) {
				int type = intent.getIntExtra(TransferIntent.TYPE, -1);
				String url;
				View taskListItem;
				TransferBean transferBean;
				TransferViewHolder transferViewHolder;
				String packageName;
				switch (type) {
				case TransferIntent.Types.DELETE:
					url = intent.getStringExtra(TransferIntent.URL);
					transferBean = (TransferBean) intent.getParcelableExtra(TransferIntent.DOWNLOADBEAN);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDelete(url);
					}
					refreshTransferPercent();
					break;
				/*case TransferIntent.Types.COMPLETE_INSTALL:
					packageName = intent.getStringExtra(TransferIntent.PACKAGENAME);
					if (!TextUtils.isEmpty(packageName) && (transferBean = getTransferBean(packageName)) != null) {
						url = transferBean.getFileUrl();
						transferBean.setFileStatus(TransferTask.TASK_LAUNCH);
						notifyAppDataChange(url, transferBean);
						taskListItem = transferListView.findViewWithTag(url);
						transferViewHolder = new TransferViewHolder(taskListItem);
						transferViewHolder.refreshDownloadStatus(transferBean, context);
					}
					break;*/
				case TransferIntent.Types.COMPLETE:
					url = intent.getStringExtra(TransferIntent.URL);
					transferBean = (TransferBean) intent.getParcelableExtra(TransferIntent.DOWNLOADBEAN);
					if (!TextUtils.isEmpty(url) && transferBean != null) {
						notifyAppDataChange(url, transferBean, true);
						taskListItem = transferListView.findViewWithTag(url);
						transferViewHolder = new TransferViewHolder(taskListItem);
						transferViewHolder.refreshDownloadStatus(transferBean, context);
						refreshTransferPercent();
					}
					break;
				case TransferIntent.Types.ERROR:
				case TransferIntent.Types.WAIT:
				case TransferIntent.Types.PREDOWNLOAD:
				case TransferIntent.Types.PAUSE:
					url = intent.getStringExtra(TransferIntent.URL);
					transferBean = (TransferBean) intent.getParcelableExtra(TransferIntent.DOWNLOADBEAN);
					if (!TextUtils.isEmpty(url) && transferBean != null) {
						notifyAppDataChange(url, transferBean, false);
						taskListItem = transferListView.findViewWithTag(url);
						transferViewHolder = new TransferViewHolder(taskListItem);
						transferViewHolder.refreshDownloadStatus(transferBean, context);
						transferViewHolder.refreshData(intent.getStringExtra(TransferIntent.PROCESS_PROMOT),
								intent.getStringExtra(TransferIntent.PROCESS_SPEED),
								Integer.valueOf(intent.getStringExtra(TransferIntent.PROCESS_PROGRESS)));
					}
					break;

				case TransferIntent.Types.PROCESS:
					url = intent.getStringExtra(TransferIntent.URL);
					transferBean = (TransferBean) intent.getParcelableExtra(TransferIntent.DOWNLOADBEAN);
					if (!TextUtils.isEmpty(url) && transferBean != null) {
						notifyAppDataChange(url, transferBean, false);
						taskListItem = transferListView.findViewWithTag(url);
						transferViewHolder = new TransferViewHolder(taskListItem);
						transferViewHolder.refreshData(intent.getStringExtra(TransferIntent.PROCESS_PROMOT),
								intent.getStringExtra(TransferIntent.PROCESS_SPEED),
								Integer.valueOf(intent.getStringExtra(TransferIntent.PROCESS_PROGRESS)));

						refreshTransferPercent();
					}
					break;
				}
			}
		}

		private TransferBean getTransferBean(String packageName) {
			List<TransferBean> list = transferListAdapter.getList();
			for (TransferBean transferBean : list) {
				if (transferBean.getFileType() == FileType.APK) {
					try {
						String absPath = transferBean.getFileSavePath();
						PackageManager pm = context.getPackageManager();
						PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES);
						if (pkgInfo != null && pkgInfo.packageName.endsWith(packageName)) {
							return transferBean;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			return null;
		}

		/** 
		* @Title: notifyAppDataChange 
		* @Description: 修改adapter数据，防止滑动list数据不对 
		* @param @param url
		* @param @param bean     
		* @return void    
		* @throws 
		*/

		private void notifyAppDataChange(String url, TransferBean bean, boolean complete) {
			List<TransferBean> list = transferListAdapter.getList();
			if (bean == null || list == null)
				return;
			for (int i = 0; i < list.size(); i++) {
				TransferBean transferBean = list.get(i);
				if (transferBean.getFileUrl() != null && transferBean.getFileUrl().equals(url)) {
					list.set(i, bean);
					break;
				}
			}
		}

		private void notifyAppDelete(String url) {
			List<TransferBean> list = transferListAdapter.getList();
			if (url == null)
				return;
			for (int i = 0; i < list.size(); i++) {
				TransferBean transferBean = list.get(i);
				if (transferBean.getFileUrl() != null && transferBean.getFileUrl().equals(url)) {
					list.remove(transferBean);
					break;
				}
			}
			if (list.isEmpty()) {
				showEmptyView();
			} else {
				transferListAdapter.setList(list);
			}
		}

		private void refreshTransferPercent() {
			Message msg = percentHandler.obtainMessage();
			msg.what = 1;
			msg.obj = getPercent();
			percentHandler.sendMessage(msg);
		}

		private HashMap<String, Long> getPercent() {
			HashMap<String, Long> sizeMap = new HashMap<String, Long>();
			List<TransferBean> list = transferListAdapter.getList();
			if (list == null)
				return null;
			long totalByte = 0, finishByte = 0;
			for (TransferBean transferBean : list) {
				totalByte += transferBean.getFileSize();
				finishByte += transferBean.getCurrentBytes();
			}
			sizeMap.put("totalByte", totalByte);
			sizeMap.put("finishByte", finishByte);
			sizeMap.put("fileCount", (long) list.size());
			return sizeMap;
		}
	}

	private void refreshServerProgress(long progress) {
		if (progress != lastProgress) {
			ClientManager.getInstance(context).updateServerProgress(progress, serverIp);
			lastProgress = progress;
			if (lastProgress == 100) {
				AudioEffectManager.getInstance().playReceiveSuccessAudioEffect(context);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregistReceiver();
		AudioEffectManager.getInstance().releaseMusicPlayer();
	}

	private void registReceiver() {
		transferUiReceiver = new TransferUiReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(TransferIntent.INTENT_UPDATE_UI);
		BroadcastManager.registerReceiver(transferUiReceiver, filter);

		reconnectReceiver = new ReconnectReceiver();
		IntentFilter reconnectReceiverFilter = new IntentFilter();
		reconnectReceiverFilter.addAction(TransferIntent.INTENT_TRANSFER_RECONNECT);
		BroadcastManager.registerReceiver(reconnectReceiver, reconnectReceiverFilter);

		inited = true;
	}

	private void unregistReceiver() {
		BroadcastManager.unregisterReceiver(transferUiReceiver);
		BroadcastManager.unregisterReceiver(reconnectReceiver);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			onBackPressed();
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		exit();
	}

	private void exit() {
		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				MediaPlayerUtil.getInstance(context).release();
				TransferManager.getInstance().deleteAllDownload(context);
				startActivity(new Intent(context, ZeroDataShareActivity.class));
				finish();

			}
		};

		DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		Utils.showDialog(context, ResourceUtil.getString(context, R.string.warm_tips),
				ResourceUtil.getString(context, R.string.dialog_zerodata_receive_exit),
				ResourceUtil.getString(context, R.string.confirm), positiveListener,
				ResourceUtil.getString(context, R.string.cancel), negativeListener);
	}

	private void sendKillActivity() {
		Intent intent = new Intent(ZeroDataConstant.ACTION_HTTP_REQUEST_FINSISH);
		BroadcastManager.sendBroadcast(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mh_navigate_ll:
			onBackPressed();
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
		SkinConfigManager.getInstance().setViewBackground(context, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(context, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
	}

}
