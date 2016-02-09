package com.x.publics.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.AsyncTask;

import com.x.publics.download.error.FileAlreadyExistException;
import com.x.publics.download.error.NoMemoryException;
import com.x.publics.download.error.ServerException;
import com.x.publics.model.DownloadBean;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.Constan.MediaType;

/**
* @ClassName: DownloadTask
* @Description: 下载类 

* @date 2014-1-10 上午09:38:30
* 
*/

public class DownloadTask extends AsyncTask<Void, Integer, Long> {

	public final static int TIME_OUT = 30000;
	private final static int BUFFER_SIZE = 1024 * 8;

	private static final String TAG = "DownloadTask";
	private static final boolean DEBUG = true;
	public static final String TEMP_SUFFIX = ".download";

	public static final int ERROR_NETWORK_ERROR = 0;
	public static final int ERROR_IO_ERROR = 1;
	public static final int ERROR_MEMORYNOENOUGH_ERROR = 2;

	public static final int TASK_PAUSE = 100;
	public static final int TASK_DOWNLOADING = 101;
	public static final int TASK_WAITING = 102;
	public static final int TASK_FINISH = 103;
	public static final int TASK_CONNECTING = 104;
	public static final int TASK_LAUNCH = 105;
	public static final int TASK_INSTALLING = 106;
	public DownloadBean downloadBean;
	private URL URL;
	private File file;
	private File tempFile;
	//	private String url;
	private RandomAccessFile outputStream;
	private DownloadTaskListener listener;
	private Context context;

	private long downloadSize;
	private long previousFileSize;
	private long totalSize;
	private long downloadPercent;
	private long networkSpeed;
	private long previousTime;
	private long totalTime;
	private Throwable error = null;
	private boolean interrupt = false;

	private final class ProgressReportingRandomAccessFile extends RandomAccessFile {
		private int progress = 0;
		private long currentTime, lastTime;

		public ProgressReportingRandomAccessFile(File file, String mode) throws FileNotFoundException {
			super(file, mode);
		}

		@Override
		public void write(byte[] buffer, int offset, int count) throws IOException {

			super.write(buffer, offset, count);
			progress += count;
			currentTime = System.currentTimeMillis();
			if (currentTime - lastTime >= 1000 || progress + previousFileSize == totalSize) {
				publishProgress(progress);
				lastTime = currentTime;
			}
		}
	}

	public DownloadTask(Context context, DownloadBean downloadBean) throws MalformedURLException {

		this(context, downloadBean, null);
	}

	public DownloadTask(Context context, DownloadBean downloadBean, DownloadTaskListener listener)
			throws MalformedURLException {
		this.downloadBean = downloadBean;
		this.listener = listener;
		this.context = context;
		this.URL = new URL(getUrl());
		String fileName = NetworkUtils.getFileNameFromDownloadBean(downloadBean);
		String parentPath = StorageUtils.FILE_DOWNLOAD_APK_PATH;
		if (MediaType.APP.equals(downloadBean.getMediaType()) || MediaType.GAME.equals(downloadBean.getMediaType())) {
			parentPath = StorageUtils.FILE_DOWNLOAD_APK_PATH;
		} else if (MediaType.IMAGE.equals(downloadBean.getMediaType())) {
			parentPath = StorageUtils.FILE_DOWNLOAD_WALLPAPER_PATH;
		} else if (MediaType.MUSIC.equals(downloadBean.getMediaType())) {
			parentPath = StorageUtils.FILE_DOWNLOAD_MUSIC_PATH;
		} else if (MediaType.THEME.equals(downloadBean.getMediaType())) {
			parentPath = StorageUtils.FILE_DOWNLOAD_SKIN_PATH;
		}
		this.file = new File(parentPath, fileName);
		this.downloadBean.setLocalPath(this.file.getPath());
		this.tempFile = new File(parentPath, fileName + TEMP_SUFFIX);

		previousFileSize = downloadBean.getCurrentBytes();
		totalSize = downloadBean.getTotalBytes();
		if (totalSize != 0)
			downloadPercent = (downloadSize + previousFileSize) * 100 / totalSize;
	}

	public String getUrl() {
		return downloadBean.getUrl();
	}

	public boolean isInterrupt() {

		return interrupt;
	}

	public long getDownloadPercent() {

		return downloadPercent;
	}

	public long getDownloadSize() {

		return downloadSize + previousFileSize;
	}

	public long getTotalSize() {

		return totalSize;
	}

	public long getDownloadSpeed() {

		return this.networkSpeed;
	}

	public long getTotalTime() {

		return this.totalTime;
	}

	public DownloadTaskListener getListener() {

		return this.listener;
	}

	@Override
	protected void onPreExecute() {
		previousTime = System.currentTimeMillis();
		if (listener != null)
			listener.preDownload(this);
		LogUtil.getLogger().d("beging download :" + downloadBean.getName());
	}

