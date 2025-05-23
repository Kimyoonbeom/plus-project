package com.example.plusproject.oauth.service;

import com.example.plusproject.domain.socialmember.entity.SocialMember;
import com.example.plusproject.domain.socialmember.repository.SocialMemberRepository;
import com.example.plusproject.oauth.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialMemberService {

    private final SocialMemberRepository socialMemberRepository;

    public SocialMember registerIfAbsent(OAuth2UserInfo userInfo) {
        return socialMemberRepository.findByProviderAndProviderId(userInfo.getProvider(), userInfo.getId())
                .orElseGet(() -> {
                    SocialMember socialMember = SocialMember.builder()
                            .provider(userInfo.getProvider())
                            .providerId(userInfo.getId())
                            .nickname(userInfo.getNickname())
                            .build();
                    return socialMemberRepository.save(socialMember);
                });
    }
}
