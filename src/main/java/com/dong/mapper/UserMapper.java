package com.dong.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import com.dong.domain.Permission;
import com.dong.domain.User;

/**
 * ����sqlӳ��Ľӿڣ�ʹ��ע��ָ������Ҫִ�е�SQL
 * 
 * @author XIEDONGXIAOs
 *
 */
public interface UserMapper {

	// ʹ��@Selectע��ָ��getById����Ҫִ�е�SQL
	@Select("select * from dx_t_user where userId=#{userId}")
	public User getById(long userId);
	
	// ʹ��@Updateע��ָ��update����Ҫִ�е�SQL
//	@Update("update dx_t_user set name=#{name}, age=#{age}, remark=#{remark} where userId=#{userId}")
	public int update(User user);
	
	public User getOneUser(long userId);

	@Select("select * from dx_t_user")
	public List<User> findAll();
	
	public User findByName(String name);

	public List<String> findPermissionsByUserId(long userId);

	public void insertUser(User user);

	public List<String> findRolesByUserId(long userId);

	public List<Permission> findPermissionsLstByUserId(long userId);

	public List<User> findByPage(@Param("offset") long offset, @Param("pageSize") long pageSize);

	public int getTotal();

	public void delUser(long userId);
	
}
