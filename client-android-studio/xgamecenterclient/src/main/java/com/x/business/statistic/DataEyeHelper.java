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

import android.os.Environment;
import android.util.Log;

import com.x.business.statistic.StatisticConstan.FileType;
import com.x.publics.http.Host;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.Constan.MediaType;

/**
* @ClassName: DataEyeManager
* @Description: DataEye数据统计

* @date 2014-5-29 下午2:39:27
* 
*/

public class DataEyeHelper {

	private static DataEyeHelper dataEyeManager;
	private static final String dataEyeDebugFileName = "dataeye_log.bin";
	private static final String tag = "dataeye";
	public static String saveLogFileName = "dataeye-log.txt";
	public static String saveLogPath = StorageUtils.FILE_ROOT +File.separator+saveLogFileName;
	private static FileWriter fWriter = null;
	
	public static final String dataeye_enable = "dataeye_enable.bin";
	public static final String dataeye_disable = "dataeye_disable.bin";
	
	
	public static final int  DATAMINE_DEBUG= 0 ;
	public static final int DATAMINE_OFF = 1 ;
	public static final int DATAMINE_NORMAL = 2 ;
	public static final int DATAMINE_NORMAL_DEBUG = 3 ;
	
	public static DataEyeHelper getInstance() {
		if (dataEyeManager == null) {
			dataEyeManager = new DataEyeHelper();
		}
		return dataEyeManager;
	}

	public DataEyeHelper() {

	}

	/**
	 * 
	* @Title: isDataEyeInvoke 
	* @Description: TODO(是否调用dataeye) 
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	public static boolean isDataEyeInvoke()
	{
		
		int status = getDataEyeSatus() ;
		
		if(status == DATAMINE_OFF || status ==DATAMINE_NORMAL_DEBUG)
		{
			return false ;
		}
		return true ;
	}
	
	/**
	 * 
	* @Title: isDataEyeInvoke 
	* @Description: TODO(是否调用dataeye) 
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	public static String getDataeyeHint()
	{
		int status = getDataEyeSatus() ;
		
		if(status == DATAMINE_DEBUG)
		{
			return "[datamine:debug]" ;
		}else if(status == DATAMINE_OFF)
		{
			return "[datamine:off]" ;
		}
		return "" ;
	}
	
	/**
	 * 
	* @Title: getDataEyeSatus 
	* @Description: TODO(获取调用状态) 
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	public static  int getDataEyeSatus()
	{
		if(isCheckFileExist(dataeye_enable))
		{
			return DATAMINE_DEBUG ;
		}else if(isCheckFileExist(dataeye_disable))
		{
			return DATAMINE_OFF ;
		}else{
			
		if(Host.getZappDomain() != Host.DOMAIN_SERIAL_NUMBER_SINGAPORE) //|| AccountManager.getInstance().getSDKDomain() != Host.DOMAIN_SERIAL_NUMBER_SINGAPORE)
		{
			return DATAMINE_NORMAL_DEBUG ;
		}
		}
		return DATAMINE_NORMAL ;
	}
	
	public static void showDataEyeLog(String tag ,String msg)
	{
		if(DATAMINE_DEBUG == getDataEyeSatus())
		{
			Log.d(tag, msg) ;
			saveLog(saveLogPath,msg) ;
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
	
	/**
	  * 保存日志
	  * @param tag
	  * @param msg
	  */
	public static void saveLog(String saveLogPath, String msg) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date(System.currentTimeMillis()));
			try {
				if (fWriter == null) {
					fWriter = new FileWriter(saveLogPath, true);
				}
				if (msg != null) {
					fWriter.write("[" + time + "]" + msg + "\r\n");
					fWriter.flush();
				}
			} catch (IOException e) {
			} catch (Exception e) {
			}
	}
}
