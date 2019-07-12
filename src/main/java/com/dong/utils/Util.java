package com.dong.utils;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.dong.domain.User;

import sun.misc.BASE64Encoder;

public class Util {

	/**
	 * 将byte数组转成16进制输出
	 * 
	 * @param bytes
	 * @return
	 */
	public static String convertByteToHexString(byte[] bytes) {
		String str = "";
		for (byte b : bytes) {
			int temp = b & 0xff;
			String tempHex = Integer.toHexString(temp);
			if (tempHex.length() < 2) {
				str += "0" + tempHex;
			} else {
				str += tempHex;
			}
		}
		return str;
	}

	/**
	 * JDK自带的SHA-256加密
	 * 
	 * @param message
	 * @return
	 */
	public static String SHA256(String message) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] encode = digest.digest(message.getBytes());
			return convertByteToHexString(encode);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 生成随机盐值
	 * 
	 * @return
	 */
	public static String generateSalt() {
		Random RANDOM = new SecureRandom();
		byte[] salt = new byte[16];
		RANDOM.nextBytes(salt);
		String str = new BASE64Encoder().encode(salt);
		return str;
	}

	/**
	 * 判断字符串是否为null或者空串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 获取当前用户
	 * 
	 * @return
	 */
	public static User getCurrentUser() {
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		return user;
	}

	/**
	 * 把时间转成字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String convertTimeToStr(Date date) {
		String pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
        return sdf.format(date);
	}

}
