package com.mas.rave.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 文件检测帮助类
 * 
 * @author liwei.sz
 * 
 */
public class CheckFileUtil implements FilenameFilter {
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public boolean accept(File dir, String name) {
		boolean bol = false;
		if (type.equals(Constant.APK_ADR)) {
			bol = true;
		} else if (type.equals(Constant.LOGO_ADR)) {
			bol = true;
		} else if (type.equals(Constant.IMG_ADR)) {
			bol = true;
		} else if (type.equals(Constant.APP_TXT)) {
			bol = true;
		}
		if (bol) {
			String suffix = FileAddresUtil.getSuffix(name);
			if (suffix.equals("0")) {
				bol = false;
			}
		}

		return bol;
	}
}
