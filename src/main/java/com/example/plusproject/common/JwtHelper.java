package com.example.plusproject.common;

import com.example.plusproject.config.JwtUtil;
import com.example.plusproject.domain.user.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class JwtHelper {
    private final JwtUtil jwtUtil;

    public JwtHelper(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String generateAccessToken(Long id, String email, UserRole userRole, String nickname) {
        return jwtUtil.createToken(id, email, userRole, nickname);
    }
}
