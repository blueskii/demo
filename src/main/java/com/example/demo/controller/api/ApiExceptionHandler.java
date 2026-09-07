package com.example.demo.controller.api;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.dto.ErrorResponse;
import com.example.demo.exception.BoardNotFoundException;
import com.example.demo.exception.DuplicateNoticeTitleException;
import com.example.demo.exception.NoticeNotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
	public ResponseEntity<ErrorResponse> handleBadRequest(Exception exception) {
		if (exception instanceof MethodArgumentNotValidException validationException) {
			if (validationException.getBindingResult().getFieldError() != null) {
				return response(HttpStatus.BAD_REQUEST,
					validationException.getBindingResult().getFieldError().getDefaultMessage());
			}
			return response(HttpStatus.BAD_REQUEST, "요청 값을 확인해 주세요.");
		}
		return response(HttpStatus.BAD_REQUEST, "요청 값을 확인해 주세요.");
	}

	@ExceptionHandler(NoticeNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(NoticeNotFoundException exception) {
		return response(HttpStatus.NOT_FOUND, exception.getMessage());
	}

	@ExceptionHandler(BoardNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleBoardNotFound(BoardNotFoundException exception) {
		return response(HttpStatus.NOT_FOUND, exception.getMessage());
	}

	@ExceptionHandler(DuplicateNoticeTitleException.class)
	public ResponseEntity<ErrorResponse> handleConflict(DuplicateNoticeTitleException exception) {
		return response(HttpStatus.CONFLICT, exception.getMessage());
	}

	private ResponseEntity<ErrorResponse> response(HttpStatus status, String message) {
		return ResponseEntity.status(status)
			.body(new ErrorResponse(status.value(), message, LocalDateTime.now()));
	}
}
