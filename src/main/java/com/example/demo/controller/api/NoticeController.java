package com.example.demo.controller.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.dto.NoticeCreateRequest;
import com.example.demo.dto.NoticeResponse;
import com.example.demo.dto.NoticeUpdateRequest;
import com.example.demo.entity.Notice;
import com.example.demo.service.NoticeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {

	private final NoticeService noticeService;

	public NoticeController(NoticeService noticeService) {
		this.noticeService = noticeService;
	}

	@PostMapping
	public ResponseEntity<NoticeResponse> create(@Valid @RequestBody NoticeCreateRequest request) {
		Notice created = noticeService.create(toEntity(request));
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(created.getId())
			.toUri();
		return ResponseEntity.created(location).body(toResponse(created));
	}

	@GetMapping
	public List<NoticeResponse> getAll() {
		return noticeService.getAll().stream().map(this::toResponse).toList();
	}

	@GetMapping("/{id}")
	public NoticeResponse getById(@PathVariable("id") Long id) {
		return toResponse(noticeService.getById(id));
	}

	@PutMapping("/{id}")
	public NoticeResponse update(@PathVariable("id") Long id,
			@Valid @RequestBody NoticeUpdateRequest request) {
		return toResponse(noticeService.update(id, toEntity(request)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		noticeService.delete(id);
		return ResponseEntity.noContent().build();
	}

	private Notice toEntity(NoticeCreateRequest request) {
		Notice notice = new Notice();
		notice.setTitle(request.getTitle());
		notice.setContent(request.getContent());
		notice.setAuthor(request.getAuthor());
		return notice;
	}

	private Notice toEntity(NoticeUpdateRequest request) {
		Notice notice = new Notice();
		notice.setTitle(request.getTitle());
		notice.setContent(request.getContent());
		notice.setAuthor(request.getAuthor());
		return notice;
	}

	private NoticeResponse toResponse(Notice notice) {
		NoticeResponse response = new NoticeResponse();
		response.setId(notice.getId());
		response.setTitle(notice.getTitle());
		response.setContent(notice.getContent());
		response.setAuthor(notice.getAuthor());
		response.setCreatedAt(notice.getCreatedAt());
		response.setUpdatedAt(notice.getUpdatedAt());
		return response;
	}
}
