package com.example.plusproject.common;

import org.springframework.stereotype.Component;

@Component
public class JwtHelper {
    public String generateAccessToken(Long id) {
        return "sample_access_token";
    }
}
