package com.mas.rave.common.premission;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mas.rave.main.vo.Operation;
import com.mas.rave.service.UserService;

public class PermissionFunctionTag {

	private static UserService userService;

	private final static Object lock = new Object();

	public static boolean userHasSpecialPermission(Integer userId,
			String permissionCode) {
		if (userService == null) {
			synchronized (lock) {
				if (userService == null) {
					HttpSession s = (HttpSession) RequestContextHolder
							.currentRequestAttributes().resolveReference(
									RequestAttributes.REFERENCE_SESSION);
					ServletContext context = s.getServletContext();
					WebApplicationContext applicationContext = WebApplicationContextUtils
							.getWebApplicationContext(context);
					userService = applicationContext.getBean(UserService.class);
				}
			}
		}
		if (userService == null) {
			throw new RuntimeException("系统初始化失败!!!");
		}
		List<Operation> all = userService.getOperationsByUserId(userId);
		for (Operation o : all) {
			if (StringUtils.equalsIgnoreCase(o.getCode(), permissionCode))
				return true;
		}
		return false;
	}
}
