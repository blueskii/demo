package com.example.demo.exception;

public class DuplicateNoticeTitleException extends RuntimeException {

	public DuplicateNoticeTitleException() {
		super("이미 사용 중인 공지사항 제목입니다.");
	}
}
