/**   
* @Title: AppDownloadLogRequest.java
* @Package com.x.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2015-3-20 上午10:14:23
* @version V1.0   
*/

package com.x.publics.http.model;

import com.x.publics.utils.Constan;

/**
* @ClassName: AppDownloadLogRequest
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2015-3-20 上午10:14:23
* 
*/

public class AppDownloadLogRequest extends CommonRequest {

	public AppDownloadLogRequest() {
		super(Constan.Rc.APP_DOWNLOAD_LOG, Constan.SIGN);
	}

}
