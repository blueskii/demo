package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BoardResponse {

	private Long id;
	private String title;
	private String content;
	private String author;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
