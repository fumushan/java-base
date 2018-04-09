package com.base.util.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis服务类
 */
public class RedisUtils {

	public static Logger logger = LoggerFactory.getLogger(RedisUtils.class);

	public final static String REDIS_HOST = "127.0.0.1";// 服务器地址
	public final static int REDIS_PORT = 6379;// 服务器端口
	public final static String REDIS_USERNAME = "";// 账号
	public final static String REDIS_PASSWORD = "fms800811739";// 密码

	public final static int REDIS_MAX_IDLE = 8;// 最大空闲连接数
	public final static int REDIS_MAX_TOTAL = 8;// 最大连接数
	public final static int REDIS_MAX_WAITMILLIS = 1000;// 最大等待时间,超时就抛出异常
	public final static int REDIS_TIMEOUT = 5000;// 超时时间
	public final static int REDIS_DATASOURCE = 0;// 数据库编号0~15

	public static JedisPool initJedisPool() {
		logger.info("初始化Redis连接池");
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(REDIS_MAX_IDLE);
		jedisPoolConfig.setMaxTotal(REDIS_MAX_TOTAL);
		jedisPoolConfig.setMaxWaitMillis(REDIS_MAX_WAITMILLIS);
		jedisPoolConfig.setTestOnBorrow(false);
		jedisPoolConfig.setTestOnReturn(true);

		logger.info("初始化Redis连接池：host=" + REDIS_HOST + ",port=" + REDIS_PORT);
		JedisPool jedisPool = new JedisPool(jedisPoolConfig, REDIS_HOST, REDIS_PORT, REDIS_TIMEOUT);
		logger.info("初始化Redis连接池成功");
		return jedisPool;
	}

	public static Jedis initJedis() {
		logger.info("Redis开始连接");
		Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT);
		logger.info("Redis连接成功：host=" + REDIS_HOST + ",port=" + REDIS_PORT);
		return jedis;
	}

	public static void setString(String key, String value) {
		Jedis jedis = RedisUtils.initJedis();
		jedis.set(key, value);
	}

	public static String getString(String key) {
		Jedis jedis = RedisUtils.initJedis();
		return jedis.get(key);
	}

	public static void main(String[] args) {
		// 方法一
		JedisPool jedisPool = RedisUtils.initJedisPool();
		Jedis jedis = jedisPool.getResource();

		// 方法二
		// Jedis jedis = RedisUtils.initJedis();

		jedis.set("fumushan", "1234567");
		System.out.println(jedis.get("fumushan"));
	}
}
