package com.example.plusproject.oauth;

import com.example.plusproject.common.type.OAuth2Provider;

public interface OAuth2Client {
    String generateLoginPageUrl();

    String getAccessToken(String authorizationCode);

    OAuth2UserInfo retrieveUserInfo(String accessToken);

    OAuth2Provider getProvider();
}
