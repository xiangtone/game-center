package com.hykj.gamecenter.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.hykj.gamecenter.activity.GroupAppListActivity;
import com.hykj.gamecenter.activity.HomePageActivity;
import com.hykj.gamecenter.activity.PhoneAppInfoActivity;
import com.hykj.gamecenter.controller.ProtocolListener;
import com.hykj.gamecenter.data.GroupInfo;
import com.hykj.gamecenter.data.TopicInfo;
import com.hykj.gamecenter.db.CSACDatabaseHelper;
import com.hykj.gamecenter.db.DatabaseUtils;
import com.hykj.gamecenter.protocol.Apps;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.StringUtils;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.HashMap;

public class MessageReceiver extends XGPushBaseReceiver {
	private static final String TAG = "MessageReceiver";
//	private Intent intent = new Intent("com.qq.xgdemo.activity.UPDATE_LISTVIEW");
	public static final String LogTag = "TPushReceiver";

	private void show(Context context, String text) {
//		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	// 通知展示
	@Override
	public void onNotifactionShowedResult(Context context,
										  XGPushShowedResult notifiShowedRlt) {
		if (context == null || notifiShowedRlt == null) {
			return;
		}
//		XGNotification notific = new XGNotification();
//		notific.setMsg_id(notifiShowedRlt.getMsgId());
//		notific.setTitle(notifiShowedRlt.getTitle());
//		notific.setContent(notifiShowedRlt.getContent());
//		// notificationActionType==1为Activity，2为url，3为intent
//		notific.setNotificationActionType(notifiShowedRlt
//				.getNotificationActionType());
//		// Activity,url,intent都可以通过getActivity()获得
//		notific.setActivity(notifiShowedRlt.getActivity());
//		notific.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//				.format(Calendar.getInstance().getTime()));
//		NotificationService.getInstance(context).save(notific);
//		context.sendBroadcast(intent);
//		show(context, "您有1条新消息, " + "通知被展示 ， " + notifiShowedRlt.toString());
	}

	@Override
	public void onUnregisterResult(Context context, int errorCode) {
//		if (context == null) {
//			return;
//		}
//		String text = "";
//		if (errorCode == XGPushBaseReceiver.SUCCESS) {
//			text = "反注册成功";
//		} else {
//			text = "反注册失败" + errorCode;
//		}
//		Log.d(LogTag, text);
//		show(context, text);

	}

	@Override
	public void onSetTagResult(Context context, int errorCode, String tagName) {
//		if (context == null) {
//			return;
//		}
//		String text = "";
//		if (errorCode == XGPushBaseReceiver.SUCCESS) {
//			text = "\"" + tagName + "\"设置成功";
//		} else {
//			text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
//		}
//		Log.d(LogTag, text);
//		show(context, text);

	}

	@Override
	public void onDeleteTagResult(Context context, int errorCode, String tagName) {
//		if (context == null) {
//			return;
//		}
//		String text = "";
//		if (errorCode == XGPushBaseReceiver.SUCCESS) {
//			text = "\"" + tagName + "\"删除成功";
//		} else {
//			text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
//		}
//		Log.d(LogTag, text);
//		show(context, text);

	}

	// 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
	@Override
	public void onNotifactionClickedResult(Context context,
										   XGPushClickedResult message) {
		if (context == null || message == null) {
			return;
		}
//		String text = "";
		if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
			// 通知在通知栏被点击啦。。。。。
			// APP自己处理点击的相关动作
			// 这个动作可以在activity的onResume也能监听，请看第3点相关内容
//			text = "通知被打开 :" + message;
			handleMsgClick(context, message);

		} else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
			// 通知被清除啦。。。。
			// APP自己处理通知被清除后的相关动作
//			text = "通知被清除 :" + message;
		}
//		 APP自主处理的过程。。。
//		Log.d(LogTag, text);
//		show(context, text);
	}

	private static final String OPEN = "open";
	private static final String APPINFO = "appinfo";
	private static final String SPECIAL = "special";
	private static final String GROUPTYPE = "grouptype";
	private static final String LINK = "link";
	private static final String DOWNLOAD = "download";
	private static final String RECOMM = "recomm";


	private void handleMsgClick(Context context, XGPushClickedResult message) {
		// 获取自定义key-value
		String customContent = message.getCustomContent();
		Logger.i(TAG, "handleMsgClick " + "customContent " + customContent, "oddshou");
		if (customContent != null && customContent.length() != 0) {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1为前台配置的key
				if (!obj.isNull(OPEN)) {
					intent.setClass(context, HomePageActivity.class);
					//跳转主页,使用默认操作
//					context.startActivity(intent);
				} else if (!obj.isNull(APPINFO)) {
					String allValue = obj.getString(APPINFO);
					HashMap<String, String> keyvalueMap = StringUtils.splitString(allValue);
					String appid = keyvalueMap.get("appid");
					if (TextUtils.isEmpty(appid)) {
						return;
					}
					intent.setClass(
							context,PhoneAppInfoActivity.class
							/*App.getDevicesType() == App.PHONE ? PhoneAppInfoActivity.class
									: PadAppInfoActivity.class*/);
					//                AppsGroupElemInfoParcelable infos = new AppsGroupElemInfoParcelable(info);
					Apps.GroupElemInfo info = new Apps.GroupElemInfo();
					info.appId = Integer.valueOf(appid);
					info.posId = 0;
					intent.putExtra(ProtocolListener.KEY.GROUP_INFO, info);
					intent.putExtra(ProtocolListener.KEY.APP_POSITION, info.posId);
					intent.putExtra(StatisticManager.APP_POS_TYPE, ReportConstants.STATIS_TYPE.NOTIFY);
//					intent.putExtra(MTAConstants.KEY_DETAIL_PAGE_FROM,
//							MTAConstants.DETAIL_GAME_PAGE_CERCLE_ADV + orderIndex);
					context.startActivity(intent);
				} else if (!obj.isNull(SPECIAL)) {
					String allValue = obj.getString(SPECIAL);
					HashMap<String, String> keyvalueMap = StringUtils.splitString(allValue);
					String groupId = keyvalueMap.get("groupid");
					if (TextUtils.isEmpty(groupId)) {
						return;
					}
					GroupInfo groupInfo = DatabaseUtils.getGroupinfoByDB(CSACDatabaseHelper.GroupInfoColumns.GROUP_ID + "=" + groupId, null);
					TopicInfo topicInfo = new TopicInfo();
					topicInfo.mAppCount = groupInfo.recommWrod;
					topicInfo.mTopic = groupInfo.groupName;
					topicInfo.mTip = groupInfo.groupDesc;
					topicInfo.mPicUrl = groupInfo.groupPicUrl;
					intent.putExtra(ProtocolListener.KEY.TOPIC_INFO, topicInfo);
					intent.putExtra(ProtocolListener.KEY.MAIN_TYPE, ProtocolListener.MAIN_TYPE.TOPIC);
					intent.putExtra(ProtocolListener.KEY.ITEM_TYPE, ProtocolListener.ITEM_TYPE.UNSHOW_SNAPSHOT);
					intent.putExtra(ProtocolListener.KEY.GROUP_ID, groupInfo.groupId);
					intent.putExtra(ProtocolListener.KEY.GROUP_CLASS, groupInfo.groupClass);
					intent.putExtra(ProtocolListener.KEY.GROUP_TYPE, groupInfo.groupType);
					intent.putExtra(ProtocolListener.KEY.ORDERBY, groupInfo.orderType);
					intent.putExtra(ProtocolListener.KEY.CATEGORY_NAME,
							groupInfo.groupName);
//					intent.putExtra(ProtocolListener.KEY.ORDERBY, info.jumpOrderType);
					intent.putExtra(StatisticManager.APP_POS_TYPE, ReportConstants.STATIS_TYPE.NOTIFY);
					intent.setClass(context, GroupAppListActivity.class);
					context.startActivity(intent);
				} else if (!obj.isNull(GROUPTYPE)) {
					String allValue = obj.getString(GROUPTYPE);
					HashMap<String, String> keyvalueMap = StringUtils.splitString(allValue);
					String groupId = keyvalueMap.get("groupid");
					String groupType = keyvalueMap.get("grouptype");
					String orderType = keyvalueMap.get("ordertype");
					GroupInfo groupInfo = null;
					if (!TextUtils.isEmpty(groupId)) {
						//跳转分类
						groupInfo = DatabaseUtils.getGroupinfoByDB(CSACDatabaseHelper.GroupInfoColumns.GROUP_ID + "=" + groupId, null);
					}else if (!TextUtils.isEmpty(groupType) && !TextUtils.isEmpty(orderType)) {
						String selection = CSACDatabaseHelper.GroupInfoColumns.GROUP_TYPE + " =? and "+ CSACDatabaseHelper.GroupInfoColumns.ORDER_TYPE + " =?";
						String [] selectionArgs = new String[] {groupType, orderType};
						groupInfo = DatabaseUtils.getGroupinfoByDB(selection, selectionArgs);
					}else {
						return;
					}
					intent.putExtra(ProtocolListener.KEY.GROUP_ID, groupInfo.groupId);
					intent.putExtra(ProtocolListener.KEY.GROUP_CLASS, groupInfo.groupClass);
					intent.putExtra(ProtocolListener.KEY.GROUP_TYPE, groupInfo.groupType);
					intent.putExtra(ProtocolListener.KEY.ORDERBY, groupInfo.orderType);
					intent.putExtra(ProtocolListener.KEY.CATEGORY_NAME, groupInfo.groupName);
					intent.putExtra(ProtocolListener.KEY.MAIN_TYPE, ProtocolListener.MAIN_TYPE.JING_PIN);
					intent.putExtra(StatisticManager.APP_POS_TYPE, ReportConstants.STATIS_TYPE.NOTIFY);
					intent.setClass(context, GroupAppListActivity.class);
					context.startActivity(intent);
				} else if (!obj.isNull(LINK)) {
					//webview加载
//					String url = obj.getString(LINK);
//					intent.setClass(context, Html5HelpActivity.class);
//					intent.putExtra(StatisticManager.KEY_HTML5_HELP_TITLE,
//							context.getResources().getString(R.string.user_aggrement));
//					intent.putExtra(StatisticManager.KEY_HTML5_HELP_URL,
//							url);
//					context.startActivity(intent);
					//浏览器加载
					String allValue = obj.getString(LINK);
					HashMap<String, String> keyvalueMap = StringUtils.splitString(allValue);
					String url = keyvalueMap.get("url");
					if (TextUtils.isEmpty(url)) {
						return;
					}
					url = URLDecoder.decode(url);
					intent.setAction("android.intent.action.VIEW");
					Uri content_url = Uri.parse(url);
					intent.setData(content_url);
					context.startActivity(intent);

				} else if (!obj.isNull(DOWNLOAD)) {
					//暂时不处理下载，下载需要考虑移动网络还是wifi因素--oddshou
					String appinfo = obj.getString(DOWNLOAD);

				} else if (!obj.isNull(RECOMM)){
					//跳转官方推荐
					intent.setClass(context, HomePageActivity.class);
					intent.putExtra(HomePageActivity.KEY_GOTO_RECOMM, true);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onRegisterResult(Context context, int errorCode,
								 XGPushRegisterResult message) {
		// TODO Auto-generated method stub
//		if (context == null || message == null) {
//			return;
//		}
//		String text = "";
//		if (errorCode == XGPushBaseReceiver.SUCCESS) {
//			text = message + "注册成功";
//			// 在这里拿token
//			String token = message.getToken();
//		} else {
//			text = message + "注册失败，错误码：" + errorCode;
//		}
//		Log.d(LogTag, text);
//		show(context, text);
	}

	// 消息透传
	@Override
	public void onTextMessage(Context context, XGPushTextMessage message) {
		// TODO Auto-generated method stub
//		String text = "收到消息:" + message.toString();
//		// 获取自定义key-value
//		String customContent = message.getCustomContent();
//		if (customContent != null && customContent.length() != 0) {
//			try {
//				JSONObject obj = new JSONObject(customContent);
//				// key1为前台配置的key
//				if (!obj.isNull("key")) {
//					String value = obj.getString("key");
//					Log.d(LogTag, "get custom value:" + value);
//				}
//				// ...
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//		// APP自主处理消息的过程...
//		Log.d(LogTag, text);
//		show(context, text);
	}

}
