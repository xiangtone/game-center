package com.x.business.zerodata.client;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.x.business.zerodata.client.http.TransferHost;
import com.x.business.zerodata.client.http.ZeroClientRequest;
import com.x.business.zerodata.client.http.TransferHost.ResponseCode;
import com.x.business.zerodata.client.http.model.TransferResponse;
import com.x.business.zerodata.connection.helper.ZeroDataConnectHelper;
import com.x.business.zerodata.connection.manager.ConnectHotspotManage;
import com.x.business.zerodata.helper.ZeroDataConstant;
import com.x.business.zerodata.helper.ZeroDataResourceHelper;
import com.x.business.zerodata.transfer.TransferManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.http.volley.DefaultRetryPolicy;
import com.x.publics.http.volley.NoConnectionError;
import com.x.publics.http.volley.TimeoutError;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.zerodata.ZeroDataClientActivity;
import com.x.ui.activity.zerodata.ZeroDataQrConnectingActivity;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 
 
 *
 */
public class ClientManager {

	public static ClientManager clientManager;
	public Context context;

	public static ClientManager getInstance(Context context) {

		if (clientManager == null) {
			clientManager = new ClientManager(context);
		}
		return clientManager;
	}

	public ClientManager(Context context) {
		this.context = context;
	}

	/** 
	* @Title: startRequestServerThread 
	* @Description: 获取传输列表请求 
	* @param @param serverIp     
	* @return void    
	*/

	public void startRequestServerThread(final String serverIp) {
		requestDataThread(serverIp);
	}

	/** 
	* @Title: startRequestServerThread 
	* @Description: 获取传输列表请求 
	* @param @param serverIp     
	* @return void    
	*/

	public void startQrRequestServerThread(final String serverIp) {
		requestQrDataThread(serverIp);
	}

