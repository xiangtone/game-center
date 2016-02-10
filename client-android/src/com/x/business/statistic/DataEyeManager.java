/**   
* @Title: DataEyeManager.java
* @Package com.x.business.statistic
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-5-29 下午2:39:27
* @version V1.0   
*/

package com.x.business.statistic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.dataeye.DCZApp;
import com.x.business.statistic.StatisticConstan.FileType;
import com.x.publics.model.DownloadBean;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.Constan.MediaType;

/**
* @ClassName: DataEyeManager
* @Description: DataEye数据统计

* @date 2014-5-29 下午2:39:27
* 
*/

public class DataEyeManager {

	private static DataEyeManager dataEyeManager;
	private static final String tag = "dataeye";
	
	public static DataEyeManager getInstance() {
		if (dataEyeManager == null) {
			dataEyeManager = new DataEyeManager();
		}
		return dataEyeManager;
	}

	public DataEyeManager() {

	}

	
	
	public void onResume(Context context) {
		try {
			context.getClass().getName() ;
			if(DataEyeHelper.isDataEyeInvoke())
			{
				DCZApp.onResume(context);
				String logMsg = "[api=onResume ActivityName="+context.getClass().getSimpleName()+"]" ;
				DataEyeHelper.showDataEyeLog(tag,logMsg) ;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onPause(Context context) {
		try {
			
			if(DataEyeHelper.isDataEyeInvoke())
			{
			DCZApp.onPause(context);
			String logMsg = "[api=onPause ActivityName="+context.getClass().getSimpleName()+"]" ;
			DataEyeHelper.showDataEyeLog(tag,logMsg) ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 
	* @Title: source 
	* @Description: TODO 
	* @param @param srcName 来源名称
	* @param @param fileType 文件类型（1:应用，2:游戏, 3:铃声，4:图片）
	* @param @param fileName 文件名 (仅对srcName为Relative时有效)
	* @param @param fileSize 文件大小 (单位统一为K)
	* @param @param appVer 应用版本号（仅对应用&游戏有效）
	* @param @param keyword 搜索关键词（仅搜索时有效）
	* @param @param result    搜索结果
	* @return void    
	*/

	public void source(int srcName, int fileType, String fileName, long fileSize, String appVer, String keyword,
			boolean result) {
		try {
			if(DataEyeHelper.isDataEyeInvoke())
			{
				DCZApp.source(srcName, fileType, fileName, fileSize, appVer, keyword, result);
				String logMsg = "[api=source,srcName="+srcName+",fileType="+fileType+",fileName="+fileName+",fileSize="+fileSize+",appVer="+appVer+",keyword="+keyword+",result="+result+"]" ;
				DataEyeHelper.showDataEyeLog(tag,logMsg) ;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 
	* @Title: view 
	* @Description: TODO 
	* @param @param fileType 文件类型（1:应用，2:游戏, 3:铃声，4:图片）
	* @param @param category  应用分类（仅对应用有效）(
	* @param @param fileName 文件名
	* @param @param fileSize 文件大小 (单位统一为K)
	* @param @param appVer 应用版本号（仅对应用&游戏有效）
	* @param @param keyword  搜索关键词（仅搜索时有效）
	* @return void    
	*/

	public void view(int fileType, int category, String fileName, long fileSize, String appVer, String keyword) {
		try {
			if(DataEyeHelper.isDataEyeInvoke())
			{
				DCZApp.view(fileType, category, fileName, fileSize, appVer, keyword);
				String logMsg = "[api=view,fileType="+fileType+",category="+category+",fileName="+fileName+",fileSize="+fileSize+",appVer="+appVer+",keyword="+keyword+"]" ;
				DataEyeHelper.showDataEyeLog(tag,logMsg) ;	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 
	* @Title: download 
	* @Description: 应用下载统计 
	* @param @param downloadBean    
	* @return void    
	*/

	public void download(DownloadBean downloadBean) {
		try {
			if(DataEyeHelper.isDataEyeInvoke())
			{
				DCZApp.download(downloadBean.getFileType(), downloadBean.getCategoryId(), downloadBean.getName(), downloadBean.getFileSize(),
						downloadBean.getVersionName());
				
				String logMsg = "[api=download,fileType="+downloadBean.getFileType()+",category="+downloadBean.getCategoryId()+",fileName="+downloadBean.getName()+",fileSize="+downloadBean.getFileSize()+",appVer="+downloadBean.getVersionName()+"]" ;
				DataEyeHelper.showDataEyeLog(tag,logMsg) ;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 
	* @Title: downloadStart 
	* @Description: 用户开始下载某应用时下载
	* @param @param fileType 文件类型
	* @param @param category 文件名
	* @param @param appName
	* @param @param appSize
	* @param @param appVer    
	* @return void    
	*/

	public void downloadStart(DownloadBean downloadBean) {
		try {
			if(DataEyeHelper.isDataEyeInvoke())
			{
				DCZApp.downloadStart(downloadBean.getFileType(), downloadBean.getCategoryId(), downloadBean.getName(),
						downloadBean.getFileSize(), downloadBean.getVersionName());
				String logMsg = "[api=downloadStart,fileType="+downloadBean.getFileType()+",category="+downloadBean.getCategoryId()+",fileName="+downloadBean.getName()+",fileSize="+downloadBean.getFileSize()+",appVer="+downloadBean.getVersionName()+"]" ;
				DataEyeHelper.showDataEyeLog(tag,logMsg) ;	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void downloadInterrupt(DownloadBean downloadBean, String reason) {
		try {
			if(DataEyeHelper.isDataEyeInvoke())
			{
				DCZApp.downloadInterrupt(downloadBean.getFileType(), downloadBean.getCategoryId(), downloadBean.getName(),
						downloadBean.getFileSize(), downloadBean.getVersionName(), reason);
				String logMsg = "[api=downloadInterrupt,fileType="+downloadBean.getFileType()+",category="+downloadBean.getCategoryId()+",fileName="+downloadBean.getName()+",fileSize="+downloadBean.getFileSize()+",appVer="+downloadBean.getVersionName()+",reason="+reason+"]" ;
				DataEyeHelper.showDataEyeLog(tag,logMsg) ;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void downloadReopen(DownloadBean downloadBean, String reason) {
		try {
			if(DataEyeHelper.isDataEyeInvoke())
			{
				DCZApp.downloadReopen(downloadBean.getFileType(), downloadBean.getCategoryId(), downloadBean.getName(),
						downloadBean.getFileSize(), downloadBean.getVersionName(), reason);
				String logMsg = "[api=downloadReopen,fileType="+downloadBean.getFileType()+",category="+downloadBean.getCategoryId()+",fileName="+downloadBean.getName()+",fileSize="+downloadBean.getFileSize()+",appVer="+downloadBean.getVersionName()+",reason="+reason+"]" ;
				DataEyeHelper.showDataEyeLog(tag,logMsg) ;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void downloadFail(DownloadBean downloadBean, String reason) {
		try {
			if(DataEyeHelper.isDataEyeInvoke())
			{
				DCZApp.downloadFail(downloadBean.getFileType(), downloadBean.getCategoryId(), downloadBean.getName(),
						downloadBean.getFileSize(), downloadBean.getVersionName(), reason);
				String logMsg = "[api=downloadFail,fileType="+downloadBean.getFileType()+",category="+downloadBean.getCategoryId()+",fileName="+downloadBean.getName()+",fileSize="+downloadBean.getFileSize()+",appVer="+downloadBean.getVersionName()+",reason="+reason+"]" ;
				DataEyeHelper.showDataEyeLog(tag,logMsg) ;	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void downloadSuccess(DownloadBean downloadBean) {
		try {
			if(DataEyeHelper.isDataEyeInvoke())
			{
				DCZApp.downloadSuccess(downloadBean.getFileType(), downloadBean.getCategoryId(), downloadBean.getName(),
						downloadBean.getFileSize(), downloadBean.getVersionName());
				String logMsg = "[api=downloadSuccess,fileType="+downloadBean.getFileType()+",category="+downloadBean.getCategoryId()+",fileName="+downloadBean.getName()+",fileSize="+downloadBean.getFileSize()+",appVer="+downloadBean.getVersionName()+"]" ;
				DataEyeHelper.showDataEyeLog(tag,logMsg) ;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 
	* @Title: module 
	* @Description:功能统计
	* @param @param moduleName
	* @param @param start    true开始 false结束
	* @return void    
	*/ 
	
	public void module(String moduleName, boolean start) {
		if (start) {
			moduleStart(moduleName);
		} else {
			moduleStop(moduleName);
		}
	}

	/** 
	* @Title: moduleStart 
	* @Description: 被统计功能开始使用时调用 
	* @param @param moduleName 功能名
	* @return void    
	*/

	public void moduleStart(String moduleName) {
		try {
			if(DataEyeHelper.isDataEyeInvoke())
			{
				DCZApp.moduleStart(moduleName);
				String logMsg = "[api=moduleStart,moduleName="+moduleName+"]" ;
				DataEyeHelper.showDataEyeLog(tag,logMsg) ;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 
	* @Title: moduleStop 
	* @Description: 被统计功能开始结束时调用
	* @param @param moduleName 功能名
	* @return void    
	*/

	public void moduleStop(String moduleName) {
		try {
			if(DataEyeHelper.isDataEyeInvoke())
			{
				DCZApp.moduleEnd(moduleName);
				String logMsg = "[api=moduleStop,moduleName="+moduleName+"]" ;
				DataEyeHelper.showDataEyeLog(tag,logMsg) ;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 
	* @Title: exit
	* @Description: 退出
	* @param @param 
	* @return void    
	*/

	public void exit() {
		try {
			if(DataEyeHelper.isDataEyeInvoke())
			{
				DCZApp.exit() ;
				String logMsg = "[api=exit]" ;
				DataEyeHelper.showDataEyeLog(tag,logMsg) ;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isCheckFileExist(String sdkDebugFileName) {
		try {
			File checkDebugFile = new File(Environment
					.getExternalStorageDirectory().getPath()
					+ File.separator
					+ sdkDebugFileName);
			if (checkDebugFile.exists()) {
				return true;
			}
		} catch (Exception e) {
				e.printStackTrace() ;
			return false;
		}
		return false;
	}
	
	public int getFileType(String mediaType){
		int fileType = 0;
		if (MediaType.APP.equals(mediaType)) {
			fileType = FileType.APPS;
		} else if (MediaType.GAME.equals(mediaType)) {
			fileType = FileType.GAME;
		} else if (MediaType.MUSIC.equals(mediaType)) {
			fileType = FileType.RINGTONES;
		} else if (MediaType.IMAGE.equals(mediaType)) {
			fileType = FileType.WALLPAPER;
		}
		return fileType;
	}
	
	public static String getSearchKey(String searchKey)
	{
		if(!TextUtils.isEmpty(searchKey))
		{
			if(searchKey.length()>30)
			{
				return searchKey.substring(0, 29) ;
			}
			return searchKey ;
		}
		
		return "" ;
	}
	
}
