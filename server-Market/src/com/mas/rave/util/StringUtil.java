package com.mas.rave.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class StringUtil {
	/**
	 * 将一个字符串的首字母改为大写或者小写
	 * 
	 * @param srcString
	 *            源字符串
	 * @param flag
	 *            大小写标识，ture小写，false大些
	 * @return 改写后的新字符串
	 */
	public static String toLowerCaseInitial(String srcString, boolean flag) {
		StringBuilder sb = new StringBuilder();
		if (flag) {
			sb.append(Character.toLowerCase(srcString.charAt(0)));
		} else {
			sb.append(Character.toUpperCase(srcString.charAt(0)));
		}
		sb.append(srcString.substring(1));
		return sb.toString();
	}

	/**
	 * 将一个字符串按照句点（.）分隔，返回最后一段
	 * 
	 * @param clazzName
	 *            源字符串
	 * @return 句点（.）分隔后的最后一段字符串
	 */
	public static String getLastName(String clazzName) {
		String[] ls = clazzName.split("\\.");
		return ls[ls.length - 1];
	}

	/**
	 * 格式化文件路径，将其中不规范的分隔转换为标准的分隔符,并且去掉末尾的"/"符号。
	 * 
	 * @param path
	 *            文件路径
	 * @return 格式化后的文件路径
	 */
	public static String formatPath(String path) {
		String reg0 = "\\\\＋";
		String reg = "\\\\＋|/＋";
		String temp = path.trim().replaceAll(reg0, "/");
		temp = temp.replaceAll(reg, "/");
		if (temp.endsWith("/")) {
			temp = temp.substring(0, temp.length() - 1);
		}
		if (System.getProperty("file.separator").equals("\\")) {
			temp = temp.replace('/', '\\');
		}
		return temp;
	}

	/**
	 * 格式化文件路径，将其中不规范的分隔转换为标准的分隔符,并且去掉末尾的"/"符号(适用于FTP远程文件路径或者Web资源的相对路径)。
	 * 
	 * @param path
	 *            文件路径
	 * @return 格式化后的文件路径
	 */
	public static String formatPath4Ftp(String path) {
		String reg0 = "\\\\＋";
		String reg = "\\\\＋|/＋";
		String temp = path.trim().replaceAll(reg0, "/");
		temp = temp.replaceAll(reg, "/");
		if (temp.endsWith("/")) {
			temp = temp.substring(0, temp.length() - 1);
		}
		return temp;
	}

	/**
	 * 获取文件父路径
	 * 
	 * @param path
	 *            文件路径
	 * @return 文件父路径
	 */
	public static String getParentPath(String path) {
		return new File(path).getParent();
	}

	/**
	 * 获取相对路径
	 * 
	 * @param fullPath
	 *            全路径
	 * @param rootPath
	 *            根路径
	 * @return 相对根路径的相对路径
	 */
	public static String getRelativeRootPath(String fullPath, String rootPath) {
		String relativeRootPath = null;
		String _fullPath = formatPath(fullPath);
		String _rootPath = formatPath(rootPath);

		if (_fullPath.startsWith(_rootPath)) {
			relativeRootPath = fullPath.substring(_rootPath.length());
		} else {
			throw new RuntimeException("要处理的两个字符串没有包含关系，处理失败！");
		}
		if (relativeRootPath == null)
			return null;
		else
			return formatPath(relativeRootPath);
	}

	/**
	 * 获取当前系统换行符
	 * 
	 * @return 系统换行符
	 */
	public static String getSystemLineSeparator() {
		return System.getProperty("line.separator");
	}

	/**
	 * 将用“|”分隔的字符串转换为字符串集合列表，剔除分隔后各个字符串前后的空格
	 * 
	 * @param series
	 *            将用“|”分隔的字符串
	 * @return 字符串集合列表
	 */
	public static List<String> series2List(String series) {
		return series2List(series, "\\|");
	}

	/**
	 * 将用正则表达式regex分隔的字符串转换为字符串集合列表，剔除分隔后各个字符串前后的空格
	 * 
	 * @param series
	 *            用正则表达式分隔的字符串
	 * @param regex
	 *            分隔串联串的正则表达式
	 * @return 字符串集合列表
	 */
	private static List<String> series2List(String series, String regex) {
		List<String> result = new ArrayList<String>();
		if (series != null && regex != null) {
			for (String s : series.split(regex)) {
				if (s.trim() != null && !s.trim().equals(""))
					result.add(s.trim());
			}
		}
		return result;
	}

	/**
	 * @param strList
	 *            字符串集合列表
	 * @return 通过“|”串联为一个字符串
	 */
	public static String list2series(List<String> strList) {
		StringBuffer series = new StringBuffer();
		for (String s : strList) {
			series.append(s).append("|");
		}
		return series.toString();
	}

	public static String listInteger2String(List<Integer> strList) {
		StringBuffer series = new StringBuffer();
		for (Integer s : strList) {
			series.append(s).append(",");
		}
		if (series != null && !series.toString().equals(""))
			return series.substring(0, series.length() - 1);
		else
			return "";
	}

	/**
	 * 将字符串的首字母转为小写
	 * 
	 * @param resStr
	 *            源字符串
	 * @return 首字母转为小写后的字符串
	 */
	public static String firstToLowerCase(String resStr) {
		if (resStr == null) {
			return null;
		} else if ("".equals(resStr.trim())) {
			return "";
		} else {
			StringBuffer sb = new StringBuffer();
			Character c = resStr.charAt(0);
			if (Character.isLetter(c)) {
				if (Character.isUpperCase(c))
					c = Character.toLowerCase(c);
				sb.append(resStr);
				sb.setCharAt(0, c);
				return sb.toString();
			}
		}
		return resStr;
	}

	/**
	 * 将字符串的首字母转为大写
	 * 
	 * @param resStr
	 *            源字符串
	 * @return 首字母转为大写后的字符串
	 */
	public static String firstToUpperCase(String resStr) {
		if (resStr == null) {
			return null;
		} else if ("".equals(resStr.trim())) {
			return "";
		} else {
			StringBuffer sb = new StringBuffer();
			Character c = resStr.charAt(0);
			if (Character.isLetter(c)) {
				if (Character.isLowerCase(c))
					c = Character.toUpperCase(c);
				sb.append(resStr);
				sb.setCharAt(0, c);
				return sb.toString();
			}
		}
		return resStr;
	}

	// 检测是否有中文
	public static boolean chineseValid(String s) {
		int length = s.length();
		byte[] b;
		for (int i = 0; i < length; i++) {
			b = s.substring(i).getBytes();
			if ((b[0] & 0xff) > 128)
				return true;
		}
		return false;
	}

	// 字符大小比较
	public static int versionsCompare(String v1, String v2) {
		return v1.compareTo(v2);
	}

	/**
	 * 字符串转换 把 # 转换成<br>
	 * 
	 * @param str
	 *            >
	 * @return
	 */
	public static String convertBlankLinesToBr(String str) {
		if (StringUtils.isNotEmpty(str)) {
			return str.replaceAll("#", "<br/>");
		}
		return str;
	}

	/**
	 * 字符串转换 把 \r\n 转换成#
	 * 
	 * @param str
	 * <br>
	 * @return
	 */
	public static String convertBlankLinesToHashSymbol(String str) {
		if (StringUtils.isNotEmpty(str)) {
			return str.replaceAll("\r\n", "#");
		}
		return str;
	}

	public static String convertBlankLinesToHashSymbolBr(String str) {
		if (StringUtils.isNotEmpty(str)) {
			return str.replaceAll("\r\n", "<br/>");
		}
		return str;
	}

	public static String convertBlankLinesToHashSymbolBrTo(String str) {
		if (StringUtils.isNotEmpty(str)) {
			return str.replaceAll("<br/>", "\r\n");
		}
		return str;
	}

	
	public static String convertNtoBr(String str) {
		if (StringUtils.isNotEmpty(str)) {
			return str.replaceAll("\n", "<br/>");
		}
		return str;
	}

	public static String convertBrToN(String str) {
		if (StringUtils.isNotEmpty(str)) {
			return str.replaceAll("<br/>", "\n");
		}
		return str;
	}
	
	
	/**
	 * 字符串转换 把# 转换成/r/n
	 * 
	 * @param str
	 * @return
	 */
	public static String convertHashSymbolToBlankLines(String str) {
		if (StringUtils.isNotEmpty(str)) {
			return str.replaceAll("#", "\r\n");
		}
		return str;
	}

	public static void main(String[] args) {
		System.out.println(chineseValid("aaa"));
	}

}
