package com.dong.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局异常处理
 * 
 * @author xiedongxiao
 *
 */

@ControllerAdvice
public class GlobalExceptionController {

	/**
	 * 日志
	 */
	private static Logger log = LogManager.getLogger(GlobalExceptionController.class);
	
    @ExceptionHandler(Exception.class)
//    @LyodsLog(position = 0, details = "HandleException:")
    public ModelAndView handleException(Exception ex, HttpServletRequest request, HttpServletResponse resonpse) {
    	
    	ModelAndView mav = new ModelAndView("exception");
//    	request.setAttribute("errorMsg", ex.getMessage());
    	mav.addObject("errorMsg", ex.getMessage());
    	log.error("System Error:" + ex.getMessage());
    	return mav;
    	
//        String acceptStr = request.getHeader("accept");
//        boolean isJson = false;
//        // 判断是否json格式
//        if (acceptStr != null && acceptStr.indexOf("application/json") >= 0) {
//            isJson = true;
//        }
//        log.error("Lyods Engine Error:", ex);
//        if (isJson) {
//            try {
//                if (ex instanceof LyodsException) {
//                    Util.writeJsonErrorMsg(messageSource, request, resonpse, ex.getMessage());
//                } else if (ex instanceof UnauthorizedException) {
//                    Util.writeJsonErrorMsg(messageSource, request, resonpse, "ERRCODE.9997");
//                } else if (ex instanceof UnauthenticatedException) {
//                    Util.writeJsonErrorMsg(messageSource, request, resonpse, "ERRCODE.2001");
//                } else if (ex instanceof InvalidDataAccessApiUsageException) {
//                    Util.writeJsonErrorMsg(messageSource, request, resonpse, "ERRCODE.9991");
//                } else if (ex instanceof SizeLimitExceededException) {
//                    Util.writeJsonErrorMsg(messageSource, request, resonpse, "ERRCODE.9980");
//                } else {
//                    Util.writeJsonErrorMsg(messageSource, request, resonpse, "ERRCODE.9999", Util.getStackTrace(ex));
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        } else {
//            ModelAndView mav = new ModelAndView("error/error");
//            String retUrl = request.getParameter("returnUrl");
//            if (Util.isEmpty(retUrl)) {
//                retUrl = (String) request.getAttribute("returnUrl");
//            }
//            if (!Util.isEmpty(retUrl)) {
//                mav.addObject("returnUrl", retUrl);
//                // 均没有设置，如果是ERRCODE.2001则不处理,否则默认取上一步URL地址
//            } else {
//                if (!"ERRCODE.2001".equals(ex.getMessage())) {
//                    String preUrl = request.getHeader("Referer");
//                    if(!Util.isEmpty(preUrl))
//					{
//						String url = request.getRequestURL().toString();
//						String urlPre=url.substring(0,url.indexOf(request.getRequestURI()))+request.getContextPath()+"/";
//						if(preUrl.indexOf(urlPre)>=0)
//						{
//							mav.addObject("returnUrl", preUrl.substring(urlPre.length()));
//						}else
//						{
//							mav.addObject(
//									"errorMsg",String.format("Referer is Cross Site Request Forgery URL is %s, PreURL is %s.",url,preUrl));
//							return mav;
//						}						
//					}
//                }
//            }
//            // 返回出错信息
//            if (ex instanceof LyodsException) {
//                mav.addObject("errorMsg", Util.getMessageByCode(messageSource, request, ex.getMessage()));
//            } else if (ex instanceof UnauthorizedException) {
//                mav.addObject("errorMsg", Util.getMessageByCode(messageSource, request, "ERRCODE.9997"));
//            } else if (ex instanceof UnauthenticatedException) {
//                mav.addObject("errorMsg", Util.getMessageByCode(messageSource, request, "ERRCODE.2001"));
//            } else if (ex instanceof InvalidDataAccessApiUsageException) {
//                mav.addObject("errorMsg", Util.getMessageByCode(messageSource, request, "ERRCODE.9991"));
//            } else if (ex instanceof SizeLimitExceededException) {
//                mav.addObject("errorMsg", Util.getMessageByCode(messageSource, request, "ERRCODE.9980"));
//            } else {
//                mav.addObject("errorMsg",
//                        Util.getMessageByCode(messageSource, request, "ERRCODE.9999") + "[" + Util.getStackTrace(ex)
//                                + "]");
//            }
//            return mav;
//        }
    }
	
}
