package com.example.plusproject.oauth.client.kakao.dto;

import com.example.plusproject.common.type.OAuth2Provider;
import com.example.plusproject.oauth.OAuth2UserInfo;
import lombok.Getter;

@Getter
public class KakaoLoginUserInfoResponse extends OAuth2UserInfo {
    public KakaoLoginUserInfoResponse(Long id, KakaoUserPropertiesResponse properties) {
        super(OAuth2Provider.KAKAO, String.valueOf(id), properties.getNickname());
    }
}
