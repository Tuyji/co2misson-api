package com.co2nsensus.co2mission.redis.config;

import com.co2nsensus.co2mission.redis.MessagePublisher;
import com.co2nsensus.co2mission.redis.RedisMessagePublisher;
import com.co2nsensus.co2mission.redis.SubscriptionMessageSubscriber;
import com.co2nsensus.co2mission.redis.TransactionMessageSubscriber;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.text.SimpleDateFormat;

@Configuration
public class RedisSubscriptionConfig {

	@Value("${app.redis.payment-channel}")
	private String paymentChannelTopic;

	@Value("${app.redis.subscription-updated-channel}")
	private String subscriptionUpdatedChannelTopic;

	@Autowired
	private JedisConnectionFactory redisConnectionFactory;

	@Autowired
	public void setTransactionMessageSubscriber(TransactionMessageSubscriber transactionMessageSubscriber) {
		this.transactionMessageSubscriber = transactionMessageSubscriber;
	}

	private TransactionMessageSubscriber transactionMessageSubscriber;

	@Bean
	RedisMessageListenerContainer redisContainer(
			@Qualifier("subscriptionUpdatedListenerAdapter") MessageListenerAdapter subscriptionUpdatedListenerAdapter) {
		final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);
		container.addMessageListener(paymentCreatedListenerAdapter(), paymentChannelTopic());
		container.addMessageListener(subscriptionUpdatedListenerAdapter, subscriptionUpdatedChannelTopic());
		return container;
	}

	@Bean("subscriptionUpdatedListenerAdapter")
	MessageListenerAdapter subscriptionUpdatedListenerAdapter(
			SubscriptionMessageSubscriber subscriptionMessageSubscriber) {
		return new MessageListenerAdapter(subscriptionMessageSubscriber, "onUpdated");
	}

	@Bean
	public RedisTemplate<String, Object> redisPublisherTemplate() {
		final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);

		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		return redisTemplate;
	}

	@Bean
	MessageListenerAdapter paymentCreatedListenerAdapter() {
		return new MessageListenerAdapter(transactionMessageSubscriber);
	}

	@Bean
	MessagePublisher redisPublisher() {
		return new RedisMessagePublisher(redisPublisherTemplate(), paymentChannelTopic());
	}

	@Bean
	ChannelTopic paymentChannelTopic() {
		return new ChannelTopic(paymentChannelTopic);
	}

	@Bean
	ChannelTopic subscriptionUpdatedChannelTopic() {
		return new ChannelTopic(subscriptionUpdatedChannelTopic);
	}

}
