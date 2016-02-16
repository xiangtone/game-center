package com.mas.rave.vo;

import com.mas.rave.main.vo.Operation;


public class OperationVO extends Operation{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9122044881951953678L;

	private boolean checked;

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
