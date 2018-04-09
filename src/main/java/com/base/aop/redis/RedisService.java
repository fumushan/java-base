package com.base.aop.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis服务类
 */
public class RedisService {

	/**
	 * StringRedisTemplate与RedisTemplate
	 * StringRedisTemplate继承RedisTemplate,但两者的数据是不共通的
	 */
	private StringRedisTemplate stringRedisTemplate;

	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 缓存字符串
	 */
	public void putString(String key, String value) {
		stringRedisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 缓存字符串(设置超时)
	 */
	public void putString(String key, String value, long timeout) {
		stringRedisTemplate.opsForValue().set(key, value, timeout);
	}

	/**
	 * 获取缓存字符串
	 */
	public String getString(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}

	/**
	 * StringRedisTemplate删除缓存字符串
	 */
	public boolean deleteString(String key) {
		return stringRedisTemplate.delete(key);
	}

	/**
	 * 缓存对象
	 */
	public void putObject(String key, Object object) {
		redisTemplate.opsForValue().set(key, object);
	}

	/**
	 * 缓存对象(设置超市)
	 */
	public void putObject(String key, Object object, long timeout) {
		redisTemplate.opsForValue().set(key, object, timeout);
	}

	/**
	 * 获取缓存对象
	 */
	public Object getObject(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	/**
	 * 缓存List集合
	 */
	public long putList(String key, List<Object> list) {
		return redisTemplate.opsForList().leftPush(key, list);
	}

	/**
	 * 获取List集合
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getList(String key) {
		return (List<Object>) redisTemplate.opsForList().leftPop(key);
	}

	/**
	 * 缓存Map集合
	 */
	public void putMap(String key, Map<Object, Object> map) {
		redisTemplate.opsForHash().putAll(key, map);
	}

	/**
	 * 获取缓存Map集合
	 */
	public Map<Object, Object> getMap(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

	/**
	 * 缓存Set集合
	 */
	public long putSet(String key, Set<Object> set) {
		return redisTemplate.opsForSet().add(key, set);
	}

	/**
	 * 获取缓存Set集合
	 */
	public Set<Object> getSet(String key, Set<Object> set) {
		return redisTemplate.opsForSet().members(key);
	}

	/**
	 * RedisTemplate删除缓存对象
	 */
	public boolean deleteObject(String key) {
		return redisTemplate.delete(key);
	}

}
