package com.example.plusproject.domain.search.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KeywordCount {
	private String keyword;
	private Long count;
}
