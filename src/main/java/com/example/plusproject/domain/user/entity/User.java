package com.example.plusproject.domain.user.entity;

import com.example.plusproject.common.entity.BaseEntity;
import com.example.plusproject.domain.user.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "user")
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole userRole;

	@Column(nullable = false, unique = true)
	private String email;

	@Setter
	@Column(nullable = false)
	private String password;

	private String nickname;

	public User(UserRole userRole, String email, String password, String nickname) {
		this.userRole = userRole;
		this.email = email;
		this.password = password;
		this.nickname = nickname;
	}
}
