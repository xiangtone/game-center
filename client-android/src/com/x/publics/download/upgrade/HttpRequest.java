package com.x.publics.download.upgrade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;

import com.x.R;

/**
 * Http操作类
 * 
 
 * 
 */
public class HttpRequest {

	private Context mContext;// 上下文
	private static DefaultHttpClient client;// 单例的DefaultHttpClient
	private static int inc;// HTTP请求计数器
	private HttpGet httpGet;// GET请求实例类
	private HttpPost httpPost;// POST请求实例类
	private HttpResponse response;// HTTP请求响应结果类

	private static final int defaultTimeout = 10000;// 默认从连接池中取连接的超时时间为10秒
	private static final int defaultConnectionTimeout = 10000;// 默认连接超时时间为10秒
	private static final int defaultSoTimeout = 10000;// 默认单次HTTP请求超时时间为10秒
	private static final int defaultSocketBufferSize = -1;// 默认不设置缓冲区的空间大小
	private static final boolean defaultRedirecting = true;// 默认自动跳转
	private static final String defaultUploadContentCharset = HTTP.UTF_8;// 默认上传HTTP数据内容编码为UTF_8
	private static final String defaultDownloadContentCharset = HTTP.UTF_8;// 默认下载HTTP数据内容编码为UTF_8
	private static final int maxRedirectingCount = 5;// 最大跳转次数

	private int timeout = defaultTimeout;// 从连接池中取连接的超时时间
	private int connectionTimeout = defaultConnectionTimeout;// 连接超时时间
	private int soTimeout = defaultSoTimeout;// 请求超时时间
	private int socketBufferSize = defaultSocketBufferSize;// 缓冲区的空间大小
	private boolean redirecting = defaultRedirecting;// 是否自动跳转
	private String uploadContentCharset = defaultUploadContentCharset;// 上传数据内容编码
	private String downloadContentCharset = defaultDownloadContentCharset;// 下载数据内容编码
	private String suffixStr = "";// 数据行读取时，追加行尾缀 //'','\n', '\r', "\r\n"
	private int resultCode = 0;// HTTP请求状态值
	private int resultReason;// HTTP请求结果描述

	/**
	 * HttpRequest类实例化
	 * 
	 * @param context
	 */
	public HttpRequest(Context context) {
		inc++;
		if (context != null) {
			mContext = context.getApplicationContext();
		}
	}

	/**
	 * 中断请求
	 */
	public void abortHttpPost() {
		if (httpPost != null && !httpPost.isAborted()) {
			httpPost.abort();
		}
	}

	/**
	 * 中断httpGet请求
	 */
	public void abortHttpGet() {
		if (httpGet != null && !httpGet.isAborted()) {
			httpGet.abort();
		}
	}

	/**
	 * 资源卸载及内存释放
	 */
	public void unInit() {
		if (httpGet != null) {
			httpGet.abort();
			httpGet = null;
		}
		if (httpPost != null) {
			httpPost.abort();
			httpPost = null;
		}
		if (response != null) {
			response = null;
		}
		if (mContext != null) {
			mContext = null;
		}
		inc--;
		if (inc == 0 && client != null)// 当所有请求都结束后，关闭连接
		{
			client.getConnectionManager().shutdown();
			if (inc == 0) {
				client = null;
			}
		}
	}

	/**
	 * 设置从连接池中取连接的超时时间
	 * 
	 * @param milliseconds
	 */
	public void setTimeout(int milliseconds) {
		timeout = milliseconds;
	}

	/**
	 * 设置连接超时时间
	 * 
	 * @param milliseconds
	 */
	public void setConnectionTimeout(int milliseconds) {
		connectionTimeout = milliseconds;
	}

	/**
	 * 设置请求超时时间
	 * 
	 * @param milliseconds
	 */
	public void setSoTimeout(int milliseconds) {
		soTimeout = milliseconds;
	}

	/**
	 * 设置缓冲区的空间大小
	 * 
	 * @param size
	 */
	public void setSocketBufferSize(int size) {
		socketBufferSize = size;
	}

	/**
	 * 设置是否自动跳转
	 * 
	 * @param bool
	 */
	public void setRedirecting(boolean bool) {
		redirecting = bool;
	}

	/**
	 * 设置上传HTTP数据内容编码
	 * 
	 * @param bool
	 */
	public void setUploadContentCharset(String charset) {
		uploadContentCharset = charset;
	}

	/**
	 * 设置下载HTTP数据内容编码
	 * 
	 * @param bool
	 */
	public void setDownloadContentCharset(String charset) {
		downloadContentCharset = charset;
	}

