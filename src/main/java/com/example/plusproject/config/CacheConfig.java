package com.example.plusproject.config;

import java.time.Duration;
import java.util.List;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	@Primary
	public CacheManager localCacheManager(){
		return new ConcurrentMapCacheManager("bookSearchLocal","TopKeywordLocal");
	}

	@Bean
	public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory){
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.activateDefaultTyping(
			objectMapper.getPolymorphicTypeValidator(),
			ObjectMapper.DefaultTyping.NON_FINAL,
			JsonTypeInfo.As.PROPERTY
		);

		GenericJackson2JsonRedisSerializer objectSerializer =
			new GenericJackson2JsonRedisSerializer(objectMapper);

		Jackson2JsonRedisSerializer<List> stringListSerializer =
			new Jackson2JsonRedisSerializer<>(List.class);

		RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
			.entryTtl(Duration.ofMinutes(10))
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(objectSerializer));

		RedisCacheConfiguration topKeywordConfig = RedisCacheConfiguration.defaultCacheConfig()
			.entryTtl(Duration.ofMinutes(10))
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(stringListSerializer));

		return RedisCacheManager.builder(connectionFactory)
			.cacheDefaults(defaultConfig)
			.withCacheConfiguration("TopKeywordRedis",topKeywordConfig)
			.build();


	}
}
