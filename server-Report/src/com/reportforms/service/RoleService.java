package com.reportforms.service;

import java.util.List;

import com.reportforms.model.Menu;
import com.reportforms.model.MenuType;
import com.reportforms.model.Operation;
import com.reportforms.model.Role;
import com.reportforms.model.User;



public interface RoleService {

Role addNewRole(Role role);
	
	Role getRoleById(Integer id);
	
	Role getRoleByName(String name);
	
	List<Role> getAllRoles();
	
	void updateBaseInfo(Role role);
	
	void deleteRoleById(Integer id);
	
	void batchDelete(Integer[] ids);
	
	/**
	 * 上升排序
	 * @param id
	 */
	void higherRole(Integer id);
	
	/**
	 * 下降排序
	 * @param id
	 */
	void lowerRole(Integer id);
	
	//////////////////////////////////////////////////////////
	//////////////////操作功能接口/////////////////////////////
	//////////////////////////////////////////////////////////
	
	List<Operation> getAllOperations();
	
	List<Operation> getOperationsByRoleId(Integer roleId);
	
	void resetRoleOperationRelationship(Integer roleId,List<Integer> operationIds);
	
	List<MenuType> getMenuTypesByRoleId(Integer roleId);
	
	void resetRoleMenuRelationship(Integer roleId,List<Integer> menus);
	
	List<Menu> getMenusByRoleId(Integer roleId);
	
	List<User> getUsersByRoleId(Integer roleId);
}
