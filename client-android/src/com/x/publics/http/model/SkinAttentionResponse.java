/**   
* @Title: SkinAttentionResponse.java
* @Package com.mas.amineappstore.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-12-3 下午1:56:38
* @version V1.0   
*/

package com.x.publics.http.model;

/**
* @ClassName: SkinAttentionResponse
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-12-3 下午1:56:38
* 
*/

public class SkinAttentionResponse extends CommonResponse {

	private int skinCode;
	private boolean skinAttention;

	public int getSkinCode() {
		return skinCode;
	}

	public void setSkinCode(int skinCode) {
		this.skinCode = skinCode;
	}

	public boolean isSkinAttention() {
		return skinAttention;
	}

	public void setSkinAttention(boolean skinAttention) {
		this.skinAttention = skinAttention;
	}
}
