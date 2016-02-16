package com.reportforms.service;

import java.util.List;

import com.reportforms.model.Menu;
import com.reportforms.model.MenuExample;
import com.reportforms.model.MenuType;


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
	
	List<MenuType> getMenuTypes();
	
	void updateMenuType(MenuType menuType);
	
	void updateMenu(Menu menu);
	
	public List<Menu> queryByCriteria(Menu menu);
	
	public List<Menu> queryAll(Menu menu);
	
	public Integer countByExample(MenuExample example);

}
