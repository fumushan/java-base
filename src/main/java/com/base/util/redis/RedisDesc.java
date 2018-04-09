package com.base.util.redis;

public class RedisDesc {

	/**
	 * SpringBoot集成Redis,使用方式按照下列步骤配置
	 */

	/**
	 * 1、增加依赖
	 */
	// <dependency>
	// <groupId>org.springframework.boot</groupId>
	// <artifactId>spring-boot-starter-data-redis</artifactId>
	// </dependency>

	/**
	 * 2、配置application.properties中的RedisPool配置属性
	 */
	// spring.redis.database=5
	// spring.redis.host=127.0.0.1
	// spring.redis.port=6379
	// spring.redis.password=123456
	// spring.redis.timeout=5000
	// spring.redis.pool.max-idle=10
	// spring.redis.pool.min-idle=5
	// spring.redis.pool.max-active=2000
	// spring.redis.pool.max-wait=1000

	/**
	 * 3、配置RedisConfig类
	 */
	// @Bean
	// public static JedisPool initJedisPool() {
	// JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
	// jedisPoolConfig.setMaxIdle(REDIS_MAX_IDLE);
	// jedisPoolConfig.setMaxTotal(REDIS_MAX_TOTAL);
	// jedisPoolConfig.setMaxWaitMillis(REDIS_MAX_WAITMILLIS);
	// jedisPoolConfig.setTestOnBorrow(false);
	// jedisPoolConfig.setTestOnReturn(true);
	// JedisPool jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379, 5000);//配置、地址、端口、超时设置
	// return jedisPool;
	// }

	/**
	 * 4、使用RedisTemplate进行缓存操作,可以自定义服务类RedisService
	 */
	// public class RedisService {
	// private RedisTemplate<String, Object> redisTemplate;
	//
	// public void setObject(String key, Object object) {
	// redisTemplate.opsForValue().set(key, object);
	// }
	//
	// public Object getObject(String key) {
	// return redisTemplate.opsForValue().get(key);
	// }
	// }

}
