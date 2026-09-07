package com.example.demo.exception;

public class DuplicateBoardTitleException extends RuntimeException {

	public DuplicateBoardTitleException() {
		super("이미 사용 중인 게시글 제목입니다.");
	}
}
