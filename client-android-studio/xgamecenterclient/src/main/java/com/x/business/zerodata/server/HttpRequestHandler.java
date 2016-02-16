package com.x.business.zerodata.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.x.business.zerodata.client.http.TransferHost;
import com.x.business.zerodata.helper.ZeroDataConstant;
import com.x.business.zerodata.helper.ZeroDataResourceHelper;
import com.x.business.zerodata.history.TransferHistoryManager;
import com.x.business.zerodata.server.service.params.ServerParams;
import com.x.publics.download.BroadcastManager;
import com.x.publics.utils.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

public class HttpRequestHandler implements Runnable {

	private static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";

	private String mServerVersion = null;

	final static String CRLF = "\r\n";

	private Context context;
	private ServerResponseReceiver serverResponseReceiver;
	private String clientGetPramas;
	private String clientReconnectGetPramas;

	private Socket mSocket;
	private OutputStream mOutput;
	private BufferedReader mBr;
	private boolean isResponse = false;
	private boolean isCilentOut = false;
	private boolean isResponseReconnect = false ;
	private boolean isReconnectCilentOut = false ;
	private ServerParams mServerParams;
	
	//	private LogHelper mLogAdapter;

	// SimpleDateFormat is not threadsafe, so we need an instance per thread
	private DateFormat mHttpDate = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);

	private static Map<String, String> mimeTypes;
	static {
		// Maybe there is a /etc/mime-types available?
		mimeTypes = new HashMap<String, String>();
		mimeTypes.put("htm", "text/html");
		mimeTypes.put("css", "text/css");
		mimeTypes.put("html", "text/html");
		mimeTypes.put("xhtml", "text/xhtml");
		mimeTypes.put("txt", "text/html");
		mimeTypes.put("pdf", "application/pdf");
		mimeTypes.put("jpg", "image/jpeg");
		mimeTypes.put("gif", "image/gif");
		mimeTypes.put("png", "image/png");
		mimeTypes.put("svg", "image/svg+xml");
	}

	/**
	 * Create the object to manage the request;
	 * 
	 * @param socket
	 *            The socket were the connection is.
	 * @param serverVersion
	 *            The server version. This version will be printed in the auto
	 *            generated files (like file indexing)
	 * @throws Exception
	 */
	public HttpRequestHandler(Socket socket, ServerParams serverParams, String serverVersion, Context context)
			throws Exception {
		mServerParams = serverParams;
		mSocket = socket;
		mOutput = socket.getOutputStream();
		mBr = new BufferedReader(new InputStreamReader(socket.getInputStream()), 2 * 1024);
		mServerVersion = serverVersion;
		this.context = context;
		if (null == mServerVersion) {
			mServerVersion = "";
		} else {
			mServerVersion = "v" + mServerVersion;
		}

		registerServerResponseReceiver();
	}

	public void run() {
		try {
			LogUtil.getLogger().i("zerodata", "HttpRequestHandler [ is running ]");
			processRequest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function to process the request
	 * 
	 * @throws Exception
	 */
	private void processRequest() {

		Map<String, String> requestHeader = new HashMap<String, String>();
		Map<String, String> responseHeader = new LinkedHashMap<String, String>();

		String statusLine = null;
		String httpRequest = "";
		String error = null;
		String info = null;
		String entityBody = null;
		FileInputStream fis = null;
		RandomAccessFile raf = null;
		String fileName = null;
		File file = null;
		boolean sendBody = true;

		try {
			// Analyze the HTTP-Request
			httpRequest = mBr.readLine();
			//			GET 
			//			/index?deviceModel=ALCATEL ONE TOUCH P320X&mac=28:9a:fa:1c:24:47&osVersionName=4.2.2&osVersion=17&imsi=000000000000000&nickName=&imei=014012000001049 
			//			HTTP/1.1
			StringTokenizer s = new StringTokenizer(httpRequest);
			String httpCommand = s.nextToken();
			String fileGet = null;

			String requestMethod = getRequestMethod(URLDecoder.decode(s.nextToken(), "UTF-8"));

			// Analyze all HTTP-Request-Headers
			while (true) {

				String headerLine = mBr.readLine();

				if (headerLine.equals(CRLF) || headerLine.equals("")) {
					break;
				}

				int idx = headerLine.indexOf(" ");
				if (idx >= 1)
					requestHeader.put(headerLine.substring(0, idx), headerLine.substring(idx + 1));

			}
			//解析请求
			if (httpCommand.equals("GET") || httpCommand.equals("HEAD")) {
				LogUtil.getLogger().d("=========requestMethod:" + requestMethod);

				//1.请求下载协议
				if (requestMethod.equals(TransferHost.GET_TRANSFER_LIST)) {
					statusLine = "200 OK";
					responseHeader.put("Content-Type:", "application/json;charset=utf-8");
					entityBody = ZeroDataResourceHelper.getInstance(this.context).getServerTransferData();

					clientGetPramas = URLDecoder.decode(httpRequest, "UTF-8");
					isCilentOut = false ;
					LogUtil.getLogger().i("zerodata", "HttpRequestHandler[httpRequest=" + clientGetPramas + "]");
					sendServerBroadcast(clientGetPramas,ZeroDataConstant.ACTION_HTTP_REQUEST_HANDLER) ;
					
					while (true) {
						Thread.sleep(200) ;
						if (isResponse) {
							break;
						}
					}
					if(isCilentOut)
					{
						entityBody = ZeroDataResourceHelper.getInstance(this.context).getClientOutResponse(isCilentOut) ;	
					}
					
					
					//2.更新进度
				} else if (requestMethod.contains(TransferHost.UPDATE_PROGRESS)) {
					statusLine = "200 OK";
					String updateProgressPramas = URLDecoder.decode(httpRequest, "UTF-8");
					sendServerBroadcast(updateProgressPramas,ZeroDataConstant.ACTION_HTTP_UPDATE_PROGRESS_HANDLER) ;
					//3.取消连接
				} else if (requestMethod.contains(TransferHost.DISCONNECT)) {
					statusLine = "200 OK";
					String disconnectPramas = URLDecoder.decode(httpRequest, "UTF-8");
					sendServerBroadcast(disconnectPramas,ZeroDataConstant.ACTION_HTTP_DISCONNECT_HANDLER) ;
					entityBody = ZeroDataResourceHelper.getInstance(this.context).getDisconnectResponse() ;
					//4.重连
				}else if (requestMethod.contains(TransferHost.RECONNECT)) {
					statusLine = "200 OK";
					responseHeader.put("Content-Type:", "application/json;charset=utf-8");
					clientReconnectGetPramas = URLDecoder.decode(httpRequest, "UTF-8");
					isReconnectCilentOut = false ;
					sendServerBroadcast(clientReconnectGetPramas,ZeroDataConstant.ACTION_HTTP_RECONNECT_REQUEST_HANDLER) ;
					
					while (true) {
						if (isResponseReconnect) {
							break;
						}
					}
					if(isReconnectCilentOut)
					{
						entityBody = ZeroDataResourceHelper.getInstance(this.context).getClientOutResponse(isReconnectCilentOut) ;	
					}else 
					{
						entityBody = ZeroDataResourceHelper.getInstance(this.context).getServerTransferData() ;
					} 
				} 
				else {
					//5.下载zapp地址
					if (requestMethod.endsWith("/")) {
						fileName = ZeroDataResourceHelper.getInstance(context).getZappSourceDir(); //wifi邀请安装zApp
						responseHeader.put("Content-Disposition:", "attachment; filename=zApp.apk");
						//6.下载文件
					} else {
						fileName = requestMethod;
					}
					file = new File(fileName);

					boolean fileExists = true;
					long range = 0l;
					if (requestHeader.get("Range:") != null) {
						String size = requestHeader.get("Range:").replace("bytes=", "").replace("-", "");
						range = Long.valueOf(size);
					}
					if (file.exists()) {
						fis = new FileInputStream(file);
						raf = new RandomAccessFile(file, "r");
						if (range != 0) {
							raf.seek(range);
						}
					} else {
 						fileExists = false;
					}
					responseHeader.put("Date:", mHttpDate.format(System.currentTimeMillis()));
					boolean notModified = false;

					if (!sendBody) {
						;
					} else if (fileExists) {
						responseHeader.put("Last-Modified:", mHttpDate.format(file.lastModified()));

						if (!notModified) {
							statusLine = "200 OK";
							responseHeader.put("Content-Type:", contentType(file.getName()));
							if (mServerParams.getCacheTime() > 0) {
								responseHeader.put(
										"Expires:",
										mHttpDate.format(System.currentTimeMillis()
												+ (mServerParams.getCacheTime() * 60 * 1000)));
							}
						}

					} else {
						statusLine = "404 Bad Request";
						error = "Bad Request";
					}

					if (httpCommand.equals("HEAD")) {
						sendBody = false;
					}
				}

			} else if (httpCommand.equals("POST")) {
				statusLine = "405 Method Not Allowed";
				error = "Method Not Allowed";
				responseHeader.put("Allow:", "HEAD, GET");

			} else {
				// HTTP 1.0 only defines HEAD, GET, POST.
				statusLine = "400 Bad Request";
				error = "Bad Request";
			}

		} catch (Exception e) {
			statusLine = "500 Internal Server Error";
			responseHeader.put("Content-Type:", "text/html");
			error = "Internal Server Error";
			e.printStackTrace();
		}
		try {

			// Send the status line.
			mOutput.write(("HTTP/1.0 " + statusLine + CRLF).getBytes());

			// lets try to find out the content-length.
			if (entityBody != null) {
				responseHeader.put("Content-Length:", Integer.valueOf(entityBody.getBytes().length).toString());
			} else if (raf != null) {
				long length = file.length();
				if (length > 0) {
					responseHeader.put("Content-Length:", "" + length);
				}
			}

			// Output all response headers
			for (Entry<String, String> header : responseHeader.entrySet())
				mOutput.write((header.getKey() + " " + header.getValue() + CRLF).getBytes());

			// Send a blank line to indicate the end of the header
			// lines.
			mOutput.write(CRLF.getBytes());

			// Send the entity body.
			if (!sendBody) {
				// do not send the body.
			} else if (entityBody != null) {
				mOutput.write(entityBody.getBytes("UTF-8"));
				LogUtil.getLogger().d("write===" + entityBody);
			} else if (fis != null) {
				sendBytes(raf, mOutput);
				raf.close();
				TransferHistoryManager.getInstance().saveTransfershareHistory(
						ZeroDataResourceHelper.getInstance(context).getTransfer(fileName));
			} else {
				// no content
			}
			mOutput.close();
			mBr.close();
			mSocket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send bytes for the request
	 * 
	 * @param fis
	 *            fileInputStream to send
	 * @param os
	 *            The output Stream to use for sending
	 * @throws Exception
	 */
	private void sendBytes(FileInputStream fis, OutputStream os) throws Exception {

		byte[] buffer = new byte[1024 * 1024];
		int bytes = 0;

		while ((bytes = fis.read(buffer)) != -1) {

			os.write(buffer, 0, bytes);
		}
	}

	private void sendBytes(RandomAccessFile raf, OutputStream os) throws Exception {

		byte[] buffer = new byte[1024 * 1024];
		int bytes = 0;

		while ((bytes = raf.read(buffer)) != -1) {

			os.write(buffer, 0, bytes);
		}
	}

	/**
	 * Get content type
	 * 
	 * @param fileName
	 *            The file
	 * @return Content type
	 */
	private String contentType(String fileName) {

		String ext = "";
		int idx = fileName.lastIndexOf(".");
		if (idx >= 0) {
			ext = fileName.substring(idx + 1);
		}

		if (mimeTypes.containsKey(ext))
			return mimeTypes.get(ext);
		else
			return "application/octet-stream";
	}


	
	/**
	 * // /index?deviceModel=ALCATEL ONE TOUCH P320X&mac=28:9a:fa:1c:24:47&osVersionName=4.2.2&osVersion=17&imsi=000000000000000&nickName=&imei=014012000001049
	* @Title: getRequestMethod 
	* @Description: TODO(获取请求头) 
	* @param @param token
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	private String getRequestMethod(String token) {
		String method = null;
		if (token.startsWith("/") && token.contains("?")) {
			method = token.substring(1, token.lastIndexOf("?"));
		} else {
			method = token;
		}
		return method;
	}

	/**
	 * 
	* @Title: sendServerBroadcast 
	* @Description: TODO(通知服务器端响应) 
	* @param @param clientGetPramas
	* @param @param action    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void sendServerBroadcast(String clientGetPramas,String action)
	{
		Intent intent = new Intent(action);
		intent.putExtra(ZeroDataConstant.CLIENT_REQUEST_PARAMS, clientGetPramas);
		BroadcastManager.sendBroadcast(intent);
	}
	
	/**
	 * 
	* @Title: registerServerResponseReceiver 
	* @Description: TODO(注册回调) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void registerServerResponseReceiver() {

		serverResponseReceiver = new ServerResponseReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ZeroDataConstant.ACTION_HTTP_RESPONSE);
		filter.addAction(ZeroDataConstant.ACTION_HTTP_RECONNECT_RESPONSE);
		BroadcastManager.registerReceiver(serverResponseReceiver, filter);
	}

	/**
	 *	
	* @ClassName: ServerResponseReceiver
	* @Description: TODO(服务端回调)
	
	* @date 2014-4-21 上午11:43:10
	*
	 */
	public class ServerResponseReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			//首次连接响应
			if (intent != null && ZeroDataConstant.ACTION_HTTP_RESPONSE.equals(intent.getAction())) {
				if (!TextUtils.isEmpty(clientGetPramas)
						&& clientGetPramas.equals(intent.getStringExtra(ZeroDataConstant.CLIENT_REQUEST_PARAMS))) {
					isCilentOut = intent.getBooleanExtra("isCilentOut", false);
					isResponse = intent.getBooleanExtra("isResponse", false);
					LogUtil.getLogger().i("zerodata", "ServerResponseReceiver[isResponse=" + isResponse + "]");
				}

			}//重新连接
			else if (intent != null && ZeroDataConstant.ACTION_HTTP_RECONNECT_RESPONSE.equals(intent.getAction())) {
				if (!TextUtils.isEmpty(clientReconnectGetPramas)
						&& clientReconnectGetPramas.equals(intent.getStringExtra(ZeroDataConstant.CLIENT_REQUEST_PARAMS))) {
					isResponseReconnect = true ;
					isReconnectCilentOut = intent.getBooleanExtra("isReconnectCilentOut", false);
					LogUtil.getLogger().i("zerodata", "ServerResponseReceiver[isReconnectCilentOut=" + isResponse + "]");
				}

			}
		}
	}
}