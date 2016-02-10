/**   
* @Title: PushNotificationBean.java
* @Package com.x.business.notification
* @Description: TODO(用一句话描述该文件做什么)

* @date 2015-8-5 下午3:31:26
* @version V1.0   
*/


package com.x.publics.model;

import android.graphics.Bitmap;

/**
 * @ClassName: PushNotificationBean
 * @Description: 通知 bean
 
 * @date 2015-8-5 下午3:31:26
 * 
 */

public class PushNotificationBean {
	/**推送信息:标题*/
	private String notificationTitle;
	/**推送信息:综述 描述*/
	private String notificationSummary;
	/**推送信息:信息图片*/
	private Bitmap  notificationPic;
	/**推送信息:推送 big logo*/
	private Bitmap  notificationBigIcon;
	/**推送信息:推送 small logo*/
	private Bitmap  notificationSmallIcon;
	/**推送信息:推送 时间*/
	private String  notificationTime;
	/**推送：  intent  */
	private  Object  intentObject;
	/**推送： 打开链接Type*/
	private int  openType;
	/**推送： 推送类型  * 0：普通文字推送  *1：带图片的推送 */
	private int  mode;
	
	/** 
	 * @return notificationTitle 
	 */
	
	public String getNotificationTitle() {
		return notificationTitle;
	}
	/** 
	 * @param notificationTitle 要设置的 notificationTitle 
	 */
	
	public void setNotificationTitle(String notificationTitle) {
		this.notificationTitle = notificationTitle;
	}
	/** 
	 * @return notificationSummary 
	 */
	
	public String getNotificationSummary() {
		return notificationSummary;
	}
	/** 
	 * @param notificationSummary 要设置的 notificationSummary 
	 */
	
	public void setNotificationSummary(String notificationSummary) {
		this.notificationSummary = notificationSummary;
	}
	/** 
	 * @return notificationPic 
	 */
	
	public Bitmap getNotificationPic() {
		return notificationPic;
	}
	/** 
	 * @param notificationPic 要设置的 notificationPic 
	 */
	
	public void setNotificationPic(Bitmap notificationPic) {
		this.notificationPic = notificationPic;
	}
	/** 
	 * @return notificationBigIcon 
	 */
	
	public Bitmap getNotificationBigIcon() {
		return notificationBigIcon;
	}
	/** 
	 * @param notificationBigIcon 要设置的 notificationBigIcon 
	 */
	
	public void setNotificationBigIcon(Bitmap notificationBigIcon) {
		this.notificationBigIcon = notificationBigIcon;
	}
	/** 
	 * @return notificationSmallIcon 
	 */
	
	public Bitmap getNotificationSmallIcon() {
		return notificationSmallIcon;
	}
	/** 
	 * @param notificationSmallIcon 要设置的 notificationSmallIcon 
	 */
	
	public void setNotificationSmallIcon(Bitmap notificationSmallIcon) {
		this.notificationSmallIcon = notificationSmallIcon;
	}
	/** 
	 * @return notificationTime 
	 */
	
	public String getNotificationTime() {
		return notificationTime;
	}
	/** 
	 * @param notificationTime 要设置的 notificationTime 
	 */
	
	public void setNotificationTime(String notificationTime) {
		this.notificationTime = notificationTime;
	}
	/** 
	 * @return intentObject 
	 */
	
	public Object getIntentObject() {
		return intentObject;
	}
	/** 
	 * @param intentObject 要设置的 intentObject 
	 */
	
	public void setIntentObject(Object intentObject) {
		this.intentObject = intentObject;
	}
	/** 
	* @return openType 
	*/ 
	
	
	public int getOpenType() {
		return openType;
	}
	/** 
	* @param openType 要设置的 openType 
	*/ 
	
	public void setOpenType(int openType) {
		this.openType = openType;
	}
	/** 
	 * @return mode 
	 */
	
	public int getMode() {
		return mode;
	}
	/** 
	 * @param mode 要设置的 mode 
	 */
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	
}
