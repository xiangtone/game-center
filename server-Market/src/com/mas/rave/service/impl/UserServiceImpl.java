package com.mas.rave.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.MyCollectionUtils.Equaltor;
import com.mas.rave.common.MyNumberUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.UserMapper;
import com.mas.rave.dao.UserRoleMapper;
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.Cp;
import com.mas.rave.main.vo.Menu;
import com.mas.rave.main.vo.Operation;
import com.mas.rave.main.vo.Role;
import com.mas.rave.main.vo.User;
import com.mas.rave.main.vo.UserExample;
import com.mas.rave.main.vo.UserRole;
import com.mas.rave.main.vo.UserRoleExample;
import com.mas.rave.service.AppFileService;
import com.mas.rave.service.CpService;
import com.mas.rave.service.RoleService;
import com.mas.rave.service.UserService;
import com.mas.rave.util.MD5;

@Service
public class UserServiceImpl implements UserService {

	private final Logger log = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserRoleMapper userRoleMapper;
	@Autowired
	private RoleService roleService;

	@Autowired
	private CpService cpService;

	@Autowired
	private AppFileService appFileService;

	@Override
	public Integer addNewUser(User user) {
		user.setInsertDate(new Date());
		userMapper.insert(user);
		return user.getId();
	}

	@Override
	public PaginationVo<User> searchUsers(UserCriteria criteria, int currentPage, int pageSize) {
		UserExample example = new UserExample();
		UserExample.Criteria _criteria = example.createCriteria();
		Map<Integer, Object> params = criteria.getParams();
		for (Integer key : params.keySet()) {
			if (key.equals(1)) {
				// _criteria.andNameLike(params.get(key).toString());
				_criteria.andNameEqualTo(params.get(key).toString());
			} else if (key.equals(2) && params.get(key) != null) {
				Boolean isActivable = (Boolean) params.get(key);
				_criteria.andActivableEqualTo(isActivable);
			}
		}
		List<User> data = userMapper.selectByExample(example, new RowBounds((currentPage - 1) * pageSize, pageSize));

		int recordCount = userMapper.countByExample(example);
		PaginationVo<User> result = new PaginationVo<User>(data, recordCount, pageSize, currentPage);
		return result;
	}

	@Override
	public User getUserById(Integer id) {
		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Menu> getMenusByUserId(Integer userId) {
		List<Role> roles = getRolesByUserId(userId);
		// Set<Menu> result = new HashSet<Menu>();
		List<Menu> m = new ArrayList<Menu>();

		for (Role role : roles) {
			List<Menu> _menus = roleService.getMenusByRoleId(role.getId());
			for (Menu _menu : _menus) {
				m.add(_menu);
			}
		}

		return m;
	}

	@Override
	public List<Operation> getOperationsByUserId(Integer userid) {
		// 获取用户角色
		List<Role> roles = getRolesByUserId(userid);
		Map<Integer, Operation> result = new HashMap<Integer, Operation>();
		for (Role role : roles) {
			List<Operation> operations = roleService.getOperationsByRoleId(role.getId());
			for (Operation operation : operations) {
				if (result.get(operation.getId()) == null) {
					result.put(operation.getId(), operation);
				}
			}
		}
		return new ArrayList<Operation>(result.values());
	}

	@Override
	public List<Role> getRolesByUserId(Integer userId) {
		UserRoleExample example = new UserRoleExample();
		example.createCriteria().andUserIdEqualTo(userId);
		List<UserRole> userRoles = userRoleMapper.selectByExample(example);
		List<Role> result = new ArrayList<Role>();
		for (UserRole ur : userRoles) {
			result.add(ur.getRole());
		}
		return result;
	}

	@Override
	public User getUserByLoginName(String loginName) {
		UserExample example = new UserExample();
		example.createCriteria().andNameEqualTo(loginName);
		List<User> result = userMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(result))
			return null;
		else
			return result.get(0);
	}

