package com.base.aop;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

@Aspect
public class CacheAop {

	private final static Log logger = LogFactory.getLog(CacheAop.class);

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Around("@annotation(com.base.aop.CacheGet)")
	public Object cacheGet(final ProceedingJoinPoint pjp) throws Throwable {

		Object[] args = pjp.getArgs();
		Method method = getMethod(pjp);
		CacheGet cache = method.getAnnotation(CacheGet.class);
		String key = SpringExpressionUtils.parseValue(cache.key(), method, args, String.class);
		String cacheName = cache.clazz().getName();
		if (StringUtils.isBlank(key) || StringUtils.isBlank(cacheName)) {
			return pjp.proceed();
		}
		HashOperations<String, String, Object> operation = redisTemplate.opsForHash();
		Object value = operation.get(cacheName, key);
		logger.info("cacheGet执行,name值：" + cacheName + ",key值：" + key);
		if (value != null) {
			logger.info("缓存已存在,直接返回结果");
			return value;
		}
		value = pjp.proceed();
		if (value != null) {
			operation.put(cacheName, key, value);
			logger.info("缓存不存在,设置缓存");
		}
		return value;
	}

	@Around("@annotation(com.base.aop.CachePut)")
	public Object CachePut(final ProceedingJoinPoint pjp) throws Throwable {

		Object value = pjp.proceed();
		Object[] args = pjp.getArgs();
		Method method = getMethod(pjp);
		CachePut cache = method.getAnnotation(CachePut.class);
		String key = SpringExpressionUtils.parseValue(cache.key(), method, args, String.class);
		String cacheName = cache.clazz().getName();
		if (StringUtils.isNotBlank(key) && value != null) {
			HashOperations<String, String, Object> valueOper = redisTemplate.opsForHash();
			valueOper.put(cacheName, key, value);
			logger.info("CachePut执行,name值：" + cacheName + ",key值：" + key + ",value值：" + value);
		}
		return value;
	}

	@Around("@annotation(com.base.aop.CacheDelete)")
	public Object cacheDelete(final ProceedingJoinPoint pjp) throws Throwable {

		Object value = pjp.proceed();
		Object[] args = pjp.getArgs();
		Method method = getMethod(pjp);
		CacheDelete cache = method.getAnnotation(CacheDelete.class);
		String[] keys = cache.key();
		String key = "";
		Object[] array = null;
		Object keyValue = null;
		for (String k : keys) {
			keyValue = SpringExpressionUtils.parseValue(k, method, args, Object.class);
			if (keyValue != null) {
				break;
			}
		}
		if (keyValue == null) {
			return value;
		}
		if (keyValue instanceof String[]) {
			array = (String[]) keyValue;
		} else if (keyValue instanceof String) {
			key = (String) keyValue;
			if (StringUtils.isNotBlank(key)) {
				if (key.indexOf(",") > 0) {
					array = key.split(",");
				} else {
					array = new String[] { key };
				}
			}
		}
		HashOperations<String, String, Object> valueOper = redisTemplate.opsForHash();
		if (array != null) {
			valueOper.delete(cache.clazz().getName(), array);
			logger.info("cacheDelete执行,name值：" + cache.clazz().getName() + ",key值：" + key);
		}
		return value;
	}

	private Method getMethod(ProceedingJoinPoint pjp) {
		return ((MethodSignature) pjp.getSignature()).getMethod();
	}
}
