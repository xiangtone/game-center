package com.mas.rave.common.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.mas.rave.main.vo.User;

public class RequestUtils {
	public static int getInt(HttpServletRequest request, String paramName,
			int defaultValue) {
		try {
			int result = Integer.parseInt(request.getParameter(paramName));
			int max = getPageInt(request,"maxPage");
			// 如果输入负数或零
			if (result <= 0) {
				result = 1;
			}
			// 如果输入的页数超过最大页数,以最大页数显示
			if (max != 0 && result > max && paramName.equals("currentPage")) {
				result = max;
			}
			return result;
		} catch (NumberFormatException ex) {
			return defaultValue;
		}
	}

	public static int getPageInt(HttpServletRequest request,String paramName) {
		int page = 0;
		try {
			// 最大页数
			String val = request.getParameter(paramName);
			if (StringUtils.isNotEmpty(val)) {
				page = Integer.parseInt(val);
			}
		} catch (Exception ex) {
		}
		return page;
	}
}
