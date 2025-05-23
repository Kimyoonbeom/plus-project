package com.example.plusproject.domain.socialmember.entity;

import com.example.plusproject.common.type.OAuth2Provider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OAuth2Provider provider; // KAKAO, NAVER

    @Column(nullable = false)
    private String providerId;

    @Column(nullable = false)
    private String nickname;

    @Builder
    public SocialMember(Long id, OAuth2Provider provider, String providerId, String nickname) {
        this.id = id;
        this.provider = provider;
        this.providerId = providerId;
        this.nickname = nickname;
    }
}
