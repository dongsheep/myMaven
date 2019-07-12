package com.dong.service;

import java.util.List;

import com.dong.constant.MyPage;
import com.dong.domain.User;

public interface UserServiceI {
	
	public User getUserById(int userId);
	
	public int updateUser(User user);

	public List<User> findAllUser();

	public void addUser(User user);

	public MyPage<User> findUserByPage(long offset, long pageSize);

	public void delete(String delIds);
	
}
