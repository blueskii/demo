package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class BoardIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	void clearBoards() {
		jdbcTemplate.update("DELETE FROM board_post");
	}

	@Test
	void boardPageIsRendered() throws Exception {
		mockMvc.perform(get("/board"))
			.andExpect(status().isOk())
			.andExpect(view().name("board/list"))
			.andExpect(content().string(org.hamcrest.Matchers.containsString("자유 게시판")));
	}

	@Test
	void supportsCrudLifecycle() throws Exception {
		String createdBody = mockMvc.perform(post("/api/board")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{"title":"첫 인사","content":"반갑습니다.","author":"홍길동"}
					"""))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", org.hamcrest.Matchers.matchesPattern(".*/api/board/\\d+")))
			.andExpect(jsonPath("$.title").value("첫 인사"))
			.andReturn().getResponse().getContentAsString();

		Long id = Long.valueOf(createdBody.replaceAll(".*\"id\":(\\d+).*", "$1"));

		mockMvc.perform(get("/api/board"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(id));

		mockMvc.perform(get("/api/board/{id}", id))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content").value("반갑습니다."));

		mockMvc.perform(put("/api/board/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{"title":"수정된 인사","content":"모두 환영합니다.","author":"운영자"}
					"""))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("수정된 인사"));

		mockMvc.perform(delete("/api/board/{id}", id))
			.andExpect(status().isNoContent())
			.andExpect(content().string(""));

		assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM board_post", Integer.class)).isZero();
	}

	@Test
	void returnsExpectedClientErrors() throws Exception {
		mockMvc.perform(post("/api/board")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{"title":"","content":"","author":""}
					"""))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("must not be blank"));

		mockMvc.perform(get("/api/board/999999"))
			.andExpect(status().isNotFound());

		mockMvc.perform(put("/api/board/999999")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{"title":"수정","content":"내용","author":"작성자"}
					"""))
			.andExpect(status().isNotFound());

		mockMvc.perform(delete("/api/board/999999"))
			.andExpect(status().isNotFound());
	}
}
