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
 * �Զ���redis����aop������
 * 
 * @author xiedongxiao
 *
 */

@Component
@Aspect
public class MyRedisInterceptor {

	/**
	 * ��־
	 */
	private static Logger log = LogManager.getLogger(MyRedisInterceptor.class);

	@Resource
	private RedisUtil redisUtil;

	@Value("${redis.expireTime}")
	private long expireTime;

	// �����
	@Pointcut("execution(* com.dong.service..*Impl.*(..))")
	private void anyMethod() {
	}// ����һ�������

	// @Before("anyMethod() && args(name)")
	// public void doAccessCheck(String name) {
	// // System.out.println(name);
	// // System.out.println("ǰ��֪ͨ");
	// }

	// @AfterReturning("anyMethod()")
	// public void doAfter() {
	// // System.out.println("����֪ͨ");
	// }

	// @After("anyMethod()")
	// public void after() {
	// System.out.println("����֪ͨ");
	// }
	//
	// @AfterThrowing("anyMethod()")
	// public void doAfterThrow() {
	// System.out.println("����֪ͨ");
	// }

	@Around("anyMethod()")
	public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
		// System.out.println("���뻷��֪ͨ");
		// Object object = pjp.proceed();// ִ�и÷���
		// System.out.println("�˳�����");
		// return object;
		Object result = null;
		MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();
		// Method method = joinPointObject.getMethod(); // ֻ�ܻ�ýӿڷ����������Ǿ���ʵ����ķ���
		Object target = pjp.getTarget();
		Method method = target.getClass().getMethod(joinPointObject.getName(), joinPointObject.getParameterTypes());
		MyRedis redis = method.getAnnotation(com.dong.constant.MyRedis.class);
		// �ж��Ƿ���redisע����߷������Զ���redisע��
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
	 * ��ȡ�����key key ������ע���ϣ�֧��SPEL���ʽ
	 * 
	 * @param pjp
	 * @return
	 */
	private String parseKey(String key, Method method, Object[] args) {
		// ��ȡ�����ط����������б�(ʹ��Spring֧�����)
		LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
		String[] paraNameArr = u.getParameterNames(method);
		// ʹ��SPEL����key�Ľ���
		ExpressionParser parser = new SpelExpressionParser();
		// SPEL������
		StandardEvaluationContext context = new StandardEvaluationContext();
		// �ѷ�����������SPEL��������
		for (int i = 0; i < paraNameArr.length; i++) {
			context.setVariable(paraNameArr[i], args[i]);
		}
		return parser.parseExpression(key).getValue(context, String.class);
	}

}
