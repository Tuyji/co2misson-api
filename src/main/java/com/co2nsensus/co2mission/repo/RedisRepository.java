package com.co2nsensus.co2mission.repo;

import java.util.HashMap;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RedisRepository {

	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;

	public RedisRepository(@Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
		this.redisTemplate = redisTemplate;
		this.objectMapper = objectMapper;
	}

	public Object getAllKeysFromRedis(String key) {
		Object redisObject = new HashMap<Object, Object>();
		try {
			redisObject = redisTemplate.opsForHash().entries(key);
		} catch (Exception e) {
			log.error("REDIS ERR: RedisRepository.getAllKeysFromRedis : ", e);
		} finally {
			return redisObject;
		}
	}

	public Object getDataFromRedis(String key, String id) {
		Object redisObject = null;
		try {
			redisObject = redisTemplate.opsForHash().get(key, id);
		} catch (Exception e) {
			log.error("REDIS ERR: RedisRepository.getDataFromRedis : ", e);
		} finally {
			return redisObject;
		}
	}

	public void putDataToRedis(String key, String id, Object redisValue) {
		boolean isException = false;
		try {
			if (Objects.nonNull(redisValue)) {
				redisTemplate.opsForHash().put(key, id,
						objectMapper.writeValueAsString(redisValue));
			}
		} catch (Exception e) {
			isException = true;
			log.error("REDIS ERR: RedisRepository.putDataToRedis : ", e);
		} finally {
			if (isException) {
				log.error("Check Redis! Redis may be down!");
			}
		}
	}

}
