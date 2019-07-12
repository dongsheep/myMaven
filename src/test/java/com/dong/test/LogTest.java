package com.dong.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogTest {

	public static void main(String[] args) {
//	    Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);  
//	    Logger logger = LogManager.getLogger("mylog");  
	    Logger logger = LogManager.getLogger(LogTest.class.getName());  
	    logger.trace("trace level");  
	    logger.debug("debug level");  
	    logger.info("info level");  
	    logger.warn("warn level");  
	    logger.error("error level");  
	    logger.fatal("fatal level");
	}
	
}
