package com.dong.test;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-redis.xml" })
public class RedisTest {

	@Autowired
	private RedisTemplate<Serializable, Object> redisTemplate;

	@Test
	public void setValue() {
		redisTemplate.boundValueOps("name").set("Liverpool", 20, TimeUnit.SECONDS);
		
//		BoundListOperations<Serializable,Object> boundListOps = redisTemplate.boundListOps("lst");
//		boundListOps.rightPush("1");
//		boundListOps.rightPush("2");
//		boundListOps.rightPush("3");
		
	}

}