	@Override
	protected Long doInBackground(Void... params) {

		long result = -1;
		try {
			result = download();
		} catch (NetworkErrorException e) {
			error = e;
		} catch (NoMemoryException e) {
			error = e;
		} catch (SocketException e) {
			error = e;
		} catch (IOException e) {
			error = e;
		} catch (IllegalArgumentException e) {
			error = e;
		} catch (ServerException e) {
			error = e;
		} catch (Exception e) {
			error = e;
		} finally {
			if (client != null) {
				client.close();
			}
		}

		return result;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {

		if (progress.length > 1) {
			totalSize = progress[1];
			if (totalSize == -1) {
				if (listener != null)
					listener.errorDownload(this, error);
			} else {

			}
		} else {
			if (interrupt)
				return;
			totalTime = System.currentTimeMillis() - previousTime;
			downloadSize = progress[0];
			downloadBean.setCurrentBytes(downloadSize + previousFileSize);
			downloadPercent = (downloadSize + previousFileSize) * 100 / totalSize;
			networkSpeed = downloadSize / totalTime;
			if (listener != null)
				listener.updateProcess(this);
		}
	}

	@Override
	protected void onPostExecute(Long result) {

		if (result == -1 || error != null) {
			if (DEBUG && error != null) {
				LogUtil.getLogger().e("Download failed." + error.getMessage());
				error.printStackTrace();
			}

			if (listener != null) {
				if (error instanceof FileAlreadyExistException)
					listener.finishDownload(this);
				else {
					if (interrupt)
						return;
					listener.errorDownload(this, error);
				}
			}
			return;
		}
		if (interrupt)
			return;
		// finish download
		if (tempFile.exists())
			tempFile.renameTo(file);
		if (listener != null)
			listener.finishDownload(this);
	}

	@Override
	public void onCancelled() {

		super.onCancelled();
		interrupt = true;
		if (listener != null)
			listener.pauseDownload(this);
		LogUtil.getLogger().d("pause download task :" + downloadBean.getName());
	}

	private AndroidHttpClient client;
	private HttpGet httpGet;
	private HttpResponse response;

	private long download() throws NetworkErrorException, IOException, NoMemoryException, ServerException {

		LogUtil.getLogger().v("url:" + getUrl() + ",totalSize: " + totalSize);

		/*
		 * check net work
		 */
		if (!NetworkUtils.isNetworkAvailable(context)) {
			throw new NetworkErrorException("Network blocked.");
		}

		/*
		 * check file length
		 */
		client = AndroidHttpClient.newInstance("DownloadTask");
		String url = URLEncoder.encode(getUrl(), "utf-8").replaceAll("\\+", "%20");
		url = url.replaceAll("%3A", ":").replaceAll("%2F", "/");
		//		url = "https://codeload.github.com/tanhuanpei/android-async-http/zip/master";
		httpGet = new HttpGet(url);
		response = client.execute(httpGet);
		if (response.getStatusLine().getStatusCode() != 200)
			throw new ServerException("");
		totalSize = response.getEntity().getContentLength();
		downloadBean.setTotalBytes(totalSize);
		if (file.exists() && totalSize == file.length()) {
			LogUtil.getLogger().v("Output file already exists. Skipping download.");
			tempFile.delete();
			return 1;
			//			throw new FileAlreadyExistException("Output file already exists. Skipping download.");
		} else if (tempFile.exists()) {
			httpGet.addHeader("Range", "bytes=" + tempFile.length() + "-");
			previousFileSize = tempFile.length();

			client.close();
			client = AndroidHttpClient.newInstance("DownloadTask");
			response = client.execute(httpGet);

			LogUtil.getLogger().v("File is not complete, download now.");
			LogUtil.getLogger().v("File length:" + tempFile.length() + " totalSize:" + totalSize);
		}

		/*
		 * check memory
		 */
		long storage = StorageUtils.getAvailableStorage();
		LogUtil.getLogger().v("storage:" + storage + " totalSize:" + totalSize);

		if (totalSize - tempFile.length() > storage) {
			throw new NoMemoryException("SD card no memory.");
		}

		/*
		 * start download
		 */
		outputStream = new ProgressReportingRandomAccessFile(tempFile, "rw");

		publishProgress(0, (int) totalSize);
		InputStream input = response.getEntity().getContent();
		int bytesCopied = copy(input, outputStream);

		if ((previousFileSize + bytesCopied) != totalSize && totalSize != -1 && !interrupt) {
			throw new IOException("Download incomplete: " + bytesCopied + " != " + totalSize);
		}
		if (isInterrupt())
			LogUtil.getLogger().v("Download " + downloadBean.getName() + " interrupt");
		else
			LogUtil.getLogger().v("Download " + downloadBean.getName() + " successfully.");

		return bytesCopied;

	}

	public int copy(InputStream input, RandomAccessFile out) throws NetworkErrorException, IOException {

		if (input == null || out == null) {
			return -1;
		}

		byte[] buffer = new byte[BUFFER_SIZE];

		BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);

		int count = 0, n = 0;
		long errorBlockTimePreviousTime = -1, expireTime = 0;

		try {

			out.seek(out.length());

			while (!interrupt) {
				n = in.read(buffer, 0, BUFFER_SIZE);
				if (n == -1) {
					break;
				}
				out.write(buffer, 0, n);
				count += n;
				/*
				 * check network
				 */
				if (!NetworkUtils.isNetworkAvailable(context)) {
					throw new NetworkErrorException("Network blocked.");
				}

				if (networkSpeed == 0) {
					if (errorBlockTimePreviousTime > 0) {
						expireTime = System.currentTimeMillis() - errorBlockTimePreviousTime;
						if (expireTime > TIME_OUT) {
							throw new ConnectTimeoutException("connection time out.");
						}
					} else {
						errorBlockTimePreviousTime = System.currentTimeMillis();
					}
				} else {
					expireTime = 0;
					errorBlockTimePreviousTime = -1;
				}
			}
		} finally {
			client.close(); // must close client first
			client = null;
			out.close();
			in.close();
			input.close();
		}
		return count;

	}

}
