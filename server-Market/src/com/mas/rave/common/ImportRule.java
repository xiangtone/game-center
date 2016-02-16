package com.mas.rave.common;

import java.io.File;

public interface ImportRule {
	
	/**
	 * 字段是否需要校验
	 * @param fieldName
	 * @return
	 */
	public Object getResource();
	
	/**
	 * 检查字段是否合法，如果不合法则返回不合法的原因，合法则返回null
	 * @param fieldName
	 * @param value 字段值
	 * @return 返回null则检查通过，否则不通过
	 */
	public String checkField(String fieldName,String value,Object resource);
	
	/**
	 * 移动文件到指定目录
	 * @param srcFile
	 * @param resource
	 * @return
	 */
	//public String moveFile(File srcFile,Object resource);
}
