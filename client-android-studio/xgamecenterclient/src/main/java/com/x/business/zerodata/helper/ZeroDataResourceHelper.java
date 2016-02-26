package com.x.business.zerodata.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import com.x.R;
import com.x.business.resource.ResourceManage;
import com.x.business.zerodata.client.http.TransferHost;
import com.x.business.zerodata.client.http.model.TransferRequest;
import com.x.business.zerodata.client.http.model.TransferResponse;
import com.x.business.zerodata.client.http.model.Transfers;
import com.x.business.zerodata.client.http.model.TransferResponse.State;
import com.x.business.zerodata.connection.helper.ZeroDataConnectHelper;
import com.x.db.LocalAppEntityManager;
import com.x.db.resource.NativeResourceConstant.FileType;
import com.x.publics.model.InstallAppBean;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.Utils;

/**
 * 
 
 * 
 */
public class ZeroDataResourceHelper {

	private static ZeroDataResourceHelper zeroDataResourceHelper;
	private Context context;
	public static SharedPreferences prefers;

	public static final int[] headPortraitArray = new int[] { R.drawable.ic_head01, R.drawable.ic_head02,
			R.drawable.ic_head03, R.drawable.ic_head04, R.drawable.ic_head05, R.drawable.ic_head06,
			R.drawable.ic_head07, R.drawable.ic_head08 };//头像资源

	public static final int[] headBgArray = new int[] { R.drawable.ic_head01_bg, R.drawable.ic_head02_bg,
			R.drawable.ic_head03_bg, R.drawable.ic_head04_bg, R.drawable.ic_head05_bg, R.drawable.ic_head06_bg,
			R.drawable.ic_head07_bg, R.drawable.ic_head08_bg, }; // 头像背景资源

	public static final String ZERODATA_HEAD_PORTRAIT_KEY = "zerodataHeadPortrait";
	public static final String ZERODATA_HEAD_BG_PORTRAIT_KEY = "zerodataHeadBgPortrait";

	/**
	 * 构建实例
	 * 
	 
	 * @param context
	 * @return
	 */
	public static ZeroDataResourceHelper getInstance(Context context) {
		if (zeroDataResourceHelper == null) {
			zeroDataResourceHelper = new ZeroDataResourceHelper(context);
		}
		prefers = context.getSharedPreferences(ZeroDataConstant.ZERO_DATA_RESOURCE_PREFES, Context.MODE_PRIVATE);
		return zeroDataResourceHelper;
	}

	public ZeroDataResourceHelper(Context context) {
		this.context = context;
	}

	/**
	 * @return
	 */
	public int getShareResFileCount() {
		String[] shareRes = getShareResData();
		if (shareRes != null) {
			return shareRes.length;
		}
		return 0;
	}

	public String[] getShareResData() {
		String resourcePrefers = prefers.getString(ZeroDataConstant.ZERO_DATA_RESOURCE_KEY, "");
		if (!TextUtils.isEmpty(resourcePrefers)) {
			String[] splits = resourcePrefers.split(",@,");
			if (splits != null) {
				return splits;
			}
		}
		return null;
	}

	/**
	 * 获取分享文件的总大小
	 * 
	 * @return
	 */
	public String getShareResFileCountSize() {

		String resourcePrefers = prefers.getString(ZeroDataConstant.ZERO_DATA_RESOURCE_KEY, "");
		LogUtil.getLogger().d("zerodata", "resourcePrefers[" + resourcePrefers + "]");
		if (!TextUtils.isEmpty(resourcePrefers)) {
			String[] splits = resourcePrefers.split(",@,");
			if (splits != null) {
				Long fileSize = 0L;
				for (int i = 0; i < splits.length; i++) {
					fileSize += getfileSize(splits[i]);
				}
				return StorageUtils.sizeMB(fileSize);
			}
		}
		return "";
	}

	/**
	 * 
	 * @param fileFullPath
	 * @return
	 */
	public static Long getfileSize(String fileFullPath) {
		if (!TextUtils.isEmpty(fileFullPath)) {
			File file = new File(fileFullPath);
			if (file != null && file.exists()) {
				return file.length();
			}
		}
		return 0L;

	}

