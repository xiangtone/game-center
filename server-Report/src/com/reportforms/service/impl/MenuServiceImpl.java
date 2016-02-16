package com.reportforms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.reportforms.dao.MenuMapper;
import com.reportforms.dao.MenuTypeMapper;
import com.reportforms.dao.RoleMenuMapper;
import com.reportforms.model.Menu;
import com.reportforms.model.MenuExample;
import com.reportforms.model.MenuExample.Criteria;
import com.reportforms.model.MenuType;
import com.reportforms.model.MenuTypeExample;
import com.reportforms.model.RoleMenuExample;
import com.reportforms.service.MenuService;


@Service
public class MenuServiceImpl implements MenuService{

	@Autowired
	private MenuTypeMapper menuTypeMapper;
	@Autowired
	private MenuMapper menuMapper;
	@Autowired
	private RoleMenuMapper roleMenuMapper;
	
	/* (non-Javadoc)
	 * @see cn.tempus.gongdan.service.MenuService#addMenu(java.lang.Integer, cn.tempus.gongdan.domain.Menu)
	 */
	@Override
	public void addMenu(Integer typeId, Menu menu) {
		if(typeId == null)
			throw new RuntimeException("typeId is null! ");
		MenuType menuType = getMenuTypeById(typeId);
		if(menuType == null){
			throw new RuntimeException("typeId : " + typeId + "不存在");
		}
		menu.setType(menuType);
		
		//添加到最后位置
		/*MenuExample example = new MenuExample();
		example.createCriteria().andTypeIdEqualTo(typeId);
		example.setOrderByClause("seq desc");
		List<Menu> menus = menuMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(menus)){
			menu.setSeq(1);
		}else{
			menu.setSeq(menus.get(0).getSeq() + 1);
		}*/
		
		menuMapper.insert(menu);
	}

