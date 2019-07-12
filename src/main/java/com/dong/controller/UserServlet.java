package com.dong.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dong.domain.User;
import com.dong.service.UserServiceI;

@WebServlet("/userServlet")
public class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private UserServiceI userService;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// super.doGet(req, resp);
		User user = userService.getUserById(1);
		user.setName("WebServlet");
		request.setAttribute("cuser", user);
		request.getRequestDispatcher("/index.html").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// super.doPost(req, resp);
	}

	@Override
	public void init() throws ServletException {
		// super.init();
		// 在Servlet初始化时获取Spring上下文对象(ApplicationContext)
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		// 从ApplicationContext中获取userService
		userService = (UserServiceI) ac.getBean("userService");
	}

}
