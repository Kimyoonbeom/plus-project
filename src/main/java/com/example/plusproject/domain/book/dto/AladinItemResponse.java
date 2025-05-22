package com.example.plusproject.domain.book.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AladinItemResponse {
	private List<AladinItem> item;

	@Getter
	@NoArgsConstructor
	public static class AladinItem {
		private String title;
		private String author;
		private String publisher;
		private String description;

		@JsonProperty("pubDate")
		private LocalDate publishedAt;

		@JsonProperty("priceSales")
		private int price;

		@JsonProperty("cover")
		private String imageUrl;

		private int stockstatus;
	}
}
