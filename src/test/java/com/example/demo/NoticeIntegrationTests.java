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
class NoticeIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	void clearNotices() {
		jdbcTemplate.update("DELETE FROM notice");
	}

	@Test
	void noticePageIsRendered() throws Exception {
		mockMvc.perform(get("/notice"))
			.andExpect(status().isOk())
			.andExpect(view().name("notice/list"))
			.andExpect(content().string(org.hamcrest.Matchers.containsString("공지사항")));
	}

	@Test
	void supportsCrudLifecycle() throws Exception {
		String createdBody = mockMvc.perform(post("/api/notice")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{"title":"서비스 점검","content":"점검 안내입니다.","author":"관리자"}
					"""))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", org.hamcrest.Matchers.matchesPattern(".*/api/notice/\\d+")))
			.andExpect(jsonPath("$.title").value("서비스 점검"))
			.andReturn().getResponse().getContentAsString();

		Long id = Long.valueOf(createdBody.replaceAll(".*\"id\":(\\d+).*", "$1"));

		mockMvc.perform(get("/api/notice"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(id));

		mockMvc.perform(get("/api/notice/{id}", id))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content").value("점검 안내입니다."));

		mockMvc.perform(put("/api/notice/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{"title":"점검 완료","content":"정상 운영 중입니다.","author":"운영팀"}
					"""))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("점검 완료"));

		mockMvc.perform(delete("/api/notice/{id}", id))
			.andExpect(status().isNoContent())
			.andExpect(content().string(""));

		assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM notice", Integer.class)).isZero();
	}

	@Test
	void returnsExpectedClientErrors() throws Exception {
		mockMvc.perform(post("/api/notice")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{"title":"","content":"","author":""}
					"""))
			.andExpect(status().isBadRequest());

		mockMvc.perform(get("/api/notice/999999"))
			.andExpect(status().isNotFound());

		String body = """
			{"title":"중복 제목","content":"내용","author":"관리자"}
			""";
		mockMvc.perform(post("/api/notice").contentType(MediaType.APPLICATION_JSON).content(body))
			.andExpect(status().isCreated());
		mockMvc.perform(post("/api/notice").contentType(MediaType.APPLICATION_JSON).content(body))
			.andExpect(status().isConflict());
	}
}
