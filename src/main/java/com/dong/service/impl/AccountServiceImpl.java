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
	 * ͨ��name��ѯ�û�
	 * 
	 */
	public User getUserByUserName(String username) {
		return userMapper.findByName(username);
	}

	/**
	 * ͨ��name��ѯ�û���Ȩ��
	 */
	public List<String> getPermissionsByUserName(String username) {
		// ���ݵ�¼����ѯ�û��Ƿ����
		User user = getUserByUserName(username);
		if (user == null) {
			return null;
		}
		// ��ȡ��¼�û�������Ȩ��
		List<String> list = userMapper.findPermissionsByUserId(user.getUserId());
		return list;
	}

	/**
	 * ͨ��name��ѯ�û��Ľ�ɫ
	 */
	public List<String> getRolesByUserName(String username) {
		// ���ݵ�¼����ѯ�û��Ƿ����
		User user = getUserByUserName(username);
		if (user == null) {
			return null;
		}
		// ��ȡ��¼�û�������Ȩ��
		List<String> list = userMapper.findRolesByUserId(user.getUserId());
		return list;
	}

	/**
	 * ����userId��ѯ�˵�Ȩ��
	 */
	public List<Menu> getMenuByUserId(long userId) {
		List<Permission> pers = userMapper.findPermissionsLstByUserId(userId);
		List<Menu> rec = new ArrayList<Menu>();
		Map<Long, Menu> maps = new HashMap<Long, Menu>();
		if (pers != null && pers.size() > 0) {
			for (Permission p : pers) {
				Menu m = new Menu(p);
				if (m.getMenuId() != m.getpId()) { // �Ǹ��ڵ�
					Menu pm = maps.get(m.getpId());// ��ȡ���ڵ�
					if (pm != null) { // ������ڵ�Ϊ�գ����ӽڵ�Ҳ��Ȩ��
						pm.getChilds().add(m);
						maps.put(m.getMenuId(), m);
					}
				} else { // ���ڵ�
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
