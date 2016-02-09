/**
 * 文件名	: LogUtil.java
 * 作者		: Tank
 * 创建日期	: 2013-6-21
 * 版权    	:  
 * 描述    	: 
 * 修改历史	: 
 */
package com.x.publics.utils;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 
 * 
 */
public class LogUtil {
	private final static String tag = "LogUtil";
	public static int logLevel = Log.VERBOSE;
	private static LogUtil logger = new LogUtil();;
	private static boolean debug = true;
	public static boolean saveLog = false;
	/** 日志默认存储文件名 **/
	public static String saveLogFileName = "zapp-log.txt";
	/** 日志默认存储路径  **/
	public static String saveLogPath = StorageUtils.FILE_ROOT + File.separator + saveLogFileName;
	
	private static FileWriter fWriter = null;
	public static final String zapp_log_enable = "zapp_log.bin";
	public static final String zapp_log_file_enable = "zapp_log_file.bin";

	public static LogUtil getLogger() {
		saveLog = Utils.isCheckFileExist(zapp_log_file_enable);
		debug = Utils.isCheckFileExist(zapp_log_enable);
		return logger;
	}

	private LogUtil() {

	}

	private String getFunctionName() {

		StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts == null) {
			return null;
		}

		for (StackTraceElement st : sts) {

			if (st.isNativeMethod()) {
				continue;
			}
			if (st.getClassName().equals(Thread.class.getName())) {
				continue;
			}

			if (st.getClassName().equals(this.getClass().getName())) {
				continue;
			}
			return "[ " + Thread.currentThread().getName() + ": " + st.getFileName() + ":" + st.getLineNumber() + " ]";
		}
		return null;
	}

	public void i(Object str) {
		if (!debug)
			return;
		if (logLevel <= Log.INFO) {
			String msg = "";
			String name = getFunctionName();
			if (name != null) {
				msg = name + " - " + str;
			} else {
				msg = str.toString();
			}
			Log.i(tag, msg);
			saveLog(msg);
		}
	}

	public void v(Object str) {
		if (!debug)
			return;
		if (logLevel <= Log.VERBOSE) {
			String msg = "";
			String name = getFunctionName();
			if (name != null) {
				msg = name + " - " + str;
			} else {
				msg = str.toString();
			}
			Log.v(tag, msg);
			saveLog(msg);
		}
	}

	public void w(Object str) {
		if (!debug)
			return;
		if (logLevel <= Log.WARN) {
			String msg = "";
			String name = getFunctionName();
			if (name != null) {
				msg = name + " - " + str;
			} else {
				msg = str.toString();
			}
			Log.w(tag, msg);
			saveLog(msg);
		}
	}

	public void e(Object str) {
		if (!debug)
			return;
		if (logLevel <= Log.ERROR) {
			String msg = "";
			String name = getFunctionName();
			if (name != null) {
				msg = name + " - " + str;
			} else {
				msg = str.toString();
			}
			Log.e(tag, msg);
			saveLog(msg);
		}
	}

	public void e(String tag, Exception ex) {
		if (!debug)
			return;
		if (logLevel <= Log.ERROR) {
			Log.e(tag, "error", ex);
			saveLog(ex.toString());

		}
	}

	public void d(String tag, Object str) {
		if (!debug)
			return;
		if (logLevel <= Log.DEBUG) {
			String msg = "";
			String name = getFunctionName();
			if (name != null) {
				msg = name + " - " + str;
			} else {
				msg = str.toString();
			}
			Log.d(tag, msg);
			saveLog(msg);
		}
	}

	public void i(String tag, Object str) {
		if (!debug)
			return;
		if (logLevel <= Log.INFO) {
			String msg = "";
			String name = getFunctionName();
			if (name != null) {
				msg = name + " - " + str;
			} else {
				msg = str.toString();
			}
			Log.i(tag, msg);
			saveLog(msg);
		}
	}

	public void v(String tag, Object str) {
		if (!debug)
			return;
		if (logLevel <= Log.VERBOSE) {
			String msg = "";
			String name = getFunctionName();
			if (name != null) {
				msg = name + " - " + str;
			} else {
				msg = str.toString();
			}
			Log.v(tag, msg);
			saveLog(msg);
		}
	}

	public void w(String tag, Object str) {
		if (!debug)
			return;
		if (logLevel <= Log.WARN) {
			String msg = "";
			String name = getFunctionName();
			if (name != null) {
				msg = name + " - " + str;
			} else {
				msg = str.toString();
			}
			Log.w(tag, msg);
			saveLog(msg);
		}
	}

	public void e(String tag, Object str) {
		if (!debug)
			return;
		if (logLevel <= Log.ERROR) {
			String msg = "";
			String name = getFunctionName();
			if (name != null) {
				msg = name + " - " + str;
			} else {
				msg = str.toString();
			}
			Log.e(tag, msg);
			saveLog(msg);
		}
	}

	public void e(Exception ex) {
		if (!debug)
			return;
		if (logLevel <= Log.ERROR) {
			Log.e(tag, "error", ex);
			saveLog(ex.toString());

		}
	}

	public void d(Object str) {
		if (!debug)
			return;
		if (logLevel <= Log.DEBUG) {
			String msg = "";
			String name = getFunctionName();
			if (name != null) {
				msg = name + " - " + str;
			} else {
				msg = str.toString();
			}
			Log.d(tag, msg);
			saveLog(msg);
		}
	}

	/**
	  * 保存日志
	  * @param tag
	  * @param msg
	  */
	public static void saveLog(String msg) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date(System.currentTimeMillis()));
		if (saveLog) {
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

}
