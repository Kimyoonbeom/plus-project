package com.example.plusproject.oauth.client.kakao;

import com.example.plusproject.common.type.OAuth2Provider;
import com.example.plusproject.oauth.OAuth2Client;
import com.example.plusproject.oauth.OAuth2UserInfo;
import com.example.plusproject.oauth.client.kakao.dto.KakaoLoginUserInfoResponse;
import com.example.plusproject.oauth.client.kakao.dto.KakaoTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class KakaoOAuth2Client implements OAuth2Client {
    private final RestClient restClient;

    private final static String AUTH_SERVER_BASE_URL = "https://kauth.kakao.com";
    private final static String RESOURCE_SERVER_BASE_URL = "https://kapi.kakao.com";

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUrl;

    @Override
    public String generateLoginPageUrl() {
        return AUTH_SERVER_BASE_URL
                + "/oauth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUrl
                + "&response_type=" + "code";
    }

    @Override
    public String getAccessToken(String authorizationCode) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("code", authorizationCode);

        return restClient.post()
                .uri(AUTH_SERVER_BASE_URL + "/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, resp) -> {
                    throw new RuntimeException("카카오 AccessToken 조회 실패");
                })
                .body(KakaoTokenResponse.class)
                .getAccessToken();
    }

    @Override
    public OAuth2UserInfo retrieveUserInfo(String accessToken) {
        return restClient.get()
                .uri(RESOURCE_SERVER_BASE_URL + "/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, resp) -> {
                    throw new RuntimeException("카카오 UserInfo 조회 실패");
                })
                .body(KakaoLoginUserInfoResponse.class);
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.KAKAO;
    }
}
