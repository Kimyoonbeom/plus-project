package com.example.plusproject.domain.search.controller;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.plusproject.common.dto.AuthUser;
import com.example.plusproject.domain.book.dto.BookPageDto;
import com.example.plusproject.domain.book.dto.BookResponseDto;
import com.example.plusproject.domain.search.service.SearchService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;

	@PostMapping("/search/v1") // 일반 검색과 페이징, 캐시 사용 안함
	public ResponseEntity<Page<BookResponseDto>> search1(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestParam(defaultValue = "1") Integer page,
		@RequestParam(defaultValue = "10") Integer size,
		@RequestParam(required = false) String keyword,
		HttpServletRequest request,
		HttpServletResponse response
	){
		System.out.println("🔥 authUser null 여부 확인 " + authUser);
		String userId = resolveUserId(authUser,request,response);
		System.out.println("🔥 guestId = " + userId);
		keyword = keyword.trim().toLowerCase();
		System.out.println("🔥 keyword = " + keyword);
		searchService.saveSearchLog(userId,keyword);
		return ResponseEntity.ok(searchService.search1(keyword, page, size));
	}

	@PostMapping("/search/v2") // 일반 검색과 페이징, 캐시 사용 안함
	public ResponseEntity<Page<BookResponseDto>> search2(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestParam(defaultValue = "1") Integer page,
		@RequestParam(defaultValue = "10") Integer size,
		@RequestParam(required = false) String keyword,
		HttpServletRequest request,
		HttpServletResponse response
	){
		System.out.println("🔥 authUser null 여부 확인 " + authUser);
		String userId = resolveUserId(authUser,request,response);
		System.out.println("🔥 guestId = " + userId);
		keyword = keyword.trim().toLowerCase();
		System.out.println("🔥 keyword = " + keyword);
		searchService.saveSearchLog(userId,keyword);
		return ResponseEntity.ok(searchService.search2(keyword, page, size));
	}

	@PostMapping("/search/v3") // 일반 검색과 페이징, 캐시 사용 안함
	public ResponseEntity<Page<BookResponseDto>> search3(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestParam(defaultValue = "1") Integer page,
		@RequestParam(defaultValue = "10") Integer size,
		@RequestParam(required = false) String keyword,
		HttpServletRequest request,
		HttpServletResponse response
	){
		System.out.println("🔥 authUser null 여부 확인 " + authUser);
		String userId = resolveUserId(authUser,request,response);
		System.out.println("🔥 guestId = " + userId);
		keyword = keyword.trim().toLowerCase();
		System.out.println("🔥 keyword = " + keyword);
		searchService.saveSearchLog(userId,keyword);
		return ResponseEntity.ok(searchService.search3(keyword, page, size).toPage());
	}

	@GetMapping("/topkeywords/db")
	public List<String> getTopKeywordsDB(){
		return searchService.getTopKeywordsWithDB();
	}

	@GetMapping("/topkeywords/localcache")
	public List<String> getTopKeywordsLocalCache(){
		return searchService.getTopKeywordsWithLocalCache();
	}

	@GetMapping("/topkeywords/redis")
	public List<String> getTopKeywordsRedis(){
		return searchService.getTopKeywordsWithRedis();
	}


	private String resolveUserId(AuthUser authUser, HttpServletRequest request, HttpServletResponse response){
		System.out.println("🔥 resolveUserId 메서드 진입 🔥");
		if(authUser != null){
			System.out.println("🔥 authUser가 있을 경우 if문 진입 확인 🔥");
			System.out.println("🔥 authUser null 여부 확인 " + authUser.getId());
			System.out.println("🔥 auth userId = " + String.valueOf(authUser.getId()));
			return String.valueOf(authUser.getId());
		}

		String guestId = null;
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			for(Cookie cookie : cookies){
				if("guestId".equals(cookie.getName())){
					guestId= cookie.getValue();
					break;
				}
			}
		}

		if(guestId==null||guestId.isBlank()){
			guestId = UUID.randomUUID().toString();
			ResponseCookie cookie = ResponseCookie.from("guestId",guestId)
				.httpOnly(true)
				.path("/")
				.maxAge(Duration.ofDays(30))
				.build();
			response.addHeader("Set-Cookie", cookie.toString());
		}
		return guestId;
	}
}
