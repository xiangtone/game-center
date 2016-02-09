/**   
* @Title: StatisticManager.java
* @Package com.mas.amineappstore.business.statistic
* @Description: TODO 

* @date 2014-4-8 下午02:39:45
* @version V1.0   
*/

package com.x.business.statistic;

import org.json.JSONObject;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;

import com.x.business.account.AccountManager;
import com.x.business.update.UpdateManage;
import com.x.db.dao.UpdateApp;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.AccordDownloadRequest;
import com.x.publics.http.model.MachineActivateRequest;
import com.x.publics.http.model.MachineActivateResponse;
import com.x.publics.http.model.MasPlay;
import com.x.publics.http.model.MasUser;
import com.x.publics.http.model.SkinDownloadRequest;
import com.x.publics.http.model.AccordDownloadRequest.AccordDownloadData;
import com.x.publics.http.model.MachineActivateRequest.ClientMachine;
import com.x.publics.http.model.MachineActivateRequest.Data;
import com.x.publics.http.model.SkinDownloadRequest.SkinData;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.DownloadBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.SystemInfo;
import com.x.publics.utils.Utils;
import com.x.publics.utils.Constan.MediaType;

/**
* @ClassName: StatisticManager
* @Description: 数据统计 

* @date 2014-4-8 下午02:39:45
* 
*/

public class StatisticManager {

	public static String saveLogFileName = "activate.txt";
	public static String saveLogPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
			+ saveLogFileName;

	private static StatisticManager statisticManager;

	public static StatisticManager getInstance() {
		if (statisticManager == null) {
			statisticManager = new StatisticManager();
		}
		return statisticManager;
	}

	public StatisticManager() {

	}

	/** 
	* @Title: statisticDownloads 
	* @Description: 下载统计 
	* @param @param downloadBean
	* @param @param context     
	* @return void    
	*/

