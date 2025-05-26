package com.example.plusproject.oauth;

import com.example.plusproject.common.type.OAuth2Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuth2UserInfo {
    private OAuth2Provider provider;
    private String id;
    private String nickname;
}