	@Override
	public void addType(MenuType menuType) {
		//添加到最后位置
		MenuTypeExample example = new MenuTypeExample();
		example.setOrderByClause("seq desc");
		List<MenuType> menuTypes = menuTypeMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(menuTypes)){
			menuType.setSeq(1);
		}else{
			menuType.setSeq(menuTypes.get(0).getSeq() + 1);
		}
		menuTypeMapper.insert(menuType);
	}

	@Override
	public void deleteMenu(Integer id) {
		Assert.notNull(id,"id is null");
		
		//删除菜单和角色的关联数据
		RoleMenuExample example = new RoleMenuExample();
		example.createCriteria().andMenuIdEqualTo(id);
		roleMenuMapper.deleteByExample(example);
		
		
		menuMapper.deleteByPrimaryKey(id);
		
	}

	@Override
	public void deleteType(Integer id) {
		if(id == null)
			throw new RuntimeException("id is null");
		
		//删除一级菜单
		menuTypeMapper.deleteByPrimaryKey(id);
		
		//删除所属的菜单项
		List<Menu> menus = getMenusOfType(id);
		for(Menu menu : menus){
			deleteMenu(menu.getId());
		}
		
		
	}

	@Override
	public void downMenu(Integer id) {
		Menu menu = getMenuById(id);
		if(menu == null)
			return;
		
		List<Menu> menus = getMenusOfType(menu.getType().getId());
		for(int i = 0 ; i < menus.size() ; i++){
			if(menu.getId().equals(menus.get(i).getId())){
				if(i == menus.size() - 1)
					break;
				else{
					int seq = menu.getSeq();
					menu.setSeq(menus.get(i+1).getSeq());
					menuMapper.updateByPrimaryKeySelective(menu);
					menus.get(i+1).setSeq(seq);
					menuMapper.updateByPrimaryKeySelective(menus.get(i+1));
					break;
				}
			}
		}
		
	}

	@Override
	public void downType(Integer id) {
		MenuType menuType = getMenuTypeById(id);
		if(menuType == null)
			return;
		
		List<MenuType> menuTypes = getMenuTypes();
		for(int i = 0 ; i < menuTypes.size() ; i++){
			if(menuType.getId().equals(menuTypes.get(i).getId())){
				if(i == menuTypes.size() - 1)
					break;
				else{
					int seq = menuType.getSeq();
					menuType.setSeq(menuTypes.get(i+1).getSeq());
					menuTypeMapper.updateByPrimaryKeySelective(menuType);
					menuTypes.get(i+1).setSeq(seq);
					menuTypeMapper.updateByPrimaryKeySelective(menuTypes.get(i+1));
					break;
				}
			}
		}
		
	}

	@Override
	public void upMenu(Integer id) {
		Menu menu = getMenuById(id);
		if(menu == null)
			return ;
		List<Menu> menus = getMenusOfType(menu.getType().getId());
		for(int i = 0 ; i < menus.size() ; i++){
			if(menu.getId().equals(menus.get(i).getId())){
				if(i == 0)
					break;
				else{
					int seq = menu.getSeq();
					menu.setSeq(menus.get(i-1).getSeq());
					menuMapper.updateByPrimaryKeySelective(menu);
					menus.get(i-1).setSeq(seq);
					menuMapper.updateByPrimaryKeySelective(menus.get(i-1));
					break;
				}
			}
		}
		
	}

	@Override
	public void upType(Integer id) {
		MenuType menuType = getMenuTypeById(id);
		if(menuType == null)
			return ;
		List<MenuType> menuTypes = getMenuTypes();
		for(int i = 0 ; i < menuTypes.size() ; i++){
			if(menuType.getId().equals(menuTypes.get(i).getId())){
				if(i == 0)
					break;
				else{
					int seq = menuType.getSeq();
					menuType.setSeq(menuTypes.get(i-1).getSeq());
					menuTypeMapper.updateByPrimaryKeySelective(menuType);
					menuTypes.get(i-1).setSeq(seq);
					menuTypeMapper.updateByPrimaryKeySelective(menuTypes.get(i-1));
					break;
				}
			}
		}
		
	}

	@Override
	public Menu getMenuById(Integer id) {
		return menuMapper.selectByPrimaryKey(id);
	}

	@Override
	public MenuType getMenuTypeById(Integer id) {
		return menuTypeMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Menu> getMenusOfType(Integer typeId) {
		Assert.notNull(typeId, "typeId is null");
		MenuExample example = new MenuExample();
		example.createCriteria().andTypeIdEqualTo(typeId);
		example.setOrderByClause("seq asc");
		return menuMapper.selectByExample(example);
	}

	@Override
	public List<MenuType> getMenuTypes() {
		MenuTypeExample example = new MenuTypeExample();
		example.setOrderByClause("seq asc");
		return menuTypeMapper.selectByExample(example);
	}

	@Override
	public void updateMenu(Menu menu) {
		Assert.notNull(menu);
		Assert.notNull(menu.getId());
		//Menu _menu = getMenuById(menu.getId());
		//_menu.setName(menu.getName());
		//_menu.setUri(menu.getUri());
		menuMapper.updateByPrimaryKey(menu);
		
	}

	@Override
	public void updateMenuType(MenuType menuType) {
		Assert.notNull(menuType);
		Assert.notNull(menuType.getId());
		//MenuType _menuType = getMenuTypeById(menuType.getId());
		//_menuType.setName(menuType.getName());
		menuTypeMapper.updateByPrimaryKey(menuType);
		
	}
	
	@Override
	public List<Menu> queryByCriteria(Menu menu) {
		// TODO Auto-generated method stub
		return menuMapper.selectByOr(menu);
	}
	
	@Override
	public List<Menu> queryAll(Menu menu) {
		// TODO Auto-generated method stub
		MenuExample example = new MenuExample();
		example.setOrderByClause("seq asc ");
		return menuMapper.selectByExample(example);
	}
	
	@Override
	public Integer countByExample(MenuExample example) {
		// TODO Auto-generated method stub
		return menuMapper.countByExample(example);
	}

}
