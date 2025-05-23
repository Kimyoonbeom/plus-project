package com.example.plusproject.oauth.controller;

import com.example.plusproject.common.type.OAuth2Provider;
import com.example.plusproject.oauth.service.OAuth2LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class OAuth2LoginController {
    private final OAuth2LoginService oAuth2LoginService;

    @GetMapping("/oauth2/login/{provider}")
    public void redirectLoginPage(
            @PathVariable OAuth2Provider provider,
            HttpServletResponse response
    ) throws IOException {
        String loginPageUrl = oAuth2LoginService.generateLoginPageUrl(provider);
        response.sendRedirect(loginPageUrl);
    }

    @GetMapping("/oauth2/callback/{provider}")
    public ResponseEntity<String> oauth2Login(
            @PathVariable OAuth2Provider provider,
            @RequestParam("code") String authorizationCode
    ) {
        String accessToken = oAuth2LoginService.login(provider, authorizationCode);
        return ResponseEntity.ok(accessToken);
    }
}
