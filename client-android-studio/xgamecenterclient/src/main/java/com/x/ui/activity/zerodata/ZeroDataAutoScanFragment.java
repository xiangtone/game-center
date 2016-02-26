/**   
* @Title: AppsUpgradeFragment.java
* @Package com.x.activity
* @Description: TODO 

* @date 2014-1-24 上午10:28:28
* @version V1.0   
*/

package com.x.ui.activity.zerodata;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.x.R;
import com.x.business.audio.AudioEffectManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.zerodata.client.http.model.ClientEntity;
import com.x.business.zerodata.client.http.model.TransferRequest;
import com.x.business.zerodata.connection.helper.ZeroDataConnectHelper;
import com.x.business.zerodata.helper.ZeroDataConstant;
import com.x.business.zerodata.helper.ZeroDataResourceHelper;
import com.x.db.resource.NativeResourceConstant;
import com.x.publics.download.BroadcastManager;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.TextUtils;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.activity.resource.ResourceManagementActivity;

/**
* @Description: 我要分享里的“自动搜索”页面

* @date 2014-1-24 上午10:28:28
* 
*/

public class ZeroDataAutoScanFragment extends BaseFragment implements OnClickListener {

	private TextView nickNameBottomTv;//显示昵称
	private ImageView[] imageViews = new ImageView[5];
	private LinearLayout[] layouts = new LinearLayout[5];
	private TextView[] deviceNames = new TextView[5];
	private TextView[] sendPropressTv = new TextView[5];//显示发送数据进度
	private ImageView[] deviceChioced = new ImageView[5];
	private ClientRequestReceiver clientRequestReceiver;
	public ServerAutoHandler serverAutoHandler;
	private TextView showShareDataTipTv = null;
	//雷达View
	private ImageView scan_o, scan_t, scan_s;
	private Animation animation_o, animation_t, animation_s;

	ClientEntity clientEntity;
	public List<ClientEntity> clientDevicePool = new ArrayList<ClientEntity>();
	private static final int clientDevices[] = { 0, 1, 2, 3, 4 };
	public String clientGetPramas;
	public String tisp;
	//新手引导【头像对应的图标】	
	private Dialog mDialog = null;
	private ImageView guideIv, imageView;
	private boolean autoScanIsFirst = true, isStart = false;
	private boolean mIsVisibleToUser;

