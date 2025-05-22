package com.example.plusproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class PlusProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlusProjectApplication.class, args); // 이 줄이 실행되어야 함
    }
}
