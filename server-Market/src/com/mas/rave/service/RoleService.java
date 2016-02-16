package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.Menu;
import com.mas.rave.main.vo.MenuType;
import com.mas.rave.main.vo.Operation;
import com.mas.rave.main.vo.Role;
import com.mas.rave.main.vo.User;
import com.mas.rave.main.vo.UserRole;

public interface RoleService {
	static class RoleCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

	public PaginationVo<Role> searchRoles(RoleCriteria criteria, int currentPage, int pageSize);

	Role addNewRole(Role role);

	Role getRoleById(Integer id);

	Role getRoleByName(String name);

	List<Role> getAllRoles();

	void updateBaseInfo(Role role);

	void deleteRoleById(Integer id);

	boolean batchDelete(Integer[] ids);

	/**
	 * 上升排序
	 * 
	 * @param id
	 */
	void higherRole(Integer id);

	/**
	 * 下降排序
	 * 
	 * @param id
	 */
	void lowerRole(Integer id);

	// ////////////////////////////////////////////////////////
	// ////////////////操作功能接口/////////////////////////////
	// ////////////////////////////////////////////////////////

	List<Operation> getAllOperations();

	List<Operation> getOperationsByRoleId(Integer roleId);

	void resetRoleOperationRelationship(Integer roleId, List<Integer> operationIds);

	List<MenuType> getMenuTypesByRoleId(Integer roleId);

	void resetRoleMenuRelationship(Integer roleId, List<Integer> menus);

	List<Menu> getMenusByRoleId(Integer roleId);

	List<User> getUsersByRoleId(Integer roleId);

	UserRole checkUser(Integer roleId);
}