	/**
	 * 内容读取时追加行尾缀
	 * 
	 * @param suffix
	 */
	public void setSuffixStr(String suffix) {
		suffixStr = suffix;
	}

	/**
	 * 返回请求结果状态
	 * 
	 * @return 0为请求成功，-1为请求失败
	 */
	public int getResultCode() {
		return resultCode;
	}

	/**
	 * 返回请求结果原因
	 * 
	 * @return
	 */
	public int getResultReason() {
		return resultReason;
	}

	/**
	 * 初始化HttpClient
	 * 
	 * @return
	 */
	private HttpClient initHttpClient() {
		if (client == null) {
			HttpParams httpParams = new BasicHttpParams();
			// 设置一些基本参数
			HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);// 设置HTTP协议版本
			HttpProtocolParams.setUseExpectContinue(httpParams, false);// 是否向服务器预先请求一次询问是否允许大数据POST上传。（如果设置为true，则有两次请求）
																		// //"http.protocol.expect-continue"
			// HttpProtocolParams.setUserAgent(httpParams,"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
			/* 设置我们的HttpClient支持HTTP和HTTPS两种模式 */
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));
			/* 使用线程安全的连接管理来创建HttpClient */
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					httpParams, schReg);
			client = new DefaultHttpClient(conMgr, httpParams);
		}
		return client;
	}

	/**
	 * 初始化HTTP协议参数设置
	 * 
	 * @param httpParams
	 */
	private String initHttpParams(HttpParams httpParams) {
		String host = null;
		HttpProtocolParams.setContentCharset(httpParams, uploadContentCharset);// 设置内容编码
		/* 从连接池中取连接的超时时间 */
		ConnManagerParams.setTimeout(httpParams, timeout);
		/* 连接超时 */
		HttpConnectionParams
				.setConnectionTimeout(httpParams, connectionTimeout);
		/* 请求超时 */
		HttpConnectionParams.setSoTimeout(httpParams, soTimeout);
		/* 设置缓冲区空间大小 */
		if (socketBufferSize > 0) {
			HttpConnectionParams.setSocketBufferSize(httpParams,
					socketBufferSize);
		}
		/* 设置是否自动跳转 */
		HttpClientParams.setRedirecting(httpParams, redirecting);
		httpParams.removeParameter(ConnRoutePNames.DEFAULT_PROXY);
		if (SystemInfo.isCurrentWapNetwork(mContext))// 当前为WAP网络
		{
			String defaultHost = android.net.Proxy.getDefaultHost();
			int defaultPort = android.net.Proxy.getDefaultPort();
			if (defaultHost != null && defaultHost.length() > 0
					&& defaultPort != -1) {
				HttpHost proxy = new HttpHost(defaultHost, defaultPort);
				httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
				host = defaultHost;
			}
		}
		return host;
	}

	/**
	 * 根据response实体返回文本数据
	 * 
	 * @param response
	 * @param charset
	 * @return
	 */
	public String getBody(HttpResponse response, String charset) {
		String result = null;
		try {
			resultCode = -1;
			resultReason = R.string.http_request_fail;
			HttpEntity ent = response.getEntity();
			if (ent != null) {
				InputStream in = ent.getContent();
				if (in != null) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in, charset));
					String line = null;
					StringBuilder sb = new StringBuilder();
					while ((line = reader.readLine()) != null) {
						sb.append(line + suffixStr);
					}
					result = sb.toString();
					resultCode = 0;
					resultReason = R.string.http_request_success;
				}
			}
		} catch (NullPointerException e) {
			resultCode = -1;
			resultReason = R.string.nullPointerException;
		} catch (UnsupportedEncodingException e) {
			resultCode = -1;
			resultReason = R.string.unsupporte_dencoding_exception;
		} catch (IOException e) {
			resultCode = -1;
			resultReason = R.string.ioexception;
		} catch (IllegalStateException e) {
			resultCode = -1;
			resultReason = R.string.illegal_state_exception;
		} catch (Exception e) {
			resultCode = -1;
			resultReason = R.string.exception;
		}
		return result;
	}

	/**
	 * GET请求，返回HttpResponse实体，支持添加头信息列表
	 * 
	 * @param url
	 * @param headerList
	 * @return
	 */
	public HttpResponse getUrlHeaderList(String url,
			ArrayList<BasicHeader> headerList) {
		BasicHeader[] headerArr = null;
		if (headerList != null) {
			int len = headerList.size();
			if (len > 0) {
				headerArr = new BasicHeader[len];
				for (int i = 0; i < len; i++) {
					headerArr[i] = headerList.get(i);
				}
			}
		}
		return getUrlHeaderArr(url, headerArr);
	}

	/**
	 * GET请求基类，返回HttpResponse实体，支持添加头信息数组
	 * 
	 * @param url
	 * @param headerArr
	 * @return
	 */
	private HttpResponse getUrlHeaderArr(String url, BasicHeader[] headerArr) {
		if (mContext == null || url == null || url.length() == 0) {
			resultCode = -1;
			resultReason = R.string.parameter_is_not_valid;
			return null;
		}
		if (!SystemInfo.isNetworkAvailable(mContext)) {
			resultCode = -1;
			resultReason = R.string.no_network_connection;
			return null;
		}
		response = null;
		HttpClient client = initHttpClient();
		if (client == null) {
			client = new DefaultHttpClient();
		}
		HttpParams httpParams = client.getParams();
		String host = initHttpParams(httpParams);
		try {
			resultCode = -1;
			resultReason = R.string.http_request_fail;
			if (httpGet != null) {
				httpGet.abort();
			}
			httpGet = new HttpGet(url);
			if (headerArr != null && headerArr.length > 0) {
				httpGet.setHeaders(headerArr);
			}
			if (host != null) {
				httpGet.setHeader("Host", host);
				httpGet.setHeader("X-Online-Host", Tools.getXHost(url));
			}

			httpGet.setHeader(HTTP.CONTENT_TYPE,
					"application/json:charset=utf-8");

			Header[] headerlist = httpGet.getAllHeaders();
			if (headerlist != null && headerlist.length > 0) {
				for (int i = 0; i < headerlist.length; i++) {
					// Logs.log("HttpRequest",
					// "getUrlHeaderArr() " + headerlist[i].getName() + ": " +
					// headerlist[i].getValue());
				}
			} else {
				// Logs.log("HttpRequest",
				// "getUrlHeaderArr() headerlist is null or headerlist.length=0");
			}
			response = client.execute(httpGet);
			resultCode = 0;
			resultReason = R.string.http_request_success;
		} catch (IllegalArgumentException e) {
			resultCode = -1;
			resultReason = R.string.illegal_state_exception;
		} catch (ConnectionPoolTimeoutException e) {
			resultCode = -1;
			resultReason = R.string.connection_pool_timeout_exception;
		} catch (ConnectTimeoutException e) {
			resultCode = -1;
			resultReason = R.string.connection_pool_timeout_exception;
		} catch (SocketTimeoutException e) {
			resultCode = -1;
			resultReason = R.string.connection_pool_timeout_exception;
		} catch (ClientProtocolException e) {
			resultCode = -1;
			resultReason = R.string.client_protocol_exception;
		} catch (IOException e) {
			resultCode = -1;
			resultReason = R.string.client_protocol_exception;
		} catch (Exception e) {
			resultCode = -1;
			resultReason = R.string.exception;
		}
		return response;
	}

	/**
	 * GET请求对外开放基本接口，返回String文本，内部处理了自动跳转
	 * 
	 * @param url
	 * @return
	 */
	public String getUrl(String url) {
		return getUrl(url, null);
	}

	/**
	 * GET请求对外开放基本接口，返回String文本，内部处理了自动跳转，支持添加头信息列表
	 * 
	 * @param url
	 * @param headerList
	 * @return
	 */
	public String getUrl(String url, ArrayList<BasicHeader> headerList) {
		String result = null;
		BasicHeader[] headerArr = null;
		if (headerList != null) {
			int len = headerList.size();
			if (len > 0) {
				headerArr = new BasicHeader[len];
				for (int i = 0; i < len; i++) {
					headerArr[i] = headerList.get(i);
				}
			}
		}
		HttpResponse response = getUrlHeaderArr(url, headerArr);
		if (response != null && response.getStatusLine() != null) {
			int stateCode = response.getStatusLine().getStatusCode();
			if (stateCode == HttpStatus.SC_OK
					|| stateCode == HttpStatus.SC_PARTIAL_CONTENT) {
				result = getBody(response, downloadContentCharset);
			} else if (stateCode == HttpStatus.SC_MOVED_TEMPORARILY
					|| stateCode == HttpStatus.SC_MOVED_PERMANENTLY) {
				String jumpurl = null;
				Header header = response.getFirstHeader("Location");
				if (header != null) {
					jumpurl = header.getValue();
				}
				if (jumpurl != null && jumpurl.length() > 0) {
					for (int i = 0; i < maxRedirectingCount; i++) {
						response = getUrlHeaderArr(jumpurl, headerArr);
						if (response != null
								&& response.getStatusLine() != null) {
							stateCode = response.getStatusLine()
									.getStatusCode();
							if (stateCode == HttpStatus.SC_OK
									|| stateCode == HttpStatus.SC_PARTIAL_CONTENT) {
								result = getBody(response,
										downloadContentCharset);
								break;
							} else if (stateCode == HttpStatus.SC_MOVED_TEMPORARILY
									|| stateCode == HttpStatus.SC_MOVED_PERMANENTLY) {
								jumpurl = null;
								header = response.getFirstHeader("Location");
								if (header != null) {
									jumpurl = header.getValue();
								}
								if (jumpurl == null || jumpurl.length() == 0) {
									break;
								}
							} else {
								break;
							}
						} else {
							break;
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 检测HTTP请求的返回结果是否有效
	 * 
	 * @param response
	 * @return
	 */
	public boolean isHttpResponseAvailable(HttpResponse response) {
		boolean result = false;
		if (response != null) {
			if (response.getStatusLine() != null) {
				int stateCode = response.getStatusLine().getStatusCode();
				if (stateCode == HttpStatus.SC_OK
						|| stateCode == HttpStatus.SC_PARTIAL_CONTENT) {
					result = true;
				} else if (stateCode == HttpStatus.SC_MOVED_TEMPORARILY
						|| stateCode == HttpStatus.SC_MOVED_PERMANENTLY) {
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * POST请求基类，返回HttpResponse实体，支持添加头信息数组
	 * 
	 * @param url
	 * @param headerArr
	 * @return
	 */
	private HttpResponse getPOSTHeaderArr(String url, BasicHeader[] headerArr,
			List<BasicNameValuePair> nameValuePairs, String body) {
		if (mContext == null || url == null || url.length() == 0) {
			resultCode = -1;
			resultReason = R.string.parameter_is_not_valid;
			return null;
		}
		if (!SystemInfo.isNetworkAvailable(mContext)) {
			resultCode = -1;
			resultReason = R.string.no_network_connection;
			return null;
		}
		response = null;
		HttpClient client = initHttpClient();
		if (client == null) {
			client = new DefaultHttpClient();
		}
		HttpParams httpParams = client.getParams();
		String host = initHttpParams(httpParams);
		try {
			resultCode = -1;
			resultReason = R.string.http_request_fail;
			if (httpPost != null) {
				httpPost.abort();
			}
			httpPost = new HttpPost(url);
			if (headerArr != null && headerArr.length > 0) {
				httpPost.setHeaders(headerArr);
			}
			if (host != null) {
				httpPost.setHeader("Host", host);
				httpPost.setHeader("X-Online-Host", Tools.getXHost(url));
			}
			httpPost.setHeader(HTTP.CONTENT_TYPE, URLEncodedUtils.CONTENT_TYPE);

			httpPost.setHeader("Accept ", "application/json");
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader("Connection", "Keep-Alive");

			Header[] headerlist = httpPost.getAllHeaders();
			if (headerlist != null && headerlist.length > 0) {
				for (int i = 0; i < headerlist.length; i++) {
					// Logs.log("HttpRequest",
					// "getPOSTHeaderArr() " + headerlist[i].getName() + ": " +
					// headerlist[i].getValue());
				}
			} else {
				// Logs.log("HttpRequest",
				// "getPOSTHeaderArr() headerlist is null or headerlist.length=0");
			}
			try {
				if (nameValuePairs != null && nameValuePairs.size() > 0) {
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
							defaultUploadContentCharset));
				} else if (body != null && body.length() > 0) {
					httpPost.setEntity(new StringEntity(body,
							defaultUploadContentCharset));
				}
			} catch (UnsupportedEncodingException e) {
			}
			response = client.execute(httpPost);
			resultCode = 0;
			resultReason = R.string.http_request_success;
		} catch (IllegalArgumentException e) {
			resultCode = -1;
			resultReason = R.string.illegal_state_exception;
		} catch (ConnectionPoolTimeoutException e) {
			resultCode = -1;
			resultReason = R.string.connection_pool_timeout_exception;
		} catch (ConnectTimeoutException e) {
			resultCode = -1;
			resultReason = R.string.connection_pool_timeout_exception;
		} catch (SocketTimeoutException e) {
			resultCode = -1;
			resultReason = R.string.connection_pool_timeout_exception;
		} catch (ClientProtocolException e) {
			resultCode = -1;
			resultReason = R.string.client_protocol_exception;
		} catch (IOException e) {
			resultCode = -1;
			resultReason = R.string.client_protocol_exception;
		} catch (Exception e) {
			resultCode = -1;
			resultReason = R.string.exception;
		} finally {
			// TODO..
		}
		return response;
	}

	/**
	 * POST请求，返回HttpResponse实体，支持添加头信息列表，支持添加POST参数
	 * 
	 * @param url
	 * @param headerList
	 * @param nameValuePairs
	 * @param body
	 * @return
	 */
	public HttpResponse getPOSTHeaderList(String url,
			ArrayList<BasicHeader> headerList,
			List<BasicNameValuePair> nameValuePairs, String body) {
		BasicHeader[] headerArr = null;
		if (headerList != null) {
			int len = headerList.size();
			if (len > 0) {
				headerArr = new BasicHeader[len];
				for (int i = 0; i < len; i++) {
					headerArr[i] = headerList.get(i);
				}
			}
		}
		return getPOSTHeaderArr(url, headerArr, nameValuePairs, body);
	}

	/**
	 * POST请求对外开放基本接口，返回String文本，支持设置整个HTTP的BODY字段
	 * 
	 * @param url
	 * @param body
	 * @return
	 */
	public String postUrl(String url, String body) {
		return postUrl(url, null, null, body);
	}

	/**
	 * POST请求对外开放基本接口，返回String文本，支持添加头信息列表，支持设置整个HTTP的BODY字段
	 * 
	 * @param url
	 * @param headerList
	 * @param body
	 * @return
	 */
	public String postUrl(String url, ArrayList<BasicHeader> headerList,
			String body) {
		return postUrl(url, headerList, null, body);
	}

	/**
	 * POST请求对外开放基本接口，返回String文本，支持添加POST参数
	 * 
	 * @param url
	 * @param nameValuePairs
	 * @return
	 */
	public String postUrl(String url, List<BasicNameValuePair> nameValuePairs) {
		return postUrl(url, null, nameValuePairs, null);
	}

	/**
	 * POST请求对外开放基本接口，返回String文本，支持添加头信息列表，支持添加POST参数
	 * 
	 * @param url
	 * @param headerList
	 * @param nameValuePairs
	 * @return
	 */
	public String postUrl(String url, ArrayList<BasicHeader> headerList,
			List<BasicNameValuePair> nameValuePairs) {
		return postUrl(url, headerList, nameValuePairs, null);
	}

	/**
	 * POST请求对外开放基本接口，返回String文本，支持添加头信息列表，支持添加POST参数，支持设置整个HTTP的BODY字段
	 * 
	 * @param url
	 * @param headerList
	 * @param nameValuePairs
	 * @param body
	 * @return
	 */
	public String postUrl(String url, ArrayList<BasicHeader> headerList,
			List<BasicNameValuePair> nameValuePairs, String body) {
		String result = null;
		BasicHeader[] headerArr = null;
		if (headerList != null) {
			int len = headerList.size();
			if (len > 0) {
				headerArr = new BasicHeader[len];
				for (int i = 0; i < len; i++) {
					headerArr[i] = headerList.get(i);
				}
			}
		}
		HttpResponse response = getPOSTHeaderArr(url, headerArr,
				nameValuePairs, body);
		if (response != null && response.getStatusLine() != null) {
			int stateCode = response.getStatusLine().getStatusCode();
			if (stateCode == HttpStatus.SC_OK
					|| stateCode == HttpStatus.SC_PARTIAL_CONTENT) {
				result = getBody(response, downloadContentCharset);
			} else if (stateCode == HttpStatus.SC_MOVED_TEMPORARILY
					|| stateCode == HttpStatus.SC_MOVED_PERMANENTLY) {
				String jumpurl = null;
				Header header = response.getFirstHeader("Location");
				if (header != null) {
					jumpurl = header.getValue();
				}
				if (jumpurl != null && jumpurl.length() > 0) {
					for (int i = 0; i < maxRedirectingCount; i++) {
						response = getPOSTHeaderArr(url, headerArr,
								nameValuePairs, body);
						if (response != null
								&& response.getStatusLine() != null) {
							stateCode = response.getStatusLine()
									.getStatusCode();
							if (stateCode == HttpStatus.SC_OK
									|| stateCode == HttpStatus.SC_PARTIAL_CONTENT) {
								result = getBody(response,
										downloadContentCharset);
								break;
							} else if (stateCode == HttpStatus.SC_MOVED_TEMPORARILY
									|| stateCode == HttpStatus.SC_MOVED_PERMANENTLY) {
								jumpurl = null;
								header = response.getFirstHeader("Location");
								if (header != null) {
									jumpurl = header.getValue();
								}
								if (jumpurl == null || jumpurl.length() == 0) {
									break;
								}
							} else {
								break;
							}
						} else {
							break;
						}
					}
				}
			}
		}
		return result;
	}

}
