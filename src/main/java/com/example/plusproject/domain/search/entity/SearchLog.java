package com.example.plusproject.domain.search.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "search_log")
public class SearchLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String guestId;

	private String keyword;

	private LocalDateTime searchedAt;

	public SearchLog(String guestId, String keyword, LocalDateTime searchedAt) {
		this.guestId = guestId;
		this.keyword = keyword;
		this.searchedAt = searchedAt;
	}
}
