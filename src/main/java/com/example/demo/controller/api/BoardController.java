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

import com.example.demo.dto.BoardCreateRequest;
import com.example.demo.dto.BoardResponse;
import com.example.demo.dto.BoardUpdateRequest;
import com.example.demo.entity.BoardPost;
import com.example.demo.service.BoardService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/board")
public class BoardController {

	private final BoardService boardService;

	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}

	@PostMapping
	public ResponseEntity<BoardResponse> create(@Valid @RequestBody BoardCreateRequest request) {
		BoardPost created = boardService.create(toEntity(request.getTitle(), request.getContent(), request.getAuthor()));
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(created.getId())
			.toUri();
		return ResponseEntity.created(location).body(toResponse(created));
	}

	@GetMapping
	public List<BoardResponse> getAll() {
		return boardService.getAll().stream().map(this::toResponse).toList();
	}

	@GetMapping("/{id}")
	public BoardResponse getById(@PathVariable("id") Long id) {
		return toResponse(boardService.getById(id));
	}

	@PutMapping("/{id}")
	public BoardResponse update(@PathVariable("id") Long id,
			@Valid @RequestBody BoardUpdateRequest request) {
		return toResponse(boardService.update(id, toEntity(request.getTitle(), request.getContent(), request.getAuthor())));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		boardService.delete(id);
		return ResponseEntity.noContent().build();
	}

	private BoardPost toEntity(String title, String content, String author) {
		BoardPost boardPost = new BoardPost();
		boardPost.setTitle(title);
		boardPost.setContent(content);
		boardPost.setAuthor(author);
		return boardPost;
	}

	private BoardResponse toResponse(BoardPost boardPost) {
		BoardResponse response = new BoardResponse();
		response.setId(boardPost.getId());
		response.setTitle(boardPost.getTitle());
		response.setContent(boardPost.getContent());
		response.setAuthor(boardPost.getAuthor());
		response.setCreatedAt(boardPost.getCreatedAt());
		response.setUpdatedAt(boardPost.getUpdatedAt());
		return response;
	}
}
