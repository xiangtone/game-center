/**   
 * @Title: NotificationConstan.java
 * @Package com.mas.amineappstore.business.notification
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2015-8-5 下午5:14:46
 * @version V1.0   
 */

package com.x.business.notification;

/**
 * @ClassName: NotificationConstan
 * @Description: 通知推送相关的静态常量
 
 * @date 2015-8-5 下午5:14:46
 * 
 */

public class NotificationConstan {
	/** 通知信息 详情标志位 */
	public static final int NOTI_DETAIL_ID = 1100000;
	/** 通知栏点击: 请求详细页面 请求 code */
	public static final int DETAIL_REQ_CODE = 1200010;
	/** 通知栏点击: 请求详细页面 控件功能 code */
	public static final int DETAIL_BUTTON_ID = 2200010;

	/**打开类型：浏览器URL*/
	public static final int OPEN_URL = 1;
	/**打开类型：Activity*/
	public static final int OPEN_ACTIVITY = 0;
	/**推送类型：图片模式*/
	public static final int PUSH_MODE_PICTURE=1;
	/**推送类型：文本模式*/
	public static final int PUSH_MODE_TEXT=0;
	
	/** 通知栏按钮点击事件对应的ACTION */
	public final static String ACTION_BUTTON = "com.mas.amineappstore.intent.action.NotifyClick";
	/** 通知栏标识 */
	public final static String INTENT_NOTICEID_TAG = "NoticeId";
	/** 通知 点击控件 标识 */
	public final static String INTENT_BUTTONID_TAG = "ViewId";
	/** 通知 intent 字段 */
	public final static String INTENT_INTENT_TAG = "NotifyIntent";
	/** 通知 打开类型 */
	public final static String INTENT_OPEN_TYPE_TAG="OpenType";
	
	

}