	public void putServerTransferData() {

		String[] shareRes = getShareResData();
		if (shareRes != null) {
			ArrayList<Transfers> list = new ArrayList<Transfers>();
			for (int i = 0; i < shareRes.length; i++) {
				File file = new File(shareRes[i]);
				Transfers transfers = new Transfers();
				transfers.setFileName(file.getName());
				transfers.setFileRawPath(file.getPath());
				transfers.setFileSize(file.length());
				transfers.setFileSuffix(getFileSuffix(file.getPath()));
				transfers.setFileType(Utils.getFileType(file.getPath()));
				if (transfers.getFileType() == FileType.APK) {
					InstallAppBean installAppBean = LocalAppEntityManager.getInstance().getLocalAppBySourceDir(
							file.getPath());
					transfers.setExAttribute(installAppBean == null ? file.getName() : installAppBean.getAppName()
							+ ".apk");
				} else {
					transfers.setExAttribute(file.getName());
				}
				transfers.setFileUrl(file.getPath());
				list.add(transfers);
			}

			TransferResponse response = new TransferResponse();
			response.setTransferList(list);

			State state = new State();
			state.setCode(200);
			state.setMsg("ok");
			response.setState(state);
			String transferPrefers = JsonUtil.objectToJson(response);
			prefers.edit().putString(ZeroDataConstant.ZERO_DATA_SHARE_TRANSFER_KEY, transferPrefers).commit();
			LogUtil.getLogger().i("zerodata", "transferPrefers[" + transferPrefers + "]");
		}

	}

	private String getFileSuffix(String path) {
		if (path == null || !path.contains("."))
			return "";
		return path.substring(path.lastIndexOf(".") + 1, path.length());
	}

	public String getServerTransferData() {
		return prefers.getString(ZeroDataConstant.ZERO_DATA_SHARE_TRANSFER_KEY, "");
	}

	public String getDisconnectResponse() {

		TransferResponse response = new TransferResponse();

		State state = new State();
		state.setCode(TransferHost.ResponseCode.DISCONNECT_RESPONSE);
		state.setMsg(TransferHost.ResponseMsg.ok);
		response.setState(state);
		return JsonUtil.objectToJson(response);
	}

