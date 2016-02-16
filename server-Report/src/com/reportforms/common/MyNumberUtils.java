package com.reportforms.common;

public abstract class MyNumberUtils {

	/**
	 * 比较两个Integer类型的大小.
	 * <p>
	 * 
	 * @param i1 the first number
	 * @param i2 the second number
	 * @return 如果任何一个参数为null，则返回<code>2</code>,如果i1大于i2，则返回1,如果i1小于i2，则
	 * 返回-1，如果相等，则返回0
	 */
	public static int compareInteger(Integer i1,Integer i2){
		if(i1 == null || i2 == null)
			return 2;
		return i1.compareTo(i2);
	}
	
	public static void main(String[] args){
		Integer i = null;
		Integer j = null;
		System.out.println(MyNumberUtils.compareInteger(i, j));
	}
}
