package com.example.plusproject.oauth.service;

import com.example.plusproject.common.JwtHelper;
import com.example.plusproject.domain.socialmember.entity.SocialMember;
import com.example.plusproject.oauth.OAuth2UserInfo;
import com.example.plusproject.oauth.client.kakao.KakaoOAuth2Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2LoginService {

    private final JwtHelper jwtHelper;

    private final KakaoOAuth2Client kakaoOAuth2Client;

    public String generateLoginPageUrl1(String provider) {
        if( provider.equals("kakao")){
            return kakaoOAuth2Client.generateLoginPageUrl();
        }else if ( provider.equals("naver")){
            //네이버 Client.url ㅇㅇ
        }
        return "실패"; // 🤔예외처리
    }

    public String login1(String provider, String authorizationCode) {
        String accessToken = null;
        OAuth2UserInfo userInfo = null;

        if( provider.equals("kakao")){
            accessToken = kakaoOAuth2Client.getAccessToken(authorizationCode);
            userInfo = kakaoOAuth2Client.retrieveUserInfo(accessToken);
        }else if ( provider.equals("naver")){
            //네이버 Client.url
        }

        if( userInfo == null){
            // 🤔예외처리
        }

        SocialMember socialMember = SocialMember.builder()
                .providerId(userInfo.getId())
                .nickname(userInfo.getNickname())
                .build();

        return jwtHelper.generateAccessToken(socialMember.getId(), socialMember.getEmail(), socialMember.getUserRole(), socialMember.getNickname());
    }

}
