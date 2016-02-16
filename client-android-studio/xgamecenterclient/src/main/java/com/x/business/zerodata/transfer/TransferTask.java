package com.x.business.zerodata.transfer;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.AsyncTask;

import com.x.publics.download.AndroidHttpClient;
import com.x.publics.download.error.FileAlreadyExistException;
import com.x.publics.download.error.NoMemoryException;
import com.x.publics.download.error.ServerException;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.StorageUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;

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

public class TransferTask extends AsyncTask<Void, Long, Long> {

	public final static int TIME_OUT = 30000;
	private final static int BUFFER_SIZE = 1024 * 8;

	private static final String TAG = "TransferTask";
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
	public TransferBean transferBean;
	private URL URL;
	private File file;
	private File tempFile;
	//	private String url;
	private RandomAccessFile outputStream;
	private TransferTaskListener listener;
	private Context context;

	private String fileName;
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
		private Long progress = 0L;
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

	public TransferTask(Context context, TransferBean transferBean) throws MalformedURLException {

		this(context, transferBean, null);
	}

	public TransferTask(Context context, TransferBean transferBean, TransferTaskListener listener)
			throws MalformedURLException {
		this.transferBean = transferBean;
		this.listener = listener;
		this.context = context;
		this.URL = new URL(this.transferBean.getFileUrl());
		fileName = transferBean.getExAttribute();
		this.transferBean.setFileName(fileName);
		this.tempFile = new File(StorageUtils.FILE_ZERO_SHARE_PATH, fileName + TEMP_SUFFIX);

		//		previousFileSize = downloadBean.getCurrentBytes();
		totalSize = transferBean.getFileSize();
		if (totalSize != 0)
			downloadPercent = (downloadSize + previousFileSize) * 100 / totalSize;
	}

	public String getUrl() {

		return this.transferBean.getFileUrl();
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

	public File getTempFile() {

		return this.tempFile;
	}

	public TransferTaskListener getListener() {

		return this.listener;
	}

	@Override
	protected void onPreExecute() {
		this.file = new File(TransferManager.getInstance().getSavePath(fileName));
		this.transferBean.setFileSavePath(this.file.getPath());
		previousTime = System.currentTimeMillis();
		if (listener != null)
			listener.preDownload(this);
		LogUtil.getLogger().d("beging download :" + transferBean.getFileName());
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
	protected void onProgressUpdate(Long... progress) {

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
			//			downloadBean.setCurrentBytes(downloadSize + previousFileSize);
			LogUtil.getLogger().e(
					"aaaaaaaaaa",
					"downloadPercent=" + downloadPercent + ",downloadSize=" + downloadSize + ",previousFileSize="
							+ previousFileSize + ",totalSize=" + totalSize);
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
			}

			if (listener != null) {
				if (error instanceof FileAlreadyExistException)
					listener.finishDownload(this);
				else
					listener.errorDownload(this, error);
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
		LogUtil.getLogger().d("pause download task :" + transferBean.getFileName());
	}

	private AndroidHttpClient client;
	private HttpGet httpGet;
	private HttpResponse response;

	private long download() throws NetworkErrorException, IOException, NoMemoryException, ServerException {

		LogUtil.getLogger().v("url:" + getUrl() + ",totalSize: " + totalSize);

		//		/*
		//		 * check net work
		//		 */
				if (!NetworkUtils.isNetworkAvailable(context) || !NetworkUtils.getNetworkInfo(context).equals(NetworkUtils.NETWORK_TYPE_WIFI)) {
					throw new NetworkErrorException("Network blocked.");
				}

		/*
		 * check file length
		 */
		//		client = AndroidHttpClient.newInstance("TransferTask");
		String url = URLEncoder.encode(this.transferBean.getFileUrl(), "utf-8").replaceAll("\\+", "%20");
		url = url.replaceAll("%3A", ":").replaceAll("%2F", "/");
		httpGet = new HttpGet(url);
		//		response = client.execute(httpGet);
		//		if (response.getStatusLine().getStatusCode() != 200)
		//			throw new ServerException("");
		//		totalSize = response.getEntity().getContentLength();
		//		downloadBean.setTotalBytes(totalSize);
		if (file.exists() && totalSize == file.length()) {
			//			file.delete();
		}
		if (tempFile.exists()) {
			httpGet.addHeader("Range", "bytes=" + tempFile.length() + "-");
			previousFileSize = tempFile.length();

			//			client.close();
			//			response = client.execute(httpGet);
		}
		client = AndroidHttpClient.newInstance("TransferTask");
		response = client.execute(httpGet);
		if (response.getStatusLine().getStatusCode() != 200)
			throw new ServerException("");

		totalSize = response.getEntity().getContentLength();
		LogUtil.getLogger().v("File is not complete, download now.");
		LogUtil.getLogger().v("File length:" + tempFile.length() + " totalSize:" + totalSize);
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
		publishProgress(0L, totalSize);
		InputStream input = response.getEntity().getContent();
		int bytesCopied = copy(input, outputStream);

		if ((previousFileSize + bytesCopied) != totalSize && totalSize != -1 && !interrupt) {
			tempFile.delete();
			throw new IOException("Download incomplete: " + bytesCopied + " != " + totalSize);
		}
		if (isInterrupt())
			LogUtil.getLogger().v("Download " + transferBean.getFileName() + " interrupt");
		else
			LogUtil.getLogger().v("Download " + transferBean.getFileName() + " successfully.");

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
