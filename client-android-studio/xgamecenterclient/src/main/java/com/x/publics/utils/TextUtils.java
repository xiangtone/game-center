package com.x.publics.utils;

public class TextUtils {

	public static boolean isEmpty(String str) {
		if (str == null || str.equals("")) {
			return true;
		}
		return false;
	}

	public static boolean equal(String src, String target) {
		if (src == null)
			return false;
		if (target == null)
			return false;
		return src.equals(target);
	}
}
