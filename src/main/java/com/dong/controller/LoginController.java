package com.dong.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dong.domain.Menu;
import com.dong.domain.User;
import com.dong.service.AccountServiceI;
import com.dong.utils.RedisUtil;

@Controller
public class LoginController {
	
	/**
	 * ��־
	 */
	private static Logger logger = LogManager.getLogger(LoginController.class);  

	@Resource
	private AccountServiceI accountService;
	
	@Resource
	private RedisUtil redisUtil;

	/**
	 * ��ת��¼ҳ��
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login.html", method = RequestMethod.GET)
	public String goToLogin(HttpServletRequest request, HttpServletResponse response, Model model) {
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser != null) {
            currentUser.logout();
        }
		return "login";
	}

	/**
	 * ��¼����
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public String Login(HttpServletRequest request, HttpServletResponse response, Model model, String username, String password) {
		// �û���¼ʵ��
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser != null) {
			currentUser.logout();
		}
		User user = accountService.getUserByUserName(username);
		// ��¼���Ž�shiro token
		UsernamePasswordToken token = new UsernamePasswordToken(user.getName(), user.getPassword());
		boolean isOk = false;
		try {
			currentUser.login(token);
			isOk = true;
		} catch (Exception e) {
			logger.error("Login error, please check it.");
			e.printStackTrace();
		}
		if (!isOk) {
			return "redirect:/login.html";
        }
		// ��¼�ɹ�����ȡ�˵���Ϣ
        user = (User) currentUser.getPrincipal();
        List<Menu> menus = accountService.getMenuByUserId(user.getUserId());
        currentUser.getSession().setAttribute("menus", menus);
        // �Ƿ�������redis
        currentUser.getSession().setAttribute("redisConnection", redisUtil.isConnected());
		return "redirect:/index.html";
	}
	
	/**
	 * �˳���¼
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/logout.html", method = RequestMethod.GET)
	public String goToLogout(HttpServletRequest request, HttpServletResponse response, Model model) {
		Subject currentUser = SecurityUtils.getSubject();
        if (SecurityUtils.getSubject().getSession() != null) {
//            if (Util.getCurrentUser() != null && Util.getShiroSession() != null) {
//                request.setAttribute("LOGIN_NAME", Util.getCurrentUser().getLoginName());
//                accountService.logoutSuccessful(Util.getCurrentUser().getUserId(),
//                        (String) Util.getShiroSession().getId());
//            }
            try {
                currentUser.logout();
                logger.debug("The user already logout.");
            } catch (InvalidSessionException e) {
            	logger.debug("The user already logout.");
            }
        }
        return "redirect:/index.html";
	}

}
