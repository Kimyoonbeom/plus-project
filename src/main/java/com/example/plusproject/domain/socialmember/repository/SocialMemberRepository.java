package com.example.plusproject.domain.socialmember.repository;

import com.example.plusproject.common.type.OAuth2Provider;
import com.example.plusproject.domain.socialmember.entity.SocialMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialMemberRepository extends JpaRepository<SocialMember, Long> {

    Optional<SocialMember> findByProviderAndProviderId(OAuth2Provider provider, String id);
}
