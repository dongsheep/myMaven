package com.dong.service;

import java.util.List;

import com.dong.domain.Menu;
import com.dong.domain.User;

public interface AccountServiceI {

	public List<String> getPermissionsByUserName(String username);
	
	public User getUserByUserName(String username);

	public List<String> getRolesByUserName(String username);
	
	public List<Menu> getMenuByUserId(long userId);

}
