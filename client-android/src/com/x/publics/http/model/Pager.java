/**   
* @Title: Pager.java
* @Package com.x.model
* @Description: TODO 

* @date 2014-1-14 下午07:37:13
* @version V1.0   
*/

package com.x.publics.http.model;

import com.x.publics.utils.Constan;

/**
* @ClassName: Pager
* @Description: TODO 

* @date 2014-1-14 下午07:37:13
* 
*/

public class Pager {

	public Pager(int pn) {
		this.pn = pn;
		this.ps = Constan.PAGE_SIZE;
	}

	public int pn;

	public int ps;

	public int getPn() {
		return pn;
	}

	public void setPn(int pn) {
		this.pn = pn;
	}

	public int getPs() {
		return ps;
	}

	public void setPs(int ps) {
		this.ps = ps;
	}

}
