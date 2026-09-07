package com.example.demo.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Notice {

	private Long id;
	private String title;
	private String content;
	private String author;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
