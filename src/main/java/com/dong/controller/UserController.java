package com.dong.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dong.constant.MyPage;
import com.dong.domain.User;
import com.dong.service.UserServiceI;
import com.dong.utils.Util;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value = "/system")
@RequiresAuthentication
public class UserController {

	/**
	 * 日志
	 */
	private static Logger log = LogManager.getLogger(UserController.class);

	@Resource
	private UserServiceI userService;

	@Resource
	private RedisCacheManager cacheManager;

	/**
	 * 跳转用户管理列表
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/user/user.do", method = RequestMethod.GET)
	@RequiresPermissions(value = "system-user")
	public String goToUserPage(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		log.debug("Go to user.jsp...");
//		try {
//			String str = null;
//			str.split(";");
//		} catch (Exception e) {
//			throw new Exception("System Internal Error...");
//		}
		return "system/user";
	}

	/**
	 * 获取用户列表数据
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/user/getUser.do", method = RequestMethod.POST)
	@RequiresPermissions(value = "system-user")
	public void getUser(HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "limit", required = true, defaultValue = "10") long pageSize,
			@RequestParam(name = "offset", required = true, defaultValue = "1") long offset) throws IOException {
		MyPage<User> users = userService.findUserByPage(offset, pageSize);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", users.getContent());
		map.put("total", users.getTotal());

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(map);
		response.setContentType("application/json;charset=utf-8");
		PrintWriter pw = response.getWriter();
		pw.write(json);
		pw.close();
	}

	/**
	 * 新增用户
	 * 
	 * @param request
	 * @param response
	 * @param user
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/user/add.do", method = RequestMethod.POST)
	@RequiresPermissions(value = "system-user-add")
	@ResponseBody
	public Object addUser(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
		// 设置盐
		user.setSalt(Util.generateSalt());
		// 加盐加密
		user.setPassword(Util.SHA256(user.getPassword() + user.getSalt()));
		userService.addUser(user);
		// 返回信息给前端
		Map<String, String> retmsg = new HashMap<String, String>();
		retmsg.put("CODE", "200");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(retmsg);
		return json;
	}

	/**
	 * 获取一条记录的信息
	 * 
	 * @param request
	 * @param response
	 * @param userId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/user/getOne.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@RequiresPermissions(value = "system-user")
	@ResponseBody
	public Object getOne(HttpServletRequest request, HttpServletResponse response, int userId) throws IOException {
		User user = userService.getUserById(userId);
		// 返回信息给前端
		Map<String, Object> retmsg = new HashMap<String, Object>();
		retmsg.put("CODE", "200");
		retmsg.put("data", user);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(retmsg);
		return json;
	}

	/**
	 * 修改用户
	 * 
	 * @param request
	 * @param response
	 * @param userId
	 * @param name
	 * @param age
	 * @param remark
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/user/upd.do", method = RequestMethod.POST)
	@RequiresPermissions(value = "system-user-upd")
	@ResponseBody
	public Object updUser(HttpServletRequest request, HttpServletResponse response, long userId, String name, int age, String remark) throws IOException {
		User user = userService.getUserById(new Long(userId).intValue());
		user.setName(name);
		user.setAge(age);
		user.setRemark(remark);
		user.setModifyUser(Util.getCurrentUser().getUserId());
		user.setModifyDate(Util.convertTimeToStr(new Date()));
		userService.updateUser(user);
		// 返回信息给前端
		Map<String, String> retmsg = new HashMap<String, String>();
		retmsg.put("CODE", "200");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(retmsg);
		return json;
	}

	/**
	 * 删除用户
	 * 
	 * @param request
	 * @param response
	 * @param delIds
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/user/del.do", method = RequestMethod.POST)
	@RequiresPermissions(value = "system-user-del")
	@ResponseBody
	public Object delUser(HttpServletRequest request, HttpServletResponse response, String delIds) throws IOException {
		userService.delete(delIds);
		// 返回信息给前端
		Map<String, String> retmsg = new HashMap<String, String>();
		retmsg.put("CODE", "200");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(retmsg);
		return json;
	}

}
