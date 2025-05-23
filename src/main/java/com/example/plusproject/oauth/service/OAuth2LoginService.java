package com.example.plusproject.oauth.service;

import com.example.plusproject.common.JwtHelper;
import com.example.plusproject.common.type.OAuth2Provider;
import com.example.plusproject.domain.socialmember.entity.SocialMember;
import com.example.plusproject.oauth.OAuth2ClientService;
import com.example.plusproject.oauth.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2LoginService {
    private final OAuth2ClientService oAuth2ClientService;
    private final SocialMemberService socialMemberService;
    private final JwtHelper jwtHelper;

    public String generateLoginPageUrl(OAuth2Provider provider) {
        return oAuth2ClientService.generateLoginPageUrl(provider);
    }

    public String login(OAuth2Provider provider, String authorizationCode) {
        OAuth2UserInfo userInfo = oAuth2ClientService.login(provider, authorizationCode);
        SocialMember socialMember = socialMemberService.registerIfAbsent(userInfo);
        return jwtHelper.generateAccessToken(socialMember.getId());
    }
}
