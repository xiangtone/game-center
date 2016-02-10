/**   
* @Title: SkinAttentionRequest.java
* @Package com.x.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-12-3 下午1:51:33
* @version V1.0   
*/


package com.x.publics.http.model;

import com.x.publics.utils.Constan;

/**
* @ClassName: SkinAttentionRequest
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-12-3 下午1:51:33
* 
*/

public class SkinAttentionRequest extends CommonRequest{

	public int skinCode;
	
	public SkinAttentionRequest() {
		super(Constan.Rc.SKIN_ATTENTION, Constan.SIGN);
	}

	public int getSkinCode() {
		return skinCode;
	}

	public void setSkinCode(int skinCode) {
		this.skinCode = skinCode;
	}

}
