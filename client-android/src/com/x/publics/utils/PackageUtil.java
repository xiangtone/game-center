/**   
* @Title: PackageUtil.java
* @Package com.x.publics.utils
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-10-10 上午10:14:00
* @version V1.0   
*/

package com.x.publics.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.text.TextUtils;

import com.x.business.settings.SettingModel;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.utils.ShellUtils.CommandResult;

/**
* @ClassName: PackageUtil
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-10-10 上午10:14:00
* 
*/

public class PackageUtil {

	private static  ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5); 
	
	
	/**
	 * 安装apk
	 * 
	 * @param context
	 * @param fullPath
	 */
	public static void installApk(Context context, String fullPath) {
		fullPath = fullPath.replaceAll(".tmp", "");
		LogUtil.getLogger().d("install apk path > " + fullPath);
		if (fullPath.startsWith("/data/data/")) {
			try {
				Runtime.getRuntime().exec("chmod 644 " + fullPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		SettingModel settingModel = Utils.getSettingModel(context);
		if (settingModel.isSilentInstall() && RootUtil.isAppRootPermission()) {
			sillentInstall(context, fullPath);
		} else {
			normalInstall(context, fullPath);
		}

	}
	
	public static void normalInstall(Context context , String fullPath){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(new File(fullPath)), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}
	
	public static void sillentInstall(Context context , String path){
		command(context, path, SilentUtil.INSTALL_APP);
	}
	
	private static void commandUnInstall(Context context , String packageName){
		command(context, packageName, SilentUtil.UNINSTALL_APP);
	}

	private  static void command(Context context, final String param , String command) {
		String minePkgName = "";// 本程序包名
		try {
			minePkgName = context.getPackageName();
			String classpath = context.getPackageManager().getPackageInfo(minePkgName, 0).applicationInfo.publicSourceDir;
			System.out.println("classPath: " + classpath);
			final String[] progArray = new String[] {
					RootUtil.ROOT_SU,
					"-c",
					"export CLASSPATH=" + classpath
							+ " && export LD_LIBRARY_PATH=/vendor/lib:/system/lib && exec app_process /data/app "
							+ minePkgName + ".publics.utils.SilentUtil " // /system/bin
							+ param +" "+command };
			
			fixedThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					sendInstallingBroadcast(param);
					CommandResult result = ShellUtils.exeCmd(progArray, true);
					if(!TextUtils.isEmpty(result.errorMsg) || (!TextUtils.isEmpty(result.successMsg) && (result.successMsg.contains("permission denied")||result.successMsg.contains("_FAILED_")))){
						sendInstallResultBroadcast(param, SilentUtil.INSTALL_FAILED_OTHER);
					}
				}
			});
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendInstallingBroadcast(String path){
		Intent updateIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
		updateIntent.putExtra(MyIntents.TYPE, MyIntents.Types.INSTALLING);
		updateIntent.putExtra(MyIntents.LOCAL_PATH,path);
		BroadcastManager.sendBroadcast(updateIntent);
		DownloadEntityManager.getInstance().updateDownloadInstallingStatus(path);
	}
	
	public static void sendInstallResultBroadcast(String path , int result ){
		Intent updateIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
		updateIntent.putExtra(MyIntents.TYPE, MyIntents.Types.INSTALL_RESULT);
		updateIntent.putExtra(MyIntents.LOCAL_PATH,path);
		updateIntent.putExtra(MyIntents.INSTALL_RESULT_CODE,result);
		BroadcastManager.sendBroadcast(updateIntent);
	}

	/**
	 * 卸载apk
	 * 
	 * @param context
	 * @param PkgName
	 */
	public static void unstallApk(Context context, String PkgName) {
		Uri packageURI = Uri.parse("package:" + PkgName);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		context.startActivity(uninstallIntent);
	}

	/**
	 * 批量卸载apk
	 * 
	 * @param context
	 * @param PkgName
	 */
	public static void unstallApks(Context context, ArrayList<String> pkgNameList) {
		for (String pkgName : pkgNameList) {
			Uri packageURI = Uri.parse("package:" + pkgName);
			Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
			context.startActivity(uninstallIntent);
		}
	}

}
