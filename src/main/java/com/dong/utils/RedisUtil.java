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
 * reids工具类
 * 
 * @author xiedongxiao
 *
 */

public class RedisUtil {

	/**
	 * 日志
	 */
	private static Logger log = LogManager.getLogger(RedisUtil.class);

	@Resource
	private RedisTemplate<Serializable, Object> redisTemplate;

	/**
	 * 判断redis是否连接
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
	 * 写入缓存
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
	 * 写入缓存(带过期时间)
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
	 * 读取缓存
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
	 * 缓存中是否存在对应的key
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 删除缓存
	 * 
	 * @param key
	 */
	public void remove(final String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * 批量删除缓存
	 * 
	 * @param keys
	 */
	public void remove(final String... keys) {
		for (String key : keys) {
			remove(key);
		}
	}

	/**
	 * 根据pattern批量删除key
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
