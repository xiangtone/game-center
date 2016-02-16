package com.reportforms.vo;

import com.reportforms.model.Role;



public class RoleVO extends Role{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1337658628199272996L;
	
	private boolean checked;

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	

}
