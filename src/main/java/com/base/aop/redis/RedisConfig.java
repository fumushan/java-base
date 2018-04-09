package com.base.aop.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

	public final static String REDIS_HOST = "127.0.0.1";// 服务器地址
	public final static int REDIS_PORT = 6379;// 服务器端口
	public final static String REDIS_USERNAME = "";// 账号
	public final static String REDIS_PASSWORD = "fms800811739";// 密码

	public final static int REDIS_MAX_IDLE = 8;// 最大空闲连接数
	public final static int REDIS_MAX_TOTAL = 8;// 最大连接数
	public final static int REDIS_MAX_WAITMILLIS = 1000;// 最大等待时间,超时就抛出异常
	public final static int REDIS_TIMEOUT = 5000;// 超时时间
	public final static int REDIS_DATASOURCE = 0;// 数据库编号0~15

	/**
	 * RedisPool初始化配置
	 */
	@Bean
	public static JedisPool initJedisPool() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(REDIS_MAX_IDLE);
		jedisPoolConfig.setMaxTotal(REDIS_MAX_TOTAL);
		jedisPoolConfig.setMaxWaitMillis(REDIS_MAX_WAITMILLIS);
		jedisPoolConfig.setTestOnBorrow(false);
		jedisPoolConfig.setTestOnReturn(true);
		JedisPool jedisPool = new JedisPool(jedisPoolConfig, REDIS_HOST, REDIS_PORT, REDIS_TIMEOUT);
		return jedisPool;
	}

	/**
	 * StringRedisTemplate配置
	 */
	@Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

	/**
	 * RedisTemplate配置
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		Jackson2JsonRedisSerializer<Object> redisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
		objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		redisSerializer.setObjectMapper(objectMapper);

		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(redisSerializer);
		template.setValueSerializer(redisSerializer);
		template.setHashKeySerializer(redisSerializer);
		template.setHashValueSerializer(redisSerializer);
		template.afterPropertiesSet();
		return template;
	}

}
