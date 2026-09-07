package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NoticeCreateRequest {

	@NotBlank
	@Size(max = 100)
	private String title;

	@NotBlank
	@Size(max = 5000)
	private String content;

	@NotBlank
	@Size(max = 50)
	private String author;
}
