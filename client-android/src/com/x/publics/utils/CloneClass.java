package com.x.publics.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 
 
 *
 */
public class CloneClass {

	/**
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Object clone(Object obj) throws Exception {
		Field[] fields = obj.getClass().getDeclaredFields();
		Object newObj = obj.getClass().newInstance();
		for (int i = 0, j = fields.length; i < j; i++) {
			String propertyName = fields[i].getName();
			Object propertyValue = getProperty(obj, propertyName);
			setProperty(newObj, propertyName, propertyValue);
		}
		return newObj;
	}

	/**
	 * 
	 * @param obj
	 * @param targetObj
	 * @return
	 * @throws Exception
	 */
	public static Object clone(Object obj, Object targetObj) throws Exception {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int i = 0, j = fields.length; i < j; i++) {
			String propertyName = fields[i].getName();
			Object propertyValue = getProperty(obj, propertyName);
			if (propertyValue != null)
				setProperty(targetObj, propertyName, propertyValue);
		}
		return targetObj;
	}

	/**
	 * 
	 * @param obj
	 * @param targetObj
	 * @return
	 * @throws Exception
	 */
	public static Object cloneSuper(Object obj, Object targetObj) throws Exception {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int i = 0, j = fields.length; i < j; i++) {
			String propertyName = fields[i].getName();
			Object propertyValue = getProperty(obj, propertyName);
			setSuperProperty(targetObj, propertyName, propertyValue);
		}
		return targetObj;
	}

	// 反射调用getter方法，得到field的值
	private static Object getProperty(Object bean, String propertyName) {
		Class clazz = bean.getClass();
		try {
			Field field = clazz.getDeclaredField(propertyName);
			Method method = clazz.getDeclaredMethod(getGetterName(field.getName()), new Class[] {});
			return method.invoke(bean, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 根据field名，得到getter方法名
	private static String getGetterName(String propertyName) {
		String method = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		return method;
	}

	// 反射调用setter方法，进行赋值
	private static Object setProperty(Object bean, String propertyName, Object value) {
		Class clazz = bean.getClass();
		try {
			Field field = clazz.getDeclaredField(propertyName);
			Method method = clazz.getDeclaredMethod(getSetterName(field.getName()), new Class[] { field.getType() });
			return method.invoke(bean, new Object[] { value });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 反射调用setter方法，进行赋值
	private static Object setSuperProperty(Object bean, String propertyName, Object value) {
		Class clazz = bean.getClass().getSuperclass();
		try {
			Field field = clazz.getDeclaredField(propertyName);

			Method method = clazz.getDeclaredMethod(getSetterName(field.getName()), new Class[] { field.getType() });
			return method.invoke(bean, new Object[] { value });
		} catch (Exception e) {
		}
		return null;
	}

	// 根据field名，得到setter方法名
	private static String getSetterName(String propertyName) {
		String method = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		return method;
	}
}
