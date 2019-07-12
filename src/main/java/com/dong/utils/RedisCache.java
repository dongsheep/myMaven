package com.dong.utils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * 将Redis作为二级缓存
 * 
 * @author xiedongxiao
 *
 */

public class RedisCache implements Cache {

	private static Logger log = LogManager.getLogger(RedisCache.class);

	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final String id; // cache instance id

	private RedisTemplate<Object, Object> redisTemplate;

	private static final long EXPIRE_TIME_IN_MINUTES = 30; // redis过期时间

	/**
	 * 必须有，否则报错
	 * 
	 * @param id
	 */
	public RedisCache(String id) {
		if (id == null) {
			throw new IllegalArgumentException("Cache instances require an ID");
		}
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void putObject(Object key, Object value) {
		try {
			RedisTemplate<Object, Object> redisTemplate = getRedisTemplate();
			ValueOperations<Object, Object> opsForValue = redisTemplate.opsForValue();
			opsForValue.set(key, value, EXPIRE_TIME_IN_MINUTES, TimeUnit.MINUTES);
			log.debug("Put query result to redis");
		} catch (Throwable t) {
			log.error("Redis put failed", t);
		}
	}

	public Object getObject(Object key) {
		RedisTemplate<Object, Object> redisTemplate = getRedisTemplate();
		ValueOperations<Object, Object> opsForValue = redisTemplate.opsForValue();
		log.debug("Get cached query result from redis");
		return opsForValue.get(key);
	}

	public Object removeObject(Object key) {
		RedisTemplate<Object, Object> redisTemplate = getRedisTemplate();
		redisTemplate.delete(key);
		log.debug("Remove cached query result from redis");
		return null;
	}

	public void clear() {
		RedisTemplate<Object, Object> redisTemplate = getRedisTemplate();
		redisTemplate.execute((RedisCallback<Object>) connection -> {
			connection.flushDb();
			return null;
		});
//		redisTemplate.execute(new RedisCallback<Object>() {
//			public Object doInRedis(RedisConnection connection) throws DataAccessException {
//				connection.flushDb();
//				return null;
//			}
//		});
		log.debug("Clear all the cached query result from redis");
	}

	public int getSize() {
		return 0;
	}

	public ReadWriteLock getReadWriteLock() {
		return readWriteLock;
	}
	
	private RedisTemplate<Object, Object> getRedisTemplate() {
        if (redisTemplate == null) {
//            redisTemplate = ApplicationContextHolder.getBean("redisTemplate");
            
            String resource = "classpath:spring-redis.xml";
            ApplicationContext appCtx = new ClassPathXmlApplicationContext(resource);
            redisTemplate = (RedisTemplate<Object, Object>) appCtx.getBean("redisTemplate");
        }
        return redisTemplate;
}

}
