package com.example.plusproject.config;

import java.rmi.ServerException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.plusproject.domain.user.enums.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static final String BEARER_PREFIX = "Bearer ";
	private static final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

	@Value("${jwt.secret.key}")
	private String secretKey;

	public String createToken(Long userId, String email, UserRole userRole, String nickName) {
		Date date = new Date();

		return BEARER_PREFIX +
			Jwts.builder()
				.setSubject(String.valueOf(userId))
				.claim("email", email)
				.claim("userRole", userRole)
				.claim("nickName", nickName)
				.setExpiration(new Date(date.getTime() + TOKEN_TIME))
				.setIssuedAt(date)
				.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
				.compact();
	}

	public String substringToken(String tokenValue) {
		if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)){
			return tokenValue.substring(7);
		}
		throw new RuntimeException("NOT FOUND TOKEN");
	}

	public Claims extractClaims(String token){
		return Jwts.parserBuilder()
			.setSigningKey(secretKey.getBytes())
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

}
