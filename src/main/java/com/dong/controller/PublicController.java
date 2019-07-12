package com.dong.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dong.service.UserServiceI;

@Controller
public class PublicController {
	
	/**
	 * »’÷æ
	 */
	private static Logger log = LogManager.getLogger(PublicController.class);

	@Resource
	private UserServiceI userService;

	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String goToIndex(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.debug("Go To index.jsp...");
		
		return "index";
	}
	
	

}
