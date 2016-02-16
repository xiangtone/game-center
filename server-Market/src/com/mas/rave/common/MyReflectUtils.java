package com.mas.rave.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class MyReflectUtils {

	public static String[] complementProperites(Class<?> targetType,String[] properites){
		Field[] fields = targetType.getDeclaredFields();
		List<String> result = new ArrayList<String>();
		if(properites == null)
			properites = new String[0];
		for(Field field : fields){
			if(Modifier.isStatic(field.getModifiers()))
				continue;
			String fieldName = field.getName();
			boolean addFlag = true;
			for(String property : properites){
				if(property.equalsIgnoreCase(fieldName)){
					addFlag = false;
					break;
				}
			}
			if(addFlag)
				result.add(fieldName);
		}
		return result.toArray(new String[result.size()]);
	}
	
}