	/**
	 * 
	 * @Title: getReconnectResponse
	 * @Description: TODO(客户端连接数越界)
	 * @param @param isClientOut
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getClientOutResponse(boolean isClientOut) {

		TransferResponse response = new TransferResponse();

		State state = new State();
		state.setCode(TransferHost.ResponseCode.SUCCESS);
		state.setMsg(TransferHost.ResponseMsg.ok);
		if (isClientOut) {
			state.setCode(TransferHost.ResponseCode.CLIENT_OUT_SIZE);
		}
		response.setState(state);
		return JsonUtil.objectToJson(response);
	}

	/**
	 * 解析协议参数
	 * 
	 * @param httpGetParams
	 * @return
	 */
	public TransferRequest parseHttpGetParams(String httpGetParams) {
		if (!TextUtils.isEmpty(httpGetParams)) {
			String getPramas = getHttpGetPramas(httpGetParams);
			TransferRequest transferRequest = null;
			try {
				String[] splits = getPramas.split("&");
				transferRequest = new TransferRequest();
				transferRequest.setRawClientGetPramas(httpGetParams);
				for (int i = 0; i < splits.length; i++) {
					String[] params = splits[i].split("=");
					if ("nickName".equals(params[0])) {
						transferRequest.setNickName(params[1]);

					} else if ("mac".equals(params[0])) {
						transferRequest.setMac(params[1]);

					} else if ("imei".equals(params[0])) {
						transferRequest.setImei(params[1]);

					} else if ("imsi".equals(params[0])) {
						transferRequest.setImsi(params[1]);

					} else if ("deviceModel".equals(params[0])) {
						transferRequest.setDeviceModel(params[1]);

					} else if ("osVersion".equals(params[0])) {
						transferRequest.setOsVersion(Integer.valueOf(params[1]));

					} else if ("osVersionName".equals(params[0])) {
						transferRequest.setOsVersionName(params[1]);

					} else if ("currentProgress".equals(params[0])) {
						transferRequest.setCurrentProgress(Integer.valueOf(params[1]));

					} else if ("connectType".equals(params[0])) {
						transferRequest.setConnectType(Integer.valueOf(params[1]));
					} else if ("headPortrait".equals(params[0])) {
						transferRequest.setHeadPortrait(Integer.valueOf(params[1]));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return transferRequest;
		}
		return null;
	}

	private String getHttpGetPramas(String httpGetParams) {
		String result = "";
		if (!TextUtils.isEmpty(httpGetParams)) {
			int index1 = httpGetParams.indexOf("?");
			int index2 = httpGetParams.indexOf(" HTTP");
			result = httpGetParams.substring(index1 + 1, index2);
		}
		return result;
	}

	public String getZappSourceDir() {
		String path = null;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
			if (info != null)
				path = info.applicationInfo.publicSourceDir;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}

	public Transfers getTransfer(String rawPath) {
		Transfers transfers = new Transfers();
		return transfers;
	}

	/**
	 * 
	 * @Title: saveFromActivity
	 * @Description: TODO(保存初始页)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void saveFromActivity(Context context, String activityKey, String activityName) {
		SharedPrefsUtil.putValue(context, activityKey, activityName);

	}

	public static String getFromActivity(Context context, String activityKey) {
		return SharedPrefsUtil.getValue(context, activityKey, "");

	}

	/**
	 * 
	 * @Title: saveExtraUriData
	 * @Description: TODO()
	 * @param @param isMultiple
	 * @param @param intent 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public boolean putExtraUriData(Intent intent) {
		if (intent != null) {
			String action = intent.getAction();
			String type = intent.getType();

			String filePath = "";

			if (TextUtils.isEmpty(type) || TextUtils.isEmpty(action)) {
				return false;
			}
			// 如果是多文件
			if (Intent.ACTION_SEND_MULTIPLE.equals(action)) {
				ZeroDataResourceHelper.getInstance(context).prefers.edit()
						.putString(ZeroDataConstant.ZERO_DATA_RESOURCE_KEY, "").commit();
				ArrayList<Uri> urisList = null;
				if (type.startsWith("audio") || type.startsWith("image") || type.startsWith("video")
						|| type.startsWith("application")) {
					urisList = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
				} else if (type.startsWith("text")) {
					urisList = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
				}
				if (urisList != null && urisList.size() > 0) {
					for (int i = 0; i < urisList.size(); i++) {
						String path = "";
						if (urisList.get(i) != null && urisList.get(i).toString().startsWith("file://")) {
							path = urisList.get(i).getPath();
						} else {
							path = ResourceManage.getInstance(context).getFilePath(context, urisList.get(i));
						}

						if (i == (urisList.size() - 1))
							filePath = filePath + path;
						else
							filePath = filePath + path + ",@,";
					}

				} else {
					return false;
				}
				// 如果是单文件
			} else if (Intent.ACTION_SEND.equals(action)) {
				ZeroDataResourceHelper.getInstance(context).prefers.edit()
						.putString(ZeroDataConstant.ZERO_DATA_RESOURCE_KEY, "").commit();
				Uri uri = null;
				if ((type.startsWith("audio") || type.startsWith("image") || type.startsWith("video") || type
						.startsWith("application"))) {

					uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
				} else if (type.startsWith("text")) {
					uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
				}
				if (uri != null) {
					if (uri.toString().startsWith("file://")) {
						filePath = uri.getPath();
					} else {
						filePath = ResourceManage.getInstance(context).getFilePath(context, uri);
					}
				}
			}
			// 获取到数据
			if (!TextUtils.isEmpty(filePath)) {
				this.context.getSharedPreferences(ZeroDataConstant.ZERO_DATA_RESOURCE_PREFES, Context.MODE_PRIVATE)
						.edit().putString(ZeroDataConstant.ZERO_DATA_RESOURCE_KEY, filePath).commit();
				return true;
			}

		}
		return false;
	}

	/**
	 * 
	 * @Title: getZerodataHeadPortrait
	 * @Description: TODO
	 * @param @param context
	 * @param @param headPortraitNum
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	public static int getZerodataRandomHeadPortraitValue() {

		Random random = new Random(System.currentTimeMillis());
		int headPortrait = random.nextInt(headPortraitArray.length);
		return headPortrait;
	}

	/**
	 * 
	 * @Title: getZerodataHeadPortrait
	 * @Description: TODO
	 * @param @param context
	 * @param @param headPortraitNum
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	public static int getSelfZerodataHeadPortrait(Context context) {
		int headPortrait = SharedPrefsUtil.getValue(context, ZERODATA_HEAD_PORTRAIT_KEY, -1);
		if (headPortrait == -1) {
			headPortrait = getZerodataRandomHeadPortraitValue();
			saveZerodataHeadPortrait(context, headPortrait);
		}
		return getZerodataHeadPortraitResource(headPortrait);
	}

	/**
	 * 
	 * @Title: getSelfZerodataHeadBgPortrait
	 * @Description: TODO
	 * @param @param context
	 * @param @return
	 * @return int
	 */
	public static int getSelfZerodataHeadBgPortrait(Context context) {
		int headPortrait = SharedPrefsUtil.getValue(context, ZERODATA_HEAD_PORTRAIT_KEY, -1);
		if (headPortrait == -1) {
			headPortrait = getZerodataRandomHeadPortraitValue();
			saveZerodataHeadPortrait(context, headPortrait);
		}
		return getZerodataHeadBgPortraitResource(headPortrait);
	}

	/**
	 * 
	 * @Title: getSelfZerodataHeadPortraitValue
	 * @Description: TODO
	 * @param @param context
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	public static int getSelfZerodataHeadPortraitValue(Context context) {
		int headPortrait = SharedPrefsUtil.getValue(context, ZERODATA_HEAD_PORTRAIT_KEY, -1);
		if (headPortrait == -1) {
			headPortrait = getZerodataRandomHeadPortraitValue();
			saveZerodataHeadPortrait(context, headPortrait);
		}
		return headPortrait;
	}

	/**
	 * 
	 * @Title: getZerodataHeadPortrait
	 * @Description: TODO
	 * @param @param context
	 * @param @param headPortrait
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	public static int getZerodataHeadPortrait(Context context, int headPortrait) {
		if (headPortrait < 0 || headPortrait > headPortraitArray.length + 1) {
			headPortrait = 0;
		}
		return getZerodataHeadPortraitResource(headPortrait);
	}

	/**
	 * 
	 * @Title: saveZerodataHeadPortrait
	 * @Description: TODO
	 * @param @param context
	 * @param @param headPortraitNum
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	public static void saveZerodataHeadPortrait(Context context, int headPortraitNum) {
		SharedPrefsUtil.putValue(context, ZERODATA_HEAD_PORTRAIT_KEY, headPortraitNum);
	}

	/**
	 * 
	 * @Title: getZerodataHeadPortraitResource
	 * @Description: TODO
	 * @param @param headPortraitNum
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	public static int getZerodataHeadPortraitResource(int headPortraitNum) {
		int headPortrait = 0;
		switch (headPortraitNum) {
		case 0:
			headPortrait = headPortraitArray[0];
			break;
		case 1:
			headPortrait = headPortraitArray[1];
			break;
		case 2:
			headPortrait = headPortraitArray[2];
			break;
		case 3:
			headPortrait = headPortraitArray[3];
			break;
		case 4:
			headPortrait = headPortraitArray[4];
			break;
		case 5:
			headPortrait = headPortraitArray[5];
			break;
		case 6:
			headPortrait = headPortraitArray[6];
			break;
		case 7:
			headPortrait = headPortraitArray[7];
			break;
		default:
			headPortrait = headPortraitArray[0];
			break;
		}
		return headPortrait;
	}

	/**
	 * 
	 * @Title: getZerodataHeadPortraitResource
	 * @Description: TODO
	 * @param @param headPortraitNum
	 * @param @return
	 * @return int
	 */
	public static int getZerodataHeadBgPortraitResource(int headPortraitNum) {
		int headBgPortrait = 0;
		switch (headPortraitNum) {
		case 0:
			headBgPortrait = headBgArray[0];
			break;
		case 1:
			headBgPortrait = headBgArray[1];
			break;
		case 2:
			headBgPortrait = headBgArray[2];
			break;
		case 3:
			headBgPortrait = headBgArray[3];
			break;
		case 4:
			headBgPortrait = headBgArray[4];
			break;
		case 5:
			headBgPortrait = headBgArray[5];
			break;
		case 6:
			headBgPortrait = headBgArray[6];
			break;
		case 7:
			headBgPortrait = headBgArray[7];
			break;
		default:
			headBgPortrait = headBgArray[0];
			break;
		}
		return headBgPortrait;
	}

	/**
	 * 
	 * @Title: getHeadPortraitValueFromSSID
	 * @Description: TODO
	 * @param @param SSID
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	public static int getHeadPortraitValueFromSSID(String SSID) {
		if (!TextUtils.isEmpty(SSID)) {
			try {
				String HeadPortraitValue = SSID.substring(ZeroDataConnectHelper.SERVER_HOTSPOT_HEADER.length() + 1,
						ZeroDataConnectHelper.SERVER_HOTSPOT_HEADER.length() + 2);
				return Integer.valueOf(HeadPortraitValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
}
