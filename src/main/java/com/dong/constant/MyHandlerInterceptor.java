package com.dong.constant;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dong.domain.User;
import com.dong.service.AccountServiceI;
import com.dong.utils.Util;

/**
 * 自定义拦截器
 * 
 * @author xiedongxiao
 *
 */
public class MyHandlerInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 日志
	 */
	private static Logger log = LogManager.getLogger(MyHandlerInterceptor.class);

	@Resource
	private AccountServiceI accountService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		log.debug("call preHandle...");
		// 是否是需要验证权限或者验证的路径
		boolean isAuth = false;
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			RequiresPermissions mthReqPer = handlerMethod.getMethodAnnotation(RequiresPermissions.class);
			if (mthReqPer != null) {
				log.debug("handlerMethod*(" + handlerMethod + ")* has RequiresPermissions annotation.");
				isAuth = true;
			} else {
				RequiresAuthentication mthReqAuth = handlerMethod.getMethodAnnotation(RequiresAuthentication.class);
				if (mthReqAuth != null) {
					log.debug("handlerMethod*(" + handlerMethod + ")* has RequiresAuthentication annotation.");
					isAuth = true;
				} else {
					RequiresAuthentication clsReqAuth = handlerMethod.getBeanType().getAnnotation(RequiresAuthentication.class);
					if (clsReqAuth != null) {
						log.debug("handlerClass*(" + handlerMethod.getBean() + ")* has RequiresAuthentication annotation.");
						isAuth = true;
					}
				}
			}
		}
		if (isAuth) {
			User currUser = Util.getCurrentUser();
			if (currUser == null) {
				log.warn("Please login first...");
				request.getRequestDispatcher("/index.html").forward(request, response);
				return false;
			}
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//		log.debug("postHandle");
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//		log.debug("afterCompletion");
		super.afterCompletion(request, response, handler, ex);
	}

}
