package com.mas.rave.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.mas.rave.main.vo.Menu;

/**
 * 创建主页面左侧菜单项
 * 
 * @author lisong.lan
 * 
 */
@SuppressWarnings("unchecked")
public class MenuUtil {

	/**
	 * 构建菜单项
	 * 
	 * @param list
	 * @param request
	 * @return
	 */
	public static String parseMenus(List<Menu> list, HttpServletRequest request) {
		StringBuilder strb = new StringBuilder();
		if (null != list && !list.isEmpty()) {
			// Collections.sort(list);
			strb.append("<ul class='menu_one'>");
			// 递归创建菜单树
			create(list, strb, 0, request);
			strb.append("</ul>");
		} else {
			strb.append("");
		}
		return strb.toString();
	}

	/**
	 * 递归遍历集合,创建菜单树
	 * 
	 * @param list
	 * @param strb
	 * @param parentId
	 */
	private static void create(List<Menu> list, StringBuilder strb, int parentId, HttpServletRequest request) {
		for (Menu menu : list) {
			if (null == menu.getParentId() || menu.getParentId().intValue() != parentId) {
				continue;
			}
			if (parentId != 0) {
				strb.append("<li id='").append(menu.getCode()).append("'>");
			} else {
				strb.append("<li class='menu_one'>");
				strb.append("<a href='#' class='one_a'><span class='nav_1'><img src='" + request.getContextPath() + "/static/" + menu.getIcon() + "'/></span><small class='m_jt'></small>"
						+ menu.getName() + "</a>");
				strb.append("<ul class='menu_two'  id='").append(menu.getCode()).append("1'>");
			}
			// strb.append("<span>");
			// 父级节点没有URL,最终子节点才有URL
			if (StringUtils.isNotEmpty(menu.getUri())) {
				strb.append("<a href='").append(request.getContextPath()).append("/").append(menu.getUri()).append("' class='menu_a'>");
				strb.append("<span id='" + menu.getCode() + "1'>");
				strb.append(menu.getName());
				strb.append("</span>");
				strb.append("</a>");
			} else {
				// strb.append(menu.getName());
			}
			// strb.append("</span>");
			create(list, strb, menu.getId(), request);
			if (parentId != 0) {
				strb.append("</li>");
			} else {
				strb.append("</ul></li>");
			}
		}

	}

}
