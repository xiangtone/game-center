package com.x.publics.model;

import java.io.Serializable;

/**
 * 
* @ClassName: CountryBean
* @Description: 国家实体类

* @date 2014-5-20 下午5:03:13
*
 */
public class CountryBean implements Serializable {

	private static final long serialVersionUID = -8918955512385824003L;
	private int id;
	private String name;
	private String url;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
