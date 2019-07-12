package com.dong.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dong.domain.Menu;
import com.dong.domain.Permission;
import com.dong.domain.User;
import com.dong.mapper.UserMapper;
import com.dong.service.AccountServiceI;

@Service
public class AccountServiceImpl implements AccountServiceI {

	@Resource
	private UserMapper userMapper;

	/**
	 * 通过name查询用户
	 * 
	 */
	public User getUserByUserName(String username) {
		return userMapper.findByName(username);
	}

	/**
	 * 通过name查询用户的权限
	 */
	public List<String> getPermissionsByUserName(String username) {
		// 根据登录名查询用户是否存在
		User user = getUserByUserName(username);
		if (user == null) {
			return null;
		}
		// 获取登录用户的所有权限
		List<String> list = userMapper.findPermissionsByUserId(user.getUserId());
		return list;
	}

	/**
	 * 通过name查询用户的角色
	 */
	public List<String> getRolesByUserName(String username) {
		// 根据登录名查询用户是否存在
		User user = getUserByUserName(username);
		if (user == null) {
			return null;
		}
		// 获取登录用户的所有权限
		List<String> list = userMapper.findRolesByUserId(user.getUserId());
		return list;
	}

	/**
	 * 根据userId查询菜单权限
	 */
	public List<Menu> getMenuByUserId(long userId) {
		List<Permission> pers = userMapper.findPermissionsLstByUserId(userId);
		List<Menu> rec = new ArrayList<Menu>();
		Map<Long, Menu> maps = new HashMap<Long, Menu>();
		if (pers != null && pers.size() > 0) {
			for (Permission p : pers) {
				Menu m = new Menu(p);
				if (m.getMenuId() != m.getpId()) { // 非父节点
					Menu pm = maps.get(m.getpId());// 获取父节点
					if (pm != null) { // 如果父节点为空，则子节点也无权限
						pm.getChilds().add(m);
						maps.put(m.getMenuId(), m);
					}
				} else { // 父节点
					maps.put(m.getMenuId(), m);
				}
			}
			for (Permission p : pers) {
				if (p.getHeight() == 0) {
					rec.add(maps.get(p.getPermissionId()));
				}
			}
		}
		return rec;
	}

}
