package com.dong.utils;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * reids������
 * 
 * @author xiedongxiao
 *
 */

public class RedisUtil {

	/**
	 * ��־
	 */
	private static Logger log = LogManager.getLogger(RedisUtil.class);

	@Resource
	private RedisTemplate<Serializable, Object> redisTemplate;

	/**
	 * �ж�redis�Ƿ�����
	 * 
	 * @return
	 */
	public boolean isConnected() {
		boolean flag = true;
		try {
			RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
			RedisConnection connection = connectionFactory.getConnection();
			String ping = connection.ping();
			if (Util.isEmpty(ping)) {
				flag = false;
			}
		} catch (Exception e) {
			log.warn("The redis doen not open connection...");
			flag = false;
		}
		return flag;
	}

	/**
	 * д�뻺��
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(final String key, Object value) {
		boolean rec = false;
		try {
			ValueOperations<Serializable, Object> opsForValue = redisTemplate.opsForValue();
			opsForValue.set(key, value);
			rec = true;
		} catch (Exception e) {
			log.error("Set redis error..." + e.toString());
			e.printStackTrace();
		}
		return rec;
	}

	/**
	 * д�뻺��(������ʱ��)
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 * @return
	 */
	public boolean set(final String key, Object value, long expireTime) {
		boolean rec = false;
		try {
			ValueOperations<Serializable, Object> opsForValue = redisTemplate.opsForValue();
			opsForValue.set(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			rec = true;
		} catch (Exception e) {
			log.error("Set redis error..." + e.toString());
			e.printStackTrace();
		}
		return rec;
	}

	/**
	 * ��ȡ����
	 * 
	 * @param key
	 * @return
	 */
	public Object get(final String key) {
		ValueOperations<Serializable, Object> opsForValue = redisTemplate.opsForValue();
		Object obj = opsForValue.get(key);
		return obj;
	}

	/**
	 * �������Ƿ���ڶ�Ӧ��key
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * ɾ������
	 * 
	 * @param key
	 */
	public void remove(final String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * ����ɾ������
	 * 
	 * @param keys
	 */
	public void remove(final String... keys) {
		for (String key : keys) {
			remove(key);
		}
	}

	/**
	 * ����pattern����ɾ��key
	 * 
	 * @param pattern
	 */
	public void removePattern(final String pattern) {
		Set<Serializable> keys = redisTemplate.keys(pattern);
		if (keys.size() > 0) {
			redisTemplate.delete(keys);
		}
	}

}
