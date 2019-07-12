package com.dong.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dong.constant.MyPage;
import com.dong.constant.MyRedis;
import com.dong.domain.User;
import com.dong.mapper.UserMapper;
import com.dong.service.UserServiceI;
import com.dong.utils.Util;

@Service
public class UserServiceImpl implements UserServiceI {

	@Resource
	private UserMapper userMapper;
	
	/**
	 * ����userId��ѯ�û�
	 */
//	@Cacheable(value = "user", key= "'userId_'+#userId")
//	@MyRedis(fieldValue = "#userId", key = "userId")
	public User getUserById(int userId) {
		User user = userMapper.getOneUser(userId);
		return user;
	}

	/**
	 * �޸��û�
	 */
	public int updateUser(User user) {
		return userMapper.update(user);
	}

	/**
	 * ��ѯ�����û�
	 */
	public List<User> findAllUser() {
		return userMapper.findAll();
	}

	/**
	 * �����û�
	 */
	public void addUser(User user) {
		userMapper.insertUser(user);
	}

	/**
	 * ��ҳ��ѯ�û�
	 */
	public MyPage<User> findUserByPage(long offset, long pageSize) {
		MyPage<User> data = new MyPage<User>();
		List<User> users = userMapper.findByPage(offset, pageSize);
		int total = userMapper.getTotal();
		data.setContent(users);
		data.setTotal(total);
		return data;
	}

	/**
	 * ɾ���û�
	 */
	public void delete(String delIds) {
		if (!Util.isEmpty(delIds)) {
			String[] strArray = delIds.split(";");
			for (String id : strArray) {
				userMapper.delUser(Long.parseLong(id));
			}
		}
	}

}
