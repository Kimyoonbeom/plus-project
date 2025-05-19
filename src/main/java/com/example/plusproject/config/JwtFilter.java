package com.example.plusproject.config;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.plusproject.common.dto.AuthUser;
import com.example.plusproject.domain.user.enums.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter implements Filter {

	private final JwtUtil jwtUtil;
	private static final List<String> WITHE_LIST=List.of(
		"/auth",
		"/search",
		"/test",
		"/topkeywords"
	);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException{Filter.super.init(filterConfig);}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws
		IOException,
		ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String url = httpRequest.getRequestURI();

		for (String prefix : WITHE_LIST){
			if(url.startsWith(prefix)){
				filterChain.doFilter(request,response);
				return;
			}
		}

		String bearerJwt = httpRequest.getHeader("Authorization");

		if(bearerJwt == null){
			httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,"토큰이 필요합니다.");
			return;
		}

		String jwt = jwtUtil.substringToken(bearerJwt);

		try{
			Claims claims = jwtUtil.extractClaims(jwt);
			if(claims == null){
				httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,"잘못된 토큰입니다.");
				return;
			}

			String userRoleStr = claims.get("userRole",String.class);
			UserRole userRole = UserRole.valueOf(userRoleStr);

			AuthUser authUser = new AuthUser(
				Long.parseLong(claims.getSubject()),
				(String)claims.get("email"),
				userRole,
				(String)claims.get("nickName")
			);

			SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+userRoleStr);
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
				authUser,
				null,
				List.of(authority)
			);

			SecurityContextHolder.getContext().setAuthentication(auth);

			if(url.startsWith("/admin")){
				if(!UserRole.ADMIN.equals(userRole)){
					httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN,"권한이 없습니다.");
					return;
				}
				filterChain.doFilter(request,response);
				return;
			}

			filterChain.doFilter(request,response);
		}catch (SecurityException | MalformedJwtException e){
			log.error("Invalid JWT signature, 유효하지 않는 서명입니다.", e);
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"유효하지 않는 서명입니다.");
		}catch (ExpiredJwtException e){
			log.error("Expired JWT token, 만료된 토큰입니다.", e);
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"만료된 토큰 입니다.");
		}catch (UnsupportedJwtException e){
			log.error("Unsupported JWT token, 지원되지 않는 토큰입니다.", e);
			httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,"지원되지 않는 토큰입니다.");
		}catch (Exception e){
			log.error("Internal server error", e);
			httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void destroy(){Filter.super.destroy();}
}
