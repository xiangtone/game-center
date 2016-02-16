package com.reportforms.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyCollectionUtils {

	public static interface Equaltor<T, E> {
		boolean equal(T o1, E o2);
	}

	public static class EqualtorImpl<T, E> implements Equaltor<T, E> {
		@Override
		public boolean equal(T o1, E o2) {
			if (o1 == null || o2 == null)
				return false;
			return o1.equals(o2);
		}
	}

	public static List<Integer> array2List(String[] strs) {
		List<Integer> result = new ArrayList<Integer>();
		if (strs == null)
			return result;
		for (String s : strs) {
			result.add(Integer.parseInt(s));
		}
		return result;
	}

	public static List<Integer> array2List(int[] ints) {
		List<Integer> result = new ArrayList<Integer>();
		if (ints == null)
			return result;
		for (int i : ints) {
			result.add(i);
		}
		return result;
	}

	public static int[] list2Array(List<Integer> ints) {
		if (ints == null)
			return new int[] {};
		int[] result = new int[ints.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = ints.get(i);
		}
		return result;
	}

	/**
	 * 判断集合是否为空
	 * 
	 * @param List
	 * @return
	 */
	public static <T> boolean isEmpty(List<T> List) {
		return (List == null) || (List.size() == 0);
	}

	/**
	 * 
	 * @see #isEmpty(List)
	 * @param List
	 * @return
	 */
	public static <T> boolean isNotEmpty(List<T> List) {
		return !isEmpty(List);
	}

	/**
	 * 集合差值操作
	 * 
	 * <pre>
	 * 	List&lt;Integer&gt; c1 = {1,2,3,4,5};
	 * 	List&lt;Integer&gt; c2 = {2,5};
	 * 	List&lt;Integer&gt; result = ListUtils.sub(c1,c2);
	 * 	result = {1,3,4};
	 * </pre>
	 * 
	 * @param c1
	 * @param c2
	 * @return 如果结果集合为空，则返size为0的集合对象，不会返回<code>null</code>
	 */

	public static <T, E> List<T> sub(List<T> c1, List<E> c2,
			Equaltor<T, E> equaltor) {
		if (isEmpty(c1))
			return Collections.emptyList();
		if (isEmpty(c2))
			return c1;
		List<T> diff = null;
		try {
			diff = (List<T>) c1.getClass().newInstance();
			diff.clear();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		for (T out : c1) {
			boolean flag = true;
			for (E in : c2) {
				if (equaltor.equal(out, in)) {
					flag = false;
					break;
				}
			}
			if (flag)
				diff.add(out);
		}
		return diff;
	}

	/**
	 * 交集操作
	 * 
	 * @param <T>
	 * @param <E>
	 * @param c1
	 * @param c2
	 * @param equaltor
	 * @return 如果结果集合为空，则返size为0的集合对象，不会返回<code>null</code>
	 */
	@SuppressWarnings("unchecked")
	public static <T, E> List<T> intersect(List<T> c1, List<E> c2,
			Equaltor<T, E> equaltor) {
		if (isEmpty(c1) || isEmpty(c2))
			return Collections.EMPTY_LIST;
		List<T> intersect = null;
		try {
			intersect = c1.getClass().newInstance();
			intersect.clear();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		for (T out : c1) {
			boolean flag = true;
			for (E in : c2) {
				if (equaltor.equal(out, in)) {
					flag = false;
					break;
				}
			}
			if (!flag)
				intersect.add(out);
		}
		return intersect;
	}

	public static <T> List<T> arrayToList(T[] lst) {
		if (lst == null)
			return null;
		List<T> list = new ArrayList<T>();
		for (T t : lst) {
			list.add(t);
		}
		return list;
	}

	public static <T> List<T> removeDuplicates(List<T> list,
			String... propertyName) {
		T t;

		return null;
	}

	public static Integer[] splitToIntArray(String ids) {
		String[] idStrArray = ids.split(",");
		Integer[] idIntArray = new Integer[idStrArray.length];
		for (int i = 0; i < idStrArray.length; i++) {
			idIntArray[i] = Integer.valueOf(idStrArray[i]);
		}
		return idIntArray;
	}
	
}
