package com.reportforms.common.premission;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.reportforms.model.Operation;
import com.reportforms.model.User;
import com.reportforms.service.UserService;




@Aspect
public class PermissionAspect {
	
	private Logger logger = Logger.getLogger(PermissionAspect.class);
	
	@Autowired
	private UserService userService;

	@Before("execution(* com.reportforms.action.*.*(..))")
	public void checkPermission(JoinPoint point) {
		PermissionAnnotation permissionAnnotation = null;
		MethodSignature methodSig = (MethodSignature) point.getStaticPart()
				.getSignature();
		Method targetMethod = methodSig.getMethod();
		permissionAnnotation = targetMethod
				.getAnnotation(PermissionAnnotation.class);
		if (permissionAnnotation == null)
			return;

		HttpSession s = (HttpSession) RequestContextHolder
				.currentRequestAttributes().resolveReference(
						RequestAttributes.REFERENCE_SESSION);
		User user = (User) s.getAttribute("loginUser");
		if (user == null)
			throw new RuntimeException("请先登录!!!");

		String operationCode = permissionAnnotation.code();
		logger.info("operationCode:"+operationCode);

		List<Operation> allOperations = userService.getOperationsByUserId(user.getId());
		for(Operation o : allOperations){
			if(StringUtils.trimToEmpty(operationCode).equalsIgnoreCase(o.getCode()))
				return;
		}
		throw new PermissionException("user.id : " + user.getId() + "没有 "
					+ operationCode + " 操作权限");
		}

	
	@AfterThrowing(pointcut="execution(* com.reportforms.action.*.*(..))",throwing="ex")
	public void logerr(Exception ex) {
		
		Log logger = LogFactory.getLog(PermissionAspect.class);
		logger.error("",ex);
	}
}
