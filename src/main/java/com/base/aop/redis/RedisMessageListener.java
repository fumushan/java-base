package com.base.aop.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSON;

public class RedisMessageListener implements MessageListener {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RedisTemplate<?, ?> redisTemplate;

	@Override
	public void onMessage(Message message, byte[] pattern) {

		byte[] body = message.getBody();// 请使用valueSerializer
		byte[] channel = message.getChannel();
		// 请参考配置文件，本例中key，value的序列化方式均为string。
		// 其中key必须为stringSerializer。和redisTemplate.convertAndSend对应
		// redisTemplate.convertAndSend(topic, msg);
		Object msg = redisTemplate.getValueSerializer().deserialize(body);
		String topic = (String) redisTemplate.getStringSerializer().deserialize(channel);
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("topic:" + topic);
		logger.info("msg:" + JSON.toJSONString(msg));
	}
}