	public static Fragment newInstance(Bundle bundle) {
		ZeroDataAutoScanFragment fragment = new ZeroDataAutoScanFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_scan_wifi_ap_auto, null);
		init(rootView);
		registerClientRequestReceiver();
		serverAutoHandler = new ServerAutoHandler();
		return rootView;
	}

	private void init(View rootView) {
		imageView = (ImageView) rootView.findViewById(R.id.bg_imageView);
		nickNameBottomTv = (TextView) rootView.findViewById(R.id.ahs_fmNickName_tv);
		showShareDataTipTv = (TextView) rootView.findViewById(R.id.ahs_sharing_of_data_tv);

		scan_o = (ImageView) rootView.findViewById(R.id.ahs_scan_o);
		scan_t = (ImageView) rootView.findViewById(R.id.ahs_scan_t);
		scan_s = (ImageView) rootView.findViewById(R.id.ahs_scan_s);
		animation_o = AnimationUtils.loadAnimation(mActivity, R.anim.share_scan_send);
		animation_t = AnimationUtils.loadAnimation(mActivity, R.anim.share_scan_send);
		animation_s = AnimationUtils.loadAnimation(mActivity, R.anim.share_scan_send);
		scan_o.setAnimation(animation_o);
		animation_o.setStartOffset(0);
		scan_t.setAnimation(animation_t);
		animation_t.setStartOffset(500);
		scan_s.setAnimation(animation_s);
		animation_s.setStartOffset(1000);
		// 显示自己头像
		rootView.findViewById(R.id.img_my_head).setBackgroundResource(
				ZeroDataResourceHelper.getSelfZerodataHeadPortrait(mActivity));

		imageViews[0] = (ImageView) rootView.findViewById(R.id.img_1);
		imageViews[1] = (ImageView) rootView.findViewById(R.id.img_2);
		imageViews[2] = (ImageView) rootView.findViewById(R.id.img_3);
		imageViews[3] = (ImageView) rootView.findViewById(R.id.img_4);
		imageViews[4] = (ImageView) rootView.findViewById(R.id.img_5);

		layouts[0] = (LinearLayout) rootView.findViewById(R.id.ahs_receive_device_layout1);
		layouts[1] = (LinearLayout) rootView.findViewById(R.id.ahs_receive_device_layout2);
		layouts[2] = (LinearLayout) rootView.findViewById(R.id.ahs_receive_device_layout3);
		layouts[3] = (LinearLayout) rootView.findViewById(R.id.ahs_receive_device_layout4);
		layouts[4] = (LinearLayout) rootView.findViewById(R.id.ahs_receive_device_layout5);

		deviceNames[0] = (TextView) rootView.findViewById(R.id.ahs_receive_device_tv1);
		deviceNames[1] = (TextView) rootView.findViewById(R.id.ahs_receive_device_tv2);
		deviceNames[2] = (TextView) rootView.findViewById(R.id.ahs_receive_device_tv3);
		deviceNames[3] = (TextView) rootView.findViewById(R.id.ahs_receive_device_tv4);
		deviceNames[4] = (TextView) rootView.findViewById(R.id.ahs_receive_device_tv5);

		sendPropressTv[0] = (TextView) rootView.findViewById(R.id.ahs_connection_stateTv1);
		sendPropressTv[1] = (TextView) rootView.findViewById(R.id.ahs_connection_stateTv2);
		sendPropressTv[2] = (TextView) rootView.findViewById(R.id.ahs_connection_stateTv3);
		sendPropressTv[3] = (TextView) rootView.findViewById(R.id.ahs_connection_stateTv4);
		sendPropressTv[4] = (TextView) rootView.findViewById(R.id.ahs_connection_stateTv5);

		deviceChioced[0] = (ImageView) rootView.findViewById(R.id.ahs_receive_chioceDevice_iv1);
		deviceChioced[1] = (ImageView) rootView.findViewById(R.id.ahs_receive_chioceDevice_iv2);
		deviceChioced[2] = (ImageView) rootView.findViewById(R.id.ahs_receive_chioceDevice_iv3);
		deviceChioced[3] = (ImageView) rootView.findViewById(R.id.ahs_receive_chioceDevice_iv4);
		deviceChioced[4] = (ImageView) rootView.findViewById(R.id.ahs_receive_chioceDevice_iv5);

		for (int i = 0; i < layouts.length; i++) {
			layouts[i].setOnClickListener(this);
		}

		//装载数据
		int files = ZeroDataResourceHelper.getInstance(this.getActivity()).getShareResFileCount();
		String filesSize = ZeroDataResourceHelper.getInstance(this.getActivity()).getShareResFileCountSize();

		tisp = ResourceUtil.getString(mActivity, R.string.shared) + files
				+ ResourceUtil.getString(mActivity, R.string.share_files_with) + filesSize;

		if (files <= 1) {

			tisp = ResourceUtil.getString(mActivity, R.string.shared) + files
					+ ResourceUtil.getString(mActivity, R.string.share_file_with) + filesSize;
		}
		showShareDataTipTv.setText(tisp);

		nickNameBottomTv.setText(ZeroDataConnectHelper.getZeroShareNickName(this.getActivity()));

		// 设置背景图片
		Utils.setBackgroundResource(imageView, R.drawable.zapp_radar_background, mActivity);
	}

	/**
	 * 启动动画
	 * @param layout 所需显示的那个动画的layout
	 * @param witchReceiver 显示的是哪个layout的动画
	 */
	private void startCartoon(LinearLayout[] layouts, final int witchReceive, int headPortraitValue) {
		AudioEffectManager.getInstance().playScanResultAudioEffect(mActivity);//播放音效
		isStart = true;
		showGuideDialog();
		LinearInterpolator lip = new LinearInterpolator();
		Animation receiverAnim = AnimationUtils.loadAnimation(mActivity, R.anim.shar_of_receiver);
		receiverAnim.setInterpolator(lip);
		layouts[witchReceive].startAnimation(receiverAnim);
		imageViews[witchReceive].setBackgroundResource(ZeroDataResourceHelper
				.getZerodataHeadPortraitResource(headPortraitValue)); // 显示别人头像
		receiverAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
			}
		});
	}

	/**
	 * 动画消失
	 * @param layout 所需消失的那个动画的layout
	 * @param witchReceiver 消失的是哪个layout的动画
	 */
	private void stopCartoon(final LinearLayout[] layouts, final int witchReceive) {
		LinearInterpolator lip = new LinearInterpolator();
		Animation receiverAnim = AnimationUtils.loadAnimation(ZeroDataAutoScanFragment.this.getActivity(),
				R.anim.shar_of_receiver_disappear);
		receiverAnim.setInterpolator(lip);
		layouts[witchReceive].startAnimation(receiverAnim);
		receiverAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				layouts[witchReceive].setVisibility(View.INVISIBLE);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		if (animation_o != null)
			scan_o.startAnimation(animation_o);
		if (animation_t != null)
			scan_t.startAnimation(animation_t);
		if (animation_s != null)
			scan_s.startAnimation(animation_s);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				AudioEffectManager.getInstance().playBeginScanAudioEffect(mActivity);
			}
		}, 500);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		BroadcastManager.unregisterReceiver(clientRequestReceiver);
		Utils.recycleBackgroundResource(imageView);
	}

	@Override
	public void onPause() {
		super.onPause();
		AudioEffectManager.getInstance().releaseMusicPlayer();
		scan_o.clearAnimation();
		scan_t.clearAnimation();
		scan_s.clearAnimation();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ahs_receive_device_layout1:
			handleServerResponse(clientDevices[0]);
			break;
		case R.id.ahs_receive_device_layout2:
			handleServerResponse(clientDevices[1]);
			break;
		case R.id.ahs_receive_device_layout3:
			handleServerResponse(clientDevices[2]);
			break;
		case R.id.ahs_receive_device_layout4:
			handleServerResponse(clientDevices[3]);
			break;
		case R.id.ahs_receive_device_layout5:
			handleServerResponse(clientDevices[4]);
			break;
		default:
			break;
		}
	}

	//再次分享
	public void shareAgain() {
		Intent resourceintent = new Intent(this.getActivity(), ResourceManagementActivity.class);
		resourceintent.putExtra("MODE", NativeResourceConstant.SHARE_MODE);
		startActivity(resourceintent);
		this.getActivity().finish();
	}

	/**
	 * 注册监听
	 */
	private void registerClientRequestReceiver() {

		clientRequestReceiver = new ClientRequestReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ZeroDataConstant.ACTION_HTTP_REQUEST_HANDLER);
		filter.addAction(ZeroDataConstant.ACTION_HTTP_UPDATE_PROGRESS_HANDLER);
		filter.addAction(ZeroDataConstant.ACTION_HTTP_DISCONNECT_HANDLER);
		filter.addAction(ZeroDataConstant.ACTION_HTTP_RECONNECT_REQUEST_HANDLER);
		filter.addAction(ZeroDataConstant.ACTION_ACTIVITY_CLEAR_CLIENT_DEVICE_HANDLER);
		BroadcastManager.registerReceiver(clientRequestReceiver, filter);
	}

	/**
	 * 监听到客户端请求
	 
	 *
	 */
	public class ClientRequestReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			//客户端请求协议数据
			if (intent != null && intent.getAction().equals(ZeroDataConstant.ACTION_HTTP_REQUEST_HANDLER)) {
				String clientRequestParams = intent.getStringExtra(ZeroDataConstant.CLIENT_REQUEST_PARAMS);
				handleAddClient(clientRequestParams, false);
				//接收更新进度
			} else if (intent != null
					&& intent.getAction().equals(ZeroDataConstant.ACTION_HTTP_UPDATE_PROGRESS_HANDLER)) {
				String clientRequestParams = intent.getStringExtra(ZeroDataConstant.CLIENT_REQUEST_PARAMS);
				handleClientUpdateProgress(clientRequestParams);
				//断开连接
			} else if (intent != null && intent.getAction().equals(ZeroDataConstant.ACTION_HTTP_DISCONNECT_HANDLER)) {
				String clientRequestParams = intent.getStringExtra(ZeroDataConstant.CLIENT_REQUEST_PARAMS);
				handleDisconnectDevices(clientRequestParams);
				//重连
			} else if (intent != null
					&& intent.getAction().equals(ZeroDataConstant.ACTION_HTTP_RECONNECT_REQUEST_HANDLER)) {

				String clientRequestParams = intent.getStringExtra(ZeroDataConstant.CLIENT_REQUEST_PARAMS);
				handleAddClient(clientRequestParams, true);
				//服务或者连接断开清空数据
			} else if (intent != null
					&& intent.getAction().equals(ZeroDataConstant.ACTION_ACTIVITY_CLEAR_CLIENT_DEVICE_HANDLER)) {
				clearAllClientDevicePoolView();
			}
		}
	}

	/**
	 * 服务端处理器
	 
	 *
	 */
	class ServerAutoHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			//新增客户端
			case ZeroDataConnectHelper.SERVER_ADD_DEVICE:
				hanlderAddClient(msg.obj);
				break;
			//更新进度
			case ZeroDataConnectHelper.SERVER_UPDATE_PROGRESS:
				doUpdateProgress(msg.obj);
				break;
			//断开连接
			case ZeroDataConnectHelper.SERVER_DISCONNECT:
				doDisConnectDevices(msg.obj);
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 处理客户端请求
	 * @param clientRequestParams
	 */
	public void handleAddClient(String clientRequestParams, boolean isReconnect) {
		if (!TextUtils.isEmpty(clientRequestParams)) {
			TransferRequest transferRequest = null;
			LogUtil.getLogger().i("zerodata", "transferRequest[" + clientRequestParams + "]");
			transferRequest = ZeroDataResourceHelper.getInstance(ZeroDataAutoScanFragment.this.getActivity())
					.parseHttpGetParams(clientRequestParams);

			if (transferRequest == null) {
				return;
			}
			boolean isExsitDevice = false;

			for (int i = 0; i < clientDevicePool.size(); i++) {
				if (clientDevicePool.get(i).getTransferRequest().getMac().equals(transferRequest.getMac())
						|| clientDevicePool.get(i).getTransferRequest().getImei().equals(transferRequest.getImei())) {
					isExsitDevice = true;
				}
			}
			//如果设备池数量大于上限
			if (clientDevicePool.size() >= ZeroDataConnectHelper.SERVER_MAX_CLIENTS) {
				//处理客户端重连
				if (isReconnect) {
					sendReconnectResponseBroadCast(clientRequestParams, true);
				} else {
					sendCancelResponseBroadCast(clientRequestParams, true);
				}

				return;
			}
			//处理客户端重连
			if (isReconnect) {
				sendReconnectResponseBroadCast(clientRequestParams, false);
			}
			//如果该设备已经存在
			if (!isExsitDevice) {
				ClientEntity ce = new ClientEntity();
				ce.setTransferRequest(transferRequest);

				int devicesLayout = ZeroDataConnectHelper.getServerDeviceLayout(clientDevicePool);
				ce.setDevicesLayout(devicesLayout);
				clientDevicePool.add(ce);

				Message msg = serverAutoHandler.obtainMessage(ZeroDataConnectHelper.SERVER_ADD_DEVICE);
				msg.obj = ce;
				serverAutoHandler.sendMessage(msg);
			}

		}

	}

	/**
	 * 处理客户端更新进度
	 * @param clientRequestParams
	 */
	public void handleClientUpdateProgress(String clientRequestParams) {
		if (!TextUtils.isEmpty(clientRequestParams)) {
			TransferRequest transferRequest = null;
			LogUtil.getLogger().i("zerodata", "transferRequest[" + clientRequestParams + "]");
			transferRequest = ZeroDataResourceHelper.getInstance(ZeroDataAutoScanFragment.this.getActivity())
					.parseHttpGetParams(clientRequestParams);

			if (transferRequest == null) {
				return;
			}
			if (clientDevicePool.size() > 0) {
				for (int i = 0; i < clientDevicePool.size(); i++) {
					if (clientDevicePool.get(i).getTransferRequest().getMac().equals(transferRequest.getMac())
							|| clientDevicePool.get(i).getTransferRequest().getImei().equals(transferRequest.getImei())) {
						ClientEntity ce = clientDevicePool.get(i);
						ce.setTransferRequest(transferRequest);
						Message msg = serverAutoHandler.obtainMessage(ZeroDataConnectHelper.SERVER_UPDATE_PROGRESS);
						msg.obj = ce;
						serverAutoHandler.sendMessage(msg);
					}
				}
			}
		}

	}

	/**
	 * 处理客户端取消连接
	 * @param clientRequestParams
	 */
	public void handleDisconnectDevices(String clientRequestParams) {
		if (!TextUtils.isEmpty(clientRequestParams)) {
			TransferRequest transferRequest = null;
			LogUtil.getLogger().i("zerodata", "transferRequest[" + clientRequestParams + "]");
			transferRequest = ZeroDataResourceHelper.getInstance(ZeroDataAutoScanFragment.this.getActivity())
					.parseHttpGetParams(clientRequestParams);

			if (transferRequest == null) {
				return;
			}

			if (clientDevicePool.size() > 0) {
				for (int i = 0; i < clientDevicePool.size(); i++) {
					if (clientDevicePool.get(i).getTransferRequest().getMac().equals(transferRequest.getMac())) {
						ClientEntity ce = clientDevicePool.get(i);
						ce.setTransferRequest(transferRequest);
						Message msg = serverAutoHandler.obtainMessage(ZeroDataConnectHelper.SERVER_DISCONNECT);
						msg.obj = ce;
						serverAutoHandler.sendMessage(msg);
					}
				}
			}
		}

	}

	/**
	 * 更新进度
	 * @param obj
	 */
	public void doUpdateProgress(Object obj) {
		if (obj != null && obj instanceof ClientEntity) {
			ClientEntity ce = (ClientEntity) obj;
			int progress = ce.getTransferRequest().getCurrentProgress();
			sendPropressTv[ce.getDevicesLayout()].setVisibility(View.VISIBLE);
			sendPropressTv[ce.getDevicesLayout()].setText(progress + "%");
		}
	}

	/**
	 * 取消连接
	 * @param obj
	 */
	public void doDisConnectDevices(Object obj) {
		if (obj != null && obj instanceof ClientEntity) {
			ClientEntity ce = (ClientEntity) obj;
			stopCartoon(layouts, ce.getDevicesLayout());
			sendPropressTv[ce.getDevicesLayout()].setVisibility(View.INVISIBLE);//重置显示进度
			sendPropressTv[ce.getDevicesLayout()].setText("");
			deviceChioced[ce.getDevicesLayout()].setVisibility(View.INVISIBLE);
			if (clientDevicePool.size() > 0) {
				for (int i = 0; i < clientDevicePool.size(); i++) {
					if (clientDevicePool.get(i).getTransferRequest().getMac().equals(ce.getTransferRequest().getMac())
							|| clientDevicePool.get(i).getTransferRequest().getImei()
									.equals(ce.getTransferRequest().getImei())) {
						clientDevicePool.remove(i);
						if (clientDevicePool.size() == 0) { //所有接受者断开重新播放扫描音效
							AudioEffectManager.getInstance().playBeginScanAudioEffect(mActivity);
						}
					}
				}
			}

		}
	}

	/**
	 * 新增客户端
	 * @param obj
	 */
	public void hanlderAddClient(Object obj) {
		if (obj != null && obj instanceof ClientEntity) {
			ClientEntity ce = (ClientEntity) obj;
			deviceNames[ce.getDevicesLayout()].setText(ce.getTransferRequest().getNickName());
			layouts[ce.getDevicesLayout()].setVisibility(View.VISIBLE);
			sendPropressTv[ce.getDevicesLayout()].setVisibility(View.INVISIBLE);//重置显示进度
			sendPropressTv[ce.getDevicesLayout()].setText("");
			startCartoon(layouts, ce.getDevicesLayout(), ce.getTransferRequest().getHeadPortrait());
			//处理自动连接
			if (ce.getTransferRequest().getConnectType() == ZeroDataConnectHelper.CLIENT_CONNECT_TYPE_AUTO) {
				handleServerResponse(ce.getDevicesLayout());
			}
		}
	}

	/**
	 * 响应请求
	 * @param deviceLayout
	 */
	private void handleServerResponse(int deviceLayout) {
		if (deviceNames != null && deviceNames.length >= 5) {
			deviceChioced[deviceLayout].setVisibility(View.VISIBLE);
			for (int i = 0; i < clientDevicePool.size(); i++) {
				if (clientDevicePool.get(i).getDevicesLayout() == deviceLayout) {
					sendResponseBroadCast(clientDevicePool.get(i).getTransferRequest().getRawClientGetPramas());
					break;
				}
			}

		}

	}

	/**
	 *  开始响应客户端
	 * @param clientGetPramas
	 */
	private void sendResponseBroadCast(String clientGetPramas) {
		Intent intent = new Intent(ZeroDataConstant.ACTION_HTTP_RESPONSE);
		intent.putExtra("isResponse", true);
		intent.putExtra(ZeroDataConstant.CLIENT_REQUEST_PARAMS, clientGetPramas);
		BroadcastManager.sendBroadcast(intent);
	}

	/**
	 * 
	* @Title: sendCancelResponseBroadCast 
	* @Description: TODO(连接数越界) 
	* @param @param clientGetPramas
	* @param @param isCilentOut    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void sendCancelResponseBroadCast(String clientGetPramas, boolean isCilentOut) {
		Intent intent = new Intent(ZeroDataConstant.ACTION_HTTP_RESPONSE);
		intent.putExtra("isCilentOut", isCilentOut);
		intent.putExtra(ZeroDataConstant.CLIENT_REQUEST_PARAMS, clientGetPramas);
		BroadcastManager.sendBroadcast(intent);
	}

	/**
	 * 
	* @Title: sendServerReconnectResponseBroadCast 
	* @Description: TODO(响应重连的客户端) 
	* @param @param clientGetPramas
	* @param @param isClientOut    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void sendReconnectResponseBroadCast(String clientGetPramas, boolean isReconnectCilentOut) {
		Intent intent = new Intent(ZeroDataConstant.ACTION_HTTP_RECONNECT_RESPONSE);
		intent.putExtra("isReconnectCilentOut", isReconnectCilentOut);
		intent.putExtra(ZeroDataConstant.CLIENT_REQUEST_PARAMS, clientGetPramas);
		BroadcastManager.sendBroadcast(intent);
	}

	/**
	 * 
	* @Title: clearAllClientDevicePoolView 
	* @Description: TODO(重置界面状态) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void clearAllClientDevicePoolView() {
		if (clientDevicePool != null && clientDevicePool.size() > 0) {
			clientDevicePool.clear();
		}
		for (int i = 0; i < layouts.length; i++) {
			layouts[i].setVisibility(View.INVISIBLE);
			deviceChioced[i].setVisibility(View.INVISIBLE);
			sendPropressTv[i].setText("");
			sendPropressTv[i].setVisibility(View.INVISIBLE);
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void setGuideDialog() {
		if (mDialog != null)
			return;
		mDialog = new Dialog(mActivity, R.style.guideDialog);
		mDialog.setContentView(R.layout.guide_zerodatashare_head);
		Window dialogWindow = mDialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		lp.y = (int) this.getResources().getDimension(R.dimen.guide_wifishare_marginTop); // 新位置Y坐标
		dialogWindow.setAttributes(lp);
		guideIv = (ImageView) mDialog.findViewById(R.id.guide_head_iv);
		guideIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				SharedPrefsUtil.putValue(mActivity, "autoScanIsFirst", false);
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
			}
		});
		mDialog.setCanceledOnTouchOutside(false);
		// 增加监听mialog back事件重置第一次进入状态值
		mDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
					SharedPrefsUtil.putValue(mActivity, "autoScanIsFirst", false);
					dialog.dismiss();
				}
				return false;
			}
		});
		mDialog.show();
	}

	/**
	* 新手引导
	 */
	private void showGuideDialog() {
		autoScanIsFirst = SharedPrefsUtil.getValue(mActivity, "autoScanIsFirst", true);
		if (isStart == false || mIsVisibleToUser == false || autoScanIsFirst == false)
			return;
		setGuideDialog();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		mIsVisibleToUser = isVisibleToUser;
		if (isVisibleToUser == true) {
			showGuideDialog();
		} else {
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}
			mDialog = null;
		}
	}

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	private void setSkinTheme() {
		for (int i = 0; i < deviceChioced.length; i++) {
			SkinConfigManager.getInstance().setViewBackground(mActivity, deviceChioced[i],
					SkinConstan.SHARE_DEVICE_SELECTED);
		}
	}
}
