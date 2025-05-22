package com.example.plusproject.domain.book.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverBookResponse {

	private String lastBuildDate;
	private int total;
	private int start;
	private int display;

	@JsonProperty("items")
	private List<NaverItem> item;

	@Getter
	@NoArgsConstructor
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class NaverItem {
		private String title;
		private String link;
		private String image;
		private String author;
		private Integer discount;
		private String publisher;
		private String pubdate;
		private String isbn;
		private String description;
	}
}
