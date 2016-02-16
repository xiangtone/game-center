package com.reportforms.util;

import java.util.List;

import com.reportforms.model.Menu;

/**
 * 左侧权限菜单栏的公共处理类
 * @author lisong.lan
 *
 */
public class MenuUtil {
	
	/**
	 * 加载子级菜单
	 * @param parentMenu
	 * @return
	 */
	public static List<Menu> loadChildMenu(List<Menu> parentMenu){
		for (Menu menu1 : parentMenu) {
			if(null != menu1 && null != menu1.getParentId() && menu1.getParentId() == 0){
				for (Menu menu2 : parentMenu) {
					if(null != menu2 && null != menu2.getParentId() && menu2.getParentId() == menu1.getId()){
						menu1.getChildMenu().add(menu2);
					}
				}
			}
		}
		return parentMenu;
	}

}
