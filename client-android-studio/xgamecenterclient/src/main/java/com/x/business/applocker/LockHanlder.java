/**   
* @Title: LockHanlder.java
* @Package com.x.business.applocker
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-10-27 下午4:28:21
* @version V1.0   
*/


package com.x.business.applocker;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * @ClassName: LockHanlder
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-10-27 下午4:28:21
 * 
 */

public class LockHanlder extends Handler {

	private static final long REPEAT_TIME = 500L;
	public Context context ;
	
	public LockHanlder(Context context) {
		super();
		this.context = context;
		this.sendEmptyMessageDelayed(0,REPEAT_TIME) ;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch (msg.what) {
		case 0:
			LockTask.getInstance(context).run() ;
			this.sendEmptyMessageDelayed(0,REPEAT_TIME) ;
			break;
		default:
			break;
		}
	}

	

}
