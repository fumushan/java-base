package com.base.aop.redis;

import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * 封装redis 缓存服务器服务接口
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class RedisUtil {

	private RedisTemplate<String, Object> redisTemplate;

	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 缓存Object对象
	 */
	public void putString(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 获取Object对象
	 */
	public Object getString(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	/**
	 * 删除Object对象
	 */
	public boolean deleteString(String key) {
		return redisTemplate.delete(key);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 缓存List集合
	 */
	public Long putList(String key, Object value) {
		return redisTemplate.opsForList().leftPush(key, value);
	}

	/**
	 * 缓存List集合
	 */
	public Long putList(Class clazz, Object value) {
		return redisTemplate.opsForList().leftPush(clazz.getSimpleName(), value);
	}

	/**
	 * 获取List缓存
	 */
	public Object getList(String key) {
		return redisTemplate.opsForList().leftPop(key);
	}

	/**
	 * 获取List缓存
	 */
	public Object getList(Class clazz) {
		return redisTemplate.opsForList().leftPop(clazz.getSimpleName());
	}

	/**
	 * 获取List集合长度
	 */
	public Long getListSize(String key) {
		return redisTemplate.opsForList().size(key);
	}

	/**
	 * 获取List集合长度
	 */
	public Long getListSize(Class clazz) {
		return redisTemplate.opsForList().size(clazz.getSimpleName());
	}

	/**
	 * 删除List集合缓存中值为value的n个数据,返回删除的个数,如果没有这个元素则返回0
	 */
	public void deleteList(Class clazz, long n, String value) {
		redisTemplate.opsForList().remove(clazz.getSimpleName(), n, value);
	}

	/**
	 * 删除List集合缓存中值为value的n个数据,返回删除的个数,如果没有这个元素则返回0
	 */
	public void deleteList(String key, long n, String value) {
		redisTemplate.opsForList().remove(key, n, value);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 缓存Map集合
	 */
	public void putMap(String key, Map<Object, Object> map) {
		redisTemplate.opsForHash().putAll(key, map);
	}

	/**
	 * 缓存Map集合
	 */
	public void putMap(Class clazz, Map<Object, Object> map) {
		redisTemplate.opsForHash().putAll(clazz.getSimpleName(), map);
	}

	/**
	 * 获取Map集合
	 */
	public Map<Object, Object> getMap(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

	/**
	 * 获取Map集合
	 */
	public Map<Object, Object> getMap(Class clazz) {
		return redisTemplate.opsForHash().entries(clazz.getSimpleName());
	}

	/**
	 * 获取Map集合长度
	 */
	public long getMapSize(String key) {
		return redisTemplate.opsForHash().size(key);
	}

	/**
	 * 获取Map集合长度
	 */
	public long getMapSize(Class clazz) {
		return redisTemplate.opsForHash().size(clazz.getSimpleName());
	}

}