package com.reportforms.vo;

import com.reportforms.model.Operation;


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
