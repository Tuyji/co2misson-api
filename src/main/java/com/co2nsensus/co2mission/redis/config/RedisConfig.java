package com.co2nsensus.co2mission.redis.config;

import com.co2nsensus.co2mission.logging.RedisCacheErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig extends CachingConfigurerSupport {

	@Value("${app.redis.payment-channel}")
	private String paymentChannelTopic;

	@Value("${app.redis.subscription-updated-channel}")
	private String subscriptionUpdatedChannelTopic;

	@Value("${spring.redis.config.host}")
	private String redisHost;

	@Value("${spring.redis.config.port}")
	private String redisPort;

	@Value("${spring.redis.config.password}")
	private String redisPassword;

	@Override
	public CacheErrorHandler errorHandler() {
		return new RedisCacheErrorHandler();
	}
}