package com.hykj.gamecenter.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hykj.gamecenter.net.logic.IImageDownloadListener;
import com.hykj.gamecenter.utils.FileUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

public class ImageDownloadSession extends AsyncHttpSession {

	private HttpGet mHttpGet = null;
	private final String TAG = "ImageDownloadSession";

	private IImageDownloadListener mListener;

	public ImageDownloadSession(String url) {
		super(url);
	}

	public void registerCallBack(IImageDownloadListener listener) {
		mListener = listener;
		mCallBack = listener;
	}

	public void release() {
		if (mHttpGet != null)
			mHttpGet.abort();
		shutDown();
	}

	private void handleResponseData(HttpEntity httpEntity) {
		InputStream in = null;
		ByteArrayOutputStream baos = null;
		byte[] data = null;
		byte[] readBuffer = new byte[4096];
		int totalLen = (int) httpEntity.getContentLength();
		int nReceiveLen = 0;

		try {
			in = httpEntity.getContent();
			baos = new ByteArrayOutputStream();

			while (nReceiveLen < totalLen) {
				int nReadLen = in.read(readBuffer);
				if (nReadLen == -1)
					break;

				nReceiveLen += nReadLen;
				baos.write(readBuffer, 0, nReadLen);

			}
			data = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
				if (baos != null) {
					baos.close();
					baos = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mListener.onDownloadIconFinish(mUrl, data);
	}

	private boolean findInLocalCache() {
		String fileName = FileUtils.getImageFileNameByUrl(mUrl);
		File file = new File(fileName);
		if (file.exists()) {
			// FileInputStream fis;
			try {
				// fis = new FileInputStream(file);
				Bitmap bitmap = BitmapFactory.decodeFile(fileName); // .decodeStream(fis);
//				Bitmap bitmap = ImageUtils.getFileBitmap(file);
				if (bitmap != null) {
					mListener.onGetIcon(mUrl, bitmap);
					return true;
				} else {
					return false;
				}
				// fis.close();
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	protected void doRun() {
		if (findInLocalCache()) {
			return;
		}

		while (mHasRetry < HttpSessionConstant.MAX_RETRY.POST_DATA) {
			try {
				HttpClient httpClient = getHttpClient();

				mHttpGet = new HttpGet(mUrl);

				HttpContext localContext = new BasicHttpContext();
				mResponse = httpClient.execute(mHttpGet, localContext);

				int statusCode = mResponse.getStatusLine().getStatusCode();
				if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_PARTIAL_CONTENT) {

					HttpEntity httpEntity = mResponse.getEntity();
					if (httpEntity != null) {
						handleResponseData(httpEntity);
					}

					release();
					return;
				}

				String strStatus = mResponse.getStatusLine().toString();
				release();
				if (mCallBack != null)
					mCallBack.onError(statusCode, strStatus);

				return;
			} catch (Exception excp) {
				excp.printStackTrace();

				// Timeout
				if (excp instanceof InterruptedIOException) {
					release();
					if (mCallBack != null)
						mCallBack.onError(HttpSessionConstant.ERROR_CODE.ERR_CONNECT_TIMEOUT, excp.toString());
					return;
				}

				// Unknown host
				if (excp instanceof UnknownHostException) {
					release();
					if (mCallBack != null)
						mCallBack.onError(HttpSessionConstant.ERROR_CODE.ERR_UNKNOWN_HOST, excp.toString());
					return;
				}

				// connect refuse
				if (excp instanceof ConnectException) {
					release();
					if (mCallBack != null)
						mCallBack.onError(HttpSessionConstant.ERROR_CODE.ERR_CONNECT_REFUSE, excp.toString());
					return;
				}

				// protocol error
				if (excp instanceof ClientProtocolException) {
					release();
					if (mCallBack != null)
						mCallBack.onError(HttpSessionConstant.ERROR_CODE.ERR_PROTOCOL_ERROR, excp.toString());
					return;
				}
				//NoHttpResponseException
				if (excp instanceof NoHttpResponseException) {
					release();
					if (mCallBack != null)
						mCallBack.onError(HttpSessionConstant.ERROR_CODE.ERR_NO_RESPOND, excp.toString());
					return;
				}

			} finally {
				release();
			}
			mHasRetry++;
		}
	}
}
