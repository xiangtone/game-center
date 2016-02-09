/**   
* @Title: NoticeActivity.java
* @Package com.mas.amineappstore.business.notice
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-10-11 下午5:25:40
* @version V1.0   
*/

package com.x.business.notice;

import android.app.Activity;
import android.os.Bundle;

import com.x.publics.utils.Utils;

/**
* @ClassName: NoticeActivity
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-10-11 下午5:25:40
* 
*/

public class NoticeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		finish();
	}

	/** 
	* @Title: initData 
	* @Description: TODO 
	* @param     
	* @return void    
	*/
	private void initData() {
		String appName = getIntent().getStringExtra("appName");
		String packageName = getIntent().getStringExtra("packageName");
		Utils.shareMsg(this, appName, packageName);
	}

}
