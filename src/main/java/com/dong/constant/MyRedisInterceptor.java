package com.dong.constant;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.dong.utils.RedisUtil;

/**
 * 自定义redis缓存aop拦截器
 * 
 * @author xiedongxiao
 *
 */

@Component
@Aspect
public class MyRedisInterceptor {

	/**
	 * 日志
	 */
	private static Logger log = LogManager.getLogger(MyRedisInterceptor.class);

	@Resource
	private RedisUtil redisUtil;

	@Value("${redis.expireTime}")
	private long expireTime;

	// 切入点
	@Pointcut("execution(* com.dong.service..*Impl.*(..))")
	private void anyMethod() {
	}// 定义一个切入点

	// @Before("anyMethod() && args(name)")
	// public void doAccessCheck(String name) {
	// // System.out.println(name);
	// // System.out.println("前置通知");
	// }

	// @AfterReturning("anyMethod()")
	// public void doAfter() {
	// // System.out.println("后置通知");
	// }

	// @After("anyMethod()")
	// public void after() {
	// System.out.println("最终通知");
	// }
	//
	// @AfterThrowing("anyMethod()")
	// public void doAfterThrow() {
	// System.out.println("例外通知");
	// }

	@Around("anyMethod()")
	public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
		// System.out.println("进入环绕通知");
		// Object object = pjp.proceed();// 执行该方法
		// System.out.println("退出方法");
		// return object;
		Object result = null;
		MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();
		// Method method = joinPointObject.getMethod(); // 只能获得接口方法，而不是具体实现类的方法
		Object target = pjp.getTarget();
		Method method = target.getClass().getMethod(joinPointObject.getName(), joinPointObject.getParameterTypes());
		MyRedis redis = method.getAnnotation(com.dong.constant.MyRedis.class);
		// 判断是否开启redis注解或者方法有自定义redis注解
		Subject currentUser = SecurityUtils.getSubject();
		if (redis == null || !(Boolean) currentUser.getSession().getAttribute("redisConnection")) {
			try {
				result = pjp.proceed();
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return result;
		}
		try {
			String key = redis.key();
			String fieldValue = parseKey(redis.fieldValue(), method, pjp.getArgs());
			result = redisUtil.get(key + "_" + fieldValue);
			if (result == null) {
				try {
					result = pjp.proceed();
					long expire = redis.expireTime();
					if (expire == -1) {
						expire = expireTime;
					}
					redisUtil.set(key + "_" + fieldValue, result, expire);
					log.info("The key[" + key + "_" + fieldValue + "] has been set in redis...");
				} catch (Throwable e) {
					e.printStackTrace();
				}
			} else {
				log.info("The key[" + key + "_" + fieldValue + "] has been read from redis...");
			}
		} catch (Exception e) {
			log.warn("The redis does not open connection or something wrong, please check it...");
			try {
				result = pjp.proceed();
			} catch (Throwable e2) {
				e2.printStackTrace();
			}
			return result;
		}
		return result;
	}

	/**
	 * 获取缓存的key key 定义在注解上，支持SPEL表达式
	 * 
	 * @param pjp
	 * @return
	 */
	private String parseKey(String key, Method method, Object[] args) {
		// 获取被拦截方法参数名列表(使用Spring支持类库)
		LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
		String[] paraNameArr = u.getParameterNames(method);
		// 使用SPEL进行key的解析
		ExpressionParser parser = new SpelExpressionParser();
		// SPEL上下文
		StandardEvaluationContext context = new StandardEvaluationContext();
		// 把方法参数放入SPEL上下文中
		for (int i = 0; i < paraNameArr.length; i++) {
			context.setVariable(paraNameArr[i], args[i]);
		}
		return parser.parseExpression(key).getValue(context, String.class);
	}

}
