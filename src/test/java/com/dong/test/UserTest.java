package com.dong.test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.shiro.SecurityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dong.domain.User;
import com.dong.mapper.UserMapper;
import com.dong.service.UserServiceI;
import com.dong.utils.MybatisUtil;

// Spring�ṩ��Junit���Կ��
@RunWith(SpringJUnit4ClassRunner.class)
// ������@ContextConfigurationע�Ⲣʹ�ø�ע���locations����ָ��spring�������ļ�֮��
@ContextConfiguration(locations = { "classpath:spring-servlet.xml", "classpath:spring-mybatis.xml", "classpath:spring-redis.xml", "classpath:apache-shiro.xml" })
public class UserTest {

	public static void main(String[] args) {
		// ��ȡmybatis�����ļ�
		String resource = "mybatis-config.xml";
		InputStream is = UserTest.class.getClassLoader().getResourceAsStream(resource);
		// ����sqlSession����
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
		// ������ִ��ӳ���ļ���sql��sqlSession
		SqlSession session = sessionFactory.openSession();
		// ��ѯ
		User user = session.selectOne("com.dong.mapper.UserMapper.getOneUser", 1);
		session.close();
		System.out.println(user.toString());
	}

	@Test
	public void testGetUser() throws Exception {
		SqlSession sqlSession = MybatisUtil.getSqlSession();
		// �õ�UserMapperI�ӿڵ�ʵ�������UserMapperI�ӿڵ�ʵ���������sqlSession.getMapper(UserMapperI.class)��̬��������
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		// ִ�в�ѯ����������ѯ����Զ���װ��User����
		User user = mapper.getById(1);
		// User user = mapper.getOneUser(1);
		// ʹ��SqlSessionִ����SQL֮����Ҫ�ر�SqlSession
		sqlSession.close();
		System.out.println(user);
	}

	@Test
	public void testUpdateUser() {
		// SqlSession sqlSession = MybatisUtil.getSqlSession(false); // �ֶ��ύ����
		// UserMapperI mapper = sqlSession.getMapper(UserMapperI.class);
		// User user = mapper.getById(1);
		// user.setAge(20);
		// mapper.update(user);
		// sqlSession.commit();
		// sqlSession.close();
	}

	@Test
	public void getUserCount() {
		SqlSession sqlSession = MybatisUtil.getSqlSession();
		String statement = "com.dong.mapper.UserMapper.getUserCount";// ӳ��sql�ı�ʶ�ַ���
		Map<String, Integer> parameterMap = new HashMap<String, Integer>();
		parameterMap.put("userId", 1);
		parameterMap.put("user_count", -1);
		sqlSession.selectOne(statement, parameterMap);
		Integer result = parameterMap.get("user_count");
		sqlSession.close();
		System.out.println(result);
	}

	// ����spring��Ĳ���
	@Resource
	private UserServiceI userService;

//	@Before
//	public void before() {
//		// ʹ��"spring-servlet.xml"��"spring-mybatis.xml"�����������ļ�����Spring������
//		ApplicationContext ac = new ClassPathXmlApplicationContext(
//				new String[] { "spring-servlet.xml", "spring-mybatis.xml" });
//		// ��Spring�����и���bean��idȡ������Ҫʹ�õ�userService����
//		userService = (UserServiceI) ac.getBean("userService");
//	}

	@Test
	public void testGetUser2() throws Exception {
		String resource = "apache-shiro.xml";
	    ClassPathXmlApplicationContext appCtx = new ClassPathXmlApplicationContext(resource);
	    org.apache.shiro.mgt.SecurityManager securityManager = (org.apache.shiro.mgt.SecurityManager) appCtx.getBean("securityManager");
	    SecurityUtils.setSecurityManager(securityManager);
		
		User user = userService.getUserById(100);
		System.out.println(user);
		
	}
	
	@Test
	public void testUpdate() throws Exception {
		String resource = "apache-shiro.xml";
	    ClassPathXmlApplicationContext appCtx = new ClassPathXmlApplicationContext(resource);
	    org.apache.shiro.mgt.SecurityManager securityManager = (org.apache.shiro.mgt.SecurityManager) appCtx.getBean("securityManager");
	    SecurityUtils.setSecurityManager(securityManager);
	    
	    User user = new User();
	    user.setUserId(100);
	    user.setName("Tom");
	    userService.updateUser(user);
	}

}
