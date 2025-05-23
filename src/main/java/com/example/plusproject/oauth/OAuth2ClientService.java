package com.example.plusproject.oauth;

import com.example.plusproject.common.type.OAuth2Provider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OAuth2ClientService {
    private final Map<OAuth2Provider, OAuth2Client> oAuth2Clients;

    public OAuth2ClientService(List<OAuth2Client> oAuth2Clients) {
        this.oAuth2Clients = oAuth2Clients.stream()
                .collect(Collectors.toMap(OAuth2Client::getProvider, Function.identity()));
    }

    public String generateLoginPageUrl(OAuth2Provider provider) {
        OAuth2Client oAuth2Client = this.create(provider);
        return oAuth2Client.generateLoginPageUrl();
    }

    public OAuth2UserInfo login(OAuth2Provider provider, String authorizationCode) {
        OAuth2Client oAuth2Client = this.create(provider);
        String accessToken = oAuth2Client.getAccessToken(authorizationCode);
        return oAuth2Client.retrieveUserInfo(accessToken);
    }

    // 자율성
    // 캡슐화
    // 관심사의 분리
    private OAuth2Client create(OAuth2Provider provider) {
        return Optional.ofNullable(oAuth2Clients.get(provider))
                .orElseThrow(RuntimeException::new);
    }
}
