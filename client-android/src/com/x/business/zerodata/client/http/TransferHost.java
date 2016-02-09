/**   
* @Title: Host.java
* @Package com.mas.amineappstore.business.zerodata.client.http
* @Description: TODO 

* @date 2014-4-8 上午10:56:09
* @version V1.0   
*/

package com.x.business.zerodata.client.http;

/**
* @ClassName: Host
* @Description: TODO 

* @date 2014-4-8 上午10:56:09
* 
*/

public class TransferHost {

	public static final String PORT = "8080";
	public static final String HTTP_PREFIX = "http://";
	public static final String HTTP_COLON = ":";
	public static final String HTTP_DEC = "/";

	public static class ResponseCode {
		public static final int SUCCESS = 200;
		public static final int CLIENT_OUT_SIZE = 201;
		public static final int DISCONNECT_RESPONSE = 202;
	}
	
	public static class ResponseMsg {
		public static final String ok = "ok";
		public static final String failure = "failure";
	}

	/**  获取分享信息请求  **/
	public static final String GET_TRANSFER_LIST = "getTransferList";
	public static final int GET_TRANSFER_LIST_RC_CODE = 10001;

	/** 下载请求  **/
	public static final String TRANSFER = "transfer";
	public static final int TRANSFER_RC_CODE = 10002;

	/** 断开连接请求  **/
	public static final String DISCONNECT = "disconnect";
	public static final int DISCONNECT_RC_CODE = 10003;

	/** 更新服务端传输进度请求  **/
	public static final String UPDATE_PROGRESS = "updateProgress";
	public static final int UPDATE_PROGRESS_RC_CODE = 10004;
	
	/** 重新连接请求  **/
	public static final String RECONNECT = "reconnect";
	public static final int RECONNECT_RC_CODE = 10005;

}
