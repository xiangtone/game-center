/**   
* @Title: WebGameInterface.java
* @Package com.mas.amineappstore.business.webgame
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-8-26 下午2:53:44
* @version V1.0   
*/


package com.x.business.webgame;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.x.publics.utils.LogUtil;

/**
* @ClassName: WebGameInterface
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-8-26 下午2:53:44
* 
*/

public class WebGameInterface {
	private static final String TAG = "WebGameInterface";
	
	private Context context ;
	
	public WebGameInterface(Context context){
		this.context = context;
	}
	
	@JavascriptInterface
	public void share(String content) {
		zappShare(content);
		LogUtil.getLogger().d(TAG, content);
	}

	private void zappShare(String content) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share");
		shareIntent.putExtra(Intent.EXTRA_TEXT, content);
		Intent targetInent  = Intent.createChooser(shareIntent, "");
		
		targetInent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(targetInent);
	}

}
