package com.co2nsensus.co2mission.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisMessagePublisher implements MessagePublisher {

	private RedisTemplate<String, Object> redisTemplate;
	private ChannelTopic topic;


	public RedisMessagePublisher(@Qualifier("redisPublisherTemplate") final RedisTemplate<String, Object> redisTemplate,
			final @Qualifier("paymentChannelTopic") ChannelTopic topic) {
		this.redisTemplate = redisTemplate;
		this.topic = topic;
	}

	public void publish(final String message) {
		redisTemplate.convertAndSend(topic.getTopic(), message);
	}
}