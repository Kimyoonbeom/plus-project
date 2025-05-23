package com.example.plusproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestConfig {
    @Bean
    public RestClient restClient() {
        // Timeout, Retry, ...
        return RestClient.builder().build();
    }
}