	@Override
	public List<User> queryUserByCondition(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetRoleRelationship(final Integer userId, List<Integer> roleIds) {
		Assert.notNull(userId, "param:userId is null!");
		if (getUserById(userId) == null) {
			throw new IllegalArgumentException("userId : " + userId + " does not exist!!!");
		}
		if (roleIds == null) {
			roleIds = new ArrayList<Integer>();
		}
		// 重置前角色
		List<Role> originalRoles = getRolesByUserId(userId);
		// 新增
		List<Integer> newRoles = MyCollectionUtils.sub(roleIds, originalRoles, new Equaltor<Integer, Role>() {
			@Override
			public boolean equal(Integer o1, Role o2) {
				return MyNumberUtils.compareInteger(o1, o2.getId()) == 0;
			}
		});
		bindUsersToRoles(new ArrayList<Integer>() {
			{
				add(userId);
			}
		}, newRoles);
		// 删除
		List<Role> delRoles = MyCollectionUtils.sub(originalRoles, roleIds, new Equaltor<Role, Integer>() {
			@Override
			public boolean equal(Role o1, Integer o2) {
				return MyNumberUtils.compareInteger(o1.getId(), o2) == 0;
			}
		});
		List<Integer> _delRoles = new ArrayList<Integer>();
		for (Role role : delRoles) {
			_delRoles.add(role.getId());
		}
		unBindUsersToRoles(new ArrayList<Integer>() {
			{
				add(userId);
			}
		}, _delRoles);
	}

	/**
	 * 解除一组用户和一组角色的关系.
	 * 
	 * @param userIds
	 * @param roleIds
	 */
	private void unBindUsersToRoles(List<Integer> userIds, List<Integer> roleIds) {
		if (CollectionUtils.isEmpty(userIds) || CollectionUtils.isEmpty(roleIds))
			return;
		for (Integer userId : userIds) {
			for (Integer roleId : roleIds) {
				deleteUserRole(userId, roleId);
			}
		}
	}

	/**
	 * 删除用户和角色的关联
	 * 
	 * @param userId
	 * @param roleId
	 */
	private void deleteUserRole(Integer userId, Integer roleId) {
		Assert.notNull(userId);
		Assert.notNull(roleId);
		if (getUserById(userId) == null) {
			throw new IllegalArgumentException("userId : " + userId + " does not exist");
		}
		if (roleService.getRoleById(roleId) == null) {
			throw new IllegalArgumentException("roleId : " + roleId + " does not exist");
		}
		if (getUserRoleByCombinationKeys(userId, roleId) == null)// 关系本身不存在，不用删除
			return;
		UserRoleExample example = new UserRoleExample();
		example.createCriteria().andUserIdEqualTo(userId).andRoleIdEqualTo(roleId);
		userRoleMapper.deleteByExample(example);
	}

	/**
	 * 绑定一组用户到一组角色
	 * 
	 * @param userIds
	 * @param roleIds
	 */
	private void bindUsersToRoles(List<Integer> userIds, List<Integer> roleIds) {
		if (CollectionUtils.isEmpty(userIds) || CollectionUtils.isEmpty(roleIds))
			return;
		for (Integer userId : userIds) {
			for (Integer roleId : roleIds) {
				addNewUserRole(userId, roleId);
			}
		}
	}

	/**
	 * 新增用户和角色的关联
	 * 
	 * @param userId
	 * @param roleId
	 */
	private void addNewUserRole(Integer userId, Integer roleId) {
		Assert.notNull(userId);
		Assert.notNull(roleId);
		User user = getUserById(userId);
		if (user == null) {
			throw new IllegalArgumentException("userId : " + userId + " does not exist");
		}
		Role role = roleService.getRoleById(roleId);
		if (role == null) {
			throw new IllegalArgumentException("roleId : " + roleId + " does not exist");
		}
		if (getUserRoleByCombinationKeys(userId, roleId) != null)// 已经存在关系
			return;
		UserRole userRole = new UserRole();
		userRole.setUser(user);
		userRole.setRole(role);
		userRoleMapper.insert(userRole);
	}

	/**
	 * 得到指定用户和指定角色的关联中间对象
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	private UserRole getUserRoleByCombinationKeys(Integer userId, Integer roleId) {
		if (userId == null || roleId == null)
			return null;
		UserRoleExample example = new UserRoleExample();
		example.createCriteria().andUserIdEqualTo(userId).andRoleIdEqualTo(roleId);
		List<UserRole> userRoles = userRoleMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(userRoles)) {
			return null;
		}
		return userRoles.get(0);
	}

	@Override
	public void update(User user) {
		user.setInsertDate(null);
		userMapper.updateByPrimaryKeySelective(user);
		List<Integer> ids = new ArrayList<Integer>();
		for (Role role : user.getRoles()) {
			if (role.getId() != null)
				ids.add(role.getId());
		}
		resetRoleRelationship(user.getId(), ids);

	}

	@Override
	public boolean validUser(User user) {
		UserExample example = new UserExample();
		user.setPassword(MD5.getMd5Value(user.getPassword()).toUpperCase());
		example.createCriteria().andActivableEqualTo(true).andNameEqualTo(user.getName()).andPasswordEqualTo(user.getPassword());
		if (CollectionUtils.isEmpty(userMapper.selectByExample(example))) {
			return false;
		}
		return true;
	}

	@Override
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delete(id);
		}

	}

	@Override
	public void delete(Integer id) {
		userMapper.deleteByPrimaryKey(id);

		UserRoleExample example = new UserRoleExample();
		example.createCriteria().andUserIdEqualTo(id);
		userRoleMapper.deleteByExample(example);
	}

	@Override
	public void active(Integer id) {
		User user = new User();
		user.setId(id);
		user.setActivable(true);
		userMapper.updateByPrimaryKeySelective(user);

	}

	@Override
	public void batchActive(Integer[] ids) {
		for (Integer id : ids) {
			active(id);
		}
	}

	@Override
	public void batchDisable(Integer[] ids) {
		for (Integer id : ids) {
			disable(id);
		}
	}

	@Override
	public void disable(Integer id) {
		User user = new User();
		user.setId(id);
		user.setActivable(false);
		userMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public List<User> selectByExample(User user) {
		UserExample example = new UserExample();
		user.setPassword(MD5.getMd5Value(user.getPassword()).toUpperCase());
		// log.debug("pwd:" + user.getPassword());
		example.createCriteria().andActivableEqualTo(true).andNameEqualTo(user.getName()).andPasswordEqualTo(user.getPassword());
		return userMapper.selectByExample(example);
	}

	@Override
	public HashMap<String, Object> getMenuStates() {
		HashMap<String, Object> map = new HashMap<String, Object>(2);
		List<Cp> cps = cpService.getCpStates(1);
		if (cps != null && cps.size() > 0) {
			map.put("cp", cps);
		}

		List<AppFile> files = appFileService.getAllCpFile();
		if (files != null && files.size() > 0) {
			map.put("file", files);
		}
		return map;
	}

}
