package com.reportforms.vo;

import com.reportforms.model.Menu;


public class MenuVO extends Menu{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3506148156409067115L;

	
	private boolean checked;


	public boolean isChecked() {
		return checked;
	}


	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	
}
