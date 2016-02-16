package com.mas.rave.service;

import java.util.List;
import java.util.Map;

import com.mas.rave.main.vo.Menu;
import com.mas.rave.main.vo.MenuType;


public interface MenuService {
	
	MenuType getMenuTypeById(Integer id);
	
	void addType(MenuType menuType);
	
	void deleteType(Integer id);
	
	void upType(Integer id);
	
	void downType(Integer id);
	
	Menu getMenuById(Integer id);
	
	void addMenu(Integer typeId,Menu menu);
	
	void deleteMenu(Integer id);
	
	void upMenu(Integer id);
	
	void downMenu(Integer id);
	
	List<Menu> getMenusOfType(Integer typeId);
	
	List<Menu> getMenus();
	
	List<MenuType> getMenuTypes();
	
	void updateMenuType(MenuType menuType);
	
	void updateMenu(Menu menu);
	
	List<Menu> queryByParentId(List<String> list);

	List<Menu> queryByNameAndCode(Map<String, String> paramMap);
	
    List<Menu>   queryMenusByroleId (Integer roleId );
	
}
