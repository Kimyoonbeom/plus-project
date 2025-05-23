package com.example.plusproject.domain.socialmember.entity;

import com.example.plusproject.common.type.OAuth2Provider;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.enums.UserRole;
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

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private UserRole userRole;

    @Builder
    public SocialMember(Long id, OAuth2Provider provider, String providerId, String nickname, String email) {
        this.id = id;
        this.provider = provider;
        this.providerId = providerId;
        this.nickname = nickname;
        this.email = email;
        this.userRole = UserRole.USER;
    }

    @Builder
    public SocialMember( String providerId, String nickname) {
        this.provider = OAuth2Provider.KAKAO;
        this.providerId = providerId;
        this.nickname = nickname;
        this.email = "email@email.com";
        this.userRole = UserRole.USER;
    }

}
