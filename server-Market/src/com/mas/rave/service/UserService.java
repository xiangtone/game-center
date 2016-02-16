package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.Menu;
import com.mas.rave.main.vo.Operation;
import com.mas.rave.main.vo.Role;
import com.mas.rave.main.vo.User;

/**
 * 
 * @author Administrator
 * 
 */
public interface UserService {

	static class UserCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		/**
		 * 账号模型匹配
		 * 
		 * @param keyName
		 * @return
		 */
		public UserCriteria nameLike(String keyName) {
			params.put(1, keyName);
			return this;
		}

		public UserCriteria activable(Boolean isActivable) {
			params.put(2, isActivable);
			return this;
		}

		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

	Integer addNewUser(User user);

	User getUserById(Integer id);

	PaginationVo<User> searchUsers(UserCriteria criteria, int currentPage, int pageSize);

	/**
	 * 验证用户合法性
	 * <p/>
	 * <ul>
	 * <li>用户名</li>
	 * <li>密码</li>
	 * <li>是否禁用</li>
	 * </ul>
	 * 
	 * @param user
	 * @return
	 */
	boolean validUser(User user);

	/**
	 * 更新用户基本资料。
	 * 
	 * @param user
	 */
	void update(User user);

	/**
	 * 重置指定用户的角色关系.
	 * <p>
	 * 此方法将会抛弃之前的设置，重置为指定的角色关系.
	 * 
	 * @param userId
	 *            用户ID
	 * @param roleIds
	 *            所属角色
	 */
	void resetRoleRelationship(Integer userId, List<Integer> roleIds);

	/**
	 * 获得指定用户ID的所属角色组.
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	List<Role> getRolesByUserId(Integer userId);

	/**
	 * 获得指定用户的所有的操作权限定义.
	 * 
	 * @param userid
	 * @return
	 */
	List<Operation> getOperationsByUserId(Integer userid);

	/**
	 * 获得指定用户可以操作的菜单项
	 * 
	 * @param userId
	 * @return
	 */
	List<Menu> getMenusByUserId(Integer userId);

	/**
	 * 
	 * @param loginName
	 * @return
	 */
	User getUserByLoginName(String loginName);

	/*
	 * 根据页面条件查询用户
	 */
	List<User> queryUserByCondition(User user);

	void delete(Integer id);

	void batchDelete(Integer[] ids);

	void active(Integer id);

	void batchActive(Integer[] ids);

	void disable(Integer id);

	void batchDisable(Integer[] ids);

	public List<User> selectByExample(User user);

	public HashMap<String, Object> getMenuStates();

}
