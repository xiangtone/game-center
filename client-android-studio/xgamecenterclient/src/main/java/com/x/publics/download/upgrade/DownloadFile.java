package com.x.publics.download.upgrade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.message.BasicHeader;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

public class DownloadFile {

	private Context mContext;
	public final static String DEFAULT_SAVE_FILE_DIR = "zApp/Download/Upgrade";
	private final static String TEMP_FILE_END = ".temp";
	private final static int defaultBlockSize = 5 * 1024;// 默认分段下载长度每次最多下载180K
	private int blockSize = defaultBlockSize;
	private long downloadFileSize = -1;// 下载文件大小
	private boolean isInterrup;// 是否终止下载任务
	private DownloadProgress downloadProgress;// 下载进度信息类
	private int type;// 下载失败的类型
	private String reason;// 下载失败的原因
	private String mUrl;// 原始下载路径
	private boolean downloadPhone;// 是否下载在手机内部存储
	private String sdcardPath;// SD卡的路径
	private String mSavePath;
	private HttpRequest httpRequest;
	public String savePath;

	/**
	 * 构造方法
	 * 
	 * @param context
	 */
	public DownloadFile(Context context) {
		if (context != null) {
			mContext = context.getApplicationContext();
		}
	}

	/**
	 * 设置分段下载长度
	 * 
	 * @param size
	 */
	public void setBlockSize(int size) {
		blockSize = size;
	}

	/**
	 * 返回下载文件的大小
	 * 
	 * @return
	 */
	public long getDownloadFileSize() {
		return downloadFileSize;
	}

	/**
	 * 停止下载
	 */
	public void stopDownload() {
		isInterrup = true;
		type = DOWNLOAD_STATE_TYPE_INTERRUPT;
		reason = DOWNLOAD_RESULT_REASON_CANCEL_FAIL;
	}

	/**
	 * 设置下载进度及状态回调接口
	 * 
	 * @param p
	 */
	public void setDownloadProgress(DownloadProgress progress) {
		downloadProgress = progress;
	}

	/**
	 * 返回下载失败类型
	 * 
	 * @return
	 */
	public int getDownloadFailType() {
		return type;
	}

	/**
	 * 返回下载失败原因
	 * 
	 * @return
	 */
	public String getDownloadFailReason() {
		return reason;
	}

	/**
	 * 返回最终文件保存路径
	 * 
	 * @return
	 */
	public String getSavePath() {
		return mSavePath;
	}

	public static final int DOWNLOAD_STATE_TYPE_NO_NETWORK = 1;
	public static final int DOWNLOAD_STATE_TYPE_HAS_DOWNLOADED = 2;
	public static final int DOWNLOAD_STATE_TYPE_CONNECT_FAIL = 3;
	public static final int DOWNLOAD_STATE_TYPE_INTERRUPT = 4;

	public static final String DOWNLOAD_RESULT_REASON_NO_NETWORK = "网络连接失败！";
	public static final String DOWNLOAD_RESULT_REASON_HAS_DOWNLOADED = "已经成功下载过了！";
	public static final String DOWNLOAD_RESULT_REASON_CONNECT_FAIL = "服务器连接失败！";
	public static final String DOWNLOAD_RESULT_REASON_CANCEL_FAIL = "下载已取消！";

	private byte[] cacheByte;

	private byte[] getCacheByte(int length) {
		if (cacheByte == null) {
			cacheByte = new byte[length];
		}
		return cacheByte;
	}