	/**
	 * 客户端请求
	 * @param serverIp
	 */
	public void requestDataThread(final String serverIp) {

		final String url = getTransferListUrl(serverIp);

		final DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(10000, 10000);

		final Listener<JSONObject> listener = new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				if (response != null) {
					TransferResponse transferResponse = (TransferResponse) JsonUtil.jsonToBean(response,
							TransferResponse.class);
					if (transferResponse != null && transferResponse.state != null) {
						switch (transferResponse.state.code) {
						case ResponseCode.SUCCESS:
							ZeroDataClientActivity.isDownloadStop = true ;
							TransferManager.getInstance().launchTransferProcess(context, response.toString(), serverIp);
							break;
						case ResponseCode.CLIENT_OUT_SIZE:
							sendBroadCast(ZeroDataConstant.ACTION_HTTP_REQUEST_CLIENT_OUT_SIZE);
							break;
						}

					}

				}
			}
		};
		final ErrorListener errorListener = new ErrorListener() {
			int noConnectionCounter = 0;

			@Override
			public void onErrorResponse(VolleyError error) {

				if (error instanceof NoConnectionError) {
					if (noConnectionCounter >= ZeroDataConnectHelper.CLIENT_RECONNECTION_COUNTER) {
						sendBroadCast(ZeroDataConstant.ACTION_HTTP_REQUEST_NO_CONNECTION);
					} else {
						try {
							Thread.sleep(2000L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						LogUtil.getLogger().i("zerodata", "ClientManager[reconnect url=" + url + "]");
						ZeroClientRequest.getInstance(context).getTransferInfo(url, getDeviceRequestInfo(), listener,
								this, retryPolicy);
						noConnectionCounter++;
					}
				} else if (error instanceof TimeoutError) {
					sendBroadCast(ZeroDataConstant.ACTION_HTTP_REQUEST_TIMEOUT);
				}
			}
		};

		//开始连接服务端
		if (!TextUtils.isEmpty(url)) {
			LogUtil.getLogger().i("zerodata", "ClientManager[url=" + url + "]");
			ZeroClientRequest.getInstance(context).getTransferInfo(url, getDeviceRequestInfo(), listener,
					errorListener, retryPolicy);
		}
	}

	/**
	 * 二维码请求
	 * @param serverIp
	 */
	public void requestQrDataThread(final String serverIp) {

		final String url = getTransferListUrl(serverIp);

		final DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(10000, 10000);

		final Listener<JSONObject> listener = new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				LogUtil.getLogger().d(response.toString());
				if (response != null) {
					ZeroDataQrConnectingActivity.isDownloadStop = true ;
					TransferManager.getInstance().launchTransferProcess(context, response.toString(), serverIp);

				}

			}
		};
		final ErrorListener errorListener = new ErrorListener() {

			int noConnectionCounter = 0;

			@Override
			public void onErrorResponse(VolleyError error) {
				if (error instanceof NoConnectionError) {
					if (noConnectionCounter >= ZeroDataConnectHelper.CLIENT_RECONNECTION_COUNTER) {
						sendBroadCast(ZeroDataConstant.ACTION_HTTP_REQUEST_NO_CONNECTION);
					} else {
						try {
							Thread.sleep(2000L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						LogUtil.getLogger().i("zerodata", "ClientManager[reconnect url=" + url + "]");
						ZeroClientRequest.getInstance(context).getTransferInfo(url, getQrDeviceRequestInfo(), listener,
								this, retryPolicy);
						noConnectionCounter++;
					}
				} else if (error instanceof TimeoutError) {
					sendBroadCast(ZeroDataConstant.ACTION_HTTP_REQUEST_TIMEOUT);
				}
			}
		};

		//开始连接服务端
		if (!TextUtils.isEmpty(url)) {
			LogUtil.getLogger().i("zerodata", "ClientManager[url=" + url + "]");
			ZeroClientRequest.getInstance(context).getTransferInfo(url, getQrDeviceRequestInfo(), listener,
					errorListener, retryPolicy);
		}
	}

	/** 
	* @Title: updateServerProgress 
	* @Description: 更新服务端传输进度请求
	* @param @param percent
	* @param @param serverIp     
	* @return void    
	*/

	public void updateServerProgress(long percent, final String serverUrl) {

		final String url = getUpdateServerProgressUrl(serverUrl);

		final Listener<JSONObject> listener = new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {

			}
		};
		final ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
			}
		};

		if (!TextUtils.isEmpty(url)) {
			LogUtil.getLogger().i("zerodata", "ClientManager[url=" + url + ",percent:" + percent + "]");
			ZeroClientRequest.getInstance(context).updateServerProgress(url, getUpdateServerProgress(percent),
					listener, errorListener);
		}

	}

	/** 
	* @Title: disconnectHostSpot 
	* @Description: 主动断开服务器请求 
	* @param @param serverIp     
	* @return void    
	*/

	public void disconnectHostSpot(final String serverIp, final boolean isReSumeNetwork, final boolean isReconnectAp) {

		final String url = getdisconnectHostSpotUrl(serverIp);

		final Listener<JSONObject> listener = new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				sendBroadCastExtra(ZeroDataConstant.ACTION_HTTP_REQUEST_DISCONNECT,isReconnectAp);
				if (response != null && isReSumeNetwork) {
					ConnectHotspotManage.getInstance(context).resumeClientNetwork();
				}

			}
		};
		final ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				sendBroadCastExtra(ZeroDataConstant.ACTION_HTTP_REQUEST_DISCONNECT,isReconnectAp);
				if (isReSumeNetwork) {
					ConnectHotspotManage.getInstance(context).resumeClientNetwork();
				}

			}

		};

		if (!TextUtils.isEmpty(url)) {
			LogUtil.getLogger().i("zerodata", "ClientManager[url=" + url + "]");
			ZeroClientRequest.getInstance(context).disconnectRequest(url, getDeviceRequestInfo(), listener,
					errorListener);
		}

	}
	
	public void reconnectHostSpot(final String serverIp,final Listener<JSONObject> listener,final ErrorListener errorListener){
		final String url = getreconnectHostSpotUrl(serverIp);
		if (!TextUtils.isEmpty(url)) {
			LogUtil.getLogger().i("zerodata", "ClientManager[url=" + url + "]");
			ZeroClientRequest.getInstance(context).reconnectRequest(url, getReconnectRequestInfo(), listener,
					errorListener);
		}
	}

	/**
	 * 
	* @Title: sendBroadCast 
	* @Description: TODO(回调) 
	* @param @param action    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void sendBroadCast(String action) {
		Intent intent = new Intent(action);
		BroadcastManager.sendBroadcast(intent);
	}
	
	/**
	 * 
	* @Title: sendBroadCast 
	* @Description: TODO(回调) 
	* @param @param action    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void sendBroadCastExtra(String action , boolean extra) {
		Intent intent = new Intent(action);
		intent.putExtra("extra", extra) ;
		BroadcastManager.sendBroadcast(intent);
	}

	/** 
	* @Title: getDeviceRequestInfo 
	* @Description: 获取手机信息 
	* @param @return     
	* @return HashMap<String,Object>    
	*/

	public HashMap<String, Object> getDeviceRequestInfo() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("nickName", ZeroDataConnectHelper.getZeroShareNickName(context));
		map.put("mac", Utils.getMAC(context));
		map.put("imei", Utils.getIMEI(context));
		map.put("imsi", Utils.getIMSI(context));
		map.put("deviceModel", Utils.getDeviceModel());
		map.put("osVersion", Utils.getAndroidSDKINT());
		map.put("osVersionName", Utils.getAndroidRelease());
		map.put("headPortrait", ZeroDataResourceHelper.getSelfZerodataHeadPortraitValue(context));
		return map;
	}

	/**
	 * 二维码扫描
	 * @return
	 */
	public HashMap<String, Object> getQrDeviceRequestInfo() {
		HashMap<String, Object> map = getDeviceRequestInfo();
		map.put("connectType", ZeroDataConnectHelper.CLIENT_CONNECT_TYPE_AUTO);
		return map;
	}
	
	/**
	 * 重连请求
	 * @return
	 */
	public HashMap<String, Object> getReconnectRequestInfo() {
		HashMap<String, Object> map = getDeviceRequestInfo();
		map.put("connectType", ZeroDataConnectHelper.CLIENT_CONNECT_TYPE_AUTO);
		return map;
	}

	public HashMap<String, Object> getUpdateServerProgress(long percent) {
		HashMap<String, Object> map = getDeviceRequestInfo();
		map.put("currentProgress", percent);
		return map;
	}

	/** 
	* @Title: getTransferListUrl 
	* @Description: 获取请求地址 
	* @param @param serverIp
	* @param @return     
	* @return String    
	*/

	public String getTransferListUrl(String serverIp) {
		String url = null;
		if (!TextUtils.isEmpty(serverIp)) {
			url = "http://" + serverIp + ":" + TransferHost.PORT + "/" + TransferHost.GET_TRANSFER_LIST;
		}
		return url;
	}

	public String getServerUrl(String serverIp) {
		String url = null;
		if (!TextUtils.isEmpty(serverIp)) {
			url = "http://" + serverIp + ":" + TransferHost.PORT + "/";
		}
		return url;
	}

	/** 
	* @Title: getUpdateServerProgressUrl 
	* @Description: 获取请求地址 
	* @param @param serverIp
	* @param @return     
	* @return String    
	*/

	public String getUpdateServerProgressUrl(String serverIp) {
		String url = null;
		if (!TextUtils.isEmpty(serverIp)) {
			url = "http://" + serverIp + ":" + TransferHost.PORT + "/" + TransferHost.UPDATE_PROGRESS;
		}
		return url;
	}

	/** 
	* @Title: getdisconnectHostSpotUrl 
	* @Description: 获取请求地址  
	* @param @param serverIp
	* @param @return     
	* @return String    
	*/

	public String getdisconnectHostSpotUrl(String serverIp) {
		String url = null;
		if (!TextUtils.isEmpty(serverIp)) {
			url = "http://" + serverIp + ":" + TransferHost.PORT + "/" + TransferHost.DISCONNECT;
		}
		return url;
	}
	

	
	/** 
	* @Title: getreconnectHostSpotUrl 
	* @Description: 获取重连请求地址 
	* @param @param serverIp
	* @param @return    
	* @return String    
	*/ 
	
	public String getreconnectHostSpotUrl(String serverIp) {
		String url = null;
		if (!TextUtils.isEmpty(serverIp)) {
			url = "http://" + serverIp + ":" + TransferHost.PORT + "/" + TransferHost.RECONNECT;
		}
		return url;
	}

	public void uninit() {
		clientManager = null;
	}
}
