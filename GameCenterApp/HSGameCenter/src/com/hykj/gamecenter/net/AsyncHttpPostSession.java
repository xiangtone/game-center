package com.hykj.gamecenter.net;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class AsyncHttpPostSession extends AsyncHttpSession {
	
	private static final String TAG = "AsyncHttpPostSession";

	private byte[] mRequestData = null;
	private HttpPost mHttpPost = null;

	public AsyncHttpPostSession(String url) {
		super(url);
	}

	public void doPost(byte[] requestData) {
		mRequestData = requestData;
		createPost();
	}

	private void createPost() {
		AsyncHttpPostSessionManager.getInstance().submit(this);
	}

	public void release() {
		if (mHttpPost != null)
			mHttpPost.abort();
		shutDown();
	}

	@Override
	protected void doRun() {
		while (mHasRetry < HttpSessionConstant.MAX_RETRY.POST_DATA) {
			try {
				HttpClient httpClient = getHttpClient();

				mHttpPost = new HttpPost(mUrl);
				mHttpPost.setHeader("Content-Type", "multipart/form-data");
				/*表单中enctype="multipart/form-data"的意思，
				 * 是设置表单的MIME编码。
				 * 默认情况，这个编码格式是application/x-www-form-urlencoded，
				 * 不能用于文件上传；
				 * 只有使用了multipart/form-data，才能完整的传递文件数据，
				 * 进行下面的操作. add by firewang*/
				mHttpPost.setEntity(new ByteArrayEntity(mRequestData));

				Log.e(TAG, "AsyncHttp doRun hasRetry" + mHasRetry);
				HttpContext localContext = new BasicHttpContext();
				mResponse = httpClient.execute(mHttpPost, localContext);

				int statusCode = mResponse.getStatusLine().getStatusCode();
				Log.e(TAG, "statusCode-->" + statusCode);
				Log.e(TAG, "respose" + mResponse);
				if (statusCode == HttpStatus.SC_OK) {
					HttpEntity httpEntity = mResponse.getEntity();
					Log.e(TAG, "httpEntity is null? " + (httpEntity == null) );
					if (httpEntity != null) {
						byte[] byteData = EntityUtils.toByteArray(httpEntity);
						Log.e(TAG, "mCallBack is null? "  + (mCallBack == null) );
						if (mCallBack != null)
							mCallBack.onSucceed(byteData);
						String strStatus = mResponse.getStatusLine().toString();
						Log.e(TAG, "strStatus-->" + strStatus);
					}
					release();
					return;
				}

				String strStatus = mResponse.getStatusLine().toString();
				Log.e(TAG, "strStatus-->" + strStatus);
				release();
				if (mCallBack != null)
					mCallBack.onError(statusCode, strStatus);

				return;
			} catch (Exception excp) {
				excp.printStackTrace();
				Log.e(TAG, "exception "+ excp);
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

			} finally {
				release();
			}
			mHasRetry++;
		}
	}
}
