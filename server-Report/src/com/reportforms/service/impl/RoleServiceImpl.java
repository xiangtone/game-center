package com.reportforms.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.reportforms.common.MyCollectionUtils;
import com.reportforms.common.MyNumberUtils;
import com.reportforms.common.MyReflectUtils;
import com.reportforms.common.MyCollectionUtils.Equaltor;
import com.reportforms.dao.OperationMapper;
import com.reportforms.dao.RoleMapper;
import com.reportforms.dao.RoleMenuMapper;
import com.reportforms.dao.RoleOperationMapper;
import com.reportforms.dao.UserRoleMapper;
import com.reportforms.model.Menu;
import com.reportforms.model.MenuType;
import com.reportforms.model.Operation;
import com.reportforms.model.OperationExample;
import com.reportforms.model.Role;
import com.reportforms.model.RoleExample;
import com.reportforms.model.RoleMenu;
import com.reportforms.model.RoleMenuExample;
import com.reportforms.model.RoleOperation;
import com.reportforms.model.RoleOperationExample;
import com.reportforms.model.User;
import com.reportforms.model.UserRole;
import com.reportforms.model.UserRoleExample;
import com.reportforms.service.MenuService;
import com.reportforms.service.RoleService;
import com.reportforms.service.UserService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private RoleOperationMapper roleOperationMapper;
	@Autowired
	private OperationMapper operationMapper;
	@Autowired
	private MenuService menuService;
	@Autowired
	private RoleMenuMapper roleMenuMapper;
	@Autowired
	private UserRoleMapper userRoleMapper;
	@Autowired
	private UserService userService;

	@Override
	public Role addNewRole(Role role) {
		Assert.notNull(role.getName(), "role.name is null!");
		/*
		 * seq设置最大值(排在最后)
		 */
		role.setSeq(getMaxSeq() + 1);

		roleMapper.insert(role);
		return getRoleById(role.getId());
	}

	private int getMaxSeq() {
		List<Role> roles = getAllRoles();
		if (CollectionUtils.isEmpty(roles))
			return 0;
		return roles.get(roles.size() - 1).getSeq();
	}

	@Override
	public List<Role> getAllRoles() {
		RoleExample example = new RoleExample();
		example.setOrderByClause("seq asc");

		return roleMapper.selectByExample(example);
	}

	@Override
	public Role getRoleById(Integer id) {
		return roleMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public Role getRoleByName(String name){
		RoleExample example = new RoleExample();
		example.createCriteria().andNameEqualTo(name);
		List<Role> result = roleMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(result))
			return null;
		else
			return result.get(0);
	}

	private void updateRole(Role role, String[] updateProperties) {
		Role _role = getRoleById(role.getId());
		if (_role == null) {
			throw new IllegalArgumentException("role.id is not exist : "
					+ role.getId());
		}
		BeanUtils.copyProperties(role, _role, MyReflectUtils
				.complementProperites(Role.class, updateProperties));
		roleMapper.updateByPrimaryKey(_role);
	}

	@Override
	public void higherRole(Integer id) {
		Assert.notNull(id, "param(id) is null!");
		Role curRole = getRoleById(id);
		if (curRole == null)
			return;
		List<Role> roles = getAllRoles();
		for (int i = 0; i < roles.size(); i++) {
			if (curRole.getId().equals(roles.get(i).getId())) {
				if (i == 0)
					break;
				else {
					int seq = curRole.getSeq();
					curRole.setSeq(roles.get(i - 1).getSeq());
					updateRole(curRole, new String[] { "seq" });
					roles.get(i - 1).setSeq(seq);
					updateRole(roles.get(i - 1), new String[] { "seq" });
					break;
				}
			}
		}
	}

	@Override
	public void lowerRole(Integer id) {
		Assert.notNull(id, "param(id) is null!");
		Role curRole = getRoleById(id);
		if (curRole == null)
			return;
		List<Role> roles = getAllRoles();
		for (int i = 0; i < roles.size(); i++) {
			if (curRole.getId().equals(roles.get(i).getId())) {
				if (i == roles.size() - 1)
					break;
				else {
					int seq = curRole.getSeq();
					curRole.setSeq(roles.get(i + 1).getSeq());
					updateRole(curRole, new String[] { "seq" });
					roles.get(i + 1).setSeq(seq);
					updateRole(roles.get(i + 1), new String[] { "seq" });
					break;
				}
			}
		}
	}

	@Override
	public void updateBaseInfo(Role role) {
		updateRole(role, new String[] { "name", "description" });
	}

	@Override
	public List<Operation> getOperationsByRoleId(Integer roleId) {
		RoleOperationExample example = new RoleOperationExample();
		example.createCriteria().andRoleIdEqualTo(roleId);
		List<RoleOperation> ros = roleOperationMapper.selectByExample(example);
		List<Operation> result = new ArrayList<Operation>();
		for (RoleOperation ro : ros) {
			result.add(ro.getOperation());
		}
		return result;
	}

	@SuppressWarnings("serial")
	@Override
	public void resetRoleOperationRelationship(final Integer roleId,
			List<Integer> operationIds) {
		Assert.notNull(roleId, "param:roleId is null!");
		if (getRoleById(roleId) == null) {
			throw new IllegalArgumentException("roleId : " + roleId
					+ " does not exist!!!");
		}
		if (operationIds == null) {
			operationIds = new ArrayList<Integer>();
		}
		// 重置前角色
		List<Operation> originalOperations = getOperationsByRoleId(roleId);
		// 新增
		List<Integer> newOperations = MyCollectionUtils.sub(operationIds,
				originalOperations, new Equaltor<Integer, Operation>() {
					@Override
					public boolean equal(Integer o1, Operation o2) {
						return MyNumberUtils.compareInteger(o1, o2.getId()) == 0;
					}
				});
		bindRolesToOperation(new ArrayList<Integer>() {
			{
				add(roleId);
			}
		}, newOperations);
		// 删除
		List<Operation> delOperations = MyCollectionUtils.sub(
				originalOperations, operationIds,
				new Equaltor<Operation, Integer>() {
					@Override
					public boolean equal(Operation o1, Integer o2) {
						return MyNumberUtils.compareInteger(o1.getId(), o2) == 0;
					}
				});
		List<Integer> _delOperations = new ArrayList<Integer>();
		for (Operation operation : delOperations) {
			_delOperations.add(operation.getId());
		}
		unBindRolesToOperations(new ArrayList<Integer>() {
			{
				add(roleId);
			}
		}, _delOperations);

	}

	/**
	 * 绑定一组角色和权限定义。
	 * 
	 * @param roleIds
	 * @param roleIds
	 */
	private void bindRolesToOperation(List<Integer> roleIds,
			List<Integer> operationIds) {
		if (CollectionUtils.isEmpty(roleIds)
				|| CollectionUtils.isEmpty(operationIds))
			return;
		for (Integer roleId : roleIds) {
			for (Integer operationId : operationIds) {
				addNewRoleOperation(roleId, operationId);
			}
		}
	}

	/**
	 * 新增角色和权限的关系
	 * 
	 * @param userId
	 * @param roleId
	 */
	private void addNewRoleOperation(Integer roleId, Integer operationId) {
		Assert.notNull(roleId);
		Assert.notNull(operationId);
		Role role = getRoleById(roleId);
		if (role == null) {
			throw new IllegalArgumentException("roleId : " + roleId
					+ " does not exist");
		}
		Operation operation = operationMapper.selectByPrimaryKey(operationId);
		if (operation == null) {
			throw new IllegalArgumentException("operationId : " + operationId
					+ " does not exist");
		}
		if (getUserRoleByCombinationKeys(roleId, operationId) != null)// 已经存在关系
			return;
		RoleOperation roleOperation = new RoleOperation();
		roleOperation.setRole(role);
		roleOperation.setOperation(operation);
		roleOperationMapper.insert(roleOperation);
	}

	/**
	 * 得到指定角色和权限之间的中间关联对象
	 * 
	 * @param roleId
	 * @param operationId
	 * @return
	 */
	private RoleOperation getUserRoleByCombinationKeys(Integer roleId,
			Integer operationId) {
		if (roleId == null || operationId == null)
			return null;
		RoleOperationExample example = new RoleOperationExample();
		example.createCriteria().andRoleIdEqualTo(roleId).andOperationIdEqualTo(
				operationId);
		List<RoleOperation> roleOperations = roleOperationMapper
				.selectByExample(example);
		if (CollectionUtils.isEmpty(roleOperations)) {
			return null;
		}
		return roleOperations.get(0);
	}

	/**
	 * 解除一组角色和权限定义.
	 * 
	 * @param userIds
	 * @param roleIds
	 */
	private void unBindRolesToOperations(List<Integer> roleIds,
			List<Integer> operationIds) {
		if (CollectionUtils.isEmpty(roleIds)
				|| CollectionUtils.isEmpty(operationIds))
			return;
		for (Integer roleId : roleIds) {
			for (Integer operationId : operationIds) {
				deleteRoleOperation(roleId, operationId);
			}
		}
	}

	/**
	 * 删除角色和权限定义关联.
	 * 
	 * @param roleId
	 * @param operationId
	 */
	private void deleteRoleOperation(Integer roleId, Integer operationId) {
		Assert.notNull(roleId);
		Assert.notNull(operationId);
		RoleOperationExample example = new RoleOperationExample();
		example.createCriteria().andRoleIdEqualTo(roleId).andOperationIdEqualTo(
				operationId);
		roleOperationMapper.deleteByExample(example);
	}

	@Override
	public List<Operation> getAllOperations() {
		OperationExample example = new OperationExample();
		example.setOrderByClause("seq asc");
		return operationMapper.selectByExample(example);
	}

	@Override
	public List<MenuType> getMenuTypesByRoleId(Integer roleId) {
		Assert.notNull(roleId, "roleId is null!");
		RoleMenuExample example = new RoleMenuExample();
		example.createCriteria().andRoleIdEqualTo(roleId);
		List<RoleMenu> roleMenus = roleMenuMapper.selectByExample(example);
		List<Integer> menuIds = new ArrayList<Integer>();
		for(RoleMenu rm : roleMenus){
			menuIds.add(rm.getMenu().getId());
		}
		return filterMenus(menuIds);
	}

	private List<MenuType> filterMenus(List<Integer> menuIds) {
		Map<Integer, MenuType> temp = new HashMap<Integer, MenuType>();
		for(Integer menuId : menuIds){
			Menu _menu = menuService.getMenuById(menuId);
			Integer _key = _menu.getType().getId();
			if(temp.containsKey(_key)){
				temp.get(_key).getMenus().add(_menu);
			}else{
				MenuType _menuType = _menu.getType();
				_menuType.getMenus().add(_menu);
				temp.put(_key, _menuType);
			}
		}
		List<MenuType> result = new ArrayList<MenuType>();
		result.addAll(temp.values());
		Collections.sort(result);
		return result;
	}

	public List<Menu> getMenusByRoleId(Integer roleId){
		RoleMenuExample example = new RoleMenuExample();
		example.createCriteria().andRoleIdEqualTo(roleId);
		List<RoleMenu> roleMenus = roleMenuMapper.selectByExample(example);
		List<Menu> menuIds = new ArrayList<Menu>();
		for(RoleMenu rm : roleMenus){
			menuIds.add(rm.getMenu());
		}
		return menuIds;
	}
	
	@Override
	public void resetRoleMenuRelationship(final Integer roleId, List<Integer> menus) {
		Assert.notNull(roleId, "param:roleId is null!");
		if (getRoleById(roleId) == null) {
			throw new IllegalArgumentException("roleId : " + roleId
					+ " does not exist!!!");
		}
		if (menus == null) {
			menus = new ArrayList<Integer>();
		}
		// 重置前菜单配置
		List<Menu> originalMenus = getMenusByRoleId(roleId);
		// 新增
		List<Integer> newMenus = MyCollectionUtils.sub(menus,
				originalMenus, new Equaltor<Integer, Menu>() {
					@Override
					public boolean equal(Integer o1, Menu o2) {
						return MyNumberUtils.compareInteger(o1, o2.getId()) == 0;
					}
				});
		bindRolesToMenus(new ArrayList<Integer>() {
			{
				add(roleId);
			}
		}, newMenus);
		// 删除
		List<Menu> delMenus = MyCollectionUtils.sub(
				originalMenus, menus,
				new Equaltor<Menu, Integer>() {
					@Override
					public boolean equal(Menu o1, Integer o2) {
						return MyNumberUtils.compareInteger(o1.getId(), o2) == 0;
					}
				});
		List<Integer> _delMenus = new ArrayList<Integer>();
		for (Menu menu : delMenus) {
			_delMenus.add(menu.getId());
		}
		unBindRolesToMenus(new ArrayList<Integer>() {
			{
				add(roleId);
			}
		}, _delMenus);

	}

	private void unBindRolesToMenus(ArrayList<Integer> roleIds,
			List<Integer> delMenus) {
		if (CollectionUtils.isEmpty(roleIds)
				|| CollectionUtils.isEmpty(delMenus))
			return;
		for (Integer roleId : roleIds) {
			for (Integer menuId : delMenus) {
				deleteRoleMenu(roleId, menuId);
			}
		}
		
	}

	private void deleteRoleMenu(Integer roleId, Integer menuId) {
		Assert.notNull(roleId);
		Assert.notNull(menuId);
		RoleMenuExample example = new RoleMenuExample();
		example.createCriteria().andRoleIdEqualTo(roleId).andMenuIdEqualTo(menuId);
		roleMenuMapper.deleteByExample(example);
		
	}

	private void bindRolesToMenus(ArrayList<Integer> roleIds,
			List<Integer> newMenus) {
		if (CollectionUtils.isEmpty(roleIds)
				|| CollectionUtils.isEmpty(newMenus))
			return;
		for (Integer roleId : roleIds) {
			for (Integer menuId : newMenus) {
				addNewRoleMenu(roleId, menuId);
			}
		}
		
	}

	private void addNewRoleMenu(Integer roleId, Integer menuId) {
		Assert.notNull(roleId);
		Assert.notNull(menuId);
		Role role = getRoleById(roleId);
		if (role == null) {
			throw new IllegalArgumentException("roleId : " + roleId
					+ " does not exist");
		}
		Menu menu = menuService.getMenuById(menuId);
		if (menu == null) {
			throw new IllegalArgumentException("menuId : " + menuId
					+ " does not exist");
		}
		if (getRoleMenuByCombinationKeys(roleId, menuId) != null)// 已经存在关系
			return;
		RoleMenu roleMenu = new RoleMenu();
		roleMenu.setRole(role);
		roleMenu.setMenu(menu);
		roleMenuMapper.insert(roleMenu);
		
	}

	private Object getRoleMenuByCombinationKeys(Integer roleId, Integer menuId) {
		if (roleId == null || menuId == null)
			return null;
		RoleMenuExample example = new RoleMenuExample();
		example.createCriteria().andRoleIdEqualTo(roleId).andMenuIdEqualTo(menuId);
		List<RoleMenu> roleMenus = roleMenuMapper
				.selectByExample(example);
		if (CollectionUtils.isEmpty(roleMenus)) {
			return null;
		}
		return roleMenus.get(0);
	}

	@Override
	public List<User> getUsersByRoleId(Integer roleId)
	{
		UserRoleExample example = new UserRoleExample();
		example.createCriteria().andRoleIdEqualTo(roleId);
		List<UserRole> roleUsers = userRoleMapper.selectByExample(example);
		List<User> userIds = new ArrayList<User>();
		for(UserRole ur : roleUsers){
			userIds.add(ur.getUser());
		}
		return userIds;
	}

	@Override
	public void deleteRoleById(Integer id){
		resetRoleOperationRelationship(id,null);
		resetRoleMenuRelationship(id, null);
		
		UserRoleExample example = new UserRoleExample();
		example.createCriteria().andRoleIdEqualTo(id);
		userRoleMapper.deleteByExample(example);
		
		roleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void batchDelete(Integer[] ids) {
		for(Integer id : ids){
			deleteRoleById(id);
		}
		
	}
}
