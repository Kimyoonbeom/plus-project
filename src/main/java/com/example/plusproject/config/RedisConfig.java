package com.example.plusproject.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
class RedisConfig {

	@Value("${spring.data.redis.host}")
	private String host;
	@Value("${spring.data.redis.port}")
	private int port;
	@Value("${spring.data.redis.password}")
	private String password;
	private static final String REDISSON_PREFIX = "redis://";

	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();
		config.useSingleServer()
			.setAddress(REDISSON_PREFIX + host + ":" + port);
		// 	.setPassword(password);
		// System.out.println("🐞 REDIS 연결 주소: " + host + ":" + port);
		// System.out.println("🐞 REDIS 비밀번호: " + password);

		RedisStandaloneConfiguration config2 = new RedisStandaloneConfiguration("localhost", 6379);
		config2.setDatabase(0);
		return Redisson.create(config);
	}

	@Bean
	public RedisTemplate<String, Long> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class)); // Long 값 직렬화
		return redisTemplate;
	}
}