	/**
	 * 分块下载
	 */
	private boolean downloadBlockFile(String url,
			FileOutputStream fileOutputStream, long totalSize, long startPos,
			long blockSize) {
		if (!SystemInfo.isNetworkAvailable(mContext))// 当前无网络
		{
			type = DOWNLOAD_STATE_TYPE_NO_NETWORK;
			reason = DOWNLOAD_RESULT_REASON_NO_NETWORK;
			return false;
		}
		String sProperty = "bytes=" + startPos + "-";
		if (blockSize > 0) {
			sProperty = sProperty + (startPos + blockSize - 1);
		}
		ArrayList<BasicHeader> headerList = new ArrayList<BasicHeader>();
		headerList
				.add(new BasicHeader(
						"Accept",
						"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*"));// "*/*"
		headerList.add(new BasicHeader("Connection", "Keep-Alive"));
		headerList.add(new BasicHeader("RANGE", sProperty));
		HttpResponse response = null;
		if (!isInterrup) {
			if (httpRequest == null) {
				httpRequest = new HttpRequest(mContext);
				httpRequest.setConnectionTimeout(20000);
				httpRequest.setSoTimeout(20000);
				httpRequest.setSoTimeout(20000);
				httpRequest.setRedirecting(true);
			}
			response = httpRequest.getUrlHeaderList(url, headerList);
		}
		if (response != null && response.getStatusLine() != null) {
			int stateCode = response.getStatusLine().getStatusCode();
			if (stateCode == HttpStatus.SC_OK
					|| stateCode == HttpStatus.SC_PARTIAL_CONTENT) {
				try {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						InputStream is = entity.getContent();
						if (is != null) {
							byte[] buf = getCacheByte(20480);// 20*1024
							int ch = -1;
							int finishSize = 0;
							while ((ch = is.read(buf)) != -1 && !isInterrup) {
								if (!isInterrup) {
									fileOutputStream.write(buf, 0, ch);
									fileOutputStream.flush();
									finishSize += ch;
								} else {
									return false;
								}
							}
							if (isInterrup) {
								return false;
							}
							if (blockSize == finishSize) {
								return true;
							}
						}
					}
				} catch (IOException e) {
				} catch (Exception e) {
				}
			}
		}
		return false;
	}

	public boolean downloadFile(String url, String fileDir, String fileName,
			DownloadProgress progress) {
		boolean result = false;
		isInterrup = false;
		mUrl = url;
		if (progress != null) {
			downloadProgress = progress;
		}
		if (downloadProgress != null) {
			downloadProgress.downloadReady(url);
		}
		if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(fileDir)
				&& !TextUtils.isEmpty(fileName)) {
			if (!isInterrup) {
				result = download(url, fileDir, fileName);
			}
		} else {

		}
		if (downloadProgress != null) {
			if (result) {
				downloadProgress.downloadSucess(mUrl, mSavePath, downloadPhone,
						downloadFileSize, type, reason);
			} else {
				downloadProgress.downloadFail(mUrl, mSavePath, downloadPhone,
						type, reason);
			}
		}
		if (httpRequest != null) {
			httpRequest.unInit();
			httpRequest = null;
			cacheByte = null;
		}
		return result;
	}

	private boolean download(String url, String fileDir, String fileName) {
		boolean result = false;
		File file = new File(fileDir + File.separator + fileName);
		savePath = file.getPath();
		String saveName = fileName;
		long startPos = 0, block = blockSize;
		long downloadSize = 0;
		long totalSize = 0;
		if (!SystemInfo.isNetworkAvailable(mContext))// 当前无网络
		{
			if (file.exists()) {
				totalSize = file.length();
				if (totalSize > 0) {
					downloadFileSize = totalSize;
					type = DOWNLOAD_STATE_TYPE_HAS_DOWNLOADED;
					reason = DOWNLOAD_RESULT_REASON_HAS_DOWNLOADED;
					return true;
				}
			}
			type = DOWNLOAD_STATE_TYPE_NO_NETWORK;
			reason = DOWNLOAD_RESULT_REASON_NO_NETWORK;
			return false;
		}

		ArrayList<BasicHeader> headerList = new ArrayList<BasicHeader>();
		headerList.add(new BasicHeader("Accept", "*/*"));// "*/*"
		headerList.add(new BasicHeader("Connection", "Keep-Alive"));// "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*"
		headerList.add(new BasicHeader("RANGE", "bytes=0-9"));
		if (httpRequest == null) {
			httpRequest = new HttpRequest(mContext);
			httpRequest.setConnectionTimeout(10000);
			httpRequest.setSoTimeout(20000);
		}
		httpRequest.setRedirecting(false);
		HttpResponse response = httpRequest.getUrlHeaderList(url, headerList);

		if (response != null && response.getStatusLine() != null) {
			int stateCode = response.getStatusLine().getStatusCode();
			if (stateCode == HttpStatus.SC_OK
					|| stateCode == HttpStatus.SC_PARTIAL_CONTENT) {

			} else if (stateCode == HttpStatus.SC_MOVED_TEMPORARILY
					|| stateCode == HttpStatus.SC_MOVED_PERMANENTLY)// 跳转
			{

			} else {
				response = null;
			}
		}

		try {
			if (response != null && response.getStatusLine() != null) {
				int stateCode = response.getStatusLine().getStatusCode();
				if (stateCode == HttpStatus.SC_OK
						|| stateCode == HttpStatus.SC_PARTIAL_CONTENT) {

					Header[] headerArr = response.getAllHeaders();
					if (headerArr != null && headerArr.length > 0) {
						Header header;
						for (int i = 0; i < headerArr.length; i++) {
							header = headerArr[i];
							if (header != null) {
								// TODO..
							}
						}
					}

					long contentLength = 0;

					Header headerContentRange = response
							.getFirstHeader("Content-Range");
					if (headerContentRange != null) {
						String contentRange = headerContentRange.getValue();// contentRange=bytes
																			// 0-184319/9057633
						int index = contentRange.indexOf("/");
						if (index >= 0) {
							String totalSizeStr = contentRange
									.substring(index + 1);
							try {
								totalSize = Long.parseLong(totalSizeStr);
							} catch (NumberFormatException e) {

							}
						}

						if (totalSize == 0) {
							response = null;
						}
					} else {
						Header headerContentLength = response
								.getFirstHeader("Content-Length");
						if (headerContentLength != null) {
							String contentLengthStr = headerContentLength
									.getValue();
							try {
								contentLength = Long
										.parseLong(contentLengthStr);
								totalSize = contentLength;
							} catch (NumberFormatException e) {

							}
						}
					}

					if (totalSize > 0) {
						downloadFileSize = totalSize;
						downloadPhone = savePath.startsWith("/data/data/");
						if (file.exists()) {
							if (file.length() == totalSize) {
								type = DOWNLOAD_STATE_TYPE_HAS_DOWNLOADED;
								reason = DOWNLOAD_RESULT_REASON_HAS_DOWNLOADED;
								return true;
							} else// 已经下载的文件已经被更新替换
							{
								file.delete();
							}
						}

						File tempfile = new File(savePath + TEMP_FILE_END);
						if (tempfile.exists())// 下载过程中的临时文件已经存在
						{
							downloadSize = tempfile.length();
							if (downloadSize == totalSize) {
								type = DOWNLOAD_STATE_TYPE_HAS_DOWNLOADED;
								reason = DOWNLOAD_RESULT_REASON_HAS_DOWNLOADED;
								tempfile.renameTo(file);
								return true;
							} else if (downloadSize > totalSize)// 已经下载的文件已经被更新替换
							{
								tempfile.delete();
								downloadSize = 0;
							}
						}

						File fileDirs = new File(fileDir);
						if (!fileDirs.exists()) {
							String state = Environment
									.getExternalStorageState();
							boolean isMkdirs = fileDirs.mkdirs();
						}
						if (!fileDirs.exists()
								&& Tools.getDirAvailableSize() > (totalSize - downloadSize))// 目录存在并且剩余空间足够
						{

						} else {
							downloadSize = 0;
							sdcardPath = Tools.getSdcardPath(totalSize);
							if (sdcardPath == null) {
								downloadPhone = true;
								file = mContext.getFileStreamPath(saveName);
								mSavePath = file.getAbsolutePath();
								if (file.exists()) {
									if (file.length() == totalSize) {
										type = DOWNLOAD_STATE_TYPE_HAS_DOWNLOADED;
										reason = DOWNLOAD_RESULT_REASON_HAS_DOWNLOADED;
										return true;
									} else// 已经下载的文件已经被更新替换
									{
										file.delete();
									}
								}
								tempfile = mContext.getFileStreamPath(saveName
										+ TEMP_FILE_END);
								if (tempfile.exists())// 下载过程中的临时文件已经存在
								{
									downloadSize = tempfile.length();
									if (downloadSize == totalSize) {
										type = DOWNLOAD_STATE_TYPE_HAS_DOWNLOADED;
										reason = DOWNLOAD_RESULT_REASON_HAS_DOWNLOADED;
										tempfile.renameTo(file);
										return true;
									} else if (downloadSize > totalSize)// 已经下载的文件已经被更新替换
									{
										tempfile.delete();
										downloadSize = 0;
									}
								}
							} else {
								downloadPhone = false;
								file = new File(sdcardPath + "/"
										+ DEFAULT_SAVE_FILE_DIR + "/"
										+ saveName);
								mSavePath = file.getAbsolutePath();
								if (file.exists()) {
									if (file.length() == totalSize) {
										type = DOWNLOAD_STATE_TYPE_HAS_DOWNLOADED;
										reason = DOWNLOAD_RESULT_REASON_HAS_DOWNLOADED;
										return true;
									} else// 已经下载的文件已经被更新替换
									{
										file.delete();
									}
								}
								tempfile = new File(sdcardPath + "/"
										+ DEFAULT_SAVE_FILE_DIR + "/"
										+ saveName + TEMP_FILE_END);
								if (tempfile.exists())// 下载过程中的临时文件已经存在
								{
									downloadSize = tempfile.length();
									if (downloadSize == totalSize) {
										type = DOWNLOAD_STATE_TYPE_HAS_DOWNLOADED;
										reason = DOWNLOAD_RESULT_REASON_HAS_DOWNLOADED;
										tempfile.renameTo(file);
										return true;
									} else if (downloadSize > totalSize)// 已经下载的文件已经被更新替换
									{
										tempfile.delete();
										downloadSize = 0;
									}
								}
							}
						}

						if (contentLength == 0)// 支持断点续传
						{
							startPos = downloadSize;// 断点续传的关键代码
						} else {
							startPos = 0;
						}
						FileOutputStream fileOutputStream = null;
						if (!downloadPhone) {
							fileOutputStream = new FileOutputStream(tempfile,
									downloadSize > 0 && contentLength == 0);// new
																			// File(sdcardPath+FILE_SAVE_DIR);//Environment.getExternalStorageDirectory()+FILE_SAVE_DIR
						} else {
							int mode = Context.MODE_WORLD_READABLE
									| Context.MODE_WORLD_WRITEABLE;
							if (downloadSize > 0 && contentLength == 0) {
								mode |= Context.MODE_APPEND;
							}
							fileOutputStream = mContext.openFileOutput(saveName
									+ TEMP_FILE_END, mode);// new
															// FileOutputStream(tempfile);
						}

						if (downloadProgress != null) {
							downloadProgress.downloadStart(mUrl, mSavePath,
									downloadPhone, startPos, downloadFileSize);
						}

						httpRequest.setRedirecting(true);
						result = true;
						while (startPos < totalSize) {
							if (isInterrup) {
								result = false;
								break;
							}
							if (contentLength == 0) {
								if ((totalSize - startPos) >= blockSize) {
									block = blockSize;
								} else {
									block = totalSize - startPos;
								}
							} else {
								block = contentLength;
							}
							if (downloadBlockFile(url, fileOutputStream,
									totalSize, startPos, block)) {
								startPos += block;
								if (downloadProgress != null) {
									downloadProgress.downloading(mUrl,
											mSavePath, downloadPhone, startPos,
											totalSize);
								}
							} else {
								result = false;
								break;
							}
							if (isInterrup) {
								result = false;
								break;
							}
						}
						if (result && tempfile.length() > 0) {
							tempfile.renameTo(file);
							mSavePath = file.getAbsolutePath();
						} else {
							result = false;
						}
						if (isInterrup) {
							return false;
						}
						if (fileOutputStream != null) {
							fileOutputStream.close();
							fileOutputStream = null;
						}
					}
				} else {
					response = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response == null) {
			type = DOWNLOAD_STATE_TYPE_CONNECT_FAIL;
			reason = DOWNLOAD_RESULT_REASON_CONNECT_FAIL;
			Log.e("download", reason);
		}
		return result;
	}

	public boolean downloadFile(String url, String fileDir, String fileName) {
		return downloadFile(url, fileDir, fileName, null);
	}

}