	public void statisticDownloads(final DownloadBean downloadBean, final Context context) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						LogUtil.getLogger().d(response.toString());
					}
				};
				ErrorListener myErrorListener = new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
					}
				};

				AccordDownloadData data = new AccordDownloadData();
				AccordDownloadRequest request = null;

				// 获取文件类型，进行匹配
				String mediaType = downloadBean.getMediaType();

				if (MediaType.APP.equals(mediaType) || MediaType.GAME.equals(mediaType)) {
					// 应用
					request = new AccordDownloadRequest(Constan.Rc.ACCORD_APP_DOWNLOAD);
					data.setApkId(downloadBean.getResourceId());
					data.setAppId(downloadBean.getAppId());
					data.setAppName(downloadBean.getName());
					data.setClientId(AccountManager.getInstance().getClientId(context));
					data.setPackageName(downloadBean.getPackageName());
					data.setVersionCode(downloadBean.getVersionCode());
					data.setVersionName(downloadBean.getVersionName());
					data.setCt(SharedPrefsUtil.getValue(context, "ct_" + downloadBean.getResourceId(), 0));
                    data.setDownOrUpdate(getDownOrUpdate(context, downloadBean.getPackageName()));
					
				} else if (MediaType.IMAGE.equals(mediaType)) {
					// 壁纸
					request = new AccordDownloadRequest(Constan.Rc.ACCORD_WALLPAPER_DOWNLOAD);
					data.setClientId(AccountManager.getInstance().getClientId(context));
					data.setImageId(downloadBean.getResourceId());
					data.setImageName(downloadBean.getName());

				} else if (MediaType.MUSIC.equals(mediaType)) {
					// 音乐
					request = new AccordDownloadRequest(Constan.Rc.ACCORD_MUSIC_DOWNLOAD);
					data.setClientId(AccountManager.getInstance().getClientId(context));
					data.setMusicId(downloadBean.getResourceId());
					data.setMusicName(downloadBean.getName());
				} else if(MediaType.THEME.equals(mediaType)) {
					//皮肤
					SkinDownloadRequest skinDownloadRequest = new SkinDownloadRequest();
					SkinData skinData = new SkinData();
					skinData.setClientId(AccountManager.getInstance().getClientId(context));
					skinData.setDownOrUpdate(1);
					skinData.setImei(SystemInfo.getIMEI(context));
					skinData.setPackageName(downloadBean.getPackageName());
					skinData.setSkinId(downloadBean.getResourceId());
					skinData.setSkinName(downloadBean.getName());
					skinData.setVersionCode(downloadBean.getVersionCode());
					skinData.setVersionName(downloadBean.getVersionName());
					skinDownloadRequest.setData(skinData);
					DataFetcher.getInstance().skinDownloadStatistic(skinDownloadRequest, myResponseListent, myErrorListener);
					return;
				}

				MasPlay masPlay = new MasPlay();
				try {
					PackageManager pm = context.getPackageManager();
					PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);

					masPlay.setMasPackageName(context.getPackageName());
					masPlay.setMasVersionCode(packageInfo.versionCode);
					masPlay.setMasVersionName(packageInfo.versionName);

				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}

				MasUser masUser = new MasUser();
				masUser.setUserId(AccountManager.getInstance().getUserId(context));
				masUser.setUserName(AccountManager.getInstance().getUserName(context));

				request.setData(data);
				request.setMasPlay(masPlay);
				request.setMasUser(masUser);

				DataFetcher.getInstance().accordDownloadData(request, myResponseListent, myErrorListener);
			}
		}).start();

	}

	/** 
	* @Title: activateDevice 
	* @Description: 激活统计 
	* @param @param context    
	* @return void    
	*/

	public void activateDevice(final Context context) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				setAlreadyActivateDevice(context);
				Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						LogUtil.getLogger().d("BootReceiver", response.toString());
						MachineActivateResponse activateResponse = (MachineActivateResponse) JsonUtil.jsonToBean(
								response, MachineActivateResponse.class);
						if (activateResponse != null && activateResponse.state != null
								&& activateResponse.state.code == 200) {
							setActivateServerSuccess(context);
							LogUtil.getLogger().d("BootReceiver", "setActivateServerSuccess");
						}
					}
				};

				ErrorListener myErrorListener = new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
						LogUtil.getLogger().d("BootReceiver", "post activateDevice data fail");
					}
				};

				MachineActivateRequest machineActivateRequest = new MachineActivateRequest();
				ClientMachine clientMachine = new ClientMachine();
				clientMachine.setCreateTime(getDeviceActivateTime(context));
				clientMachine.setDeviceModel(SystemInfo.getDeviceModel());
				clientMachine.setDeviceType(1);
				clientMachine.setDeviceVendor(SystemInfo.getDeviceManufacturer());
				clientMachine.setImei(SystemInfo.getIMEI(context));
				clientMachine.setImsi(SystemInfo.getIMSI(context));
				clientMachine.setMac(SystemInfo.getMAC(context));
				clientMachine.setNetType(SystemInfo.getNetworkInfo(context));
				clientMachine.setOsAddtional("");
				clientMachine.setOsVersion("" + SystemInfo.getAndroidSDKINT());
				clientMachine.setOsVersionName(SystemInfo.getAndroidRelease());
				clientMachine.setPhone(SystemInfo.getPhoneNumber(context));
				clientMachine.setScreenDensity(SystemInfo.getDensity(context));
				clientMachine.setScreenHeight(SystemInfo.getScreenHeight(context));
				clientMachine.setScreenWidth(SystemInfo.getScreenWidth(context));
				clientMachine.setServiceSupplier("");
				clientMachine.setSmsc("");

				Data data = new Data();
				data.setAppPackageName(Utils.getPackageName(context));
				data.setAppVersionCode(Utils.getVersionCode(context));
				data.setAppVersionName(Utils.getVersionName(context));

				machineActivateRequest.setClientMachine(clientMachine);
				machineActivateRequest.setData(data);

				DataFetcher.getInstance().activateDevice(machineActivateRequest, myResponseListent, myErrorListener);
				LogUtil.getLogger().d("BootReceiver", "DataFetcher [activateDevice]");
			}
		}).start();
	}

	/** 
	* @Description: 重新上传激活数据
	*/

	public void reActivateDevice(Context context) {
		if (!isActivateServerSuccess(context) && isAlreadyActivateDevice(context)) {
			activateDevice(context);
			LogUtil.getLogger().d("BootReceiver", "reActivateDevice");
		}
	}

	/** 
	* @Description: 是否已经激活（未成功上传服务器）
	*/

	public boolean isAlreadyActivateDevice(Context context) {
		return SharedPrefsUtil.getValue(context, "isAlreadyActivateDevice", false);
	}

	/** 
	* @Description: 已经激活（未成功上传服务器）
	*/
	public void setAlreadyActivateDevice(Context context) {
		SharedPrefsUtil.putValue(context, "isAlreadyActivateDevice", true);
	}

	/** 
	* @Description: 是否已经激活（是否成功上传服务器） 
	*/

	public boolean isActivateServerSuccess(Context context) {
		return SharedPrefsUtil.getValue(context, "isActivateServerSuccess", false);
	}

	/** 
	* @Description: 激活成功（成功上传服务器） 
	*/

	public void setActivateServerSuccess(Context context) {
		SharedPrefsUtil.putValue(context, "isActivateServerSuccess", true);
	}

	/**
	 * @Description: 是否预装
	 */
	public boolean iszAppInSystem(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			String packageName = Utils.getPackageName(context);
			PackageInfo info = pm.getPackageInfo(packageName, 0);
			return (info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @Description: 第一个定时是否成功
	 */
	public boolean isFirstAlarmSuccess(Context context) {
		return SharedPrefsUtil.getValue(context, "isFirstAlarmSuccess", false);
	}

	/**
	 * @Description: 第一个定时成功
	 */
	public void setFirstAlarmSuccess(Context context) {
		SharedPrefsUtil.putValue(context, "isFirstAlarmSuccess", true);
	}

	/**
	 * @Description: 保存设备激活时间
	 */
	public void setDeviceActivateTime(Context context) {
		long time = System.currentTimeMillis();
		SharedPrefsUtil.putValue(context, "deviceActivateTime", time);
	}

	/**
	 * @Description: 获取设备激活时间
	 */
	public long getDeviceActivateTime(Context context) {
		return SharedPrefsUtil.getValue(context, "deviceActivateTime", 0l);
	}
	
	/**
	 * @Description: 获取下载或更新值
	 */
	private int getDownOrUpdate(Context context,String packageName) {
		UpdateApp  app = UpdateManage.getInstance(context).getUpdateAppByPackageName(packageName);
		return app == null ? 1 : 2;
	}
}
